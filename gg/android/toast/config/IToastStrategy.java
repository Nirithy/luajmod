package android.toast.config;

import android.app.Application;
import android.toast.ToastParams;

public interface IToastStrategy {
    void cancelToast();

    IToast createToast(ToastParams arg1);

    void registerStrategy(Application arg1);

    void showToast(ToastParams arg1);
}

