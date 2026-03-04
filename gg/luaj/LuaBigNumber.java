package luaj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class LuaBigNumber extends LuaNumber {
    public static BigDecimal MAX_INT;
    public static BigDecimal MAX_LONG;
    public static BigDecimal MAX_LONG_IN_DOUBLE;
    public static BigDecimal MIN_INT;
    public static BigDecimal MIN_LONG;
    public static BigDecimal MIN_LONG_IN_DOUBLE;
    public static int defaultScale;
    final BigDecimal v;

    static {
        LuaBigNumber.MAX_LONG = BigDecimal.valueOf(0x7FFFFFFFFFFFFFFFL);
        LuaBigNumber.MIN_LONG = BigDecimal.valueOf(0x8000000000000000L);
        LuaBigNumber.MAX_INT = BigDecimal.valueOf(0x7FFFFFFFL);
        LuaBigNumber.MIN_INT = BigDecimal.valueOf(0xFFFFFFFF80000000L);
        LuaBigNumber.MAX_LONG_IN_DOUBLE = BigDecimal.valueOf(0x20000000000000L);
        LuaBigNumber.MIN_LONG_IN_DOUBLE = BigDecimal.valueOf(0xFFE0000000000000L);
        LuaBigNumber.defaultScale = 20;
    }

    public LuaBigNumber(double f) {
        this.v = BigDecimal.valueOf(f);
    }

    public LuaBigNumber(int v) {
        this.v = BigDecimal.valueOf(v);
    }

    public LuaBigNumber(long v) {
        this.v = BigDecimal.valueOf(v);
    }

    public LuaBigNumber(String s) {
        this.v = new BigDecimal(s);
    }

    public LuaBigNumber(BigDecimal bigDecimal0) {
        this.v = bigDecimal0;
    }

    @Override  // luaj.LuaValue
    public LuaValue add(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.add(luaValue0) : new LuaBigNumber(this.v.add(bigDecimal0));
    }

    @Override  // luaj.LuaValue
    public LuaValue band(LuaValue luaValue0) {
        if(this.isbigint()) {
            if(luaValue0.isbignumber()) {
                if(((LuaBigNumber)luaValue0).isbigint()) {
                    return new LuaBigNumber(this.v.toBigIntegerExact().and(((LuaBigNumber)luaValue0).checkbignum().toBigInteger()).toString());
                }
            }
            else if(luaValue0.islongnumber()) {
                return new LuaBigNumber(this.v.toBigIntegerExact().and(BigInteger.valueOf(luaValue0.tolong())).toString());
            }
        }
        return super.band(luaValue0);
    }

    @Override  // luaj.LuaValue
    public static LuaBigNumber bignumberOf(double f) {
        return new LuaBigNumber(f);
    }

    @Override  // luaj.LuaValue
    public static LuaBigNumber bignumberOf(int v) {
        return new LuaBigNumber(v);
    }

    @Override  // luaj.LuaValue
    public static LuaBigNumber bignumberOf(long v) {
        return new LuaBigNumber(v);
    }

    @Override  // luaj.LuaValue
    public static LuaBigNumber bignumberOf(String s) {
        return new LuaBigNumber(s);
    }

    @Override  // luaj.LuaValue
    public static LuaBigNumber bignumberOf(BigDecimal bigDecimal0) {
        return new LuaBigNumber(bigDecimal0);
    }

    @Override  // luaj.LuaValue
    public LuaValue bnot() {
        return this.isbigint() ? new LuaBigNumber(this.v.toBigIntegerExact().not().toString()) : super.bnot();
    }

    @Override  // luaj.LuaValue
    public LuaValue bor(LuaValue luaValue0) {
        if(this.isbigint()) {
            if(luaValue0.isbignumber()) {
                if(((LuaBigNumber)luaValue0).isbigint()) {
                    return new LuaBigNumber(this.v.toBigIntegerExact().or(((LuaBigNumber)luaValue0).checkbignum().toBigInteger()).toString());
                }
            }
            else if(luaValue0.islongnumber()) {
                return new LuaBigNumber(this.v.toBigIntegerExact().or(BigInteger.valueOf(luaValue0.tolong())).toString());
            }
        }
        return super.bor(luaValue0);
    }

    @Override  // luaj.LuaValue
    public LuaValue bxor(LuaValue luaValue0) {
        if(this.isbigint()) {
            if(luaValue0.isbignumber()) {
                if(((LuaBigNumber)luaValue0).isbigint()) {
                    return new LuaBigNumber(this.v.toBigIntegerExact().xor(((LuaBigNumber)luaValue0).checkbignum().toBigInteger()).toString());
                }
            }
            else if(luaValue0.islongnumber()) {
                return new LuaBigNumber(this.v.toBigIntegerExact().xor(BigInteger.valueOf(luaValue0.tolong())).toString());
            }
        }
        return super.bxor(luaValue0);
    }

    @Override  // luaj.LuaValue
    public BigDecimal checkbignum() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber checkbignumber() {
        return this;
    }

    @Override  // luaj.LuaValue
    public double checkdouble() {
        return this.v.doubleValue();
    }

    @Override  // luaj.LuaValue
    public int checkint() {
        return this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public String checkjstring() {
        return String.valueOf(this.v);
    }

    @Override  // luaj.LuaValue
    public long checklong() {
        return this.v.longValue();
    }

    @Override  // luaj.LuaValue
    public LuaString checkstring() {
        return LuaBigNumber.valueOf(String.valueOf(this.v));
    }

    @Override  // luaj.LuaValue
    public LuaValue div(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.div(luaValue0) : new LuaBigNumber(this.v.divide(bigDecimal0, LuaBigNumber.defaultScale, RoundingMode.HALF_UP));
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 == null ? super.eq_b(luaValue0) : this.v.compareTo(bigDecimal0) == 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.eq_b(luaValue0) : this.v.compareTo(bigDecimal0) == 0;
    }

    @Override  // luaj.LuaValue
    public boolean equals(Object object0) {
        return object0 instanceof LuaBigNumber && ((LuaBigNumber)object0).v.compareTo(this.v) == 0;
    }

    private LuaString getLuaStr() {
        return LuaString.valueOf(this.getStr());
    }

    private String getStr() {
        return this.v == null ? "null" : this.v.toString();
    }

    @Override  // luaj.LuaValue
    public boolean gt_b(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 == null ? super.gt_b(luaValue0) : this.v.compareTo(bigDecimal0) > 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.gt_b(luaValue0) : this.v.compareTo(bigDecimal0) > 0;
    }

    @Override  // luaj.LuaValue
    public boolean gteq_b(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 == null ? super.gteq_b(luaValue0) : this.v.compareTo(bigDecimal0) >= 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.gteq_b(luaValue0) : this.v.compareTo(bigDecimal0) >= 0;
    }

    @Override
    public int hashCode() {
        return this.v.hashCode();
    }

    @Override  // luaj.LuaValue
    public LuaValue idiv(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.idiv(luaValue0) : new LuaBigNumber(this.v.divide(bigDecimal0, 0, RoundingMode.FLOOR));
    }

    public boolean isConvertible() {
        return this.v.compareTo(LuaBigNumber.MAX_LONG_IN_DOUBLE) <= 0 && this.v.compareTo(LuaBigNumber.MIN_LONG_IN_DOUBLE) >= 0;
    }

    public boolean isbigint() {
        try {
            if(new BigDecimal(this.v.toBigIntegerExact().toString()).compareTo(this.v) == 0) {
                return true;
            }
        }
        catch(Exception unused_ex) {
        }
        return false;
    }

    @Override  // luaj.LuaValue
    public boolean isbignumber() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean isintnumber() {
        return this.v.compareTo(LuaBigNumber.MAX_INT) <= 0 && this.v.compareTo(LuaBigNumber.MIN_INT) >= 0 && BigDecimal.valueOf(this.v.intValue()).compareTo(this.v) == 0;
    }

    @Override  // luaj.LuaValue
    public boolean islongnumber() {
        return this.v.compareTo(LuaBigNumber.MAX_LONG) <= 0 && this.v.compareTo(LuaBigNumber.MIN_LONG) >= 0 && BigDecimal.valueOf(this.v.longValue()).compareTo(this.v) == 0;
    }

    @Override  // luaj.LuaValue
    public boolean lt_b(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 == null ? super.lt_b(luaValue0) : this.v.compareTo(bigDecimal0) < 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.lt_b(luaValue0) : this.v.compareTo(bigDecimal0) < 0;
    }

    @Override  // luaj.LuaValue
    public boolean lteq_b(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 == null ? super.lteq_b(luaValue0) : this.v.compareTo(bigDecimal0) <= 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.lteq_b(luaValue0) : this.v.compareTo(bigDecimal0) <= 0;
    }

    @Override  // luaj.LuaValue
    public LuaValue mod(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.mod(luaValue0) : new LuaBigNumber(this.v.remainder(bigDecimal0));
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.mul(luaValue0) : new LuaBigNumber(this.v.multiply(bigDecimal0));
    }

    @Override  // luaj.LuaValue
    public LuaValue neg() {
        return new LuaBigNumber(this.v.negate());
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(double f) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(int v) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(long v) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(String s) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public BigDecimal optbignum(BigDecimal bigDecimal0) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(double f) {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(int v) {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(long v) {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(String s) {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaBigNumber optbignumber(BigDecimal bigDecimal0) {
        return this;
    }

    @Override  // luaj.LuaValue
    public double optdouble(double f) {
        return this.v.doubleValue();
    }

    @Override  // luaj.LuaValue
    public int optint(int v) {
        return this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public String optjstring(String s) {
        return this.getStr();
    }

    @Override  // luaj.LuaValue
    public long optlong(long v) {
        return this.v.longValue();
    }

    @Override  // luaj.LuaValue
    public LuaString optstring(LuaString luaString0) {
        return this.getLuaStr();
    }

    @Override  // luaj.LuaValue
    public LuaValue pow(LuaValue luaValue0) {
        if(luaValue0.isnumber() && luaValue0.isintnumber()) {
            int v = luaValue0.toint();
            return v < 0 ? new LuaBigNumber(BigDecimal.ONE.divide(this.v.pow(-v), LuaBigNumber.defaultScale, RoundingMode.HALF_UP)) : new LuaBigNumber(this.v.pow(v));
        }
        return super.pow(luaValue0);
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
            return bigDecimal0 != null && this.v.compareTo(bigDecimal0) == 0;
        }
        if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 != null && this.v.compareTo(bigDecimal0) == 0;
    }

    @Override  // luaj.LuaValue
    public LuaValue shl(LuaValue luaValue0) {
        if(luaValue0.isnumber()) {
            return luaValue0.isintnumber() ? new LuaBigNumber(this.v.toBigIntegerExact().shiftLeft(luaValue0.toint()).toString()) : super.pow(luaValue0);
        }
        return super.shl(luaValue0);
    }

    @Override  // luaj.LuaValue
    public LuaValue shr(LuaValue luaValue0) {
        if(luaValue0.isnumber()) {
            return luaValue0.isintnumber() ? new LuaBigNumber(this.v.toBigIntegerExact().shiftRight(luaValue0.toint()).toString()) : super.pow(luaValue0);
        }
        return super.shr(luaValue0);
    }

    @Override  // luaj.LuaValue
    public LuaString strvalue() {
        return this.getLuaStr();
    }

    @Override  // luaj.LuaValue
    public LuaValue sub(LuaValue luaValue0) {
        BigDecimal bigDecimal0 = null;
        if(luaValue0.isbignumber()) {
            bigDecimal0 = ((LuaBigNumber)luaValue0).v;
        }
        else if(luaValue0.isnumber()) {
            bigDecimal0 = luaValue0.islongnumber() ? BigDecimal.valueOf(luaValue0.tolong()) : BigDecimal.valueOf(luaValue0.todouble());
        }
        return bigDecimal0 == null ? super.sub(luaValue0) : new LuaBigNumber(this.v.subtract(bigDecimal0));
    }

    @Override  // luaj.LuaValue
    public byte tobyte() {
        return (byte)this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public char tochar() {
        return (char)this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public double todouble() {
        return this.v.doubleValue();
    }

    @Override  // luaj.LuaValue
    public float tofloat() {
        return this.v.floatValue();
    }

    @Override  // luaj.LuaValue
    public int toint() {
        return this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        return this.getStr();
    }

    @Override  // luaj.LuaValue
    public long tolong() {
        return this.v.longValue();
    }

    @Override  // luaj.LuaValue
    public short toshort() {
        return (short)this.v.intValue();
    }

    @Override  // luaj.LuaValue
    public LuaValue tostring() {
        return this.getLuaStr();
    }
}

