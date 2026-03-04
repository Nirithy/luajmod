package luaj.compiler;

import luaj.LocVars;
import luaj.Lua;
import luaj.LuaError;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Prototype;
import luaj.Upvaldesc;

public class Constants extends Lua {
    static final int LUAI_MAXUPVAL = 0xFF;
    static final int LUAI_MAXVARS = 200;
    public static final int MAXSTACK = 0xFA;
    static final int NO_REG = 0xFF;
    static final int OpArgK = 3;
    static final int OpArgN = 0;
    static final int OpArgR = 2;
    static final int OpArgU = 1;
    static final int iABC = 0;
    static final int iABx = 1;
    static final int iAsBx = 2;

    public static int CREATE_ABC(int o, int a, int b, int c) [...] // Inlined contents

    public static int CREATE_ABx(int o, int a, int bc) {
        return o & 0x3F | a << 6 & 0x3FC0 | bc << 14 & 0xFFFFC000;
    }

    public static int CREATE_Ax(int o, int a) {
        return o & 0x3F | a << 6 & 0xFFFFFFC0;
    }

    public static void SETARG_A(InstructionPtr i, int u) {
        i.set(i.get() & 0xFFFFC03F | u << 6 & 0x3FC0);
    }

    public static void SETARG_A(int[] code, int index, int u) {
        code[index] = code[index] & 0xFFFFC03F | u << 6 & 0x3FC0;
    }

    public static void SETARG_B(InstructionPtr i, int u) {
        i.set(i.get() & 0x7FFFFF | u << 23 & 0xFF800000);
    }

    public static void SETARG_Bx(InstructionPtr i, int u) {
        i.set(i.get() & 0x3FFF | u << 14 & 0xFFFFC000);
    }

    public static void SETARG_C(InstructionPtr i, int u) {
        i.set(i.get() & 0xFF803FFF | u << 14 & 0x7FC000);
    }

    public static void SETARG_sBx(InstructionPtr i, int u) {
        Constants.SETARG_Bx(i, u + 0x1FFFF);
    }

    public static void SET_OPCODE(InstructionPtr i, int o) {
        i.set(i.get() & 0xFFFFFFC0 | o & 0x3F);
    }

    protected static void _assert(boolean b) {
        if(!b) {
            throw new LuaError("compiler assert failed");
        }
    }

    static Labeldesc[] grow(Labeldesc[] v, int min_n) {
        if(v == null) {
            return new Labeldesc[2];
        }
        return v.length >= min_n ? v : Constants.realloc(v, v.length * 2);
    }

    static byte[] realloc(byte[] v, int n) {
        byte[] a = new byte[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static char[] realloc(char[] v, int n) {
        char[] a = new char[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static int[] realloc(int[] v, int n) {
        int[] a = new int[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static LocVars[] realloc(LocVars[] v, int n) {
        LocVars[] a = new LocVars[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static LuaString[] realloc(LuaString[] v, int n) {
        LuaString[] a = new LuaString[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static LuaValue[] realloc(LuaValue[] v, int n) {
        LuaValue[] a = new LuaValue[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static Prototype[] realloc(Prototype[] v, int n) {
        Prototype[] a = new Prototype[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static Upvaldesc[] realloc(Upvaldesc[] v, int n) {
        Upvaldesc[] a = new Upvaldesc[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static Labeldesc[] realloc(Labeldesc[] v, int n) {
        Labeldesc[] a = new Labeldesc[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }

    static Vardesc[] realloc(Vardesc[] v, int n) {
        Vardesc[] a = new Vardesc[n];
        if(v != null) {
            System.arraycopy(v, 0, a, 0, Math.min(v.length, n));
        }
        return a;
    }
}

