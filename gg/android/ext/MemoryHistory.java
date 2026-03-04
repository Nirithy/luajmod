package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryHistory implements DialogInterface.OnClickListener {
    public static class HistoryItem {
        long address;
        private CharSequence description;
        String packageName;
        int top;

        public HistoryItem(long address, CharSequence description, String packageName, int top) {
            this.address = address;
            this.description = description;
            this.packageName = packageName;
            this.top = top;
        }

        public CharSequence toCharSequence() {
            return Tools.concat(new CharSequence[]{Tools.colorize(AddressItem.getStringAddress(this.address, 0), Tools.getColor(0x7F0A0003)), Tools.colorize((": " + this.description), -1)});  // color:address
        }
    }

    public static class MemoryBack extends MenuItem implements View.OnLongClickListener {
        private final MemoryHistory history;

        public MemoryBack(MemoryHistory history) {
            super(0x7F07024A, 0x7F020030);  // string:back "Back"
            this.history = history;
        }

        @Override  // android.ext.MenuItem
        public ImageView getButton(boolean fromUI) {
            ImageView imageView0 = super.getButton(fromUI);
            imageView0.setOnLongClickListener(this);
            return imageView0;
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            this.history.back();
        }

        @Override  // android.view.View$OnLongClickListener
        public boolean onLongClick(View v) {
            this.history.choice();
            return true;
        }
    }

    public static class MemoryForward extends MenuItem implements View.OnLongClickListener {
        private final MemoryHistory history;

        public MemoryForward(MemoryHistory history) {
            super(0x7F07024B, 0x7F020031);  // string:forward "Forward"
            this.history = history;
        }

        @Override  // android.ext.MenuItem
        public ImageView getButton(boolean fromUI) {
            ImageView imageView0 = super.getButton(fromUI);
            imageView0.setOnLongClickListener(this);
            return imageView0;
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            this.history.forward();
        }

        @Override  // android.view.View$OnLongClickListener
        public boolean onLongClick(View v) {
            this.history.choice();
            return true;
        }
    }

    private static final int MAX_SIZE = 100;
    private final List list;
    private int position;

    public MemoryHistory() {
        this.position = 0;
        this.list = new ArrayList();
    }

    public boolean add(long address, CharSequence description) {
        return this.add(address, description, 0);
    }

    public boolean add(long address, CharSequence description, int top) {
        int size = this.list.size();
        if(this.position < size) {
            for(int i = size - 1; i >= this.position; --i) {
                this.list.remove(i);
            }
            size = this.position;
        }
        if(size >= 100) {
            --this.position;
            this.list.remove(0);
        }
        ++this.position;
        ProcessInfo pi = MainService.instance.processInfo;
        HistoryItem memoryHistory$HistoryItem0 = new HistoryItem(address, description, (pi == null ? null : pi.packageName), top);
        return this.list.add(memoryHistory$HistoryItem0);
    }

    public void back() {
        if(this.list.size() == 0) {
            Tools.showToast(0x7F07011B);  // string:history_empty "History is empty"
            return;
        }
        if(this.position < 2) {
            Tools.showToast(0x7F07024C, 0);  // string:no_previous "No previous entries"
            return;
        }
        this.onClick(null, this.position - 2);
    }

    public void choice() {
        int v = this.list.size();
        if(v == 0) {
            Tools.showToast(0x7F07011B);  // string:history_empty "History is empty"
            return;
        }
        CharSequence[] items = new CharSequence[v];
        Drawable[] icons = new Drawable[v];
        HashMap cache = new HashMap();
        for(int i = 0; i < v; ++i) {
            HistoryItem item = (HistoryItem)this.list.get(i);
            items[i] = item.toCharSequence();
            if(item.packageName != null) {
                Drawable cached = (Drawable)cache.get(item.packageName);
                if(cached == null) {
                    try {
                        cached = Tools.tryCloneIcon(Tools.getResized(Tools.getApplicationIcon(Tools.getApplicationInfo(item.packageName)), Tools.dp2px48()));
                    }
                    catch(Throwable e) {
                        Log.e(("Failed load icon for " + item.packageName), e);
                    }
                    cache.put(item.packageName, cached);
                }
                icons[i] = cached;
            }
        }
        AlertDialog alertDialog0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F070100)).setSingleChoiceItems(items, this.position - 1, this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:goto_address "Go to the address:"
        ListView listView0 = alertDialog0.getListView();
        if(listView0 != null) {
            Tools.addIconsToListViewItems(listView0, items, icons, 0x30, 0x7F090002);  // style:SmallText
        }
        Alert.show(alertDialog0);
    }

    public void forward() {
        int v = this.list.size();
        if(v == 0) {
            Tools.showToast(0x7F07011B);  // string:history_empty "History is empty"
            return;
        }
        if(this.position >= v) {
            Tools.showToast(0x7F07024D, 0);  // string:no_next "No next entries"
            return;
        }
        this.onClick(null, this.position);
    }

    public long getCurrent() {
        return this.position <= 0 || this.position > this.list.size() ? 0L : ((HistoryItem)this.list.get(this.position - 1)).address;
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        HistoryItem item = (HistoryItem)this.list.get(which);
        if(item != null) {
            this.position = which + 1;
            MainService.instance.goToAddress(item.address, item.top);
        }
        if(dialog != null) {
            Tools.dismiss(dialog);
        }
    }
}

