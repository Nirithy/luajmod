package android.toast;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager.BadTokenException;

final class SafeHandler extends Handler {
    private final Handler mHandler;

    SafeHandler(Handler handler0) {
        this.mHandler = handler0;
    }

    @Override  // android.os.Handler
    public void handleMessage(Message message0) {
        try {
            this.mHandler.handleMessage(message0);
        }
        catch(WindowManager.BadTokenException | IllegalStateException windowManager$BadTokenException0) {
            windowManager$BadTokenException0.printStackTrace();
        }
    }
}

