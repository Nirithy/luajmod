package android.ext;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.fix.ContextWrapper;
import android.os.Build.VERSION;
import android.os.IBinder;

public class BootstrapService extends Service {
    public static volatile boolean allow;
    public static volatile Runnable forExit;
    public static volatile boolean fromApp;
    public static volatile BootstrapService instance;
    public static volatile boolean stop;

    static {
        BootstrapService.fromApp = false;
        BootstrapService.forExit = null;
        BootstrapService.allow = false;
        BootstrapService.stop = false;
    }

    public BootstrapService() {
        BootstrapService.instance = this;
    }

    private void checkReasonStop() {
        if(!BootstrapService.stop && BootstrapService.allow) {
            String[] arr_s = ExceptionHandler.getApps();
            for(int i = 2; i < arr_s.length; i += 2) {
                String check = arr_s[i];
                if(Tools.isPackageInstalled(arr_s[i + 1])) {
                    RuntimeException runtimeException0 = new RuntimeException("BootstrapService stop: " + check);
                    Log.w(("Found: " + check), runtimeException0);
                    ExceptionHandler.storeException(Thread.currentThread(), runtimeException0);
                }
            }
        }
    }

    @Override  // android.content.ContextWrapper
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return SP.wrap(super.getSharedPreferences(name, mode));
    }

    private void init() {
        Tools.init(this);
        if(BootstrapService.allow) {
            if(MainService.context == null) {
                MainService.context = ServiceContext.wrap(this);
            }
            ExceptionHandler.install();
        }
    }

    @Override  // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d(("BootstrapService onBind: " + intent));
        return null;
    }

    @Override  // android.app.Service
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(("BootstrapService onConfigurationChanged: " + newConfig));
        if(!BootstrapService.fromApp) {
            return;
        }
        Configuration configuration1 = ContextWrapper.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(configuration1);
        MainService.onConfigurationChanged(configuration1);
    }

    @Override  // android.app.Service
    public void onCreate() {
        Log.d(("BootstrapService onCreate: " + BootstrapService.fromApp + '-' + BootstrapService.allow + "; " + BootstrapService.instance + "; " + this));
        if(!BootstrapService.fromApp) {
            this.stopService();
            return;
        }
        BootstrapService.instance = this;
        super.onCreate();
        this.init();
    }

    @Override  // android.app.Service
    public void onDestroy() {
        if(BootstrapService.forExit != null) {
            LogWrapper.d("AndroidService", "wait 3");
            BootstrapService.forExit.run();
            return;
        }
        if(!BootstrapService.fromApp) {
            Log.d(("BootstrapService onDestroy 1: " + BootstrapService.fromApp + '-' + BootstrapService.stop));
            return;
        }
        Log.d(("BootstrapService onDestroy 2: " + BootstrapService.fromApp + '-' + BootstrapService.stop), new RuntimeException());
        this.checkReasonStop();
        this.stopForeground(true);
        if(!BootstrapInstrumentation.isBootstraped()) {
            Bootstrap.onDestroy();
        }
        super.onDestroy();
    }

    @Override  // android.app.Service
    public void onLowMemory() {
        Log.d("BootstrapService onLowMemory");
        if(!BootstrapService.fromApp) {
            return;
        }
        super.onLowMemory();
        MainService.onTrimMemory(-1);
    }

    @Override  // android.app.Service
    public void onRebind(Intent intent) {
        Log.d(("BootstrapService onRebind: " + intent));
        super.onRebind(intent);
    }

    @Override  // android.app.Service
    public void onStart(Intent intent, int startId) {
        Log.d(("BootstrapService onStart: " + startId + "; " + intent));
        super.onStart(intent, startId);
    }

    @Override  // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z;
        Log.d(("BootstrapService onStartCommand: " + BootstrapService.fromApp + '-' + BootstrapService.allow + "; " + BootstrapService.instance + "; " + this + "; " + MainService.context));
        Log.d(("BootstrapService onStartCommand: " + startId + ' ' + flags + ' ' + intent));
        if(!BootstrapService.fromApp) {
            this.stopService();
            return 2;
        }
        this.init();
        if(BootstrapService.allow) {
            Main.onStart();
            Throwable e = null;
            int i = 0;
            int n = Config.vSpaceReal ? 4 : 2;
            while(i < n) {
                MainService mainService0 = MainService.instance;
                if(i % 2 == 0) {
                    try {
                        z = true;
                    label_17:
                        this.startForeground(1, mainService0.getNotification(z, false));
                        Log.d(("startForeground 1." + i));
                        return super.onStartCommand(intent, flags, startId);
                    }
                    catch(Throwable e2) {
                        goto label_21;
                    }
                }
                else {
                    z = false;
                }
                goto label_17;
            label_21:
                if(e != null && Build.VERSION.SDK_INT >= 19) {
                    e2.addSuppressed(e);
                }
                if(i == n - 1) {
                    Log.w("Failed startForeground", e2);
                    ExceptionHandler.sendException(Thread.currentThread(), e2, false);
                }
                e = e2;
                ++i;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override  // android.app.Service
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(("BootstrapService onTaskRemoved: " + BootstrapService.fromApp + '-' + BootstrapService.stop + '-' + BootstrapService.allow + "; " + rootIntent), new RuntimeException());
        if(!BootstrapService.fromApp) {
            return;
        }
        this.checkReasonStop();
        super.onTaskRemoved(rootIntent);
    }

    @Override  // android.app.Service
    public void onTrimMemory(int level) {
        Log.d(("BootstrapService onTrimMemory: " + level));
        if(!BootstrapService.fromApp) {
            return;
        }
        super.onTrimMemory(level);
        MainService.onTrimMemory(level);
    }

    @Override  // android.app.Service
    public boolean onUnbind(Intent intent) {
        Log.d(("BootstrapService onUnbind: " + intent));
        return super.onUnbind(intent);
    }

    void stopService() {
        try {
            this.stopForeground(true);
            Log.d("BootstrapService stopForeground");
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        try {
            String s = Tools.getPackageName();
            Log.d(("BootstrapService stop: " + this.stopService(new Intent().setClassName(s, "com.ggdqo.AnalyticsService"))));
        }
        catch(Throwable e) {
            Log.e("Failed stop Service", e);
        }
    }
}

