package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RecordScript extends MenuItem {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener {
        private AlertDialog dialog;
        private EditText edit;

        private Impl() {
        }

        Impl(Impl recordScript$Impl0) {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface d, int which) {
            Record record;
            EditText edit = this.edit;
            if(edit != null) {
                String s = edit.getText().toString().trim();
                if(Tools.isPath(s)) {
                    if(MainService.instance.currentRecord != null) {
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(Re.s(0x7F0702E6)).setPositiveButton(Re.s(0x7F0702E8), new DialogInterface.OnClickListener() {  // string:error "Error"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface d, int w) {
                                MainService.instance.interruptRecord();
                                Impl.this.onClick(null, which);
                            }
                        }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                        return;
                    }
                    try {
                        record = new Record(s);
                    }
                    catch(Throwable e) {
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(s + "\n\n" + e.toString()).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
                        return;
                    }
                    History.add(s, 4);
                    MainService.instance.startRecord(record);
                    Tools.dismiss(this.dialog);
                }
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                if(!MainService.instance.mDaemonManager.isDaemonRun()) {
                    return;
                }
                View view1 = LayoutInflater.inflateStatic(0x7F040015, null);  // layout:save
                ((TextView)view1.findViewById(0x7F0B000E)).setText(Re.s(0x7F0702E4));  // id:message
                EditTextPath edit = (EditTextPath)view1.findViewById(0x7F0B000F);  // id:file
                this.edit = edit;
                edit.setText(PkgPath.load("record-path", "-record", ".lua"));
                edit.setDataType(4);
                edit.setPathType(2);
                view1.findViewById(0x7F0B000B).setTag(edit);  // id:path_selector
                AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view1, false)).setPositiveButton(Re.s(0x7F0702E5), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:record "Record"
                Alert.setOnShowListener(alertDialog0, this);
                Alert.setOnDismissListener(alertDialog0, this);
                this.dialog = alertDialog0;
                Alert.show(alertDialog0, edit);
                return;
            }
            this.onClick(null, -1);
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            EditText edit = this.edit;
            if(edit != null) {
                PkgPath.save(edit.getText().toString(), "record-path", "-record", ".lua");
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }
    }

    private static final String RECORD_PATH = "record-path";

    public RecordScript() {
        super(0x7F0702E4, 0x7F020041);  // string:record_script "Record script"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }
}

