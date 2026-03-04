package android.fix;

import android.content.Context;
import android.content.res.Configuration;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.util.AttributeSet;

public class LinearLayout extends android.widget.LinearLayout {
    public LinearLayout(Context context) {
        super(context);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override  // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        Log.d(("LinearLayout onConfigurationChanged: " + (newConfig == null ? null : newConfig.toString())));
        super.onConfigurationChanged(ContextWrapper.onConfigurationChanged(newConfig));
    }

    @Override  // android.widget.LinearLayout
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

