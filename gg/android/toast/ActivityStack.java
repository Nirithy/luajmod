package android.toast;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Application;
import android.os.Bundle;

final class ActivityStack implements Application.ActivityLifecycleCallbacks {
    private Activity mForegroundActivity;
    private static volatile ActivityStack sInstance;

    public Activity getForegroundActivity() {
        return this.mForegroundActivity;
    }

    public static ActivityStack getInstance() {
        if(ActivityStack.sInstance == null) {
            Class class0 = ActivityStack.class;
            synchronized(class0) {
                if(ActivityStack.sInstance == null) {
                    ActivityStack.sInstance = new ActivityStack();
                }
                return ActivityStack.sInstance;
            }
        }
        return ActivityStack.sInstance;
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity0, Bundle bundle0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity0) {
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity0) {
        if(this.mForegroundActivity != activity0) {
            return;
        }
        this.mForegroundActivity = null;
    }

    @Override  // android.app.Application$ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity0) {
        this.mForegroundActivity = activity0;
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

    public void register(Application application0) {
        if(application0 == null) {
            return;
        }
        application0.registerActivityLifecycleCallbacks(this);
    }
}

