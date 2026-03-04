package android.ext;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.fix.ContextWrapper;
import android.os.Bundle;

public class BootstrapInstrumentation extends Instrumentation {
    public static volatile BootstrapInstrumentation mInstance;
    public static volatile boolean mIsBootstraped;

    static {
        BootstrapInstrumentation.mIsBootstraped = false;
        Log.d("Instrumentation clinit");
        ExceptionHandler.install();
    }

    public BootstrapInstrumentation() {
        BootstrapInstrumentation.mInstance = this;
        Log.d("Instrumentation init");
        ExceptionHandler.install();
    }

    public static void exit() {
        BootstrapInstrumentation instance = BootstrapInstrumentation.mInstance;
        if(instance != null) {
            instance.finish(0, new Bundle());
        }
    }

    public Context getContext(boolean target) {
        Context context = target ? this.getTargetContext() : this.getContext();
        Context context1 = target ? this.getContext() : this.getTargetContext();
        if(context == null) {
            context = context1;
        }
        return ContextWrapper.wrap(context);
    }

    public static boolean isBootstraped() {
        return BootstrapInstrumentation.mIsBootstraped;
    }

    @Override  // android.app.Instrumentation
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        BaseActivity.setHwBundle(paramBundle);
        Tools.init(this.getTargetContext());
        Tools.init(this.getContext());
        if(MainService.context == null) {
            MainService.context = ServiceContext.wrap(this.getContext(false));
        }
        Log.d(("Instrumentation onCreate " + paramBundle));
        ExceptionHandler.install();
        BootstrapInstrumentation.mInstance = this;
        BootstrapInstrumentation.mIsBootstraped = true;
        this.start();
    }

    @Override  // android.app.Instrumentation
    public void onDestroy() {
        Bootstrap.onDestroy();
        super.onDestroy();
    }

    @Override  // android.app.Instrumentation
    public boolean onException(Object obj, Throwable e) {
        Log.w(("Instrumentation onException: " + obj), e);
        return super.onException(obj, e);
    }

    @Override  // android.app.Instrumentation
    public void onStart() {
        super.onStart();
        Log.d("Instrumentation onStart com.ggdqo.ActivityMain");
        Context context0 = this.getContext(true);
        Tools.setComponentEnabledSetting(100, new ComponentName("com.ggdqo", "com.ggdqo.ActivityMain"), 1);
        context0.startActivity(Tools.getStartIntent(context0, "com.ggdqo", "com.ggdqo.ActivityMain"));
    }
}

