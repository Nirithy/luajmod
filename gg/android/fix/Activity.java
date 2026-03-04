package android.fix;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.ext.Alert;
import android.ext.Log;
import android.ext.MainService;
import android.ext.SP;
import android.ext.Tools;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import java.io.File;

public class Activity extends android.app.Activity {
    private AlertDialog mainDialog;
    private View mainView;
    private Context wrapper;

    public Activity() {
        this.mainView = null;
        this.mainDialog = null;
        this.wrapper = null;
    }

    @Override  // android.app.Activity
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override  // android.app.Activity
    public View findViewById(int id) {
        return this.mainView == null ? super.findViewById(id) : this.mainView.findViewById(id);
    }

    @Override  // android.app.Activity
    public void finish() {
        this.hideDialog();
        this.finish_();
    }

    // 检测为 Lambda 实现
    public void finish_() [...]

    @Override  // android.view.ContextThemeWrapper
    public AssetManager getAssets() {
        Context context0 = this.getWrapper();
        return context0 == null ? super.getAssets() : context0.getAssets();
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
        catch(SecurityException e) {
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
        catch(SecurityException e) {
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
    public String getPackageName() [...] // 潜在的解密器

    @Override  // android.view.ContextThemeWrapper
    public Resources getResources() {
        Context context0 = this.getWrapper();
        return context0 == null ? super.getResources() : context0.getResources();
    }

    @Override  // android.content.ContextWrapper
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return SP.wrap(super.getSharedPreferences(name, mode));
    }

    @Override  // android.app.Activity
    public Object getSystemService(String name) {
        try {
            return SystemService.wrap(super.getSystemService(name));
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return null;
        }
    }

    private Context getWrapper() {
        if(this.wrapper == null) {
            Context context0 = this.getBaseContext();
            if(context0 == null) {
                return null;
            }
            this.wrapper = ContextWrapper.wrap(context0);
        }
        return this.wrapper;
    }

    private void hideDialog() {
        try {
            if(this.mainDialog != null) {
                this.mainDialog.dismiss();
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    public boolean moveTaskToBack(boolean nonRoot) {
        try {
            this.hideDialog();
            return super.moveTaskToBack(nonRoot);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            this.finish();
            return true;
        }
    }

    @Override  // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        try {
            super.onCreate(savedInstanceState, persistentState);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    public void onDestroy() {
        this.hideDialog();
        try {
            super.onDestroy();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    public void onLowMemory() {
        Log.d("Activity onLowMemory");
        super.onLowMemory();
        MainService.onTrimMemory(-1);
    }

    @Override  // android.app.Activity
    protected void onPause() {
        try {
            super.onPause();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            super.onPostCreate(savedInstanceState);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onPostResume() {
        try {
            super.onPostResume();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onRestart() {
        try {
            super.onRestart();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onResume() {
        try {
            super.onResume();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onStart() {
        try {
            super.onStart();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    protected void onStop() {
        try {
            super.onStop();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Activity
    public void onTrimMemory(int level) {
        Log.d(("Activity onTrimMemory: " + level));
        super.onTrimMemory(level);
        MainService.onTrimMemory(level);
    }

    @Override  // android.app.Activity
    public void setContentView(int layoutResID) {
        RuntimeException dbg;
        try {
            dbg = new RuntimeException();
            super.setContentView(layoutResID);
        }
        catch(RuntimeException e) {
            if(dbg == e) {
                goto label_7;
            }
            else {
                try {
                    Log.e("Failed call super setContentView", e);
                    goto label_8;
                label_7:
                    dbg = e;
                    try {
                    label_8:
                        this.getWindow().setContentView(layoutResID);
                    }
                    catch(RuntimeException e2) {
                        if(dbg == e2) {
                            dbg = e2;
                        }
                        else {
                            Log.e("Failed fix call setContentView", e2);
                        }
                        if(Build.VERSION.SDK_INT >= 19) {
                            e.addSuppressed(e2);
                        }
                        throw e;
                    }
                    if(Build.VERSION.SDK_INT >= 11) {
                        try {
                            this.getActionBar();
                            goto label_43;
                        }
                        catch(RuntimeException e2) {
                        }
                        if(dbg == e2) {
                            goto label_42;
                        }
                        else {
                            Log.e("Failed call setup action bar", e2);
                        }
                    }
                    goto label_43;
                }
                catch(RuntimeException e) {
                    goto label_26;
                }
            }
            goto label_8;
            try {
            label_26:
                View view0 = LayoutInflater.inflateStatic(layoutResID, null);
                this.mainView = view0;
                AlertDialog alertDialog0 = Alert.create(this).setView(view0).setTitle(this.getTitle()).setCancelable(false).create();
                try {
                    alertDialog0.setOnCancelListener((/* 缺少LAMBDA参数 */) -> try {
                        super.finish();
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    });
                    alertDialog0.setCancelable(true);
                }
                catch(Throwable e2) {
                    Log.badImplementation(e2);
                }
                this.mainDialog = alertDialog0;
                alertDialog0.show();
                goto label_43;
            }
            catch(Throwable e2) {
                Log.e("Failed fix call setContentView", e2);
                if(Build.VERSION.SDK_INT >= 19) {
                    e.addSuppressed(e2);
                }
                throw e;
            }
        label_42:
            dbg = e2;
        }
    label_43:
        if(Log.getStackTraceString(dbg).contains("com.ggdqo")) {
            this.mainView = null;
            super.setContentView(layoutResID - 1);
        }

        class android.fix.Activity.2 implements DialogInterface.OnCancelListener {
            @Override  // android.content.DialogInterface$OnCancelListener
            public void onCancel(DialogInterface dialog) {
                Activity.this.finish_();
            }
        }

    }

    @Override  // android.app.Activity
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        }
        catch(SecurityException e) {
            if(intent.getAction().equals("android.intent.action.UNINSTALL_PACKAGE")) {
                intent.setAction("android.intent.action.DELETE");
                this.startActivity(intent);
                return;
            }
            if(!intent.getAction().equals("android.intent.action.INSTALL_PACKAGE")) {
                throw e;
            }
            intent.setAction("android.intent.action.VIEW");
            this.startActivity(intent);
        }
        catch(ActivityNotFoundException e) {
            Log.badImplementation(e);
        }
        catch(RuntimeException e) {
            if(!ContextWrapper.startActivityUI(e, this, intent)) {
                throw e;
            }
        }
    }

    // 检测为 Lambda 实现
    @Override  // android.app.Activity
    public void startActivityForResult(Intent intent, int requestCode) [...]

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
}

