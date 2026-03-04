package luaj;

import Thousand_Dust.ScriptLib;
import android.content.Context;
import android.ext.Tools;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.nio.channels.FileChannel;
import lasm.Lasm;
import lasm.ParseException;
import luaj.compiler.LuaC;
import luaj.lib.BaseLib.CountingBufferedInputStream;
import luaj.lib.BaseLib;
import luaj.lib.Bit32Lib;
import luaj.lib.CoroutineLib;
import luaj.lib.DebugLib;
import luaj.lib.DexLib;
import luaj.lib.FileLib;
import luaj.lib.HTTPLib;
import luaj.lib.IoLib;
import luaj.lib.MathLib;
import luaj.lib.ModLib;
import luaj.lib.OsLib;
import luaj.lib.PackageLib;
import luaj.lib.ResourceFinder;
import luaj.lib.StackLib;
import luaj.lib.StringLib;
import luaj.lib.TableLib;
import luaj.lib.ToastLib;
import luaj.lib.ToolsLib;
import luaj.lib.Utf8Lib;
import luaj.lib.jse.LuajavaLib;

public class Globals extends LuaTable {
    public interface Compiler {
        Prototype compile(InputStream arg1, String arg2) throws IOException;
    }

    public interface Loader {
        LuaFunction load(Prototype arg1, String arg2, LuaValue arg3) throws IOException;
    }

    static class PbInputStream extends PushbackInputStream {
        static final int SIZE;
        boolean skipBOM;

        static {
            PbInputStream.SIZE = 65;
        }

        private PbInputStream(InputStream in) {
            super(in, PbInputStream.SIZE);
            this.skipBOM = false;
        }

        @Override
        public int read() throws IOException {
            int r1;
            byte[] buf = this.buf;
            if(buf == null) {
                throw new IOException();
            }
            if(this.pos < PbInputStream.SIZE) {
                int v = this.pos;
                this.pos = v + 1;
                r1 = buf[v] & 0xFF;
            }
            else {
                r1 = this.in.read();
            }
            if(r1 != 0xEF || !this.skipBOM) {
                return r1;
            }
            int v2 = super.read();
            if(v2 != 0xBB) {
                this.unread(v2);
                return r1;
            }
            int v3 = super.read();
            if(v3 != 0xBF) {
                this.unread(v3);
                this.unread(0xBB);
                return r1;
            }
            return this.read();
        }

        public PbInputStream setSkipBOM(boolean skipBOM) {
            this.skipBOM = skipBOM;
            return this;
        }

        static PbInputStream wrap(InputStream is) {
            if(!(is instanceof PbInputStream)) {
                is = new PbInputStream(is);
            }
            return (PbInputStream)is;
        }
    }

    public interface Undumper {
        Prototype undump(InputStream arg1, String arg2) throws IOException;
    }

    private static final int MAX_OPEN_FILES = 0x400;
    private static final long MAX_WRITE_BYTES = 0x40000000L;
    public PrintStream STDERR;
    public InputStream STDIN;
    public PrintStream STDOUT;
    public BaseLib baselib;
    public Compiler compiler;
    public DebugLib debuglib;
    public ResourceFinder finder;
    public boolean fullVersion;
    private String lastFile;
    public Loader loader;
    public int logMillis;
    public int openFiles;
    public PackageLib package_;
    public LuaThread running;
    public Undumper undumper;
    private String workDir;
    public long writeBytes;

    public Globals() {
        this.fullVersion = true;
        this.openFiles = 0;
        this.lastFile = null;
        this.writeBytes = 0L;
        this.logMillis = 0;
        this.STDIN = null;
        this.STDOUT = System.out;
        this.STDERR = System.err;
        this.running = new LuaThread(this);
        this.workDir = null;
    }

    public void addOpenFiles(String filename) {
        if(filename == null) {
        label_3:
            ++this.openFiles;
            if(this.openFiles >= 0x400) {
                throw new LuaError("Too many open files: " + this.openFiles + "; last: " + filename);
            }
        }
        else if(!filename.equals(this.lastFile)) {
            this.lastFile = filename;
            goto label_3;
        }
    }

    public void addWriteBytes(long writeBytes) {
        this.writeBytes += writeBytes;
        if(this.writeBytes >= 0x40000000L) {
            Context context0 = Tools.getContext();
            throw new LuaError("Too many write to files: " + Tools.formatFileSize(context0, this.writeBytes) + "; last: " + Tools.formatFileSize(context0, writeBytes));
        }
    }

    @Override  // luaj.LuaValue
    public Globals checkglobals() {
        return this;
    }

    public Prototype compilePrototype(InputStream stream, String chunkname) throws IOException {
        if(this.compiler == null) {
            Globals.error("No compiler.");
        }
        return this.compiler.compile(PbInputStream.wrap(stream).setSkipBOM(true), chunkname);
    }

    public static Globals initTest() {
        Globals globals = new Globals();
        LoadState.install(globals);
        LuaC.install(globals);
        globals.load(new BaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new Utf8Lib());
        globals.load(new MathLib());
        globals.load(new IoLib());
        globals.load(new OsLib());
        globals.load(new DebugLib());
        globals.load(new LuajavaLib());
        globals.load(new CoroutineLib());
        globals.load(new FileLib());
        globals.load(new HTTPLib());
        globals.load(new DexLib());
        globals.load(new ToolsLib());
        globals.load(new ScriptLib(globals));
        globals.load(new StackLib());
        globals.load(new ModLib());
        globals.load(new ToastLib());
        return globals;
    }

    public LuaValue load(InputStream is, String chunkname, String mode, LuaValue environment) {
        LuaValue luaValue1;
        try {
            try {
                Prototype prototype0 = this.loadPrototype(is, chunkname, mode);
                luaValue1 = this.loader.load(prototype0, chunkname, environment);
                goto label_18;
            }
            catch(LuaError l) {
            }
            catch(EOFException e) {
                goto label_6;
            }
            catch(Exception e) {
                goto label_11;
            }
            throw l;
        label_6:
            luaValue1 = Globals.error("truncated precompiled chunk", e);
        }
        catch(Throwable throwable0) {
            goto label_16;
        }
        try {
            is.close();
        }
        catch(IOException unused_ex) {
        }
        return luaValue1;
        try {
        label_11:
            luaValue1 = Globals.error(("load " + chunkname + ": " + e), e);
        }
        catch(Throwable throwable0) {
            goto label_16;
        }
        try {
            is.close();
        }
        catch(IOException unused_ex) {
        }
        return luaValue1;
        try {
        label_16:
            is.close();
        }
        catch(IOException unused_ex) {
        }
        throw throwable0;
        try {
        label_18:
            is.close();
        }
        catch(IOException unused_ex) {
        }
        return luaValue1;
    }

    public Prototype loadPrototype(InputStream is, String chunkname, String mode) throws IOException {
        if(mode.indexOf(98) >= 0) {
            if(this.undumper == null) {
                Globals.error("No undumper.");
            }
            byte[] sig = new byte[4];
            int v = is.read(sig);
            if(sig[0] == LoadState.LUA_SIGNATURE[0] && sig[1] == LoadState.LUA_SIGNATURE[1] && sig[2] == LoadState.LUA_SIGNATURE[2] && sig[3] == LoadState.LUA_SIGNATURE[3]) {
                Prototype prototype0 = this.undumper.undump(is, chunkname);
                if(is instanceof CountingBufferedInputStream) {
                    InputStream inputStream1 = ((CountingBufferedInputStream)is).getSource();
                    if(inputStream1 instanceof FileInputStream) {
                        long v1 = ((CountingBufferedInputStream)is).getCount();
                        if(v1 < ((FileInputStream)inputStream1).getChannel().size()) {
                            prototype0.tailPos = (int)v1;
                            return prototype0;
                        }
                    }
                }
                else if(is instanceof FileInputStream) {
                    FileChannel fileChannel0 = ((FileInputStream)is).getChannel();
                    long v2 = fileChannel0.position();
                    if(v2 < fileChannel0.size()) {
                        prototype0.tailPos = (int)v2;
                        return prototype0;
                    }
                }
                return prototype0;
            }
            if(v > 0) {
                is = PbInputStream.wrap(is);
                ((PbInputStream)is).unread(sig, 0, v);
            }
        }
        if(mode.indexOf(0x74) < 0) {
            throw new LuaError("attempt to load a text chunk (mode is \'" + mode + "\'");
        }
        byte[] header = new byte[61];
        int v3 = is.read(header);
        if(v3 > 0) {
            is = PbInputStream.wrap(is);
            ((PbInputStream)is).unread(header, 0, v3);
        }
        if(new String(header).equals("; --[=========[ Lua assembler file generated by GameGuardian ")) {
            try {
                return new Lasm(is).assemble();
            }
            catch(ParseException e) {
                throw new LuaError("Failed assemble: " + e.getMessage(), e);
            }
        }
        if(header[0] == LoadState.LUA_SIGNATURE[0] && header[1] == LoadState.LUA_SIGNATURE[1] && header[2] == LoadState.LUA_SIGNATURE[2] && header[3] == LoadState.LUA_SIGNATURE[3]) {
            throw new LuaError("attempt to load a binary chunk (mode is \'" + mode + "\')");
        }
        return this.compilePrototype(is, chunkname);
    }

    public LuaValue loadfile(String filename) {
        try {
            InputStream inputStream0 = this.finder.findResource(filename);
            String s1 = this.newFile(filename).getAbsolutePath();
            LuaValue luaValue0 = this.load(inputStream0, "@" + s1, "bt", this);
            if(luaValue0 instanceof LuaClosure) {
                ((LuaClosure)luaValue0).sourceFile = s1;
            }
            return luaValue0;
        }
        catch(Exception e) {
            return Globals.error(("load " + filename + ": " + e), e);
        }
    }

    public File newFile(String filename) {
        String dir = this.workDir;
        return dir == null || filename.length() != 0 && filename.charAt(0) == 0x2F ? new File(filename) : new File(dir, filename);
    }

    public void setWorkDir(String dir) {
        this.workDir = dir;
    }

    public Varargs yield(Varargs varargs0) {
        if(this.running == null || this.running.isMainThread()) {
            throw new LuaError("cannot yield main thread");
        }
        return this.running.state.lua_yield(varargs0);
    }
}

