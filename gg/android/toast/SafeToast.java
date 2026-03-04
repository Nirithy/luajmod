package android.toast;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;
import java.lang.reflect.Field;

public class SafeToast extends NotificationToast {
    private boolean mHookTN;

    public SafeToast(Application application0) {
        super(application0);
    }

    private void hookToastTN() {
        if(this.mHookTN) {
            return;
        }
        try {
            this.mHookTN = true;
            Field field0 = Toast.class.getDeclaredField("mTN");
            field0.setAccessible(true);
            Object object0 = field0.get(this);
            Field field1 = field0.getType().getDeclaredField("mHandler");
            field1.setAccessible(true);
            Handler handler0 = (Handler)field1.get(object0);
            if(handler0 instanceof SafeHandler) {
                return;
            }
            field1.set(object0, new SafeHandler(handler0));
        }
        catch(IllegalAccessException | NoSuchFieldException illegalAccessException0) {
            illegalAccessException0.printStackTrace();
        }
    }

    @Override  // android.toast.NotificationToast
    public void show() {
        this.hookToastTN();
        super.show();
    }
}

