package luaj;

import luaj.lib.MathLib;

public class LuaDouble extends LuaNumber {
    public static final String JSTR_NAN = "nan";
    public static final String JSTR_NEGINF = "-inf";
    public static final String JSTR_POSINF = "inf";
    private static final double MAX_LONG = 9007199254740992.0;
    public static final LuaDouble NAN;
    public static final LuaDouble NEGINF;
    public static final LuaDouble POSINF;
    final double v;

    static {
        LuaDouble.NAN = new LuaDouble(NaN);
        LuaDouble.POSINF = new LuaDouble(Infinity);
        LuaDouble.NEGINF = new LuaDouble(-Infinity);
    }

    private LuaDouble(double d) {
        this.v = d;
    }

    @Override  // luaj.LuaValue
    public LuaValue add(double f) {
        return LuaDouble.valueOf(this.v + f);
    }

    @Override  // luaj.LuaValue
    public LuaValue add(LuaValue rhs) {
        return !rhs.isnumber() ? super.add(rhs) : LuaDouble.valueOf(this.v + rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue band(LuaValue rhs) {
        return this.noint("&", rhs) ? super.band(rhs) : LuaDouble.valueOf(this.tolong() & rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaValue bnot() {
        return !this.islongnumber() ? super.bnot() : LuaDouble.valueOf(~this.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaValue bor(LuaValue rhs) {
        return this.noint("|", rhs) ? super.bor(rhs) : LuaDouble.valueOf(this.tolong() | rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaValue bxor(LuaValue rhs) {
        return this.noint("~", rhs) ? super.bxor(rhs) : LuaDouble.valueOf(this.tolong() ^ rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public double checkdouble() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public int checkint() {
        int i = (int)(((long)this.v));
        if(((double)i) != this.v) {
            throw new LuaError("number \'" + this.tojstring() + "\' has no integer representation");
        }
        return i;
    }

    @Override  // luaj.LuaValue
    public LuaLong checkinteger() {
        return LuaLong.valueOf(((long)this.v));
    }

    @Override  // luaj.LuaValue
    public LuaValue checkinteger() {
        return LuaValue.valueOf(((int)(((long)this.v))));
    }

    @Override  // luaj.LuaValue
    public String checkjstring() {
        return this.tojstring();
    }

    @Override  // luaj.LuaValue
    public long checklong() {
        long i = (long)this.v;
        if(((double)i) != this.v) {
            throw new LuaError("number \'" + this.tojstring() + "\' has no integer representation");
        }
        return i;
    }

    @Override  // luaj.LuaValue
    public LuaString checkstring() {
        return LuaString.valueOf(this.tojstring());
    }

    public static LuaValue ddiv(double lhs, double rhs) {
        if(rhs != 0.0) {
            return LuaDouble.valueOf(lhs / rhs);
        }
        if(lhs > 0.0) {
            return LuaDouble.POSINF;
        }
        return lhs == 0.0 ? LuaDouble.NAN : LuaDouble.NEGINF;
    }

    public static double ddiv_d(double lhs, double rhs) {
        if(rhs != 0.0) {
            return lhs / rhs;
        }
        if(lhs > 0.0) {
            return Infinity;
        }
        return lhs == 0.0 ? NaN : -Infinity;
    }

    public static LuaValue didiv(double lhs, double rhs) {
        if(rhs != 0.0) {
            return LuaDouble.valueOf(Math.floor(lhs / rhs));
        }
        if(lhs > 0.0) {
            return LuaDouble.POSINF;
        }
        return lhs == 0.0 ? LuaDouble.NAN : LuaDouble.NEGINF;
    }

    @Override  // luaj.LuaValue
    public LuaValue div(double f) {
        return LuaDouble.ddiv(this.v, f);
    }

    @Override  // luaj.LuaValue
    public LuaValue div(int v) {
        return LuaDouble.ddiv(this.v, v);
    }

    @Override  // luaj.LuaValue
    public LuaValue div(LuaValue rhs) {
        return rhs.isnumber() ? LuaDouble.ddiv(this.v, rhs.todouble()) : super.div(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue divInto(double f) {
        return LuaDouble.ddiv(f, this.v);
    }

    public static LuaValue dmod(double lhs, double rhs) {
        if(rhs == 0.0 || lhs == Infinity || lhs == -Infinity) {
            return LuaDouble.NAN;
        }
        if(rhs == Infinity) {
            return lhs < 0.0 ? LuaDouble.POSINF : LuaDouble.valueOf(lhs);
        }
        if(rhs == -Infinity) {
            return lhs > 0.0 ? LuaDouble.NEGINF : LuaDouble.valueOf(lhs);
        }
        return LuaDouble.valueOf(lhs - Math.floor(lhs / rhs) * rhs);
    }

    public static double dmod_d(double lhs, double rhs) {
        if(rhs == 0.0 || lhs == Infinity || lhs == -Infinity) {
            return NaN;
        }
        if(rhs == Infinity) {
            return lhs < 0.0 ? Infinity : lhs;
        }
        if(rhs == -Infinity) {
            return lhs > 0.0 ? -Infinity : lhs;
        }
        return lhs - Math.floor(lhs / rhs) * rhs;
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue val) {
        return val.isnumber() ? this.v == val.todouble() : super.eq_b(val);
    }

    @Override  // luaj.LuaValue
    public boolean equals(Object o) {
        return o instanceof LuaDouble && Double.doubleToRawLongBits(((LuaDouble)o).v) == Double.doubleToRawLongBits(this.v);
    }

    @Override  // luaj.LuaValue
    public boolean gt_b(LuaValue rhs) {
        return rhs instanceof LuaNumber ? this.v > rhs.todouble() : super.gt_b(rhs);
    }

    @Override  // luaj.LuaValue
    public boolean gteq_b(LuaValue rhs) {
        return rhs instanceof LuaNumber ? this.v >= rhs.todouble() : super.gteq_b(rhs);
    }

    @Override
    public int hashCode() {
        int iv = (int)this.v;
        if(((double)iv) == this.v) {
            return (int)(((long)iv) >>> 0x20 ^ ((long)iv));
        }
        long x = Double.doubleToRawLongBits(this.v);
        return (int)(x >>> 0x20 ^ x);
    }

    @Override  // luaj.LuaValue
    public LuaValue idiv(LuaValue rhs) {
        return rhs.isnumber() ? LuaDouble.didiv(this.v, rhs.todouble()) : super.idiv(rhs);
    }

    @Override  // luaj.LuaValue
    public boolean isintnumber() {
        return ((double)(((int)this.v))) == this.v;
    }

    @Override  // luaj.LuaValue
    public boolean islongnumber() {
        return -9007199254740992.0 <= this.v && this.v <= 9007199254740992.0 && ((double)(((long)this.v))) == this.v;
    }

    @Override  // luaj.LuaValue
    public boolean isvalidkey() {
        return !Double.isNaN(this.v);
    }

    @Override  // luaj.LuaValue
    public boolean lt_b(LuaValue rhs) {
        return rhs instanceof LuaNumber ? this.v < rhs.todouble() : super.lt_b(rhs);
    }

    @Override  // luaj.LuaValue
    public boolean lteq_b(LuaValue rhs) {
        return rhs instanceof LuaNumber ? this.v <= rhs.todouble() : super.lteq_b(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue mod(LuaValue rhs) {
        return rhs.isnumber() ? LuaDouble.dmod(this.v, rhs.todouble()) : super.mod(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue modFrom(double f) {
        return LuaDouble.dmod(f, this.v);
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(double f) {
        return LuaDouble.valueOf(this.v * f);
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(int v) {
        return LuaDouble.valueOf(((double)v) * this.v);
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(LuaValue rhs) {
        return !rhs.isnumber() ? super.mul(rhs) : LuaDouble.valueOf(this.v * rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue neg() {
        return LuaDouble.valueOf(-this.v);
    }

    private boolean noint(String op, LuaValue rhs) {
        if(!rhs.isnumber()) {
            return true;
        }
        if(!this.islongnumber()) {
            throw new LuaError("number \'" + this.tojstring() + "\' has no integer representation (for bitwise operation \'" + this.tojstring() + " " + op + " " + rhs.tojstring() + "\')");
        }
        if(!rhs.islongnumber()) {
            throw new LuaError("number \'" + rhs.tojstring() + "\' has no integer representation (for bitwise operation \'" + this.tojstring() + " " + op + " " + rhs.tojstring() + "\')");
        }
        return false;
    }

    @Override  // luaj.LuaValue
    public double optdouble(double defval) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public int optint(int defval) {
        return (int)(((long)this.v));
    }

    @Override  // luaj.LuaValue
    public LuaLong optinteger(LuaLong defval) {
        return LuaLong.valueOf(((long)this.v));
    }

    @Override  // luaj.LuaValue
    public LuaValue optinteger(LuaValue luaValue0) {
        return LuaValue.valueOf(((int)(((long)this.v))));
    }

    @Override  // luaj.LuaValue
    public String optjstring(String defval) {
        return this.tojstring();
    }

    @Override  // luaj.LuaValue
    public long optlong(long defval) {
        return (long)this.v;
    }

    @Override  // luaj.LuaValue
    public LuaString optstring(LuaString defval) {
        return LuaString.valueOf(this.tojstring());
    }

    @Override  // luaj.LuaValue
    public LuaValue pow(LuaValue rhs) {
        return !rhs.isnumber() ? super.pow(rhs) : LuaDouble.valueOf(Math.pow(this.v, rhs.todouble()));
    }

    @Override  // luaj.LuaValue
    public LuaValue powWith(double f) {
        return MathLib.dpow(f, this.v);
    }

    @Override  // luaj.LuaValue
    public LuaValue powWith(int v) {
        return MathLib.dpow(v, this.v);
    }

    @Override  // luaj.LuaValue
    public boolean raweq(double f) {
        return this.v == f;
    }

    @Override  // luaj.LuaValue
    public boolean raweq(int v) {
        return this.v == ((double)v);
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaValue val) {
        return val.isnumber() ? this.v == val.todouble() : false;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public LuaValue shl(LuaValue rhs) {
        return this.noint("<<", rhs) ? super.shl(rhs) : LuaLong.shl(this.tolong(), rhs.tolong());
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public LuaValue shr(LuaValue rhs) {
        return this.noint(">>", rhs) ? super.shr(rhs) : LuaLong.shr(this.tolong(), rhs.tolong());
    }

    @Override  // luaj.LuaValue
    public LuaString strvalue() {
        return LuaString.valueOf(this.tojstring());
    }

    @Override  // luaj.LuaValue
    public LuaValue sub(LuaValue rhs) {
        return !rhs.isnumber() ? super.sub(rhs) : LuaDouble.valueOf(this.v - rhs.todouble());
    }

    @Override  // luaj.LuaValue
    public LuaValue subFrom(double f) {
        return LuaDouble.valueOf(f - this.v);
    }

    @Override  // luaj.LuaValue
    public byte tobyte() {
        return (byte)(((int)(((long)this.v))));
    }

    @Override  // luaj.LuaValue
    public char tochar() {
        return (char)(((int)(((long)this.v))));
    }

    @Override  // luaj.LuaValue
    public double todouble() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public float tofloat() {
        return (float)this.v;
    }

    @Override  // luaj.LuaValue
    public int toint() {
        return (int)(((long)this.v));
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        String ret;
        if(this.v == 0.0) {
            return Double.doubleToLongBits(this.v) >> 0x3F == 0L ? "0.0" : "-0.0";
        }
        if(Double.isNaN(this.v)) {
            return "nan";
        }
        if(Double.isInfinite(this.v)) {
            return this.v < 0.0 ? "-inf" : "inf";
        }
        long l = (long)this.v;
        if(((double)l) == this.v) {
            ret = Long.toString(l);
            return ret.indexOf(46) != -1 || ret.indexOf(101) != -1 || ret.indexOf(69) != -1 ? ret : ret + ".0";
        }
        ret = Double.toString(this.v);
        return ret.indexOf(46) != -1 || ret.indexOf(101) != -1 || ret.indexOf(69) != -1 ? ret : ret + ".0";
    }

    @Override  // luaj.LuaValue
    public long tolong() {
        return (long)this.v;
    }

    @Override  // luaj.LuaValue
    public short toshort() {
        return (short)(((int)(((long)this.v))));
    }

    @Override  // luaj.LuaValue
    public LuaValue tostring() {
        return LuaString.valueOf(this.tojstring());
    }

    @Override  // luaj.LuaValue
    public static LuaNumber valueOf(double d) {
        return new LuaDouble(d);
    }
}

