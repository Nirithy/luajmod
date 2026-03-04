package android.ext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public class ServiceBase {
    private Context mContext;

    protected ServiceBase(Context context) {
        this.mContext = context;
    }

    protected Context getContext() {
        return this.mContext;
    }

    protected SharedPreferences getSharedPreferences(String name, int mode) {
        return this.mContext.getSharedPreferences(name, mode);
    }

    protected Object getSystemService(String name) {
        return this.mContext.getSystemService(name);
    }

    protected Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return this.mContext.registerReceiver(receiver, filter);
    }

    protected void stopSelf() {
        BootstrapService service = BootstrapService.instance;
        BootstrapService.stop = true;
        if(service != null) {
            Log.d("BootstrapService stopSelf");
            service.stopSelf();
        }
        BootstrapInstrumentation instrumentation = BootstrapInstrumentation.mInstance;
        if(instrumentation != null) {
            instrumentation.onDestroy();
        }
    }

    protected void unregisterReceiver(BroadcastReceiver receiver) {
        this.mContext.unregisterReceiver(receiver);
    }
}

