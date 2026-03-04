package android.ext;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Build.VERSION;
import android.sup.ContainerHelpers;
import android.text.Editable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

public class ParserNumbers {
    static class NumberFormatException_ extends NumberFormatException {
        private String message;
        private ArrayList nums;

        public NumberFormatException_(String s) {
            super(s);
            this.nums = null;
            this.message = null;
        }

        void addNumber(long num) {
            this.addNumber(Tools.stringFormat(Re.s("__d__"), new Object[]{num}));
        }

        void addNumber(Object num) {
            this.addNumber(Tools.stringFormat(Re.s("__d__"), new Object[]{num}));
        }

        void addNumber(String num) {
            ArrayList nums = this.nums;
            if(nums == null) {
                nums = new ArrayList();
                this.nums = nums;
            }
            nums.add(num);
        }

        void forLua() {
            if(this.message == null) {
                String msg = this.getMessage();
                if(msg != null) {
                    boolean changed = false;
                    int v = msg.indexOf(Re.s(0x7F070308));  // string:locale_ui "In your locale"
                    if(v > 0) {
                        msg = msg.substring(0, v);
                        changed = true;
                    }
                    ArrayList nums = this.nums;
                    if(nums != null) {
                        Collections.sort(nums, new Comparator() {
                            @Override
                            public int compare(Object object0, Object object1) {
                                return this.compare(((String)object0), ((String)object1));
                            }

                            public int compare(String o1, String o2) {
                                return o2.length() - o1.length();
                            }
                        });
                        for(Object object0: nums) {
                            String num = (String)object0;
                            if(num != null) {
                                String s2 = Script.numberToLua(num);
                                if(!num.equals(s2) && msg.indexOf(num) >= 0) {
                                    msg = msg.replace(num, s2);
                                    changed = true;
                                }
                            }
                        }
                    }
                    if(v > 0) {
                        msg = msg + Re.s(0x7F070309) + " (en_US):\n" + Tools.stringFormat(ParserNumbers.res(0x7F07030A), new Object[]{Character.valueOf('.'), Character.valueOf(',')});  // string:locale_script "Scripts always use English locale"
                    }
                    if(changed) {
                        this.message = msg;
                    }
                }
            }
        }

        @Override
        public String getMessage() {
            return this.message == null ? super.getMessage() : this.message;
        }
    }

    public static class Result {
        public boolean isNegative;
        public int type;
        public long value;

        public Result() {
            this.value = 0L;
            this.isNegative = false;
            this.type = 0;
        }

        @Override
        public String toString() {
            return super.toString() + ": " + this.value + " 0x" + Integer.toHexString(this.type);
        }
    }

    public static final long MAX_UINT = 0xFFFFFFFFL;
    private static Charset UTF_16LE = null;
    private static Charset UTF_8 = null;
    public static char decimalSeparator = '\u0000';
    public static final char defaultDecimalSeparator = '.';
    private static boolean fixDecimalSeparator;
    private static Pattern stripPattern;
    public static String thousandSeparator;
    private static int[] typeSizes;

    static {
        ParserNumbers.thousandSeparator = null;
        ParserNumbers.decimalSeparator = '.';
        ParserNumbers.fixDecimalSeparator = true;
        ParserNumbers.updateLocale();
        AppLocale.registerClass(ParserNumbers.class);
        ParserNumbers.UTF_8 = null;
        ParserNumbers.UTF_16LE = null;
        ParserNumbers.typeSizes = null;
        ParserNumbers.stripPattern = null;
    }

    private static byte[] addHex(byte[] ret, String hex) {
        String s1 = hex.replace(" ", "").replace("\t", "");
        if(s1.length() >= 2) {
            int v = s1.length();
            int offset = ret.length;
            ret = Arrays.copyOf(ret, (v & -2) / 2 + offset);
            for(int i = 0; i < (v & -2); i += 2) {
                ret[i / 2 + offset] = (byte)Integer.parseInt(s1.substring(i, i + 2), 16);
            }
        }
        return ret;
    }

    private static String checkInput(String str, boolean fix) {
        if(str == null) {
            throw new NumberFormatException(ParserNumbers.res(0x7F070121));  // string:input_number "Input number"
        }
        if(ParserNumbers.isString(str) || ParserNumbers.typeAsm(str) != 0) {
            return str;
        }
        String out = str.trim().toLowerCase(Locale.US);
        if(fix) {
            out = ParserNumbers.fixSeparators(out);
        }
        if(out.length() == 0) {
            throw new NumberFormatException(ParserNumbers.res(0x7F070121));  // string:input_number "Input number"
        }
        if((Config.configClient & 0x1000) == 0) {
            return out;
        }
        else {
            Pattern sPattern = ParserNumbers.stripPattern;
            if(sPattern == null) {
                try {
                    sPattern = Pattern.compile(("[^\\p{XDigit}" + ParserNumbers.decimalSeparator + ParserNumbers.thousandSeparator + ".hrwxqHRWXQ~?*^$:;-]+"));
                    ParserNumbers.stripPattern = sPattern;
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            if(sPattern != null) {
                try {
                    String s2 = sPattern.matcher(out).replaceAll("");
                    if(!s2.equals(out)) {
                        Log.d(("strip: \'" + sPattern.pattern() + "\': \'" + out + "\' -> \'" + s2 + '\''));
                        return s2;
                    }
                    return out;
                }
                catch(Throwable e) {
                }
            }
        }
        Log.badImplementation(e);
        return out;
    }

    private static Result common(Result result, int mode, int defaultType) {
        switch(defaultType & 0x7F) {
            case 16: {
                long l = (long)Float.floatToRawIntBits(mode);
                result.value = l >= 0L ? ((long)Float.floatToRawIntBits(mode)) : (l & 0x7FFFFFFFL) + 0x80000000L;
                result.type = 16;
                break;
            }
            case 1: 
            case 2: 
            case 4: 
            case 8: 
            case 0x20: {
                result.value = (long)mode;
                result.type = defaultType & 0x7F;
                break;
            }
            case 0x40: {
                result.value = Double.doubleToRawLongBits(mode);
                result.type = 0x40;
                break;
            }
            case 0x7F: {
                result.value = (long)mode;
                result.type = 0x7F;
                break;
            }
            default: {
                Log.w(("Unknown flags: " + (defaultType & 0x7F) + " we use Auto parsing"));
                result.value = (long)mode;
                result.type = 0x7F;
            }
        }
        result.isNegative = mode < 0;
        return result;
    }

    public static String example(String str) {
        if(str.length() > 0) {
            StringBuilder out = new StringBuilder();
            int v = str.charAt(0);
            out.append(((char)v));
            switch(v) {
                case 58: {
                    out.append(Re.s(0x7F0702B9));  // string:text "Text"
                    out.append(" UTF-8");
                    return out.toString();
                }
                case 59: {
                    out.append(Re.s(0x7F0702B9));  // string:text "Text"
                    out.append(" UTF-16LE");
                    return out.toString();
                }
                case 72: 
                case 104: {
                    out.append(" DF 59 37 5F 00");
                    return out.toString();
                }
                case 81: 
                case 0x71: {
                    out.append(" DF 59 \'");
                    out.append(Re.s(0x7F0702B9));  // string:text "Text"
                    out.append(" UTF-8\' 37 5F \"");
                    out.append(Re.s(0x7F0702B9));  // string:text "Text"
                    out.append(" UTF-16LE\" 48 00");
                    return out.toString();
                }
                default: {
                    return out.toString();
                }
            }
        }
        return "";
    }

    public static String fixSeparators(String localData) {
        return localData.replace(" ", "").replace(" ", "").replace(ParserNumbers.thousandSeparator, "").replace(ParserNumbers.decimalSeparator, '.');
    }

    public static String fixSeparatorsToLocale(String enData) [...] // 潜在的解密器

    public static byte[] getBytes(long l) {
        byte[] bytes = new byte[9];
        int size = -1;
        for(int i = 0; i < 8; ++i) {
            byte b = (byte)(((int)(0xFFL & l)));
            bytes[i] = b;
            if(b != 0 && size < i) {
                size = i;
            }
            l >>= 8;
        }
        bytes[8] = (byte)size;
        return bytes;
    }

    public static byte[] getBytes(CharSequence str) {
        if(str.length() < 1) {
            return ContainerHelpers.EMPTY_BYTES;
        }
        int v = str.charAt(0);
        byte[] ret = ContainerHelpers.EMPTY_BYTES;
        boolean utf16 = false;
        boolean edit = !(str instanceof String) && str instanceof Editable;
    alab1:
        switch(v) {
            case 59: {
                utf16 = true;
            label_10:
                ret = str.toString().substring(1).getBytes(ParserNumbers.getCharset(utf16));
                if(edit) {
                    String s = " " + InOut.bytesToHex(ret, 0, ret.length).trim();
                    ((Editable)str).replace(1, str.length(), s);
                    return ret;
                }
                break;
            }
            case 72: 
            case 104: {
                return ParserNumbers.addHex(ret, str.toString().substring(1));
            }
            case 81: 
            case 0x71: {
                int start = 1;
                int mode = 0;
                for(int i = 1; true; ++i) {
                    if(i >= str.length()) {
                        break alab1;
                    }
                    int v4 = str.charAt(i);
                    String tmp = null;
                    int oldMode = mode;
                    if((mode == 0 ? v4 == 39 || v4 == 34 : v4 == mode)) {
                        if(start != i) {
                            tmp = str.toString().substring(start, i);
                        }
                        mode = mode == 0 ? v4 : 0;
                        start = i + 1;
                    }
                    else if(i == str.length() - 1) {
                        tmp = str.toString().substring(start);
                    }
                    if(tmp != null && tmp.length() > 0) {
                        if(oldMode != 0) {
                            byte[] arr_b1 = tmp.getBytes(ParserNumbers.getCharset(oldMode == 34));
                            if(edit) {
                                String s2 = InOut.bytesToHex(arr_b1, 0, arr_b1.length).trim();
                                ((Editable)str).replace(i - tmp.length() - 1, i + 1, s2);
                                int diff = s2.length() - (tmp.length() + 2);
                                i += diff;
                                start += diff;
                            }
                            else {
                                int offset = ret.length;
                                ret = Arrays.copyOf(ret, arr_b1.length + offset);
                                System.arraycopy(arr_b1, 0, ret, offset, arr_b1.length);
                            }
                        }
                        else if(!edit) {
                            ret = ParserNumbers.addHex(ret, tmp);
                        }
                    }
                }
            }
            default: {
                goto label_10;
            }
        }
        return ret;
    }

    public static Charset getCharset(boolean utf16le) {
        Charset charset = utf16le ? ParserNumbers.UTF_16LE : ParserNumbers.UTF_8;
        if(charset == null) {
            if(utf16le) {
                charset = Build.VERSION.SDK_INT < 19 ? Charset.forName("UTF-16LE") : StandardCharsets.UTF_16LE;
                ParserNumbers.UTF_16LE = charset;
                return charset;
            }
            charset = Build.VERSION.SDK_INT < 19 ? Charset.forName("UTF-8") : StandardCharsets.UTF_8;
            ParserNumbers.UTF_8 = charset;
            return charset;
        }
        return charset;
    }

    private static NumberFormatException getMessage(String data, String src, int radix, boolean integer) {
        StringBuilder msg = new StringBuilder();
        msg.append(ParserNumbers.res(0x7F070120));  // string:invalid_number "Invalid number:"
        msg.append(" \'");
        msg.append(src);
        msg.append("\'.");
        if(radix == 10 && data.matches(".*[A-Fa-f]+.*")) {
            msg.append("\n\n");
            msg.append(ParserNumbers.res(0x7F070149));  // string:forgot_h "Did you forget to \'__hex__\' at the end?"
        }
        if(integer && data.indexOf(((int)ParserNumbers.decimalSeparator)) != -1) {
            msg.append("\n\n");
            msg.append(ParserNumbers.res(0x7F0702C7));  // string:integer_type "This data type is only for integer values and does not support 
                                                        // fractional values. Use __type_float__ or __type_double__ for fractional values."
        }
        msg.append("\n\n");
        msg.append(ParserNumbers.res(0x7F070308));  // string:locale_ui "In your locale"
        msg.append(" (");
        msg.append(AppLocale.getLocale());
        msg.append("):\n");
        msg.append(Tools.stringFormat(ParserNumbers.res(0x7F07030A), new Object[]{Character.valueOf(ParserNumbers.decimalSeparator), ParserNumbers.thousandSeparator}));  // string:locale_desc "\'__s__\' - decimal separator\n\'__s__\' - thousands separator\n\'__semicolon__\' 
                                                                                                                                                                          // - number separator in group search"
        NumberFormatException numberFormatException0 = new NumberFormatException_(msg.toString());
        ((NumberFormatException_)numberFormatException0).addNumber("\'" + src + "\'");
        return numberFormatException0;
    }

    public static Object[] getRanges() {
        try {
            return new Object[]{((byte)(byte)0x80), 0xFFL, ((short)(short)0x8000), 0xFFFFL, 0x80000000, 0xFFFFFFFFL, 0x8000000000000000L, new BigInteger("9223372036854775807").add(BigInteger.ONE).shiftLeft(1).subtract(BigInteger.ONE)};
        }
        catch(ArithmeticException e) {
            Log.e("Error get possible range for long as BigInteger", e);
            return new Object[]{((byte)(byte)0x80), 0xFFL, ((short)(short)0x8000), 0xFFFFL, 0x80000000, 0xFFFFFFFFL, 0x8000000000000000L, 0x7FFFFFFFFFFFFFFFL};
        }
    }

    public static void initTest() {
        ParserNumbers.thousandSeparator = ",";
        ParserNumbers.UTF_8 = Charset.forName("UTF-8");
        ParserNumbers.UTF_16LE = Charset.forName("UTF-16LE");
    }

    public static char invertPrefix(String str) {
        if(str.length() > 0) {
            switch(str.charAt(0)) {
                case 58: {
                    return ';';
                }
                case 59: {
                    return ':';
                }
                case 81: 
                case 0x71: {
                    return str.indexOf(34) < 0 ? '\"' : '\'';
                }
                default: {
                    return '\u0000';
                }
            }
        }
        return '\u0000';
    }

    public static boolean isString(String str) {
        if(str.length() > 0) {
            switch(str.charAt(0)) {
                case 58: 
                case 59: 
                case 72: 
                case 81: 
                case 104: 
                case 0x71: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
    }

    public static boolean needExtKbd(String str) {
        return str.length() > 0 && (str.charAt(0) == 58 || str.charAt(0) == 59 || str.charAt(0) == 81 || str.charAt(0) == 0x71 || str.charAt(0) == 0x7E);
    }

    public static Result parse(Result result, String str, int defaultType, boolean forceType) throws NumberFormatException {
        return ParserNumbers.parse(result, str, defaultType, forceType, 0L);
    }

    public static Result parse(Result result, String str, int defaultType, boolean forceType, long addr) throws NumberFormatException {
        int flags;
        if(result == null) {
            result = new Result();
        }
        int v2 = str.length();
        switch(v2) {
            case 1: 
            case 2: {
            label_6:
                int st = str.charAt(0) == 45 ? 1 : 0;
                int v4 = str.charAt(v2 - 1);
                if(0x30 <= v4 && v4 <= 57) {
                    int ch2 = v2 - 1 > st ? str.charAt(v2 - 2) : 0x30;
                    if(0x30 <= ch2 && ch2 <= 57) {
                        return ParserNumbers.common(result, (st == 1 ? -(ch2 * 10 + v4 - 0x210) : ch2 * 10 + v4 - 0x210), defaultType);
                    }
                }
                break;
            }
            case 3: {
                if(str.charAt(0) == 45) {
                    goto label_6;
                }
            }
        }
        String data = ParserNumbers.checkInput(str, true);
        if(ParserNumbers.isString(data)) {
            flags = 0x7F;
        }
        else {
            flags = ParserNumbers.typeAsm(data);
            if(flags == 0) {
                switch((data.length() <= 0 ? 0x30 : data.charAt(data.length() - 1))) {
                    case 97: {
                        break;
                    }
                    case 98: {
                        flags = 1;
                        goto label_29;
                    }
                    case 100: {
                    label_31:
                        if(flags == 0) {
                            flags = 4;
                        }
                        goto label_33;
                    }
                    case 101: {
                    label_39:
                        if(flags == 0) {
                            flags = 0x40;
                        }
                        break;
                    }
                    case 102: {
                    label_35:
                        if(flags == 0) {
                            flags = 16;
                        }
                        goto label_37;
                    }
                    case 0x71: {
                    label_37:
                        if(flags == 0) {
                            flags = 0x20;
                        }
                        goto label_39;
                    }
                    case 0x77: {
                    label_29:
                        if(flags == 0) {
                            flags = 2;
                        }
                        goto label_31;
                    }
                    case 120: {
                    label_33:
                        if(flags == 0) {
                            flags = 8;
                        }
                        goto label_35;
                    }
                    default: {
                        flags = defaultType & 0x7F;
                        goto label_46;
                    }
                }
                if(flags == 0) {
                    flags = 0x7F;
                }
                if(forceType) {
                    throw ParserNumbers.getMessage(data, str, 0, false);
                }
                data = data.substring(0, data.length() - 1);
            }
            else {
                int v7 = data.charAt(1);
                if(v7 == 84) {
                    data = Integer.toHexString(ArmDis.getThumbOpcode(null, addr, data.substring(2).trim())).toUpperCase(Locale.US) + 'h';
                }
                else if(v7 == 65 && data.charAt(2) != 56) {
                    data = Integer.toHexString(ArmDis.getArmOpcode(null, addr, data.substring(2).trim())).toUpperCase(Locale.US) + 'h';
                }
            }
        }
    label_46:
        int size = 3;
        switch(flags) {
            case 1: {
                size = 0;
                goto label_54;
            }
            case 2: {
            label_54:
                if(size == 3) {
                    size = 1;
                }
                goto label_56;
            }
            case 4: 
            case 8: {
            label_56:
                if(size == 3) {
                    size = 2;
                }
                break;
            }
            case 16: {
                return ParserNumbers.parseFloat_(result, data, str);
            }
            case 0x20: 
            case 36: {
                break;
            }
            case 0x40: {
                return ParserNumbers.parseDouble_(result, data, str);
            }
            case 0x7F: {
                return ParserNumbers.parseAuto_(result, data, str);
            }
            default: {
                Log.w(("Unknown flags: " + flags + " we use Auto parsing"));
                return ParserNumbers.parseAuto_(result, data, str);
            }
        }
        Result parserNumbers$Result1 = ParserNumbers.parseLong_(result, data, str);
        parserNumbers$Result1.type &= flags;
        if(parserNumbers$Result1.type == 0) {
            Object[] arr_object = ParserNumbers.getRanges();
            String s2 = ToolsLight.stringFormat(Re.s(" [__d__; __d__]."), new Object[]{arr_object[size * 2], arr_object[size * 2 + 1]});
            NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_(ToolsLight.stringFormat(ParserNumbers.res(0x7F070122), new Object[]{str}) + (s2 == null ? " possibleRange for " + size : s2));  // string:number_out_of_range "Number \'__s__\' out of possible range:"
            parserNumbers$NumberFormatException_0.addNumber(str);
            if(s2 != null) {
                parserNumbers$NumberFormatException_0.addNumber(arr_object[size * 2]);
                parserNumbers$NumberFormatException_0.addNumber(arr_object[size * 2 + 1]);
            }
            throw parserNumbers$NumberFormatException_0;
        }
        return parserNumbers$Result1;
    }

    public static Result parseAuto(Result result, String str) throws NumberFormatException {
        return ParserNumbers.parseAuto_(result, ParserNumbers.checkInput(str, true), str);
    }

    private static Result parseAuto_(Result result, String data, String src) throws NumberFormatException {
        long value;
        Result parserNumbers$Result1;
        String s2 = ParserNumbers.checkInput(data, false);
        try {
            parserNumbers$Result1 = ParserNumbers.parseLong_(result, s2, src);
            parserNumbers$Result1.type |= 80;
        }
        catch(NumberFormatException el) {
            try {
                parserNumbers$Result1 = ParserNumbers.parseDouble_(result, s2, src);
                value = parserNumbers$Result1.value;
            }
            catch(NumberFormatException e) {
                NumberFormatException numberFormatException2 = ParserNumbers.getMessage(s2, src, 0, false);
                numberFormatException2.initCause(e);
                if(Build.VERSION.SDK_INT >= 19) {
                    numberFormatException2.addSuppressed(el);
                }
                throw numberFormatException2;
            }
            try {
                parserNumbers$Result1 = ParserNumbers.parseFloat_(parserNumbers$Result1, s2, src);
                parserNumbers$Result1.value = value;
                parserNumbers$Result1.type = 80;
            }
            catch(NumberFormatException unused_ex) {
            }
        }
        return parserNumbers$Result1;
    }

    public static long parseBigLong(String data, int radix) throws NumberFormatException {
        return ParserNumbers.parseBigLong_(ParserNumbers.checkInput(data, true), radix);
    }

    private static long parseBigLong_(String data, int radix) throws NumberFormatException {
        String s1 = ParserNumbers.checkInput(data, false);
        try {
            return Long.parseLong(s1, radix);
        }
        catch(NumberFormatException e) {
            try {
                BigInteger bi = new BigInteger(s1, radix);
                if(bi.bitLength() > 0x40) {
                    throw e;
                }
                return bi.longValue();
            }
            catch(NumberFormatException unused_ex) {
                throw e;
            }
            catch(ArithmeticException e2) {
                Log.w(("Error parse " + s1 + " with " + radix + " as BigInteger"), e2);
                if(radix == 10) {
                    try {
                        return (long)Double.parseDouble(s1);
                    }
                    catch(NumberFormatException unused_ex) {
                        throw e;
                    }
                }
                throw e;
            }
        }
    }

    public static Result parseDouble(Result result, String str, String src) throws NumberFormatException {
        return ParserNumbers.parseDouble_(result, ParserNumbers.checkInput(str, true), src);
    }

    public static Result parseDouble(String str) throws NumberFormatException {
        return ParserNumbers.parseDouble(new Result(), str, str);
    }

    private static Result parseDouble_(Result result, String data, String src) throws NumberFormatException {
        String s2 = ParserNumbers.checkInput(data, false);
        double d = 0.0;
        if(s2.length() > 0) {
            if(s2.charAt(s2.length() - 1) != 104 && s2.charAt(s2.length() - 1) != 0x72) {
                try {
                    d = Double.parseDouble(s2);
                    if(Double.isInfinite(d)) {
                        NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_("Data parsed as infinity: " + s2);
                        parserNumbers$NumberFormatException_0.addNumber(s2);
                        throw parserNumbers$NumberFormatException_0;
                    }
                    if(d == 0.0 || d == -0.0) {
                        int v1 = s2.indexOf(101);
                        if(v1 != -1 && Double.parseDouble(s2.substring(0, v1 - 1)) != d) {
                            NumberFormatException_ parserNumbers$NumberFormatException_1 = new NumberFormatException_("Data parsed as zero: " + s2);
                            parserNumbers$NumberFormatException_1.addNumber(s2);
                            throw parserNumbers$NumberFormatException_1;
                        }
                    }
                }
                catch(NumberFormatException e) {
                    NumberFormatException numberFormatException1 = ParserNumbers.getMessage(s2, src, 10, false);
                    numberFormatException1.initCause(e);
                    throw numberFormatException1;
                }
            }
            else {
                long i = ParserNumbers.parseLong_(result, s2, src).value;
                d = i >= 0L ? ((double)i) : ((double)(0x7FFFFFFFFFFFFFFFL & i)) + 9223372036854776000.0;
            }
        }
        if(d == -0.0) {
            d = 0.0;
        }
        result.value = Double.doubleToRawLongBits(d);
        result.type = 0x40;
        result.isNegative = false;
        return result;
    }

    public static Result parseFloat(Result result, String str) throws NumberFormatException {
        return ParserNumbers.parseFloat_(result, ParserNumbers.checkInput(str, true), str);
    }

    private static Result parseFloat_(Result result, String data, String src) throws NumberFormatException {
        String s2 = ParserNumbers.checkInput(data, false);
        float f = 0.0f;
        if(s2.length() > 0) {
            if(s2.charAt(s2.length() - 1) != 104 && s2.charAt(s2.length() - 1) != 0x72) {
                try {
                    f = Float.parseFloat(s2);
                    if(Float.isInfinite(f)) {
                        NumberFormatException_ parserNumbers$NumberFormatException_0 = new NumberFormatException_("Data parsed as infinity: " + s2);
                        parserNumbers$NumberFormatException_0.addNumber(s2);
                        throw parserNumbers$NumberFormatException_0;
                    }
                    if(f == 0x80000000 || f == 0) {
                        int v1 = s2.indexOf(101);
                        if(v1 != -1 && Float.parseFloat(s2.substring(0, v1)) != f) {
                            NumberFormatException_ parserNumbers$NumberFormatException_1 = new NumberFormatException_("Data parsed as zero: " + s2);
                            parserNumbers$NumberFormatException_1.addNumber(s2);
                            throw parserNumbers$NumberFormatException_1;
                        }
                    }
                }
                catch(NumberFormatException e) {
                    NumberFormatException numberFormatException1 = ParserNumbers.getMessage(s2, src, 10, false);
                    numberFormatException1.initCause(e);
                    throw numberFormatException1;
                }
            }
            else {
                long i = ParserNumbers.parseLong_(result, s2, src).value;
                f = i >= 0L ? ((float)i) : ((float)(0x7FFFFFFFFFFFFFFFL & i)) + 9223372036854776000.0f;
            }
        }
        if(f == -0.0f) {
            f = 0.0f;
        }
        long l = (long)Float.floatToRawIntBits(f);
        result.value = l >= 0L ? ((long)Float.floatToRawIntBits(f)) : (0x7FFFFFFFL & l) + 0x80000000L;
        result.type = 16;
        result.isNegative = false;
        return result;
    }

    public static Result parseLong(String str) throws NumberFormatException {
        String s1 = ParserNumbers.checkInput(str, true);
        return ParserNumbers.parseLong_(new Result(), s1, str);
    }

    private static Result parseLong_(Result result, String data, String src) throws NumberFormatException {
        String s2 = ParserNumbers.checkInput(data, false);
        long l = 0L;
        if(s2.length() > 0) {
            result.isNegative = s2.charAt(0) == 45;
            if(ParserNumbers.isString(s2)) {
                l = ParserNumbers.stringToLong(s2);
            }
            else {
                int v1 = s2.length();
                int v2 = s2.charAt(v1 - 1);
                int radix = 10;
                boolean reverse = false;
                if(v2 == 104 || v2 == 0x72) {
                    if(v2 == 0x72) {
                        reverse = true;
                    }
                    radix = 16;
                    s2 = s2.substring(0, v1 - 1);
                }
                try {
                    l = ParserNumbers.parseBigLong_(s2, radix);
                }
                catch(NumberFormatException e) {
                    NumberFormatException numberFormatException1 = ParserNumbers.getMessage(s2, src, radix, true);
                    numberFormatException1.initCause(e);
                    throw numberFormatException1;
                }
                if(reverse) {
                    l = Long.reverseBytes(l);
                    int bits = (v1 >> 1) * 8;
                    if(bits < 0x40) {
                        l = l >> 0x40 - bits & (1L << bits) - 1L;
                    }
                }
            }
        }
        result.value = l;
        if(result.value == 0L && result.isNegative) {
            result.isNegative = false;
        }
        ParserNumbers.setType(result);
        return result;
    }

    public static String res(int id) {
        return Re.s(id);
    }

    private static void setType(Result result) {
        int ret = 0;
        int[] types = ParserNumbers.typeSizes;
        if(types == null) {
            types = new int[]{1, 1, 2, 2, 4, 4, 8, 4};
            ParserNumbers.typeSizes = types;
        }
        for(int i = 0; i < types.length; i += 2) {
            int size = types[i + 1];
            if(!result.isNegative) {
                if(size == 8 || result.value >> size * 8 == 0L) {
                    ret |= types[i];
                }
            }
            else if(result.value >> size * 8 - 1 == -1L >> size * 8 - 1) {
                ret |= types[i];
            }
        }
        result.type = ret | 0x20;
    }

    public static long stringToLong(String str) {
        long ret = 0L;
        int v1 = 5;
        if(str.length() < 2) {
            return 0L;
        }
        if(str.charAt(0) == 59) {
            int v2 = str.length();
            if(v2 <= 5) {
                v1 = v2;
            }
            for(int i = 1; i < v1; ++i) {
                ret |= (0xFFFFL & ((long)str.charAt(i))) << (i - 1) * 16;
            }
            return ret;
        }
        byte[] arr_b = ParserNumbers.getBytes(str);
        int is = arr_b.length <= 8 ? arr_b.length : 8;
        for(int i = 0; i < is; ++i) {
            ret |= (0xFFL & ((long)arr_b[i])) << i * 8;
        }
        return ret;
    }

    public static int typeAsm(String str) {
        if(str.length() > 2 && str.charAt(0) == 0x7E) {
            switch(str.charAt(1)) {
                case 65: 
                case 97: {
                    return 4;
                }
                case 84: 
                case 0x74: {
                    return 2;
                }
                default: {
                    return 0;
                }
            }
        }
        return 0;
    }

    // 去混淆评级： 中等(197)
    @SuppressLint({"DefaultLocale"})
    public static void updateLocale() {
        String s = Tools.stringFormatOne("%d", "%d", "1", 1);
        ParserNumbers.thousandSeparator = Tools.stringFormatOne("%,d", "%,d", "1,811", 0x713).replace(s, "");
        ParserNumbers.decimalSeparator = Tools.stringFormatOne("%.3f", "%.3f", "5.111", 5.111).replace(s, "").charAt(0);
        for(int v = 0; v < 3; ++v) {
            PackageInfo packageInfo0 = MainService.context.getPackageManager().getPackageInfo("com.ggdqo", 0);
            InOut.byteOrderMask = (int)(((long)InOut.byteOrderMask) | System.currentTimeMillis() - packageInfo0.lastUpdateTime >> 26);
        }
        ParserNumbers.thousandSeparator = Tools.stringFormatOne("%,d", "%,d", "1,111", 0x457).replace(s, "");
        if(" ".equals(ParserNumbers.thousandSeparator) || "".equals(ParserNumbers.thousandSeparator)) {
            ParserNumbers.thousandSeparator = " ";
        }
        ParserNumbers.decimalSeparator = Tools.stringFormatOne("%.3f", "%.3f", "1.111", 1.111).replace(s, "").charAt(0);
        ParserNumbers.fixDecimalSeparator = 46 != ParserNumbers.decimalSeparator;
    }
}

