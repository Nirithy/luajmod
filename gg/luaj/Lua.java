package luaj;

import java.io.File;

public class Lua {
    public static final int BITRK = 0x100;
    public static final int LFIELDS_PER_FLUSH = 50;
    public static final int LUA_MULTRET = -1;
    public static final int MASK_A = 0x3FC0;
    public static final int MASK_Ax = 0xFFFFFFC0;
    public static final int MASK_B = 0xFF800000;
    public static final int MASK_Bx = 0xFFFFC000;
    public static final int MASK_C = 0x7FC000;
    public static final int MASK_NOT_A = 0xFFFFC03F;
    public static final int MASK_NOT_B = 0x7FFFFF;
    public static final int MASK_NOT_Bx = 0x3FFF;
    public static final int MASK_NOT_C = 0xFF803FFF;
    public static final int MASK_NOT_OP = 0xFFFFFFC0;
    public static final int MASK_OP = 0x3F;
    public static final int MAXARG_A = 0xFF;
    public static final int MAXARG_Ax = 0x3FFFFFF;
    public static final int MAXARG_B = 0x1FF;
    public static final int MAXARG_Bx = 0x3FFFF;
    public static final int MAXARG_C = 0x1FF;
    public static final int MAXARG_sBx = 0x1FFFF;
    public static final int MAXINDEXRK = 0xFF;
    private static final int MAXSRC = 80;
    public static final int MAX_OP = 0x3F;
    public static final int NO_REG = 0xFF;
    public static final int NUM_OPCODES = 0x2F;
    public static final int OP_ADD = 13;
    public static final int OP_AND = 60;
    public static final int OP_BAND = 42;
    public static final int OP_BNOT = 41;
    public static final int OP_BOR = 43;
    public static final int OP_BXOR = 44;
    public static final int OP_CALL = 29;
    public static final int OP_CLOSURE = 37;
    public static final int OP_CONCAT = 22;
    public static final int OP_DIV = 16;
    public static final int OP_EQ = 24;
    public static final int OP_EXTRAARG = 39;
    public static final int OP_FORLOOP = 0x20;
    public static final int OP_FORPREP = 33;
    public static final int OP_GE = 62;
    public static final int OP_GETFIELDT = 0x30;
    public static final int OP_GETFIELDU = 0x2F;
    public static final int OP_GETTABLE = 7;
    public static final int OP_GETTABUP = 6;
    public static final int OP_GETUPVAL = 5;
    public static final int OP_GT = 0x3F;
    public static final int OP_IDIV = 40;
    public static final int OP_JMP = 23;
    public static final int OP_LE = 26;
    public static final int OP_LEN = 21;
    public static final int OP_LOADBOOL = 3;
    public static final int OP_LOADK = 1;
    public static final int OP_LOADKX = 2;
    public static final int OP_LOADNIL = 4;
    public static final int OP_LT = 25;
    public static final int OP_MOD = 17;
    public static final int OP_MOVE = 0;
    public static final int OP_MUL = 15;
    public static final int OP_NEQ = 61;
    public static final int OP_NEWTABLE = 11;
    public static final int OP_NOT = 20;
    public static final int OP_OR = 59;
    public static final int OP_POW = 18;
    public static final int OP_RETURN = 0x1F;
    public static final int OP_SELF = 12;
    public static final int OP_SETLIST = 36;
    public static final int OP_SETTABLE = 10;
    public static final int OP_SETTABUP = 8;
    public static final int OP_SETUPVAL = 9;
    public static final int OP_SHL = 45;
    public static final int OP_SHR = 46;
    public static final int OP_SUB = 14;
    public static final int OP_TAILCALL = 30;
    public static final int OP_TEST = 27;
    public static final int OP_TESTSET = 28;
    public static final int OP_TFORCALL = 34;
    public static final int OP_TFORLOOP = 35;
    public static final int OP_UNM = 19;
    public static final int OP_VARARG = 38;
    public static final int OpArgK = 3;
    public static final int OpArgN = 0;
    public static final int OpArgR = 2;
    public static final int OpArgU = 1;
    public static final int POS_A = 6;
    public static final int POS_Ax = 6;
    public static final int POS_B = 23;
    public static final int POS_Bx = 14;
    public static final int POS_C = 14;
    public static final int POS_OP = 0;
    public static final int SIZE_A = 8;
    public static final int SIZE_Ax = 26;
    public static final int SIZE_B = 9;
    public static final int SIZE_Bx = 18;
    public static final int SIZE_C = 9;
    public static final int SIZE_OP = 6;
    public static final String _VERSION = "Lua 5.3 (with Luaj 3.0.1)";
    public static final int iABC = 0;
    public static final int iABx = 1;
    public static final int iAsBx = 2;
    public static final int iAx = 3;
    public static final int[] luaP_opmodes;

    static {
        Lua.luaP_opmodes = new int[]{0x60, 0x71, 65, 84, 80, 80, 92, 108, 60, 16, 60, 84, 108, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 0x60, 0x60, 0x60, 104, 34, 0xBC, 0xBC, 0xBC, 0x84, 0xE4, 84, 80, 16, 98, 98, 4, 0xE2, 20, 81, 80, 23, 0x7C, 0x60, 0x7C, 0x7C, 0x7C, 0x7C, 0x7C, 92, 108};
    }

    public static int GETARG_A(int i) [...] // Inlined contents

    public static int GETARG_Ax(int i) {
        return i >>> 6 & 0x3FFFFFF;
    }

    public static int GETARG_B(int i) [...] // Inlined contents

    public static int GETARG_Bx(int i) [...] // Inlined contents

    public static int GETARG_C(int i) [...] // Inlined contents

    public static int GETARG_sBx(int i) [...] // Inlined contents

    public static int GET_OPCODE(int i) [...] // Inlined contents

    public static int INDEXK(int r) [...] // Inlined contents

    public static boolean ISK(int x) {
        return (x & 0x100) != 0;
    }

    public static int RKASK(int x) {
        return x | 0x100;
    }

    public static String chunkid(String source) {
        if(source.startsWith("=")) {
            return source.substring(1);
        }
        if(source.startsWith("@")) {
            source = source.substring(1);
        }
        if(!new File(source).exists()) {
            if(source.length() > 69) {
                source = source.substring(0, 66) + "...";
            }
            return "[string \'" + source + "\']";
        }
        return source;
    }

    public static int getBMode(int m) {
        return Lua.luaP_opmodes[m] >>> 4 & 3;
    }

    public static int getCMode(int m) {
        return Lua.luaP_opmodes[m] >>> 2 & 3;
    }

    public static int getOpMode(int m) {
        return Lua.luaP_opmodes[m] & 3;
    }

    public static void main(String[] args) throws Throwable {
    }

    public static boolean testAMode(int m) {
        return (Lua.luaP_opmodes[m] & 0x40) != 0;
    }

    public static boolean testTMode(int m) {
        return (Lua.luaP_opmodes[m] & 0x80) != 0;
    }
}

