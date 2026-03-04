package android.toast;

import android.app.Activity;

public class ActivityToast extends CustomToast {
    private final ToastImpl mToastImpl;

    public ActivityToast(Activity activity0) {
        this.mToastImpl = new ToastImpl(activity0, this);
    }

    @Override  // android.toast.config.IToast
    public void cancel() {
        this.mToastImpl.cancel();
    }

    @Override  // android.toast.config.IToast
    public void show() {
        this.mToastImpl.show();
    }
}

