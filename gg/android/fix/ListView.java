package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.ext.MainService;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class ListView extends android.widget.ListView {
    public ListView(Context context) {
        super(context);
    }

    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override  // android.widget.ListView
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = false;
        try {
            boolean badFirmware = true;
            result = super.dispatchTouchEvent(ev);
            badFirmware = false;
        }
        catch(OutOfMemoryError | IndexOutOfBoundsException | NullPointerException e) {
            Log.badImplementation(e);
        }
        if(badFirmware) {
            MainService.reportBadFirmware();
        }
        return result;
    }

    @Override  // android.widget.AbsListView
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.ListView
    protected void layoutChildren() {
        try {
            super.layoutChildren();
        }
        catch(IllegalStateException e) {
            Log.w("Layout failed 1", e);
            ListAdapter listAdapter0 = this.getAdapter();
            if(listAdapter0 instanceof BaseAdapter) {
                ((BaseAdapter)listAdapter0).notifyDataSetChanged();
                try {
                    super.layoutChildren();
                }
                catch(IllegalStateException e2) {
                    Log.w("Layout failed 2", e2);
                }
            }
        }
    }

    @Override  // android.widget.AbsListView
    protected void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        }
        catch(StackOverflowError e) {
            Log.badImplementation(e);
        }
        View view0 = this.getEmptyView();
        if(view0 != null) {
            this.setEmptyView(view0);
        }
    }

    @Override  // android.widget.AbsListView
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        }
        catch(NoSuchMethodError | NullPointerException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.ListView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }

    @Override  // android.widget.AbsListView
    public void setFastScrollEnabled(boolean enabled) {
        try {
            super.setFastScrollEnabled(enabled);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }
}

