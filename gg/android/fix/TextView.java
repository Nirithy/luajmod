package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.ext.Tools;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView.BufferType;

public class TextView extends android.widget.TextView {
    private Rect bounds;
    private Paint.FontMetricsInt fm;

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    private static int getDefSize(int size, int measureSpec) {
        int v2 = View.MeasureSpec.getMode(measureSpec);
        int v3 = View.MeasureSpec.getSize(measureSpec);
        switch(v2) {
            case 0x80000000: {
                return Math.min(size, v3);
            }
            case 0: {
                return size;
            }
            case 0x40000000: {
                return v3;
            }
            default: {
                return size;
            }
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

    @Override  // android.view.View
    protected void onDetachedFromWindow() {
        Tools.fixLeak(this);
        super.onDetachedFromWindow();
    }

    @Override  // android.widget.TextView
    public boolean onDragEvent(DragEvent event) {
        try {
            return super.onDragEvent(event);
        }
        catch(ClassCastException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
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

    @Override  // android.widget.TextView
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
            try {
                int v2 = this.getSuggestedMinimumWidth();
                int v3 = this.getSuggestedMinimumHeight();
                int atMost = View.MeasureSpec.getMode(widthMeasureSpec) == 0x80000000 ? View.MeasureSpec.getSize(widthMeasureSpec) : 0x7FFFFFFF;
                if(atMost == 0) {
                    atMost = 0x7FFFFFFF;
                }
                TextPaint textPaint0 = this.getPaint();
                CharSequence charSequence0 = this.getText();
                int lines = 0;
                int maxWidth = (int)(((double)Tools.dp2px(1.0f)) + 0.99);
                int start = 0;
                int v9 = charSequence0.length();
                for(int i = 0; true; ++i) {
                    if(i > v9) {
                        if(this.bounds == null) {
                            this.bounds = new Rect();
                        }
                        if(this.fm == null) {
                            this.fm = new Paint.FontMetricsInt();
                        }
                        textPaint0.getTextBounds("!\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0, 94, this.bounds);
                        int perLine = Math.max(this.bounds.bottom - this.bounds.top + 1, textPaint0.getFontMetricsInt(this.fm));
                        if(perLine <= 5) {
                            perLine = Math.max(perLine, ((int)(((double)this.getTextSize()) * 1.5 + 0.99)));
                        }
                        int measuredWidth = Math.max(v2, this.getCompoundPaddingLeft() + maxWidth + this.getCompoundPaddingRight());
                        int measuredHeight = Math.max(v3, perLine * lines + this.getCompoundPaddingTop() + this.getCompoundPaddingBottom() + (this.fm.bottom - this.fm.top - (this.fm.descent - this.fm.ascent)));
                        this.setMeasuredDimension(TextView.getDefSize(measuredWidth, widthMeasureSpec), TextView.getDefSize(measuredHeight, heightMeasureSpec));
                        return;
                    }
                    if(i == v9 || charSequence0.charAt(i) == 10) {
                        ++lines;
                        if(i > start) {
                            int lineWidth = (int)(((double)textPaint0.measureText(charSequence0, start, i)) + 0.99);
                            if(maxWidth < lineWidth) {
                                maxWidth = lineWidth;
                            }
                            lines += lineWidth / atMost;
                        }
                        start = i + 1;
                    }
                }
            }
            catch(Throwable e2) {
                Log.badImplementation(e2);
                ExceptionHandler.sendException(Thread.currentThread(), e, false);
                ExceptionHandler.sendException(Thread.currentThread(), e2, false);
            }
        }
    }

    @Override  // android.widget.TextView
    public boolean onTextContextMenuItem(int id) {
        try {
            return super.onTextContextMenuItem(id);
        }
        catch(NullPointerException e) {
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
        catch(NullPointerException e) {
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
    public void setText(CharSequence text, TextView.BufferType type) {
        try {
            super.setText(text, type);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    public void setVisibility(int visibility) {
        try {
            super.setVisibility(visibility);
        }
        catch(StackOverflowError e) {
            Log.badImplementation(e);
        }
    }
}

