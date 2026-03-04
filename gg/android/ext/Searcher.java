package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.fix.SparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.Locale;

public class Searcher implements DialogInterface.OnClickListener, TextWatcher, View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, Runnable {
    private static final int ASM_ARM = -500;
    private static final int ASM_ARM64 = -700;
    private static final int ASM_THUMB = -600;
    public static final String FUZZY_EQUAL = "N = O+D";
    public static final String FUZZY_GREATER = "N > O+D";
    public static final String FUZZY_LESS = "N < O+D";
    public static final String FUZZY_NOT_EQUAL = "N ≠ O+D";
    private static final int HEX = -300;
    private static final int HEX_UTF = -400;
    public static final String MASK_PLACEHOLDER = "?";
    public static final int MODE_SEARCH = 0;
    public static final int MODE_SEARCH_DIFF = 2;
    public static final int MODE_SEARCH_FUZZY = 1;
    public static final int MODE_SEARCH_MASK = 3;
    public static final int MODE_SEARCH_POINTER = 4;
    private static final int UTF_16LE = -200;
    private static final int UTF_8 = -100;
    private final Button cancel;
    private String examplesList;
    private final Button extBtn;
    private final int flags;
    public volatile boolean focusValue;
    public static volatile boolean fuzzyExtended;
    private final View fuzzyRow;
    private final Button hex1;
    private final Button hex2;
    private final EditText mask;
    private final View maskRow;
    private final TextView maskView;
    final MemoryRange memoryRange;
    private final TextView message;
    private final Button minGroupSize;
    private final CheckBox modeHacking;
    private final View modeHackingRow;
    private final EditText number;
    private final View numberСonverter;
    private final EditText offset;
    private final CheckBox offsetHex;
    private final View offsetRow;
    private final CheckBox ordered;
    private int prevFlags;
    private boolean prevKbd;
    private char prevStr;
    public static final int[] ranges;
    private final SystemSpinner signSpinner;
    private int type;
    private final TextView typeHint;
    private final int[][] typeMatrix;
    private final View[] typeMatrixHeader;
    private final View typeRow;
    private final SystemSpinnerType typeSpinner;
    private final TextView value;
    private final View valueRow;
    private final View view;

    static {
        Searcher.ranges = new int[5];
        Searcher.fuzzyExtended = false;
    }

    public Searcher(int type, int flags) {
        this.focusValue = true;
        this.prevKbd = false;
        this.prevStr = '\u0000';
        this.prevFlags = 0;
        this.type = type;
        this.flags = flags;
        this.view = LayoutInflater.inflateStatic(0x7F040025, null);  // layout:service_searcher
        this.message = (TextView)this.view.findViewById(0x7F0B000E);  // id:message
        this.signSpinner = (SystemSpinner)this.view.findViewById(0x7F0B012D);  // id:sign_spinner
        this.signSpinner.setOnItemSelectedListener(this);
        this.typeHint = (TextView)this.view.findViewById(0x7F0B00F2);  // id:type_hint
        this.number = (EditText)this.view.findViewById(0x7F0B004D);  // id:number
        this.number.setDataType(1);
        this.numberСonverter = this.view.findViewById(0x7F0B0042);  // id:number_converter
        this.valueRow = this.view.findViewById(0x7F0B00F1);  // id:value_row
        this.maskRow = this.view.findViewById(0x7F0B012E);  // id:edit_mask_row
        this.mask = (EditText)this.view.findViewById(0x7F0B012F);  // id:edit_mask
        this.maskView = (TextView)this.view.findViewById(0x7F0B0130);  // id:mask_view
        android.ext.Searcher.1 maskWatcher = new TextWatcher() {
            @Override  // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                Searcher.this.updateMaskView();
            }

            @Override  // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override  // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Searcher.this.updateMaskView();
            }
        };
        this.mask.addTextChangedListener(maskWatcher);
        this.offsetRow = this.view.findViewById(0x7F0B0131);  // id:offset_row
        this.offset = (EditText)this.view.findViewById(0x7F0B012A);  // id:offset
        this.offsetHex = (CheckBox)this.view.findViewById(0x7F0B0066);  // id:hex
        this.typeRow = this.view.findViewById(0x7F0B0133);  // id:type_row
        this.typeSpinner = (SystemSpinnerType)this.view.findViewById(0x7F0B0067);  // id:type
        this.numberСonverter.setTag(new Object[]{this.number, this.typeSpinner});
        this.typeSpinner.setOnItemSelectedListener(this);
        this.modeHackingRow = this.view.findViewById(0x7F0B0134);  // id:mode_hacking_row
        this.modeHacking = (CheckBox)this.view.findViewById(0x7F0B0135);  // id:mode_hacking
        this.modeHacking.setOnCheckedChangeListener(this);
        this.ordered = (CheckBox)this.view.findViewById(0x7F0B0136);  // id:ordered
        this.ordered.setOnCheckedChangeListener(this);
        this.hex1 = (Button)this.view.findViewById(0x7F0B0138);  // id:hex1
        this.hex1.setOnClickListener(this);
        this.hex2 = (Button)this.view.findViewById(0x7F0B0139);  // id:hex2
        this.hex2.setOnClickListener(this);
        this.minGroupSize = (Button)this.view.findViewById(0x7F0B0137);  // id:min_group_size
        this.minGroupSize.setTag(2);
        this.minGroupSize.setOnClickListener(this);
        this.fuzzyRow = this.view.findViewById(0x7F0B013A);  // id:fuzzy_row
        this.memoryRange = (MemoryRange)this.view.findViewById(0x7F0B0140);  // id:memory_range
        this.memoryRange.init();
        this.extBtn = (Button)this.view.findViewById(0x7F0B0019);  // id:ext_btn
        this.cancel = (Button)this.view.findViewById(0x7F0B0141);  // id:cancel
        for(Object object0: this.view.getFocusables(2)) {
            View v = (View)object0;
            if(v instanceof EditText) {
                Tools.setOnFocusChangeListener(v, this);
            }
        }
        this.memoryRange.setType(Searcher.ranges[type]);
        this.memoryRange.setTypeChangeListener(this);
        this.typeMatrixHeader = new View[]{this.signSpinner, this.valueRow, this.typeRow, this.modeHackingRow, this.fuzzyRow, this.extBtn, this.cancel, this.maskRow, this.typeHint, this.numberСonverter, this.offsetRow};
        int[][] arr2_v = new int[5][];
        arr2_v[0] = new int[]{1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0};
        int[] arr_v = new int[11];
        arr_v[2] = 1;
        arr_v[8] = 1;
        arr_v[9] = 1;
        arr2_v[1] = arr_v;
        arr2_v[2] = new int[]{0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0};
        int[] arr_v1 = new int[11];
        arr_v1[0] = 1;
        arr_v1[1] = 1;
        arr_v1[2] = 1;
        arr_v1[7] = 1;
        arr2_v[3] = arr_v1;
        int[] arr_v2 = new int[11];
        arr_v2[1] = 1;
        arr_v2[2] = 1;
        arr_v2[10] = 1;
        arr2_v[4] = arr_v2;
        this.typeMatrix = arr2_v;
        if(type < 0 || type >= this.typeMatrix.length) {
            throw new IllegalArgumentException("Type must be between: 0 and " + (this.typeMatrix.length - 1) + " but got: " + type);
        }
        this.setVisibility(type);
        this.examplesList = Re.s(0x7F070027);  // string:examples_search "\n__help_exact_search_title__:\n\t-340\n\n__help_xor_search_title__:\n\t-340__mode_xor__16\n\n__help_range_search_title__: 
                                               // \n\t298__tilde__764\n\n__help_xor_search_title__ + __help_range_search_title__: 
                                               // \n\t298__tilde__764__mode_xor__32\n\n__help_group_search_title__: \n\t895__semicolon__438__semicolon__-83__colon__300\n\t895D__semicolon__438F__semicolon__-83B__colon__300\n\t800__tilde__900__semicolon__438__semicolon__-90__tilde__-60__colon__300\n\t800D__tilde__900D__semicolon__438E__semicolon__-90B__tilde__-60B__colon__300\n\t800__tilde____tilde__900__semicolon__438__semicolon__-90__tilde__-60__colon__300\n\t800D__tilde__900D__semicolon__438E__semicolon__-90B__tilde____tilde__-60B__colon__300\n\n__ordered_group_search__: 
                                               // \n\t895__semicolon__438__semicolon__-83__colon____colon__300\n\t895D__semicolon__438F__semicolon__-83B__colon____colon__300\n\t800__tilde__900__semicolon__438__semicolon__-90__tilde__-60__colon____colon__300\n\t800D__tilde__900D__semicolon__438E__semicolon__-90B__tilde__-60B__colon____colon__300\n\t800__tilde__900__semicolon__438__semicolon__-90__tilde____tilde__-60__colon____colon__300\n\t800D__tilde____tilde__900D__semicolon__438E__semicolon__-90B__tilde__-60B__colon____colon__300\n\t123__semicolon____any____semicolon__456__colon____colon__9\n\n\t\t__any__ 
                                               // = __any_value__\n\n__format_hex__: \n\tBAFE56DE__hex__\n\n__format_rhex__: \n\tDE56FEBA__rhex__\n__text_examples__\n\n__rtfm__\n 
                                               //    "
        this.value = (TextView)this.view.findViewById(0x7F0B001B);  // id:value
        if(type == 2) {
            this.value.setText(0x7F070114);  // string:difference "Difference"
            ((TextView)this.view.findViewById(0x7F0B013B)).setText(Re.s("N - __new_value__; O - __old_value__; D - __difference__;"));  // id:fuzzy_text
            Tools.setButtonHelpBackground(this.value);
            this.value.setOnClickListener(this);
            this.examplesList = Re.s(0x7F070040);  // string:diff_examples "\n123\n123__tilde__456\n123__tilde____tilde__456\n\n__rtfm__\n 
                                                   //    "
            this.setFuzzyExtended(Searcher.fuzzyExtended);
        }
        else if(type == 0) {
            Tools.setButtonHelpBackground(this.value);
            this.value.setOnClickListener(this);
        }
        else if(type == 3) {
            Tools.setButtonHelpBackground(this.value);
            this.value.setOnClickListener(this);
            this.value.setText(0x7F07008E);  // string:address "Address:"
            this.examplesList = Re.s(0x7F070038);  // string:mask_examples "\n__address__ = A20\n__mask__ FFFFFFFF\n\n__address__ != B20\n__mask__ 
                                                   // FF0\n\n__address__ = 0B?0\n__mask__ FFF\n\n__address__ != ??F??\n__mask__ BA0\n\n__rtfm__\n 
                                                   //    "
            this.number.addTextChangedListener(maskWatcher);
            this.updateMaskView();
        }
        else if(type == 4) {
            this.value.setText(0x7F07008E);  // string:address "Address:"
            TextView offsetText = (TextView)this.view.findViewById(0x7F0B0132);  // id:offset_text
            Tools.setButtonHelpBackground(offsetText);
            offsetText.setOnClickListener(this);
        }
        this.extBtn.setOnClickListener(this);
        String valueText = Tools.stripColon(this.value.getText().toString());
        TextView textView1 = this.value;
        if(this.signSpinner.getVisibility() != 0) {
            valueText = String.valueOf(valueText) + ':';
        }
        textView1.setText(valueText);
        this.init();
    }

    @Override  // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    @Override  // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public long getDirectMask() {
        return ParserNumbers.parseBigLong(this.getMask(), 16);
    }

    private static String getMask(String input) {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < input.length(); ++i) {
            out.append((input.charAt(i) == 0x3F ? "0" : "F"));
        }
        return out.toString();
    }

    public String getMask() {
        String s = this.mask.getText().toString().trim();
        History.add(s, 1);
        return s;
    }

    public long getMem(int index) throws NumberFormatException {
        return this.memoryRange.getMem(index);
    }

    public String getNumber() {
        if(this.type == 2 && !Searcher.fuzzyExtended) {
            return "0";
        }
        String s = this.number.getText().toString().trim();
        History.add(s, 1);
        return s;
    }

    public EditText getNumberEdit() {
        return this.number;
    }

    public String getOffset() {
        String s = this.offset.getText().toString().trim();
        History.add(s, 1);
        return s;
    }

    public int getSign() {
        return this.signSpinner.getSelected();
    }

    public int getType() {
        if(this.type == 0) {
            String s = this.number.getText().toString();
            if(ParserNumbers.isString(s)) {
                return s.charAt(0) == 59 ? 2 : 1;
            }
            int v = ParserNumbers.typeAsm(s);
            return v == 0 ? this.typeSpinner.getSelected() : v;
        }
        return this.typeSpinner.getSelected();
    }

    public View getView() {
        return this.view;
    }

    public View getViewForAttach() {
        return InternalKeyboard.getView(this.getViewForAttachSimple());
    }

    public View getViewForAttachSimple() {
        return Tools.getViewForAttach(this.getView());
    }

    private void init() {
        this.setHint(this.prevStr, this.flags);
        this.signSpinner.setData((this.type == 3 ? AddressItem.getSignNamesSmall() : AddressItem.getSignNames()));
        this.setType(AddressItem.getDataForSearch(this.flags, true), this.flags);
        this.number.addTextChangedListener(this);
    }

    private boolean isGroupSearch() {
        if(this.type != 0) {
            return false;
        }
        String s = this.number.getText().toString().trim();
        return s.length() >= 1 && s.charAt(0) != 61 && s.indexOf(59) > 0;
    }

    public boolean isModeHacking() {
        return this.modeHacking.isChecked();
    }

    public boolean isOffsetHex() {
        return this.offsetHex.isChecked();
    }

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case 0x7F0B0135: {  // id:mode_hacking
                if(isChecked && this.getSign() != 0x20000000) {
                    this.setSign(0x20000000);
                    return;
                }
                break;
            }
            case 0x7F0B0136: {  // id:ordered
                if(this.type == 0) {
                    String s = this.number.getText().toString();
                    if(ParserNumbers.isString(s)) {
                        int v = ParserNumbers.invertPrefix(s);
                        if((v == 39 || v == 58) != isChecked) {
                            if(v == 34 || v == 39) {
                                String s1 = String.valueOf(((char)v));
                                Editable editable0 = this.number.getText();
                                int v2 = s.length();
                                for(int i = 1; i < v2; ++i) {
                                    if(s.charAt(i) == (isChecked ? 39 : 34)) {
                                        editable0.replace(i, i + 1, s1);
                                    }
                                }
                                return;
                            }
                            this.number.getText().replace(0, 1, String.valueOf(((char)v)));
                            return;
                        }
                    }
                    else {
                        goto label_23;
                    }
                }
                else {
                label_23:
                    if(this.isGroupSearch()) {
                        String s2 = this.number.getText().toString();
                        if(s2.contains("::") != isChecked) {
                            int st = s2.indexOf((isChecked ? ":" : "::"));
                            int en = st + (isChecked ? ":" : "::").length();
                            if(st == -1) {
                                st = s2.length();
                                en = st;
                            }
                            this.number.getText().replace(st, en, (isChecked ? "::" : ":"));
                            return;
                        }
                    }
                }
                break;
            }
        }
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if(which <= -100) {
            String prefix = null;
            int type = 0;
            switch(which) {
                case -700: {
                    prefix = "~A8 ";
                    type = 4;
                    break;
                }
                case -600: {
                    prefix = "~T ";
                    type = 2;
                    break;
                }
                case -500: {
                    prefix = "~A ";
                    type = 4;
                    break;
                }
                case -400: {
                    prefix = "Q";
                    type = 1;
                    break;
                }
                case -300: {
                    prefix = "h";
                    type = 1;
                    break;
                }
                case -200: {
                    prefix = ";";
                    type = 2;
                    break;
                }
                case -100: {
                    type = 1;
                    prefix = ":";
                }
            }
            if(prefix != null) {
                this.number.getText().insert(0, prefix);
                this.setType(type);
            }
        }
        this.updateHint();
        if(this.isModeHacking() && this.getSign() != 0x20000000) {
            this.modeHacking.setChecked(false);
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        Editable editable0;
        if(v != null) {
            switch(v.getId()) {
                case 0x7F0B0019: {  // id:ext_btn
                    if(this.type == 2) {
                        this.setFuzzyExtended(!Searcher.fuzzyExtended);
                    }
                    if(this.number.getVisibility() == 0 && this.valueRow.getVisibility() == 0) {
                        this.number.requestFocus();
                        return;
                    }
                    break;
                }
                case 0x7F0B001B: {  // id:value
                    Searcher.showExamples(this.examplesList);
                    return;
                }
                case 0x7F0B0132: {  // id:offset_text
                    Alert.show(Alert.create().setMessage(Re.s(0x7F07025D)).setNegativeButton(Re.s(0x7F07009D), null));  // string:offset_help "The maximum limit for a possible offset."
                    return;
                }
                case 0x7F0B0137:   // id:min_group_size
                case 0x7F0B0138:   // id:hex1
                case 0x7F0B0139: {  // id:hex2
                    if(this.isGroupSearch()) {
                        Object object0 = v.getTag();
                        if(object0 instanceof Integer) {
                            int v = (int)(((Integer)object0));
                            String s = this.number.getText().toString();
                            String split = s.contains("::") ? "::" : ":";
                            int st = s.indexOf(split);
                            int v2 = s.length();
                            if(st == -1) {
                                st = v2;
                            }
                            this.number.getText().replace(st, v2, String.valueOf(split) + v);
                            return;
                        }
                    }
                    else {
                        editable0 = this.number.getText();
                        try {
                            if(editable0.length() > 0) {
                                int v3 = v.getId();
                                int v4 = editable0.charAt(0);
                                boolean hex = v4 == 72 || v4 == 104;
                                if(v3 == 0x7F0B0137 && !hex) {  // id:min_group_size
                                    ParserNumbers.getBytes(editable0);
                                    editable0.replace(0, 1, "h");
                                    return;
                                }
                                if(v3 == 0x7F0B0138 && v4 == 58) {  // id:hex1
                                    editable0.replace(0, editable0.length(), "Q \'" + editable0.toString().substring(1).replace("\'", "\' 27 \'") + '\'');
                                    return;
                                }
                                if(v3 == 0x7F0B0138 && v4 == 59) {  // id:hex1
                                    editable0.replace(0, editable0.length(), "Q \"" + editable0.toString().substring(1).replace("\"", "\" 22 \"") + '\"');
                                    return;
                                }
                                if(hex) {
                                    byte[] arr_b = ParserNumbers.getBytes(editable0.toString());
                                    HexText.getText(null, 0, arr_b, arr_b.length, v3 == 0x7F0B0137 || v3 == 0x7F0B0139, v3 == 0x7F0B0138 || v3 == 0x7F0B0139, editable0);  // id:min_group_size
                                    editable0.replace(0, 1, "Q");
                                    return;
                                }
                                int str = v3 == 0x7F0B0138 ? 34 : 39;  // id:hex1
                                int start = 0;
                                for(int i = 1; i < editable0.length(); ++i) {
                                    if(editable0.charAt(i) == str) {
                                        if(start == 0) {
                                            start = i + 1;
                                        }
                                        else {
                                            byte[] arr_b1 = editable0.toString().substring(start, i).getBytes(ParserNumbers.getCharset(str == 34));
                                            String s2 = InOut.bytesToHex(arr_b1, 0, arr_b1.length).trim();
                                            editable0.replace(start - 1, i + 1, s2);
                                            i = start + s2.length() - 2;
                                            start = 0;
                                        }
                                    }
                                }
                            }
                        }
                        catch(Throwable e) {
                            Log.d(("Failed convert \'" + editable0 + "\'"), e);
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }

    @Override  // android.view.View$OnFocusChangeListener
    public void onFocusChange(View v, boolean hasFocus) {
        boolean z1 = !hasFocus && (this.type == 1 || this.type == 2 && !Searcher.fuzzyExtended);
        if((Config.configClient & 1) != 0) {
            InternalKeyboard kbdView = (InternalKeyboard)v.getRootView().findViewById(0x7F0B002A);  // id:keyboard
            if(kbdView != null) {
                if(this.type == 0 && ParserNumbers.needExtKbd(this.number.getText().toString())) {
                    boolean z2 = this.number.isFocused();
                    InternalKeyboard.needExternalKbd(this.number, z2);
                    return;
                }
                kbdView.setHideKeyboard(z1);
                Tools.hideKeyboard(v);
            }
        }
        else if(z1) {
            Tools.hideKeyboard(this.view);
        }
    }

    public void onShow() {
        EditText focus;
        switch(this.type) {
            case 1: {
                focus = null;
                break;
            }
            case 2: {
                focus = Searcher.fuzzyExtended ? this.number : null;
                break;
            }
            case 3: {
                focus = this.focusValue ? this.number : this.mask;
                break;
            }
            case 4: {
                focus = this.focusValue ? this.number : this.offset;
                break;
            }
            default: {
                focus = this.number;
            }
        }
        if(focus != null) {
            focus.requestFocus();
            Tools.selectAll(focus);
            return;
        }
        this.onFocusChange(this.view, false);
    }

    @Override  // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.updateGroupSearchHelpers(start == 0 && before == 1 && count == 0);
    }

    public static long[] parseMask(String rawAddr, long directMask) throws NumberFormatException {
        long v1 = ParserNumbers.parseBigLong(rawAddr.replace("?", "0"), 16);
        long mask = ParserNumbers.parseBigLong(Searcher.getMask(rawAddr), 16) & directMask;
        return new long[]{v1 & mask, mask};
    }

    public long[] parseMask() throws NumberFormatException {
        return Searcher.parseMask(this.getNumber(), this.getDirectMask());
    }

    @Override
    public void run() {
        Searcher.ranges[this.type] = this.memoryRange.getType();
    }

    public void setExamplesList(String examplesList) {
        this.examplesList = examplesList;
    }

    public void setFocusValue(boolean focus) {
        this.focusValue = focus;
    }

    private void setFuzzyExtended(boolean value) {
        int v = 0;
        Searcher.fuzzyExtended = value;
        String s = value ? Re.s(0x7F070157) : Re.s(0x7F070156);  // string:less "Less"
        this.extBtn.setText(s);
        this.valueRow.setVisibility((value ? 0 : 8));
        this.typeRow.setVisibility((value ? 0 : 8));
        View view0 = this.view.findViewById(0x7F0B013B);  // id:fuzzy_text
        if(!value) {
            v = 8;
        }
        view0.setVisibility(v);
        ((TextView)this.view.findViewById(0x7F0B013C)).setText((value ? "N = O+D" : Re.s(0x7F070093)));  // id:equal_btn
        ((TextView)this.view.findViewById(0x7F0B013D)).setText((value ? "N ≠ O+D" : Re.s(0x7F070094)));  // id:not_equal_btn
        ((TextView)this.view.findViewById(0x7F0B013E)).setText((value ? "N > O+D" : Re.s(0x7F070095)));  // id:larger_btn
        ((TextView)this.view.findViewById(0x7F0B013F)).setText((value ? "N < O+D" : Re.s(0x7F070096)));  // id:smaller_btn
    }

    private void setHint(char str, int flags) {
        if(this.prevFlags != flags || this.prevStr != str) {
            String s = this.type != 0 || str == 0 ? AddressItem.getLimits(flags) : Tools.stripColon(0x7F07018F) + ": " + ParserNumbers.example(this.number.getText().toString());  // string:examples "Examples"
            this.typeHint.setText(s);
            this.prevStr = str;
            this.prevFlags = flags;
        }
    }

    public void setMask(String number) {
        this.mask.setText(number.trim());
    }

    public void setMessage(CharSequence message) {
        this.message.setText(message);
    }

    public void setNumber(String number) {
        this.number.setText(number.trim());
    }

    public void setOffset(String number) {
        this.offset.setText(number.trim());
    }

    public void setOffsetHex(boolean checked) {
        this.offsetHex.setChecked(checked);
    }

    public void setOnCancelClickListener(View.OnClickListener l) {
        this.cancel.setOnClickListener(l);
    }

    public void setSign(int sign) {
        this.signSpinner.setSelected(sign);
        if(this.isModeHacking() && sign != 0x20000000) {
            this.modeHacking.setChecked(false);
        }
    }

    public void setType(int type) {
        this.typeSpinner.setSelected(type);
        this.updateHint();
    }

    public void setType(SparseArray sparseArray0, int type) {
        if(this.type == 0) {
            String s = Re.s(0x7F0702B9);  // string:text "Text"
            sparseArray0.put(-100, s + " UTF-8");
            sparseArray0.put(-200, s + " UTF-16LE");
            sparseArray0.put(-300, "HEX");
            sparseArray0.put(-400, "HEX + UTF-8 + UTF-16LE");
            sparseArray0.put(-500, "ARM (x32)");
            sparseArray0.put(-600, "Thumb");
        }
        this.typeSpinner.setData(sparseArray0);
        this.setType(type);
    }

    SparseArray setTypeInternal(int flags) {
        SparseArray sparseArray0 = AddressItem.getDataForSearch(flags, true);
        int lastType = MainService.getLastType();
        if(lastType != 0 && sparseArray0.get(lastType) == null) {
            lastType = flags;
        }
        this.setType(sparseArray0, lastType);
        return sparseArray0;
    }

    private void setVisibility(int type) {
        for(int i = 0; i < this.typeMatrix[type].length; ++i) {
            this.typeMatrixHeader[i].setVisibility((this.typeMatrix[type][i] == 1 ? 0 : 8));
        }
    }

    public static void showExamples(String examplesList) {
        Alert.show(Alert.create().setMessage(Re.s(0x7F07018F) + ":\n" + examplesList).setPositiveButton(Re.s(0x7F07012B), ConfigListAdapter.getShowHelpListener()).setNegativeButton(Re.s(0x7F0700A1), null));  // string:examples "Examples"
    }

    private void updateGroupSearchHelpers(boolean skip) {
        if(this.type == 0) {
            String s = this.number.getText().toString();
            boolean z1 = ParserNumbers.isString(s);
            boolean isAsm = ParserNumbers.typeAsm(s) != 0;
            boolean isGroup = !z1 && this.isGroupSearch();
            boolean z4 = ParserNumbers.needExtKbd(s);
            int strExt = !z1 || !z4 ? 0 : 1;
            if((this.ordered.getTag() == null ? 0 : 1) != strExt) {
                String s1 = strExt == 0 ? Re.s(0x7F07019B) : "UTF-16LE";  // string:ordered "Ordered"
                this.ordered.setText(s1);
                this.ordered.setTag((strExt == 0 ? null : this.ordered));
            }
            if((Config.configClient & 1) != 0) {
                boolean curr = z4 && this.number.isFocused();
                if(this.prevKbd != curr && (!curr || !skip)) {
                    this.prevKbd = curr;
                    InternalKeyboard.needExternalKbd(this.number, curr);
                }
            }
            this.setHint(((char)(z1 ? s.charAt(0) : 0)), this.typeSpinner.getSelected());
            this.typeRow.setVisibility((!z1 && !isAsm ? 0 : 8));
            this.signSpinner.setVisibility((!z1 && !isGroup ? 0 : 8));
            this.modeHacking.setVisibility((!z1 && !isAsm && !isGroup ? 0 : 8));
            this.ordered.setVisibility((strExt == 0 && !isGroup ? 8 : 0));
            this.minGroupSize.setVisibility((!z1 && !isGroup ? 8 : 0));
            this.hex1.setVisibility((z1 ? 0 : 8));
            this.hex2.setVisibility(8);
            if(z1 || isGroup) {
                if(this.modeHacking.isChecked()) {
                    this.modeHacking.setChecked(false);
                }
                if(this.getSign() != 0x20000000) {
                    this.setSign(0x20000000);
                }
                if(isGroup) {
                    String text = s.trim();
                    boolean z6 = text.contains("::");
                    int type = this.getType();
                    if(type == 0) {
                        type = 4;
                    }
                    int v2 = SearchButtonListener.getMinGroupSize(text, type, z6);
                    this.ordered.setChecked(z6);
                    this.minGroupSize.setTag(v2);
                    this.minGroupSize.setText(String.valueOf((z6 ? "::" : ":")) + v2);
                    return;
                }
                this.ordered.setChecked(ParserNumbers.invertPrefix(s) == 39 || ParserNumbers.invertPrefix(s) == 58);
                int v3 = s.charAt(0);
                boolean hex = v3 == 72 || v3 == 104;
                this.minGroupSize.setText((hex ? "HEX+U8" : "HEX"));
                this.hex1.setText((!hex && v3 != 59 ? "HEX+U8" : "HEX+U16"));
                if(hex || (v3 == 81 || v3 == 0x71)) {
                    this.hex2.setVisibility(0);
                }
                this.hex2.setText((hex ? "HEX+U8+U16" : "HEX+U16"));
            }
        }
    }

    private void updateHint() {
        this.setHint(this.prevStr, this.typeSpinner.getSelected());
        this.updateGroupSearchHelpers(false);
    }

    void updateMaskView() {
        String mask;
        if(this.type == 3) {
            try {
                mask = "???";
                mask = Long.toHexString(this.parseMask()[1]).toUpperCase(Locale.US);
            }
            catch(Throwable unused_ex) {
            }
            String s1 = Tools.stripColon(0x7F070204) + ": " + mask;  // string:resultant_mask "Resultant mask:"
            this.maskView.setText(s1);
        }
    }
}

