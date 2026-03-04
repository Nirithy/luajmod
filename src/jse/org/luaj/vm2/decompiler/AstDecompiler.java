package org.luaj.vm2.decompiler;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;

public class AstDecompiler {
    public static abstract class AstNode {
        public abstract String format(int indent);

        protected String getIndent(int indent) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                sb.append("  ");
            }
            return sb.toString();
        }
    }

    public static class Block extends AstNode {
        public List<AstNode> statements = new ArrayList<>();

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            for (AstNode stmt : statements) {
                sb.append(stmt.format(indent)).append("\n");
            }
            return sb.toString();
        }
    }

    public static class Assignment extends AstNode {
        public String target;
        public String value;

        public Assignment(String target, String value) {
            this.target = target;
            this.value = value;
        }

        @Override
        public String format(int indent) {
            return getIndent(indent) + target + " = " + value;
        }
    }

    public static class Call extends AstNode {
        public String function;
        public List<String> args;
        public String returnTarget;

        public Call(String function, List<String> args, String returnTarget) {
            this.function = function;
            this.args = args;
            this.returnTarget = returnTarget;
        }

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIndent(indent));
            if (returnTarget != null && !returnTarget.isEmpty()) {
                sb.append(returnTarget).append(" = ");
            }
            sb.append(function).append("(");
            for (int i = 0; i < args.size(); i++) {
                sb.append(args.get(i));
                if (i < args.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }

    public static class IfStmt extends AstNode {
        public String condition;
        public Block thenBlock;
        public Block elseBlock;

        public IfStmt(String condition, Block thenBlock, Block elseBlock) {
            this.condition = condition;
            this.thenBlock = thenBlock;
            this.elseBlock = elseBlock;
        }

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIndent(indent)).append("if ").append(condition).append(" then\n");
            if (thenBlock != null) {
                sb.append(thenBlock.format(indent + 1));
            }
            if (elseBlock != null && !elseBlock.statements.isEmpty()) {
                sb.append(getIndent(indent)).append("else\n");
                sb.append(elseBlock.format(indent + 1));
            }
            sb.append(getIndent(indent)).append("end");
            return sb.toString();
        }
    }

    public static class FunctionDef extends AstNode {
        public String name;
        public List<String> params;
        public Block body;

        public FunctionDef(String name, List<String> params, Block body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIndent(indent)).append("function ");
            if (name != null && !name.isEmpty()) {
                sb.append(name);
            }
            sb.append("(");
            for (int i = 0; i < params.size(); i++) {
                sb.append(params.get(i));
                if (i < params.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")\n");
            if (body != null) {
                sb.append(body.format(indent + 1));
            }
            sb.append(getIndent(indent)).append("end");
            return sb.toString();
        }
    }

    public static class WhileStmt extends AstNode {
        public String condition;
        public Block body;

        public WhileStmt(String condition, Block body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIndent(indent)).append("while ").append(condition).append(" do\n");
            if (body != null) {
                sb.append(body.format(indent + 1));
            }
            sb.append(getIndent(indent)).append("end");
            return sb.toString();
        }
    }

    public static class ForStmt extends AstNode {
        public String varName;
        public String start;
        public String end;
        public String step;
        public Block body;

        public ForStmt(String varName, String start, String end, String step, Block body) {
            this.varName = varName;
            this.start = start;
            this.end = end;
            this.step = step;
            this.body = body;
        }

        @Override
        public String format(int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(getIndent(indent)).append("for ").append(varName).append(" = ").append(start).append(", ").append(end);
            if (step != null && !step.equals("1")) {
                sb.append(", ").append(step);
            }
            sb.append(" do\n");
            if (body != null) {
                sb.append(body.format(indent + 1));
            }
            sb.append(getIndent(indent)).append("end");
            return sb.toString();
        }
    }

    public static class RawLuaStmt extends AstNode {
        public String code;
        public RawLuaStmt(String code) {
            this.code = code;
        }
        @Override
        public String format(int indent) {
            return getIndent(indent) + code;
        }
    }

    public static class Label extends AstNode {
        public String name;
        public Label(String name) {
            this.name = name;
        }
        @Override
        public String format(int indent) {
            return getIndent(indent) + "::" + name + "::";
        }
    }

    public static class Goto extends AstNode {
        public String name;
        public Goto(String name) {
            this.name = name;
        }
        @Override
        public String format(int indent) {
            return getIndent(indent) + "goto " + name;
        }
    }

    private static String getK(LuaValue[] k, int index) {
        if (k != null && index >= 0 && index < k.length) {
            return formatConstant(k[index]);
        }
        return "\"<missing_k_" + index + ">\"";
    }

    // True recursive AST decompilation function
    public static Block parseCFG(Prototype p) {
        Block rootBlock = new Block();
        parsePrototypeRecursive(p, rootBlock, "main");
        return rootBlock;
    }

    private static void parsePrototypeRecursive(Prototype p, Block rootBlock, String name) {
        if (p == null || p.code == null) {
            return;
        }

        int[] code = p.code;
        LuaValue[] k = p.k;

        String[] vars = new String[p.maxstacksize > 0 ? p.maxstacksize : 256];
        for (int j = 0; j < vars.length; j++) {
            vars[j] = "v" + j;
        }

        if (!name.equals("main")) {
            List<String> params = new ArrayList<>();
            for (int i = 0; i < p.numparams; i++) {
                params.add("v" + i);
            }
            if (p.is_vararg > 0) {
                params.add("...");
            }

            Block body = new Block();
            FunctionDef funcDef = new FunctionDef(name, params, body);
            rootBlock.statements.add(funcDef);
            rootBlock = body; // Now append inside the function body
        }

        List<CFGBuilder.BasicBlock> blocks = CFGBuilder.buildCFG(p);

        for (CFGBuilder.BasicBlock block : blocks) {
            rootBlock.statements.add(new RawLuaStmt("-- Basic Block " + block.id + " (PC " + block.startPc + " to " + block.endPc + ")"));
            rootBlock.statements.add(new Label("BLOCK_" + block.id));

            for (int pc = block.startPc; pc <= block.endPc; pc++) {
                int i = code[pc];
                int opcode = Lua.GET_OPCODE(i);
                int a = Lua.GETARG_A(i);
                int b = Lua.GETARG_B(i);
                int c = Lua.GETARG_C(i);
                int bx = Lua.GETARG_Bx(i);
                int sbx = Lua.GETARG_sBx(i);

            try {
                switch (opcode) {
                    case Lua.OP_MOVE:
                        vars[a] = vars[b];
                        rootBlock.statements.add(new Assignment("v" + a, vars[b]));
                        break;
                    case Lua.OP_LOADK:
                        vars[a] = getK(k, bx);
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_LOADBOOL:
                        vars[a] = b != 0 ? "true" : "false";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        if (c != 0) rootBlock.statements.add(new RawLuaStmt("-- skip next"));
                        break;
                    case Lua.OP_LOADNIL:
                        for (int j = a; j <= a + b; j++) vars[j] = "nil";
                        rootBlock.statements.add(new RawLuaStmt("for i=" + a + ", " + (a + b) + " do v[i] = nil end"));
                        break;
                    case Lua.OP_GETUPVAL:
                        vars[a] = "upvalue_" + b;
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_GETTABUP:
                        String keyStr = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        if (b == 0) {
                            vars[a] = keyStr; // Global access
                        } else {
                            vars[a] = keyStr.startsWith("\"") ? "upvalue_" + b + "." + keyStr.replace("\"", "") : "upvalue_" + b + "[" + keyStr + "]";
                        }
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_GETTABLE:
                        String keyStr2 = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        vars[a] = keyStr2.startsWith("\"") ? vars[b] + "." + keyStr2.replace("\"", "") : vars[b] + "[" + keyStr2 + "]";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_SETTABUP:
                        String keyStr3 = b > 0xff ? getK(k, b & 0xff) : vars[b];
                        String valStr = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        if (a == 0) {
                            rootBlock.statements.add(new Assignment(keyStr3, valStr));
                        } else {
                            String target = keyStr3.startsWith("\"") ? "upvalue_" + a + "." + keyStr3.replace("\"", "") : "upvalue_" + a + "[" + keyStr3 + "]";
                            rootBlock.statements.add(new Assignment(target, valStr));
                        }
                        break;
                    case Lua.OP_SETUPVAL:
                        rootBlock.statements.add(new Assignment("upvalue_" + b, "v" + a));
                        break;
                    case Lua.OP_SETTABLE:
                        String keyStr4 = b > 0xff ? getK(k, b & 0xff) : vars[b];
                        String valStr2 = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        String target2 = keyStr4.startsWith("\"") ? "v" + a + "." + keyStr4.replace("\"", "") : "v" + a + "[" + keyStr4 + "]";
                        rootBlock.statements.add(new Assignment(target2, valStr2));
                        break;
                    case Lua.OP_NEWTABLE:
                        vars[a] = "{}";
                        rootBlock.statements.add(new Assignment("v" + a, "{}"));
                        break;
                    case Lua.OP_SELF:
                        String keyStr5 = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        vars[a+1] = vars[b];
                        vars[a] = vars[b] + "[" + keyStr5 + "]";
                        rootBlock.statements.add(new Assignment("v" + (a+1), vars[b]));
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_ADD:
                    case Lua.OP_SUB:
                    case Lua.OP_MUL:
                    case Lua.OP_DIV:
                    case Lua.OP_MOD:
                    case Lua.OP_POW:
                        String op = opcode == Lua.OP_ADD ? "+" : opcode == Lua.OP_SUB ? "-" : opcode == Lua.OP_MUL ? "*" : opcode == Lua.OP_DIV ? "/" : opcode == Lua.OP_MOD ? "%" : "^";
                        String left = b > 0xff ? getK(k, b & 0xff) : vars[b];
                        String right = c > 0xff ? getK(k, c & 0xff) : vars[c];
                        vars[a] = "(" + left + " " + op + " " + right + ")";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_UNM:
                        vars[a] = "(-v" + b + ")";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_NOT:
                        vars[a] = "(not v" + b + ")";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_LEN:
                        vars[a] = "(#v" + b + ")";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_CONCAT:
                        StringBuilder concatSb = new StringBuilder();
                        for (int j = b; j <= c; j++) {
                            if (j > b) concatSb.append(" .. ");
                            concatSb.append(vars[j]);
                        }
                        vars[a] = "(" + concatSb.toString() + ")";
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_JMP:
                        int targetBlockJmp = getTargetBlockId(blocks, pc + 1 + sbx);
                        rootBlock.statements.add(new Goto("BLOCK_" + targetBlockJmp));
                        break;
                    case Lua.OP_EQ:
                    case Lua.OP_LT:
                    case Lua.OP_LE:
                        String cmp = opcode == Lua.OP_EQ ? "==" : opcode == Lua.OP_LT ? "<" : "<=";
                        if (a == 0) cmp = opcode == Lua.OP_EQ ? "~=" : opcode == Lua.OP_LT ? ">=" : ">";
                        String leftCmp = b > 0xff ? getK(k, b & 0xff) : vars[b];
                        String rightCmp = c > 0xff ? getK(k, c & 0xff) : vars[c];

                        int thenTarget = getTargetBlockId(blocks, pc + 2);
                        int elseTarget = getTargetBlockId(blocks, pc + 1); // skip JMP
                        Block thenBlock = new Block();
                        thenBlock.statements.add(new Goto("BLOCK_" + thenTarget));
                        Block elseBlock = new Block();
                        elseBlock.statements.add(new Goto("BLOCK_" + elseTarget));
                        rootBlock.statements.add(new IfStmt("(" + leftCmp + " " + cmp + " " + rightCmp + ")", thenBlock, elseBlock));
                        break;
                    case Lua.OP_TEST:
                        int tTarget = getTargetBlockId(blocks, pc + 2);
                        int eTarget = getTargetBlockId(blocks, pc + 1);
                        Block testBlock = new Block();
                        testBlock.statements.add(new Goto("BLOCK_" + tTarget));
                        Block testElseBlock = new Block();
                        testElseBlock.statements.add(new Goto("BLOCK_" + eTarget));
                        if (c == 0) rootBlock.statements.add(new IfStmt("(v" + a + ")", testBlock, testElseBlock));
                        else rootBlock.statements.add(new IfStmt("(not v" + a + ")", testBlock, testElseBlock));
                        break;
                    case Lua.OP_TESTSET:
                        int tsetTarget = getTargetBlockId(blocks, pc + 2);
                        int esetTarget = getTargetBlockId(blocks, pc + 1);
                        Block testsetBlock = new Block();
                        testsetBlock.statements.add(new Assignment("v" + a, "v" + b));
                        testsetBlock.statements.add(new Goto("BLOCK_" + tsetTarget));
                        Block testsetElseBlock = new Block();
                        testsetElseBlock.statements.add(new Goto("BLOCK_" + esetTarget));
                        if (c == 0) {
                            rootBlock.statements.add(new IfStmt("(v" + b + ")", testsetBlock, testsetElseBlock));
                        } else {
                            rootBlock.statements.add(new IfStmt("(not v" + b + ")", testsetBlock, testsetElseBlock));
                        }
                        break;
                    case Lua.OP_CALL:
                        List<String> callArgs = new ArrayList<>();
                        if (b == 0) {
                            callArgs.add("table.unpack(v" + (a + 1) + ")");
                        } else {
                            for (int j = 1; j < b; j++) {
                                String argVal = vars[a + j];
                                callArgs.add(argVal != null ? argVal : "v" + (a + j));
                            }
                        }

                        String funcName = vars[a];
                        if (funcName == null) funcName = "v" + a;

                        if (c == 0) {
                            rootBlock.statements.add(new Call(funcName, callArgs, "v" + a + "_varargs"));
                        } else if (c == 1) {
                            rootBlock.statements.add(new Call(funcName, callArgs, ""));
                        } else {
                            StringBuilder rets = new StringBuilder();
                            for (int j = 0; j < c - 1; j++) {
                                if (j > 0) rets.append(", ");
                                rets.append("v" + (a + j));
                                vars[a + j] = "ret_" + j + "_of_" + funcName.replaceAll("[^a-zA-Z0-9_]", "_");
                            }
                            rootBlock.statements.add(new Call(funcName, callArgs, rets.toString()));
                        }
                        break;
                    case Lua.OP_TAILCALL:
                        List<String> tailArgs = new ArrayList<>();
                        if (b == 0) {
                            tailArgs.add("table.unpack(v" + (a + 1) + ")");
                        } else {
                            for (int j = 1; j < b; j++) {
                                String argVal = vars[a + j];
                                tailArgs.add(argVal != null ? argVal : "v" + (a + j));
                            }
                        }
                        String tailFuncName = vars[a];
                        if (tailFuncName == null) tailFuncName = "v" + a;
                        Call tailCall = new Call(tailFuncName, tailArgs, "");
                        rootBlock.statements.add(new RawLuaStmt("return " + tailCall.format(0).trim()));
                        break;
                    case Lua.OP_RETURN:
                        if (b == 0) {
                            rootBlock.statements.add(new RawLuaStmt("return table.unpack(v" + a + "_varargs)"));
                        } else if (b == 1) {
                            rootBlock.statements.add(new RawLuaStmt("return"));
                        } else {
                            StringBuilder rets2 = new StringBuilder();
                            for (int j = 0; j < b - 1; j++) {
                                if (j > 0) rets2.append(", ");
                                rets2.append(vars[a + j]);
                            }
                            rootBlock.statements.add(new RawLuaStmt("return " + rets2.toString()));
                        }
                        break;
                    case Lua.OP_FORLOOP:
                        int forloopTarget = getTargetBlockId(blocks, pc + 1 + sbx);
                        Block forLoopBody = new Block();
                        forLoopBody.statements.add(new RawLuaStmt("-- loop body (jump to BLOCK_" + forloopTarget + ")"));
                        rootBlock.statements.add(new ForStmt("v" + a, "v" + a + "_start", "v" + (a+1), "v" + (a+2), forLoopBody));
                        break;
                    case Lua.OP_FORPREP:
                        int forprepTarget = getTargetBlockId(blocks, pc + 1 + sbx);
                        rootBlock.statements.add(new RawLuaStmt("-- for loop prepare (jumps to BLOCK_" + forprepTarget + ")"));
                        break;
                    case Lua.OP_TFORCALL:
                        rootBlock.statements.add(new RawLuaStmt("v" + (a+3) + ", dummy = v" + a + "(v" + (a+1) + ", v" + (a+2) + ")"));
                        break;
                    case Lua.OP_TFORLOOP:
                        int tforTarget = getTargetBlockId(blocks, pc + 1 + sbx);
                        Block tforBlock = new Block();
                        tforBlock.statements.add(new Assignment("v" + a, "v" + (a+1)));
                        tforBlock.statements.add(new RawLuaStmt("-- tfor loop body (jump to BLOCK_" + tforTarget + ")"));
                        rootBlock.statements.add(new WhileStmt("v" + (a+1) + " ~= nil", tforBlock));
                        break;
                    case Lua.OP_SETLIST:
                        rootBlock.statements.add(new RawLuaStmt("-- SETLIST omitted for runnable pseudo code"));
                        break;
                    case Lua.OP_CLOSURE:
                        vars[a] = "closure_" + bx;
                        rootBlock.statements.add(new Assignment("v" + a, vars[a]));
                        break;
                    case Lua.OP_VARARG:
                        vars[a] = "vararg";
                        rootBlock.statements.add(new Assignment("v" + a, "..."));
                        break;
                    default:
                        rootBlock.statements.add(new RawLuaStmt("-- OP_" + opcode + " a=" + a + " b=" + b + " c=" + c + " bx=" + bx));
                        break;
                }
            } catch (Exception e) {
                rootBlock.statements.add(new RawLuaStmt("-- error decompiling: " + e.getMessage()));
            }
            }
        }

        // Recursively generate child prototypes
        if (p.p != null) {
            for (int i = 0; i < p.p.length; i++) {
                parsePrototypeRecursive(p.p[i], rootBlock, "closure_" + i);
            }
        }
    }

    private static int getTargetBlockId(List<CFGBuilder.BasicBlock> blocks, int pc) {
        for (CFGBuilder.BasicBlock b : blocks) {
            if (pc >= b.startPc && pc <= b.endPc) {
                return b.id;
            }
        }
        return -1;
    }

    private static String formatConstant(LuaValue v) {
        if (v == null) return "nil";
        if (v.type() == LuaValue.TSTRING) {
            String str = v.tojstring().replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\"");
            return "\"" + str + "\"";
        }
        return v.tojstring();
    }
}
