package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.SparseArray;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import java.lang.ref.WeakReference;

public abstract class SearchMenuItem extends MenuItem implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    public static final char COLON = ':';
    public static final char HEX = 'h';
    public static final char HEX_UTF = 'Q';
    public static final char HEX_UTF_16LE = '\"';
    public static final char HEX_UTF_8 = '\'';
    public static final char SEMICOLON = ';';
    public static final char TILDE = '~';
    public static final char UTF_16LE = ';';
    public static final char UTF_8 = ':';
    public static final char XOR_MODE = 'X';
    public static final char XOR_MODE_SMALL = 'x';
    protected int lastButton;
    protected String lastInput;
    protected String lastMask;
    protected Searcher searcher;
    protected int signLastSelect;
    protected WeakReference weakDialog;

    public SearchMenuItem(int title) {
        super(title);
        this.lastInput = "";
        this.signLastSelect = 0x20000000;
        this.lastMask = "FFFFFFFFFFFFFFFF";
        this.lastButton = -1;
        this.weakDialog = new WeakReference(null);
    }

    public SearchMenuItem(int title, int resId) {
        super(title, resId);
        this.lastInput = "";
        this.signLastSelect = 0x20000000;
        this.lastMask = "FFFFFFFFFFFFFFFF";
        this.lastButton = -1;
        this.weakDialog = new WeakReference(null);
    }

    public SearchMenuItem(String title) {
        super(title);
        this.lastInput = "";
        this.signLastSelect = 0x20000000;
        this.lastMask = "FFFFFFFFFFFFFFFF";
        this.lastButton = -1;
        this.weakDialog = new WeakReference(null);
    }

    public SearchMenuItem(String title, int resId) {
        super(title, resId);
        this.lastInput = "";
        this.signLastSelect = 0x20000000;
        this.lastMask = "FFFFFFFFFFFFFFFF";
        this.lastButton = -1;
        this.weakDialog = new WeakReference(null);
    }

    public static String checkScript(String input) {
        if(input != null) {
            String s1 = input.trim();
            if(s1.length() > 0 && input.charAt(0) == 61) {
                String script = s1.substring(1);
                script = script.indexOf("return") >= 0 ? s1.substring(1) : "return " + script;
                try {
                    Log.d(("oneLiner IN: \'" + script + "\'"));
                    input = Script.oneLiner(script);
                    Log.d(("oneLiner OUT: \'" + input + "\'"));
                    return input;
                }
                catch(Throwable e) {
                    Log.d("oneLiner ERR:", e);
                    NumberFormatException numberFormatException0 = new NumberFormatException(e.getMessage());
                    numberFormatException0.initCause(e);
                    throw numberFormatException0;
                }
            }
        }
        return input;
    }

    public static void inputError(NumberFormatException e, EditText edit) {
        Log.w("Error", e);
        if(edit != null && e instanceof AsmFailedException) {
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(e.getMessage()).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:error "Error"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Editable editable0 = edit.getText();
                    String s = editable0.toString();
                    String data = ((AsmFailedException)e).data;
                    if(!s.contains(data)) {
                        return;
                    }
                    int v1 = s.indexOf(data);
                    editable0.replace(v1, data.length() + v1, ((AsmFailedException)e).suggestion);
                }
            }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
            return;
        }
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(e.getMessage()).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface d, int which) {
        switch(which) {
            case -2: {
                break;
            }
            case -3: 
            case -1: {
                this.lastButton = which;
                this.onClick(null);
                break;
            }
            default: {
                try {
                    this.searcher.setType(which);
                    this.onClickCallback(d, which);
                    Tools.hideKeyboard(this.weakDialog);
                }
                catch(NumberFormatException e) {
                    SearchMenuItem.inputError(e, this.searcher.getNumberEdit());
                    return;
                }
                catch(Throwable e) {
                    Log.e("Exception on start search", e);
                }
                Tools.dismiss(this.weakDialog);
            }
        }
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        String input;
        if(v != null && v.getTag() instanceof Integer) {
            this.lastButton = (int)(((Integer)v.getTag()));
        }
        if(v != null && v.getTag() instanceof MenuItem) {
            if(!MainService.instance.mDaemonManager.isDaemonRun()) {
                return;
            }
            if(this.searcher == null) {
                this.searcher = new Searcher((this instanceof MaskButtonListener ? 3 : 0), 0x7F);
            }
            this.searcher.setMessage(Re.s((this instanceof MaskButtonListener ? 0x7F0701FE : 0x7F0700ED)));  // string:mask_request "Enter an address to search for"
            this.searcher.setSign(this.signLastSelect);
            this.searcher.setNumber(this.lastInput);
            if(this instanceof MaskButtonListener) {
                this.searcher.setMask(this.lastMask);
                this.searcher.getView().setTag(new Flags(1));
            }
            this.searcher.setTypeInternal(0x7F);
            AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(this.searcher.getViewForAttach()).setNegativeButton(Re.s(0x7F0700A1), null);  // string:cancel "Cancel"
            if(MainService.instance.mResultCount == 0L) {
                alertDialog$Builder0.setPositiveButton(Re.s(0x7F0701A3), this);  // string:new_search "New search"
            }
            else {
                alertDialog$Builder0.setPositiveButton(Re.s(0x7F0701A0), this).setNeutralButton(Re.s(0x7F0701A3), this);  // string:refine "Refine"
            }
            this.lastButton = -1;
            AlertDialog alertDialog0 = alertDialog$Builder0.create();
            Alert.setOnDismissListener(alertDialog0, this);
            Alert.setOnShowListener(alertDialog0, this);
            this.weakDialog = new WeakReference(alertDialog0);
            Alert.show(alertDialog0, this.searcher.getNumberEdit());
            return;
        }
        int v = this.searcher.getType();
        if(v == 0) {
            if(this instanceof MaskButtonListener) {
                new TypeSelector(AddressItem.getDataForSearch(0x7F, true), "0", Re.s(0x7F0700EE), this);  // string:type_request "Select the type of data to search"
                return;
            }
            String s = this.searcher.getNumber();
            try {
                input = SearchMenuItem.checkScript(s);
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, this.searcher.getNumberEdit());
                return;
            }
            SparseArray sparseArray0 = AddressItem.getDataForSearch(0x7F, true);
            if(input.indexOf(59) != -1 || input.indexOf(0x7E) != -1 || input.indexOf(88) != -1 || input.indexOf(120) != -1) {
                input = "0";
            }
            new TypeSelector(sparseArray0, input, Re.s(0x7F0700EE), this);  // string:type_request "Select the type of data to search"
            return;
        }
        this.onClick(null, v);
    }

    public abstract void onClickCallback(DialogInterface arg1, int arg2);

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        if(this.searcher != null) {
            this.searcher.getViewForAttachSimple();
        }
    }

    @Override  // android.content.DialogInterface$OnShowListener
    public void onShow(DialogInterface dialog) {
        Tools.setButtonListener(dialog, -1, -1, this);
        Tools.setButtonListener(dialog, -3, -3, this);
        if(this.searcher != null) {
            this.searcher.onShow();
        }
    }
}

