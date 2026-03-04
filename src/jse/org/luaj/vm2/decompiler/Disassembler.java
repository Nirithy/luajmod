package org.luaj.vm2.decompiler;

import org.luaj.vm2.Lua;
import org.luaj.vm2.Print;
import org.luaj.vm2.Prototype;

public class Disassembler {

    public static void disassemble(Prototype p) {
        if (p == null || p.code == null) {
            System.out.println("No instructions found in prototype.");
            return;
        }

        System.out.println("; Disassembly of Prototype");
        System.out.println("; Instructions: " + p.code.length);

        int[] code = p.code;
        for (int pc = 0; pc < code.length; pc++) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            
            String name = (opcode >= 0 && opcode < Print.OPNAMES.length) 
                          ? Print.OPNAMES[opcode] 
                          : "OP_" + opcode;

            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);

            System.out.printf("[%04d] %-10s ", pc, name);

            // Based on opcode format, print parameters. We do a rough guess here
            // based on the instruction type but we can just print all non-zero args
            // For a better decompiler we would switch on the opcode.
            switch (opcode) {
                // iABC format instructions
                case Lua.OP_MOVE:
                case Lua.OP_UNM:
                case Lua.OP_NOT:
                case Lua.OP_LEN:
                case Lua.OP_RETURN:
                case Lua.OP_GETUPVAL:
                case Lua.OP_SETUPVAL:
                    System.out.printf("%d %d", a, b);
                    break;
                case Lua.OP_LOADBOOL:
                case Lua.OP_GETTABUP:
                case Lua.OP_GETTABLE:
                case Lua.OP_SETTABUP:
                case Lua.OP_SETTABLE:
                case Lua.OP_NEWTABLE:
                case Lua.OP_SELF:
                case Lua.OP_ADD:
                case Lua.OP_SUB:
                case Lua.OP_MUL:
                case Lua.OP_DIV:
                case Lua.OP_MOD:
                case Lua.OP_POW:
                case Lua.OP_CONCAT:
                case Lua.OP_EQ:
                case Lua.OP_LT:
                case Lua.OP_LE:
                case Lua.OP_TESTSET:
                case Lua.OP_CALL:
                case Lua.OP_TAILCALL:
                case Lua.OP_SETLIST:
                    System.out.printf("%d %d %d", a, b, c);
                    break;
                // iABx format instructions
                case Lua.OP_LOADK:
                case Lua.OP_CLOSURE:
                    System.out.printf("%d %d", a, bx);
                    break;
                // iAsBx format instructions
                case Lua.OP_JMP:
                case Lua.OP_FORLOOP:
                case Lua.OP_FORPREP:
                case Lua.OP_TFORLOOP:
                    System.out.printf("%d %d", a, sbx);
                    break;
                // other forms
                case Lua.OP_LOADKX:
                    System.out.printf("%d", a);
                    break;
                case Lua.OP_TEST:
                    System.out.printf("%d %d", a, c);
                    break;
                default:
                    System.out.printf("%d %d %d", a, b, c); // fallback
                    break;
            }
            System.out.println();
        }
    }
}
