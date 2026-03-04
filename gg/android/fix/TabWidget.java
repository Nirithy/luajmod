package android.fix;

import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.util.AttributeSet;

public class TabWidget extends android.widget.TabWidget {
    public TabWidget(Context context) {
        super(context);
    }

    public TabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override  // android.widget.TabWidget
    public void focusCurrentTab(int index) {
        try {
            super.focusCurrentTab(index);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
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
    public boolean performClick() {
        try {
            return super.performClick();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
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

