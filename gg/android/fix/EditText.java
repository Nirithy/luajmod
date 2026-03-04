package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView.BufferType;

public class EditText extends android.widget.EditText {
    private static final int imeForce = 0x72000000;

    public EditText(Context context) {
        super(context);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override  // android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        try {
            super.dispatchWindowFocusChanged(hasFocus);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    protected void drawableStateChanged() {
        try {
            super.drawableStateChanged();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    public boolean getSecClipboardEnabled() {
        return false;
    }

    @Override  // android.widget.TextView
    public void invalidateDrawable(Drawable drawable) {
        try {
            super.invalidateDrawable(drawable);
        }
        catch(NoSuchFieldError e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public boolean onDragEvent(DragEvent event) {
        try {
            return super.onDragEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        try {
            int v1 = super.getImeOptions();
            if((v1 & 0x72000000) != 0x72000000) {
                super.setImeOptions(v1 | 0x72000000);
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        try {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        try {
            super.onInitializeAccessibilityNodeInfo(info);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.EditText
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        try {
            return super.onKeyShortcut(keyCode, event);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }

    @Override  // android.widget.EditText
    public boolean onTextContextMenuItem(int id) {
        try {
            return super.onTextContextMenuItem(id);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        try {
            super.onWindowFocusChanged(hasWindowFocus);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public boolean performLongClick() {
        try {
            return super.performLongClick();
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
    public void refreshDrawableState() {
        try {
            super.refreshDrawableState();
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

    @Override  // android.widget.TextView
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        try {
            super.setCompoundDrawables(left, top, right, bottom);
        }
        catch(StringIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public void setImeOptions(int imeOptions) {
        try {
            super.setImeOptions(0x72000000 | imeOptions);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.EditText
    public void setText(CharSequence text, TextView.BufferType type) {
        try {
            super.setText(text, type);
        }
        catch(SecurityException | NullPointerException e) {
            Log.badImplementation(e);
        }
    }
}

