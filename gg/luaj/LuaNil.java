package luaj;

import java.math.BigDecimal;

public class LuaNil extends LuaValue {
    public static LuaValue s_metatable;

    @Override  // luaj.LuaValue
    public LuaValue checknotnil() {
        return this.argerror("value");
    }

    @Override  // luaj.LuaValue
    public boolean equals(Object o) {
        return o instanceof LuaNil;
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return LuaNil.s_metatable;
    }

    @Override  // luaj.LuaValue
    public boolean isnil() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean isvalidkey() {
        return false;
    }

    @Override  // luaj.LuaValue
    public LuaValue not() {
        return LuaValue.TRUE;
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(double f) {
        return BigDecimal.valueOf(f);
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(int v) {
        return BigDecimal.valueOf(v);
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(long v) {
        return BigDecimal.valueOf(v);
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(String s) {
        return new BigDecimal(s);
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(BigDecimal bigDecimal0) {
        return bigDecimal0;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(double f) {
        return new LuaBigNumber(f);
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(int v) {
        return new LuaBigNumber(v);
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(long v) {
        return new LuaBigNumber(v);
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(String s) {
        return new LuaBigNumber(s);
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(BigDecimal bigDecimal0) {
        return new LuaBigNumber(bigDecimal0);
    }

    @Override  // luaj.LuaValue
    public boolean optboolean(boolean defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaClosure optclosure(LuaClosure defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public double optdouble(double defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaFunction optfunction(LuaFunction defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public int optint(int defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaLong optinteger(LuaLong defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaValue optinteger(LuaValue luaValue0) {
        return luaValue0;
    }

    @Override  // luaj.LuaValue
    public String optjstring(String defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public long optlong(long defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaNumber optnumber(LuaNumber defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaString optstring(LuaString defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaTable opttable(LuaTable defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public LuaThread optthread(LuaThread defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public Object optuserdata(Class class0, Object object0) {
        return object0;
    }

    @Override  // luaj.LuaValue
    public Object optuserdata(Object object0) {
        return object0;
    }

    @Override  // luaj.LuaValue
    public LuaValue optvalue(LuaValue defval) {
        return defval;
    }

    @Override  // luaj.LuaValue
    public String toString() {
        return "nil";
    }

    @Override  // luaj.LuaValue
    public boolean toboolean() {
        return false;
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        return "nil";
    }

    @Override  // luaj.LuaValue
    public int type() {
        return 0;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "nil";
    }
}

