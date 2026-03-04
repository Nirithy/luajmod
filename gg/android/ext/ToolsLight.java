package android.ext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class ToolsLight {
    private static final char[] digits;
    private static final ThreadLocal prefixBuf;

    static {
        ToolsLight.prefixBuf = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return this.initialValue();
            }

            protected char[] initialValue() {
                return new char[16];
            }
        };
        ToolsLight.digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    }

    public static Appendable prefix(Appendable out, int cnt, char filler, String str) {
        try {
            int v2 = str.length();
            for(int j = 0; true; ++j) {
                if(j >= cnt - v2) {
                    return out.append(str);
                }
                out.append(filler);
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String prefix(int cnt, char filler, String str) {
        int v1 = str.length();
        if(v1 < cnt) {
            char[] buf = (char[])ToolsLight.prefixBuf.get();
            Arrays.fill(buf, 0, cnt - v1, filler);
            return new String(buf, 0, cnt);
        }
        return str;
    }

    public static Appendable prefixIntegerHex(Appendable out, int cnt, int v) {
        return ToolsLight.prefix(out, cnt, '0', Integer.toHexString(v).toUpperCase(Locale.US));
    }

    public static Appendable prefixLongHex(Appendable out, int cnt, long v) {
        return ToolsLight.prefix(out, cnt, '0', Long.toHexString(v).toUpperCase(Locale.US));
    }

    public static String prefixLongHex(int cnt, long v) {
        return ToolsLight.prefix(cnt, '0', Long.toHexString(v).toUpperCase(Locale.US));
    }

    public static String stringFormat(String format, Object[] args) {
        return Tools.stringFormat(format, args);
    }

    public static String toString64(int i) [...] // 潜在的解密器

    public static String toString64(long i) [...] // 潜在的解密器
}

