package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OffsetCalculator extends MenuItem implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, TextWatcher, CompoundButton.OnCheckedChangeListener, Runnable {
    private EditText editAddr;
    private EditText editOffset;
    private CheckBox hex;
    private static String lastInput;
    private static String lastOffset;
    long result;
    private ImageView save;
    private final Long source;
    private TextView textResult;
    private static boolean useHex;
    private TextView valueByte;
    private TextView valueDouble;
    private TextView valueDword;
    private TextView valueFloat;
    private TextView valueHex;
    private TextView valueJava;
    private TextView valueQword;
    private TextView valueRHex;
    private TextView valueString;
    private TextView valueWord;
    private TextView valueXor;

    static {
        OffsetCalculator.lastInput = "";
        OffsetCalculator.lastOffset = "";
        OffsetCalculator.useHex = true;
    }

    public OffsetCalculator() {
        this(null);
    }

    public OffsetCalculator(Long source) {
        super(0x7F07016D, 0x7F020010);  // string:offset_calculator "Offset calculator"
        this.result = 0L;
        this.editAddr = null;
        this.editOffset = null;
        this.textResult = null;
        this.hex = null;
        this.save = null;
        this.source = source;
    }

    @Override  // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        this.update();
    }

    @Override  // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    private int getSize(long address, long data, int size, TextView view, int type, int typeSize, int align, boolean clear) {
        String value;
        if(clear || (((long)(align - 1)) & address) != 0L) {
            value = "-";
        }
        else {
            value = AddressItem.getStringDataTrim(address, data, type);
            if(size == 0) {
                size = typeSize;
            }
        }
        view.setText(String.valueOf(value) + AddressItem.getShortName(type) + ';');
        return size;
    }

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.update();
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        String s = AddressItem.getStringAddress(this.result, 4);
        switch(which) {
            case -3: {
                History.add(this.editOffset.getText().toString().trim(), 1);
                Tools.copyText(s);
                return;
            }
            case -1: {
                History.add(this.editOffset.getText().toString().trim(), 1);
                MainService mainService0 = MainService.instance;
                String s1 = Tools.stripColon(0x7F070252) + ": (" + this.editAddr.getText().toString().trim() + " + " + this.editOffset.getText().toString().trim() + ')';  // string:from_calc "From the offset calculator:"
                mainService0.goToAddress(this.source, s, s1);
            }
        }
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        if(v != null && v.getId() == 0x7F0B012C) {  // id:save
            this.save();
            return;
        }
        new OffsetCalculator().show(null);
    }

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        if(this.editAddr != null) {
            OffsetCalculator.useHex = this.hex.isChecked();
            OffsetCalculator.lastOffset = this.editOffset.getText().toString().trim();
            OffsetCalculator.lastInput = this.editAddr.getText().toString().trim();
        }
    }

    @Override  // android.content.DialogInterface$OnShowListener
    public void onShow(DialogInterface dialog) {
        if(this.editAddr.getText().length() > 0) {
            this.editOffset.requestFocus();
        }
        else {
            this.editAddr.requestFocus();
        }
        Tools.selectAll(this.editOffset);
        this.update();
    }

    @Override  // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.update();
    }

    @Override
    public void run() {
        long address;
        try {
            address = this.result;
            int v1 = AddressItem.getTypeForAddress(address, true);
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OffsetCalculator.this.updateValue(address, MainService.instance.mDaemonManager.getMemoryContent(address, v1), false);
                    }
                    catch(Throwable e) {
                        Log.w(("Failed update value: " + address + ' ' + MainService.instance.mDaemonManager.getMemoryContent(address, v1)), e);
                    }
                }
            });
        }
        catch(Throwable e) {
            Log.w(("Failed update value: " + address), e);
        }
    }

    private void save() {
        SparseIntArray counts = new SparseIntArray();
        new TypeSelector(AddressItem.getDataForEditAll(AddressItem.getTypeForAddress(this.result, false, counts)), counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int flags) {
                SavedListAdapter savedList = MainService.instance.savedList;
                SavedItem savedItem0 = new SavedItem(OffsetCalculator.this.result, 0L, flags);
                if(!savedItem0.isPossibleItem()) {
                    savedItem0.flags = AddressItem.getTypeForAddress(savedItem0.address, true);
                }
                SavedItem savedItem1 = savedList.getItemByAddress(OffsetCalculator.this.result);
                if(savedItem1 == null || savedItem1.flags != savedItem0.flags) {
                    savedList.add(savedItem0);
                    savedList.reloadData();
                }
                Tools.showToast(Tools.stringFormat(Re.s(0x7F07013D), new Object[]{1}));  // string:items_added "Items (__d__) added to the saved list"
            }
        });
    }

    public void show(String addrString) {
        View view0 = LayoutInflater.inflateStatic(0x7F040023, null);  // layout:service_offset
        this.editAddr = (EditText)view0.findViewById(0x7F0B0049);  // id:address
        this.editOffset = (EditText)view0.findViewById(0x7F0B012A);  // id:offset
        this.textResult = (TextView)view0.findViewById(0x7F0B012B);  // id:result
        this.save = Config.setIconSize(((ImageView)view0.findViewById(0x7F0B012C)));  // id:save
        this.save.setOnClickListener(this);
        this.hex = (CheckBox)view0.findViewById(0x7F0B0066);  // id:hex
        this.hex.setChecked(OffsetCalculator.useHex);
        this.hex.setOnCheckedChangeListener(this);
        this.valueHex = (TextView)view0.findViewById(0x7F0B006B);  // id:value_hex
        this.valueRHex = (TextView)view0.findViewById(0x7F0B006C);  // id:value_rhex
        this.valueString = (TextView)view0.findViewById(0x7F0B006D);  // id:value_string
        this.valueJava = (TextView)view0.findViewById(0x7F0B006E);  // id:value_java
        this.valueDword = (TextView)view0.findViewById(0x7F0B0123);  // id:value_dword
        this.valueFloat = (TextView)view0.findViewById(0x7F0B0124);  // id:value_float
        this.valueDouble = (TextView)view0.findViewById(0x7F0B0125);  // id:value_double
        this.valueWord = (TextView)view0.findViewById(0x7F0B0126);  // id:value_word
        this.valueByte = (TextView)view0.findViewById(0x7F0B0127);  // id:value_byte
        this.valueQword = (TextView)view0.findViewById(0x7F0B0128);  // id:value_qword
        this.valueXor = (TextView)view0.findViewById(0x7F0B0129);  // id:value_xor
        EditText editText0 = this.editAddr;
        if(addrString == null) {
            addrString = "";
        }
        editText0.setText(addrString);
        this.editAddr.setDataType(1);
        this.editOffset.setText("");
        this.editOffset.setDataType(1);
        this.editAddr.addTextChangedListener(this);
        this.editOffset.addTextChangedListener(this);
        this.update();
        AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view0)).setPositiveButton(Re.s(0x7F07008D), this).setNegativeButton(Re.s(0x7F0700A1), null).setNeutralButton(Re.s(0x7F070161), this).create();  // string:goto1 "Goto"
        Alert.setOnDismissListener(alertDialog0, this);
        Alert.setOnShowListener(alertDialog0, this);
        Alert.show(alertDialog0, this.editOffset);
    }

    private void update() {
        long addr;
        int v = 16;
        if(this.editOffset == null || this.textResult == null) {
            return;
        }
        StringBuilder out = new StringBuilder();
        try {
            addr = ParserNumbers.parseBigLong(this.editAddr.getText().toString().trim(), 16);
        }
        catch(Throwable e) {
            Log.d("Failed parse", e);
            if(out.length() != 0) {
                out.append("; ");
            }
            out.append(Re.s(0x7F07008E));  // string:address "Address:"
            out.append(' ');
            out.append(e.getMessage());
            addr = 0L;
        }
        String s = this.editOffset.getText().toString().trim();
        try {
            if(!this.hex.isChecked()) {
                v = 10;
            }
            this.result = addr + ParserNumbers.parseBigLong(s, v);
        }
        catch(Throwable e) {
            Log.d("Failed parse", e);
            if(out.length() != 0) {
                out.append("; ");
            }
            out.append(Re.s(0x7F070169));  // string:offset "Offset:"
            out.append(' ');
            out.append(e.getMessage());
            this.result = 0L;
        }
        if(out.length() == 0) {
            out.append(AddressItem.getStringAddress(this.result, 4));
            Handler handler0 = ThreadManager.getHandlerMainThread();
            handler0.removeCallbacks(this);
            handler0.post(this);
        }
        else {
            this.updateValue(0L, 0L, true);
        }
        this.textResult.setText(out.toString());
    }

    void updateValue(long address, long value, boolean clear) {
        String text;
        if(this.textResult == null) {
            return;
        }
        int size = this.getSize(address, value, this.getSize(address, value, this.getSize(address, value, this.getSize(address, value, this.getSize(address, value, this.getSize(address, value, this.getSize(address, value, 0, this.valueDouble, 0x40, 8, 4, clear), this.valueQword, 0x20, 8, 4, clear), this.valueFloat, 16, 4, 4, clear), this.valueDword, 4, 4, 4, clear), this.valueXor, 8, 4, 4, clear), this.valueWord, 2, 2, 2, clear), this.valueByte, 1, 1, 1, clear);
        if(size == 0) {
            size = 1;
        }
        if(clear) {
            text = "-h;";
        }
        else {
            if(size != 8) {
                value &= (1L << size * 8) - 1L;
            }
            text = ToolsLight.prefixLongHex(size * 2, value) + "h;";
        }
        this.valueHex.setText(text);
        String text = clear ? "-r;" : ToolsLight.prefixLongHex(size * 2, (size == 8 ? Long.reverseBytes(value) >> 0x40 - size * 8 : Long.reverseBytes(value) >> 0x40 - size * 8 & (1L << size * 8) - 1L)) + "r;";
        this.valueRHex.setText(text);
        String text = clear ? "-;" : "\'" + MemoryContentAdapter.longToString(value, size) + "\';";
        this.valueString.setText(text);
        String text = clear ? "-;" : "\"" + MemoryContentAdapter.longToJava(value, size) + "\";";
        this.valueJava.setText(text);
    }
}

