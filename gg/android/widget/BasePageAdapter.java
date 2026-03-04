package android.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class BasePageAdapter {
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private DataSetObservable mObservable;

    public BasePageAdapter() {
        this.mObservable = new DataSetObservable();
    }

    public void destroyItem(View view0, int v, Object object0) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void destroyItem(ViewGroup viewGroup0, int v, Object object0) {
        this.destroyItem(viewGroup0, v, object0);
    }

    public void finishUpdate(View view0) {
    }

    public void finishUpdate(ViewGroup viewGroup0) {
    }

    public abstract int getCount();

    public int getItemPosition(Object object0) [...] // Inlined contents

    public CharSequence getPageTitle(int v) {
        return null;
    }

    public float getPageWidth(int v) [...] // Inlined contents

    public Object instantiateItem(View view0, int v) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    public Object instantiateItem(ViewGroup viewGroup0, int v) {
        return this.instantiateItem(viewGroup0, v);
    }

    public abstract boolean isViewFromObject(View arg1, Object arg2);

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver0) {
        this.mObservable.registerObserver(dataSetObserver0);
    }

    public void restoreState(Parcelable parcelable0, ClassLoader classLoader0) {
    }

    public Parcelable saveState() [...] // Inlined contents

    public void setPrimaryItem(View view0, int v, Object object0) {
    }

    public void setPrimaryItem(ViewGroup viewGroup0, int v, Object object0) {
    }

    public void startUpdate(View view0) {
    }

    public void startUpdate(ViewGroup viewGroup0) {
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver0) {
        this.mObservable.unregisterObserver(dataSetObserver0);
    }
}

