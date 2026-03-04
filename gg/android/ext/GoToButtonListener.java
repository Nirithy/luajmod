package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;

public class GoToButtonListener extends MenuItem {
    static class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener {
        private AlertDialog dialog;
        private EditText edit;
        private static String lastInput;

        static {
            Impl.lastInput = "";
        }

        private Impl() {
        }

        Impl(Impl goToButtonListener$Impl0) {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                this.onClick(((AlertDialog)dialog).getButton(-1));
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            if(v == null || v.getTag() instanceof MenuItem) {
                View view1 = LayoutInflater.inflateStatic(0x7F040021, null);  // layout:service_goto
                TextView addr = (TextView)view1.findViewById(0x7F0B011D);  // id:addr
                Tools.setButtonHelpBackground(addr);
                addr.setOnClickListener(this);
                EditText editText0 = (EditText)view1.findViewById(0x7F0B0065);  // id:mem_edit_addr
                this.edit = editText0;
                RegionList regionList = (RegionList)view1.findViewById(0x7F0B0004);  // id:region_from
                regionList.setTag(editText0);
                android.ext.GoToButtonListener.Impl.1 listener = new View.OnClickListener() {
                    @Override  // android.view.View$OnClickListener
                    public void onClick(View v) {
                        int filter;
                        switch(v.getId()) {
                            case 0x7F0B0120: {  // id:cb
                                filter = 16;
                                break;
                            }
                            case 0x7F0B0121: {  // id:ps
                                filter = 0x40000;
                                break;
                            }
                            case 0x7F0B0122: {  // id:xa
                                filter = 0x4000;
                                break;
                            }
                            default: {
                                filter = 8;
                            }
                        }
                        regionList.onClick(regionList, filter);
                    }
                };
                view1.findViewById(0x7F0B011F).setOnClickListener(listener);  // id:cd
                view1.findViewById(0x7F0B0120).setOnClickListener(listener);  // id:cb
                view1.findViewById(0x7F0B0121).setOnClickListener(listener);  // id:ps
                view1.findViewById(0x7F0B0122).setOnClickListener(listener);  // id:xa
                view1.findViewById(0x7F0B011E).setOnClickListener(this);  // id:history
                editText0.setText("");
                editText0.setDataType(1);
                AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(Re.s(0x7F07008D), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:goto1 "Goto"
                this.dialog = alertDialog0;
                Alert.setOnDismissListener(alertDialog0, this);
                Alert.setOnShowListener(alertDialog0, this);
                Alert.show(alertDialog0, editText0);
                return;
            }
            switch(v.getId()) {
                case 0x7F0B011D: {  // id:addr
                    Alert.show(Alert.create().setMessage(Re.s(0x7F07018F) + ":\nBAFE890\nBAFE890+3F0\nBAFE890-A40\nBAFE890+FE-CD7+44-3E\n\nlibc.so\nlibc.so+E0\nlibc.so-32C").setPositiveButton(Re.s(0x7F07012B), ConfigListAdapter.getShowHelpListener()).setNegativeButton(Re.s(0x7F0700A1), null));  // string:examples "Examples"
                    return;
                }
                case 0x7F0B011E: {  // id:history
                    Tools.dismiss(this.dialog);
                    MainService.instance.memoryHistory.choice();
                    return;
                }
                default: {
                    try {
                        EditText edit = this.edit;
                        if(edit != null) {
                            String str = SearchMenuItem.checkScript(edit.getText().toString().trim());
                            History.add(str, 1);
                            MainService.instance.goToAddress(null, str, Tools.stripColon(0x7F070251));  // string:from_goto "Call goto"
                        }
                        Tools.dismiss(this.dialog);
                    }
                    catch(NumberFormatException e) {
                        SearchMenuItem.inputError(e, this.edit);
                    }
                }
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            EditText edit = this.edit;
            if(edit != null) {
                Impl.lastInput = edit.getText().toString().trim();
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this, this.edit);
        }
    }

    public GoToButtonListener() {
        super(0x7F07008D, 0x7F02002A);  // string:goto1 "Goto"
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(null).onClick(v);
    }
}

