package luaj;

import java.math.BigDecimal;
import luaj.compiler.LexState;

public abstract class LuaValue extends Varargs {
    static final class None extends LuaNil {
        private None() {
        }

        None(None luaValue$None0) {
        }

        @Override  // luaj.LuaValue
        public LuaValue arg(int i) {
            return None.NIL;
        }

        @Override  // luaj.LuaValue
        public LuaValue arg1() {
            return None.NIL;
        }

        @Override  // luaj.Varargs
        void copyto(LuaValue[] dest, int offset, int length) {
            while(length > 0) {
                dest[offset] = None.NIL;
                --length;
                ++offset;
            }
        }

        @Override  // luaj.LuaValue
        public int narg() {
            return 0;
        }

        @Override  // luaj.LuaValue
        public Varargs subargs(int start) {
            return start > 0 ? this : None.argerror(1, "start must be > 0");
        }

        @Override  // luaj.LuaNil
        public String tojstring() {
            return "none";
        }
    }

    public static final LuaString ADD = null;
    public static final LuaString BAND = null;
    public static final LuaBigNumber BIG_NUMBER_ZERO = null;
    public static final LuaString BNOT = null;
    public static final LuaString BOR = null;
    public static final LuaString BXOR = null;
    public static final LuaString CALL = null;
    public static final LuaString CONCAT = null;
    public static final LuaString DIV = null;
    public static final LuaString EMPTYSTRING = null;
    public static final LuaString ENV = null;
    public static final LuaString EQ = null;
    public static final LuaBoolean FALSE = null;
    public static final LuaString IDIV = null;
    public static final LuaString INDEX = null;
    public static final LuaString LE = null;
    public static final LuaString LEN = null;
    public static final LuaString LT = null;
    private static final int MAXSTACK = 0xFA;
    private static final int MAXTAGLOOP = 100;
    public static final LuaString METATABLE = null;
    public static final LuaNumber MINUSONE = null;
    public static final LuaString MOD = null;
    public static final LuaString MODE = null;
    public static final LuaString MUL = null;
    public static final LuaString NAME = null;
    public static final LuaString NEWINDEX = null;
    public static final LuaValue NIL = null;
    public static final LuaValue[] NILS = null;
    public static final LuaValue NONE = null;
    public static final LuaValue[] NOVALS = null;
    public static final LuaNumber ONE = null;
    public static final LuaString ONESTRING = null;
    public static final LuaString POW = null;
    public static final LuaString SHL = null;
    public static final LuaString SHR = null;
    public static final LuaString SUB = null;
    public static final int TBOOLEAN = 1;
    public static final int TFUNCTION = 6;
    public static final int TINT = -2;
    public static final int TNIL = 0;
    public static final int TNONE = -1;
    public static final int TNUMBER = 3;
    public static final LuaString TOSTRING = null;
    public static final LuaBoolean TRUE = null;
    public static final int TSTRING = 4;
    public static final int TTABLE = 5;
    public static final int TTHREAD = 8;
    public static final int TUSERDATA = 7;
    public static final int TVALUE = 9;
    public static final String[] TYPE_NAMES;
    public static final LuaString UNM;
    public static final LuaNumber ZERO;
    public static final LuaString ZEROSTRING;
    public LuaValue uservalue;

    static {
        LuaValue.TYPE_NAMES = new String[]{"nil", "boolean", "lightuserdata", "number", "string", "table", "function", "userdata", "thread", "value"};
        LuaValue.NIL = new LuaNil();
        LuaValue.TRUE = new LuaBoolean(true);
        LuaValue.FALSE = new LuaBoolean(false);
        LuaValue.NONE = new None(null);
        LuaValue.ZERO = LuaLong.valueOf(0L);
        LuaValue.ONE = LuaLong.valueOf(1L);
        LuaValue.MINUSONE = LuaLong.valueOf(-1L);
        LuaValue.NOVALS = new LuaValue[0];
        LuaValue.ENV = LuaString.rawValueOf("_ENV");
        LuaValue.INDEX = LuaString.rawValueOf("__index");
        LuaValue.NEWINDEX = LuaString.rawValueOf("__newindex");
        LuaValue.CALL = LuaString.rawValueOf("__call");
        LuaValue.MODE = LuaString.rawValueOf("__mode");
        LuaValue.METATABLE = LuaString.rawValueOf("__metatable");
        LuaValue.ADD = LuaString.rawValueOf("__add");
        LuaValue.SUB = LuaString.rawValueOf("__sub");
        LuaValue.DIV = LuaString.rawValueOf("__div");
        LuaValue.MUL = LuaString.rawValueOf("__mul");
        LuaValue.POW = LuaString.rawValueOf("__pow");
        LuaValue.MOD = LuaString.rawValueOf("__mod");
        LuaValue.UNM = LuaString.rawValueOf("__unm");
        LuaValue.LEN = LuaString.rawValueOf("__len");
        LuaValue.EQ = LuaString.rawValueOf("__eq");
        LuaValue.LT = LuaString.rawValueOf("__lt");
        LuaValue.LE = LuaString.rawValueOf("__le");
        LuaValue.TOSTRING = LuaString.rawValueOf("__tostring");
        LuaValue.CONCAT = LuaString.rawValueOf("__concat");
        LuaValue.IDIV = LuaString.rawValueOf("__idiv");
        LuaValue.BNOT = LuaString.rawValueOf("__bnot");
        LuaValue.BAND = LuaString.rawValueOf("__band");
        LuaValue.BOR = LuaString.rawValueOf("__bor");
        LuaValue.BXOR = LuaString.rawValueOf("__bxor");
        LuaValue.SHL = LuaString.rawValueOf("__shl");
        LuaValue.SHR = LuaString.rawValueOf("__shr");
        LuaValue.NAME = LuaString.rawValueOf("__name");
        LuaValue.EMPTYSTRING = LuaString.rawValueOf("");
        LuaValue.ZEROSTRING = LuaString.rawValueOf("0");
        LuaValue.ONESTRING = LuaString.rawValueOf("1");
        LuaValue.BIG_NUMBER_ZERO = new LuaBigNumber(BigDecimal.ZERO);
        LuaValue.NILS = new LuaValue[0xFA];
        for(int i = 0; i < 0xFA; ++i) {
            LuaValue.NILS[i] = LuaValue.NIL;
        }
    }

    public LuaValue _getField(LuaValue luaValue0) {
        return this.get(luaValue0);
    }

    public LuaValue add(double f) {
        return this.arithmtwith(LuaValue.ADD, f);
    }

    public LuaValue add(int v) {
        return this.add(((double)v));
    }

    public LuaValue add(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.ADD, this, rhs);
    }

    // 去混淆评级： 低(20)
    public LuaValue and(LuaValue rhs) {
        return this.toboolean() ? rhs : this;
    }

    @Override  // luaj.Varargs
    public LuaValue arg(int index) {
        return index == 1 ? this : LuaValue.NIL;
    }

    @Override  // luaj.Varargs
    public LuaValue arg1() {
        return this;
    }

    public static LuaValue argerror(int iarg, String msg) {
        throw new LuaArgError(iarg, null, msg);
    }

    public static LuaValue argerror(int iarg, String funcname, String msg) {
        throw new LuaArgError(iarg, funcname, msg);
    }

    protected LuaValue argerror(String expected) {
        throw new LuaError("bad argument: " + expected + " expected, got " + this.typenamemt());
    }

    protected LuaValue aritherror() {
        throw new LuaError("attempt to perform arithmetic on " + this.typename());
    }

    protected LuaValue aritherror(String fun) {
        throw new LuaError("attempt to perform arithmetic \'" + fun + "\' on " + this.typename());
    }

    public static LuaValue arithmt(LuaValue tag, LuaValue op1, LuaValue op2) {
        LuaValue h = op1.metatag(tag);
        if(h.isnil()) {
            h = op2.metatag(tag);
            if(h.isnil()) {
                LuaValue.error(("attempt to perform arithmetic " + tag + " on a " + op1.typenamemt() + " value and a " + op2.typenamemt() + " value"));
            }
        }
        return h.call(op1, op2);
    }

    protected LuaValue arithmtwith(LuaValue luaValue0, double f) {
        LuaValue luaValue1 = this.metatag(luaValue0);
        if(luaValue1.isnil()) {
            LuaValue.error(("attempt to perform arithmetic " + luaValue0 + " on number and " + this.typename()));
        }
        return luaValue1.call(LuaValue.valueOf(f), this);
    }

    public static void assert_(boolean b, String msg) {
        if(!b) {
            throw new LuaError(msg);
        }
    }

    public LuaValue band(LuaValue rhs) {
        return LuaValue.bitwisemt(LuaValue.BAND, this, rhs);
    }

    public static LuaBigNumber bignumberOf(double f) {
        return new LuaBigNumber(f);
    }

    public static LuaBigNumber bignumberOf(int v) {
        return new LuaBigNumber(v);
    }

    public static LuaBigNumber bignumberOf(long v) {
        return new LuaBigNumber(v);
    }

    public static LuaBigNumber bignumberOf(String s) {
        return new LuaBigNumber(s);
    }

    public static LuaBigNumber bignumberOf(BigDecimal bigDecimal0) {
        return new LuaBigNumber(bigDecimal0);
    }

    public static LuaValue bitwisemt(LuaValue tag, LuaValue op1, LuaValue op2) {
        LuaValue h = op1.metatag(tag);
        if(h.isnil()) {
            LuaValue bad = null;
            h = op2.metatag(tag);
            if(h.isnil()) {
                if(op1.isnumber() && !op1.islongnumber()) {
                    bad = op1;
                }
                else if(op2.isnumber() && !op2.islongnumber()) {
                    bad = op2;
                }
                String s = LuaValue.tagToOp(tag);
                if(bad != null) {
                    throw new LuaError("number \'" + bad.tojstring() + "\' has no integer representation (for bitwise operation \'" + op1.tojstring() + " " + s + " " + op2.tojstring() + "\')");
                }
                LuaValue.error(("attempt to perform bitwise operation " + s + " on a " + op1.typename() + " value and a " + op2.typename() + " value"));
            }
        }
        return h.call(op1, op2);
    }

    public LuaValue bnot() {
        return this.checkmetatag(LuaValue.BNOT, "attempt to perform bitwise on ").call(this);
    }

    public LuaValue bor(LuaValue rhs) {
        return LuaValue.bitwisemt(LuaValue.BOR, this, rhs);
    }

    public Buffer buffer() {
        return new Buffer(this);
    }

    public LuaValue bxor(LuaValue rhs) {
        return LuaValue.bitwisemt(LuaValue.BXOR, this, rhs);
    }

    public LuaValue call() {
        return this.callmt().call(this);
    }

    public LuaValue call(String arg) {
        return this.call(LuaValue.valueOf(arg));
    }

    public LuaValue call(LuaValue arg) {
        return this.callmt().call(this, arg);
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return this.callmt().call(this, arg1, arg2);
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        return this.callmt().invoke(new LuaValue[]{this, arg1, arg2, arg3}).arg1();
    }

    protected LuaValue callmt() {
        return this.checkmetatag(LuaValue.CALL, "attempt to call ");
    }

    public BigDecimal checkbignum() {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber checkbignumber() {
        this.argerror("bignumber");
        return null;
    }

    public boolean checkboolean() {
        this.argerror("boolean");
        return false;
    }

    public LuaClosure checkclosure() {
        this.argerror("closure");
        return null;
    }

    public double checkdouble() {
        this.argerror("number");
        return 0.0;
    }

    public LuaFunction checkfunction() {
        this.argerror("function");
        return null;
    }

    public Globals checkglobals() {
        this.argerror("globals");
        return null;
    }

    public int checkint() {
        this.argerror("int");
        return 0;
    }

    public LuaLong checkinteger() {
        this.argerror("integer");
        return null;
    }

    public LuaValue checkinteger() {
        this.argerror("integer");
        return null;
    }

    public String checkjstring() {
        this.argerror("string");
        return null;
    }

    public long checklong() {
        this.argerror("long");
        return 0L;
    }

    protected LuaValue checkmetatag(LuaValue tag, String reason) {
        LuaValue luaValue1 = this.metatag(tag);
        if(luaValue1.isnil()) {
            throw tag != LuaValue.BNOT || !this.isnumber() ? new LuaError(reason + "a " + this.typenamemt() + " value") : new LuaError("number has no integer representation (for bitwise operation \'~" + this.tojstring() + "\')");
        }
        return luaValue1;
    }

    public LuaValue checknotnil() {
        return this;
    }

    public LuaNumber checknumber() {
        this.argerror("number");
        return null;
    }

    public LuaNumber checknumber(String msg) {
        throw new LuaError(msg);
    }

    public LuaString checkstring() {
        this.argerror("string");
        return null;
    }

    public LuaTable checktable() {
        this.argerror("table");
        return null;
    }

    public LuaThread checkthread() {
        this.argerror("thread");
        return null;
    }

    public Object checkuserdata() {
        this.argerror("userdata");
        return null;
    }

    public Object checkuserdata(Class class0) {
        this.argerror("userdata");
        return null;
    }

    protected LuaValue compareerror(String rhs) {
        throw new LuaError("attempt to compare " + this.typename() + " with " + rhs);
    }

    protected LuaValue compareerror(LuaValue rhs) {
        throw new LuaError("attempt to compare " + this.typename() + " with " + rhs.typename());
    }

    public LuaValue comparemt(LuaValue tag, LuaValue op1) {
        LuaValue h = this.metatag(tag);
        if(h.isnil()) {
            h = op1.metatag(tag);
            if(h.isnil()) {
                if(LuaValue.LE.raweq(tag)) {
                    LuaValue h = this.metatag(LuaValue.LT);
                    if(!h.isnil()) {
                        return h.call(op1, this).not();
                    }
                    h = op1.metatag(LuaValue.LT);
                    if(!h.isnil()) {
                        return h.call(op1, this).not();
                    }
                }
                String s = this.typenamemt();
                String s1 = op1.typenamemt();
                return s.equals(s1) ? LuaValue.error(("attempt to compare " + ("two " + s + " values"))) : LuaValue.error(("attempt to compare " + (s + " with " + s1)));
            }
        }
        return h.call(this, op1);
    }

    public Buffer concat(Buffer rhs) {
        return rhs.concatTo(this);
    }

    public LuaValue concat(LuaValue rhs) {
        return this.concatmt(rhs);
    }

    public LuaValue concatTo(LuaNumber lhs) {
        return lhs.concatmt(this);
    }

    public LuaValue concatTo(LuaString lhs) {
        return lhs.concatmt(this);
    }

    public LuaValue concatTo(LuaValue lhs) {
        return lhs.concatmt(this);
    }

    public LuaValue concatmt(LuaValue rhs) {
        LuaValue h = this.metatag(LuaValue.CONCAT);
        if(h.isnil()) {
            LuaValue bad = null;
            h = rhs.metatag(LuaValue.CONCAT);
            if(h.isnil()) {
                if(!this.isstring()) {
                    bad = this;
                }
                else if(!rhs.isstring()) {
                    bad = rhs;
                }
                if(bad != null) {
                    throw new LuaError("attempt to concatenate a " + bad.typename() + " value");
                }
                LuaValue.error(("attempt to concatenate " + this.typename() + " and " + rhs.typename()));
            }
        }
        return h.call(this, rhs);
    }

    public LuaValue div(double f) {
        return this.aritherror("div");
    }

    public LuaValue div(int v) {
        return this.aritherror("div");
    }

    public LuaValue div(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.DIV, this, rhs);
    }

    public LuaValue divInto(double f) {
        return this.arithmtwith(LuaValue.DIV, f);
    }

    // 去混淆评级： 低(20)
    public LuaValue eq(LuaValue val) {
        return this.eq_b(val) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public boolean eq_b(LuaValue val) {
        return this == val;
    }

    public static final boolean eqmtcall(LuaValue lhs, LuaValue rhs) {
        LuaValue h = LuaValue.NIL;
        LuaValue luaValue3 = lhs.getmetatable();
        if(luaValue3 != null) {
            h = luaValue3.rawget(LuaValue.EQ);
        }
        if(h.isnil()) {
            LuaValue mt = rhs.getmetatable();
            if(mt != null) {
                h = mt.rawget(LuaValue.EQ);
            }
        }
        return h.isnil() ? false : h.call(lhs, rhs).toboolean();
    }

    public static final boolean eqmtcall(LuaValue luaValue0, LuaValue luaValue1, LuaValue luaValue2, LuaValue luaValue3) {
        LuaValue luaValue4 = luaValue1.rawget(LuaValue.EQ);
        return luaValue4.isnil() || luaValue4 != luaValue3.rawget(LuaValue.EQ) ? false : luaValue4.call(luaValue0, luaValue2).toboolean();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    public static LuaValue error(String message) {
        throw new LuaError(message);
    }

    public static LuaValue error(String message, Throwable cause) {
        throw new LuaError(message, cause);
    }

    public LuaValue get(int key) {
        return this.get(LuaLong.valueOf(key));
    }

    public LuaValue get(String key) {
        return this.get(LuaValue.valueOf(key));
    }

    public LuaValue get(LuaValue key) {
        return LuaValue.gettable(this, key);
    }

    public LuaValue getmetatable() {
        return null;
    }

    protected static LuaValue gettable(LuaValue t, LuaValue key) {
        LuaValue luaValue3;
        int loop = 0;
        do {
            if(t.istable()) {
                LuaValue luaValue2 = t.rawget(key);
                if(luaValue2.isnil()) {
                    luaValue3 = t.metatag(LuaValue.INDEX);
                    if(luaValue3.isnil()) {
                        return luaValue2;
                    }
                    goto label_10;
                }
                return luaValue2;
            }
            else {
                luaValue3 = t.metatag(LuaValue.INDEX);
                if(luaValue3.isnil()) {
                    t.indexerror(key.tojstring());
                }
            }
        label_10:
            if(luaValue3.isfunction()) {
                return luaValue3.call(t, key);
            }
            t = luaValue3;
            ++loop;
        }
        while(loop < 100);
        LuaValue.error("loop in gettable");
        return LuaValue.NIL;
    }

    public LuaValue getuservalue() {
        return this.uservalue;
    }

    // 去混淆评级： 低(20)
    public LuaValue gt(LuaValue rhs) {
        return this.gt_b(rhs) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public boolean gt_b(double f) {
        this.compareerror("number");
        return false;
    }

    public boolean gt_b(int v) {
        this.compareerror("number");
        return false;
    }

    public boolean gt_b(LuaValue rhs) {
        return rhs.comparemt(LuaValue.LE, this).toboolean();
    }

    // 去混淆评级： 低(20)
    public LuaValue gteq(LuaValue rhs) {
        return this.gteq_b(rhs) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public boolean gteq_b(int v) {
        this.compareerror("number");
        return false;
    }

    public boolean gteq_b(LuaValue rhs) {
        return rhs.comparemt(LuaValue.LT, this).toboolean();
    }

    public LuaValue idiv(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.IDIV, this, rhs);
    }

    protected LuaValue illegal(String op, String typename) {
        throw new LuaError("illegal operation \'" + op + "\' for " + typename);
    }

    private void indexerror(String key) {
        LuaValue.error(("attempt to index ? (a " + this.typename() + " value) with key \'" + key + '\''));
    }

    public Varargs inext(LuaValue index) {
        return this.typerror("table");
    }

    public void initupvalue1(LuaValue env) {
    }

    public Varargs invoke() {
        return this.invoke(LuaValue.NONE);
    }

    public Varargs invoke(LuaValue arg1, LuaValue arg2, Varargs varargs) {
        return this.invoke(LuaValue.varargsOf(arg1, arg2, varargs));
    }

    public Varargs invoke(LuaValue arg, Varargs varargs) {
        return this.invoke(LuaValue.varargsOf(arg, varargs));
    }

    public Varargs invoke(Varargs args) {
        return this.callmt().invoke(this, args);
    }

    public Varargs invoke(LuaValue[] args) {
        return this.invoke(LuaValue.varargsOf(args));
    }

    public Varargs invoke(LuaValue[] args, Varargs varargs) {
        return this.invoke(LuaValue.varargsOf(args, varargs));
    }

    public Varargs invokemethod(String name) {
        return this.get(name).invoke(this);
    }

    public Varargs invokemethod(String name, Varargs args) {
        return this.get(name).invoke(LuaValue.varargsOf(this, args));
    }

    public Varargs invokemethod(String name, LuaValue[] args) {
        return this.get(name).invoke(LuaValue.varargsOf(this, LuaValue.varargsOf(args)));
    }

    public Varargs invokemethod(LuaValue name) {
        return this.get(name).invoke(this);
    }

    public Varargs invokemethod(LuaValue name, Varargs args) {
        return this.get(name).invoke(LuaValue.varargsOf(this, args));
    }

    public Varargs invokemethod(LuaValue name, LuaValue[] args) {
        return this.get(name).invoke(LuaValue.varargsOf(this, LuaValue.varargsOf(args)));
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean isbignumber() {
        return false;
    }

    public boolean isboolean() {
        return false;
    }

    public boolean isclosure() {
        return false;
    }

    public boolean isfunction() {
        return false;
    }

    public boolean isint() {
        return false;
    }

    public boolean isintnumber() {
        return false;
    }

    public boolean isinttype() {
        return false;
    }

    public boolean islong() {
        return false;
    }

    public boolean islongnumber() {
        return false;
    }

    public boolean isnil() {
        return false;
    }

    public boolean isnumber() {
        return false;
    }

    public boolean isstring() {
        return false;
    }

    public boolean istable() {
        return false;
    }

    public boolean isthread() {
        return false;
    }

    public boolean isuserdata() {
        return false;
    }

    public boolean isuserdata(Class class0) {
        return false;
    }

    public boolean isvalidkey() {
        return true;
    }

    public LuaValue len() {
        return this.checkmetatag(LuaValue.LEN, "attempt to get length of ").call(this);
    }

    protected LuaValue lenerror() {
        throw new LuaError("attempt to get length of a " + this.typename() + " value");
    }

    public int length() {
        return this.len().toint();
    }

    public static LuaTable listOf(LuaValue[] unnamedValues) {
        return new LuaTable(null, unnamedValues, null);
    }

    public static LuaTable listOf(LuaValue[] unnamedValues, Varargs lastarg) {
        return new LuaTable(null, unnamedValues, lastarg);
    }

    public LuaValue load(LuaValue library) {
        return library.call(LuaValue.EMPTYSTRING, this);
    }

    // 去混淆评级： 低(20)
    public LuaValue lt(LuaValue rhs) {
        return this.lt_b(rhs) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public boolean lt_b(int v) {
        this.compareerror("number");
        return false;
    }

    public boolean lt_b(LuaValue rhs) {
        return this.comparemt(LuaValue.LT, rhs).toboolean();
    }

    // 去混淆评级： 低(20)
    public LuaValue lteq(LuaValue rhs) {
        return this.lteq_b(rhs) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public boolean lteq_b(int v) {
        this.compareerror("number");
        return false;
    }

    public boolean lteq_b(LuaValue rhs) {
        return this.comparemt(LuaValue.LE, rhs).toboolean();
    }

    @Override  // luaj.Varargs
    public static void main(String[] args) throws Throwable {
    }

    protected static Metatable metatableOf(LuaValue mt) {
        if(mt != null && mt.istable()) {
            LuaValue luaValue1 = mt.rawget(LuaValue.MODE);
            if(luaValue1.isstring()) {
                String s = luaValue1.tojstring();
                boolean weakkeys = s.indexOf(107) >= 0;
                boolean weakvalues = s.indexOf(0x76) >= 0;
                if(weakkeys || weakvalues) {
                    return new WeakTable(weakkeys, weakvalues, mt);
                }
            }
            return (LuaTable)mt;
        }
        return mt != null ? new NonTableMetatable(mt) : null;
    }

    public LuaValue metatag(LuaValue tag) {
        LuaValue luaValue1 = this.getmetatable();
        return luaValue1 == null ? LuaValue.NIL : luaValue1.rawget(tag);
    }

    public LuaValue method(String name) {
        return this.get(name).call(this);
    }

    public LuaValue method(String name, LuaValue arg) {
        return this.get(name).call(this, arg);
    }

    public LuaValue method(String name, LuaValue arg1, LuaValue arg2) {
        return this.get(name).call(this, arg1, arg2);
    }

    public LuaValue method(LuaValue name) {
        return this.get(name).call(this);
    }

    public LuaValue method(LuaValue name, LuaValue arg) {
        return this.get(name).call(this, arg);
    }

    public LuaValue method(LuaValue name, LuaValue arg1, LuaValue arg2) {
        return this.get(name).call(this, arg1, arg2);
    }

    public LuaValue mod(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.MOD, this, rhs);
    }

    public LuaValue modFrom(double f) {
        return this.arithmtwith(LuaValue.MOD, f);
    }

    public LuaValue mul(double f) {
        return this.arithmtwith(LuaValue.MUL, f);
    }

    public LuaValue mul(int v) {
        return this.mul(((double)v));
    }

    public LuaValue mul(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.MUL, this, rhs);
    }

    @Override  // luaj.Varargs
    public int narg() {
        return 1;
    }

    public LuaValue neg() {
        return this.checkmetatag(LuaValue.UNM, "attempt to perform arithmetic on ").call(this);
    }

    // 去混淆评级： 低(20)
    public LuaValue neq(LuaValue val) {
        return this.eq_b(val) ? LuaValue.FALSE : LuaValue.TRUE;
    }

    public boolean neq_b(LuaValue val) {
        return !this.eq_b(val);
    }

    public Varargs next(LuaValue index) {
        return this.typerror("table");
    }

    public LuaValue not() {
        return LuaValue.FALSE;
    }

    public static LuaNumber numberOf(String s) {
        if(s.indexOf(46) == -1 && s.indexOf(101) == -1 && s.indexOf(69) == -1) {
            if(s.length() <= 19) {
                try {
                    return new BigDecimal(s).compareTo(LuaBigNumber.MAX_LONG) < 0 ? LuaLong.valueOf(Long.parseLong(s)) : LuaBigNumber.bignumberOf(s);
                }
                catch(NumberFormatException unused_ex) {
                    try {
                        return LuaBigNumber.bignumberOf(s);
                    }
                    catch(Exception unused_ex) {
                        return LuaDouble.valueOf(Double.parseDouble(s));
                    }
                }
            }
            try {
                return LuaBigNumber.bignumberOf(s);
            }
            catch(Exception unused_ex) {
            }
        }
        return LuaDouble.valueOf(Double.parseDouble(s));
    }

    public Varargs onInvoke(Varargs args, boolean tailcall) {
        return this.invoke(args);
    }

    public BigDecimal optbignum(double f) {
        this.argerror("bignumber");
        return null;
    }

    public BigDecimal optbignum(int v) {
        this.argerror("bignumber");
        return null;
    }

    public BigDecimal optbignum(long v) {
        this.argerror("bignumber");
        return null;
    }

    public BigDecimal optbignum(String s) {
        this.argerror("bignumber");
        return null;
    }

    public BigDecimal optbignum(BigDecimal bigDecimal0) {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber optbignumber(double f) {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber optbignumber(int v) {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber optbignumber(long v) {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber optbignumber(String s) {
        this.argerror("bignumber");
        return null;
    }

    public LuaBigNumber optbignumber(BigDecimal bigDecimal0) {
        this.argerror("bignumber");
        return null;
    }

    public boolean optboolean(boolean defval) {
        this.argerror("boolean");
        return false;
    }

    public LuaClosure optclosure(LuaClosure defval) {
        this.argerror("closure");
        return null;
    }

    public double optdouble(double defval) {
        this.argerror("number");
        return 0.0;
    }

    public LuaFunction optfunction(LuaFunction defval) {
        this.argerror("function");
        return null;
    }

    public int optint(int defval) {
        this.argerror("int");
        return 0;
    }

    public LuaLong optinteger(LuaLong defval) {
        this.argerror("integer");
        return null;
    }

    public LuaValue optinteger(LuaValue luaValue0) {
        this.argerror("integer");
        return null;
    }

    public String optjstring(String defval) {
        this.argerror("String");
        return null;
    }

    public long optlong(long defval) {
        this.argerror("long");
        return 0L;
    }

    public LuaNumber optnumber(LuaNumber defval) {
        this.argerror("number");
        return null;
    }

    public LuaString optstring(LuaString defval) {
        this.argerror("string");
        return null;
    }

    public LuaTable opttable(LuaTable defval) {
        this.argerror("table");
        return null;
    }

    public LuaThread optthread(LuaThread defval) {
        this.argerror("thread");
        return null;
    }

    public Object optuserdata(Class class0, Object object0) {
        this.argerror(class0.getName());
        return null;
    }

    public Object optuserdata(Object object0) {
        this.argerror("object");
        return null;
    }

    public LuaValue optvalue(LuaValue defval) {
        return this;
    }

    // 去混淆评级： 低(20)
    public LuaValue or(LuaValue rhs) {
        return this.toboolean() ? this : rhs;
    }

    public static LuaValue parseBinary(CharSequence charSequence0) {
        try {
            StringBuilder stringBuilder0 = new StringBuilder("0");
            for(int v = 2; true; ++v) {
                if(v >= charSequence0.length()) {
                    return LuaValue.valueOf(Long.parseLong(stringBuilder0.toString(), 2));
                }
                stringBuilder0 = stringBuilder0.append(charSequence0.charAt(v));
            }
        }
        catch(Exception exception0) {
            return LuaValue.valueOf(exception0.toString());
        }
    }

    public static LuaValue parseHex(CharSequence str) {
        int s;
        double sgn;
        int v = str.length();
        int s;
        for(s = 0; true; ++s) {
            sgn = 1.0;
            if(s >= v || !LexState.isspace(str.charAt(s))) {
                break;
            }
        }
        if(s < v) {
            int v2 = str.charAt(s);
            if(v2 != 43 && v2 != 45) {
                s = s;
            }
            else {
                if(v2 == 45) {
                    sgn = -1.0;
                }
                s = s + 1;
            }
        }
        else {
            s = s;
        }
        if(s + 2 >= v) {
            return null;
        }
        if(str.charAt(s) != 0x30) {
            return null;
        }
        switch(str.charAt(s + 1)) {
            case 88: 
            case 120: {
                int s = s + 2;
                double m = 0.0;
                long l = 0L;
                int e = 0;
                int longSize = 0;
                while(s < v) {
                    int ch = str.charAt(s);
                    if(!LexState.isxdigit(ch)) {
                        break;
                    }
                    int v9 = LexState.hexvalue(ch);
                    if(longSize != 0 || v9 != 0) {
                        if(longSize < 30) {
                            m = 16.0 * m + ((double)v9);
                        }
                        else {
                            e += 4;
                        }
                        l = 16L * l + ((long)v9);
                        ++longSize;
                    }
                    ++s;
                }
                if(s < v && str.charAt(s) == 46) {
                    longSize += 0x800000;
                    ++s;
                    int max = 0x7FFFFFFF;
                    while(s < v) {
                        int ch = str.charAt(s);
                        if(!LexState.isxdigit(ch)) {
                            break;
                        }
                        if(max == 0x7FFFFFFF && m != 0.0) {
                            max = s + 30;
                        }
                        if(s < max) {
                            m = 16.0 * m + ((double)LexState.hexvalue(ch));
                            e -= 4;
                            ++longSize;
                        }
                        ++s;
                    }
                }
                if((0x7FFFFF & longSize) == 0) {
                    return null;
                }
                if(s < v) {
                    switch(str.charAt(s)) {
                        case 80: 
                        case 0x70: {
                            longSize += 0x1000000;
                            ++s;
                            int exp1 = 0;
                            boolean neg1 = false;
                            if(s < v) {
                                int ch = str.charAt(s);
                                if(ch == 43 || ch == 45) {
                                    if(ch == 45) {
                                        neg1 = true;
                                    }
                                    ++s;
                                }
                            }
                            boolean found;
                            for(found = false; s < v; found = true) {
                                int ch = str.charAt(s);
                                if(!LexState.isdigit(ch)) {
                                    break;
                                }
                                exp1 = exp1 * 10 + ch - 0x30;
                                ++s;
                            }
                            if(!found) {
                                return null;
                            }
                            if(neg1) {
                                exp1 = -exp1;
                            }
                            e += exp1;
                        }
                    }
                }
                while(s < v && LexState.isspace(str.charAt(s))) {
                    ++s;
                }
                if(s != v) {
                    return null;
                }
                if(longSize < 0x800000) {
                    return sgn > 0.0 ? LuaLong.valueOf(l) : LuaLong.valueOf(-l);
                }
                return LuaValue.valueOf(sgn * m * Math.pow(2.0, e));
            }
            default: {
                return null;
            }
        }
    }

    public static LuaValue parseNumber(String str) {
        return str.indexOf(120) >= 0 || str.indexOf(88) >= 0 ? LuaValue.parseHex(str) : LuaValue.numberOf(str.trim());
    }

    public static LuaValue parseNumber(String str, LuaValue fallback) {
        try {
            LuaValue ret = LuaValue.parseNumber(str);
            return ret == null ? fallback : ret;
        }
        catch(NumberFormatException unused_ex) {
            return fallback;
        }
    }

    public LuaValue pow(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.POW, this, rhs);
    }

    public LuaValue powWith(double f) {
        return this.arithmtwith(LuaValue.POW, f);
    }

    public LuaValue powWith(int v) {
        return this.powWith(((double)v));
    }

    public void presize(int i) {
        this.typerror("table");
    }

    public boolean raweq(double f) {
        return false;
    }

    public boolean raweq(int v) {
        return false;
    }

    public boolean raweq(LuaString val) {
        return false;
    }

    public boolean raweq(LuaUserdata luaUserdata0) {
        return false;
    }

    public boolean raweq(LuaValue val) {
        return this == val;
    }

    public LuaValue rawget(int key) {
        return this.rawget(LuaValue.valueOf(key));
    }

    public LuaValue rawget(String key) {
        return this.rawget(LuaValue.valueOf(key));
    }

    public LuaValue rawget(LuaValue key) {
        return this.unimplemented("rawget");
    }

    public int rawlen() {
        this.typerror("table or string");
        return 0;
    }

    public void rawset(int key, String value) {
        if(value != null) {
            this.rawset(key, LuaValue.valueOf(value));
        }
    }

    public void rawset(int key, LuaValue value) {
        if(value != null) {
            this.rawset(LuaValue.valueOf(key), value);
        }
    }

    public void rawset(String key, double value) {
        if(key != null) {
            this.rawset(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void rawset(String key, int value) {
        if(key != null) {
            this.rawset(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void rawset(String key, String value) {
        if(key != null && value != null) {
            this.rawset(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void rawset(String key, LuaValue value) {
        if(key != null && value != null) {
            this.rawset(LuaValue.valueOf(key), value);
        }
    }

    public void rawset(LuaValue key, LuaValue value) {
        if(key != null && value != null) {
            this.unimplemented("rawset");
        }
    }

    public void rawsetlist(int key0, Varargs values) {
        int v2 = values.narg();
        for(int i = 0; i < v2; ++i) {
            this.rawset(key0 + i, values.arg(i + 1));
        }
    }

    public void set(int key, String value) {
        if(value != null) {
            this.set(key, LuaValue.valueOf(value));
        }
    }

    public void set(int key, LuaValue value) {
        if(value != null) {
            this.set(LuaLong.valueOf(key), value);
        }
    }

    public void set(String key, double value) {
        if(key != null) {
            this.set(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void set(String key, int value) {
        if(key != null) {
            this.set(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void set(String key, long value) {
        if(key != null) {
            this.set(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void set(String key, String value) {
        if(key != null && value != null) {
            this.set(LuaValue.valueOf(key), LuaValue.valueOf(value));
        }
    }

    public void set(String key, LuaValue value) {
        if(key != null && value != null) {
            this.set(LuaValue.valueOf(key), value);
        }
    }

    public void set(LuaValue key, LuaValue value) {
        if(key != null && value != null) {
            LuaValue.settable(this, key, value);
        }
    }

    public LuaValue setmetatable(LuaValue metatable) {
        return this.argerror("table");
    }

    protected static boolean settable(LuaValue t, LuaValue key, LuaValue value) {
        LuaValue luaValue3;
        int loop = 0;
        do {
            if(t.istable()) {
                if(t.rawget(key).isnil()) {
                    luaValue3 = t.metatag(LuaValue.NEWINDEX);
                    if(luaValue3.isnil()) {
                        t.rawset(key, value);
                        return true;
                    }
                    goto label_10;
                }
                t.rawset(key, value);
                return true;
            }
            else {
                luaValue3 = t.metatag(LuaValue.NEWINDEX);
                if(luaValue3.isnil()) {
                    throw new LuaError("table expected for set index (\'" + key + "\') value, got " + t.typename());
                }
            }
        label_10:
            if(luaValue3.isfunction()) {
                luaValue3.call(t, key, value);
                return true;
            }
            t = luaValue3;
            ++loop;
        }
        while(loop < 100);
        LuaValue.error("loop in settable");
        return false;
    }

    public void setuservalue(LuaValue luaValue0) {
        this.uservalue = luaValue0;
    }

    public LuaValue shl(LuaValue rhs) {
        return LuaValue.bitwisemt(LuaValue.SHL, this, rhs);
    }

    public LuaValue shr(LuaValue rhs) {
        return LuaValue.bitwisemt(LuaValue.SHR, this, rhs);
    }

    public int strcmp(LuaString rhs) {
        LuaValue.error(("attempt to compare " + this.typename()));
        return 0;
    }

    public int strcmp(LuaValue rhs) {
        LuaValue.error(("attempt to compare " + this.typename()));
        return 0;
    }

    public LuaValue strongvalue() {
        return this;
    }

    public LuaString strvalue() {
        this.typerror("string or number");
        return null;
    }

    public LuaValue sub(LuaValue rhs) {
        return LuaValue.arithmt(LuaValue.SUB, this, rhs);
    }

    public LuaValue subFrom(double f) {
        return this.arithmtwith(LuaValue.SUB, f);
    }

    public LuaValue subFrom(int v) {
        return this.subFrom(((double)v));
    }

    @Override  // luaj.Varargs
    public Varargs subargs(int start) {
        if(start == 1) {
            return this;
        }
        return start <= 1 ? LuaValue.argerror(1, "start must be > 0") : LuaValue.NONE;
    }

    public static LuaTable tableOf() {
        return new LuaTable();
    }

    public static LuaTable tableOf(int narray, int nhash) {
        return new LuaTable(narray, nhash);
    }

    public static LuaTable tableOf(Varargs varargs, int firstarg) {
        return new LuaTable(varargs, firstarg);
    }

    public static LuaTable tableOf(LuaValue[] namedValues) {
        return new LuaTable(namedValues, null, null);
    }

    public static LuaTable tableOf(LuaValue[] namedValues, LuaValue[] unnamedValues) {
        return new LuaTable(namedValues, unnamedValues, null);
    }

    public static LuaTable tableOf(LuaValue[] namedValues, LuaValue[] unnamedValues, Varargs lastarg) {
        return new LuaTable(namedValues, unnamedValues, lastarg);
    }

    public static String tagToOp(LuaValue tag) {
        if(tag == LuaValue.BAND) {
            return "&";
        }
        if(tag == LuaValue.BOR) {
            return "|";
        }
        if(tag == LuaValue.BXOR) {
            return "~";
        }
        if(tag == LuaValue.SHL) {
            return "<<";
        }
        return tag == LuaValue.SHR ? ">>" : tag.tojstring();
    }

    // 去混淆评级： 低(20)
    public boolean testfor_b(LuaValue limit, LuaValue step) {
        return step.gt_b(LuaValue.ZERO) ? this.lteq_b(limit) : this.gteq_b(limit);
    }

    @Override  // luaj.Varargs
    public String toString() {
        return this.tojstring();
    }

    public BigDecimal tobignum() {
        return BigDecimal.ZERO;
    }

    public LuaBigNumber tobignumber() {
        return LuaValue.BIG_NUMBER_ZERO;
    }

    public boolean toboolean() {
        return true;
    }

    public byte tobyte() {
        return 0;
    }

    public char tochar() {
        return '\u0000';
    }

    public double todouble() {
        return 0.0;
    }

    public float tofloat() {
        return 0.0f;
    }

    public int toint() {
        return 0;
    }

    @Override  // luaj.Varargs
    public String tojstring() {
        return this.typenamemt() + ": " + Integer.toHexString(this.hashCode());
    }

    public long tolong() {
        return 0L;
    }

    public LuaValue tonumber() {
        return LuaValue.NIL;
    }

    public short toshort() {
        return 0;
    }

    public LuaValue tostring() {
        return LuaValue.NIL;
    }

    public Object touserdata() {
        return null;
    }

    public Object touserdata(Class class0) {
        return null;
    }

    public abstract int type();

    public abstract String typename();

    public String typenamemt() {
        LuaValue luaValue0 = this.metatag(LuaValue.NAME);
        return luaValue0.isstring() ? luaValue0.tojstring() : this.typename();
    }

    protected LuaValue typerror(String expected) {
        throw new LuaError(expected + " expected, got " + this.typenamemt());
    }

    protected LuaValue unimplemented(String fun) {
        throw new LuaError("\'" + fun + "\' not implemented for " + this.typename());
    }

    public static LuaUserdata userdataOf(Object object0) {
        return new LuaUserdata(object0);
    }

    public static LuaUserdata userdataOf(Object object0, LuaValue luaValue0) {
        return new LuaUserdata(object0, luaValue0);
    }

    // 去混淆评级： 低(20)
    public static LuaBoolean valueOf(boolean b) {
        return b ? LuaValue.TRUE : LuaValue.FALSE;
    }

    public static LuaLong valueOf(long l) {
        return LuaLong.valueOf(l);
    }

    public static LuaNumber valueOf(double d) {
        return LuaDouble.valueOf(d);
    }

    public static LuaNumber valueOf(int v) {
        return LuaValue.valueOf(v);
    }

    public static LuaString valueOf(String s) {
        return LuaString.valueOf(s);
    }

    public static LuaString valueOf(byte[] bytes) {
        return LuaString.valueOf(bytes);
    }

    public static LuaString valueOf(byte[] bytes, int off, int len) {
        return LuaString.valueOf(bytes, off, len);
    }

    public static LuaValue valueOf(int v) {
        return LuaValue.valueOf(v);
    }

    public static Varargs varargsOf(LuaValue v1, LuaValue v2, Varargs v3) {
        return v3.narg() != 0 ? new ArrayPartVarargs(new LuaValue[]{v1, v2}, 0, 2, v3) : new PairVarargs(v1, v2);
    }

    public static Varargs varargsOf(LuaValue v, Varargs r) {
        return r.narg() != 0 ? new PairVarargs(v, r) : v;
    }

    public static Varargs varargsOf(LuaValue[] v) {
        switch(v.length) {
            case 0: {
                return LuaValue.NONE;
            }
            case 1: {
                return v[0];
            }
            case 2: {
                return new PairVarargs(v[0], v[1]);
            }
            default: {
                return new ArrayVarargs(v, LuaValue.NONE);
            }
        }
    }

    public static Varargs varargsOf(LuaValue[] v, int offset, int length) {
        switch(length) {
            case 0: {
                return LuaValue.NONE;
            }
            case 1: {
                return v[offset];
            }
            case 2: {
                return new PairVarargs(v[offset], v[offset + 1]);
            }
            default: {
                return new ArrayPartVarargs(v, offset, length, LuaValue.NONE);
            }
        }
    }

    public static Varargs varargsOf(LuaValue[] v, int offset, int length, Varargs more) {
        switch(length) {
            case 0: {
                return more;
            }
            case 1: {
                return more.narg() > 0 ? new PairVarargs(v[offset], more) : v[offset];
            }
            case 2: {
                return more.narg() > 0 ? new ArrayPartVarargs(v, offset, 2, more) : new PairVarargs(v[offset], v[offset + 1]);
            }
            default: {
                return new ArrayPartVarargs(v, offset, length, more);
            }
        }
    }

    public static Varargs varargsOf(LuaValue[] v, Varargs r) {
        switch(v.length) {
            case 0: {
                return r;
            }
            case 1: {
                return r.narg() > 0 ? new PairVarargs(v[0], r) : v[0];
            }
            case 2: {
                return r.narg() > 0 ? new ArrayVarargs(v, r) : new PairVarargs(v[0], v[1]);
            }
            default: {
                return new ArrayVarargs(v, r);
            }
        }
    }
}

