package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Dump extends MenuItem {
    static class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener {
        private static final String DUMP_PATH = "dump-path";
        private AlertDialog dialog;
        private EditText edit;
        private long memFrom;
        private long memTo;
        private android.ext.EditText memoryFrom;
        private android.ext.EditText memoryTo;
        private CheckBox skipSystemLibs;

        private Impl() {
            this.memFrom = 0L;
            this.memTo = -1L;
        }

        Impl(Impl dump$Impl0) {
        }

        private String getDefaultPath() {
            return Tools.getSdcardPath() + "/dump";
        }

        private long getMem(int index) throws NumberFormatException {
            long v1 = MemoryRange.getMem(index, this.memoryFrom, "0", this.memoryTo, "-1");
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
                    String s = Tools.trimDirPath(edit.getText().toString().trim());
                    if(Tools.isPath(s)) {
                        History.add(s, 4);
                        int flags = this.skipSystemLibs.isChecked() ? 1 : 0;
                        Log.d(("Dump: " + s + "; " + Long.toHexString(this.memFrom) + '-' + Long.toHexString(this.memTo) + "; " + Integer.toHexString(flags)));
                        Tools.dismiss(this.dialog);
                        ProcessInfo info = MainService.instance.processInfo;
                        String prefix = info == null ? "unknown" : info.packageName;
                        if(MainService.instance.showBusyDialog()) {
                            MainService.instance.mDaemonManager.doDump(this.memFrom, this.memTo, flags, s, prefix);
                            Record record = MainService.instance.currentRecord;
                            if(record != null) {
                                record.write("gg.dumpMemory(");
                                Consts.logHex(record, this.memFrom);
                                record.write(", ");
                                Consts.logHex(record, this.memTo);
                                record.write(", ");
                                Consts.logString(record, s);
                                record.write(", ");
                                Consts.logConst(record, record.consts.DUMP_, flags);
                                record.write(")\n");
                                return;
                            }
                        }
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
                View view1 = LayoutInflater.inflateStatic(0x7F040002, null);  // layout:dump
                EditTextPath edit = (EditTextPath)view1.findViewById(0x7F0B000A);  // id:path
                this.edit = edit;
                edit.setText(Tools.getSharedPreferences().getString("dump-path", this.getDefaultPath()));
                edit.setDataType(4);
                edit.setPathType(0);
                view1.findViewById(0x7F0B000B).setTag(edit);  // id:path_selector
                TypicalValues.setCacheDirs(((Button)view1.findViewById(0x7F0B000C)), edit);  // id:typical_values
                android.ext.EditText editText0 = (android.ext.EditText)view1.findViewById(0x7F0B0003);  // id:memory_from
                this.memoryFrom = editText0;
                view1.findViewById(0x7F0B0004).setTag(editText0);  // id:region_from
                android.ext.EditText editText1 = (android.ext.EditText)view1.findViewById(0x7F0B0008);  // id:memory_to
                this.memoryTo = editText1;
                view1.findViewById(0x7F0B0009).setTag(editText1);  // id:region_to
                editText0.setText(AddressItem.getStringAddress(this.memFrom, 4));
                editText0.setDataType(1);
                InternalKeyboard.setFlagsFor(editText0, 1);
                editText1.setText(AddressItem.getStringAddress(this.memTo, 4));
                editText1.setDataType(1);
                this.skipSystemLibs = (CheckBox)view1.findViewById(0x7F0B000D);  // id:skip_system_libs
                android.ext.Dump.Impl.1 focus = new FocusListener() {
                    @Override  // android.ext.InternalKeyboard$FocusListener
                    protected boolean useExternal(View v, boolean hasFocus) {
                        return v == edit;
                    }
                };
                for(Object object0: view1.getFocusables(2)) {
                    View child = (View)object0;
                    if(child instanceof android.ext.EditText) {
                        Tools.setOnFocusChangeListener(child, focus);
                    }
                }
                AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07008C), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:save "Save"
                this.dialog = alertDialog0;
                Alert.setOnShowListener(alertDialog0, this);
                Alert.setOnDismissListener(alertDialog0, this);
                Alert.show(alertDialog0, edit);
                return;
            }
            this.onClick(null, -1);
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            EditText edit = this.edit;
            if(edit == null) {
                return;
            }
            new SPEditor().putString("dump-path", edit.getText().toString().trim(), this.getDefaultPath()).commit();
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }
    }

    public Dump() {
        super(0x7F0701A9, 0x7F020025);  // string:dump_memory "Dump memory"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(null).onClick(v);
    }
}

