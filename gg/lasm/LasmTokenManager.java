package lasm;

import java.io.IOException;
import java.io.PrintStream;

public class LasmTokenManager implements LasmConstants {
    protected char curChar;
    int curLexState;
    public PrintStream debugStream;
    int defaultLexState;
    protected SimpleCharStream input_stream;
    static final long[] jjbitVec0;
    static final long[] jjbitVec2;
    int jjmatchedKind;
    int jjmatchedPos;
    public static final int[] jjnewLexState;
    int jjnewStateCnt;
    static final int[] jjnextStates;
    int jjround;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    public static final String[] jjstrLiteralImages;
    static final long[] jjtoSkip;
    static final long[] jjtoSpecial;
    static final long[] jjtoToken;
    public static final String[] lexStateNames;

    static {
        LasmTokenManager.jjbitVec0 = new long[]{-2L, -1L, -1L, -1L};
        LasmTokenManager.jjbitVec2 = new long[]{0L, 0L, -1L, -1L};
        LasmTokenManager.jjnextStates = new int[]{3, 0, 5, 49, 33, 34, 39, 40, 44, 45, 1, 2, 1, 2, 0, 5, 46, 3, 0, 5, 27, 0x20, 22, 24, 25, 17, 19, 20, 33, 34, 39, 40, 30, 0x1F, 37, 38, 41, 42, 50, 0x1F, 0x20, 37, 38, 42, 43, 1, 2, 44, 3, 0, 5, 25, 30, 20, 22, 23, 15, 17, 18, 0x1F, 0x20, 37, 38, 0x2F, 0x30, 28, 29, 35, 36, 39, 40, 37, 50, 39, 40, 45, 46, 0x20, 33, 1, 2, 37, 26, 38, 34, 3, 0, 5, 39, 40, 45, 46, 29, 30, 43, 44, 51, 35, 36, 41, 42, 46, 0x2F, 3, 4, 3, 4, 2, 7, 0x30, 5, 2, 7, 29, 34, 24, 26, 27, 19, 21, 22, 5, 2, 7, 35, 36, 41, 42, 0x20, 33};
        LasmTokenManager.jjstrLiteralImages = new String[]{"", null, null, null, null, "MOVE", "LOADK", "LOADBOOL", "LOADNIL", "GETUPVAL", "GETTABUP", "GETTABLE", "SETTABUP", "SETUPVAL", "SETTABLE", "NEWTABLE", "SELF", "ADD", "SUB", "MUL", "DIV", "MOD", "POW", "UNM", "NOT", "LEN", "CONCAT", "JMP", "EQ", "LT", "LE", "TEST", "TESTSET", "CALL", "TAILCALL", "RETURN", "FORLOOP", "FORPREP", "TFORCALL", "TFORLOOP", "SETLIST", "CLOSURE", "VARARG", "IDIV", "BNOT", "BAND", "BOR", "BXOR", "SHL", "SHR", "CONST", "FUNC[", "GOTO[", "OP", "SET_TOP", "SKIP_NEXT", "nil", "true", "false", null, null, null, ".source", ".linedefined", ".lastlinedefined", ".numparams", ".is_vararg", ".maxstacksize", ".upval", ".line", ".local", ".end local", ".func", ".end", null, "..", null, null, null, null, null, null, null, null, null, null, null, null, "[", "]"};
        LasmTokenManager.lexStateNames = new String[]{"STATE_NAME", "STATE_SINT", "STATE_V_NAME", "DEFAULT"};
        LasmTokenManager.jjnewLexState = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, 0, 3, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, 3, -1, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        LasmTokenManager.jjtoToken = new long[]{0xFFFFFFFFFFFFFFE1L, 0x313FFFFL};
        LasmTokenManager.jjtoSkip = new long[]{30L, 0L};
        LasmTokenManager.jjtoSpecial = new long[]{16L, 0L};
    }

    public LasmTokenManager(SimpleCharStream stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[52];
        this.jjstateSet = new int[104];
        this.curLexState = 3;
        this.defaultLexState = 3;
        this.input_stream = stream;
    }

    public LasmTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        this.SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        this.ReInitRounds();
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        this.ReInit(stream);
        this.SwitchTo(lexState);
    }

    private void ReInitRounds() {
        this.jjround = 0x80000001;
        for(int i = 52; i > 0; --i) {
            this.jjrounds[i - 1] = 0x80000000;
        }
    }

    public void SwitchTo(int lexState) {
        if(lexState >= 4 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }

    public Token getNextToken() {
        Token specialToken = null;
        int curPos = 0;
        while(true) {
            try {
            label_2:
                this.curChar = this.input_stream.BeginToken();
                switch(this.curLexState) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        this.jjmatchedKind = 0x7FFFFFFF;
                        this.jjmatchedPos = 0;
                        curPos = this.jjMoveStringLiteralDfa0_1();
                        goto label_24;
                    }
                    case 2: {
                        this.jjmatchedKind = 0x7FFFFFFF;
                        this.jjmatchedPos = 0;
                        curPos = this.jjMoveStringLiteralDfa0_2();
                        goto label_24;
                    }
                    case 3: {
                        goto label_21;
                    }
                    default: {
                        goto label_24;
                    }
                }
            }
            catch(IOException unused_ex) {
                this.jjmatchedKind = 0;
                Token token1 = this.jjFillToken();
                token1.specialToken = specialToken;
                return token1;
            }
            this.jjmatchedKind = 0x7FFFFFFF;
            this.jjmatchedPos = 0;
            curPos = this.jjMoveStringLiteralDfa0_0();
            goto label_24;
        label_21:
            this.jjmatchedKind = 0x7FFFFFFF;
            this.jjmatchedPos = 0;
            curPos = this.jjMoveStringLiteralDfa0_3();
        label_24:
            if(this.jjmatchedKind == 0x7FFFFFFF) {
                break;
            }
            if(this.jjmatchedPos + 1 < curPos) {
                this.input_stream.backup(curPos - this.jjmatchedPos - 1);
            }
            if((LasmTokenManager.jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
                Token token2 = this.jjFillToken();
                token2.specialToken = specialToken;
                if(LasmTokenManager.jjnewLexState[this.jjmatchedKind] != -1) {
                    this.curLexState = LasmTokenManager.jjnewLexState[this.jjmatchedKind];
                }
                return token2;
            }
            if((LasmTokenManager.jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
                Token token3 = this.jjFillToken();
                if(specialToken != null) {
                    token3.specialToken = specialToken;
                    specialToken.next = token3;
                }
                specialToken = token3;
            }
            if(LasmTokenManager.jjnewLexState[this.jjmatchedKind] == -1) {
                goto label_2;
            }
            this.curLexState = LasmTokenManager.jjnewLexState[this.jjmatchedKind];
        }
        int error_line = this.input_stream.getEndLine();
        int error_column = this.input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try {
            this.input_stream.readChar();
            this.input_stream.backup(1);
        }
        catch(IOException unused_ex) {
            EOFSeen = true;
            error_after = curPos > 1 ? this.input_stream.GetImage() : "";
            if(this.curChar != 10 && this.curChar != 13) {
                ++error_column;
            }
            else {
                ++error_line;
                error_column = 0;
            }
        }
        if(!EOFSeen) {
            this.input_stream.backup(1);
            error_after = curPos > 1 ? this.input_stream.GetImage() : "";
        }
        throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
    }

    private void jjAddStates(int start, int end) {
        while(true) {
            int v2 = this.jjnewStateCnt;
            this.jjnewStateCnt = v2 + 1;
            this.jjstateSet[v2] = LasmTokenManager.jjnextStates[start];
            if(start == end) {
                break;
            }
            ++start;
        }
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
        return hiByte == 0 ? (LasmTokenManager.jjbitVec2[i2] & l2) != 0L : (LasmTokenManager.jjbitVec0[i1] & l1) != 0L;
    }

    private void jjCheckNAdd(int state) {
        if(this.jjrounds[state] != this.jjround) {
            int v1 = this.jjnewStateCnt;
            this.jjnewStateCnt = v1 + 1;
            this.jjstateSet[v1] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private void jjCheckNAddStates(int start, int end) {
        while(true) {
            this.jjCheckNAdd(LasmTokenManager.jjnextStates[start]);
            if(start == end) {
                break;
            }
            ++start;
        }
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        this.jjCheckNAdd(state1);
        this.jjCheckNAdd(state2);
    }

    protected Token jjFillToken() {
        String im = LasmTokenManager.jjstrLiteralImages[this.jjmatchedKind];
        String s1 = im == null ? this.input_stream.GetImage() : im;
        int v = this.input_stream.getBeginLine();
        int v1 = this.input_stream.getBeginColumn();
        int v2 = this.input_stream.getEndLine();
        int v3 = this.input_stream.getEndColumn();
        Token token0 = Token.newToken(this.jjmatchedKind, s1);
        token0.beginLine = v;
        token0.endLine = v2;
        token0.beginColumn = v1;
        token0.endColumn = v3;
        return token0;
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 52;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = 0x7FFFFFFF;
        while(true) {
            int v5 = this.jjround + 1;
            this.jjround = v5;
            if(v5 == 0x7FFFFFFF) {
                this.ReInitRounds();
            }
            if(this.curChar < 0x40) {
                long l = 1L << this.curChar;
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 0: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(0x60, 100);
                            }
                            else if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(101, 104);
                            }
                            else if((0x2400L & l) == 0L) {
                                switch(this.curChar) {
                                    case 34: {
                                        this.jjCheckNAddStates(0x76, 120);
                                        break;
                                    }
                                    case 39: {
                                        this.jjCheckNAddStates(0x73, 0x75);
                                        break;
                                    }
                                    case 45: {
                                        this.jjAddStates(0x71, 0x72);
                                        break;
                                    }
                                    case 46: {
                                        this.jjCheckNAdd(30);
                                        break;
                                    }
                                    case 58: {
                                        int v7 = this.jjnewStateCnt;
                                        this.jjnewStateCnt = v7 + 1;
                                        this.jjstateSet[v7] = 9;
                                        break;
                                    }
                                    case 59: {
                                        if(kind > 4) {
                                            kind = 4;
                                        }
                                        this.jjCheckNAddStates(109, 0x70);
                                    }
                                }
                            }
                            else {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(105, 108);
                            }
                            switch(this.curChar) {
                                case 13: {
                                    int v8 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v8 + 1;
                                    this.jjstateSet[v8] = 6;
                                    break;
                                }
                                case 0x30: {
                                    int v9 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v9 + 1;
                                    this.jjstateSet[v9] = 16;
                                }
                            }
                            break;
                        }
                        case 1: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 61) {
                                    kind = 61;
                                }
                                int v10 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v10 + 1;
                                this.jjstateSet[v10] = 1;
                            }
                            break;
                        }
                        case 2: {
                            if((0x2400L & l) != 0L) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(105, 108);
                            }
                            break;
                        }
                        case 3: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(3, 4);
                            }
                            break;
                        }
                        case 4: {
                            if(this.curChar == 59) {
                                this.jjCheckNAddStates(0x79, 0x7B);
                            }
                            break;
                        }
                        case 5: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                this.jjCheckNAddStates(0x79, 0x7B);
                            }
                            break;
                        }
                        case 6: {
                            if(this.curChar == 10) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(105, 108);
                            }
                            break;
                        }
                        case 7: {
                            if(this.curChar == 13) {
                                int v11 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v11 + 1;
                                this.jjstateSet[v11] = 6;
                            }
                            break;
                        }
                        case 8: {
                            if(this.curChar == 58) {
                                int v12 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v12 + 1;
                                this.jjstateSet[v12] = 9;
                            }
                            break;
                        }
                        case 10: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                int v13 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v13 + 1;
                                this.jjstateSet[v13] = 10;
                            }
                            break;
                        }
                        case 12: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 77) {
                                    kind = 77;
                                }
                                int v14 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v14 + 1;
                                this.jjstateSet[v14] = 12;
                            }
                            break;
                        }
                        case 14: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 78) {
                                    kind = 78;
                                }
                                int v15 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v15 + 1;
                                this.jjstateSet[v15] = 14;
                            }
                            break;
                        }
                        case 15: {
                            if(this.curChar == 0x30) {
                                int v16 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v16 + 1;
                                this.jjstateSet[v16] = 16;
                            }
                            break;
                        }
                        case 17: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                int v17 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v17 + 1;
                                this.jjstateSet[v17] = 17;
                            }
                            break;
                        }
                        case 18: {
                            if(this.curChar == 34) {
                                this.jjCheckNAddStates(0x76, 120);
                            }
                            break;
                        }
                        case 20: {
                            this.jjCheckNAddStates(0x76, 120);
                            break;
                        }
                        case 21: {
                            if((0xFFFFFFFBFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(0x76, 120);
                            }
                            break;
                        }
                        case 22: {
                            if(this.curChar == 34 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 23: {
                            if(this.curChar == 39) {
                                this.jjCheckNAddStates(0x73, 0x75);
                            }
                            break;
                        }
                        case 25: {
                            this.jjCheckNAddStates(0x73, 0x75);
                            break;
                        }
                        case 26: {
                            if((0xFFFFFF7FFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(0x73, 0x75);
                            }
                            break;
                        }
                        case 27: {
                            if(this.curChar == 39 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 28: {
                            if(this.curChar == 45) {
                                this.jjAddStates(0x71, 0x72);
                            }
                            break;
                        }
                        case 29: {
                            if(this.curChar == 46) {
                                this.jjCheckNAdd(30);
                            }
                            break;
                        }
                        case 30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(30, 0x1F);
                            }
                            break;
                        }
                        case 0x20: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(33);
                            }
                            break;
                        }
                        case 33: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(33);
                            }
                            break;
                        }
                        case 34: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddStates(0x7C, 0x7F);
                            }
                            break;
                        }
                        case 35: {
                            if((0x3FF000000000000L & l) != 0L) {
                                this.jjCheckNAddTwoStates(35, 36);
                            }
                            break;
                        }
                        case 36: {
                            if(this.curChar == 46) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(37, 38);
                            }
                            break;
                        }
                        case 37: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(37, 38);
                            }
                            break;
                        }
                        case 39: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(40);
                            }
                            break;
                        }
                        case 40: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(40);
                            }
                            break;
                        }
                        case 41: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(41, 42);
                            }
                            break;
                        }
                        case 43: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 44: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 45: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(101, 104);
                            }
                            break;
                        }
                        case 46: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(46, 0x2F);
                            }
                            break;
                        }
                        case 0x2F: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(0x30);
                            }
                            break;
                        }
                        case 0x30: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(0x30);
                            }
                            break;
                        }
                        case 49: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAddStates(109, 0x70);
                            }
                            break;
                        }
                        case 50: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(0x60, 100);
                            }
                            break;
                        }
                        case 51: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAdd(51);
                            }
                            break;
                        }
                        case 52: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(3, 4);
                            }
                            else if(this.curChar == 59) {
                                this.jjCheckNAddStates(0x79, 0x7B);
                            }
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(46, 0x2F);
                            }
                            else if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(0x30);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else if(this.curChar < 0x80) {
                long l = 1L << (this.curChar & 0x3F);
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 0: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 61) {
                                    kind = 61;
                                }
                                this.jjCheckNAdd(1);
                            }
                            switch(this.curChar) {
                                case 0x75: {
                                    int v19 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v19 + 1;
                                    this.jjstateSet[v19] = 14;
                                    break;
                                }
                                case 0x76: {
                                    int v20 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v20 + 1;
                                    this.jjstateSet[v20] = 12;
                                }
                            }
                            break;
                        }
                        case 1: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 61) {
                                    kind = 61;
                                }
                                this.jjCheckNAdd(1);
                            }
                            break;
                        }
                        case 5: {
                            this.jjAddStates(0x79, 0x7B);
                            break;
                        }
                        case 9: 
                        case 10: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                this.jjCheckNAdd(10);
                            }
                            break;
                        }
                        case 11: {
                            if(this.curChar == 0x76) {
                                int v21 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v21 + 1;
                                this.jjstateSet[v21] = 12;
                            }
                            break;
                        }
                        case 13: {
                            if(this.curChar == 0x75) {
                                int v22 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v22 + 1;
                                this.jjstateSet[v22] = 14;
                            }
                            break;
                        }
                        case 16: {
                            if((0x100000001000000L & l) != 0L) {
                                this.jjCheckNAdd(17);
                            }
                            break;
                        }
                        case 17: {
                            if((0x7E0000007EL & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                this.jjCheckNAdd(17);
                            }
                            break;
                        }
                        case 19: {
                            if(this.curChar == 92) {
                                int v23 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v23 + 1;
                                this.jjstateSet[v23] = 20;
                            }
                            break;
                        }
                        case 20: {
                            this.jjCheckNAddStates(0x76, 120);
                            break;
                        }
                        case 21: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(0x76, 120);
                            }
                            break;
                        }
                        case 24: {
                            if(this.curChar == 92) {
                                int v24 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v24 + 1;
                                this.jjstateSet[v24] = 25;
                            }
                            break;
                        }
                        case 25: {
                            this.jjCheckNAddStates(0x73, 0x75);
                            break;
                        }
                        case 26: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(0x73, 0x75);
                            }
                            break;
                        }
                        case 0x1F: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(0x80, 0x81);
                            }
                            break;
                        }
                        case 38: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(69, 70);
                            }
                            break;
                        }
                        case 42: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(94, 0x5F);
                            }
                            break;
                        }
                        case 0x30: {
                            if(kind > 4) {
                                kind = 4;
                            }
                            int v25 = this.jjnewStateCnt;
                            this.jjnewStateCnt = v25 + 1;
                            this.jjstateSet[v25] = 0x30;
                        }
                    }
                }
                while(i != startsAt);
            }
            else {
                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3F);
                int i2 = (this.curChar & 0xFF) >> 6;
                long l2 = 1L << (this.curChar & 0x3F);
                while(true) {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 5: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjAddStates(0x79, 0x7B);
                            }
                            break;
                        }
                        case 20: 
                        case 21: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(0x76, 120);
                            }
                            break;
                        }
                        case 25: 
                        case 26: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(0x73, 0x75);
                            }
                            break;
                        }
                        case 0x30: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                int v31 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v31 + 1;
                                this.jjstateSet[v31] = 0x30;
                            }
                        }
                    }
                    if(i == startsAt) {
                        break;
                    }
                }
            }
            if(kind != 0x7FFFFFFF) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = 0x7FFFFFFF;
            }
            ++curPos;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 52 - startsAt;
            if(i == startsAt) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
                continue;
            }
            catch(IOException unused_ex) {
            }
            break;
        }
        return curPos;
    }

    private int jjMoveNfa_1(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 51;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = 0x7FFFFFFF;
        while(true) {
            int v5 = this.jjround + 1;
            this.jjround = v5;
            if(v5 == 0x7FFFFFFF) {
                this.ReInitRounds();
            }
            if(this.curChar < 0x40) {
                long l = 1L << this.curChar;
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 0: {
                            if((0x2400L & l) != 0L) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 1: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            break;
                        }
                        case 2: {
                            if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 3: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 4: {
                            if(this.curChar == 10) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 5: {
                            if(this.curChar == 13) {
                                int v7 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v7 + 1;
                                this.jjstateSet[v7] = 4;
                            }
                            break;
                        }
                        case 6: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 60) {
                                    kind = 60;
                                }
                                this.jjCheckNAddStates(71, 76);
                            }
                            else if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(77, 80);
                            }
                            else if((0x2400L & l) == 0L) {
                                switch(this.curChar) {
                                    case 34: {
                                        this.jjCheckNAddStates(25, 27);
                                        break;
                                    }
                                    case 39: {
                                        this.jjCheckNAddStates(22, 24);
                                        break;
                                    }
                                    case 45: {
                                        this.jjCheckNAddStates(81, 83);
                                        break;
                                    }
                                    case 46: {
                                        this.jjCheckNAdd(27);
                                        break;
                                    }
                                    case 58: {
                                        int v8 = this.jjnewStateCnt;
                                        this.jjnewStateCnt = v8 + 1;
                                        this.jjstateSet[v8] = 7;
                                        break;
                                    }
                                    case 59: {
                                        if(kind > 4) {
                                            kind = 4;
                                        }
                                        this.jjCheckNAddStates(84, 87);
                                    }
                                }
                            }
                            else {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            switch(this.curChar) {
                                case 13: {
                                    int v9 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v9 + 1;
                                    this.jjstateSet[v9] = 4;
                                    break;
                                }
                                case 0x30: {
                                    int v10 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v10 + 1;
                                    this.jjstateSet[v10] = 14;
                                }
                            }
                            break;
                        }
                        case 8: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                int v11 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v11 + 1;
                                this.jjstateSet[v11] = 8;
                            }
                            break;
                        }
                        case 10: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 77) {
                                    kind = 77;
                                }
                                int v12 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v12 + 1;
                                this.jjstateSet[v12] = 10;
                            }
                            break;
                        }
                        case 12: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 78) {
                                    kind = 78;
                                }
                                int v13 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v13 + 1;
                                this.jjstateSet[v13] = 12;
                            }
                            break;
                        }
                        case 13: {
                            if(this.curChar == 0x30) {
                                int v14 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v14 + 1;
                                this.jjstateSet[v14] = 14;
                            }
                            break;
                        }
                        case 15: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                int v15 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v15 + 1;
                                this.jjstateSet[v15] = 15;
                            }
                            break;
                        }
                        case 16: {
                            if(this.curChar == 34) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 18: {
                            this.jjCheckNAddStates(25, 27);
                            break;
                        }
                        case 19: {
                            if((0xFFFFFFFBFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 20: {
                            if(this.curChar == 34 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 21: {
                            if(this.curChar == 39) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 23: {
                            this.jjCheckNAddStates(22, 24);
                            break;
                        }
                        case 24: {
                            if((0xFFFFFF7FFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 25: {
                            if(this.curChar == 39 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 26: {
                            if(this.curChar == 46) {
                                this.jjCheckNAdd(27);
                            }
                            break;
                        }
                        case 27: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(27, 28);
                            }
                            break;
                        }
                        case 29: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(30);
                            }
                            break;
                        }
                        case 30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(30);
                            }
                            break;
                        }
                        case 0x1F: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(77, 80);
                            }
                            break;
                        }
                        case 0x20: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(0x20, 33);
                            }
                            break;
                        }
                        case 33: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(34);
                            }
                            break;
                        }
                        case 34: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(34);
                            }
                            break;
                        }
                        case 35: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAddStates(84, 87);
                            }
                            break;
                        }
                        case 36: {
                            if(this.curChar == 45) {
                                this.jjCheckNAddStates(81, 83);
                            }
                            break;
                        }
                        case 37: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 60) {
                                    kind = 60;
                                }
                                this.jjCheckNAdd(37);
                            }
                            break;
                        }
                        case 38: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddStates(88, 91);
                            }
                            break;
                        }
                        case 39: {
                            if((0x3FF000000000000L & l) != 0L) {
                                this.jjCheckNAddTwoStates(39, 40);
                            }
                            break;
                        }
                        case 40: {
                            if(this.curChar == 46) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(41, 42);
                            }
                            break;
                        }
                        case 41: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(41, 42);
                            }
                            break;
                        }
                        case 43: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 44: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 45: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(45, 46);
                            }
                            break;
                        }
                        case 0x2F: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(0x30);
                            }
                            break;
                        }
                        case 0x30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(0x30);
                            }
                            break;
                        }
                        case 49: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 60) {
                                    kind = 60;
                                }
                                this.jjCheckNAddStates(71, 76);
                            }
                            break;
                        }
                        case 50: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAdd(50);
                            }
                            break;
                        }
                        case 51: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            else if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(0x20, 33);
                            }
                            else if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(34);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else if(this.curChar < 0x80) {
                long l = 1L << (this.curChar & 0x3F);
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            this.jjAddStates(0, 2);
                            break;
                        }
                        case 6: {
                            switch(this.curChar) {
                                case 0x75: {
                                    int v17 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v17 + 1;
                                    this.jjstateSet[v17] = 12;
                                    break;
                                }
                                case 0x76: {
                                    int v18 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v18 + 1;
                                    this.jjstateSet[v18] = 10;
                                }
                            }
                            break;
                        }
                        case 7: 
                        case 8: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                this.jjCheckNAdd(8);
                            }
                            break;
                        }
                        case 9: {
                            if(this.curChar == 0x76) {
                                int v19 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v19 + 1;
                                this.jjstateSet[v19] = 10;
                            }
                            break;
                        }
                        case 11: {
                            if(this.curChar == 0x75) {
                                int v20 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v20 + 1;
                                this.jjstateSet[v20] = 12;
                            }
                            break;
                        }
                        case 14: {
                            if((0x100000001000000L & l) != 0L) {
                                this.jjCheckNAdd(15);
                            }
                            break;
                        }
                        case 15: {
                            if((0x7E0000007EL & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                this.jjCheckNAdd(15);
                            }
                            break;
                        }
                        case 17: {
                            if(this.curChar == 92) {
                                int v21 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v21 + 1;
                                this.jjstateSet[v21] = 18;
                            }
                            break;
                        }
                        case 18: {
                            this.jjCheckNAddStates(25, 27);
                            break;
                        }
                        case 19: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 22: {
                            if(this.curChar == 92) {
                                int v22 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v22 + 1;
                                this.jjstateSet[v22] = 23;
                            }
                            break;
                        }
                        case 23: {
                            this.jjCheckNAddStates(22, 24);
                            break;
                        }
                        case 24: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 28: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(92, 93);
                            }
                            break;
                        }
                        case 34: {
                            if(kind > 4) {
                                kind = 4;
                            }
                            int v23 = this.jjnewStateCnt;
                            this.jjnewStateCnt = v23 + 1;
                            this.jjstateSet[v23] = 34;
                            break;
                        }
                        case 42: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(94, 0x5F);
                            }
                            break;
                        }
                        case 46: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(0x3F, 0x40);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else {
                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3F);
                int i2 = (this.curChar & 0xFF) >> 6;
                long l2 = 1L << (this.curChar & 0x3F);
                while(true) {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjAddStates(0, 2);
                            }
                            break;
                        }
                        case 18: 
                        case 19: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 23: 
                        case 24: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 34: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                int v29 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v29 + 1;
                                this.jjstateSet[v29] = 34;
                            }
                        }
                    }
                    if(i == startsAt) {
                        break;
                    }
                }
            }
            if(kind != 0x7FFFFFFF) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = 0x7FFFFFFF;
            }
            ++curPos;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 51 - startsAt;
            if(i == startsAt) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
                continue;
            }
            catch(IOException unused_ex) {
            }
            break;
        }
        return curPos;
    }

    private int jjMoveNfa_2(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 51;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = 0x7FFFFFFF;
        while(true) {
            int v5 = this.jjround + 1;
            this.jjround = v5;
            if(v5 == 0x7FFFFFFF) {
                this.ReInitRounds();
            }
            if(this.curChar < 0x40) {
                long l = 1L << this.curChar;
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 0: {
                            if((0x2400L & l) != 0L) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 1: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            break;
                        }
                        case 2: {
                            if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 3: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 4: {
                            if(this.curChar == 10) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 5: {
                            if(this.curChar == 13) {
                                int v7 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v7 + 1;
                                this.jjstateSet[v7] = 4;
                            }
                            break;
                        }
                        case 6: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(38, 42);
                            }
                            else if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(43, 46);
                            }
                            else if((0x2400L & l) == 0L) {
                                switch(this.curChar) {
                                    case 34: {
                                        this.jjCheckNAddStates(56, 58);
                                        break;
                                    }
                                    case 39: {
                                        this.jjCheckNAddStates(53, 55);
                                        break;
                                    }
                                    case 45: {
                                        this.jjAddStates(51, 52);
                                        break;
                                    }
                                    case 46: {
                                        this.jjCheckNAdd(26);
                                        break;
                                    }
                                    case 58: {
                                        int v8 = this.jjnewStateCnt;
                                        this.jjnewStateCnt = v8 + 1;
                                        this.jjstateSet[v8] = 7;
                                        break;
                                    }
                                    case 59: {
                                        if(kind > 4) {
                                            kind = 4;
                                        }
                                        this.jjCheckNAddStates(0x2F, 50);
                                    }
                                }
                            }
                            else {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            switch(this.curChar) {
                                case 13: {
                                    int v9 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v9 + 1;
                                    this.jjstateSet[v9] = 4;
                                    break;
                                }
                                case 0x30: {
                                    int v10 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v10 + 1;
                                    this.jjstateSet[v10] = 12;
                                }
                            }
                            break;
                        }
                        case 8: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                int v11 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v11 + 1;
                                this.jjstateSet[v11] = 8;
                            }
                            break;
                        }
                        case 10: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 78) {
                                    kind = 78;
                                }
                                int v12 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v12 + 1;
                                this.jjstateSet[v12] = 10;
                            }
                            break;
                        }
                        case 11: {
                            if(this.curChar == 0x30) {
                                int v13 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v13 + 1;
                                this.jjstateSet[v13] = 12;
                            }
                            break;
                        }
                        case 13: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                int v14 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v14 + 1;
                                this.jjstateSet[v14] = 13;
                            }
                            break;
                        }
                        case 14: {
                            if(this.curChar == 34) {
                                this.jjCheckNAddStates(56, 58);
                            }
                            break;
                        }
                        case 16: {
                            this.jjCheckNAddStates(56, 58);
                            break;
                        }
                        case 17: {
                            if((0xFFFFFFFBFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(56, 58);
                            }
                            break;
                        }
                        case 18: {
                            if(this.curChar == 34 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 19: {
                            if(this.curChar == 39) {
                                this.jjCheckNAddStates(53, 55);
                            }
                            break;
                        }
                        case 21: {
                            this.jjCheckNAddStates(53, 55);
                            break;
                        }
                        case 22: {
                            if((0xFFFFFF7FFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(53, 55);
                            }
                            break;
                        }
                        case 23: {
                            if(this.curChar == 39 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 24: {
                            if(this.curChar == 45) {
                                this.jjAddStates(51, 52);
                            }
                            break;
                        }
                        case 25: {
                            if(this.curChar == 46) {
                                this.jjCheckNAdd(26);
                            }
                            break;
                        }
                        case 26: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(26, 27);
                            }
                            break;
                        }
                        case 28: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(29);
                            }
                            break;
                        }
                        case 29: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(29);
                            }
                            break;
                        }
                        case 30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddStates(59, 62);
                            }
                            break;
                        }
                        case 0x1F: {
                            if((0x3FF000000000000L & l) != 0L) {
                                this.jjCheckNAddTwoStates(0x1F, 0x20);
                            }
                            break;
                        }
                        case 0x20: {
                            if(this.curChar == 46) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(33, 34);
                            }
                            break;
                        }
                        case 33: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(33, 34);
                            }
                            break;
                        }
                        case 35: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(36);
                            }
                            break;
                        }
                        case 36: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(36);
                            }
                            break;
                        }
                        case 37: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(37, 38);
                            }
                            break;
                        }
                        case 39: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(40);
                            }
                            break;
                        }
                        case 40: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(40);
                            }
                            break;
                        }
                        case 41: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(43, 46);
                            }
                            break;
                        }
                        case 42: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(42, 43);
                            }
                            break;
                        }
                        case 43: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 44: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(44);
                            }
                            break;
                        }
                        case 45: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAddStates(0x2F, 50);
                            }
                            break;
                        }
                        case 0x2F: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 59) {
                                    kind = 59;
                                }
                                int v15 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v15 + 1;
                                this.jjstateSet[v15] = 0x2F;
                            }
                            break;
                        }
                        case 0x30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 77) {
                                    kind = 77;
                                }
                                int v16 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v16 + 1;
                                this.jjstateSet[v16] = 0x30;
                            }
                            break;
                        }
                        case 49: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(38, 42);
                            }
                            break;
                        }
                        case 50: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAdd(50);
                            }
                            break;
                        }
                        case 51: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            else if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(42, 43);
                            }
                            else if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(44);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else if(this.curChar < 0x80) {
                long l = 1L << (this.curChar & 0x3F);
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            this.jjAddStates(0, 2);
                            break;
                        }
                        case 6: {
                            switch(this.curChar) {
                                case 0x75: {
                                    int v18 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v18 + 1;
                                    this.jjstateSet[v18] = 10;
                                    break;
                                }
                                case 0x76: {
                                    this.jjAddStates(0x3F, 0x40);
                                }
                            }
                            break;
                        }
                        case 7: 
                        case 8: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                this.jjCheckNAdd(8);
                            }
                            break;
                        }
                        case 9: {
                            if(this.curChar == 0x75) {
                                int v19 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v19 + 1;
                                this.jjstateSet[v19] = 10;
                            }
                            break;
                        }
                        case 12: {
                            if((0x100000001000000L & l) != 0L) {
                                this.jjCheckNAdd(13);
                            }
                            break;
                        }
                        case 13: {
                            if((0x7E0000007EL & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                this.jjCheckNAdd(13);
                            }
                            break;
                        }
                        case 15: {
                            if(this.curChar == 92) {
                                int v20 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v20 + 1;
                                this.jjstateSet[v20] = 16;
                            }
                            break;
                        }
                        case 16: {
                            this.jjCheckNAddStates(56, 58);
                            break;
                        }
                        case 17: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(56, 58);
                            }
                            break;
                        }
                        case 20: {
                            if(this.curChar == 92) {
                                int v21 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v21 + 1;
                                this.jjstateSet[v21] = 21;
                            }
                            break;
                        }
                        case 21: {
                            this.jjCheckNAddStates(53, 55);
                            break;
                        }
                        case 22: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(53, 55);
                            }
                            break;
                        }
                        case 27: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(65, 66);
                            }
                            break;
                        }
                        case 34: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(67, 68);
                            }
                            break;
                        }
                        case 38: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(69, 70);
                            }
                            break;
                        }
                        case 44: {
                            if(kind > 4) {
                                kind = 4;
                            }
                            int v22 = this.jjnewStateCnt;
                            this.jjnewStateCnt = v22 + 1;
                            this.jjstateSet[v22] = 44;
                            break;
                        }
                        case 46: {
                            if(this.curChar == 0x76) {
                                this.jjAddStates(0x3F, 0x40);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else {
                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3F);
                int i2 = (this.curChar & 0xFF) >> 6;
                long l2 = 1L << (this.curChar & 0x3F);
                while(true) {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjAddStates(0, 2);
                            }
                            break;
                        }
                        case 16: 
                        case 17: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(56, 58);
                            }
                            break;
                        }
                        case 21: 
                        case 22: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(53, 55);
                            }
                            break;
                        }
                        case 44: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                int v28 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v28 + 1;
                                this.jjstateSet[v28] = 44;
                            }
                        }
                    }
                    if(i == startsAt) {
                        break;
                    }
                }
            }
            if(kind != 0x7FFFFFFF) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = 0x7FFFFFFF;
            }
            ++curPos;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 51 - startsAt;
            if(i == startsAt) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
                continue;
            }
            catch(IOException unused_ex) {
            }
            break;
        }
        return curPos;
    }

    private int jjMoveNfa_3(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 50;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = 0x7FFFFFFF;
        while(true) {
            int v5 = this.jjround + 1;
            this.jjround = v5;
            if(v5 == 0x7FFFFFFF) {
                this.ReInitRounds();
            }
            if(this.curChar < 0x40) {
                long l = 1L << this.curChar;
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 0: {
                            if((0x2400L & l) != 0L) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 1: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            break;
                        }
                        case 2: {
                            if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 3: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            break;
                        }
                        case 4: {
                            if(this.curChar == 10) {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            break;
                        }
                        case 5: {
                            if(this.curChar == 13) {
                                int v7 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v7 + 1;
                                this.jjstateSet[v7] = 4;
                            }
                            break;
                        }
                        case 6: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(3, 7);
                            }
                            else if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(8, 11);
                            }
                            else if((0x2400L & l) == 0L) {
                                switch(this.curChar) {
                                    case 34: {
                                        this.jjCheckNAddStates(25, 27);
                                        break;
                                    }
                                    case 39: {
                                        this.jjCheckNAddStates(22, 24);
                                        break;
                                    }
                                    case 45: {
                                        this.jjAddStates(20, 21);
                                        break;
                                    }
                                    case 46: {
                                        this.jjCheckNAdd(28);
                                        break;
                                    }
                                    case 58: {
                                        int v8 = this.jjnewStateCnt;
                                        this.jjnewStateCnt = v8 + 1;
                                        this.jjstateSet[v8] = 7;
                                        break;
                                    }
                                    case 59: {
                                        if(kind > 4) {
                                            kind = 4;
                                        }
                                        this.jjCheckNAddStates(16, 19);
                                    }
                                }
                            }
                            else {
                                if(kind > 74) {
                                    kind = 74;
                                }
                                this.jjCheckNAddStates(12, 15);
                            }
                            switch(this.curChar) {
                                case 13: {
                                    int v9 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v9 + 1;
                                    this.jjstateSet[v9] = 4;
                                    break;
                                }
                                case 0x30: {
                                    int v10 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v10 + 1;
                                    this.jjstateSet[v10] = 14;
                                }
                            }
                            break;
                        }
                        case 8: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                int v11 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v11 + 1;
                                this.jjstateSet[v11] = 8;
                            }
                            break;
                        }
                        case 10: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 77) {
                                    kind = 77;
                                }
                                int v12 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v12 + 1;
                                this.jjstateSet[v12] = 10;
                            }
                            break;
                        }
                        case 12: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 78) {
                                    kind = 78;
                                }
                                int v13 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v13 + 1;
                                this.jjstateSet[v13] = 12;
                            }
                            break;
                        }
                        case 13: {
                            if(this.curChar == 0x30) {
                                int v14 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v14 + 1;
                                this.jjstateSet[v14] = 14;
                            }
                            break;
                        }
                        case 15: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                int v15 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v15 + 1;
                                this.jjstateSet[v15] = 15;
                            }
                            break;
                        }
                        case 16: {
                            if(this.curChar == 34) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 18: {
                            this.jjCheckNAddStates(25, 27);
                            break;
                        }
                        case 19: {
                            if((0xFFFFFFFBFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 20: {
                            if(this.curChar == 34 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 21: {
                            if(this.curChar == 39) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 23: {
                            this.jjCheckNAddStates(22, 24);
                            break;
                        }
                        case 24: {
                            if((0xFFFFFF7FFFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 25: {
                            if(this.curChar == 39 && kind > 81) {
                                kind = 81;
                            }
                            break;
                        }
                        case 26: {
                            if(this.curChar == 45) {
                                this.jjAddStates(20, 21);
                            }
                            break;
                        }
                        case 27: {
                            if(this.curChar == 46) {
                                this.jjCheckNAdd(28);
                            }
                            break;
                        }
                        case 28: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(28, 29);
                            }
                            break;
                        }
                        case 30: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(0x1F);
                            }
                            break;
                        }
                        case 0x1F: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(0x1F);
                            }
                            break;
                        }
                        case 0x20: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddStates(28, 0x1F);
                            }
                            break;
                        }
                        case 33: {
                            if((0x3FF000000000000L & l) != 0L) {
                                this.jjCheckNAddTwoStates(33, 34);
                            }
                            break;
                        }
                        case 34: {
                            if(this.curChar == 46) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(35, 36);
                            }
                            break;
                        }
                        case 35: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(35, 36);
                            }
                            break;
                        }
                        case 37: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(38);
                            }
                            break;
                        }
                        case 38: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(38);
                            }
                            break;
                        }
                        case 39: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAddTwoStates(39, 40);
                            }
                            break;
                        }
                        case 41: {
                            if((0x280000000000L & l) != 0L) {
                                this.jjCheckNAdd(42);
                            }
                            break;
                        }
                        case 42: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 84) {
                                    kind = 84;
                                }
                                this.jjCheckNAdd(42);
                            }
                            break;
                        }
                        case 43: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddStates(8, 11);
                            }
                            break;
                        }
                        case 44: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(44, 45);
                            }
                            break;
                        }
                        case 45: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(46);
                            }
                            break;
                        }
                        case 46: {
                            if((0xFFFFFFFFFFFFDBFFL & l) != 0L) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(46);
                            }
                            break;
                        }
                        case 0x2F: {
                            if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAddStates(16, 19);
                            }
                            break;
                        }
                        case 0x30: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAddStates(3, 7);
                            }
                            break;
                        }
                        case 49: {
                            if((0x3FF000000000000L & l) != 0L) {
                                if(kind > 0x4F) {
                                    kind = 0x4F;
                                }
                                this.jjCheckNAdd(49);
                            }
                            break;
                        }
                        case 50: {
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(1, 2);
                            }
                            else if(this.curChar == 59) {
                                this.jjCheckNAddStates(0, 2);
                            }
                            if((0x100001200L & l) != 0L) {
                                this.jjCheckNAddTwoStates(44, 45);
                            }
                            else if(this.curChar == 59) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                this.jjCheckNAdd(46);
                            }
                        }
                    }
                }
                while(i != startsAt);
            }
            else if(this.curChar < 0x80) {
                long l = 1L << (this.curChar & 0x3F);
                do {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            this.jjAddStates(0, 2);
                            break;
                        }
                        case 6: {
                            switch(this.curChar) {
                                case 0x75: {
                                    int v17 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v17 + 1;
                                    this.jjstateSet[v17] = 12;
                                    break;
                                }
                                case 0x76: {
                                    int v18 = this.jjnewStateCnt;
                                    this.jjnewStateCnt = v18 + 1;
                                    this.jjstateSet[v18] = 10;
                                }
                            }
                            break;
                        }
                        case 7: 
                        case 8: {
                            if((0x7FFFFFE87FFFFFEL & l) != 0L) {
                                if(kind > 76) {
                                    kind = 76;
                                }
                                this.jjCheckNAdd(8);
                            }
                            break;
                        }
                        case 9: {
                            if(this.curChar == 0x76) {
                                int v19 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v19 + 1;
                                this.jjstateSet[v19] = 10;
                            }
                            break;
                        }
                        case 11: {
                            if(this.curChar == 0x75) {
                                int v20 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v20 + 1;
                                this.jjstateSet[v20] = 12;
                            }
                            break;
                        }
                        case 14: {
                            if((0x100000001000000L & l) != 0L) {
                                this.jjCheckNAdd(15);
                            }
                            break;
                        }
                        case 15: {
                            if((0x7E0000007EL & l) != 0L) {
                                if(kind > 80) {
                                    kind = 80;
                                }
                                this.jjCheckNAdd(15);
                            }
                            break;
                        }
                        case 17: {
                            if(this.curChar == 92) {
                                int v21 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v21 + 1;
                                this.jjstateSet[v21] = 18;
                            }
                            break;
                        }
                        case 18: {
                            this.jjCheckNAddStates(25, 27);
                            break;
                        }
                        case 19: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 22: {
                            if(this.curChar == 92) {
                                int v22 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v22 + 1;
                                this.jjstateSet[v22] = 23;
                            }
                            break;
                        }
                        case 23: {
                            this.jjCheckNAddStates(22, 24);
                            break;
                        }
                        case 24: {
                            if((0xFFFFFFFFEFFFFFFFL & l) != 0L) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 29: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(0x20, 33);
                            }
                            break;
                        }
                        case 36: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(34, 35);
                            }
                            break;
                        }
                        case 40: {
                            if((0x2000000020L & l) != 0L) {
                                this.jjAddStates(36, 37);
                            }
                            break;
                        }
                        case 46: {
                            if(kind > 4) {
                                kind = 4;
                            }
                            int v23 = this.jjnewStateCnt;
                            this.jjnewStateCnt = v23 + 1;
                            this.jjstateSet[v23] = 46;
                        }
                    }
                }
                while(i != startsAt);
            }
            else {
                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3F);
                int i2 = (this.curChar & 0xFF) >> 6;
                long l2 = 1L << (this.curChar & 0x3F);
                while(true) {
                    --i;
                    switch(this.jjstateSet[i]) {
                        case 3: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjAddStates(0, 2);
                            }
                            break;
                        }
                        case 18: 
                        case 19: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(25, 27);
                            }
                            break;
                        }
                        case 23: 
                        case 24: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                this.jjCheckNAddStates(22, 24);
                            }
                            break;
                        }
                        case 46: {
                            if(LasmTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                                if(kind > 4) {
                                    kind = 4;
                                }
                                int v29 = this.jjnewStateCnt;
                                this.jjnewStateCnt = v29 + 1;
                                this.jjstateSet[v29] = 46;
                            }
                        }
                    }
                    if(i == startsAt) {
                        break;
                    }
                }
            }
            if(kind != 0x7FFFFFFF) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = 0x7FFFFFFF;
            }
            ++curPos;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 50 - startsAt;
            if(i == startsAt) {
                return curPos;
            }
            try {
                this.curChar = this.input_stream.readChar();
                continue;
            }
            catch(IOException unused_ex) {
            }
            break;
        }
        return curPos;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch(this.curChar) {
            case 9: {
                return this.jjStartNfaWithStates_0(0, 2, 52);
            }
            case 12: {
                return this.jjStartNfaWithStates_0(0, 3, 52);
            }
            case 0x20: {
                return this.jjStartNfaWithStates_0(0, 1, 52);
            }
            case 46: {
                return this.jjMoveStringLiteralDfa1_0(0xC000000000000000L, 0xBFFL);
            }
            case 65: {
                return this.jjMoveStringLiteralDfa1_0(0x20000L, 0L);
            }
            case 66: {
                return this.jjMoveStringLiteralDfa1_0(0xF00000000000L, 0L);
            }
            case 67: {
                return this.jjMoveStringLiteralDfa1_0(0x4020204000000L, 0L);
            }
            case 68: {
                return this.jjMoveStringLiteralDfa1_0(0x100000L, 0L);
            }
            case 69: {
                return this.jjMoveStringLiteralDfa1_0(0x10000000L, 0L);
            }
            case 70: {
                return this.jjMoveStringLiteralDfa1_0(0x8003000000000L, 0L);
            }
            case 71: {
                return this.jjMoveStringLiteralDfa1_0(0x10000000000E00L, 0L);
            }
            case 73: {
                return this.jjMoveStringLiteralDfa1_0(0x80000000000L, 0L);
            }
            case 74: {
                return this.jjMoveStringLiteralDfa1_0(0x8000000L, 0L);
            }
            case 76: {
                return this.jjMoveStringLiteralDfa1_0(0x620001C0L, 0L);
            }
            case 77: {
                return this.jjMoveStringLiteralDfa1_0(0x280020L, 0L);
            }
            case 78: {
                return this.jjMoveStringLiteralDfa1_0(0x1008000L, 0L);
            }
            case 0x4F: {
                return this.jjMoveStringLiteralDfa1_0(0x20000000000000L, 0L);
            }
            case 80: {
                return this.jjMoveStringLiteralDfa1_0(0x400000L, 0L);
            }
            case 82: {
                return this.jjMoveStringLiteralDfa1_0(0x800000000L, 0L);
            }
            case 83: {
                return this.jjMoveStringLiteralDfa1_0(0xC3010000057000L, 0L);
            }
            case 84: {
                return this.jjMoveStringLiteralDfa1_0(0xC580000000L, 0L);
            }
            case 85: {
                return this.jjMoveStringLiteralDfa1_0(0x800000L, 0L);
            }
            case 86: {
                return this.jjMoveStringLiteralDfa1_0(0x40000000000L, 0L);
            }
            case 102: {
                return this.jjMoveStringLiteralDfa1_0(0x400000000000000L, 0L);
            }
            case 110: {
                return this.jjMoveStringLiteralDfa1_0(0x100000000000000L, 0L);
            }
            case 0x74: {
                return this.jjMoveStringLiteralDfa1_0(0x200000000000000L, 0L);
            }
            default: {
                return this.jjMoveNfa_0(0, 0);
            }
        }
    }

    private int jjMoveStringLiteralDfa0_1() {
        switch(this.curChar) {
            case 9: {
                return this.jjStartNfaWithStates_1(0, 2, 51);
            }
            case 12: {
                return this.jjStartNfaWithStates_1(0, 3, 51);
            }
            case 0x20: {
                return this.jjStartNfaWithStates_1(0, 1, 51);
            }
            case 46: {
                return this.jjMoveStringLiteralDfa1_1(0xC000000000000000L, 0xBFFL);
            }
            case 65: {
                return this.jjMoveStringLiteralDfa1_1(0x20000L, 0L);
            }
            case 66: {
                return this.jjMoveStringLiteralDfa1_1(0xF00000000000L, 0L);
            }
            case 67: {
                return this.jjMoveStringLiteralDfa1_1(0x4020204000000L, 0L);
            }
            case 68: {
                return this.jjMoveStringLiteralDfa1_1(0x100000L, 0L);
            }
            case 69: {
                return this.jjMoveStringLiteralDfa1_1(0x10000000L, 0L);
            }
            case 70: {
                return this.jjMoveStringLiteralDfa1_1(0x8003000000000L, 0L);
            }
            case 71: {
                return this.jjMoveStringLiteralDfa1_1(0x10000000000E00L, 0L);
            }
            case 73: {
                return this.jjMoveStringLiteralDfa1_1(0x80000000000L, 0L);
            }
            case 74: {
                return this.jjMoveStringLiteralDfa1_1(0x8000000L, 0L);
            }
            case 76: {
                return this.jjMoveStringLiteralDfa1_1(0x620001C0L, 0L);
            }
            case 77: {
                return this.jjMoveStringLiteralDfa1_1(0x280020L, 0L);
            }
            case 78: {
                return this.jjMoveStringLiteralDfa1_1(0x1008000L, 0L);
            }
            case 0x4F: {
                return this.jjMoveStringLiteralDfa1_1(0x20000000000000L, 0L);
            }
            case 80: {
                return this.jjMoveStringLiteralDfa1_1(0x400000L, 0L);
            }
            case 82: {
                return this.jjMoveStringLiteralDfa1_1(0x800000000L, 0L);
            }
            case 83: {
                return this.jjMoveStringLiteralDfa1_1(0xC3010000057000L, 0L);
            }
            case 84: {
                return this.jjMoveStringLiteralDfa1_1(0xC580000000L, 0L);
            }
            case 85: {
                return this.jjMoveStringLiteralDfa1_1(0x800000L, 0L);
            }
            case 86: {
                return this.jjMoveStringLiteralDfa1_1(0x40000000000L, 0L);
            }
            case 102: {
                return this.jjMoveStringLiteralDfa1_1(0x400000000000000L, 0L);
            }
            case 110: {
                return this.jjMoveStringLiteralDfa1_1(0x100000000000000L, 0L);
            }
            case 0x74: {
                return this.jjMoveStringLiteralDfa1_1(0x200000000000000L, 0L);
            }
            default: {
                return this.jjMoveNfa_1(6, 0);
            }
        }
    }

    private int jjMoveStringLiteralDfa0_2() {
        switch(this.curChar) {
            case 9: {
                return this.jjStartNfaWithStates_2(0, 2, 51);
            }
            case 12: {
                return this.jjStartNfaWithStates_2(0, 3, 51);
            }
            case 0x20: {
                return this.jjStartNfaWithStates_2(0, 1, 51);
            }
            case 46: {
                return this.jjMoveStringLiteralDfa1_2(0xC000000000000000L, 0xBFFL);
            }
            case 65: {
                return this.jjMoveStringLiteralDfa1_2(0x20000L, 0L);
            }
            case 66: {
                return this.jjMoveStringLiteralDfa1_2(0xF00000000000L, 0L);
            }
            case 67: {
                return this.jjMoveStringLiteralDfa1_2(0x4020204000000L, 0L);
            }
            case 68: {
                return this.jjMoveStringLiteralDfa1_2(0x100000L, 0L);
            }
            case 69: {
                return this.jjMoveStringLiteralDfa1_2(0x10000000L, 0L);
            }
            case 70: {
                return this.jjMoveStringLiteralDfa1_2(0x8003000000000L, 0L);
            }
            case 71: {
                return this.jjMoveStringLiteralDfa1_2(0x10000000000E00L, 0L);
            }
            case 73: {
                return this.jjMoveStringLiteralDfa1_2(0x80000000000L, 0L);
            }
            case 74: {
                return this.jjMoveStringLiteralDfa1_2(0x8000000L, 0L);
            }
            case 76: {
                return this.jjMoveStringLiteralDfa1_2(0x620001C0L, 0L);
            }
            case 77: {
                return this.jjMoveStringLiteralDfa1_2(0x280020L, 0L);
            }
            case 78: {
                return this.jjMoveStringLiteralDfa1_2(0x1008000L, 0L);
            }
            case 0x4F: {
                return this.jjMoveStringLiteralDfa1_2(0x20000000000000L, 0L);
            }
            case 80: {
                return this.jjMoveStringLiteralDfa1_2(0x400000L, 0L);
            }
            case 82: {
                return this.jjMoveStringLiteralDfa1_2(0x800000000L, 0L);
            }
            case 83: {
                return this.jjMoveStringLiteralDfa1_2(0xC3010000057000L, 0L);
            }
            case 84: {
                return this.jjMoveStringLiteralDfa1_2(0xC580000000L, 0L);
            }
            case 85: {
                return this.jjMoveStringLiteralDfa1_2(0x800000L, 0L);
            }
            case 86: {
                return this.jjMoveStringLiteralDfa1_2(0x40000000000L, 0L);
            }
            case 102: {
                return this.jjMoveStringLiteralDfa1_2(0x400000000000000L, 0L);
            }
            case 110: {
                return this.jjMoveStringLiteralDfa1_2(0x100000000000000L, 0L);
            }
            case 0x74: {
                return this.jjMoveStringLiteralDfa1_2(0x200000000000000L, 0L);
            }
            default: {
                return this.jjMoveNfa_2(6, 0);
            }
        }
    }

    private int jjMoveStringLiteralDfa0_3() {
        switch(this.curChar) {
            case 9: {
                return this.jjStartNfaWithStates_3(0, 2, 50);
            }
            case 12: {
                return this.jjStartNfaWithStates_3(0, 3, 50);
            }
            case 0x20: {
                return this.jjStartNfaWithStates_3(0, 1, 50);
            }
            case 46: {
                return this.jjMoveStringLiteralDfa1_3(0xC000000000000000L, 0xBFFL);
            }
            case 65: {
                return this.jjMoveStringLiteralDfa1_3(0x20000L, 0L);
            }
            case 66: {
                return this.jjMoveStringLiteralDfa1_3(0xF00000000000L, 0L);
            }
            case 67: {
                return this.jjMoveStringLiteralDfa1_3(0x4020204000000L, 0L);
            }
            case 68: {
                return this.jjMoveStringLiteralDfa1_3(0x100000L, 0L);
            }
            case 69: {
                return this.jjMoveStringLiteralDfa1_3(0x10000000L, 0L);
            }
            case 70: {
                return this.jjMoveStringLiteralDfa1_3(0x8003000000000L, 0L);
            }
            case 71: {
                return this.jjMoveStringLiteralDfa1_3(0x10000000000E00L, 0L);
            }
            case 73: {
                return this.jjMoveStringLiteralDfa1_3(0x80000000000L, 0L);
            }
            case 74: {
                return this.jjMoveStringLiteralDfa1_3(0x8000000L, 0L);
            }
            case 76: {
                return this.jjMoveStringLiteralDfa1_3(0x620001C0L, 0L);
            }
            case 77: {
                return this.jjMoveStringLiteralDfa1_3(0x280020L, 0L);
            }
            case 78: {
                return this.jjMoveStringLiteralDfa1_3(0x1008000L, 0L);
            }
            case 0x4F: {
                return this.jjMoveStringLiteralDfa1_3(0x20000000000000L, 0L);
            }
            case 80: {
                return this.jjMoveStringLiteralDfa1_3(0x400000L, 0L);
            }
            case 82: {
                return this.jjMoveStringLiteralDfa1_3(0x800000000L, 0L);
            }
            case 83: {
                return this.jjMoveStringLiteralDfa1_3(0xC3010000057000L, 0L);
            }
            case 84: {
                return this.jjMoveStringLiteralDfa1_3(0xC580000000L, 0L);
            }
            case 85: {
                return this.jjMoveStringLiteralDfa1_3(0x800000L, 0L);
            }
            case 86: {
                return this.jjMoveStringLiteralDfa1_3(0x40000000000L, 0L);
            }
            case 91: {
                return this.jjStopAtPos(0, 88);
            }
            case 93: {
                return this.jjStopAtPos(0, 89);
            }
            case 102: {
                return this.jjMoveStringLiteralDfa1_3(0x400000000000000L, 0L);
            }
            case 110: {
                return this.jjMoveStringLiteralDfa1_3(0x100000000000000L, 0L);
            }
            case 0x74: {
                return this.jjMoveStringLiteralDfa1_3(0x200000000000000L, 0L);
            }
            default: {
                return this.jjMoveNfa_3(6, 0);
            }
        }
    }

    private int jjMoveStringLiteralDfa10_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(8, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return this.jjMoveStringLiteralDfa11_0(v4, 0x8000000000000000L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa11_0(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(9, v4, v5);
            return 10;
        }
        return this.jjStartNfa_0(9, v4, v5);
    }

    private int jjMoveStringLiteralDfa10_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(8, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return this.jjMoveStringLiteralDfa11_1(v4, 0x8000000000000000L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa11_1(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 10;
        }
        return this.jjStartNfa_1(9, v4, v5);
    }

    private int jjMoveStringLiteralDfa10_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(8, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return this.jjMoveStringLiteralDfa11_2(v4, 0x8000000000000000L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa11_2(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 10;
        }
        return this.jjStartNfa_2(9, v4, v5);
    }

    private int jjMoveStringLiteralDfa10_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(8, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return this.jjMoveStringLiteralDfa11_3(v4, 0x8000000000000000L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa11_3(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 10;
        }
        return this.jjStartNfa_3(9, v4, v5);
    }

    private int jjMoveStringLiteralDfa11_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(9, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return (0x8000000000000000L & v4) == 0L ? this.jjStartNfa_0(10, v4, v5) : this.jjStopAtPos(11, 0x3F);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa12_0(v4, 0L, v5, 1L);
                }
                case 0x7A: {
                    return this.jjMoveStringLiteralDfa12_0(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(10, v4, v5);
            return 11;
        }
        return this.jjStartNfa_0(10, v4, v5);
    }

    private int jjMoveStringLiteralDfa11_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(9, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return (0x8000000000000000L & v4) == 0L ? this.jjStartNfa_1(10, v4, v5) : this.jjStopAtPos(11, 0x3F);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa12_1(v4, 0L, v5, 1L);
                }
                case 0x7A: {
                    return this.jjMoveStringLiteralDfa12_1(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 11;
        }
        return this.jjStartNfa_1(10, v4, v5);
    }

    private int jjMoveStringLiteralDfa11_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(9, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return (0x8000000000000000L & v4) == 0L ? this.jjStartNfa_2(10, v4, v5) : this.jjStopAtPos(11, 0x3F);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa12_2(v4, 0L, v5, 1L);
                }
                case 0x7A: {
                    return this.jjMoveStringLiteralDfa12_2(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 11;
        }
        return this.jjStartNfa_2(10, v4, v5);
    }

    private int jjMoveStringLiteralDfa11_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(9, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return (0x8000000000000000L & v4) == 0L ? this.jjStartNfa_3(10, v4, v5) : this.jjStopAtPos(11, 0x3F);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa12_3(v4, 0L, v5, 1L);
                }
                case 0x7A: {
                    return this.jjMoveStringLiteralDfa12_3(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 11;
        }
        return this.jjStartNfa_3(10, v4, v5);
    }

    private int jjMoveStringLiteralDfa12_0(long old0, long active0, long old1, long active1) {
        long v4 = active1 & old1;
        if((active0 & old0 | v4) == 0L) {
            return this.jjStartNfa_0(10, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return (8L & v4) == 0L ? this.jjStartNfa_0(11, 0L, v4) : this.jjStopAtPos(12, 67);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa13_0(v4, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(11, 0L, v4);
            return 12;
        }
        return this.jjStartNfa_0(11, 0L, v4);
    }

    private int jjMoveStringLiteralDfa12_1(long old0, long active0, long old1, long active1) {
        long v4 = active1 & old1;
        if((active0 & old0 | v4) == 0L) {
            return this.jjStartNfa_1(10, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return (8L & v4) == 0L ? this.jjStartNfa_1(11, 0L, v4) : this.jjStopAtPos(12, 67);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa13_1(v4, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 12;
        }
        return this.jjStartNfa_1(11, 0L, v4);
    }

    private int jjMoveStringLiteralDfa12_2(long old0, long active0, long old1, long active1) {
        long v4 = active1 & old1;
        if((active0 & old0 | v4) == 0L) {
            return this.jjStartNfa_2(10, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return (8L & v4) == 0L ? this.jjStartNfa_2(11, 0L, v4) : this.jjStopAtPos(12, 67);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa13_2(v4, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 12;
        }
        return this.jjStartNfa_2(11, 0L, v4);
    }

    private int jjMoveStringLiteralDfa12_3(long old0, long active0, long old1, long active1) {
        long v4 = active1 & old1;
        if((active0 & old0 | v4) == 0L) {
            return this.jjStartNfa_3(10, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 101: {
                    return (8L & v4) == 0L ? this.jjStartNfa_3(11, 0L, v4) : this.jjStopAtPos(12, 67);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa13_3(v4, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 12;
        }
        return this.jjStartNfa_3(11, 0L, v4);
    }

    private int jjMoveStringLiteralDfa13_0(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_0(11, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'n' ? this.jjMoveStringLiteralDfa14_0(v2, 1L) : this.jjStartNfa_0(12, 0L, v2);
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(12, 0L, v2);
            return 13;
        }
    }

    private int jjMoveStringLiteralDfa13_1(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_1(11, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'n' ? this.jjMoveStringLiteralDfa14_1(v2, 1L) : this.jjStartNfa_1(12, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 13;
        }
    }

    private int jjMoveStringLiteralDfa13_2(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_2(11, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'n' ? this.jjMoveStringLiteralDfa14_2(v2, 1L) : this.jjStartNfa_2(12, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 13;
        }
    }

    private int jjMoveStringLiteralDfa13_3(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_3(11, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'n' ? this.jjMoveStringLiteralDfa14_3(v2, 1L) : this.jjStartNfa_3(12, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 13;
        }
    }

    private int jjMoveStringLiteralDfa14_0(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_0(12, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'e' ? this.jjMoveStringLiteralDfa15_0(v2, 1L) : this.jjStartNfa_0(13, 0L, v2);
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(13, 0L, v2);
            return 14;
        }
    }

    private int jjMoveStringLiteralDfa14_1(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_1(12, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'e' ? this.jjMoveStringLiteralDfa15_1(v2, 1L) : this.jjStartNfa_1(13, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 14;
        }
    }

    private int jjMoveStringLiteralDfa14_2(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_2(12, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'e' ? this.jjMoveStringLiteralDfa15_2(v2, 1L) : this.jjStartNfa_2(13, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 14;
        }
    }

    private int jjMoveStringLiteralDfa14_3(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_3(12, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            return this.curChar == 'e' ? this.jjMoveStringLiteralDfa15_3(v2, 1L) : this.jjStartNfa_3(13, 0L, v2);
        }
        catch(IOException unused_ex) {
            return 14;
        }
    }

    private int jjMoveStringLiteralDfa15_0(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_0(13, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(14, 0L, v2);
            return 15;
        }
        if(this.curChar != 'd') {
            return this.jjStartNfa_0(14, 0L, v2);
        }
        return (1L & v2) == 0L ? this.jjStartNfa_0(14, 0L, v2) : this.jjStopAtPos(15, 0x40);
    }

    private int jjMoveStringLiteralDfa15_1(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_1(13, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch(IOException unused_ex) {
            return 15;
        }
        if(this.curChar != 'd') {
            return this.jjStartNfa_1(14, 0L, v2);
        }
        return (1L & v2) == 0L ? this.jjStartNfa_1(14, 0L, v2) : this.jjStopAtPos(15, 0x40);
    }

    private int jjMoveStringLiteralDfa15_2(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_2(13, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch(IOException unused_ex) {
            return 15;
        }
        if(this.curChar != 'd') {
            return this.jjStartNfa_2(14, 0L, v2);
        }
        return (1L & v2) == 0L ? this.jjStartNfa_2(14, 0L, v2) : this.jjStopAtPos(15, 0x40);
    }

    private int jjMoveStringLiteralDfa15_3(long old1, long active1) {
        long v2 = active1 & old1;
        if(v2 == 0L) {
            return this.jjStartNfa_3(13, 0L, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
        }
        catch(IOException unused_ex) {
            return 15;
        }
        if(this.curChar != 'd') {
            return this.jjStartNfa_3(14, 0L, v2);
        }
        return (1L & v2) == 0L ? this.jjStartNfa_3(14, 0L, v2) : this.jjStopAtPos(15, 0x40);
    }

    private int jjMoveStringLiteralDfa1_0(long active0, long active1) {
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 46: {
                    return (0x800L & active1) == 0L ? this.jjStartNfa_0(0, active0, active1) : this.jjStopAtPos(1, 75);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x240600000000L, active1, 0L);
                }
                case 68: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x80000020000L, active1, 0L);
                }
                case 69: {
                    goto label_9;
                }
                case 70: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0xC000000000L, active1, 0L);
                }
                case 72: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x3000000000000L, active1, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x100000L, active1, 0L);
                }
                case 75: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x80000000000000L, active1, 0L);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x20000000000L, active1, 0L);
                }
                case 77: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x8000000L, active1, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x100000800000L, active1, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x144030056001E0L, active1, 0L);
                }
                case 80: {
                    return (0x20000000000000L & active0) == 0L ? this.jjStartNfa_0(0, active0, active1) : this.jjStartNfaWithStates_0(1, 53, 1);
                }
                case 81: {
                    return (0x10000000L & active0) == 0L ? this.jjStartNfa_0(0, active0, active1) : this.jjStartNfaWithStates_0(1, 28, 1);
                }
                case 84: {
                    return (0x20000000L & active0) == 0L ? this.jjStartNfa_0(0, active0, active1) : this.jjStartNfaWithStates_0(1, 29, 1);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x80000000C0000L, active1, 0L);
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x800000000000L, active1, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x400000000000000L, active1, 0L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 640L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x100L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x100000000000000L, active1, 4L);
                }
                case 108: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x8000000000000000L, active1, 97L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 8L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x200000000000000L, active1, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0x4000000000000000L, active1, 0L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 16L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(0, active0, active1);
            return 1;
        }
        return this.jjStartNfa_0(0, active0, active1);
    label_9:
        if((0x40000000L & active0) != 0L) {
            this.jjmatchedKind = 30;
            this.jjmatchedPos = 1;
        }
        return this.jjMoveStringLiteralDfa2_0(active0, 0x4001098201FE00L, active1, 0L);
    }

    private int jjMoveStringLiteralDfa1_1(long active0, long active1) {
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 46: {
                    return (0x800L & active1) == 0L ? this.jjStartNfa_1(0, active0, active1) : this.jjStopAtPos(1, 75);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x240600000000L, active1, 0L);
                }
                case 68: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x80000020000L, active1, 0L);
                }
                case 69: {
                    goto label_8;
                }
                case 70: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0xC000000000L, active1, 0L);
                }
                case 72: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x3000000000000L, active1, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x100000L, active1, 0L);
                }
                case 75: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x80000000000000L, active1, 0L);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x20000000000L, active1, 0L);
                }
                case 77: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x8000000L, active1, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x100000800000L, active1, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x144030056001E0L, active1, 0L);
                }
                case 80: {
                    return (0x20000000000000L & active0) == 0L ? this.jjStartNfa_1(0, active0, active1) : this.jjStopAtPos(1, 53);
                }
                case 81: {
                    return (0x10000000L & active0) == 0L ? this.jjStartNfa_1(0, active0, active1) : this.jjStopAtPos(1, 28);
                }
                case 84: {
                    return (0x20000000L & active0) == 0L ? this.jjStartNfa_1(0, active0, active1) : this.jjStopAtPos(1, 29);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x80000000C0000L, active1, 0L);
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x800000000000L, active1, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x400000000000000L, active1, 0L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0L, active1, 640L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0L, active1, 0x100L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x100000000000000L, active1, 4L);
                }
                case 108: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x8000000000000000L, active1, 97L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0L, active1, 8L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0L, active1, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x200000000000000L, active1, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0x4000000000000000L, active1, 0L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa2_1(active0, 0L, active1, 16L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 1;
        }
        return this.jjStartNfa_1(0, active0, active1);
    label_8:
        if((0x40000000L & active0) != 0L) {
            this.jjmatchedKind = 30;
            this.jjmatchedPos = 1;
        }
        return this.jjMoveStringLiteralDfa2_1(active0, 0x4001098201FE00L, active1, 0L);
    }

    private int jjMoveStringLiteralDfa1_2(long active0, long active1) {
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 46: {
                    return (0x800L & active1) == 0L ? this.jjStartNfa_2(0, active0, active1) : this.jjStopAtPos(1, 75);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x240600000000L, active1, 0L);
                }
                case 68: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x80000020000L, active1, 0L);
                }
                case 69: {
                    goto label_8;
                }
                case 70: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0xC000000000L, active1, 0L);
                }
                case 72: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x3000000000000L, active1, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x100000L, active1, 0L);
                }
                case 75: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x80000000000000L, active1, 0L);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x20000000000L, active1, 0L);
                }
                case 77: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x8000000L, active1, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x100000800000L, active1, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x144030056001E0L, active1, 0L);
                }
                case 80: {
                    return (0x20000000000000L & active0) == 0L ? this.jjStartNfa_2(0, active0, active1) : this.jjStopAtPos(1, 53);
                }
                case 81: {
                    return (0x10000000L & active0) == 0L ? this.jjStartNfa_2(0, active0, active1) : this.jjStopAtPos(1, 28);
                }
                case 84: {
                    return (0x20000000L & active0) == 0L ? this.jjStartNfa_2(0, active0, active1) : this.jjStopAtPos(1, 29);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x80000000C0000L, active1, 0L);
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x800000000000L, active1, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x400000000000000L, active1, 0L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0L, active1, 640L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0L, active1, 0x100L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x100000000000000L, active1, 4L);
                }
                case 108: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x8000000000000000L, active1, 97L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0L, active1, 8L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0L, active1, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x200000000000000L, active1, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0x4000000000000000L, active1, 0L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa2_2(active0, 0L, active1, 16L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 1;
        }
        return this.jjStartNfa_2(0, active0, active1);
    label_8:
        if((0x40000000L & active0) != 0L) {
            this.jjmatchedKind = 30;
            this.jjmatchedPos = 1;
        }
        return this.jjMoveStringLiteralDfa2_2(active0, 0x4001098201FE00L, active1, 0L);
    }

    private int jjMoveStringLiteralDfa1_3(long active0, long active1) {
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 46: {
                    return (0x800L & active1) == 0L ? this.jjStartNfa_3(0, active0, active1) : this.jjStopAtPos(1, 75);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x240600000000L, active1, 0L);
                }
                case 68: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x80000020000L, active1, 0L);
                }
                case 69: {
                    goto label_8;
                }
                case 70: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0xC000000000L, active1, 0L);
                }
                case 72: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x3000000000000L, active1, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x100000L, active1, 0L);
                }
                case 75: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x80000000000000L, active1, 0L);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x20000000000L, active1, 0L);
                }
                case 77: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x8000000L, active1, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x100000800000L, active1, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x144030056001E0L, active1, 0L);
                }
                case 80: {
                    return (0x20000000000000L & active0) == 0L ? this.jjStartNfa_3(0, active0, active1) : this.jjStopAtPos(1, 53);
                }
                case 81: {
                    return (0x10000000L & active0) == 0L ? this.jjStartNfa_3(0, active0, active1) : this.jjStopAtPos(1, 28);
                }
                case 84: {
                    return (0x20000000L & active0) == 0L ? this.jjStartNfa_3(0, active0, active1) : this.jjStopAtPos(1, 29);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x80000000C0000L, active1, 0L);
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x800000000000L, active1, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x400000000000000L, active1, 0L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0L, active1, 640L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0L, active1, 0x100L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x100000000000000L, active1, 4L);
                }
                case 108: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x8000000000000000L, active1, 97L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0L, active1, 8L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0L, active1, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x200000000000000L, active1, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0x4000000000000000L, active1, 0L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa2_3(active0, 0L, active1, 16L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 1;
        }
        return this.jjStartNfa_3(0, active0, active1);
    label_8:
        if((0x40000000L & active0) != 0L) {
            this.jjmatchedKind = 30;
            this.jjmatchedPos = 1;
        }
        return this.jjMoveStringLiteralDfa2_3(active0, 0x4001098201FE00L, active1, 0L);
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(0, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x1C0L, v5, 0L);
                }
                case 66: {
                    return (0x40000L & v4) == 0L ? this.jjStartNfa_0(1, v4, v5) : this.jjStartNfaWithStates_0(2, 18, 1);
                }
                case 68: {
                    goto label_12;
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x80080400000000L, v5, 0L);
                }
                case 76: {
                    goto label_16;
                }
                case 77: {
                    return (0x800000L & v4) == 0L ? this.jjStartNfa_0(1, v4, v5) : this.jjStartNfaWithStates_0(2, 23, 1);
                }
                case 78: {
                    return (0x2000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0xC200004000000L, v5, 0L) : this.jjStartNfaWithStates_0(2, 25, 1);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x92C000000000L, v5, 0L);
                }
                case 80: {
                    return (0x8000000L & v4) == 0L ? this.jjStartNfa_0(1, v4, v5) : this.jjStartNfaWithStates_0(2, 27, 1);
                }
                case 82: {
                    goto label_23;
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x180000000L, v5, 0L);
                }
                case 84: {
                    return (0x1000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x50010800007E00L, v5, 0L) : this.jjStartNfaWithStates_0(2, 24, 1);
                }
                case 86: {
                    return (0x100000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x20L, v5, 0L) : this.jjStartNfaWithStates_0(2, 20, 1);
                }
                case 87: {
                    return (0x400000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x8000L, v5, 0L) : this.jjStartNfaWithStates_0(2, 22, 1);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0L, v5, 9L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x8000000000000000L, v5, 0x20L);
                }
                case 108: {
                    return (0x100000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x400000000000000L, v5, 0L) : this.jjStartNfaWithStates_0(2, 56, 1);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0L, v5, 640L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x4000000000000000L, v5, 0x40L);
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0L, v5, 16L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0L, v5, 4L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa3_0(v4, 0x200000000000000L, v5, 0x102L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(1, v4, v5);
            return 2;
        }
        return this.jjStartNfa_0(1, v4, v5);
    label_12:
        if((0x20000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(2, 17, 1);
        }
        return (0x200000L & v4) == 0L ? this.jjStartNfa_0(1, v4, v5) : this.jjStartNfaWithStates_0(2, 21, 1);
    label_16:
        if((0x80000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(2, 19, 1);
        }
        return (0x1000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x200010000L, v5, 0L) : this.jjStartNfaWithStates_0(2, 0x30, 1);
    label_23:
        if((0x400000000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(2, 46, 1);
        }
        return (0x2000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_0(v4, 0x43000000000L, v5, 0L) : this.jjStartNfaWithStates_0(2, 49, 1);
    }

    private int jjMoveStringLiteralDfa2_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(0, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x1C0L, v5, 0L);
                }
                case 66: {
                    return (0x40000L & v4) == 0L ? this.jjStartNfa_1(1, v4, v5) : this.jjStopAtPos(2, 18);
                }
                case 68: {
                    goto label_11;
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x80080400000000L, v5, 0L);
                }
                case 76: {
                    goto label_15;
                }
                case 77: {
                    return (0x800000L & v4) == 0L ? this.jjStartNfa_1(1, v4, v5) : this.jjStopAtPos(2, 23);
                }
                case 78: {
                    return (0x2000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0xC200004000000L, v5, 0L) : this.jjStopAtPos(2, 25);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x92C000000000L, v5, 0L);
                }
                case 80: {
                    return (0x8000000L & v4) == 0L ? this.jjStartNfa_1(1, v4, v5) : this.jjStopAtPos(2, 27);
                }
                case 82: {
                    goto label_22;
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x180000000L, v5, 0L);
                }
                case 84: {
                    return (0x1000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x50010800007E00L, v5, 0L) : this.jjStopAtPos(2, 24);
                }
                case 86: {
                    return (0x100000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x20L, v5, 0L) : this.jjStopAtPos(2, 20);
                }
                case 87: {
                    return (0x400000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x8000L, v5, 0L) : this.jjStopAtPos(2, 22);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0L, v5, 9L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x8000000000000000L, v5, 0x20L);
                }
                case 108: {
                    return (0x100000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x400000000000000L, v5, 0L) : this.jjStopAtPos(2, 56);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0L, v5, 640L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x4000000000000000L, v5, 0x40L);
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0L, v5, 16L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0L, v5, 4L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa3_1(v4, 0x200000000000000L, v5, 0x102L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 2;
        }
        return this.jjStartNfa_1(1, v4, v5);
    label_11:
        if((0x20000L & v4) != 0L) {
            return this.jjStopAtPos(2, 17);
        }
        return (0x200000L & v4) == 0L ? this.jjStartNfa_1(1, v4, v5) : this.jjStopAtPos(2, 21);
    label_15:
        if((0x80000L & v4) != 0L) {
            return this.jjStopAtPos(2, 19);
        }
        return (0x1000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x200010000L, v5, 0L) : this.jjStopAtPos(2, 0x30);
    label_22:
        if((0x400000000000L & v4) != 0L) {
            return this.jjStopAtPos(2, 46);
        }
        return (0x2000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_1(v4, 0x43000000000L, v5, 0L) : this.jjStopAtPos(2, 49);
    }

    private int jjMoveStringLiteralDfa2_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(0, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x1C0L, v5, 0L);
                }
                case 66: {
                    return (0x40000L & v4) == 0L ? this.jjStartNfa_2(1, v4, v5) : this.jjStopAtPos(2, 18);
                }
                case 68: {
                    goto label_11;
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x80080400000000L, v5, 0L);
                }
                case 76: {
                    goto label_15;
                }
                case 77: {
                    return (0x800000L & v4) == 0L ? this.jjStartNfa_2(1, v4, v5) : this.jjStopAtPos(2, 23);
                }
                case 78: {
                    return (0x2000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0xC200004000000L, v5, 0L) : this.jjStopAtPos(2, 25);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x92C000000000L, v5, 0L);
                }
                case 80: {
                    return (0x8000000L & v4) == 0L ? this.jjStartNfa_2(1, v4, v5) : this.jjStopAtPos(2, 27);
                }
                case 82: {
                    goto label_22;
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x180000000L, v5, 0L);
                }
                case 84: {
                    return (0x1000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x50010800007E00L, v5, 0L) : this.jjStopAtPos(2, 24);
                }
                case 86: {
                    return (0x100000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x20L, v5, 0L) : this.jjStopAtPos(2, 20);
                }
                case 87: {
                    return (0x400000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x8000L, v5, 0L) : this.jjStopAtPos(2, 22);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0L, v5, 9L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x8000000000000000L, v5, 0x20L);
                }
                case 108: {
                    return (0x100000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x400000000000000L, v5, 0L) : this.jjStopAtPos(2, 56);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0L, v5, 640L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x4000000000000000L, v5, 0x40L);
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0L, v5, 16L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0L, v5, 4L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa3_2(v4, 0x200000000000000L, v5, 0x102L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 2;
        }
        return this.jjStartNfa_2(1, v4, v5);
    label_11:
        if((0x20000L & v4) != 0L) {
            return this.jjStopAtPos(2, 17);
        }
        return (0x200000L & v4) == 0L ? this.jjStartNfa_2(1, v4, v5) : this.jjStopAtPos(2, 21);
    label_15:
        if((0x80000L & v4) != 0L) {
            return this.jjStopAtPos(2, 19);
        }
        return (0x1000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x200010000L, v5, 0L) : this.jjStopAtPos(2, 0x30);
    label_22:
        if((0x400000000000L & v4) != 0L) {
            return this.jjStopAtPos(2, 46);
        }
        return (0x2000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_2(v4, 0x43000000000L, v5, 0L) : this.jjStopAtPos(2, 49);
    }

    private int jjMoveStringLiteralDfa2_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(0, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x1C0L, v5, 0L);
                }
                case 66: {
                    return (0x40000L & v4) == 0L ? this.jjStartNfa_3(1, v4, v5) : this.jjStopAtPos(2, 18);
                }
                case 68: {
                    goto label_11;
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x80080400000000L, v5, 0L);
                }
                case 76: {
                    goto label_15;
                }
                case 77: {
                    return (0x800000L & v4) == 0L ? this.jjStartNfa_3(1, v4, v5) : this.jjStopAtPos(2, 23);
                }
                case 78: {
                    return (0x2000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0xC200004000000L, v5, 0L) : this.jjStopAtPos(2, 25);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x92C000000000L, v5, 0L);
                }
                case 80: {
                    return (0x8000000L & v4) == 0L ? this.jjStartNfa_3(1, v4, v5) : this.jjStopAtPos(2, 27);
                }
                case 82: {
                    goto label_22;
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x180000000L, v5, 0L);
                }
                case 84: {
                    return (0x1000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x50010800007E00L, v5, 0L) : this.jjStopAtPos(2, 24);
                }
                case 86: {
                    return (0x100000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x20L, v5, 0L) : this.jjStopAtPos(2, 20);
                }
                case 87: {
                    return (0x400000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x8000L, v5, 0L) : this.jjStopAtPos(2, 22);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0L, v5, 9L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x8000000000000000L, v5, 0x20L);
                }
                case 108: {
                    return (0x100000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x400000000000000L, v5, 0L) : this.jjStopAtPos(2, 56);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0L, v5, 640L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x4000000000000000L, v5, 0x40L);
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0L, v5, 16L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0L, v5, 4L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa3_3(v4, 0x200000000000000L, v5, 0x102L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 2;
        }
        return this.jjStartNfa_3(1, v4, v5);
    label_11:
        if((0x20000L & v4) != 0L) {
            return this.jjStopAtPos(2, 17);
        }
        return (0x200000L & v4) == 0L ? this.jjStartNfa_3(1, v4, v5) : this.jjStopAtPos(2, 21);
    label_15:
        if((0x80000L & v4) != 0L) {
            return this.jjStopAtPos(2, 19);
        }
        return (0x1000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x200010000L, v5, 0L) : this.jjStopAtPos(2, 0x30);
    label_22:
        if((0x400000000000L & v4) != 0L) {
            return this.jjStopAtPos(2, 46);
        }
        return (0x2000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa3_3(v4, 0x43000000000L, v5, 0L) : this.jjStopAtPos(2, 49);
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(1, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x40000000000L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x8000004000000L, v5, 0L);
                }
                case 68: {
                    return (0x200000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_0(v4, 0x1C0L, v5, 0L) : this.jjStartNfaWithStates_0(3, 45, 1);
                }
                case 69: {
                    return (0x20L & v4) == 0L ? this.jjStartNfa_0(2, v4, v5) : this.jjStartNfaWithStates_0(3, 5, 1);
                }
                case 70: {
                    return (0x10000L & v4) == 0L ? this.jjStartNfa_0(2, v4, v5) : this.jjStartNfaWithStates_0(3, 16, 1);
                }
                case 76: {
                    return (0x200000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_0(v4, 0x11400000000L, v5, 0L) : this.jjStartNfaWithStates_0(3, 33, 1);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x10000000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x80002000000000L, v5, 0L);
                }
                case 82: {
                    return (0x800000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_0(v4, 0xC000000000L, v5, 0L) : this.jjStartNfaWithStates_0(3, 0x2F, 1);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x4020000000000L, v5, 0L);
                }
                case 84: {
                    goto label_20;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x800002200L, v5, 0L);
                }
                case 86: {
                    return (0x80000000000L & v4) == 0L ? this.jjStartNfa_0(2, v4, v5) : this.jjStartNfaWithStates_0(3, 43, 1);
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x40000000000000L, v5, 4L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0L, v5, 0x40L);
                }
                case 100: {
                    goto label_29;
                }
                case 101: {
                    return (0x200000000000000L & v4) == 0L ? this.jjStartNfa_0(2, v4, v5) : this.jjStartNfaWithStates_0(3, 57, 1);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0L, v5, 2L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x8000000000000000L, v5, 0x120L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x400000000000000L, v5, 1L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0L, v5, 16L);
                }
                case 120: {
                    return this.jjMoveStringLiteralDfa4_0(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(2, v4, v5);
            return 3;
        }
        return this.jjStartNfa_0(2, v4, v5);
    label_20:
        if((0x80000000L & v4) != 0L) {
            this.jjmatchedKind = 0x1F;
            this.jjmatchedPos = 3;
            return this.jjMoveStringLiteralDfa4_0(v4, 0x10000DC00L, v5, 0L);
        }
        return (0x100000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_0(v4, 0x10000DC00L, v5, 0L) : this.jjStartNfaWithStates_0(3, 44, 1);
    label_29:
        if((0x200L & v5) != 0L) {
            this.jjmatchedKind = 73;
            this.jjmatchedPos = 3;
        }
        return this.jjMoveStringLiteralDfa4_0(v4, 0L, v5, 0x80L);
    }

    private int jjMoveStringLiteralDfa3_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(1, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x40000000000L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x8000004000000L, v5, 0L);
                }
                case 68: {
                    return (0x200000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_1(v4, 0x1C0L, v5, 0L) : this.jjStopAtPos(3, 45);
                }
                case 69: {
                    return (0x20L & v4) == 0L ? this.jjStartNfa_1(2, v4, v5) : this.jjStopAtPos(3, 5);
                }
                case 70: {
                    return (0x10000L & v4) == 0L ? this.jjStartNfa_1(2, v4, v5) : this.jjStopAtPos(3, 16);
                }
                case 76: {
                    return (0x200000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_1(v4, 0x11400000000L, v5, 0L) : this.jjStopAtPos(3, 33);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x10000000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x80002000000000L, v5, 0L);
                }
                case 82: {
                    return (0x800000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_1(v4, 0xC000000000L, v5, 0L) : this.jjStopAtPos(3, 0x2F);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x4020000000000L, v5, 0L);
                }
                case 84: {
                    goto label_19;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x800002200L, v5, 0L);
                }
                case 86: {
                    return (0x80000000000L & v4) == 0L ? this.jjStartNfa_1(2, v4, v5) : this.jjStopAtPos(3, 43);
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x40000000000000L, v5, 4L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0L, v5, 0x40L);
                }
                case 100: {
                    goto label_28;
                }
                case 101: {
                    return (0x200000000000000L & v4) == 0L ? this.jjStartNfa_1(2, v4, v5) : this.jjStopAtPos(3, 57);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0L, v5, 2L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x8000000000000000L, v5, 0x120L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x400000000000000L, v5, 1L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0L, v5, 16L);
                }
                case 120: {
                    return this.jjMoveStringLiteralDfa4_1(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 3;
        }
        return this.jjStartNfa_1(2, v4, v5);
    label_19:
        if((0x80000000L & v4) != 0L) {
            this.jjmatchedKind = 0x1F;
            this.jjmatchedPos = 3;
            return this.jjMoveStringLiteralDfa4_1(v4, 0x10000DC00L, v5, 0L);
        }
        return (0x100000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_1(v4, 0x10000DC00L, v5, 0L) : this.jjStopAtPos(3, 44);
    label_28:
        if((0x200L & v5) != 0L) {
            this.jjmatchedKind = 73;
            this.jjmatchedPos = 3;
        }
        return this.jjMoveStringLiteralDfa4_1(v4, 0L, v5, 0x80L);
    }

    private int jjMoveStringLiteralDfa3_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(1, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x40000000000L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x8000004000000L, v5, 0L);
                }
                case 68: {
                    return (0x200000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_2(v4, 0x1C0L, v5, 0L) : this.jjStopAtPos(3, 45);
                }
                case 69: {
                    return (0x20L & v4) == 0L ? this.jjStartNfa_2(2, v4, v5) : this.jjStopAtPos(3, 5);
                }
                case 70: {
                    return (0x10000L & v4) == 0L ? this.jjStartNfa_2(2, v4, v5) : this.jjStopAtPos(3, 16);
                }
                case 76: {
                    return (0x200000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_2(v4, 0x11400000000L, v5, 0L) : this.jjStopAtPos(3, 33);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x10000000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x80002000000000L, v5, 0L);
                }
                case 82: {
                    return (0x800000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_2(v4, 0xC000000000L, v5, 0L) : this.jjStopAtPos(3, 0x2F);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x4020000000000L, v5, 0L);
                }
                case 84: {
                    goto label_19;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x800002200L, v5, 0L);
                }
                case 86: {
                    return (0x80000000000L & v4) == 0L ? this.jjStartNfa_2(2, v4, v5) : this.jjStopAtPos(3, 43);
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x40000000000000L, v5, 4L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0L, v5, 0x40L);
                }
                case 100: {
                    goto label_28;
                }
                case 101: {
                    return (0x200000000000000L & v4) == 0L ? this.jjStartNfa_2(2, v4, v5) : this.jjStopAtPos(3, 57);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0L, v5, 2L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x8000000000000000L, v5, 0x120L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x400000000000000L, v5, 1L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0L, v5, 16L);
                }
                case 120: {
                    return this.jjMoveStringLiteralDfa4_2(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 3;
        }
        return this.jjStartNfa_2(2, v4, v5);
    label_19:
        if((0x80000000L & v4) != 0L) {
            this.jjmatchedKind = 0x1F;
            this.jjmatchedPos = 3;
            return this.jjMoveStringLiteralDfa4_2(v4, 0x10000DC00L, v5, 0L);
        }
        return (0x100000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_2(v4, 0x10000DC00L, v5, 0L) : this.jjStopAtPos(3, 44);
    label_28:
        if((0x200L & v5) != 0L) {
            this.jjmatchedKind = 73;
            this.jjmatchedPos = 3;
        }
        return this.jjMoveStringLiteralDfa4_2(v4, 0L, v5, 0x80L);
    }

    private int jjMoveStringLiteralDfa3_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(1, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x40000000000L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x8000004000000L, v5, 0L);
                }
                case 68: {
                    return (0x200000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_3(v4, 0x1C0L, v5, 0L) : this.jjStopAtPos(3, 45);
                }
                case 69: {
                    return (0x20L & v4) == 0L ? this.jjStartNfa_3(2, v4, v5) : this.jjStopAtPos(3, 5);
                }
                case 70: {
                    return (0x10000L & v4) == 0L ? this.jjStartNfa_3(2, v4, v5) : this.jjStopAtPos(3, 16);
                }
                case 76: {
                    return (0x200000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_3(v4, 0x11400000000L, v5, 0L) : this.jjStopAtPos(3, 33);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x10000000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x80002000000000L, v5, 0L);
                }
                case 82: {
                    return (0x800000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_3(v4, 0xC000000000L, v5, 0L) : this.jjStopAtPos(3, 0x2F);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x4020000000000L, v5, 0L);
                }
                case 84: {
                    goto label_19;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x800002200L, v5, 0L);
                }
                case 86: {
                    return (0x80000000000L & v4) == 0L ? this.jjStartNfa_3(2, v4, v5) : this.jjStopAtPos(3, 43);
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x40000000000000L, v5, 4L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0L, v5, 0x40L);
                }
                case 100: {
                    goto label_28;
                }
                case 101: {
                    return (0x200000000000000L & v4) == 0L ? this.jjStartNfa_3(2, v4, v5) : this.jjStopAtPos(3, 57);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0L, v5, 2L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x8000000000000000L, v5, 0x120L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x400000000000000L, v5, 1L);
                }
                case 0x75: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0L, v5, 16L);
                }
                case 120: {
                    return this.jjMoveStringLiteralDfa4_3(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 3;
        }
        return this.jjStartNfa_3(2, v4, v5);
    label_19:
        if((0x80000000L & v4) != 0L) {
            this.jjmatchedKind = 0x1F;
            this.jjmatchedPos = 3;
            return this.jjMoveStringLiteralDfa4_3(v4, 0x10000DC00L, v5, 0L);
        }
        return (0x100000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa4_3(v4, 0x10000DC00L, v5, 0L) : this.jjStopAtPos(3, 44);
    label_28:
        if((0x200L & v5) != 0L) {
            this.jjmatchedKind = 73;
            this.jjmatchedPos = 3;
        }
        return this.jjMoveStringLiteralDfa4_3(v4, 0L, v5, 0x80L);
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(2, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 0x20: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 0x80L);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x400DC00L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x80L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x4400000000L, v5, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x10000000000L, v5, 0L);
                }
                case 75: {
                    return (0x40L & v4) == 0L ? this.jjStartNfa_0(3, v4, v5) : this.jjStartNfaWithStates_0(4, 6, 1);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x8000000000L, v5, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x100L, v5, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x1000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x2200L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x42800000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x100000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa5_0(v4, 0x40000000000000L, v5, 0L) : this.jjStartNfaWithStates_0(4, 50, 1);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x20000000000L, v5, 0L);
                }
                case 91: {
                    goto label_24;
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 80L);
                }
                case 99: {
                    return (0x100L & v5) == 0L ? this.jjStartNfa_0(3, v4, v5) : this.jjStopAtPos(4, 72);
                }
                case 101: {
                    goto label_30;
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 8L);
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 1L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa5_0(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(3, v4, v5);
            return 4;
        }
        return this.jjStartNfa_0(3, v4, v5);
    label_24:
        if((0x8000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 51);
        }
        return (0x10000000000000L & v4) == 0L ? this.jjStartNfa_0(3, v4, v5) : this.jjStopAtPos(4, 52);
    label_30:
        if((0x400000000000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(4, 58, 1);
        }
        if((0x20L & v5) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 4;
        }
        return this.jjMoveStringLiteralDfa5_0(v4, 0x8000000000000000L, v5, 0L);
    }

    private int jjMoveStringLiteralDfa4_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(2, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 0x20: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 0x80L);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x400DC00L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x80L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x4400000000L, v5, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x10000000000L, v5, 0L);
                }
                case 75: {
                    return (0x40L & v4) == 0L ? this.jjStartNfa_1(3, v4, v5) : this.jjStopAtPos(4, 6);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x8000000000L, v5, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x100L, v5, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x1000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x2200L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x42800000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x100000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa5_1(v4, 0x40000000000000L, v5, 0L) : this.jjStopAtPos(4, 50);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x20000000000L, v5, 0L);
                }
                case 91: {
                    goto label_23;
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 80L);
                }
                case 99: {
                    return (0x100L & v5) == 0L ? this.jjStartNfa_1(3, v4, v5) : this.jjStopAtPos(4, 72);
                }
                case 101: {
                    goto label_29;
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 8L);
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 1L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa5_1(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 4;
        }
        return this.jjStartNfa_1(3, v4, v5);
    label_23:
        if((0x8000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 51);
        }
        return (0x10000000000000L & v4) == 0L ? this.jjStartNfa_1(3, v4, v5) : this.jjStopAtPos(4, 52);
    label_29:
        if((0x400000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 58);
        }
        if((0x20L & v5) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 4;
        }
        return this.jjMoveStringLiteralDfa5_1(v4, 0x8000000000000000L, v5, 0L);
    }

    private int jjMoveStringLiteralDfa4_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(2, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 0x20: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 0x80L);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x400DC00L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x80L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x4400000000L, v5, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x10000000000L, v5, 0L);
                }
                case 75: {
                    return (0x40L & v4) == 0L ? this.jjStartNfa_2(3, v4, v5) : this.jjStopAtPos(4, 6);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x8000000000L, v5, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x100L, v5, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x1000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x2200L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x42800000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x100000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa5_2(v4, 0x40000000000000L, v5, 0L) : this.jjStopAtPos(4, 50);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x20000000000L, v5, 0L);
                }
                case 91: {
                    goto label_23;
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 80L);
                }
                case 99: {
                    return (0x100L & v5) == 0L ? this.jjStartNfa_2(3, v4, v5) : this.jjStopAtPos(4, 72);
                }
                case 101: {
                    goto label_29;
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 8L);
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 1L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa5_2(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 4;
        }
        return this.jjStartNfa_2(3, v4, v5);
    label_23:
        if((0x8000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 51);
        }
        return (0x10000000000000L & v4) == 0L ? this.jjStartNfa_2(3, v4, v5) : this.jjStopAtPos(4, 52);
    label_29:
        if((0x400000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 58);
        }
        if((0x20L & v5) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 4;
        }
        return this.jjMoveStringLiteralDfa5_2(v4, 0x8000000000000000L, v5, 0L);
    }

    private int jjMoveStringLiteralDfa4_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(2, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 0x20: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 0x80L);
                }
                case 65: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x400DC00L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x80L, v5, 0L);
                }
                case 67: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x4400000000L, v5, 0L);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x10000000000L, v5, 0L);
                }
                case 75: {
                    return (0x40L & v4) == 0L ? this.jjStartNfa_3(3, v4, v5) : this.jjStopAtPos(4, 6);
                }
                case 76: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x8000000000L, v5, 0L);
                }
                case 78: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x100L, v5, 0L);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x1000000000L, v5, 0L);
                }
                case 80: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x2200L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x42800000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x100000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa5_3(v4, 0x40000000000000L, v5, 0L) : this.jjStopAtPos(4, 50);
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x20000000000L, v5, 0L);
                }
                case 91: {
                    goto label_23;
                }
                case 0x5F: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 80L);
                }
                case 99: {
                    return (0x100L & v5) == 0L ? this.jjStartNfa_3(3, v4, v5) : this.jjStopAtPos(4, 72);
                }
                case 101: {
                    goto label_29;
                }
                case 0x70: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0x4000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 8L);
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 1L);
                }
                case 0x76: {
                    return this.jjMoveStringLiteralDfa5_3(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 4;
        }
        return this.jjStartNfa_3(3, v4, v5);
    label_23:
        if((0x8000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 51);
        }
        return (0x10000000000000L & v4) == 0L ? this.jjStartNfa_3(3, v4, v5) : this.jjStopAtPos(4, 52);
    label_29:
        if((0x400000000000000L & v4) != 0L) {
            return this.jjStopAtPos(4, 58);
        }
        if((0x20L & v5) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 4;
        }
        return this.jjMoveStringLiteralDfa5_3(v4, 0x8000000000000000L, v5, 0L);
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(3, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x4400000000L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0xDC00L, v5, 0L);
                }
                case 69: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x2100000000L, v5, 0L);
                }
                case 71: {
                    return (0x40000000000L & v4) == 0L ? this.jjStartNfa_0(4, v4, v5) : this.jjStartNfaWithStates_0(5, 42, 1);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x100L, v5, 0L);
                }
                case 78: {
                    return (0x800000000L & v4) == 0L ? this.jjMoveStringLiteralDfa6_0(v4, 0x80000000000000L, v5, 0L) : this.jjStartNfaWithStates_0(5, 35, 1);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x40009000000080L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x20000000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x10000000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000L & v4) == 0L ? this.jjStartNfa_0(4, v4, v5) : this.jjStartNfaWithStates_0(5, 26, 1);
                }
                case 86: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x2200L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x4000000000000000L, v5, 0L);
                }
                case 100: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0x8000000000000000L, v5, 0L);
                }
                case 108: {
                    goto label_24;
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa6_0(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(4, v4, v5);
            return 5;
        }
        return this.jjStartNfa_0(4, v4, v5);
    label_24:
        if((16L & v5) != 0L) {
            return this.jjStopAtPos(5, 68);
        }
        return (0x40L & v5) == 0L ? this.jjMoveStringLiteralDfa6_0(v4, 0L, v5, 0x81L) : this.jjStopAtPos(5, 70);
    }

    private int jjMoveStringLiteralDfa5_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(3, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x4400000000L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0xDC00L, v5, 0L);
                }
                case 69: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x2100000000L, v5, 0L);
                }
                case 71: {
                    return (0x40000000000L & v4) == 0L ? this.jjStartNfa_1(4, v4, v5) : this.jjStopAtPos(5, 42);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x100L, v5, 0L);
                }
                case 78: {
                    return (0x800000000L & v4) == 0L ? this.jjMoveStringLiteralDfa6_1(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(5, 35);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x40009000000080L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x20000000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x10000000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000L & v4) == 0L ? this.jjStartNfa_1(4, v4, v5) : this.jjStopAtPos(5, 26);
                }
                case 86: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x2200L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x4000000000000000L, v5, 0L);
                }
                case 100: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0x8000000000000000L, v5, 0L);
                }
                case 108: {
                    goto label_23;
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa6_1(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 5;
        }
        return this.jjStartNfa_1(4, v4, v5);
    label_23:
        if((16L & v5) != 0L) {
            return this.jjStopAtPos(5, 68);
        }
        return (0x40L & v5) == 0L ? this.jjMoveStringLiteralDfa6_1(v4, 0L, v5, 0x81L) : this.jjStopAtPos(5, 70);
    }

    private int jjMoveStringLiteralDfa5_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(3, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x4400000000L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0xDC00L, v5, 0L);
                }
                case 69: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x2100000000L, v5, 0L);
                }
                case 71: {
                    return (0x40000000000L & v4) == 0L ? this.jjStartNfa_2(4, v4, v5) : this.jjStopAtPos(5, 42);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x100L, v5, 0L);
                }
                case 78: {
                    return (0x800000000L & v4) == 0L ? this.jjMoveStringLiteralDfa6_2(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(5, 35);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x40009000000080L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x20000000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x10000000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000L & v4) == 0L ? this.jjStartNfa_2(4, v4, v5) : this.jjStopAtPos(5, 26);
                }
                case 86: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x2200L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x4000000000000000L, v5, 0L);
                }
                case 100: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0x8000000000000000L, v5, 0L);
                }
                case 108: {
                    goto label_23;
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa6_2(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 5;
        }
        return this.jjStartNfa_2(4, v4, v5);
    label_23:
        if((16L & v5) != 0L) {
            return this.jjStopAtPos(5, 68);
        }
        return (0x40L & v5) == 0L ? this.jjMoveStringLiteralDfa6_2(v4, 0L, v5, 0x81L) : this.jjStopAtPos(5, 70);
    }

    private int jjMoveStringLiteralDfa5_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(3, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x4400000000L, v5, 0L);
                }
                case 66: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0xDC00L, v5, 0L);
                }
                case 69: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x2100000000L, v5, 0L);
                }
                case 71: {
                    return (0x40000000000L & v4) == 0L ? this.jjStartNfa_3(4, v4, v5) : this.jjStopAtPos(5, 42);
                }
                case 73: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x100L, v5, 0L);
                }
                case 78: {
                    return (0x800000000L & v4) == 0L ? this.jjMoveStringLiteralDfa6_3(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(5, 35);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x40009000000080L, v5, 0L);
                }
                case 82: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x20000000000L, v5, 0L);
                }
                case 83: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x10000000000L, v5, 0L);
                }
                case 84: {
                    return (0x4000000L & v4) == 0L ? this.jjStartNfa_3(4, v4, v5) : this.jjStopAtPos(5, 26);
                }
                case 86: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x2200L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x4000000000000000L, v5, 0L);
                }
                case 100: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0x8000000000000000L, v5, 0L);
                }
                case 108: {
                    goto label_23;
                }
                case 0x74: {
                    return this.jjMoveStringLiteralDfa6_3(v4, 0L, v5, 8L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 5;
        }
        return this.jjStartNfa_3(4, v4, v5);
    label_23:
        if((16L & v5) != 0L) {
            return this.jjStopAtPos(5, 68);
        }
        return (0x40L & v5) == 0L ? this.jjMoveStringLiteralDfa6_3(v4, 0L, v5, 0x81L) : this.jjStopAtPos(5, 70);
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(4, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0x2200L, v5, 0L);
                }
                case 69: {
                    return (0x20000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_0(v4, 0x80000000000000L, v5, 0L) : this.jjStartNfaWithStates_0(6, 41, 1);
                }
                case 76: {
                    return (0x100L & v4) == 0L ? this.jjMoveStringLiteralDfa7_0(v4, 0x440000C800L, v5, 0L) : this.jjStartNfaWithStates_0(6, 8, 1);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0x8000000080L, v5, 0L);
                }
                case 80: {
                    goto label_14;
                }
                case 84: {
                    goto label_19;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0x1400L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0L, v5, 8L);
                }
                case 101: {
                    return (0x4000000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_0(v4, 0x8000000000000000L, v5, 0L) : this.jjStopAtPos(6, 62);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0L, v5, 1L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0L, v5, 0x80L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa7_0(v4, 0L, v5, 6L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(5, v4, v5);
            return 6;
        }
        return this.jjStartNfa_0(5, v4, v5);
    label_14:
        if((0x1000000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(6, 36, 1);
        }
        if((0x2000000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(6, 37, 1);
        }
        return (0x40000000000000L & v4) == 0L ? this.jjStartNfa_0(5, v4, v5) : this.jjStartNfaWithStates_0(6, 54, 1);
    label_19:
        if((0x100000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(6, 0x20, 1);
        }
        return (0x10000000000L & v4) == 0L ? this.jjStartNfa_0(5, v4, v5) : this.jjStartNfaWithStates_0(6, 40, 1);
    }

    private int jjMoveStringLiteralDfa6_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(4, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0x2200L, v5, 0L);
                }
                case 69: {
                    return (0x20000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_1(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(6, 41);
                }
                case 76: {
                    return (0x100L & v4) == 0L ? this.jjMoveStringLiteralDfa7_1(v4, 0x440000C800L, v5, 0L) : this.jjStopAtPos(6, 8);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0x8000000080L, v5, 0L);
                }
                case 80: {
                    goto label_13;
                }
                case 84: {
                    goto label_18;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0x1400L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0L, v5, 8L);
                }
                case 101: {
                    return (0x4000000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_1(v4, 0x8000000000000000L, v5, 0L) : this.jjStopAtPos(6, 62);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0L, v5, 1L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0L, v5, 0x80L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa7_1(v4, 0L, v5, 6L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 6;
        }
        return this.jjStartNfa_1(5, v4, v5);
    label_13:
        if((0x1000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 36);
        }
        if((0x2000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 37);
        }
        return (0x40000000000000L & v4) == 0L ? this.jjStartNfa_1(5, v4, v5) : this.jjStopAtPos(6, 54);
    label_18:
        if((0x100000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 0x20);
        }
        return (0x10000000000L & v4) == 0L ? this.jjStartNfa_1(5, v4, v5) : this.jjStopAtPos(6, 40);
    }

    private int jjMoveStringLiteralDfa6_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(4, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0x2200L, v5, 0L);
                }
                case 69: {
                    return (0x20000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_2(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(6, 41);
                }
                case 76: {
                    return (0x100L & v4) == 0L ? this.jjMoveStringLiteralDfa7_2(v4, 0x440000C800L, v5, 0L) : this.jjStopAtPos(6, 8);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0x8000000080L, v5, 0L);
                }
                case 80: {
                    goto label_13;
                }
                case 84: {
                    goto label_18;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0x1400L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0L, v5, 8L);
                }
                case 101: {
                    return (0x4000000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_2(v4, 0x8000000000000000L, v5, 0L) : this.jjStopAtPos(6, 62);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0L, v5, 1L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0L, v5, 0x80L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa7_2(v4, 0L, v5, 6L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 6;
        }
        return this.jjStartNfa_2(5, v4, v5);
    label_13:
        if((0x1000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 36);
        }
        if((0x2000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 37);
        }
        return (0x40000000000000L & v4) == 0L ? this.jjStartNfa_2(5, v4, v5) : this.jjStopAtPos(6, 54);
    label_18:
        if((0x100000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 0x20);
        }
        return (0x10000000000L & v4) == 0L ? this.jjStartNfa_2(5, v4, v5) : this.jjStopAtPos(6, 40);
    }

    private int jjMoveStringLiteralDfa6_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(4, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 65: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0x2200L, v5, 0L);
                }
                case 69: {
                    return (0x20000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_3(v4, 0x80000000000000L, v5, 0L) : this.jjStopAtPos(6, 41);
                }
                case 76: {
                    return (0x100L & v4) == 0L ? this.jjMoveStringLiteralDfa7_3(v4, 0x440000C800L, v5, 0L) : this.jjStopAtPos(6, 8);
                }
                case 0x4F: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0x8000000080L, v5, 0L);
                }
                case 80: {
                    goto label_13;
                }
                case 84: {
                    goto label_18;
                }
                case 85: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0x1400L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0L, v5, 8L);
                }
                case 101: {
                    return (0x4000000000000000L & v4) == 0L ? this.jjMoveStringLiteralDfa7_3(v4, 0x8000000000000000L, v5, 0L) : this.jjStopAtPos(6, 62);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0L, v5, 1L);
                }
                case 0x6F: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0L, v5, 0x80L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa7_3(v4, 0L, v5, 6L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 6;
        }
        return this.jjStartNfa_3(5, v4, v5);
    label_13:
        if((0x1000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 36);
        }
        if((0x2000000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 37);
        }
        return (0x40000000000000L & v4) == 0L ? this.jjStartNfa_3(5, v4, v5) : this.jjStopAtPos(6, 54);
    label_18:
        if((0x100000000L & v4) != 0L) {
            return this.jjStopAtPos(6, 0x20);
        }
        return (0x10000000000L & v4) == 0L ? this.jjStartNfa_3(5, v4, v5) : this.jjStopAtPos(6, 40);
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(5, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 69: {
                    goto label_10;
                }
                case 76: {
                    goto label_15;
                }
                case 80: {
                    goto label_24;
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa8_0(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa8_0(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa8_0(v4, 0L, v5, 0x88L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa8_0(v4, 0x8000000000000000L, v5, 0L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa8_0(v4, 0L, v5, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(6, v4, v5);
            return 7;
        }
        return this.jjStartNfa_0(6, v4, v5);
    label_10:
        if((0x800L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 11, 1);
        }
        if((0x4000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 14, 1);
        }
        return (0x8000L & v4) == 0L ? this.jjStartNfa_0(6, v4, v5) : this.jjStartNfaWithStates_0(7, 15, 1);
    label_15:
        if((0x80L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 7, 1);
        }
        if((0x200L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 9, 1);
        }
        if((0x2000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 13, 1);
        }
        if((0x400000000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 34, 1);
        }
        return (0x4000000000L & v4) == 0L ? this.jjStartNfa_0(6, v4, v5) : this.jjStartNfaWithStates_0(7, 38, 1);
    label_24:
        if((0x400L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 10, 1);
        }
        if((0x1000L & v4) != 0L) {
            return this.jjStartNfaWithStates_0(7, 12, 1);
        }
        return (0x8000000000L & v4) == 0L ? this.jjStartNfa_0(6, v4, v5) : this.jjStartNfaWithStates_0(7, 39, 1);
    }

    private int jjMoveStringLiteralDfa7_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(5, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 69: {
                    goto label_9;
                }
                case 76: {
                    goto label_14;
                }
                case 80: {
                    goto label_23;
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa8_1(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa8_1(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa8_1(v4, 0L, v5, 0x88L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa8_1(v4, 0x8000000000000000L, v5, 0L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa8_1(v4, 0L, v5, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 7;
        }
        return this.jjStartNfa_1(6, v4, v5);
    label_9:
        if((0x800L & v4) != 0L) {
            return this.jjStopAtPos(7, 11);
        }
        if((0x4000L & v4) != 0L) {
            return this.jjStopAtPos(7, 14);
        }
        return (0x8000L & v4) == 0L ? this.jjStartNfa_1(6, v4, v5) : this.jjStopAtPos(7, 15);
    label_14:
        if((0x80L & v4) != 0L) {
            return this.jjStopAtPos(7, 7);
        }
        if((0x200L & v4) != 0L) {
            return this.jjStopAtPos(7, 9);
        }
        if((0x2000L & v4) != 0L) {
            return this.jjStopAtPos(7, 13);
        }
        if((0x400000000L & v4) != 0L) {
            return this.jjStopAtPos(7, 34);
        }
        return (0x4000000000L & v4) == 0L ? this.jjStartNfa_1(6, v4, v5) : this.jjStopAtPos(7, 38);
    label_23:
        if((0x400L & v4) != 0L) {
            return this.jjStopAtPos(7, 10);
        }
        if((0x1000L & v4) != 0L) {
            return this.jjStopAtPos(7, 12);
        }
        return (0x8000000000L & v4) == 0L ? this.jjStartNfa_1(6, v4, v5) : this.jjStopAtPos(7, 39);
    }

    private int jjMoveStringLiteralDfa7_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(5, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 69: {
                    goto label_9;
                }
                case 76: {
                    goto label_14;
                }
                case 80: {
                    goto label_23;
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa8_2(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa8_2(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa8_2(v4, 0L, v5, 0x88L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa8_2(v4, 0x8000000000000000L, v5, 0L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa8_2(v4, 0L, v5, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 7;
        }
        return this.jjStartNfa_2(6, v4, v5);
    label_9:
        if((0x800L & v4) != 0L) {
            return this.jjStopAtPos(7, 11);
        }
        if((0x4000L & v4) != 0L) {
            return this.jjStopAtPos(7, 14);
        }
        return (0x8000L & v4) == 0L ? this.jjStartNfa_2(6, v4, v5) : this.jjStopAtPos(7, 15);
    label_14:
        if((0x80L & v4) != 0L) {
            return this.jjStopAtPos(7, 7);
        }
        if((0x200L & v4) != 0L) {
            return this.jjStopAtPos(7, 9);
        }
        if((0x2000L & v4) != 0L) {
            return this.jjStopAtPos(7, 13);
        }
        if((0x400000000L & v4) != 0L) {
            return this.jjStopAtPos(7, 34);
        }
        return (0x4000000000L & v4) == 0L ? this.jjStartNfa_2(6, v4, v5) : this.jjStopAtPos(7, 38);
    label_23:
        if((0x400L & v4) != 0L) {
            return this.jjStopAtPos(7, 10);
        }
        if((0x1000L & v4) != 0L) {
            return this.jjStopAtPos(7, 12);
        }
        return (0x8000000000L & v4) == 0L ? this.jjStartNfa_2(6, v4, v5) : this.jjStopAtPos(7, 39);
    }

    private int jjMoveStringLiteralDfa7_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(5, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 69: {
                    goto label_9;
                }
                case 76: {
                    goto label_14;
                }
                case 80: {
                    goto label_23;
                }
                case 88: {
                    return this.jjMoveStringLiteralDfa8_3(v4, 0x80000000000000L, v5, 0L);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa8_3(v4, 0L, v5, 6L);
                }
                case 99: {
                    return this.jjMoveStringLiteralDfa8_3(v4, 0L, v5, 0x88L);
                }
                case 102: {
                    return this.jjMoveStringLiteralDfa8_3(v4, 0x8000000000000000L, v5, 0L);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa8_3(v4, 0L, v5, 1L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 7;
        }
        return this.jjStartNfa_3(6, v4, v5);
    label_9:
        if((0x800L & v4) != 0L) {
            return this.jjStopAtPos(7, 11);
        }
        if((0x4000L & v4) != 0L) {
            return this.jjStopAtPos(7, 14);
        }
        return (0x8000L & v4) == 0L ? this.jjStartNfa_3(6, v4, v5) : this.jjStopAtPos(7, 15);
    label_14:
        if((0x80L & v4) != 0L) {
            return this.jjStopAtPos(7, 7);
        }
        if((0x200L & v4) != 0L) {
            return this.jjStopAtPos(7, 9);
        }
        if((0x2000L & v4) != 0L) {
            return this.jjStopAtPos(7, 13);
        }
        if((0x400000000L & v4) != 0L) {
            return this.jjStopAtPos(7, 34);
        }
        return (0x4000000000L & v4) == 0L ? this.jjStartNfa_3(6, v4, v5) : this.jjStopAtPos(7, 38);
    label_23:
        if((0x400L & v4) != 0L) {
            return this.jjStopAtPos(7, 10);
        }
        if((0x1000L & v4) != 0L) {
            return this.jjStopAtPos(7, 12);
        }
        return (0x8000000000L & v4) == 0L ? this.jjStartNfa_3(6, v4, v5) : this.jjStopAtPos(7, 39);
    }

    private int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(6, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 84: {
                    return (0x80000000000000L & v4) == 0L ? this.jjStartNfa_0(7, v4, v5) : this.jjStartNfaWithStates_0(8, 55, 1);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0L, v5, 0x80L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0x8000000000000000L, v5, 0L);
                }
                case 107: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0L, v5, 8L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa9_0(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(7, v4, v5);
            return 8;
        }
        return this.jjStartNfa_0(7, v4, v5);
    }

    private int jjMoveStringLiteralDfa8_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(6, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 84: {
                    return (0x80000000000000L & v4) == 0L ? this.jjStartNfa_1(7, v4, v5) : this.jjStopAtPos(8, 55);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0L, v5, 0x80L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0x8000000000000000L, v5, 0L);
                }
                case 107: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0L, v5, 8L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa9_1(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 8;
        }
        return this.jjStartNfa_1(7, v4, v5);
    }

    private int jjMoveStringLiteralDfa8_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(6, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 84: {
                    return (0x80000000000000L & v4) == 0L ? this.jjStartNfa_2(7, v4, v5) : this.jjStopAtPos(8, 55);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0L, v5, 0x80L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0x8000000000000000L, v5, 0L);
                }
                case 107: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0L, v5, 8L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa9_2(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 8;
        }
        return this.jjStartNfa_2(7, v4, v5);
    }

    private int jjMoveStringLiteralDfa8_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(6, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 84: {
                    return (0x80000000000000L & v4) == 0L ? this.jjStartNfa_3(7, v4, v5) : this.jjStopAtPos(8, 55);
                }
                case 97: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0L, v5, 0x80L);
                }
                case 101: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0L, v5, 1L);
                }
                case 105: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0x8000000000000000L, v5, 0L);
                }
                case 107: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0L, v5, 8L);
                }
                case 109: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0L, v5, 2L);
                }
                case 0x72: {
                    return this.jjMoveStringLiteralDfa9_3(v4, 0L, v5, 4L);
                }
            }
        }
        catch(IOException unused_ex) {
            return 8;
        }
        return this.jjStartNfa_3(7, v4, v5);
    }

    private int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_0(7, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return this.jjMoveStringLiteralDfa10_0(v4, 0L, v5, 1L);
                }
                case 103: {
                    return (4L & v5) == 0L ? this.jjStartNfa_0(8, v4, v5) : this.jjStopAtPos(9, 66);
                }
                case 108: {
                    return (0x80L & v5) == 0L ? this.jjStartNfa_0(8, v4, v5) : this.jjStopAtPos(9, 71);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa10_0(v4, 0x8000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return (2L & v5) == 0L ? this.jjMoveStringLiteralDfa10_0(v4, 0L, v5, 8L) : this.jjStopAtPos(9, 65);
                }
            }
        }
        catch(IOException unused_ex) {
            this.jjStopStringLiteralDfa_0(8, v4, v5);
            return 9;
        }
        return this.jjStartNfa_0(8, v4, v5);
    }

    private int jjMoveStringLiteralDfa9_1(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_1(7, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return this.jjMoveStringLiteralDfa10_1(v4, 0L, v5, 1L);
                }
                case 103: {
                    return (4L & v5) == 0L ? this.jjStartNfa_1(8, v4, v5) : this.jjStopAtPos(9, 66);
                }
                case 108: {
                    return (0x80L & v5) == 0L ? this.jjStartNfa_1(8, v4, v5) : this.jjStopAtPos(9, 71);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa10_1(v4, 0x8000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return (2L & v5) == 0L ? this.jjMoveStringLiteralDfa10_1(v4, 0L, v5, 8L) : this.jjStopAtPos(9, 65);
                }
            }
        }
        catch(IOException unused_ex) {
            return 9;
        }
        return this.jjStartNfa_1(8, v4, v5);
    }

    private int jjMoveStringLiteralDfa9_2(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_2(7, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return this.jjMoveStringLiteralDfa10_2(v4, 0L, v5, 1L);
                }
                case 103: {
                    return (4L & v5) == 0L ? this.jjStartNfa_2(8, v4, v5) : this.jjStopAtPos(9, 66);
                }
                case 108: {
                    return (0x80L & v5) == 0L ? this.jjStartNfa_2(8, v4, v5) : this.jjStopAtPos(9, 71);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa10_2(v4, 0x8000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return (2L & v5) == 0L ? this.jjMoveStringLiteralDfa10_2(v4, 0L, v5, 8L) : this.jjStopAtPos(9, 65);
                }
            }
        }
        catch(IOException unused_ex) {
            return 9;
        }
        return this.jjStartNfa_2(8, v4, v5);
    }

    private int jjMoveStringLiteralDfa9_3(long old0, long active0, long old1, long active1) {
        long v4 = active0 & old0;
        long v5 = active1 & old1;
        if((v4 | v5) == 0L) {
            return this.jjStartNfa_3(7, old0, old1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch(this.curChar) {
                case 100: {
                    return this.jjMoveStringLiteralDfa10_3(v4, 0L, v5, 1L);
                }
                case 103: {
                    return (4L & v5) == 0L ? this.jjStartNfa_3(8, v4, v5) : this.jjStopAtPos(9, 66);
                }
                case 108: {
                    return (0x80L & v5) == 0L ? this.jjStartNfa_3(8, v4, v5) : this.jjStopAtPos(9, 71);
                }
                case 110: {
                    return this.jjMoveStringLiteralDfa10_3(v4, 0x8000000000000000L, v5, 0L);
                }
                case 0x73: {
                    return (2L & v5) == 0L ? this.jjMoveStringLiteralDfa10_3(v4, 0L, v5, 8L) : this.jjStopAtPos(9, 65);
                }
            }
        }
        catch(IOException unused_ex) {
            return 9;
        }
        return this.jjStartNfa_3(8, v4, v5);
    }

    private int jjStartNfaWithStates_0(int pos, int kind, int state) {
        try {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = pos;
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_0(state, pos + 1);
        }
        catch(IOException unused_ex) {
            return pos + 1;
        }
    }

    private int jjStartNfaWithStates_1(int pos, int kind, int state) {
        try {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = pos;
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_1(state, pos + 1);
        }
        catch(IOException unused_ex) {
            return pos + 1;
        }
    }

    private int jjStartNfaWithStates_2(int pos, int kind, int state) {
        try {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = pos;
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_2(state, pos + 1);
        }
        catch(IOException unused_ex) {
            return pos + 1;
        }
    }

    private int jjStartNfaWithStates_3(int pos, int kind, int state) {
        try {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = pos;
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_3(state, pos + 1);
        }
        catch(IOException unused_ex) {
            return pos + 1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0, long active1) {
        return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
    }

    private final int jjStartNfa_1(int pos, long active0, long active1) {
        return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(pos, active0, active1), pos + 1);
    }

    private final int jjStartNfa_2(int pos, long active0, long active1) {
        return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(pos, active0, active1), pos + 1);
    }

    private final int jjStartNfa_3(int pos, long active0, long active1) {
        return this.jjMoveNfa_3(this.jjStopStringLiteralDfa_3(pos, active0, active1), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1) {
        switch(pos) {
            case 0: {
                if((0x7FFFFFFFFFFFFE0L & active0) != 0L) {
                    this.jjmatchedKind = 61;
                    return 1;
                }
                if((14L & active0) != 0L) {
                    return 52;
                }
                return (0xC000000000000000L & active0) == 0L && (0xBFFL & active1) == 0L ? -1 : 30;
            }
            case 1: {
                if((0x20000072000000L & active0) == 0L) {
                    if((0x7DFFFFF8DFFFFE0L & active0) == 0L) {
                        return -1;
                    }
                    if(this.jjmatchedPos != 1) {
                        this.jjmatchedKind = 61;
                        this.jjmatchedPos = 1;
                        return 1;
                    }
                }
                break;
            }
            case 2: {
                if((0x10340000BFE0000L & active0) == 0L) {
                    if((0x6DCBFFF8401FFE0L & active0) != 0L) {
                        this.jjmatchedKind = 61;
                        this.jjmatchedPos = 2;
                        return 1;
                    }
                    return -1;
                }
                break;
            }
            case 3: {
                if((0x200B80380010020L & active0) == 0L) {
                    if((0x4DC07FC0400FFC0L & active0) == 0L) {
                        return -1;
                    }
                    if(this.jjmatchedPos != 3) {
                        this.jjmatchedKind = 61;
                        this.jjmatchedPos = 3;
                        return 1;
                    }
                }
                break;
            }
            case 4: {
                if((0x404000000000040L & active0) == 0L) {
                    if((0xC007FD0400FF80L & active0) == 0L) {
                        return -1;
                    }
                    if(this.jjmatchedPos != 4) {
                        this.jjmatchedKind = 61;
                        this.jjmatchedPos = 4;
                        return 1;
                    }
                }
                break;
            }
            case 5: {
                if((0x40804000000L & active0) == 0L) {
                    if((0xC003F50000FF80L & active0) != 0L) {
                        this.jjmatchedKind = 61;
                        this.jjmatchedPos = 5;
                        return 1;
                    }
                    return -1;
                }
                break;
            }
            case 6: {
                if((0x8000C40000FE80L & active0) != 0L) {
                    this.jjmatchedKind = 61;
                    this.jjmatchedPos = 6;
                    return 1;
                }
                return (0x40033100000100L & active0) == 0L ? -1 : 1;
            }
            case 7: {
                if((0x80000000000000L & active0) != 0L) {
                    this.jjmatchedKind = 61;
                    this.jjmatchedPos = 7;
                    return 1;
                }
                return (0xC40000FE80L & active0) == 0L ? -1 : 1;
            }
            case 8: {
                return (0x80000000000000L & active0) == 0L ? -1 : 1;
            }
            default: {
                return -1;
            }
        }
        return 1;
    }

    private final int jjStopStringLiteralDfa_1(int pos, long active0, long active1) {
        if(pos != 0) {
            return -1;
        }
        if((14L & active0) != 0L) {
            return 51;
        }
        return (0xC000000000000000L & active0) == 0L && (0xBFFL & active1) == 0L ? -1 : 27;
    }

    private final int jjStopStringLiteralDfa_2(int pos, long active0, long active1) {
        if(pos != 0) {
            return -1;
        }
        if((0xC000000000000000L & active0) != 0L || (0xBFFL & active1) != 0L) {
            return 26;
        }
        return (14L & active0) == 0L ? -1 : 51;
    }

    private final int jjStopStringLiteralDfa_3(int pos, long active0, long active1) {
        if(pos != 0) {
            return -1;
        }
        if((0xC000000000000000L & active0) != 0L || (0xBFFL & active1) != 0L) {
            return 28;
        }
        return (14L & active0) == 0L ? -1 : 50;
    }

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }
}

