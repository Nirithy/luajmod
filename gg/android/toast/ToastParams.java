package android.toast;

import android.toast.config.IToastInterceptor;
import android.toast.config.IToastStrategy;
import android.toast.config.IToastStyle;

public class ToastParams {
    public boolean crossPageShow;
    public long delayMillis;
    public int duration;
    public IToastInterceptor interceptor;
    public IToastStrategy strategy;
    public IToastStyle style;
    public CharSequence text;

    public ToastParams() {
        this.duration = -1;
        this.delayMillis = 0L;
    }
}

