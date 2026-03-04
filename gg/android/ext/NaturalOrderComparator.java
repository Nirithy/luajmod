package android.ext;

public class NaturalOrderComparator {
    static char charAt(String s, int len, int i) {
        return i < len ? s.charAt(i) : '\u0000';
    }

    public static int compare(Object o1, Object o2) {
        String s = o1.toString();
        String s1 = o2.toString();
        int ia = 0;
        int v2 = s.length();
        int v3 = s1.length();
        for(int ib = 0; true; ++ib) {
            int nzb = 0;
            int nza = 0;
            int ca = ia < v2 ? s.charAt(ia) : 0;
            int cb = ib < v3 ? s1.charAt(ib) : 0;
            while(ca == 0x20 || ca == 0x30) {
                nza = ca == 0x30 ? nza + 1 : 0;
                ++ia;
                ca = ia >= v2 ? 0 : s.charAt(ia);
            }
            while(cb == 0x20 || cb == 0x30) {
                nzb = cb == 0x30 ? nzb + 1 : 0;
                ++ib;
                cb = ib >= v3 ? 0 : s1.charAt(ib);
            }
            if(ca <= 57 && cb <= 57 && 0x30 <= ca && 0x30 <= cb) {
                int v8 = NaturalOrderComparator.compareRight(s.substring(ia), s1.substring(ib));
                if(v8 != 0) {
                    return v8;
                }
            }
            if(ca == 0 && cb == 0) {
                return nza - nzb;
            }
            if(ca < cb) {
                return -1;
            }
            if(ca > cb) {
                return 1;
            }
            ++ia;
        }
    }

    private static int compareRight(String a, String b) {
        int bias = 0;
        int ia = 0;
        int v3 = a.length();
        int v4 = b.length();
        for(int ib = 0; true; ++ib) {
            int ca = ia < v3 ? a.charAt(ia) : 0;
            int cb = ib < v4 ? b.charAt(ib) : 0;
            boolean isNotDigitA = ca > 57 || 0x30 > ca;
            boolean isNotDigitB = cb > 57 || 0x30 > cb;
            if(isNotDigitA && isNotDigitB) {
                break;
            }
            if(isNotDigitA) {
                return -1;
            }
            if(isNotDigitB) {
                return 1;
            }
            if(ca == 0 && cb == 0) {
                break;
            }
            if(bias == 0) {
                if(ca < cb) {
                    bias = -1;
                }
                else if(ca > cb) {
                    bias = 1;
                }
            }
            ++ia;
        }
        return bias;
    }
}

