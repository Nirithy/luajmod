package android.ext;

import android.fix.SparseArray;
import android.util.SparseIntArray;

public class AddressItem {
    public static class Pair {
        public int count;
        int flags;
        CharSequence name;

        public Pair(int flags, CharSequence name, int count) {
            this.flags = flags;
            this.name = name;
            this.count = count;
        }

        @Override
        public String toString() {
            return this.count <= 0 ? this.name.toString() : this.count + ": " + this.name.toString();
        }
    }

    public static class Steps {
        double dl;
        float fl;
        long val;

    }

    public static final long[] DATA_MASK = null;
    public static final int FLAG_ALL_OTHERS = 0xFFFFFF80;
    public static final int FLAG_ALTER_ADD = 0x40000000;
    public static final int FLAG_AUTO = 0x7F;
    public static final int FLAG_FUZZY = 0x80000000;
    public static final int FLAG_MODE_HACKING = 0x2000000;
    public static final int FLAG_MODE_XOR = 0x200000;
    public static final int FLAG_ORDERED = 0x400000;
    public static final int FLAG_RANGE = 0x800000;
    public static final int FLAG_REVERT = 0x1000000;
    public static final int FLAG_SIGN = 0x3C000000;
    public static final int FLAG_VALUE_EQUAL = 0x20000000;
    public static final int FLAG_VALUE_GREATER_OR_EQUAL = 0x4000000;
    public static final int FLAG_VALUE_LESS_OR_EQUAL = 0x8000000;
    public static final int FLAG_VALUE_NOT_EQUAL = 0x10000000;
    public static final int TYPE_BYTE = 1;
    public static final int TYPE_COUNT = 7;
    public static final int TYPE_DOUBLE = 0x40;
    public static final int TYPE_DWORD = 4;
    public static final int TYPE_FLOAT = 16;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_QWORD = 0x20;
    public static final int TYPE_WORD = 2;
    public static final int TYPE_XOR = 8;
    public static final int USED_TYPE_COUNT = 0x80;
    public long address;
    private static int colorDefault;
    private static SparseIntArray colors;
    public long data;
    public int flags;
    private static SparseArray names;
    private static SparseArray namesShort;
    private static final int[] order;
    private static SparseArray signNames;
    private static SparseArray signNamesSmall;

    static {
        AddressItem.DATA_MASK = new long[]{0L, 0xFFL, 0xFFFFL, 0xFFFFFFL, 0xFFFFFFFFL, 0xFFFFFFFFFFL, 0xFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFL, -1L};
        AddressItem.names = null;
        AddressItem.namesShort = null;
        AddressItem.signNames = null;
        AddressItem.signNamesSmall = null;
        AddressItem.colors = null;
        AddressItem.colorDefault = 0xFFFFFF;
        Object[] arr_object = ParserNumbers.getRanges();
        SparseArray names_ = new SparseArray();
        SparseArray namesShort_ = new SparseArray();
        AddressItem.initName(names_, namesShort_, 1, "B: %s (%,d - %,d)", 0x7F070007, arr_object[0], arr_object[1]);  // string:type_byte "Byte"
        AddressItem.initName(names_, namesShort_, 2, "W: %s (%,d - %,d)", 0x7F070008, arr_object[2], arr_object[3]);  // string:type_word "Word"
        AddressItem.initName(names_, namesShort_, 4, "D: %s (%,d - %,d)", 0x7F070009, arr_object[4], arr_object[5]);  // string:type_dword "Dword"
        AddressItem.initName(names_, namesShort_, 8, "X: %s (%,d - %,d)", 0x7F07000A, arr_object[4], arr_object[5]);  // string:type_xor "Xor"
        AddressItem.initName(names_, namesShort_, 16, "F: %s (%.1e - %.1e)", 0x7F07000B, -3.402823E+38f, 3.402823E+38f);  // string:type_float "Float"
        AddressItem.initName(names_, namesShort_, 0x20, "Q: %s (%,d - %,d)", 0x7F07000C, arr_object[6], arr_object[7]);  // string:type_qword "Qword"
        AddressItem.initName(names_, namesShort_, 0x40, "E: %s (%.1e - %.1e)", 0x7F07000D, -1.797693E+308, 1.797693E+308);  // string:type_double "Double"
        AddressItem.namesShort = namesShort_;
        AddressItem.names = names_;
        AddressItem.updateLocale();
        SparseArray signNames_ = new SparseArray();
        signNames_.append(0x20000000, "=");
        signNames_.append(0x4000000, "≥");
        signNames_.append(0x8000000, "≤");
        signNames_.append(0x10000000, "≠");
        AddressItem.signNames = signNames_;
        SparseArray signNamesSmall_ = new SparseArray();
        signNamesSmall_.append(0x20000000, "=");
        signNamesSmall_.append(0x10000000, "≠");
        AddressItem.signNamesSmall = signNamesSmall_;
        AppLocale.registerClass(AddressItem.class);
        AddressItem.order = new int[]{8, 0x20, 1, 2, 0x40, 16, 4};
    }

    protected AddressItem() {
    }

    public AddressItem(long address, long data, int flags) {
        this.address = address;
        this.data = data;
        this.flags = flags;
    }

    public AddressItem(AddressItem item) {
        this(item.address, item.data, item.flags);
    }

    public void alter() {
        this.alter(0);
    }

    public void alter(int x) {
        MainService.instance.mDaemonManager.addForAlter(this, x);
    }

    public AddressItem copy() {
        return new AddressItem(this);
    }

    public static long dataForSearch(Result result, String data, int flags) throws NumberFormatException {
        return AddressItem.parseString(result, data, flags & 0x7F, 0x7F0700CF, 0L).value;  // string:number_name "number"
    }

    public static long dataFromString(long address, String data, int flags) throws NumberFormatException {
        long result = AddressItem.parseString(null, data, flags & 0x7F, 0x7F0700CF, address).value;  // string:number_name "number"
        return (flags & 0x7F) == 8 ? result ^ address : result;
    }

    public Steps dataFromString(Steps steps, Result result, String data, long addr, String step, int stepCount) throws NumberFormatException {
        if(result == null) {
            result = AddressItem.parseString(null, data, this.flags, 0x7F0700CF, addr);  // string:number_name "number"
        }
        long value = result.value;
        if(stepCount != 0) {
            if(steps == null) {
                Result parserNumbers$Result1 = AddressItem.parseString(null, step, this.flags, 0x7F0700D0, 0L);  // string:step_name "increment"
                steps = new Steps();
                steps.fl = Float.intBitsToFloat(((int)parserNumbers$Result1.value));
                steps.dl = Double.longBitsToDouble(parserNumbers$Result1.value);
                steps.val = parserNumbers$Result1.value;
            }
            switch(result.type) {
                case 16: {
                    value = (long)Float.floatToRawIntBits(Float.intBitsToFloat(((int)value)) + steps.fl * ((float)stepCount));
                    break;
                }
                case 0x40: {
                    value = Double.doubleToRawLongBits(Double.longBitsToDouble(value) + steps.dl * ((double)stepCount));
                    break;
                }
                default: {
                    value += steps.val * ((long)stepCount);
                }
            }
        }
        if(this.flags == 8) {
            value ^= this.address;
        }
        this.data = value;
        return steps;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof AddressItem)) {
            return false;
        }
        return this.address == ((AddressItem)obj).address ? ((this.flags ^ ((AddressItem)obj).flags) & 0x7F) == 0 : false;
    }

    public static int fixAutoFlag(int flags, long number, boolean negative) {
        int excludeFlags = 0;
        if(flags == 0x40 || flags == 80) {
            return flags;
        }
        if(negative) {
            if(number >> 7 != -1L) {
                excludeFlags = 1;
            }
            if(number >> 15 != -1L) {
                excludeFlags |= 2;
            }
            if(number >> 0x1F != -1L) {
                excludeFlags |= 12;
            }
            return flags & ~excludeFlags;
        }
        if(number >> 8 != 0L) {
            excludeFlags = 1;
        }
        if(number >> 16 != 0L) {
            excludeFlags |= 2;
        }
        return number >> 0x20 == 0L ? flags & ~excludeFlags : flags & ~(excludeFlags | 12);
    }

    public static long fixValue(long value, int flags) {
        long old = value;
        switch(flags & 0x7F) {
            case 1: {
                value = (long)(((byte)(((int)value))));
                break;
            }
            case 2: {
                value = (long)(((short)(((int)value))));
                break;
            }
            case 4: 
            case 8: {
                value = (long)(((int)value));
            }
        }
        if(old != value) {
            Log.d(("fixValue[" + flags + ", " + Integer.toHexString(flags) + "]: " + old + '[' + Long.toHexString(old) + "] -> " + value + '[' + Long.toHexString(value) + ']'));
        }
        return value;
    }

    public static long getAddress(long address, int flags) [...] // Inlined contents

    public long getAddress() {
        return this.address;
    }

    public static int getAlign(int flags) {
        if((flags & 0x60) == 0 && (flags & 28) == 0) {
            if((flags & 2) != 0) {
                return 2;
            }
            return (flags & 1) == 0 ? 4 : 1;
        }
        return 4;
    }

    public int getAlign() {
        return AddressItem.getAlign(this.flags);
    }

    public static int getColor(int flags) {
        try {
            SparseIntArray cl = AddressItem.colors;
            if(cl == null) {
                cl = new SparseIntArray(8);
                cl.append(1, Tools.getColor(0x7F0A0005));  // color:type_byte
                cl.append(2, Tools.getColor(0x7F0A0006));  // color:type_word
                cl.append(4, Tools.getColor(0x7F0A0007));  // color:type_dword
                cl.append(8, Tools.getColor(0x7F0A0009));  // color:type_xor
                cl.append(16, Tools.getColor(0x7F0A000A));  // color:type_float
                cl.append(0x20, Tools.getColor(0x7F0A0008));  // color:type_qword
                cl.append(0x40, Tools.getColor(0x7F0A000B));  // color:type_double
                AddressItem.colors = cl;
                AddressItem.colorDefault = Tools.getColor(0x7F0A0004);  // color:type_unknown
            }
            int out = cl.get(AddressItem.getType(flags));
            return out == 0 ? AddressItem.colorDefault : out;
        }
        catch(Throwable e) {
            Log.e(("Failed getColor for: " + flags), e);
            return AddressItem.colorDefault;
        }
    }

    public int getColor() {
        return AddressItem.getColor(this.flags);
    }

    public static SparseArray getDataForEdit(int flags) {
        SparseArray list = new SparseArray();
        switch(flags) {
            case 4: 
            case 8: 
            case 16: {
                list.append(4, ((CharSequence)AddressItem.names.get(4)));
                list.append(16, ((CharSequence)AddressItem.names.get(16)));
                list.append(8, ((CharSequence)AddressItem.names.get(8)));
                return list;
            }
            case 0x20: 
            case 0x40: {
                list.append(0x20, ((CharSequence)AddressItem.names.get(0x20)));
                list.append(0x40, ((CharSequence)AddressItem.names.get(0x40)));
                return list;
            }
            default: {
                if((flags & 2) != 0) {
                    list.append(flags & 2, ((CharSequence)AddressItem.names.get(2)));
                    return list;
                }
                if((flags & 1) != 0) {
                    list.append(flags & 1, ((CharSequence)AddressItem.names.get(1)));
                    return list;
                }
                Log.d(("Unknown flags: " + flags + ' ' + Integer.toBinaryString(flags)));
                return list;
            }
        }
    }

    public SparseArray getDataForEdit() {
        return AddressItem.getDataForEdit(this.flags);
    }

    public static SparseArray getDataForEditAll(int flags) {
        return AddressItem.getDataForEditAll(flags, 0L);
    }

    public static SparseArray getDataForEditAll(int flags, long addr) {
        SparseArray sparseArray0 = AddressItem.getDataForSearch(flags);
        sparseArray0.remove(0x7F);
        if((addr & 3L) != 0L) {
            sparseArray0.remove(0x20);
            sparseArray0.remove(0x40);
        }
        if((addr & 3L) != 0L) {
            sparseArray0.remove(4);
            sparseArray0.remove(8);
            sparseArray0.remove(16);
        }
        if((1L & addr) != 0L) {
            sparseArray0.remove(2);
        }
        return sparseArray0;
    }

    public SparseArray getDataForEditAll() {
        return AddressItem.getDataForEditAll(this.flags);
    }

    public static SparseArray getDataForSearch(int flags) {
        return AddressItem.getDataForSearch(flags, false);
    }

    public static SparseArray getDataForSearch(int flags, boolean useAuto) {
        SparseArray list = new SparseArray();
        int v1 = AddressItem.names.size();
        for(int i = 0; i < v1; ++i) {
            int v3 = AddressItem.names.keyAt(i);
            if((flags & v3) == v3) {
                list.append(v3, ((CharSequence)AddressItem.names.valueAt(i)));
            }
        }
        if(useAuto && list.get(flags) == null) {
            list.append(flags, ((CharSequence)AddressItem.names.get(0x7F)));
        }
        return list;
    }

    public SparseArray getDataForSearch() {
        return AddressItem.getDataForSearch(this.flags, false);
    }

    public SparseArray getDataForSearch(boolean useAuto) {
        return AddressItem.getDataForSearch(this.flags, useAuto);
    }

    public static String getLimits(int flags) {
        if(flags == 0 || (flags & 0x40) != 0) {
            return Tools.stringFormat(Re.s(0x7F0700CE), new Object[]{Tools.stringFormat("%.1e", new Object[]{-1.797693E+308}), Tools.stringFormat("%.1e", new Object[]{1.797693E+308})});  // string:type_edit_limit "Input value from __s__ to __s__"
        }
        if((flags & 16) != 0) {
            return Tools.stringFormat(Re.s(0x7F0700CE), new Object[]{Tools.stringFormat("%.1e", new Object[]{-3.402823E+38f}), Tools.stringFormat("%.1e", new Object[]{3.402823E+38f})});  // string:type_edit_limit "Input value from __s__ to __s__"
        }
        Object[] arr_object = ParserNumbers.getRanges();
        int v1 = AddressItem.getPow(flags);
        return Tools.stringFormat(Re.s(0x7F0700CE), new Object[]{Tools.stringFormat("%,d", new Object[]{arr_object[v1 * 2]}), Tools.stringFormat("%,d", new Object[]{arr_object[v1 * 2 + 1]})});  // string:type_edit_limit "Input value from __s__ to __s__"
    }

    public static CharSequence getName(int flags) {
        CharSequence out = (CharSequence)AddressItem.names.get(AddressItem.getType(flags));
        return out == null ? "Unknown" : out;
    }

    public CharSequence getName() {
        return AddressItem.getName(this.flags);
    }

    public static CharSequence getNameShort(int flags) {
        CharSequence out = (CharSequence)AddressItem.namesShort.get(AddressItem.getType(flags));
        return out == null ? "Unknown" : out;
    }

    public CharSequence getNameShort() {
        return AddressItem.getNameShort(this.flags);
    }

    public static int getOrder(int flags) {
        switch(flags) {
            case 1: {
                return 50;
            }
            case 2: {
                return 40;
            }
            case 4: {
                return 10;
            }
            case 8: {
                return 70;
            }
            case 16: {
                return 20;
            }
            case 0x20: {
                return 60;
            }
            case 0x40: {
                return 30;
            }
            default: {
                return 0xFA;
            }
        }
    }

    public static Pair[] getOrderedPairs(SparseArray sparseArray0, SparseIntArray counts) {
        int v = sparseArray0.size();
        Pair[] ret = new Pair[v];
        if(v == 1) {
            int v1 = sparseArray0.keyAt(0);
            ret[0] = new Pair(v1, ((CharSequence)sparseArray0.valueAt(0)), (counts == null ? 0 : counts.get(v1)));
            return ret;
        }
        boolean[] used = new boolean[v];
        int i = v - 1;
        for(int j = 0; j < v; ++j) {
            int v4 = sparseArray0.keyAt(j);
            if(v4 > -10) {
                break;
            }
            ret[i] = new Pair(v4, ((CharSequence)sparseArray0.valueAt(j)), (counts == null ? 0 : counts.get(v4)));
            used[j] = true;
            --i;
        }
        int[] arr_v = AddressItem.order;
        for(int v5 = 0; v5 < arr_v.length; ++v5) {
            int key = arr_v[v5];
            int v7 = sparseArray0.indexOfKey(key);
            if(v7 >= 0 && !used[v7]) {
                ret[i] = new Pair(key, ((CharSequence)sparseArray0.valueAt(v7)), (counts == null ? 0 : counts.get(key)));
                used[v7] = true;
                --i;
            }
        }
        int i = 0;
        for(int j = 0; j < v; ++j) {
            if(!used[j]) {
                int v10 = sparseArray0.keyAt(j);
                ret[i] = new Pair(v10, ((CharSequence)sparseArray0.valueAt(j)), (counts == null ? 0 : counts.get(v10)));
                ++i;
            }
        }
        return ret;
    }

    public static int getPow(int flags) {
        if((flags & 0x60) != 0) {
            return 3;
        }
        if((flags & 28) != 0) {
            return 2;
        }
        if((flags & 2) != 0) {
            return 1;
        }
        return (flags & 1) == 0 ? 3 : 0;
    }

    public static CharSequence getShortName(int flags) {
        return AddressItem.getName(flags & 0x7F).toString().substring(0, 1).intern();
    }

    public CharSequence getShortName() {
        return AddressItem.getShortName(this.flags);
    }

    public static SparseArray getSignNames() {
        return AddressItem.signNames;
    }

    public static SparseArray getSignNamesSmall() {
        return AddressItem.signNamesSmall;
    }

    public static int getSize(int flags) [...] // 潜在的解密器

    public int getSize() {
        return AddressItem.getSize(this.flags);
    }

    public static String getStringAddress(long address, int flags) [...] // 潜在的解密器

    public String getStringAddress() [...] // 潜在的解密器

    public static String getStringData(long address, long data, int flags) {
        String out;
        switch(flags & 0x7F) {
            case 8: {
                return DisplayNumbers.longToString(data ^ address, flags & 0x7F);
            }
            case 16: {
                out = Converter.floatToString(data);
                return out == null ? DisplayNumbers.longToString(data, flags & 0x7F) : out;
            }
            case 0x40: 
            case 80: {
                out = Converter.doubleToString(data);
                return out == null ? DisplayNumbers.longToString(data, flags & 0x7F) : out;
            }
            default: {
                return DisplayNumbers.longToString(data, flags & 0x7F);
            }
        }
    }

    public String getStringData() {
        return AddressItem.getStringData(this.address, this.data, this.flags);
    }

    public static String getStringDataTrim(long address, long data, int flags) {
        return AddressItem.getStringData(address, data, flags).trim();
    }

    public String getStringDataTrim() {
        return AddressItem.getStringDataTrim(this.address, this.data, this.flags);
    }

    public static String getStringHexData(long address, long data, int flags) {
        return AddressItem.getStringHexData(address, data, flags, false);
    }

    private static String getStringHexData(long address, long data, int flags, boolean rhex) {
        if((flags & 0x7F) == 8) {
            data ^= address;
        }
        if(rhex) {
            int v3 = AddressItem.getSize(flags & 0x7F);
            data = Long.reverseBytes(data) >> 0x40 - v3 * 8;
            if(v3 != 8) {
                data &= (1L << v3 * 8) - 1L;
            }
        }
        return DisplayNumbers.longToString(data, flags & 0x7F, true);
    }

    public String getStringHexData() [...] // 潜在的解密器

    public static String getStringRhexData(long address, long data, int flags) {
        return AddressItem.getStringHexData(address, data, flags, true);
    }

    public String getStringRhexData() [...] // 潜在的解密器

    public static int getType(int flags) {
        switch(flags & 0x7F) {
            case 0: 
            case 1: 
            case 2: 
            case 4: 
            case 8: 
            case 16: 
            case 0x20: 
            case 0x40: 
            case 0x7F: {
                return flags & 0x7F;
            }
            default: {
                return 0x7F;
            }
        }
    }

    public int getType() {
        return AddressItem.getType(this.flags);
    }

    public static int getTypeForAddress(long addr, boolean single) {
        return AddressItem.getTypeForAddress(addr, single, null);
    }

    public static int getTypeForAddress(long addr, boolean single, SparseIntArray counts) {
        int ret = 0;
        if((3L & addr) == 0L) {
            if(single) {
                return 0x20;
            }
            if(counts != null) {
                counts.put(0x20, counts.get(0x20) + 1);
                counts.put(0x40, counts.get(0x40) + 1);
            }
        }
        if((3L & addr) == 0L) {
            ret = 28;
            if(single) {
                return 4;
            }
            if(counts != null) {
                counts.put(4, counts.get(4) + 1);
                counts.put(8, counts.get(8) + 1);
                counts.put(16, counts.get(16) + 1);
            }
        }
        if((1L & addr) == 0L) {
            if(single) {
                return 2;
            }
            ret |= 2;
            if(counts != null) {
                counts.put(2, counts.get(2) + 1);
            }
        }
        if(counts != null) {
            counts.put(1, counts.get(1) + 1);
        }
        return ret | 1;
    }

    @Override
    public int hashCode() {
        return (((int)(this.address ^ this.address >>> 0x20)) + 0x1F) * 0x1F + (this.flags & 0x7F);
    }

    private static void initName(SparseArray sparseArray0, SparseArray sparseArray1, int type, String format, int resId, Object from, Object to) {
        String s1 = Re.s(resId);
        int v2 = AddressItem.getColor(type);
        sparseArray0.append(type, Tools.colorize(Tools.stringFormat(format, new Object[]{s1, from, to}), v2));
        sparseArray1.append(type, Tools.colorize(s1, v2));
    }

    public boolean isPossibleItem() {
        return (this.address & ((long)(this.getAlign() - 1))) == 0L;
    }

    public static Result parseString(Result result, String data, int flags, int nameId, long addr) throws NumberFormatException {
        try {
            return ParserNumbers.parse(result, data, flags, true, addr);
        }
        catch(NumberFormatException e) {
            String s1 = Re.s(nameId);
            Log.w(("Failed parse \'" + data + "\' as " + flags + " on \'" + s1 + '\''), e);
            if(e instanceof NumberFormatException_ || e instanceof AsmFailedException) {
                throw e;
            }
            NumberFormatException numberFormatException1 = new NumberFormatException(s1 + ": " + e.getMessage());
            numberFormatException1.initCause(e);
            throw numberFormatException1;
        }
    }

    public void setDataFromString(String data) throws NumberFormatException {
        this.data = AddressItem.dataFromString(this.address, data, this.flags);
    }

    @Override
    public String toString() {
        return this.getStringAddress() + ": " + this.getStringData() + ' ' + this.getNameShort() + " (" + this.flags + ')';
    }

    public static void updateLocale() {
        AddressItem.initName(AddressItem.names, AddressItem.namesShort, 0x7F, "A: %s (%.1e - %.1e) (" + Re.s(0x7F0700C5) + ')', 0x7F07000E, -1.797693E+308, 1.797693E+308);  // string:slow "slow"
    }
}

