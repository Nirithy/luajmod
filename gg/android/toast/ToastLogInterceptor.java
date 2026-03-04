package android.toast;

import android.toast.config.IToastInterceptor;
import android.util.Log;
import java.lang.reflect.Modifier;

public class ToastLogInterceptor implements IToastInterceptor {
    // 去混淆评级： 低(40)
    protected boolean filterClass(Class class0) {
        return IToastInterceptor.class.isAssignableFrom(class0) || Toaster.class.equals(class0) || class0.isInterface() || Modifier.isAbstract(class0.getModifiers());
    }

    @Override  // android.toast.config.IToastInterceptor
    public boolean intercept(ToastParams toastParams0) {
        this.printToast(toastParams0.text);
        return false;
    }

    protected boolean isLogEnable() {
        return Toaster.isDebugMode();
    }

    protected void printLog(String s) {
        Log.i("Toaster", s);
    }

    protected void printToast(CharSequence charSequence0) {
        if(!this.isLogEnable()) {
            return;
        }
        StackTraceElement[] arr_stackTraceElement = new Throwable().getStackTrace();
        for(int v = 0; v < arr_stackTraceElement.length; ++v) {
            StackTraceElement stackTraceElement0 = arr_stackTraceElement[v];
            int v1 = stackTraceElement0.getLineNumber();
            if(v1 > 0) {
                String s = stackTraceElement0.getClassName();
                try {
                    if(!this.filterClass(Class.forName(s))) {
                        this.printLog("(" + stackTraceElement0.getFileName() + ":" + v1 + ") " + charSequence0.toString());
                        return;
                    }
                }
                catch(ClassNotFoundException classNotFoundException0) {
                    classNotFoundException0.printStackTrace();
                }
            }
        }
    }
}

