package luaj.compiler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import luaj.LoadState;
import luaj.LocVars;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Print;
import luaj.Prototype;
import luaj.Upvaldesc;

public class DumpState {
    public static final boolean ALLOW_INTEGER_CASTING = false;
    private boolean IS_LITTLE_ENDIAN;
    private int NUMBER_FORMAT;
    public static final int NUMBER_FORMAT_DEFAULT = 0;
    public static final int NUMBER_FORMAT_FLOATS_OR_DOUBLES = 0;
    public static final int NUMBER_FORMAT_INTS_ONLY = 1;
    public static final int NUMBER_FORMAT_NUM_PATCH_INT32 = 4;
    public static final int SIZEOF_INSTRUCTION = 4;
    public static final int SIZEOF_INT = 4;
    private int SIZEOF_LUA_NUMBER;
    public static final int SIZEOF_SIZET = 4;
    boolean fix;
    int status;
    boolean strip;
    DataOutputStream writer;

    public DumpState(OutputStream w, boolean strip, boolean fix) {
        this.IS_LITTLE_ENDIAN = true;
        this.NUMBER_FORMAT = 0;
        this.SIZEOF_LUA_NUMBER = 8;
        this.writer = new DataOutputStream(w);
        this.strip = strip;
        this.fix = fix;
        this.status = 0;
    }

    public static int dump(Prototype f, OutputStream w, boolean stripDebug, int numberFormat, boolean littleendian) throws IOException {
        if(numberFormat != 0 && numberFormat != 1 && numberFormat != 4) {
            throw new IllegalArgumentException("number format not supported: " + numberFormat);
        }
        DumpState dumpState0 = new DumpState(w, stripDebug, false);
        dumpState0.IS_LITTLE_ENDIAN = littleendian;
        dumpState0.NUMBER_FORMAT = numberFormat;
        dumpState0.SIZEOF_LUA_NUMBER = numberFormat == 1 ? 4 : 8;
        dumpState0.dumpHeader();
        dumpState0.dumpFunction(f, null);
        return dumpState0.status;
    }

    public static int dump(Prototype f, OutputStream w, boolean strip, boolean fix) throws IOException {
        DumpState D = new DumpState(w, strip, fix);
        D.dumpHeader();
        D.dumpFunction(f, null);
        return D.status;
    }

    void dumpBlock(byte[] b, int size) throws IOException {
        this.writer.write(b, 0, size);
    }

    void dumpChar(int b) throws IOException {
        this.writer.write(b);
    }

    void dumpCode(Prototype f) throws IOException {
        int[] code = f.code;
        this.dumpInt(code.length);
        int[] fixed = this.fix ? new int[code.length] : code;
        if(this.fix) {
            Print.fix(f, fixed);
        }
        for(int pc = 0; pc < code.length; ++pc) {
            this.dumpInt(fixed[pc]);
        }
    }

    void dumpConstants(Prototype prototype0) throws IOException {
        LuaValue[] arr_luaValue = prototype0.k;
        this.dumpInt(arr_luaValue.length);
        for(int v = 0; v < arr_luaValue.length; ++v) {
            LuaValue luaValue0 = arr_luaValue[v];
            switch(luaValue0.type()) {
                case 0: {
                    this.writer.write(0);
                    break;
                }
                case 1: {
                    this.writer.write(1);
                    this.dumpChar(((int)luaValue0.toboolean()));
                    break;
                }
                case 3: {
                    switch(this.NUMBER_FORMAT) {
                        case 0: {
                            if(!luaValue0.isbignumber() || luaValue0.islongnumber()) {
                                this.writer.write(3);
                                this.dumpDouble(luaValue0.todouble());
                            }
                            else {
                                this.writer.write(2);
                                this.dumpString(luaValue0.tostring().checkstring());
                            }
                            break;
                        }
                        case 1: {
                            if(!DumpState.ALLOW_INTEGER_CASTING && !luaValue0.isint()) {
                                throw new IllegalArgumentException("not an integer: " + luaValue0);
                            }
                            this.writer.write(3);
                            this.dumpInt(luaValue0.toint());
                            break;
                        }
                        case 4: {
                            if(luaValue0.isint()) {
                                this.writer.write(-2);
                                this.dumpInt(luaValue0.toint());
                            }
                            else {
                                this.writer.write(3);
                                this.dumpDouble(luaValue0.todouble());
                            }
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("number format not supported: " + this.NUMBER_FORMAT);
                        }
                    }
                    break;
                }
                case 4: {
                    this.writer.write(4);
                    this.dumpString(((LuaString)luaValue0));
                    break;
                }
                default: {
                    throw new IllegalArgumentException("bad type for " + luaValue0);
                }
            }
        }
        int v1 = prototype0.p.length;
        this.dumpInt(v1);
        for(int v2 = 0; v2 < v1; ++v2) {
            this.dumpFunction(prototype0.p[v2], prototype0.source);
        }
    }

    void dumpDebug(Prototype f, LuaString psource) throws IOException {
        if(!this.strip && (psource == null || !psource.eq_b(f.source))) {
            this.dumpString(f.source);
        }
        else {
            this.dumpInt(0);
        }
        int[] lineinfo = f.lineinfo;
        int n = this.strip ? 0 : lineinfo.length;
        this.dumpInt(n);
        for(int i = 0; i < n; ++i) {
            this.dumpInt(lineinfo[i]);
        }
        LocVars[] locvars = f.locvars;
        int n = this.strip ? 0 : locvars.length;
        this.dumpInt(n);
        for(int i = 0; i < n; ++i) {
            LocVars lvi = locvars[i];
            this.dumpString(lvi.varname);
            this.dumpInt(lvi.startpc);
            this.dumpInt(lvi.endpc);
        }
        Upvaldesc[] upvalues = f.upvalues;
        int n = this.strip ? 0 : upvalues.length;
        this.dumpInt(n);
        for(int i = 0; i < n; ++i) {
            this.dumpString(upvalues[i].name);
        }
    }

    void dumpDouble(double d) throws IOException {
        this.writer.writeLong((this.IS_LITTLE_ENDIAN ? Long.reverseBytes(Double.doubleToLongBits(d)) : Double.doubleToLongBits(d)));
    }

    void dumpFunction(Prototype f, LuaString psource) throws IOException {
        this.dumpInt(f.linedefined);
        this.dumpInt(f.lastlinedefined);
        this.dumpChar(f.numparams);
        this.dumpChar(f.is_vararg);
        this.dumpChar(f.maxstacksize);
        this.dumpCode(f);
        this.dumpConstants(f);
        this.dumpUpvalues(f);
        this.dumpDebug(f, psource);
    }

    void dumpHeader() throws IOException {
        int v = 0;
        DataOutputStream writer = this.writer;
        writer.write(LoadState.LUA_SIGNATURE);
        writer.write(82);
        writer.write(0);
        if(this.IS_LITTLE_ENDIAN) {
            v = 1;
        }
        writer.write(v);
        writer.write(4);
        writer.write(4);
        writer.write(4);
        writer.write(this.SIZEOF_LUA_NUMBER);
        writer.write(this.NUMBER_FORMAT);
        writer.write(LoadState.LUAC_TAIL);
    }

    void dumpInt(int x) throws IOException {
        DataOutputStream writer = this.writer;
        if(this.IS_LITTLE_ENDIAN) {
            x = Integer.reverseBytes(x);
        }
        writer.writeInt(x);
    }

    void dumpString(LuaString s) throws IOException {
        if(s == null) {
            s = LuaValue.valueOf("");
        }
        int v = s.len().toint();
        this.dumpInt(v + 1);
        s.write(this.writer, 0, v);
        this.writer.write(0);
    }

    void dumpUpvalues(Prototype f) throws IOException {
        DataOutputStream writer = this.writer;
        Upvaldesc[] upvalues = f.upvalues;
        this.dumpInt(upvalues.length);
        for(int i = 0; i < upvalues.length; ++i) {
            Upvaldesc u = upvalues[i];
            writer.writeByte((u.instack ? 1 : 0));
            writer.writeByte(((int)u.idx));
        }
    }
}

