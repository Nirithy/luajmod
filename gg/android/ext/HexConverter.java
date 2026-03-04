package android.ext;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.ImageButtonView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View;
import java.util.ArrayList;
import java.util.Locale;

public class HexConverter extends ImageButtonView implements View.OnClickListener {
    private boolean useXor;

    public HexConverter(Context context) {
        super(context);
        this.useXor = true;
        this.init();
    }

    public HexConverter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.useXor = true;
        this.init();
    }

    public HexConverter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.useXor = true;
        this.init();
    }

    public HexConverter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.useXor = true;
        this.init();
    }

    private void init() {
        this.setOnClickListener(this);
        if(!this.isInEditMode()) {
            this.setImageResource(0x7F02000C);  // drawable:ic_arrow_down_drop_circle_white_24dp
            Config.setIconSize(this);
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        int showMask;
        String value;
        Object object0 = this.getTag();
        EditText ed = null;
        int flags = 0;
        if(object0 instanceof EditText) {
            ed = (EditText)object0;
        }
        else if(object0 instanceof Object[] && ((Object[])object0).length == 2 && ((Object[])object0)[0] instanceof EditText) {
            ed = (EditText)((Object[])object0)[0];
            if(((Object[])object0)[1] instanceof Integer) {
                flags = (int)(((Integer)((Object[])object0)[1]));
            }
            else if(((Object[])object0)[1] instanceof SystemSpinnerType) {
                flags = ((SystemSpinnerType)((Object[])object0)[1]).getSelected();
            }
        }
        if(flags == 0) {
            flags = 0x20;
        }
        if(ed == null) {
            return;
        }
        ed.requestFocus();
        Editable editable0 = ed.getText();
        int start = ed.getSelectionStart();
        int end = ed.getSelectionEnd();
        boolean fullText = start < 0 || end < 0 || start == end;
        if(fullText) {
            start = 0;
            end = editable0.length();
        }
        try {
            value = editable0.subSequence(start, end).toString().trim();
        }
        catch(StringIndexOutOfBoundsException e) {
            Log.w(("subSequence fail for " + start + ", " + end + "; " + editable0.length()), e);
            start = 0;
            end = editable0.length();
            value = editable0.toString();
            fullText = true;
        }
        if(value.length() == 0) {
            Tools.showToast(Re.s(0x7F0702FE));  // string:empty_field "Empty input field: no number to convert"
            return;
        }
        try {
            if(ParserNumbers.typeAsm(value) != 0) {
                fullText = false;
                int v3 = value.charAt(1);
                if(v3 == 84) {
                    value = value.substring(2).trim();
                    value = Integer.toHexString(ArmDis.getThumbOpcode(null, 0L, value)).toUpperCase(Locale.US) + 'h';
                }
                else if(v3 == 65 && value.charAt(2) != 56) {
                    value = value.substring(2).trim();
                    value = Integer.toHexString(ArmDis.getArmOpcode(null, 0L, value)).toUpperCase(Locale.US) + 'h';
                }
            }
            if(fullText) {
                char[] arr_c = {ParserNumbers.decimalSeparator, ':', ';', '~', 'X', 'W', 'Q'};
                for(int v4 = 0; v4 < 7; ++v4) {
                    int v5 = value.indexOf(((int)arr_c[v4]));
                    if(v5 > 0) {
                        value = value.substring(0, v5);
                    }
                }
                end = value.length();
            }
            long val = ParserNumbers.parseLong(value).value;
            ArrayList list = new ArrayList();
            ArrayList replace = new ArrayList();
            String s1 = Tools.stringFormat("%,d", new Object[]{val});
            list.add(s1);
            replace.add(s1);
            String str = this.trim(AddressItem.getStringHexData(0L, val, flags), '0', true) + 'h';
            list.add(str);
            replace.add(str);
            String str = this.trim(AddressItem.getStringRhexData(0L, val, flags), '0', false) + 'r';
            list.add(str);
            replace.add(str);
            if(this.useXor) {
                String str = Tools.stringFormat("%,d", new Object[]{((long)(((long)Config.xorKey) ^ val))});
                list.add(Tools.stringFormat("XOR %,d = %s", new Object[]{Config.xorKey, str}));
                replace.add(str);
                list.add("XOR ... = ???");
                replace.add(null);
            }
            if(val != 0L) {
                byte[] arr_b = ParserNumbers.getBytes(val);
                int size = arr_b[8];
                if(size != -1) {
                    try {
                        String str = ":" + new String(arr_b, 0, size + 1, "UTF-8");
                        list.add("UTF-8: " + str);
                        replace.add(str);
                    }
                    catch(Throwable e) {
                        Log.d(("HexConverter: " + value), e);
                    }
                    if((size & 1) == 0) {
                        ++size;
                    }
                    try {
                        String str = ";" + new String(arr_b, 0, size + 1, "UTF-16LE");
                        list.add("UTF-16LE: " + str);
                        replace.add(str);
                    }
                    catch(Throwable e) {
                        Log.d(("HexConverter: " + value), e);
                    }
                }
            }
            int v8 = MainService.instance.getTabIndex();
            if(v8 == 1) {
                showMask = AddressArrayAdapter.showMask;
            }
            else {
                switch(v8) {
                    case 2: {
                        showMask = SavedListAdapter.showMask;
                        break;
                    }
                    case 3: {
                        showMask = MainService.instance.mMemListAdapter.showMask;
                        break;
                    }
                    default: {
                        showMask = 0;
                    }
                }
            }
            if(showMask != 0) {
                for(int i = 0; true; ++i) {
                    String op = null;
                    if(i >= 3) {
                        break;
                    }
                    if((i != 0 || (showMask & 16) != 0) && ((i != 1 || (showMask & 0x20) != 0) && (i != 2 || (showMask & 0x40) != 0))) {
                        if(i == 0) {
                            op = ArmDis.getArmOpcode(null, 0L, val);
                        }
                        if(i == 1) {
                            op = ArmDis.getThumbOpcode(null, 0L, val);
                        }
                        if(i == 2) {
                            op = Arm64Dis.disasm(Arm64Dis.args(), 0L, ((int)val), new StringBuilder()).toString();
                        }
                        if(op != null) {
                            String op = op.trim();
                            int v11 = op.indexOf(59);
                            if(v11 != -1) {
                                op = op.substring(0, v11).trim();
                            }
                            String name = null;
                            if(op.length() > 0) {
                                if(i == 0) {
                                    name = "ARM (x32)";
                                    op = "~A " + op;
                                }
                                if(i == 1) {
                                    name = "Thumb";
                                    op = "~T " + op;
                                }
                                if(i == 2) {
                                    name = "ARM (x64)";
                                    op = "~A8 " + op;
                                }
                                list.add(name + ": " + op);
                                replace.add(op);
                            }
                        }
                    }
                }
            }
            android.ext.HexConverter.1 hexConverter$10 = new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if(which >= 0 && which < replace.size()) {
                            String repl = (String)replace.get(which);
                            if(repl == null) {
                                Option config$Option0 = Config.get(0x7F0B00BB);  // id:config_xor_key
                                config$Option0.setOnChangedListner(new OnChangedListener() {
                                    @Override  // android.ext.Config$Option$OnChangedListener
                                    public void onChanged(int value) {
                                        config$Option0.setOnChangedListner(null);
                                        String s = Tools.stringFormat("%,d", new Object[]{((long)(this.val$val ^ ((long)Config.xorKey)))});
                                        try {
                                            this.val$edit.setSelection(this.val$from, this.val$to);
                                            this.val$text.replace(this.val$from, this.val$to, s);
                                        }
                                        catch(IndexOutOfBoundsException e) {
                                            Log.w("Failed replace text", e);
                                            this.val$edit.setText(s);
                                        }
                                        this.val$edit.requestFocus();
                                    }
                                });
                                config$Option0.change();
                            }
                            else {
                                try {
                                    ed.setSelection(start, end);
                                    editable0.replace(start, end, repl);
                                }
                                catch(IndexOutOfBoundsException e) {
                                    Log.w("Failed replace text", e);
                                    ed.setText(repl);
                                }
                            }
                        }
                        ed.requestFocus();
                    }
                    catch(Throwable e) {
                        Log.d(("HexConverter failed with " + which), e);
                    }
                }
            };
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle((value + " →"))).setItems(((CharSequence[])list.toArray(new String[list.size()])), hexConverter$10).setNegativeButton(Re.s(0x7F0700A1), hexConverter$10).create());  // string:cancel "Cancel"
        }
        catch(Throwable e) {
            Tools.showToast((Re.s(0x7F0702FF) + "\n\n" + e.getMessage()));  // string:need_integer "You must select an integer to convert."
            Log.d(("HexConverter: " + value), e);
        }
    }

    public void setUseXor(boolean value) {
        this.useXor = value;
    }

    private String trim(String hex, char remove, boolean fromStart) {
        int start = 0;
        int end = hex.length();
        if(fromStart) {
            for(int i = 0; i < end - 2 && hex.charAt(i) == remove; ++i) {
                start = i + 1 & -2;
            }
        }
        else {
            for(int i = end - 1; i >= 2 && hex.charAt(i) == remove; --i) {
                end = i + 1 & -2;
            }
        }
        return hex.substring(start, end);
    }
}

