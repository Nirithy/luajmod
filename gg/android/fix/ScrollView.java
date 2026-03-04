package android.fix;

import android.annotation.TargetApi;
import android.content.Context;
import android.ext.ExceptionHandler;
import android.ext.Log;
import android.ext.Tools;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollView extends android.widget.ScrollView {
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_VISIBLE = 1;
    private static final long TAP_TIMEOUT;
    private long mPendingDrag;
    private int mState;
    private boolean useFastScroll;

    static {
        ScrollView.TAP_TIMEOUT = 100L;
    }

    public ScrollView(Context context) {
        super(context);
        this.useFastScroll = false;
        this.mPendingDrag = -1L;
        this.mState = 1;
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.useFastScroll = false;
        this.mPendingDrag = -1L;
        this.mState = 1;
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.useFastScroll = false;
        this.mPendingDrag = -1L;
        this.mState = 1;
    }

    @TargetApi(23)
    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.useFastScroll = false;
        this.mPendingDrag = -1L;
        this.mState = 1;
    }

    private void beginDrag() {
        this.mPendingDrag = -1L;
        this.mState = 2;
        this.requestDisallowInterceptTouchEvent(true);
        this.cancelFling();
    }

    private void cancelFling() {
        MotionEvent motionEvent0 = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
        this.onTouchEvent(motionEvent0);
        motionEvent0.recycle();
    }

    private void cancelPendingDrag() {
        this.mPendingDrag = -1L;
    }

    private int getPos(float y) {
        int v = this.getHeight();
        int v1 = Tools.dp2px48();
        float maxY = (float)this.getChildAt(0).getHeight();
        return (int)Math.min(Math.max((y - ((float)(v1 / 2))) / (((float)(v - 1)) - 2.0f * ((float)(v1 / 2))) * maxY, 0.0f), maxY);
    }

    private boolean isPointInsideX(float x) {
        return x >= ((float)(this.getWidth() - Tools.dp2px48()));
    }

    @Override  // android.widget.ScrollView
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.ScrollView
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.useFastScroll) {
            switch(ev.getActionMasked()) {
                case 0: {
                    if(this.isPointInsideX(ev.getX())) {
                        this.startPendingDrag();
                        return super.onInterceptTouchEvent(ev);
                    }
                    break;
                }
                case 2: {
                    if(!this.isPointInsideX(ev.getX())) {
                        this.cancelPendingDrag();
                        return super.onInterceptTouchEvent(ev);
                    }
                    if(this.mPendingDrag >= 0L && this.mPendingDrag <= SystemClock.uptimeMillis()) {
                        this.beginDrag();
                        this.scrollTo(this.getScrollX(), this.getPos(ev.getY()));
                        return this.onTouchEvent(ev);
                    }
                    break;
                }
                case 1: 
                case 3: {
                    this.cancelPendingDrag();
                    return super.onInterceptTouchEvent(ev);
                }
                default: {
                    return super.onInterceptTouchEvent(ev);
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override  // android.widget.ScrollView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }

    @Override  // android.widget.ScrollView
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        try {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.ScrollView
    public boolean onTouchEvent(MotionEvent me) {
        if(this.useFastScroll) {
            switch(me.getActionMasked()) {
                case 0: {
                    if(this.isPointInsideX(me.getX())) {
                        this.beginDrag();
                        return true;
                    }
                    break;
                }
                case 1: {
                    if(this.mPendingDrag >= 0L) {
                        this.beginDrag();
                        this.scrollTo(this.getScrollX(), this.getPos(me.getY()));
                    }
                    if(this.mState == 2) {
                        this.requestDisallowInterceptTouchEvent(false);
                        this.mState = 1;
                        return true;
                    }
                    break;
                }
                case 2: {
                    if(this.mPendingDrag >= 0L) {
                        this.beginDrag();
                    }
                    if(this.mState == 2) {
                        this.scrollTo(this.getScrollX(), this.getPos(me.getY()));
                        return true;
                    }
                    break;
                }
                case 3: {
                    this.cancelPendingDrag();
                }
            }
        }
        try {
            return super.onTouchEvent(me);
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

    public void setFastScrollEnabled(boolean enabled) {
        this.useFastScroll = enabled;
        if(Build.VERSION.SDK_INT >= 16) {
            this.setScrollBarSize(Tools.dp2px48() / 2);
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

    private void startPendingDrag() {
        this.mPendingDrag = SystemClock.uptimeMillis() + ScrollView.TAP_TIMEOUT;
    }
}

