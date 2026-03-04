package luaj;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import luaj.lib.MathLib;

public class LuaString extends LuaValue {
    static class DeprecatedLuaString extends LuaString {
        public DeprecatedLuaString(byte[] bytes, int offset, int length) {
            super(bytes, offset, length, null);
        }

        @Override  // luaj.LuaValue
        public boolean isDeprecated() {
            return true;
        }
    }

    static final class RecentShortStrings {
        static final LuaString[] recent_short_strings;

        static {
            RecentShortStrings.recent_short_strings = new LuaString[0x80];
        }
    }

    static final int RECENT_STRINGS_CACHE_SIZE = 0x80;
    static final int RECENT_STRINGS_MAX_LENGTH = 0x20;
    public final byte[] m_bytes;
    public final int m_hashcode;
    public final int m_length;
    public final int m_offset;
    private LuaValue numValue;
    private static final boolean[] numeral;
    public static LuaValue s_metatable;

    static {
        LuaString.numeral = new boolean[0x80];
        for(int i = 0x30; i <= 57; i = (byte)(i + 1)) {
            LuaString.numeral[i] = true;
        }
        for(int i = 65; i <= 70; i = (byte)(i + 1)) {
            LuaString.numeral[i] = true;
        }
        for(int i = 97; i <= 102; i = (byte)(i + 1)) {
            LuaString.numeral[i] = true;
        }
        LuaString.numeral[13] = true;
        LuaString.numeral[10] = true;
        LuaString.numeral[9] = true;
        LuaString.numeral[0x20] = true;
        LuaString.numeral[43] = true;
        LuaString.numeral[45] = true;
        LuaString.numeral[46] = true;
        LuaString.numeral[80] = true;
        LuaString.numeral[0x70] = true;
        LuaString.numeral[88] = true;
        LuaString.numeral[120] = true;
    }

    private LuaString(byte[] bytes, int offset, int length) {
        this.numValue = null;
        this.m_bytes = bytes;
        this.m_offset = offset;
        this.m_length = length;
        this.m_hashcode = LuaString.hashCode(bytes, offset, length);
    }

    LuaString(byte[] arr_b, int v, int v1, LuaString luaString0) {
        this(arr_b, v, v1);
    }

    @Override  // luaj.LuaValue
    public LuaValue add(double f) {
        return LuaValue.valueOf(this.checkarith() + f);
    }

    @Override  // luaj.LuaValue
    public LuaValue add(int v) {
        return LuaValue.valueOf(this.checkarith() + ((double)v));
    }

    @Override  // luaj.LuaValue
    public LuaValue add(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.add(rhs) : luaValue1.add(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue band(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.band(rhs) : luaValue1.band(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue bnot() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.bnot() : luaValue0.bnot();
    }

    @Override  // luaj.LuaValue
    public LuaValue bor(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.bor(rhs) : luaValue1.bor(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue bxor(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.bxor(rhs) : luaValue1.bxor(rhs);
    }

    private boolean byteseq(byte[] bytes, int off, int len) {
        return this.m_length == len && LuaString.equals(this.m_bytes, this.m_offset, bytes, off, len);
    }

    public int charAt(int index) {
        if(index < 0 || index >= this.m_length) {
            throw new IndexOutOfBoundsException();
        }
        return this.luaByte(index);
    }

    private double checkarith() {
        double f = this.scannumber();
        if(Double.isNaN(f)) {
            this.aritherror();
        }
        return f;
    }

    private LuaValue checkarith() {
        LuaValue luaValue0 = this.getNum();
        if(luaValue0.isnil()) {
            this.aritherror();
        }
        return luaValue0;
    }

    @Override  // luaj.LuaValue
    public double checkdouble() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checkdouble() : luaValue0.checkdouble();
    }

    @Override  // luaj.LuaValue
    public int checkint() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checkint() : luaValue0.checkint();
    }

    @Override  // luaj.LuaValue
    public LuaLong checkinteger() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checkinteger() : luaValue0.checkinteger();
    }

    @Override  // luaj.LuaValue
    public LuaValue checkinteger() {
        return LuaValue.valueOf(this.checkint());
    }

    @Override  // luaj.LuaValue
    public String checkjstring() {
        return this.tojstring();
    }

    @Override  // luaj.LuaValue
    public long checklong() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checklong() : luaValue0.checklong();
    }

    @Override  // luaj.LuaValue
    public LuaNumber checknumber() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checknumber() : luaValue0.checknumber();
    }

    @Override  // luaj.LuaValue
    public LuaNumber checknumber(String msg) {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.checknumber(msg) : luaValue0.checknumber(msg);
    }

    @Override  // luaj.LuaValue
    public LuaString checkstring() [...] // Inlined contents

    @Override  // luaj.LuaValue
    public LuaValue concat(LuaValue rhs) {
        return rhs.concatTo(this);
    }

    @Override  // luaj.LuaValue
    public LuaValue concatTo(LuaNumber lhs) {
        return this.concatTo(lhs.strvalue());
    }

    @Override  // luaj.LuaValue
    public LuaValue concatTo(LuaString lhs) {
        byte[] b = new byte[lhs.m_length + this.m_length];
        System.arraycopy(lhs.m_bytes, lhs.m_offset, b, 0, lhs.m_length);
        System.arraycopy(this.m_bytes, this.m_offset, b, lhs.m_length, this.m_length);
        return LuaString.valueUsing(b, 0, b.length);
    }

    public void copyInto(int strOffset, byte[] bytes, int arrayOffset, int len) {
        System.arraycopy(this.m_bytes, this.m_offset + strOffset, bytes, arrayOffset, len);
    }

    public static String decodeAsUtf8(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length);
    }

    @Override  // luaj.LuaValue
    public LuaValue div(double f) {
        return LuaDouble.ddiv(this.checkarith(), f);
    }

    @Override  // luaj.LuaValue
    public LuaValue div(int v) {
        return LuaDouble.ddiv(this.checkarith(), v);
    }

    @Override  // luaj.LuaValue
    public LuaValue div(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.div(rhs) : luaValue1.div(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue divInto(double f) {
        return LuaDouble.ddiv(f, this.checkarith());
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue val) {
        return val.raweq(this);
    }

    public static boolean equals(LuaString a, int i, LuaString b, int j, int n) {
        return LuaString.equals(a.m_bytes, a.m_offset + i, b.m_bytes, b.m_offset + j, n);
    }

    public static boolean equals(byte[] a, int i, byte[] b, int j, int n) {
        if(a.length >= i + n && b.length >= j + n) {
            while(true) {
                int j = j;
                int i = i;
                --n;
                if(n < 0) {
                    return true;
                }
                i = i + 1;
                j = j + 1;
                if(a[i] != b[j]) {
                    break;
                }
            }
        }
        return false;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean equals(Object o) {
        return o instanceof LuaString ? this.raweq(((LuaString)o)) : false;
    }

    private LuaValue getNum() {
        LuaValue n = this.numValue;
        if(n == null) {
            n = this.tonumber();
            this.numValue = n;
        }
        return n;
    }

    private static LuaString getSpecial(String s) {
        int v = s == null ? 0 : s.length();
        if(v == 0) {
            return LuaString.EMPTYSTRING;
        }
        if(v == 1) {
            int v1 = s.charAt(0);
            if(v1 == 0x30) {
                return LuaString.ZEROSTRING;
            }
            return v1 == 49 ? LuaString.ONESTRING : null;
        }
        return null;
    }

    private static LuaString getSpecial(byte[] bytes, int off, int len) {
        if(len == 0) {
            return LuaString.EMPTYSTRING;
        }
        if(len == 1) {
            switch(bytes[off]) {
                case 0x30: {
                    return LuaString.ZEROSTRING;
                }
                case 49: {
                    return LuaString.ONESTRING;
                }
                default: {
                    return null;
                }
            }
        }
        return null;
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return LuaString.s_metatable;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean gt_b(LuaValue rhs) {
        return rhs.isstring() ? rhs.strcmp(this) < 0 : super.gt_b(rhs);
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean gteq_b(LuaValue rhs) {
        return rhs.isstring() ? rhs.strcmp(this) <= 0 : super.gteq_b(rhs);
    }

    public static int hashCode(byte[] bytes, int offset, int length) {
        int h = length;
        int step = (length >> 5) + 1;
        for(int l1 = length; l1 >= step; l1 -= step) {
            h ^= (h << 5) + (h >> 2) + (bytes[offset + l1 - 1] & 0xFF);
        }
        return h >>> 6 | h << 26;
    }

    @Override
    public int hashCode() {
        return this.m_hashcode;
    }

    @Override  // luaj.LuaValue
    public LuaValue idiv(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.idiv(rhs) : luaValue1.idiv(rhs);
    }

    public int indexOf(byte b, int start) {
        while(true) {
            if(start >= this.m_length) {
                return -1;
            }
            if(this.m_bytes[this.m_offset + start] == b) {
                break;
            }
            ++start;
        }
        return start;
    }

    public int indexOf(LuaString s, int start) {
        int v1 = s.length();
        int limit = this.m_length - v1;
        int i;
        for(i = start; true; ++i) {
            if(i > limit) {
                return -1;
            }
            if(LuaString.equals(this.m_bytes, this.m_offset + i, s.m_bytes, s.m_offset, v1)) {
                break;
            }
        }
        return i;
    }

    public int indexOfAny(LuaString accept) {
        int j;
        int ilimit = this.m_offset + this.m_length;
        int jlimit = accept.m_offset + accept.m_length;
        int i;
        for(i = this.m_offset; true; ++i) {
            if(i >= ilimit) {
                return -1;
            }
            j = accept.m_offset;
        label_6:
            if(j < jlimit) {
                break;
            }
        }
        if(this.m_bytes[i] != accept.m_bytes[j]) {
            ++j;
            goto label_6;
        }
        return i - this.m_offset;
    }

    public boolean isValidUtf8() {
        byte[] m_bytes = this.m_bytes;
        int j = this.m_offset + this.m_length;
        int i = this.m_offset;
        while(true) {
            if(i >= j) {
                return true;
            }
            int i = i + 1;
            int c = m_bytes[i];
            if(c >= 0) {
                i = i;
            }
            else {
                if((c & 0xE0) != 0xC0 || i >= j) {
                    i = i;
                }
                else {
                    i = i + 1;
                    if((m_bytes[i] & 0xC0) == 0x80) {
                        continue;
                    }
                }
                if((c & 0xF0) == 0xE0 && i + 1 < j) {
                    int i = i + 1;
                    if((m_bytes[i] & 0xC0) == 0x80) {
                        i = i + 1;
                        if((m_bytes[i] & 0xC0) == 0x80) {
                            continue;
                        }
                    }
                    else {
                        i = i;
                    }
                }
                if((c & 0xF8) != 0xF0 || i + 2 >= j || (m_bytes[i] & 0xC0) != 0x80 || (m_bytes[i + 1] & 0xC0) != 0x80 || (m_bytes[i + 2] & 0xC0) != 0x80) {
                    break;
                }
                i += 3;
            }
        }
        return false;
    }

    @Override  // luaj.LuaValue
    public boolean isint() {
        return this.getNum().isint();
    }

    @Override  // luaj.LuaValue
    public boolean islong() {
        return this.getNum().islong();
    }

    @Override  // luaj.LuaValue
    public boolean islongnumber() {
        return this.getNum().islongnumber();
    }

    @Override  // luaj.LuaValue
    public boolean isnumber() {
        return this.getNum().isnumber();
    }

    @Override  // luaj.LuaValue
    public boolean isstring() {
        return true;
    }

    public int lastIndexOf(LuaString s) {
        int v = s.length();
        int i;
        for(i = this.m_length - v; true; --i) {
            if(i < 0) {
                return -1;
            }
            if(LuaString.equals(this.m_bytes, this.m_offset + i, s.m_bytes, s.m_offset, v)) {
                break;
            }
        }
        return i;
    }

    @Override  // luaj.LuaValue
    public LuaValue len() {
        return LuaLong.valueOf(this.m_length);
    }

    @Override  // luaj.LuaValue
    public int length() {
        return this.m_length;
    }

    public static int lengthAsUtf8(char[] chars) {
        return new String(chars).getBytes().length;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean lt_b(LuaValue rhs) {
        return rhs.isstring() ? rhs.strcmp(this) > 0 : super.lt_b(rhs);
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean lteq_b(LuaValue rhs) {
        return rhs.isstring() ? rhs.strcmp(this) >= 0 : super.lteq_b(rhs);
    }

    public int luaByte(int index) {
        return this.m_bytes[this.m_offset + index] & 0xFF;
    }

    public LuaString makeDeprecated() {
        return new DeprecatedLuaString(this.m_bytes, this.m_offset, this.m_length);
    }

    @Override  // luaj.LuaValue
    public LuaValue mod(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.mod(rhs) : luaValue1.mod(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue modFrom(double f) {
        return LuaDouble.dmod(f, this.checkarith());
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(double f) {
        return LuaValue.valueOf(this.checkarith() * f);
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(int v) {
        return LuaValue.valueOf(this.checkarith() * ((double)v));
    }

    @Override  // luaj.LuaValue
    public LuaValue mul(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.mul(rhs) : luaValue1.mul(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue neg() {
        LuaValue luaValue0 = this.getNum();
        return luaValue0.isnil() ? super.neg() : luaValue0.neg();
    }

    @Override  // luaj.LuaValue
    public double optdouble(double defval) {
        return this.checkdouble();
    }

    @Override  // luaj.LuaValue
    public int optint(int defval) {
        return this.checkint();
    }

    @Override  // luaj.LuaValue
    public LuaLong optinteger(LuaLong defval) {
        return this.checkinteger();
    }

    @Override  // luaj.LuaValue
    public LuaValue optinteger(LuaValue luaValue0) {
        return this.checkinteger();
    }

    @Override  // luaj.LuaValue
    public String optjstring(String defval) {
        return this.tojstring();
    }

    @Override  // luaj.LuaValue
    public long optlong(long defval) {
        return this.checklong();
    }

    @Override  // luaj.LuaValue
    public LuaNumber optnumber(LuaNumber defval) {
        return this.checknumber();
    }

    @Override  // luaj.LuaValue
    public LuaString optstring(LuaString defval) {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaValue pow(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.pow(rhs) : luaValue1.pow(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue powWith(double f) {
        return MathLib.dpow(f, this.checkarith());
    }

    @Override  // luaj.LuaValue
    public LuaValue powWith(int v) {
        return MathLib.dpow(v, this.checkarith());
    }

    static LuaString rawValueOf(String string) {
        byte[] arr_b = string.getBytes();
        return new LuaString(arr_b, 0, arr_b.length);
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaString s) {
        if(this != s) {
            if(s.m_length != this.m_length) {
                return false;
            }
            if(s.m_bytes != this.m_bytes || s.m_offset != this.m_offset) {
                if(s.hashCode() != this.hashCode()) {
                    return false;
                }
                for(int i = 0; i < this.m_length; ++i) {
                    if(s.m_bytes[s.m_offset + i] != this.m_bytes[this.m_offset + i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaValue val) {
        return val.raweq(this);
    }

    @Override  // luaj.LuaValue
    public int rawlen() {
        return this.m_length;
    }

    private double scandouble(int v, int v1) {
        if(v1 > v + 0x40) {
            v1 = v + 0x40;
        }
        int v2 = v;
        while(true) {
            if(v2 >= v1) {
                char[] arr_c = new char[v1 - v];
                for(int v3 = v; v3 < v1; ++v3) {
                    arr_c[v3 - v] = (char)this.m_bytes[v3];
                }
                try {
                    return Double.parseDouble(new String(arr_c));
                }
                catch(Exception unused_ex) {
                    return Double.NaN;
                }
            }
            switch(this.m_bytes[v2]) {
                case 43: 
                case 45: 
                case 46: 
                case 0x30: 
                case 49: 
                case 50: 
                case 51: 
                case 52: 
                case 53: 
                case 54: 
                case 55: 
                case 56: 
                case 57: 
                case 69: 
                case 101: {
                    ++v2;
                    break;
                }
                default: {
                    return Double.NaN;
                }
            }
        }
    }

    private double scanlong(int v, int v1, int v2) {
        int v5;
        long v3 = 0L;
        boolean z = this.m_bytes[v1] == 45;
        if(z) {
            ++v1;
        }
        while(true) {
            if(v1 >= v2) {
                if(z) {
                    v3 = -v3;
                }
                return (double)v3;
            }
            int v4 = this.m_bytes[v1];
            if(v > 10 && (this.m_bytes[v1] < 0x30 || this.m_bytes[v1] > 57)) {
                v5 = this.m_bytes[v1] < 65 || this.m_bytes[v1] > 90 ? 87 : 55;
            }
            else {
                v5 = 0x30;
            }
            int v6 = v4 - v5;
            if(v6 < 0 || v6 >= v) {
                return Double.NaN;
            }
            v3 = v3 * ((long)v) + ((long)v6);
            if(v3 < 0L) {
                return Double.NaN;
            }
            ++v1;
        }
    }

    public double scannumber() {
        int v = this.m_offset + this.m_length;
        int v1;
        for(v1 = this.m_offset; v1 < v && this.m_bytes[v1] == 0x20; ++v1) {
        }
        int v2;
        for(v2 = v; v1 < v2 && this.m_bytes[v2 - 1] == 0x20; --v2) {
        }
        if(v1 >= v2) {
            return Double.NaN;
        }
        if(this.m_bytes[v1] == 0x30 && v1 + 1 < v2) {
            switch(this.m_bytes[v1 + 1]) {
                case 88: 
                case 120: {
                    return this.scanlong(16, v1 + 2, v2);
                }
            }
        }
        double f = this.scanlong(10, v1, v2);
        return Double.isNaN(f) ? this.scandouble(v1, v2) : f;
    }

    public double scannumber(int v) {
        if(v < 2 || v > 36) {
            return Double.NaN;
        }
        int v1 = this.m_offset;
        int v2 = this.m_offset + this.m_length;
        while(v1 < v2 && this.m_bytes[v1] == 0x20) {
            ++v1;
        }
        while(v1 < v2 && this.m_bytes[v2 - 1] == 0x20) {
            --v2;
        }
        return v1 < v2 ? this.scanlong(v, v1, v2) : Double.NaN;
    }

    @Override  // luaj.LuaValue
    public LuaValue shl(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.shl(rhs) : luaValue1.shl(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue shr(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.shr(rhs) : luaValue1.shr(rhs);
    }

    @Override  // luaj.LuaValue
    public int strcmp(LuaString rhs) {
        int i = 0;
        for(int j = 0; true; ++j) {
            if(i >= this.m_length || j >= rhs.m_length) {
                return this.m_length - rhs.m_length;
            }
            if(this.m_bytes[this.m_offset + i] != rhs.m_bytes[rhs.m_offset + j]) {
                return this.m_bytes[this.m_offset + i] - rhs.m_bytes[rhs.m_offset + j];
            }
            ++i;
        }
    }

    @Override  // luaj.LuaValue
    public int strcmp(LuaValue lhs) {
        return -lhs.strcmp(this);
    }

    @Override  // luaj.LuaValue
    public LuaString strvalue() {
        return this;
    }

    @Override  // luaj.LuaValue
    public LuaValue sub(LuaValue rhs) {
        LuaValue luaValue1 = this.getNum();
        return luaValue1.isnil() ? super.sub(rhs) : luaValue1.sub(rhs);
    }

    @Override  // luaj.LuaValue
    public LuaValue subFrom(double f) {
        return LuaValue.valueOf(f - this.checkarith());
    }

    public LuaString substring(int beginIndex, int endIndex) {
        int off = this.m_offset + beginIndex;
        int len = endIndex - beginIndex;
        return len < this.m_length / 2 ? LuaString.valueOf(this.m_bytes, off, len) : LuaString.valueUsing(this.m_bytes, off, len);
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.m_bytes, this.m_offset, this.m_length);
    }

    @Override  // luaj.LuaValue
    public byte tobyte() {
        return this.getNum().tobyte();
    }

    @Override  // luaj.LuaValue
    public char tochar() {
        return this.getNum().tochar();
    }

    @Override  // luaj.LuaValue
    public double todouble() {
        return this.getNum().todouble();
    }

    @Override  // luaj.LuaValue
    public float tofloat() {
        return this.getNum().tofloat();
    }

    @Override  // luaj.LuaValue
    public int toint() {
        return this.getNum().toint();
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        return LuaString.decodeAsUtf8(this.m_bytes, this.m_offset, this.m_length);
    }

    @Override  // luaj.LuaValue
    public long tolong() {
        return this.getNum().tolong();
    }

    @Override  // luaj.LuaValue
    public LuaValue tonumber() {
        byte[] bytes = this.m_bytes;
        int offset = this.m_offset;
        int length = this.m_length;
        boolean[] num = LuaString.numeral;
        for(int i = 0; true; ++i) {
            if(i >= length) {
                try {
                    return LuaValue.parseNumber(new String(bytes, offset, length), LuaString.NIL);
                }
                catch(Throwable unused_ex) {
                    return LuaString.NIL;
                }
            }
            int ch = bytes[offset + i];
            if(ch < 9 || !num[ch]) {
                return LuaString.NIL;
            }
        }
    }

    public LuaValue tonumber(int radix) {
        int v7;
        byte[] m_bytes = this.m_bytes;
        int offset = this.m_offset;
        int end = offset + this.m_length;
        while(offset < end && m_bytes[offset] <= 0x20 && m_bytes[offset] > 0) {
            ++offset;
        }
        while(offset < end && m_bytes[end - 1] <= 0x20 && m_bytes[end - 1] > 0) {
            --end;
        }
        if(offset == end) {
            return LuaString.NIL;
        }
        int firstByte = m_bytes[offset];
        if(firstByte == 43 || firstByte == 45) {
            ++offset;
        }
        if(offset == end) {
            return LuaString.NIL;
        }
        long result = 0L;
        for(int offset = offset; true; ++offset) {
            if(offset >= end) {
                if(firstByte != 45) {
                    result = -result;
                    if(result < 0L) {
                        return LuaString.NIL;
                    }
                }
                return LuaLong.valueOf(result);
            }
            int digit = m_bytes[offset];
            if(radix > 10 && (digit < 0x30 || digit > 57)) {
                v7 = digit < 65 || digit > 90 ? 87 : 55;
            }
            else {
                v7 = 0x30;
            }
            int digit = digit - v7;
            if(digit < 0 || digit >= radix) {
                return LuaString.NIL;
            }
            if(0x8000000000000000L / ((long)radix) > result) {
                return LuaString.NIL;
            }
            long next = ((long)radix) * result - ((long)digit);
            if(next > result) {
                return LuaString.NIL;
            }
            result = next;
        }
    }

    @Override  // luaj.LuaValue
    public short toshort() {
        return this.getNum().toshort();
    }

    @Override  // luaj.LuaValue
    public LuaValue tostring() {
        return this;
    }

    @Override  // luaj.LuaValue
    public int type() {
        return 4;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "string";
    }

    private static LuaString valueFromCopy(byte[] bytes, int off, int len) {
        byte[] copy = new byte[len];
        System.arraycopy(bytes, off, copy, 0, len);
        return new LuaString(copy, 0, len);
    }

    @Override  // luaj.LuaValue
    public static LuaString valueOf(String string) {
        LuaString special = LuaString.getSpecial(string);
        if(special != null) {
            return special;
        }
        byte[] arr_b = string.getBytes();
        return LuaString.valueUsing(arr_b, 0, arr_b.length);
    }

    @Override  // luaj.LuaValue
    public static LuaString valueOf(byte[] bytes) {
        return LuaString.valueOf(bytes, 0, bytes.length);
    }

    @Override  // luaj.LuaValue
    public static LuaString valueOf(byte[] bytes, int off, int len) {
        LuaString special = LuaString.getSpecial(bytes, off, len);
        if(special != null) {
            return special;
        }
        if(len > 0x20) {
            return LuaString.valueFromCopy(bytes, off, len);
        }
        int v2 = LuaString.hashCode(bytes, off, len);
        LuaString t = RecentShortStrings.recent_short_strings[v2 & 0x7F];
        if(t != null && t.m_hashcode == v2 && t.byteseq(bytes, off, len)) {
            return t;
        }
        LuaString luaString2 = LuaString.valueFromCopy(bytes, off, len);
        RecentShortStrings.recent_short_strings[v2 & 0x7F] = luaString2;
        return luaString2;
    }

    public static LuaString valueOf(char[] bytes) {
        return LuaString.valueOf(bytes, 0, bytes.length);
    }

    public static LuaString valueOf(char[] bytes, int off, int len) {
        byte[] b = new byte[len];
        for(int i = 0; i < len; ++i) {
            b[i] = (byte)bytes[i + off];
        }
        return LuaString.valueUsing(b, 0, len);
    }

    public static LuaString valueUsing(byte[] bytes) {
        return LuaString.valueUsing(bytes, 0, bytes.length);
    }

    public static LuaString valueUsing(byte[] bytes, int off, int len) {
        LuaString special = LuaString.getSpecial(bytes, off, len);
        if(special != null) {
            return special;
        }
        if(bytes.length > 0x20) {
            return new LuaString(bytes, off, len);
        }
        int v2 = LuaString.hashCode(bytes, off, len);
        LuaString t = RecentShortStrings.recent_short_strings[v2 & 0x7F];
        if(t != null && t.m_hashcode == v2 && t.byteseq(bytes, off, len)) {
            return t;
        }
        LuaString s = new LuaString(bytes, off, len);
        RecentShortStrings.recent_short_strings[v2 & 0x7F] = s;
        return s;
    }

    public void write(DataOutputStream writer, int i, int len) throws IOException {
        writer.write(this.m_bytes, this.m_offset + i, len);
    }
}

