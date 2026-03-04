package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Unrandomizer extends MenuItem {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnShowListener, View.OnClickListener {
        AlertDialog dialog;
        EditText doubleEdit;
        EditText doubleIncEdit;
        CheckBox doubleIncUse;
        CheckBox doubleUse;
        EditText qwordEdit;
        EditText qwordIncEdit;
        CheckBox qwordIncUse;
        CheckBox qwordUse;

        private Impl() {
        }

        Impl(Impl unrandomizer$Impl0) {
        }

        private void initRow(CheckBox prev, CheckBox ch, boolean checked, EditText ed, String text) {
            ch.setChecked(checked);
            ch.setText(Re.s(ch.getText().toString()));
            ed.setDataType(1);
            ed.setText(text);
            ed.setTag(ch);
            ed.addTextChangedListener(new TextWatcher() {
                @Override  // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                    ch.setChecked(true);
                    if(prev != null) {
                        prev.setChecked(true);
                    }
                }

                @Override  // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override  // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            if(prev != null) {
                prev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!isChecked) {
                            ch.setChecked(false);
                        }
                    }
                });
                ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            prev.setChecked(true);
                        }
                    }
                });
            }
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            try {
                int flags = 0;
                boolean z = this.qwordUse.isChecked();
                boolean qIncUse = false;
                long qBase = 0L;
                long qInc = 0L;
                if(z) {
                    flags = 2;
                    qBase = this.parse(this.qwordEdit, true);
                    Unrandomizer.lastQword = qBase;
                    qIncUse = this.qwordIncUse.isChecked();
                    if(qIncUse) {
                        qInc = this.parse(this.qwordIncEdit, true);
                        Unrandomizer.lastQwordInc = qInc;
                    }
                }
                Unrandomizer.lastQwordUse = z;
                Unrandomizer.lastQwordIncUse = qIncUse;
                boolean z2 = this.doubleUse.isChecked();
                boolean dIncUse = false;
                double dBase = 0.0;
                double dInc = 0.0;
                if(z2) {
                    flags |= 4;
                    dBase = Double.longBitsToDouble(this.parse(this.doubleEdit, false));
                    Unrandomizer.lastDouble = dBase;
                    dIncUse = this.doubleIncUse.isChecked();
                    if(dIncUse) {
                        dInc = Double.longBitsToDouble(this.parse(this.doubleIncEdit, false));
                        Unrandomizer.lastDoubleInc = dInc;
                    }
                }
                Unrandomizer.lastDoubleUse = z2;
                Unrandomizer.lastDoubleIncUse = dIncUse;
                Unrandomizer.apply(flags, qBase, qInc, dBase, dInc);
                Tools.dismiss(dialog);
                Record record = MainService.instance.currentRecord;
                if(record != null) {
                    record.write("gg.unrandomizer(");
                    if(z) {
                        record.write(Long.toString(qBase));
                        record.write(", ");
                        record.write(Long.toString(qInc));
                        record.write(", ");
                    }
                    else {
                        record.write("nil, nil, ");
                    }
                    if(z2) {
                        record.write(Double.toString(dBase));
                        record.write(", ");
                        record.write(Double.toString(dInc));
                    }
                    else {
                        record.write("nil, nil");
                    }
                    record.write(")\n");
                }
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, null);
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                View view1 = LayoutInflater.inflateStatic(0x7F04002E, null);  // layout:unrand
                TextView message = (TextView)view1.findViewById(0x7F0B000E);  // id:message
                Tools.setButtonHelpBackground(message);
                message.setOnClickListener(this);
                this.qwordUse = (CheckBox)view1.findViewById(0x7F0B014B);  // id:rd_qword_use
                this.qwordEdit = (EditText)view1.findViewById(0x7F0B014C);  // id:rd_qword
                this.qwordIncUse = (CheckBox)view1.findViewById(0x7F0B014D);  // id:rd_qword_inc_use
                this.qwordIncEdit = (EditText)view1.findViewById(0x7F0B014E);  // id:rd_qword_inc
                this.initRow(null, this.qwordUse, Unrandomizer.lastQwordUse, this.qwordEdit, DisplayNumbers.longToString(Unrandomizer.lastQword, 0x20));
                this.initRow(this.qwordUse, this.qwordIncUse, Unrandomizer.lastQwordIncUse, this.qwordIncEdit, DisplayNumbers.longToString(Unrandomizer.lastQwordInc, 0x20));
                this.doubleUse = (CheckBox)view1.findViewById(0x7F0B014F);  // id:rd_double_use
                this.doubleEdit = (EditText)view1.findViewById(0x7F0B0150);  // id:rd_double
                this.doubleIncUse = (CheckBox)view1.findViewById(0x7F0B0151);  // id:rd_double_inc_use
                this.doubleIncEdit = (EditText)view1.findViewById(0x7F0B0152);  // id:rd_double_inc
                this.initRow(null, this.doubleUse, Unrandomizer.lastDoubleUse, this.doubleEdit, "0.0");
                this.initRow(this.doubleUse, this.doubleIncUse, Unrandomizer.lastDoubleIncUse, this.doubleIncEdit, "0.01");
                this.dialog = Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07009D), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:ok "OK"
                Alert.setOnShowListener(this.dialog, this);
                Alert.show(this.dialog, this.qwordEdit);
                return;
            }
            if(view.getId() != 0x7F0B000E) {  // id:message
                this.onClick(this.dialog, -1);
                return;
            }
            ConfigListAdapter.showHelp(0x7F07027E);  // string:help_unrandomizer "\nThe unrandomizer is a feature designed to replace certain 
                                                     // functions commonly used in games to produce random values. At a very simple level 
                                                     // then, for an example, there may be a game that uses a randomizer function to determine 
                                                     // the player\'s reward. Enabling the unrandomizer would then cause __app_name__ to 
                                                     // try to replace this random value with the specified value in order to change the 
                                                     // result (for example, to get a certain reward).\n\nThe unrandomizer won\'t work in 
                                                     // all cases. It will only handle functions that it knows about. In most cases and 
                                                     // for most users, this will not be used, as it\'s more of an advanced/experimental 
                                                     // feature.\n\nSome functions return __type_qword__, some - __type_double__ (rarely 
                                                     // used).\n\n__type_double__ values must be in the range from 0 to 1. However, you 
                                                     // can specify any values there, because __app_name__ will correct all the incorrect 
                                                     // variants automatically.\n\nYou can also specify an increment step. In this case, 
                                                     // each subsequent result will increase by this value. For example, for increment 1 
                                                     // and the initial value 0, instead of a random value, 0, 1, 2, 3, ... will be returned.\nThis 
                                                     // option can be useful for a complete search of all possible random results.\n\nYou 
                                                     // can specify which functions to modify in the __app_name__ settings.\n    "
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }

        private long parse(EditText ed, boolean qword) {
            String val = SearchMenuItem.checkScript(ed.getText().toString().trim());
            long ret = qword ? ParserNumbers.parseLong(val).value : ParserNumbers.parseDouble(val).value;
            History.add(val, 1);
            return ret;
        }
    }

    static volatile double lastDouble;
    static volatile double lastDoubleInc;
    static volatile boolean lastDoubleIncUse;
    static volatile boolean lastDoubleUse;
    static volatile Unrandomizer lastInstance;
    static volatile long lastQword;
    static volatile long lastQwordInc;
    static volatile boolean lastQwordIncUse;
    static volatile boolean lastQwordUse;

    static {
        Unrandomizer.lastQwordUse = false;
        Unrandomizer.lastQword = 0L;
        Unrandomizer.lastQwordIncUse = false;
        Unrandomizer.lastQwordInc = 1L;
        Unrandomizer.lastDoubleUse = false;
        Unrandomizer.lastDouble = 0.0;
        Unrandomizer.lastDoubleIncUse = false;
        Unrandomizer.lastDoubleInc = 0.01;
        Unrandomizer.lastInstance = null;
    }

    public Unrandomizer() {
        super(0x7F07027C, 0x7F02001C);  // string:unrandomizer "Unrandomizer"
        Unrandomizer.lastInstance = this;
    }

    public static void apply(int flags, long qBase, long qInc, double dBase, double dInc) {
        MainService.instance.mDaemonManager.unrand(flags, qBase, qInc, dBase, dInc);
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }

    public static void reset() {
        Unrandomizer.lastQwordUse = false;
        Unrandomizer.lastQwordIncUse = false;
        Unrandomizer.lastDoubleUse = false;
        Unrandomizer.lastDoubleIncUse = false;
    }
}

