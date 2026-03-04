package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.sup.LongSparseArrayChecked;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavedListAdapter extends BaseAdapterIndexer implements Listener, ListAdapter {
    static class ViewHolderSaved extends ViewHolderSearch {
        final ImageView freeze;
        final TextView name;

        ViewHolderSaved(View view, Listener listener) {
            super(view, listener);
            this.freeze = Config.setIconSize(((ImageView)view.findViewById(0x7F0B0101)));  // id:freeze
            this.freeze.setOnClickListener(listener);
            this.freeze.setOnLongClickListener(listener);
            this.name = (TextView)view.findViewById(0x7F0B0051);  // id:name
        }
    }

    static final int MASK_DEFAULT = 0;
    private static final String SAVED_LIST_FORMAT = "saved-list-format";
    private final LongSparseArrayChecked list;
    private final Result result;
    static volatile int showMask;

    static {
        SavedListAdapter.showMask = 0;
    }

    public SavedListAdapter() {
        this.result = new Result();
        this.list = new LongSparseArrayChecked();
        SavedListAdapter.showMask = Tools.getSharedPreferences().getInt("saved-list-format", 0);
    }

    public void add(SavedItem item) {
        this.add(item, 0, true);
    }

    public void add(SavedItem item, byte checked, boolean notify) {
        DaemonManager daemon = MainService.instance.mDaemonManager;
        if((item.flags & 0x40000000) == 0x40000000) {
            item = item.copy();
        }
        if(item.freeze) {
            item.flags &= 0xBFFFFFFF;
            daemon.addForFreeze(item);
        }
        else if((item.flags & 0x40000000) == 0x40000000) {
            item.flags &= 0xBFFFFFFF;
        }
        long v = item.getAddress();
        SavedItem prev = (SavedItem)this.list.put(v, item, checked);
        if(prev != null && prev.freeze && !item.freeze) {
            daemon.addForUnfreeze(item);
        }
        if(notify) {
            this.notifyDataSetChanged();
        }
    }

    public void clear() {
        MainService.instance.mDaemonManager.clearLockList();
        this.truncate();
        this.notifyDataSetChanged();
    }

    @Override  // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    public SavedItem getItem(int position) {
        return (SavedItem)this.list.valueAt(position);
    }

    @Override  // android.widget.Adapter
    public Object getItem(int v) {
        return this.getItem(v);
    }

    public SavedItem getItemByAddress(long addr) {
        SavedItem item = (SavedItem)this.list.get(addr);
        return item == null ? null : item;
    }

    @Override  // android.widget.Adapter
    public long getItemId(int position) {
        SavedItem item = (SavedItem)this.list.valueAt(position);
        return item == null ? 0L : item.getAddress();
    }

    public LongSparseArrayChecked getList() {
        return this.list;
    }

    private View getNewView(ViewGroup parent) {
        View view0 = LayoutInflater.inflateStatic(0x7F040024, parent, false);  // layout:service_saved_item
        new ViewHolderSaved(view0, this);
        return view0;
    }

    @Override  // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean checked;
        SavedItem item;
        View view = convertView == null ? this.getNewView(parent) : convertView;
        ViewHolderSaved holder = (ViewHolderSaved)view.getTag();
        if(holder.wrongOrientation()) {
            view = this.getNewView(parent);
            holder = (ViewHolderSaved)view.getTag();
        }
        boolean smallItems = (Config.smallItems & 1 << Tools.isLandscape() + 2) != 0;
        if(position >= this.list.size()) {
            item = new SavedItem(0L, 0L, 4, "null for " + position);
            checked = false;
        }
        else {
            item = (SavedItem)this.list.valueAt(position);
            checked = this.list.checkAt(position);
        }
        holder.pos = position;
        SavedListAdapter.sizeRemove(holder.freeze, smallItems);
        holder.name.getLayoutParams().width = smallItems ? -2 : -1;
        holder.info.setOrientation((smallItems ? 0 : 1));
        SavedListAdapter.sizeRemove(holder.remove, smallItems);
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = holder.ch.getLayoutParams();
        viewGroup$LayoutParams0.height = smallItems ? -2 : Tools.dp2px48();
        holder.freeze.setTag(item);
        if(holder.ch.isChecked() != checked) {
            holder.ch.setOnCheckedChangeListener(null);
            holder.ch.setChecked(checked);
        }
        holder.ch.setOnCheckedChangeListener(this);
        holder.type.setTag(item);
        holder.remove.setTag(item);
        holder.updateBackground(checked, -2);
        int v1 = item.getColor();
        holder.value.setTextColor(v1);
        holder.type.setTextColor(v1);
        String data = item.getStringDataTrim();
        Result result = this.result;
        MainService.instance.revertMap.get(item.address, item.flags, result);
        if(result.found) {
            data = data + " (" + AddressItem.getStringData(item.address, result.data, item.flags) + ')';
        }
        String s1 = item.getName();
        holder.name.setText(s1);
        holder.type.setText(item.getShortName());
        holder.region.setText(RegionList.getRegion(item.address));
        holder.address.setText(item.getStringAddress() + ": ");
        holder.value.setText(data);
        int v2 = item.getFreezeImageResource();
        holder.freeze.setImageResource(v2);
        MemoryContentAdapter.fillData(holder, item.address, item.data, SavedListAdapter.showMask, true, item.getSize());
        return view;
    }

    @Override  // android.widget.Adapter, android.widget.BaseAdapter
    public boolean hasStableIds() {
        return false;
    }

    public void loadData(BufferReader reader) {
        reader.reset();
        boolean updated = false;
        ListView listview = MainService.instance.mSavedListView;
        int v = listview.getFirstVisiblePosition();
        int v1 = listview.getLastVisiblePosition();
        LongSparseArrayChecked list = this.list;
        while(true) {
            try {
            label_6:
                if(reader.readInt() == 0) {
                    break;
                }
                long v2 = reader.readLong();
                long v3 = reader.readLongLong();
                int v4 = list.indexOfKey(v2);
                if(v4 < 0) {
                    goto label_6;
                }
                SavedItem item = (SavedItem)list.valueAt(v4);
                if(item == null || item.data == v3) {
                    goto label_6;
                }
                if(v <= v4 && v4 <= v1) {
                    updated = true;
                }
                item.data = v3;
            }
            catch(IOException e) {
                Log.e("???", e);
                break;
            }
        }
        if(updated) {
            this.notifyDataSetChanged();
        }
    }

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        ViewHolderBase holder = (ViewHolderBase)v.getTag();
        if(holder == null) {
            return;
        }
        try {
            this.list.setCheckAt(holder.pos, isChecked);
            MainService.instance.updateStatusBar();
        }
        catch(IndexOutOfBoundsException e) {
            Log.e("ArrayAdapter onCheckedChanged failed", e);
        }
        holder.updateBackground(isChecked, -2);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        switch(v.getId()) {
            case 0x7F0B0049: {  // id:address
                ViewHolderBase holder = (ViewHolderBase)v.getTag();
                if(holder != null) {
                    Tools.click(holder.ch);
                    return;
                }
                break;
            }
            case 0x7F0B0076: {  // id:addr_item_remove
                SavedItem selected = (SavedItem)v.getTag();
                if(selected != null) {
                    try {
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(selected.getName())).setMessage(Re.s(0x7F0700B0)).setPositiveButton(Re.s(0x7F07009B), (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> SavedListAdapter.this.removeAddr(this.val$selected.getAddress())).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                    }
                    catch(IndexOutOfBoundsException e) {
                        Log.e("SavedListAdapter onClick Failed", e);
                    }
                    return;
                }
                break;
            }
            case 0x7F0B0101: {  // id:freeze
                SavedItem savedItem0 = ((SavedItem)v.getTag()).copy();
                savedItem0.freeze = !savedItem0.freeze;
                this.add(savedItem0);
                if(v instanceof ImageButton) {
                    ((ImageButton)v).setImageResource(savedItem0.getFreezeImageResource());
                }
                MainService.instance.mDaemonManager.doWaited();
            }
        }

        class android.ext.SavedListAdapter.1 implements DialogInterface.OnClickListener {
            android.ext.SavedListAdapter.1(SavedItem savedItem0) {
            }

            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SavedListAdapter.this.remove(this.val$selected);
            }
        }

    }

    public void reloadData() {
        DaemonManager daemon = MainService.instance.mDaemonManager;
        daemon.doWaited();
        LongSparseArrayChecked list = this.list;
        if(list.size() == 0) {
            return;
        }
        else {
            try {
                ArrayList arrayList0 = new ArrayList(list.size());
                for(int i = 0; true; ++i) {
                    if(i >= list.size()) {
                        if(arrayList0.size() == 0) {
                            break;
                        }
                        int v1 = arrayList0.size();
                        int[] flags = new int[v1];
                        long[] addresses = new long[v1];
                        for(int i = 0; true; ++i) {
                            if(i >= v1) {
                                daemon.getMemoryItems(flags, addresses);
                                return;
                            }
                            AddressItem item = (AddressItem)arrayList0.get(i);
                            flags[i] = item.flags;
                            addresses[i] = item.address;
                        }
                    }
                    try {
                        SavedItem item = (SavedItem)list.valueAt(i);
                        if(item != null) {
                            arrayList0.add(item);
                        }
                    }
                    catch(IndexOutOfBoundsException e) {
                        Log.w("list changed", e);
                    }
                }
                return;
            }
            catch(OutOfMemoryError e) {
            }
        }
        Log.w("OOM in reloadData", e);
    }

    public void remove(int position) {
        SavedItem previous = (SavedItem)this.list.removeAt(position);
        if(previous != null) {
            if(previous.freeze) {
                DaemonManager daemon = MainService.instance.mDaemonManager;
                daemon.addForUnfreeze(previous);
                daemon.doWaited();
            }
            if(this.list.size() == 0) {
                this.truncate();
            }
            this.notifyDataSetChanged();
        }
    }

    // 检测为 Lambda 实现
    public void remove(SavedItem item) [...]

    public void remove(List list0) {
        LongSparseArrayChecked list = this.list;
        DaemonManager daemon = MainService.instance.mDaemonManager;
        for(Object object0: list0) {
            SavedItem item = (SavedItem)object0;
            if(item != null) {
                SavedItem previous = (SavedItem)list.delete(item.getAddress());
                if(previous != null && previous.freeze) {
                    daemon.addForUnfreeze(previous);
                }
            }
        }
        daemon.doWaited();
        if(list.size() == 0) {
            this.truncate();
        }
        this.notifyDataSetChanged();
    }

    public void remove(long[] addrs, int cnt) {
        LongSparseArrayChecked list = this.list;
        DaemonManager daemon = MainService.instance.mDaemonManager;
        for(int i = 0; i < cnt; ++i) {
            SavedItem previous = (SavedItem)list.delete(addrs[i]);
            if(previous != null && previous.freeze) {
                daemon.addForUnfreeze(previous);
            }
        }
        daemon.doWaited();
        if(list.size() == 0) {
            this.truncate();
        }
        this.notifyDataSetChanged();
    }

    public void removeAddr(long addr) {
        int v1 = this.list.indexOfKey(addr);
        if(v1 >= 0) {
            this.remove(v1);
        }
    }

    static void setShowMask(int val) {
        SavedListAdapter.showMask = val;
        new SPEditor().putInt("saved-list-format", val, 0).commit();
        SavedListAdapter.updateMask();
        MainService.instance.savedList.notifyDataSetChanged();
    }

    public static void sizeRemove(ImageView remove, boolean smallItems) {
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = remove.getLayoutParams();
        int dp = smallItems ? 24 : 0x30;
        int v1 = Config.getIconSize();
        if(v1 > dp) {
            dp = v1;
        }
        int px = (int)Tools.dp2px(dp);
        viewGroup$LayoutParams0.height = px;
        Config.setIconSize(remove, px, px, v1);
    }

    private void truncate() {
        this.list.truncate();
        MainService.instance.mDaemonManager.inOut.truncate();
    }

    static void updateMask() {
        String fill;
        TextView mSavedFormat = MainService.instance.mSavedFormat;
        if(mSavedFormat != null) {
            if(SavedListAdapter.showMask == 0) {
                fill = "   ";
            }
            else {
                fill = Integer.bitCount(SavedListAdapter.showMask) == 1 ? "  " : "";
            }
            mSavedFormat.setText(Tools.concat(new CharSequence[]{"F: ", MemoryContentAdapter.getValueFormat(SavedListAdapter.showMask), fill}));
        }
    }
}

