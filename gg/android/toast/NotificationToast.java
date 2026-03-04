package android.toast;

import android.app.Application;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NotificationToast extends SystemToast {
    private static boolean sHookService;

    public NotificationToast(Application application0) {
        super(application0);
    }

    private static void hookNotificationService() {
        if(NotificationToast.sHookService) {
            return;
        }
        try {
            NotificationToast.sHookService = true;
            Method method0 = Toast.class.getDeclaredMethod("getService");
            method0.setAccessible(true);
            Object object0 = method0.invoke(null);
            if(object0 == null) {
                return;
            }
            if(Proxy.isProxyClass(object0.getClass()) && Proxy.getInvocationHandler(object0) instanceof NotificationServiceProxy) {
                return;
            }
            Object object1 = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{Class.forName("android.app.INotificationManager")}, new NotificationServiceProxy(object0));
            Field field0 = Toast.class.getDeclaredField("sService");
            field0.setAccessible(true);
            field0.set(null, object1);
        }
        catch(Exception exception0) {
            exception0.printStackTrace();
        }
    }

    @Override  // android.widget.Toast, android.toast.config.IToast
    public void show() {
        NotificationToast.hookNotificationService();
        super.show();
    }
}

