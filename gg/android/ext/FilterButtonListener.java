package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.fix.SparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class FilterButtonListener extends MenuItem {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnShowListener, View.OnClickListener, View.OnLongClickListener {
        private CheckBox cAddrGreat;
        private CheckBox cAddrLess;
        private CheckBox cFractional;
        private CheckBox cPointer;
        private CheckBox cType;
        private CheckBox cValGreat;
        private CheckBox cValLess;
        private AlertDialog dialog;
        private EditText eAddrGreat;
        private EditText eAddrLess;
        private EditText eFractional;
        private EditText eMaxShow;
        private EditText eValGreat;
        private EditText eValLess;
        private SharedPreferences preferences;
        private SystemSpinner sFractional;
        private SystemSpinner sPointer;
        private SystemSpinner sType;
        private final int[] showMasks;
        private final CheckBox[] values;
        private View view;

        private Impl() {
            this.showMasks = new int[]{1, 2, 4, 8, 16, 0x20, 0x40};
            this.values = new CheckBox[this.showMasks.length];
        }

        Impl(Impl filterButtonListener$Impl0) {
        }

        private void addWatcher(View view, CheckBox check) {
            if(view instanceof EditText) {
                ((EditText)view).addTextChangedListener(new TextWatcher() {
                    @Override  // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        check.setChecked(true);
                    }

                    @Override  // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override  // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                return;
            }
            if(view instanceof SystemSpinner) {
                ((SystemSpinner)view).setOnItemSelectedListener(new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        check.setChecked(true);
                    }
                });
            }
        }

        private void doView(SPEditor editor, View view) {
            int defValue = 0;
            if(view != null) {
                if(view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)view;
                    int v1 = viewGroup.getChildCount();
                    int i = 0;
                    while(i < v1) {
                        try {
                            View view1 = viewGroup.getChildAt(i);
                            if(view1 != null) {
                                this.doView(editor, view1);
                            }
                            ++i;
                            continue;
                        }
                        catch(ArrayIndexOutOfBoundsException unused_ex) {
                        }
                        ++i;
                    }
                }
                else {
                    int v3 = view.getId();
                    if(v3 != -1) {
                        if(view instanceof EditText) {
                            if(editor == null) {
                                ((EditText)view).setText(this.preferences.getString(Re.getName(v3), (v3 == 0x7F0B010B ? "100" : "")));  // id:eMaxShow
                                ((EditText)view).setDataType(1);
                                return;
                            }
                            String s = ((EditText)view).getText().toString().trim();
                            History.add(s, 1);
                            editor.putString(Re.getName(v3), s, "");
                            return;
                        }
                        if(view instanceof SystemSpinner) {
                            if(v3 == 0x7F0B0117) {  // id:sFractional
                                defValue = 0x20000000;
                            }
                            if(editor == null) {
                                ((SystemSpinner)view).setSelected(this.preferences.getInt(Re.getName(v3), defValue));
                                return;
                            }
                            editor.putInt(Re.getName(v3), ((SystemSpinner)view).getSelected(), defValue);
                            return;
                        }
                        if(view instanceof CheckBox) {
                            if(editor == null) {
                                ((CheckBox)view).setChecked(FilterButtonListener.checked.get(v3, false));
                                return;
                            }
                            boolean z = ((CheckBox)view).isChecked();
                            FilterButtonListener.checked.put(v3, z);
                        }
                    }
                }
            }
        }

        private void loadResources() {
            if(this.view != null) {
                return;
            }
            this.view = LayoutInflater.inflateStatic(0x7F04001F, null);  // layout:service_filter_search
            this.preferences = Tools.getSharedPreferences();
            this.eMaxShow = (EditText)this.view.findViewById(0x7F0B010B);  // id:eMaxShow
            this.cAddrGreat = (CheckBox)this.view.findViewById(0x7F0B010C);  // id:cAddrGreat
            this.eAddrGreat = (EditText)this.view.findViewById(0x7F0B010D);  // id:eAddrGreat
            this.cAddrLess = (CheckBox)this.view.findViewById(0x7F0B010E);  // id:cAddrLess
            this.eAddrLess = (EditText)this.view.findViewById(0x7F0B010F);  // id:eAddrLess
            this.cValGreat = (CheckBox)this.view.findViewById(0x7F0B0110);  // id:cValGreat
            this.eValGreat = (EditText)this.view.findViewById(0x7F0B0111);  // id:eValGreat
            this.cValLess = (CheckBox)this.view.findViewById(0x7F0B0112);  // id:cValLess
            this.eValLess = (EditText)this.view.findViewById(0x7F0B0113);  // id:eValLess
            this.cType = (CheckBox)this.view.findViewById(0x7F0B0114);  // id:cType
            this.sType = (SystemSpinner)this.view.findViewById(0x7F0B0115);  // id:sType
            this.cFractional = (CheckBox)this.view.findViewById(0x7F0B0116);  // id:cFractional
            this.sFractional = (SystemSpinner)this.view.findViewById(0x7F0B0117);  // id:sFractional
            this.eFractional = (EditText)this.view.findViewById(0x7F0B0118);  // id:eFractional
            this.cPointer = (CheckBox)this.view.findViewById(0x7F0B0119);  // id:cPointer
            this.sPointer = (SystemSpinner)this.view.findViewById(0x7F0B011A);  // id:sPointer
            CharSequence[] arr_charSequence = MemoryContentAdapter.getNames();
            int[] arr_v = MemoryContentAdapter.getColors();
            for(int i = 0; i < this.showMasks.length; ++i) {
                CheckBox ch = (CheckBox)this.view.findViewById(new int[]{0x7F0B006B, 0x7F0B006C, 0x7F0B006D, 0x7F0B006E, 0x7F0B006F, 0x7F0B0070, 0x7F0B0071}[i]);  // id:value_hex
                ch.setTextColor(arr_v[i]);
                ch.setText(arr_charSequence[i]);
                ch.setChecked((AddressArrayAdapter.showMask & this.showMasks[i]) != 0);
                ch.setId(-1);
                this.values[i] = ch;
            }
            this.cAddrGreat.setText(Tools.stripColon(0x7F07008E) + " ≥");  // string:address "Address:"
            this.cAddrLess.setText(Tools.stripColon(0x7F07008E) + " ≤");  // string:address "Address:"
            this.cValGreat.setText(Tools.stripColon(0x7F070086) + " ≥");  // string:number "Number:"
            this.cValLess.setText(Tools.stripColon(0x7F070086) + " ≤");  // string:number "Number:"
            this.sType.setData(AddressItem.getDataForEditAll(-1));
            Tools.setButtonHelpBackground(this.cFractional);
            this.cFractional.setOnLongClickListener(this);
            this.sFractional.setData(AddressItem.getSignNamesSmall());
            SparseArray data = new SparseArray();
            data.put(4, Tools.colorize(("-: " + Re.s(0x7F07009C)), Tools.getColor(0x7F0A000C)));  // string:no "No"
            data.put(8, Tools.colorize(("R: " + Re.s(0x7F070244)), Tools.getColor(0x7F0A0010)));  // string:read_only "read-only"
            data.put(16, Tools.colorize(("W: " + Re.s(0x7F070243)), Tools.getColor(0x7F0A000F)));  // string:writable "writable"
            data.put(2, Tools.colorize(("X: " + Re.s(0x7F070242)), Tools.getColor(0x7F0A000E)));  // string:executable "executable"
            data.put(1, Tools.colorize(("WX: " + Re.s(0x7F070245)), Tools.getColor(0x7F0A000D)));  // string:writable_and_executable "writable and executable"
            this.sPointer.setData(data);
            this.doView(null, this.view);
            this.addWatcher(this.eAddrGreat, this.cAddrGreat);
            this.addWatcher(this.eAddrLess, this.cAddrLess);
            this.addWatcher(this.eValGreat, this.cValGreat);
            this.addWatcher(this.eValLess, this.cValLess);
            this.addWatcher(this.sType, this.cType);
            this.addWatcher(this.sFractional, this.cFractional);
            this.addWatcher(this.eFractional, this.cFractional);
            this.addWatcher(this.sPointer, this.cPointer);
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            this.onClick(((AlertDialog)dialog).getButton(which));
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            if(v == null || v.getTag() instanceof MenuItem) {
                this.loadResources();
                AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(Tools.getViewForAttach(this.view))).setPositiveButton(Re.s(0x7F0700AD), this).setNeutralButton(Re.s(0x7F07023F), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:apply "Apply"
                this.dialog = alertDialog0;
                Alert.setOnShowListener(alertDialog0, this);
                Alert.show(alertDialog0, this.eMaxShow);
                return;
            }
            if(v.getTag() instanceof Boolean) {
                this.cAddrGreat.setChecked(false);
                this.cAddrLess.setChecked(false);
                this.cValGreat.setChecked(false);
                this.cValLess.setChecked(false);
                this.cType.setChecked(false);
                this.cFractional.setChecked(false);
                this.cPointer.setChecked(false);
                for(int i = 0; i < this.showMasks.length; ++i) {
                    this.values[i].setChecked(false);
                }
            }
            try {
                int v1 = Converter.parseStringToInt(this.eMaxShow.getText().toString().trim());
                if(v1 < 1) {
                    throw new NumberFormatException(Tools.stringFormat(Re.s(0x7F0700D1), new Object[]{Re.s(0x7F070092), 1}));  // string:number_less "Number \'__s__\' is less than minimum value (__d__) for this 
                                                                                                                               // type."
                }
                if(v1 > 100000) {
                    throw new NumberFormatException(Tools.stringFormat(Re.s(0x7F0700D2), new Object[]{Re.s(0x7F070092), 100000}));  // string:number_greater "Number \'__s__\' is greater than maximum value (__d__) for 
                                                                                                                                    // this type."
                }
                Converter.setFilters((this.cAddrGreat.isChecked() ? this.eAddrGreat.getText().toString() : null), (this.cAddrLess.isChecked() ? this.eAddrLess.getText().toString() : null), (this.cValGreat.isChecked() ? this.eValGreat.getText().toString() : null), (this.cValLess.isChecked() ? this.eValLess.getText().toString() : null), (this.cType.isChecked() ? this.sType.getSelected() : 0), (this.cFractional.isChecked() ? (this.sFractional.getSelected() == 0x10000000 ? "!" : "") + this.eFractional.getText().toString() : null), (this.cPointer.isChecked() ? this.sPointer.getSelected() : 0));
                Converter.setShowCount(v1);
                int mask = 0;
                for(int i = 0; true; ++i) {
                    if(i >= this.showMasks.length) {
                        AddressArrayAdapter.setShowMask(mask);
                        SPEditor sPEditor0 = new SPEditor(this.preferences.edit());
                        this.doView(sPEditor0, this.view);
                        sPEditor0.commit();
                        Tools.dismiss(this.dialog);
                        MainService.instance.refreshResultList(false);
                        return;
                    }
                    if(this.values[i].isChecked()) {
                        mask |= this.showMasks[i];
                    }
                }
            }
            catch(NumberFormatException ex) {
                Log.w("Error", ex);
                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(ex.getMessage()).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
            }
        }

        @Override  // android.view.View$OnLongClickListener
        public boolean onLongClick(View v) {
            if(v != null && v.getId() == 0x7F0B0116) {  // id:cFractional
                Alert.show(Alert.create().setMessage(Re.s(0x7F07004D)).setNegativeButton(Re.s(0x7F07009D), null));  // string:fractional_hint_ "__fractional_hint__\nx = 123__decimal__456; {x} = 0__decimal__456\nx 
                                                                                                                    // = -123__decimal__456; {x} = -0__decimal__456"
                return true;
            }
            return false;
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this, this.eMaxShow);
            Tools.setButtonListener(dialog, -3, Boolean.TRUE, this);
        }
    }

    static final SparseBooleanArray checked;

    static {
        FilterButtonListener.checked = new SparseBooleanArray();
    }

    public FilterButtonListener(MainService service) {
        super(0x7F070090, 0x7F020024);  // string:filter "Filter"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }
}

