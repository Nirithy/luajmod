package android.ext;

import android.content.DialogInterface;
import android.view.View.OnClickListener;
import java.util.Arrays;

public class SearchButtonListener extends SearchMenuItem implements View.OnClickListener {
    static class GroupSearch {
        int area;
        int[] flags;
        long[] from;
        long[] to;

        public GroupSearch(int size) {
            this.from = new long[size];
            this.to = new long[size];
            this.flags = new int[size];
            this.area = 2;
        }
    }

    public static class RangeResult {
        public long from;
        public int sign;
        public long to;
        public int type;

        public RangeResult() {
            this.from = 0L;
            this.to = 0L;
            this.type = 0;
            this.sign = 0x20000000;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder0 = new StringBuilder(super.toString()).append(": ").append(this.from);
            return this.sign == 0x20000000 ? stringBuilder0.append("~").append(this.to).append(' ').append(this.type).toString() : stringBuilder0.append("~~").append(this.to).append(' ').append(this.type).toString();
        }
    }

    public static class XorMode {
        public String input;
        public int x;

    }

    private static final int AREA_MAX = 0x10000;
    private static final int AREA_MIN = 2;
    public static final int GROUP_MAX = 0x20;
    public static String lastTextSearch;

    static {
        SearchButtonListener.lastTextSearch = null;
    }

    public SearchButtonListener() {
        super(0x7F070102, 0x7F020034);  // string:search_known_value "Known (exact) search"
    }

    private static boolean doGroupSearch(byte seq, String input, int defaultType, boolean ordered, boolean valueEncrypted, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        GroupSearch searchButtonListener$GroupSearch0 = SearchButtonListener.parseGroupSearch(input.trim(), defaultType, ordered);
        String msg = valueEncrypted ? "" + Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F07012F), Re.s(0x7F070117)}) + '\n' : "";  // string:does_not_support "\"__s__\" does not support \"__s__\"."
        if(sign != 0x20000000) {
            msg = msg + Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F07012F), AddressItem.getSignNames().get(sign)}) + '\n';  // string:does_not_support "\"__s__\" does not support \"__s__\"."
        }
        if(msg.length() > 0) {
            throw new NumberFormatException(msg);
        }
        return SearchButtonListener.startGroupSearch(seq, searchButtonListener$GroupSearch0.from, searchButtonListener$GroupSearch0.to, searchButtonListener$GroupSearch0.flags, searchButtonListener$GroupSearch0.area, (ordered ? 0x400000 : 0), memoryFrom, memoryTo, forceNew);
    }

    private static boolean doRangeSearch(byte seq, String input, int defaultType, boolean valueEncrypted, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        String s1 = input.trim();
        XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(s1, true);
        int x = searchButtonListener$XorMode0 == null ? 0 : searchButtonListener$XorMode0.x;
        if(searchButtonListener$XorMode0 != null) {
            s1 = searchButtonListener$XorMode0.input;
        }
        RangeResult searchButtonListener$RangeResult0 = SearchButtonListener.parseRange(null, s1, defaultType, sign);
        String msg = valueEncrypted ? "" + Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F07018B), Re.s(0x7F070117)}) + '\n' : "";  // string:does_not_support "\"__s__\" does not support \"__s__\"."
        if(sign != 0x10000000 && sign != 0x20000000) {
            msg = msg + Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F07018B), AddressItem.getSignNames().get(sign)}) + '\n';  // string:does_not_support "\"__s__\" does not support \"__s__\"."
        }
        if(msg.length() > 0) {
            throw new NumberFormatException(msg);
        }
        if(x != 0) {
            searchButtonListener$RangeResult0.type |= 0x200000;
        }
        return SearchButtonListener.startRangeSearch(seq, searchButtonListener$RangeResult0.from, searchButtonListener$RangeResult0.to, x, searchButtonListener$RangeResult0.sign | searchButtonListener$RangeResult0.type, memoryFrom, memoryTo, forceNew);
    }

    public static boolean doSearch(byte seq, int flags, byte[] text, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        int v3 = flags & 0x7F;
        MainService service = MainService.instance;
        if(service.mResultCount != 0L && !forceNew) {
            v3 &= MainService.getLastFlags() & 0x7F;
        }
        if(v3 == 0) {
            service.clear(seq);
            return true;
        }
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
        service.mDaemonManager.searchText(seq, v3, text, memoryFrom, memoryTo);
        MainService.setLastFlags(v3, seq);
        return false;
    }

    public static boolean doSearch(byte seq, String input, int flags, boolean valueEncrypted, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        int v4 = flags & 0x7F;
        int v5 = sign & 0x3C000000;
        MainService service = MainService.instance;
        if(ParserNumbers.isString(input)) {
            boolean z2 = SearchButtonListener.doSearch(seq, (input.charAt(0) == 59 ? 2 : 1), ParserNumbers.getBytes(input), memoryFrom, memoryTo, forceNew);
            SearchButtonListener.lastTextSearch = input;
            return z2;
        }
        if(input.indexOf(59) != -1) {
            return SearchButtonListener.doGroupSearch(seq, input, v4, input.contains("::"), valueEncrypted, v5, memoryFrom, memoryTo, forceNew);
        }
        if(input.indexOf(0x7E) != -1 && ParserNumbers.typeAsm(input) == 0) {
            return SearchButtonListener.doRangeSearch(seq, input, v4, valueEncrypted, v5, memoryFrom, memoryTo, forceNew);
        }
        if(service.mResultCount != 0L && !forceNew) {
            v4 &= MainService.getLastFlags() & 0x7F;
        }
        if(v4 == 0) {
            service.clear(seq);
            return true;
        }
        XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(input, true);
        int x = searchButtonListener$XorMode0 == null ? 0 : searchButtonListener$XorMode0.x;
        if(searchButtonListener$XorMode0 != null) {
            input = searchButtonListener$XorMode0.input;
        }
        Result result = new Result();
        long v7 = AddressItem.dataForSearch(result, input, v4);
        int v8 = v4 & result.type;
        if(Integer.bitCount(v8) > 1) {
            v8 = AddressItem.fixAutoFlag(v8, v7, result.isNegative);
        }
        else if(!valueEncrypted && (v8 != 16 && v8 != 0x40) && (v5 == 0x4000000 || v5 == 0x8000000) && input.indexOf(45) == -1 && (1L << AddressItem.getSize(v8) * 8 - 1 & v7) != 0L) {
            Log.d(("unsigned search 1: \'" + input + "\' " + Integer.toHexString(v8) + ' ' + false + ' ' + Integer.toHexString(v5)));
            if(v5 == 0x8000000) {
                input = input + "~0";
                v5 = 0x10000000;
            }
            else if(v5 == 0x4000000) {
                input = input + "~-1";
                v5 = 0x20000000;
            }
            Log.d(("unsigned search 2: \'" + input + "\' " + Integer.toHexString(v8) + ' ' + false + ' ' + Integer.toHexString(v5)));
            return SearchButtonListener.doSearch(seq, input, v8, ((byte)false), v5, memoryFrom, memoryTo, forceNew);
        }
        int v9 = v8 | (!valueEncrypted || x != 0 ? v5 : 0x2000000);
        if(x != 0) {
            if(valueEncrypted) {
                throw new NumberFormatException(Tools.stringFormat(Re.s(0x7F07018D), new Object[]{Re.s(0x7F070225), Re.s(0x7F070117)}));  // string:does_not_support "\"__s__\" does not support \"__s__\"."
            }
            v9 |= 0x200000;
        }
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
        service.mDaemonManager.searchNumber(seq, v7, x, v9, memoryFrom, memoryTo);
        MainService.setLastFlags(v8, seq);
        return false;
    }

    public static boolean doSearch(byte seq, short maxOffset, long memoryFrom, long memoryTo) throws NumberFormatException {
        MainService service = MainService.instance;
        if(service.mResultCount == 0L) {
            service.clear(seq);
            return true;
        }
        if(!service.showBusyDialog()) {
            throw new NumberFormatException(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
        }
        service.lockApp(seq);
        service.showSearchHint = false;
        int flags = service.processInfo == null || !service.processInfo.x64 ? 4 : 0x20;
        service.mDaemonManager.searchPointer(seq, maxOffset, flags, memoryFrom, memoryTo);
        MainService.setLastFlags(flags, seq);
        return false;
    }

    public static boolean doSearch(byte b, short v, long v1, long v2, long v3) {
        MainService mainService0 = MainService.instance;
        if(mainService0.mResultCount == 0L) {
            mainService0.clear(b);
            return true;
        }
        if(!mainService0.showBusyDialog()) {
            throw new NumberFormatException(Re.s(0x7F070222));  // string:speedhack_loaded "Speedhack is loaded."
        }
        mainService0.mDaemonManager.sendConfig(b);
        mainService0.lockApp(b);
        mainService0.usedFuzzy = false;
        mainService0.showSearchHint = false;
        int v4 = mainService0.processInfo == null || !mainService0.processInfo.x64 ? 4 : 0x20;
        mainService0.mDaemonManager.searchPointer(b, v, v4, v1, v2, v3);
        MainService.setLastFlags(v4, b);
        return false;
    }

    public static boolean doSearch(String input, int flags, boolean valueEncrypted, int sign, long memoryFrom, long memoryTo, boolean forceNew) throws NumberFormatException {
        String s1 = SearchButtonListener.checkScript(input);
        boolean z2 = SearchButtonListener.doSearch(((byte)0), s1, flags, valueEncrypted, sign, memoryFrom, memoryTo, forceNew);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            if(forceNew) {
                record.write("\ngg.clearResults()\n");
            }
            if(!forceNew && MainService.instance.mResultCount != 0L) {
                record.write("gg.refineNumber(");
            }
            else {
                record.write("gg.searchNumber(");
            }
            Consts.logNumberString(record, s1);
            record.write(", ");
            Consts.logConst(record, record.consts.TYPE_, flags);
            record.write(", ");
            record.write(Boolean.toString(valueEncrypted));
            record.write(", ");
            Consts.logConst(record, record.consts.SIGN_, sign);
            record.write(", ");
            Consts.logHex(record, memoryFrom);
            record.write(", ");
            Consts.logHex(record, memoryTo);
            record.write(")\n");
        }
        return z2;
    }

    private static void fixUnsigned(RangeResult ret, String input) {
        int v = 0x20000000;
        int flags = ret.type & 0x7F;
        long signMask = 1L << AddressItem.getSize(flags) * 8 - 1;
        if(Integer.bitCount(flags) == 1 && (flags != 16 && flags != 0x40) && input.indexOf(45) == -1 && (ret.from & signMask) == 0L && (ret.to & signMask) != 0L) {
            Log.d(("unsigned search 3: \'" + input + "\' " + ret.from + ' ' + ret.to + ' ' + Integer.toHexString(flags) + ' ' + Integer.toHexString(ret.sign)));
            long t = ret.to;
            ret.to = ret.from;
            ret.from = t;
            if(ret.sign == 0x20000000) {
                v = 0x10000000;
            }
            ret.sign = v;
            Log.d(("unsigned search 4: \'" + input + "\' " + ret.from + ' ' + ret.to + ' ' + Integer.toHexString(flags) + ' ' + Integer.toHexString(ret.sign)));
        }
    }

    public static int getMinGroupSize(String input, int defaultType, boolean ordered) {
        try {
            GroupSearch searchButtonListener$GroupSearch0 = SearchButtonListener.parseGroupSearch(input, defaultType, ordered);
            int cnt = 0;
            int s = searchButtonListener$GroupSearch0.flags.length - 1;
            for(int i = 0; true; ++i) {
                if(i >= s) {
                    return cnt + 1;
                }
                cnt += AddressItem.getSize(searchButtonListener$GroupSearch0.flags[i]);
            }
        }
        catch(NumberFormatException unused_ex) {
            return 2;
        }
    }

    @Override  // android.ext.SearchMenuItem
    public void onClickCallback(DialogInterface d, int which) {
        boolean z = true;
        String s = this.searcher.getNumber();
        boolean z1 = this.searcher.isModeHacking();
        int v1 = this.searcher.getSign();
        long v2 = this.searcher.getMem(0);
        long v3 = this.searcher.getMem(1);
        if(this.lastButton != -3) {
            z = false;
        }
        SearchButtonListener.doSearch(s, which, z1, v1, v2, v3, z);
    }

    @Override  // android.ext.SearchMenuItem
    public void onDismiss(DialogInterface dialog) {
        this.lastInput = this.searcher.getNumber();
        this.signLastSelect = this.searcher.getSign();
        MainService.lastType = this.searcher.getType();
        super.onDismiss(dialog);
    }

    public void openSearch(String text) {
        if(text == null) {
            text = "";
        }
        this.lastInput = text;
        this.getButton().performClick();
    }

    private static GroupSearch parseGroupSearch(String input, int defaultType, boolean ordered) throws NumberFormatException {
        String[] arr_s = input.trim().split((ordered ? "::" : ":"));
        if(arr_s.length > 2) {
            throw new NumberFormatException(Re.s(0x7F07018A) + " \'" + (ordered ? "::" : ":") + '\'');  // string:too_many "Too many"
        }
        long v1 = arr_s.length == 1 ? 0x200L : ParserNumbers.parseLong(arr_s[1]).value;
        if(((int)v1) < 2 || ((int)v1) > 0x10000) {
            NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_(Tools.stringFormat(Re.s(0x7F07011D), new Object[]{2, 0x10000}) + ' ' + ((int)v1));  // string:invalid_area "Group size must be between __d__ and __d__:"
            parserNumbers$NumberFormatException_0.addNumber(0x10000L);
            throw parserNumbers$NumberFormatException_0;
        }
        String[] parts = arr_s[0].split(";");
        if(parts.length < 2) {
            throw new NumberFormatException(Re.s(0x7F07011E));  // string:union_need_two_numbers "For group search there must be at least two numbers"
        }
        if(parts.length > 0x20) {
            throw new NumberFormatException(Tools.stripColon(0x7F07011F) + ": " + 0x20);  // string:too_many_union_numbers "Too many numbers, more than:"
        }
        GroupSearch searchButtonListener$GroupSearch0 = new GroupSearch(parts.length);
        Result res = null;
        for(int i = 0; i < parts.length; ++i) {
            if(parts[i].indexOf(0x7E) == -1) {
                res = ParserNumbers.parse(res, parts[i], defaultType, false);
                searchButtonListener$GroupSearch0.from[i] = res.value;
                searchButtonListener$GroupSearch0.to[i] = 0L;
                searchButtonListener$GroupSearch0.flags[i] = res.type;
            }
            else {
                RangeResult searchButtonListener$RangeResult0 = SearchButtonListener.parseRange(res, parts[i], defaultType, 0x20000000);
                searchButtonListener$GroupSearch0.from[i] = searchButtonListener$RangeResult0.from;
                searchButtonListener$GroupSearch0.to[i] = searchButtonListener$RangeResult0.to;
                searchButtonListener$GroupSearch0.flags[i] = searchButtonListener$RangeResult0.type | searchButtonListener$RangeResult0.sign;
            }
        }
        searchButtonListener$GroupSearch0.area = (int)v1;
        return searchButtonListener$GroupSearch0;
    }

    public static RangeResult parseRange(Result result, String input, int defaultType, int sign) throws NumberFormatException {
        String s1 = input.trim();
        String separator = "~~";
        if(!s1.contains("~~")) {
            separator = "~";
        }
        else if(sign == 0x20000000) {
            sign = 0x10000000;
        }
        else {
            sign = 0x20000000;
        }
        String[] parts = s1.split(separator);
        if(parts.length > 2) {
            throw new NumberFormatException(Re.s(0x7F07018A) + " \'" + separator + '\'');  // string:too_many "Too many"
        }
        if(parts.length != 2) {
            parts = (String[])Arrays.copyOf(parts, 2);
        }
        long[] numbers = new long[2];
        int[] flags = new int[2];
        for(int i = 1; i >= 0; --i) {
            String pi = parts[i];
            String pi = pi == null ? "" : pi.trim();
            result = ParserNumbers.parse(result, pi, (i == 1 ? defaultType : flags[1]), false);
            if(defaultType == 0x7F && result.type == 0x40 && "BWDXFQEAbwdxfqea".indexOf(pi.charAt(pi.length() - 1)) == -1) {
                result.type |= 16;
            }
            numbers[i] = result.value;
            flags[i] = result.type;
        }
        int usedFlags = flags[0] & flags[1];
        String msg = usedFlags == 0 ? "" + Re.s(0x7F07018E) + '\n' : "";  // string:range_bounds_diff_types "Range bounds must be the same type."
        if(msg.length() > 0) {
            throw new NumberFormatException(msg);
        }
        if(defaultType == 0x7F && (usedFlags & 0x20) == 0) {
            for(int i = 0; i < 2; ++i) {
                if((flags[i] & 0x20) != 0) {
                    numbers[i] = Double.doubleToRawLongBits(numbers[i]);
                }
            }
        }
        RangeResult ret = new RangeResult();
        ret.from = numbers[0];
        ret.to = numbers[1];
        ret.type = usedFlags;
        ret.sign = sign;
        SearchButtonListener.fixUnsigned(ret, s1);
        return ret;
    }

    public static XorMode parseXorMode(String input, boolean forSearch) throws NumberFormatException {
        XorMode ret = null;
        if(!ParserNumbers.isString(input) && ParserNumbers.typeAsm(input) == 0 && (input.indexOf(88) != -1 || input.indexOf(120) != -1)) {
            ret = new XorMode();
            String[] arr_s = input.split("[Xx]");
            String strX = arr_s.length == 2 ? arr_s[1] : "";
            int x = (int)ParserNumbers.parse(null, strX, 2, false).value;
            ret.input = arr_s.length <= 0 ? "" : arr_s[0];
            if(forSearch && (x < 1 || x > 0x1000)) {
                NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_(Tools.stringFormat(Re.s(0x7F070122), new Object[]{strX}) + " [1; 4096]");  // string:number_out_of_range "Number \'__s__\' out of possible range:"
                parserNumbers$NumberFormatException_0.addNumber(strX);
                throw parserNumbers$NumberFormatException_0;
            }
            ret.x = x;
        }
        return ret;
    }

    public void searchNearby(String address) {
        this.getButton().performClick();
        MemoryRange memoryRange = this.searcher.memoryRange;
        memoryRange.setType(2);
        memoryRange.setAddress(address);
    }

    private static boolean startGroupSearch(byte seq, long[] from, long[] to, int[] flags, int area, int general_flags, long memoryFrom, long memoryTo, boolean forceNew) {
        MainService service = MainService.instance;
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
        service.lockApp();
        service.showSearchHint = false;
        service.mDaemonManager.searchGroup(seq, general_flags, area, flags, from, to, memoryFrom, memoryTo);
        int lastFlags = 0;
        for(int i = 0; i < flags.length; ++i) {
            lastFlags |= flags[i];
        }
        MainService.setLastFlags(lastFlags, seq);
        return false;
    }

    private static boolean startRangeSearch(byte seq, long number, long number2, int x, int flags, long memoryFrom, long memoryTo, boolean forceNew) {
        MainService service = MainService.instance;
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
        service.mDaemonManager.searchRangeNumber(seq, number, number2, x, flags, memoryFrom, memoryTo);
        MainService.setLastFlags(flags, seq);
        return false;
    }
}

