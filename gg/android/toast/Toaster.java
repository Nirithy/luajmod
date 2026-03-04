package android.toast;

import android.app.Application;
import android.content.res.Resources.NotFoundException;
import android.toast.config.IToastInterceptor;
import android.toast.config.IToastStrategy;
import android.toast.config.IToastStyle;
import android.toast.style.BlackToastStyle;
import android.toast.style.CustomToastStyle;
import android.toast.style.LocationToastStyle;

public final class Toaster {
    private static Application sApplication;
    private static Boolean sDebugMode;
    private static IToastInterceptor sToastInterceptor;
    private static IToastStrategy sToastStrategy;
    private static IToastStyle sToastStyle;

    public static void cancel() {
        Toaster.sToastStrategy.cancelToast();
    }

    private static void checkInitStatus() {
        if(Toaster.sApplication == null) {
            throw new IllegalStateException("Toaster has not been initialized");
        }
    }

    public static void debugShow(int v) {
        Toaster.debugShow(Toaster.stringIdToCharSequence(v));
    }

    public static void debugShow(CharSequence charSequence0) {
        if(!Toaster.isDebugMode()) {
            return;
        }
        ToastParams toastParams0 = new ToastParams();
        toastParams0.text = charSequence0;
        Toaster.show(toastParams0);
    }

    public static void debugShow(Object object0) {
        Toaster.debugShow(Toaster.objectToCharSequence(object0));
    }

    public static void delayedShow(int v, long v1) {
        Toaster.delayedShow(Toaster.stringIdToCharSequence(v), v1);
    }

    public static void delayedShow(CharSequence charSequence0, long v) {
        ToastParams toastParams0 = new ToastParams();
        toastParams0.text = charSequence0;
        toastParams0.delayMillis = v;
        Toaster.show(toastParams0);
    }

    public static void delayedShow(Object object0, long v) {
        Toaster.delayedShow(Toaster.objectToCharSequence(object0), v);
    }

    public static IToastInterceptor getInterceptor() {
        return Toaster.sToastInterceptor;
    }

    public static IToastStrategy getStrategy() {
        return Toaster.sToastStrategy;
    }

    public static IToastStyle getStyle() {
        return Toaster.sToastStyle;
    }

    public static void init(Application application0) {
        Toaster.init(application0, Toaster.sToastStyle);
    }

    public static void init(Application application0, IToastStrategy iToastStrategy0) {
        Toaster.init(application0, iToastStrategy0, null);
    }

    public static void init(Application application0, IToastStrategy iToastStrategy0, IToastStyle iToastStyle0) {
        Toaster.sApplication = application0;
        ActivityStack.getInstance().register(application0);
        if(iToastStrategy0 == null) {
            iToastStrategy0 = new ToastStrategy();
        }
        Toaster.setStrategy(iToastStrategy0);
        if(iToastStyle0 == null) {
            iToastStyle0 = new BlackToastStyle();
        }
        Toaster.setStyle(iToastStyle0);
    }

    public static void init(Application application0, IToastStyle iToastStyle0) {
        Toaster.init(application0, null, iToastStyle0);
    }

    static boolean isDebugMode() {
        if(Toaster.sDebugMode == null) {
            Toaster.checkInitStatus();
            Toaster.sDebugMode = Boolean.valueOf((Toaster.sApplication.getApplicationInfo().flags & 2) != 0);
        }
        return Toaster.sDebugMode.booleanValue();
    }

    public static boolean isInit() [...] // 潜在的解密器

    private static CharSequence objectToCharSequence(Object object0) {
        return object0 == null ? "null" : object0.toString();
    }

    public static void setDebugMode(boolean z) {
        Toaster.sDebugMode = Boolean.valueOf(z);
    }

    public static void setGravity(int v) {
        Toaster.setGravity(v, 0, 0);
    }

    public static void setGravity(int v, int v1, int v2) {
        Toaster.setGravity(v, v1, v2, 0.0f, 0.0f);
    }

    public static void setGravity(int v, int v1, int v2, float f, float f1) {
        Toaster.sToastStyle = new LocationToastStyle(Toaster.sToastStyle, v, v1, v2, f, f1);
    }

    public static void setInterceptor(IToastInterceptor iToastInterceptor0) {
        Toaster.sToastInterceptor = iToastInterceptor0;
    }

    public static void setStrategy(IToastStrategy iToastStrategy0) {
        if(iToastStrategy0 == null) {
            return;
        }
        Toaster.sToastStrategy = iToastStrategy0;
        iToastStrategy0.registerStrategy(Toaster.sApplication);
    }

    public static void setStyle(IToastStyle iToastStyle0) {
        if(iToastStyle0 == null) {
            return;
        }
        Toaster.sToastStyle = iToastStyle0;
    }

    public static void setView(int v) {
        if(v <= 0) {
            return;
        }
        if(Toaster.sToastStyle == null) {
            return;
        }
        Toaster.setStyle(new CustomToastStyle(v, Toaster.sToastStyle.getGravity(), Toaster.sToastStyle.getXOffset(), Toaster.sToastStyle.getYOffset(), Toaster.sToastStyle.getHorizontalMargin(), Toaster.sToastStyle.getVerticalMargin()));
    }

    public static void show(int v) {
        Toaster.show(Toaster.stringIdToCharSequence(v));
    }

    public static void show(ToastParams toastParams0) {
        Toaster.checkInitStatus();
        if(toastParams0.text != null && toastParams0.text.length() != 0) {
            if(toastParams0.strategy == null) {
                toastParams0.strategy = Toaster.sToastStrategy;
            }
            if(toastParams0.interceptor == null) {
                if(Toaster.sToastInterceptor == null) {
                    Toaster.sToastInterceptor = new ToastLogInterceptor();
                }
                toastParams0.interceptor = Toaster.sToastInterceptor;
            }
            if(toastParams0.style == null) {
                toastParams0.style = Toaster.sToastStyle;
            }
            if(toastParams0.interceptor.intercept(toastParams0)) {
                return;
            }
            if(toastParams0.duration == -1) {
                toastParams0.duration = toastParams0.text.length() <= 20 ? 0 : 1;
            }
            toastParams0.strategy.showToast(toastParams0);
        }
    }

    public static void show(CharSequence charSequence0) {
        ToastParams toastParams0 = new ToastParams();
        toastParams0.text = charSequence0;
        Toaster.show(toastParams0);
    }

    public static void show(Object object0) {
        Toaster.show(Toaster.objectToCharSequence(object0));
    }

    public static void showLong(int v) {
        Toaster.showLong(Toaster.stringIdToCharSequence(v));
    }

    public static void showLong(CharSequence charSequence0) {
        ToastParams toastParams0 = new ToastParams();
        toastParams0.text = charSequence0;
        toastParams0.duration = 1;
        Toaster.show(toastParams0);
    }

    public static void showLong(Object object0) {
        Toaster.showLong(Toaster.objectToCharSequence(object0));
    }

    public static void showShort(int v) {
        Toaster.showShort(Toaster.stringIdToCharSequence(v));
    }

    public static void showShort(CharSequence charSequence0) {
        ToastParams toastParams0 = new ToastParams();
        toastParams0.text = charSequence0;
        toastParams0.duration = 0;
        Toaster.show(toastParams0);
    }

    public static void showShort(Object object0) {
        Toaster.showShort(Toaster.objectToCharSequence(object0));
    }

    private static CharSequence stringIdToCharSequence(int v) {
        Toaster.checkInitStatus();
        try {
            return Toaster.sApplication.getResources().getText(v);
        }
        catch(Resources.NotFoundException unused_ex) {
            return String.valueOf(v);
        }
    }
}

