package android.toast.style;

import android.content.Context;
import android.toast.config.IToastStyle;
import android.view.LayoutInflater;
import android.view.View;

public class CustomToastStyle implements IToastStyle {
    private final int mGravity;
    private final float mHorizontalMargin;
    private final int mLayoutId;
    private final float mVerticalMargin;
    private final int mXOffset;
    private final int mYOffset;

    public CustomToastStyle(int v) {
        this(v, 80);
    }

    public CustomToastStyle(int v, int v1) {
        this(v, v1, 0, 100);
    }

    public CustomToastStyle(int v, int v1, int v2, int v3) {
        this(v, v1, v2, v3, 0.0f, 0.0f);
    }

    public CustomToastStyle(int v, int v1, int v2, int v3, float f, float f1) {
        this.mLayoutId = v;
        this.mGravity = v1;
        this.mXOffset = v2;
        this.mYOffset = v3;
        this.mHorizontalMargin = f;
        this.mVerticalMargin = f1;
    }

    @Override  // android.toast.config.IToastStyle
    public View createView(Context context0) {
        return LayoutInflater.from(context0).inflate(this.mLayoutId, null);
    }

    @Override  // android.toast.config.IToastStyle
    public int getGravity() {
        return this.mGravity;
    }

    @Override  // android.toast.config.IToastStyle
    public float getHorizontalMargin() {
        return this.mHorizontalMargin;
    }

    @Override  // android.toast.config.IToastStyle
    public float getVerticalMargin() {
        return this.mVerticalMargin;
    }

    @Override  // android.toast.config.IToastStyle
    public int getXOffset() {
        return this.mXOffset;
    }

    @Override  // android.toast.config.IToastStyle
    public int getYOffset() {
        return this.mYOffset;
    }
}

