package android.widget;

import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrayPageAdapter extends BasePageAdapter {
    public ArrayList mListViews;

    public ArrayPageAdapter() {
        this.mListViews = new ArrayList();
    }

    public ArrayPageAdapter(ArrayList arrayList0) {
        this.mListViews = arrayList0;
    }

    public ArrayPageAdapter(View[] arr_view) {
        this.mListViews = new ArrayList(Arrays.asList(arr_view));
    }

    public void add(View view0) {
        this.mListViews.add(view0);
    }

    @Override  // android.widget.BasePageAdapter
    public void destroyItem(View view0, int v, Object object0) {
        ((PageView)view0).removeView(((View)this.mListViews.get(v)));
    }

    @Override  // android.widget.BasePageAdapter
    public int getCount() {
        return this.mListViews.size();
    }

    public ArrayList getData() {
        return this.mListViews;
    }

    public View getItem(int v) {
        return (View)this.mListViews.get(v);
    }

    public void insert(int v, View view0) {
        this.mListViews.add(v, view0);
    }

    @Override  // android.widget.BasePageAdapter
    public Object instantiateItem(View view0, int v) {
        ((PageView)view0).addView(((View)this.mListViews.get(v)), 0);
        return (View)this.mListViews.get(v);
    }

    @Override  // android.widget.BasePageAdapter
    public boolean isViewFromObject(View view0, Object object0) {
        return view0 == object0;
    }

    public View remove(int v) {
        return (View)this.mListViews.remove(v);
    }

    public boolean remove(View view0) {
        return this.mListViews.remove(view0);
    }
}

