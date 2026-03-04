package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class TimersEditor implements DialogInterface.OnClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int CHECKED = 0;
    private static final int CNT = 3;
    private static final int COLS = 4;
    private static final int CURRENT = 2;
    private static final int GREEN = 0xFF005500;
    private static final int RED = 0xFF550000;
    private final int ROWS;
    private static final int USED = 1;
    private final boolean[] cols;
    private final boolean[] defaults;
    private static final int[] ids;
    private final String logName;
    private final boolean[] rows;
    private final String spPrefix;
    private final boolean[] state;
    private final int type;
    private WeakReference weakAdapter;

    static {
        TimersEditor.ids = new int[]{0x7F0B0147, 0x7F0B0148, 0x7F0B0149, 0x7F0B014A};  // id:app
    }

    public TimersEditor(String logName, String spPrefix, int ROWS, boolean[] defaults, int type) {
        this.weakAdapter = new WeakReference(null);
        this.logName = logName;
        this.spPrefix = spPrefix;
        this.ROWS = ROWS;
        this.defaults = defaults;
        this.type = type;
        this.rows = new boolean[ROWS];
        this.cols = new boolean[4];
        this.state = new boolean[ROWS * 12];
        Log.d((String.valueOf(logName) + ':'));
        this.getData();
    }

    public boolean[] getData() {
        boolean[] state = this.state;
        boolean[] data = new boolean[this.ROWS * 4];
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < this.ROWS * 4; ++i) {
            if(i % 4 == 0) {
                s.append(' ');
                s.append(i / 4 + 1);
                s.append(':');
            }
            if(state[i * 3]) {
                data[i] = true;
                s.append('1');
            }
            else {
                s.append('0');
            }
        }
        Log.d((this.logName + " get:" + s.toString()));
        return data;
    }

    View getHeader(View convertView, ViewGroup parent) {
        if(convertView == null || convertView.getId() != 0x7F04002B) {  // layout:timer_header
            View header = LayoutInflater.inflateStatic(0x7F04002B, parent, false);  // layout:timer_header
            header.setId(0x7F04002B);  // layout:timer_header
            for(int j = 0; j < 4; ++j) {
                TextView v = (TextView)header.findViewById(TimersEditor.ids[j]);
                v.setTag(j);
                v.setOnClickListener(this);
                v.setPaintFlags(v.getPaintFlags() | 8);
                v.setFocusable(true);
            }
            return header;
        }
        return convertView;
    }

    View getRow(int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater layoutInflater0 = LayoutInflater.getInflater();
        if(convertView != null && convertView.getId() == 0x7F04002C) {  // layout:timer_row
            row = convertView;
        }
        else {
            row = layoutInflater0.inflate(0x7F04002C, parent, false);  // layout:timer_row
            row.setId(0x7F04002C);  // layout:timer_row
            View[] holder = new View[5];
            for(int j = 0; j < 4; ++j) {
                CheckBox ch = (CheckBox)row.findViewById(TimersEditor.ids[j]);
                ch.setFocusable(true);
                ch.setOnCheckedChangeListener(this);
                holder[j] = ch;
            }
            TextView name = (TextView)row.findViewById(0x7F0B0051);  // id:name
            name.setPaintFlags(name.getPaintFlags() | 8);
            name.setOnClickListener(this);
            name.setFocusable(true);
            holder[4] = name;
            row.setTag(holder);
        }
        View[] holder = (View[])row.getTag();
        for(int j = 0; j < 4; ++j) {
            CheckBox ch = (CheckBox)holder[j];
            ch.setTag(null);
            ch.setChecked(this.state[-10 + 12 * position + 3 * j]);
            ch.setTag(((int)((position - 1) * 4 + j)));
            ch.setBackgroundColor((this.state[-11 + 12 * position + 3 * j] ? 0xFF005500 : 0xFF550000));
        }
        TextView name = (TextView)holder[4];
        name.setTag(((int)(position - 1)));
        name.setText(Integer.toString(position));
        return row;
    }

    private View getView() {
        boolean[] state = this.state;
        for(int i = 0; i < this.ROWS * 4; ++i) {
            state[i * 3 + 2] = state[i * 3];
        }
        View view0 = LayoutInflater.inflateStatic(0x7F04002D, null);  // layout:timer_table
        new FastScrollerFix(((ListView)view0));
        ((ListView)view0).setItemsCanFocus(true);
        android.ext.TimersEditor.1 adapter = new BaseAdapter() {
            @Override  // android.widget.Adapter
            public int getCount() {
                return TimersEditor.this.ROWS + 1;
            }

            @Override  // android.widget.Adapter
            public Object getItem(int position) {
                return position;
            }

            @Override  // android.widget.Adapter
            public long getItemId(int position) {
                return (long)position;
            }

            @Override  // android.widget.Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                return position == 0 ? TimersEditor.this.getHeader(convertView, parent) : TimersEditor.this.getRow(position, convertView, parent);
            }
        };
        ((ListView)view0).setAdapter(adapter);
        this.weakAdapter = new WeakReference(adapter);
        return view0;
    }

    public void load() {
        ProcessInfo info = MainService.instance.processInfo;
        if(info == null) {
            return;
        }
        boolean[] state = this.state;
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        String s = String.valueOf(this.spPrefix) + '-' + info.packageName + '-';
        boolean[] def = this.defaults;
        for(int i = 0; i < this.ROWS * 4; ++i) {
            try {
                state[i * 3] = sharedPreferences0.getBoolean(String.valueOf(s) + i, def[i]);
            }
            catch(Throwable unused_ex) {
                int v1 = sharedPreferences0.getInt(String.valueOf(s) + i, (def[i] ? 1 : 0));
                state[i * 3] = v1 == 0 || v1 == 1 ? v1 != 0 : def[i];
            }
        }
        this.getData();
    }

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView != null) {
            Object object0 = buttonView.getTag();
            if(object0 != null && object0 instanceof Integer) {
                this.state[((int)(((Integer)object0))) * 3 + 2] = isChecked;
            }
        }
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        boolean[] state = this.state;
        if(which == -3) {
            boolean[] def = this.defaults;
            for(int i = 0; i < this.ROWS * 4; ++i) {
                state[i * 3 + 2] = def[i];
            }
        }
        for(int i = 0; i < this.ROWS * 4; ++i) {
            state[i * 3] = state[i * 3 + 2];
        }
        this.save();
        MainService.instance.mDaemonManager.shConfig(this.type);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        boolean z = false;
        if(v.getId() == 0x7F0B0051) {  // id:name
            int v2 = (int)(((Integer)v.getTag()));
            this.rows[v2] = !this.rows[v2];
            for(int j = 0; j < 4; ++j) {
                this.state[(v2 * 4 + j) * 3 + 2] = this.rows[v2];
            }
        }
        else {
            int v = (int)(((Integer)v.getTag()));
            boolean[] arr_z = this.cols;
            if(!this.cols[v]) {
                z = true;
            }
            arr_z[v] = z;
            for(int i = 0; i < this.ROWS; ++i) {
                this.state[(i * 4 + v) * 3 + 2] = this.cols[v];
            }
        }
        this.updateView();
    }

    private void save() {
        ProcessInfo info = MainService.instance.processInfo;
        if(info == null) {
            return;
        }
        boolean[] state = this.state;
        SPEditor edit = new SPEditor();
        String s = String.valueOf(this.spPrefix) + '-' + info.packageName + '-';
        boolean[] def = this.defaults;
        for(int i = 0; i < this.ROWS * 4; ++i) {
            edit.putBoolean(String.valueOf(s) + i, state[i * 3], def[i]);
        }
        edit.commit();
    }

    public void show() {
        this.load();
        MainService.instance.mDaemonManager.shUsage(this.type);
        Alert.show(Alert.create().setView(Tools.getViewForAttach(this.getView())).setNeutralButton(Re.s(0x7F07023F), this).setNegativeButton(Re.s(0x7F0700A1), null).setPositiveButton(Re.s(0x7F07008C), this).create());  // string:reset_button "Reset"
    }

    public void updateUsage(boolean[] data) {
        boolean[] state = this.state;
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < this.ROWS * 4; ++i) {
            if(i % 4 == 0) {
                s.append(' ');
                s.append(i / 4 + 1);
                s.append(':');
            }
            boolean checked = data[i];
            state[i * 3 + 1] = checked;
            if(checked) {
                s.append('1');
            }
            else {
                s.append('0');
            }
        }
        Log.d((this.logName + " usage:" + s.toString()));
        this.updateView();
    }

    private void updateView() {
        BaseAdapter adapter = (BaseAdapter)this.weakAdapter.get();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}

