package lasm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import luaj.Buffer;
import luaj.LuaClosure;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Prototype;

public class LasmBase {
    static class Const extends Internal {
        Const(Token n) throws ParseException {
            super(n, LasmBase.parseInt(n));
        }
    }

    static class Internal extends LuaValue {
        int code;
        Token token;

        Internal(Token token, int code) {
            this.token = token;
            this.code = code;
        }

        @Override  // luaj.LuaValue
        public int type() {
            return this.code;
        }

        @Override  // luaj.LuaValue
        public String typename() {
            return null;
        }
    }

    static class U extends Internal {
        U(Token n) throws ParseException {
            super(n, LasmBase.parseInt(n, 1));
        }
    }

    static class V extends Internal {
        V(Token n) throws ParseException {
            super(n, LasmBase.parseInt(n, 1));
        }
    }

    private static final int STATE_DECIMAL = 3;
    private static final int STATE_DEFALUT = 0;
    private static final int STATE_ESCAPE = 1;
    private static final int STATE_HEX = 2;
    private static final int STATE_SKIP_WHITE_SPACE = 4;
    private static final int STATE_UNICODE = 6;

    static void checkRange(V a, V b, int num) throws ParseException {
    }

    static void checkSame(V a, V b) throws ParseException {
        if(a.code != b.code) {
            throw new LasmParseException(b.token, "Register must be " + a.token.image);
        }
    }

    public static void dump(Prototype p, String filename, boolean strip, boolean fix, String src) throws IOException {
    }

    public static String getLine(Throwable e, String file, boolean color) throws IOException {
        String ret = "";
        int line = 0;
        int column = 0;
        if(e instanceof LasmParseException) {
            line = ((LasmParseException)e).currentToken.beginLine;
            column = ((LasmParseException)e).currentToken.beginColumn;
        }
        else if(e instanceof ParseException) {
            line = ((ParseException)e).currentToken.next.beginLine;
            column = ((ParseException)e).currentToken.next.beginColumn;
        }
        else if(e instanceof TokenMgrError) {
            line = ((TokenMgrError)e).errorLine;
            column = ((TokenMgrError)e).errorColumn;
        }
        if(line > 0) {
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int i = 0;
            String s2;
            while((s2 = bufferedReader0.readLine()) != null) {
                ++i;
                if(i == line) {
                    if(color) {
                        ret = "\n" + s2.substring(0, column - 1) + '⁣' + s2.substring(column - 1) + "\n";
                        break;
                    }
                    ret = "\n" + s2 + "\n" + new String(new char[column - 1]).replace("\u0000", "_") + "^\n";
                    break;
                }
            }
            bufferedReader0.close();
        }
        return ret;
    }

    public static void main(String[] args) throws Throwable {
    }

    static int parseHex(Token n) throws ParseException {
        try {
            return (int)Long.parseLong(n.image.substring(2), 16);
        }
        catch(NumberFormatException e) {
            throw new LasmParseException(n, "Failed parse \'" + n.image + "\' as hex", e);
        }
    }

    static int parseInt(Token n) throws ParseException {
        return LasmBase.parseInt(n, 0);
    }

    static int parseInt(Token n, int skip) throws ParseException {
        try {
            return Integer.parseInt(n.image.substring(skip));
        }
        catch(NumberFormatException e) {
            throw new LasmParseException(n, "Failed parse \'" + n.image + "\' as integer", e);
        }
    }

    static int parseIntMax(Token n, int max) throws ParseException {
        int v1 = LasmBase.parseInt(n, 0);
        if(v1 > max) {
            throw new LasmParseException(n, "\'" + n.image + "\' parsed as " + v1 + " out of possible range [0; " + max + "]");
        }
        return v1;
    }

    static long parseLong(Token n) throws ParseException {
        try {
            return Long.parseLong(n.image);
        }
        catch(NumberFormatException e) {
            throw new LasmParseException(n, "Failed parse \'" + n.image + "\' as long", e);
        }
    }

    static LuaValue parseLuaNumber(Token n) throws ParseException {
        try {
            return LuaValue.parseNumber(n.image, LuaValue.ZERO);
        }
        catch(NumberFormatException e) {
            throw new LasmParseException(n, "Failed parse \'" + n.image + "\' as Lua number", e);
        }
    }

    static LuaString parseLuaString(Token str) throws ParseException {
        if(str.kind == 56) {
            return null;
        }
        byte[] arr_b = str.image.getBytes();
        Buffer buffer0 = new Buffer(arr_b.length - 2);
        int state = 0;
        int num = 0;
        int pos = 0;
        int i = 1;
        int n = arr_b.length - 1;
        while(true) {
            if(i >= n) {
                if(state == 3) {
                    buffer0.append(((byte)num));
                }
                if(state == 2 || state == 6) {
                    str.beginColumn += arr_b.length - 1;
                    throw new LasmParseException(str, "Unfinished " + (state == 6 ? "unicode" : "hex") + " escape sequence");
                }
                return buffer0.tostring();
            }
            int ch = arr_b[i];
        alab1:
            switch(state) {
                case 0: {
                    if(ch == 92) {
                        state = 1;
                    }
                    else {
                        buffer0.append(((byte)ch));
                    }
                    break;
                }
                case 1: {
                    if(0x30 > ch || ch > 57) {
                        switch(ch) {
                            case 97: {
                                state = 0;
                                buffer0.append(7);
                                break alab1;
                            }
                            case 98: {
                                state = 0;
                                buffer0.append(8);
                                break alab1;
                            }
                            case 102: {
                                state = 0;
                                buffer0.append(12);
                                break alab1;
                            }
                            case 110: {
                                state = 0;
                                buffer0.append(10);
                                break alab1;
                            }
                            case 0x72: {
                                state = 0;
                                buffer0.append(13);
                                break alab1;
                            }
                            case 0x74: {
                                state = 0;
                                buffer0.append(9);
                                break alab1;
                            }
                            case 0x76: {
                                state = 0;
                                buffer0.append(11);
                                break alab1;
                            }
                            case 0x75: 
                            case 120: {
                                pos = ch == 0x75 ? -1 : 0;
                                num = 0;
                                state = ch == 0x75 ? 6 : 2;
                                break alab1;
                            }
                            case 0x7A: {
                                state = 4;
                                break alab1;
                            }
                            default: {
                                state = 0;
                                buffer0.append(((byte)ch));
                                break alab1;
                            }
                        }
                    }
                    else {
                        pos = 1;
                        num = ch - 0x30;
                        state = 3;
                        break;
                    }
                    goto label_64;
                }
                case 3: {
                    if(0x30 > ch || ch > 57) {
                        --i;
                        state = 0;
                        buffer0.append(((byte)num));
                    }
                    else {
                        num = num * 10 + (ch - 0x30);
                        ++pos;
                        if(pos >= 3) {
                            state = 0;
                            buffer0.append(((byte)num));
                        }
                    }
                    break;
                }
                case 4: {
                    switch(ch) {
                        case 9: 
                        case 10: 
                        case 11: 
                        case 12: 
                        case 13: 
                        case 0x20: {
                            break alab1;
                        }
                        default: {
                            state = 0;
                            buffer0.append(((byte)ch));
                            break alab1;
                        }
                    }
                }
                case 2: 
                case 6: {
                label_64:
                    num *= 16;
                    if(pos != -1) {
                        if(state == 6 && ch == 0x7D) {
                            state = 0;
                            num /= 16;
                            if(num > 0x10FFFF) {
                                throw new LasmParseException(str, "UTF-8 value too large: 0x" + Integer.toHexString(num) + " (max allowed = 0x" + "10ffff" + ")");
                            }
                            buffer0.appendCodePoint(num);
                            break;
                        }
                        else if(0x30 <= ch && ch <= 57) {
                            num += ch - 0x30;
                        }
                        else if(97 <= ch && ch <= 102) {
                            num += ch - 87;
                        }
                        else if(65 > ch || ch > 70) {
                            pos += 100;
                        }
                        else {
                            num += ch - 55;
                        }
                    }
                    else if(ch != 0x7B) {
                        pos = 99;
                    }
                    ++pos;
                    if(pos >= state) {
                        if(pos == 2) {
                            state = 0;
                            buffer0.append(((byte)num));
                            break;
                        }
                        str.beginColumn += i;
                        throw new LasmParseException(str, "Invalid char \'" + ch + "\' (code 0x" + Integer.toHexString(ch) + ") in " + (state == 6 ? "unicode" : "hex") + " escape sequence, must be " + (state == 6 ? "\\u{XXX}" : "\\xXX"));
                    }
                    break;
                }
                default: {
                    buffer0.append(((byte)ch));
                }
            }
            ++i;
        }
    }

    public static void print(LuaClosure func, String filename) throws IOException {
    }
}

