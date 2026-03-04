package luaj;

import android.ext.Log;
import android.ext.Script.DebugFunction;
import luaj.lib.DebugLib.CallFrame;
import luaj.lib.DebugLib;

public class LuaClosure extends LuaFunction {
    private static final UpValue[] NOUPVALUES;
    private static int deep;
    final Globals globals;
    public final Prototype p;
    public volatile int pc;
    public String sourceFile;
    public UpValue[] upValues;
    private Varargs varargs;

    static {
        LuaClosure.NOUPVALUES = new UpValue[0];
        LuaClosure.deep = 0;
    }

    public LuaClosure(Prototype p, LuaValue env, boolean fillUpValues) {
        this.sourceFile = null;
        this.varargs = LuaClosure.NONE;
        this.p = p;
        if(p.upvalues != null && p.upvalues.length != 0) {
            int len = p.upvalues.length;
            UpValue[] upValues = new UpValue[len];
            if(fillUpValues) {
                LuaValue[] stack = new LuaValue[len];
                stack[0] = env;
                upValues[0] = new UpValue(stack, 0);
                for(int i = 1; i < len; ++i) {
                    stack[i] = LuaClosure.NIL;
                    upValues[i] = new UpValue(stack, i);
                }
            }
            this.upValues = upValues;
        }
        else {
            this.upValues = LuaClosure.NOUPVALUES;
        }
        this.globals = env instanceof Globals ? ((Globals)env) : null;
    }

    @Override  // luaj.LuaValue
    public final LuaValue call() {
        return this.execute(this.getNewStack(), LuaClosure.NONE, false).arg1();
    }

    @Override  // luaj.LuaValue
    public final LuaValue call(LuaValue arg) {
        LuaValue[] arr_luaValue = this.getNewStack();
        if(this.p.numparams != 0) {
            arr_luaValue[0] = arg;
            return this.execute(arr_luaValue, LuaClosure.NONE, false).arg1();
        }
        if(this.p.is_vararg == 0) {
            arg = LuaClosure.NONE;
        }
        return this.execute(arr_luaValue, arg, false).arg1();
    }

    @Override  // luaj.LuaValue
    public final LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaValue[] arr_luaValue = this.getNewStack();
        switch(this.p.numparams) {
            case 0: {
                return this.p.is_vararg == 0 ? this.execute(arr_luaValue, LuaClosure.NONE, false).arg1() : this.execute(arr_luaValue, LuaClosure.varargsOf(arg1, arg2), false).arg1();
            }
            case 1: {
                arr_luaValue[0] = arg1;
                if(this.p.is_vararg == 0) {
                    arg2 = LuaClosure.NONE;
                }
                return this.execute(arr_luaValue, arg2, false).arg1();
            }
            default: {
                arr_luaValue[0] = arg1;
                arr_luaValue[1] = arg2;
                return this.execute(arr_luaValue, LuaClosure.NONE, false).arg1();
            }
        }
    }

    @Override  // luaj.LuaValue
    public final LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        LuaValue[] arr_luaValue = this.getNewStack();
        switch(this.p.numparams) {
            case 0: {
                return this.p.is_vararg == 0 ? this.execute(arr_luaValue, LuaClosure.NONE, false).arg1() : this.execute(arr_luaValue, LuaClosure.varargsOf(arg1, arg2, arg3), false).arg1();
            }
            case 1: {
                arr_luaValue[0] = arg1;
                return this.p.is_vararg == 0 ? this.execute(arr_luaValue, LuaClosure.NONE, false).arg1() : this.execute(arr_luaValue, LuaClosure.varargsOf(arg2, arg3), false).arg1();
            }
            case 2: {
                arr_luaValue[0] = arg1;
                arr_luaValue[1] = arg2;
                if(this.p.is_vararg == 0) {
                    arg3 = LuaClosure.NONE;
                }
                return this.execute(arr_luaValue, arg3, false).arg1();
            }
            default: {
                arr_luaValue[0] = arg1;
                arr_luaValue[1] = arg2;
                arr_luaValue[2] = arg3;
                return this.execute(arr_luaValue, LuaClosure.NONE, false).arg1();
            }
        }
    }

    private LuaValue checkRet(LuaValue ret) {
        if(ret == null) {
            throw new NullPointerException("return null as LuaValue");
        }
        return ret;
    }

    private Varargs checkRet(Varargs ret) {
        if(ret == null) {
            throw new NullPointerException("return null as Varargs");
        }
        return ret;
    }

    @Override  // luaj.LuaValue
    public LuaClosure checkclosure() {
        return this;
    }

    private static void errorHook(Globals globals, LuaError le, StringBuilder msg) {
        if(globals != null) {
            LuaThread r = globals.running;
            if(r.errorfunc != null) {
                le.traceback = msg.toString();
                try {
                    LuaValue luaValue0 = r.errorfunc;
                    LuaValue luaValue1 = le.object == null ? LuaValue.valueOf(le.traceback) : le.object;
                    le.object = luaValue0.call(luaValue1);
                }
                catch(Throwable unused_ex) {
                    le.object = LuaValue.valueOf("error in error handling");
                }
                return;
            }
            if(globals.debuglib != null) {
                msg.append('\n');
                msg.append(globals.debuglib.traceback(globals, le.level));
            }
        }
        le.traceback = msg.toString();
    }

    protected Varargs execute(LuaValue[] stack, Varargs varargs_, boolean tailcall) {
        Throwable throwable3;
        String name;
        Varargs v;
        boolean notcl;
        Varargs args;
        Varargs args;
        Prototype[] pp;
        String sourceFile;
        LuaValue NONE;
        LuaBoolean FALSE;
        LuaBoolean TRUE;
        LuaValue NIL;
        int i = 0;
        int a = 0;
        int b = 0;
        int c = 0;
        int pc = 0;
        int top = 0;
        Varargs v = LuaClosure.NONE;
        int[] code = this.p.code;
        LuaValue[] k = this.p.k;
        UpValue[] upValues = this.upValues;
        this.varargs = varargs_;
        UpValue[] openups = this.p.p.length <= 0 ? null : new UpValue[stack.length];
        Globals glob = this.globals;
        DebugLib debuglib = glob == null ? null : glob.debuglib;
        if(debuglib != null) {
            debuglib.onCall(this, this.varargs, stack, tailcall);
        }
        try {
            ++LuaClosure.deep;
            NIL = LuaValue.NIL;
            TRUE = LuaValue.TRUE;
            FALSE = LuaValue.FALSE;
            NONE = LuaValue.NONE;
            LuaNumber ZERO = LuaValue.ZERO;
            sourceFile = this.sourceFile;
            pp = this.p.p;
            Thread thread0 = Thread.currentThread();
        alab1:
            while(true) {
            label_25:
                if(thread0.isInterrupted()) {
                    throw new InterruptedException("Script thread interrupted");
                }
                if(debuglib != null) {
                    debuglib.onInstruction(pc, v, top);
                }
                this.pc = pc;
                i = code[pc];
                a = i >> 6 & 0xFF;
                switch(i & 0x3F) {
                    case 0: {
                        goto label_122;
                    }
                    case 1: {
                        goto label_124;
                    }
                    case 2: {
                        goto label_126;
                    }
                    case 3: {
                        goto label_132;
                    }
                    case 4: {
                        goto label_136;
                    }
                    case 5: {
                        goto label_147;
                    }
                    case 6: {
                        goto label_151;
                    }
                    case 7: {
                        goto label_155;
                    }
                    case 8: {
                        goto label_157;
                    }
                    case 9: {
                        goto label_163;
                    }
                    case 10: {
                        goto label_167;
                    }
                    case 11: {
                        goto label_171;
                    }
                    case 12: {
                        goto label_173;
                    }
                    case 13: {
                        goto label_178;
                    }
                    case 14: {
                        goto label_182;
                    }
                    case 15: {
                        goto label_186;
                    }
                    case 16: {
                        goto label_190;
                    }
                    case 17: {
                        goto label_194;
                    }
                    case 18: {
                        goto label_198;
                    }
                    case 19: {
                        goto label_202;
                    }
                    case 20: {
                        goto label_204;
                    }
                    case 21: {
                        goto label_206;
                    }
                    case 22: {
                        goto label_208;
                    }
                    case 23: {
                        goto label_220;
                    }
                    case 24: {
                        goto label_232;
                    }
                    case 25: {
                        goto label_237;
                    }
                    case 26: {
                        goto label_242;
                    }
                    case 27: {
                        goto label_247;
                    }
                    case 28: {
                        goto label_250;
                    }
                    case 29: {
                        goto label_256;
                    }
                    case 30: {
                        this.setCaller(stack[a]);
                        switch(0xFF800000 & i) {
                            case 0x800000: {
                                args = NONE;
                                break;
                            }
                            case 0x1000000: {
                                args = stack[a + 1];
                                break;
                            }
                            case 0x1800000: {
                                args = LuaClosure.varargsOf(stack[a + 1], stack[a + 2]);
                                break;
                            }
                            case 0x2000000: {
                                args = LuaClosure.varargsOf(stack[a + 1], stack[a + 2], stack[a + 3]);
                                break;
                            }
                            default: {
                                b = i >>> 23;
                                args = b <= 0 ? LuaClosure.varargsOf(stack, a + 1, top - v.narg() - (a + 1), v) : LuaClosure.varargsOf(stack, a + 1, b - 1);
                            }
                        }
                        args = new TailcallVarargs(glob, stack[a], args);
                        if(!tailcall) {
                            args = args.eval();
                        }
                        break alab1;
                    }
                    case 0x1F: {
                        goto label_64;
                    }
                    case 0x20: {
                        goto label_322;
                    }
                    case 33: {
                        goto label_333;
                    }
                    case 34: {
                        goto label_341;
                    }
                    case 35: {
                        goto label_355;
                    }
                    case 36: {
                        goto label_359;
                    }
                    case 37: {
                        goto label_386;
                    }
                    case 38: {
                        goto label_402;
                    }
                    case 39: {
                        throw new IllegalArgumentException("Uexecutable opcode: EXTRAARG");
                    }
                    case 40: {
                        goto label_415;
                    }
                    case 41: {
                        goto label_419;
                    }
                    case 42: {
                        goto label_421;
                    }
                    case 43: {
                        goto label_425;
                    }
                    case 44: {
                        goto label_429;
                    }
                    case 45: {
                        goto label_433;
                    }
                    case 46: {
                        goto label_437;
                    }
                    case 0x2F: {
                        goto label_441;
                    }
                    case 0x30: {
                        goto label_446;
                    }
                    default: {
                        if((i & 0x3F) != 0x3F) {
                            throw new IllegalArgumentException("Illegal opcode: " + (i & 0x3F));
                        }
                        throw new LuaError("Damaged script: 5");
                    }
                }
            }
        }
        catch(LuaError le) {
            goto label_451;
        }
        catch(StackOverflowError e) {
            goto label_519;
        }
        catch(InterruptedException e) {
            goto label_522;
        }
        catch(Throwable e) {
            goto label_525;
        }
        if(openups != null) {
            int u = stack.length;
            while(true) {
                --u;
                if(u < 0) {
                    break;
                }
                UpValue upValue = openups[u];
                if(upValue != null) {
                    upValue.close();
                }
            }
        }
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        return args;
        try {
        label_64:
            b = i >>> 23;
            switch(b) {
                case 0: {
                    goto label_80;
                }
                case 1: {
                    goto label_94;
                }
                case 2: {
                    goto label_107;
                }
                default: {
                    args = LuaClosure.varargsOf(stack, a, b - 1);
                    if(openups != null) {
                        break;
                    }
                    goto label_75;
                }
            }
        }
        catch(LuaError le) {
            goto label_451;
        }
        catch(StackOverflowError e) {
            goto label_519;
        }
        catch(InterruptedException e) {
            goto label_522;
        }
        catch(Throwable e) {
            goto label_525;
        }
        int u = stack.length;
        while(true) {
            --u;
            if(u < 0) {
                break;
            }
            UpValue upValue = openups[u];
            if(upValue != null) {
                upValue.close();
            }
        }
    label_75:
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        return args;
        try {
        label_80:
            args = LuaClosure.varargsOf(stack, a, top - v.narg() - a, v);
            if(openups != null) {
                goto label_82;
            }
            goto label_89;
        }
        catch(LuaError le) {
            goto label_451;
        }
        catch(StackOverflowError e) {
            goto label_519;
        }
        catch(InterruptedException e) {
            goto label_522;
        }
        catch(Throwable e) {
            goto label_525;
        }
    label_82:
        int u = stack.length;
        while(true) {
            --u;
            if(u < 0) {
                break;
            }
            UpValue upValue = openups[u];
            if(upValue != null) {
                upValue.close();
            }
        }
    label_89:
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        return args;
    label_94:
        if(openups != null) {
            int u = stack.length;
            while(true) {
                --u;
                if(u < 0) {
                    break;
                }
                UpValue upValue = openups[u];
                if(upValue != null) {
                    upValue.close();
                }
            }
        }
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        return NONE;
        try {
        label_107:
            args = stack[a];
            if(openups != null) {
                goto label_109;
            }
            goto label_116;
        }
        catch(LuaError le) {
            goto label_451;
        }
        catch(StackOverflowError e) {
            goto label_519;
        }
        catch(InterruptedException e) {
            goto label_522;
        }
        catch(Throwable e) {
            goto label_525;
        }
    label_109:
        int u = stack.length;
        while(true) {
            --u;
            if(u < 0) {
                break;
            }
            UpValue upValue = openups[u];
            if(upValue != null) {
                upValue.close();
            }
        }
    label_116:
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        return args;
        try {
            throw new IllegalArgumentException("Uexecutable opcode: EXTRAARG");
        label_122:
            stack[a] = stack[i >>> 23];
            goto label_448;
        label_124:
            stack[a] = k[i >>> 14];
            goto label_448;
        label_126:
            ++pc;
            i = code[pc];
            if((i & 0x3F) != 39) {
                throw new LuaError("EXTRAARG expected after LOADKX, got " + ((i & 0x3F) >= Print.OPNAMES.length - 1 ? "UNKNOWN_OP_" + (i & 0x3F) : Print.OPNAMES[i & 0x3F]));
            }
            stack[a] = k[i >>> 6];
            goto label_448;
        label_132:
            stack[a] = i >>> 23 == 0 ? FALSE : TRUE;
            if((0x7FC000 & i) != 0) {
                ++pc;
                goto label_448;
            label_136:
                int b = i >>> 23;
                int a;
                for(a = a; true; a = a) {
                    b = b - 1;
                    if(b < 0) {
                        break;
                    }
                    a = a + 1;
                    stack[a] = NIL;
                    b = b;
                }
                a = a;
                goto label_448;
            label_147:
                if(i >>> 23 >= upValues.length) {
                    throw LuaClosure.upValueOutOfRange(1, i >>> 23, upValues.length);
                }
                stack[a] = upValues[i >>> 23].getValue();
                goto label_448;
            label_151:
                if(i >>> 23 >= upValues.length) {
                    throw LuaClosure.upValueOutOfRange(2, i >>> 23, upValues.length);
                }
                c = i >> 14 & 0x1FF;
                goto label_444;
            label_155:
                c = i >> 14 & 0x1FF;
                goto label_447;
            label_157:
                if(a >= upValues.length) {
                    throw LuaClosure.upValueOutOfRange(3, a, upValues.length);
                }
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                upValues[a].getValue().set((b <= 0xFF ? stack[b] : k[b & 0xFF]), (c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_163:
                if(i >>> 23 >= upValues.length) {
                    throw LuaClosure.upValueOutOfRange(4, i >>> 23, upValues.length);
                }
                upValues[i >>> 23].setValue(stack[a]);
                goto label_448;
            label_167:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a].set((b <= 0xFF ? stack[b] : k[b & 0xFF]), (c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_171:
                stack[a] = new LuaTable(i >>> 23, i >> 14 & 0x1FF);
                goto label_448;
            label_173:
                LuaValue o = stack[i >>> 23];
                stack[a + 1] = o;
                c = i >> 14 & 0x1FF;
                stack[a] = o.get((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_178:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).add((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_182:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).sub((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_186:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).mul((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_190:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).div((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_194:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).mod((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_198:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).pow((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                goto label_448;
            label_202:
                stack[a] = stack[i >>> 23].neg();
                goto label_448;
            label_204:
                stack[a] = stack[i >>> 23].not();
                goto label_448;
            label_206:
                stack[a] = stack[i >>> 23].len();
                goto label_448;
            label_208:
                b = i >>> 23;
                c = i >> 14 & 0x1FF;
                if(c > b + 1) {
                    Buffer buffer0 = stack[c].buffer();
                    while(true) {
                        --c;
                        if(c < b) {
                            break;
                        }
                        buffer0.concatTo(stack[c]);
                    }
                    stack[a] = buffer0.value();
                }
                else {
                    stack[a] = stack[c - 1].concat(stack[c]);
                }
                goto label_448;
            label_220:
                pc += (i >>> 14) - 0x1FFFF;
                if(a > 0) {
                    --a;
                    if(openups != null) {
                        b = stack.length;
                        while(true) {
                            --b;
                            if(b < 0) {
                                goto label_448;
                            }
                            UpValue openupsB = openups[b];
                            if(openupsB != null && openupsB.index >= a) {
                                openupsB.close();
                                openups[b] = null;
                            }
                        }
                    label_232:
                        b = i >>> 23;
                        c = i >> 14 & 0x1FF;
                        if((b <= 0xFF ? stack[b] : k[b & 0xFF]).eq_b((c <= 0xFF ? stack[c] : k[c & 0xFF])) != (a != 0)) {
                            ++pc;
                            goto label_448;
                        label_237:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            if((b <= 0xFF ? stack[b] : k[b & 0xFF]).lt_b((c <= 0xFF ? stack[c] : k[c & 0xFF])) != (a != 0)) {
                                ++pc;
                                goto label_448;
                            label_242:
                                b = i >>> 23;
                                c = i >> 14 & 0x1FF;
                                if((b <= 0xFF ? stack[b] : k[b & 0xFF]).lteq_b((c <= 0xFF ? stack[c] : k[c & 0xFF])) != (a != 0)) {
                                    ++pc;
                                    goto label_448;
                                label_247:
                                    if(stack[a].toboolean() != ((0x7FC000 & i) != 0)) {
                                        ++pc;
                                        goto label_448;
                                    label_250:
                                        LuaValue o = stack[i >>> 23];
                                        if(o.toboolean() == ((0x7FC000 & i) != 0)) {
                                            stack[a] = o;
                                        }
                                        else {
                                            ++pc;
                                        }
                                        goto label_448;
                                    label_256:
                                        this.setCaller(stack[a]);
                                        if(debuglib != null) {
                                            try {
                                                if(stack[a].isclosure()) {
                                                label_261:
                                                    notcl = false;
                                                }
                                                else {
                                                    notcl = true;
                                                }
                                                if(notcl) {
                                                    if(stack[a].isfunction()) {
                                                        debuglib.onCall(stack[a].checkfunction(), false);
                                                    }
                                                    else {
                                                        notcl = false;
                                                    }
                                                }
                                                goto label_267;
                                            }
                                            catch(NullPointerException e) {
                                                goto label_308;
                                            }
                                        }
                                        goto label_261;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            goto label_448;
        }
        catch(LuaError le) {
            goto label_451;
        }
        catch(StackOverflowError e) {
            goto label_519;
        }
        catch(InterruptedException e) {
            goto label_522;
        }
        catch(Throwable e) {
            goto label_525;
        }
        try {
        label_267:
            switch(i & 0xFFFFC000) {
                case 0x800000: {
                    v = this.checkRet(stack[a].invoke(NONE));
                    top = a + v.narg();
                    break;
                }
                case 0x804000: {
                    stack[a].call();
                    break;
                }
                case 0x808000: {
                    stack[a] = this.checkRet(stack[a].call());
                    break;
                }
                case 0x1000000: {
                    v = this.checkRet(stack[a].invoke(stack[a + 1]));
                    top = a + v.narg();
                    break;
                }
                case 0x1004000: {
                    stack[a].call(stack[a + 1]);
                    break;
                }
                case 0x1008000: {
                    stack[a] = this.checkRet(stack[a].call(stack[a + 1]));
                    break;
                }
                case 0x1804000: {
                    stack[a].call(stack[a + 1], stack[a + 2]);
                    break;
                }
                case 0x1808000: {
                    stack[a] = this.checkRet(stack[a].call(stack[a + 1], stack[a + 2]));
                    break;
                }
                case 0x2004000: {
                    stack[a].call(stack[a + 1], stack[a + 2], stack[a + 3]);
                    break;
                }
                case 0x2008000: {
                    stack[a] = this.checkRet(stack[a].call(stack[a + 1], stack[a + 2], stack[a + 3]));
                    break;
                }
                default: {
                    b = i >>> 23;
                    c = i >> 14 & 0x1FF;
                    v = this.checkRet(stack[a].invoke((b <= 0 ? LuaClosure.varargsOf(stack, a + 1, top - v.narg() - (a + 1), v) : LuaClosure.varargsOf(stack, a + 1, b - 1))));
                    if(c > 0) {
                        v.copyto(stack, a, c - 1);
                        v = NONE;
                    }
                    else {
                        top = a + v.narg();
                        v = v.dealias();
                    }
                }
            }
        }
        catch(Throwable throwable1) {
            try {
                if(notcl) {
                    debuglib.onReturn();
                }
                throw throwable1;
            }
            catch(NullPointerException e) {
                goto label_308;
            }
            catch(LuaError le) {
                goto label_451;
            }
            catch(StackOverflowError e) {
                goto label_519;
            }
            catch(InterruptedException e) {
                goto label_522;
            }
            catch(Throwable e) {
                goto label_525;
            }
        }
        if(notcl) {
            try {
                try {
                    debuglib.onReturn();
                    goto label_448;
                }
                catch(NullPointerException e) {
                label_308:
                    String callArgs = "";
                    switch(i & 0xFFFFC000) {
                        case 0x800000: {
                            callArgs = NONE.toString();
                            break;
                        }
                        case 0x804000: 
                        case 0x808000: {
                            break;
                        }
                        case 0x1000000: 
                        case 0x1004000: 
                        case 0x1008000: {
                            callArgs = stack[a + 1].toString();
                            break;
                        }
                        case 0x1804000: 
                        case 0x1808000: {
                            callArgs = stack[a + 1] + ", " + stack[a + 2];
                            break;
                        }
                        case 0x2004000: 
                        case 0x2008000: {
                            callArgs = stack[a + 1] + ", " + stack[a + 2] + ", " + stack[a + 3];
                            break;
                        }
                        default: {
                            callArgs = (b <= 0 ? LuaClosure.varargsOf(stack, a + 1, top - v.narg() - (a + 1), v) : LuaClosure.varargsOf(stack, a + 1, b - 1)).toString();
                        }
                    }
                    NullPointerException nullPointerException1 = new NullPointerException("Null from " + stack[a].getClass().getName() + " (" + callArgs + ")");
                    nullPointerException1.initCause(e);
                    throw nullPointerException1;
                }
            label_322:
                LuaValue limit = stack[a + 1];
                LuaValue step = stack[a + 2];
                LuaValue luaValue6 = stack[a].add(step);
                if(!step.gt_b(ZERO)) {
                    if(luaValue6.gteq_b(limit)) {
                    label_329:
                        stack[a] = luaValue6;
                        stack[a + 3] = luaValue6;
                        pc += (i >>> 14) - 0x1FFFF;
                        goto label_448;
                    label_333:
                        LuaNumber luaNumber1 = stack[a].checknumber("\'for\' initial value must be a number");
                        LuaNumber luaNumber2 = stack[a + 1].checknumber("\'for\' limit must be a number");
                        LuaNumber luaNumber3 = stack[a + 2].checknumber("\'for\' step must be a number");
                        stack[a] = luaNumber1.sub(luaNumber3);
                        stack[a + 1] = luaNumber2;
                        stack[a + 2] = luaNumber3;
                        pc += (i >>> 14) - 0x1FFFF;
                        goto label_448;
                    label_341:
                        this.setCaller(stack[a]);
                        try {
                            v = this.checkRet(stack[a].invoke(LuaClosure.varargsOf(stack[a + 1], stack[a + 2])));
                            c = i >> 14 & 0x1FF;
                        }
                        catch(NullPointerException e) {
                            NullPointerException nullPointerException3 = new NullPointerException("Null from " + stack[a].getClass().getName() + " (" + LuaClosure.varargsOf(stack[a + 1], stack[a + 2]) + ")");
                            nullPointerException3.initCause(e);
                            throw nullPointerException3;
                        }
                        while(true) {
                            --c;
                            if(c < 0) {
                                v = NONE;
                                goto label_448;
                            }
                            stack[a + 3 + c] = v.arg(c + 1);
                        }
                    label_355:
                        if(!stack[a + 1].isnil()) {
                            stack[a] = stack[a + 1];
                            pc += (i >>> 14) - 0x1FFFF;
                            goto label_448;
                        label_359:
                            c = i >> 14 & 0x1FF;
                            if(c == 0) {
                                ++pc;
                                c = code[pc];
                            }
                            int offset = (c - 1) * 50;
                            LuaValue o = stack[a];
                            b = i >>> 23;
                            if(b == 0) {
                                b = top - a - 1;
                                int m = b - v.narg();
                                int j;
                                for(j = 1; j <= m; ++j) {
                                    o.set(offset + j, stack[a + j]);
                                }
                                while(j <= b) {
                                    o.set(offset + j, v.arg(j - m));
                                    ++j;
                                }
                            }
                            else {
                                o.presize(offset + b);
                                for(int j = 1; j <= b; ++j) {
                                    o.set(offset + j, stack[a + j]);
                                }
                            }
                            goto label_448;
                        label_386:
                            Prototype newp = pp[i >>> 14];
                            LuaClosure luaClosure0 = new LuaClosure(newp, glob, false);
                            luaClosure0.sourceFile = sourceFile;
                            Upvaldesc[] uv = newp.upvalues;
                            UpValue[] upv = luaClosure0.upValues;
                            for(int j = 0; j < uv.length; ++j) {
                                Upvaldesc uvj = uv[j];
                                upv[j] = uvj.instack ? this.findupval(stack, uvj.idx, openups) : upValues[uvj.idx];
                            }
                            stack[a] = luaClosure0;
                            goto label_448;
                        label_402:
                            b = i >>> 23;
                            if(b == 0) {
                                v = this.varargs;
                                b = v.narg();
                                top = a + b;
                            }
                            else {
                                Varargs varargs = this.varargs;
                                for(int j = 1; j < b; ++j) {
                                    stack[a + j - 1] = varargs.arg(j);
                                }
                            }
                            goto label_448;
                        label_415:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).idiv((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_419:
                            stack[a] = stack[i >>> 23].bnot();
                            goto label_448;
                        label_421:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).band((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_425:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).bor((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_429:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).bxor((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_433:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).shl((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_437:
                            b = i >>> 23;
                            c = i >> 14 & 0x1FF;
                            stack[a] = (b <= 0xFF ? stack[b] : k[b & 0xFF]).shr((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_441:
                            if(i >>> 23 >= upValues.length) {
                                throw LuaClosure.upValueOutOfRange(2, i >>> 23, upValues.length);
                            }
                            c = i >> 14 & 0x1FF;
                        label_444:
                            stack[a] = upValues[i >>> 23].getValue().get((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                            goto label_448;
                        label_446:
                            c = i >> 14 & 0x1FF;
                        label_447:
                            stack[a] = stack[i >>> 23].get((c <= 0xFF ? stack[c] : k[c & 0xFF]));
                        }
                    }
                }
                else if(luaValue6.lteq_b(limit)) {
                    goto label_329;
                }
            label_448:
                ++pc;
                goto label_25;
            }
            catch(LuaError le) {
                goto label_451;
            }
            catch(StackOverflowError e) {
                goto label_519;
            }
            catch(InterruptedException e) {
                goto label_522;
            }
            catch(Throwable e) {
                goto label_525;
            }
        }
        goto label_448;
        try {
        label_451:
            if(le.traceback == null) {
                int reg = -1;
                int up = -1;
                switch(i & 0x3F) {
                    case 8: {
                        up = a;
                        break;
                    }
                    case 5: 
                    case 6: 
                    case 9: {
                        up = i >>> 23;
                        break;
                    }
                    case 22: {
                        int _c = i >> 14 & 0x1FF;
                        if(_c <= (i >>> 23) + 1) {
                            if(!stack[c - 1].isstring()) {
                                reg = c - 1;
                            }
                            else if(!stack[c].isstring()) {
                                reg = c;
                            }
                        }
                        else if(!stack[c].isstring()) {
                            reg = c;
                        }
                        else if(!stack[_c].isstring()) {
                            reg = _c;
                        }
                        break;
                    }
                    case 25: 
                    case 26: {
                        if(b <= 0xFF && !stack[b].isstring()) {
                            reg = b;
                        }
                        else if(c <= 0xFF && !stack[c].isstring()) {
                            reg = c;
                        }
                        break;
                    }
                    case 10: 
                    case 27: 
                    case 29: 
                    case 30: 
                    case 34: 
                    case 36: {
                        reg = a;
                        break;
                    }
                    case 13: 
                    case 14: 
                    case 15: 
                    case 16: 
                    case 17: 
                    case 18: 
                    case 40: {
                        if(b <= 0xFF && !stack[b].isnumber()) {
                            reg = b;
                        }
                        else if(c <= 0xFF && !stack[c].isnumber()) {
                            reg = c;
                        }
                        break;
                    }
                    case 7: 
                    case 12: 
                    case 19: 
                    case 20: 
                    case 21: 
                    case 28: 
                    case 41: {
                        reg = i >>> 23;
                        break;
                    }
                    case 42: 
                    case 43: 
                    case 44: 
                    case 45: 
                    case 46: {
                        if(b <= 0xFF && !stack[b].islongnumber()) {
                            reg = b;
                        }
                        else if(c <= 0xFF && !stack[c].islongnumber()) {
                            reg = c;
                        }
                    }
                }
                if(up >= 0) {
                    StringBuilder stringBuilder0 = new StringBuilder("upvalue \'");
                    LuaString luaString0 = up < this.p.upvalues.length ? this.p.upvalues[up].name : "?";
                    name = stringBuilder0.append(luaString0).append("\'").toString();
                }
                else {
                    name = DebugLib.getobjname(pc, reg, this.p);
                }
                if(name != null && name.startsWith("method ") && le instanceof LuaArgError) {
                    le = new LuaArgError(((LuaArgError)le));
                }
                LuaClosure.processErrorHooks(this.globals, le, this.p, pc, name);
            }
            if(le instanceof StackOverflow && LuaClosure.deep == 1) {
                throwable3 = le.getCause();
                if(throwable3 != null) {
                    Log.w(("Stack overflow 1: " + throwable3.getMessage()));
                    goto label_511;
                }
            }
            throw le;
        }
        catch(Throwable throwable2) {
            goto label_529;
        }
        try {
        label_511:
            Log.w("Stack overflow 2", throwable3);
            throw le;
        }
        catch(Throwable e2) {
            try {
                Log.w("Stack overflow 3", e2);
                throw le;
            }
            catch(Throwable unused_ex) {
                try {
                    Log.w(("Stack overflow 4: " + e2.getMessage()));
                    throw le;
                label_519:
                    String s3 = e.getMessage();
                    throw new StackOverflow((s3 == null ? "stack overflow" : "stack overflow (" + s3 + ")"), e);
                label_522:
                    Log.w("Interrupted closure", e);
                    throw new LuaError(e);
                label_525:
                    Internal luaError$Internal0 = new Internal(e);
                    LuaClosure.processErrorHooks(this.globals, luaError$Internal0, this.p, pc, null);
                    throw luaError$Internal0;
                }
                catch(Throwable throwable2) {
                }
            }
        }
    label_529:
        if(openups != null) {
            int u = stack.length;
            while(true) {
                --u;
                if(u < 0) {
                    break;
                }
                UpValue upValue = openups[u];
                if(upValue != null) {
                    upValue.close();
                }
            }
        }
        if(debuglib != null && !tailcall) {
            debuglib.onReturn();
        }
        this.varargs = LuaClosure.NONE;
        --LuaClosure.deep;
        throw throwable2;
    }

    private UpValue findupval(LuaValue[] stack, short idx, UpValue[] openups) {
        UpValue up;
        int free = -1;
        for(int i = 0; true; ++i) {
            if(i >= openups.length) {
                if(free >= 0) {
                    up = new UpValue(stack, ((int)idx));
                    openups[free] = up;
                    return up;
                }
                LuaClosure.error(("No space for upvalue " + openups.length));
                return null;
            }
            up = openups[i];
            if(up != null) {
                if(up.index == idx) {
                    return up;
                }
            }
            else if(free < 0) {
                free = i;
            }
        }
    }

    private LuaValue[] getNewStack() {
        int max = this.p.maxstacksize;
        LuaValue[] stack = new LuaValue[max];
        System.arraycopy(LuaClosure.NILS, 0, stack, 0, max);
        return stack;
    }

    public Varargs getVarargs() {
        return this.varargs;
    }

    @Override  // luaj.LuaValue
    public final Varargs invoke(Varargs varargs) {
        return this.onInvoke(varargs, false).eval();
    }

    @Override  // luaj.LuaValue
    public boolean isclosure() {
        return true;
    }

    @Override  // luaj.LuaFunction
    public static void main(String[] args) throws Throwable {
    }

    // 去混淆评级： 低(30)
    @Override  // luaj.LuaFunction
    public String name() {
        return "<null" + ':' + this.p.linedefined + '>';
    }

    @Override  // luaj.LuaValue
    public final Varargs onInvoke(Varargs varargs, boolean tailcall) {
        LuaValue[] arr_luaValue = this.getNewStack();
        if(this.p.maxstacksize < this.p.numparams) {
            throw new LuaError("Damaged script: .maxstacksize (" + this.p.maxstacksize + ") < .numparams (" + this.p.numparams + ")");
        }
        for(int i = 0; i < this.p.numparams; ++i) {
            arr_luaValue[i] = varargs.arg(i + 1);
        }
        return this.p.is_vararg == 0 ? this.execute(arr_luaValue, LuaClosure.NONE, tailcall) : this.execute(arr_luaValue, varargs.subargs(this.p.numparams + 1), tailcall);
    }

    @Override  // luaj.LuaValue
    public LuaClosure optclosure(LuaClosure defval) {
        return this;
    }

    private static void processErrorHooks(Globals globals, LuaError le, Prototype p, int pc, String name) {
        CallFrame frame = null;
        if(le.traceback != null) {
            return;
        }
        if(p != null) {
            if(globals != null && globals.debuglib != null) {
                frame = globals.debuglib.getCallFrame(le.level);
                if(frame != null) {
                    String src = frame.shortsource();
                    if(src == null) {
                        src = "?";
                    }
                    le.file = src;
                    le.line = frame.currentline();
                }
            }
            if(frame == null) {
                le.file = p.source == null ? "?" : p.source.tojstring();
                le.line = p.lineinfo == null || pc < 0 || pc >= p.lineinfo.length ? -1 : p.lineinfo[pc];
            }
            le.setFileLine();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(le.getMessage());
        if(name != null) {
            sb.append(" (");
            sb.append(name);
            sb.append(")");
        }
        sb.append("\nlevel = ");
        sb.append(le.level);
        if(p != null) {
            sb.append(", const = ");
            sb.append(p.k.length);
            sb.append(", proto = ");
            sb.append(p.p.length);
            sb.append(", upval = ");
            sb.append(p.upvalues.length);
            sb.append(", vars = ");
            sb.append(p.maxstacksize);
            sb.append(", code = ");
            sb.append(p.code.length);
            sb.append('\n');
            if(pc < 0 || pc >= p.code.length) {
                sb.append("PC ");
                sb.append(pc);
            }
            else {
                Print.printOpCode(sb, p, pc, null);
                sb.append('\n');
                Print.describe(sb, pc, p.code[pc]);
            }
        }
        LuaClosure.errorHook(globals, le, sb);
    }

    public static Varargs protectedCall(Globals globals, LuaFunction caller, LuaValue func, Varargs args) {
        Varargs varargs1;
        int old;
        if(globals != null && globals.debuglib != null) {
            globals.debuglib.onCall(caller, false);
        }
        try {
            old = LuaClosure.deep;
            LuaClosure.deep = 0;
            varargs1 = LuaClosure.varargsOf(LuaClosure.TRUE, func.invoke(args));
            LuaClosure.deep = old;
        }
        catch(Throwable e) {
            try {
                LuaError le = e instanceof LuaError ? ((LuaError)e) : new Internal(e);
                if(le.traceback == null) {
                    LuaClosure.processErrorHooks(globals, le, null, 0, null);
                }
                LuaValue m = le.getMessageObject();
                LuaBoolean luaBoolean0 = LuaClosure.FALSE;
                if(m == null) {
                    m = LuaClosure.NIL;
                }
                varargs1 = LuaClosure.varargsOf(luaBoolean0, m);
                LuaClosure.deep = old;
            }
            catch(Throwable throwable1) {
                LuaClosure.deep = old;
                if(globals != null && globals.debuglib != null) {
                    globals.debuglib.onReturn();
                }
                throw throwable1;
            }
            if(globals != null && globals.debuglib != null) {
                globals.debuglib.onReturn();
                return varargs1;
            }
            return varargs1;
        }
        if(globals != null && globals.debuglib != null) {
            globals.debuglib.onReturn();
        }
        return varargs1;
    }

    private void setCaller(LuaValue val) {
        if(val instanceof DebugFunction) {
            ((DebugFunction)val).setCaller(this);
        }
    }

    public void setVarargs(Varargs varargs) {
        this.varargs = varargs;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaFunction
    public String tojstring() {
        return "function: null:0-0";
    }

    private static LuaError upValueOutOfRange(int type, int index, int len) {
        return new LuaError("Damaged script " + type + ": an attempt to get an upvalue with index " + index + " when there are " + len + " in total.");
    }
}

