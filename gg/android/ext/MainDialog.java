package android.ext;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.DeadObjectException;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainDialog extends Dialog implements DialogInterface.OnDismissListener {
    private View view;

    public MainDialog(Context context, View view) {
        super(context);
        this.view = view;
        this.requestWindowFeature(1);
        this.setContentView(Tools.getViewForAttach(view));
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.setOnDismissListener(this);
    }

    @Override  // android.app.Dialog
    public void dismiss() {
        try {
            super.dismiss();
            Tools.getViewForAttach(this.view);
        }
        catch(Throwable e) {
            Throwable throwable1 = e.getCause();
            if(throwable1 != null && throwable1 instanceof DeadObjectException) {
                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700EB)).setMessage(Re.s(0x7F0700EC)).setPositiveButton(Re.s(0x7F0700B8), new ExitListener(900)).setNegativeButton(Re.s(0x7F0700B9), null));  // string:failed_remove_window "Failed to remove window"
            }
            Log.e("main dismiss failed", e);
        }
        if(this.isShowing()) {
            Log.w("main hide fail");
        }
    }

    @Override  // android.app.Dialog
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    private WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = SystemConstants.getTypePhone();
        params.width = -1;
        params.height = -1;
        params.format = -2;
        params.horizontalMargin = 0.0f;
        params.horizontalWeight = 1.0f;
        params.verticalMargin = 0.0f;
        params.verticalWeight = 1.0f;
        params.dimAmount = 0.0f;
        return params;
    }

    @Override  // android.app.Dialog
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("MainDialog back");
        MainService.instance.dismissDialog();
    }

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        Log.d("MainDialog dismiss");
    }

    @Override  // android.app.Dialog
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(super.onKeyDown(keyCode, event)) {
            return true;
        }
        if(keyCode == 82) {
            event.startTracking();
            return true;
        }
        return false;
    }

    @Override  // android.app.Dialog
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(super.onKeyUp(keyCode, event)) {
            return true;
        }
        if(keyCode == 82 && event.isTracking() && !event.isCanceled()) {
            this.onMenuPressed();
            return true;
        }
        return false;
    }

    public void onMenuPressed() {
        Log.d("MainDialog menu");
        MainService.instance.onMenuPressed();
    }

    @Override  // android.app.Dialog
    public void setContentView(int layoutResID) {
        try {
            super.setContentView(layoutResID);
        }
        catch(RuntimeException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Dialog
    public void setContentView(View view) {
        try {
            super.setContentView(view);
        }
        catch(RuntimeException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        try {
            super.setContentView(view, params);
        }
        catch(RuntimeException e) {
            Log.badImplementation(e);
        }
    }

    @TargetApi(11)
    private void setup() {
        this.view.setPadding(0, ((Config.configClient & 16) == 0 ? 0 : ((int)Tools.dp2px(24.0f))), 0, 0);
        Window window0 = this.getWindow();
        window0.setAttributes(this.getLayoutParams());
        window0.setBackgroundDrawable(new ColorDrawable(0));
        if((Config.configClient & 2) == 0) {
            window0.clearFlags(0x1000000);
        }
        else {
            window0.addFlags(0x1000000);
        }
        ShowApp.register(window0);
    }

    @Override  // android.app.Dialog
    public void show() {
        this.setup();
        Tools.fixViewParams(this.getWindow());
        try {
            super.show();
            Tools.fixViews(this.getWindow().getDecorView());
        }
        catch(Throwable e) {
            Throwable throwable1 = e.getCause();
            if(throwable1 != null && throwable1 instanceof DeadObjectException) {
                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700EA)).setMessage(Re.s(0x7F0700EC)).setPositiveButton(Re.s(0x7F0700B8), new ExitListener(800)).setNegativeButton(Re.s(0x7F0700B9), null));  // string:failed_add_window "Failed to add window"
            }
            Log.e("main show failed", e);
        }
        if(!this.isShowing()) {
            Log.w("main show fail");
        }
    }
}

