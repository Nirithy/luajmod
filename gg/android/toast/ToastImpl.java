package android.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

final class ToastImpl {
    private static final Handler HANDLER;
    private final Runnable mCancelRunnable;
    private boolean mGlobalShow;
    private final String mPackageName;
    private boolean mShow;
    private final Runnable mShowRunnable;
    private final CustomToast mToast;
    private WindowLifecycle mWindowLifecycle;

    static {
        ToastImpl.HANDLER = new Handler(Looper.getMainLooper());
    }

    ToastImpl(Activity activity0, CustomToast customToast0) {
        this(activity0, customToast0);
        this.mGlobalShow = false;
        this.mWindowLifecycle = new WindowLifecycle(activity0);
    }

    ToastImpl(Application application0, CustomToast customToast0) {
        this(application0, customToast0);
        this.mGlobalShow = true;
        this.mWindowLifecycle = new WindowLifecycle(application0);
    }

    private ToastImpl(Context context0, CustomToast customToast0) {
        this.mShowRunnable = new Runnable() {
            // 检测为 Lambda 实现
            void lambda$run$0$android-toast-ToastImpl$1() [...]

            @Override
            public void run() {
                WindowManager windowManager0 = ToastImpl.this.mWindowLifecycle.getWindowManager();
                if(windowManager0 == null) {
                    return;
                }
                WindowManager.LayoutParams windowManager$LayoutParams0 = new WindowManager.LayoutParams();
                windowManager$LayoutParams0.height = -2;
                windowManager$LayoutParams0.width = -2;
                windowManager$LayoutParams0.format = -3;
                windowManager$LayoutParams0.flags = 0x98;
                windowManager$LayoutParams0.packageName = ToastImpl.this.mPackageName;
                windowManager$LayoutParams0.gravity = ToastImpl.this.mToast.getGravity();
                windowManager$LayoutParams0.x = ToastImpl.this.mToast.getXOffset();
                windowManager$LayoutParams0.y = ToastImpl.this.mToast.getYOffset();
                windowManager$LayoutParams0.verticalMargin = ToastImpl.this.mToast.getVerticalMargin();
                windowManager$LayoutParams0.horizontalMargin = ToastImpl.this.mToast.getHorizontalMargin();
                windowManager$LayoutParams0.windowAnimations = ToastImpl.this.mToast.getAnimationsId();
                if(ToastImpl.this.mGlobalShow) {
                    if(Build.VERSION.SDK_INT >= 26) {
                        windowManager$LayoutParams0.type = 0x7F6;
                        windowManager$LayoutParams0.flags &= -17;
                    }
                    else {
                        windowManager$LayoutParams0.type = 2003;
                    }
                }
                try {
                    windowManager0.addView(ToastImpl.this.mToast.getView(), windowManager$LayoutParams0);
                    ToastImpl.HANDLER.postDelayed(() -> ToastImpl.this.cancel(), (ToastImpl.this.mToast.getDuration() == 1 ? ((long)ToastImpl.this.mToast.getLongDuration()) : ((long)ToastImpl.this.mToast.getShortDuration())));
                    ToastImpl.this.mWindowLifecycle.register(ToastImpl.this);
                    ToastImpl.this.setShow(true);
                    ToastImpl.this.trySendAccessibilityEvent(ToastImpl.this.mToast.getView());
                }
                catch(IllegalStateException | WindowManager.BadTokenException illegalStateException0) {
                    illegalStateException0.printStackTrace();
                }
            }
        };
        this.mCancelRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    WindowManager windowManager0 = ToastImpl.this.mWindowLifecycle.getWindowManager();
                    if(windowManager0 != null) {
                        windowManager0.removeViewImmediate(ToastImpl.this.mToast.getView());
                    }
                }
                catch(IllegalArgumentException illegalArgumentException0) {
                    illegalArgumentException0.printStackTrace();
                }
                finally {
                    ToastImpl.this.mWindowLifecycle.unregister();
                    ToastImpl.this.setShow(false);
                }
            }
        };
        this.mToast = customToast0;
        this.mPackageName = "com.ggdqo";
    }

    void cancel() {
        if(!this.isShow()) {
            return;
        }
        Handler handler0 = ToastImpl.HANDLER;
        handler0.removeCallbacks(this.mShowRunnable);
        if(this.isMainThread()) {
            this.mCancelRunnable.run();
            return;
        }
        handler0.removeCallbacks(this.mCancelRunnable);
        handler0.post(this.mCancelRunnable);
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    boolean isShow() {
        return this.mShow;
    }

    void setShow(boolean z) {
        this.mShow = z;
    }

    void show() {
        if(this.isShow()) {
            return;
        }
        if(this.isMainThread()) {
            this.mShowRunnable.run();
            return;
        }
        ToastImpl.HANDLER.removeCallbacks(this.mShowRunnable);
        ToastImpl.HANDLER.post(this.mShowRunnable);
    }

    private void trySendAccessibilityEvent(View view0) {
        AccessibilityEvent accessibilityEvent0;
        AccessibilityManager accessibilityManager0 = (AccessibilityManager)view0.getContext().getSystemService("accessibility");
        if(!accessibilityManager0.isEnabled()) {
            return;
        }
        if(Build.VERSION.SDK_INT >= 30) {
            accessibilityEvent0 = new AccessibilityEvent();
            accessibilityEvent0.setEventType(0x40);
        }
        else {
            accessibilityEvent0 = AccessibilityEvent.obtain(0x40);
        }
        accessibilityEvent0.setClassName("android.widget.Toast");
        accessibilityEvent0.setPackageName("com.ggdqo");
        view0.dispatchPopulateAccessibilityEvent(accessibilityEvent0);
        accessibilityManager0.sendAccessibilityEvent(accessibilityEvent0);
    }
}

