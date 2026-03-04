package android.sup;

public class LongSparseLongArray implements Cloneable {
    private long[] mKeys;
    private int mSize;
    private long[] mValues;

    public LongSparseLongArray() {
        this(16);
    }

    public LongSparseLongArray(int initialCapacity) {
        if(initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_LONGS;
        }
        else {
            int v1 = ContainerHelpers.idealLongArraySize(initialCapacity);
            this.mKeys = new long[v1];
            this.mValues = new long[v1];
        }
        this.mSize = 0;
    }

    public void append(long key, long value) {
        if(this.mSize != 0 && key <= this.mKeys[this.mSize - 1]) {
            this.put(key, value);
            return;
        }
        this.mKeys = GrowingArrayUtils.append(this.mKeys, this.mSize, key);
        this.mValues = GrowingArrayUtils.append(this.mValues, this.mSize, value);
        ++this.mSize;
    }

    public void clear() {
        this.mSize = 0;
    }

    public LongSparseLongArray clone() {
        LongSparseLongArray clone = null;
        try {
            clone = (LongSparseLongArray)super.clone();
            clone.mKeys = (long[])this.mKeys.clone();
            clone.mValues = (long[])this.mValues.clone();
        }
        catch(CloneNotSupportedException unused_ex) {
        }
        return clone;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    public void delete(long key) {
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if(v1 >= 0) {
            this.removeAt(v1);
        }
    }

    public long get(long key) {
        return this.get(key, 0L);
    }

    public long get(long key, long valueIfKeyNotFound) {
        int v2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        return v2 >= 0 ? this.mValues[v2] : valueIfKeyNotFound;
    }

    public int indexOfKey(long key) {
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(long value) {
        int i;
        for(i = 0; true; ++i) {
            if(i >= this.mSize) {
                return -1;
            }
            if(this.mValues[i] == value) {
                break;
            }
        }
        return i;
    }

    public long keyAt(int index) {
        return this.mKeys[index];
    }

    public boolean put(long key, long value) {
        int v2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if(v2 >= 0) {
            long[] mValues = this.mValues;
            if(mValues[v2] == value) {
                return false;
            }
            mValues[v2] = value;
            return true;
        }
        this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, ~v2, key);
        this.mValues = GrowingArrayUtils.insert(this.mValues, this.mSize, ~v2, value);
        ++this.mSize;
        return true;
    }

    public void removeAt(int index) {
        System.arraycopy(this.mKeys, index + 1, this.mKeys, index, this.mSize - (index + 1));
        System.arraycopy(this.mValues, index + 1, this.mValues, index, this.mSize - (index + 1));
        --this.mSize;
    }

    public int size() {
        return this.mSize;
    }

    @Override
    public String toString() {
        if(this.size() <= 0) {
            return "{}";
        }
        StringBuilder stringBuilder0 = new StringBuilder(this.mSize * 28);
        stringBuilder0.append('{');
        for(int i = 0; i < this.mSize; ++i) {
            if(i > 0) {
                stringBuilder0.append(", ");
            }
            stringBuilder0.append(this.keyAt(i));
            stringBuilder0.append('=');
            stringBuilder0.append(this.valueAt(i));
        }
        stringBuilder0.append('}');
        return stringBuilder0.toString();
    }

    public void truncate() {
        this.mKeys = new long[16];
        this.mValues = new long[16];
        this.mSize = 0;
    }

    public long valueAt(int index) {
        return this.mValues[index];
    }
}

