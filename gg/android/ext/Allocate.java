package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CheckBox;

public class Allocate extends MenuItem {
    static class Impl implements DialogInterface.OnClickListener, DialogInterface.OnShowListener, View.OnClickListener {
        private EditText addr;
        private AlertDialog dialog;
        private CheckBox exec;
        private CheckBox read;
        private CheckBox write;

        private Impl() {
            this.read = null;
            this.write = null;
            this.exec = null;
            this.addr = null;
            this.dialog = null;
        }

        Impl(Impl allocate$Impl0) {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface d, int which) {
            long address;
            EditText addr = this.addr;
            if(addr == null) {
                return;
            }
            else {
                try {
                    String data = SearchMenuItem.checkScript(addr.getText().toString().trim());
                    if(data.endsWith("h")) {
                        data = data.substring(0, data.length() - 1);
                    }
                    if(data.length() == 0) {
                        data = "0";
                    }
                    try {
                        address = ParserNumbers.parseBigLong(data, 16);
                        if(address != 0L) {
                            History.add(data, 1);
                        }
                    }
                    catch(NumberFormatException e) {
                        addr.requestFocus();
                        throw e;
                    }
                    int mode = this.read.isChecked() ? 2 : 0;
                    if(this.write.isChecked()) {
                        mode |= 1;
                    }
                    if(this.exec.isChecked()) {
                        mode |= 4;
                    }
                    Log.d(("Allocate: " + mode + "; " + address));
                    Tools.dismiss(this.dialog);
                    MainService.instance.mDaemonManager.allocatePage(address, mode);
                    Record record = MainService.instance.currentRecord;
                    if(record != null) {
                        record.write("gg.allocatePage(");
                        Consts.logConst(record, record.consts.PROT_, mode);
                        record.write(", ");
                        Consts.logHex(record, address);
                        record.write(")\n");
                        return;
                    }
                    return;
                }
                catch(NumberFormatException e) {
                }
            }
            SearchMenuItem.inputError(e, addr);
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                this.show(null);
                return;
            }
            this.onClick(null, -1);
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface d) {
            Tools.setButtonListener(this.dialog, -1, null, this);
        }

        public void show(String page) {
            View view0 = LayoutInflater.inflateStatic(0x7F040000, null);  // layout:alloc
            EditText editText0 = (EditText)view0.findViewById(0x7F0B0003);  // id:memory_from
            this.addr = editText0;
            view0.findViewById(0x7F0B0004).setTag(editText0);  // id:region_from
            if(page != null) {
                editText0.setText(page);
            }
            editText0.setDataType(1);
            this.read = (CheckBox)view0.findViewById(0x7F0B0000);  // id:read
            this.write = (CheckBox)view0.findViewById(0x7F0B0001);  // id:write
            this.exec = (CheckBox)view0.findViewById(0x7F0B0002);  // id:exec
            AlertDialog alertDialog0 = Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0702DE)).setView(InternalKeyboard.getView(view0)).setPositiveButton(Re.s(0x7F07009D), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:allocate_page "Allocate memory page"
            this.dialog = alertDialog0;
            Alert.setOnShowListener(alertDialog0, this);
            Alert.show(alertDialog0, editText0);
        }
    }

    public static final int PROT_EXEC = 4;
    public static final int PROT_NONE = 0;
    public static final int PROT_READ = 2;
    public static final int PROT_SEM = 8;
    public static final int PROT_WRITE = 1;

    public Allocate() {
        super(0x7F0702DE, 0x7F020015);  // string:allocate_page "Allocate memory page"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(null).onClick(v);
    }

    public static void show(String page) {
        new Impl(null).show(page);
    }
}

