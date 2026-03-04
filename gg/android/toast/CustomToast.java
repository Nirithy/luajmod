package android.toast;

import android.toast.config.IToast;
import android.view.View;
import android.widget.TextView;

public abstract class CustomToast implements IToast {
    private int mAnimations;
    private int mDuration;
    private int mGravity;
    private float mHorizontalMargin;
    private int mLongDuration;
    private TextView mMessageView;
    private int mShortDuration;
    private float mVerticalMargin;
    private View mView;
    private int mXOffset;
    private int mYOffset;

    public CustomToast() {
        this.mAnimations = 0x1030004;
        this.mShortDuration = 2000;
        this.mLongDuration = 3500;
    }

    public int getAnimationsId() {
        return this.mAnimations;
    }

    @Override  // android.toast.config.IToast
    public int getDuration() {
        return this.mDuration;
    }

    @Override  // android.toast.config.IToast
    public int getGravity() {
        return this.mGravity;
    }

    @Override  // android.toast.config.IToast
    public float getHorizontalMargin() {
        return this.mHorizontalMargin;
    }

    public int getLongDuration() {
        return this.mLongDuration;
    }

    public int getShortDuration() {
        return this.mShortDuration;
    }

    @Override  // android.toast.config.IToast
    public float getVerticalMargin() {
        return this.mVerticalMargin;
    }

    @Override  // android.toast.config.IToast
    public View getView() {
        return this.mView;
    }

    @Override  // android.toast.config.IToast
    public int getXOffset() {
        return this.mXOffset;
    }

    @Override  // android.toast.config.IToast
    public int getYOffset() {
        return this.mYOffset;
    }

    public void setAnimationsId(int v) {
        this.mAnimations = v;
    }

    @Override  // android.toast.config.IToast
    public void setDuration(int v) {
        this.mDuration = v;
    }

    @Override  // android.toast.config.IToast
    public void setGravity(int v, int v1, int v2) {
        this.mGravity = v;
        this.mXOffset = v1;
        this.mYOffset = v2;
    }

    public void setLongDuration(int v) {
        this.mLongDuration = v;
    }

    @Override  // android.toast.config.IToast
    public void setMargin(float f, float f1) {
        this.mHorizontalMargin = f;
        this.mVerticalMargin = f1;
    }

    public void setShortDuration(int v) {
        this.mShortDuration = v;
    }

    @Override  // android.toast.config.IToast
    public void setText(int v) {
        View view0 = this.mView;
        if(view0 == null) {
            return;
        }
        this.setText(view0.getResources().getString(v));
    }

    @Override  // android.toast.config.IToast
    public void setText(CharSequence charSequence0) {
        TextView textView0 = this.mMessageView;
        if(textView0 == null) {
            return;
        }
        textView0.setText(charSequence0);
    }

    @Override  // android.toast.config.IToast
    public void setView(View view0) {
        this.mView = view0;
        if(view0 == null) {
            this.mMessageView = null;
            return;
        }
        this.mMessageView = this.findMessageView(view0);
    }
}

