package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import java.lang.ref.WeakReference;

public class PointerButtonListener extends SearchMenuItem {
    private static String lastOffset;
    private static boolean useHex;

    static {
        PointerButtonListener.lastOffset = "0";
        PointerButtonListener.useHex = true;
    }

    public PointerButtonListener() {
        super(0x7F07025A, 0x7F020040);  // string:help_pointer_search_title "Pointer search"
    }

    private int getFlags() {
        ProcessInfo info = MainService.instance.processInfo;
        return info == null || !info.x64 ? 4 : 36;
    }

    @Override  // android.ext.SearchMenuItem
    public void onClick(DialogInterface d, int which) {
        try {
            long v1 = ParserNumbers.parseBigLong(PointerButtonListener.checkScript(this.searcher.getNumber()), 16);
            long v2 = ParserNumbers.parseBigLong(PointerButtonListener.checkScript(this.searcher.getOffset()), (this.searcher.isOffsetHex() ? 16 : 10));
            int v3 = this.searcher.getType();
            String input = AddressItem.getStringAddress(v1, v3) + 'h';
            String s1 = AddressItem.getStringAddress(v1 - v2, v3) + 'h';
            if(v2 > 0L) {
                input = String.valueOf(s1) + '~' + input;
            }
            else if(v2 < 0L) {
                input = String.valueOf(input) + '~' + s1;
            }
            SearchButtonListener.doSearch(input, v3, false, 0x20000000, this.searcher.getMem(0), this.searcher.getMem(1), which == -3 || (MainService.getLastFlags() & v3) == 0);
            MainService.instance.setTabIndex(1);
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

    @Override  // android.ext.SearchMenuItem
    public void onClick(View v) {
        if(v != null && v.getTag() instanceof Integer) {
            this.onClick(null, ((int)(((Integer)v.getTag()))));
            return;
        }
        this.show(null);
    }

    @Override  // android.ext.SearchMenuItem
    public void onClickCallback(DialogInterface d, int which) {
    }

    @Override  // android.ext.SearchMenuItem
    public void onDismiss(DialogInterface dialog) {
        if(this.searcher != null) {
            PointerButtonListener.useHex = this.searcher.isOffsetHex();
            PointerButtonListener.lastOffset = this.searcher.getOffset();
            this.lastInput = this.searcher.getNumber();
        }
        super.onDismiss(dialog);
    }

    public void show(String addrString) {
        boolean z = true;
        if(!MainService.instance.mDaemonManager.isDaemonRun()) {
            return;
        }
        MainService service = MainService.instance;
        int v = this.getFlags();
        if(this.searcher == null) {
            this.searcher = new Searcher(4, v);
        }
        this.searcher.setFocusValue(addrString == null);
        Searcher searcher0 = this.searcher;
        if(addrString == null) {
            addrString = this.lastInput;
        }
        searcher0.setNumber(addrString);
        this.searcher.setMessage(Re.s(0x7F0701FE));  // string:mask_request "Enter an address to search for"
        this.searcher.setOffset("0");
        this.searcher.setOffsetHex(PointerButtonListener.useHex);
        Searcher searcher1 = this.searcher;
        if(v == 4) {
            z = false;
        }
        searcher1.setType(AddressItem.getDataForSearch(v, z), v);
        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(this.searcher.getViewForAttach()).setNegativeButton(Re.s(0x7F0700A1), null);  // string:cancel "Cancel"
        if(service.mResultCount != 0L && (MainService.getLastFlags() & v) != 0) {
            alertDialog$Builder0.setPositiveButton(Re.s(0x7F0701A0), this).setNeutralButton(Re.s(0x7F0701A3), this);  // string:refine "Refine"
        }
        else {
            alertDialog$Builder0.setPositiveButton(Re.s(0x7F0701A3), this);  // string:new_search "New search"
        }
        AlertDialog alertDialog0 = alertDialog$Builder0.create();
        Alert.setOnDismissListener(alertDialog0, this);
        Alert.setOnShowListener(alertDialog0, this);
        this.weakDialog = new WeakReference(alertDialog0);
        Alert.show(alertDialog0, this.searcher.getNumberEdit());
    }
}

