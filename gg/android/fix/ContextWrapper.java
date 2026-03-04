package android.fix;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.ext.ConfigListAdapter;
import android.ext.Log;
import android.ext.SP;
import android.ext.ThreadManager;
import android.ext.Tools;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;

public class ContextWrapper extends ContextThemeWrapper {
    private static Configuration globalConfig;
    private static Locale globalLocale;
    private Configuration localConfig;
    private Resources localRes;
    private static volatile boolean useFix;

    static {
        ContextWrapper.useFix = true;
        ContextWrapper.globalConfig = null;
        ContextWrapper.globalLocale = Locale.getDefault();
    }

    public ContextWrapper(Context base) {
        super(base, 0x7F090004);  // style:FixTheme
        this.localConfig = null;
        this.localRes = null;
    }

    @Override  // android.content.ContextWrapper
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        try {
            return super.bindService(service, conn, flags);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    public static void checkFix(Context con) {
        if(con instanceof ContextWrapper) {
            ContextWrapper context = (ContextWrapper)con;
            try {
                LayoutInflater.from(context);
            }
            catch(AssertionError e) {
                try {
                    LayoutInflater.from(context.getBaseContext());
                    Log.e("Failed use fix for LayoutInflater", e);
                    ContextWrapper.useFix = false;
                }
                catch(AssertionError e2) {
                    Log.e("Can not get LayoutInflater", e2);
                }
            }
            Log.e("Check LayoutInflater - ok");
        }
    }

    private boolean execIntent(Intent intent) {
        String[] arr_s = {"am", "start", intent.toUri(5)};
        Log.w(("Try exec intent: " + intent + "; " + Arrays.toString(arr_s)));
        try {
            Process process0 = Tools.exec(arr_s);
            process0.getInputStream().close();
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(process0.getErrorStream()));
            StringBuilder err = new StringBuilder();
            String s;
            while((s = bufferedReader0.readLine()) != null) {
                err.append(s);
                err.append('\n');
            }
            bufferedReader0.close();
            if(err.length() > 0) {
                Log.w(("Failed exec intent: " + intent + "; " + Arrays.toString(arr_s) + ":\n" + err.toString()));
                return false;
            }
            return true;
        }
        catch(Throwable e) {
            Log.w(("Failed exec intent: " + intent + "; " + Arrays.toString(arr_s)), e);
            return false;
        }
    }

    public static Configuration fixConfig(Configuration config) {
        ConfigListAdapter.setLocale(config, ContextWrapper.globalLocale);
        return new Configuration(config);
    }

    @Override  // android.view.ContextThemeWrapper
    public AssetManager getAssets() {
        return this.getResources().getAssets();
    }

    @Override  // android.content.ContextWrapper
    public File getCacheDir() {
        try {
            return super.getCacheDir();
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return Tools.getCacheDir();
        }
    }

    public static ContextWrapper getContextWrapper(Context context) {
        try {
            Class.forName("com.wobian.wv.wb.WBContext");
            return new ContextWrapper(context) {
                private Object mPackageInfo;

                {
                    Context $anonymous0 = context;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    try {
                        Context context1 = this.getBaseContext();
                        Class class0 = Class.forName("android.app.ContextImpl");
                        if(class0.isInstance(context1)) {
                            Field field0 = class0.getDeclaredField("mPackageInfo");
                            field0.setAccessible(true);
                            this.mPackageInfo = field0.get(context1);
                            Log.d(("WBContext: " + this.mPackageInfo));
                        }
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }
            };
        }
        catch(Throwable unused_ex) {
            return new ContextWrapper(context);
        }
    }

    @Override  // android.content.ContextWrapper
    public File getExternalCacheDir() {
        try {
            return super.getExternalCacheDir();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            return null;
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return null;
        }
    }

    @Override  // android.content.ContextWrapper
    public File getExternalFilesDir(String type) {
        try {
            return super.getExternalFilesDir(type);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
            return null;
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return null;
        }
    }

    @Override  // android.content.ContextWrapper
    public File getFilesDir() {
        try {
            return super.getFilesDir();
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return Tools.getFilesDir();
        }
    }

    @Override  // android.content.ContextWrapper
    public PackageManager getPackageManager() {
        try {
            return super.getPackageManager();
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return Tools.getPackageManager();
        }
    }

    @Override  // android.content.ContextWrapper
    public String getPackageName() {
        try {
            return super.getPackageName();
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return Tools.getPackageName();
        }
    }

    @Override  // android.view.ContextThemeWrapper
    public Resources getResources() {
        Configuration global = ContextWrapper.globalConfig;
        Configuration local = this.localConfig;
        Resources superRes = null;
        Resources localRes = this.localRes;
        if(local != global || localRes != null && !localRes.getConfiguration().equals(local)) {
            this.localConfig = global;
            superRes = super.getResources();
            if(global == null) {
                localRes = superRes;
            }
            else {
                try {
                    superRes.updateConfiguration(global, null);
                    if(localRes != null && localRes != superRes) {
                        localRes.updateConfiguration(global, null);
                    }
                }
                catch(Throwable e) {
                    Log.e("Failed set locale", e);
                }
                Context tmp = null;
                if(Build.VERSION.SDK_INT >= 17) {
                    try {
                        tmp = this.createConfigurationContext(global);
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }
                localRes = tmp == null ? superRes : tmp.getResources();
            }
        }
        if(localRes == null) {
            if(superRes == null) {
                superRes = super.getResources();
            }
            localRes = superRes;
        }
        this.localRes = localRes;
        return localRes;
    }

    @Override  // android.content.ContextWrapper
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return SP.wrap(super.getSharedPreferences(name, mode));
    }

    @Override  // android.view.ContextThemeWrapper
    public Object getSystemService(String name) {
        return SystemService.wrap(super.getSystemService(name));
    }

    public static boolean isUseFix() {
        return ContextWrapper.useFix;
    }

    public static Configuration onConfigurationChanged(Configuration config) {
        Configuration configuration1 = ContextWrapper.fixConfig(config);
        ContextWrapper.setOverride(configuration1, null);
        return configuration1;
    }

    public static void setOverride(Configuration newConfig, Locale newLocale) {
        if(newLocale != null) {
            ContextWrapper.globalLocale = newLocale;
        }
        Configuration config = newConfig == null ? ContextWrapper.globalConfig : newConfig;
        if(config == null) {
            config = Tools.getContext().getResources().getConfiguration();
        }
        Configuration config = ContextWrapper.fixConfig(config);
        if(!config.equals(ContextWrapper.globalConfig)) {
            ContextWrapper.globalConfig = config;
        }
    }

    @Override  // android.content.ContextWrapper
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        }
        catch(Throwable e) {
            if((intent == null || !"android.intent.action.WEB_SEARCH".equals(intent.getAction())) && !ContextWrapper.startActivityUI(e, this, intent) && !this.execIntent(intent)) {
                throw e;
            }
        }
    }

    public static boolean startActivityUI(Throwable e, Context context, Intent intent) {
        String s = e.getMessage();
        if(!ThreadManager.isInUiThread() && s != null && s.contains("Looper.prepare()")) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        context.startActivity(intent);
                    }
                    catch(Throwable e) {
                        Log.w(("Failed startActivity from UI thread: " + intent), e);
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override  // android.content.ContextWrapper
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        try {
            return super.startInstrumentation(className, profileFile, arguments);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.content.ContextWrapper
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            super.unregisterReceiver(receiver);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
    }

    public static Context wrap(Context context) {
        return context != null && !(context instanceof ContextWrapper) ? ContextWrapper.getContextWrapper(context) : context;
    }
}

