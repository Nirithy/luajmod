package android.fix;

import android.content.Context;
import android.content.res.Configuration;
import android.ext.Log;
import android.sup.ContainerHelpers;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

public class WrapLayout extends ViewGroup {
    public static final int FILL_FROM_CENTER = 0;
    public static final int FILL_FROM_LEFT = -1;
    public static final int FILL_FROM_RIGHT = 1;
    private int fill;
    private int[] x;

    public WrapLayout(Context context) {
        super(context);
        this.x = ContainerHelpers.EMPTY_INTS;
        this.fill = -1;
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.x = ContainerHelpers.EMPTY_INTS;
        this.fill = -1;
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.x = ContainerHelpers.EMPTY_INTS;
        this.fill = -1;
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.x = ContainerHelpers.EMPTY_INTS;
        this.fill = -1;
    }

    @Override  // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewGroup.LayoutParams;
    }

    @Override  // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-2, -2);
    }

    @Override  // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        View child;
        super.onConfigurationChanged(newConfig);
        boolean toolbar = false;
        int v = this.getChildCount();
        for(int i = 0; i < v; ++i) {
            try {
                child = this.getChildAt(i);
            }
            catch(ArrayIndexOutOfBoundsException unused_ex) {
                child = null;
            }
            if(child != null && child instanceof ToolbarButton) {
                ((ToolbarButton)child).setIcon(false);
                toolbar = true;
            }
        }
        if(toolbar) {
            this.requestLayout();
        }
    }

    @Override  // android.view.ViewGroup
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int v4 = this.getChildCount();
        this.update(false, r - l, b - t);
        int[] xa = this.x;
        int i = 0;
        while(i < v4) {
            try {
                View view0 = this.getChildAt(i);
                if(view0 != null && view0.getVisibility() != 8) {
                    int v6 = view0.getMeasuredWidth();
                    int v7 = view0.getMeasuredHeight();
                    int x = xa[i];
                    int y = xa[v4 + i];
                    view0.layout(x, y, x + v6, y + v7);
                }
                ++i;
                continue;
            }
            catch(ArrayIndexOutOfBoundsException unused_ex) {
            }
            ++i;
        }
    }

    @Override  // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int v2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int v4 = this.update(true, v2, height);
        if(View.MeasureSpec.getMode(heightMeasureSpec) == 0) {
            height = v4;
        }
        else if(View.MeasureSpec.getMode(heightMeasureSpec) == 0x80000000 && v4 <= height) {
            height = v4;
        }
        this.setMeasuredDimension(v2, height);
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

    public void setFill(int fill) {
        int old = this.fill;
        this.fill = fill;
        if(old != fill) {
            this.requestLayout();
            this.postInvalidate();
        }
    }

    private int update(boolean force, int width, int height) {
        int v14;
        View child;
        int v4;
        int v3;
        int v2 = this.getChildCount();
        if(!force && this.x.length == v2 * 2) {
            return 0;
        }
        boolean fillCenter = this.fill == 0;
        boolean fillRight = this.fill > 0;
        int[] arr_v = new int[v2 * 2];
        int[] h = new int[v2];
        int[] w = fillRight ? new int[v2] : null;
        if(fillRight) {
            v3 = this.getPaddingLeft();
            v4 = this.getPaddingRight();
        }
        else {
            v3 = this.getPaddingRight();
            v4 = this.getPaddingLeft();
        }
        int freeWidth = width - v3 - v4;
        int offsetX = v4;
        int offsetY = this.getPaddingTop();
        int lineHeight = 0;
        int lineStart = 0;
        int lineXLeft = 0;
        int lineXRight = 0;
        int line = 0;
        for(int i = 0; i < v2; ++i) {
            try {
                child = this.getChildAt(i);
            }
            catch(ArrayIndexOutOfBoundsException unused_ex) {
                child = null;
            }
            if(child != null) {
                if(child.getVisibility() != 8) {
                    ViewGroup.LayoutParams viewGroup$LayoutParams0 = child.getLayoutParams();
                    if(viewGroup$LayoutParams0.width == -1) {
                        v14 = View.MeasureSpec.makeMeasureSpec(freeWidth, 0x40000000);
                    }
                    else {
                        v14 = viewGroup$LayoutParams0.width <= 0 ? View.MeasureSpec.makeMeasureSpec(freeWidth, 0x80000000) : View.MeasureSpec.makeMeasureSpec(viewGroup$LayoutParams0.width, 0x40000000);
                    }
                    child.measure(v14, (viewGroup$LayoutParams0.height <= 0 ? View.MeasureSpec.makeMeasureSpec(height, 0) : View.MeasureSpec.makeMeasureSpec(viewGroup$LayoutParams0.height, 0x40000000)));
                    int v15 = child.getMeasuredWidth();
                    int v16 = child.getMeasuredHeight();
                    if(offsetX > v4 && offsetX + v15 > width - v3) {
                        for(int j = lineStart; j < i; ++j) {
                            arr_v[v2 + j] += (lineHeight - h[j]) / 2;
                            if(fillCenter) {
                                arr_v[j] += (freeWidth - offsetX) / 2 + v4 - lineXLeft;
                            }
                        }
                        ++line;
                        lineStart = i;
                        offsetX = v4;
                        offsetY += lineHeight;
                        lineXLeft = 0;
                        lineXRight = 0;
                        lineHeight = v16;
                    }
                    else if(lineHeight < v16) {
                        lineHeight = v16;
                    }
                    if(!fillCenter) {
                        arr_v[i] = offsetX;
                    }
                    else if((i - lineStart) % 2 == 0) {
                        lineXLeft -= v15;
                        arr_v[i] = lineXLeft;
                    }
                    else {
                        arr_v[i] = lineXRight;
                        lineXRight += v15;
                    }
                    arr_v[v2 + i] = offsetY;
                    h[i] = v16;
                    if(fillRight) {
                        w[i] = v15;
                    }
                    offsetX += v15;
                    if(child instanceof ToolbarButton) {
                        ((ToolbarButton)child).setIcon(line == 0);
                    }
                }
                else if(child instanceof ToolbarButton) {
                    ((ToolbarButton)child).setIcon(false);
                }
            }
        }
        for(int j = lineStart; j < v2; ++j) {
            arr_v[v2 + j] += (lineHeight - h[j]) / 2;
            if(fillCenter) {
                arr_v[j] += (freeWidth - offsetX) / 2 + v4 - lineXLeft;
            }
        }
        if(fillRight) {
            for(int i = 0; i < v2; ++i) {
                arr_v[i] = width - arr_v[i] - w[i];
            }
        }
        this.x = arr_v;
        return offsetY + lineHeight + this.getPaddingBottom();
    }
}

