package android.ext;

import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class BaseAdapterLC extends BaseAdapter implements View.OnLongClickListener {
    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View view) {
        if(view != null) {
            ListView listView0 = Tools.getListView(view);
            if(listView0 != null) {
                int v = listView0.getPositionForView(view);
                if(v != -1) {
                    AdapterView.OnItemLongClickListener adapterView$OnItemLongClickListener0 = listView0.getOnItemLongClickListener();
                    if(adapterView$OnItemLongClickListener0 != null) {
                        adapterView$OnItemLongClickListener0.onItemLongClick(listView0, view, v, this.getItemId(v));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

