package android.ext;

import android.fix.LayoutInflater;
import android.sup.ArrayListResults;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import java.util.ArrayList;

public class AddressArrayAdapter extends BaseAdapterIndexer implements Listener {
    private static final int MASK_DEFAULT = 0;
    private static final String SEARCH_RESULTS_FORMAT = "search-results-format";
    private ArrayListResults mList;
    private final Result result;
    static volatile int showMask;
    private AddressItem tmp;

    static {
        AddressArrayAdapter.showMask = 0;
    }

    public AddressArrayAdapter(ArrayListResults mAddressList) {
        this.result = new Result();
        this.tmp = new AddressItem();
        this.mList = mAddressList;
        AddressArrayAdapter.showMask = Tools.getSharedPreferences().getInt("search-results-format", 0);
    }

    @Override  // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override  // android.widget.Adapter
    public Object getItem(int position) {
        try {
            return this.mList.get(position, null);
        }
        catch(IndexOutOfBoundsException e) {
            Log.w("Failed get item in memory list", e);
            return null;
        }
    }

    @Override  // android.widget.Adapter
    public long getItemId(int position) {
        return (long)position;
    }

    private View getNewView(ViewGroup parent) {
        View view0 = LayoutInflater.inflateStatic(0x7F040018, parent, false);  // layout:service_address_item
        new ViewHolderSearch(view0, this);
        return view0;
    }

    @Override  // android.widget.Adapter
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null || view.getTag() == null) {
            view = this.getNewView(parent);
        }
        boolean smallItems = (Config.smallItems & 1 << Tools.isLandscape()) != 0;
        ViewHolderSearch holder = (ViewHolderSearch)view.getTag();
        if(holder.wrongOrientation()) {
            view = this.getNewView(parent);
            holder = (ViewHolderSearch)view.getTag();
        }
        holder.pos = position;
        holder.remove.setTag(position);
        holder.type.setTag(position);
        AddressItem item = null;
        boolean checked = false;
        ArrayListResults mList = this.mList;
        if(mList.size() > position) {
            try {
                item = mList.get(position, this.tmp);
                checked = mList.checked(position);
            }
            catch(IndexOutOfBoundsException unused_ex) {
                item = null;
            }
        }
        holder.info.setOrientation((smallItems ? 0 : 1));
        SavedListAdapter.sizeRemove(holder.remove, smallItems);
        holder.ch.setChecked(checked);
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = holder.ch.getLayoutParams();
        viewGroup$LayoutParams0.height = smallItems ? -2 : Tools.dp2px48();
        holder.updateBackground(checked, -2);
        if(item != null) {
            int v1 = item.getColor();
            holder.value.setTextColor(v1);
            holder.type.setTextColor(v1);
            String data = item.getStringData();
            Result result = this.result;
            MainService.instance.revertMap.get(item.address, item.flags, result);
            if(result.found) {
                data = data + " (" + AddressItem.getStringData(item.address, result.data, item.flags) + ')';
            }
            holder.address.setText("00000000");
            holder.value.setText(data);
            CharSequence charSequence0 = item.getShortName();
            holder.type.setText(charSequence0);
            holder.region.setText(RegionList.getRegion(item.address));
            MemoryContentAdapter.fillData(holder, item.address, item.data, AddressArrayAdapter.showMask, true, item.getSize());
            return view;
        }
        holder.address.setText("null");
        holder.value.setText("null");
        holder.type.setText("null");
        holder.region.setText("null");
        MemoryContentAdapter.fillData(holder, 0L, 0L, 0, false, 0);
        return view;
    }

    @Override  // android.widget.BaseAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        ViewHolderBase holder = (ViewHolderBase)v.getTag();
        if(holder == null) {
            return;
        }
        try {
            this.mList.checked(holder.pos, isChecked);
            MainService.instance.updateStatusBar();
        }
        catch(IndexOutOfBoundsException e) {
            Log.e("ArrayAdapter onCheckedChanged failed", e);
        }
        holder.updateBackground(isChecked, -2);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        Integer position = (Integer)v.getTag();
        if(position == null) {
            return;
        }
        try {
            AddressItem addressItem0 = this.mList.get(((int)position), this.tmp);
            ArrayList items = new ArrayList();
            items.add(addressItem0);
            MainService.instance.mDaemonManager.remove(items);
        }
        catch(IndexOutOfBoundsException e) {
            Log.e("ArrayAdapter onClick Failed", e);
        }
        MainService.instance.refreshResultList(false);
    }

    static void setShowMask(int val) {
        AddressArrayAdapter.showMask = val;
        new SPEditor().putInt("search-results-format", val, 0).commit();
    }
}

