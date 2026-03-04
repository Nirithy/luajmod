package android.ext;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class ToastManager {
    private static Context fix;
    private static final Object lastMonitor;
    private static volatile Toast lastToast;
    private static volatile WeakReference lastUserHide;
    private static final Object lastUserMonitor;
    private static volatile WeakReference lastUserToast;
    private static Context source;

    static {
        ToastManager.lastToast = null;
        ToastManager.lastMonitor = new Object();
        ToastManager.lastUserToast = null;
        ToastManager.lastUserMonitor = new Object();
        ToastManager.lastUserHide = null;
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                Toast toast0;
                try {
                    Thread.sleep(3000L);
                }
                catch(Throwable e) {
                    Log.e("toastThread", e);
                }
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        Object object0 = ToastManager.lastMonitor;
                        synchronized(object0) {
                            toast0 = ToastManager.lastToast;
                            ToastManager.lastToast = null;
                        }
                        if((Config.configClient & 0x2000) != 0) {
                            ThreadManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast toast0 = Toast.makeText(ToastManager.getContext(), " ", 1);
                                        View view0 = toast0.getView();
                                        view0.setBackgroundColor(0);
                                        toast0.setView(view0);
                                        ToastManager.fixToast(toast0).show();
                                        Object object0 = ToastManager.lastMonitor;
                                        synchronized(object0) {
                                            ToastManager.lastToast = toast0;
                                        }
                                    }
                                    catch(Throwable e) {
                                        Log.w("Toast show", e);
                                    }
                                }
                            });
                        }
                        ToastManager.cancel(toast0);
                        Thread.sleep(1500L);
                    }
                    catch(Throwable e) {
                        Log.e("toastThread", e);
                    }
                }
            }
        }, "toastThread").start();
        ToastManager.source = null;
        ToastManager.fix = null;
    }

    // 检测为 Lambda 实现
    private static void cancel(Toast prevToast) [...]

    private static Toast fixToast(Toast t) {
        try {
            Field field0 = Toast.class.getDeclaredField("mTN");
            field0.setAccessible(true);
            Object object0 = field0.get(t);
            Field[] arr_field = object0.getClass().getDeclaredFields();
            for(int v = 0; true; ++v) {
                if(v >= arr_field.length) {
                    return t;
                }
                Field field = arr_field[v];
                String s = field.getName();
                if(s.equals("mShow") || s.equals("mHide")) {
                    field.setAccessible(true);
                    Object object1 = field.get(object0);
                    if(object1 instanceof Runnable) {
                        field.set(object0, RunnableWrapper.wrap(((Runnable)object1)));
                    }
                }
                else if(s.equals("mHandler")) {
                    field.setAccessible(true);
                    Object object2 = field.get(object0);
                    if(object2 instanceof Handler) {
                        field.set(object0, HandlerWrapper.wrap(((Handler)object2)));
                    }
                }
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return t;
        }
    }

    public static Context getContext() {
        return Tools.getContext();
    }

    public static void init() {
    }

    public static void show(Toast toast) {
        Toast prevToast;
        Toast prevUserToast;
        Runnable runnable0 = null;
        synchronized(ToastManager.lastUserMonitor) {
            prevUserToast = ToastManager.lastUserToast == null ? null : ((Toast)ToastManager.lastUserToast.get());
            ToastManager.lastUserToast = null;
            WeakReference wrRunnable = ToastManager.lastUserHide;
            if(wrRunnable != null) {
                runnable0 = (Runnable)wrRunnable.get();
            }
            ToastManager.lastUserHide = null;
        }
        ToastManager.fixToast(toast).show();
        android.ext.ToastManager.2 hide = () -> {
            if(this.val$toast != null) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            this.val$toast.cancel();
                        }
                        catch(Throwable e) {
                            Log.w("Toast cancel", e);
                        }
                    }
                });
            }
        };
        synchronized(ToastManager.lastUserMonitor) {
            ToastManager.lastUserToast = new WeakReference(toast);
            ToastManager.lastUserHide = new WeakReference(hide);
        }
        synchronized(ToastManager.lastMonitor) {
            prevToast = ToastManager.lastToast;
            ToastManager.lastToast = null;
        }
        ToastManager.cancel(prevToast);
        ToastManager.cancel(prevUserToast);
        Handler handler0 = ThreadManager.getHandlerUiThread();
        if(runnable0 != null) {
            handler0.removeCallbacks(runnable0);
        }
        handler0.postDelayed(hide, 3000L);

        class android.ext.ToastManager.2 implements Runnable {
            android.ext.ToastManager.2(Toast toast0) {
            }

            @Override
            public void run() {
                ToastManager.cancel(this.val$toast);
            }
        }

    }
}

