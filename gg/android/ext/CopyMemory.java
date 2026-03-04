package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;

public class CopyMemory extends MenuItem {
    static class Impl implements DialogInterface.OnClickListener, DialogInterface.OnShowListener, View.OnClickListener {
        private int bytes;
        private AlertDialog dialog;
        private EditText edit;
        private long memFrom;
        private long memTo;
        private EditText memoryFrom;
        private EditText memoryTo;

        private Impl() {
            this.memFrom = 0L;
            this.memTo = 0L;
            this.bytes = 0;
        }

        Impl(Impl copyMemory$Impl0) {
        }

        private long getMem(int index) throws NumberFormatException {
            long v1 = MemoryRange.getMem(index, this.memoryFrom, "0", this.memoryTo, "0");
            if(index == 0) {
                this.memFrom = v1;
                return v1;
            }
            this.memTo = v1;
            return v1;
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            EditText edit = this.edit;
            if(edit == null) {
                return;
            }
            else {
                try {
                    this.getMem(0);
                    this.getMem(1);
                    String data = SearchMenuItem.checkScript(edit.getText().toString().trim());
                    if(data.length() == 0) {
                        data = "0";
                    }
                    try {
                        this.bytes = (int)ParserNumbers.parseLong(data).value;
                        if(this.bytes != 0) {
                            History.add(data, 1);
                        }
                    }
                    catch(NumberFormatException e) {
                        edit.requestFocus();
                        throw e;
                    }
                    Log.d(("Copy: " + this.bytes + "; " + Long.toHexString(this.memFrom) + '-' + Long.toHexString(this.memTo)));
                    Tools.dismiss(this.dialog);
                    MainService.instance.mDaemonManager.copyMemory(this.memFrom, this.memTo, this.bytes);
                    Record record = MainService.instance.currentRecord;
                    if(record != null) {
                        record.write("gg.copyMemory(");
                        Consts.logHex(record, this.memFrom);
                        record.write(", ");
                        Consts.logHex(record, this.memTo);
                        record.write(", ");
                        record.write(Integer.toString(this.bytes));
                        record.write(")\n");
                        return;
                    }
                    return;
                }
                catch(NumberFormatException e) {
                }
            }
            SearchMenuItem.inputError(e, edit);
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                View view1 = LayoutInflater.inflateStatic(0x7F04000B, null);  // layout:memory_copy
                EditText editText0 = (EditText)view1.findViewById(0x7F0B003F);  // id:count_bytes
                this.edit = editText0;
                view1.findViewById(0x7F0B0040).setTag(editText0);  // id:bytes_converter
                editText0.setText(DisplayNumbers.longToString(this.bytes, 4));
                editText0.setDataType(1);
                EditText editText1 = (EditText)view1.findViewById(0x7F0B0003);  // id:memory_from
                this.memoryFrom = editText1;
                view1.findViewById(0x7F0B0004).setTag(editText1);  // id:region_from
                EditText editText2 = (EditText)view1.findViewById(0x7F0B003D);  // id:memory_dst
                this.memoryTo = editText2;
                view1.findViewById(0x7F0B003E).setTag(editText2);  // id:region_dst
                editText1.setText(AddressItem.getStringAddress(this.memFrom, 4));
                editText1.setDataType(1);
                editText2.setText(AddressItem.getStringAddress(this.memTo, 4));
                editText2.setDataType(1);
                AlertDialog alertDialog0 = Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0701B2)).setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07009D), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:copy_memory "Copy memory"
                this.dialog = alertDialog0;
                Alert.setOnShowListener(alertDialog0, this);
                Alert.show(alertDialog0, editText0);
                return;
            }
            this.onClick(null, -1);
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }
    }

    public CopyMemory() {
        super(0x7F0701B2, 0x7F020017);  // string:copy_memory "Copy memory"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(null).onClick(v);
    }
}

