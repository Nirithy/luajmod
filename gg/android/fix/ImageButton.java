package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.ext.Tools;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.AttributeSet;

public class ImageButton extends android.widget.ImageButton {
    public ImageButton(Context context) {
        super(context);
        if(Build.VERSION.SDK_INT < 11) {
            Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
            this.setMinimumHeight(Tools.dp2px48());
        }
    }

    public ImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(Build.VERSION.SDK_INT < 11) {
            Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
            this.setMinimumHeight(Tools.dp2px48());
        }
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(Build.VERSION.SDK_INT < 11) {
            Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
            this.setMinimumHeight(Tools.dp2px48());
        }
    }

    @TargetApi(21)
    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(Build.VERSION.SDK_INT < 11) {
            Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
            this.setMinimumHeight(Tools.dp2px48());
        }
    }

    @Override  // android.widget.ImageView
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }
        catch(NoSuchFieldError e) {
            Log.badImplementation(e);
        }
        catch(RuntimeException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.ImageView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }

    @Override  // android.view.View
    public void playSoundEffect(int soundConstant) {
        try {
            super.playSoundEffect(soundConstant);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    public void sendAccessibilityEvent(int eventType) {
        try {
            super.sendAccessibilityEvent(eventType);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }
}

