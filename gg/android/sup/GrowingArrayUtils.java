package android.sup;

import java.lang.reflect.Array;

public final class GrowingArrayUtils {
    static final boolean $assertionsDisabled;

    static {
        GrowingArrayUtils.$assertionsDisabled = !GrowingArrayUtils.class.desiredAssertionStatus();
    }

    public static float[] append(float[] array, int currentSize, float element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 > array.length) {
            float[] newArray = new float[GrowingArrayUtils.growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    public static int[] append(int[] array, int currentSize, int element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 > array.length) {
            int[] newArray = new int[GrowingArrayUtils.growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    public static long[] append(long[] array, int currentSize, long element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 > array.length) {
            long[] newArray = new long[GrowingArrayUtils.growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    public static Object[] append(Object[] array, int currentSize, Object object0) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 > array.length) {
            Object[] newArray = (Object[])Array.newInstance(array.getClass().getComponentType(), GrowingArrayUtils.growSize(currentSize));
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = object0;
        return array;
    }

    public static boolean[] append(boolean[] array, int currentSize, boolean element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 > array.length) {
            boolean[] newArray = new boolean[GrowingArrayUtils.growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    public static int growSize(int currentSize) {
        return currentSize > 4 ? currentSize * 2 : 8;
    }

    public static int[] insert(int[] array, int currentSize, int index, int element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        int[] newArray = new int[GrowingArrayUtils.growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    public static long[] insert(long[] array, int currentSize, int index, long element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        long[] newArray = new long[GrowingArrayUtils.growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    public static Object[] insert(Object[] array, int currentSize, int index, Object object0) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = object0;
            return array;
        }
        Object[] newArray = (Object[])Array.newInstance(array.getClass().getComponentType(), GrowingArrayUtils.growSize(currentSize));
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = object0;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    public static boolean[] insert(boolean[] array, int currentSize, int index, boolean element) {
        if(!GrowingArrayUtils.$assertionsDisabled && currentSize > array.length) {
            throw new AssertionError();
        }
        if(currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }
        boolean[] newArray = new boolean[GrowingArrayUtils.growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }
}

