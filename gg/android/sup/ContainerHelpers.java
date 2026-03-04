package android.sup;

import android.view.View;

public class ContainerHelpers {
    public static final boolean[] EMPTY_BOOLEANS;
    public static final byte[] EMPTY_BYTES;
    public static final int[] EMPTY_INTS;
    public static final long[] EMPTY_LONGS;
    public static final Object[] EMPTY_OBJECTS;
    public static final String[] EMPTY_STRINGS;
    public static final View[] EMPTY_VIEWS;

    static {
        ContainerHelpers.EMPTY_STRINGS = new String[0];
        ContainerHelpers.EMPTY_BYTES = new byte[0];
        ContainerHelpers.EMPTY_INTS = new int[0];
        ContainerHelpers.EMPTY_LONGS = new long[0];
        ContainerHelpers.EMPTY_OBJECTS = new Object[0];
        ContainerHelpers.EMPTY_BOOLEANS = new boolean[0];
        ContainerHelpers.EMPTY_VIEWS = new View[0];
    }

    static int binarySearch(int[] array, int size, int value) {
        int mid;
        int lo = 0;
        int hi = size - 1;
        while(true) {
            if(lo > hi) {
                return ~lo;
            }
            mid = lo + hi >>> 1;
            int midVal = array[mid];
            if(midVal < value) {
                lo = mid + 1;
            }
            else {
                if(midVal <= value) {
                    break;
                }
                hi = mid - 1;
            }
        }
        return mid;
    }

    static int binarySearch(long[] array, int size, long value) {
        int mid;
        int lo = 0;
        int hi = size - 1;
        while(true) {
            if(lo > hi) {
                return ~lo;
            }
            mid = lo + hi >>> 1;
            long midVal = array[mid];
            if(midVal < value) {
                lo = mid + 1;
            }
            else {
                if(midVal <= value) {
                    break;
                }
                hi = mid - 1;
            }
        }
        return mid;
    }

    // 去混淆评级： 低(20)
    public static boolean equal(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static int idealByteArraySize(int need) {
        for(int i = 4; true; ++i) {
            if(i >= 0x20) {
                return need;
            }
            if(need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
    }

    public static int idealIntArraySize(int need) {
        return ContainerHelpers.idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need) {
        return ContainerHelpers.idealByteArraySize(need * 8) / 8;
    }
}

