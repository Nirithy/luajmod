package android.ext;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.UiAutomation;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.fix.TextView;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

public class CheckFloatingWindow implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener, View.OnTouchListener {
    static class TestButton extends TextView implements View.OnTouchListener {
        int flags;
        String name;
        int type;

        public TestButton(Context context, String name, int type, int flags) {
            super(context);
            this.name = name;
            this.type = type;
            this.flags = flags;
            this.setTextColor(Color.rgb(0xFF, 0xFF, 0xFF));
            this.setText(name);
            this.setOnTouchListener(this);
        }

        @Override  // android.view.View
        protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
            Log.d(("TestButton: dispatchGenericFocusedEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchGenericFocusedEvent(event);
        }

        @Override  // android.view.View
        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            Log.d(("TestButton: dispatchGenericMotionEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchGenericMotionEvent(event);
        }

        @Override  // android.view.View
        protected boolean dispatchGenericPointerEvent(MotionEvent event) {
            Log.d(("TestButton: dispatchGenericPointerEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchGenericPointerEvent(event);
        }

        @Override  // android.view.View
        public boolean dispatchKeyEvent(KeyEvent event) {
            Log.d(("TestButton: dispatchKeyEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchKeyEvent(event);
        }

        @Override  // android.view.View
        public boolean dispatchTouchEvent(MotionEvent event) {
            Log.d(("TestButton: dispatchTouchEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchTouchEvent(event);
        }

        @Override  // android.view.View
        public boolean dispatchTrackballEvent(MotionEvent event) {
            Log.d(("TestButton: dispatchTrackballEvent for " + this.name + ": " + event), new RuntimeException());
            return super.dispatchTrackballEvent(event);
        }

        @Override  // android.widget.TextView
        public boolean onGenericMotionEvent(MotionEvent event) {
            Log.d(("TestButton: onGenericMotionEvent for " + this.name + ": " + event), new RuntimeException());
            return super.onGenericMotionEvent(event);
        }

        @Override  // android.view.View$OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(("TestButton: onTouch for " + this.name + ": " + event), new RuntimeException());
            return false;
        }

        @Override  // android.fix.TextView
        public boolean onTouchEvent(MotionEvent event) {
            Log.d(("TestButton: onTouchEvent for " + this.name + ": " + event), new RuntimeException());
            return super.onTouchEvent(event);
        }
    }

    private static boolean beBad;
    private static AlertDialog dialog;
    Activity parent;
    private static volatile boolean pass;
    private static View[] views;

    static {
        CheckFloatingWindow.pass = false;
        CheckFloatingWindow.views = null;
        CheckFloatingWindow.dialog = null;
        CheckFloatingWindow.beBad = false;
    }

    public CheckFloatingWindow(Activity parent) {
        this.parent = parent;
    }

    private static boolean canDrawOverlays(Context context) {
        boolean simple = Tools.canDrawOverlays(context);
        if(simple && Config.vSpaceReal) {
            int v = Process.myUid();
            try {
                Method method0 = Settings.class.getDeclaredMethod("isCallingPackageAllowedToDrawOverlays", Context.class, Integer.TYPE, String.class, Boolean.TYPE);
                method0.setAccessible(true);
                Object object0 = method0.invoke(null, context, v, Config.vSpacePkg, Boolean.FALSE);
                if(object0 instanceof Boolean && !((Boolean)object0).booleanValue()) {
                    simple = false;
                }
                Log.d(("canDrawOverlays extended 1: " + object0));
            }
            catch(Throwable e) {
                Log.w("Failed check canDrawOverlays", e);
            }
            try {
                int v1 = ((AppOpsManager)context.getSystemService("appops")).checkOpNoThrow("android:system_alert_window", v, Config.vSpacePkg);
                boolean extended = false;
                switch(v1) {
                    case 0: {
                        extended = true;
                        break;
                    }
                    case 3: {
                        if(Tools.getPackageManager().checkPermission("android.permission.SYSTEM_ALERT_WINDOW", Config.vSpacePkg) == 0) {
                            extended = true;
                        }
                    }
                }
                if(!extended) {
                    simple = false;
                }
                Log.d(("canDrawOverlays extended 2: " + extended + ' ' + v1));
            }
            catch(Throwable e) {
                Log.w("Failed check canDrawOverlays", e);
            }
            return simple;
        }
        return simple;
    }

    // 检测为 Lambda 实现
    void check() [...]

    @TargetApi(23)
    public static void checkAPI() {
        try {
            if(Build.VERSION.SDK_INT >= 23) {
                BaseActivity parent = BaseActivity.instance;
                boolean z = CheckFloatingWindow.canDrawOverlays(parent);
                Log.d(("canDrawOverlays: " + z));
                if(!z) {
                    CheckFloatingWindow.beBad = true;
                    AlertDialog.Builder alertDialog$Builder0 = Alert.create(parent);
                    alertDialog$Builder0.setCustomTitle(Tools.getCustomTitle(0x7F07010E)).setMessage(0x7F070111).setNegativeButton(Re.s(0x7F0700B9), null).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:floating_window_fail "Unable to show floating window"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent0 = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                                intent0.setData(Uri.parse(("package:" + Tools.getPackageName())));
                                parent.startActivityForResult(intent0, 0);
                            }
                            catch(Throwable e) {
                                Log.e("Failed call intent", e);
                            }
                        }
                    });
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Alert.tryShow(alertDialog$Builder0.create());
                            }
                            catch(Throwable e) {
                                Log.e("Failed show float dialog", e);
                            }
                        }
                    });
                    return;
                }
                if(CheckFloatingWindow.beBad) {
                    CheckFloatingWindow.beBad = false;
                    ConfigListAdapter.needRestart();
                }
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    // 检测为 Lambda 实现
    void checkAll() [...]

    private void clearAll(boolean withDialog) {
        View[] list = CheckFloatingWindow.views;
        if(list != null) {
            for(int v = 0; v < list.length; ++v) {
                View v = list[v];
                if(v != null) {
                    Tools.removeView(v);
                }
            }
            CheckFloatingWindow.views = null;
        }
        if(withDialog) {
            AlertDialog d = CheckFloatingWindow.dialog;
            if(d != null) {
                Tools.dismiss(d);
                CheckFloatingWindow.dialog = null;
            }
        }
    }

    public void doCheck() {
        new DaemonThread(() -> try {
            int v = CheckFloatingWindow.this.getColor(Tools.RANDOM);
            int size = (int)Tools.dp2px(128.0f);
            View view0 = new View(MainService.context);
            view0.setBackgroundColor(v);
            view0.setOnClickListener(CheckFloatingWindow.this);
            try {
                CheckFloatingWindow.this.showView(view0, size);
                Thread.sleep(1000L);
                int[] coords = new int[2];
                view0.getLocationOnScreen(coords);
                CheckFloatingWindow.this.sendClick(coords[0] + (size >> 1), coords[1] + (size >> 1));
                Thread.sleep(1000L);
            }
            finally {
                Tools.removeView(view0);
            }
            if(CheckFloatingWindow.pass) {
                Tools.showToast(0x7F070110);  // string:check_success "Verification was successful"
                return;
            }
            CheckFloatingWindow.this.showAlert();
        }
        catch(Throwable e) {
            Log.e("Failed check floating window", e);
        }, "CheckFloatingWindow").start();

        class android.ext.CheckFloatingWindow.1 implements Runnable {
            @Override
            public void run() {
                CheckFloatingWindow.this.check();
            }
        }

    }

    private int getColor(Random rnd) {
        return Color.rgb(rnd.nextInt(0x7F) + 0x40, rnd.nextInt(0x7F) + 0x40, rnd.nextInt(0x7F) + 0x40);
    }

    @TargetApi(18)
    private void injectInputEvent(MotionEvent event, boolean sync) {
        if(BootstrapInstrumentation.mInstance != null) {
            BootstrapInstrumentation.mInstance.sendPointerSync(event);
            if(Build.VERSION.SDK_INT >= 18) {
                UiAutomation uiAutomation0 = BootstrapInstrumentation.mInstance.getUiAutomation();
                if(uiAutomation0 != null) {
                    uiAutomation0.injectInputEvent(event, sync);
                }
            }
        }
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        this.clearAll(true);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        Log.d(("TestButton: onClick for " + v), new RuntimeException());
        if(v instanceof TestButton) {
            Log.d(("onClick for " + ((TestButton)v).name));
            Option config$Option0 = Config.get(0x7F0B00BD);  // id:config_float_type
            config$Option0.value = ((TestButton)v).type;
            Option config$Option1 = Config.get(0x7F0B00BE);  // id:config_float_flags
            config$Option1.value = ((TestButton)v).flags;
            Config.save();
            this.clearAll(true);
            BaseActivity.restartApp();
            return;
        }
        CheckFloatingWindow.pass = true;
    }

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        this.clearAll(true);
    }

    @Override  // android.content.DialogInterface$OnShowListener
    public void onShow(DialogInterface dialog) {
        new DaemonThread(() -> {
            int i;
            View[] list = null;
            try {
                Random rnd = Tools.RANDOM;
                DisplayMetrics metrics = new DisplayMetrics();
                ((WindowManager)Tools.getContext().getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
                int v = Tools.dp2px48();
                int line = metrics.widthPixels / v / 2 * 2;
                int cnt = line * (metrics.heightPixels / v);
                if(cnt > 100) {
                    cnt = 100;
                }
                Log.d(("checkAll: " + v + "; " + line + "; " + cnt + "; " + v + "; " + metrics));
                list = new View[cnt];
                i = 0;
                while(true) {
                label_12:
                    if(i >= cnt) {
                        goto label_34;
                    }
                    boolean firstHalf = i / (cnt / 2) == 0;
                    int type = i % (cnt / 2) + 2000;
                    int v5 = CheckFloatingWindow.this.getColor(rnd);
                    TestButton checkFloatingWindow$TestButton0 = new TestButton(MainService.context.getApplicationContext(), Integer.toString(i % (cnt / 2)) + (firstHalf ? "" : "\'"), type, (firstHalf ? 0 : 0x20) | 8);
                    checkFloatingWindow$TestButton0.setBackgroundColor(v5);
                    checkFloatingWindow$TestButton0.setOnClickListener(CheckFloatingWindow.this);
                    try {
                        int v6 = firstHalf ? 0 : line / 2;
                        CheckFloatingWindow.this.showView(checkFloatingWindow$TestButton0, v, type, (firstHalf ? 0 : 0x20) | 8, (i % (line / 2) + v6) * v, i % (cnt / 2) / (line / 2) * v);
                        list[i] = checkFloatingWindow$TestButton0;
                    }
                    catch(Throwable e) {
                        Log.e(("Failed show: " + type), e);
                    }
                    break;
                }
            }
            catch(Throwable e) {
                Log.e("Failed checkAll floating window", e);
                goto label_34;
            }
            ++i;
            goto label_12;
        label_34:
            CheckFloatingWindow.this.clearAll(false);
            CheckFloatingWindow.views = list;
        }, "ChangeFloatingWindow").start();

        class android.ext.CheckFloatingWindow.4 implements Runnable {
            @Override
            public void run() {
                CheckFloatingWindow.this.checkAll();
            }
        }

    }

    @Override  // android.view.View$OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(("dbg: " + v + ' ' + event), new RuntimeException());
        return false;
    }

    @TargetApi(12)
    public void sendClick(int x, int y) {
        try {
            Tools.waitForTimeout(Tools.exec(new String[]{"input", "tap", Integer.toString(x), Integer.toString(y)}), 15);
        }
        catch(IOException e) {
            Log.w("sendClick", e);
        }
        long v2 = SystemClock.uptimeMillis();
        MotionEvent motionEvent0 = MotionEvent.obtain(v2, v2, 0, ((float)x), ((float)y), 0);
        MotionEvent motionEvent1 = MotionEvent.obtain(v2, v2, 1, ((float)x), ((float)y), 0);
        if(Build.VERSION.SDK_INT >= 12) {
            motionEvent0.setSource(0x1002);
            motionEvent1.setSource(0x1002);
        }
        this.injectInputEvent(motionEvent0, true);
        this.injectInputEvent(motionEvent1, true);
        motionEvent0.recycle();
        motionEvent1.recycle();
    }

    public static AlertDialog.Builder setupDialog(AlertDialog.Builder builder) {
        String app;
        if(Config.vSpaceReal) {
            app = Config.vSpaceName;
            return builder.setCustomTitle(Tools.getCustomTitle(0x7F07010E)).setMessage(Tools.stringFormat(Tools.stripColon(0x7F0702C4), new Object[]{app, app, app, app}) + ":\nhttps://productforums.google.com/forum/#!topic/translate/1Pywh5vI1kE\n\n" + Tools.stripColon(0x7F0702C8) + ":\n" + "http://gameguardian.net/v-240").setNegativeButton(Re.s(0x7F07009D), null);  // string:floating_window_fail "Unable to show floating window"
        }
        app = Re.s(0x7F070001);  // string:app_name "GameGuardian"
        return builder.setCustomTitle(Tools.getCustomTitle(0x7F07010E)).setMessage(Tools.stringFormat(Tools.stripColon(0x7F0702C4), new Object[]{app, app, app, app}) + ":\nhttps://productforums.google.com/forum/#!topic/translate/1Pywh5vI1kE\n\n" + Tools.stripColon(0x7F0702C8) + ":\n" + "http://gameguardian.net/v-240").setNegativeButton(Re.s(0x7F07009D), null);  // string:floating_window_fail "Unable to show floating window"
    }

    private void showAlert() {
        AlertDialog.Builder alertDialog$Builder0 = CheckFloatingWindow.setupDialog(Alert.create(this.parent));
        alertDialog$Builder0.setPositiveButton(Re.s(0x7F070224), new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:7360nqrzq0sureohpv2Bgr@ilqgFrpphqw)frpphqw@76<88"));  // string:site "Site"
        if(Build.VERSION.SDK_INT >= 23) {
            alertDialog$Builder0.setNeutralButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:fix_it "Fix it"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent0 = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                    intent0.setData(Uri.parse(("package:" + (Config.vSpaceReal ? Config.vSpacePkg : Tools.getPackageName()))));
                    CheckFloatingWindow.this.parent.startActivity(intent0);
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
                    Log.e("Failed show float dialog", e);
                }
            }
        });
    }

    public void showDialog() {
        AlertDialog alertDialog0 = Alert.create(this.parent).setNegativeButton(Re.s(0x7F0700A1), this).setCancelable(true).create();  // string:cancel "Cancel"
        alertDialog0.setOnDismissListener(this);
        alertDialog0.setCanceledOnTouchOutside(false);
        alertDialog0.setCancelable(true);
        alertDialog0.setOnShowListener(this);
        CheckFloatingWindow.dialog = alertDialog0;
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Alert.tryShow(alertDialog0);
                }
                catch(Throwable e) {
                    Log.e(("Failed show dialog: " + alertDialog0), e);
                }
            }
        });
    }

    // 去混淆评级： 低(20)
    private void showView(View testView, int size) throws InterruptedException {
        this.showView(testView, size, 0x7F6, 0x100, 0, 0);
    }

    private void showView(View testView, int size, int type, int flags, int x, int y) throws InterruptedException {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = type;
        layoutParams.width = size;
        layoutParams.height = size;
        layoutParams.format = -2;
        layoutParams.flags = flags;
        layoutParams.alpha = 1.0f;
        layoutParams.x = x;
        layoutParams.y = y;
        layoutParams.gravity = 51;
        android.ext.CheckFloatingWindow.2 show = new Runnable() {
            @Override
            public void run() {
                try {
                    Tools.addView(testView, layoutParams);
                }
                catch(Throwable e) {
                    Log.e(("Failed show test view(" + layoutParams.type + ", " + layoutParams.flags + "): " + testView + "; " + layoutParams), e);
                }
                synchronized(this) {
                    this.notify();
                }
            }
        };
        synchronized(show) {
            ThreadManager.runOnUiThread(show);
            show.wait();
        }
    }
}

