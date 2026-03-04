package android.toast;

import android.app.Application;
import android.toast.config.IToast;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SystemToast extends Toast implements IToast {
    private TextView mMessageView;

    public SystemToast(Application application0) {
        super(application0);
    }

    @Override  // android.widget.Toast, android.toast.config.IToast
    public void setText(CharSequence charSequence0) {
        super.setText(charSequence0);
        TextView textView0 = this.mMessageView;
        if(textView0 == null) {
            return;
        }
        textView0.setText(charSequence0);
    }

    @Override  // android.widget.Toast, android.toast.config.IToast
    public void setView(View view0) {
        super.setView(view0);
        if(view0 == null) {
            this.mMessageView = null;
            return;
        }
        this.mMessageView = this.findMessageView(view0);
    }
}

