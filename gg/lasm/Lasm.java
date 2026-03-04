package lasm;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import luaj.LuaLong;
import luaj.LuaValue;
import luaj.Prototype;

public class Lasm extends LasmBase implements LasmConstants {
    private List jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private static int[] jj_la1_2;
    public Token jj_nt;
    private int jj_ntk;
    public Token token;
    public LasmTokenManager token_source;

    static {
        Lasm.jj_la1_init_0();
        Lasm.jj_la1_init_1();
        Lasm.jj_la1_init_2();
    }

    public Lasm(InputStream stream) {
        this(stream, null);
    }

    public Lasm(InputStream stream, String encoding) {
        this.jj_la1 = new int[34];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.token_source = new LasmTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public Lasm(Reader stream) {
        this.jj_la1 = new int[34];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new LasmTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public Lasm(LasmTokenManager tm) {
        this.jj_la1 = new int[34];
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public final LasmPrototype Code() throws ParseException {
        LasmPrototype p = new LasmPrototype();
        this.jj_consume_token(62);
        this.jj_consume_token(81);
        p.source = Lasm.parseLuaString(this.token);
        this.jj_consume_token(74);
        this.jj_consume_token(0x3F);
        this.jj_consume_token(0x4F);
        p.linedefined = Lasm.parseInt(this.token);
        this.jj_consume_token(74);
        this.jj_consume_token(0x40);
        this.jj_consume_token(0x4F);
        p.lastlinedefined = Lasm.parseInt(this.token);
        this.jj_consume_token(74);
        this.jj_consume_token(65);
        this.jj_consume_token(0x4F);
        p.numparams = Lasm.parseIntMax(this.token, 0xFF);
        this.jj_consume_token(74);
        this.jj_consume_token(66);
        this.jj_consume_token(0x4F);
        p.is_vararg = Lasm.parseIntMax(this.token, 0xFF);
        this.jj_consume_token(74);
        this.jj_consume_token(67);
        this.jj_consume_token(0x4F);
        p.init(this.token);
        this.jj_consume_token(74);
        while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 68) {
            this.UpVal(p);
        }
        this.jj_la1[1] = this.jj_gen;
    label_29:
        this.Op(p);
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: 
            case 27: 
            case 28: 
            case 29: 
            case 30: 
            case 0x1F: 
            case 0x20: 
            case 33: 
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: 
            case 40: 
            case 41: 
            case 42: 
            case 43: 
            case 44: 
            case 45: 
            case 46: 
            case 0x2F: 
            case 0x30: 
            case 49: 
            case 53: 
            case 69: 
            case 70: 
            case 76: {
                goto label_29;
            }
            default: {
                this.jj_la1[2] = this.jj_gen;
                while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 72) {
                    this.Func(p);
                }
                this.jj_la1[3] = this.jj_gen;
                return p.build();
            }
        }
    }

    public final Token Dest() throws ParseException {
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 52: {
                this.jj_consume_token(52);
                Token token0 = this.jj_consume_token(60);
                this.jj_consume_token(89);
                return token0;
            }
            case 76: {
                return this.jj_consume_token(76);
            }
            default: {
                this.jj_la1[8] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public final void Func(LasmPrototype p) throws ParseException {
        this.jj_consume_token(72);
        Token token0 = this.jj_consume_token(61);
        this.jj_consume_token(74);
        p.addFunc(token0, this.Code());
        this.jj_consume_token(73);
        this.jj_consume_token(74);
    }

    public final LuaValue K() throws ParseException {
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 50: {
                this.jj_consume_token(50);
                this.jj_consume_token(88);
                this.jj_consume_token(0x4F);
                LuaValue k = new Const(this.token);
                this.jj_consume_token(89);
                return k;
            }
            case 56: {
                this.jj_consume_token(56);
                return LuaValue.NIL;
            }
            case 57: {
                this.jj_consume_token(57);
                return LuaValue.TRUE;
            }
            case 58: {
                this.jj_consume_token(58);
                return LuaValue.FALSE;
            }
            case 0x4F: {
                this.jj_consume_token(0x4F);
                return LuaLong.valueOf(Lasm.parseLong(this.token));
            }
            case 80: {
                this.jj_consume_token(80);
                return Lasm.parseLuaNumber(this.token);
            }
            case 81: {
                this.jj_consume_token(81);
                return Lasm.parseLuaString(this.token);
            }
            case 84: {
                this.jj_consume_token(84);
                return Lasm.parseLuaNumber(this.token);
            }
            default: {
                this.jj_la1[5] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public final void Op(LasmPrototype p) throws ParseException {
        Token op;
        LuaValue a = null;
        LuaValue b = null;
        LuaValue c = null;
        Token d = null;
        Token e = null;
        while(true) {
            int v = this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk;
            if(v != 69 && v != 70 && v != 76) {
                this.jj_la1[10] = this.jj_gen;
            alab1:
                switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                    case 6: {
                        op = this.jj_consume_token(6);
                        a = this.R();
                        b = this.K();
                        break;
                    }
                    case 7: {
                        op = this.jj_consume_token(7);
                        a = this.R();
                        d = this.jj_consume_token(0x4F);
                        if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 55) {
                            e = this.jj_consume_token(55);
                        }
                        else {
                            this.jj_la1[15] = this.jj_gen;
                        }
                        break;
                    }
                    case 9: {
                        op = this.jj_consume_token(9);
                        a = this.R();
                        b = this.U();
                        break;
                    }
                    case 10: {
                        op = this.jj_consume_token(10);
                        a = this.R();
                        b = this.U();
                        c = this.RK();
                        break;
                    }
                    case 12: {
                        op = this.jj_consume_token(12);
                        a = this.U();
                        b = this.RK();
                        c = this.RK();
                        break;
                    }
                    case 13: {
                        op = this.jj_consume_token(13);
                        a = this.R();
                        b = this.U();
                        break;
                    }
                    case 15: {
                        op = this.jj_consume_token(15);
                        a = this.R();
                        d = this.jj_consume_token(0x4F);
                        e = this.jj_consume_token(0x4F);
                        break;
                    }
                    case 11: 
                    case 16: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 11: {
                                this.jj_consume_token(11);
                                break;
                            }
                            case 16: {
                                this.jj_consume_token(16);
                                break;
                            }
                            default: {
                                this.jj_la1[17] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        b = this.R();
                        c = this.RK();
                        break;
                    }
                    case 26: {
                        op = this.jj_consume_token(26);
                        a = this.R();
                        b = this.R();
                        this.jj_consume_token(75);
                        c = this.R();
                        break;
                    }
                    case 27: {
                        op = this.jj_consume_token(27);
                        int v1 = this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk;
                        if(v1 == 59 || v1 == 77) {
                            a = this.R();
                        }
                        else {
                            this.jj_la1[21] = this.jj_gen;
                        }
                        d = this.Dest();
                        break;
                    }
                    case 28: 
                    case 29: 
                    case 30: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 28: {
                                this.jj_consume_token(28);
                                break;
                            }
                            case 29: {
                                this.jj_consume_token(29);
                                break;
                            }
                            case 30: {
                                this.jj_consume_token(30);
                                break;
                            }
                            default: {
                                this.jj_la1[23] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        d = this.jj_consume_token(0x4F);
                        a = this.RK();
                        b = this.RK();
                        break;
                    }
                    case 0x1F: {
                        op = this.jj_consume_token(0x1F);
                        a = this.R();
                        d = this.jj_consume_token(0x4F);
                        break;
                    }
                    case 0x20: {
                        op = this.jj_consume_token(0x20);
                        a = this.R();
                        b = this.R();
                        d = this.jj_consume_token(0x4F);
                        break;
                    }
                    case 33: {
                        op = this.jj_consume_token(33);
                        a = this.R();
                        if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 75) {
                            this.jj_consume_token(75);
                            b = this.R();
                        }
                        else {
                            this.jj_la1[24] = this.jj_gen;
                        }
                        int v2 = this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk;
                        if(v2 == 54 || v2 == 59 || v2 == 77) {
                            switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                                case 54: {
                                    d = this.jj_consume_token(54);
                                    break;
                                }
                                case 59: 
                                case 77: {
                                    Lasm.checkSame(((V)a), this.R());
                                    this.jj_consume_token(75);
                                    c = this.R();
                                    break;
                                }
                                default: {
                                    this.jj_la1[25] = this.jj_gen;
                                    this.jj_consume_token(-1);
                                    throw new ParseException();
                                }
                            }
                        }
                        else {
                            this.jj_la1[26] = this.jj_gen;
                        }
                        break;
                    }
                    case 35: {
                        op = this.jj_consume_token(35);
                        int v3 = this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk;
                        if(v3 == 59 || v3 == 77) {
                            a = this.R();
                            if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 75) {
                                this.jj_consume_token(75);
                                b = this.R();
                            }
                            else {
                                this.jj_la1[29] = this.jj_gen;
                            }
                        }
                        else {
                            this.jj_la1[30] = this.jj_gen;
                        }
                        break;
                    }
                    case 8: 
                    case 38: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 8: {
                                this.jj_consume_token(8);
                                break;
                            }
                            case 38: {
                                this.jj_consume_token(38);
                                break;
                            }
                            default: {
                                this.jj_la1[16] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        this.jj_consume_token(75);
                        b = this.R();
                        break;
                    }
                    case 36: 
                    case 37: 
                    case 39: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 36: {
                                this.jj_consume_token(36);
                                break;
                            }
                            case 37: {
                                this.jj_consume_token(37);
                                break;
                            }
                            case 39: {
                                this.jj_consume_token(39);
                                break;
                            }
                            default: {
                                this.jj_la1[22] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        d = this.Dest();
                        break;
                    }
                    case 40: {
                        op = this.jj_consume_token(40);
                        a = this.R();
                        if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 75) {
                            this.jj_consume_token(75);
                            b = this.R();
                        }
                        else {
                            this.jj_la1[18] = this.jj_gen;
                        }
                        d = this.jj_consume_token(0x4F);
                        break;
                    }
                    case 41: {
                        op = this.jj_consume_token(41);
                        a = this.R();
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 51: {
                                this.jj_consume_token(51);
                                d = this.jj_consume_token(0x4F);
                                this.jj_consume_token(89);
                                break alab1;
                            }
                            case 61: {
                                d = this.jj_consume_token(61);
                                break alab1;
                            }
                            default: {
                                this.jj_la1[0x1F] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                    }
                    case 34: 
                    case 42: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 34: {
                                this.jj_consume_token(34);
                                break;
                            }
                            case 42: {
                                this.jj_consume_token(42);
                                break;
                            }
                            default: {
                                this.jj_la1[27] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 75) {
                            this.jj_consume_token(75);
                            b = this.R();
                        }
                        else {
                            this.jj_la1[28] = this.jj_gen;
                        }
                        break;
                    }
                    case 5: 
                    case 23: 
                    case 24: 
                    case 25: 
                    case 44: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 5: {
                                this.jj_consume_token(5);
                                break;
                            }
                            case 23: {
                                this.jj_consume_token(23);
                                break;
                            }
                            case 24: {
                                this.jj_consume_token(24);
                                break;
                            }
                            case 25: {
                                this.jj_consume_token(25);
                                break;
                            }
                            case 44: {
                                this.jj_consume_token(44);
                                break;
                            }
                            default: {
                                this.jj_la1[20] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        b = this.R();
                        break;
                    }
                    case 14: 
                    case 17: 
                    case 18: 
                    case 19: 
                    case 20: 
                    case 21: 
                    case 22: 
                    case 43: 
                    case 45: 
                    case 46: 
                    case 0x2F: 
                    case 0x30: 
                    case 49: {
                        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                            case 14: {
                                this.jj_consume_token(14);
                                break;
                            }
                            case 17: {
                                this.jj_consume_token(17);
                                break;
                            }
                            case 18: {
                                this.jj_consume_token(18);
                                break;
                            }
                            case 19: {
                                this.jj_consume_token(19);
                                break;
                            }
                            case 20: {
                                this.jj_consume_token(20);
                                break;
                            }
                            case 21: {
                                this.jj_consume_token(21);
                                break;
                            }
                            case 22: {
                                this.jj_consume_token(22);
                                break;
                            }
                            case 43: {
                                this.jj_consume_token(43);
                                break;
                            }
                            case 45: {
                                this.jj_consume_token(45);
                                break;
                            }
                            case 46: {
                                this.jj_consume_token(46);
                                break;
                            }
                            case 0x2F: {
                                this.jj_consume_token(0x2F);
                                break;
                            }
                            case 0x30: {
                                this.jj_consume_token(0x30);
                                break;
                            }
                            case 49: {
                                this.jj_consume_token(49);
                                break;
                            }
                            default: {
                                this.jj_la1[19] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                        }
                        op = this.token;
                        a = this.R();
                        b = this.RK();
                        c = this.RK();
                        break;
                    }
                    case 53: {
                        this.jj_consume_token(53);
                        this.jj_consume_token(88);
                        op = this.jj_consume_token(0x4F);
                        this.jj_consume_token(89);
                        d = this.jj_consume_token(80);
                        break;
                    }
                    default: {
                        this.jj_la1[0x20] = this.jj_gen;
                        this.jj_consume_token(-1);
                        throw new ParseException();
                    }
                }
                this.jj_consume_token(74);
                while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 71) {
                    this.jj_consume_token(71);
                    V lasmBase$V0 = this.R();
                    this.jj_consume_token(81);
                    p.endLocal(lasmBase$V0, this.token, 1);
                    this.jj_consume_token(74);
                }
                this.jj_la1[33] = this.jj_gen;
                p.addOp(op, a, b, c, d, e);
                return;
            }
            switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
                case 69: {
                    this.jj_consume_token(69);
                    this.jj_consume_token(0x4F);
                    p.addLine(this.token);
                    this.jj_consume_token(74);
                    while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 71) {
                        this.jj_consume_token(71);
                        V lasmBase$V1 = this.R();
                        this.jj_consume_token(81);
                        p.endLocal(lasmBase$V1, this.token, 0);
                        this.jj_consume_token(74);
                    }
                    this.jj_la1[13] = this.jj_gen;
                    break;
                }
                case 70: {
                    this.jj_consume_token(70);
                    V lasmBase$V2 = this.R();
                    this.jj_consume_token(81);
                    p.startLocal(lasmBase$V2, this.token);
                    this.jj_consume_token(74);
                    while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 71) {
                        this.jj_consume_token(71);
                        V v = this.R();
                        this.jj_consume_token(81);
                        p.endLocal(v, this.token, 0);
                        this.jj_consume_token(74);
                    }
                    this.jj_la1[12] = this.jj_gen;
                    break;
                }
                case 76: {
                    this.jj_consume_token(76);
                    p.addLabel(this.token);
                    this.jj_consume_token(74);
                    while((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 71) {
                        this.jj_consume_token(71);
                        V lasmBase$V4 = this.R();
                        this.jj_consume_token(81);
                        p.endLocal(lasmBase$V4, this.token, 0);
                        this.jj_consume_token(74);
                    }
                    this.jj_la1[11] = this.jj_gen;
                    break;
                }
                default: {
                    this.jj_la1[14] = this.jj_gen;
                    this.jj_consume_token(-1);
                    throw new ParseException();
                }
            }
        }
    }

    public final V R() throws ParseException {
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 59: {
                this.jj_consume_token(59);
                return new V(this.token);
            }
            case 77: {
                this.jj_consume_token(77);
                return new V(this.token);
            }
            default: {
                this.jj_la1[7] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public final LuaValue RK() throws ParseException {
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 59: 
            case 77: {
                return this.R();
            }
            case 50: 
            case 56: 
            case 57: 
            case 58: 
            case 0x4F: 
            case 80: 
            case 81: 
            case 84: {
                return this.K();
            }
            default: {
                this.jj_la1[4] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public final Internal RU() throws ParseException {
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 59: 
            case 77: {
                return this.R();
            }
            case 78: {
                return this.U();
            }
            default: {
                this.jj_la1[6] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public void ReInit(InputStream stream) {
        this.ReInit(stream, null);
    }

    public void ReInit(InputStream stream, String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(LasmTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for(int i = 0; i < 34; ++i) {
            this.jj_la1[i] = -1;
        }
    }

    public final U U() throws ParseException {
        this.jj_consume_token(78);
        return new U(this.token);
    }

    public final void UpVal(LasmPrototype p) throws ParseException {
        Token token0;
        this.jj_consume_token(68);
        Internal lasmBase$Internal0 = this.RU();
        switch((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk)) {
            case 56: {
                token0 = this.jj_consume_token(56);
                break;
            }
            case 81: {
                token0 = this.jj_consume_token(81);
                break;
            }
            default: {
                this.jj_la1[9] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
        }
        this.jj_consume_token(74);
        p.addUpval(lasmBase$Internal0, token0);
    }

    public final Prototype assemble() throws ParseException {
        if((this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) == 74) {
            this.jj_consume_token(74);
        }
        else {
            this.jj_la1[0] = this.jj_gen;
        }
        Prototype prototype0 = this.Code();
        this.jj_consume_token(0);
        return prototype0;
    }

    public final void disable_tracing() {
    }

    public final void enable_tracing() {
    }

    public ParseException generateParseException() {
        this.jj_expentries.clear();
        boolean[] la1tokens = new boolean[90];
        if(this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for(int i = 0; i < 34; ++i) {
            if(this.jj_la1[i] == this.jj_gen) {
                for(int j = 0; j < 0x20; ++j) {
                    if((Lasm.jj_la1_0[i] & 1 << j) != 0) {
                        la1tokens[j] = true;
                    }
                    if((Lasm.jj_la1_1[i] & 1 << j) != 0) {
                        la1tokens[j + 0x20] = true;
                    }
                    if((Lasm.jj_la1_2[i] & 1 << j) != 0) {
                        la1tokens[j + 0x40] = true;
                    }
                }
            }
        }
        for(int i = 0; i < 90; ++i) {
            if(la1tokens[i]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i;
                this.jj_expentries.add(this.jj_expentry);
            }
        }
        int[][] exptokseq = new int[this.jj_expentries.size()][];
        for(int i = 0; i < this.jj_expentries.size(); ++i) {
            exptokseq[i] = (int[])this.jj_expentries.get(i);
        }
        return new ParseException(this.token, exptokseq, Lasm.tokenImage);
    }

    public final Token getNextToken() {
        if(this.token.next == null) {
            Token token0 = this.token;
            Token token1 = this.token_source.getNextToken();
            token0.next = token1;
            this.token = token1;
        }
        else {
            this.token = this.token.next;
        }
        this.jj_ntk = -1;
        ++this.jj_gen;
        return this.token;
    }

    public final Token getToken(int index) {
        Token t;
        int i = 0;
        Token t;
        for(t = this.token; i < index; t = t) {
            if(t.next == null) {
                t = this.token_source.getNextToken();
                t.next = t;
            }
            else {
                t = t.next;
            }
            ++i;
        }
        return t;
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if(oldToken.next == null) {
            Token token1 = this.token;
            Token token2 = this.token_source.getNextToken();
            token1.next = token2;
            this.token = token2;
        }
        else {
            this.token = this.token.next;
        }
        this.jj_ntk = -1;
        if(this.token.kind == kind) {
            ++this.jj_gen;
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw this.generateParseException();
    }

    private static void jj_la1_init_0() {
        int[] arr_v = new int[34];
        arr_v[2] = 0xFFFFFFE0;
        arr_v[16] = 0x100;
        arr_v[17] = 0x10800;
        arr_v[19] = 0x7E4000;
        arr_v[20] = 0x3800020;
        arr_v[23] = 0x70000000;
        arr_v[0x20] = 0xFFFFFFE0;
        Lasm.jj_la1_0 = arr_v;
    }

    private static void jj_la1_init_1() {
        Lasm.jj_la1_1 = new int[]{0, 0, 0x23FFFF, 0, 0xF040000, 0x7040000, 0x8000000, 0x8000000, 0x100000, 0x1000000, 0, 0, 0, 0, 0, 0x800000, 0x40, 0, 0, 256000, 0x1000, 0x8000000, 0xB0, 0, 0, 0x8400000, 0x8400000, 0x404, 0, 0, 0x8000000, 0x20080000, 0x23FFFF, 0};
    }

    private static void jj_la1_init_2() {
        Lasm.jj_la1_2 = new int[]{0x400, 16, 0x1060, 0x100, 0x13A000, 0x138000, 0x6000, 0x2000, 0x1000, 0x20000, 0x1060, 0x80, 0x80, 0x80, 0x1060, 0, 0, 0, 0x800, 0, 0, 0x2000, 0, 0, 0x800, 0x2000, 0x2000, 0, 0x800, 0x800, 0x2000, 0, 0, 0x80};
    }

    private int jj_ntk() {
        this.jj_nt = this.token.next;
        if(this.token.next == null) {
            Token token0 = this.token;
            Token token1 = this.token_source.getNextToken();
            token0.next = token1;
            this.jj_ntk = token1.kind;
            return token1.kind;
        }
        this.jj_ntk = this.jj_nt.kind;
        return this.jj_nt.kind;
    }
}

