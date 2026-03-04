package luaj;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.ext.Alert;
import android.ext.BaseActivity.GoOnForum;
import android.ext.Log;
import android.ext.Re;
import android.ext.ThreadManager;
import android.ext.Tools;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadState {
    public static class BadHeader {
        String msg;

    }

    static final class GlobalsUndumper implements Undumper {
        private GlobalsUndumper() {
        }

        GlobalsUndumper(GlobalsUndumper loadState$GlobalsUndumper0) {
        }

        @Override  // luaj.Globals$Undumper
        public Prototype undump(InputStream stream, String chunkname) throws IOException {
            return LoadState.undump(stream, chunkname);
        }
    }

    public static final int LUAC_FORMAT = 0;
    public static final int LUAC_HEADERSIZE = 12;
    public static final byte[] LUAC_TAIL = null;
    public static final int LUAC_VERSION = 82;
    public static final byte[] LUA_SIGNATURE = null;
    public static final int LUA_TBOOLEAN = 1;
    public static final int LUA_TFUNCTION = 6;
    public static final int LUA_TINT = -2;
    public static final int LUA_TNIL = 0;
    public static final int LUA_TNONE = -1;
    public static final int LUA_TNUMBER = 3;
    public static final int LUA_TSTRING = 4;
    public static final int LUA_TTABLE = 5;
    public static final int LUA_TTHREAD = 8;
    public static final int LUA_TUSERDATA = 7;
    public static final int LUA_TVALUE = 9;
    private static final int[] NOINTS = null;
    private static final LocVars[] NOLOCVARS = null;
    private static final Prototype[] NOPROTOS = null;
    private static final Upvaldesc[] NOUPVALDESCS = null;
    private static final LuaValue[] NOVALUES = null;
    public static final int NUMBER_FORMAT_FLOATS_OR_DOUBLES = 0;
    public static final int NUMBER_FORMAT_INTS_ONLY = 1;
    public static final int NUMBER_FORMAT_NUM_PATCH_INT32 = 4;
    public static final String SOURCE_BINARY_STRING = "=?";
    public static volatile boolean badHeader;
    private byte[] buf;
    public static String encoding;
    public static final Undumper instance;
    public final DataInputStream is;
    private int luacFormat;
    private boolean luacLittleEndian;
    private int luacNumberFormat;
    private int luacSizeofInstruction;
    private int luacSizeofInt;
    private int luacSizeofLuaNumber;
    private int luacSizeofSizeT;
    private int luacVersion;
    String name;

    static {
        LoadState.instance = (InputStream stream, String chunkname) -> {
            String sname = chunkname == null ? null : LoadState.getSourceName(chunkname);
            LoadState s = new LoadState(stream, sname);
            s.loadHeader();
            if(chunkname == null) {
                return null;
            }
            if(s.luacNumberFormat != 0 && s.luacNumberFormat != 1 && s.luacNumberFormat != 4) {
                throw new LuaError("unsupported int size: " + s.luacNumberFormat);
            }
            return LoadState.fillSource(s.loadFunction(), LuaString.valueOf(sname));
        };
        LoadState.encoding = null;
        LoadState.LUA_SIGNATURE = new byte[]{27, 76, 0x75, 97};
        LoadState.LUAC_TAIL = new byte[]{25, -109, 13, 10, 26, 10};
        LoadState.NOVALUES = new LuaValue[0];
        LoadState.NOPROTOS = new Prototype[0];
        LoadState.NOLOCVARS = new LocVars[0];
        LoadState.NOUPVALDESCS = new Upvaldesc[0];
        LoadState.NOINTS = new int[0];
        LoadState.badHeader = false;
    }

    private LoadState(InputStream stream, String name) {
        this.buf = new byte[0x200];
        this.name = name;
        this.is = new DataInputStream(stream);
    }

    private static Prototype fillSource(Prototype p, LuaString def) {
        if(p.source == null) {
            p.source = def;
        }
        for(int i = 0; i < p.p.length; ++i) {
            LoadState.fillSource(p.p[i], p.source);
        }
        return p;
    }

    public static String getSourceName(String name) {
        if(name.startsWith("@") || name.startsWith("=")) {
            return name.substring(1);
        }
        return name.startsWith("\u001B") ? "=?" : name;
    }

    public static void install(Globals globals) {
        globals.undumper = LoadState.instance;
    }

    void loadConstants(Prototype f) throws IOException {
        int v = this.loadInt();
        LuaValue[] values = v <= 0 ? LoadState.NOVALUES : new LuaValue[v];
        for(int i = 0; true; ++i) {
            if(i >= v) {
                f.k = values;
                int n = this.loadInt();
                Prototype[] protos = n <= 0 ? LoadState.NOPROTOS : new Prototype[n];
                for(int i = 0; i < n; ++i) {
                    protos[i] = this.loadFunction();
                }
                f.p = protos;
                return;
            }
            switch(this.is.readByte()) {
                case -2: {
                    values[i] = LuaLong.valueOf(this.loadInt());
                    break;
                }
                case 0: {
                    values[i] = LuaValue.NIL;
                    break;
                }
                case 1: {
                    values[i] = this.is.readUnsignedByte() == 0 ? LuaValue.FALSE : LuaValue.TRUE;
                    break;
                }
                case 2: {
                    values[i] = LuaBigNumber.bignumberOf(this.loadString().checkjstring());
                    break;
                }
                case 3: {
                    values[i] = this.loadNumber();
                    break;
                }
                case 4: {
                    values[i] = this.loadString();
                    break;
                }
                default: {
                    throw new IllegalStateException("bad constant");
                }
            }
        }
    }

    void loadDebug(Prototype f) throws IOException {
        LuaString luaString0 = this.loadString();
        if(luaString0 != null) {
            f.source = luaString0;
        }
        f.lineinfo = this.loadIntArray();
        int v = this.loadInt();
        f.locvars = v <= 0 ? LoadState.NOLOCVARS : new LocVars[v];
        for(int i = 0; i < v; ++i) {
            LuaString luaString1 = this.loadString();
            int v2 = this.loadInt();
            int v3 = this.loadInt();
            LocVars[] arr_locVars = f.locvars;
            arr_locVars[i] = new LocVars(luaString1, v2, v3);
        }
        int n = this.loadInt();
        for(int i = 0; i < n; ++i) {
            Upvaldesc upvaldesc0 = f.upvalues[i];
            upvaldesc0.name = this.loadString();
        }
    }

    public Prototype loadFunction() throws IOException {
        Prototype f = new Prototype();
        f.linedefined = this.loadInt();
        f.lastlinedefined = this.loadInt();
        f.numparams = this.is.readUnsignedByte();
        f.is_vararg = this.is.readUnsignedByte();
        f.maxstacksize = this.is.readUnsignedByte();
        if(f.maxstacksize < f.numparams) {
            throw new LuaError("Damaged script 2: .maxstacksize (" + f.maxstacksize + ") < .numparams (" + f.numparams + ")");
        }
        f.code = this.loadIntArray();
        this.loadConstants(f);
        this.loadUpvalues(f);
        this.loadDebug(f);
        return f;
    }

    public void loadHeader() throws IOException {
        this.luacVersion = this.is.readByte() & 0xFF;
        this.luacFormat = this.is.readByte() & 0xFF;
        int v = this.is.readByte();
        this.luacLittleEndian = v != 0;
        this.luacSizeofInt = this.is.readByte() & 0xFF;
        this.luacSizeofSizeT = this.is.readByte() & 0xFF;
        this.luacSizeofInstruction = this.is.readByte() & 0xFF;
        this.luacSizeofLuaNumber = this.is.readByte() & 0xFF;
        this.luacNumberFormat = this.is.readByte() & 0xFF;
        String error = this.luacVersion == 82 ? "" : "" + Re.s(0x7F070333) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'52\'", "\'" + Integer.toHexString(this.luacVersion) + "\'"}) + "\n";  // string:unknown_version "Unknown version."
        if(this.luacFormat != 0) {
            error = error + Re.s(0x7F070334) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'0\'", "\'" + this.luacFormat + "\'"}) + "\n";  // string:unknown_format "Unknown format."
        }
        if(v != 0 && v != 1) {
            error = error + Re.s(0x7F070335) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'0\' " + Re.s(0x7F07033A) + " \'1\'", "\'" + (v & 0xFF) + "\'"}) + "\n";  // string:unknown_endianness "Unknown endianness."
        }
        if(this.luacSizeofInt != 4) {
            error = error + Tools.stringFormat(Re.s(0x7F070336), new Object[]{"int"}) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'4\'", "\'" + this.luacSizeofInt + "\'"}) + "\n";  // string:unknown_size_s "Unknown size of __s__."
        }
        if(this.luacSizeofSizeT != 4) {
            error = error + Tools.stringFormat(Re.s(0x7F070336), new Object[]{"size_t"}) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'4\'", "\'" + this.luacSizeofSizeT + "\'"}) + "\n";  // string:unknown_size_s "Unknown size of __s__."
        }
        if(this.luacSizeofInstruction != 4) {
            error = error + Re.s(0x7F070337) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'4\'", "\'" + this.luacSizeofInstruction + "\'"}) + "\n";  // string:unknown_size_instr "Unknown size of instruction."
        }
        if(this.luacSizeofLuaNumber != 4 && this.luacSizeofLuaNumber != 8) {
            error = error + Re.s(0x7F070338) + " " + Tools.stringFormat(Re.s(0x7F070339), new Object[]{"\'4\' " + Re.s(0x7F07033A) + " \'8\'", "\'" + this.luacSizeofLuaNumber + "\'"}) + "\n";  // string:unknown_size_num "Unknown size of Lua number."
        }
        if(error.length() > 0) {
            if(LoadState.badHeader || ThreadManager.isInUiThread()) {
                String error = "-------------------\n" + Re.s(0x7F070332).trim() + "\n\n" + error + "\n" + Re.s(0x7F07033C).trim() + "\n-------------------\n";  // string:invalid_header_1 "Invalid binary script header."
                Tools.showToast(error.trim(), 1);
                Print.ps.println(error);
            }
            else {
                LoadState.badHeader = true;
                BadHeader monitor = new BadHeader();
                monitor.msg = error.trim();
                synchronized(monitor) {
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog$Builder0 = Alert.create(Tools.getContext()).setMessage(LoadState.this.name + "\n\n" + Tools.stringFormat(Re.s(0x7F07007E), new Object[]{monitor.msg})).setNeutralButton(Re.s(0x7F070164), new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2wrslf2594440elqdu|0vfulswv0zlwk0fruuxswhg0ru0lqydolg0khdghuv2"));  // string:invalid_header "__invalid_header_1__\n\n__s__\n\n__invalid_header_3__\n\n__invalid_header_2__"
                            luaj.LoadState.1.1 loadState$1$10 = new DialogInterface.OnClickListener() {
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    this.val$monitor.msg = null;
                                }
                            };
                            if(Tools.RANDOM.nextBoolean()) {
                                alertDialog$Builder0.setPositiveButton(Re.s(0x7F07009B), loadState$1$10).setNegativeButton(Re.s(0x7F07009C), null);  // string:yes "Yes"
                            }
                            else {
                                alertDialog$Builder0.setNegativeButton(Re.s(0x7F07009B), loadState$1$10).setPositiveButton(Re.s(0x7F07009C), null);  // string:yes "Yes"
                            }
                            AlertDialog alertDialog0 = alertDialog$Builder0.create();
                            Alert.setOnDismissListener(alertDialog0, new DialogInterface.OnDismissListener() {
                                @Override  // android.content.DialogInterface$OnDismissListener
                                public void onDismiss(DialogInterface dialog) {
                                    synchronized(this.val$monitor) {
                                        this.val$monitor.notify();
                                    }
                                }
                            });
                            Alert.show(alertDialog0);
                        }
                    });
                    try {
                        monitor.wait();
                    }
                    catch(InterruptedException e) {
                        Log.w("Interrupted wait", e);
                        Thread.currentThread().interrupt();
                        throw new LuaError(e);
                    }
                    if(monitor.msg != null) {
                        throw new LuaError(Re.s(0x7F070332).trim() + "\n\n" + error + "\n" + Re.s(0x7F07033C).trim());  // string:invalid_header_1 "Invalid binary script header."
                    }
                }
            }
        }
        for(int i = 0; true; ++i) {
            if(i >= LoadState.LUAC_TAIL.length) {
                return;
            }
            int b = this.is.readByte();
            if(b != LoadState.LUAC_TAIL[i]) {
                throw new LuaError("Unexpected byte in luac tail of header, index = " + i + " (" + (b & 0xFF) + ", not " + (LoadState.LUAC_TAIL[i] & 0xFF) + ")");
            }
        }
    }

    int loadInt() throws IOException {
        this.is.readFully(this.buf, 0, 4);
        return this.luacLittleEndian ? this.buf[3] << 24 | (this.buf[2] & 0xFF) << 16 | (this.buf[1] & 0xFF) << 8 | this.buf[0] & 0xFF : this.buf[0] << 24 | (this.buf[1] & 0xFF) << 16 | (this.buf[2] & 0xFF) << 8 | this.buf[3] & 0xFF;
    }

    long loadInt64() throws IOException {
        if(this.luacLittleEndian) {
            int v = this.loadInt();
            return ((long)this.loadInt()) << 0x20 | ((long)v) & 0xFFFFFFFFL;
        }
        return ((long)this.loadInt()) << 0x20 | ((long)this.loadInt()) & 0xFFFFFFFFL;
    }

    int[] loadIntArray() throws IOException {
        int v = this.loadInt();
        if(v == 0) {
            return LoadState.NOINTS;
        }
        if(this.buf.length < v << 2) {
            this.buf = new byte[v << 2];
        }
        this.is.readFully(this.buf, 0, v << 2);
        int[] array = new int[v];
        int i = 0;
        for(int j = 0; i < v; j += 4) {
            array[i] = this.luacLittleEndian ? this.buf[j + 3] << 24 | (this.buf[j + 2] & 0xFF) << 16 | (this.buf[j + 1] & 0xFF) << 8 | this.buf[j] & 0xFF : this.buf[j] << 24 | (this.buf[j + 1] & 0xFF) << 16 | (this.buf[j + 2] & 0xFF) << 8 | this.buf[j + 3] & 0xFF;
            ++i;
        }
        return array;
    }

    LuaValue loadNumber() throws IOException {
        return this.luacNumberFormat == 1 ? LuaLong.valueOf(this.loadInt()) : LoadState.longBitsToLuaNumber(this.loadInt64());
    }

    LuaString loadString() throws IOException {
        int size = this.luacSizeofSizeT == 8 ? ((int)this.loadInt64()) : this.loadInt();
        if(size == 0) {
            return null;
        }
        byte[] bytes = new byte[size];
        this.is.readFully(bytes, 0, size);
        return LuaString.valueUsing(bytes, 0, bytes.length - 1);
    }

    void loadUpvalues(Prototype f) throws IOException {
        int v = this.loadInt();
        f.upvalues = v <= 0 ? LoadState.NOUPVALDESCS : new Upvaldesc[v];
        for(int i = 0; i < v; ++i) {
            boolean instack = this.is.readByte() != 0;
            int v2 = this.is.readByte();
            Upvaldesc[] arr_upvaldesc = f.upvalues;
            arr_upvaldesc[i] = new Upvaldesc(null, instack, v2 & 0xFF);
        }
    }

    public static LuaValue longBitsToLuaNumber(long bits) {
        if((0x7FFFFFFFFFFFFFFFL & bits) == 0L) {
            return LuaValue.ZERO;
        }
        int e = ((int)(bits >> 52 & 0x7FFL)) - 0x3FF;
        return e >= 0 && e < 0x1F && (bits & 0xFFFFFFFFFFFFFL & (1L << 52 - e) - 1L) == 0L ? LuaLong.valueOf((bits >> 0x3F == 0L ? ((int)((bits & 0xFFFFFFFFFFFFFL) >> 52 - e)) | 1 << e : -(((int)((bits & 0xFFFFFFFFFFFFFL) >> 52 - e)) | 1 << e))) : LuaValue.valueOf(Double.longBitsToDouble(bits));
    }

    // 检测为 Lambda 实现
    public static Prototype undump(InputStream stream, String chunkname) throws IOException [...]
}

