package android.toast;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Application;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.WindowManager;

final class WindowLifecycle implements Application.ActivityLifecycleCallbacks {
    private Activity mActivity;
    private Application mApplication;
    private ToastImpl mToastImpl;

    WindowLifecycle(Activity activity0) {
        this.mActivity = activity0;
    }

    WindowLifecycle(Application application0) {
        this.mApplication = application0;
    }

    public WindowManager getWindowManager() {
        if(this.mActivity != null) {
            return this.mActivity.isDestroyed() ? null : this.mActivity.getWindowManager();
        }
        return this.mApplication == null ? null : ((WindowManager)this.mApplication.getSystemService("window"));
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity0, Bundle bundle0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity0) {
        if(this.mActivity != activity0) {
            return;
        }
        ToastImpl toastImpl0 = this.mToastImpl;
        if(toastImpl0 != null) {
            toastImpl0.cancel();
        }
        this.unregister();
        this.mActivity = null;
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity0) {
        if(this.mActivity != activity0) {
            return;
        }
        ToastImpl toastImpl0 = this.mToastImpl;
        if(toastImpl0 == null) {
            return;
        }
        toastImpl0.cancel();
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity0, Bundle bundle0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity0) {
    }

    void register(ToastImpl toastImpl0) {
        this.mToastImpl = toastImpl0;
        if(this.mActivity == null) {
            return;
        }
        if(Build.VERSION.SDK_INT >= 29) {
            this.mActivity.registerActivityLifecycleCallbacks(this);
            return;
        }
        this.mActivity.getApplication().registerActivityLifecycleCallbacks(this);
    }

    void unregister() {
        this.mToastImpl = null;
        if(this.mActivity == null) {
            return;
        }
        if(Build.VERSION.SDK_INT >= 29) {
            this.mActivity.unregisterActivityLifecycleCallbacks(this);
            return;
        }
        this.mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
    }
}

