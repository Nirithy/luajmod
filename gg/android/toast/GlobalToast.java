package android.toast;

import android.app.Application;

public class GlobalToast extends CustomToast {
    private final ToastImpl mToastImpl;

    public GlobalToast(Application application0) {
        this.mToastImpl = new ToastImpl(application0, this);
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

