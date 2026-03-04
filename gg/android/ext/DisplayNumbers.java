package android.ext;

public class DisplayNumbers {
    private static final int FULL_MASK = 3;
    private static final int NEGATIVE_MASK = 2;
    private static final int POSITIVE_MASK = 1;
    private static final int SIGN_MASK = 0;
    private static final int SIZE_MASK = 4;
    private static long[] masks;

    static {
        DisplayNumbers.masks = new long[12];
        for(int i = 0; i < 3; ++i) {
            long mask = (1L << (1 << i) * 8) - 1L;
            DisplayNumbers.masks[i * 4] = mask & (-1L ^ mask >> 1);
            DisplayNumbers.masks[i * 4 + 1] = mask >> 1;
            DisplayNumbers.masks[i * 4 + 2] = -1L ^ mask >> 1;
            DisplayNumbers.masks[i * 4 + 3] = mask;
        }
    }

    public static boolean isSignedLess(long A, long B, int flags) {
        int v3 = AddressItem.getPow(flags);
        if(v3 == 3) {
            return A < B;
        }
        long sign_A = A & DisplayNumbers.masks[v3 * 4];
        return sign_A == (B & DisplayNumbers.masks[v3 * 4]) ? (A & DisplayNumbers.masks[v3 * 4 + 1]) < (B & DisplayNumbers.masks[v3 * 4 + 1]) : sign_A != 0L;
    }

    public static long longToSigned(long l, int pow, boolean hex) {
        if(pow != 3) {
            if(hex) {
                return l & DisplayNumbers.masks[pow * 4 + 3];
            }
            return (DisplayNumbers.masks[pow * 4] & l) == 0L ? l & DisplayNumbers.masks[pow * 4 + 1] : l | DisplayNumbers.masks[pow * 4 + 2];
        }
        return l;
    }

    public static String longToString(long l, int flags) {
        return DisplayNumbers.longToString(l, flags, false);
    }

    public static String longToString(long l, int flags, boolean hex) {
        int v2 = AddressItem.getPow(flags);
        long v3 = DisplayNumbers.longToSigned(l, v2, hex);
        return hex ? ToolsLight.prefixLongHex(1 << v2 + 1, v3) : DisplayNumbers.longToString_(v3);
    }

    private static String longToString_(long l) {
        String s = Tools.stringFormatOne("%,d", "%d", null, l);
        if(s == null) {
            s = Long.toString(l);
        }
        if(s == null) {
            s = l;
        }
        return s == null ? "fail" : s;
    }

    public static void main(String[] args) {
    }
}

