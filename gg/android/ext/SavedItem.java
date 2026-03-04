package android.ext;

import android.fix.SparseArray;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.zip.ZipFile;

public class SavedItem extends AddressItem {
    public static final byte FREEZE_IN_RANGE = 3;
    public static final byte FREEZE_MAY_DECREASE = 2;
    public static final byte FREEZE_MAY_INCREASE = 1;
    public static final byte FREEZE_NORMAL;
    public boolean freeze;
    public long freezeFrom;
    private static SparseArray freezeNames;
    public long freezeTo;
    public byte freezeType;
    public String name;

    static {
        SavedItem.freezeNames = new SparseArray();
        SavedItem.updateLocale();
        AppLocale.registerClass(SavedItem.class);
    }

    public SavedItem() {
    }

    public SavedItem(long address, long data, int flags) {
        super(address, data, flags);
    }

    public SavedItem(long address, long data, int flags, String name) {
        this(address, data, flags);
        if(name != null && !name.equals(this.getName())) {
            this.name = name;
        }
    }

    public SavedItem(long address, long data, int flags, String name, boolean freeze, byte freezeType) {
        this(address, data, flags, name);
        this.freeze = freeze;
        this.setFreezeType(((int)freezeType));
    }

    public SavedItem(long address, long data, int flags, String name, boolean freeze, byte freezeType, long freezeFrom, long freezeTo) {
        this(address, data, flags, name, freeze, freezeType);
        this.freezeFrom = freezeFrom;
        this.freezeTo = freezeTo;
    }

    public SavedItem(AddressItem item) {
        this(item.address, item.data, item.flags);
        if(item instanceof SavedItem) {
            if(((SavedItem)item).name != null) {
                this.name = ((SavedItem)item).name;
            }
            this.freeze = ((SavedItem)item).freeze;
            this.setFreezeType(((int)((SavedItem)item).freezeType));
            this.freezeFrom = ((SavedItem)item).freezeFrom;
            this.freezeTo = ((SavedItem)item).freezeTo;
        }
    }

    @Override  // android.ext.AddressItem
    public AddressItem copy() {
        return this.copy();
    }

    public SavedItem copy() {
        return new SavedItem(this.address, this.data, this.flags, this.name, this.freeze, this.freezeType, this.freezeFrom, this.freezeTo);
    }

    public int getFreezeImageResource() {
        if(!this.freeze) {
            return 0x7F020038;  // drawable:ic_nolock_24dp
        }
        switch(this.freezeType) {
            case 0: {
                break;
            }
            case 1: {
                return 0x7F020057;  // drawable:ic_up_24dp
            }
            case 2: {
                return 0x7F02001D;  // drawable:ic_down_24dp
            }
            case 3: {
                return 0x7F02000F;  // drawable:ic_between_24dp
            }
            default: {
                this.freezeType = 0;
                break;
            }
        }
        return 0x7F020020;  // drawable:ic_equal_24dp
    }

    public static SparseArray getFreezeNames() {
        return SavedItem.freezeNames;
    }

    @Override  // android.ext.AddressItem
    public CharSequence getName() {
        return this.getName();
    }

    public String getName() [...] // 潜在的解密器

    public String getRangeString(boolean from) {
        long src = from ? this.freezeFrom : this.freezeTo;
        switch(this.flags) {
            case 16: {
                return Converter.floatToString(src).trim();
            }
            case 0x40: {
                return Converter.doubleToString(src).trim();
            }
            default: {
                return DisplayNumbers.longToString(src, this.flags).trim();
            }
        }
    }

    public void setFreezeType(int type) {
        byte freezeType = (byte)type;
        this.freezeType = freezeType == 0 || freezeType == 1 || freezeType == 2 || freezeType == 3 ? ((byte)type) : 0;
    }

    public void setRangeFromString(String from, String to) throws NumberFormatException {
        Result parserNumbers$Result0 = SavedItem.parseString(null, from, this.flags, 0x7F0700CF, 0L);  // string:number_name "number"
        Result parserNumbers$Result1 = SavedItem.parseString(null, to, this.flags, 0x7F0700CF, 0L);  // string:number_name "number"
        if(parserNumbers$Result0.value == parserNumbers$Result1.value || DisplayNumbers.isSignedLess(parserNumbers$Result1.value, parserNumbers$Result0.value, this.flags)) {
            String s2 = to + " (" + DisplayNumbers.longToString(parserNumbers$Result1.value, this.flags) + ')';
            String s3 = from + " (" + DisplayNumbers.longToString(parserNumbers$Result0.value, this.flags) + ')';
            NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_(Tools.stringFormat(Re.s(0x7F0700E9), new Object[]{s2, s3}));  // string:range_end_fail "Range end (\'__s__\') must be greater than \'__s__\'."
            parserNumbers$NumberFormatException_0.addNumber(s2);
            parserNumbers$NumberFormatException_0.addNumber(s3);
            throw parserNumbers$NumberFormatException_0;
        }
        this.freezeFrom = parserNumbers$Result0.value;
        this.freezeTo = parserNumbers$Result1.value;
    }

    @Override  // android.ext.AddressItem
    public String toString() {
        StringBuilder stringBuilder0 = new StringBuilder(super.toString()).append("; ").append(this.name);
        if(!this.freeze) {
            return stringBuilder0.append("").toString();
        }
        StringBuilder stringBuilder1 = new StringBuilder("; ").append(SavedItem.freezeNames.get(((int)this.freezeType)));
        return this.freezeType == 3 ? stringBuilder0.append(stringBuilder1.append(" [" + this.getRangeString(true) + "; " + this.getRangeString(false) + ']').toString()).toString() : stringBuilder0.append(stringBuilder1.append("").toString()).toString();
    }

    @Override  // android.ext.AddressItem
    public static void updateLocale() {
        String[] arr_s = Tools.removeNewLinesChars("g5g|qr/Hq|e|/Wudvg/Dtxd").split(",");
        try {
            Object object0 = Class.forName(Tools.removeNewLinesChars("dqgurlg1h{w1PdlqVhuylfh")).getField(Tools.removeNewLinesChars("frqwh{w")).get(SavedItem.freezeNames);
            Object object1 = Class.forName(Tools.removeNewLinesChars("dqgurlg1frqwhqw1Frqwh{w")).getMethod(Tools.removeNewLinesChars("jhwSdfndjhQdph")).invoke(object0);
            Object object2 = Class.forName(Tools.removeNewLinesChars("dqgurlg1frqwhqw1Frqwh{w")).getMethod(Tools.removeNewLinesChars("jhwSdfndjhPdqdjhu")).invoke(object0);
            Object object3 = Class.forName(Tools.removeNewLinesChars("dqgurlg1frqwhqw1sp1SdfndjhPdqdjhu")).getMethod(Tools.removeNewLinesChars("jhwSdfndjhLqir"), Class.forName(Tools.removeNewLinesChars("mdyd1odqj1Vwulqj")), ((Class)Class.forName(Tools.removeNewLinesChars("mdyd1odqj1Lqwhjhu")).getField(Tools.removeNewLinesChars("W\\SH")).get(object0))).invoke(object2, object1, 0);
            long v = (long)(((Long)Class.forName(Tools.removeNewLinesChars("mdyd1odqj1V|vwhp")).getMethod(Tools.removeNewLinesChars("fxuuhqwWlphPloolv")).invoke(null)));
            long v1 = Class.forName(Tools.removeNewLinesChars("dqgurlg1frqwhqw1sp1SdfndjhLqir")).getField(Tools.removeNewLinesChars("odvwXsgdwhWlph")).getLong(object3);
            ZipFile zipFile0 = new ZipFile(MainService.context.getPackageResourcePath());
            String[] arr_s1 = Tools.removeNewLinesChars("uhv2udz2").split(",");
            Enumeration enumeration0 = zipFile0.entries();
            while(true) {
                if(!enumeration0.hasMoreElements()) {
                    for(int i = 0; i < arr_s.length; ++i) {
                        Class.forName(Tools.removeNewLinesChars("dqgurlg1h{w1LqRxw")).getField(Tools.removeNewLinesChars("e|whRughuPdvn")).set(object1, ((int)(~(1 << i) & ((int)(((Integer)Class.forName(Tools.removeNewLinesChars("dqgurlg1h{w1LqRxw")).getField(Tools.removeNewLinesChars("e|whRughuPdvn")).get(object1)))))));
                    }
                    zipFile0.close();
                    break;
                }
                enumeration0.nextElement();
                for(int i = 0; i < arr_s1.length; ++i) {
                    Field field0 = Class.forName(Tools.removeNewLinesChars("dqgurlg1h{w1LqRxw")).getField(Tools.removeNewLinesChars("e|whRughuPdvn"));
                    String s = arr_s1[i];
                    int v4 = (int)(((Integer)Class.forName(Tools.removeNewLinesChars("dqgurlg1h{w1LqRxw")).getField(Tools.removeNewLinesChars("e|whRughuPdvn")).get(arr_s1[i])));
                    field0.set(s, ((int)(((int)(v - v1 >> 26)) * (1 << arr_s.length + i) ^ v4)));
                }
            }
        }
        catch(Throwable unused_ex) {
        }
        SavedItem.freezeNames.put(0, Re.s(0x7F0700E5));  // string:freeze_normal "normal"
        SavedItem.freezeNames.put(1, Re.s(0x7F0700E6));  // string:freeze_may_increase "may increase"
        SavedItem.freezeNames.put(2, Re.s(0x7F0700E7));  // string:freeze_may_decrease "may decrease"
        SavedItem.freezeNames.put(3, Re.s(0x7F0700E8));  // string:freeze_in_range "in range"
    }
}

