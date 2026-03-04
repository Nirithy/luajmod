package android.ext;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Looper;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static class IgnoredException extends RuntimeException {
        private static final long serialVersionUID = 0x3085E1A093D6BB97L;

        public IgnoredException() {
        }

        public IgnoredException(String message) {
            super(message);
        }

        public IgnoredException(String message, Throwable cause) {
            super(message, cause);
        }

        public IgnoredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public IgnoredException(Throwable cause) {
            super(cause);
        }
    }

    public static class ThreadEx extends Thread {
        public ThreadEx(Runnable target, String name) {
            super(target, name);
            ExceptionHandler.install(this);
        }
    }

    public static final int BREVENT = 2;
    public static final int FIELDS = 2;
    public static final String LAST_EXCEPTION = "last_exception";
    public static final String LAST_EXCEPTION_TRACE = "last_exception_trace";
    private static final String NATIVE_CRASH_LOG = "client_crash.log";
    public static final int NEOSAFE = 5;
    public static final int PREVENT_RUNNING = 1;
    public static final int SECURITY = 3;
    private static final String STORE_EXCEPTION_DELIMITER = "@#%~~%#@";
    private static final String STORE_EXCEPTION_FILENAME = "store_ex.txt";
    public static final int WHETSTONE = 4;
    public static final int XPRIVACY;
    private static volatile Throwable last;
    private final Thread.UncaughtExceptionHandler previousHandler;
    private static volatile boolean restart;
    private final String type;

    static {
        ExceptionHandler.last = null;
        ExceptionHandler.restart = false;
    }

    private ExceptionHandler(Thread.UncaughtExceptionHandler previousHandler, String type) {
        this.previousHandler = previousHandler;
        this.type = type;
    }

    private static void alert(String crash, int fReason, DialogInterface.OnClickListener listener) {
        String[] arr_s = ExceptionHandler.getApps();
        Log.w((arr_s[fReason * 2] + " is reason of last fail"));
        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700BB)).setMessage(Tools.stringFormat(Re.s(0x7F0702A3), new Object[]{arr_s[fReason * 2]}) + "\n\n" + crash).setNeutralButton(Re.s(0x7F070156), new DialogInterface.OnClickListener() {  // string:last_run_failed "Last run failed"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Tools.openAppInfo(arr_s[fReason * 2 + 1]);
            }
        }).setNegativeButton(Re.s(0x7F07009D), null);  // string:ok "OK"
        if(listener != null) {
            alertDialog$Builder0.setPositiveButton(Re.s(0x7F070164), listener);  // string:fix_it "Fix it"
        }
        Alert.showAfterDaemonStart(alertDialog$Builder0);
    }

    public static void checkLastException() {
        ExceptionHandler.checkNativeCrash();
        try {
            SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
            if(sharedPreferences0.contains("last_exception")) {
                ExceptionHandler.showLastException(sharedPreferences0.getString("last_exception", "- unknown -"), sharedPreferences0.getString("last_exception_trace", "- unknown -"), new Runnable() {
                    @Override
                    public void run() {
                        sharedPreferences0.edit().remove("last_exception").commit();
                    }
                });
            }
        }
        catch(StackOverflowError e) {
            Log.badImplementation(e);
        }
        try {
            File file0 = new File(Tools.getFilesDirHidden(), "store_ex.txt");
            if(file0.exists()) {
                try {
                    BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(new FileInputStream(file0)));
                    StringBuilder out = new StringBuilder();
                    String s;
                    while((s = bufferedReader0.readLine()) != null) {
                        out.append(s);
                        out.append('\n');
                    }
                    bufferedReader0.close();
                    String[] arr_s = out.toString().split("@#%~~%#@", 2);
                    ExceptionHandler.showLastException(arr_s[0], arr_s[1], new Runnable() {
                        @Override
                        public void run() {
                            file0.delete();
                        }
                    });
                }
                catch(IOException e) {
                    Log.e("Failed load exception", e);
                }
            }
        }
        catch(StackOverflowError e) {
            Log.badImplementation(e);
        }
    }

    private static void checkNativeCrash() {
        File file0 = new File(Tools.getFilesDirHidden(), "client_crash.log");
        if((Config.ignore & 0x40L) == 0L && file0.exists() && file0.length() > 0L) {
            String s = "Native crash detected: " + file0.length();
            Log.w(s);
            try {
                StringBuilder log = new StringBuilder();
                log.append(s);
                log.append("\n\n");
                boolean send = false;
                try {
                    BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(new FileInputStream(file0)));
                    while(!Thread.interrupted()) {
                        String line = bufferedReader0.readLine();
                        if(line == null) {
                            break;
                        }
                        if(!send) {
                            String s2 = DaemonManager.getNativeCheck();
                            if(line.contains(s2) && !line.trim().equals(s2)) {
                                send = true;
                                goto label_19;
                            }
                            Log.w("Native crash check fail.");
                            break;
                        }
                    label_19:
                        log.append(line);
                        log.append('\n');
                    }
                    bufferedReader0.close();
                }
                catch(Throwable e) {
                    log.append(e.getMessage());
                    log.append('\n');
                    Log.w("Native crash load fail.", e);
                }
                if(send) {
                    String s3 = log.toString();
                    if(!ExceptionHandler.checkReasonCrash(s3)) {
                        if(!s3.contains(">>> " + Tools.getPackageName() + " <<<") || !s3.contains("backtrace:")) {
                            Log.w("Native crash skipped.");
                        }
                        else {
                            Log.w("Native crash sended.");
                            ExceptionHandler.sendMessage(s3);
                        }
                    }
                }
                file0.delete();
            }
            catch(Throwable e) {
                Log.w("Native crash check file fail.", e);
            }
        }
        try {
            file0.createNewFile();
        }
        catch(Throwable e) {
            Log.w("Native crash create file fail.", e);
        }
    }

    public static boolean checkReasonCrash(String crash) {
        String[] arr_s = ExceptionHandler.getApps();
        if(crash.contains(" Process  (") && crash.contains(") has open file /")) {
            Log.w("unmount is reason of last fail");
            Alert.showAfterDaemonStart(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700BB)).setMessage(Re.s(0x7F070290) + "\n\n" + crash).setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:last_run_failed "Last run failed"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    String internal;
                    try {
                        internal = Tools.getContext().getFilesDir().getAbsolutePath();
                    }
                    catch(Throwable e) {
                        internal = "error";
                        Log.w("Failed get files dir", e);
                    }
                    ConfigListAdapter.changePath(internal);
                }
            }).setNegativeButton(Re.s(0x7F07009D), null));  // string:ok "OK"
            return true;
        }
        if(crash.contains("CZAutoRunController")) {
            ExceptionHandler.alert(crash, 3, new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25<70krz0wr0uxq0lq0skrhql{rv0jdphjxdugldq2"));
            return true;
        }
        if(crash.contains(arr_s[8])) {
            ExceptionHandler.alert(crash, 4, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        RootDetector.runCmd("exec setprop persist.sys.whetstone.level 0", 15);
                        Tools.showToast(Re.s((arr_s[8] + ": " + Re.s(0x7F07009D))));  // string:ok "OK"
                    }
                    catch(Throwable unused_ex) {
                        Log.w(("Failed fix " + arr_s[8]));
                        Tools.showToast(Re.s((arr_s[8] + ": " + Re.s(0x7F070275))));  // string:patch_failed "Patch is not loaded."
                    }
                }
            });
            return true;
        }
        if(crash.contains(arr_s[10])) {
            ExceptionHandler.alert(crash, 5, null);
            return true;
        }
        return false;
    }

    public static String[] getApps() {
        return new String[]{"XPrivacy", "biz.bokhorst.xprivacy", "Prevent Running", "me.piebridge.forcestopgb", "Brevent", "me.piebridge.brevent", "Security", "com.chaozhuo.permission.controller", "Whetstone", "com.miui.whetstone", "NeoSafe", "cn.nubia.security2"};
    }

    public static void install() {
        ExceptionHandler.install(Thread.currentThread());
    }

    public static void install(Thread thread) {
        Thread.UncaughtExceptionHandler thread$UncaughtExceptionHandler0 = Thread.getDefaultUncaughtExceptionHandler();
        if(thread$UncaughtExceptionHandler0 == null || !(thread$UncaughtExceptionHandler0 instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(thread$UncaughtExceptionHandler0, "default"));
        }
        Thread.UncaughtExceptionHandler ex = thread.getUncaughtExceptionHandler();
        if(ex == null || !(ex instanceof ExceptionHandler)) {
            if(ex == null) {
                ex = thread.getThreadGroup();
            }
            thread.setUncaughtExceptionHandler(new ExceptionHandler(ex, "thread:" + thread.getName()));
        }
    }

    public static String sendException(Thread th, Throwable ex, boolean crash) {
        String toast = "GG crashed";
        String s1 = th.getName();
        if(crash) {
            Log.e(("Exception in thread " + s1), ex);
            Log.crash();
        }
        StringBuilder msg = new StringBuilder();
        msg.append("GG: ");
        msg.append(96.0f);
        msg.append(" [");
        msg.append(0x3E79);
        msg.append("]\nAndroid: ");
        msg.append(Build.VERSION.RELEASE);
        msg.append("\nSDK: ");
        msg.append(Build.VERSION.SDK_INT);
        msg.append("\nvSpace: ");
        msg.append("su");
        msg.append("\nThread: ");
        msg.append(s1);
        msg.append('\n');
        String s2 = Log.getStackTraceString(ex);
        boolean badSOE = (ex instanceof StackOverflowError || s2.contains("java.lang.StackOverflowError")) && s2.contains("com.lody.virtual.client.ipc.");
        msg.append(s2);
        if(s2.contains("ActivityNotFoundException")) {
            Tools.describeActivities(Apk.PACKAGE, msg);
        }
        msg.append("\nHash: ");
        try {
            msg.append(Installer.getHashes());
        }
        catch(Throwable e) {
            msg.append("???");
            Log.e("dbg", e);
        }
        String s3 = msg.toString();
        boolean haveContext = Tools.getContext() != null;
        boolean eglCause = s3.contains("EGL_") || s3.contains("egl") || s3.contains("HardwareRenderer");
        if(crash && haveContext && eglCause && (Config.configClient & 2) != 0) {
            Config.get(0x7F0B00AE).value = 0;  // id:config_acceleration
            Config.save();
            return "GG crashed. Hardware acceleration fail.";
        }
        if(crash && eglCause && (Config.configClient & 2) == 0 && BaseActivity.hw) {
            ExceptionHandler.restart = true;
            BaseActivity.hw = false;
            return "GG crashed. Hardware acceleration fail. Try use \'GG (SW)\' icon for start.";
        }
        if(crash && haveContext && s3.contains("Suggestion") && (Config.configClient & 8) != 0) {
            Config.get(0x7F0B00A0).value = 0;  // id:config_suggestions
            Config.save();
            return "GG crashed. Cause: suggestions.";
        }
        if(crash && haveContext && s3.contains("playSoundEffect") && (Config.configClient & 0x20) != 0) {
            Config.get(0x7F0B00AF).value = 0;  // id:config_use_sound_effects
            Config.save();
            return "GG crashed. Cause: sound effects.";
        }
        try {
            File file0 = new File(Tools.getSdcardPath(), "crash.txt");
            FileOutputStream fileOutputStream0 = new FileOutputStream(file0, true);
            BaseActivity.writeLogcatHeader(fileOutputStream0, file0.getAbsolutePath(), "");
            fileOutputStream0.write("".getBytes());
            fileOutputStream0.write(s3.getBytes());
            fileOutputStream0.close();
            toast = "GG crashed\nLog: " + file0.getAbsolutePath();
        }
        catch(Throwable e) {
            Log.w("Failed out crash to file", e);
        }
        try {
            File file1 = new File(Tools.getSdcardPath(), "logcat.txt");
            BaseActivity.writeLogcatHeader(new FileOutputStream(file1, true), file1.getAbsolutePath(), "");
            String[] arr_s = {"logcat", "-f", Tools.getRealPath(file1.getAbsolutePath()), "-d", "-v", "threadtime", "-b", "main", "-b", "system", "*:V"};
            try {
                if(!Tools.waitForTimeout(Tools.exec(arr_s), 15)) {
                    Log.w(("timeout fail 1: " + Arrays.toString(arr_s)), new RuntimeException());
                }
            }
            catch(Exception e) {
                Log.w("exec", e);
            }
            toast = toast + "\nLogcat: " + file1.getAbsolutePath();
        }
        catch(Throwable e) {
            Log.w("Failed out logcat to file", e);
        }
        try {
            Config.get(0x7F0B00C2).value = 0x3E79;  // id:record_logcat
            Config.save();
            toast = toast + "\nNext run rec logcat: " + BaseActivity.getLogcatFilename();
        }
        catch(Throwable e) {
            Log.w("Failed enable logcat on next run", e);
        }
        try {
            String[] arr_s1 = ExceptionHandler.getApps();
            if(crash && s3.contains(": XPrivacy")) {
                throw new RuntimeException(arr_s1[0] + " is cause");
            }
            if(badSOE) {
                Log.d("VSpace StackOverflowError - log not sended");
                return toast;
            }
            if(!UsageStats.sendLog(s3, true)) {
                throw new RuntimeException("Failed sendLogBlocking");
            }
        }
        catch(Throwable e) {
            Log.w("Cannot send exception info", e);
            if(crash) {
                ExceptionHandler.storeException(s3, s1, Log.getStackTraceString(ex));
                return toast;
            }
        }
        return toast;
    }

    public static void sendMessage(String msg) {
        UsageStats.sendLog(("GG: " + 96.0f + " [" + 0x3E79 + "]\nAndroid: " + Build.VERSION.RELEASE + "\nSDK: " + Build.VERSION.SDK_INT + "\nvSpace: " + Main.getVSpace() + '\n' + msg + "\nHash: " + Installer.getHashes()), true);
    }

    private static void showLastException(String message, String trace, Runnable onDone) {
        String[] arr_s = ExceptionHandler.getApps();
        int reason = -1;
        for(int i = 0; i < arr_s.length; i += 2) {
            if(message.contains(": " + arr_s[i])) {
                reason = i;
                break;
            }
        }
        android.ext.ExceptionHandler.1 listener = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if(which == -1) {
                    UsageStats.sendLog(message);
                    Tools.showToast(0x7F0700BC);  // string:thank_you "Thank you!"
                }
                if(which == -3) {
                    Tools.openAppInfo(arr_s[reason + 1]);
                }
                onDone.run();
            }
        };
        if(reason >= 0) {
            Log.w((arr_s[reason] + " is reason of last fail"));
            Alert.showAfterDaemonStart(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700BB)).setMessage(Tools.stringFormat(Re.s(0x7F0702A3), new Object[]{arr_s[reason]}) + "\n\n" + trace).setNeutralButton(Re.s(0x7F070156), listener).setNegativeButton(Re.s(0x7F07009D), listener));  // string:last_run_failed "Last run failed"
            return;
        }
        Alert.showAfterDaemonStart(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700BB)).setMessage(Re.s(0x7F0700BA) + "\n\n" + message).setPositiveButton(Re.s(0x7F07009B), listener).setNegativeButton(Re.s(0x7F07009C), listener));  // string:last_run_failed "Last run failed"
    }

    private void showToast(String message) {
        try {
            new Thread("showToast") {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        ToastManager.show(Toast.makeText(ToastManager.getContext(), message, 1));
                        Looper.loop();
                    }
                    catch(Throwable unused_ex) {
                    }
                }
            }.start();
            try {
                Thread.sleep(4000L);
            }
            catch(InterruptedException unused_ex) {
            }
        }
        catch(Throwable unused_ex) {
        }
    }

    public static void storeException(String message, String thread, String exceptionTrace) {
        if(Tools.getContext() != null) {
            Tools.getSharedPreferences().edit().putString("last_exception", message).putString("last_exception_trace", exceptionTrace).commit();
            return;
        }
        File file0 = new File(Tools.getFilesDirHidden(), "store_ex.txt");
        try {
            BufferedWriter bufferedWriter0 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file0)));
            bufferedWriter0.write(message + "@#%~~%#@" + exceptionTrace);
            bufferedWriter0.close();
        }
        catch(IOException e) {
            Log.e("Failed store exception", e);
        }
    }

    public static void storeException(Thread th, Throwable ex) {
        String s = th.getName();
        String s1 = Log.getStackTraceString(ex);
        ExceptionHandler.storeException(("GG: " + 96.0f + " [" + 0x3E79 + "]\nAndroid: " + Build.VERSION.RELEASE + "\nSDK: " + Build.VERSION.SDK_INT + "\nvSpace: " + Main.getVSpace() + "\nThread: " + s + '\n' + s1 + "\nHash: " + Installer.getHashes()), s, s1);
    }

    @Override
    public void uncaughtException(Thread th, Throwable ex) {
        if(ExceptionHandler.last == ex) {
            Log.d(("uncaughtException already called for: " + ex));
        }
        else {
            ExceptionHandler.last = ex;
            if(!(ex instanceof IgnoredException)) {
                this.showToast(ExceptionHandler.sendException(th, ex, true));
                if(ExceptionHandler.restart) {
                    BaseActivity.restartApp();
                }
            }
        }
        if(this.previousHandler != null) {
            this.previousHandler.uncaughtException(th, ex);
        }
    }
}

