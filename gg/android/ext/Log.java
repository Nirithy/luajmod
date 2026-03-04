package android.ext;

import android.content.Context;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Log {
    public static class LogWrapper {
        public static final int MAX_MSG_LEN = 0x3FF;
        public static final int MAX_TAG_LEN = 23;
        private static final boolean OFF;

        public static int d(String tag, String msg) {
            return LogWrapper.log(2, tag, msg, null);
        }

        public static int d(String tag, String msg, Throwable tr) {
            return LogWrapper.log(3, tag, msg, tr);
        }

        public static int e(String tag, String msg) {
            return LogWrapper.log(9, tag, msg, null);
        }

        public static int e(String tag, String msg, Throwable tr) {
            return LogWrapper.log(10, tag, msg, tr);
        }

        private static String fixTag(String in) {
            if(in == null) {
                return "null";
            }
            return in.length() <= 23 ? in : in.substring(0, 23);
        }

        public static int i(String tag, String msg) {
            return LogWrapper.log(4, tag, msg, null);
        }

        public static int i(String tag, String msg, Throwable tr) {
            return LogWrapper.log(5, tag, msg, tr);
        }

        private static int log(int meth, String tag, String msg, Throwable tr) {
            String s2 = LogWrapper.fixTag(tag);
            if(msg == null) {
                msg = "null";
            }
            int offset = 0;
            int v2 = msg.length();
            do {
                int len = v2 - offset <= 0x3FF ? v2 - offset : 0x3FF;
                String s3 = msg.substring(offset, offset + len);
                offset += len;
                switch(meth) {
                    case 0: {
                        try {
                            android.util.Log.e(s2, s3);
                            break;
                        label_12:
                            if(offset == v2) {
                                try {
                                    android.util.Log.e(s2, s3, tr);
                                }
                                catch(OutOfMemoryError unused_ex) {
                                    android.util.Log.e(s2, s3);
                                }
                            }
                            else {
                                android.util.Log.e(s2, s3);
                            }
                            break;
                        label_19:
                            android.util.Log.e(s2, s3);
                            break;
                        label_21:
                            if(offset == v2) {
                                try {
                                    android.util.Log.e(s2, s3, tr);
                                }
                                catch(OutOfMemoryError unused_ex) {
                                    android.util.Log.e(s2, s3);
                                }
                            }
                            else {
                                android.util.Log.e(s2, s3);
                            }
                            break;
                        label_28:
                            android.util.Log.e(s2, s3);
                            break;
                        label_30:
                            if(offset == v2) {
                                try {
                                    android.util.Log.e(s2, s3, tr);
                                }
                                catch(OutOfMemoryError unused_ex) {
                                    android.util.Log.e(s2, s3);
                                }
                            }
                            else {
                                android.util.Log.e(s2, s3);
                            }
                            break;
                        label_37:
                            android.util.Log.e(s2, s3);
                            break;
                        label_39:
                            if(offset == v2) {
                                try {
                                    android.util.Log.e(s2, s3, tr);
                                }
                                catch(OutOfMemoryError unused_ex) {
                                    android.util.Log.e(s2, s3);
                                }
                            }
                            else {
                                android.util.Log.e(s2, s3);
                            }
                            break;
                            try {
                            label_46:
                                android.util.Log.e(s2, "", tr);
                            }
                            catch(OutOfMemoryError unused_ex) {
                                android.util.Log.e(s2, "OOM for Log 2");
                            }
                            break;
                        label_50:
                            android.util.Log.e(s2, s3);
                            break;
                        label_52:
                            if(offset == v2) {
                                try {
                                    android.util.Log.e(s2, s3, tr);
                                }
                                catch(OutOfMemoryError unused_ex) {
                                    android.util.Log.e(s2, s3);
                                }
                            }
                            else {
                                android.util.Log.e(s2, s3);
                            }
                            break;
                        }
                        catch(OutOfMemoryError unused_ex) {
                            goto label_59;
                        }
                    }
                    case 1: {
                        goto label_12;
                    }
                    case 2: {
                        goto label_19;
                    }
                    case 3: {
                        goto label_21;
                    }
                    case 4: {
                        goto label_28;
                    }
                    case 5: {
                        goto label_30;
                    }
                    case 6: {
                        goto label_37;
                    }
                    case 7: {
                        goto label_39;
                    }
                    case 8: {
                        goto label_46;
                    }
                    case 9: {
                        goto label_50;
                    }
                    case 10: {
                        goto label_52;
                    label_59:
                        android.util.Log.e(s2, "OOM for Log 1");
                    }
                }
            }
            while(offset < v2);
            return 0;
        }

        public static int v(String tag, String msg) {
            return LogWrapper.log(0, tag, msg, null);
        }

        public static int v(String tag, String msg, Throwable tr) {
            return LogWrapper.log(1, tag, msg, tr);
        }

        public static int w(String tag, String msg) {
            return LogWrapper.log(6, tag, msg, null);
        }

        public static int w(String tag, String msg, Throwable tr) {
            return LogWrapper.log(7, tag, msg, tr);
        }

        public static int w(String tag, Throwable tr) {
            return LogWrapper.log(8, tag, null, tr);
        }
    }

    public static final String BAD_IMPLEMENTATION = "Bad implementation";
    private static final boolean DISABLE_LOG = true;
    private static final long FLUSH_LOG_EVERY_MS = 5000L;
    static String LOG_FILE;
    static volatile boolean closed;
    static final Runnable flushTask;
    private static long lastFlush;
    public static String lastSearchLog;
    static StringBuilder lastSearchLogCurrent;
    static volatile BufferedWriter writer;

    static {
        Log.closed = false;
        Log.writer = null;
        Log.lastFlush = 0L;
        Log.flushTask = () -> {
            if(Log.writer != null) {
                long v = System.currentTimeMillis();
                if(v - Log.lastFlush > 5000L || true) {
                    try {
                        try {
                            Log.writer.flush();
                        }
                        catch(NullPointerException unused_ex) {
                        }
                        Log.lastFlush = v;
                        return true;
                    }
                    catch(IOException e) {
                        LogWrapper.d("AndroidService", "Log write: I/O", e);
                        return false;
                    }
                }
            }
            return false;
        };
        Log.LOG_FILE = "/last_run.log";
        Log.lastSearchLog = "- none -";
        Log.lastSearchLogCurrent = null;
    }

    public static int badImplementation(Throwable tr) {
        return Log.w("Bad implementation", tr);
    }

    // 检测为 Lambda 实现
    public static void close() [...]

    public static void crash() {
        ThreadManager.runOnLogThread(() -> {
            ThreadManager.runOnLogThread(new Runnable() {
                @Override
                public void run() {
                    Log.closed = true;
                    if(Log.writer == null) {
                        return;
                    }
                    try {
                        Log.writer.flush();
                        Log.writer.close();
                        Log.writer = null;
                    }
                    catch(IOException e) {
                        LogWrapper.d("AndroidService", "Log close: I/O", e);
                    }
                }
            });
        });

        class android.ext.Log.4 implements Runnable {
            @Override
            public void run() {
                Log.close();
            }
        }

    }

    public static int d(String msg) {
        return Log.d("AndroidService", msg);
    }

    public static int d(String tag, String msg) {
        return LogWrapper.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return LogWrapper.d(tag, msg, tr);
    }

    public static int d(String msg, Throwable tr) {
        return Log.d("AndroidService", msg, tr);
    }

    public static int e(String msg) {
        return Log.e("AndroidService", msg);
    }

    public static int e(String tag, String msg) {
        return LogWrapper.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return LogWrapper.e(tag, msg, tr);
    }

    public static int e(String msg, Throwable tr) {
        return Log.e("AndroidService", msg, tr);
    }

    // 检测为 Lambda 实现
    public static boolean flush(boolean force) [...]

    private static File getPath() throws IOException {
        ArrayList list = new ArrayList();
        Context context0 = Tools.getContext();
        try {
            list.add(context0.getExternalFilesDir(null));
        }
        catch(Throwable e) {
            Log.w("Failed getExternalFilesDir", e);
        }
        try {
            list.add(context0.getExternalCacheDir());
        }
        catch(Throwable e) {
            Log.w("Failed getExternalCacheDir", e);
        }
        list.add(Tools.getFilesDir());
        list.add(Tools.getCacheDir());
        for(Object object0: list) {
            File path = (File)object0;
            if(path != null) {
                try {
                    path.mkdirs();
                    File file1 = new File(path, "write.tmp");
                    file1.delete();
                    if(file1.exists()) {
                        continue;
                    }
                    file1.createNewFile();
                    if(!file1.exists()) {
                        continue;
                    }
                    file1.delete();
                    return path;
                }
                catch(IOException e) {
                    Log.w(("check " + path), e);
                }
            }
        }
        throw new IOException("Failed find path");
    }

    public static String getStackTraceString(Throwable tr) {
        Throwable t;
        if(tr == null) {
            return "";
        }
        Throwable t = tr;
        while(true) {
            if(t == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                try {
                    tr.printStackTrace(pw);
                    goto label_20;
                }
                catch(OutOfMemoryError unused_ex) {
                    pw.write("OutOfMemoryError 1\n");
                    t = tr;
                }
                catch(Throwable e) {
                    pw.write("Exception on printStackTrace: " + e.getMessage());
                    if(false) {
                        goto label_14;
                    }
                    goto label_20;
                }
            label_14:
                while(t != null) {
                    try {
                        pw.write(t.toString());
                        pw.write("\n");
                        t = t.getCause();
                    }
                    catch(OutOfMemoryError unused_ex) {
                        pw.write("OutOfMemoryError 2\n");
                        break;
                    }
                }
            label_20:
                pw.flush();
                String ret = sw.toString();
                return ret == null ? "" : ret;
            }
            if(t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
    }

    public static void here() {
        Thread thread0 = Thread.currentThread();
        StackTraceElement el = thread0.getStackTrace()[3];
        Log.d(("here: " + thread0.getName() + " : " + thread0.getId() + " : " + el.getFileName() + " : " + el.getClassName() + " : " + el.getMethodName() + " : " + el.getLineNumber()));
    }

    public static int i(String msg) {
        return Log.i("AndroidService", msg);
    }

    public static int i(String tag, String msg) {
        return LogWrapper.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return LogWrapper.i(tag, msg, tr);
    }

    public static int i(String msg, Throwable tr) {
        return Log.i("AndroidService", msg, tr);
    }

    public static int iFull(String info) {
        int v = info.length();
        for(int pos = 0; pos < v; pos += 1003) {
            Log.i(info.substring(pos, (pos + 1003 >= v ? v : pos + 1003)));
        }
        return 0;
    }

    static void reopen() {
    }

    public static void runLogOnProcessErrStream(Process process, InputStream errStream) {
        if(process == null) {
            return;
        }
        new DaemonThread("runLogOnProcessErrStream") {
            @Override
            public void run() {
                int len;
                int v2;
                String line;
                int len;
                int[] keys;
                int error;
                boolean needCheck;
                BufferedReader bufferedReader0;
                super.run();
                try {
                    bufferedReader0 = new BufferedReader(new InputStreamReader(errStream), 0x200);
                    needCheck = false;
                    error = 0;
                    keys = null;
                    len = 0;
                    while(true) {
                    label_6:
                        if(android.ext.Log.5.interrupted()) {
                            return;
                        }
                        if(needCheck) {
                            try {
                                process.exitValue();
                                return;
                            }
                            catch(IllegalThreadStateException unused_ex) {
                                break;
                            }
                            catch(ArrayIndexOutOfBoundsException e) {
                            }
                            Log.badImplementation(e);
                            break;
                        }
                        goto label_13;
                    }
                }
                catch(NullPointerException e) {
                    Log.w("runLogOnProcessErrStream failed", e);
                    return;
                }
                needCheck = false;
                try {
                label_13:
                    String s = bufferedReader0.readLine();
                    if(s != null) {
                        line = s.trim();
                        if(line.length() == 0) {
                            goto label_6;
                        }
                        else {
                            goto label_17;
                        }
                    }
                    goto label_69;
                }
                catch(IOException unused_ex) {
                    len = len;
                    needCheck = true;
                    len = len;
                    goto label_6;
                }
                catch(InterruptedException unused_ex) {
                    return;
                }
                catch(IllegalArgumentException illegalArgumentException0) {
                    goto label_77;
                }
                catch(NullPointerException e) {
                    Log.w("runLogOnProcessErrStream failed", e);
                    return;
                }
            label_17:
                if(line.startsWith("KEY: ")) {
                    try {
                        v2 = Integer.parseInt(line.substring(5));
                        if(keys == null) {
                            keys = new int[0x200];
                        }
                        if(len == keys.length) {
                            keys = Arrays.copyOf(keys, keys.length + 0x200);
                        }
                        len = len + 1;
                        keys[len] = v2;
                        len = len;
                        goto label_6;
                    }
                    catch(NumberFormatException numberFormatException0) {
                    }
                    catch(IOException unused_ex) {
                        len = len;
                        needCheck = true;
                        len = len;
                        goto label_6;
                    }
                    catch(InterruptedException unused_ex) {
                        return;
                    }
                    catch(IllegalArgumentException illegalArgumentException0) {
                        goto label_77;
                    }
                    catch(NullPointerException e) {
                        Log.w("runLogOnProcessErrStream failed", e);
                        return;
                    }
                    len = len;
                    goto label_31;
                    try {
                        len = len + 1;
                        keys[len] = v2;
                        len = len;
                        goto label_6;
                    }
                    catch(NumberFormatException numberFormatException0) {
                    }
                    catch(IOException unused_ex) {
                        needCheck = true;
                        len = len;
                        goto label_6;
                    }
                    catch(InterruptedException unused_ex) {
                        return;
                    }
                    catch(IllegalArgumentException illegalArgumentException0) {
                        goto label_78;
                    }
                    catch(NullPointerException e) {
                        Log.w("runLogOnProcessErrStream failed", e);
                        return;
                    }
                    try {
                    label_31:
                        Log.w(("Failed parse: " + line), numberFormatException0);
                        len = len;
                        goto label_6;
                    }
                    catch(IOException unused_ex) {
                    }
                    catch(InterruptedException unused_ex) {
                        return;
                    }
                    catch(IllegalArgumentException illegalArgumentException0) {
                        goto label_78;
                    }
                    catch(NullPointerException e) {
                        Log.w("runLogOnProcessErrStream failed", e);
                        return;
                    }
                    needCheck = true;
                    len = len;
                    goto label_6;
                }
                else if(line.startsWith("KEY_END")) {
                    try {
                        if(keys != null && len > 0) {
                            Config.useKeys(Arrays.copyOf(keys, len));
                        }
                        keys = null;
                        len = 0;
                        goto label_6;
                    label_43:
                        if(line.startsWith("EVENT: ")) {
                            try {
                                int v4 = Integer.parseInt(line.substring(7));
                                if(MainService.instance == null) {
                                    goto label_6;
                                }
                                MainService.instance.onKeyCode(v4);
                            }
                            catch(NumberFormatException e) {
                                Log.w(("Failed parse: " + line), e);
                            }
                        }
                        else if(line.startsWith("BAD_KERNEL")) {
                            ThreadManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MainService.instance.onSendCode(0, 20, null);
                                }
                            });
                        }
                        else {
                            StringBuilder sb = Log.lastSearchLogCurrent;
                            if(line.contains(" dbg: ") || line.endsWith("Send code: 2")) {
                                sb = new StringBuilder();
                                Log.lastSearchLogCurrent = sb;
                            }
                            if(line.contains(" dbg: ") || line.contains(" clocks: ") || line.startsWith("MR[") || line.contains("::searchDone:")) {
                                if(sb == null) {
                                    sb = new StringBuilder();
                                    Log.lastSearchLogCurrent = sb;
                                }
                                sb.append(line);
                                sb.append('\n');
                            }
                            if(!line.contains("::searchDone:") && !line.endsWith("Send code: 3")) {
                                goto label_6;
                            }
                            if(sb != null) {
                                Log.lastSearchLog = sb.toString();
                            }
                            Log.lastSearchLogCurrent = null;
                        }
                        goto label_6;
                    label_69:
                        android.ext.Log.5.sleep(500L);
                        goto label_6;
                    }
                    catch(IOException unused_ex) {
                        len = len;
                        needCheck = true;
                        len = len;
                        goto label_6;
                    }
                    catch(InterruptedException unused_ex) {
                        return;
                    }
                    catch(IllegalArgumentException illegalArgumentException0) {
                        goto label_77;
                    }
                    catch(NullPointerException e) {
                        Log.w("runLogOnProcessErrStream failed", e);
                        return;
                    }
                }
                else {
                    goto label_43;
                }
                goto label_69;
            label_77:
                len = len;
                try {
                label_78:
                    Log.badImplementation(illegalArgumentException0);
                    ++error;
                    if(error > 30) {
                        return;
                    }
                    len = len;
                    goto label_6;
                }
                catch(NullPointerException e) {
                    Log.w("runLogOnProcessErrStream failed", e);
                }
            }
        }.start();
    }

    public static void s(String line) {
        synchronized(Log.class) {
            if(!ThreadManager.isInLogThread() && !Log.closed) {
                ThreadManager.runOnLogThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Tools.getContext() != null && Log.writer != null) {
                            try {
                                Log.writer.append(line);
                                Log.writer.newLine();
                                if(!Log.flush(false)) {
                                    Handler handler0 = ThreadManager.getHandlerLogThread();
                                    handler0.removeCallbacks(Log.flushTask);
                                    handler0.postDelayed(Log.flushTask, 5000L);
                                }
                            }
                            catch(IOException e) {
                                LogWrapper.d("AndroidService", "Log write: I/O", e);
                                String s = e.getMessage();
                                if(s != null && s.contains("EBADF")) {
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public static int v(String msg) {
        return Log.v("AndroidService", msg);
    }

    public static int v(String tag, String msg) {
        return LogWrapper.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return LogWrapper.v(tag, msg, tr);
    }

    public static int v(String msg, Throwable tr) {
        return Log.v("AndroidService", msg, tr);
    }

    public static int w(String msg) {
        return Log.w("AndroidService", msg);
    }

    public static int w(String tag, String msg) {
        return LogWrapper.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return LogWrapper.w(tag, msg, tr);
    }

    public static int w(String msg, Throwable tr) {
        return Log.w("AndroidService", msg, tr);
    }

    public static int wt(String tag, Throwable tr) {
        return LogWrapper.w(tag, tr);
    }

    class android.ext.Log.1 implements Runnable {
        @Override
        public void run() {
            Log.flush(true);
        }
    }

}

