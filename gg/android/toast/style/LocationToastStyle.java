package android.toast.style;

import android.content.Context;
import android.toast.config.IToastStyle;
import android.view.View;

public class LocationToastStyle implements IToastStyle {
    private final int mGravity;
    private final float mHorizontalMargin;
    private final IToastStyle mStyle;
    private final float mVerticalMargin;
    private final int mXOffset;
    private final int mYOffset;

    public LocationToastStyle(IToastStyle iToastStyle0, int v) {
        this(iToastStyle0, v, 0, 0, 0.0f, 0.0f);
    }

    public LocationToastStyle(IToastStyle iToastStyle0, int v, int v1, int v2, float f, float f1) {
        this.mStyle = iToastStyle0;
        this.mGravity = v;
        this.mXOffset = v1;
        this.mYOffset = v2;
        this.mHorizontalMargin = f;
        this.mVerticalMargin = f1;
    }

    @Override  // android.toast.config.IToastStyle
    public View createView(Context context0) {
        return this.mStyle.createView(context0);
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

