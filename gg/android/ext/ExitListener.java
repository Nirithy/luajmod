package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;

public class ExitListener implements DialogInterface.OnClickListener {
    private boolean restart;
    private final int source;

    public ExitListener(int src) {
        this(src, false);
    }

    public ExitListener(int src, boolean restart) {
        this.source = src;
        this.restart = restart;
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface arg0, int arg1) {
        Log.d(("ExitListener called: " + this.source));
        Main.doRestart = this.restart ? 1 : 0;
        Main.exit();
    }
}

