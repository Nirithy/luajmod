package android.ext;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Application;
import android.os.Build.VERSION;
import android.os.Bundle;

public class Appl extends Application {
    static {
        Log.d("Application clinit");
        ExceptionHandler.install();
    }

    public Appl() {
        Log.d("Application init");
        ExceptionHandler.install();
    }

    @Override  // android.app.Application
    public void onCreate() {
        Log.d("Application onCreate");
        super.onCreate();
        try {
            if(Build.VERSION.SDK_INT >= 14) {
                this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        Log.d(("onActivityCreated: " + activity + "; " + savedInstanceState));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityDestroyed(Activity activity) {
                        Log.d(("onActivityDestroyed: " + activity));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityPaused(Activity activity) {
                        Log.d(("onActivityPaused: " + activity));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityResumed(Activity activity) {
                        Log.d(("onActivityResumed: " + activity));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        Log.d(("onActivitySaveInstanceState: " + activity + "; " + outState));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityStarted(Activity activity) {
                        Log.d(("onActivityStarted: " + activity));
                    }

                    @Override  // android.app.Application$ActivityLifecycleCallbacks
                    public void onActivityStopped(Activity activity) {
                        Log.d(("onActivityStopped: " + activity));
                    }
                });
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Application
    public void onLowMemory() {
        Log.d("Application onLowMemory");
        super.onLowMemory();
        MainService.onTrimMemory(-1);
    }

    @Override  // android.app.Application
    public void onTerminate() {
        Log.d("Application onTerminate");
        super.onTerminate();
    }

    @Override  // android.app.Application
    public void onTrimMemory(int level) {
        Log.d(("Application onTrimMemory: " + level));
        super.onTrimMemory(level);
        MainService.onTrimMemory(level);
    }
}

