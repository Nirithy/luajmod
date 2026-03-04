package org.luaj.vm2.decompiler;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.StringInterceptor;
import org.luaj.vm2.Varargs;

public class PseudoExecution {

    /**
     * Executes the bytecodes of the prototype without actually applying side effects,
     * skipping errors, and simply loading DummyLuaValues when fields or globals
     * cannot be resolved.
     */
    public static void analyze(Prototype p, Globals globals) {
        if (p == null || p.code == null) {
            return;
        }
        
        System.out.println("Starting pseudo-execution engine on " + p.code.length + " instructions.");
        int pc = 0;
        int[] code = p.code;
        LuaValue[] k = p.k;
        
        // Setup a dummy execution stack
        LuaValue[] stack = new LuaValue[p.maxstacksize > 0 ? p.maxstacksize : 256];
        for(int j=0; j<stack.length; j++) {
            stack[j] = LuaValue.NIL;
        }

        // Dummy UpValues
        LuaValue[] upvalues = new LuaValue[p.upvalues != null ? p.upvalues.length : 0];
        for (int j = 0; j < upvalues.length; j++) {
            upvalues[j] = new DummyLuaValue("Upvalue_" + j);
        }
        
        // Loop over the bytecode until we hit a return
        while (pc < code.length) {
            int i = code[pc++];
            int opcode = Lua.GET_OPCODE(i);
            
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);
            
            try {
                switch (opcode) {
                    case Lua.OP_MOVE:
                        stack[a] = stack[b];
                        break;
                    
                    case Lua.OP_LOADK:
                        stack[a] = k[bx];
                        if (k[bx].isstring()) {
                            StringInterceptor.onStringCreate(
                                ((LuaString)k[bx]).m_bytes,
                                ((LuaString)k[bx]).m_offset,
                                ((LuaString)k[bx]).m_length
                            );
                        }
                        break;
                    
                    case Lua.OP_LOADKX:
                        int nextI = code[pc++];
                        if (Lua.GET_OPCODE(nextI) == Lua.OP_EXTRAARG) {
                            stack[a] = k[Lua.GETARG_Ax(nextI)];
                        }
                        break;
                        
                    case Lua.OP_LOADBOOL:
                        stack[a] = (b != 0) ? LuaValue.TRUE : LuaValue.FALSE;
                        if (c != 0) pc++;
                        break;
                        
                    case Lua.OP_LOADNIL:
                        for (int j = a; j <= a + b; j++) {
                            stack[j] = LuaValue.NIL;
                        }
                        break;
                        
                    case Lua.OP_GETUPVAL:
                        stack[a] = upvalues[b];
                        break;
                        
                    case Lua.OP_GETTABUP:
                        // Instead of crashing if UpValue table does not have RK(C)
                        LuaValue gettabupKey = c > 0xff ? k[c & 0xff] : stack[c];
                        // Usually upvalue[0] is _ENV. We put a DummyLuaValue
                        stack[a] = new DummyLuaValue("Global_" + gettabupKey.tojstring());
                        break;
                        
                    case Lua.OP_GETTABLE:
                        LuaValue gettableKey = c > 0xff ? k[c & 0xff] : stack[c];
                        stack[a] = new DummyLuaValue("TableField_" + gettableKey.tojstring());
                        break;
                        
                    case Lua.OP_SETTABUP:
                    case Lua.OP_SETUPVAL:
                    case Lua.OP_SETTABLE:
                    case Lua.OP_NEWTABLE:
                    case Lua.OP_SELF:
                    case Lua.OP_ADD:
                    case Lua.OP_SUB:
                    case Lua.OP_MUL:
                    case Lua.OP_DIV:
                    case Lua.OP_MOD:
                    case Lua.OP_POW:
                    case Lua.OP_UNM:
                    case Lua.OP_NOT:
                    case Lua.OP_LEN:
                        stack[a] = new DummyLuaValue("Math/LogicResult");
                        break;
                        
                    case Lua.OP_CONCAT:
                        StringBuilder concatSb = new StringBuilder();
                        for (int j = b; j <= c; j++) {
                            if (stack[j] != null && stack[j].isstring()) {
                                concatSb.append(stack[j].tojstring());
                            }
                        }
                        String concatResult = concatSb.toString();
                        stack[a] = LuaValue.valueOf(concatResult);
                        StringInterceptor.onStringCreate(concatResult.getBytes(), 0, concatResult.length());
                        break;
                        
                    case Lua.OP_JMP:
                        pc += sbx;
                        if (a > 0) {
                           // close upvalues logic
                        }
                        break;
                        
                    case Lua.OP_EQ:
                    case Lua.OP_LT:
                    case Lua.OP_LE:
                    case Lua.OP_TEST:
                    case Lua.OP_TESTSET:
                        // Just simple fall-through for testing. 
                        // Real jumps depend on runtime values which we bypass
                        break;
                        
                    case Lua.OP_CALL:
                        // Place dummy returned values on stack if C >= 2
                        if (c >= 2) {
                            for (int j = 0; j < c - 1; j++) {
                                stack[a + j] = new DummyLuaValue("CallResult_" + j);
                            }
                        } else if (c == 0) {
                            // Variable return
                            stack[a] = new DummyLuaValue("VarReturn");
                        }
                        break;
                        
                    case Lua.OP_TAILCALL:
                    case Lua.OP_RETURN:
                        // End of block
                        if (opcode == Lua.OP_RETURN && pc == code.length) {
                             // normal exit
                        }
                        break;
                        
                    case Lua.OP_FORLOOP:
                    case Lua.OP_FORPREP:
                    case Lua.OP_TFORCALL:
                    case Lua.OP_TFORLOOP:
                        // bypass loops logic for safety
                        if (opcode == Lua.OP_FORPREP) {
                            pc += sbx;
                        }
                        break;
                        
                    case Lua.OP_SETLIST:
                    case Lua.OP_CLOSURE:
                        if (opcode == Lua.OP_CLOSURE) {
                            stack[a] = new DummyLuaValue("Closure_" + bx);
                        }
                        break;
                        
                    case Lua.OP_VARARG:
                        stack[a] = new DummyLuaValue("VarArg");
                        break;
                        
                    default:
                        // No side effects, ignore safely
                        break;
                }
            } catch (Exception e) {
                System.err.println("Warning: execution bypassed an error at PC " + (pc - 1) + ", opcode=" + opcode + ": " + e.getMessage());
            }
        }
        System.out.println("Pseudo-execution completed gracefully.");
    }
}
