package org.luaj.vm2.decompiler;

import java.util.List;

import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;

public class PseudoCompiler {

    public static String decompile(Prototype p) {
        if (p == null || p.code == null) return "-- No instructions";

        List<CFGBuilder.BasicBlock> blocks = CFGBuilder.buildCFG(p);

        StringBuilder sb = new StringBuilder();
        int[] code = p.code;
        LuaValue[] k = p.k;

        String[] vars = new String[p.maxstacksize > 0 ? p.maxstacksize : 256];
        for (int i = 0; i < vars.length; i++) {
            vars[i] = "v" + i;
        }

        for (CFGBuilder.BasicBlock block : blocks) {
            sb.append("\n  -- [Basic Block ").append(block.id).append("] PCs: ").append(block.startPc).append("-").append(block.endPc).append("\n");

            if (!block.inEdges.isEmpty()) {
                sb.append("  -- In from: ");
                for (CFGBuilder.BasicBlock in : block.inEdges) sb.append(in.id).append(" ");
                sb.append("\n");
            }
            sb.append("  ::LABEL_").append(block.id).append("::\n");

            for (int pc = block.startPc; pc <= block.endPc; pc++) {
                int i = code[pc];
                int opcode = Lua.GET_OPCODE(i);
                int a = Lua.GETARG_A(i);
                int b = Lua.GETARG_B(i);
                int c = Lua.GETARG_C(i);
                int bx = Lua.GETARG_Bx(i);
                int sbx = Lua.GETARG_sBx(i);

                sb.append("  ");
            try {
                switch (opcode) {
                    case Lua.OP_MOVE:
                        vars[a] = vars[b];
                        sb.append(String.format("v%d = %s", a, vars[b]));
                        break;
                    case Lua.OP_LOADK:
                        vars[a] = formatConstant(k[bx]);
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_LOADBOOL:
                        vars[a] = b != 0 ? "true" : "false";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        if (c != 0) sb.append(" -- skip next");
                        break;
                    case Lua.OP_LOADNIL:
                        for (int j = a; j <= a + b; j++) vars[j] = "nil";
                        sb.append(String.format("for i=%d, %d do vi = nil end", a, a + b));
                        break;
                    case Lua.OP_GETUPVAL:
                        vars[a] = "upvalue_" + b;
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_GETTABUP:
                        String keyStr = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        if (b == 0) {
                            vars[a] = keyStr; // Global access
                        } else {
                            vars[a] = "upvalue_" + b + "[" + keyStr + "]";
                        }
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_GETTABLE:
                        String keyStr2 = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        vars[a] = vars[b] + "[" + keyStr2 + "]";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_SETTABUP:
                        String keyStr3 = b > 0xff ? formatConstant(k[b & 0xff]) : vars[b];
                        String valStr = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        if (a == 0) {
                            sb.append(String.format("%s = %s", keyStr3, valStr));
                        } else {
                            sb.append(String.format("upvalue_%d[%s] = %s", a, keyStr3, valStr));
                        }
                        break;
                    case Lua.OP_SETUPVAL:
                        sb.append(String.format("upvalue_%d = v%d", b, a));
                        break;
                    case Lua.OP_SETTABLE:
                        String keyStr4 = b > 0xff ? formatConstant(k[b & 0xff]) : vars[b];
                        String valStr2 = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        sb.append(String.format("v%d[%s] = %s", a, keyStr4, valStr2));
                        break;
                    case Lua.OP_NEWTABLE:
                        vars[a] = "{}";
                        sb.append(String.format("v%d = {}", a));
                        break;
                    case Lua.OP_SELF:
                        String keyStr5 = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        vars[a+1] = vars[b];
                        vars[a] = vars[b] + "[" + keyStr5 + "]";
                        sb.append(String.format("v%d = %s; v%d = %s", a+1, vars[b], a, vars[a]));
                        break;
                    case Lua.OP_ADD:
                    case Lua.OP_SUB:
                    case Lua.OP_MUL:
                    case Lua.OP_DIV:
                    case Lua.OP_MOD:
                    case Lua.OP_POW:
                        String op = opcode == Lua.OP_ADD ? "+" : opcode == Lua.OP_SUB ? "-" : opcode == Lua.OP_MUL ? "*" : opcode == Lua.OP_DIV ? "/" : opcode == Lua.OP_MOD ? "%" : "^";
                        String left = b > 0xff ? formatConstant(k[b & 0xff]) : vars[b];
                        String right = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        vars[a] = "(" + left + " " + op + " " + right + ")";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_UNM:
                        vars[a] = "(-v" + b + ")";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_NOT:
                        vars[a] = "(not v" + b + ")";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_LEN:
                        vars[a] = "(#v" + b + ")";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_CONCAT:
                        StringBuilder concatSb = new StringBuilder();
                        for (int j = b; j <= c; j++) {
                            if (j > b) concatSb.append(" .. ");
                            concatSb.append(vars[j]);
                        }
                        vars[a] = "(" + concatSb.toString() + ")";
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_JMP:
                        sb.append(String.format("goto PC_%d", pc + 1 + sbx));
                        break;
                    case Lua.OP_EQ:
                    case Lua.OP_LT:
                    case Lua.OP_LE:
                        String cmp = opcode == Lua.OP_EQ ? "==" : opcode == Lua.OP_LT ? "<" : "<=";
                        if (a == 0) cmp = opcode == Lua.OP_EQ ? "~=" : opcode == Lua.OP_LT ? ">=" : ">";
                        String leftCmp = b > 0xff ? formatConstant(k[b & 0xff]) : vars[b];
                        String rightCmp = c > 0xff ? formatConstant(k[c & 0xff]) : vars[c];
                        sb.append(String.format("if (%s %s %s) then", leftCmp, cmp, rightCmp));
                        break;
                    case Lua.OP_TEST:
                        if (c == 0) sb.append(String.format("if (v%d) then", a));
                        else sb.append(String.format("if (not v%d) then", a));
                        break;
                    case Lua.OP_TESTSET:
                        if (c == 0) {
                            sb.append(String.format("if (v%d) then v%d = v%d else", b, a, b));
                        } else {
                            sb.append(String.format("if (not v%d) then v%d = v%d else", b, a, b));
                        }
                        break;
                    case Lua.OP_CALL:
                        StringBuilder callArgs = new StringBuilder();
                        if (b == 0) {
                            callArgs.append(String.format("v%d ... top", a + 1));
                        } else {
                            for (int j = 1; j < b; j++) {
                                if (j > 1) callArgs.append(", ");
                                callArgs.append(vars[a + j]);
                            }
                        }

                        String callExpr = vars[a] + "(" + callArgs.toString() + ")";

                        if (c == 0) {
                            sb.append(String.format("v%d... = %s", a, callExpr));
                            vars[a] = callExpr;
                        } else if (c == 1) {
                            sb.append(callExpr);
                        } else {
                            StringBuilder rets = new StringBuilder();
                            for (int j = 0; j < c - 1; j++) {
                                if (j > 0) rets.append(", ");
                                rets.append("v" + (a + j));
                                vars[a + j] = "ret_" + j + "_of_" + vars[a];
                            }
                            sb.append(String.format("%s = %s", rets.toString(), callExpr));
                        }
                        break;
                    case Lua.OP_TAILCALL:
                        sb.append(String.format("return v%d(...)", a));
                        break;
                    case Lua.OP_RETURN:
                        if (b == 0) {
                            sb.append(String.format("return v%d ... top", a));
                        } else if (b == 1) {
                            sb.append("return");
                        } else {
                            StringBuilder rets2 = new StringBuilder();
                            for (int j = 0; j < b - 1; j++) {
                                if (j > 0) rets2.append(", ");
                                rets2.append(vars[a + j]);
                            }
                            sb.append(String.format("return %s", rets2.toString()));
                        }
                        break;
                    case Lua.OP_FORLOOP:
                        sb.append(String.format("v%d += v%d; if v%d <= v%d then goto PC_%d", a, a+2, a, a+1, pc + 1 + sbx));
                        break;
                    case Lua.OP_FORPREP:
                        sb.append(String.format("v%d -= v%d; goto PC_%d", a, a+2, pc + 1 + sbx));
                        break;
                    case Lua.OP_TFORCALL:
                        sb.append(String.format("v%d, ... v%d = v%d(v%d, v%d)", a+3, a+2+c, a, a+1, a+2));
                        break;
                    case Lua.OP_TFORLOOP:
                        sb.append(String.format("if v%d ~= nil then v%d = v%d; goto PC_%d", a+1, a, a+1, pc + 1 + sbx));
                        break;
                    case Lua.OP_SETLIST:
                        sb.append(String.format("v%d[...] = ...", a));
                        break;
                    case Lua.OP_CLOSURE:
                        vars[a] = "closure_" + bx;
                        sb.append(String.format("v%d = %s", a, vars[a]));
                        break;
                    case Lua.OP_VARARG:
                        vars[a] = "vararg";
                        sb.append(String.format("v%d = ...", a));
                        break;
                    default:
                        sb.append(String.format("-- OP_%d a=%d b=%d c=%d bx=%d", opcode, a, b, c, bx));
                        break;
                }
            } catch (Exception e) {
                sb.append(" -- error decompiling: ").append(e.getMessage());
            }
            sb.append("\n");
            }

            if (!block.outEdges.isEmpty()) {
                sb.append("  -- Out to: ");
                for (CFGBuilder.BasicBlock out : block.outEdges) sb.append(out.id).append(" ");
                sb.append("\n");
            }
        }

        return sb.toString();
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
