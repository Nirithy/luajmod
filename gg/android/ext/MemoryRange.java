package android.ext;

import android.content.Context;
import android.fix.LayoutInflater;
import android.fix.LinearLayout;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MemoryRange extends LinearLayout implements View.OnClickListener {
    public static final int MEMORY_FROM = 0;
    public static final int MEMORY_TO = 1;
    public static final int TYPE_ALL = 0;
    public static final int TYPE_NEARBY = 2;
    public static final int TYPE_RANGE = 1;
    private EditText address;
    private CheckBox after;
    private CheckBox before;
    private EditText distance;
    private Runnable listener;
    private EditText memoryFrom;
    private EditText memoryTo;
    private View nearbyRow;
    private Button range;
    private View rangeRow;
    private int type;

    public MemoryRange(Context context) {
        super(context);
        this.listener = null;
        this.type = 0;
        this.range = null;
    }

    public MemoryRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.listener = null;
        this.type = 0;
        this.range = null;
    }

    public MemoryRange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.listener = null;
        this.type = 0;
        this.range = null;
    }

    public MemoryRange(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.listener = null;
        this.type = 0;
        this.range = null;
    }

    public static long getMem(int index, EditText memoryFrom, String defaultFrom, EditText memoryTo, String defaultTo) throws NumberFormatException {
        EditText edit = index == 0 ? memoryFrom : memoryTo;
        String data = SearchMenuItem.checkScript(edit.getText().toString().trim());
        if(data.length() == 0) {
            data = index == 0 ? defaultFrom : defaultTo;
        }
        String from = SearchMenuItem.checkScript(memoryFrom.getText().toString().trim());
        boolean z = from.contains("?");
        if(z) {
            data = from.replace("?", (index == 0 ? "0" : "F"));
            if(index != 0) {
                edit.setText(data);
            }
        }
        try {
            long v1 = ParserNumbers.parseBigLong(data, 16);
            if(index == 0 && z) {
                History.add(from, 1);
            }
            History.add(data, 1);
            return v1;
        }
        catch(NumberFormatException e) {
            edit.requestFocus();
            throw e;
        }
    }

    public long getMem(int index) throws NumberFormatException {
        long distance;
        long address;
        switch(this.type) {
            case 1: {
                long v1 = MemoryRange.getMem(index, this.memoryFrom, "0", this.memoryTo, "-1");
                if(index == 0) {
                    Config.memoryFrom = v1;
                }
                else {
                    Config.memoryTo = v1;
                }
                Config.save();
                return v1;
            }
            case 2: {
                String data = SearchMenuItem.checkScript(this.address.getText().toString().trim());
                try {
                    address = ParserNumbers.parseBigLong(data, 16);
                    History.add(data, 1);
                }
                catch(NumberFormatException e) {
                    this.address.requestFocus();
                    throw e;
                }
                if((index == 0 ? this.before : this.after).isChecked()) {
                    String data = SearchMenuItem.checkScript(this.distance.getText().toString().trim());
                    try {
                        distance = ParserNumbers.parseBigLong(data, 16);
                        History.add(data, 1);
                    }
                    catch(NumberFormatException e) {
                        this.distance.requestFocus();
                        throw e;
                    }
                    Config.nearbyDistance = distance;
                    Config.save();
                    if(index == 0) {
                        distance = -distance;
                    }
                    return address + distance;
                }
                return address;
            }
            default: {
                return index == 0 ? 0L : -1L;
            }
        }
    }

    public int getType() {
        return this.type;
    }

    public void init() {
        if(this.range != null) {
            return;
        }
        LayoutInflater.inflateStatic(0x7F04000D, this);  // layout:memory_range
        this.range = (Button)this.findViewById(0x7F0B0046);  // id:range
        this.rangeRow = this.findViewById(0x7F0B0047);  // id:range_row
        this.memoryFrom = (EditText)this.findViewById(0x7F0B0003);  // id:memory_from
        this.memoryFrom.setDataType(1);
        this.findViewById(0x7F0B0004).setTag(this.memoryFrom);  // id:region_from
        this.memoryTo = (EditText)this.findViewById(0x7F0B0008);  // id:memory_to
        this.memoryTo.setDataType(1);
        this.findViewById(0x7F0B0009).setTag(this.memoryTo);  // id:region_to
        this.memoryFrom.setText("00000000");
        this.memoryTo.setText("00000000");
        this.nearbyRow = this.findViewById(0x7F0B0048);  // id:nearby_row
        this.address = (EditText)this.findViewById(0x7F0B0049);  // id:address
        this.address.setDataType(1);
        this.before = (CheckBox)this.findViewById(0x7F0B004A);  // id:before
        this.after = (CheckBox)this.findViewById(0x7F0B004B);  // id:after
        this.distance = (EditText)this.findViewById(0x7F0B004C);  // id:distance
        this.distance.setDataType(1);
        this.distance.setText("0");
        this.range.setOnClickListener(this);
        InternalKeyboard.setFlagsFor(this.memoryFrom, 1);
        this.update();
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        this.setType((this.type + 1) % 3);
    }

    public void setAddress(String address) {
        this.address.setText(address);
    }

    public void setType(int type) {
        int old = this.type;
        this.type = type;
        this.update();
        Runnable l = this.listener;
        if(old != type && l != null) {
            l.run();
        }
    }

    public void setTypeChangeListener(Runnable listener) {
        this.listener = listener;
    }

    private void update() {
        int v2;
        int v = 0;
        int type = this.type;
        Button button0 = this.range;
        if(type == 1) {
            v2 = 0x7F0701A8;  // string:only_memory "Only within the memory range:"
        }
        else {
            v2 = type == 2 ? 0x7F070352 : 0x7F07025C;  // string:nearby "Nearby:"
        }
        button0.setText(Re.s(v2));
        this.rangeRow.setVisibility((type == 1 ? 0 : 8));
        View view0 = this.nearbyRow;
        if(type != 2) {
            v = 8;
        }
        view0.setVisibility(v);
    }
}

