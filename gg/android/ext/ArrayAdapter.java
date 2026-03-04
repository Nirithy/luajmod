package android.ext;

import android.content.Context;
import android.fix.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collection;
import java.util.List;

public class ArrayAdapter extends android.widget.ArrayAdapter {
    private LayoutInflater mInflater;

    public ArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, List list0) {
        super(context, resource, textViewResourceId, list0);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, int resource, List list0) {
        super(context, resource, list0);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, List list0) {
        super(context, 0x1090011, 0x1020014, list0);
        this.mInflater = null;
    }

    public ArrayAdapter(Context context, Object[] objects) {
        super(context, 0x1090011, 0x1020014, objects);
        this.mInflater = null;
    }

    @Override  // android.widget.ArrayAdapter
    public void addAll(Collection collection0) {
        if(Build.VERSION.SDK_INT < 11) {
            for(Object object0: collection0) {
                this.add(object0);
            }
            return;
        }
        super.addAll(collection0);
    }

    @Override  // android.widget.ArrayAdapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object2;
        View view;
        TextView text;
        Object item = null;
        try {
            text = null;
            view = super.getView(position, convertView, parent);
        }
        catch(Throwable e) {
            Log.e("Failed get view for ArrayAdapter", e);
            if(convertView == null) {
                if(this.mInflater == null) {
                    this.mInflater = LayoutInflater.getInflater();
                }
                view = this.mInflater.inflate(0x7F040016, parent, false);  // layout:select_dialog_item
            }
            else {
                view = convertView;
            }
            text = (TextView)view;
            item = this.getItem(position);
            if(item instanceof CharSequence) {
                text.setText(((CharSequence)item));
            }
            else {
                text.setText(item.toString());
            }
        }
        if(item != null || !(view instanceof TextView)) {
            object2 = item;
        }
        else {
            Object object1 = this.getItem(position);
            text = (TextView)view;
            object2 = object1;
        }
        if(object2 instanceof MenuItem && text != null) {
            Drawable drawable0 = ((MenuItem)object2).getDrawable();
            if(drawable0 != null) {
                Tools.addIconToTextView(text, drawable0, 24);
            }
        }
        return view;
    }
}

