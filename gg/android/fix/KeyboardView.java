package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class KeyboardView extends android.inputmethodservice.KeyboardView {
    public static class ContextWrapperInner extends ContextWrapper {
        Context base;

        public ContextWrapperInner(Context base) {
            super(base);
            this.base = base;
        }

        // 去混淆评级： 低(20)
        @Override  // android.content.ContextWrapper
        public Object getSystemService(String name) {
            return "audio".equals(name) ? null : this.base.getSystemService(name);
        }
    }

    public static volatile boolean inEditMode;

    static {
        KeyboardView.inEditMode = true;
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        if(KeyboardView.inEditMode) {
            context = new ContextWrapperInner(context);
        }
        super(context, attrs);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        if(KeyboardView.inEditMode) {
            context = new ContextWrapperInner(context);
        }
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if(KeyboardView.inEditMode) {
            context = new ContextWrapperInner(context);
        }
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override  // android.inputmethodservice.KeyboardView
    public void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }
        catch(OutOfMemoryError e) {
            Log.badImplementation(e);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.inputmethodservice.KeyboardView
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }
}

