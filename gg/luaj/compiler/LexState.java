package luaj.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Hashtable;
import luaj.LocVars;
import luaj.Lua;
import luaj.LuaBigNumber;
import luaj.LuaDouble;
import luaj.LuaError;
import luaj.LuaLong;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Prototype;

public class LexState extends Constants {
    static class CharBuffer implements CharSequence {
        char[] buff;
        int nbuff;

        @Override
        public char charAt(int index) {
            return this.buff[index];
        }

        @Override
        public int length() {
            return this.nbuff;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }

    static class ConsControl {
        int na;
        int nh;
        expdesc t;
        int tostore;
        expdesc v;

        ConsControl() {
            this.v = new expdesc();
        }
    }

    static class Dyndata {
        Vardesc[] actvar;
        Labeldesc[] gt;
        Labeldesc[] label;
        int n_actvar;
        int n_gt;
        int n_label;

        Dyndata() {
            this.n_actvar = 0;
            this.n_gt = 0;
            this.n_label = 0;
        }

        @Override
        public String toString() {
            return "Dyndata [actvar=" + Arrays.toString(this.actvar) + ", n_actvar=" + this.n_actvar + ", gt=" + Arrays.toString(this.gt) + ", n_gt=" + this.n_gt + ", label=" + Arrays.toString(this.label) + ", n_label=" + this.n_label + ']';
        }
    }

    static class LHS_assign {
        LHS_assign prev;
        expdesc v;

        LHS_assign() {
            this.v = new expdesc();
        }
    }

    static class Labeldesc {
        int line;
        short nactvar;
        LuaString name;
        int pc;

        public Labeldesc(LuaString name, int pc, int line, short nactvar) {
            this.name = name;
            this.pc = pc;
            this.line = line;
            this.nactvar = nactvar;
        }

        @Override
        public String toString() {
            return "Labeldesc [name=" + this.name + ", pc=" + this.pc + ", line=" + this.line + ", nactvar=" + ((int)this.nactvar) + ']';
        }
    }

    static class Priority {
        final byte left;
        final byte right;

        public Priority(int i, int j) {
            this.left = (byte)i;
            this.right = (byte)j;
        }
    }

    static class SemInfo {
        LuaValue r;
        LuaString ts;

        private SemInfo() {
        }

        SemInfo(SemInfo lexState$SemInfo0) {
        }
    }

    static class Token {
        final SemInfo seminfo;
        int token;

        private Token() {
            this.seminfo = new SemInfo(null);
        }

        Token(Token lexState$Token0) {
        }

        public void set(Token other) {
            this.token = other.token;
            this.seminfo.r = other.seminfo.r;
            this.seminfo.ts = other.seminfo.ts;
        }
    }

    static class Vardesc {
        final short idx;

        Vardesc(int idx) {
            this.idx = (short)idx;
        }

        @Override
        public String toString() {
            return "Vardesc [idx=" + ((int)this.idx) + ']';
        }
    }

    static class expdesc {
        static class U {
            LuaValue _nval;
            short ind_idx;
            short ind_t;
            short ind_vt;
            int info;

            public LuaValue nval() {
                return this._nval == null ? LuaLong.valueOf(this.info) : this._nval;
            }

            public void setNval(LuaValue r) {
                this._nval = r;
            }
        }

        final IntPtr f;
        int k;
        final IntPtr t;
        final U u;

        expdesc() {
            this.u = new U();
            this.t = new IntPtr();
            this.f = new IntPtr();
        }

        protected expdesc clone2() {
            expdesc lexState$expdesc0 = new expdesc();
            lexState$expdesc0.setvalue(this);
            return lexState$expdesc0;
        }

        boolean hasjumps() {
            return this.t.i != this.f.i;
        }

        void init(int k, int i) {
            this.f.i = -1;
            this.t.i = -1;
            this.k = k;
            this.u.info = i;
        }

        boolean isnumeral() {
            return this.k == 5 && this.t.i == -1 && this.f.i == -1;
        }

        public void setvalue(expdesc other) {
            this.f.i = other.f.i;
            this.k = other.k;
            this.t.i = other.t.i;
            this.u._nval = other.u._nval;
            this.u.ind_idx = other.u.ind_idx;
            this.u.ind_t = other.u.ind_t;
            this.u.ind_vt = other.u.ind_vt;
            this.u.info = other.u.info;
        }
    }

    private static final int EOZ = -1;
    static final int FIRST_RESERVED = 0x101;
    CompileState L;
    private static final int LUAI_MAXCCALLS = 200;
    private static final int LUA_COMPAT_LSTR = 1;
    private static final boolean LUA_COMPAT_VARARG = true;
    private static final int MAX_INT = 0x7FFFFFFD;
    static final int NO_JUMP = -1;
    static final int NUM_RESERVED = 22;
    static final int OPR_ADD = 0;
    static final int OPR_AND = 13;
    static final int OPR_BAND = 16;
    static final int OPR_BNOT = 4;
    static final int OPR_BOR = 17;
    static final int OPR_BXOR = 18;
    static final int OPR_CONCAT = 6;
    static final int OPR_DIV = 3;
    static final int OPR_EQ = 8;
    static final int OPR_GE = 12;
    static final int OPR_GT = 11;
    static final int OPR_IDIV = 15;
    static final int OPR_LE = 10;
    static final int OPR_LEN = 2;
    static final int OPR_LT = 9;
    static final int OPR_MINUS = 0;
    static final int OPR_MOD = 4;
    static final int OPR_MUL = 2;
    static final int OPR_NE = 7;
    static final int OPR_NOBINOPR = 21;
    static final int OPR_NOT = 1;
    static final int OPR_NOUNOPR = 3;
    static final int OPR_OR = 14;
    static final int OPR_POW = 5;
    static final int OPR_SHL = 19;
    static final int OPR_SHR = 20;
    static final int OPR_SUB = 1;
    static final Hashtable RESERVED = null;
    protected static final String RESERVED_LABEL_BREAK = "break";
    protected static final String RESERVED_LOCAL_VAR_FOR_CONTROL = "(for control)";
    protected static final String RESERVED_LOCAL_VAR_FOR_GENERATOR = "(for generator)";
    protected static final String RESERVED_LOCAL_VAR_FOR_INDEX = "(for index)";
    protected static final String RESERVED_LOCAL_VAR_FOR_LIMIT = "(for limit)";
    protected static final String RESERVED_LOCAL_VAR_FOR_STATE = "(for state)";
    protected static final String RESERVED_LOCAL_VAR_FOR_STEP = "(for step)";
    protected static final String[] RESERVED_LOCAL_VAR_KEYWORDS = null;
    private static final Hashtable RESERVED_LOCAL_VAR_KEYWORDS_TABLE = null;
    static final int TK_AND = 0x101;
    static final int TK_ASSIGN_ADD = 294;
    static final int TK_ASSIGN_BAND = 301;
    static final int TK_ASSIGN_BOR = 302;
    static final int TK_ASSIGN_CONCAT = 303;
    static final int TK_ASSIGN_DIV = 297;
    static final int TK_ASSIGN_IDIV = 300;
    static final int TK_ASSIGN_MOD = 298;
    static final int TK_ASSIGN_MUL = 296;
    static final int TK_ASSIGN_POW = 299;
    static final int TK_ASSIGN_SHL = 304;
    static final int TK_ASSIGN_SHR = 305;
    static final int TK_ASSIGN_SUB = 295;
    static final int TK_BREAK = 0x102;
    static final int TK_CASE = 307;
    static final int TK_CONCAT = 280;
    static final int TK_CONTINUE = 308;
    static final int TK_DBCOLON = 0x120;
    static final int TK_DEFAULT = 309;
    static final int TK_DO = 0x103;
    static final int TK_DOTS = 281;
    static final int TK_ELSE = 260;
    static final int TK_ELSEIF = 0x105;
    static final int TK_END = 0x106;
    static final int TK_EOF = 289;
    static final int TK_EQ = 282;
    static final int TK_FALSE = 0x107;
    static final int TK_FIELD = 310;
    static final int TK_FOR = 0x108;
    static final int TK_FUNCTION = 0x109;
    static final int TK_GE = 283;
    static final int TK_GOTO = 0x10A;
    static final int TK_IDIV = 279;
    static final int TK_IF = 0x10B;
    static final int TK_IN = 0x10C;
    static final int TK_INTEGER = 291;
    static final int TK_LE = 284;
    static final int TK_LOCAL = 0x10D;
    static final int TK_NAME = 292;
    static final int TK_NE = 285;
    static final int TK_NIL = 270;
    static final int TK_NOT = 0x10F;
    static final int TK_NUMBER = 290;
    static final int TK_OR = 0x110;
    static final int TK_REPEAT = 273;
    static final int TK_RETURN = 274;
    static final int TK_SHL = 286;
    static final int TK_SHR = 0x11F;
    static final int TK_STRING = 293;
    static final int TK_SWITCH = 306;
    static final int TK_THEN = 275;
    static final int TK_TRUE = 276;
    static final int TK_UNTIL = 277;
    static final int TK_WHILE = 278;
    private static final int UCHAR_MAX = 0xFF;
    static final int UNARY_PRIORITY = 100;
    static final int VCALL = 12;
    static final int VFALSE = 3;
    static final int VINDEXED = 9;
    static final int VJMP = 10;
    static final int VK = 4;
    static final int VKNUM = 5;
    static final int VLOCAL = 7;
    static final int VNIL = 1;
    static final int VNONRELOC = 6;
    static final int VRELOCABLE = 11;
    static final int VTRUE = 2;
    static final int VUPVAL = 8;
    static final int VVARARG = 13;
    static final int VVOID;
    char[] buff;
    private final byte[][] bytes;
    private CharBuffer charBuffer;
    int current;
    byte decpoint;
    Dyndata dyd;
    private final int[] ec;
    LuaString envn;
    FuncState fs;
    int lastline;
    int linenumber;
    final Token lookahead;
    static final String[] luaX_tokens;
    int nbuff;
    static Priority[] priority;
    LuaString source;
    final Token t;
    InputStream z;

    static {
        LexState.luaX_tokens = new String[]{"and", "break", "do", "else", "elseif", "end", "false", "for", "function", "goto", "if", "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while", "//", "..", "...", "==", ">=", "<=", "~=", "<<", ">>", "::", "<eof>", "<number>", "<integer>", "<name>", "<string>", "+=", "-=", "*=", "/=", "%=", "^=", "//=", "&=", "|=", "..=", "<<=", ">>=", "switch", "case", "continue", "default", "->"};
        LexState.RESERVED_LOCAL_VAR_KEYWORDS = new String[]{"(for control)", "(for generator)", "(for index)", "(for limit)", "(for state)", "(for step)", "break"};
        LexState.RESERVED_LOCAL_VAR_KEYWORDS_TABLE = new Hashtable();
        for(int i = 0; i < LexState.RESERVED_LOCAL_VAR_KEYWORDS.length; ++i) {
            LexState.RESERVED_LOCAL_VAR_KEYWORDS_TABLE.put(LexState.RESERVED_LOCAL_VAR_KEYWORDS[i], Boolean.TRUE);
        }
        LexState.RESERVED = new Hashtable();
        for(int i = 0; i < 22; ++i) {
            LuaString luaString0 = LuaValue.valueOf(LexState.luaX_tokens[i]);
            LexState.RESERVED.put(luaString0, ((int)(i + 0x101)));
        }
        LexState.priority = new Priority[]{new Priority(60, 60), new Priority(60, 60), new Priority(70, 70), new Priority(70, 70), new Priority(70, 70), new Priority(100, 90), new Priority(50, 40), new Priority(30, 30), new Priority(30, 30), new Priority(30, 30), new Priority(30, 30), new Priority(30, 30), new Priority(30, 30), new Priority(20, 20), new Priority(10, 10), new Priority(70, 70), new Priority(36, 36), new Priority(0x20, 0x20), new Priority(34, 34), new Priority(38, 38), new Priority(38, 38)};
        for(int i = 306; i <= 309; ++i) {
            LuaString luaString1 = LuaValue.valueOf(LexState.luaX_tokens[i - 0x101]);
            LexState.RESERVED.put(luaString1, i);
        }
    }

    public LexState(CompileState state, InputStream stream) {
        this.t = new Token(null);
        this.lookahead = new Token(null);
        this.dyd = new Dyndata();
        this.charBuffer = new CharBuffer();
        this.ec = new int[24];
        this.z = stream;
        this.buff = new char[0x20];
        this.L = state;
        this.bytes = new byte[][]{new byte[]{(byte)0xC0, (byte)0xB1}, new byte[]{(byte)0xE0, (byte)0x80, (byte)0xB1}, new byte[]{-16, (byte)0x80, (byte)0x80, (byte)0xB1}, new byte[]{-8, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0xB1}, new byte[]{-4, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0xB1}};
    }

    private static final String LUA_QL(Object o) [...] // 潜在的解密器

    private static final String LUA_QS(String s) [...] // Inlined contents

    Prototype addprototype() {
        Prototype f = this.fs.f;
        if(f.p == null || this.fs.np >= f.p.length) {
            f.p = LexState.realloc(f.p, Math.max(1, this.fs.np * 2));
        }
        Prototype[] arr_prototype = f.p;
        int v = this.fs.np;
        this.fs.np = v + 1;
        Prototype clp = new Prototype();
        arr_prototype[v] = clp;
        return clp;
    }

    void adjust_assign(int nvars, int nexps, expdesc e) {
        FuncState fs = this.fs;
        int extra = nvars - nexps;
        if(this.hasmultret(e.k)) {
            int extra = extra + 1 >= 0 ? extra + 1 : 0;
            fs.setreturns(e, extra);
            if(extra > 1) {
                fs.reserveregs(extra - 1);
            }
        }
        else {
            if(e.k != 0) {
                fs.exp2nextreg(e);
            }
            if(extra > 0) {
                int reg = fs.freereg;
                fs.reserveregs(extra);
                fs.nil(reg, extra);
            }
        }
    }

    void adjustlocalvars(int nvars) {
        FuncState fs = this.fs;
        fs.nactvar = (short)(fs.nactvar + nvars);
        while(nvars > 0) {
            LocVars locVars0 = fs.getlocvar(fs.nactvar - nvars);
            locVars0.startpc = fs.pc;
            --nvars;
        }
    }

    void anchor_token() {
        LexState._assert(this.fs != null || this.t.token == 289);
        if(this.t.token == 292 || this.t.token == 293) {
            this.L.cachedLuaString(this.t.seminfo.ts);
        }
    }

    void assignment(LHS_assign lexState$LHS_assign0, int v, int v1) {
        expdesc lexState$expdesc0 = new expdesc();
        this.check_condition(7 <= lexState$LHS_assign0.v.k && lexState$LHS_assign0.v.k <= 9, "syntax error");
        if(this.testnext(44)) {
            LHS_assign lexState$LHS_assign1 = new LHS_assign();
            lexState$LHS_assign1.prev = lexState$LHS_assign0;
            this.suffixedexp(lexState$LHS_assign1.v);
            if(lexState$LHS_assign1.v.k != 9) {
                this.check_conflict(lexState$LHS_assign0, lexState$LHS_assign1.v);
            }
            this.assignment(lexState$LHS_assign1, v + 1, v1);
            lexState$expdesc0.init(6, this.fs.freereg - 1);
            this.fs.storevar(lexState$LHS_assign0.v, lexState$expdesc0);
            return;
        }
        int v2 = 21;
        switch(this.t.token) {
            case 61: {
                break;
            }
            case 285: {
                v2 = 18;
                break;
            }
            case 294: {
                v2 = 0;
                break;
            }
            case 295: {
                v2 = 1;
                break;
            }
            case 296: {
                v2 = 2;
                break;
            }
            case 297: {
                v2 = 3;
                break;
            }
            case 298: {
                v2 = 4;
                break;
            }
            case 299: {
                v2 = 5;
                break;
            }
            case 300: {
                v2 = 15;
                break;
            }
            case 301: {
                v2 = 16;
                break;
            }
            case 302: {
                v2 = 17;
                break;
            }
            case 303: {
                v2 = 6;
                break;
            }
            case 304: {
                v2 = 19;
                break;
            }
            case 305: {
                v2 = 20;
                break;
            }
            default: {
                this.syntaxerror("unexpected symbol assign");
            }
        }
        this.next();
        int v3 = this.explist(lexState$expdesc0);
        if(v3 != v) {
            this.adjust_assign(v, v3, lexState$expdesc0);
            if(v3 > v) {
                this.fs.freereg = (short)(this.fs.freereg - (v3 - v));
            }
            lexState$expdesc0.init(6, this.fs.freereg - 1);
            this.fs.storevar(lexState$LHS_assign0.v, lexState$expdesc0);
            return;
        }
        this.fs.setoneret(lexState$expdesc0);
        this.fs.storevar(lexState$LHS_assign0.v, lexState$expdesc0, v2);
    }

    void block() {
        FuncState fs = this.fs;
        fs.enterblock(new BlockCnt(), false);
        this.statlist();
        fs.leaveblock();
    }

    boolean block_follow(boolean z) {
        switch(this.t.token) {
            case 277: {
                return z;
            }
            case 0x7D: 
            case 260: 
            case 0x105: 
            case 0x106: 
            case 289: 
            case 307: 
            case 309: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    void body(expdesc e, boolean needself, int line) {
        FuncState new_fs = new FuncState();
        BlockCnt bl = new BlockCnt();
        new_fs.f = this.addprototype();
        new_fs.f.linedefined = line;
        this.open_func(new_fs, bl);
        this.checknext(40);
        if(needself) {
            this.new_localvarliteral("self");
            this.adjustlocalvars(1);
        }
        this.parlist();
        this.checknext(41);
        this.statlist();
        new_fs.f.lastlinedefined = this.linenumber;
        this.check_match(0x106, 0x109, line);
        this.codeclosure(e);
        this.close_func();
    }

    void breaklabel() {
        LuaString luaString0 = LuaString.valueOf("break");
        Dyndata lexState$Dyndata0 = this.dyd;
        Labeldesc[] arr_lexState$Labeldesc = LexState.grow(this.dyd.label, this.dyd.n_label + 1);
        lexState$Dyndata0.label = arr_lexState$Labeldesc;
        int v = this.dyd.n_label;
        this.dyd.n_label = v + 1;
        this.findgotos(this.dyd.label[this.newlabelentry(arr_lexState$Labeldesc, v, luaString0, 0, this.fs.pc)]);
    }

    void buffreplace(char from, char to) {
        int n = this.nbuff;
        char[] p = this.buff;
        while(true) {
            --n;
            if(n < 0) {
                break;
            }
            if(p[n] == from) {
                p[n] = to;
            }
        }
    }

    void check(int c) {
        if(this.t.token != c) {
            this.error_expected(c);
        }
    }

    void check_condition(boolean c, String msg) {
        if(!c) {
            this.syntaxerror(msg);
        }
    }

    void check_conflict(LHS_assign lh, expdesc v) {
        FuncState fs = this.fs;
        short extra = fs.freereg;
        boolean conflict = false;
        while(lh != null) {
            if(lh.v.k == 9) {
                if(lh.v.u.ind_vt == v.k && lh.v.u.ind_t == v.u.info) {
                    conflict = true;
                    lh.v.u.ind_vt = 7;
                    lh.v.u.ind_t = extra;
                }
                if(v.k == 7 && lh.v.u.ind_idx == v.u.info) {
                    conflict = true;
                    lh.v.u.ind_idx = extra;
                }
            }
            lh = lh.prev;
        }
        if(conflict) {
            fs.codeABC((v.k == 7 ? 0 : 5), ((int)extra), v.u.info, 0);
            fs.reserveregs(1);
        }
    }

    void check_match(int what, int who, int where) {
        if(!this.testnext(what)) {
            if(where == this.linenumber) {
                this.error_expected(what);
                return;
            }
            this.syntaxerror(this.L.pushfstring(LexState.LUA_QS(this.token2str(what)) + " expected " + "(to close " + LexState.LUA_QS(this.token2str(who)) + " at line " + where + ')'));
        }
    }

    boolean check_next(String set) {
        if(set.indexOf(this.current) < 0) {
            return false;
        }
        this.save_and_next();
        return true;
    }

    void checkname(expdesc e) {
        this.codestring(e, this.str_checkname());
    }

    void checknext(int c) {
        this.check(c);
        this.next();
    }

    void close_func() {
        boolean z = false;
        FuncState fs = this.fs;
        Prototype f = fs.f;
        fs.ret(0, 0);
        fs.leaveblock();
        f.code = LexState.realloc(f.code, fs.pc);
        f.lineinfo = LexState.realloc(f.lineinfo, fs.pc);
        f.k = LexState.realloc(f.k, fs.nk);
        f.p = LexState.realloc(f.p, fs.np);
        f.locvars = LexState.realloc(f.locvars, ((int)fs.nlocvars));
        f.upvalues = LexState.realloc(f.upvalues, ((int)fs.nups));
        if(fs.bl == null) {
            z = true;
        }
        LexState._assert(z);
        this.fs = fs.prev;
    }

    void closegoto(int g, Labeldesc label) {
        FuncState fs = this.fs;
        Labeldesc[] gl = this.dyd.gt;
        Labeldesc gt = gl[g];
        LexState._assert(gt.name.eq_b(label.name));
        if(gt.nactvar < label.nactvar) {
            LuaString vname = fs.getlocvar(((int)gt.nactvar)).varname;
            this.semerror(this.L.pushfstring("<goto " + gt.name + "> at line " + gt.line + " jumps into the scope of local \'" + vname.tojstring() + '\''));
        }
        fs.patchlist(gt.pc, label.pc);
        System.arraycopy(gl, g + 1, gl, g, this.dyd.n_gt - g - 1);
        int v1 = this.dyd.n_gt - 1;
        this.dyd.n_gt = v1;
        gl[v1] = null;
    }

    void codeclosure(expdesc v) {
        FuncState fs = this.fs.prev;
        v.init(11, fs.codeABx(37, 0, fs.np - 1));
        fs.exp2nextreg(v);
    }

    void codestring(expdesc e, LuaString s) {
        e.init(4, this.fs.stringK(s));
    }

    int cond() {
        expdesc v = new expdesc();
        this.expr(v);
        if(v.k == 1) {
            v.k = 3;
        }
        this.fs.goiftrue(v);
        return v.f.i;
    }

    void constructor(expdesc t) {
        FuncState fs = this.fs;
        int line = this.linenumber;
        int v1 = fs.codeABC(11, 0, 0, 0);
        ConsControl cc = new ConsControl();
        cc.tostore = 0;
        cc.nh = 0;
        cc.na = 0;
        cc.t = t;
        t.init(11, v1);
        cc.v.init(0, 0);
        fs.exp2nextreg(t);
        this.checknext(0x7B);
        do {
            LexState._assert(cc.v.k == 0 || cc.tostore > 0);
            if(this.t.token == 0x7D) {
                break;
            }
            fs.closelistfield(cc);
            switch(this.t.token) {
                case 91: {
                    this.recfield(cc);
                    break;
                }
                case 292: {
                    this.lookahead();
                    if(this.lookahead.token == 61) {
                        this.recfield(cc);
                    }
                    else {
                        this.listfield(cc);
                    }
                    break;
                }
                default: {
                    this.listfield(cc);
                }
            }
        }
        while(this.testnext(44) || this.testnext(59));
        this.check_match(0x7D, 0x7B, line);
        fs.lastlistfield(cc);
        InstructionPtr instructionPtr0 = new InstructionPtr(fs.f.code, v1);
        LexState.SETARG_B(instructionPtr0, LexState.luaO_int2fb(cc.na));
        LexState.SETARG_C(instructionPtr0, LexState.luaO_int2fb(cc.nh));
    }

    void continuelabel() {
        LuaString luaString0 = LuaString.valueOf("continue");
        Dyndata lexState$Dyndata0 = this.dyd;
        Labeldesc[] arr_lexState$Labeldesc = LexState.grow(this.dyd.label, this.dyd.n_label + 1);
        lexState$Dyndata0.label = arr_lexState$Labeldesc;
        int v = this.dyd.n_label;
        this.dyd.n_label = v + 1;
        this.findgotos(this.dyd.label[this.newlabelentry(arr_lexState$Labeldesc, v, luaString0, 0, this.fs.pc)]);
    }

    boolean currIsNewline() {
        return this.current == 10 || this.current == 13;
    }

    void enterlevel() {
        int v = this.L.nCcalls + 1;
        this.L.nCcalls = v;
        if(v > 200) {
            this.lexerror("chunk has too many syntax levels", 0);
        }
    }

    void error_expected(int token) {
        this.syntaxerror(this.L.pushfstring(LexState.LUA_QS(this.token2str(token)) + " expected"));
    }

    void escerror(int n, String msg) {
        this.save(92);
        for(int i = 0; i < n && this.ec[i] != -1; ++i) {
            this.save(this.ec[i]);
        }
        this.lexerror(msg, 293);
    }

    void escerrorutf8(int ch, int n, String msg) {
        this.ec[0] = 0x75;
        this.ec[1] = ch;
        this.escerror(n, msg);
    }

    void escerrorutf8(int n, String msg) {
        this.escerrorutf8(0x7B, n, msg);
    }

    int exp1() {
        expdesc e = new expdesc();
        this.expr(e);
        int k = e.k;
        this.fs.exp2nextreg(e);
        return k;
    }

    int explist(expdesc v) {
        int n = 1;
        this.expr(v);
        while(this.testnext(44)) {
            this.fs.exp2nextreg(v);
            this.expr(v);
            ++n;
        }
        return n;
    }

    void expr(expdesc v) {
        this.subexpr(v, 0);
    }

    void exprstat() {
        FuncState funcState0 = this.fs;
        LHS_assign lexState$LHS_assign0 = new LHS_assign();
        this.suffixedexp(lexState$LHS_assign0.v);
        if(this.t.token != 44 && this.t.token != 61 && (this.t.token < 294 || this.t.token > 305) && this.t.token != 285) {
            this.check_condition(lexState$LHS_assign0.v.k == 12, "syntax error");
            LexState.SETARG_C(funcState0.getcodePtr(lexState$LHS_assign0.v), 1);
            return;
        }
        lexState$LHS_assign0.prev = null;
        this.assignment(lexState$LHS_assign0, 1, 21);
    }

    void fieldsel(expdesc v) {
        FuncState fs = this.fs;
        expdesc key = new expdesc();
        fs.exp2anyregup(v);
        this.next();
        this.checkname(key);
        fs.indexed(v, key);
    }

    void findgotos(Labeldesc lb) {
        Labeldesc[] gl = this.dyd.gt;
        int i = this.fs.bl.firstgoto;
        while(i < this.dyd.n_gt) {
            if(gl[i].name.eq_b(lb.name)) {
                this.closegoto(i, lb);
            }
            else {
                ++i;
            }
        }
    }

    boolean findlabel(int g) {
        BlockCnt bl = this.fs.bl;
        Dyndata dyd = this.dyd;
        Labeldesc gt = dyd.gt[g];
        for(int i = bl.firstlabel; true; ++i) {
            if(i >= dyd.n_label) {
                return false;
            }
            Labeldesc lb = dyd.label[i];
            if(lb.name.eq_b(gt.name)) {
                if(gt.nactvar > lb.nactvar && (bl.upval || dyd.n_label > bl.firstlabel)) {
                    this.fs.patchclose(gt.pc, ((int)lb.nactvar));
                }
                this.closegoto(g, lb);
                return true;
            }
        }
    }

    void forbody(int base, int line, int nvars, boolean isnum) {
        int v4;
        BlockCnt bl = new BlockCnt();
        FuncState fs = this.fs;
        this.adjustlocalvars(3);
        this.testnext(0x103);
        int v3 = isnum ? fs.codeAsBx(33, base, -1) : fs.jump();
        fs.enterblock(bl, false);
        this.adjustlocalvars(nvars);
        fs.reserveregs(nvars);
        this.block();
        this.continuelabel();
        fs.leaveblock();
        fs.patchtohere(v3);
        if(isnum) {
            v4 = fs.codeAsBx(0x20, base, -1);
        }
        else {
            fs.codeABC(34, base, 0, nvars);
            fs.fixline(line);
            v4 = fs.codeAsBx(35, base + 2, -1);
        }
        fs.patchlist(v4, v3 + 1);
        fs.fixline(line);
    }

    void forlist(LuaString indexname) {
        FuncState fs = this.fs;
        expdesc e = new expdesc();
        int nvars = 4;
        int base = fs.freereg;
        this.new_localvarliteral("(for generator)");
        this.new_localvarliteral("(for state)");
        this.new_localvarliteral("(for control)");
        this.new_localvar(indexname);
        while(this.testnext(44)) {
            this.new_localvar(this.str_checkname());
            ++nvars;
        }
        this.checknext(0x10C);
        int line = this.linenumber;
        this.adjust_assign(3, this.explist(e), e);
        fs.checkstack(3);
        this.forbody(base, line, nvars - 3, false);
    }

    void fornum(LuaString varname, int line) {
        FuncState fs = this.fs;
        int base = fs.freereg;
        this.new_localvarliteral("(for index)");
        this.new_localvarliteral("(for limit)");
        this.new_localvarliteral("(for step)");
        this.new_localvar(varname);
        this.checknext(61);
        this.exp1();
        this.checknext(44);
        this.exp1();
        if(this.testnext(44)) {
            this.exp1();
        }
        else {
            fs.codeK(((int)fs.freereg), fs.numberK(LuaLong.valueOf(1L)));
            fs.reserveregs(1);
        }
        this.forbody(base, line, 1, true);
    }

    void forstat(int line) {
        FuncState fs = this.fs;
        fs.enterblock(new BlockCnt(), true);
        this.next();
        LuaString luaString0 = this.str_checkname();
        switch(this.t.token) {
            case 61: {
                this.fornum(luaString0, line);
                break;
            }
            case 44: 
            case 0x10C: {
                this.forlist(luaString0);
                break;
            }
            default: {
                this.syntaxerror("\'=\' or \'in\' expected");
            }
        }
        this.check_match(0x106, 0x108, line);
        fs.leaveblock();
    }

    void funcargs(expdesc f, int line) {
        int nparams;
        boolean z = false;
        FuncState fs = this.fs;
        expdesc args = new expdesc();
        switch(this.t.token) {
            case 40: {
                this.next();
                if(this.t.token == 41) {
                    args.k = 0;
                }
                else {
                    this.explist(args);
                    fs.setmultret(args);
                }
                this.check_match(41, 40, line);
                break;
            }
            case 0x7B: {
                this.constructor(args);
                break;
            }
            case 293: {
                this.codestring(args, this.t.seminfo.ts);
                this.next();
                break;
            }
            default: {
                this.syntaxerror("function arguments expected");
                return;
            }
        }
        if(f.k == 6) {
            z = true;
        }
        LexState._assert(z);
        int base = f.u.info;
        if(this.hasmultret(args.k)) {
            nparams = -1;
        }
        else {
            if(args.k != 0) {
                fs.exp2nextreg(args);
            }
            nparams = fs.freereg - (base + 1);
        }
        f.init(12, fs.codeABC(29, base, nparams + 1, 2));
        fs.fixline(line);
        fs.freereg = (short)(base + 1);
    }

    boolean funcname(expdesc v) {
        this.singlevar(v);
        while(true) {
            switch(this.t.token) {
                case 46: {
                    this.fieldsel(v);
                    break;
                }
                case 58: {
                    this.fieldsel(v);
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }

    void funcstat(int line) {
        expdesc v = new expdesc();
        expdesc b = new expdesc();
        this.next();
        this.body(b, this.funcname(v), line);
        this.fs.storevar(v, b);
        this.fs.fixline(line);
    }

    int getbinopr(int op) {
        switch(op) {
            case 37: {
                return 4;
            }
            case 38: {
                return 16;
            }
            case 42: {
                return 2;
            }
            case 43: {
                return 0;
            }
            case 45: {
                return 1;
            }
            case 0x2F: {
                return 3;
            }
            case 60: {
                return 9;
            }
            case 62: {
                return 11;
            }
            case 94: {
                return 5;
            }
            case 0x7C: {
                return 17;
            }
            case 0x7E: {
                return 18;
            }
            case 0x101: {
                return 13;
            }
            case 0x110: {
                return 14;
            }
            case 279: {
                return 15;
            }
            case 280: {
                return 6;
            }
            case 282: {
                return 8;
            }
            case 283: {
                return 12;
            }
            case 284: {
                return 10;
            }
            case 285: {
                return 7;
            }
            case 286: {
                return 19;
            }
            case 0x11F: {
                return 20;
            }
            default: {
                return 21;
            }
        }
    }

    void getfield(expdesc lexState$expdesc0) {
        FuncState funcState0 = this.fs;
        expdesc lexState$expdesc1 = new expdesc();
        funcState0.exp2anyregup(lexState$expdesc0);
        this.next();
        this.checkname(lexState$expdesc1);
        funcState0.field(lexState$expdesc0, lexState$expdesc1);
    }

    int getunopr(int op) {
        switch(op) {
            case 35: {
                return 2;
            }
            case 45: {
                return 0;
            }
            case 0x7E: {
                return 4;
            }
            case 0x10F: {
                return 1;
            }
            default: {
                return 3;
            }
        }
    }

    void gotostat(int v) {
        LuaString luaString0;
        int v1 = this.linenumber;
        if(this.testnext(0x10A)) {
            luaString0 = this.str_checkname();
        }
        else if(this.testnext(308)) {
            luaString0 = LuaString.valueOf("continue");
        }
        else {
            this.next();
            luaString0 = LuaString.valueOf("break");
        }
        Dyndata lexState$Dyndata0 = this.dyd;
        Labeldesc[] arr_lexState$Labeldesc = LexState.grow(lexState$Dyndata0.gt, this.dyd.n_gt + 1);
        lexState$Dyndata0.gt = arr_lexState$Labeldesc;
        int v2 = this.dyd.n_gt;
        this.dyd.n_gt = v2 + 1;
        this.findlabel(this.newlabelentry(arr_lexState$Labeldesc, v2, luaString0, v1, v));
    }

    boolean hasmultret(int k) {
        return k == 12 || k == 13;
    }

    public static int hexvalue(int c) {
        if(c <= 57) {
            return c - 0x30;
        }
        return c > 70 ? c - 87 : c - 55;
    }

    void ifstat(int line) {
        IntPtr intPtr0 = new IntPtr(-1);
        this.test_then_block(intPtr0);
        while(this.t.token == 0x105) {
            this.test_then_block(intPtr0);
        }
        if(this.testnext(260)) {
            this.block();
        }
        this.check_match(0x106, 0x10B, line);
        this.fs.patchtohere(intPtr0.i);
    }

    void inclinenumber() {
        int old = this.current;
        LexState._assert(this.currIsNewline());
        this.nextChar();
        if(this.currIsNewline() && this.current != old) {
            this.nextChar();
        }
        int v1 = this.linenumber + 1;
        this.linenumber = v1;
        if(v1 >= 0x7FFFFFFD) {
            this.syntaxerror("chunk has too many lines (> 2147483645)");
        }
    }

    public static boolean isReservedKeyword(String varName) {
        return LexState.RESERVED_LOCAL_VAR_KEYWORDS_TABLE.containsKey(varName);
    }

    // 去混淆评级： 低(30)
    public static boolean isalnum(int v) {
        return v >= 0x30 && v <= 57 || v >= 97 && v <= 0x7A || v >= 65 && v <= 90 || (v == 36 || v == 0x5F) || v >= 0x80;
    }

    // 去混淆评级： 低(20)
    public static boolean isalpha(int v) {
        return v >= 97 && v <= 0x7A || v >= 65 && v <= 90 || (v == 36 || v == 0x5F) || v >= 0x80;
    }

    private static boolean iscntrl(int token) {
        return token < 0x20 || token == 0xFF;
    }

    public static boolean isdigit(int c) {
        return c >= 0x30 && c <= 57;
    }

    public static boolean isspace(int c) {
        return c >= 0 && c <= 0x20;
    }

    // 去混淆评级： 低(30)
    public static boolean isxdigit(int c) {
        return c >= 0x30 && c <= 57 || c >= 97 && c <= 102 || c >= 65 && c <= 70;
    }

    void labelstat(LuaString label, int line) {
        this.fs.checkrepeated(this.dyd.label, this.dyd.n_label, label);
        this.checknext(0x120);
        Dyndata lexState$Dyndata0 = this.dyd;
        Labeldesc[] arr_lexState$Labeldesc = LexState.grow(this.dyd.label, this.dyd.n_label + 1);
        lexState$Dyndata0.label = arr_lexState$Labeldesc;
        int v1 = this.dyd.n_label;
        this.dyd.n_label = v1 + 1;
        int v2 = this.newlabelentry(arr_lexState$Labeldesc, v1, label, line, this.fs.getlabel());
        this.skipnoopstat();
        if(this.block_follow(false)) {
            this.dyd.label[v2].nactvar = this.fs.bl.nactvar;
        }
        this.findgotos(this.dyd.label[v2]);
    }

    void leavelevel() {
        --this.L.nCcalls;
    }

    void lexerror(String msg, int token) {
        String s1 = Lua.chunkid(this.source.tojstring());
        if(token != 0) {
            msg = msg + " near " + this.txtToken(token);
        }
        throw new LuaError(s1, this.linenumber, msg);
    }

    void listfield(ConsControl cc) {
        this.expr(cc.v);
        this.fs.checklimit(cc.na, 0x7FFFFFFD, "items in a constructor");
        ++cc.na;
        ++cc.tostore;
    }

    int llex(SemInfo lexState$SemInfo0) {
        this.nbuff = 0;
    alab1:
        while(true) {
            int v = this.current;
            switch(v) {
                case -1: {
                    return 289;
                }
                case 10: 
                case 13: {
                    this.inclinenumber();
                    break;
                }
                case 9: 
                case 11: 
                case 12: 
                case 0x20: {
                    this.nextChar();
                    break;
                }
                case 37: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 37;
                    }
                    this.nextChar();
                    return 298;
                }
                case 38: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 38;
                    }
                    this.nextChar();
                    return 301;
                }
                case 34: 
                case 39: {
                    this.read_string(v, lexState$SemInfo0);
                    return 293;
                }
                case 42: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 42;
                    }
                    this.nextChar();
                    return 296;
                }
                case 43: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 43;
                    }
                    this.nextChar();
                    return 294;
                }
                case 45: {
                    this.nextChar();
                    int v5 = this.current;
                    if(v5 == 61) {
                        this.nextChar();
                        return 295;
                    }
                    switch(v5) {
                        case 45: {
                            goto label_118;
                        }
                        case 62: {
                            this.nextChar();
                            return 310;
                        }
                    }
                    return 45;
                label_118:
                    this.nextChar();
                    if(this.current == 91) {
                        int v6 = this.skip_sep();
                        this.nbuff = 0;
                        if(v6 >= 0) {
                            this.read_long_string(null, v6);
                            this.nbuff = 0;
                            break;
                        }
                    }
                label_126:
                    if(this.currIsNewline() || this.current == -1) {
                        break;
                    }
                    break alab1;
                }
                case 46: {
                    this.save_and_next();
                    if(this.check_next(".")) {
                        if(this.check_next(".")) {
                            return 281;
                        }
                        return this.check_next("=") ? 303 : 280;
                    }
                    if(!LexState.isdigit(this.current)) {
                        return 46;
                    }
                    this.read_numeral(lexState$SemInfo0, false);
                    return 290;
                }
                case 0x2F: {
                    this.nextChar();
                    int v2 = this.current;
                    if(v2 == 61) {
                        this.nextChar();
                        return 297;
                    }
                    if(v2 == 0x2F) {
                        this.nextChar();
                        if(this.current != 61) {
                            return 279;
                        }
                        this.nextChar();
                        return 300;
                    }
                    return 0x2F;
                }
                case 0x30: 
                case 49: 
                case 50: 
                case 51: 
                case 52: 
                case 53: 
                case 54: 
                case 55: 
                case 56: 
                case 57: {
                    this.read_numeral(lexState$SemInfo0, true);
                    return 290;
                }
                case 58: {
                    this.nextChar();
                    if(this.current != 58) {
                        return 58;
                    }
                    this.nextChar();
                    return 0x120;
                }
                case 60: {
                    this.nextChar();
                    int v3 = this.current;
                    if(v3 == 60) {
                        this.nextChar();
                        if(this.current == 61) {
                            this.nextChar();
                            return 304;
                        }
                        return 286;
                    }
                    if(v3 == 61) {
                        this.nextChar();
                        return 284;
                    }
                    return 60;
                }
                case 61: {
                    goto label_136;
                }
                case 62: {
                    this.nextChar();
                    int v4 = this.current;
                    if(v4 == 62) {
                        this.nextChar();
                        if(this.current == 61) {
                            this.nextChar();
                            return 305;
                        }
                        return 0x11F;
                    }
                    if(v4 != 61) {
                        return 62;
                    }
                    this.nextChar();
                    return 283;
                }
                case 91: {
                    goto label_129;
                }
                case 94: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 94;
                    }
                    this.nextChar();
                    return 299;
                }
                case 0x7C: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 0x7C;
                    }
                    this.nextChar();
                    return 302;
                }
                case 0x7E: {
                    this.nextChar();
                    if(this.current != 61) {
                        return 0x7E;
                    }
                    this.nextChar();
                    return 285;
                }
                default: {
                    if(LexState.isalpha(v) || this.current == 0x5F) {
                        do {
                            this.read_name();
                        }
                        while(LexState.isalnum(this.current));
                        LuaString luaString0 = this.newstring(this.buff, 0, this.nbuff);
                        Hashtable hashtable0 = LexState.RESERVED;
                        if(hashtable0.containsKey(luaString0)) {
                            return (int)(((Integer)hashtable0.get(luaString0)));
                        }
                        lexState$SemInfo0.ts = luaString0;
                        return 292;
                    }
                    int v1 = this.current;
                    this.nextChar();
                    return v1;
                }
            }
        }
        this.nextChar();
        goto label_126;
    label_129:
        int v7 = this.skip_sep();
        if(v7 >= 0) {
            this.read_long_string(lexState$SemInfo0, v7);
            return 293;
        }
        if(v7 == -1) {
            return 91;
        }
        this.lexerror("invalid long string delimiter", 293);
    label_136:
        this.nextChar();
        if(this.current != 61) {
            return 61;
        }
        this.nextChar();
        return 282;
    }

    void localfunc() {
        expdesc b = new expdesc();
        FuncState fs = this.fs;
        this.new_localvar(this.str_checkname());
        this.adjustlocalvars(1);
        this.body(b, false, this.linenumber);
        LocVars locVars0 = fs.getlocvar(fs.nactvar - 1);
        locVars0.startpc = fs.pc;
    }

    void localstat() {
        int nexps;
        int nvars = 0;
        expdesc e = new expdesc();
        do {
            this.new_localvar(this.str_checkname());
            ++nvars;
        }
        while(this.testnext(44));
        if(this.testnext(61)) {
            nexps = this.explist(e);
        }
        else {
            e.k = 0;
            nexps = 0;
        }
        this.adjust_assign(nvars, nexps, e);
        this.adjustlocalvars(nvars);
    }

    void lookahead() {
        LexState._assert(this.lookahead.token == 289);
        this.lookahead.token = this.llex(this.lookahead.seminfo);
    }

    static int luaO_int2fb(int x) {
        int e;
        for(e = 0; x >= 16; ++e) {
            x = x + 1 >> 1;
        }
        return x >= 8 ? e + 1 << 3 | x - 8 : x;
    }

    @Override  // luaj.Lua
    public static void main(String[] args) throws Throwable {
    }

    public void mainfunc(FuncState funcstate) {
        this.open_func(funcstate, new BlockCnt());
        this.fs.f.is_vararg = 1;
        expdesc v = new expdesc();
        v.init(7, 0);
        this.fs.newupvalue(this.envn, v);
        this.next();
        this.statlist();
        this.check(289);
        this.close_func();
    }

    void new_localvar(LuaString name) {
        int v = this.registerlocalvar(name);
        this.fs.checklimit(this.dyd.n_actvar + 1, 200, "local variables");
        if(this.dyd.actvar == null || this.dyd.n_actvar + 1 > this.dyd.actvar.length) {
            Dyndata lexState$Dyndata0 = this.dyd;
            lexState$Dyndata0.actvar = LexState.realloc(this.dyd.actvar, Math.max(1, this.dyd.n_actvar * 2));
        }
        Vardesc[] arr_lexState$Vardesc = this.dyd.actvar;
        int v1 = this.dyd.n_actvar;
        this.dyd.n_actvar = v1 + 1;
        arr_lexState$Vardesc[v1] = new Vardesc(v);
    }

    void new_localvarliteral(String v) {
        this.new_localvar(this.newstring(v));
    }

    int newlabelentry(Labeldesc[] l, int index, LuaString name, int line, int pc) {
        l[index] = new Labeldesc(name, pc, line, this.fs.nactvar);
        return index;
    }

    LuaString newstring(String s) {
        return this.L.newTString(s);
    }

    LuaString newstring(char[] arr_c, int v, int v1) {
        char[] arr_c1 = new String(arr_c, v, v1).toCharArray();
        byte[] arr_b = new byte[arr_c1.length];
        for(int v2 = 0; v2 < arr_c1.length; ++v2) {
            arr_b[v2] = (byte)arr_c1[v2];
        }
        return this.L.newTString(new String(arr_b));
    }

    void next() {
        this.lastline = this.linenumber;
        if(this.lookahead.token != 289) {
            this.t.set(this.lookahead);
            this.lookahead.token = 289;
            return;
        }
        this.t.token = this.llex(this.t.seminfo);
    }

    void nextChar() {
        try {
            this.current = this.z.read();
        }
        catch(IOException e) {
            e.printStackTrace();
            this.current = -1;
        }
    }

    void one(expdesc lexState$expdesc0) {
        FuncState funcState0 = this.fs;
        int v = funcState0.codeABC(11, 0, 0, 0);
        ConsControl lexState$ConsControl0 = new ConsControl();
        lexState$ConsControl0.na = 0;
        lexState$ConsControl0.nh = 0;
        lexState$ConsControl0.tostore = 0;
        lexState$ConsControl0.t = lexState$expdesc0;
        lexState$expdesc0.init(11, v);
        lexState$ConsControl0.v.init(0, 0);
        funcState0.exp2nextreg(lexState$expdesc0);
        this.testnext(0x3F);
        this.testnext(0x40);
        do {
            LexState._assert(lexState$ConsControl0.v.k == 0 || lexState$ConsControl0.tostore > 0);
            if(this.t.token == 0x40) {
                break;
            }
            funcState0.closelistfield(lexState$ConsControl0);
            this.listfield(lexState$ConsControl0);
        }
        while(this.testnext(44) || this.testnext(59));
        funcState0.lastlistfield(lexState$ConsControl0);
        InstructionPtr instructionPtr0 = new InstructionPtr(funcState0.f.code, v);
        LexState.SETARG_B(instructionPtr0, LexState.luaO_int2fb(lexState$ConsControl0.na));
        LexState.SETARG_C(instructionPtr0, LexState.luaO_int2fb(lexState$ConsControl0.nh));
    }

    void open_func(FuncState fs, BlockCnt bl) {
        fs.prev = this.fs;
        fs.ls = this;
        this.fs = fs;
        fs.pc = 0;
        fs.lasttarget = -1;
        fs.jpc = new IntPtr(-1);
        fs.freereg = 0;
        fs.nk = 0;
        fs.np = 0;
        fs.nups = 0;
        fs.nlocvars = 0;
        fs.nactvar = 0;
        fs.firstlocal = this.dyd.n_actvar;
        fs.bl = null;
        fs.f.source = this.source;
        fs.f.maxstacksize = 2;
        fs.enterblock(bl, false);
    }

    void parlist() {
        FuncState fs = this.fs;
        Prototype f = fs.f;
        int nparams = 0;
        f.is_vararg = 0;
        if(this.t.token != 41) {
            while(true) {
                switch(this.t.token) {
                    case 281: {
                        this.next();
                        f.is_vararg = 1;
                        break;
                    }
                    case 292: {
                        this.new_localvar(this.str_checkname());
                        ++nparams;
                        break;
                    }
                    default: {
                        this.syntaxerror("<name> or \'...\' expected");
                    }
                }
                if(f.is_vararg != 0 || !this.testnext(44)) {
                    break;
                }
            }
        }
        this.adjustlocalvars(nparams);
        f.numparams = fs.nactvar;
        fs.reserveregs(((int)fs.nactvar));
    }

    void primaryexp(expdesc v) {
        switch(this.t.token) {
            case 40: {
                int line = this.linenumber;
                this.next();
                this.expr(v);
                this.check_match(41, 40, line);
                this.fs.dischargevars(v);
                return;
            }
            case 292: {
                this.singlevar(v);
                return;
            }
            default: {
                this.syntaxerror("unexpected symbol");
            }
        }
    }

    void read_long_string(SemInfo seminfo, int sep) {
        int cont = 0;
        this.save_and_next();
        if(this.currIsNewline()) {
            this.inclinenumber();
        }
        boolean endloop = false;
        while(!endloop) {
            switch(this.current) {
                case -1: {
                    this.lexerror((seminfo == null ? "unfinished long comment" : "unfinished long string"), 289);
                    continue;
                }
                case 10: 
                case 13: {
                    this.save(10);
                    this.inclinenumber();
                    if(seminfo != null) {
                        continue;
                    }
                    this.nbuff = 0;
                    continue;
                }
                case 91: {
                    if(this.skip_sep() != sep) {
                        continue;
                    }
                    this.save_and_next();
                    ++cont;
                    if(sep != 0) {
                        continue;
                    }
                    this.lexerror("nesting of [[...]] is deprecated", 91);
                    continue;
                }
                case 93: {
                    if(this.skip_sep() == sep) {
                        break;
                    }
                    continue;
                }
                default: {
                    if(seminfo == null) {
                        this.nextChar();
                    }
                    else {
                        this.save_and_next();
                    }
                    continue;
                }
            }
            this.save_and_next();
            endloop = true;
        }
        if(seminfo != null) {
            seminfo.ts = this.L.newTString(LuaString.valueOf(this.buff, sep + 2, this.nbuff - (sep + 2) * 2));
        }
    }

    void read_name() {
        int v = -1;
        switch(this.current) {
            case 0xC0: {
                v = 0;
                break;
            }
            case 0xE0: {
                v = 1;
                break;
            }
            case 0xF0: {
                v = 2;
                break;
            }
            case 0xF8: {
                v = 3;
                break;
            }
            case 0xFC: {
                v = 4;
            }
        }
        this.save_and_next();
        if(v != -1) {
            for(int v1 = 1; true; ++v1) {
                byte[] arr_b = this.bytes[v];
                if(v1 >= arr_b.length || this.current < arr_b[v1]) {
                    break;
                }
                this.save_and_next();
            }
        }
    }

    void read_numeral(SemInfo lexState$SemInfo0, boolean z) {
        LuaValue luaValue0;
        int v = 0;
        int v1 = this.current;
        this.save_and_next();
        boolean z1 = v1 == 0x30 && this.check_next("Xx");
        boolean z2 = v1 == 0x30 && this.check_next("Bb");
        while(true) {
            if(this.check_next((z1 ? "Pp" : "Ee"))) {
                this.check_next("+-");
                z = false;
            }
            int v2 = this.current;
            if(z2 && v2 >= 0x30 && v2 <= 49 || v2 >= 0x30 && v2 <= 57 || z1 && (v2 >= 97 && v2 <= 102 || v2 >= 65 && v2 <= 70)) {
                this.save_and_next();
            }
            else {
                if(v2 != 46) {
                    break;
                }
                this.save_and_next();
                z = false;
            }
        }
        if(!z1 && z && this.nbuff > 19) {
            z = true;
        }
        try {
            if(z1) {
                this.charBuffer.buff = this.buff;
                this.charBuffer.nbuff = this.nbuff;
                luaValue0 = LuaValue.parseHex(this.charBuffer);
            }
            else if(z2) {
                this.charBuffer.buff = this.buff;
                this.charBuffer.nbuff = this.nbuff;
                luaValue0 = LuaValue.parseBinary(this.charBuffer);
            }
            else if(z) {
                long v3 = 0L;
                char[] arr_c = this.buff;
                int v4 = this.nbuff;
                if(v4 > 19) {
                    LuaBigNumber.bignumberOf(new String(arr_c, 0, v4));
                    return;
                }
                if(v4 < 19) {
                    while(true) {
                        if(v >= v4) {
                            luaValue0 = LuaLong.valueOf(v3);
                            break;
                        }
                        v3 = v3 * 10L + ((long)(arr_c[v] - 0x30));
                        ++v;
                    }
                }
                else {
                    while(true) {
                        if(v >= 18) {
                            long v5 = 10L * v3 + ((long)(arr_c[18] - 0x30));
                            if(v5 < v3) {
                                luaValue0 = LuaBigNumber.bignumberOf(new String(arr_c, 0, v4));
                                break;
                            }
                            luaValue0 = LuaLong.valueOf(v5);
                            break;
                        }
                        v3 = v3 * 10L + ((long)(arr_c[v] - 0x30));
                        ++v;
                    }
                }
            }
            else {
                luaValue0 = LuaDouble.valueOf(Double.parseDouble(new String(this.buff, 0, this.nbuff)));
            }
            lexState$SemInfo0.r = luaValue0;
        }
        catch(NumberFormatException numberFormatException0) {
            this.lexerror("malformed number (" + numberFormatException0.getMessage() + ")", 290);
        }
    }

    void read_string(int del, SemInfo seminfo) {
        int c;
        this.save_and_next();
        while(this.current != del) {
        alab1:
            switch(this.current) {
                case -1: {
                    this.lexerror("unfinished string", 289);
                    continue;
                }
                case 10: 
                case 13: {
                    this.lexerror("unfinished string", 293);
                    continue;
                }
                case 92: {
                    this.nextChar();
                    switch(this.current) {
                        case -1: {
                            continue;
                        }
                        case 10: 
                        case 13: {
                            this.save(10);
                            this.inclinenumber();
                            continue;
                        }
                        case 34: 
                        case 39: 
                        case 92: {
                            this.save_and_next();
                            continue;
                        }
                        case 97: {
                            c = 7;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 98: {
                            c = 8;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 102: {
                            c = 12;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 110: {
                            c = 10;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 0x72: {
                            c = 13;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 0x74: {
                            c = 9;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 0x75: {
                            this.readutf8esc();
                            continue;
                        }
                        case 0x76: {
                            c = 11;
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 120: {
                            c = this.readhexaesc();
                            this.save(c);
                            this.nextChar();
                            continue;
                        }
                        case 0x7A: {
                            break;
                        }
                        default: {
                            if(LexState.isdigit(this.current)) {
                                this.save(this.readdecesc());
                            }
                            else {
                                this.ec[0] = this.current;
                                this.escerror(1, "invalid escape sequence");
                            }
                            continue;
                        }
                    }
                    this.nextChar();
                    while(true) {
                        if(!LexState.isspace(this.current)) {
                            break alab1;
                        }
                        if(this.currIsNewline()) {
                            this.inclinenumber();
                        }
                        else {
                            this.nextChar();
                        }
                    }
                }
                default: {
                    this.save_and_next();
                }
            }
        }
        this.save_and_next();
        seminfo.ts = this.L.newTString(LuaString.valueOf(this.buff, 1, this.nbuff - 2));
    }

    int readdecesc() {
        int r = 0;
        int i;
        for(i = 0; i < 3 && LexState.isdigit(this.current); ++i) {
            int v2 = this.current;
            this.ec[i] = v2;
            r = r * 10 + v2 - 0x30;
            this.nextChar();
        }
        if(r > 0xFF) {
            this.ec[i] = this.current;
            this.escerror(i + 1, "decimal escape too large");
        }
        return r;
    }

    int readhexaesc() {
        int r = 0;
        for(int i = 1; i < 3; ++i) {
            this.nextChar();
            int v2 = this.current;
            this.ec[i] = v2;
            if(!LexState.isxdigit(v2)) {
                this.ec[0] = 120;
                this.escerror(i + 1, "hexadecimal digit expected");
            }
            r = (r << 4) + LexState.hexvalue(v2);
        }
        return r;
    }

    void readutf8esc() {
        int v3;
        this.nextChar();
        int ch = this.current;
        if(ch != 0x7B) {
            this.escerrorutf8(ch, 2, "missing \'{\', got \'" + ((char)ch) + "\'");
        }
        int num = 0;
        int cnt;
        for(cnt = 2; true; ++cnt) {
            this.nextChar();
            if(cnt == 23) {
                this.escerrorutf8(23, "unicode escape too long");
            }
            v3 = this.current;
            this.ec[cnt] = v3;
            if(!LexState.isxdigit(v3)) {
                break;
            }
            num = (num << 4) + LexState.hexvalue(v3);
            if(num > 0x10FFFF) {
                this.escerrorutf8(cnt + 1, "UTF-8 value too large: 0x" + Integer.toHexString(num) + " (max allowed = 0x" + "10ffff" + ")");
            }
        }
        if(cnt == 2) {
            this.escerrorutf8(3, "hexadecimal digit expected, got \'" + ((char)v3) + "\'");
        }
        if(v3 != 0x7D) {
            this.escerrorutf8(cnt + 1, "missing \'}\', got \'" + ((char)v3) + "\'");
        }
        byte[] arr_b = new String(Character.toChars(num)).getBytes();
        for(int v4 = 0; v4 < arr_b.length; ++v4) {
            this.save(((int)arr_b[v4]));
        }
        this.nextChar();
    }

    void recfield(ConsControl cc) {
        FuncState fs = this.fs;
        int reg = this.fs.freereg;
        expdesc key = new expdesc();
        expdesc val = new expdesc();
        if(this.t.token == 292) {
            fs.checklimit(cc.nh, 0x7FFFFFFD, "items in a constructor");
            this.checkname(key);
        }
        else {
            this.yindex(key);
        }
        ++cc.nh;
        this.checknext(61);
        int v1 = fs.exp2RK(key);
        this.expr(val);
        fs.codeABC(10, cc.t.u.info, v1, fs.exp2RK(val));
        fs.freereg = (short)reg;
    }

    int registerlocalvar(LuaString varname) {
        FuncState fs = this.fs;
        Prototype f = fs.f;
        if(f.locvars == null || fs.nlocvars + 1 > f.locvars.length) {
            f.locvars = LexState.realloc(f.locvars, fs.nlocvars * 2 + 1);
        }
        LocVars[] arr_locVars = f.locvars;
        int v = fs.nlocvars;
        arr_locVars[v] = new LocVars(varname, 0, 0);
        int v1 = fs.nlocvars;
        fs.nlocvars = (short)(v1 + 1);
        return v1;
    }

    void removevars(int tolevel) {
        FuncState fs = this.fs;
        while(fs.nactvar > tolevel) {
            short v1 = (short)(fs.nactvar - 1);
            fs.nactvar = v1;
            LocVars locVars0 = fs.getlocvar(((int)v1));
            locVars0.endpc = fs.pc;
        }
    }

    void repeatstat(int line) {
        FuncState fs = this.fs;
        int v1 = fs.getlabel();
        BlockCnt bl1 = new BlockCnt();
        BlockCnt bl2 = new BlockCnt();
        fs.enterblock(bl1, true);
        fs.enterblock(bl2, false);
        this.next();
        this.statlist();
        this.continuelabel();
        this.check_match(277, 273, line);
        int v2 = this.cond();
        if(bl2.upval) {
            fs.patchclose(v2, ((int)bl2.nactvar));
        }
        fs.leaveblock();
        fs.patchlist(v2, v1);
        fs.leaveblock();
    }

    void retstat() {
        int first;
        int nret;
        boolean z = true;
        FuncState fs = this.fs;
        expdesc e = new expdesc();
        if(!this.block_follow(true) && this.t.token != 59) {
            nret = this.explist(e);
            if(this.hasmultret(e.k)) {
                fs.setmultret(e);
                if(e.k == 12 && nret == 1) {
                    LexState.SET_OPCODE(fs.getcodePtr(e), 30);
                    if(Lua.GETARG_A(fs.getcode(e)) != fs.nactvar) {
                        z = false;
                    }
                    LexState._assert(z);
                }
                first = fs.nactvar;
                nret = -1;
            }
            else if(nret == 1) {
                first = fs.exp2anyreg(e);
            }
            else {
                fs.exp2nextreg(e);
                first = fs.nactvar;
                if(nret != fs.freereg - first) {
                    z = false;
                }
                LexState._assert(z);
            }
        }
        else {
            nret = 0;
            first = 0;
        }
        fs.ret(first, nret);
        this.testnext(59);
    }

    void save(int c) {
        char[] buff = this.buff;
        int nbuff = this.nbuff;
        if(buff == null || nbuff + 1 > buff.length) {
            buff = LexState.realloc(buff, nbuff * 2 + 1);
            this.buff = buff;
        }
        buff[nbuff] = (char)c;
        this.nbuff = nbuff + 1;
    }

    void save_and_next() {
        this.save(this.current);
        this.nextChar();
    }

    void semerror(String msg) {
        this.t.token = 0;
        this.syntaxerror(msg);
    }

    void setinput(CompileState L, int firstByte, InputStream z, LuaString source) {
        this.decpoint = 46;
        this.L = L;
        this.lookahead.token = 289;
        this.z = z;
        this.fs = null;
        this.linenumber = 1;
        this.lastline = 1;
        this.source = source;
        this.envn = LuaValue.ENV;
        this.nbuff = 0;
        this.current = firstByte;
        this.skipShebang();
    }

    void simpleexp(expdesc v) {
        switch(this.t.token) {
            case 0x7B: {
                this.constructor(v);
                return;
            }
            case 0x107: {
                v.init(3, 0);
                break;
            }
            case 0x109: {
                this.next();
                this.body(v, false, this.linenumber);
                return;
            }
            case 270: {
                v.init(1, 0);
                break;
            }
            case 276: {
                v.init(2, 0);
                break;
            }
            case 281: {
                FuncState fs = this.fs;
                this.check_condition(fs.f.is_vararg != 0, "cannot use \'...\' outside a vararg function");
                v.init(13, fs.codeABC(38, 0, 1, 0));
                break;
            }
            case 290: {
                v.init(5, 0);
                v.u.setNval(this.t.seminfo.r);
                break;
            }
            case 293: {
                this.codestring(v, this.t.seminfo.ts);
                break;
            }
            default: {
                this.suffixedexp(v);
                return;
            }
        }
        this.next();
    }

    void singlevar(expdesc var) {
        boolean z = true;
        LuaString luaString0 = this.str_checkname();
        FuncState fs = this.fs;
        if(FuncState.singlevaraux(fs, luaString0, var, 1) == 0) {
            expdesc key = new expdesc();
            FuncState.singlevaraux(fs, this.envn, var, 1);
            if(var.k != 7 && var.k != 8) {
                z = false;
            }
            LexState._assert(z);
            this.codestring(key, luaString0);
            fs.indexed(var, key);
        }
    }

    private void skipShebang() {
        if(this.current == 35) {
            while(!this.currIsNewline() && this.current != -1) {
                this.nextChar();
            }
        }
    }

    int skip_sep() {
        int count = 0;
        int s = this.current;
        this.save_and_next();
        while(this.current == 61) {
            this.save_and_next();
            ++count;
        }
        return this.current == s ? count : -count - 1;
    }

    void skipnoopstat() {
        while(this.t.token == 59 || this.t.token == 0x120) {
            this.statement();
        }
    }

    void statement() {
        int v = this.linenumber;
        this.enterlevel();
        switch(this.t.token) {
            case 59: {
                this.next();
                break;
            }
            case 0x103: {
                this.next();
                this.block();
                this.check_match(0x106, 0x103, v);
                break;
            }
            case 0x108: {
                this.forstat(v);
                break;
            }
            case 0x109: {
                this.funcstat(v);
                break;
            }
            case 0x10A: {
                this.gotostat(this.fs.jump());
                break;
            }
            case 0x10B: {
                this.ifstat(v);
                break;
            }
            case 0x10D: {
                this.next();
                if(this.testnext(0x109)) {
                    this.localfunc();
                }
                else {
                    this.localstat();
                }
                break;
            }
            case 273: {
                this.repeatstat(v);
                break;
            }
            case 274: {
                this.next();
                this.retstat();
                break;
            }
            case 278: {
                this.whilestat(v);
                break;
            }
            case 0x120: {
                this.next();
                this.labelstat(this.str_checkname(), v);
                break;
            }
            case 306: {
                this.switchstat(v);
                break;
            }
            case 0x102: 
            case 308: {
                this.gotostat(this.fs.jump());
                break;
            }
            default: {
                this.exprstat();
            }
        }
        if(this.fs.f.maxstacksize < this.fs.freereg || this.fs.freereg < this.fs.nactvar) {
            this.syntaxerror("statement");
        }
        this.fs.freereg = this.fs.nactvar;
        this.leavelevel();
    }

    void statlist() {
        while(true) {
            if(this.block_follow(true)) {
                return;
            }
            if(this.t.token == 274) {
                this.statement();
                return;
            }
            this.statement();
        }
    }

    LuaString str_checkname() {
        this.check(292);
        LuaString ts = this.t.seminfo.ts;
        this.next();
        return ts;
    }

    int subexpr(expdesc lexState$expdesc0, int v) {
        this.enterlevel();
        int v1 = this.getunopr(this.t.token);
        if(v1 == 3) {
            this.simpleexp(lexState$expdesc0);
        }
        else {
            int v2 = this.linenumber;
            this.next();
            this.subexpr(lexState$expdesc0, 100);
            this.fs.prefix(v1, lexState$expdesc0, v2);
        }
        int v3;
        for(v3 = this.getbinopr(this.t.token); v3 != 21 && LexState.priority[v3].left > v; v3 = v5) {
            expdesc lexState$expdesc1 = new expdesc();
            int v4 = this.linenumber;
            this.next();
            this.fs.infix(v3, lexState$expdesc0);
            int v5 = this.subexpr(lexState$expdesc1, ((int)LexState.priority[v3].right));
            this.fs.posfix(v3, lexState$expdesc0, lexState$expdesc1, v4);
        }
        this.leavelevel();
        if(this.t.token == 0x3F) {
            this.teopr(lexState$expdesc0);
        }
        return v3;
    }

    void suffixedexp(expdesc v) {
        int line = this.linenumber;
        this.primaryexp(v);
    alab1:
        while(true) {
            switch(this.t.token) {
                case 46: {
                    this.fieldsel(v);
                    break;
                }
                case 58: {
                    expdesc key = new expdesc();
                    this.next();
                    this.checkname(key);
                    this.fs.self(v, key);
                    this.funcargs(v, line);
                    break;
                }
                case 91: {
                    expdesc key = new expdesc();
                    this.fs.exp2anyregup(v);
                    this.yindex(key);
                    this.fs.indexed(v, key);
                    break;
                }
                case 40: 
                case 0x7B: 
                case 293: {
                    this.fs.exp2nextreg(v);
                    this.funcargs(v, line);
                    break;
                }
                case 310: {
                    this.getfield(v);
                    break;
                }
                default: {
                    break alab1;
                }
            }
        }
    }

    void switch_read_var(expdesc lexState$expdesc0) {
        this.enterlevel();
        this.subexpr(lexState$expdesc0, 0);
        this.leavelevel();
    }

    void switchstat(int v) {
        this.next();
        boolean z = this.testnext(40);
        expdesc lexState$expdesc0 = new expdesc();
        this.switch_read_var(lexState$expdesc0);
        if(z) {
            this.check(41);
            this.next();
            this.testnext(0x103);
            this.testnext(275);
            this.testnext(58);
            z = this.testnext(0x7B);
        }
        IntPtr intPtr0 = new IntPtr(-1);
        while(this.t.token == 307) {
            this.test_case_block(intPtr0, lexState$expdesc0.clone2());
        }
        if(this.testnext(309)) {
            this.testnext(58);
            boolean z1 = this.testnext(0x7B);
            this.block();
            if(z1) {
                this.check(0x7D);
                this.next();
            }
        }
        if(z) {
            this.check_match(0x7D, 306, v);
        }
        else {
            this.check_match(0x106, 306, v);
        }
        this.fs.patchtohere(intPtr0.i);
    }

    void syntaxerror(String msg) {
        this.lexerror(msg, this.t.token);
    }

    void teopr(expdesc lexState$expdesc0) {
        expdesc lexState$expdesc1 = new expdesc();
        int v = this.linenumber;
        this.fs.infix(13, lexState$expdesc0);
        this.one(lexState$expdesc1);
        this.fs.posfix(13, lexState$expdesc0, lexState$expdesc1, v);
        if(this.t.token == 0x40) {
            expdesc lexState$expdesc2 = new expdesc();
            int v1 = this.linenumber;
            this.fs.infix(14, lexState$expdesc0);
            this.one(lexState$expdesc2);
            this.fs.posfix(14, lexState$expdesc0, lexState$expdesc2, v1);
        }
        expdesc lexState$expdesc3 = new expdesc();
        this.fs.exp2anyregup(lexState$expdesc0);
        lexState$expdesc3.init(5, 0);
        LuaLong luaLong0 = LuaValue.valueOf(1L);
        lexState$expdesc3.u.setNval(luaLong0);
        this.fs.exp2val(lexState$expdesc3);
        this.fs.indexed(lexState$expdesc0, lexState$expdesc3);
        this.fs.dischargevars(lexState$expdesc0);
    }

    void test_case_block(IntPtr intPtr0, expdesc lexState$expdesc0) {
        expdesc lexState$expdesc1 = new expdesc();
        BlockCnt funcState$BlockCnt0 = new BlockCnt();
        this.next();
        expdesc lexState$expdesc2 = lexState$expdesc0.clone2();
        this.enterlevel();
        this.fs.infix(8, lexState$expdesc0);
        this.expr(lexState$expdesc1);
        this.fs.posfix(8, lexState$expdesc0, lexState$expdesc1, this.linenumber);
        while(this.testnext(44) || this.testnext(307)) {
            expdesc lexState$expdesc3 = lexState$expdesc2.clone2();
            expdesc lexState$expdesc4 = new expdesc();
            this.fs.infix(8, lexState$expdesc3);
            this.expr(lexState$expdesc4);
            this.fs.posfix(8, lexState$expdesc3, lexState$expdesc4, this.linenumber);
            this.fs.infix(14, lexState$expdesc0);
            this.fs.posfix(14, lexState$expdesc0, lexState$expdesc3, this.linenumber);
            this.testnext(58);
        }
        this.testnext(58);
        boolean z = this.testnext(0x7B);
        this.leavelevel();
        this.fs.goiftrue(lexState$expdesc0);
        this.fs.enterblock(funcState$BlockCnt0, false);
        int v = lexState$expdesc0.f.i;
        this.statlist();
        this.fs.leaveblock();
        if(this.t.token == 307 || this.t.token == 309) {
            this.fs.concat(intPtr0, this.fs.jump());
        }
        this.fs.patchtohere(v);
        if(z) {
            this.check(0x7D);
            this.next();
        }
    }

    void test_then_block(IntPtr intPtr0) {
        int v;
        expdesc lexState$expdesc0 = new expdesc();
        BlockCnt funcState$BlockCnt0 = new BlockCnt();
        this.next();
        this.expr(lexState$expdesc0);
        this.testnext(275);
        if(this.t.token == 0x102 || this.t.token == 0x10A || this.t.token == 308) {
            this.fs.goiffalse(lexState$expdesc0);
            this.fs.enterblock(funcState$BlockCnt0, false);
            this.gotostat(lexState$expdesc0.t.i);
            this.skipnoopstat();
            if(this.block_follow(false)) {
                this.fs.leaveblock();
                return;
            }
            v = this.fs.jump();
        }
        else {
            this.fs.goiftrue(lexState$expdesc0);
            this.fs.enterblock(funcState$BlockCnt0, false);
            v = lexState$expdesc0.f.i;
        }
        this.statlist();
        this.fs.leaveblock();
        if(this.t.token == 260 || this.t.token == 0x105) {
            this.fs.concat(intPtr0, this.fs.jump());
        }
        this.fs.patchtohere(v);
    }

    boolean testnext(int c) {
        if(this.t.token == c) {
            this.next();
            return true;
        }
        return false;
    }

    String token2str(int token) {
        if(token < 0x101) {
            return LexState.iscntrl(token) ? "\'<\\" + token + ">\' (symbol with decimal code " + token + " or 0x" + Integer.toHexString(token) + ")" : String.valueOf(((char)token));
        }
        String ret = LexState.luaX_tokens[token - 0x101];
        return token == 289 || token == 290 || token == 291 || token == 292 || token == 293 ? ret : "\'" + ret + '\'';
    }

    String txtToken(int token) {
        return token == 290 || token == 291 || token == 292 || token == 293 ? "\'" + new String(this.buff, 0, this.nbuff) + '\'' : this.token2str(token);
    }

    void undefgoto(Labeldesc gt, FuncState fs, BlockCnt bl) {
        this.linenumber = gt.line;
        String s = gt.name.tojstring();
        boolean z = LexState.isReservedKeyword(s);
        String msg = z ? "<" + gt.name + "> at line " + gt.line + " not inside a loop" : "no visible label \'" + gt.name + "\' for <goto> at line " + gt.line;
        if(!z) {
            Dyndata dyd = this.dyd;
            for(int i = bl.firstlabel; i < dyd.n_label; ++i) {
                Labeldesc lb = dyd.label[i];
                if(lb != null && lb.name != null) {
                    String s2 = lb.name.tojstring();
                    if(s2.equalsIgnoreCase(s)) {
                        msg = msg + "\ndid you mean label \'" + s2 + "\' at line " + lb.line + '?';
                    }
                }
            }
        }
        this.semerror(msg);
    }

    static final boolean vkisinreg(int k) {
        return k == 6 || k == 7;
    }

    static final boolean vkisvar(int k) {
        return 7 <= k && k <= 9;
    }

    void whilestat(int line) {
        FuncState fs = this.fs;
        BlockCnt bl = new BlockCnt();
        this.next();
        int v1 = fs.getlabel();
        int v2 = this.cond();
        fs.enterblock(bl, true);
        this.testnext(0x103);
        this.block();
        this.continuelabel();
        fs.patchlist(fs.jump(), v1);
        this.check_match(0x106, 278, line);
        fs.leaveblock();
        fs.patchtohere(v2);
    }

    void yindex(expdesc v) {
        this.next();
        this.expr(v);
        this.fs.exp2val(v);
        this.checknext(93);
    }
}

