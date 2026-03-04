package luaj;

public class LuaLong extends LuaNumber {
    public static final LuaLong MAX_VALUE;
    public static final LuaLong MIN_VALUE;
    private static final LuaLong[] longValues;
    private static final LuaString[] luaStrings;
    private static final String[] strings;
    public final long v;

    static {
        LuaLong.MAX_VALUE = new LuaLong(0x7FFFFFFFFFFFFFFFL);
        LuaLong.MIN_VALUE = new LuaLong(0x8000000000000000L);
        LuaLong.longValues = new LuaLong[0x200];
        LuaLong.luaStrings = new LuaString[0x200];
        LuaLong.strings = new String[0x200];
    }

    LuaLong(long l) {
        this.v = l;
    }

    @Override  // luaj.LuaValue
    public LuaValue add(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.add(rhs);
        }
        return rhs.islongnumber() ? LuaLong.valueOf(this.v + rhs.tolong()) : LuaLong.valueOf(((double)this.v) + rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue band(LuaValue rhs) {
        return !rhs.islongnumber() ? super.band(rhs) : LuaLong.valueOf(this.v & rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaValue bnot() {
        return LuaLong.valueOf(~this.v);
    }

    @Override  // luaj.LuaValue
    public LuaValue bor(LuaValue rhs) {
        return !rhs.islongnumber() ? super.bor(rhs) : LuaLong.valueOf(this.v | rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaValue bxor(LuaValue rhs) {
        return !rhs.islongnumber() ? super.bxor(rhs) : LuaLong.valueOf(this.v ^ rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public double checkdouble() {
        return (double)this.v;
    }

    @Override  // luaj.LuaValue
    public int checkint() {
        return (int)this.v;
    }

    @Override  // luaj.LuaValue
    public LuaLong checkinteger() {
        return this;
    }

    @Override  // luaj.LuaValue
    public String checkjstring() {
        return String.valueOf(this.v);
    }

    @Override  // luaj.LuaValue
    public long checklong() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public LuaString checkstring() {
        return LuaLong.valueOf(String.valueOf(this.v));
    }

    @Override  // luaj.LuaValue
    public LuaValue div(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.div(rhs);
        }
        return rhs.islongnumber() && rhs.tolong() != 0L ? LuaLong.valueOf(this.v / rhs.tolong()) : LuaDouble.ddiv(this.v, rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue val) {
        if(!val.isnumber()) {
            return super.eq_b(val);
        }
        return val.islongnumber() ? this.v == val.tolong() : ((double)this.v) == val.todouble();
    }

    @Override  // luaj.LuaValue
    public boolean equals(Object o) {
        return o instanceof LuaLong && ((LuaLong)o).v == this.v;
    }

    public static long floorDiv(long x, long y) {
        if(y == 0L) {
            throw new LuaError("attempt to divide by zero (as \'" + x + " // " + 0L + "\')");
        }
        long r = x / y;
        return (x ^ y) >= 0L || r * y == x ? r : r - 1L;
    }

    private LuaString getLuaStr() {
        if(this.v <= 0xFFL && this.v >= 0xFFFFFFFFFFFFFF00L) {
            LuaString ret = LuaLong.luaStrings[((int)(this.v + 0x100L))];
            if(ret == null) {
                ret = LuaString.valueOf(this.getStr());
                LuaLong.luaStrings[((int)(this.v + 0x100L))] = ret;
            }
            return ret;
        }
        return LuaString.valueOf(this.getStr());
    }

    private String getStr() {
        if(this.v <= 0xFFL && this.v >= 0xFFFFFFFFFFFFFF00L) {
            String ret = LuaLong.strings[((int)(this.v + 0x100L))];
            if(ret == null) {
                ret = Long.toString(this.v);
                LuaLong.strings[((int)(this.v + 0x100L))] = ret;
            }
            return ret;
        }
        return Long.toString(this.v);
    }

    @Override  // luaj.LuaValue
    public boolean gt_b(LuaValue rhs) {
        if(!(rhs instanceof LuaNumber)) {
            return super.gt_b(rhs);
        }
        return rhs.islongnumber() ? this.v > rhs.tolong() : ((double)this.v) > rhs.todouble();
    }

    @Override  // luaj.LuaValue
    public boolean gteq_b(LuaValue rhs) {
        if(!(rhs instanceof LuaNumber)) {
            return super.gteq_b(rhs);
        }
        return rhs.islongnumber() ? this.v >= rhs.tolong() : ((double)this.v) >= rhs.todouble();
    }

    public static int hashCode(long x) [...] // Inlined contents

    @Override
    public int hashCode() {
        return (int)(this.v >>> 0x20 ^ this.v);
    }

    @Override  // luaj.LuaValue
    public LuaValue idiv(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.idiv(rhs);
        }
        return rhs.islong() ? LuaLong.valueOf(LuaLong.floorDiv(this.v, rhs.tolong())) : LuaDouble.didiv(this.v, rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public boolean isint() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean isintnumber() {
        return ((long)(((int)this.v))) == this.v;
    }

    @Override  // luaj.LuaValue
    public boolean isinttype() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean islong() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean islongnumber() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean lt_b(LuaValue rhs) {
        if(!(rhs instanceof LuaNumber)) {
            return super.lt_b(rhs);
        }
        return rhs.islongnumber() ? this.v < rhs.tolong() : ((double)this.v) < rhs.todouble();
    }

    @Override  // luaj.LuaValue
    public boolean lteq_b(LuaValue rhs) {
        if(!(rhs instanceof LuaNumber)) {
            return super.lteq_b(rhs);
        }
        return rhs.islongnumber() ? this.v <= rhs.tolong() : ((double)this.v) <= rhs.todouble();
    }

    public static long mod(long a, long b) {
        if(b == 0L) {
            throw new LuaError("attempt to perform \'n%0\' (as \'" + a + " % " + 0L + "\')");
        }
        long ret = a % b;
        if(ret != 0L) {
            int v3 = ret >= 0L ? 0 : 1;
            if(b < 0L) {
                return (1 ^ v3) == 0 ? ret : ret + b;
            }
            return v3 == 0 ? ret : ret + b;
        }
        return 0L;
    }

    @Override  // luaj.LuaValue
    public LuaValue mod(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.mod(rhs);
        }
        return rhs.islong() ? LuaLong.valueOf(LuaLong.mod(this.v, rhs.tolong())) : LuaDouble.dmod(this.v, rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.mul(rhs);
        }
        return rhs.islongnumber() ? LuaLong.valueOf(this.v * rhs.tolong()) : LuaLong.valueOf(((double)this.v) * rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue neg() {
        return LuaLong.valueOf(-this.v);
    }

    @Override  // luaj.LuaValue
    public double optdouble(double defval) {
        return (double)this.v;
    }

    @Override  // luaj.LuaValue
    public int optint(int defval) {
        return (int)this.v;
    }

    @Override  // luaj.LuaValue
    public LuaLong optinteger(LuaLong defval) {
        return this;
    }

    @Override  // luaj.LuaValue
    public String optjstring(String defval) {
        return this.getStr();
    }

    @Override  // luaj.LuaValue
    public long optlong(long defval) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public LuaString optstring(LuaString defval) {
        return this.getLuaStr();
    }

    @Override  // luaj.LuaValue
    public LuaValue pow(LuaValue rhs) {
        return !rhs.isnumber() ? super.pow(rhs) : LuaLong.valueOf(Math.pow(this.v, rhs.todouble()));
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaValue val) {
        if(!val.isnumber()) {
            return false;
        }
        return val.islongnumber() ? this.v == val.tolong() : ((double)this.v) == val.todouble();
    }

    public static LuaValue shl(long n, long m) {
        if(m < 0L) {
            return LuaLong.shr(n, -m);
        }
        return m > 0x3FL ? LuaLong.ZERO : LuaLong.valueOf(n << ((int)m));
    }

    @Override  // luaj.LuaValue
    public LuaValue shl(LuaValue rhs) {
        return rhs.islongnumber() ? LuaLong.shl(this.v, rhs.tolong()) : super.shl(rhs);
    }

    public static LuaValue shr(long n, long m) {
        if(m < 0L) {
            return LuaLong.shl(n, -m);
        }
        return m > 0x3FL ? LuaLong.ZERO : LuaLong.valueOf(n >>> ((int)m));
    }

    @Override  // luaj.LuaValue
    public LuaValue shr(LuaValue rhs) {
        return rhs.islongnumber() ? LuaLong.shr(this.v, rhs.tolong()) : super.shr(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaString strvalue() {
        return this.getLuaStr();
    }

    @Override  // luaj.LuaValue
    public LuaValue sub(LuaValue rhs) {
        if(!rhs.isnumber()) {
            return super.sub(rhs);
        }
        return rhs.islongnumber() ? LuaLong.valueOf(this.v - rhs.tolong()) : LuaLong.valueOf(((double)this.v) - rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public byte tobyte() {
        return (byte)(((int)this.v));
    }

    @Override  // luaj.LuaValue
    public char tochar() {
        return (char)(((int)this.v));
    }

    @Override  // luaj.LuaValue
    public double todouble() {
        return (double)this.v;
    }

    @Override  // luaj.LuaValue
    public float tofloat() {
        return (float)this.v;
    }

    @Override  // luaj.LuaValue
    public int toint() {
        return (int)this.v;
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        return this.getStr();
    }

    @Override  // luaj.LuaValue
    public long tolong() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public short toshort() {
        return (short)(((int)this.v));
    }

    @Override  // luaj.LuaValue
    public LuaValue tostring() {
        return this.getLuaStr();
    }

    @Override  // luaj.LuaValue
    public static LuaLong valueOf(long l) {
        if(l <= 0xFFL && l >= 0xFFFFFFFFFFFFFF00L) {
            LuaLong ret = LuaLong.longValues[((int)(l + 0x100L))];
            if(ret == null) {
                ret = new LuaLong(l);
                LuaLong.longValues[((int)(l + 0x100L))] = ret;
            }
            return ret;
        }
        if(l == 0x7FFFFFFFFFFFFFFFL) {
            return LuaLong.MAX_VALUE;
        }
        return l == 0x8000000000000000L ? LuaLong.MIN_VALUE : new LuaLong(l);
    }
}

