package android.ext;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UiToggler extends BroadcastReceiver {
    private static final String ACTION = "action";
    public static final int ACTION_SCREENSHOT = 1;
    public static final int ACTION_TOGGLE_UI = 0;
    private static final String HASH = "check-hash";
    private static final int hash;

    static {
        UiToggler.hash = Tools.RANDOM.nextInt();
    }

    private static Intent getIntent() {
        String s = Tools.getPackageName();
        Intent intent = new Intent();
        intent.setClassName(s, s + ".Receiver");
        intent.setAction(s);
        intent.setFlags((intent.getFlags() | 0x1000020) & 0xFF7FFFEF);
        intent.putExtra("check-hash", UiToggler.hash);
        return intent;
    }

    public static PendingIntent getPendingIntent(int action) {
        try {
            return PendingIntent.getBroadcast(Tools.getContext(), Tools.RANDOM.nextInt(), UiToggler.getIntent().putExtra("action", action), 0x8000000);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return null;
        }
    }

    @Override  // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            Log.w(("onReceive broadcast: " + intent + "; " + context + "; " + MainService.instance));
            int res = 0;
            if(MainService.instance == null) {
                res = 1;
            }
            else if(intent == null) {
                res = 2;
            }
            else if(intent.getIntExtra("check-hash", 0) != UiToggler.hash) {
                res = 3;
            }
            Log.w(("Broadcast: " + res));
            if(res != 0) {
                return;
            }
            try {
                context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            }
            catch(Throwable e) {
                Log.w("Failed hide status bar", e);
            }
            if(intent.getIntExtra("action", 0) != 1) {
                MainService.instance.toggleDialog();
                return;
            }
            ConfigListAdapter.takeScreenshot();
        }
        catch(Throwable e) {
            Log.w(("onReceive broadcast fail: " + intent), e);
        }
    }
}

