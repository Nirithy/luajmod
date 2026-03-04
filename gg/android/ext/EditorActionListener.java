package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class EditorActionListener implements TextView.OnEditorActionListener {
    private WeakReference weakDialog;

    public EditorActionListener(AlertDialog dialog) {
        new WeakReference(null);
        this.weakDialog = new WeakReference(dialog);
    }

    @Override  // android.widget.TextView$OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if(actionId == 2 || actionId == 3 || actionId == 4 || actionId == 6) {
            AlertDialog receiver = (AlertDialog)this.weakDialog.get();
            if(receiver instanceof AlertDialog) {
                Button button0 = receiver.getButton(-1);
                if(button0 != null && button0.getVisibility() == 0) {
                    Tools.click(button0);
                    handled = true;
                }
            }
            if(!handled && receiver instanceof DialogInterface.OnClickListener) {
                ((DialogInterface.OnClickListener)receiver).onClick(null, -1);
                handled = true;
            }
            if(!handled && receiver instanceof View.OnClickListener) {
                ((View.OnClickListener)receiver).onClick(v);
                return true;
            }
        }
        return handled;
    }
}

