package android.toast;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.toast.config.IToast;
import android.toast.config.IToastStrategy;
import android.toast.config.IToastStyle;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToastStrategy implements IToastStrategy {
    class CancelToastRunnable implements Runnable {
        private CancelToastRunnable() {
        }

        CancelToastRunnable(android.toast.ToastStrategy.1 toastStrategy$10) {
        }

        @Override
        public void run() {
            IToast iToast0 = ToastStrategy.this.mToastReference == null ? null : ((IToast)ToastStrategy.this.mToastReference.get());
            if(iToast0 == null) {
                return;
            }
            iToast0.cancel();
        }
    }

    class ShowToastRunnable implements Runnable {
        private final ToastParams mToastParams;

        private ShowToastRunnable(ToastParams toastParams0) {
            this.mToastParams = toastParams0;
        }

        ShowToastRunnable(ToastParams toastParams0, android.toast.ToastStrategy.1 toastStrategy$10) {
            this(toastParams0);
        }

        @Override
        public void run() {
            IToast iToast0 = ToastStrategy.this.mToastReference == null ? null : ((IToast)ToastStrategy.this.mToastReference.get());
            if(iToast0 != null) {
                iToast0.cancel();
            }
            IToast iToast1 = ToastStrategy.this.createToast(this.mToastParams);
            WeakReference weakReference0 = new WeakReference(iToast1);
            ToastStrategy.this.mToastReference = weakReference0;
            iToast1.setDuration(this.mToastParams.duration);
            iToast1.setText(this.mToastParams.text);
            iToast1.show();
        }
    }

    private static final int DEFAULT_DELAY_TIMEOUT = 200;
    private static final Handler HANDLER = null;
    public static final int SHOW_STRATEGY_TYPE_IMMEDIATELY = 0;
    public static final int SHOW_STRATEGY_TYPE_QUEUE = 1;
    private Application mApplication;
    private final Object mCancelMessageToken;
    private volatile long mLastShowToastMillis;
    private final Object mShowMessageToken;
    private final int mShowStrategyType;
    private WeakReference mToastReference;

    static {
        ToastStrategy.HANDLER = new Handler(Looper.getMainLooper());
    }

    public ToastStrategy() {
        this(0);
    }

    public ToastStrategy(int v) {
        this.mShowMessageToken = new Object();
        this.mCancelMessageToken = new Object();
        this.mShowStrategyType = v;
        if(v != 0 && v != 1) {
            throw new IllegalArgumentException("Please don\'t pass non-existent toast show strategy");
        }
    }

    protected boolean areNotificationsEnabled(Context context0) {
        return ((NotificationManager)context0.getSystemService(NotificationManager.class)).areNotificationsEnabled();
    }

    @Override  // android.toast.config.IToastStrategy
    public void cancelToast() {
        ToastStrategy.HANDLER.removeCallbacksAndMessages(this.mCancelMessageToken);
        long v = SystemClock.uptimeMillis();
        CancelToastRunnable toastStrategy$CancelToastRunnable0 = new CancelToastRunnable(this, null);
        ToastStrategy.HANDLER.postAtTime(toastStrategy$CancelToastRunnable0, this.mCancelMessageToken, v);
    }

    @Override  // android.toast.config.IToastStrategy
    public IToast createToast(ToastParams toastParams0) {
        IToast iToast0;
        Activity activity0 = this.getForegroundActivity();
        if(Settings.canDrawOverlays(this.mApplication)) {
            iToast0 = new GlobalToast(this.mApplication);
        }
        else if(!toastParams0.crossPageShow && this.isActivityAvailable(activity0)) {
            iToast0 = new ActivityToast(activity0);
        }
        else if(Build.VERSION.SDK_INT == 25) {
            iToast0 = new SafeToast(this.mApplication);
        }
        else if(Build.VERSION.SDK_INT >= 29 || this.areNotificationsEnabled(this.mApplication)) {
            iToast0 = new SystemToast(this.mApplication);
        }
        else {
            iToast0 = new NotificationToast(this.mApplication);
        }
        if(this.isSupportToastStyle(iToast0) || !this.onlyShowSystemToastStyle()) {
            this.diyToastStyle(iToast0, toastParams0.style);
        }
        return iToast0;
    }

    protected void diyToastStyle(IToast iToast0, IToastStyle iToastStyle0) {
        iToast0.setView(iToastStyle0.createView(this.mApplication));
        iToast0.setGravity(iToastStyle0.getGravity(), iToastStyle0.getXOffset(), iToastStyle0.getYOffset());
        iToast0.setMargin(iToastStyle0.getHorizontalMargin(), iToastStyle0.getVerticalMargin());
    }

    protected int generateToastWaitMillis(ToastParams toastParams0) {
        switch(toastParams0.duration) {
            case 0: {
                return 1000;
            }
            case 1: {
                return 1500;
            }
            default: {
                return 0;
            }
        }
    }

    protected Activity getForegroundActivity() {
        return ActivityStack.getInstance().getForegroundActivity();
    }

    protected boolean isActivityAvailable(Activity activity0) {
        if(activity0 == null) {
            return false;
        }
        return activity0.isFinishing() ? false : !activity0.isDestroyed();
    }

    protected boolean isChangeEnabledCompat(long v) {
        if(Build.VERSION.SDK_INT < 30) {
            return true;
        }
        try {
            Method method0 = Class.forName("android.app.compat.CompatChanges").getMethod("isChangeEnabled", Long.TYPE);
            method0.setAccessible(true);
            return Boolean.parseBoolean(String.valueOf(method0.invoke(null, v)));
        }
        catch(ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException classNotFoundException0) {
            classNotFoundException0.printStackTrace();
            return false;
        }
    }

    protected boolean isSupportToastStyle(IToast iToast0) {
        return true;
    }

    protected boolean onlyShowSystemToastStyle() {
        return this.isChangeEnabledCompat(0x8CF3B87L);
    }

    @Override  // android.toast.config.IToastStrategy
    public void registerStrategy(Application application0) {
        this.mApplication = application0;
    }

    @Override  // android.toast.config.IToastStrategy
    public void showToast(ToastParams toastParams0) {
        int v = 0;
        switch(this.mShowStrategyType) {
            case 0: {
                Handler handler0 = ToastStrategy.HANDLER;
                handler0.removeCallbacksAndMessages(this.mShowMessageToken);
                long v1 = SystemClock.uptimeMillis() + toastParams0.delayMillis;
                if(!toastParams0.crossPageShow) {
                    v = 200;
                }
                handler0.postAtTime(new ShowToastRunnable(this, toastParams0, null), this.mShowMessageToken, v1 + ((long)v));
                return;
            }
            case 1: {
                long v2 = SystemClock.uptimeMillis() + toastParams0.delayMillis;
                if(!toastParams0.crossPageShow) {
                    v = 200;
                }
                long v3 = v2 + ((long)v);
                long v4 = (long)this.generateToastWaitMillis(toastParams0);
                if(v3 < this.mLastShowToastMillis + v4) {
                    v3 = this.mLastShowToastMillis + v4;
                }
                ShowToastRunnable toastStrategy$ShowToastRunnable0 = new ShowToastRunnable(this, toastParams0, null);
                ToastStrategy.HANDLER.postAtTime(toastStrategy$ShowToastRunnable0, this.mShowMessageToken, v3);
                this.mLastShowToastMillis = v3;
            }
        }
    }

    class android.toast.ToastStrategy.1 {
    }

}

