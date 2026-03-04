package android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.lib.jse.CoerceJavaToLua;

public class ArrayListAdapter extends BaseAdapter implements Filterable {
    class ArrayFilter extends Filter {
        static ArrayListAdapter access$0(ArrayFilter arrayListAdapter$ArrayFilter0) {
            return ArrayListAdapter.this;
        }

        @Override  // android.widget.Filter
        protected Filter.FilterResults performFiltering(CharSequence charSequence0) {
            ArrayList arrayList3;
            ArrayList arrayList2;
            Filter.FilterResults filter$FilterResults0 = new Filter.FilterResults();
            if(ArrayListAdapter.this.mOriginalValues == null) {
                synchronized(ArrayListAdapter.this.mLock) {
                    ArrayList arrayList0 = new ArrayList(ArrayListAdapter.this.mObjects);
                    ArrayListAdapter.this.mOriginalValues = arrayList0;
                }
            }
            else if(TextUtils.isEmpty(charSequence0)) {
                filter$FilterResults0.values = new ArrayList(ArrayListAdapter.this.mOriginalValues);
                filter$FilterResults0.count = ArrayListAdapter.this.mOriginalValues.size();
                ArrayListAdapter.this.mOriginalValues = null;
                return filter$FilterResults0;
            }
            if(ArrayListAdapter.this.mLuaFilter != null) {
                ArrayList arrayList1 = new ArrayList();
                try {
                    ArrayListAdapter.this.mLuaFilter.invoke(CoerceJavaToLua.coerce(new ArrayList(ArrayListAdapter.this.mOriginalValues)), CoerceJavaToLua.coerce(arrayList1), CoerceJavaToLua.coerce(charSequence0));
                }
                catch(LuaError luaError0) {
                    luaError0.printStackTrace();
                }
                filter$FilterResults0.values = arrayList1;
                filter$FilterResults0.count = arrayList1.size();
                return filter$FilterResults0;
            }
            if(charSequence0 == null || charSequence0.length() == 0) {
                synchronized(ArrayListAdapter.this.mLock) {
                    arrayList2 = new ArrayList(ArrayListAdapter.this.mOriginalValues);
                }
                filter$FilterResults0.values = arrayList2;
                filter$FilterResults0.count = arrayList2.size();
                return filter$FilterResults0;
            }
            String s = charSequence0.toString().toLowerCase();
            synchronized(ArrayListAdapter.this.mLock) {
                arrayList3 = new ArrayList(ArrayListAdapter.this.mOriginalValues);
            }
            int v3 = arrayList3.size();
            ArrayList arrayList4 = new ArrayList();
            for(int v4 = 0; v4 < v3; ++v4) {
                Object object3 = arrayList3.get(v4);
                if(object3.toString().toLowerCase().contains(s)) {
                    arrayList4.add(object3);
                }
            }
            filter$FilterResults0.values = arrayList4;
            filter$FilterResults0.count = arrayList4.size();
            return filter$FilterResults0;
        }

        @Override  // android.widget.Filter
        protected void publishResults(CharSequence charSequence0, Filter.FilterResults filter$FilterResults0) {
            ArrayListAdapter.this.mObjects = (ArrayList)filter$FilterResults0.values;
            if(filter$FilterResults0.count > 0) {
                ArrayListAdapter.this.notifyDataSetChanged();
                return;
            }
            ArrayListAdapter.this.notifyDataSetInvalidated();
        }
    }

    private Context mContext;
    private int mDropDownResource;
    private int mFieldId;
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;
    private final Object mLock;
    private LuaFunction mLuaFilter;
    private boolean mNotifyOnChange;
    private ArrayList mObjects;
    private ArrayList mOriginalValues;
    private int mResource;

    public ArrayListAdapter(Context context0) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, 0x1090003, 0, new ArrayList());
    }

    public ArrayListAdapter(Context context0, int v) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, 0, new ArrayList());
    }

    public ArrayListAdapter(Context context0, int v, int v1) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, v1, new ArrayList());
    }

    public ArrayListAdapter(Context context0, int v, int v1, List list0) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, v1, list0);
    }

    public ArrayListAdapter(Context context0, int v, int v1, Object[] arr_object) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, v1, Arrays.asList(arr_object));
    }

    public ArrayListAdapter(Context context0, int v, List list0) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, 0, list0);
    }

    public ArrayListAdapter(Context context0, int v, Object[] arr_object) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, v, 0, Arrays.asList(arr_object));
    }

    public ArrayListAdapter(Context context0, List list0) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, 0x1090003, 0, list0);
    }

    public ArrayListAdapter(Context context0, Object[] arr_object) {
        this.mLock = new Object();
        this.mFieldId = 0;
        this.mNotifyOnChange = true;
        this.init(context0, 0x1090003, 0, Arrays.asList(arr_object));
    }

    static void access$S1000001(ArrayListAdapter arrayListAdapter0, Object object0) {
        arrayListAdapter0.mLock = object0;
    }

    static void access$S1000010(ArrayListAdapter arrayListAdapter0, LuaFunction luaFunction0) {
        arrayListAdapter0.mLuaFilter = luaFunction0;
    }

    public void add(Object object0) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.add(object0);
            }
            else {
                this.mOriginalValues.add(object0);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void addAll(Collection collection0) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.addAll(collection0);
            }
            else {
                this.mOriginalValues.addAll(collection0);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void addAll(Object[] arr_object) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                Collections.addAll(this.mObjects, arr_object);
            }
            else {
                Collections.addAll(this.mOriginalValues, arr_object);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.clear();
            }
            else {
                this.mOriginalValues.clear();
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public static ArrayListAdapter createFromResource(Context context0, int v, int v1) {
        return new ArrayListAdapter(context0, v1, context0.getResources().getTextArray(v));
    }

    private View createViewFromResource(int v, View view0, ViewGroup viewGroup0, int v1) {
        TextView textView0;
        View view1 = view0 == null ? this.mInflater.inflate(v1, viewGroup0, false) : view0;
        try {
            textView0 = this.mFieldId == 0 ? ((TextView)view1) : ((TextView)view1.findViewById(this.mFieldId));
        }
        catch(ClassCastException classCastException0) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", classCastException0);
        }
        Object object0 = this.getItem(v);
        if(object0 instanceof CharSequence) {
            textView0.setText(((CharSequence)object0));
            return view1;
        }
        textView0.setText(object0.toString());
        return view1;
    }

    public void filter(CharSequence charSequence0) {
        this.getFilter().filter(charSequence0);
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override  // android.widget.Adapter
    public int getCount() {
        return this.mObjects.size();
    }

    public Object getData() {
        return this.mObjects;
    }

    @Override  // android.widget.BaseAdapter
    public View getDropDownView(int v, View view0, ViewGroup viewGroup0) {
        return this.createViewFromResource(v, view0, viewGroup0, this.mDropDownResource);
    }

    @Override  // android.widget.Filterable
    public Filter getFilter() {
        if(this.mFilter == null) {
            this.mFilter = new ArrayFilter(this);
        }
        return this.mFilter;
    }

    @Override  // android.widget.Adapter
    public Object getItem(int v) {
        return this.mObjects.get(v);
    }

    @Override  // android.widget.Adapter
    public long getItemId(int v) {
        return (long)(v + 1);
    }

    public int getPosition(Object object0) {
        return this.mObjects.indexOf(object0);
    }

    @Override  // android.widget.Adapter
    public View getView(int v, View view0, ViewGroup viewGroup0) {
        return this.createViewFromResource(v, view0, viewGroup0, this.mResource);
    }

    private void init(Context context0, int v, int v1, List list0) {
        this.mContext = context0;
        this.mInflater = (LayoutInflater)context0.getSystemService("layout_inflater");
        this.mDropDownResource = v;
        this.mResource = v;
        this.mObjects = new ArrayList(list0);
        this.mFieldId = v1;
    }

    public void insert(int v, Object object0) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.add(v, object0);
            }
            else {
                this.mOriginalValues.add(v, object0);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    @Override  // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mNotifyOnChange = true;
    }

    public void remove(int v) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.remove(v);
            }
            else {
                this.mOriginalValues.remove(v);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void remove(Object object0) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                this.mObjects.remove(object0);
            }
            else {
                this.mOriginalValues.remove(object0);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }

    public void setDropDownViewResource(int v) {
        this.mDropDownResource = v;
    }

    public void setFilter(LuaFunction luaFunction0) {
        this.mLuaFilter = luaFunction0;
    }

    public void setNotifyOnChange(boolean z) {
        this.mNotifyOnChange = z;
    }

    public void sort(Comparator comparator0) {
        synchronized(this.mLock) {
            if(this.mOriginalValues == null) {
                Collections.sort(this.mObjects, comparator0);
            }
            else {
                Collections.sort(this.mOriginalValues, comparator0);
            }
        }
        if(this.mNotifyOnChange) {
            this.notifyDataSetChanged();
        }
    }
}

