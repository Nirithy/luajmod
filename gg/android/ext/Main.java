package android.ext;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.fix.LayoutInflater;
import android.fix.SystemService;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.DisplayMetrics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static final int FROM_ACTIVITY = 2;
    public static final int FROM_SERVICE = 1;
    private static final String IMPROVE_TRANSLATE = "improve-translate";
    public static final int NO;
    static volatile int doRestart;
    public static volatile boolean exit;
    static volatile boolean loaded;

    static {
        Main.exit = false;
        Main.doRestart = 0;
        Main.loaded = false;
    }

    public static void checkAutoTranslation() {
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
                    String s = sharedPreferences0.getString("improve-translate", "");
                    String s1 = Re.s(0x7F070000) + ':' + Re.s(0x7F070083);  // string:version_number "96.0"
                    if(!s1.equals(s)) {
                        String s2 = Re.s(0x7F070022);  // string:improve_translation_en "This translation was created by an automatic translator. 
                                                       // You can improve it by using our collective translation service."
                        if(Re.s(0x7F070084).contains(s2)) {  // string:front_text "If you have any questions about __app_name__ or its use, please 
                                                             // visit our forums at __forum__"
                            ThreadManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Alert.show(Alert.create().setMessage(Re.s(0x7F070023)).setPositiveButton(Re.s(0x7F070224), new android.ext.Main.17.1(this, sharedPreferences0, s1)).setNegativeButton(Re.s(0x7F0700B9), new android.ext.Main.17.1(this, sharedPreferences0, s1)));  // string:improve_translation_ "__improve_translation_en__\n\n__improve_translation__"
                                }
                            });
                        }
                    }
                }
                catch(Throwable e) {
                    Log.e("checkAutoTranslation", e);
                }

                class android.ext.Main.17.1 implements DialogInterface.OnClickListener {
                    android.ext.Main.17.1(SharedPreferences sharedPreferences0, String s) {
                    }

                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == -1) {
                            new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:8770li0|rx0zdqw0wr0dgg0d0qhz0wudqvodwlrq0ru0lpsuryh0dq0h{lvwlqj2").onClick(dialog, -1);
                        }
                        this.val$storage.edit().putString("improve-translate", this.val$valid).commit();
                    }
                }

            }
        }, "checkAutoTranslation").start();
    }

    private static void checkBadApps() {
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                if((Config.ignore & 8L) == 0L) {
                    StringBuilder str = new StringBuilder();
                    String[] arr_s = ExceptionHandler.getApps();
                    for(int i = 2; i < arr_s.length; i += 2) {
                        String check = arr_s[i];
                        if(Tools.isPackageInstalled(arr_s[i + 1])) {
                            str.append('\n');
                            str.append(check);
                            str.append(" (");
                            str.append(arr_s[i + 1]);
                            str.append(')');
                        }
                    }
                    if(str.length() != 0) {
                        str.append('\n');
                        ThreadManager.runOnUiThread(Main.showSkipDialog(BaseActivity.context, Tools.stringFormat(Re.s(0x7F0702A4), new Object[]{str.toString()}), 8L, false));  // string:killers_found "You have installed applications that can forcibly close __app_name__:\n__s__\nTo 
                                                                                                                                                                                // avoid this, delete them, or configure them so that they do not terminate the __app_name__.\n\nIf 
                                                                                                                                                                                // you do not experience problems with the unexpected closing of the __app_name__, 
                                                                                                                                                                                // then no action is required, and you can ignore this message."
                    }
                }
            }
        }, "checkBadApps").start();
    }

    static void checkInstrumentation(byte instr) {
        int v = 0;
        File file0 = new File(Tools.getFilesDirHidden(), "instr.check");
        if(instr == 0) {
            try {
                FileOutputStream os = new FileOutputStream(file0);
                try {
                    os.write("15993".getBytes());
                }
                finally {
                    os.close();
                }
            }
            catch(Throwable e) {
                Log.e("Fail set instr file", e);
            }
            return;
        }
        if(instr == 1) {
            if(!file0.exists()) {
                Log.e(("No instr file: " + file0));
            }
            file0.delete();
            return;
        }
        if(file0.exists()) {
            try {
                FileInputStream is = new FileInputStream(file0);
                try {
                    byte[] buffer = new byte[12];
                    int read = is.read(buffer);
                    if(read > 0) {
                        v = Integer.parseInt(new String(buffer, 0, read));
                    }
                    if(v == 0x3E79 && Config.contextSource == 0) {
                        Config.get(0x7F0B00BC).value = 1;  // id:config_context_source
                        Config.save();
                    }
                }
                finally {
                    is.close();
                }
            }
            catch(Throwable e) {
                Log.e("Fail get instr file", e);
            }
            file0.delete();
            return;
        }
        try {
        }
        catch(Throwable e) {
            Log.e("Fail set instr file", e);
        }
    }

    private static void checkLocale() {
        if((Config.ignore & 0x80L) == 0L) {
            try {
                String s = Tools.stringFormat("%e", new Object[]{12000000243433737000000000000000000.0f});
                if(s.indexOf(101) == -1 && s.indexOf(69) == -1 && s.indexOf(1077) == -1 && s.indexOf(1045) == -1) {
                    ThreadManager.runOnUiThread(Main.showSkipDialog(BaseActivity.context, Tools.stringFormat(Re.s(0x7F07006E), new Object[]{"1.2E34", s}), 0x80L, false));  // string:wrong_float_ "__wrong_float__\n\n__wrong_float2__"
                    return;
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        else {
            return;
        }
        Log.badImplementation(e);
    }

    static void checkPhoenixOs() {
        if(Tools.isPackageInstalled("com.chaozhuo.permission.controller") && (Config.ignore & 4L) == 0L) {
            String s = Re.s(0x7F070236) + "\n\n" + "http://gameguardian.net/forum/gallery/image/294-how-to-run-in-phoenixos-gameguardian/";  // string:need_allow_background "On this firmware, you must allow working __app_name__ 
                                                                                                                                             // in the background, in the system settings."
            ThreadManager.runOnUiThread(Main.showSkipDialog(BaseActivity.context, s, 4L, true));
        }
    }

    private static void checkRandomName() {
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
            }
        }, "checkRandomName").start();
    }

    private static void checkSelinux() {
        File file0 = new File(Tools.getFilesDirHidden(), "sel.txt");
        try {
            if(!file0.exists()) {
                return;
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return;
        }
        if((Config.configDaemon & 4) != 0 || Build.VERSION.SDK_INT < 17) {
            file0.delete();
            return;
        }
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700BB)).setItems(new String[]{Re.s(0x7F0701F6), Re.s(0x7F0700B9)}, new DialogInterface.OnClickListener() {  // string:last_run_failed "Last run failed"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                file0.delete();
                if(which != 0) {
                    return;
                }
                Config.get(0x7F0B00BA).value = 1;  // id:selinux
                Config.save();
                BaseActivity.restartApp();
            }
        }).create());
    }

    private static void checkSlowEmulator() {
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                if((Config.ignore & 0x20L) == 0L) {
                    String str = null;
                    for(int v = 0; v < 0x8F; ++v) {
                        if(new File(new String[]{"/system/priv-app/com.bluestacks.settings", "/system/priv-app/com.bluestacks.settings/com.bluestacks.settings.apk", "/system/priv-app/com.bluestacks.bstfolder", "/system/priv-app/com.bluestacks.bstfolder/com.bluestacks.bstfolder.apk", "/system/priv-app/com.bluestacks.BstCommandProcessor", "/system/priv-app/com.bluestacks.BstCommandProcessor/com.bluestacks.BstCommandProcessor.apk", "/boot/android/android/system/priv-app/com.bluestacks.settings", "/boot/android/android/system/priv-app/com.bluestacks.settings/com.bluestacks.settings.apk", "/boot/android/android/system/priv-app/com.bluestacks.bstfolder", "/boot/android/android/system/priv-app/com.bluestacks.bstfolder/com.bluestacks.bstfolder.apk", "/boot/android/android/system/priv-app/com.bluestacks.BstCommandProcessor", "/boot/android/android/system/priv-app/com.bluestacks.BstCommandProcessor/com.bluestacks.BstCommandProcessor.apk", "/storage/emulated/0/Android/data/com.bluestacks.settings", "/storage/emulated/0/Android/data/com.bluestacks.home", "/mnt/runtime/write/emulated/0/Android/data/com.bluestacks.settings", "/mnt/runtime/write/emulated/0/Android/data/com.bluestacks.home", "/mnt/runtime/read/emulated/0/Android/data/com.bluestacks.settings", "/mnt/runtime/read/emulated/0/Android/data/com.bluestacks.home", "/mnt/runtime/default/emulated/0/Android/data/com.bluestacks.settings", "/mnt/runtime/default/emulated/0/Android/data/com.bluestacks.home", "/data/.bluestacks.prop", "/data/misc/profiles/cur/0/com.bluestacks.settings", "/data/misc/profiles/cur/0/com.bluestacks.BstCommandProcessor", "/data/misc/profiles/cur/0/com.bluestacks.appmart", "/data/misc/profiles/cur/0/com.bluestacks.bstfolder", "/data/misc/profiles/cur/0/com.bluestacks.home", "/data/misc/profiles/cur/0/com.bluestacks.filemanager", "/data/misc/profiles/cur/0/com.bluestacks.appguidance", "/data/misc/profiles/ref/com.bluestacks.settings", "/data/misc/profiles/ref/com.bluestacks.BstCommandProcessor", "/data/misc/profiles/ref/com.bluestacks.appmart", "/data/misc/profiles/ref/com.bluestacks.bstfolder", "/data/misc/profiles/ref/com.bluestacks.home", "/data/misc/profiles/ref/com.bluestacks.filemanager", "/data/misc/profiles/ref/com.bluestacks.appguidance", "/data/media/0/Android/data/com.bluestacks.settings", "/data/media/0/Android/data/com.bluestacks.home", "/data/data/com.bluestacks.settings", "/data/data/com.bluestacks.BstCommandProcessor", "/data/data/com.bluestacks.appmart", "/data/data/com.bluestacks.bstfolder", "/data/data/com.bluestacks.home", "/data/data/com.bluestacks.filemanager", "/data/data/com.bluestacks.appguidance", "/data/downloads/com.bluestacks.appmart", "/data/downloads/com.bluestacks.home", "/data/downloads/com.bluestacks.filemanager", "/data/app-lib/com.bluestacks.settings", "/data/app-lib/com.bluestacks.BstCommandProcessor", "/data/app-lib/com.bluestacks.appmart", "/data/app-lib/com.bluestacks.bstfolder", "/data/app-lib/com.bluestacks.home", "/data/app-lib/com.bluestacks.filemanager", "/data/user_de/0/com.bluestacks.settings", "/data/user_de/0/com.bluestacks.BstCommandProcessor", "/data/user_de/0/com.bluestacks.appmart", "/data/user_de/0/com.bluestacks.bstfolder", "/data/user_de/0/com.bluestacks.home", "/data/user_de/0/com.bluestacks.filemanager", "/data/user_de/0/com.bluestacks.appguidance", "/data/dalvik-cache/x86/system@priv-app@com.bluestacks.BstCommandProcessor@com.bluestacks.BstCommandProcessor.apk@classes.dex", "/data/dalvik-cache/x86/system@priv-app@com.bluestacks.bstfolder@com.bluestacks.bstfolder.apk@classes.dex", "/data/dalvik-cache/x86/data@downloads@com.bluestacks.appmart@com.bluestacks.appmart.apk@classes.dex", "/data/dalvik-cache/x86/data@downloads@com.bluestacks.filemanager@com.bluestacks.filemanager.apk@classes.dex", "/data/dalvik-cache/x86/data@downloads@com.bluestacks.home@com.bluestacks.home.apk@classes.dex", "/data/dalvik-cache/x86/system@priv-app@com.bluestacks.settings@com.bluestacks.settings.apk@classes.dex", "/data/dalvik-cache/x86_64/system@priv-app@com.bluestacks.BstCommandProcessor@com.bluestacks.BstCommandProcessor.apk@classes.dex", "/data/dalvik-cache/x86_64/system@priv-app@com.bluestacks.bstfolder@com.bluestacks.bstfolder.apk@classes.dex", "/data/dalvik-cache/x86_64/data@downloads@com.bluestacks.appmart@com.bluestacks.appmart.apk@classes.dex", "/data/dalvik-cache/x86_64/data@downloads@com.bluestacks.filemanager@com.bluestacks.filemanager.apk@classes.dex", "/data/dalvik-cache/x86_64/data@downloads@com.bluestacks.home@com.bluestacks.home.apk@classes.dex", "/data/dalvik-cache/x86_64/system@priv-app@com.bluestacks.settings@com.bluestacks.settings.apk@classes.dex", "/system/lib64/libbstfolder_jni.so", "/system/lib64/egl/libGLES_bst.so", "/system/lib64/hw/gralloc.bst.so", "/system/lib/libbstfolder_jni.so", "/system/lib/egl/libGLES_bst.so", "/system/lib/hw/gralloc.bst.so", "/system/bin/bstsvcmgrtest", "/system/bin/bstshutdown_core", "/system/bin/bstfolderd", "/system/bin/bstfolder_ctl", "/system/bin/bstime", "/system/bin/bstsyncfs", "/system/bin/bstshutdown", "/system/xbin/bstk", "/storage/emulated/0/.bstshutdown_sync", "/mnt/runtime/write/emulated/0/.bstshutdown_sync", "/mnt/runtime/read/emulated/0/.bstshutdown_sync", "/mnt/runtime/default/emulated/0/.bstshutdown_sync", "/data/media/0/.bstshutdown_sync", "/data/downloads/.bstABI2Apps", "/sys/devices/virtual/bstsensor", "/sys/devices/virtual/misc/bst_ime", "/sys/devices/virtual/misc/bst_gps", "/sys/devices/virtual/misc/bstpgaipc", "/sys/class/bstsensor", "/sys/class/misc/bst_ime", "/sys/class/misc/bst_gps", "/sys/class/misc/bstpgaipc", "/sys/bus/pci/drivers/bstsensor", "/sys/bus/pci/drivers/bstvideo", "/sys/bus/pci/drivers/bstaudio", "/sys/bus/pci/drivers/bstcamera", "/sys/bus/pci/drivers/bstinput", "/sys/bus/pci/drivers/bstpgaipc", "/sys/module/bstsensor", "/sys/module/bstvideo", "/sys/module/bstaudio", "/sys/module/bstcamera", "/sys/module/videobuf_core/holders/bstcamera", "/sys/module/bstinput", "/sys/module/bstpgaipc", "/proc/irq/16/bstcamera", "/proc/irq/17/bstaudio", "/proc/irq/18/bstinput", "/proc/irq/22/bstpgaipc", "/proc/irq/23/bstsensor", "/proc/bstid", "/proc/asound/bstaudio", "/proc/bstfolder_exports", "/boot/android/android/system/lib64/egl/libGLES_bst.so", "/boot/android/android/system/lib64/hw/gralloc.bst.so", "/boot/android/android/system/lib/egl/libGLES_bst.so", "/boot/android/android/system/lib/hw/gralloc.bst.so", "/boot/android/android/system/bin/bstsvcmgrtest", "/boot/android/android/system/bin/bstshutdown_core", "/boot/android/android/system/bin/bstfolderd", "/boot/android/android/system/bin/bstfolder_ctl", "/boot/android/android/system/bin/bstime", "/boot/android/android/system/bin/bstsyncfs", "/boot/android/android/system/bin/bstshutdown", "/boot/android/android/system/xbin/bstk", "/boot/bstsetup.env", "/boot/bstmods", "/dev/bstpgaipc", "/dev/bst_gps", "/dev/bst_ime", "/dev/bstgyro", "/dev/bstorie", "/dev/bstmegn", "/dev/bstacce", "/dev/socket/bstfolderd"}[v]).exists()) {
                            str = Re.s(0x7F070058);  // string:bluestacks "Bluestacks"
                            break;
                        }
                    }
                    if(str != null) {
                        ThreadManager.getHandlerUiThread().postDelayed(Main.showSkipDialog(null, Tools.stringFormat(Tools.stripColon(0x7F0702B8), new Object[]{str}) + Re.s(0x7F070059), 0x20L, true), 30000L);  // string:slow_emulator "You are using the \"__s__\" emulator. It is slow and contains 
                                                                                                                                                                                                                 // errors. Because of this, the search can be very slow.\n\nWe recommend using other 
                                                                                                                                                                                                                 // emulators:"
                    }
                }
            }
        }, "checkSlowEmulator").start();
    }

    public static void die() {
        Main.unloadService();
        LogWrapper.i("AndroidService", "Main: die");
        try {
            System.exit(0);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 14");
        try {
            Runtime.getRuntime().halt(0);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 15");
        try {
            Process.killProcess(Process.myPid());
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 16");
        try {
            RootDetector.runCmd(("exec kill " + Process.myPid()), 45);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 17");
        throw new IgnoredException("Failed exit from the app normally");
    }

    public static void exit() {
        Log.i("Main: exit");
        Main.exit = true;
        if(MainService.instance != null) {
            try {
                MainService.instance.updateNotification(true);
                MainService.instance.onDaemonExit();
            }
            catch(Throwable e) {
                Log.w("Exception on exit", e);
            }
        }
        LogWrapper.d("AndroidService", "exit: 1");
        new File(Tools.getFilesDirHidden(), "sel.txt").delete();
        LogWrapper.d("AndroidService", "exit: 2");
        try {
            Log.close();
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 3");
        boolean wait = BootstrapService.instance != null;
        android.ext.Main.11 forExit = new Runnable() {
            @Override
            public void run() {
                LogWrapper.d("AndroidService", "wait 4");
                ThreadManager.getHandlerUiThread().removeCallbacks(this);
                Main.exit2();
            }
        };
        LogWrapper.d("AndroidService", "wait 1: " + wait);
        if(wait) {
            BootstrapService.forExit = forExit;
            BootstrapService.instance.stopService();
        }
        boolean wait = Main.unloadService() || wait;
        LogWrapper.d("AndroidService", "wait 2: " + wait);
        if(!wait) {
            Main.exit2();
            return;
        }
        if(BootstrapService.forExit == null) {
            BootstrapService.forExit = forExit;
        }
        ThreadManager.getHandlerUiThread().postDelayed(forExit, 3000L);
    }

    static void exit2() {
        try {
            Main.sendRestartIntent();
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 4");
        try {
            ThreadManager.exit();
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 5");
        try {
            BootstrapInstrumentation.exit();
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 6");
        File file0 = new File(Tools.getCacheDir(), "skipFinalization");
        LogWrapper.d("AndroidService", "exit: 7");
        if(!file0.exists()) {
            LogWrapper.d("AndroidService", "exit: 8");
            try {
                file0.createNewFile();
            }
            catch(Throwable e) {
                LogWrapper.e("AndroidService", "OnExit", e);
            }
            LogWrapper.d("AndroidService", "exit: 9");
            try {
                System.runFinalization();
            }
            catch(Throwable e) {
                LogWrapper.e("AndroidService", "OnExit", e);
            }
            LogWrapper.d("AndroidService", "exit: 10");
            try {
                file0.delete();
            }
            catch(Throwable e) {
                LogWrapper.e("AndroidService", "OnExit", e);
            }
        }
        LogWrapper.d("AndroidService", "exit: 11");
        try {
            RootDetector.runCmd(("exec am force-stop " + Tools.getPackageName()), 45);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 12");
        try {
            RootDetector.runCmd(("exec am kill " + Tools.getPackageName()), 45);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "OnExit", e);
        }
        LogWrapper.d("AndroidService", "exit: 13");
        Main.die();
    }

    private static void failedStartInstrumentation() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = Re.s(0x7F070166);  // string:instrumentation_failed "Failed to start Instrumentation. To properly run 
                                                  // this application on your firmware, you need to allow start Instrumentation in system 
                                                  // settings for this application. For more information, look at the manual of your 
                                                  // firmware/ROM, on special forums, or in a Google search."
                    Alert.show(Alert.create(BaseActivity.context).setNegativeButton(Re.s(0x7F0700B8), new ExitListener(700)).setMessage(s).create());  // string:exit "Exit"
                }
                catch(Throwable e) {
                    throw new RuntimeException("Failed notify about instrumentation", e);
                }
            }
        });
    }

    static ComponentName getInstrumentationComponent() {
        try {
            return new ComponentName(Tools.getPackageName(), "com.ggdqo.Instrumentation");
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return null;
        }
    }

    static String getScreenSize() {
        DisplayMetrics displayMetrics0;
        int height;
        int width = 0;
        try {
            height = 0;
            Resources resources0 = Tools.getContext().getResources();
            Configuration configuration0 = resources0.getConfiguration();
            if(Build.VERSION.SDK_INT >= 13) {
                width = configuration0.screenWidthDp;
                height = configuration0.screenHeightDp;
            }
            displayMetrics0 = resources0.getDisplayMetrics();
            if(width == 0 && displayMetrics0 != null && displayMetrics0.density != 0.0f) {
                width = (int)(((float)displayMetrics0.widthPixels) / displayMetrics0.density);
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return width + 'x' + height;
        }
        if(height == 0 && displayMetrics0 != null && displayMetrics0.density != 0.0f) {
            height = (int)(((float)displayMetrics0.heightPixels) / displayMetrics0.density);
        }
        return width + 'x' + height;
    }

    static DialogInterface.OnClickListener getSkipListener(long mask) {
        return new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Config.ignore |= mask;
                Config.save();
            }
        };
    }

    // 去混淆评级： 中等(60)
    static String getVSpace() [...] // 潜在的解密器

    public static boolean isRootMode() {
        return Main.isRootMode(null, null);
    }

    public static boolean isRootMode(Context context) {
        return Main.isRootMode(context, null);
    }

    public static boolean isRootMode(Context context, DialogInterface.OnDismissListener listener) {
        return true;
    }

    static boolean loadService() {
        boolean res = false;
        try {
            BootstrapService.fromApp = true;
            String s = Tools.getPackageName();
            ComponentName componentName0 = BaseActivity.context.startService(new Intent().setClassName(s, "com.ggdqo.AnalyticsService"));
            if(componentName0 != null) {
                res = true;
            }
            Main.logDebug(10, (componentName0 == null ? null : componentName0.toString() + ' ' + componentName0.toShortString()));
        }
        catch(Throwable e) {
            Log.e("Failed load Service", e);
        }
        return res;
    }

    static void logDebug(int pos, String str) {
        Log.d(("main " + pos + ':' + Process.myUid() + ": " + BootstrapInstrumentation.isBootstraped() + ' ' + (BaseActivity.instance == null ? null : Boolean.valueOf(BaseActivity.instance.installMode)) + ' ' + BootstrapInstrumentation.mInstance + ' ' + BootstrapService.instance + ' ' + str));
    }

    public static void onStart() {
        BaseActivity.instance.setInstallerMode(2);
        if(Main.loaded) {
            return;
        }
        Main.loaded = true;
        Debug.showInfo();
        Log.iFull(("GG started: " + 96.0f + " (" + 0x3E79 + ") on " + Debug.getAndroidVersion()));
        new FloatCheck(false);
        new SavedItem(0L, 0L, 0);
        BaseActivity.instance.checkPermissions();
        CheckFloatingWindow.checkAPI();
        Bootstrap.startService();
        History.init();
        new Miui().runCheck();
        ExceptionHandler.checkLastException();
        Main.logDebug(5, null);
        Main.checkAutoTranslation();
        Main.checkSlowEmulator();
        Main.checkBadApps();
        Main.checkRandomName();
        Main.checkLocale();
        Main.checkSelinux();
        ListManager.updateOldLists();
        ConfigListAdapter.hideIcon(3);
    }

    public static void sendRestartIntent() {
        if(Main.doRestart == 0) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context0 = Tools.getContext();
                    ComponentName componentName0 = Main.getInstrumentationComponent();
                    LogWrapper.d("AndroidService", "Restart: true " + Config.contextSource + ' ' + componentName0);
                    if(Config.contextSource == 0 && componentName0 != null && context0.startInstrumentation(componentName0, null, BaseActivity.getHwBundle())) {
                        LogWrapper.d("AndroidService", "Restart 1");
                    }
                    LogWrapper.d("AndroidService", "Restart 2");
                    String s = Tools.getPackageName();
                    Tools.setComponentEnabledSetting(200, new ComponentName(s, "com.ggdqo.ActivityMain"), 1);
                    Intent intent0 = Tools.getStartIntent(context0, s, "com.ggdqo.ActivityMain");
                    Log.d(("Restart: " + intent0));
                    PendingIntent pendingIntent0 = PendingIntent.getActivity(context0, 0x1E240, intent0, 0x10000000);
                    ((AlarmManager)context0.getSystemService("alarm")).set(1, System.currentTimeMillis() + 1000L, pendingIntent0);
                    Main.die();
                }
                catch(Throwable e) {
                    throw new RuntimeException("Failed restart", e);
                }
            }
        });
    }

    static void setContext() {
        Context context = null;
        try {
            switch(Config.contextSource) {
                case 2: {
                    context = BaseActivity.instance.createPackageContext("com.ggdqo", 3);
                    break;
                }
                case 3: {
                    context = BaseActivity.appContext;
                    break;
                }
                case 4: {
                    context = BaseActivity.instance.getBaseContext();
                }
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        Log.d(("setContext: " + Config.contextSource + ' ' + context));
        if(context != null) {
            try {
                ((LayoutInflater)SystemService.wrap(LayoutInflater.from(context))).inflate(0x7F04001D, null);  // layout:service_dialog
            }
            catch(Throwable e) {
                Log.w(("Failed use context: " + context), e);
                context = null;
            }
        }
        if(context != null) {
            Tools.init(context);
            MainService.context = ServiceContext.wrap(context);
            Main.useNotificationService();
            Main.onStart();
            return;
        }
        BootstrapService.allow = true;
        if(!Main.loadService()) {
            Tools.showToast((Tools.stripColon(0x7F0702E9) + ": " + Re.s(0x7F07005E)));  // string:could_not_use "Could not use:"
            BaseActivity.instance.switchContext();
        }
    }

    static Runnable showSkipDialog(Context context, String message, long mask, boolean makeClickable) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    AlertDialog alertDialog0 = Alert.create((context == null ? Tools.getContext() : context)).setMessage(message).setPositiveButton(Re.s(0x7F07009D), null).setNeutralButton(Re.s(0x7F0700B9), Main.getSkipListener(mask)).create();  // string:ok "OK"
                    Alert.show(alertDialog0);
                    Tools.setClickableText(alertDialog0);
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
        };
    }

    public static void start() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = 1;
                Main.logDebug(1, null);
                boolean installMode = Installer.getState().isInstaller;
                BaseActivity.instance.installMode = installMode;
                if(installMode) {
                    boolean z1 = ConfigListAdapter.isClient64();
                    Log.d(("Install64: " + z1));
                    if(z1) {
                        Main.startInstall64();
                        return;
                    }
                    Main.startInstall();
                    return;
                }
                BaseActivity.exitOnDestroy = false;
                if(Main.loaded) {
                    Main.logDebug(8, null);
                    Main.onStart();
                    return;
                }
                boolean z2 = BootstrapInstrumentation.isBootstraped();
                if(!z2) {
                    v = 2;
                }
                Main.checkInstrumentation(((byte)v));
                if(!z2) {
                    Log.d(("Not instr: " + Process.myUid()));
                    Main.logDebug(2, Config.contextSource);
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                boolean tryInstr = false;
                                boolean done = false;
                                if(Config.contextSource == 0) {
                                    ComponentName componentName0 = Main.getInstrumentationComponent();
                                    if(componentName0 != null) {
                                        Log.d(("Try instr: " + Process.myUid()));
                                        Main.checkInstrumentation(((byte)0));
                                        tryInstr = true;
                                        done = BaseActivity.context.startInstrumentation(componentName0, null, BaseActivity.getHwBundle());
                                        Main.checkInstrumentation(((byte)1));
                                        tryInstr = false;
                                        Log.d(("Fail instr: " + Process.myUid()));
                                    }
                                }
                                else {
                                    Main.checkPhoenixOs();
                                }
                                Main.logDebug(3, done + ' ' + Config.contextSource);
                            }
                            catch(Throwable e) {
                                Log.e("Failed load Instrumentation", e);
                            }
                            if(tryInstr) {
                                Main.checkInstrumentation(((byte)1));
                            }
                            Main.logDebug(6, null);
                            Main.setContext();
                        }
                    });
                    Main.logDebug(4, null);
                    return;
                }
                Log.d(("Use instr: " + Process.myUid()));
                Main.logDebug(9, null);
                Main.useNotificationService();
                Main.onStart();
            }
        });
    }

    static void startInstall() {
        BaseActivity.instance.setInstallerMode(1);
        Installer.startInstall();
    }

    static void startInstall64() {
        android.ext.Main.1 callback = new Runnable() {
            private boolean called;

            {
                this.called = false;
            }

            @Override
            public void run() {
                if(this.called) {
                    return;
                }
                this.called = true;
                Main.startInstall();
            }
        };
        Alert.show(Alert.create(BaseActivity.context).setTitle(Re.s(0x7F0702D8)).setCancelable(false).setItems(new String[]{Re.s(0x7F07023D), Re.s(0x7F0702D9), Re.s(0x7F0702DA)}, new DialogInterface.OnClickListener() {  // string:install_mode "Install mode"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 1: {
                        Installer.mode = 1;
                        break;
                    }
                    case 2: {
                        Installer.mode = 2;
                    }
                }
                callback.run();
            }
        }).create(), callback, false);
    }

    private static boolean unloadService() {
        boolean res = false;
        try {
            Context context0 = Tools.getContext();
            if(context0 != null) {
                String s = Tools.getPackageName();
                boolean z1 = context0.stopService(new Intent().setClassName(s, "com.ggdqo.AnalyticsService"));
                Main.logDebug(100, " " + z1);
                return z1;
            }
            Main.logDebug(101, "");
            return false;
        }
        catch(Throwable e) {
            Log.e("Failed unload Service", e);
            return res;
        }
    }

    static void useNotificationService() {
        if(Config.contextSource == 1) {
            return;
        }
        if((Config.configClient & 0x100) != 0) {
            Main.loadService();
            return;
        }
        Main.unloadService();
    }
}

