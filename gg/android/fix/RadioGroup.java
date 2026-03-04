package android.fix;

import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.util.AttributeSet;

public class RadioGroup extends android.widget.RadioGroup {
    public RadioGroup(Context context) {
        super(context);
    }

    public RadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
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

