package android.sup;

import android.ext.AddressItem;

public class ArrayListResults {
    private static final int MIN_CAPACITY_INCREMENT = 12;
    private long[] mAddrData;
    private boolean[] mChecks;
    private int[] mFlags;
    int size;

    public ArrayListResults() {
        this(0);
    }

    public ArrayListResults(int capacity) {
        this.setCapacity(ContainerHelpers.idealLongArraySize(capacity));
    }

    public void add(long addr, long data, int flags, boolean check) {
        int size = this.size;
        if(size == this.mFlags.length) {
            this.grow();
        }
        this.mAddrData[size * 2] = addr;
        this.mAddrData[size * 2 + 1] = data;
        this.mFlags[size] = flags;
        this.mChecks[size] = check;
        this.size = size + 1;
    }

    public void checked(int index, boolean check) {
        this.mChecks[index] = check;
    }

    public boolean checked(int index) {
        return this.mChecks[index];
    }

    public void clear() {
        this.size = 0;
    }

    public void ensureCapacity(int minimumCapacity) {
        if(this.mFlags.length < minimumCapacity) {
            this.setCapacity(minimumCapacity);
        }
    }

    public AddressItem get(int index, AddressItem item) {
        if(index >= this.size) {
            ArrayListResults.throwIndexOutOfBoundsException(index, this.size);
        }
        if(item == null) {
            return new AddressItem(this.mAddrData[index * 2], this.mAddrData[index * 2 + 1], this.mFlags[index]);
        }
        item.address = this.mAddrData[index * 2];
        item.data = this.mAddrData[index * 2 + 1];
        item.flags = this.mFlags[index];
        return item;
    }

    private void grow() {
        this.setCapacity(this.size + (this.size >= 6 ? this.size >> 1 : 12));
    }

    private void setCapacity(int capacity) {
        boolean[] newChecks;
        int[] newFlags;
        long[] newAddrData;
        if(capacity == 0) {
            newAddrData = ContainerHelpers.EMPTY_LONGS;
            newFlags = ContainerHelpers.EMPTY_INTS;
            newChecks = ContainerHelpers.EMPTY_BOOLEANS;
        }
        else {
            newAddrData = new long[capacity * 2];
            newFlags = new int[capacity];
            newChecks = new boolean[capacity];
        }
        int size = this.size;
        if(size != 0) {
            System.arraycopy(this.mAddrData, 0, newAddrData, 0, size * 2);
            System.arraycopy(this.mFlags, 0, newFlags, 0, size);
            System.arraycopy(this.mChecks, 0, newChecks, 0, size);
        }
        this.mAddrData = newAddrData;
        this.mFlags = newFlags;
        this.mChecks = newChecks;
    }

    public int size() {
        return this.size;
    }

    static IndexOutOfBoundsException throwIndexOutOfBoundsException(int index, int size) {
        throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }

    public void trimToSize() {
        if(this.size == this.mChecks.length) {
            return;
        }
        this.setCapacity(this.size);
    }
}

