package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.fix.TextView;
import android.sup.LongSparseArrayChecked;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AddressEditor extends MenuItem {
    static class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private final String address;
        private final int[] alignCount;
        private TextView alignWarn;
        private AlertDialog dialog;
        private EditText edit;
        private CheckBox hex;
        private static String lastInput;
        private CheckBox makeCopy;
        private final int[] modCount;
        private SystemSpinnerType typeSpinner;
        private static boolean useHex;

        static {
            Impl.lastInput = "0";
            Impl.useHex = true;
        }

        Impl(String address) {
            this.alignCount = new int[3];
            this.modCount = new int[4];
            this.address = address;
        }

        @Override  // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            this.checkWarn();
        }

        @Override  // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        private void checkWarn() {
            if(this.edit == null) {
                return;
            }
            int v = this.typeSpinner.getSelected();
            int align = 1;
            int fix = 0;
            try {
                long v3 = this.getOffset(false);
                if(v == 0x7F) {
                    int[] alignCount = this.alignCount;
                    for(int i = 0; i < alignCount.length; ++i) {
                        if(alignCount[i] != 0 && (((long)((1 << i) - 1)) & v3) != 0L) {
                            align = 1 << i;
                            fix += alignCount[i];
                        }
                    }
                }
                else {
                    int[] modCount = this.modCount;
                    align = AddressItem.getAlign(v);
                    for(int i = 0; i < modCount.length; ++i) {
                        if(modCount[i] != 0 && (((long)i) + v3 & ((long)(align - 1))) != 0L) {
                            fix += modCount[i];
                        }
                    }
                }
            }
            catch(Throwable unused_ex) {
            }
            this.alignWarn.setText(Tools.stringFormat(Re.s((v == 0x7F ? 0x7F070069 : 0x7F07006A)), new Object[]{align, fix}));  // string:align_warn_offset "__bad_offset__\n__correct_types__"
            this.alignWarn.setVisibility((fix <= 0 ? 4 : 0));
        }

        private long getOffset(boolean withHistory) {
            String str = SearchMenuItem.checkScript(this.edit.getText().toString().trim());
            if(str.length() == 0) {
                str = "0";
            }
            long offset = ParserNumbers.parseBigLong(str, (this.address != null || this.hex.isChecked() ? 16 : 10));
            if(this.address != null) {
                offset -= ParserNumbers.parseBigLong(this.address, 16);
            }
            if(withHistory) {
                History.add(str, 1);
            }
            return offset;
        }

        @Override  // android.widget.CompoundButton$OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.checkWarn();
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            this.checkWarn();
            if(which == -1) {
                this.onClick(((AlertDialog)dialog).getButton(-1));
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            int v3;
            Object object0 = MainService.instance.getCheckList();
            if(Tools.getSelectedCount(object0) == 0) {
                Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                return;
            }
            if(object0 instanceof LongSparseArrayChecked) {
                if(v == null || v.getTag() instanceof MenuItem) {
                    View view1 = LayoutInflater.inflateStatic(0x7F040017, null);  // layout:service_address_editor
                    EditText editText0 = (EditText)view1.findViewById(0x7F0B0065);  // id:mem_edit_addr
                    this.edit = editText0;
                    this.makeCopy = (CheckBox)view1.findViewById(0x7F0B0068);  // id:make_copy
                    CheckBox checkBox0 = (CheckBox)view1.findViewById(0x7F0B0066);  // id:hex
                    this.hex = checkBox0;
                    checkBox0.setChecked(Impl.useHex);
                    checkBox0.setOnCheckedChangeListener(this);
                    this.alignWarn = (TextView)view1.findViewById(0x7F0B0069);  // id:align_warn
                    SystemSpinnerType systemSpinnerType0 = (SystemSpinnerType)view1.findViewById(0x7F0B0067);  // id:type
                    this.typeSpinner = systemSpinnerType0;
                    systemSpinnerType0.setData(AddressItem.getDataForSearch(0x7F, true));
                    systemSpinnerType0.setSelected(0x7F);
                    systemSpinnerType0.setOnItemSelectedListener(this);
                    int v = ((LongSparseArrayChecked)object0).size();
                    int[] alignCount = this.alignCount;
                    Arrays.fill(alignCount, 0);
                    int[] modCount = this.modCount;
                    Arrays.fill(modCount, 0);
                    for(int i = 0; i < v; ++i) {
                        if(((LongSparseArrayChecked)object0).checkAt(i)) {
                            SavedItem item = (SavedItem)((LongSparseArrayChecked)object0).valueAt(i);
                            if(item instanceof SavedItem) {
                                int v2 = item.getAlign();
                                if(v2 == 1) {
                                    v3 = 0;
                                }
                                else {
                                    v3 = v2 == 2 ? 1 : 2;
                                }
                                ++alignCount[v3];
                                int v4 = (int)(3L & item.address);
                                ++modCount[v4];
                            }
                        }
                    }
                    editText0.setDataType(1);
                    editText0.addTextChangedListener(this);
                    if(this.address == null) {
                        editText0.setText("0");
                    }
                    else {
                        ((LinearLayout)view1.findViewById(0x7F0B0064)).setOrientation(1);  // id:group
                        ((TextView)view1.findViewById(0x7F0B0051)).setText(Tools.stripColon(0x7F070348) + ":");  // id:name
                        checkBox0.setVisibility(8);
                    }
                    this.checkWarn();
                    AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07009D), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:ok "OK"
                    this.dialog = alertDialog0;
                    Alert.setOnDismissListener(alertDialog0, this);
                    Alert.setOnShowListener(alertDialog0, this);
                    Alert.show(alertDialog0, editText0);
                    return;
                }
                try {
                    if(this.edit != null) {
                        long v5 = this.getOffset(true);
                        ArrayList changed = new ArrayList();
                        int v6 = ((LongSparseArrayChecked)object0).size();
                    alab1:
                        for(int i = 0; true; ++i) {
                            if(i >= v6) {
                                MainService.instance.changeCheckList(0);
                                boolean z = this.makeCopy.isChecked();
                                SavedListAdapter savedList = MainService.instance.savedList;
                                if(!z) {
                                    savedList.remove(changed);
                                }
                                int v8 = this.typeSpinner.getSelected();
                                Iterator iterator0 = changed.iterator();
                                while(true) {
                                    if(!iterator0.hasNext()) {
                                        savedList.notifyDataSetChanged();
                                        savedList.reloadData();
                                        Record record = MainService.instance.currentRecord;
                                        if(record == null) {
                                            break alab1;
                                        }
                                        record.write("\nlocal copy = ");
                                        record.write((z ? "true" : "false"));
                                        record.write("\n");
                                        record.write("local t = gg.getListItems()\n");
                                        record.write("if not copy then gg.removeListItems(t) end\n");
                                        record.write("for i, v in ipairs(t) do\n");
                                        record.write("\tv.address = v.address + ");
                                        if(this.address == null && !this.hex.isChecked()) {
                                            record.write(Long.toString(v5));
                                        }
                                        else {
                                            Consts.logHex(record, v5);
                                        }
                                        record.write("\n");
                                        record.write("\tif copy then v.name = v.name..\' #2\' end\n");
                                        record.write("end\n");
                                        record.write("gg.addListItems(t)\n");
                                        record.write("t = nil\n");
                                        record.write("copy = nil\n\n");
                                        break alab1;
                                    }
                                    Object object1 = iterator0.next();
                                    SavedItem curItem = ((SavedItem)object1).copy();
                                    if(z) {
                                        curItem.name = "Var #00000000 #2";
                                    }
                                    if(v8 != 0x7F) {
                                        curItem.flags = v8;
                                    }
                                    curItem.address += v5;
                                    if(!curItem.isPossibleItem()) {
                                        curItem.flags = AddressItem.getTypeForAddress(curItem.address, true);
                                    }
                                    savedList.add(curItem, 1, false);
                                }
                            }
                            if(((LongSparseArrayChecked)object0).checkAt(i)) {
                                SavedItem item = (SavedItem)((LongSparseArrayChecked)object0).valueAt(i);
                                if(item instanceof SavedItem) {
                                    changed.add(item.copy());
                                }
                            }
                        }
                    }
                    Tools.dismiss(this.dialog);
                }
                catch(NumberFormatException e) {
                    SearchMenuItem.inputError(e, this.edit);
                }
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            if(this.address == null) {
                EditText edit = this.edit;
                if(edit != null) {
                    Impl.lastInput = edit.getText().toString().trim();
                    Impl.useHex = this.hex.isChecked();
                }
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this, this.edit);
        }

        @Override  // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.checkWarn();
        }
    }

    public AddressEditor() {
        super(0x7F0702F9, 0x7F020019);  // string:change_address_type "Change address / type"
    }

    public static void onClick(String address) {
        new Impl(address).onClick(null);
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(null).onClick(v);
    }
}

