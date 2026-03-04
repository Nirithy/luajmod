package android.ext;

import android.sup.LongSparseLongArray;
import java.util.ArrayList;
import java.util.List;

public class AddressItemSet {
    public static class Result {
        public long data;
        public boolean found;

    }

    private final LongSparseLongArray[] types;

    public AddressItemSet() {
        this(10);
    }

    public AddressItemSet(int initialCapacity) {
        LongSparseLongArray[] types = new LongSparseLongArray[7];
        for(int i = 0; i < 7; ++i) {
            types[i] = new LongSparseLongArray(initialCapacity);
        }
        this.types = types;
    }

    public void clear() {
        LongSparseLongArray[] types = this.types;
        for(int i = 0; i < 7; ++i) {
            types[i].clear();
        }
    }

    public void get(long address, int flags, Result ret) {
        int v2 = Integer.numberOfTrailingZeros(flags);
        if(v2 < 7) {
            LongSparseLongArray list = this.types[v2];
            int v3 = list.indexOfKey(address);
            if(v3 >= 0) {
                ret.data = list.valueAt(v3);
                ret.found = true;
                return;
            }
        }
        ret.found = false;
    }

    public List getAll() {
        List items = new ArrayList();
        LongSparseLongArray[] types = this.types;
        for(int i = 0; i < 7; ++i) {
            LongSparseLongArray list = types[i];
            int v1 = list.size();
            if(v1 > 0) {
                int flags = 1 << i;
                for(int j = 0; j < v1; ++j) {
                    try {
                        AddressItem item = new AddressItem();
                        item.flags = flags;
                        item.address = list.keyAt(j);
                        item.data = list.valueAt(j);
                        items.add(item);
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        Log.w(("Concurent modification for " + i), e);
                        if(true) {
                            break;
                        }
                    }
                }
            }
        }
        return items;
    }

    public boolean put(long address, int flags, long data) {
        int v3 = Integer.numberOfTrailingZeros(flags);
        return v3 >= 7 ? false : this.types[v3].put(address, data);
    }

    public void revert() {
        LongSparseLongArray[] types = this.types;
        boolean changed = false;
        for(int i = 0; i < 7; ++i) {
            LongSparseLongArray list = types[i];
            int v1 = list.size();
            if(v1 > 0) {
                if(!changed) {
                    changed = true;
                }
                int flags = 1 << i | 0x1000000;
                for(int j = 0; j < v1; ++j) {
                    try {
                        AddressItem item = new AddressItem();
                        item.flags = flags;
                        item.address = list.keyAt(j);
                        item.data = list.valueAt(j);
                        item.alter();
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        Log.w(("Concurent modification for " + i), e);
                        if(true) {
                            break;
                        }
                    }
                }
            }
        }
        if(changed) {
            MainService.instance.onMemoryChanged();
        }
    }

    public int size() {
        LongSparseLongArray[] types = this.types;
        int size = 0;
        for(int i = 0; i < 7; ++i) {
            size += types[i].size();
        }
        return size;
    }

    public void truncate() {
        LongSparseLongArray[] types = this.types;
        for(int i = 0; i < 7; ++i) {
            types[i].truncate();
        }
    }
}

