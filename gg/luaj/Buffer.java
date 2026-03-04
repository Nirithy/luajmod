package luaj;

public final class Buffer {
    private static final int DEFAULT_CAPACITY = 0x40;
    private static final int MAXUNICODE = 0x10FFFF;
    private static final byte[] NOBYTES;
    private byte[] bytes;
    private int length;
    private int offset;
    private LuaValue value;

    static {
        Buffer.NOBYTES = new byte[0];
    }

    public Buffer() {
        this(0x40);
    }

    public Buffer(int initialCapacity) {
        this.bytes = new byte[initialCapacity];
        this.length = 0;
        this.offset = 0;
        this.value = null;
    }

    public Buffer(LuaValue value) {
        this.bytes = Buffer.NOBYTES;
        this.offset = 0;
        this.length = 0;
        this.value = value;
    }

    public final Buffer append(byte b) {
        this.makeroom(0, 1);
        int v = this.length;
        this.length = v + 1;
        this.bytes[this.offset + v] = b;
        return this;
    }

    public final Buffer append(String str) {
        byte[] arr_b = str.getBytes();
        return this.append(arr_b, 0, arr_b.length);
    }

    public final Buffer append(LuaString str) {
        this.makeroom(0, str.m_length);
        str.copyInto(0, this.bytes, this.offset + this.length, str.m_length);
        this.length += str.m_length;
        return this;
    }

    public final Buffer append(LuaValue val) {
        this.append(val.strvalue());
        return this;
    }

    public final Buffer append(byte[] b, int start, int n) {
        this.makeroom(0, n);
        System.arraycopy(b, start, this.bytes, this.offset + this.length, n);
        this.length += n;
        return this;
    }

    // This method was un-flattened
    public final Buffer appendCodePoint(int cp) {
        int j;
        byte[] bytes;
        int offset;
        int m;
        int s;
        if(cp < 0 || cp > 0x10FFFF) {
            throw new IllegalArgumentException("Code point (" + cp + ") out of range [0; " + 0x10FFFF + "]");
        }
        if(cp <= 0x7F) {
            s = 1;
            m = 0;
            this.makeroom(0, 1);
            offset = this.offset + this.length;
            bytes = this.bytes;
        }
        else if(cp <= 0x7FF) {
            s = 2;
            m = 0xC0;
            this.makeroom(0, 2);
            offset = this.offset + this.length;
            bytes = this.bytes;
            bytes[offset + 1] = (byte)(cp & 0x3F | 0x80);
            cp >>= 6;
        }
        else {
            if(cp <= 0xFFFF) {
                s = 3;
                m = 0xE0;
                this.makeroom(0, 3);
                offset = this.offset + this.length;
                bytes = this.bytes;
                bytes[offset + 2] = (byte)(cp & 0x3F | 0x80);
                cp >>= 6;
                j = 1;
            }
            else {
                s = 4;
                m = 0xF0;
                this.makeroom(0, 4);
                offset = this.offset + this.length;
                bytes = this.bytes;
                bytes[offset + 3] = (byte)(cp & 0x3F | 0x80);
                cp >>= 6;
                j = 2;
            }
            while(true) {
                bytes[offset + j] = (byte)(cp & 0x3F | 0x80);
                cp >>= 6;
                --j;
                if(j <= 0) {
                    break;
                }
            }
        }
        bytes[offset] = (byte)(cp | m);
        this.length += s;
        return this;
    }

    public Buffer concatTo(LuaNumber lhs) {
        return this.value == null || this.value.isstring() ? this.prepend(lhs.strvalue()) : this.setvalue(lhs.concat(this.value));
    }

    public Buffer concatTo(LuaString lhs) {
        return this.value == null || this.value.isstring() ? this.prepend(lhs) : this.setvalue(lhs.concat(this.value));
    }

    public Buffer concatTo(LuaValue lhs) {
        return this.setvalue(lhs.concat(this.value()));
    }

    public int length() {
        return this.length;
    }

    public final void makeroom(int nbefore, int nafter) {
        int v2 = 0x20;
        if(this.value != null) {
            LuaString luaString0 = this.value.strvalue();
            this.value = null;
            this.length = luaString0.m_length;
            this.offset = nbefore;
            this.bytes = new byte[this.length + nbefore + nafter];
            System.arraycopy(luaString0.m_bytes, luaString0.m_offset, this.bytes, this.offset, this.length);
            return;
        }
        if(this.offset + this.length + nafter > this.bytes.length || this.offset < nbefore) {
            int n = this.length + nbefore + nafter;
            if(n >= 0x20) {
                v2 = n >= this.length * 2 ? n : this.length * 2;
            }
            this.realloc(v2, (nbefore == 0 ? 0 : v2 - this.length - nafter));
        }
    }

    public Buffer prepend(LuaString s) {
        this.makeroom(s.m_length, 0);
        System.arraycopy(s.m_bytes, s.m_offset, this.bytes, this.offset - s.m_length, s.m_length);
        this.offset -= s.m_length;
        this.length += s.m_length;
        this.value = null;
        return this;
    }

    private final void realloc(int newSize, int newOffset) {
        if(newSize != this.bytes.length) {
            byte[] newBytes = new byte[newSize];
            System.arraycopy(this.bytes, this.offset, newBytes, newOffset, this.length);
            this.bytes = newBytes;
            this.offset = newOffset;
        }
    }

    public Buffer setvalue(LuaValue value) {
        this.bytes = Buffer.NOBYTES;
        this.length = 0;
        this.offset = 0;
        this.value = value;
        return this;
    }

    // 去混淆评级： 低(20)
    @Override
    public String toString() {
        return "";
    }

    public String tojstring() [...] // 潜在的解密器

    public final LuaString tostring() {
        this.realloc(this.length, 0);
        return LuaString.valueOf(this.bytes, this.offset, this.length);
    }

    public LuaValue value() {
        return this.value != null ? this.value : this.tostring();
    }
}

