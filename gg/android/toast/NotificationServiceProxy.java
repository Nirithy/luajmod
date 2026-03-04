package android.toast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

final class NotificationServiceProxy implements InvocationHandler {
    private final Object mRealObject;

    public NotificationServiceProxy(Object object0) {
        this.mRealObject = object0;
    }

    @Override
    public Object invoke(Object object0, Method method0, Object[] arr_object) throws Throwable {
        int v;
        switch(method0.getName()) {
            case "cancelToast": {
                v = 2;
                break;
            }
            case "enqueueToast": {
                v = 0;
                break;
            }
            case "enqueueToastEx": {
                v = 1;
                break;
            }
            default: {
                v = -1;
            }
        }
        if(v == 0 || v == 1 || v == 2) {
            arr_object[0] = "android";
        }
        return method0.invoke(this.mRealObject, arr_object);
    }
}

