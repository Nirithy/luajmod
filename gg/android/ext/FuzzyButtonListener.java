package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.fix.SparseArray;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class FuzzyButtonListener extends SearchMenuItem implements View.OnClickListener {
    private SparseArray data;
    private long memoryFrom;
    private long memoryTo;

    public FuzzyButtonListener() {
        super(0x7F070101, 0x7F02002B);  // string:search_unknown_value "Unknown (fuzzy) search"
        this.memoryFrom = 0L;
        this.memoryTo = -1L;
    }

    public static boolean doRefine(byte seq, String input, int flags, int sign, int count, long memoryFrom, long memoryTo) throws NumberFormatException {
        int v9;
        long number;
        long number2 = 0L;
        int v6 = sign & 0x3C000000;
        MainService service = MainService.instance;
        int v7 = flags & 0x7F & (MainService.getLastFlags() & 0x7F);
        if(v7 == 0) {
            service.clear(seq);
            return true;
        }
        boolean range = input.indexOf(0x7E) != -1;
        Result res = new Result();
        if(range) {
            if(v6 != 0x10000000 && v6 != 0x20000000) {
                throw new NumberFormatException(Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F07018B), AddressItem.getSignNames().get(v6)}));  // string:does_not_support "\"__s__\" does not support \"__s__\"."
            }
            RangeResult searchButtonListener$RangeResult0 = SearchButtonListener.parseRange(res, input, v7, v6);
            number = searchButtonListener$RangeResult0.from;
            number2 = searchButtonListener$RangeResult0.to;
            v9 = searchButtonListener$RangeResult0.type;
            v6 = searchButtonListener$RangeResult0.sign;
        }
        else {
            number = AddressItem.dataForSearch(res, input, v7);
            v9 = v7 & res.type;
        }
        if(Integer.bitCount(v9) > 1) {
            v9 = AddressItem.fixAutoFlag(v9, number, res.isNegative);
            if(number2 != 0L) {
                v9 = AddressItem.fixAutoFlag(v9, number2, res.isNegative);
            }
        }
        if(!service.showBusyDialog()) {
            throw new NumberFormatException(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
        }
        service.lockApp(seq);
        service.showSearchHint = false;
        service.mDaemonManager.fuzzyRefine(seq, number, number2, (range ? v9 | 0x800000 : v9), v6, count, memoryFrom, memoryTo);
        MainService.setLastFlags(v9, seq);
        return false;
    }

    public static boolean doRefine(String input, int flags, int sign, int count, long memoryFrom, long memoryTo) throws NumberFormatException {
        String s1 = FuzzyButtonListener.checkScript(input);
        boolean z = FuzzyButtonListener.doRefine(((byte)0), s1, flags, sign, count, memoryFrom, memoryTo);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.searchFuzzy(");
            Consts.logString(record, s1);
            record.write(", ");
            Consts.logConst(record, record.consts.SIGN_FUZZY_, sign);
            record.write(", ");
            Consts.logConst(record, record.consts.TYPE_, flags);
            record.write(", ");
            Consts.logHex(record, memoryFrom);
            record.write(", ");
            Consts.logHex(record, memoryTo);
            record.write(")\n");
        }
        return z;
    }

    public static boolean doSearch(byte seq, int flags, long memoryFrom, long memoryTo) throws NumberFormatException {
        MainService service = MainService.instance;
        if((flags & 0x7F) == 0) {
            service.clear(seq);
            return true;
        }
        if(!service.showBusyDialog()) {
            throw new NumberFormatException(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
        }
        if(service.mResultCount != 0L) {
            service.clear(seq);
        }
        service.usedFuzzy = true;
        service.mDaemonManager.sendConfig(seq);
        service.lockApp(seq);
        service.showSearchHint = false;
        service.mDaemonManager.fuzzyStart(seq, flags & 0x7F, memoryFrom, memoryTo);
        MainService.setLastFlags(flags & 0x7F, seq);
        return false;
    }

    public static boolean doSearch(int flags, long memoryFrom, long memoryTo) throws NumberFormatException {
        boolean z = FuzzyButtonListener.doSearch(((byte)0), flags, memoryFrom, memoryTo);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("\ngg.startFuzzy(");
            Consts.logConst(record, record.consts.TYPE_, flags);
            record.write(", ");
            Consts.logHex(record, memoryFrom);
            record.write(", ");
            Consts.logHex(record, memoryTo);
            record.write(")\n");
        }
        return z;
    }

    private void multiEqual() {
        View view0 = LayoutInflater.inflateStatic(0x7F040020, null);  // layout:service_fuzzy_equal
        SeekBar countSeekbar = (SeekBar)view0.findViewById(0x7F0B011C);  // id:count_fuzzy_equal_seekbar
        countSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)view0.findViewById(0x7F0B011B)).setText(progress + 1);  // id:count_fuzzy_equal
            }

            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700CA)).setMessage(Re.s(0x7F0700C4)).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:fuzzy_equal "Value unchanged"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                try {
                    FuzzyButtonListener.doRefine("0", MainService.getLastFlags() & 0x7F, 0x20000000, countSeekbar.getProgress() + 1, FuzzyButtonListener.this.searcher.getMem(0), FuzzyButtonListener.this.searcher.getMem(1));
                }
                catch(NumberFormatException e) {
                    SearchMenuItem.inputError(e, FuzzyButtonListener.this.searcher.getNumberEdit());
                }
            }
        }).setNegativeButton(Re.s(0x7F0700A1), null).setView(view0));  // string:cancel "Cancel"
    }

    @Override  // android.ext.SearchMenuItem
    public void onClick(DialogInterface dialog, int which) {
        if(which == -1) {
            this.onClick(((AlertDialog)dialog).getButton(-1));
            return;
        }
        if(MainService.instance.mResultCount == 0L) {
            MainService.lastType = which;
            FuzzyButtonListener.doSearch(which, this.memoryFrom, this.memoryTo);
        }
    }

    @Override  // android.ext.SearchMenuItem, android.view.View$OnClickListener
    public void onClick(View v) {
        MainService service = MainService.instance;
        if(v == null || v.getTag() instanceof MenuItem) {
            new FuzzyButtonListener().show();
            return;
        }
        if(v.getTag() instanceof Integer && service.mResultCount == 0L) {
            try {
                this.memoryFrom = this.searcher.getMem(0);
                this.memoryTo = this.searcher.getMem(1);
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, null);
                return;
            }
            Tools.hideKeyboard(this.weakDialog);
            int v = this.searcher.getType();
            if(v == 0) {
                new TypeSelector(this.data, "0", Re.s(0x7F0700EE), this);  // string:type_request "Select the type of data to search"
            }
            else {
                FuzzyButtonListener.doSearch(v, this.memoryFrom, this.memoryTo);
            }
        }
        int sign = 0;
        switch(v.getId()) {
            case 0x7F0B013C: {  // id:equal_btn
                if(!Searcher.fuzzyExtended && !service.processPaused) {
                    this.multiEqual();
                }
                else {
                    sign = 0x20000000;
                }
                break;
            }
            case 0x7F0B013D: {  // id:not_equal_btn
                sign = 0x10000000;
                break;
            }
            case 0x7F0B013E: {  // id:larger_btn
                sign = 0x4000000;
                break;
            }
            case 0x7F0B013F: {  // id:smaller_btn
                sign = 0x8000000;
            }
        }
        if(sign != 0) {
            try {
                int flags = this.searcher.getType();
                if(flags == 0 || !Searcher.fuzzyExtended) {
                    flags = MainService.getLastFlags() & 0x7F;
                }
                FuzzyButtonListener.doRefine(this.searcher.getNumber(), flags, sign, 1, this.searcher.getMem(0), this.searcher.getMem(1));
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, this.searcher.getNumberEdit());
                return;
            }
        }
        Tools.hideKeyboard(this.weakDialog);
        Tools.dismiss(this.weakDialog);
    }

    @Override  // android.ext.SearchMenuItem
    public void onClickCallback(DialogInterface d, int which) {
    }

    @Override  // android.ext.SearchMenuItem
    public void onDismiss(DialogInterface dialog) {
        MainService.lastType = this.searcher.getType();
        super.onDismiss(dialog);
    }

    private void show() {
        MainService service = MainService.instance;
        if(!service.mDaemonManager.isDaemonRun()) {
            return;
        }
        int v = MainService.getLastFlags();
        if(service.mResultCount == 0L) {
            this.searcher = new Searcher(1, v);
            this.data = this.searcher.setTypeInternal(v);
            this.searcher.setMessage(Re.s(0x7F0700EE));  // string:type_request "Select the type of data to search"
            AlertDialog alertDialog0 = Alert.create().setView(this.searcher.getViewForAttach()).setPositiveButton(Re.s(0x7F07008B), this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:search "Search"
            Alert.setOnDismissListener(alertDialog0, this);
            Alert.setOnShowListener(alertDialog0, this);
            this.weakDialog = new WeakReference(alertDialog0);
            Alert.show(alertDialog0);
            return;
        }
        this.searcher = new Searcher(2, v);
        this.data = this.searcher.setTypeInternal(v);
        this.searcher.setMessage(Re.s(0x7F070097));  // string:compare_current_last "How has the value changed?"
        this.searcher.setOnCancelClickListener(this);
        View view0 = this.searcher.getViewForAttach();
        ((Button)view0.findViewById(0x7F0B013C)).setOnClickListener(this);  // id:equal_btn
        ((Button)view0.findViewById(0x7F0B013D)).setOnClickListener(this);  // id:not_equal_btn
        ((Button)view0.findViewById(0x7F0B013E)).setOnClickListener(this);  // id:larger_btn
        ((Button)view0.findViewById(0x7F0B013F)).setOnClickListener(this);  // id:smaller_btn
        AlertDialog alertDialog1 = Alert.create().setView(view0).create();
        Alert.setOnDismissListener(alertDialog1, this);
        Alert.setOnShowListener(alertDialog1, this);
        this.weakDialog = new WeakReference(alertDialog1);
        Alert.show(alertDialog1, this.searcher.getNumberEdit());
    }
}

