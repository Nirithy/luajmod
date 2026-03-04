package android.ext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.fix.ContextWrapper;
import android.os.Bundle;

public class ServiceContext extends ContextWrapper {
    public ServiceContext(Context base) {
        super(base);
    }

    @Override  // android.content.ContextWrapper
    public Context getApplicationContext() {
        Context context = super.getApplicationContext();
        return context == null ? BaseActivity.appContext : context;
    }

    @Override  // android.fix.ContextWrapper
    public Object getSystemService(String name) {
        try {
            return super.getSystemService(name);
        }
        catch(Throwable e) {
            Log.e(("getSystemService fail: " + name), e);
            return null;
        }
    }

    @Override  // android.content.ContextWrapper
    public void startActivities(Intent[] intents) {
        if(intents != null) {
            for(int v = 0; v < intents.length; ++v) {
                Intent intent = intents[v];
                if(intent != null) {
                    intent.setFlags(0x10000000);
                }
            }
        }
        super.startActivities(intents);
    }

    @Override  // android.content.ContextWrapper
    public void startActivities(Intent[] intents, Bundle options) {
        if(intents != null) {
            for(int v = 0; v < intents.length; ++v) {
                Intent intent = intents[v];
                if(intent != null) {
                    intent.setFlags(0x10000000);
                }
            }
        }
        super.startActivities(intents, options);
    }

    @Override  // android.fix.ContextWrapper
    public void startActivity(Intent intent) {
        if(intent != null) {
            intent.setFlags(0x10000000);
        }
        super.startActivity(intent);
    }

    @Override  // android.content.ContextWrapper
    public void startActivity(Intent intent, Bundle options) {
        if(intent != null) {
            intent.setFlags(0x10000000);
        }
        super.startActivity(intent, options);
    }

    @Override  // android.fix.ContextWrapper
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            super.unregisterReceiver(receiver);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.fix.ContextWrapper
    public static Context wrap(Context context) {
        return context != null && !(context instanceof ServiceContext) ? new ServiceContext(context) : context;
    }
}

