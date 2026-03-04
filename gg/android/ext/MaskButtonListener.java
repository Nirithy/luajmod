package android.ext;

import android.content.DialogInterface;
import android.view.View.OnClickListener;

public class MaskButtonListener extends SearchMenuItem implements View.OnClickListener {
    public MaskButtonListener() {
        super(0x7F0701FF, 0x7F020035);  // string:help_mask_search_title "Address (mask) search"
    }

    public static boolean doSearch(byte seq, String input, long mask, int flags, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        int v5 = flags & 0x7F;
        MainService service = MainService.instance;
        if(service.mResultCount != 0L && !forceNew) {
            v5 &= MainService.getLastFlags() & 0x7F;
        }
        if(v5 == 0) {
            service.clear(seq);
            return true;
        }
        long[] arr_v = Searcher.parseMask(input, mask);
        if(!service.showBusyDialog()) {
            throw new NumberFormatException(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
        }
        if(service.mResultCount != 0L && forceNew) {
            service.clear(seq);
        }
        if(service.mResultCount == 0L) {
            service.usedFuzzy = false;
            service.mDaemonManager.sendConfig(seq);
        }
        service.lockApp(seq);
        service.showSearchHint = false;
        service.mDaemonManager.searchMask(seq, arr_v[0], arr_v[1], v5 | sign, memoryFrom, memoryTo);
        MainService.setLastFlags(v5, seq);
        return false;
    }

    public static boolean doSearch(String input, long mask, int flags, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        String s1 = MaskButtonListener.checkScript(input);
        boolean z1 = MaskButtonListener.doSearch(((byte)0), s1, mask, flags, sign, memoryFrom, memoryTo, forceNew);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            if(forceNew) {
                record.write("\ngg.clearResults()\n");
            }
            if(!forceNew && MainService.instance.mResultCount != 0L) {
                record.write("gg.refineAddress(");
            }
            else {
                record.write("gg.searchAddress(");
            }
            Consts.logNumberString(record, s1);
            record.write(", ");
            Consts.logHex(record, mask);
            record.write(", ");
            Consts.logConst(record, record.consts.TYPE_, flags);
            record.write(", ");
            Consts.logConst(record, record.consts.SIGN_, sign);
            record.write(", ");
            Consts.logHex(record, memoryFrom);
            record.write(", ");
            Consts.logHex(record, memoryTo);
            record.write(")\n");
        }
        return z1;
    }

    @Override  // android.ext.SearchMenuItem
    public void onClickCallback(DialogInterface d, int which) {
        boolean z = true;
        String s = this.searcher.getNumber();
        long v1 = this.searcher.getDirectMask();
        int v2 = this.searcher.getSign();
        long v3 = this.searcher.getMem(0);
        long v4 = this.searcher.getMem(1);
        if(this.lastButton != -3) {
            z = false;
        }
        MaskButtonListener.doSearch(s, v1, which, v2, v3, v4, z);
    }

    @Override  // android.ext.SearchMenuItem
    public void onDismiss(DialogInterface dialog) {
        this.lastInput = this.searcher.getNumber();
        this.lastMask = this.searcher.getMask();
        MainService.lastType = this.searcher.getType();
        this.signLastSelect = this.searcher.getSign();
        super.onDismiss(dialog);
    }
}

