package android.ext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class History {
    static class HistoryAdapter extends BaseAdapter implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener {
        static class HItem {
            Item item;
            boolean pin;

            public HItem(Item item, boolean pin) {
                this.item = item;
                this.pin = pin;
            }

            @Override
            public String toString() {
                return this.item.toString();
            }
        }

        static class ViewHolder {
            public ImageView delete;
            HItem hItem;
            public TextView note;
            public EditText noteEdit;
            public ImageView pin;
            int position;
            public TextView value;
            public View view;

        }

        final Context context;
        AlertDialog dialog;
        static boolean editMode;
        final ArrayList items;
        private boolean softState;

        static {
            HistoryAdapter.editMode = false;
        }

        public HistoryAdapter(Context context, int type) {
            this.dialog = null;
            this.softState = false;
            this.context = context;
            History.fixSticked();
            ArrayList arrayList0 = new ArrayList();
            this.items = arrayList0;
            int i = 0;
            int sticked = History.sticked;
            for(Object object0: History.list) {
                Item item = (Item)object0;
                if(item != null && (item.type & type) != 0) {
                    arrayList0.add(new HItem(item, i < sticked));
                }
                ++i;
            }
        }

        private void editNote(Item item, Context context) {
            EditText edit = new EditText(context);
            edit.setText(this.getEditNote(item.note));
            AlertDialog alertDialog0 = Alert.create(context).setCustomTitle(Tools.getCustomTitle(item.value)).setView(edit).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:ok "OK"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    String s = edit.getText().toString();
                    HistoryAdapter.this.setNote(item, s, true);
                }
            }).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:cancel "Cancel"
            Alert.setOnShowListener(alertDialog0, new DialogInterface.OnShowListener() {
                @Override  // android.content.DialogInterface$OnShowListener
                public void onShow(DialogInterface d) {
                    Tools.selectAll(edit);
                    edit.requestFocus();
                }
            });
            Alert.show(alertDialog0, edit);
        }

        @Override  // android.widget.Adapter
        public int getCount() {
            return this.items.size();
        }

        private String getEditNote(String note) {
            String s1 = note.trim();
            return "...".equals(s1) ? "" : s1;
        }

        @Override  // android.widget.Adapter
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override  // android.widget.Adapter
        public long getItemId(int position) [...] // Inlined contents

        @Override  // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                convertView = LayoutInflater.from(this.context).inflate(0x7F040004, parent, false);  // layout:history_item
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.value = (TextView)convertView.findViewById(0x7F0B001B);  // id:value
                viewHolder.note = (TextView)convertView.findViewById(0x7F0B001C);  // id:note
                viewHolder.noteEdit = (EditText)convertView.findViewById(0x7F0B001D);  // id:note_edit
                viewHolder.noteEdit.addTextChangedListener(new TextWatcher() {
                    @Override  // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        HItem hitem = viewHolder.hItem;
                        if(hitem == null) {
                            return;
                        }
                        Item history$Item0 = hitem.item;
                        String s = viewHolder.noteEdit.getText().toString();
                        HistoryAdapter.this.setNote(history$Item0, s, false);
                    }

                    @Override  // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override  // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                Tools.setOnFocusChangeListener(viewHolder.noteEdit, this);
                viewHolder.pin = Config.setIconSize(((ImageView)convertView.findViewById(0x7F0B001A)));  // id:pin
                viewHolder.pin.setOnClickListener(this);
                viewHolder.pin.setOnLongClickListener(this);
                viewHolder.pin.setTag(viewHolder);
                viewHolder.delete = Config.setIconSize(((ImageView)convertView.findViewById(0x7F0B001E)));  // id:delete
                viewHolder.delete.setOnClickListener(this);
                viewHolder.delete.setOnLongClickListener(this);
                viewHolder.delete.setTag(viewHolder);
                viewHolder.view = convertView;
                convertView.setTag(viewHolder);
                convertView.setOnLongClickListener(this);
                convertView.setOnClickListener(this);
            }
            ViewHolder holder = (ViewHolder)convertView.getTag();
            HItem hitem = (HItem)this.items.get(position);
            holder.hItem = hitem;
            holder.value.setText(hitem.item.value);
            String note = hitem.item.note;
            holder.note.setText(note);
            holder.noteEdit.setText(this.getEditNote(note));
            holder.position = position;
            holder.note.setVisibility((HistoryAdapter.editMode ? 8 : 0));
            int edit = HistoryAdapter.editMode ? 0 : 8;
            holder.noteEdit.setVisibility(edit);
            holder.delete.setVisibility(edit);
            ((ViewGroup)convertView).setDescendantFocusability((HistoryAdapter.editMode ? 0x20000 : 0x60000));
            this.updateStyle(holder);
            return convertView;
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            boolean z = true;
            if(v != null) {
                int v = v.getId();
                if(v == 0x7F0B0005) {  // id:custom_title
                    if(HistoryAdapter.editMode) {
                        z = false;
                    }
                    HistoryAdapter.editMode = z;
                    if(!HistoryAdapter.editMode) {
                        this.setSoftState(v, false);
                    }
                    this.notifyDataSetChanged();
                    return;
                }
                ViewHolder holder = (ViewHolder)v.getTag();
                if(holder != null) {
                    int position = holder.position;
                alab1:
                    switch(v) {
                        case 0x7F0B001A: {  // id:pin
                            boolean pin = !holder.hItem.pin;
                            holder.hItem.pin = pin;
                            this.updateStyle(holder);
                            String s = holder.hItem.item.value;
                            if(!pin) {
                                z = false;
                            }
                            History.add(s, ((byte)z), holder.hItem.item.type);
                            return;
                        label_24:
                            ListView listView0 = Tools.getListView(v);
                            if(listView0 != null) {
                                AdapterView.OnItemClickListener adapterView$OnItemClickListener0 = listView0.getOnItemClickListener();
                                if(adapterView$OnItemClickListener0 != null) {
                                    adapterView$OnItemClickListener0.onItemClick(listView0, v, position, ((long)position));
                                    return;
                                }
                            }
                            break;
                        }
                        case 0x7F0B001E: {  // id:delete
                            Item remove = holder.hItem.item;
                            ListIterator listIterator0 = History.list.listIterator();
                            for(int i = 0; true; ++i) {
                                if(!listIterator0.hasNext()) {
                                    break alab1;
                                }
                                if(((Item)listIterator0.next()).equals(remove)) {
                                    listIterator0.remove();
                                    if(i < History.sticked) {
                                        --History.sticked;
                                    }
                                    this.items.remove(holder.hItem);
                                    this.notifyDataSetChanged();
                                    History.save();
                                    return;
                                }
                            }
                        }
                        default: {
                            goto label_24;
                        }
                    }
                }
            }
        }

        @Override  // android.view.View$OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus && HistoryAdapter.editMode) {
                return;
            }
            this.setSoftState(v, ((boolean)(HistoryAdapter.editMode & hasFocus)));
        }

        @Override  // android.view.View$OnLongClickListener
        public boolean onLongClick(View view) {
            if(!HistoryAdapter.editMode && view != null) {
                ViewHolder holder = null;
                while(view != null) {
                    Object object0 = view.getTag();
                    if(object0 instanceof ViewHolder) {
                        holder = (ViewHolder)object0;
                        break;
                    }
                    ViewParent viewParent0 = view.getParent();
                    if(!(viewParent0 instanceof View)) {
                        break;
                    }
                    view = (View)viewParent0;
                }
                if(holder != null) {
                    this.editNote(holder.hItem.item, view.getContext());
                    return true;
                }
            }
            return false;
        }

        void setNote(Item item, String note, boolean notify) {
            Item old = item;
            for(Object object0: History.list) {
                Item it = (Item)object0;
                if(item.equals(it)) {
                    item = it;
                    break;
                }
                if(false) {
                    break;
                }
            }
            String text = note.trim();
            if(text.length() == 0) {
                text = "...";
            }
            if(!text.equals(item.note)) {
                item.note = text;
                if(old != item) {
                    old.note = text;
                }
                if(notify) {
                    this.notifyDataSetChanged();
                }
                History.save();
            }
        }

        private void setSoftState(View v, boolean state) {
            if(this.softState != state && this.dialog != null) {
                Window window0 = this.dialog.getWindow();
                if(window0 != null) {
                    Tools.externalKeyboard(window0, v, state);
                    this.softState = state;
                }
            }
        }

        private void updateStyle(ViewHolder holder) {
            if(holder.hItem.pin) {
                this.updateStyle(holder, 0x7F02003B, 0x80744294);  // drawable:ic_pin_off_white_24dp
                return;
            }
            this.updateStyle(holder, 0x7F02003C, 0);  // drawable:ic_pin_white_24dp
        }

        private void updateStyle(ViewHolder holder, int resId, int color) {
            holder.pin.setImageResource(resId);
            holder.view.setBackgroundColor(color);
        }
    }

    static final class Item {
        int hash;
        String note;
        final int type;
        final String value;

        public Item(String value, int type, String note) {
            this.hash = 0;
            this.value = value;
            this.type = type;
            this.note = note;
        }

        // 去混淆评级： 低(20)
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Item && this.type == ((Item)obj).type && this.value.equals(((Item)obj).value);
        }

        @Override
        public int hashCode() {
            int h = this.hash;
            if(h == 0) {
                h = this.value.hashCode();
                String s = "#$@$#" + this.type;
                int v2 = s.length();
                for(int i = 0; i < v2; ++i) {
                    h = h * 0x1F + s.charAt(i);
                }
                this.hash = h;
            }
            return h;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    private static final String DEFAULT_NOTE = "...";
    public static final int NUMBER = 1;
    public static final int PATH = 4;
    public static final int SETTING = 8;
    public static final int SPEED = 16;
    public static final int TEXT = 2;
    static final LinkedList list;
    static int sticked;

    static {
        History.list = new LinkedList();
        History.sticked = 0;
    }

    public static void add(String item, byte sticky, int type) {
        if(item == null || item.length() == 0 || "0".equals(item) || "-1".equals(item) || "1".equals(item)) {
            return;
        }
        Item history$Item0 = new Item(item, type, "...");
        ListIterator listIterator0 = History.list.listIterator();
        for(int i = 0; listIterator0.hasNext(); ++i) {
            Item old = (Item)listIterator0.next();
            if(old.equals(history$Item0)) {
                history$Item0.note = old.note;
                listIterator0.remove();
                if(i >= History.sticked) {
                    break;
                }
                --History.sticked;
                if(sticky != 0) {
                    break;
                }
                sticky = 1;
                break;
            }
        }
        if(sticky == 0) {
            sticky = 2;
        }
        if(sticky == 1) {
            History.list.addFirst(history$Item0);
            ++History.sticked;
        }
        else {
            History.fixSticked();
            History.list.add(History.sticked, history$Item0);
        }
        History.truncate();
        History.save();
    }

    public static void add(String item, int type) {
        History.add(item, 0, type);
    }

    public static void clear() {
        History.list.clear();
        History.fixSticked();
    }

    static void fixSticked() {
        int val = History.sticked;
        if(val < 0) {
            History.sticked = 0;
            return;
        }
        int v1 = History.list.size();
        if(val > v1) {
            History.sticked = v1;
        }
    }

    public static int getSticked() {
        History.fixSticked();
        return History.sticked;
    }

    public static void init() {
        History.load();
    }

    private static void load() {
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        if(sharedPreferences0 == null) {
            return;
        }
        LinkedList list = History.list;
        int v = sharedPreferences0.getInt("history-size", 0);
        int max = v <= Config.historyLimit ? v : Config.historyLimit;
        boolean[] used = new boolean[History.roundUpToPowerOfTwo(max)];
        int usedMask = used.length - 1;
        int i = 0;
        while(i < v) {
            String s = sharedPreferences0.getString("history-" + i, null);
            if(s != null && s.length() != 0) {
                int type = sharedPreferences0.getInt("h-" + i, 1);
                if(type == 0) {
                    type = 1;
                }
                Item history$Item0 = new Item(s, type, sharedPreferences0.getString("hi-" + i, "..."));
                int pos = history$Item0.hashCode() & usedMask;
                if(!used[pos]) {
                    used[pos] = true;
                label_21:
                    list.addLast(history$Item0);
                    if(list.size() < max) {
                        goto label_23;
                    }
                    break;
                }
                else if(list.indexOf(history$Item0) == -1) {
                    goto label_21;
                }
            }
        label_23:
            ++i;
        }
        History.sticked = sharedPreferences0.getInt("history-sticked", 0);
        History.fixSticked();
        History.truncate();
    }

    private static int roundUpToPowerOfTwo(int x) {
        int v1 = x | x >> 1;
        int v2 = v1 | v1 >> 2;
        int v3 = v2 | v2 >> 4;
        int v4 = v3 | v3 >> 8;
        return (v4 | v4 >> 16) + 1;
    }

    static void save() {
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        if(sharedPreferences0 == null) {
            return;
        }
        SPEditor sPEditor0 = new SPEditor(sharedPreferences0.edit());
        int i = 0;
        for(Object object0: History.list) {
            sPEditor0.putString("history-" + i, ((Item)object0).value);
            sPEditor0.putInt("h-" + i, ((Item)object0).type, 1);
            sPEditor0.putString("hi-" + i, ((Item)object0).note, "...");
            ++i;
        }
        sPEditor0.putInt("history-size", i, 0);
        sPEditor0.putInt("history-sticked", History.sticked, 0);
        sPEditor0.commit();
    }

    public static void show(android.widget.EditText edit) {
        HistoryAdapter history$HistoryAdapter0 = new HistoryAdapter(Tools.getDarkContext(), (edit instanceof EditTextExt ? ((EditTextExt)edit).getDataType() : -1));
        if(history$HistoryAdapter0.getCount() == 0) {
            Tools.showToast(0x7F07011B);  // string:history_empty "History is empty"
            return;
        }
        HistoryAdapter.editMode = false;
        View view0 = Tools.getCustomTitle(Re.s(0x7F07011A), Re.s(0x7F070286));  // string:history "History"
        view0.setOnClickListener(history$HistoryAdapter0);
        history$HistoryAdapter0.dialog = Alert.create(Tools.getDarkContext()).setCustomTitle(view0).setAdapter(history$HistoryAdapter0, new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String s = history$HistoryAdapter0.getItem(which).toString();
                int v1 = Math.max(edit.getSelectionStart(), 0);
                int v2 = Math.max(edit.getSelectionEnd(), 0);
                edit.getText().replace(Math.min(v1, v2), Math.max(v1, v2), s, 0, s.length());
                int v3 = Math.max(edit.getSelectionStart(), edit.getSelectionEnd());
                edit.setSelection(v3);
            }
        }).create();
        Alert.show(history$HistoryAdapter0.dialog);
    }

    private static void truncate() {
        LinkedList list = History.list;
        int limit = Config.historyLimit;
        if(list.size() <= limit) {
            return;
        }
        for(int i = list.size() - limit; i > 0; --i) {
            list.removeLast();
        }
        History.fixSticked();
    }
}

