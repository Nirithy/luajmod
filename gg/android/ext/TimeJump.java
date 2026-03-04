package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeJump extends MenuItem {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, TextWatcher, View.OnClickListener {
        AlertDialog dialog;
        EditText edit;
        TextView hint;
        TextView panel;

        private Impl() {
        }

        Impl(Impl timeJump$Impl0) {
        }

        @Override  // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            this.updateHint();
        }

        @Override  // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            try {
                String number = SearchMenuItem.checkScript(this.edit.getText().toString().trim());
                TimeJump.makeJump(TimeJump.parseNumber(number));
                History.add(number, 1);
                Tools.dismiss(dialog);
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, this.edit);
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View view) {
            if(view == null || view.getTag() instanceof MenuItem) {
                View view1 = LayoutInflater.inflateStatic(0x7F040029, null);  // layout:time_jump
                Config.setIconSize(((ImageView)view1.findViewById(0x7F0B0011))).setOnClickListener(this);  // id:examples
                this.hint = (TextView)view1.findViewById(0x7F0B002C);  // id:hint
                this.panel = (TextView)view1.findViewById(0x7F0B0084);  // id:config_time_jump_panel
                this.panel.setOnClickListener(this);
                this.panel.setText(Config.get(0x7F0B0084).toString());  // id:config_time_jump_panel
                this.edit = (EditText)view1.findViewById(0x7F0B0145);  // id:time_jump
                String text = Tools.doubleToTime(((double)Config.timeJumpLast) / 1000.0);
                this.edit.setText(text);
                this.edit.addTextChangedListener(this);
                this.edit.setDataType(1);
                this.updateHint();
                this.dialog = Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07009D), this).setNegativeButton(Re.s(0x7F07024A), null).create();  // string:ok "OK"
                Alert.setOnShowListener(this.dialog, this);
                Alert.setOnDismissListener(this.dialog, this);
                Alert.show(this.dialog, this.edit);
                return;
            }
            switch(view.getId()) {
                case 0x7F0B0011: {  // id:examples
                    Searcher.showExamples(Re.s(0x7F070028));  // string:examples_timejump "\n\"42345678\" = 1 __unit_years_short__ 125 __unit_days_short__ 
                                                              // 2 __unit_hours_short__ 41 __unit_minutes_short__ 18 __unit_seconds_short__\n\"1:125:2:41:18\" 
                                                              // = 1 __unit_years_short__ 125 __unit_days_short__ 2 __unit_hours_short__ 41 __unit_minutes_short__ 
                                                              // 18 __unit_seconds_short__\n\"5:13\" = 5 __unit_minutes_short__ 13 __unit_seconds_short__\n\"7:3:1\" 
                                                              // = 7 __unit_hours_short__ 3 __unit_minutes_short__ 1 __unit_seconds_short__\n\"3600\" 
                                                              // = 1 __unit_hours_short__\n\"2:15:54:32\" = 2 __unit_days_short__ 15 __unit_hours_short__ 
                                                              // 54 __unit_minutes_short__ 32 __unit_seconds_short__\n\"3600.15\" = 1 __unit_hours_short__ 
                                                              // 0.15 __unit_seconds_short__\n\"7:3:1.519\" = 7 __unit_hours_short__ 3 __unit_minutes_short__ 
                                                              // 1.519 __unit_seconds_short__\n\n__rtfm__\n    "
                    return;
                }
                case 0x7F0B0084: {  // id:config_time_jump_panel
                    Option config$Option0 = Config.get(0x7F0B0084);  // id:config_time_jump_panel
                    config$Option0.setOnChangedListner(new OnChangedListener() {
                        @Override  // android.ext.Config$Option$OnChangedListener
                        public void onChanged(int value) {
                            config$Option0.setOnChangedListner(null);
                            Impl.this.panel.setText(config$Option0.toString());
                        }
                    });
                    config$Option0.change();
                    return;
                }
                default: {
                    this.onClick(this.dialog, -1);
                }
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            TimeJump.last = this.edit.getText().toString().trim();
            try {
                TimeJump.saveJump(TimeJump.parseNumber(SearchMenuItem.checkScript(TimeJump.last)));
            }
            catch(NumberFormatException unused_ex) {
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this);
        }

        @Override  // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.updateHint();
        }

        public void updateHint() {
            String text;
            EditText edit = this.edit;
            TextView hint = this.hint;
            if(edit == null) {
                text = "?";
            }
            else {
                try {
                    text = Tools.longToTime(TimeJump.parseNumber(edit.getText().toString().trim()) / 1000000000L);
                }
                catch(NumberFormatException unused_ex) {
                    text = "???";
                }
            }
            if(hint != null) {
                hint.setText(text);
            }
        }
    }

    static volatile String last;
    static volatile TimeJump lastInstance;

    static {
        TimeJump.last = "0";
        TimeJump.lastInstance = null;
    }

    public TimeJump() {
        super(0x7F07014A, 0x7F020021);  // string:time_jump "Time jump"
        TimeJump.lastInstance = this;
    }

    public static void makeJump(long jump) {
        if(MainService.instance.mAppDetector.checkAppSelect(false, new Runnable() {
            @Override
            public void run() {
                MainService.instance.mDaemonManager.addTimeJump(jump);
            }
        })) {
            MainService.instance.mDaemonManager.addTimeJump(jump);
        }
        TimeJump.saveJump(jump);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            String s = Tools.doubleToTime(((double)jump) / 1000000000.0);
            record.write("gg.timeJump(");
            Consts.logNumberString(record, s);
            record.write(")\n");
        }
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }

    public static long parseNumber(String number) throws NumberFormatException {
        String s1 = number.trim();
        double f = Tools.parseTime(s1);
        if(f < -92233720.0) {
            NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_(Tools.stringFormat(Re.s(0x7F0700D1), new Object[]{s1, 0xFFFFFFFFFA80A008L}));  // string:number_less "Number \'__s__\' is less than minimum value (__d__) for this 
                                                                                                                                                                                     // type."
            parserNumbers$NumberFormatException_0.addNumber(s1);
            parserNumbers$NumberFormatException_0.addNumber(0xFFFFFFFFFA80A008L);
            throw parserNumbers$NumberFormatException_0;
        }
        if(f > 92233720.0) {
            NumberFormatException_ parserNumbers$NumberFormatException_1 = new NumberFormatException_(Tools.stringFormat(Re.s(0x7F0700D2), new Object[]{s1, 0x57F5FF8L}));  // string:number_greater "Number \'__s__\' is greater than maximum value (__d__) for 
                                                                                                                                                                            // this type."
            parserNumbers$NumberFormatException_1.addNumber(s1);
            parserNumbers$NumberFormatException_1.addNumber(0x57F5FF8L);
            throw parserNumbers$NumberFormatException_1;
        }
        return (long)(1000000000.0 * f);
    }

    static void saveJump(long jump) {
        Config.get(0x7F0B00C0).value = (int)(((double)jump) / 1000000000.0 * 1000.0);  // id:config_time_jump_last
        Config.save();
        TimeJumpPanel panel = MainService.instance.timeJumpPanel;
        if(panel != null) {
            panel.setLast();
        }
    }
}

