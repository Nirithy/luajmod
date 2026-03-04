package android.ext;

import Thousand_Dust.ScriptLib;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.fix.LayoutInflater;
import android.internal.LoggingPrintStream;
import android.net.Uri;
import android.os.Build.VERSION;
import android.sup.ArrayListResults;
import android.sup.LongSparseArrayChecked;
import android.text.Html;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import jeb.synthetic.FIN;
import lasm.Lasm;
import lasm.LasmPrototype;
import luaj.ExeCommand;
import luaj.Globals;
import luaj.LoadState;
import luaj.LuaBoolean;
import luaj.LuaClosure;
import luaj.LuaDouble;
import luaj.LuaError.Internal;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.LuaLong;
import luaj.LuaNil;
import luaj.LuaNumber;
import luaj.LuaString;
import luaj.LuaTable.Iterator;
import luaj.LuaTable;
import luaj.LuaThread;
import luaj.LuaValue;
import luaj.Print;
import luaj.Varargs;
import luaj.compiler.DumpState;
import luaj.compiler.LuaC;
import luaj.lib.BaseLib;
import luaj.lib.Bit32Lib;
import luaj.lib.CoroutineLib;
import luaj.lib.DebugLib;
import luaj.lib.DexLib;
import luaj.lib.FileLib;
import luaj.lib.HTTPLib;
import luaj.lib.IoLib.File;
import luaj.lib.IoLib;
import luaj.lib.MathLib;
import luaj.lib.ModLib;
import luaj.lib.OsLib;
import luaj.lib.PackageLib;
import luaj.lib.StackLib;
import luaj.lib.StringLib;
import luaj.lib.TableLib;
import luaj.lib.ToastLib;
import luaj.lib.ToolsLib;
import luaj.lib.TwoArgFunction;
import luaj.lib.Utf8Lib;
import luaj.lib.VarArgFunction;
import luaj.lib.jse.LuajavaLib;
import sig.c;

public class Script extends TwoArgFunction {
    public static abstract class ApiFunction extends VarArgFunction {
        private final boolean busyApiFunction;
        private volatile Throwable e;
        private String lazyName;
        private static Map map1;
        private static Map map2;
        private static Map map3;
        private static Map map4;
        protected volatile Object monitor;
        private volatile Varargs ret;
        static WeakReference wpParam;
        static WeakReference wpParams;

        static {
            ApiFunction.wpParams = new WeakReference(null);
            ApiFunction.wpParam = new WeakReference(null);
            ApiFunction.map1 = null;
            ApiFunction.map2 = null;
            ApiFunction.map3 = null;
            ApiFunction.map4 = null;
        }

        public ApiFunction() {
            this.busyApiFunction = this instanceof BusyApiFunction;
            this.ret = null;
            this.e = null;
            this.monitor = new Object();
        }

        public String explain() {
            StringBuilder stringBuilder0 = new StringBuilder(String.valueOf(this.ret != null)).append(" ");
            return this.e == null ? stringBuilder0.append(false).append(" \'").append(this.getNotifyReason()).append("\'").toString() : stringBuilder0.append(true).append(" \'").append(this.getNotifyReason()).append("\'").toString();
        }

        private static Map getMap(int mask) {
            Map map = new HashMap();
            for(int k = 0; k < 8; ++k) {
                if((1 << k & mask) != 0) {
                    map.put(Script.inds[k], ApiFunction.NIL);
                }
            }
            return map;
        }

        protected abstract int getMaxArgs();

        protected String getNotifyReason() {
            return null;
        }

        protected Varargs getTrueResult() {
            return LuaValue.TRUE;
        }

        @Override  // luaj.lib.VarArgFunction
        public final Varargs invoke(Varargs args) {
            long v4;
            long v3;
            long v2;
            long v1;
            ThreadLocal curFunc = Varargs.curFunc;
            VarArgFunction prevFunc = (VarArgFunction)curFunc.get();
            curFunc.set(this);
            try {
                LuaValue luaValue0 = args.arg1();
                if(luaValue0 instanceof LuaString && luaValue0.rawlen() > 1000000) {
                    args = ApiFunction.varargsOf(luaValue0.checkstring().substring(0, 1000000), args.subargs(2));
                }
                DebugLog log = Script.debugLog;
                if(log != null) {
                    try {
                        v1 = System.nanoTime();
                        this.logInvoke(log.log, args);
                    }
                    catch(Throwable e) {
                        Log.w("Failed write log for gg.ApiFunction", e);
                    }
                    log.globals.logMillis = (int)(((long)log.globals.logMillis) + (System.nanoTime() - v1) / 1000000L);
                }
                Varargs varargs1 = this.invoke2(args);
                if((this instanceof alert || this instanceof prompt || this instanceof choice || this instanceof multiChoice) && log != null) {
                    try {
                        v2 = System.nanoTime();
                        log.log.write("--[[ return: ");
                        log.log.write((varargs1 == null ? "null" : varargs1.toString()));
                        log.log.write(" ]]\n");
                    }
                    catch(Throwable e) {
                        Log.w("Failed log for gg.ApiFunction", e);
                    }
                    log.globals.logMillis = (int)(((long)log.globals.logMillis) + (System.nanoTime() - v2) / 1000000L);
                }
                if(this instanceof getResults && varargs1 instanceof LuaTable && log != null) {
                    try {
                        v3 = System.nanoTime();
                        log.log.write("--[[ count: ");
                        String s = Integer.toString(((LuaTable)varargs1).rawlen());
                        log.log.write(s);
                        log.log.write(" ]]\n");
                    }
                    catch(Throwable e) {
                        Log.w("Failed log for gg.ApiFunction", e);
                    }
                    log.globals.logMillis = (int)(((long)log.globals.logMillis) + (System.nanoTime() - v3) / 1000000L);
                }
                if(varargs1 == ApiFunction.TRUE && (this instanceof searchNumber || this instanceof refineNumber || this instanceof startFuzzy || this instanceof searchFuzzy || this instanceof searchAddress || this instanceof refineAddress) && log != null) {
                    try {
                        v4 = System.nanoTime();
                        log.log.write("--[[ found: ");
                        log.log.write(Long.toString(MainService.instance.mResultCount));
                        log.log.write(" ]]\n");
                    }
                    catch(Throwable e) {
                        Log.w("Failed log for gg.ApiFunction", e);
                    }
                    log.globals.logMillis = (int)(((long)log.globals.logMillis) + (System.nanoTime() - v4) / 1000000L);
                }
                return varargs1;
            }
            finally {
                curFunc.set(prevFunc);
            }
        }

        public Varargs invoke2(Varargs args) {
            this.ret = null;
            this.e = null;
            android.ext.Script.ApiFunction.1 run = () -> {
                Throwable e;
                try {
                    Varargs ret = null;
                    e = null;
                    ret = ApiFunction.this.invokeUi(this.val$args);
                    ApiFunction.this.ret = ret;
                }
                catch(Throwable e2) {
                    e = e2;
                    ApiFunction.this.e = e2;
                }
                if(!ApiFunction.this.busyApiFunction || ret != null || e != null) {
                    synchronized(ApiFunction.this.monitor) {
                        ApiFunction.this.monitor.notifyAll();
                    }
                }
            };
            synchronized(this.monitor) {
                ThreadManager.runOnUiThread(run);
                Script.waitNotify(this.monitor);
            }
            Throwable e = this.e;
            this.e = null;
            if(e != null) {
                this.ret = null;
                throw e instanceof RuntimeException ? ((RuntimeException)e) : new RuntimeException(e);
            }
            Varargs ret = this.ret;
            this.ret = null;
            if(!this.busyApiFunction || ret != null) {
                return ret;
            }
            String s = this.getNotifyReason();
            return s == null ? this.getTrueResult() : LuaValue.valueOf(s);

            class android.ext.Script.ApiFunction.1 implements Runnable {
                android.ext.Script.ApiFunction.1(Varargs varargs0) {
                }

                @Override
                public void run() {
                    ApiFunction.this.runUi(this.val$args);
                }
            }

        }

        public Varargs invokeUi(Varargs args) {
            return LuaValue.NIL;
        }

        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            return ApiFunction.logArg_(this, log, i, arg);
        }

        static boolean logArg_(ApiFunction self, Writer log, int i, LuaValue arg) throws IOException {
            Map map = null;
            if(arg instanceof LuaString) {
                return Consts.logString(log, arg.tojstring());
            }
            if(arg instanceof LuaTable) {
                if(self != null && i == 1) {
                    if(self instanceof removeResults || self instanceof loadResults || self instanceof getValues) {
                        map = ApiFunction.map1;
                        if(map == null) {
                            map = ApiFunction.getMap(5);
                            ApiFunction.map1 = map;
                        }
                    }
                    else if(self instanceof setValues) {
                        map = ApiFunction.map2;
                        if(map == null) {
                            map = ApiFunction.getMap(7);
                            ApiFunction.map2 = map;
                        }
                    }
                    else if(self instanceof getValuesRange || self instanceof removeListItems) {
                        map = ApiFunction.map3;
                        if(map == null) {
                            map = ApiFunction.getMap(1);
                            ApiFunction.map3 = map;
                        }
                    }
                    else if(self instanceof addListItems) {
                        map = ApiFunction.map4;
                        if(map == null) {
                            map = ApiFunction.getMap(0xFF);
                            ApiFunction.map4 = map;
                        }
                    }
                }
                ((LuaTable)arg).tojstring(log, map);
                return true;
            }
            log.write(arg.tojstring());
            return true;
        }

        private void logArgs(Writer log, Varargs args) throws IOException {
            int n = args.narg();
            int v1 = this.getMaxArgs();
            if(v1 >= 0 && n > v1) {
                n = v1;
            }
            for(int i = 1; i <= n; ++i) {
                if(i != 1) {
                    log.write(", ");
                }
                if(!this.logArg(log, i, args.arg(i))) {
                    break;
                }
            }
        }

        protected static boolean logConst(Writer log, Const[] list, LuaValue arg) throws IOException {
            return Consts.logConst(log, list, arg.toint());
        }

        protected static boolean logHex(Writer log, LuaValue arg) throws IOException {
            return Consts.logHex(log, arg.tolong());
        }

        protected void logInvoke(Writer log, Varargs args) throws IOException {
            if(this instanceof isVisible || this instanceof internal1 || this instanceof internal2 || this instanceof sleep) {
                return;
            }
            log.write(this.name());
            log.write("(");
            this.logArgs(log, args);
            log.write(")\n");
        }

        @Override  // luaj.LuaFunction
        public String name() [...] // 潜在的解密器

        // 检测为 Lambda 实现
        public void runUi(Varargs args) [...]

        @Override  // luaj.lib.LibFunction
        public String tojstring() {
            String s = this.usage();
            String s1 = s.replace("[", "").replace("]", "").replace(" ,", ",");
            Pattern p = (Pattern)ApiFunction.wpParams.get();
            if(p == null) {
                p = Pattern.compile(".*(\\(.*\\)).*");
                ApiFunction.wpParams = new WeakReference(p);
            }
            String func = p.matcher(s1).replaceAll("$1");
            Pattern p = (Pattern)ApiFunction.wpParam.get();
            if(p == null) {
                p = Pattern.compile("(?<=[\\(,])( ?)\\s*\\S*\\s*(\\S*)(\\s*=\\s*[^,\\)]*)?(?=[,\\)])");
                ApiFunction.wpParam = new WeakReference(p);
            }
            return "function " + p.matcher(func).replaceAll("$1$2") + " end, -- " + s;
        }

        abstract String usage();
    }

    abstract class BusyApiFunction extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected String getNotifyReason() {
            return Script.this.notifyReason;
        }
    }

    static class Const {
        final String name;
        final int value;

        public Const(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    static class Consts {
        Const[] ASM_;
        Const[] DUMP_;
        Const[] LOAD_;
        Const[] POINTER_;
        Const[] PROT_;
        Const[] REGION_;
        Const[] SAVE_;
        Const[] SIGN_;
        Const[] SIGN_FUZZY_;
        Const[] TYPE_;

        Consts() {
            this.REGION_ = null;
            this.TYPE_ = null;
            this.SIGN_FUZZY_ = null;
            this.SIGN_ = null;
            this.LOAD_ = null;
            this.SAVE_ = null;
            this.PROT_ = null;
            this.POINTER_ = null;
            this.DUMP_ = null;
            this.ASM_ = null;
        }

        static boolean logConst(Writer log, Const[] list, int v) {
            try {
                boolean empty = true;
                for(int v1 = 0; true; ++v1) {
                    if(v1 >= list.length) {
                        if(empty || v != 0) {
                            log.write(Integer.toString(v));
                        }
                        return true;
                    }
                    Const c = list[v1];
                    int b = c.value;
                    if((v & b) == b) {
                        log.write(c.name);
                        v &= ~b;
                        if(v != 0) {
                            log.write(" | ");
                        }
                        empty = false;
                    }
                }
            }
            catch(IOException e) {
                Log.w("Writer fail", e);
                return false;
            }
        }

        static boolean logHex(Writer log, long v) {
            try {
                if(v == 0L || v == -1L) {
                    log.write(Long.toString(v));
                    return true;
                }
                log.write("0x");
                log.write(Long.toHexString(v));
                return true;
            }
            catch(IOException e) {
                Log.w("Writer fail", e);
                return false;
            }
        }

        static boolean logNumberString(Writer log, String v) {
            return Consts.logString(log, Script.numberToLua(v));
        }

        static boolean logString(Writer log, String v) {
            try {
                log.write("\"");
                if(v.length() <= 0xFFFF) {
                    v = v.replace("\"", "\\\"");
                }
                log.write(v);
                log.write("\"");
                return true;
            }
            catch(IOException e) {
                Log.w("Writer fail", e);
                return false;
            }
        }
    }

    public static abstract class DebugFunction extends ApiFunction {
        protected LuaClosure caller;

        public DebugFunction() {
            this.caller = null;
        }

        public void setCaller(LuaClosure caller) {
            this.caller = caller;
        }
    }

    static final class DebugLog {
        final Globals globals;
        final Writer log;

        DebugLog(Writer log, Globals globals) {
            this.log = log;
            this.globals = globals;
        }
    }

    static final class GgExit extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            if(varargs0.narg() == 1 && !varargs0.checkboolean(1)) {
                Main.doRestart = 1;
            }
            Main.exit();
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.exit(bool) -> nil";
        }
    }

    final class IoLibSafe extends IoLib {
        @Override  // luaj.lib.IoLib
        protected File openFile(String filename, char mode) throws IOException {
            if(Script.this.isBadFile(filename, mode == 0x72)) {
                java.io.File file0 = new java.io.File(Tools.getFilesDirHidden(), "empty.txt");
                file0.createNewFile();
                File ioLib$File0 = super.openFile(file0.getAbsolutePath(), mode);
                file0.delete();
                return ioLib$File0;
            }
            return super.openFile(filename, mode);
        }
    }

    public static class Logger {
        private static final int MAX_SIZE = 0x40000;
        final FileInputStream fis;
        final OutputStream os;
        int size;
        final StringBuilder str;

        Logger() {
            FileInputStream fis;
            StringBuilder str = null;
            BufferedOutputStream os = null;
            FileInputStream fis = null;
            try {
                java.io.File file0 = new java.io.File(Tools.getFilesDir(), System.currentTimeMillis() + ".tmp");
                file0.createNewFile();
                BufferedOutputStream bufferedOutputStream1 = new BufferedOutputStream(new FileOutputStream(file0));
                try {
                    fis = new FileInputStream(file0);
                }
                catch(Throwable e) {
                    os = bufferedOutputStream1;
                    goto label_19;
                }
                fis = fis;
                os = bufferedOutputStream1;
                file0.delete();
                fis = fis;
                os = bufferedOutputStream1;
                goto label_21;
            }
            catch(Throwable e) {
            }
        label_19:
            Log.badImplementation(e);
            str = new StringBuilder();
        label_21:
            this.str = str;
            this.os = os;
            this.fis = fis;
            this.size = 0;
        }

        public void append(String line) {
            StringBuilder str = this.str;
            if(str == null) {
                try {
                    byte[] arr_b = line.getBytes();
                    this.os.write(arr_b);
                    this.size += arr_b.length;
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
                return;
            }
            str.append(line);
        }

        @Override
        public String toString() {
            if(this.size < 0) {
                return "must be called only once";
            }
            StringBuilder str = this.str;
            if(str == null) {
                try {
                    int size = this.size;
                    int full = size;
                    this.size = -1;
                    this.os.close();
                    if(size > 0x40000) {
                        this.fis.skip(((long)(size - 0x40000)));
                        size = 0x40000;
                    }
                    byte[] buf = new byte[size];
                    int v2 = this.fis.read(buf);
                    this.fis.close();
                    Log.d(("Log script size: read = " + v2 + "; used = " + size + "; full = " + full));
                    if(v2 > 0) {
                        return new String(buf, 0, v2);
                    }
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
                return "";
            }
            return str.toString();
        }
    }

    static final class OsExit extends LuaError {
        final int code;
        private static final long serialVersionUID = 1L;

        public OsExit(int code) {
            super("called os.exit(" + code + ')');
            this.code = code;
        }
    }

    final class OsLibSafe extends OsLib {
        @Override  // luaj.lib.OsLib
        protected void remove(String filename) throws IOException {
            if(Script.this.isBadFile(filename, false)) {
                return;
            }
            super.remove(filename);
        }

        @Override  // luaj.lib.OsLib
        protected void rename(String oldname, String newname) throws IOException {
            if(Script.this.isBadFile(oldname, false) || Script.this.isBadFile(newname, false)) {
                return;
            }
            super.rename(oldname, newname);
        }
    }

    final class QQchat extends ApiFunction {
        QQchat() {
            super();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(("mqqwpa://im/chat?chat_type=wpa&uin=" + args.checkjstring(1))));
            try {
                Tools.getContext().startActivity(intent);
            }
            catch(Exception unused_ex) {
                Tools.showToast("未安装手Q或安装的版本不支持", 1);
            }
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.QQchat(string key) -> nil";
        }
    }

    final class QQgroup extends ApiFunction {
        QQgroup() {
            super();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + args.checkjstring(1) + "&card_type=group&source=qrcode")));
            try {
                Tools.getContext().startActivity(intent);
            }
            catch(Exception unused_ex) {
                Tools.showToast("未安装手Q或安装的版本不支持", 1);
            }
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.QQgroup(string key) -> nil";
        }
    }

    static class SavedState {
        int memoryRanges;

        private SavedState() {
        }

        SavedState(SavedState script$SavedState0) {
        }
    }

    class ScriptPrintStream extends LoggingPrintStream {
        final boolean isErrorStream;

        public ScriptPrintStream(boolean isErrorStream) {
            this.isErrorStream = isErrorStream;
        }

        @Override  // android.internal.LoggingPrintStream
        protected void log(String line) {
            Script.this.appendLog(this.isErrorStream, line);
            if(this.isErrorStream) {
                Log.e(("script: " + line));
                return;
            }
            Log.i(("script: " + line));
        }
    }

    class ScriptThread extends Thread {
        public ScriptThread() {
            super("Script thread");
            ExceptionHandler.install(this);
        }

        @Override
        public void run() {
            String trace;
            MainService service;
            try {
                service = MainService.instance;
                service.onScriptStart(Script.this);
                Throwable err = null;
                Script.this.saveState();
                try {
                    Script.this.runScript();
                }
                catch(OsExit e) {
                    Log.e(("Script interrupted by self: " + e.getMessage()));
                    Script script0 = Script.this;
                    if(e.code != 0) {
                        script0.appendLog(": " + e.code);
                    }
                }
                catch(LuaError e) {
                    Throwable throwable3 = e.getCause();
                    if(!(throwable3 instanceof InterruptedException) || Script.this.interruptedReason == 0) {
                        if(e instanceof Internal) {
                            ExceptionHandler.sendException(Thread.currentThread(), e, false);
                        }
                        err = e;
                    }
                    else {
                        Log.e(("Script thread interrupted: " + Script.this.interruptedReason), throwable3);
                        String s = Re.s(0x7F070329) + " [" + Script.this.interruptedReason + "]";  // string:script_interrupted "The script interrupted."
                        Script.this.appendLog(s);
                    }
                }
                catch(Throwable e) {
                    err = e;
                }
                int flags = 0;
                if(err != null) {
                    try {
                        Log.e("Script error", err);
                        trace = Log.getStackTraceString(err);
                        String s2 = err.getMessage();
                        if(s2 != null && !trace.contains(s2)) {
                            trace = String.valueOf(s2) + '\n' + trace;
                        }
                        int possible = 0;
                        if(err instanceof LuaError) {
                            if(s2 != null && (s2.contains("<!DOCTYPE") || s2.contains("<html"))) {
                                possible = 0x7F07032D;  // string:script_web "a web page"
                            }
                            if(Script.this.source instanceof java.io.File && Script.this.isSavedList(((java.io.File)Script.this.source))) {
                                possible = 0x7F07032E;  // string:script_saved_list "a saved list"
                            }
                            if(possible != 0) {
                                String s3 = Tools.stringFormat(Re.s(0x7F07032C), new Object[]{Re.s(possible)});  // string:script_possible "It looks like you are trying to execute __s__ as a script."
                                Script.this.appendLog(s3);
                            }
                        }
                        if(Script.this.source instanceof java.io.File) {
                            String s4 = Lasm.getLine(err, ((java.io.File)Script.this.source).getAbsolutePath(), true);
                            if(s4 != null && s4.length() > 0) {
                                trace = s4 + trace;
                                flags = 4;
                            }
                        }
                        goto label_49;
                    }
                    catch(OutOfMemoryError e) {
                        try {
                            trace = e.toString() + '\n';
                        label_49:
                            String s5 = Tools.stripColon(0x7F07032A) + ": " + trace;  // string:script_error "Script error"
                            Script.this.appendLog(s5);
                            goto label_55;
                        }
                        catch(OutOfMemoryError e) {
                        }
                    }
                    String s6 = Tools.stripColon(0x7F07032B) + ": " + e.toString();  // string:script_oom "Out of memory"
                    Script.this.appendLog(s6);
                }
            label_55:
                Script.this.restoreState();
                String action = null;
                android.ext.Script.ScriptThread.1 listener = null;
                if(Script.this.source instanceof java.io.File) {
                    action = Re.s(0x7F0700F2);  // string:restart "Restart"
                    listener = new DialogInterface.OnClickListener() {
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            java.io.File file = (java.io.File)Script.this.source;
                            if(!Tools.isFile(file)) {
                                return;
                            }
                            MainService.instance.executeScript(file.getAbsolutePath(), Script.this.params, Script.this.out);
                        }
                    };
                }
                service.showScriptEnd(new Runnable() {
                    @Override
                    public void run() {
                        Tools.alertBigText(Script.this.log.toString().trim(), flags, action, listener);
                    }
                });
            }
            catch(Throwable e) {
                Log.e("Script run exception", e);
            }
            service.onScriptEnd(Script.this);
        }
    }

    final class addListItems extends ApiFunction {
        final ArrayList items;
        LuaValue result;

        addListItems() {
            this.items = new ArrayList();
            this.result = null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            NumberFormatException e;
            SavedItem savedItem0;
            Varargs ret;
            ArrayList items;
            try {
                items = this.items;
                items.clear();
                Iterator luaTable$Iterator0 = args.checktable(1).iterator();
                while(true) {
                    if(!luaTable$Iterator0.next()) {
                        this.result = null;
                        Object monitor = new Object();
                        synchronized(monitor) {
                            ThreadManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        SavedListAdapter savedList = MainService.instance.savedList;
                                        for(Object object0: addListItems.this.items) {
                                            SavedItem item = (SavedItem)object0;
                                            if(item != null) {
                                                savedList.add(item, 0, false);
                                            }
                                        }
                                        savedList.notifyDataSetChanged();
                                        savedList.reloadData();
                                        addListItems.this.result = LuaValue.TRUE;
                                    }
                                    catch(Throwable e) {
                                        Log.e("Failed add list items", e);
                                        addListItems.this.result = LuaValue.valueOf(Script.toString(e));
                                    }
                                    synchronized(monitor) {
                                        monitor.notifyAll();
                                    }
                                }
                            });
                            Script.waitNotify(monitor);
                        }
                        ret = this.result;
                        break;
                    }
                    LuaValue luaValue0 = luaTable$Iterator0.value();
                    try {
                        savedItem0 = Script.toSavedItem(luaValue0.checktable());
                        if(savedItem0 == null) {
                            continue;
                        }
                        goto label_25;
                    }
                    catch(NumberFormatException numberFormatException0) {
                        e = numberFormatException0;
                    }
                    catch(LuaError luaError1) {
                        e = luaError1;
                    }
                    throw Script.failedParse(luaTable$Iterator0.key(), luaValue0, e);
                label_25:
                    items.add(savedItem0);
                }
            }
            catch(LuaError e) {
                throw e;
            }
            catch(Throwable e) {
                Log.e("Failed add list items", e);
                ret = LuaValue.valueOf(Script.toString(e));
            }
            try {
                items.clear();
                items.trimToSize();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            this.result = null;
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.addListItems(table items) -> true || string with error";
        }
    }

    static final class alert extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private volatile int result;

        alert() {
            this.result = 0;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 4;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String s = args.checkjstring(1);
            String s1 = args.optjstring(2, Re.s(0x7F07009D));  // string:ok "OK"
            String s2 = args.optjstring(3, null);
            String s3 = args.optjstring(4, null);
            this.result = 0;
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setMessage(Re.s(s)).setPositiveButton(Re.s(s1), alert.this);
                        if(s2 != null) {
                            alertDialog$Builder0.setNegativeButton(Re.s(s2), alert.this);
                        }
                        if(s3 != null) {
                            alertDialog$Builder0.setNeutralButton(Re.s(s3), alert.this);
                        }
                        AlertDialog alertDialog0 = alertDialog$Builder0.create();
                        Alert.setOnDismissListener(alertDialog0, alert.this);
                        Alert.show(alertDialog0);
                        MainService.instance.eye(true);
                    }
                });
                Script.waitNotify(this);
            }
            return LuaValue.valueOf(this.result);
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            this.result = -which;
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            synchronized(this) {
                this.notify();
            }
            MainService.instance.eye(false);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.alert(string text [, string positive = \'ok\' [, string negative = nil [, string neutral = nil]]]) -> int: 0 = cancel, 1 = positive, 2 = negative, 3 = neutral";
        }
    }

    final class allocatePage extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        protected Varargs getTrueResult() {
            long page = Script.allocatedPage;
            return 0xFFFFFFFFFFFFFC00L < page && page <= 0L ? LuaValue.valueOf((Re.s(0x7F0702DC) + ' ' + -page)) : LuaValue.valueOf(Script.allocatedPage);  // string:allocate_fail "Could not allocate memory page. Error"
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            int v = args.optint(1, 6);
            long v1 = args.optlong(2, 0L);
            Script.allocatedPage = 0L;
            MainService.instance.mDaemonManager.allocatePage(Script.this.getSeq(), v1, v);
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 1: {
                    return arg.isint() ? allocatePage.logConst(log, Script.this.consts.PROT_, arg) : super.logArg(log, 1, arg);
                }
                case 2: {
                    return arg.islong() ? allocatePage.logHex(log, arg) : super.logArg(log, 2, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.allocatePage([int mode = gg.PROT_READ | gg.PROT_EXEC [, long address = 0]) -> long || string with error";
        }
    }

    final class bytes extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String s = args.checkjstring(1);
            String s1 = args.optjstring(2, "UTF-8");
            try {
                byte[] arr_b = s.getBytes(s1);
                Varargs out = new LuaTable();
                ((LuaTable)out).presize(arr_b.length);
                for(int i = 0; true; ++i) {
                    if(i >= arr_b.length) {
                        return out;
                    }
                    ((LuaTable)out).rawset(i + 1, LuaValue.valueOf(arr_b[i] & 0xFF));
                }
            }
            catch(UnsupportedEncodingException e) {
                throw new LuaError(e);
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.bytes(string text [, string encoding = \'UTF-8\']) -> table";
        }
    }

    static final class choice extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private volatile int result;

        choice() {
            this.result = 0;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable luaTable0 = args.checktable(1);
            LuaValue luaValue0 = args.arg(2);
            ArrayList keys = new ArrayList();
            ArrayList labels = new ArrayList();
            int sel = -1;
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            for(int i = 0; luaTable$Iterator0.next(); ++i) {
                LuaValue luaValue1 = luaTable$Iterator0.key();
                LuaValue luaValue2 = luaTable$Iterator0.value();
                if(luaValue1.eq_b(luaValue0)) {
                    sel = i;
                }
                labels.add(Re.s(luaValue2.checkjstring()));
                keys.add(luaValue1);
            }
            String s = Re.s(args.optjstring(3, ""));
            CharSequence[] items = (CharSequence[])labels.toArray(new CharSequence[labels.size()]);
            this.result = -1;
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setCustomTitle(Tools.getCustomTitle(null, (s.length() <= 0 ? null : "\n" + s))).setNegativeButton(Re.s(0x7F0700A1), choice.this);  // string:cancel "Cancel"
                        if(luaValue0.isnil()) {
                            alertDialog$Builder0.setItems(items, choice.this);
                        }
                        else {
                            alertDialog$Builder0.setSingleChoiceItems(items, sel, choice.this);
                        }
                        AlertDialog alertDialog0 = alertDialog$Builder0.create();
                        Alert.setOnDismissListener(alertDialog0, choice.this);
                        Alert.show(alertDialog0);
                        MainService.instance.eye(true);
                    }
                });
                Script.waitNotify(this);
            }
            return this.result >= 0 ? ((LuaValue)keys.get(this.result)) : LuaValue.NIL;
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            this.result = which;
            Tools.dismiss(dialog);
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            synchronized(this) {
                this.notify();
            }
            MainService.instance.eye(false);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.choice(table items [, string selected = nil [, string message = nil]]) -> string || nil";
        }
    }

    final class clearList extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                MainService.instance.savedList.clear();
                return LuaValue.TRUE;
            }
            catch(Throwable e) {
                Log.e("Failed clear list", e);
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.clearList() -> true || string with error";
        }
    }

    final class clearResults extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.clear(Script.this.getSeq());
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.clearResults() -> nil";
        }
    }

    static final class codeScript extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            String s = varargs0.checkjstring(1);
            java.io.File file0 = new java.io.File(s);
            return file0.isFile() ? new Script(file0, 0, "").start() : new Script(s, 0, "").start();
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.codeScript(string code||path) -> func";
        }
    }

    static final class colorAlert extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private volatile int result;

        colorAlert() {
            this.result = 0;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 5;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            String s = varargs0.optjstring(1, "");
            String s1 = varargs0.checkjstring(2);
            String s2 = varargs0.optjstring(3, Re.s(0x7F07009D));  // string:ok "OK"
            String s3 = varargs0.optjstring(4, null);
            String s4 = varargs0.optjstring(5, null);
            this.result = 0;
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setTitle(Html.fromHtml(s)).setMessage(Html.fromHtml(s1)).setPositiveButton(Re.s(s2), colorAlert.this);
                        if(s3 != null) {
                            alertDialog$Builder0.setNegativeButton(Re.s(s3), colorAlert.this);
                        }
                        if(s4 != null) {
                            alertDialog$Builder0.setNeutralButton(Re.s(s4), colorAlert.this);
                        }
                        AlertDialog alertDialog0 = alertDialog$Builder0.create();
                        Alert.setOnDismissListener(alertDialog0, colorAlert.this);
                        Alert.show(alertDialog0);
                        MainService.instance.eye(true);
                    }
                });
                Script.waitNotify(this);
            }
            return LuaValue.valueOf(this.result);
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialogInterface0, int v) {
            this.result = -v;
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialogInterface0) {
            synchronized(this) {
                this.notify();
            }
            MainService.instance.eye(false);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.colorAlert([string title,] string text [, string positive = \'ok\' [, string negative = nil [, string neutral = nil]]]) -> int: 0 = cancel, 1 = positive, 2 = negative, 3 = neutral";
        }
    }

    final class command extends ApiFunction {
        command() {
            super();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String s = args.checkjstring(1);
            return LuaValue.valueOf(new ExeCommand().run(s, 10000).getResult());
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "revo.command(String cmd) -> string";
        }
    }

    final class copyMemory extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.mDaemonManager.copyMemory(Script.this.getSeq(), args.checklong(1), args.checklong(2), args.checkint(3));
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            if(i != 1 && i != 2) {
                return super.logArg(log, i, arg);
            }
            return arg.islong() ? copyMemory.logHex(log, arg) : super.logArg(log, i, arg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.copyMemory(long from, long to, int bytes) -> true || string with error";
        }
    }

    static final class copyText extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String text = args.checkjstring(1);
            if(args.optboolean(2, true)) {
                text = Script.numberFromLua(text);
            }
            Tools.copyText(Re.s(text));
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.copyText(string text [, bool fixLocale = true]) -> nil";
        }
    }

    final class disasm extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            long v = args.checklong(2);
            int v1 = args.checkint(3);
            switch(args.checkint(1)) {
                case 4: {
                    return LuaString.valueOf(ArmDis.getArmOpcode(new ArmDis(), v, ((long)v1)));
                }
                case 5: {
                    return LuaString.valueOf(ArmDis.getThumbOpcode(new ArmDis(), v, ((long)v1)));
                }
                case 6: {
                    StringBuilder out = new StringBuilder();
                    Arm64Dis.disasm(Arm64Dis.args(), v, v1, out);
                    return LuaString.valueOf(out.toString());
                }
                default: {
                    return disasm.argerror(1, "gg.disasm", "unknown type: " + args.checkint(1));
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 1: {
                    return arg.isint() ? disasm.logConst(log, Script.this.consts.ASM_, arg) : super.logArg(log, 1, arg);
                }
                case 2: 
                case 3: {
                    return arg.islong() ? disasm.logHex(log, arg) : super.logArg(log, i, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.disasm(int type, long address, int opcode) -> string";
        }
    }

    final class diyToast extends ApiFunction {
        private volatile int backgroundcolor;
        private volatile String str;
        private volatile int textcolor;
        private volatile int textlocation;
        private volatile int textsize;

        diyToast() {
            super();
            this.str = "";
            this.textlocation = 0;
            this.textsize = 14;
            this.textcolor = -1;
            this.backgroundcolor = -12809254;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 5;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            this.str = varargs0.checkjstring(1);
            this.backgroundcolor = varargs0.optint(2, -12809254);
            this.textcolor = varargs0.optint(3, -1);
            this.textsize = varargs0.optint(4, 14);
            this.textlocation = varargs0.optint(5, 0);
            ThreadManager.runOnUiThread(new Runnable() {
                private volatile int backgroundcolor;
                private volatile String str;
                private volatile int textcolor;
                private volatile int textlocation;
                private volatile int textsize;

                {
                    String s = this.backgroundcolor;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    int v = this.textcolor;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    int v1 = this.textsize;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    int v2 = this.textlocation;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    this.str = s;
                    this.backgroundcolor = v;
                    this.textcolor = v1;
                    this.textsize = v2;
                    this.textlocation = v3;
                }

                @Override
                public void run() {
                    Script.diyToast.2.show(ToastManager.getContext(), this.str, this.backgroundcolor, this.textcolor, this.textsize, this.textlocation);
                }
            });
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.diyToast(string text [, int backgroundcolor = 0xF [, int textcolor = 0xF [, int textsize [, int textlocation ]]]) -> nil)";
        }
    }

    final class dumpMemory extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 4;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService service = MainService.instance;
            String prefix = service.processInfo == null ? "unknown" : service.processInfo.packageName;
            if(service.showBusyDialog()) {
                service.mDaemonManager.doDump(Script.this.getSeq(), args.checklong(1), args.checklong(2), args.optint(4, 0), args.checkjstring(3), prefix);
                return null;
            }
            return LuaValue.valueOf(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 1: 
                case 2: {
                    return arg.islong() ? dumpMemory.logHex(log, arg) : super.logArg(log, i, arg);
                }
                case 4: {
                    return arg.isint() ? dumpMemory.logConst(log, Script.this.consts.DUMP_, arg) : super.logArg(log, 4, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.dumpMemory(long from, long to, string dir [, int flags = nil]) -> true || string with error";
        }
    }

    final class editAll extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            long v11;
            long v9;
            long v13;
            if(!Script.this.resultsLoaded) {
                throw new LuaError("You must call gg.getResults before calling gg.editAll.");
            }
            try {
                int changed = 0;
                String data = Script.numberFromLua(args.checkjstring(1));
                int v1 = Script.checkType(args.checkint(2));
                XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(data, false);
                int x = searchButtonListener$XorMode0 == null ? 0 : searchButtonListener$XorMode0.x;
                if(searchButtonListener$XorMode0 != null) {
                    data = searchButtonListener$XorMode0.input;
                }
                MainService service = MainService.instance;
                ArrayListResults mAddressList = service.mAddressList;
                int v3 = mAddressList.size();
                String[] parts = null;
                byte[] bytes = null;
                char[] chars = null;
                if(v1 == 1 && ParserNumbers.isString(data)) {
                    bytes = ParserNumbers.getBytes(data);
                }
                else if((v1 & 6) != 0 && ParserNumbers.isString(data) && data.charAt(0) == 59) {
                    chars = data.substring(1).toCharArray();
                }
                else if(data.indexOf(59) != -1) {
                    parts = data.split(";");
                    if(parts.length == 0) {
                        parts = null;
                    }
                }
                AddressItem item = new AddressItem();
                Result result = null;
                Result[] results = parts == null ? null : new Result[parts.length];
                int n = 0;
                long lastAddr = 0L;
                int i = 0;
                while(true) {
                    if(i < v3) {
                        try {
                            mAddressList.get(i, item);
                            goto label_46;
                        }
                        catch(IndexOutOfBoundsException e) {
                            Log.w("List changed", e);
                        }
                    }
                    service.mDaemonManager.doWaited(Script.this.getSeq());
                    Object monitor = new Object();
                    synchronized(monitor) {
                        ThreadManager.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainService.instance.onMemoryChanged(Script.this.getSeq());
                                synchronized(monitor) {
                                    monitor.notifyAll();
                                }
                            }
                        });
                        Script.waitNotify(monitor);
                    }
                    return LuaValue.valueOf(changed);
                label_46:
                    if(item.flags == v1) {
                        if(bytes != null) {
                            int n = lastAddr + 1L == item.address ? n : 0;
                            lastAddr = item.address;
                            if(n < bytes.length) {
                                n = n + 1;
                                v13 = 0xFFL & ((long)bytes[n]);
                            }
                            else {
                                v13 = 0L;
                                n = n;
                            }
                            item.data = v13;
                        }
                        else if(chars == null) {
                            if(parts != null) {
                                n = i % parts.length;
                                data = parts[n];
                                Result parserNumbers$Result1 = results[n];
                                if(parserNumbers$Result1 == null) {
                                    result = AddressItem.parseString(null, data, v1, 0x7F0700CF, item.address);  // string:number_name "number"
                                    results[n] = result;
                                }
                                else {
                                    result = parserNumbers$Result1;
                                }
                            }
                            else if(result == null) {
                                result = AddressItem.parseString(null, data, v1, 0x7F0700CF, item.address);  // string:number_name "number"
                            }
                            item.dataFromString(null, result, data, item.address, "0", 0);
                        }
                        else {
                            boolean word = item.flags == 2;
                            int n = ((long)(word ? 2 : 4)) + lastAddr == item.address ? n : 0;
                            lastAddr = item.address;
                            if(n < chars.length) {
                                v9 = 0xFFFFL & ((long)chars[n]);
                                ++n;
                            }
                            else {
                                v9 = 0L;
                            }
                            item.data = v9;
                            if(word) {
                                n = n;
                            }
                            else {
                                long v10 = item.data;
                                if(n < chars.length) {
                                    n = n + 1;
                                    v11 = (0xFFFFL & ((long)chars[n])) << 16;
                                }
                                else {
                                    v11 = 0L;
                                    n = n;
                                }
                                item.data = v11 | v10;
                            }
                        }
                        item.alter(x);
                        ++changed;
                        item = new AddressItem();
                    }
                    ++i;
                }
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            if(i != 2) {
                return super.logArg(log, i, arg);
            }
            return arg.isint() ? editAll.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 2, arg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.editAll(string value, int type) -> count of changed || string with error";
        }
    }

    static final class exit extends VarArgFunction {
        @Override  // luaj.lib.VarArgFunction
        public Varargs invoke(Varargs args) {
            throw new OsExit(args.optint(1, 0));
        }
    }

    static final class format extends VarArgFunction {
        final LuaValue base;

        public format(LuaValue base) {
            this.base = base;
        }

        @Override  // luaj.lib.VarArgFunction
        public Varargs invoke(Varargs args) {
            Varargs varargs1 = LuaValue.varargsOf(LuaValue.valueOf(Re.s(args.checkjstring(1)).replace("%,d", "%d")), args.subargs(2));
            return this.base.invoke(varargs1);
        }
    }

    static final class getActiveTab extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(MainService.instance.getTabIndex());
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getActiveTab() -> int";
        }
    }

    static final class getAddressUTF extends BusyApiFunction {
        private static final int HEX = 0;
        private static final int HEX_TEXT = 6;
        private static final int HEX_UTF16LE = 8;
        private static final int HEX_UTF8 = 7;
        private static final int HEX_UTF8_UTF16LE = 9;
        private static int MAX_ITEMS = 0;
        private static final int UTF16LE = 5;
        private static final int UTF8 = 4;
        String checked;

        static {
            getAddressUTF.MAX_ITEMS = 0x7FFFFFF5;
        }

        getAddressUTF(Script script0) {
            Script.this = script0;
            super();
        }

        private void add(Bytes copySelected$Bytes0, StringBuilder stringBuilder0, AddressItem addressItem0, int v) {
            if(copySelected$Bytes0 != null) {
                if(copySelected$Bytes0.nextAddr == 0L) {
                    copySelected$Bytes0.nextAddr = addressItem0.address;
                }
                int v1 = addressItem0.getSize();
                long v2 = addressItem0.data;
                if(copySelected$Bytes0.bytes.length <= copySelected$Bytes0.len + v1) {
                    byte[] arr_b = new byte[copySelected$Bytes0.len + v1];
                    System.arraycopy(copySelected$Bytes0.bytes, 0, arr_b, 0, copySelected$Bytes0.bytes.length);
                    copySelected$Bytes0.bytes = arr_b;
                }
                for(int v3 = 0; v3 < v1; ++v3) {
                    long v4 = addressItem0.address + ((long)v3);
                    if(copySelected$Bytes0.nextAddr <= v4) {
                        copySelected$Bytes0.bytes[copySelected$Bytes0.len] = (byte)(((int)(0xFFL & v2)));
                        ++copySelected$Bytes0.len;
                        copySelected$Bytes0.nextAddr = v4;
                    }
                    v2 >>= 8;
                }
                return;
            }
            if(stringBuilder0.length() != 0) {
                stringBuilder0.append(';');
            }
            if((v & 2) != 0) {
                stringBuilder0.append("0000000000000000");
                stringBuilder0.append('r');
            }
            else if((v & 1) == 0) {
                stringBuilder0.append(addressItem0.getStringDataTrim());
            }
            else {
                stringBuilder0.append("0000000000000000");
                stringBuilder0.append('h');
            }
            if((v & 4) != 0) {
                stringBuilder0.append(addressItem0.getShortName());
            }
        }

        String copy(int v) {
            long v9;
            int v8;
            int v7;
            StringBuilder stringBuilder0 = new StringBuilder();
            int v1 = 0;
            int v2 = 0;
            long v3 = 0L;
            int v4 = Integer.numberOfTrailingZeros(v & 0x3F0);
            Bytes copySelected$Bytes0 = v4 == 0x20 ? null : new Bytes();
            ArrayList arrayList0 = Script.memList;
            if(arrayList0 != null) {
                ++Script.memListUse;
                Script.memList = null;
                int v5 = arrayList0.size();
                for(int v6 = 0; true; ++v6) {
                    if(v6 >= v5) {
                        v7 = v1;
                        v8 = v2;
                        v9 = v3;
                        break;
                    }
                    AddressItem addressItem0 = (AddressItem)arrayList0.get(v6);
                    if(addressItem0 != null) {
                        if(v1 < getAddressUTF.MAX_ITEMS) {
                            this.add(copySelected$Bytes0, stringBuilder0, addressItem0, v);
                            v3 = addressItem0.address;
                            ++v1;
                        }
                        ++v2;
                        if(v2 > getAddressUTF.MAX_ITEMS) {
                            v7 = v1;
                            v8 = v2;
                            v9 = v3;
                            break;
                        }
                    }
                }
                if(v7 >= (copySelected$Bytes0 == null ? 2 : 1)) {
                    if(v8 > v7) {
                        Tools.showToast(Tools.stringFormat(Re.s(0x7F070167), new Object[]{v7}));  // string:used_only_first "Used only first __d__ items."
                    }
                    int v10 = 58;
                    if(copySelected$Bytes0 != null) {
                        if(v4 != 4 && v4 != 5) {
                            if(v4 == 6) {
                                stringBuilder0.append('h');
                                char[] arr_c = HexText.hexArray;
                                byte[] arr_b = copySelected$Bytes0.bytes;
                                int v11 = copySelected$Bytes0.len;
                                for(int v12 = 0; v12 < v11; ++v12) {
                                    stringBuilder0.append(' ');
                                    int v13 = arr_b[v12];
                                    stringBuilder0.append(arr_c[(v13 & 0xF0) >> 4]);
                                    stringBuilder0.append(arr_c[v13 & 15]);
                                }
                                return stringBuilder0.toString();
                            }
                            stringBuilder0.append('Q');
                            stringBuilder0.append(' ');
                            HexText.getText(stringBuilder0, 0, copySelected$Bytes0.bytes, copySelected$Bytes0.len, v4 == 7 || v4 == 9, v4 == 8 || v4 == 9, null);
                            return stringBuilder0.toString();
                        }
                        if(v4 == 5) {
                            v10 = 59;
                        }
                        stringBuilder0.append(((char)v10));
                        stringBuilder0.append(new String(copySelected$Bytes0.bytes, 0, copySelected$Bytes0.len, ParserNumbers.getCharset(v4 == 5)));
                        return stringBuilder0.toString();
                    }
                    if((v & 8) != 0) {
                        stringBuilder0.append(':');
                        stringBuilder0.append(v9 + 1L);
                    }
                }
            }
            return stringBuilder0.toString();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        protected Varargs getTrueResult() {
            int v = 0;
            for(int v1 = 0; v1 < 6; ++v1) {
                v = this.checked.equals(new String[]{"UTF-8", "UTF-16LE", "HEX", "HEX+UTF-8", "HEX+UTF-16LE", "HEX+UTF-8+UTF-16LE"}[v1]) ? 1 << v1 + 4 | v : ~(1 << v1 + 4) & v;
            }
            return LuaValue.valueOf(this.copy(v));
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            this.checked = varargs0.optjstring(3, "UTF-8").toUpperCase().replaceAll(" ", "");
            return super.invoke2(varargs0);
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs varargs0) {
            long v = varargs0.checklong(1);
            int v1 = varargs0.checkint(2);
            getAddressUTF.MAX_ITEMS = v1;
            long[] arr_v = new long[v1];
            int[] arr_v1 = new int[v1];
            for(int v2 = 0; v2 < v1; ++v2) {
                arr_v[v2] = ((long)v2) * 4L + v;
                arr_v1[v2] = 4;
            }
            MainService.instance.mDaemonManager.getMemoryItems(Script.this.getSeq(), arr_v1, arr_v);
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getAddressUTF(long address, long length, string mode = UTF-8 | UTF-16LE | HEX | HEX+UTF-8 | HEX+UTF-16LE | HEX+UTF-8+UTF-16LE) -> string";
        }
    }

    final class getAdressJavaString extends ApiFunction {
        public long j;
        public long result;
        public String s;

        static Script access$0(getAdressJavaString script$getAdressJavaString0) {
            return Script.this;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs varargs0) {
            try {
                this.j = varargs0.checklong(1);
                this.result = varargs0.checklong(2) + this.j;
                this.j = this.result;
                ThreadManager.runOnUiThread(new Runnable() {
                    {
                        getAdressJavaString.this = script$getAdressJavaString0;
                    }

                    static getAdressJavaString access$0(android.ext.Script.getAdressJavaString.1 script$getAdressJavaString$10) {
                        return getAdressJavaString.this;
                    }

                    @Override
                    public void run() {
                        getAdressJavaString.this.s = MemoryContentAdapter.longToJava(MainService.instance.mDaemonManager.getMemoryContent(getAdressJavaString.this.j, AddressItem.getTypeForAddress(getAdressJavaString.this.j, true)), 8);
                    }
                });
                return this.s != null ? LuaValue.valueOf(this.s) : LuaValue.NIL;
            }
            catch(LuaError luaError0) {
                throw luaError0;
            }
            catch(Throwable throwable0) {
                return LuaValue.valueOf(Script.toString(throwable0));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getAdressJavaString(string address,string offset) -> string";
        }
    }

    final class getAdressString extends ApiFunction {
        public long j;
        public long result;
        public String s;

        static Script access$0(getAdressString script$getAdressString0) {
            return Script.this;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs varargs0) {
            try {
                this.j = varargs0.checklong(1);
                this.result = varargs0.checklong(2) + this.j;
                this.j = this.result;
                ThreadManager.runOnUiThread(new Runnable() {
                    {
                        getAdressString.this = script$getAdressString0;
                    }

                    static getAdressString access$0(android.ext.Script.getAdressString.1 script$getAdressString$10) {
                        return getAdressString.this;
                    }

                    @Override
                    public void run() {
                        getAdressString.this.s = MemoryContentAdapter.longToJava(MainService.instance.mDaemonManager.getMemoryContent(getAdressString.this.j, AddressItem.getTypeForAddress(getAdressString.this.j, true)), 8);
                    }
                });
                return this.s != null ? LuaValue.valueOf(this.s) : LuaValue.NIL;
            }
            catch(LuaError luaError0) {
                throw luaError0;
            }
            catch(Throwable throwable0) {
                return LuaValue.valueOf(Script.toString(throwable0));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getAdressString(string address,string offset) -> string";
        }
    }

    final class getFile extends DebugFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String file;
            if(this.caller == null) {
                file = "?";
            }
            else if(this.caller.sourceFile != null) {
                file = this.caller.sourceFile;
            }
            else if(this.caller.p == null) {
                file = "???";
            }
            else if(this.caller.p.source != null) {
                file = this.caller.p.source.tojstring();
            }
            else {
                file = "??";
            }
            if(file.startsWith("@")) {
                file = file.substring(1);
            }
            return LuaValue.valueOf(file);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getFile() -> string";
        }
    }

    final class getLine extends DebugFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return this.caller == null || this.caller.p == null || (this.caller.p.lineinfo == null || this.caller.pc < 0 || this.caller.pc >= this.caller.p.lineinfo.length) ? LuaValue.valueOf(-1L) : LuaValue.valueOf(this.caller.p.lineinfo[this.caller.pc]);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getLine() -> int";
        }
    }

    final class getListItems extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            try {
                ArrayList arrayList0 = new ArrayList(MainService.instance.savedList.getCount());
                Object monitor = new Object();
                synchronized(monitor) {
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SavedListAdapter savedList = MainService.instance.savedList;
                                int v = savedList.getCount();
                            label_3:
                                for(int i = 0; i < v; ++i) {
                                    SavedItem savedItem0 = savedList.getItem(i);
                                    if(savedItem0 != null) {
                                        arrayList0.add(savedItem0);
                                    }
                                }
                            }
                            catch(Throwable e) {
                                Log.e("Failed get list items", e);
                                if(true) {
                                    goto label_12;
                                }
                                goto label_3;
                            }
                        label_12:
                            synchronized(monitor) {
                                monitor.notifyAll();
                            }
                        }
                    });
                    Script.waitNotify(monitor);
                }
                Varargs ret = new LuaTable();
                ((LuaTable)ret).presize(arrayList0.size());
                int j = 1;
                for(Object object1: arrayList0) {
                    SavedItem item = (SavedItem)object1;
                    if(item != null) {
                        ((LuaTable)ret).rawset(j, Script.fromSavedItem(item));
                        ++j;
                    }
                }
                return ret;
            }
            catch(LuaError e) {
                throw e;
            }
            catch(Throwable e) {
                Log.e("Failed get list items", e);
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getListItems() -> table || string with error";
        }
    }

    final class getLocale extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(Re.s(0x7F070083).replace("~", ""));  // string:lang_code "~~~en_US~~~"
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getLocale() -> string";
        }
    }

    static final class getRanges extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(Config.usedRanges);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getRanges() -> int";
        }
    }

    final class getRangesList extends ApiFunction {
        static Script access$0(getRangesList script$getRangesList0) {
            return Script.this;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            Pattern pattern7;
            Pattern pattern5;
            Pattern pattern3;
            Pattern pattern1;
            String s4;
            boolean z = false;
            boolean z1 = false;
            long v = 0L;
            String s = "";
            String s1 = "";
            String s2 = "";
            String s3 = "";
            if(varargs0.isnumber(1)) {
                v = varargs0.checklong(1);
                z1 = true;
                s4 = "";
            }
            else if(varargs0.istable(1)) {
                z = true;
                Iterator luaTable$Iterator0 = varargs0.checktable(1).iterator();
                while(luaTable$Iterator0.next()) {
                    if(luaTable$Iterator0.key().isstring()) {
                        String s5 = luaTable$Iterator0.key().checkjstring();
                        LuaValue luaValue0 = luaTable$Iterator0.value();
                        if(luaValue0.isnumber() && s5.equals("address")) {
                            v = luaValue0.checklong();
                            z1 = true;
                        }
                        else if(s5.equals("internalName")) {
                            s = luaValue0.checkjstring();
                        }
                        else if(s5.equals("name")) {
                            s1 = luaValue0.checkjstring();
                        }
                        else if(s5.equals("state")) {
                            s2 = luaValue0.checkjstring();
                        }
                        else if(s5.equals("type")) {
                            s3 = luaValue0.checkjstring();
                        }
                    }
                }
                s4 = s3;
            }
            else {
                if(varargs0.isstring(1)) {
                    s = varargs0.checkjstring(1);
                }
                s4 = "";
            }
            if(s.length() > 0) {
                s = Tools.quoteFilter(s);
            }
            Pattern pattern0 = null;
            if(s.length() > 0) {
                try {
                    pattern0 = Pattern.compile(s);
                }
                catch(Throwable throwable0) {
                    Log.badImplementation(throwable0);
                }
                pattern1 = pattern0;
            }
            else {
                pattern1 = null;
            }
            if(s1.length() > 0) {
                s1 = Tools.quoteFilter(s1);
            }
            Pattern pattern2 = null;
            if(s1.length() > 0) {
                try {
                    pattern2 = Pattern.compile(s1);
                }
                catch(Throwable throwable1) {
                    Log.badImplementation(throwable1);
                }
                pattern3 = pattern2;
            }
            else {
                pattern3 = null;
            }
            if(s2.length() > 0) {
                s2 = Tools.quoteFilter(s2);
            }
            Pattern pattern4 = null;
            if(s2.length() > 0) {
                try {
                    pattern4 = Pattern.compile(s2);
                }
                catch(Throwable throwable2) {
                    Log.badImplementation(throwable2);
                }
                pattern5 = pattern4;
            }
            else {
                pattern5 = null;
            }
            if(s4.length() > 0) {
                s4 = Tools.quoteFilter(s4);
            }
            Pattern pattern6 = null;
            if(s4.length() > 0) {
                try {
                    pattern6 = Pattern.compile(s4);
                }
                catch(Throwable throwable3) {
                    Log.badImplementation(throwable3);
                }
                pattern7 = pattern6;
            }
            else {
                pattern7 = null;
            }
            LuaString luaString0 = LuaString.valueOf("state");
            LuaString luaString1 = LuaString.valueOf("start");
            LuaString luaString2 = LuaString.valueOf("end");
            LuaString luaString3 = LuaString.valueOf("type");
            LuaString luaString4 = LuaString.valueOf("name");
            LuaString luaString5 = LuaString.valueOf("internalName");
            List list0 = RegionList.getList();
            Varargs varargs1 = new LuaTable();
            if(z1) {
                int v1 = 1;
                for(Object object0: list0) {
                    Region regionList$Region0 = (Region)object0;
                    if(regionList$Region0.start <= v && regionList$Region0.end >= v) {
                        LuaTable luaTable0 = new LuaTable(0, 6);
                        luaTable0.rawset(luaString0, LuaValue.valueOf(regionList$Region0.getState().toString()));
                        luaTable0.rawset(luaString1, LuaValue.valueOf(regionList$Region0.start));
                        luaTable0.rawset(luaString2, LuaValue.valueOf(regionList$Region0.end));
                        luaTable0.rawset(luaString3, LuaValue.valueOf(regionList$Region0.getType()));
                        luaTable0.rawset(luaString4, LuaValue.valueOf(regionList$Region0.getName()));
                        luaTable0.rawset(luaString5, LuaValue.valueOf(regionList$Region0.getInternalName()));
                        ((LuaTable)varargs1).set(v1, luaTable0);
                        ++v1;
                    }
                }
                return varargs1;
            }
            if(z) {
                int v2 = 1;
                for(Object object1: RegionList.getList()) {
                    String s6 = ((Region)object1).getInternalName();
                    String s7 = ((Region)object1).getName();
                    String s8 = ((Region)object1).getType();
                    long v3 = ((Region)object1).end;
                    long v4 = ((Region)object1).start;
                    String s9 = ((Region)object1).getState().toString();
                    if((pattern1 == null || pattern1.matcher(s6).find()) && (pattern3 == null || pattern3.matcher(s7).find()) && (pattern5 == null || pattern5.matcher(s9).find()) && (pattern7 == null || pattern7.matcher(s8).find())) {
                        LuaTable luaTable1 = new LuaTable(0, 6);
                        luaTable1.rawset(luaString0, LuaValue.valueOf(s9));
                        luaTable1.rawset(luaString1, LuaValue.valueOf(v4));
                        luaTable1.rawset(luaString2, LuaValue.valueOf(v3));
                        luaTable1.rawset(luaString3, LuaValue.valueOf(s8));
                        luaTable1.rawset(luaString4, LuaValue.valueOf(s7));
                        luaTable1.rawset(luaString5, LuaValue.valueOf(s6));
                        ((LuaTable)varargs1).set(v2, luaTable1);
                        ++v2;
                    }
                }
                return varargs1;
            }
            int v5 = 1;
            for(Object object2: list0) {
                Region regionList$Region1 = (Region)object2;
                String s10 = regionList$Region1.getInternalName();
                if(pattern1 == null || pattern1.matcher(s10).find()) {
                    LuaTable luaTable2 = new LuaTable(0, 6);
                    luaTable2.rawset(luaString0, LuaValue.valueOf(regionList$Region1.getState().toString()));
                    luaTable2.rawset(luaString1, LuaValue.valueOf(regionList$Region1.start));
                    luaTable2.rawset(luaString2, LuaValue.valueOf(regionList$Region1.end));
                    luaTable2.rawset(luaString3, LuaValue.valueOf(regionList$Region1.getType()));
                    luaTable2.rawset(luaString4, LuaValue.valueOf(regionList$Region1.getName()));
                    luaTable2.rawset(luaString5, LuaValue.valueOf(s10));
                    ((LuaTable)varargs1).set(v5, luaTable2);
                    ++v5;
                }
            }
            return varargs1;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getRangesList({long address,string internalName,string name,string state,string type}) -> table";
        }
    }

    final class getResults extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 9;
        }

        @Override  // android.ext.Script$ApiFunction
        protected Varargs getTrueResult() {
            Script.this.resultsLoaded = true;
            Varargs arr = new LuaTable();
            ArrayList arrayList0 = new ArrayList(MainService.instance.mAddressList.size());
            Object monitor = new Object();
            synchronized(monitor) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayListResults list = MainService.instance.mAddressList;
                        ArrayList it = arrayList0;
                        int j = 0;
                        while(j < list.size()) {
                            try {
                                AddressItem addressItem0 = list.get(j, null);
                                if(addressItem0 != null) {
                                    it.add(addressItem0);
                                }
                                ++j;
                                continue;
                            }
                            catch(IndexOutOfBoundsException unused_ex) {
                            }
                            ++j;
                        }
                        synchronized(monitor) {
                            monitor.notifyAll();
                        }
                    }
                });
                Script.waitNotify(monitor);
            }
            ((LuaTable)arr).presize(arrayList0.size());
            int i = 1;
            for(Object object1: arrayList0) {
                AddressItem item = (AddressItem)object1;
                if(item != null) {
                    ((LuaTable)arr).rawset(i, Script.fromItem(null, item));
                    ++i;
                }
            }
            return arr;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            int v = args.checkint(1);
            int v1 = args.optint(2, 0);
            try {
                Filters converter$Filters0 = Converter.getFilters(args.optlong(3, 0L), args.optlong(4, -1L), args.optjstring(5, null), args.optjstring(6, null), args.optint(7, 0), args.optjstring(8, null), args.optint(9, 0));
                Converter.getResultList(Script.this.getSeq(), v, v1, converter$Filters0);
                return null;
            }
            catch(LuaError e) {
                throw e;
            }
            catch(Throwable e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 7: {
                    return arg.isint() ? getResults.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 7, arg);
                }
                case 9: {
                    return arg.isint() ? getResults.logConst(log, Script.this.consts.POINTER_, arg) : super.logArg(log, 9, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getResults(int maxCount [, int skip = 0 [, long addressMin = nil [, long addressMax = nil [, string valueMin = nil [, string valueMax = nil [, int type = nil [, string fractional = nil [, int pointer = nil]]]]]]]]) -> table || string with error";
        }
    }

    static final class getResultsCount extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(MainService.instance.mResultCount);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getResultsCount() -> long";
        }
    }

    final class getSelectedElements extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MemoryContentAdapter memAdapter = MainService.instance.mMemListAdapter;
            boolean[] arr_z = memAdapter.getChecks();
            Varargs ret = new LuaTable();
            int i = 1;
            int j = 0;
            while(j < arr_z.length) {
                try {
                    if(arr_z[j]) {
                        goto label_9;
                    }
                }
                catch(IndexOutOfBoundsException unused_ex) {
                }
                goto label_13;
                try {
                label_9:
                    ((LuaTable)ret).rawset(i, LuaLong.valueOf(memAdapter.getAddr(j)));
                }
                catch(IndexOutOfBoundsException unused_ex) {
                }
                ++i;
            label_13:
                ++j;
            }
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getSelectedElements() -> table || string with error";
        }
    }

    final class getSelectedListItems extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            LongSparseArrayChecked longSparseArrayChecked0 = MainService.instance.savedList.getList();
            Varargs ret = new LuaTable();
            int i = 1;
            for(int j = 0; j < longSparseArrayChecked0.size(); ++j) {
                try {
                    if(longSparseArrayChecked0.checkAt(j)) {
                        SavedItem item = (SavedItem)longSparseArrayChecked0.valueAt(j);
                        if(item != null) {
                            try {
                                ((LuaTable)ret).rawset(i, Script.fromSavedItem(item));
                            }
                            catch(IndexOutOfBoundsException unused_ex) {
                            }
                            ++i;
                        }
                    }
                }
                catch(IndexOutOfBoundsException unused_ex) {
                }
            }
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getSelectedListItems() -> table || string with error";
        }
    }

    final class getSelectedResults extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            ArrayListResults list = MainService.instance.mAddressList;
            Varargs ret = new LuaTable();
            int i = 1;
            for(int j = 0; j < list.size(); ++j) {
                try {
                    if(list.checked(j)) {
                        AddressItem addressItem0 = list.get(j, null);
                        if(addressItem0 != null) {
                            try {
                                ((LuaTable)ret).rawset(i, Script.fromItem(null, addressItem0));
                            }
                            catch(IndexOutOfBoundsException unused_ex) {
                            }
                            ++i;
                        }
                    }
                }
                catch(IndexOutOfBoundsException unused_ex) {
                }
            }
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getSelectedResults() -> table || string with error";
        }
    }

    static final class getSpeed extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(MainService.instance.getSpeed());
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getSpeed() -> double";
        }
    }

    static final class getTargetInfo extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable item;
            int i;
            String pkg;
            Varargs ret;
            try {
                ret = LuaValue.NIL;
                ProcessInfo pi = MainService.instance.processInfo;
                if(pi == null) {
                    return ret;
                }
                pkg = pi.packageName;
                PackageInfo packageInfo0 = Tools.getPackageInfo(pkg, 1);
                if(packageInfo0 == null) {
                    return ret;
                }
                LuaTable tbl = new LuaTable();
                tbl.rawset("packageName", pi.packageName);
                tbl.rawset("cmdLine", pi.cmdline);
                tbl.rawset("name", pi.name);
                tbl.rawset("nativeLibraryDir", pi.libsPath);
                tbl.rawset("pid", pi.pid);
                tbl.rawset("uid", pi.uid);
                tbl.rawset("x64", (pi.x64 ? LuaValue.TRUE : LuaValue.FALSE));
                tbl.rawset("RSS", pi.rss);
                tbl.rawset("firstInstallTime", ((double)packageInfo0.firstInstallTime));
                tbl.rawset("lastUpdateTime", ((double)packageInfo0.lastUpdateTime));
                if(packageInfo0.packageName != null) {
                    tbl.rawset("packageName", packageInfo0.packageName);
                }
                if(packageInfo0.sharedUserId != null) {
                    tbl.rawset("sharedUserId", packageInfo0.sharedUserId);
                }
                tbl.rawset("sharedUserLabel", packageInfo0.sharedUserLabel);
                tbl.rawset("versionCode", packageInfo0.versionCode);
                if(packageInfo0.versionName != null) {
                    tbl.rawset("versionName", packageInfo0.versionName);
                }
                PackageManager packageManager0 = Tools.getPackageManager();
                if(packageInfo0.activities != null && packageManager0 != null) {
                    LuaTable activities = new LuaTable();
                    ActivityInfo[] arr_activityInfo = packageInfo0.activities;
                    int v = 0;
                    int i = 1;
                    while(true) {
                        if(v >= arr_activityInfo.length) {
                            tbl.rawset("activities", activities);
                            break;
                        }
                        ActivityInfo ai = arr_activityInfo[v];
                        if(ai == null) {
                            i = i;
                        }
                        else {
                            try {
                                item = new LuaTable();
                                if(ai.name != null) {
                                    item.set("name", ai.name);
                                }
                                item.set("label", ai.loadLabel(packageManager0).toString());
                                i = i + 1;
                            }
                            catch(Throwable e) {
                                i = i;
                                goto label_51;
                            }
                            try {
                                activities.set(i, item);
                                goto label_52;
                            }
                            catch(Throwable e) {
                            }
                        label_51:
                            Log.w("Failed get activity info", e);
                        }
                    label_52:
                        ++v;
                        i = i;
                    }
                }
                if(packageManager0 != null) {
                    try {
                        tbl.rawset("installer", packageManager0.getInstallerPackageName(pkg));
                    }
                    catch(Throwable e) {
                        Log.w("Failed get installer", e);
                    }
                    try {
                        tbl.rawset("enabledSetting", packageManager0.getApplicationEnabledSetting(pkg));
                    }
                    catch(Throwable e) {
                        Log.w("Failed get enabledSetting", e);
                    }
                }
                ApplicationInfo ai = packageInfo0.applicationInfo;
                if(ai != null) {
                    if(ai.backupAgentName != null) {
                        tbl.rawset("backupAgentName", ai.backupAgentName);
                    }
                    if(ai.className != null) {
                        tbl.rawset("className", ai.className);
                    }
                    if(ai.dataDir != null) {
                        tbl.rawset("dataDir", ai.dataDir);
                    }
                    tbl.rawset("descriptionRes", ai.descriptionRes);
                    tbl.rawset("flags", ai.flags);
                    tbl.rawset("icon", ai.icon);
                    tbl.rawset("labelRes", ai.labelRes);
                    tbl.rawset("logo", ai.logo);
                    if(ai.manageSpaceActivityName != null) {
                        tbl.rawset("manageSpaceActivityName", ai.manageSpaceActivityName);
                    }
                    if(ai.name != null) {
                        tbl.rawset("name", ai.name);
                    }
                    if(ai.nativeLibraryDir != null) {
                        tbl.rawset("nativeLibraryDir", ai.nativeLibraryDir);
                    }
                    if(ai.packageName != null) {
                        tbl.rawset("packageName", ai.packageName);
                    }
                    if(ai.permission != null) {
                        tbl.rawset("permission", ai.permission);
                    }
                    if(ai.processName != null) {
                        tbl.rawset("processName", ai.processName);
                    }
                    if(ai.publicSourceDir != null) {
                        tbl.rawset("publicSourceDir", ai.publicSourceDir);
                    }
                    if(ai.sourceDir != null) {
                        tbl.rawset("sourceDir", ai.sourceDir);
                    }
                    tbl.rawset("targetSdkVersion", 26);
                    if(ai.taskAffinity != null) {
                        tbl.rawset("taskAffinity", ai.taskAffinity);
                    }
                    tbl.rawset("theme", ai.theme);
                    tbl.rawset("uid", ai.uid);
                    String s1 = Tools.getApplicationLabel(ai);
                    if(s1 != null) {
                        tbl.rawset("label", s1);
                    }
                }
                return tbl;
            }
            catch(PackageManager.NameNotFoundException e) {
                Log.w(("Pkg not found: " + pkg), e);
                return ret;
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getTargetInfo() -> table || nil";
        }
    }

    static final class getTargetPackage extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String pkg = null;
            ProcessInfo pi = MainService.instance.processInfo;
            if(pi != null) {
                pkg = pi.packageName;
            }
            return pkg == null ? LuaValue.NIL : LuaValue.valueOf(pkg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getTargetPackage() -> string || nil";
        }
    }

    static final class getUTF extends ApiFunction {
        private static final int HEX = 0;
        private static final int HEX_TEXT = 6;
        private static final int HEX_UTF16LE = 8;
        private static final int HEX_UTF8 = 7;
        private static final int HEX_UTF8_UTF16LE = 9;
        private static final int MAX_ITEMS = 0x7FFFFFF5;
        private static final int RHEX = 1;
        private static final int UTF16LE = 5;
        private static final int UTF8 = 4;

        private void add(Bytes copySelected$Bytes0, StringBuilder stringBuilder0, AddressItem addressItem0, int v) {
            if(copySelected$Bytes0 != null) {
                if(copySelected$Bytes0.nextAddr == 0L) {
                    copySelected$Bytes0.nextAddr = addressItem0.address;
                }
                int v1 = addressItem0.getSize();
                long v2 = addressItem0.data;
                if(copySelected$Bytes0.bytes.length <= copySelected$Bytes0.len + v1) {
                    byte[] arr_b = new byte[copySelected$Bytes0.len + v1];
                    System.arraycopy(copySelected$Bytes0.bytes, 0, arr_b, 0, copySelected$Bytes0.bytes.length);
                    copySelected$Bytes0.bytes = arr_b;
                }
                for(int v3 = 0; v3 < v1; ++v3) {
                    long v4 = addressItem0.address + ((long)v3);
                    if(copySelected$Bytes0.nextAddr <= v4) {
                        copySelected$Bytes0.bytes[copySelected$Bytes0.len] = (byte)(((int)(0xFFL & v2)));
                        ++copySelected$Bytes0.len;
                        copySelected$Bytes0.nextAddr = v4;
                    }
                    v2 >>= 8;
                }
                return;
            }
            if(stringBuilder0.length() != 0) {
                stringBuilder0.append(';');
            }
            if((v & 2) != 0) {
                stringBuilder0.append("0000000000000000");
                stringBuilder0.append('r');
            }
            else if((v & 1) == 0) {
                stringBuilder0.append(addressItem0.getStringDataTrim());
            }
            else {
                stringBuilder0.append("0000000000000000");
                stringBuilder0.append('h');
            }
            if((v & 4) != 0) {
                stringBuilder0.append(addressItem0.getShortName());
            }
        }

        public static LongSparseArrayChecked cast(Object object0) [...] // Inlined contents

        String copy(int v, Object object0) {
            long v14;
            long v11;
            long v10;
            int v9;
            int v8;
            AddressItem addressItem1;
            StringBuilder stringBuilder0 = new StringBuilder();
            int v1 = 0;
            int v2 = 0;
            long v3 = 0L;
            long v4 = 0L;
            int v5 = Integer.numberOfTrailingZeros(v & 0x3F0);
            Bytes copySelected$Bytes0 = v5 == 0x20 ? null : new Bytes();
            if(object0 instanceof ArrayListResults) {
                AddressItem addressItem0 = new AddressItem();
                int v6 = ((ArrayListResults)object0).size();
                int v7 = 0;
                while(v7 < v6) {
                    if(((ArrayListResults)object0).checked(v7)) {
                        if(v1 < 0x7FFFFFF5) {
                            addressItem1 = addressItem0;
                            ((ArrayListResults)object0).get(v7, addressItem1);
                            this.add(copySelected$Bytes0, stringBuilder0, addressItem1, v);
                            v4 = addressItem1.address;
                            if(v1 == 0) {
                                v3 = v4;
                            }
                            ++v1;
                        }
                        else {
                            addressItem1 = addressItem0;
                        }
                        ++v2;
                        if(v2 > 0x7FFFFFF5) {
                            break;
                        }
                    }
                    else {
                        addressItem1 = addressItem0;
                    }
                    ++v7;
                    addressItem0 = addressItem1;
                }
                v8 = v1;
                v9 = v2;
                v10 = v3;
                v11 = v4;
            }
            else if(object0 instanceof LongSparseArrayChecked) {
                int v12 = ((LongSparseArrayChecked)object0).size();
                int v13 = 0;
                while(v13 < v12) {
                    if(((LongSparseArrayChecked)object0).checkAt(v13)) {
                        SavedItem savedItem0 = (SavedItem)((LongSparseArrayChecked)object0).valueAt(v13);
                        if(savedItem0 == null) {
                            v14 = v3;
                            goto label_54;
                        }
                        else {
                            if(v1 < 0x7FFFFFF5) {
                                this.add(copySelected$Bytes0, stringBuilder0, savedItem0, v);
                                v4 = savedItem0.address;
                                if(v1 == 0) {
                                    v3 = v4;
                                }
                                ++v1;
                            }
                            ++v2;
                            if(v2 > 0x7FFFFFF5) {
                                break;
                            }
                            goto label_55;
                        }
                        goto label_53;
                    }
                    else {
                    label_53:
                        v14 = v3;
                    }
                label_54:
                    v3 = v14;
                label_55:
                    ++v13;
                }
                v8 = v1;
                v9 = v2;
                v10 = v3;
                v11 = v4;
            }
            else if(object0 instanceof boolean[]) {
                MemoryContentAdapter memoryContentAdapter0 = MainService.instance.mMemListAdapter;
                int v15 = ((boolean[])object0).length - 1;
                for(int v16 = 1; v16 < v15; ++v16) {
                    if(((boolean[])object0)[v16]) {
                        long v17 = v3;
                        if(v1 < 0x7FFFFFF5) {
                            AddressItem addressItem2 = (AddressItem)memoryContentAdapter0.getItem(v16);
                            this.add(copySelected$Bytes0, stringBuilder0, addressItem2, v);
                            v4 = addressItem2.address;
                            if(v1 == 0) {
                                v17 = v4;
                            }
                            ++v1;
                        }
                        v3 = v17;
                        ++v2;
                        if(v2 <= 0x7FFFFFF5) {
                            continue;
                        }
                        break;
                    }
                }
                goto label_116;
            }
            else {
                if(object0 instanceof LuaTable) {
                    AddressItem addressItem3 = new AddressItem();
                    int v18 = ((LuaTable)object0).length();
                    int v19 = 1;
                    while(v19 <= v18) {
                        if(v1 < 0x7FFFFFF5) {
                            LuaTable luaTable0 = (LuaTable)((LuaTable)object0).get(v19);
                            long v20 = v3;
                            addressItem3.address = luaTable0.get("address").checklong();
                            addressItem3.data = luaTable0.get("value").checklong();
                            addressItem3.flags = luaTable0.get("flags").checkint();
                            this.add(copySelected$Bytes0, stringBuilder0, addressItem3, v);
                            long v21 = addressItem3.address;
                            if(v1 == 0) {
                                v20 = v21;
                            }
                            ++v1;
                            v4 = v21;
                            v3 = v20;
                        }
                        ++v2;
                        if(v2 > 0x7FFFFFF5) {
                            v8 = v1;
                            v9 = v2;
                            v10 = v3;
                            v11 = v4;
                            goto label_120;
                        }
                        ++v19;
                    }
                    v10 = v3;
                    v8 = v1;
                    v9 = v2;
                }
                else {
                label_116:
                    v8 = v1;
                    v9 = v2;
                    v10 = v3;
                }
                v11 = v4;
            }
        label_120:
            if(v8 >= (copySelected$Bytes0 == null ? 2 : 1)) {
                if(v9 > v8) {
                    Tools.showToast(Tools.stringFormat(Re.s(0x7F070167), new Object[]{v8}));  // string:used_only_first "Used only first __d__ items."
                }
                int v22 = 58;
                if(copySelected$Bytes0 != null) {
                    if(v5 != 4 && v5 != 5) {
                        if(v5 == 6) {
                            stringBuilder0.append('h');
                            char[] arr_c = HexText.hexArray;
                            byte[] arr_b = copySelected$Bytes0.bytes;
                            int v23 = copySelected$Bytes0.len;
                            for(int v24 = 0; v24 < v23; ++v24) {
                                stringBuilder0.append(' ');
                                int v25 = arr_b[v24];
                                stringBuilder0.append(arr_c[(v25 & 0xF0) >> 4]);
                                stringBuilder0.append(arr_c[v25 & 15]);
                            }
                            return stringBuilder0.toString();
                        }
                        stringBuilder0.append('Q');
                        stringBuilder0.append(' ');
                        HexText.getText(stringBuilder0, 0, copySelected$Bytes0.bytes, copySelected$Bytes0.len, v5 == 7 || v5 == 9, v5 == 8 || v5 == 9, null);
                        return stringBuilder0.toString();
                    }
                    if(v5 == 5) {
                        v22 = 59;
                    }
                    stringBuilder0.append(((char)v22));
                    stringBuilder0.append(new String(copySelected$Bytes0.bytes, 0, copySelected$Bytes0.len, ParserNumbers.getCharset(v5 == 5)));
                    return stringBuilder0.toString();
                }
                if((v & 8) != 0) {
                    stringBuilder0.append(':');
                    stringBuilder0.append(v11 + 1L - v10);
                }
            }
            return stringBuilder0.toString();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            ArrayListResults arrayListResults0;
            String s = varargs0.optjstring(1, "UTF-8").toUpperCase().replaceAll(" ", "");
            int v = 0;
            for(int v1 = 0; true; ++v1) {
                arrayListResults0 = null;
                if(v1 >= 6) {
                    break;
                }
                v = s.equals(new String[]{"UTF-8", "UTF-16LE", "HEX", "HEX+UTF-8", "HEX+UTF-16LE", "HEX+UTF-8+UTF-16LE"}[v1]) ? 1 << v1 + 4 | v : ~(1 << v1 + 4) & v;
            }
            if(varargs0.istable(2)) {
                LuaTable luaTable0 = varargs0.checktable(2);
                if(luaTable0.length() < 1) {
                    return LuaValue.valueOf("当前传递的地址列表为空");
                }
                return luaTable0.get(1).get("value").isnil() || luaTable0.get(1).get("address").isnil() || luaTable0.get(1).get("flags").isnil() ? LuaValue.valueOf("当前传递的地址列表格式异常") : LuaValue.valueOf(this.copy(v, luaTable0));
            }
            if(varargs0.isnumber(2)) {
                switch(varargs0.optint(2, 1)) {
                    case 1: {
                        arrayListResults0 = MainService.instance.mAddressList;
                        return arrayListResults0 == null ? LuaValue.valueOf("当前搜索列表、保存列表、地址修改页三者之中均无选中项") : LuaValue.valueOf(this.copy(v, arrayListResults0));
                    }
                    case 2: {
                        if(MainService.instance.savedList != null) {
                            arrayListResults0 = MainService.instance.savedList.getList();
                            return arrayListResults0 == null ? LuaValue.valueOf("当前搜索列表、保存列表、地址修改页三者之中均无选中项") : LuaValue.valueOf(this.copy(v, arrayListResults0));
                        }
                        break;
                    }
                    case 3: {
                        if(MainService.instance.mMemListAdapter != null) {
                            arrayListResults0 = MainService.instance.mMemListAdapter.getChecks();
                            return arrayListResults0 == null ? LuaValue.valueOf("当前搜索列表、保存列表、地址修改页三者之中均无选中项") : LuaValue.valueOf(this.copy(v, arrayListResults0));
                        }
                        break;
                    }
                    default: {
                        arrayListResults0 = MainService.instance.mAddressList;
                        return arrayListResults0 == null ? LuaValue.valueOf("当前搜索列表、保存列表、地址修改页三者之中均无选中项") : LuaValue.valueOf(this.copy(v, arrayListResults0));
                    }
                }
            }
            else {
                arrayListResults0 = MainService.instance.mAddressList;
            }
            return arrayListResults0 == null ? LuaValue.valueOf("当前搜索列表、保存列表、地址修改页三者之中均无选中项") : LuaValue.valueOf(this.copy(v, arrayListResults0));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getUTF(string mode = UTF-8 | UTF-16LE | HEX | HEX+UTF-8 | HEX+UTF-16LE | HEX+UTF-8+UTF-16LE [, table addressItem| int selected]) -> string";
        }
    }

    final class getValues extends BusyApiFunction {
        long[] addrs;
        int[] flags;
        final ArrayList keys;
        int nums;

        getValues() {
            this.keys = new ArrayList();
            this.addrs = null;
            this.flags = null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        protected Varargs getTrueResult() {
            ArrayList keys = this.keys;
            Varargs arr = new LuaTable();
            int i = 0;
            ((LuaTable)arr).presize(this.nums);
            ArrayList memList = Script.memList;
            if(memList == null) {
                Script.memListUse += 100000;
                ExceptionHandler.sendException(Thread.currentThread(), new RuntimeException("memList is null " + Script.memListUse), false);
            }
            else {
                ++Script.memListUse;
                Script.memList = null;
                for(Object object0: memList) {
                    ((LuaTable)arr).rawset(((LuaValue)keys.get(i)), Script.fromItem(null, ((AddressItem)object0)));
                    ++i;
                }
            }
            this.flags = null;
            this.addrs = null;
            keys.clear();
            keys.trimToSize();
            return arr;
        }

        public LuaTable init(LuaTable luaTable0) {
            LuaTable luaTable1 = new LuaTable();
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(luaTable$Iterator0.next()) {
                LuaValue luaValue0 = luaTable$Iterator0.key();
                LuaTable luaTable2 = new LuaTable();
                LuaValue luaValue1 = luaTable$Iterator0.value();
                LuaValue luaValue2 = luaValue1.rawget(1);
                if(luaValue2.isnil()) {
                    luaValue2 = luaValue1.rawget("address");
                }
                luaTable2.rawset("address", luaValue2);
                LuaValue luaValue3 = luaValue1.rawget(2);
                if(luaValue3.isnil()) {
                    luaValue3 = luaValue1.rawget("flags");
                }
                luaTable2.rawset("flags", luaValue3);
                LuaValue luaValue4 = luaValue1.rawget(3);
                if(luaValue4.isnil()) {
                    luaValue4 = luaValue1.rawget("value");
                }
                luaTable2.rawset("value", luaValue4);
                luaTable1.rawset(luaValue0, luaTable2);
            }
            return luaTable1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable luaTable0 = this.init(args.checktable(1));
            ArrayList keys = this.keys;
            this.flags = null;
            this.addrs = null;
            long[] addresses = new long[luaTable0.getArrayLength() + luaTable0.getHashEntries()];
            int[] flagses = new int[addresses.length];
            int used = 0;
            keys.clear();
            keys.ensureCapacity(addresses.length);
            int nums = 0;
            AddressItem addressItem0 = new AddressItem(0L, 0L, 4);
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            int inc = 0;
            while(luaTable$Iterator0.next()) {
                if(luaTable$Iterator0.intkey() != 0) {
                    ++nums;
                }
                LuaValue luaValue0 = luaTable$Iterator0.key();
                Script.toItem(addressItem0, luaTable$Iterator0.value().checktable(), 2);
                if(used == addresses.length) {
                    ++inc;
                    int add = used >> 1 >= 10 ? used >> 1 : 10;
                    ExceptionHandler.sendException(Thread.currentThread(), new RuntimeException("OF " + inc + "; " + addresses.length + "; " + luaTable0.getArrayLength() + " + " + luaTable0.getHashEntries() + "; " + luaValue0.isint() + ": " + luaValue0 + "; " + luaTable0.getmetatable() + "; " + luaTable0), false);
                    addresses = Arrays.copyOf(addresses, used + add);
                    flagses = Arrays.copyOf(flagses, used + add);
                }
                addresses[used] = addressItem0.address;
                flagses[used] = addressItem0.flags & 0x7F;
                keys.add(luaValue0);
                ++used;
            }
            this.nums = nums;
            if(flagses.length != used) {
                flagses = Arrays.copyOf(flagses, used);
            }
            this.flags = flagses;
            if(addresses.length != used) {
                addresses = Arrays.copyOf(addresses, used);
            }
            this.addrs = addresses;
            if(used == 0) {
                this.flags = null;
                this.addrs = null;
                return new LuaTable();
            }
            return super.invoke2(args);
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.mDaemonManager.getMemoryItems(Script.this.getSeq(), this.flags, this.addrs);
            this.flags = null;
            this.addrs = null;
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getValues(table values) -> table || string with error";
        }
    }

    final class getValuesRange extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable luaTable0 = args.checktable(1);
            Varargs ret = new LuaTable();
            ((LuaTable)ret).presize(luaTable0.getArrayLength(), ((LuaTable)ret).getHashLength());
            LuaString address = Script.inds[0];
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(luaTable$Iterator0.next()) {
                LuaValue luaValue0 = luaTable$Iterator0.value();
                long v = luaValue0.istable() ? Script.checklong(luaValue0.checktable(), address) : luaValue0.checklong();
                int v1 = luaTable$Iterator0.intkey();
                if(v1 == 0) {
                    ((LuaTable)ret).rawset(luaTable$Iterator0.key(), LuaValue.valueOf(RegionList.getRegion(v).toString()));
                }
                else {
                    ((LuaTable)ret).rawset(v1, LuaValue.valueOf(RegionList.getRegion(v).toString()));
                }
            }
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.getValuesRange(table values) -> table || string with error";
        }
    }

    final class goURL extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private String url;

        goURL() {
            super();
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            this.url = args.checkjstring(1);
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog0 = Alert.create().setTitle("网页权限").setMessage(Re.s(("是否打开链接：" + goURL.this.url))).setPositiveButton(Re.s(0x7F07009B), goURL.this).setNegativeButton(Re.s(0x7F07009C), null).create();  // string:yes "Yes"
                        Alert.setOnDismissListener(alertDialog0, goURL.this);
                        Alert.show(alertDialog0);
                    }
                });
                Script.waitNotify(this);
                return LuaString.NIL;
            }
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(which != -1) {
                return;
            }
            MainService service = MainService.instance;
            if(service.mainDialog != null) {
                service.dismissDialog();
            }
            service.showApp.hideUi();
            try {
                Tools.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.url)));
            }
            catch(Throwable e) {
                Log.w("Failed go url", e);
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface d) {
            synchronized(this) {
                this.notify();
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.goURL(string url) -> nil";
        }
    }

    static final class gotoAddress extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.goToAddress(args.checklong(1));
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.gotoAddress(long address) -> nil";
        }
    }

    final class hideUiButton extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.setScriptUiButton(false);
            return hideUiButton.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.hideUiButton() -> nil";
        }
    }

    final class intent extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private String url;

        intent() {
            super();
        }

        static String access$0(intent script$intent0) {
            return script$intent0.url;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            this.url = args.checkjstring(1);
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog0 = Alert.create().setTitle("意图操作").setMessage("将执行意图操作").setPositiveButton(Re.s(0x7F070218), intent.this).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:execute "Execute"
                        Alert.setOnDismissListener(alertDialog0, intent.this);
                        Alert.show(alertDialog0);
                    }
                });
                Script.waitNotify(this);
                return LuaString.NIL;
            }
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(which != -1) {
                return;
            }
            MainService service = MainService.instance;
            if(service.mainDialog != null) {
                service.dismissDialog();
            }
            service.showApp.hideUi();
            try {
                Tools.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.url)));
            }
            catch(Throwable e) {
                Log.w("Failed go url", e);
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface d) {
            synchronized(this) {
                this.notify();
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.intent(string url) -> nil";
        }
    }

    final class internal1 extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                int v = 2;
                Script.this.resultsLoaded = false;
                LuaString luaString0 = args.checkstring(1);
                byte[] bytes = new byte[luaString0.m_length];
                luaString0.copyInto(0, bytes, 0, luaString0.m_length);
                if(args.optlong(4, 1L) != 2L) {
                    v = 1;
                }
                return SearchButtonListener.doSearch(Script.this.getSeq(), v, bytes, args.optlong(2, 0L), args.optlong(3, -1L), ((byte)false)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "do not use";
        }
    }

    final class internal2 extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaClosure luaClosure0 = args.checkclosure(1);
            String s = args.checkjstring(2);
            Varargs ret = LuaValue.TRUE;
            PrintStream prev = Print.ps;
            try {
                Print.ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(s), 0x10000));
                Print.print(luaClosure0.p);
                Print.ps.close();
                Print.saveTail(luaClosure0, s + ".tail");
            }
            catch(Throwable e) {
                Log.e(("Failed dump " + luaClosure0 + " into \'" + s + '\''), e);
                ret = LuaValue.valueOf(Script.toString(e));
            }
            Print.ps = prev;
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "do not use";
        }
    }

    final class internal3 extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                Script.this.resultsLoaded = false;
                int maxOffset = (short)args.checkint(1);
                return SearchButtonListener.doSearch(Script.this.getSeq(), ((short)maxOffset), args.optlong(2, 0L), args.optlong(3, -1L)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "do not use";
        }
    }

    static final class isClickedUiButton extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            MainService service = MainService.instance;
            Varargs ret = isClickedUiButton.NIL;
            if(service.scriptUiButton != null) {
                if(service.scriptUiButtonClicked) {
                    service.scriptUiButtonClicked = false;
                    return isClickedUiButton.TRUE;
                }
                return isClickedUiButton.FALSE;
            }
            return ret;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.isClickedUiButton() -> bool || nil";
        }
    }

    final class isHTTPdump extends ApiFunction {
        isHTTPdump() {
            super();
        }

        private void addHeaders(String s, LuaTable luaTable0, HttpURLConnection httpURLConnection0) {
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(luaTable$Iterator0.next()) {
                httpURLConnection0.addRequestProperty(s, luaTable$Iterator0.value().checkjstring());
            }
        }

        private LuaTable getHeaders(HttpURLConnection httpURLConnection0) {
            LuaTable luaTable0 = new LuaTable();
            for(Object object0: httpURLConnection0.getHeaderFields().entrySet()) {
                String s = (String)((Map.Entry)object0).getKey();
                if(s == null) {
                    s = "null";
                }
                List list0 = (List)((Map.Entry)object0).getValue();
                LuaTable luaTable1 = new LuaTable();
                int v = 1;
                luaTable1.presize(list0.size());
                for(Object object1: list0) {
                    luaTable1.rawset(v, ((String)object1));
                    ++v;
                }
                luaTable0.rawset(s, luaTable1);
            }
            return luaTable0;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            Varargs varargs1 = this.request("https://vpn.sisu.edu.cn", new LuaTable(), null);
            if(varargs1.arg1().istable()) {
                return LuaValue.FALSE;
            }
            return varargs1.checkjstring(1).indexOf("SSL") >= 0 ? LuaValue.TRUE : LuaValue.valueOf("error");
        }

        public Varargs request(String s, LuaTable luaTable0, LuaString luaString0) {
            InputStream inputStream0;
            try {
                UsageStats.callInit = true;
                Varargs varargs0 = new LuaTable();
                HttpURLConnection httpURLConnection0 = (HttpURLConnection)new URL(s).openConnection();
                Iterator luaTable$Iterator0 = luaTable0.iterator();
                while(luaTable$Iterator0.next()) {
                    LuaValue luaValue0 = luaTable$Iterator0.key();
                    LuaValue luaValue1 = luaTable$Iterator0.value();
                    if(luaValue1 instanceof LuaTable) {
                        this.addHeaders(luaValue0.checkjstring(), luaValue1.checktable(), httpURLConnection0);
                    }
                    else {
                        httpURLConnection0.addRequestProperty(luaValue0.checkjstring(), luaValue1.checkjstring());
                    }
                }
                if(luaString0 == null) {
                    httpURLConnection0.connect();
                }
                else {
                    httpURLConnection0.setDoOutput(true);
                    httpURLConnection0.setRequestMethod("POST");
                    int v = luaString0.m_length;
                    if(httpURLConnection0.getRequestProperty("Content-Length") == null) {
                        httpURLConnection0.addRequestProperty("Content-Length", Integer.toString(v));
                    }
                    if(httpURLConnection0.getRequestProperty("Content-Type") == null) {
                        httpURLConnection0.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    }
                    httpURLConnection0.connect();
                    OutputStream outputStream0 = httpURLConnection0.getOutputStream();
                    outputStream0.write(luaString0.m_bytes, luaString0.m_offset, v);
                    luaString0 = null;
                    outputStream0.flush();
                }
                ((LuaTable)varargs0).rawset("url", s);
                ((LuaTable)varargs0).rawset("requestMethod", httpURLConnection0.getRequestMethod());
                ((LuaTable)varargs0).rawset("code", httpURLConnection0.getResponseCode());
                ((LuaTable)varargs0).rawset("message", httpURLConnection0.getResponseMessage());
                ((LuaTable)varargs0).rawset("headers", this.getHeaders(httpURLConnection0));
                ((LuaTable)varargs0).rawset("contentEncoding", httpURLConnection0.getContentEncoding());
                ((LuaTable)varargs0).rawset("contentLength", httpURLConnection0.getContentLength());
                ((LuaTable)varargs0).rawset("contentType", httpURLConnection0.getContentType());
                ((LuaTable)varargs0).rawset("date", ((double)httpURLConnection0.getDate()));
                ((LuaTable)varargs0).rawset("expiration", ((double)httpURLConnection0.getExpiration()));
                ((LuaTable)varargs0).rawset("lastModified", ((double)httpURLConnection0.getLastModified()));
                ((LuaTable)varargs0).rawset("usingProxy", (httpURLConnection0.usingProxy() ? LuaValue.TRUE : LuaValue.FALSE));
                if(httpURLConnection0 instanceof HttpsURLConnection) {
                    ((LuaTable)varargs0).rawset("cipherSuite", ((HttpsURLConnection)httpURLConnection0).getCipherSuite());
                }
                try {
                    inputStream0 = httpURLConnection0.getInputStream();
                }
                catch(IOException unused_ex) {
                    InputStream inputStream1 = httpURLConnection0.getErrorStream();
                    ((LuaTable)varargs0).rawset("error", LuaValue.TRUE);
                    inputStream0 = inputStream1;
                }
                finally {
                    httpURLConnection0.disconnect();
                }
                BufferedInputStream bufferedInputStream0 = new BufferedInputStream(inputStream0);
                ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
                byte[] arr_b = new byte[0x2000];
                while(true) {
                    int v2 = bufferedInputStream0.read(arr_b);
                    if(v2 == -1) {
                        ((LuaTable)varargs0).set("content", LuaValue.valueOf(byteArrayOutputStream0.toByteArray()));
                        return varargs0;
                    }
                    byteArrayOutputStream0.write(arr_b, 0, v2);
                }
            }
            catch(Throwable throwable0) {
                Log.w(("isHTTPdump fail for \'" + s + "\'; data = " + luaString0 + "; headers = " + luaTable0), throwable0);
                return LuaValue.valueOf(Script.toString(throwable0));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.isHTTPdump() -> false || true || error";
        }
    }

    static final class isPackageInstalled extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(Tools.isPackageInstalled(args.checkjstring(1)));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.isPackageInstalled(string pkg) -> bool";
        }
    }

    static final class isProcessPaused extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(MainService.instance.processPaused);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.isProcessPaused() -> bool";
        }
    }

    static final class isVisible extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return MainService.instance.mainDialog == null ? LuaValue.valueOf(false) : LuaValue.valueOf(true);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.isVisible() -> bool";
        }
    }

    final class loadList extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            java.io.File file0 = new java.io.File(args.checkjstring(1));
            if(!file0.exists()) {
                return LuaValue.valueOf(("File \'" + file0.getAbsolutePath() + "\' not found"));
            }
            if(!file0.isFile()) {
                return LuaValue.valueOf(("Path \'" + file0.getAbsolutePath() + "\' is not a file"));
            }
            if(!file0.canRead()) {
                return LuaValue.valueOf(("File \'" + file0.getAbsolutePath() + "\' can not be read"));
            }
            int v = args.optint(2, 0);
            try {
                ListManager.loadList(MainService.instance.processInfo.pid, file0.getAbsolutePath(), v | 4);
                return LuaValue.TRUE;
            }
            catch(Throwable e) {
                Log.e(("Failed load list: " + file0.getAbsolutePath() + "; " + v), e);
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            if(i != 2) {
                return super.logArg(log, i, arg);
            }
            return arg.isint() ? loadList.logConst(log, Script.this.consts.LOAD_, arg) : super.logArg(log, 2, arg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.loadList(string file [, int flags = 0]) -> true || string with error";
        }
    }

    final class loadResults extends BusyApiFunction {
        ArrayList items;

        loadResults() {
            this.items = null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            Script.this.resultsLoaded = false;
            ArrayList arrayList0 = Script.getItems(args);
            MainService.instance.clear(Script.this.getSeq());
            if(arrayList0.size() == 0) {
                return LuaValue.TRUE;
            }
            this.items = arrayList0;
            return super.invoke2(args);
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                MainService.instance.mDaemonManager.loadResults(Script.this.getSeq(), this.items);
                this.items = null;
                return null;
            }
            catch(Throwable throwable0) {
                this.items = null;
                throw throwable0;
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.loadResults(table results) -> true || string with error";
        }
    }

    final class makeRequest extends ApiFunction {
        private void addHeaders(String s, LuaTable luaTable0, HttpURLConnection httpURLConnection0) {
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(luaTable$Iterator0.next()) {
                httpURLConnection0.addRequestProperty(s, luaTable$Iterator0.value().checkjstring());
            }
        }

        private LuaTable getHeaders(HttpURLConnection httpURLConnection0) {
            LuaTable luaTable0 = new LuaTable();
            for(Object object0: httpURLConnection0.getHeaderFields().entrySet()) {
                String s = (String)((Map.Entry)object0).getKey();
                if(s == null) {
                    s = "null";
                }
                List list0 = (List)((Map.Entry)object0).getValue();
                LuaTable luaTable1 = new LuaTable();
                int v = 1;
                luaTable1.presize(list0.size());
                for(Object object1: list0) {
                    luaTable1.rawset(v, ((String)object1));
                    ++v;
                }
                luaTable0.rawset(s, luaTable1);
            }
            try {
                httpURLConnection0.getResponseCode();
                luaTable0.rawset("Location", httpURLConnection0.getURL().toString());
            }
            catch(Exception unused_ex) {
            }
            return luaTable0;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            String s = varargs0.checkjstring(1);
            LuaTable luaTable0 = varargs0.opttable(2, null);
            if(luaTable0 == null) {
                luaTable0 = new LuaTable();
            }
            return this.request(s, luaTable0, varargs0.optstring(3, null));
        }

        public Varargs request(String s, LuaTable luaTable0, LuaString luaString0) {
            InputStream inputStream0;
            try {
                UsageStats.callInit = true;
                Varargs varargs0 = new LuaTable();
                HttpURLConnection httpURLConnection0 = (HttpURLConnection)new URL(s).openConnection();
                Iterator luaTable$Iterator0 = luaTable0.iterator();
                while(luaTable$Iterator0.next()) {
                    LuaValue luaValue0 = luaTable$Iterator0.key();
                    LuaValue luaValue1 = luaTable$Iterator0.value();
                    if(luaValue1 instanceof LuaTable) {
                        this.addHeaders(luaValue0.checkjstring(), luaValue1.checktable(), httpURLConnection0);
                    }
                    else {
                        httpURLConnection0.addRequestProperty(luaValue0.checkjstring(), luaValue1.checkjstring());
                    }
                }
                if(luaString0 == null) {
                    httpURLConnection0.connect();
                }
                else {
                    httpURLConnection0.setDoOutput(true);
                    httpURLConnection0.setRequestMethod("POST");
                    int v = luaString0.m_length;
                    if(httpURLConnection0.getRequestProperty("Content-Length") == null) {
                        httpURLConnection0.addRequestProperty("Content-Length", Integer.toString(v));
                    }
                    if(httpURLConnection0.getRequestProperty("Content-Type") == null) {
                        httpURLConnection0.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    }
                    httpURLConnection0.connect();
                    OutputStream outputStream0 = httpURLConnection0.getOutputStream();
                    outputStream0.write(luaString0.m_bytes, luaString0.m_offset, v);
                    luaString0 = null;
                    outputStream0.flush();
                }
                ((LuaTable)varargs0).rawset("url", s);
                ((LuaTable)varargs0).rawset("requestMethod", httpURLConnection0.getRequestMethod());
                ((LuaTable)varargs0).rawset("code", httpURLConnection0.getResponseCode());
                ((LuaTable)varargs0).rawset("message", httpURLConnection0.getResponseMessage());
                ((LuaTable)varargs0).rawset("headers", this.getHeaders(httpURLConnection0));
                ((LuaTable)varargs0).rawset("contentEncoding", httpURLConnection0.getContentEncoding());
                ((LuaTable)varargs0).rawset("contentLength", httpURLConnection0.getContentLength());
                ((LuaTable)varargs0).rawset("contentType", httpURLConnection0.getContentType());
                ((LuaTable)varargs0).rawset("date", ((double)httpURLConnection0.getDate()));
                ((LuaTable)varargs0).rawset("expiration", ((double)httpURLConnection0.getExpiration()));
                ((LuaTable)varargs0).rawset("lastModified", ((double)httpURLConnection0.getLastModified()));
                ((LuaTable)varargs0).rawset("usingProxy", (httpURLConnection0.usingProxy() ? LuaValue.TRUE : LuaValue.FALSE));
                if(httpURLConnection0 instanceof HttpsURLConnection) {
                    ((LuaTable)varargs0).rawset("cipherSuite", ((HttpsURLConnection)httpURLConnection0).getCipherSuite());
                }
                try {
                    inputStream0 = httpURLConnection0.getInputStream();
                }
                catch(IOException unused_ex) {
                    InputStream inputStream1 = httpURLConnection0.getErrorStream();
                    ((LuaTable)varargs0).rawset("error", LuaValue.TRUE);
                    inputStream0 = inputStream1;
                }
                finally {
                    httpURLConnection0.disconnect();
                }
                BufferedInputStream bufferedInputStream0 = new BufferedInputStream(inputStream0);
                ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
                byte[] arr_b = new byte[0x2000];
                while(true) {
                    int v2 = bufferedInputStream0.read(arr_b);
                    if(v2 == -1) {
                        ((LuaTable)varargs0).set("content", LuaValue.valueOf(byteArrayOutputStream0.toByteArray()));
                        return varargs0;
                    }
                    byteArrayOutputStream0.write(arr_b, 0, v2);
                }
            }
            catch(Throwable throwable0) {
                Log.w(("makeRequest fail for \'" + s + "\'; data = " + luaString0 + "; headers = " + luaTable0), throwable0);
                return LuaValue.valueOf(Script.toString(throwable0));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.makeRequest(string url [, table headers = {} [, string data = nil]]) -> table || string";
        }
    }

    static final class multiChoice extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnMultiChoiceClickListener {
        volatile boolean[] result;
        private volatile boolean save;

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable luaTable0 = args.checktable(1);
            LuaTable luaTable1 = args.opttable(2, null);
            ArrayList keys = new ArrayList();
            ArrayList labels = new ArrayList();
            ArrayList checked = new ArrayList();
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(luaTable$Iterator0.next()) {
                LuaValue luaValue0 = luaTable$Iterator0.key();
                LuaValue luaValue1 = luaTable$Iterator0.value();
                CharSequence charSequence0 = Build.VERSION.SDK_INT < 11 ? Tools.colorize(Re.s(luaValue1.checkjstring()), -1) : Re.s(luaValue1.checkjstring());
                labels.add(charSequence0);
                checked.add(Boolean.valueOf(luaTable1 != null && Script.optboolean(luaTable1, luaValue0, false)));
                keys.add(luaValue0);
            }
            this.result = Tools.booleanArray(checked);
            this.save = false;
            String s = Re.s(args.optjstring(3, ""));
            CharSequence[] items = (CharSequence[])labels.toArray(new CharSequence[labels.size()]);
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(null, (s.length() <= 0 ? null : "\n" + s))).setPositiveButton(Re.s(0x7F07009D), multiChoice.this).setNegativeButton(Re.s(0x7F0700A1), multiChoice.this).setMultiChoiceItems(items, multiChoice.this.result, multiChoice.this).create();  // string:ok "OK"
                        Alert.setOnDismissListener(alertDialog0, multiChoice.this);
                        Alert.show(alertDialog0);
                        MainService.instance.eye(true);
                    }
                });
                Script.waitNotify(this);
            }
            if(!this.save) {
                return LuaValue.NIL;
            }
            Varargs ret = new LuaTable();
            for(int i = 0; i < keys.size(); ++i) {
                if(this.result[i]) {
                    ((LuaTable)ret).rawset(((LuaValue)keys.get(i)), LuaValue.TRUE);
                }
            }
            return ret;
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                this.save = true;
            }
        }

        @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            this.result[which] = isChecked;
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            synchronized(this) {
                this.notify();
            }
            MainService.instance.eye(false);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.multiChoice(table items [, table selection = {} [, string message = nil]]) -> table || nil";
        }
    }

    final class numberFromLocale extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(Script.numberToLua(args.tojstring(1)));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.numberFromLocale(string num) -> string";
        }
    }

    final class numberToLocale extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return LuaValue.valueOf(Script.numberFromLua(args.tojstring(1)));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.numberToLocale(string num) -> string";
        }
    }

    final class offsetString extends ApiFunction {
        public long j;
        public long result;
        public String s;

        static Script access$0(offsetString script$offsetString0) {
            return Script.this;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs varargs0) {
            try {
                this.j = varargs0.checklong(1);
                this.result = varargs0.checklong(2) + this.j;
                this.j = this.result;
                ThreadManager.runOnUiThread(new Runnable() {
                    {
                        offsetString.this = script$offsetString0;
                    }

                    static offsetString access$0(android.ext.Script.offsetString.1 script$offsetString$10) {
                        return offsetString.this;
                    }

                    @Override
                    public void run() {
                        offsetString.this.s = MemoryContentAdapter.longToJava(MainService.instance.mDaemonManager.getMemoryContent(offsetString.this.j, AddressItem.getTypeForAddress(offsetString.this.j, true)), 8);
                    }
                });
                return this.s != null ? LuaValue.valueOf(this.s) : LuaValue.NIL;
            }
            catch(LuaError luaError0) {
                throw luaError0;
            }
            catch(Throwable throwable0) {
                return LuaValue.valueOf(Script.toString(throwable0));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.offsetString(string address,string offset) -> string";
        }
    }

    final class processKill extends ApiFunction {
        private long lastUsed;
        private int used;

        processKill() {
            this.used = 0;
            this.lastUsed = 0L;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            long v = System.nanoTime();
            if(v / 60000000000L != this.lastUsed) {
                this.lastUsed = v / 60000000000L;
                this.used = 0;
            }
            int v1 = this.used + 1;
            this.used = v1;
            if(v1 > 4) {
                Script.this.interrupt(3);
            }
            return LuaValue.valueOf(MainService.instance.processKill(Script.this.getSeq()));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.processKill() -> bool";
        }
    }

    final class processPause extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            return LuaValue.valueOf(MainService.instance.processPause(Script.this.getSeq(), true));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.processPause() -> bool";
        }
    }

    final class processResume extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            return LuaValue.valueOf(MainService.instance.processResume(Script.this.getSeq()));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.processResume() -> bool";
        }
    }

    final class processToggle extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            return LuaValue.valueOf(MainService.instance.processToggle(Script.this.getSeq(), true));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.processToggle() -> bool";
        }
    }

    final class prompt extends ApiFunction implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, View.OnFocusChangeListener {
        volatile ArrayList edits;
        private volatile int result;
        private volatile LuaTable ret;

        prompt() {
            this.result = 0;
            this.ret = null;
            this.edits = null;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            LuaTable luaTable0 = args.checktable(1);
            LuaTable luaTable1 = args.opttable(2, null);
            LuaTable luaTable2 = args.opttable(3, null);
            this.result = 0;
            ArrayList arrayList0 = new ArrayList();
            this.edits = arrayList0;
            this.ret = new LuaTable();
            synchronized(this) {
                ThreadManager.runOnUiThread(new Runnable() {
                    public float dec;

                    public String getInit(float f) {
                        return this.dec == 1.0f ? Integer.toString(((int)f)) : Float.toString(f);
                    }

                    public int getMax(float f, float f1, float f2) [...] // Inlined contents

                    public float getMultiple(String s, String s1) {
                        int v = s.length();
                        int v1 = s.indexOf(46);
                        float f = (float)Math.pow(10.0, v - (v1 == -1 ? v : v1 + 1));
                        int v2 = s1.length();
                        int v3 = s1.indexOf(46);
                        float f1 = (float)Math.pow(10.0, v2 - (v3 == -1 ? v2 : v3 + 1));
                        return f > f1 ? f : f1;
                    }

                    public int getProgress(float f, float f1, float f2) [...] // Inlined contents

                    @Override
                    public void run() {
                        float val;
                        View view0 = LayoutInflater.inflateStatic(0x7F040013, null);  // layout:prompt
                        ViewGroup body = (ViewGroup)view0.findViewById(0x7F0B005A);  // id:prompt
                        EditText first = null;
                        EditText last = null;
                        Iterator luaTable$Iterator0 = luaTable0.iterator();
                        while(luaTable$Iterator0.next()) {
                            LuaValue luaValue0 = luaTable$Iterator0.key();
                            String prompt = Re.s(luaTable$Iterator0.value().tojstring().trim());
                            LuaValue defVal = luaTable1 == null ? prompt.NIL : luaTable1.get(luaValue0);
                            LuaValue type = luaTable2 == null ? prompt.NIL : luaTable2.get(luaValue0);
                            View view1 = LayoutInflater.inflateStatic(0x7F040014, null);  // layout:prompt_item
                            TextView text = (TextView)view1.findViewById(0x7F0B005A);  // id:prompt
                            CheckBox check = (CheckBox)view1.findViewById(0x7F0B003A);  // id:check
                            EditText edit = (EditText)view1.findViewById(0x7F0B005B);  // id:edit
                            EditTextPath path = (EditTextPath)view1.findViewById(0x7F0B000A);  // id:path
                            String s1 = type.tojstring();
                            View view2 = view1.findViewById(0x7F0B000B);  // id:path_selector
                            view2.setTag(path);
                            View view3 = view1.findViewById(0x7F0B0042);  // id:number_converter
                            view3.setTag(edit);
                            TextView seek = (TextView)view1.findViewById(0x7F0B004F);  // id:seek
                            SeekBar seekBar = (SeekBar)view1.findViewById(0x7F0B005C);  // id:seekbar
                            boolean useSeekBar = false;
                            int intType = 0;
                            if(s1.equalsIgnoreCase("checkbox")) {
                                prompt.this.remove(text);
                                prompt.this.remove(edit);
                                prompt.this.remove(path);
                                prompt.this.remove(view2);
                                prompt.this.remove(view3);
                                check.setText(prompt);
                                check.setTag(luaValue0);
                                if(!defVal.isnil()) {
                                    check.setChecked(defVal.toboolean());
                                }
                                arrayList0.add(check);
                            }
                            else if(s1.equalsIgnoreCase("text2")) {
                                prompt.this.remove(edit);
                                prompt.this.remove(path);
                                prompt.this.remove(view2);
                                prompt.this.remove(view3);
                                prompt.this.remove(check);
                                check.setTag(luaValue0);
                                arrayList0.add(check);
                            }
                            else {
                                prompt.this.remove(check);
                                String strVal = defVal.isnil() ? "" : defVal.tojstring();
                                if(s1.equalsIgnoreCase("number")) {
                                    try {
                                        strVal = Script.numberFromLua(strVal);
                                        if(prompt.indexOf(91) > 0 && prompt.indexOf(59) > 0 && prompt.charAt(prompt.length() - 1) == 93) {
                                            Matcher matcher0 = Pattern.compile("\\s*\\[(.+);(.+)\\]$").matcher(prompt);
                                            if(matcher0.find()) {
                                                String s3 = Script.numberToLua(Script.numberFromLua(matcher0.group(1).trim()));
                                                String s4 = Script.numberToLua(Script.numberFromLua(matcher0.group(2).trim()));
                                                float f = Float.parseFloat(s3);
                                                float f1 = Float.parseFloat(s4);
                                                strVal = Script.numberToLua(strVal.trim());
                                                try {
                                                    val = Float.parseFloat(strVal);
                                                }
                                                catch(Throwable e) {
                                                    Log.d(("Failed parse default value: " + strVal), e);
                                                    val = 0.0f;
                                                }
                                                if(f < f1) {
                                                    if(val < f) {
                                                        val = f;
                                                    }
                                                    if(val > f1) {
                                                        val = f1;
                                                    }
                                                    float f3 = this.getMultiple(s3, s4);
                                                    this.dec = f3;
                                                    seek.setText(this.getInit(val));
                                                    seek.setTag(luaValue0);
                                                    seekBar.setMax(((int)((f1 - f) * f3)));
                                                    seekBar.setProgress(((int)((val - f) * f3)));
                                                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                        private final float val$dec;

                                                        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                                                        public void onProgressChanged(SeekBar seekBar0, int v, boolean z) {
                                                            if(this.val$dec == 1.0f) {
                                                                seek.setText(Integer.toString(((int)f) + v));
                                                                return;
                                                            }
                                                            seek.setText(Float.toString((f * this.val$dec + ((float)v)) / this.val$dec));
                                                        }

                                                        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                                                        public void onStartTrackingTouch(SeekBar seekBar0) {
                                                        }

                                                        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                                                        public void onStopTrackingTouch(SeekBar seekBar0) {
                                                        }
                                                    });
                                                    prompt = prompt.substring(0, prompt.length() - matcher0.group(0).length());
                                                    arrayList0.add(seek);
                                                    prompt.this.remove(edit);
                                                    prompt.this.remove(view3);
                                                    useSeekBar = true;
                                                }
                                            }
                                        }
                                    }
                                    catch(Throwable e) {
                                        Log.d("Failed use seekbar", e);
                                    }
                                    if(!useSeekBar) {
                                        intType = 1;
                                    }
                                }
                                else if(s1.equalsIgnoreCase("text")) {
                                    intType = 2;
                                }
                                else if(s1.equalsIgnoreCase("path")) {
                                    intType = 4;
                                    path.setPathType(0);
                                }
                                else if(s1.equalsIgnoreCase("file")) {
                                    intType = 4;
                                    path.setPathType(1);
                                }
                                else if(s1.equalsIgnoreCase("new_file")) {
                                    intType = 4;
                                    path.setPathType(2);
                                }
                                else if(s1.equalsIgnoreCase("setting")) {
                                    intType = 8;
                                    strVal = Script.numberFromLua(strVal);
                                }
                                else if(s1.equalsIgnoreCase("speed")) {
                                    intType = 16;
                                    strVal = Script.numberFromLua(strVal);
                                }
                                if(intType != 0 && intType != 1) {
                                    prompt.this.remove(view3);
                                }
                                try {
                                    if(new java.io.File(Tools.getPackageManager().getApplicationInfo(Tools.getPackageName(), 0).sourceDir).lastModified() < System.currentTimeMillis() - 0x4E58D91L && !MainService.instance.statusBar.getText().toString().startsWith(Re.s(0x7F070000) + ' ' + MainService.instance.mDaemonManager.getStatus())) {  // string:version_number "96.0"
                                        ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Script.this.globals.rawget("gg").rawset("searchNumber", prompt.NIL);
                                                }
                                                catch(Throwable unused_ex) {
                                                }
                                            }
                                        }, ((long)(Tools.RANDOM.nextInt(5000) + 5000)));
                                    }
                                }
                                catch(Throwable unused_ex) {
                                }
                                if(intType == 4) {
                                    prompt.this.remove(edit);
                                }
                                else {
                                    prompt.this.remove(path);
                                    prompt.this.remove(view2);
                                }
                                if(!useSeekBar) {
                                    EditText ed = intType == 4 ? path : edit;
                                    ed.setTag(luaValue0);
                                    Tools.setOnFocusChangeListener(ed, prompt.this);
                                    if(intType > 0 && ed instanceof EditTextExt) {
                                        ((EditTextExt)ed).setDataType(intType);
                                    }
                                    ed.setText(strVal);
                                    arrayList0.add(ed);
                                    if(first == null) {
                                        first = ed;
                                    }
                                    last = ed;
                                }
                            }
                            if(!useSeekBar) {
                                prompt.this.remove(seek);
                                prompt.this.remove(seekBar);
                            }
                            text.setText(prompt);
                            body.addView(view1);
                        }
                        AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView2(view0)).setPositiveButton(Re.s(0x7F07009D), prompt.this).setNegativeButton(Re.s(0x7F0700A1), prompt.this).create();  // string:ok "OK"
                        Alert.setOnDismissListener(alertDialog0, prompt.this);
                        Alert.show(alertDialog0, last);
                        MainService.instance.eye(true);
                        if(first != null) {
                            first.requestFocus();
                        }
                    }
                });
                Script.waitNotify(this);
            }
            this.edits = null;
            Varargs out = this.ret;
            this.ret = null;
            return this.result == 0 ? LuaValue.NIL : out;
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface d, int which) {
            if(which == -1) {
                this.result = 1;
                ArrayList editsLocal = this.edits;
                LuaTable out = this.ret;
                if(editsLocal != null && out != null) {
                    for(Object object0: editsLocal) {
                        TextView item = (TextView)object0;
                        if(item != null) {
                            LuaValue key = (LuaValue)item.getTag();
                            if(key == null) {
                            }
                            else if(item instanceof EditText) {
                                String text = item.getText().toString();
                                int v1 = item instanceof EditTextExt ? ((EditTextExt)item).getDataType() : -1;
                                if(v1 == 1 || v1 == 8 || v1 == 16) {
                                    text = Script.numberToLua(text);
                                }
                                out.rawset(key, LuaValue.valueOf(text));
                                History.add(text, v1);
                            }
                            else if(item instanceof CheckBox) {
                                out.rawset(key, LuaValue.valueOf(((CheckBox)item).isChecked()));
                            }
                            else if(item instanceof TextView) {
                                String s1 = item.getText().toString();
                                out.rawset(key, LuaValue.valueOf(s1));
                                History.add(s1, 1);
                            }
                        }
                    }
                }
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            synchronized(this) {
                this.notify();
            }
            MainService.instance.eye(false);
        }

        @Override  // android.view.View$OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if((Config.configClient & 1) != 0 && hasFocus) {
                InternalKeyboard kbdView = (InternalKeyboard)v.getRootView().findViewById(0x7F0B002A);  // id:keyboard
                if(kbdView != null && v instanceof EditTextExt) {
                    boolean needExternal = false;
                    switch(((EditTextExt)v).getDataType()) {
                        case 2: 
                        case 4: {
                            needExternal = true;
                        }
                    }
                    kbdView.needExternal(v, needExternal);
                    if(!needExternal) {
                        Tools.hideKeyboard(v);
                    }
                }
            }
        }

        private void remove(View v) {
            v.setVisibility(8);
            Tools.getViewForAttach(v);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.prompt(table prompts [, table defaults = {} [, table types = {} ]]) -> nil || table with keys from prompts and values from inputs";
        }
    }

    final class refineAddress extends searchAddress {
    }

    final class refineNumber extends searchNumber {
    }

    final class removeListItems extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            try {
                LuaTable luaTable0 = args.checktable(1);
                luaTable0.rawlen();
                long[] list = new long[luaTable0.getArrayLength() + luaTable0.getHashEntries()];
                LuaString address = Script.inds[0];
                Iterator luaTable$Iterator0 = luaTable0.iterator();
                for(int used = 0; true; ++used) {
                    if(!luaTable$Iterator0.next()) {
                        Object monitor = new Object();
                        synchronized(monitor) {
                            ThreadManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        MainService.instance.savedList.remove(list, used);
                                    }
                                    catch(Throwable e) {
                                        Log.e("Failed remove list items", e);
                                    }
                                    synchronized(monitor) {
                                        monitor.notifyAll();
                                    }
                                }
                            });
                            Script.waitNotify(monitor);
                            return LuaValue.TRUE;
                        }
                    }
                    LuaValue luaValue0 = luaTable$Iterator0.value();
                    list[used] = luaValue0.istable() ? Script.checklong(((LuaTable)luaValue0), address) : luaValue0.checklong();
                }
            }
            catch(LuaError e) {
                throw e;
            }
            catch(Throwable e) {
                Log.e("Failed remove list items", e);
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.removeListItems(table items) -> true || string with error";
        }
    }

    final class removeResults extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            Script.this.resultsLoaded = false;
            ArrayList arrayList0 = Script.getItems(args);
            MainService.instance.mDaemonManager.remove(Script.this.getSeq(), arrayList0);
            Object monitor = new Object();
            synchronized(monitor) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainService.instance.refreshResultList(false);
                        synchronized(monitor) {
                            monitor.notifyAll();
                        }
                    }
                });
                Script.waitNotify(monitor);
                return LuaValue.TRUE;
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.removeResults(table results) -> true || string with error";
        }
    }

    final class require extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            String version = args.optjstring(1, "0");
            int v = args.optint(2, 0);
            int ver = Debug.getIntVersion(version);
            if(ver > 80000) {
                ver -= 80000;
            }
            String have = 96.0f;
            if(Debug.getIntVersion() < ver || 0x3E79 < v) {
                if(v > 0) {
                    version = version + " (" + v + ')';
                    have = 0x42C00000 + " (" + 0x3E79 + ')';
                }
                Script.this.globals.STDOUT.println(Tools.stringFormat(Re.s(0x7F07023C), new Object[]{version, have}));  // string:need_update_to "You need a newer version of __app_name__ to run this script. 
                                                                                                                        // At least version __s__. You have version __s__."
                throw new OsExit(0);
            }
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.require([string version = nil [, int build = 0]]) -> nil";
        }
    }

    final class saveList extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            java.io.File file0 = new java.io.File(args.checkjstring(1));
            if(file0.isDirectory()) {
                return LuaValue.valueOf(("Path \'" + file0.getAbsolutePath() + "\' is a directory"));
            }
            int v = args.optint(2, 0);
            MainService service = MainService.instance;
            try {
                SavedListAdapter savedList = service.savedList;
                int v1 = savedList.getCount();
                SavedItem[] list = new SavedItem[v1];
                for(int i = 0; true; ++i) {
                    if(i >= v1) {
                        ListManager.saveList(service.processInfo.pid, list, file0.getAbsolutePath(), v);
                        return LuaValue.TRUE;
                    }
                    list[i] = savedList.getItem(i);
                }
            }
            catch(Throwable e) {
                Log.e(("Failed saved list: " + file0.getAbsolutePath() + "; " + v), e);
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            if(i != 2) {
                return super.logArg(log, i, arg);
            }
            return arg.isint() ? saveList.logConst(log, Script.this.consts.SAVE_, arg) : super.logArg(log, 2, arg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.saveList(string file [, int flags = 0]) -> true || string with error";
        }
    }

    final class saveVariable extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            return new luaj.lib.GgLib.saveVariable(Script.this.globals).invoke(args);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.saveVariable(mixed variable, string filename) -> true || string with error";
        }
    }

    class searchAddress extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected final int getMaxArgs() {
            return 6;
        }

        @Override  // android.ext.Script$ApiFunction
        public final Varargs invokeUi(Varargs args) {
            Script.this.resultsLoaded = false;
            if(this instanceof refineAddress && MainService.instance.mResultCount == 0L) {
                return searchAddress.TRUE;
            }
            try {
                return MaskButtonListener.doSearch(Script.this.getSeq(), Script.numberFromLua(args.checkjstring(1)), args.optlong(2, -1L), Script.checkType(args.optint(3, 0x7F)), Script.checkSign(args.optint(4, 0x20000000)), args.optlong(5, 0L), args.optlong(6, -1L), ((byte)false)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected final boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 3: {
                    return arg.isint() ? searchAddress.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 3, arg);
                }
                case 4: {
                    return arg.isint() ? searchAddress.logConst(log, Script.this.consts.SIGN_, arg) : super.logArg(log, 4, arg);
                }
                case 2: 
                case 5: 
                case 6: {
                    return arg.islong() ? searchAddress.logHex(log, arg) : super.logArg(log, i, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        // 去混淆评级： 低(30)
        @Override  // android.ext.Script$ApiFunction
        final String usage() {
            return this instanceof refineAddress ? "gg.refineAddress(string text [, long mask = -1 [, int type = gg.TYPE_AUTO [, int sign = gg.SIGN_EQUAL [, long memoryFrom = 0 [, long memoryTo = -1]]]]]) -> true || string with error" : "gg.searchAddress(string text [, long mask = -1 [, int type = gg.TYPE_AUTO [, int sign = gg.SIGN_EQUAL [, long memoryFrom = 0 [, long memoryTo = -1]]]]]) -> true || string with error";
        }
    }

    final class searchFuzzy extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 5;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            int v1;
            int v;
            try {
                Script.this.resultsLoaded = false;
                if((args.optint(2, 0) & 0x7F) == 0) {
                    v = Script.checkType(args.optint(3, 0x7F));
                    v1 = Script.checkSign(args.optint(2, 0x20000000));
                }
                else {
                    v = Script.checkType(args.optint(2, 0x7F));
                    v1 = Script.checkSign(args.optint(3, 0x20000000));
                }
                return FuzzyButtonListener.doRefine(Script.this.getSeq(), Script.numberFromLua(args.optjstring(1, "0")), v, v1, ((byte)1), args.optlong(4, 0L), args.optlong(5, -1L)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 2: {
                    return arg.isint() ? searchFuzzy.logConst(log, Script.this.consts.SIGN_FUZZY_, arg) : super.logArg(log, 2, arg);
                }
                case 3: {
                    return arg.isint() ? searchFuzzy.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 3, arg);
                }
                case 4: 
                case 5: {
                    return arg.islong() ? searchFuzzy.logHex(log, arg) : super.logArg(log, i, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.searchFuzzy([string difference = \'0\' [, int sign = gg.SIGN_FUZZY_EQUAL [, int type = gg.TYPE_AUTO [, long memoryFrom = 0 [, long memoryTo = -1]]]]]) -> true || string with error";
        }
    }

    class searchNumber extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected final int getMaxArgs() {
            return 6;
        }

        @Override  // android.ext.Script$ApiFunction
        public final Varargs invokeUi(Varargs args) {
            Script.this.resultsLoaded = false;
            if(this instanceof refineNumber && MainService.instance.mResultCount == 0L) {
                return searchNumber.TRUE;
            }
            try {
                return SearchButtonListener.doSearch(Script.this.getSeq(), Script.numberFromLua(args.checkjstring(1)), Script.checkType(args.optint(2, 0x7F)), args.optboolean(3, false), Script.checkSign(args.optint(4, 0x20000000)), args.optlong(5, 0L), args.optlong(6, -1L), ((byte)false)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected final boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 2: {
                    return arg.isint() ? searchNumber.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 2, arg);
                }
                case 4: {
                    return arg.isint() ? searchNumber.logConst(log, Script.this.consts.SIGN_, arg) : super.logArg(log, 4, arg);
                }
                case 5: 
                case 6: {
                    return arg.islong() ? searchNumber.logHex(log, arg) : super.logArg(log, i, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        // 去混淆评级： 低(30)
        @Override  // android.ext.Script$ApiFunction
        final String usage() {
            return this instanceof refineNumber ? "gg.refineNumber(string text [, int type = gg.TYPE_AUTO [, bool encrypted = false [, int sign = gg.SIGN_EQUAL [, long memoryFrom = 0 [, long memoryTo = -1]]]]]) -> true || string with error" : "gg.searchNumber(string text [, int type = gg.TYPE_AUTO [, bool encrypted = false [, int sign = gg.SIGN_EQUAL [, long memoryFrom = 0 [, long memoryTo = -1]]]]]) -> true || string with error";
        }
    }

    static final class setConfig extends ApiFunction {
        private static final HashMap back;
        private final int[] ids;
        private final String[] names;
        private static final HashMap ranges;
        private static final HashMap small;
        private static final int[] speedhack;
        private static final HashMap toolbars;
        private static final int[] unrandom;

        static {
            HashMap hashMap0 = new HashMap();
            setConfig.ranges = hashMap0;
            hashMap0.put("a", 0x20);
            hashMap0.put("as", 0x80000);
            hashMap0.put("b", 0x20000);
            hashMap0.put("xa", 0x4000);
            hashMap0.put("xs", 0x8000);
            hashMap0.put("ca", 4);
            hashMap0.put("cb", 16);
            hashMap0.put("cd", 8);
            hashMap0.put("ch", 1);
            hashMap0.put("j", 0x10000);
            hashMap0.put("jh", 2);
            hashMap0.put("o", 0xFFE03F80);
            hashMap0.put("ps", 0x40000);
            hashMap0.put("s", 0x40);
            hashMap0.put("v", 0x100000);
            setConfig.speedhack = new int[]{0, 0xFFE00001, 0xFFE00002, 0xFFE00004, 0xFFE00008, 0xFFE00010, 0xFFE00020, 0xFFE00040, 0xFFE00080, 0xFFE00100, 0xFFE00200, 0xFFE00400, 0xFFE00800, 0xFFE01000, 0xFFE02000, 0xFFE04000, 0xFFE08000, 0xFFE10000, 0xFFE20000, 0xFFE40000, 0xFFE80000, 0xFFF00000};
            setConfig.unrandom = new int[]{0, 0xFFF80001, 0xFFF80002, 0xFFF80004, 0xFFF80008, 0xFFF80010, 0xFFF80020, 0xFFF80040, 0xFFF80080, 0xFFF80100, 0xFFF80200, 0xFFF80400, 0xFFF80800, 0xFFF81000, 0xFFF82000, 0xFFF84000, 0xFFF88000, 0xFFF90000, 0xFFFA0000, 0xFFFC0000};
            HashMap hashMap1 = new HashMap();
            setConfig.small = hashMap1;
            hashMap1.put("什么也不设置", 0);
            hashMap1.put("搜索竖屏", 1);
            hashMap1.put("搜索横屏", 2);
            hashMap1.put("保存列表竖屏", 4);
            hashMap1.put("保存列表横屏", 8);
            hashMap1.put("内存编辑器竖屏", 16);
            hashMap1.put("内存编辑器横屏", 0x20);
            HashMap hashMap2 = new HashMap();
            setConfig.back = hashMap2;
            hashMap2.put("什么也不设置", 0);
            hashMap2.put("搜索", 1);
            hashMap2.put("保存列表", 2);
            hashMap2.put("内存编辑器", 4);
            HashMap hashMap3 = new HashMap();
            setConfig.toolbars = hashMap3;
            hashMap3.put("搜索页竖屏", 0xFFFFFFC1);
            hashMap3.put("搜索页横屏", -62);
            hashMap3.put("保存列表竖屏", -60);
            hashMap3.put("保存列表横屏", -56);
            hashMap3.put("内存编辑器竖屏", 0xFFFFFFD0);
            hashMap3.put("内存编辑器横屏", 0xFFFFFFE0);
            hashMap3.put("以上全选", 0xFFFFFFE0);
        }

        setConfig() {
            this.ids = new int[]{0x7F0B0081, 0x7F0B0082, 0x7F0B0084, 0x7F0B0085, 0x7F0B0087, 0x7F0B0088, 0x7F0B0089, 0x7F0B008A, 0x7F0B008B, 0x7F0B008C, 0x7F0B008D, 0x7F0B008E, 0x7F0B0090, 0x7F0B0091, 0x7F0B0092, 0x7F0B0093, 0x7F0B0094, 0x7F0B0095, 0x7F0B0096, 0x7F0B0097, 0x7F0B009A, 0x7F0B009B, 0x7F0B009C, 0x7F0B009D, 0x7F0B009E, 0x7F0B009F, 0x7F0B00A0, 0x7F0B00A1, 0x7F0B00A2, 0x7F0B00A3, 0x7F0B00A4, 0x7F0B00A5, 0x7F0B00A6, 0x7F0B00A8, 0x7F0B00A9, 0x7F0B00AE, 0x7F0B00AF, 0x7F0B00B1};  // id:config_ranges
            this.names = new String[]{"选择内存范围", "变速:拦截", "时间跳跃面板", "反随机数生成器", "对游戏隐藏", "旁路模式", "跳过内存区域", "快速冻结", "冻结间隔", "自动暂停游戏", "搜索助手", "保存列表更新间隔", "变速:排序并删除重复项", "变速和反随机数退出时重置", "检测游戏库架构", "使用内存缓冲", "内存访问", "深度读取", "系统调用模式", "waitpid模式", "在虚拟空间中使用root", "运行守护", "使用通知", "热键", "历史记录条数", "内外键盘", "允许输入法提示", "忽略未知字符", "状态栏缩进", "可见数据类型", "小列表项目", "黑色背景", "工具栏布局", "工具栏填充方式", "悬浮窗大小", "界面加速", "启用音效", "数字格式"};
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 25;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs varargs0) {
            try {
                int v = 1;
                LuaValue luaValue0 = varargs0.arg(1);
                int v1 = 0;
                int v2 = 0;
                if(luaValue0.isnumber()) {
                    int v3 = luaValue0.checkint();
                    int[] arr_v = this.ids;
                    for(int v4 = 0; v4 < arr_v.length; ++v4) {
                        if(v3 == arr_v[v4]) {
                            v1 = v3;
                            break;
                        }
                    }
                }
                else if(luaValue0 instanceof LuaString) {
                    String s = luaValue0.checkjstring();
                    int v5 = 0;
                    while(v5 < this.ids.length) {
                        if(s.equals(this.names[v5])) {
                            v1 = this.ids[v5];
                            if(true) {
                                break;
                            }
                        }
                        else {
                            ++v5;
                        }
                    }
                }
                if(v1 == 0) {
                    return LuaValue.FALSE;
                }
                int v6 = 0;
                int v7 = varargs0.narg();
                if(v7 == 2) {
                    LuaValue luaValue4 = varargs0.checkvalue(2);
                    String s4 = null;
                    if(luaValue4 instanceof LuaString) {
                        s4 = luaValue4.checkjstring();
                    }
                    else if(luaValue4.isnumber()) {
                        v6 = luaValue4.checkint();
                    }
                    switch(v1) {
                        case 0x7F0B0081: {  // id:config_ranges
                            if(s4 != null) {
                                Integer integer3 = (Integer)setConfig.ranges.get(s4.toLowerCase());
                                v6 = integer3 == null ? 0x20 : ((int)integer3);
                            }
                            break;
                        }
                        case 0x7F0B0082: {  // id:config_speedhack_intercept
                            if(v6 > 0) {
                                int[] arr_v3 = setConfig.speedhack;
                                if(v6 < arr_v3.length) {
                                    v6 = arr_v3[v6];
                                }
                            }
                            break;
                        }
                        case 0x7F0B0085: {  // id:config_unrandomizer_intercept
                            if(v6 > 0) {
                                int[] arr_v4 = setConfig.unrandom;
                                if(v6 < arr_v4.length) {
                                    v6 = arr_v4[v6];
                                }
                            }
                            break;
                        }
                        case 0x7F0B0087: {  // id:config_hide_from_game
                            if(s4 != null) {
                                for(int v17 = 0; v17 < s4.length(); ++v17) {
                                    v6 |= s4.charAt(v17) - 0x30;
                                }
                            }
                            break;
                        }
                        case 0x7F0B0088: {  // id:config_ptrace_bypass_mode
                            if(s4 != null) {
                                switch(s4) {
                                    case "关闭": {
                                        v6 = 3;
                                        break;
                                    }
                                    case "冻结": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "恢复": {
                                        break;
                                    }
                                    case "没有": {
                                        v6 = 1;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0089: {  // id:config_skip_memory
                            if(s4 != null) {
                                switch(s4) {
                                    case "否": {
                                        break;
                                    }
                                    case "空": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "默认": {
                                        v6 = 2;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B008D: {  // id:config_search_helper
                            if(s4 != null) {
                                switch(s4) {
                                    case "关闭": {
                                        break;
                                    }
                                    case "自动切换": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "自动切换&重置搜索": {
                                        v6 = 2;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0090: {  // id:config_speeds_params
                            if(s4 != null) {
                                switch(s4) {
                                    case "排序": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "排序&去重": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "空无一物": {
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0093: {  // id:config_ram
                            if(s4 != null) {
                                switch(s4) {
                                    case "分配10MB": {
                                        v6 = 0x800;
                                        break;
                                    }
                                    case "分配1280MB": {
                                        v6 = 0x40000;
                                        break;
                                    }
                                    case "分配160MB": {
                                        v6 = 0x8000;
                                        break;
                                    }
                                    case "分配20MB": {
                                        v6 = 0x1000;
                                        break;
                                    }
                                    case "分配2560MB": {
                                        v6 = 0x80000;
                                        break;
                                    }
                                    case "分配320MB": {
                                        v6 = 0x10000;
                                        break;
                                    }
                                    case "分配3MB": {
                                        v6 = 0x200;
                                        break;
                                    }
                                    case "分配40MB": {
                                        v6 = 0x2000;
                                        break;
                                    }
                                    case "分配5120MB": {
                                        v6 = 0x100000;
                                        break;
                                    }
                                    case "分配5MB": {
                                        v6 = 0x400;
                                        break;
                                    }
                                    case "分配640MB": {
                                        v6 = 0x20000;
                                        break;
                                    }
                                    case "分配80MB": {
                                        v6 = 0x4000;
                                        break;
                                    }
                                    case "否": {
                                        break;
                                    }
                                    case "是": {
                                        v6 = 0x7FFFFFFF;
                                        break;
                                    }
                                    case "默认": {
                                        v6 = 0x7FFFFFFF;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0094:   // id:config_memory_access
                        case 0x7F0B0096:   // id:config_calls
                        case 0x7F0B0097: {  // id:config_waitpid
                            if(s4 != null) {
                                v6 = true ^ s4.equals("正常");
                            }
                            break;
                        }
                        case 0x7F0B009B: {  // id:config_prevent_unload
                            if(s4 != null) {
                                switch(s4) {
                                    case "否": {
                                        break;
                                    }
                                    case "级别1": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "级别2": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "级别3": {
                                        v6 = 3;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B009F: {  // id:config_keyboard
                            if(s4 != null) {
                                switch(s4) {
                                    case "内": {
                                        v = 0;
                                        break;
                                    }
                                    case "内置": {
                                        break;
                                    }
                                    case "外": {
                                        v = 2;
                                        break;
                                    }
                                    case "外置": {
                                        v = 3;
                                        break;
                                    }
                                    default: {
                                        v = -1;
                                    }
                                }
                                if(v == 0 || v == 1) {
                                    v6 = 1;
                                }
                            }
                            break;
                        }
                        case 0x7F0B00A4: {  // id:config_use_small_list_items
                            if(s4 != null) {
                                Integer integer4 = (Integer)setConfig.small.get(s4);
                                if(integer4 != null) {
                                    v2 = (int)integer4;
                                }
                                v6 = v2;
                            }
                            break;
                        }
                        case 0x7F0B00A5: {  // id:config_backgrounds
                            if(s4 != null) {
                                Integer integer5 = (Integer)setConfig.back.get(s4);
                                if(integer5 != null) {
                                    v2 = (int)integer5;
                                }
                                v6 = v2;
                            }
                            break;
                        }
                        case 0x7F0B00A6: {  // id:config_toolbars
                            if(s4 != null) {
                                Integer integer6 = (Integer)setConfig.toolbars.get(s4);
                                if(integer6 != null) {
                                    v2 = (int)integer6;
                                }
                                v6 = v2;
                            }
                            break;
                        }
                        case 0x7F0B00A8: {  // id:config_fill_toolbar
                            if(s4 != null) {
                                switch(s4) {
                                    case "中": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "从中间": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "从右边": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "从左边": {
                                        break;
                                    }
                                    case "右": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "左": {
                                        break;
                                    }
                                    case "默认": {
                                        v6 = 2;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B00AE: {  // id:config_acceleration
                            if(s4 != null) {
                                switch(s4) {
                                    case "硬": {
                                        v = 0;
                                        break;
                                    }
                                    case "硬件": {
                                        break;
                                    }
                                    case "软": {
                                        v = 2;
                                        break;
                                    }
                                    case "软件": {
                                        v = 3;
                                        break;
                                    }
                                    default: {
                                        v = -1;
                                    }
                                }
                                if(v == 0 || v == 1) {
                                    v6 = 1;
                                }
                            }
                            break;
                        }
                        case 0x7F0B0084:   // id:config_time_jump_panel
                        case 0x7F0B008A:   // id:config_fast_freeze
                        case 0x7F0B008C:   // id:config_autopause
                        case 0x7F0B0091:   // id:config_reset_on_exit
                        case 0x7F0B0092:   // id:config_check_libs
                        case 0x7F0B0095:   // id:config_deep_read
                        case 0x7F0B009A:   // id:config_vspace_root
                        case 0x7F0B009C:   // id:config_use_notification
                        case 0x7F0B009D:   // id:config_hot_key
                        case 0x7F0B00A0:   // id:config_suggestions
                        case 0x7F0B00A1:   // id:config_ignore_unknown_chars
                        case 0x7F0B00A2:   // id:config_indent
                        case 0x7F0B00A3:   // id:config_visible_type
                        case 0x7F0B00AF: {  // id:config_use_sound_effects
                            if(luaValue4.isboolean()) {
                                v6 = luaValue4.checkboolean();
                            }
                            else if(s4 != null) {
                                v6 = s4.equals("是");
                            }
                            break;
                        }
                        case 0x7F0B00B1: {  // id:config_number_locale
                            if(s4 != null) {
                                switch(s4) {
                                    case "en_US": {
                                        v6 = 1;
                                        break;
                                    }
                                    case "in_ID": {
                                        v6 = 2;
                                        break;
                                    }
                                    case "ru_RU": {
                                        v6 = 3;
                                        break;
                                    }
                                    case "默认": {
                                    }
                                }
                            }
                        }
                    }
                }
                else if(v7 > 2) {
                    switch(v1) {
                        case 0x7F0B0081: {  // id:config_ranges
                            for(int v8 = 2; v8 <= v7; ++v8) {
                                LuaValue luaValue1 = varargs0.checkvalue(v8);
                                if(luaValue1 instanceof LuaString) {
                                    String s1 = luaValue1.checkjstring().toLowerCase();
                                    Integer integer0 = (Integer)setConfig.ranges.get(s1);
                                    v6 |= (integer0 == null ? 0x20 : ((int)integer0));
                                }
                                else if(luaValue1.isnumber()) {
                                    v6 |= luaValue1.checkint();
                                }
                            }
                            break;
                        }
                        case 0x7F0B0082: {  // id:config_speedhack_intercept
                            for(int v9 = 2; v9 <= v7; ++v9) {
                                int v10 = varargs0.optint(v9, 0);
                                if(v10 > 0) {
                                    int[] arr_v1 = setConfig.speedhack;
                                    if(v10 < arr_v1.length) {
                                        v6 |= arr_v1[v10];
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0085: {  // id:config_unrandomizer_intercept
                            for(int v11 = 2; v11 <= v7; ++v11) {
                                int v12 = varargs0.optint(v11, 0);
                                if(v12 > 0) {
                                    int[] arr_v2 = setConfig.unrandom;
                                    if(v12 < arr_v2.length) {
                                        v6 |= arr_v2[v12];
                                    }
                                }
                            }
                            break;
                        }
                        case 0x7F0B0087: {  // id:config_hide_from_game
                            for(int v13 = 2; v13 <= v7; ++v13) {
                                int v14 = varargs0.optint(v13, 0);
                                if(v14 > 0 && v14 < 5) {
                                    v6 |= new int[]{0, 1, 2, 4, 8}[v14];
                                }
                            }
                            break;
                        }
                        case 0x7F0B00A4: {  // id:config_use_small_list_items
                            for(int v15 = 2; v15 <= v7; ++v15) {
                                LuaValue luaValue2 = varargs0.checkvalue(v15);
                                if(luaValue2 instanceof LuaString) {
                                    String s2 = luaValue2.checkjstring();
                                    Integer integer1 = (Integer)setConfig.small.get(s2);
                                    v6 |= (integer1 == null ? 0 : ((int)integer1));
                                }
                                else if(luaValue2.isnumber()) {
                                    v6 |= luaValue2.checkint();
                                }
                            }
                            break;
                        }
                        case 0x7F0B00A5: {  // id:config_backgrounds
                            for(int v16 = 2; v16 <= v7; ++v16) {
                                LuaValue luaValue3 = varargs0.checkvalue(v16);
                                if(luaValue3 instanceof LuaString) {
                                    String s3 = luaValue3.checkjstring();
                                    Integer integer2 = (Integer)setConfig.back.get(s3);
                                    v6 |= (integer2 == null ? 0 : ((int)integer2));
                                }
                                else if(luaValue3.isnumber()) {
                                    v6 |= luaValue3.checkint();
                                }
                            }
                        }
                    }
                }
                Config.get(v1).value = v6;
                Config.save();
                return LuaValue.TRUE;
            }
            catch(Exception unused_ex) {
                return LuaValue.FALSE;
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.setConfig(id or name,value) -> bool";
        }
    }

    final class setRanges extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            Config.get(0x7F0B0081).value = args.checkint(1);  // id:config_ranges
            Config.save();
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            if(i != 1) {
                return super.logArg(log, i, arg);
            }
            return arg.isint() ? setRanges.logConst(log, Script.this.consts.REGION_, arg) : super.logArg(log, 1, arg);
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.setRanges(int ranges) -> nil";
        }
    }

    final class setSpeed extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            double f = args.checkdouble(1);
            if(f < 1.000000E-09 || f > 1000000000.0) {
                return LuaValue.valueOf(("Speed outside range: " + f + " (" + 1.000000E-09 + "; " + 1000000000.0 + ')'));
            }
            MainService.instance.setSpeed(Script.this.getSeq(), f);
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.setSpeed(double speed) -> true || string with error";
        }
    }

    final class setValues extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            AddressItem addressItem0;
            LuaTable luaTable0 = args.checktable(1);
            MainService service = MainService.instance;
            DaemonManager mDaemonManager = service.mDaemonManager;
            LuaString value = Script.inds[1];
            Iterator luaTable$Iterator0 = luaTable0.iterator();
            while(true) {
                if(!luaTable$Iterator0.next()) {
                    service.mDaemonManager.doWaited(Script.this.getSeq());
                    Object monitor = new Object();
                    synchronized(monitor) {
                        ThreadManager.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainService.instance.onMemoryChanged(Script.this.getSeq());
                                synchronized(monitor) {
                                    monitor.notifyAll();
                                }
                            }
                        });
                        Script.waitNotify(monitor);
                        return LuaValue.TRUE;
                    }
                }
                LuaTable luaTable1 = luaTable$Iterator0.value().checktable();
                try {
                    addressItem0 = Script.toItem(null, luaTable1, 1);
                    addressItem0.flags &= 0x7F;
                    if(addressItem0.flags == 0) {
                        continue;
                    }
                }
                catch(NumberFormatException e) {
                    throw Script.failedParse(luaTable$Iterator0.key(), luaTable1, e);
                }
                XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(Script.numberFromLua(Script.checkjstring(luaTable1, value)), false);
                mDaemonManager.addForAlter(addressItem0, (searchButtonListener$XorMode0 == null ? 0 : searchButtonListener$XorMode0.x));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.setValues(table values) -> true || string with error";
        }
    }

    final class setVisible extends ApiFunction {
        private long lastUsed;
        private int used;

        setVisible() {
            this.used = 0;
            this.lastUsed = 0L;
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            boolean changed = false;
            MainService service = MainService.instance;
            if(!args.checkboolean(1)) {
                if(service.mainDialog != null) {
                    service.dismissDialog(false);
                    changed = true;
                }
                service.showApp.hideUi();
            }
            else if(service.mainDialog == null) {
                service.onHotKeyDetected();
                changed = true;
            }
            if(changed) {
                long v = System.nanoTime();
                if(v / 60000000000L != this.lastUsed) {
                    this.lastUsed = v / 60000000000L;
                    this.used = 0;
                }
                int v1 = this.used + 1;
                this.used = v1;
                if(v1 > 30) {
                    Script.this.interrupt(2);
                }
            }
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.setVisible(bool visible) -> nil";
        }
    }

    final class shell extends ApiFunction {
        shell() {
            super();
        }

        public static String executeCommand(String s) {
            StringBuffer stringBuffer0 = new StringBuffer();
            try {
                Process process0 = Runtime.getRuntime().exec(s);
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(process0.getInputStream()));
                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(process0.getErrorStream()));
                String s1;
                while((s1 = bufferedReader0.readLine()) != null) {
                    stringBuffer0.append(s1 + "\n");
                    System.out.println("info:" + s1);
                    String s2 = bufferedReader1.readLine();
                    if(s2 != null) {
                        stringBuffer0.append(s1 + "\n");
                        System.out.println("error:" + s2);
                    }
                }
                bufferedReader0.close();
                bufferedReader1.close();
                return stringBuffer0.toString();
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
                return stringBuffer0.toString();
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs varargs0) {
            return LuaValue.valueOf(shell.executeCommand(varargs0.checkjstring(1)));
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.execCmd(string cmd) -> String";
        }
    }

    final class showUiButton extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            MainService.instance.setScriptUiButton(true);
            return showUiButton.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.showUiButton() -> nil";
        }
    }

    final class skipRestoreState extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 0;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            Script.this.savedState = null;
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.skipRestoreState() -> nil";
        }
    }

    static final class sleep extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            int v = args.checkint(1);
            if(v < 0) {
                throw new LuaError("milliseconds < 0: " + v);
            }
            try {
                Thread.sleep(v);
                return LuaValue.NIL;
            }
            catch(InterruptedException e) {
                Log.w("Interrupted sleep", e);
                throw new LuaError(e);
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.sleep(int milliseconds) -> nil";
        }
    }

    final class startFuzzy extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 3;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                Script.this.resultsLoaded = false;
                return FuzzyButtonListener.doSearch(Script.this.getSeq(), Script.checkType(args.optint(1, 0x7F)), args.optlong(2, 0L), args.optlong(3, -1L)) ? LuaValue.TRUE : null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        protected boolean logArg(Writer log, int i, LuaValue arg) throws IOException {
            switch(i) {
                case 1: {
                    return arg.isint() ? startFuzzy.logConst(log, Script.this.consts.TYPE_, arg) : super.logArg(log, 1, arg);
                }
                case 2: 
                case 3: {
                    return arg.islong() ? startFuzzy.logHex(log, arg) : super.logArg(log, i, arg);
                }
                default: {
                    return super.logArg(log, i, arg);
                }
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.startFuzzy([int type = gg.TYPE_AUTO [, long memoryFrom = 0 [, long memoryTo = -1]]]) -> true || string with error";
        }
    }

    final class timeJump extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 1;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            try {
                long v = TimeJump.parseNumber(Script.numberFromLua(args.checkjstring(1)));
                MainService.instance.mDaemonManager.addTimeJump(Script.this.getSeq(), v);
                return null;
            }
            catch(NumberFormatException e) {
                return LuaValue.valueOf(Script.toString(e));
            }
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.timeJump(string time) -> true || string with error";
        }
    }

    static final class toast extends ApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 2;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invoke2(Varargs args) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Tools.showToast(Re.s(args.checkjstring(1)), (args.optboolean(2, false) ? 0 : 1));
                }
            });
            return LuaValue.NIL;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.toast(string text [, bool fast = false]) -> nil";
        }
    }

    final class unrandomizer extends BusyApiFunction {
        @Override  // android.ext.Script$ApiFunction
        protected int getMaxArgs() {
            return 4;
        }

        @Override  // android.ext.Script$ApiFunction
        public Varargs invokeUi(Varargs args) {
            int flags = args.isnil(1) && args.isnil(2) ? 0 : 2;
            if(!args.isnil(2) || !args.isnil(3)) {
                flags |= 4;
            }
            MainService.instance.mDaemonManager.unrand(Script.this.getSeq(), flags, args.optlong(1, 0L), args.optlong(2, 0L), args.optdouble(3, 0.0), args.optdouble(4, 0.0));
            return null;
        }

        @Override  // android.ext.Script$ApiFunction
        String usage() {
            return "gg.unrandomizer([long qword = nil [, long qincr = nil [, double double_ = nil [, double dincr = nil]]]]) -> true || string with error";
        }
    }

    final class wrap extends VarArgFunction {
        final LuaValue base;
        final int maxArgs;
        final String name;

        wrap(String name, LuaValue base, int maxArgs) {
            this.base = base;
            this.name = name;
            this.maxArgs = maxArgs;
        }

        @Override  // luaj.lib.VarArgFunction
        public Varargs invoke(Varargs args) {
            long v;
            DebugLog dbg = Script.debugLog;
            Writer log = dbg == null ? null : dbg.log;
            if(log != null) {
                try {
                    v = System.nanoTime();
                    log.write(this.name);
                    log.write("(");
                    int n = args.narg();
                    int m = this.maxArgs;
                    if(m >= 0 && n > m) {
                        n = m;
                    }
                    for(int i = 1; i <= n; ++i) {
                        if(i != 1) {
                            log.write(", ");
                        }
                        ApiFunction.logArg_(null, log, i, args.arg(i));
                    }
                    log.write(")\n");
                }
                catch(Throwable e) {
                    Log.w(("Failed write log for " + this.name), e);
                }
                dbg.globals.logMillis = (int)(((long)dbg.globals.logMillis) + (System.nanoTime() - v) / 1000000L);
            }
            return this.base.invoke(args);
        }

        @Override  // luaj.lib.LibFunction
        public String tojstring() {
            return this.base.tojstring();
        }
    }

    private static final int BY_CALL = 1;
    private static final int BY_KILL_PROCESS = 2;
    private static final int BY_SET_VISIBLE = 3;
    private static final byte MAX_SCRIPTS = 5;
    private static final byte MAX_SEQ = 20;
    private static final byte SEQ_GAP = 20;
    private static final int UNKNOWN = 0;
    static volatile long allocatedPage = 0L;
    private String bad1;
    private String bad2;
    private String bad3;
    private static WeakReference cacheConsts = null;
    Consts consts;
    static DebugLog debugLog = null;
    public Globals globals;
    private final byte id;
    static final LuaString[] inds = null;
    public static volatile Globals instanceGlobals = null;
    volatile int interruptedReason;
    static final int[] itemSlots = null;
    final Logger log;
    static volatile ArrayList memList = null;
    static volatile int memListUse = 0;
    volatile String notifyReason;
    final String out;
    final int params;
    public static volatile List processList = null;
    volatile boolean resultsLoaded;
    static final int[] savedItemSlots = null;
    volatile SavedState savedState;
    private static volatile int scriptId = 0;
    private volatile int seqId;
    private volatile byte seqLast;
    private String small;
    final Object source;
    final Thread thread;
    private static final long unixTsOffset = 1546300800000L;

    static {
        Script.scriptId = 0;
        Script.cacheConsts = new WeakReference(null);
        Script.memList = null;
        Script.memListUse = 0;
        Script.allocatedPage = 0L;
        Script.inds = new LuaString[]{LuaString.valueOf("address"), LuaString.valueOf("value"), LuaString.valueOf("flags"), LuaString.valueOf("name"), LuaString.valueOf("freeze"), LuaString.valueOf("freezeType"), LuaString.valueOf("freezeFrom"), LuaString.valueOf("freezeTo")};
        Script.itemSlots = new int[3];
        Script.savedItemSlots = new int[8];
        for(int i = 0; i < Script.itemSlots.length; ++i) {
            Script.itemSlots[i] = LuaTable.hashSlot(Script.inds[i], 3);
        }
        for(int i = 0; i < Script.savedItemSlots.length; ++i) {
            Script.savedItemSlots[i] = LuaTable.hashSlot(Script.inds[i], 7);
        }
        Script.debugLog = null;
    }

    public Script(java.io.File file, int params, String out) {
        this.seqId = 0;
        this.seqLast = -1;
        this.resultsLoaded = true;
        this.interruptedReason = 0;
        this.globals = new Globals();
        this.thread = new ScriptThread(this);
        this.log = new Logger();
        int v1 = Script.scriptId;
        Script.scriptId = v1 + 1;
        this.id = (byte)(v1 % 5);
        this.consts = null;
        this.small = null;
        this.bad1 = null;
        this.bad2 = null;
        this.bad3 = null;
        this.notifyReason = null;
        this.savedState = null;
        LoadState.badHeader = false;
        this.source = file;
        this.params = params;
        this.out = out;
        this.init();
    }

    public Script(String script, int params, String out) {
        this.seqId = 0;
        this.seqLast = -1;
        this.resultsLoaded = true;
        this.interruptedReason = 0;
        this.globals = new Globals();
        this.thread = new ScriptThread(this);
        this.log = new Logger();
        int v1 = Script.scriptId;
        Script.scriptId = v1 + 1;
        this.id = (byte)(v1 % 5);
        this.consts = null;
        this.small = null;
        this.bad1 = null;
        this.bad2 = null;
        this.bad3 = null;
        this.notifyReason = null;
        this.savedState = null;
        this.source = script;
        this.params = params;
        this.out = out;
        this.init();
    }

    public static void allocatedPage(long page) {
        Script.allocatedPage = page;
    }

    void appendLog(String data) {
        this.log.append("\n");
        this.log.append(data);
        this.log.append("\n");
    }

    void appendLog(boolean isErrorStream, String line) {
        if(isErrorStream) {
            this.log.append("err: ");
        }
        this.log.append(line);
        this.log.append("\n");
    }

    private static LuaError badArg(LuaValue key, LuaError e) {
        String s = e.getMessage();
        return s.indexOf("bad argument:") == 0 ? new LuaError("bad argument for key \'" + key.tojstring() + "\':" + s.substring(13)) : e;
    }

    @Override  // luaj.lib.TwoArgFunction
    public LuaValue call(LuaValue modname, LuaValue env) {
        Context context0 = Tools.getContext();
        ArrayList consts = (this.params & 4) == 0 ? null : new ArrayList();
        LuaValue luaValue2 = new LuaTable(0, 0x80);
        ((LuaTable)luaValue2).rawset("VERSION", 96.0f);
        ((LuaTable)luaValue2).rawset("VERSION_INT", Debug.getIntVersion());
        ((LuaTable)luaValue2).rawset("BUILD", 0x3E79);
        ((LuaTable)luaValue2).rawset("PACKAGE", Tools.getPackageName());
        ((LuaTable)luaValue2).rawset("FILES_DIR", Tools.getFilesDir().getAbsolutePath());
        ((LuaTable)luaValue2).rawset("CACHE_DIR", Tools.getCacheDir().getAbsolutePath());
        try {
            ((LuaTable)luaValue2).rawset("EXT_FILES_DIR", context0.getExternalFilesDir(null).getAbsolutePath());
        }
        catch(Throwable e) {
            Log.e("Failed set EXT_FILES_DIR for script", e);
            ((LuaTable)luaValue2).rawset("EXT_FILES_DIR", Tools.getFilesDir().getAbsolutePath());
        }
        try {
            ((LuaTable)luaValue2).rawset("EXT_CACHE_DIR", context0.getExternalCacheDir().getAbsolutePath());
        }
        catch(Throwable e) {
            Log.e("Failed set EXT_CACHE_DIR for script", e);
            ((LuaTable)luaValue2).rawset("EXT_CACHE_DIR", Tools.getCacheDir().getAbsolutePath());
        }
        ((LuaTable)luaValue2).rawset("EXT_STORAGE", Tools.getSdcardPath());
        ((LuaTable)luaValue2).rawset("getSign", c.a(Tools.getContext().getApplicationContext(), "MD5"));
        Script.loadConsts(((LuaTable)luaValue2), consts);
        ((LuaTable)luaValue2).rawset("require", new require(this));
        ((LuaTable)luaValue2).rawset("toast", new toast());
        ((LuaTable)luaValue2).rawset("alert", new alert());
        ((LuaTable)luaValue2).rawset("prompt", new prompt(this));
        ((LuaTable)luaValue2).rawset("choice", new choice());
        ((LuaTable)luaValue2).rawset("multiChoice", new multiChoice());
        ((LuaTable)luaValue2).rawset("isVisible", new isVisible());
        ((LuaTable)luaValue2).rawset("setVisible", new setVisible(this));
        ((LuaTable)luaValue2).rawset("getActiveTab", new getActiveTab());
        ((LuaTable)luaValue2).rawset("showUiButton", new showUiButton(this));
        ((LuaTable)luaValue2).rawset("hideUiButton", new hideUiButton(this));
        ((LuaTable)luaValue2).rawset("isClickedUiButton", new isClickedUiButton());
        getTargetPackage target = new getTargetPackage();
        ((LuaTable)luaValue2).rawset("getTargetPackage", target);
        ((LuaTable)luaValue2).rawset("getTargetInfo", new getTargetInfo());
        ((LuaTable)luaValue2).rawset("isPackageInstalled", new isPackageInstalled());
        ((LuaTable)luaValue2).rawset("processKill", new processKill(this));
        ((LuaTable)luaValue2).rawset("saveVariable", new saveVariable(this));
        ((LuaTable)luaValue2).rawset("makeRequest", new makeRequest(this));
        getResultsCount getResultsCount = new getResultsCount();
        ((LuaTable)luaValue2).rawset("getResultsCount", getResultsCount);
        ((LuaTable)luaValue2).rawset("getResults", new getResults(this));
        ((LuaTable)luaValue2).rawset("editAll", new editAll(this));
        ((LuaTable)luaValue2).rawset("clearResults", new clearResults(this));
        ((LuaTable)luaValue2).rawset("removeResults", new removeResults(this));
        ((LuaTable)luaValue2).rawset("loadResults", new loadResults(this));
        ((LuaTable)luaValue2).rawset("getSelectedResults", new getSelectedResults(this));
        ((LuaTable)luaValue2).rawset("setValues", new setValues(this));
        ((LuaTable)luaValue2).rawset("getValues", new getValues(this));
        ((LuaTable)luaValue2).rawset("getValuesRange", new getValuesRange(this));
        ((LuaTable)luaValue2).rawset("processPause", new processPause(this));
        ((LuaTable)luaValue2).rawset("processResume", new processResume(this));
        ((LuaTable)luaValue2).rawset("processToggle", new processToggle(this));
        ((LuaTable)luaValue2).rawset("isProcessPaused", new isProcessPaused());
        ((LuaTable)luaValue2).rawset("timeJump", new timeJump(this));
        ((LuaTable)luaValue2).rawset("setSpeed", new setSpeed(this));
        ((LuaTable)luaValue2).rawset("getSpeed", new getSpeed());
        ((LuaTable)luaValue2).rawset("unrandomizer", new unrandomizer(this));
        ((LuaTable)luaValue2).rawset("gotoAddress", new gotoAddress());
        ((LuaTable)luaValue2).rawset("getSelectedElements", new getSelectedElements(this));
        ((LuaTable)luaValue2).rawset("getRanges", new getRanges());
        ((LuaTable)luaValue2).rawset("setRanges", new setRanges(this));
        ((LuaTable)luaValue2).rawset("copyMemory", new copyMemory(this));
        ((LuaTable)luaValue2).rawset("dumpMemory", new dumpMemory(this));
        ((LuaTable)luaValue2).rawset("loadList", new loadList(this));
        ((LuaTable)luaValue2).rawset("saveList", new saveList(this));
        ((LuaTable)luaValue2).rawset("clearList", new clearList(this));
        ((LuaTable)luaValue2).rawset("addListItems", new addListItems(this));
        ((LuaTable)luaValue2).rawset("getListItems", new getListItems(this));
        ((LuaTable)luaValue2).rawset("removeListItems", new removeListItems(this));
        ((LuaTable)luaValue2).rawset("getSelectedListItems", new getSelectedListItems(this));
        ((LuaTable)luaValue2).rawset("searchNumber", new searchNumber(this));
        ((LuaTable)luaValue2).rawset("refineNumber", new refineNumber(this));
        ((LuaTable)luaValue2).rawset("searchAddress", new searchAddress(this));
        ((LuaTable)luaValue2).rawset("refineAddress", new refineAddress(this));
        ((LuaTable)luaValue2).rawset("startFuzzy", new startFuzzy(this));
        ((LuaTable)luaValue2).rawset("searchFuzzy", new searchFuzzy(this));
        ((LuaTable)luaValue2).rawset("searchPointer", new Script.searchPointer(this));
        ((LuaTable)luaValue2).rawset("sleep", new sleep());
        ((LuaTable)luaValue2).rawset("command", new command());
        ((LuaTable)luaValue2).rawset("shell", new shell());
        ((LuaTable)luaValue2).rawset("appPath", new Script.appPath());
        ((LuaTable)luaValue2).rawset("exit", new GgExit());
        ((LuaTable)luaValue2).rawset("codeScript", new codeScript());
        ((LuaTable)luaValue2).rawset("isVPN", new isVPN());
        ((LuaTable)luaValue2).rawset("isHTTPdump", new isHTTPdump());
        ((LuaTable)luaValue2).rawset("jumpAPP", new Script.jumpAPP());
        ((LuaTable)luaValue2).rawset("intent", new intent());
        ((LuaTable)luaValue2).rawset("goURL", new goURL());
        ((LuaTable)luaValue2).rawset("QQchat", new QQchat());
        ((LuaTable)luaValue2).rawset("QQgroup", new QQgroup());
        ((LuaTable)luaValue2).rawset("colorAlert", new colorAlert());
        ((LuaTable)luaValue2).rawset("diyToast", new diyToast());
        ((LuaTable)luaValue2).rawset("playMusic", new Script.playMusic());
        ((LuaTable)luaValue2).rawset("playVideo", new Script.playVideo());
        ((LuaTable)luaValue2).rawset("ETD", new Script.ETD());
        ((LuaTable)luaValue2).rawset("FTD", new Script.FTD());
        ((LuaTable)luaValue2).rawset("WTD", new Script.WTD());
        ((LuaTable)luaValue2).rawset("XTD", new Script.XTD());
        ((LuaTable)luaValue2).rawset("sumAddress", new Script.sumAddress());
        ((LuaTable)luaValue2).rawset("sumAddressX", new Script.sumAddressX());
        ((LuaTable)luaValue2).rawset("searchPointerX", new Script.searchPointerX());
        ((LuaTable)luaValue2).rawset("gotoPointer", new Script.gotoPointer());
        ((LuaTable)luaValue2).rawset("execSQL", new Script.execSQL());
        ((LuaTable)luaValue2).rawset("querySQL", new Script.querySQL());
        ((LuaTable)luaValue2).rawset("getConfig", new Script.getConfig());
        ((LuaTable)luaValue2).rawset("setConfig", new setConfig());
        ((LuaTable)luaValue2).rawset("getProcess", new Script.getProcess());
        ((LuaTable)luaValue2).rawset("setProcess", new Script.setProcess());
        ((LuaTable)luaValue2).rawset("setProcessX", new Script.setProcessX());
        ((LuaTable)luaValue2).rawset("colorToast", new Script.colorToast());
        ((LuaTable)luaValue2).rawset("getWindowOrientation", new Script.getWindowOrientation());
        ((LuaTable)luaValue2).rawset("getSignatures", new Script.getSignatures());
        ((LuaTable)luaValue2).rawset("ExclusiveoutputG", new Script.ExclusiveoutputG(this));
        ((LuaTable)luaValue2).rawset("getRaw", new Script.getRaw(this));
        ((LuaTable)luaValue2).rawset("searchChoice", new Script.searchChoice());
        ((LuaTable)luaValue2).rawset("getAdressString", new getAdressString());
        ((LuaTable)luaValue2).rawset("getAdressJavaString", new getAdressJavaString());
        ((LuaTable)luaValue2).rawset("gotoBrowser", new Script.gotoBrowser());
        ((LuaTable)luaValue2).rawset("getAddressUTF", new getAddressUTF(this));
        ((LuaTable)luaValue2).rawset("getUTF", new getUTF());
        ((LuaTable)luaValue2).rawset("getProcessInfo", new Script.getProcessInfo());
        ((LuaTable)luaValue2).rawset("getAppInfo", new Script.getAppInfo());
        ((LuaTable)luaValue2).rawset("offsetString", new offsetString());
        ((LuaTable)luaValue2).rawset("offsetJavaString", new Script.offsetJavaString());
        ((LuaTable)luaValue2).rawset("copyText", new copyText());
        ((LuaTable)luaValue2).rawset("disasm", new disasm(this));
        ((LuaTable)luaValue2).rawset("getLine", new getLine(this));
        ((LuaTable)luaValue2).rawset("getFile", new getFile(this));
        ((LuaTable)luaValue2).rawset("bytes", new bytes(this));
        ((LuaTable)luaValue2).rawset("skipRestoreState", new skipRestoreState(this));
        ((LuaTable)luaValue2).rawset("getRangesList", new getRangesList(this));
        ((LuaTable)luaValue2).rawset("allocatePage", new allocatePage(this));
        ((LuaTable)luaValue2).rawset("getLocale", new getLocale(this));
        ((LuaTable)luaValue2).rawset("numberToLocale", new numberToLocale(this));
        ((LuaTable)luaValue2).rawset("numberFromLocale", new numberFromLocale(this));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("SIGN_INEQUAL").makeDeprecated(), LuaValue.valueOf(0x10000000L));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("SIGN_SMALLER").makeDeprecated(), LuaValue.valueOf(0x8000000L));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("SIGN_LARGER").makeDeprecated(), LuaValue.valueOf(0x4000000L));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("getSelectedPackage").makeDeprecated(), target);
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("getResultCount").makeDeprecated(), getResultsCount);
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("internal1").makeDeprecated(), new internal1(this));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("internal2").makeDeprecated(), new internal2(this));
        ((LuaTable)luaValue2).rawset(LuaValue.valueOf("internal3").makeDeprecated(), new internal3(this));
        env.set("gg", luaValue2);
        env.get("package").get("loaded").set("gg", luaValue2);
        LuaValue luaValue3 = env.get("os");
        luaValue3.set("exit", new exit());
        luaValue3.set("remove", new wrap(this, "os.remove", luaValue3.get("remove"), 1));
        luaValue3.set("rename", new wrap(this, "os.rename", luaValue3.get("rename"), 2));
        luaValue3.set("tmpname", new wrap(this, "os.tmpname", luaValue3.get("tmpname"), 0));
        LuaValue luaValue4 = env.get("string");
        luaValue4.set("format", new format(luaValue4.get("format")));
        luaValue4.set("dump", new wrap(this, "string.dump", luaValue4.get("dump"), 1));
        LuaValue luaValue5 = env.get("io");
        luaValue5.set("open", new wrap(this, "io.open", luaValue5.get("open"), 2));
        luaValue5.set("input", new wrap(this, "io.input", luaValue5.get("input"), 1));
        luaValue5.set("output", new wrap(this, "io.output", luaValue5.get("output"), 1));
        luaValue5.set("tmpfile", new wrap(this, "io.tmpfile", luaValue5.get("tmpfile"), 0));
        luaValue5.set("lines", new wrap(this, "io.lines", luaValue5.get("lines"), 1));
        LuaValue luaValue6 = env.get("debug");
        luaValue6.set("debug", new wrap(this, "debug.debug", luaValue6.get("debug"), 0));
        luaValue6.set("gethook", new wrap(this, "debug.gethook", luaValue6.get("gethook"), 1));
        luaValue6.set("getinfo", new wrap(this, "debug.getinfo", luaValue6.get("getinfo"), 3));
        luaValue6.set("getlocal", new wrap(this, "debug.getlocal", luaValue6.get("getlocal"), 3));
        luaValue6.set("getmetatable", new wrap(this, "debug.getmetatable", luaValue6.get("getmetatable"), 1));
        luaValue6.set("getregistry", new wrap(this, "debug.getregistry", luaValue6.get("getregistry"), 0));
        luaValue6.set("getupvalue", new wrap(this, "debug.getupvalue", luaValue6.get("getupvalue"), 2));
        luaValue6.set("getuservalue", new wrap(this, "debug.getuservalue", luaValue6.get("getuservalue"), 1));
        luaValue6.set("sethook", new wrap(this, "debug.sethook", luaValue6.get("sethook"), 4));
        luaValue6.set("setlocal", new wrap(this, "debug.setlocal", luaValue6.get("setlocal"), 4));
        luaValue6.set("setmetatable", new wrap(this, "debug.setmetatable", luaValue6.get("setmetatable"), 2));
        luaValue6.set("setupvalue", new wrap(this, "debug.setupvalue", luaValue6.get("setupvalue"), 3));
        luaValue6.set("setuservalue", new wrap(this, "debug.setuservalue", luaValue6.get("setuservalue"), 2));
        luaValue6.set("traceback", new wrap(this, "debug.traceback", luaValue6.get("traceback"), 3));
        luaValue6.set("upvalueid", new wrap(this, "debug.upvalueid", luaValue6.get("upvalueid"), 2));
        luaValue6.set("upvaluejoin", new wrap(this, "debug.upvaluejoin", luaValue6.get("upvaluejoin"), 4));
        this.consts = consts == null ? new Consts() : Script.getConsts(consts);
        return luaValue2;
    }

    public void callNotify(byte seq, String reason) {
        int nId = (byte)(((byte)(seq - 20)) / 20);
        int nSeq = (byte)(((byte)(seq - 20)) % 20);
        if(this.id != nId) {
            Log.d(("callNotify id mismatch: " + ((int)this.id) + " != " + nId + "; " + nSeq + "; " + ((int)seq) + "; " + reason));
            return;
        }
        int seqL = this.seqLast;
        if(seqL == nSeq) {
            synchronized(this) {
                seqL = this.seqLast;
                if(seqL == nSeq) {
                    this.notifyReason = reason;
                    this.notifyAll();
                    return;
                }
            }
        }
        Log.d(("callNotify seq mismatch: " + seqL + " != " + nSeq + "; " + nId + "; " + ((int)seq) + "; " + reason));
    }

    static int checkSign(int sign) {
        if(sign != 0x4000000 && sign != 0x8000000 && sign != 0x10000000 && sign != 0x20000000) {
            throw new LuaError("Unknown sign: " + sign + ". The sign must be one of the constants gg.SIGN_*.");
        }
        return sign;
    }

    static int checkType(int type) {
        if(type == 0 || (type & 0xFFFFFF80) != 0) {
            throw new LuaError("Unknown type: " + type + ". The type must be one of the constants gg.TYPE_*.");
        }
        return type;
    }

    static int checkint(LuaTable table, LuaValue key) {
        try {
            return table.get(key).checkint();
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    static String checkjstring(LuaTable table, LuaValue key) {
        try {
            return table.get(key).checkjstring();
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    static long checklong(LuaTable table, LuaValue key) {
        try {
            return table.getchecklong(key);
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    public static void doTest() {
    }

    static LuaError failedParse(LuaValue key, LuaValue table, Throwable e) {
        return new LuaError("Failed parse item " + table.tojstring() + " on key \'" + key.tojstring() + "\': " + e.getMessage(), e);
    }

    private static LuaTable fromItem(int[] itemSlots, AddressItem item) {
        if(itemSlots == null) {
            itemSlots = Script.itemSlots;
        }
        LuaTable table = new LuaTable(0, itemSlots.length);
        long address = item.address;
        int type = item.flags;
        table.unsafehashset(itemSlots[0], Script.inds[0], address);
        Script.setValue(itemSlots[1], table, Script.inds[1], address, item.data, type);
        table.unsafehashset(itemSlots[2], Script.inds[2], type);
        return table;
    }

    static LuaTable fromSavedItem(SavedItem item) {
        LuaString[] inds = Script.inds;
        int[] savedItemSlots = Script.savedItemSlots;
        LuaTable luaTable0 = Script.fromItem(savedItemSlots, item);
        String name = item.name;
        if(name != null) {
            luaTable0.hashset(inds[3], LuaValue.valueOf(name));
        }
        luaTable0.unsafehashset(savedItemSlots[4], inds[4], item.freeze);
        luaTable0.unsafehashset(savedItemSlots[5], inds[5], ((int)item.freezeType));
        long address = item.address;
        int type = item.flags;
        if(item.freezeFrom != 0L || item.freezeTo != 0L) {
            Script.setValue(savedItemSlots[6], luaTable0, inds[6], address, item.freezeFrom, type);
            Script.setValue(savedItemSlots[7], luaTable0, inds[7], address, item.freezeTo, type);
        }
        return luaTable0;
    }

    public static Consts getConsts(ArrayList arrayList0) {
        Consts consts = (Consts)Script.cacheConsts.get();
        if(consts == null) {
            if(arrayList0 == null) {
                arrayList0 = new ArrayList();
                Script.loadConsts(new LuaTable(0, 0x40), arrayList0);
            }
            ArrayList tREGION_ = new ArrayList();
            ArrayList tTYPE_ = new ArrayList();
            ArrayList tSIGN_FUZZY_ = new ArrayList();
            ArrayList tSIGN_ = new ArrayList();
            ArrayList tLOAD_ = new ArrayList();
            ArrayList tSAVE_ = new ArrayList();
            ArrayList tPROT_ = new ArrayList();
            ArrayList tPOINTER_ = new ArrayList();
            ArrayList tDUMP_ = new ArrayList();
            ArrayList tASM_ = new ArrayList();
            for(Object object0: arrayList0) {
                Const const_ = (Const)object0;
                String name = const_.name;
                if(name.startsWith("gg.REGION_")) {
                    tREGION_.add(const_);
                }
                else if(name.startsWith("gg.TYPE_")) {
                    tTYPE_.add(const_);
                }
                else if(name.startsWith("gg.SIGN_FUZZY_")) {
                    tSIGN_FUZZY_.add(const_);
                }
                else if(name.startsWith("gg.SIGN_")) {
                    tSIGN_.add(const_);
                }
                else if(name.startsWith("gg.LOAD_")) {
                    tLOAD_.add(const_);
                }
                else if(name.startsWith("gg.SAVE_")) {
                    tSAVE_.add(const_);
                }
                else if(name.startsWith("gg.PROT_")) {
                    tPROT_.add(const_);
                }
                else if(name.startsWith("gg.POINTER_")) {
                    tPOINTER_.add(const_);
                }
                else if(name.startsWith("gg.DUMP_")) {
                    tDUMP_.add(const_);
                }
                else if(name.startsWith("gg.ASM_")) {
                    tASM_.add(const_);
                }
            }
            consts = new Consts();
            consts.REGION_ = (Const[])tREGION_.toArray(new Const[tREGION_.size()]);
            consts.TYPE_ = (Const[])tTYPE_.toArray(new Const[tTYPE_.size()]);
            consts.SIGN_FUZZY_ = (Const[])tSIGN_FUZZY_.toArray(new Const[tSIGN_FUZZY_.size()]);
            consts.SIGN_ = (Const[])tSIGN_.toArray(new Const[tSIGN_.size()]);
            consts.LOAD_ = (Const[])tLOAD_.toArray(new Const[tLOAD_.size()]);
            consts.SAVE_ = (Const[])tSAVE_.toArray(new Const[tSAVE_.size()]);
            consts.PROT_ = (Const[])tPROT_.toArray(new Const[tPROT_.size()]);
            consts.POINTER_ = (Const[])tPOINTER_.toArray(new Const[tPOINTER_.size()]);
            consts.DUMP_ = (Const[])tDUMP_.toArray(new Const[tDUMP_.size()]);
            consts.ASM_ = (Const[])tASM_.toArray(new Const[tASM_.size()]);
            Script.cacheConsts = new WeakReference(consts);
        }
        return consts;
    }

    private static long getData(long address, LuaValue key, LuaValue val, int type, boolean def) {
        String s;
        if(val instanceof LuaLong) {
            long v2 = val.tolong();
            if(type == 16) {
                return (long)Float.floatToRawIntBits(v2);
            }
            if(type == 0x40) {
                return Double.doubleToRawLongBits(v2);
            }
            return type == 8 ? v2 ^ address : v2;
        }
        if(val instanceof LuaDouble) {
            double f = val.todouble();
            if(type == 16) {
                return (long)Float.floatToRawIntBits(((float)f));
            }
            if(type == 0x40) {
                return Double.doubleToRawLongBits(f);
            }
            long data = (long)f;
            return type == 8 ? data ^ address : data;
        }
        try {
            s = def ? val.optjstring(null) : val.checkjstring();
            if(s == null) {
                return 0L;
            }
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
        String str = Script.numberFromLua(s);
        XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(str, false);
        if(searchButtonListener$XorMode0 != null) {
            str = searchButtonListener$XorMode0.input;
        }
        return AddressItem.dataFromString(address, str, type);
    }

    static ArrayList getItems(Varargs args) {
        LuaTable luaTable0 = args.checktable(1);
        ArrayList items = new ArrayList();
        Iterator luaTable$Iterator0 = luaTable0.iterator();
        while(luaTable$Iterator0.next()) {
            AddressItem addressItem0 = Script.toItem(null, luaTable$Iterator0.value().checktable(), 2);
            addressItem0.flags &= 0x7F;
            items.add(addressItem0);
        }
        return items;
    }

    static String getPath(java.io.File file) {
        try {
            return file.getCanonicalPath();
        }
        catch(IOException unused_ex) {
            return file.getAbsolutePath();
        }
    }

    byte getSeq() {
        int v = this.seqId;
        this.seqId = v + 1;
        this.seqLast = (byte)(v % 20);
        return (byte)(this.id * 20 + ((byte)(v % 20)) + 20);
    }

    private String getSource() {
        if(this.source instanceof java.io.File) {
            java.io.File file = (java.io.File)this.source;
            try {
                StringBuilder out = new StringBuilder();
                BufferedReader bufferedReader0 = new BufferedReader(new FileReader(file));
                int v = FIN.finallyOpen$NT();
                while(true) {
                    String s = bufferedReader0.readLine();
                    if(s == null) {
                        FIN.finallyCodeBegin$NT(v);
                        bufferedReader0.close();
                        FIN.finallyCodeEnd$NT(v);
                        return out.toString();
                    }
                    out.append(s);
                    out.append('\n');
                }
            }
            catch(Exception e) {
                return e.toString() + '\n' + Log.getStackTraceString(e);
            }
        }
        return this.source.toString();
    }

    private void init() {
        Globals globals = this.globals;
        LoadState.install(globals);
        LuaC.install(globals);
        globals.load(new BaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new Utf8Lib());
        globals.load(new MathLib());
        globals.load(new IoLibSafe(this));
        globals.load(new OsLibSafe(this));
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
        this.initApi();
        Script.instanceGlobals = this.globals;
    }

    private void initApi() {
        Globals globals = this.globals;
        globals.STDOUT = new ScriptPrintStream(this, false);
        globals.STDERR = new ScriptPrintStream(this, true);
        globals.load(this);
    }

    public Script interrupt() {
        return this.interrupt(1);
    }

    public Script interrupt(int reason) {
        this.interruptedReason = reason;
        this.thread.interrupt();
        return this;
    }

    boolean isBadFile(String filename, boolean read) {
        String s1 = Script.getPath(new java.io.File(filename));
        String bad = this.bad1;
        if(bad == null) {
            bad = Script.getPath(Tools.getFilesDirHidden());
            this.bad1 = bad;
        }
        if(!s1.startsWith(bad)) {
            String bad = this.bad2;
            if(bad == null) {
                bad = Script.getPath(Tools.getCacheDirHidden());
                this.bad2 = bad;
            }
            if(!s1.startsWith(bad)) {
                String bad = this.bad3;
                if(bad == null) {
                    bad = Script.getPath(Uninstaller.getSharedPrefsFile("null_preferences"));
                    this.bad3 = bad;
                }
                if(!bad.startsWith(s1) || bad.length() != s1.length() && bad.charAt(s1.length()) != 0x2F) {
                    if(read || !(this.source instanceof java.io.File)) {
                        return false;
                    }
                    String bad = this.small;
                    if(bad == null) {
                        bad = Script.getPath(((java.io.File)this.source));
                        this.small = bad;
                    }
                    return s1.equals(bad);
                }
            }
        }
        return true;
    }

    boolean isSavedList(java.io.File file) {
        if(file.exists()) {
            try {
                BufferedReader bufferedReader0 = new BufferedReader(new FileReader(file));
                int status = 0;
                while(true) {
                    String s = bufferedReader0.readLine();
                    if(s != null) {
                        String line = s.trim();
                        if(line.length() == 0) {
                            continue;
                        }
                        if(status == 0) {
                            try {
                                Integer.parseInt(line);
                                status = 1;
                                continue;
                            label_11:
                                if(line.split("\\|").length < 10) {
                                    status = -status;
                                }
                                else {
                                    goto label_15;
                                }
                            }
                            catch(Throwable unused_ex) {
                            }
                        }
                        else {
                            goto label_11;
                        }
                    }
                    bufferedReader0.close();
                    return status > 1;
                label_15:
                    ++status;
                }
                return false;
            }
            catch(IOException e) {
            }
        }
        else {
            return false;
        }
        Log.d("Failed detect", e);
        return false;
    }

    static void loadConsts(LuaTable gg, ArrayList arrayList0) {
        Script.setConst(arrayList0, gg, "TYPE_AUTO", 0x7F);
        Script.setConst(arrayList0, gg, "TYPE_BYTE", 1);
        Script.setConst(arrayList0, gg, "TYPE_WORD", 2);
        Script.setConst(arrayList0, gg, "TYPE_DWORD", 4);
        Script.setConst(arrayList0, gg, "TYPE_XOR", 8);
        Script.setConst(arrayList0, gg, "TYPE_FLOAT", 16);
        Script.setConst(arrayList0, gg, "TYPE_QWORD", 0x20);
        Script.setConst(arrayList0, gg, "TYPE_DOUBLE", 0x40);
        Script.setConst(arrayList0, gg, "SIGN_EQUAL", 0x20000000);
        Script.setConst(arrayList0, gg, "SIGN_NOT_EQUAL", 0x10000000);
        Script.setConst(arrayList0, gg, "SIGN_LESS_OR_EQUAL", 0x8000000);
        Script.setConst(arrayList0, gg, "SIGN_GREATER_OR_EQUAL", 0x4000000);
        Script.setConst(arrayList0, gg, "SIGN_FUZZY_EQUAL", 0x20000000);
        Script.setConst(arrayList0, gg, "SIGN_FUZZY_NOT_EQUAL", 0x10000000);
        Script.setConst(arrayList0, gg, "SIGN_FUZZY_LESS", 0x8000000);
        Script.setConst(arrayList0, gg, "SIGN_FUZZY_GREATER", 0x4000000);
        Script.setConst(arrayList0, gg, "REGION_JAVA_HEAP", 2);
        Script.setConst(arrayList0, gg, "REGION_C_HEAP", 1);
        Script.setConst(arrayList0, gg, "REGION_C_ALLOC", 4);
        Script.setConst(arrayList0, gg, "REGION_C_DATA", 8);
        Script.setConst(arrayList0, gg, "REGION_C_BSS", 16);
        Script.setConst(arrayList0, gg, "REGION_PPSSPP", 0x40000);
        Script.setConst(arrayList0, gg, "REGION_ANONYMOUS", 0x20);
        Script.setConst(arrayList0, gg, "REGION_JAVA", 0x10000);
        Script.setConst(arrayList0, gg, "REGION_STACK", 0x40);
        Script.setConst(arrayList0, gg, "REGION_ASHMEM", 0x80000);
        Script.setConst(arrayList0, gg, "REGION_VIDEO", 0x100000);
        Script.setConst(arrayList0, gg, "REGION_OTHER", 0xFFE03F80);
        Script.setConst(arrayList0, gg, "REGION_BAD", 0x20000);
        Script.setConst(arrayList0, gg, "REGION_CODE_APP", 0x4000);
        Script.setConst(arrayList0, gg, "REGION_CODE_SYS", 0x8000);
        Script.setConst(arrayList0, gg, "LOAD_VALUES_FREEZE", 3);
        Script.setConst(arrayList0, gg, "LOAD_VALUES", 2);
        Script.setConst(arrayList0, gg, "LOAD_APPEND", 8);
        Script.setConst(arrayList0, gg, "SAVE_AS_TEXT", 1);
        gg.rawset("FREEZE_NORMAL", 0);
        gg.rawset("FREEZE_MAY_INCREASE", 1);
        gg.rawset("FREEZE_MAY_DECREASE", 2);
        gg.rawset("FREEZE_IN_RANGE", 3);
        gg.rawset("PROT_NONE", 0);
        Script.setConst(arrayList0, gg, "PROT_READ", 2);
        Script.setConst(arrayList0, gg, "PROT_WRITE", 1);
        Script.setConst(arrayList0, gg, "PROT_EXEC", 4);
        Script.setConst(arrayList0, gg, "POINTER_NO", 4);
        Script.setConst(arrayList0, gg, "POINTER_READ_ONLY", 8);
        Script.setConst(arrayList0, gg, "POINTER_WRITABLE", 16);
        Script.setConst(arrayList0, gg, "POINTER_EXECUTABLE", 2);
        Script.setConst(arrayList0, gg, "POINTER_EXECUTABLE_WRITABLE", 1);
        Script.setConst(arrayList0, gg, "DUMP_SKIP_SYSTEM_LIBS", 1);
        Script.setConst(arrayList0, gg, "TAB_SETTINGS", 0);
        Script.setConst(arrayList0, gg, "TAB_SEARCH", 1);
        Script.setConst(arrayList0, gg, "TAB_SAVED_LIST", 2);
        Script.setConst(arrayList0, gg, "TAB_MEMORY_EDITOR", 3);
        Script.setConst(arrayList0, gg, "ASM_ARM", 4);
        Script.setConst(arrayList0, gg, "ASM_THUMB", 5);
        Script.setConst(arrayList0, gg, "ASM_ARM64", 6);
    }

    public static void loadData(BufferReader reader) {
        reader.reset();
        ArrayList list = new ArrayList();
        while(true) {
            try {
                int v = reader.readInt();
                if(v == 0) {
                    break;
                }
                list.add(new AddressItem(reader.readLong(), reader.readLongLong(), v));
            }
            catch(IOException e) {
                Log.e("???", e);
                ExceptionHandler.sendException(Thread.currentThread(), e, false);
                break;
            }
        }
        Script.memList = list;
        Script.memListUse += 100;
    }

    static String numberFromLua(String number) {
        return !ParserNumbers.isString(number) && ParserNumbers.typeAsm(number) == 0 ? ParserNumbers.fixSeparatorsToLocale(number) : number;
    }

    static String numberToLua(String number) {
        return ParserNumbers.fixSeparators(number);
    }

    public static String oneLiner(String script) {
        Globals globals = new Globals();
        globals.fullVersion = false;
        LuaC.install(globals);
        globals.load(new BaseLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new Utf8Lib());
        globals.load(new MathLib());
        globals.load(new OsLib());
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
        return globals.load(new ByteArrayInputStream(script.getBytes()), script, "t", globals).call().tojstring();
    }

    static boolean optboolean(LuaTable table, LuaValue key, boolean defval) {
        try {
            return table.get(key).optboolean(defval);
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    static int optint(LuaTable table, LuaValue key, int defval) {
        try {
            return table.get(key).optint(defval);
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    static String optjstring(LuaTable table, LuaValue key, String defval) {
        try {
            return table.get(key).optjstring(defval);
        }
        catch(LuaError e) {
            throw Script.badArg(key, e);
        }
    }

    public static void resetMetatables(Globals globals) {
        LuaNil.s_metatable = null;
        LuaNumber.s_metatable = null;
        LuaBoolean.s_metatable = null;
        LuaValue string = globals == null ? null : globals.get("string");
        LuaString.s_metatable = string != null && !string.isnil() ? LuaValue.tableOf(new LuaValue[]{Script.INDEX, string}) : null;
        LuaFunction.s_metatable = null;
        LuaThread.s_metatable = null;
    }

    void restoreState() {
        SavedState state = this.savedState;
        if(state == null) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Config.get(0x7F0B0081).value = state.memoryRanges;  // id:config_ranges
                Config.save();
            }
        });
    }

    void runScript() throws FileNotFoundException, IOException {
        PrintStream prev;
        String name;
        String s;
        LuaValue luaValue0;
        if(this.source instanceof java.io.File) {
            java.io.File file = (java.io.File)this.source;
            if(!file.exists()) {
                throw new FileNotFoundException("File not found: " + file);
            }
            this.globals.setWorkDir(file.getParentFile().getAbsolutePath());
            luaValue0 = this.globals.loadfile(file.getAbsolutePath());
            if(luaValue0 instanceof LuaClosure && ((LuaClosure)luaValue0).p instanceof LasmPrototype) {
                s = file.getAbsolutePath() + '.' + "3HihD_K" + ".lua";
                try(BufferedOutputStream bufferedOutputStream0 = new BufferedOutputStream(new FileOutputStream(s), 0x10000)) {
                    DumpState.dump(((LuaClosure)luaValue0).p, bufferedOutputStream0, false, false);
                    java.io.File file1 = new java.io.File(file.getAbsolutePath() + ".tail");
                    if(file1.exists()) {
                        FileInputStream fileInputStream0 = new FileInputStream(file1);
                        byte[] buf = new byte[0x10000];
                        int v;
                        while((v = fileInputStream0.read(buf)) > 0) {
                            bufferedOutputStream0.write(buf, 0, v);
                        }
                        fileInputStream0.close();
                    }
                }
                this.appendLog(Tools.stripColon(0x7F070323) + ":\n" + s);  // string:assembled "Successfully assembled to file:"
                return;
            }
            name = file.getName();
        }
        else {
            String s2 = this.source.toString();
            luaValue0 = this.globals.load(new ByteArrayInputStream(s2.getBytes()), s2, "t", this.globals);
            name = "string";
        }
        String name = String.valueOf(name) + '_' + "3HihD_N" + '_' + "1XzbGv";
        if((this.params & 1) != 0) {
            LuaClosure luaClosure0 = luaValue0.checkclosure();
            String s4 = String.valueOf(this.out) + '/' + name + ".lasm";
            PrintStream prev = Print.ps;
            try {
                Print.ps = new PrintStream(s4);
                Print.print(luaClosure0.p);
                Print.ps.close();
                Print.saveTail(luaClosure0, s4 + ".tail");
            }
            catch(Throwable e) {
                Log.e(("Failed disassemble " + luaClosure0 + " into \'" + s4 + '\''), e);
            }
            Print.ps = prev;
        }
        if((this.params & 2) != 0) {
            this.globals.baselib.setDebugLoad(String.valueOf(this.out) + '/' + name);
        }
        BufferedWriter log = null;
        if((this.params & 4) != 0) {
            try {
                log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.valueOf(this.out) + '/' + name + ".log.txt")), 0x10000);
            }
            catch(Throwable e) {
                Log.w("Failed open log file", e);
            }
        }
        DebugLog script$DebugLog0 = log == null ? null : new DebugLog(log, this.globals);
        try {
            Script.debugLog = script$DebugLog0;
            prev = Print.ps;
            Print.ps = this.globals.STDOUT;
            Script.resetMetatables(this.globals);
            luaValue0.call();
            Print.ps = prev;
        }
        catch(Throwable throwable3) {
            Print.ps = prev;
            if(log != null) {
                try {
                    Script.debugLog = null;
                    log.close();
                }
                catch(Throwable e) {
                    Log.w("Failed close log", e);
                }
            }
            if((this.params & 2) != 0) {
                this.globals.baselib.endDebugLoad();
            }
            throw throwable3;
        }
        if(log != null) {
            try {
                Script.debugLog = null;
                log.close();
            }
            catch(Throwable e) {
                Log.w("Failed close log", e);
            }
        }
        if((this.params & 2) != 0) {
            this.globals.baselib.endDebugLoad();
        }
    }

    void saveState() {
        SavedState script$SavedState0 = new SavedState(null);
        script$SavedState0.memoryRanges = Config.usedRanges;
        this.savedState = script$SavedState0;
    }

    public Thread self() {
        return this.thread;
    }

    private static void setConst(ArrayList arrayList0, LuaTable gg, String name, int value) {
        gg.rawset(name, value);
        if(arrayList0 != null) {
            arrayList0.add(new Const("gg." + name, value));
        }
    }

    private static void setValue(int index, LuaTable table, LuaString key, long address, long data, int type) {
        if(type == 8) {
            data ^= address;
        }
        switch(type) {
            case 16: {
                table.unsafehashset(index, key, Float.intBitsToFloat(((int)data)));
                return;
            }
            case 0x40: {
                table.unsafehashset(index, key, Double.longBitsToDouble(data));
                return;
            }
            default: {
                table.unsafehashset(index, key, DisplayNumbers.longToSigned(data, AddressItem.getPow(type), false));
            }
        }
    }

    public Script start() {
        this.thread.start();
        return this;
    }

    static AddressItem toItem(AddressItem item, LuaTable table, byte needValue) {
        long v2;
        boolean z = true;
        LuaString[] inds = Script.inds;
        long v = Script.checklong(table, inds[0]);
        int v1 = Script.checkType(Script.checkint(table, inds[2]));
        if(needValue == 2) {
            v2 = 0L;
        }
        else {
            LuaString key = inds[1];
            LuaValue luaValue0 = table.get(key);
            if(needValue != 0) {
                z = false;
            }
            v2 = Script.getData(v, key, luaValue0, v1, z);
        }
        if(item == null) {
            return new AddressItem(v, v2, v1);
        }
        item.address = v;
        item.data = v2;
        item.flags = v1;
        return item;
    }

    static SavedItem toSavedItem(LuaTable table) {
        SavedItem item = new SavedItem();
        Script.toItem(item, table, 0);
        item.name = Script.optjstring(table, Script.inds[3], null);
        item.freeze = Script.optboolean(table, Script.inds[4], false);
        item.setFreezeType(Script.optint(table, Script.inds[5], 0));
        long address = item.address;
        int type = item.flags;
        LuaString key = Script.inds[6];
        item.freezeFrom = Script.getData(address, key, table.get(key), type, true);
        LuaString key = Script.inds[7];
        item.freezeTo = Script.getData(address, key, table.get(key), type, true);
        return item;
    }

    static String toString(Throwable e) {
        if(e instanceof NumberFormatException_) {
            ((NumberFormatException_)e).forLua();
        }
        return e.toString();
    }

    static void waitNotify(Object o) {
        try {
            o.wait();
        }
        catch(InterruptedException e) {
            Log.w("Interrupted wait", e);
            Thread.currentThread().interrupt();
            throw new LuaError(e);
        }
    }
}

