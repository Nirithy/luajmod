package android.fix;

import android.annotation.TargetApi;
import android.ext.Log;
import android.os.Build.VERSION;
import java.lang.reflect.Field;

public class SparseArray extends android.util.SparseArray {
    public SparseArray() {
    }

    public SparseArray(int initialCapacity) {
        super(initialCapacity);
    }

    @Override  // android.util.SparseArray
    public void clear() {
        try {
            super.clear();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            this.fixSize();
            super.clear();
        }
    }

    @Override  // android.util.SparseArray
    public void delete(int key) {
        try {
            super.delete(key);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            this.fixSize();
            super.delete(key);
        }
    }

    private void fixSize() {
        try {
            Field field0 = android.util.SparseArray.class.getDeclaredField("mKeys");
            field0.setAccessible(true);
            Field field1 = android.util.SparseArray.class.getDeclaredField("mValues");
            field1.setAccessible(true);
            Field field2 = android.util.SparseArray.class.getDeclaredField("mSize");
            field2.setAccessible(true);
            int v = field2.getInt(this);
            int keysLen = ((int[])field0.get(this)).length;
            int valuesLen = ((Object[])field1.get(this)).length;
            if(v > keysLen || v > valuesLen) {
                field2.setInt(this, Math.min(Math.min(v, keysLen), valuesLen));
            }
        }
        catch(Throwable e) {
            Log.w("Failed fix", e);
        }
    }

    @Override  // android.util.SparseArray
    public Object get(int key, Object object0) {
        try {
            return super.get(key, object0);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            this.fixSize();
            return super.get(key, object0);
        }
    }

    @Override  // android.util.SparseArray
    public int indexOfKey(int key) {
        try {
            return super.indexOfKey(key);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            this.fixSize();
            return super.indexOfKey(key);
        }
    }

    @Override  // android.util.SparseArray
    public void put(int key, Object object0) {
        try {
            super.put(key, object0);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            this.fixSize();
            super.put(key, object0);
        }
    }

    @Override  // android.util.SparseArray
    @TargetApi(11)
    public void removeAt(int index) {
        if(Build.VERSION.SDK_INT > 11) {
            super.removeAt(index);
            return;
        }
        this.remove(this.keyAt(index));
    }

    @Override  // android.util.SparseArray
    public int size() {
        return super.size();
    }

    public Object syncGet(int key) {
        synchronized(this) {
            return this.get(key);
        }
    }

    public Object syncGet(int key, Object object0) {
        synchronized(this) {
            return this.get(key, object0);
        }
    }

    public void syncPut(int key, Object object0) {
        synchronized(this) {
            this.put(key, object0);
        }
    }
}

