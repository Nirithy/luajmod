package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ExecuteScript extends MenuItem {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener, View.OnLongClickListener {
        private AlertDialog dialog;
        private CheckBox disassemble;
        private EditText edit;
        private Button ext;
        private CheckBox load;
        private CheckBox log;
        private EditText path;
        private View stuff;

        private Impl() {
        }

        Impl(Impl executeScript$Impl0) {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface d, int which) {
            ExecuteScript.disassemble = this.disassemble.isChecked();
            ExecuteScript.load = this.load.isChecked();
            ExecuteScript.log = this.log.isChecked();
            int params = 0;
            String s = this.path.getText().toString().trim();
            if(!ExecuteScript.debug) {
            label_13:
                History.add(s, 4);
                EditText edit = this.edit;
                if(edit != null) {
                    String s1 = edit.getText().toString().trim();
                    if(Tools.isPath(s1) && Tools.isFile(s1)) {
                        History.add(s1, 4);
                        if(MainService.instance.currentScript != null) {
                            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(Re.s(0x7F070219)).setPositiveButton(Re.s(0x7F07021A), new DialogInterface.OnClickListener() {  // string:error "Error"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface d, int w) {
                                    MainService.instance.interruptScript();
                                    Impl.this.onClick(null, which);
                                }
                            }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                            return;
                        }
                        MainService.instance.executeScript(s1, params, s);
                        Tools.dismiss(this.dialog);
                        Record record = MainService.instance.currentRecord;
                        if(record != null) {
                            record.write("loadfile(");
                            Consts.logString(record, s1);
                            record.write(")()\n");
                        }
                    }
                }
            }
            else if(Tools.isPath(s)) {
                if(ExecuteScript.disassemble) {
                    params = 1;
                }
                if(ExecuteScript.load) {
                    params |= 2;
                }
                if(ExecuteScript.log) {
                    params |= 4;
                }
                goto label_13;
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                if(!MainService.instance.mDaemonManager.isDaemonRun()) {
                    return;
                }
                View view1 = LayoutInflater.inflateStatic(0x7F040003, null);  // layout:execute
                ((TextView)view1.findViewById(0x7F0B000E)).setText(Re.s(0x7F070217));  // id:message
                EditTextPath edit = (EditTextPath)view1.findViewById(0x7F0B000F);  // id:file
                this.edit = edit;
                edit.setText(PkgPath.load("script-path", "-script", ".lua"));
                edit.setDataType(4);
                edit.setPathType(1);
                view1.findViewById(0x7F0B0010).setTag(edit);  // id:file_selector
                this.stuff = view1.findViewById(0x7F0B0015);  // id:debug
                EditTextPath path = (EditTextPath)view1.findViewById(0x7F0B000A);  // id:path
                this.path = path;
                path.setText(PkgPath.path(null, "script-debug"));
                path.setDataType(4);
                path.setPathType(0);
                view1.findViewById(0x7F0B000B).setTag(path);  // id:path_selector
                CheckBox checkBox0 = (CheckBox)view1.findViewById(0x7F0B0016);  // id:disassemble
                this.disassemble = checkBox0;
                checkBox0.setChecked(ExecuteScript.disassemble);
                Tools.setButtonHelpBackground(checkBox0);
                checkBox0.setOnLongClickListener(this);
                CheckBox checkBox1 = (CheckBox)view1.findViewById(0x7F0B0017);  // id:load
                this.load = checkBox1;
                checkBox1.setChecked(ExecuteScript.load);
                CheckBox checkBox2 = (CheckBox)view1.findViewById(0x7F0B0018);  // id:log
                this.log = checkBox2;
                checkBox2.setChecked(ExecuteScript.log);
                Button button0 = (Button)view1.findViewById(0x7F0B0019);  // id:ext_btn
                this.ext = button0;
                button0.setOnClickListener(this);
                this.update();
                AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view1, false)).setPositiveButton(Re.s(0x7F070218), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:execute "Execute"
                Alert.setOnShowListener(alertDialog0, this);
                Alert.setOnDismissListener(alertDialog0, this);
                this.dialog = alertDialog0;
                Alert.show(alertDialog0, edit);
                return;
            }
            if(view.getId() != 0x7F0B0019) {  // id:ext_btn
                this.onClick(null, -1);
                return;
            }
            ExecuteScript.debug = !ExecuteScript.debug;
            this.update();
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            EditText edit = this.edit;
            if(edit != null) {
                PkgPath.save(edit.getText().toString(), "script-path", "-script", ".lua");
            }
            EditText path = this.path;
            if(path != null) {
                PkgPath.path(path.getText().toString().trim(), "script-debug");
            }
        }

        @Override  // android.view.View$OnLongClickListener
        public boolean onLongClick(View v) {
            if(v != null && v.getId() == 0x7F0B0016) {  // id:disassemble
                ConfigListAdapter.showHelp(0x7F070343);  // string:help_lasm "__app_name__ has a built-in disassembler / assembler for Lua scripts.\n\nYou 
                                                         // can select the \"__debug_disassemble__\" option in the script launch window, after 
                                                         // clicking the \"__more__\" button.\n\nThe resulting __ext_lasm__ file can be edited 
                                                         // and selected in the script launch window. The result will be a binary __ext_lua__ 
                                                         // file, with the changes made.\n\nRead the script API help for more details.\n    
                                                         // "
                return true;
            }
            return false;
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }

        private void update() {
            boolean debug = ExecuteScript.debug;
            this.ext.setText(Re.s((debug ? 0x7F070157 : 0x7F070156)));  // string:less "Less"
            this.stuff.setVisibility((debug ? 0 : 8));
        }
    }

    public static final int DISASSEMBLE = 1;
    public static final int LOAD = 2;
    public static final int LOG = 4;
    private static final String SCRIPT_DEBUG = "script-debug";
    private static final String SCRIPT_PATH = "script-path";
    static boolean debug;
    static boolean disassemble;
    static boolean load;
    static boolean log;

    static {
        ExecuteScript.debug = false;
        ExecuteScript.disassemble = true;
        ExecuteScript.load = true;
        ExecuteScript.log = true;
    }

    public ExecuteScript() {
        super(0x7F070217, 0x7F02003E);  // string:execute_script "Execute script"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }
}

