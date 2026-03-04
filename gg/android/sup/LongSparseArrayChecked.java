package android.sup;

public class LongSparseArrayChecked implements Cloneable {
    private static final Object DELETED;
    private boolean[] mChecks;
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    static {
        LongSparseArrayChecked.DELETED = new Object();
    }

    public LongSparseArrayChecked() {
        this(16);
    }

    public LongSparseArrayChecked(int initialCapacity) {
        this.mGarbage = false;
        if(initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
            this.mChecks = ContainerHelpers.EMPTY_BOOLEANS;
        }
        else {
            int v1 = ContainerHelpers.idealLongArraySize(initialCapacity);
            this.mKeys = new long[v1];
            this.mValues = new Object[v1];
            this.mChecks = new boolean[v1];
        }
        this.mSize = 0;
    }

    public void append(long key, Object object0, boolean check) {
        if(this.mSize != 0 && key <= this.mKeys[this.mSize - 1]) {
            this.put(key, object0, check);
            return;
        }
        if(this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        this.mKeys = GrowingArrayUtils.append(this.mKeys, this.mSize, key);
        this.mValues = GrowingArrayUtils.append(this.mValues, this.mSize, object0);
        this.mChecks = GrowingArrayUtils.append(this.mChecks, this.mSize, check);
        ++this.mSize;
    }

    public boolean checkAt(int index) {
        if(this.mGarbage) {
            this.gc();
        }
        return this.mChecks[index];
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for(int i = 0; i < n; ++i) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArrayChecked clone() {
        LongSparseArrayChecked clone = null;
        try {
            clone = (LongSparseArrayChecked)super.clone();
            clone.mKeys = (long[])this.mKeys.clone();
            clone.mValues = (Object[])this.mValues.clone();
            clone.mChecks = (boolean[])this.mChecks.clone();
        }
        catch(CloneNotSupportedException unused_ex) {
        }
        return clone;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this.clone();
    }

    public Object delete(long key) {
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if(v1 >= 0) {
            Object old = this.mValues[v1];
            if(old != LongSparseArrayChecked.DELETED) {
                this.mValues[v1] = LongSparseArrayChecked.DELETED;
                this.mGarbage = true;
                return old;
            }
        }
        return null;
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        long[] keys = this.mKeys;
        Object[] values = this.mValues;
        boolean[] checks = this.mChecks;
        for(int i = 0; i < n; ++i) {
            Object val = values[i];
            if(val != LongSparseArrayChecked.DELETED) {
                if(i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    checks[o] = checks[i];
                    values[i] = null;
                }
                ++o;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public Object get(long key) {
        return this.get(key, null);
    }

    public Object get(long key, Object object0) {
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        return v1 >= 0 && this.mValues[v1] != LongSparseArrayChecked.DELETED ? this.mValues[v1] : object0;
    }

    public int indexOfKey(long key) {
        if(this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(Object object0) {
        if(this.mGarbage) {
            this.gc();
        }
        int i;
        for(i = 0; true; ++i) {
            if(i >= this.mSize) {
                return -1;
            }
            if(this.mValues[i] == object0) {
                break;
            }
        }
        return i;
    }

    public int indexOfValueByValue(Object object0) {
        if(this.mGarbage) {
            this.gc();
        }
        for(int i = 0; true; ++i) {
            if(i >= this.mSize) {
                return -1;
            }
            if(object0 == null) {
                if(this.mValues[i] == null) {
                    return i;
                }
            }
            else if(object0.equals(this.mValues[i])) {
                return i;
            }
        }
    }

    public boolean isChecked(long key) {
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        return v1 >= 0 && this.mValues[v1] != LongSparseArrayChecked.DELETED ? this.mChecks[v1] : false;
    }

    public long keyAt(int index) {
        if(this.mGarbage) {
            this.gc();
        }
        return this.mKeys[index];
    }

    public Object put(long key, Object object0, byte check) {
        Object old = null;
        boolean z = true;
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if(v1 >= 0) {
            old = this.mValues[v1];
            this.mValues[v1] = object0;
            if(check == 0) {
                return old;
            }
            boolean[] arr_z = this.mChecks;
            if(check != 1) {
                z = false;
            }
            arr_z[v1] = z;
            return old;
        }
        int i = ~v1;
        if(i < this.mSize && this.mValues[i] == LongSparseArrayChecked.DELETED) {
            this.mKeys[i] = key;
            this.mValues[i] = object0;
            if(check != 0) {
                boolean[] arr_z1 = this.mChecks;
                if(check != 1) {
                    z = false;
                }
                arr_z1[i] = z;
                return null;
            }
            return old;
        }
        if(this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
            i = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        }
        this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, i, key);
        this.mValues = GrowingArrayUtils.insert(this.mValues, this.mSize, i, object0);
        boolean[] arr_z2 = this.mChecks;
        int v3 = this.mSize;
        if(check != 1) {
            z = false;
        }
        this.mChecks = GrowingArrayUtils.insert(arr_z2, v3, i, z);
        ++this.mSize;
        return null;
    }

    public void put(long key, Object object0, boolean check) {
        int v1 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if(v1 >= 0) {
            this.mValues[v1] = object0;
            this.mChecks[v1] = check;
            return;
        }
        int i = ~v1;
        if(i < this.mSize && this.mValues[i] == LongSparseArrayChecked.DELETED) {
            this.mKeys[i] = key;
            this.mValues[i] = object0;
            this.mChecks[i] = check;
            return;
        }
        if(this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
            i = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        }
        this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, i, key);
        this.mValues = GrowingArrayUtils.insert(this.mValues, this.mSize, i, object0);
        this.mChecks = GrowingArrayUtils.insert(this.mChecks, this.mSize, i, check);
        ++this.mSize;
    }

    public Object removeAt(int index) {
        Object old = this.mValues[index];
        if(old != LongSparseArrayChecked.DELETED) {
            this.mValues[index] = LongSparseArrayChecked.DELETED;
            this.mGarbage = true;
            return old;
        }
        return null;
    }

    public void setCheckAt(int index, boolean check) {
        if(this.mGarbage) {
            this.gc();
        }
        this.mChecks[index] = check;
    }

    public void setValueAt(int index, Object object0) {
        if(this.mGarbage) {
            this.gc();
        }
        this.mValues[index] = object0;
    }

    public int size() {
        if(this.mGarbage) {
            this.gc();
        }
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
            Object object0 = this.valueAt(i);
            if(object0 == this) {
                stringBuilder0.append("(this Map)");
            }
            else {
                stringBuilder0.append(object0);
            }
            stringBuilder0.append(" [");
            stringBuilder0.append(this.checkAt(i));
            stringBuilder0.append("]");
        }
        stringBuilder0.append('}');
        return stringBuilder0.toString();
    }

    public void truncate() {
        this.mKeys = new long[16];
        this.mValues = new Object[16];
        this.mChecks = new boolean[16];
        this.mSize = 0;
        this.mGarbage = false;
    }

    public Object valueAt(int index) {
        if(this.mGarbage) {
            this.gc();
        }
        return this.mValues[index];
    }
}

