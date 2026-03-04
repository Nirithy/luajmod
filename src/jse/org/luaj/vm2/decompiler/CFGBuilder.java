package org.luaj.vm2.decompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.luaj.vm2.Lua;
import org.luaj.vm2.Prototype;

public class CFGBuilder {

    public static class BasicBlock {
        public int id;
        public int startPc;
        public int endPc;
        public List<BasicBlock> inEdges = new ArrayList<>();
        public List<BasicBlock> outEdges = new ArrayList<>();

        public BasicBlock(int id, int startPc, int endPc) {
            this.id = id;
            this.startPc = startPc;
            this.endPc = endPc;
        }
    }

    public static List<BasicBlock> buildCFG(Prototype p) {
        if (p == null || p.code == null) {
            return new ArrayList<>();
        }

        int[] code = p.code;
        int len = code.length;

        Set<Integer> leaderPcs = new HashSet<>();
        leaderPcs.add(0); // The first instruction is always a leader

        // Find all leaders
        for (int pc = 0; pc < len; pc++) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);

            switch (opcode) {
                case Lua.OP_JMP:
                case Lua.OP_FORLOOP:
                case Lua.OP_FORPREP:
                case Lua.OP_TFORLOOP:
                    int sbx = Lua.GETARG_sBx(i);
                    int targetPc = pc + 1 + sbx;
                    if (targetPc < len) {
                        leaderPcs.add(targetPc);
                    }
                    if (pc + 1 < len) {
                        leaderPcs.add(pc + 1);
                    }
                    break;
                case Lua.OP_EQ:
                case Lua.OP_LT:
                case Lua.OP_LE:
                case Lua.OP_TEST:
                case Lua.OP_TESTSET:
                    // These are conditional jumps. The next instruction is ALWAYS a JMP in Lua 5.2/5.3
                    if (pc + 1 < len) {
                        leaderPcs.add(pc + 1);
                    }
                    if (pc + 2 < len) {
                        leaderPcs.add(pc + 2);
                    }
                    break;
                case Lua.OP_RETURN:
                case Lua.OP_TAILCALL:
                    if (pc + 1 < len) {
                        leaderPcs.add(pc + 1);
                    }
                    break;
            }
        }

        List<Integer> sortedLeaders = new ArrayList<>(leaderPcs);
        Collections.sort(sortedLeaders);

        List<BasicBlock> blocks = new ArrayList<>();
        for (int i = 0; i < sortedLeaders.size(); i++) {
            int start = sortedLeaders.get(i);
            int end = (i + 1 < sortedLeaders.size()) ? sortedLeaders.get(i + 1) - 1 : len - 1;
            blocks.add(new BasicBlock(i, start, end));
        }

        // Add edges
        for (BasicBlock block : blocks) {
            int lastPc = block.endPc;
            int inst = code[lastPc];
            int opcode = Lua.GET_OPCODE(inst);

            boolean fallsThrough = true;

            switch (opcode) {
                case Lua.OP_JMP:
                case Lua.OP_FORLOOP:
                case Lua.OP_FORPREP:
                case Lua.OP_TFORLOOP:
                    int sbx = Lua.GETARG_sBx(inst);
                    int targetPc = lastPc + 1 + sbx;
                    BasicBlock targetBlock = getBlockByPc(blocks, targetPc);
                    if (targetBlock != null) {
                        block.outEdges.add(targetBlock);
                        targetBlock.inEdges.add(block);
                    }
                    if (opcode == Lua.OP_JMP) {
                        fallsThrough = false; // unconditional jump
                    }
                    break;
                case Lua.OP_EQ:
                case Lua.OP_LT:
                case Lua.OP_LE:
                case Lua.OP_TEST:
                case Lua.OP_TESTSET:
                    // Conditionals skip the next instruction if true, so target is pc + 2
                    BasicBlock skipTargetBlock = getBlockByPc(blocks, lastPc + 2);
                    if (skipTargetBlock != null) {
                        block.outEdges.add(skipTargetBlock);
                        skipTargetBlock.inEdges.add(block);
                    }
                    break;
                case Lua.OP_RETURN:
                case Lua.OP_TAILCALL:
                    fallsThrough = false;
                    break;
            }

            if (fallsThrough && lastPc + 1 < len) {
                BasicBlock nextBlock = getBlockByPc(blocks, lastPc + 1);
                if (nextBlock != null) {
                    block.outEdges.add(nextBlock);
                    nextBlock.inEdges.add(block);
                }
            }
        }

        return blocks;
    }

    public static void printFlowchart(Prototype p) {
        List<BasicBlock> blocks = buildCFG(p);
        if (blocks.isEmpty()) {
            System.out.println("  [Empty Function or No Code]");
            return;
        }

        System.out.println("-------------------------------------------------");
        for (BasicBlock b : blocks) {
            System.out.println("  +-------------------------+");
            System.out.printf("  | Block %-3d               |\n", b.id);
            System.out.printf("  | PC: %-4d to %-4d      |\n", b.startPc, b.endPc);
            System.out.println("  +-------------------------+");

            if (!b.outEdges.isEmpty()) {
                System.out.print("           | \n");
                System.out.print("           v \n");
                System.out.print("   Edges: ");
                for (int i = 0; i < b.outEdges.size(); i++) {
                    System.out.print("B" + b.outEdges.get(i).id);
                    if (i < b.outEdges.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("\n");
            }
        }
        System.out.println("-------------------------------------------------");
    }

    private static BasicBlock getBlockByPc(List<BasicBlock> blocks, int pc) {
        for (BasicBlock b : blocks) {
            if (pc >= b.startPc && pc <= b.endPc) {
                return b;
            }
        }
        return null;
    }

    /**
     * Identifies junk assignment instructions and replaces them with OP_MOVE A A (NOP).
     * Junk instructions are those that load a value into a register, but that register
     * is subsequently overwritten or goes out of scope before being read by any instruction 
     * that has meaningful side effects (like CALL, SETTABLE, RETURN, etc).
     */
    public static void cleanJunk(Prototype p) {
        if (p == null || p.code == null) {
            return;
        }

        int[] code = p.code;
        int len = code.length;

        // Perform a very basic backward def-use analysis per register.
        // If a register is DEFINED but never USED before its next DEF or end of block, it's junk.
        // We will do a conservative blockless analysis:
        // A register is "used" if it is read.
        // A register is "defined" if it is written to.
        // We will traverse backwards.
        
        boolean[] used = new boolean[p.maxstacksize > 0 ? p.maxstacksize : 256];
        
        int cleanedCount = 0;

        for (int pc = len - 1; pc >= 0; pc--) {
            int i = code[pc];
            if (i == 0) continue; // Already NOP
            int opcode = Lua.GET_OPCODE(i);
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            
            // Check if this instruction is a pure definition without side effects.
            boolean isPureDef = false;
            int defReg = -1;
            
            switch (opcode) {
                case Lua.OP_LOADK:
                case Lua.OP_LOADBOOL:
                case Lua.OP_GETUPVAL:
                case Lua.OP_GETTABUP:
                case Lua.OP_GETTABLE:
                case Lua.OP_NEWTABLE:
                case Lua.OP_UNM:
                case Lua.OP_NOT:
                case Lua.OP_LEN:
                case Lua.OP_ADD:
                case Lua.OP_SUB:
                case Lua.OP_MUL:
                case Lua.OP_DIV:
                case Lua.OP_MOD:
                case Lua.OP_POW:
                case Lua.OP_CONCAT:
                    isPureDef = true;
                    defReg = a;
                    break;
                case Lua.OP_MOVE:
                    isPureDef = true;
                    defReg = a;
                    // Note: OP_MOVE also uses B, which we handle below.
                    break;
                case Lua.OP_LOADNIL:
                    // Loads a range, we'll skip complex optimization for LOADNIL for now
                    break;
            }

            if (isPureDef && !used[defReg]) {
                // The register is defined here, but moving backwards, it was never used!
                // This means the definition is dead code.
                // Replace with OP_MOVE A, A (effectively a NOP)
                // OP_MOVE is opcode 0
                int nop = (0 << 0) | (a << 6) | (a << 23); // A B format, opcode 0
                code[pc] = nop;
                cleanedCount++;
                continue; // Do not process 'uses' for a dead instruction
            }
            
            // If it was not dead code, we mark its definition as "no longer used"
            // (since we are moving backward, the definition satisfies the future uses)
            if (isPureDef) {
                used[defReg] = false;
            }

            // Now, mark the registers that this instruction USES.
            switch (opcode) {
                case Lua.OP_MOVE:
                case Lua.OP_UNM:
                case Lua.OP_NOT:
                case Lua.OP_LEN:
                    used[b] = true;
                    break;
                case Lua.OP_ADD:
                case Lua.OP_SUB:
                case Lua.OP_MUL:
                case Lua.OP_DIV:
                case Lua.OP_MOD:
                case Lua.OP_POW:
                case Lua.OP_EQ:
                case Lua.OP_LT:
                case Lua.OP_LE:
                    // B and C could be constants (if > 0xff), but if they are registers, mark them
                    if (b <= 0xff) used[b] = true;
                    if (c <= 0xff) used[c] = true;
                    break;
                case Lua.OP_GETTABUP:
                    if (c <= 0xff) used[c] = true;
                    break;
                case Lua.OP_GETTABLE:
                    used[b] = true;
                    if (c <= 0xff) used[c] = true;
                    break;
                case Lua.OP_SETTABUP:
                    if (b <= 0xff) used[b] = true;
                    if (c <= 0xff) used[c] = true;
                    break;
                case Lua.OP_SETUPVAL:
                    used[a] = true;
                    break;
                case Lua.OP_SETTABLE:
                    used[a] = true;
                    if (b <= 0xff) used[b] = true;
                    if (c <= 0xff) used[c] = true;
                    break;
                case Lua.OP_SELF:
                    used[b] = true;
                    if (c <= 0xff) used[c] = true;
                    used[a] = false; // defines A and A+1
                    used[a+1] = false;
                    break;
                case Lua.OP_CONCAT:
                    for (int j = b; j <= c; j++) used[j] = true;
                    break;
                case Lua.OP_TEST:
                    used[a] = true;
                    break;
                case Lua.OP_TESTSET:
                    used[b] = true;
                    used[a] = false; // defines A
                    break;
                case Lua.OP_CALL:
                case Lua.OP_TAILCALL:
                    used[a] = true;
                    if (b > 1) {
                        for (int j = 1; j < b; j++) used[a + j] = true;
                    }
                    // Since call can define multiple values, and we are doing a conservative backward pass,
                    // we assume call results are used if we don't know C
                    if (opcode == Lua.OP_CALL) {
                        if (c > 1) {
                            for (int j = 0; j < c - 1; j++) used[a + j] = false;
                        } else if (c == 0) {
                             // Top set, conservative
                        }
                    }
                    break;
                case Lua.OP_RETURN:
                    if (b > 1) {
                        for (int j = 0; j < b - 1; j++) used[a + j] = true;
                    }
                    break;
                case Lua.OP_FORLOOP:
                    used[a] = true;
                    used[a+1] = true;
                    used[a+2] = true;
                    used[a+3] = false; // defined
                    break;
                case Lua.OP_FORPREP:
                    used[a] = true;
                    used[a+1] = true;
                    used[a+2] = true;
                    break;
                case Lua.OP_TFORCALL:
                    used[a] = true;
                    used[a+1] = true;
                    used[a+2] = true;
                    // defines a+3 ... a+2+c
                    for (int j = 3; j <= 2 + c; j++) used[a + j] = false;
                    break;
                case Lua.OP_TFORLOOP:
                    used[a+1] = true;
                    used[a] = false;
                    break;
                case Lua.OP_SETLIST:
                    used[a] = true;
                    if (b > 0) {
                        for (int j = 1; j <= b; j++) used[a + j] = true;
                    }
                    break;
                case Lua.OP_CLOSURE:
                    // Defines A, uses upvalues which are pseudo-instructions following it
                    used[a] = false;
                    break;
                case Lua.OP_VARARG:
                    if (b > 1) {
                        for (int j = 0; j < b - 1; j++) used[a + j] = false;
                    }
                    break;
                // Add jump branching logic clearing if needed for conservative blocks
                case Lua.OP_JMP:
                    // In a rigorous CFG we would merge liveness from all successors.
                    // For this linear pass, any backward jump means we might loop,
                    // so we should conservatively mark all registers used in loops
                    // However, we just do a linear pass for dead code between jumps.
                    break;
            }
        }
        
        System.out.println("Cleaned " + cleanedCount + " junk instructions.");
    }
}
