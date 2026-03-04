package android.ext;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Miui {
    public static final int FLAG_SHOW_FLOATING_WINDOW = 0x8000000;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    private static final int VERSION_NOT_MIUI = -1;
    private static final int VERSION_UNKNOWN;
    final Context base;
    private Map codes;
    private StringBuilder logMiui;
    private int version;

    public Miui() {
        this.base = BaseActivity.instance;
        this.logMiui = new StringBuilder();
        this.version = -1;
        this.codes = new HashMap();
        this.codes.put("android.permission.SYSTEM_ALERT_WINDOW", 24);
    }

    private int SHOW_FLOATING_WINDOW() [...] // 潜在的解密器

    // 检测为 Lambda 实现
    public void check() [...]

    @TargetApi(19)
    private int checkOp(int op) {
        try {
            ApplicationInfo applicationInfo0 = this.base.getApplicationInfo();
            AppOpsManager appOpsManager = (AppOpsManager)this.base.getSystemService("appops");
            return (int)(((Integer)AppOpsManager.class.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, op, applicationInfo0.uid, applicationInfo0.packageName)));
        }
        catch(Throwable e) {
            Log.d(("MIUI-checkOp(" + op + ") failed"), e);
            this.logMiui.append("\nMIUI-checkOp(" + op + ") failed:\n");
            this.logMiui.append(Log.getStackTraceString(e));
            return -1;
        }
    }

    private boolean checkPermission(String permission) {
        this.logMiui.append("\nMIUI-checkPermission ");
        this.logMiui.append(permission);
        this.logMiui.append(" - ");
        Integer code = (Integer)this.codes.get(permission);
        boolean result = false;
        if(code == null) {
            this.logMiui.append("unknown - ");
            result = true;
        }
        else if(((int)code) != 24 || this.version != 5) {
            try {
                if(this.checkOp(((int)code)) == 0) {
                    result = true;
                }
            }
            catch(Throwable e) {
                Log.d("MIUI-checkPermission failed", e);
                this.logMiui.append("failed:\n");
                this.logMiui.append(Log.getStackTraceString(e));
                this.logMiui.append('\n');
            }
        }
        else {
            int v = this.SHOW_FLOATING_WINDOW();
            if((this.base.getApplicationInfo().flags & v) == v) {
                result = true;
            }
        }
        this.logMiui.append(result);
        return result;
    }

    private void detect() {
        try {
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(Tools.exec(new String[]{"getprop"}).getInputStream()));
            this.logMiui.append("\ngetprop:\n");
            String s;
            while((s = bufferedReader0.readLine()) != null) {
                if(s.contains("ro.product.device") && s.contains("_sprout")) {
                    this.version = -1;
                    break;
                }
                if(!s.contains("fpc.fp.miui.") && !s.contains("ro.ril.miui.imei")) {
                    if(s.contains("miui") || s.contains("MIUI")) {
                        Log.d(("MIUI found: " + s));
                        this.logMiui.append(s);
                        this.logMiui.append('\n');
                        if(this.version == -1) {
                            this.version = 0;
                        }
                    }
                    if(s.contains("ro.miui.ui.version.name")) {
                        if(s.contains("V5")) {
                            this.version = 5;
                        }
                        if(s.contains("V6")) {
                            this.version = 6;
                        }
                    }
                }
            }
            bufferedReader0.close();
        }
        catch(Throwable e) {
            Log.e("MIUI-detect", e);
            this.logMiui.append("\nMIUI-detect failed:\n");
            this.logMiui.append(Log.getStackTraceString(e));
        }
    }

    @TargetApi(19)
    private void fixPermission(String permission) {
        this.logMiui.append("\nMIUI-fixPermission ");
        this.logMiui.append(permission);
        this.logMiui.append(" - ");
        Integer code = (Integer)this.codes.get(permission);
        if(code == null) {
            this.logMiui.append("unknown");
            return;
        }
        if(((int)code) == 24 && this.version == 5) {
            try {
                Tools.getPackageManager().setApplicationEnabledSetting(Tools.getPackageName(), 0x8000000, 0x8000000);
                this.logMiui.append("success 1");
            }
            catch(Throwable e) {
                Log.d("MIUI-fixPermission failed", e);
                this.logMiui.append("failed 1:\n");
                this.logMiui.append(Log.getStackTraceString(e));
                this.logMiui.append('\n');
            }
        }
        try {
            ApplicationInfo applicationInfo0 = this.base.getApplicationInfo();
            AppOpsManager.class.getMethod("setMode", Integer.TYPE, Integer.TYPE, String.class, Integer.TYPE).invoke(((AppOpsManager)this.base.getSystemService("appops")), code, applicationInfo0.uid, applicationInfo0.packageName, 0);
            this.logMiui.append("success 2");
        }
        catch(Throwable e) {
            Log.d("MIUI-fixPermission failed", e);
            this.logMiui.append("failed 2:\n");
            this.logMiui.append(Log.getStackTraceString(e));
            this.logMiui.append('\n');
        }
    }

    String getYoutubeUrl() [...] // 潜在的解密器

    public void runCheck() {
        if(Build.VERSION.SDK_INT < 16) {
            return;
        }
        new DaemonThread(() -> {
            try {
                Miui.this.detect();
                if(Miui.this.version == -1 || (Config.ignore & 2L) != 0L) {
                    return;
                }
                if(!Miui.this.checkPermission("android.permission.SYSTEM_ALERT_WINDOW")) {
                    Miui.this.fixPermission("android.permission.SYSTEM_ALERT_WINDOW");
                    AlertDialog.Builder alertDialog$Builder0 = Alert.create(Miui.this.base).setNeutralButton(Re.s(0x7F0700B9), Main.getSkipListener(2L));  // string:skip "Ignore"
                    if(Miui.this.checkPermission("android.permission.SYSTEM_ALERT_WINDOW")) {
                        alertDialog$Builder0.setMessage(Re.s(0x7F0700C6)).setPositiveButton(Re.s(0x7F0700F2), new ExitListener(1200, true)).setNegativeButton(Re.s(0x7F07009C), null);  // string:miui_restart "__app_name__ needs to be restarted in order to apply security 
                                                                                                                                                                                        // permissions."
                    }
                    else {
                        CheckFloatingWindow.setupDialog(alertDialog$Builder0);
                        alertDialog$Builder0.setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:fix_it "Fix it"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent0 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youtube.com/watch?v=0Xxj7Kz7WjQ"));
                                    Miui.this.base.startActivity(intent0);
                                }
                                catch(Throwable e) {
                                    Log.w("Failed call activity", e);
                                }
                            }
                        });
                    }
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                AlertDialog alertDialog0 = alertDialog$Builder0.create();
                                Alert.tryShow(alertDialog0);
                                Tools.setClickableText(alertDialog0);
                            }
                            catch(Throwable e) {
                                Log.e("Failed show miui dialog", e);
                            }
                        }
                    });
                }
            }
            catch(Throwable e) {
                Log.e("MIUI-check", e);
                Miui.this.logMiui.append("\nMIUI-check failed:\n");
                Miui.this.logMiui.append(Log.getStackTraceString(e));
            }
            Miui.this.sendLog();
        }, "runCheck").start();

        class android.ext.Miui.1 implements Runnable {
            @Override
            public void run() {
                Miui.this.check();
            }
        }

    }

    private void sendLog() {
        String s = this.logMiui.toString();
        this.logMiui = new StringBuilder();
        this.logMiui.append("MIUI\nGG: ");
        this.logMiui.append(96.0f);
        this.logMiui.append(" [");
        this.logMiui.append(0x3E79);
        this.logMiui.append("]\nAndroid: ");
        this.logMiui.append(Build.VERSION.RELEASE);
        this.logMiui.append("\nSDK: ");
        this.logMiui.append(Build.VERSION.SDK_INT);
        this.logMiui.append("\nappInfo.flags: ");
        this.logMiui.append("0x" + ToolsLight.prefixLongHex(8, ((long)this.base.getApplicationInfo().flags)));
        this.logMiui.append("\ncheckOp(OP_SYSTEM_ALERT_WINDOW): ");
        this.logMiui.append(this.checkOp(24));
        this.logMiui.append(s);
        String[] arr_s = this.logMiui.toString().split("[\\r\\n]+");
        for(int v = 0; v < arr_s.length; ++v) {
            Log.d(arr_s[v]);
        }
    }
}

