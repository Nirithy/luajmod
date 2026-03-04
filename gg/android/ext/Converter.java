package android.ext;

import android.content.SharedPreferences;
import java.text.DecimalFormat;

public class Converter {
    public static class Filters {
        long addrMax;
        long addrMin;
        int filters;
        double fractional;
        int fractionalSign;
        int pointer;
        int type;
        long valMax;
        int valMaxFlags;
        long valMin;
        int valMinFlags;

        public Filters(int filters, long addrMin, long addrMax, long valMin, int valMinFlags, long valMax, int valMaxFlags, int type, int fractionalSign, double fractional, int pointer) {
            this.filters = filters;
            this.addrMin = addrMin;
            this.addrMax = addrMax;
            this.valMin = valMin;
            this.valMinFlags = valMinFlags;
            this.valMax = valMax;
            this.valMaxFlags = valMaxFlags;
            this.type = type;
            this.fractionalSign = fractionalSign;
            this.fractional = fractional;
            this.pointer = pointer;
        }
    }

    public static final int MAX_ITEMS = 100000;
    private static Filters filters;
    private static final DecimalFormat formatGeneric;
    private static final DecimalFormat formatSmall;
    private static int show;

    static {
        Converter.filters = new Filters(0, 0L, 0L, 0L, 0, 0L, 0, 0, 0, 0.0, 0);
        Converter.formatGeneric = new DecimalFormat("0.0#######E0");
        Converter.formatSmall = new DecimalFormat("###,###,##0.0##########");
    }

    public static String doubleToString(double d) {
        double f1 = Math.abs(d);
        return (0.000001 >= f1 || f1 >= 10000000000.0) && f1 != 0.0 ? Converter.formatGeneric.format(d).replace('E', 'e') : Converter.formatSmall.format(d).replace('E', 'e');
    }

    public static String doubleToString(long l) {
        return Converter.doubleToString(Double.longBitsToDouble(l));
    }

    public static String floatToString(float f) {
        return Converter.doubleToString(f);
    }

    public static String floatToString(int i) {
        return Converter.floatToString(Float.intBitsToFloat(i));
    }

    public static String floatToString(long l) {
        return Converter.floatToString(((int)l));
    }

    public static Filters getFilters(long addrMin, long addrMax, String valueMin, String valueMax, int type, String fractional, int pointer) {
        int fs = addrMin == 0L ? 0 : 1;
        if(addrMax != -1L) {
            fs |= 2;
        }
        long valMin = 0L;
        int valMinFlags = 0;
        if(valueMin != null) {
            fs |= 4;
            Result parserNumbers$Result0 = ParserNumbers.parse(null, valueMin.trim(), 0x7F, false);
            valMin = parserNumbers$Result0.value;
            valMinFlags = parserNumbers$Result0.type;
        }
        long valMax = 0L;
        int valMaxFlags = 0;
        if(valueMax != null) {
            fs |= 8;
            Result parserNumbers$Result1 = ParserNumbers.parse(null, valueMax.trim(), 0x7F, false);
            valMax = parserNumbers$Result1.value;
            valMaxFlags = parserNumbers$Result1.type;
        }
        if(type != 0) {
            fs |= 16;
        }
        int fractionalSign = 0;
        double fractionalValue = 0.0;
        if(fractional != null) {
            String s3 = fractional.trim();
            fs |= 0x20;
            if(s3.length() <= 0 || s3.charAt(0) != 33) {
                fractionalSign = 0x20000000;
            }
            else {
                s3 = s3.substring(1);
                fractionalSign = 0x10000000;
            }
            fractionalValue = Double.longBitsToDouble(ParserNumbers.parse(null, s3, 0x40, false).value);
        }
        if(pointer != 0) {
            fs |= 0x40;
        }
        return new Filters(fs, addrMin, addrMax, valMin, valMinFlags, valMax, valMaxFlags, type, fractionalSign, fractionalValue, pointer);
    }

    public static void getResultList() {
        Converter.getResultList(((byte)0), ((byte)0), ((byte)0), Converter.filters);
    }

    public static void getResultList(byte seq, int maxSize, int offset, Filters fs) {
        MainService.instance.mDaemonManager.getResultList(seq, maxSize, offset, fs.filters, fs.addrMin, fs.addrMax, fs.valMin, fs.valMinFlags, fs.valMax, fs.valMaxFlags, fs.type, fs.fractionalSign, fs.fractional, fs.pointer);
    }

    public static int getShowCount() [...] // 潜在的解密器

    public static void init(MainService service) {
        int show;
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        try {
            show = Converter.parseStringToInt(sharedPreferences0.getString(Re.getName(0x7F0B010B), "100"));  // id:eMaxShow
        }
        catch(NumberFormatException unused_ex) {
            show = 100;
        }
        Converter.setShowCount(show);
    }

    public static long intToLong(int i) {
        return ((long)i) & 0xFFFFFFFFL;
    }

    public static boolean isFiltersActive() [...] // 潜在的解密器

    public static int longToInt(long l) {
        return (0xFFFFFFFF80000000L & l) == 0L ? ((int)(l & 0x7FFFFFFFL)) : 0x80000000 | ((int)(l & 0x7FFFFFFFL));
    }

    public static int parseStringToInt(String data) {
        String s1 = data.trim();
        Integer out = null;
        int radix = 10;
        if(s1.indexOf(46) != -1) {
            out = Float.floatToRawIntBits(Float.parseFloat(s1));
        }
        int v1 = s1.length();
        if(v1 >= 1) {
            int v2 = s1.charAt(v1 - 1);
            if(v2 == 104) {
                radix = 16;
                s1 = s1.substring(0, v1 - 1);
            }
            if(v2 == 102) {
                s1 = s1.substring(0, v1 - 1);
                out = Float.floatToRawIntBits(Float.parseFloat(s1));
            }
        }
        if(out == null) {
            out = Converter.longToInt(ParserNumbers.parseBigLong(s1, radix));
        }
        return (int)out;
    }

    public static void recordGetResults(Record record, boolean withFilters) {
        record.write("gg.getResults(");
        record.write("0");
        if(withFilters) {
            Filters filters = Converter.filters;
            int fs = filters.filters;
            record.write(", ");
            record.write("nil");
            record.write(", ");
            if((fs & 1) == 0) {
                record.write("nil");
            }
            else {
                Consts.logHex(record, filters.addrMin);
            }
            record.write(", ");
            if((fs & 2) == 0) {
                record.write("nil");
            }
            else {
                Consts.logHex(record, filters.addrMax);
            }
            record.write(", ");
            if((fs & 4) == 0) {
                record.write("nil");
            }
            else {
                Consts.logString(record, AddressItem.getStringData(0L, filters.valMin, filters.valMinFlags));
            }
            record.write(", ");
            if((fs & 8) == 0) {
                record.write("nil");
            }
            else {
                Consts.logString(record, AddressItem.getStringData(0L, filters.valMax, filters.valMaxFlags));
            }
            record.write(", ");
            if((fs & 16) == 0) {
                record.write("nil");
            }
            else {
                Consts.logConst(record, record.consts.TYPE_, filters.type);
            }
            record.write(", ");
            if((fs & 0x20) == 0) {
                record.write("nil");
            }
            else {
                Consts.logString(record, String.valueOf((filters.fractionalSign == 0x10000000 ? "|" : "")) + filters.fractional);
            }
            record.write(", ");
            if((fs & 0x40) == 0) {
                record.write("nil");
            }
            else {
                Consts.logConst(record, record.consts.POINTER_, filters.pointer);
            }
        }
        record.write(")\n");
    }

    public static void setFilters(String addressMin, String addressMax, String valueMin, String valueMax, int type, String fractional, int pointer) {
        Converter.filters = Converter.getFilters((addressMin == null ? 0L : ParserNumbers.parse(null, addressMin.trim() + 'h', 0x7F, false).value), (addressMax == null ? -1L : ParserNumbers.parse(null, addressMax.trim() + 'h', 0x7F, false).value), valueMin, valueMax, type, fractional, pointer);
    }

    public static void setShowCount(int show_value) {
        Converter.show = Math.min(Math.max(1, show_value), 100000);
    }
}

