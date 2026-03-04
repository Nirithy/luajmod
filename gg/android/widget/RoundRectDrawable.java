package android.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

class RoundRectDrawable extends Drawable {
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private boolean mInsetForPadding;
    private boolean mInsetForRadius;
    private float mPadding;
    private final Paint mPaint;
    private float mRadius;

    public RoundRectDrawable(int v, float f) {
        this.mInsetForPadding = false;
        this.mInsetForRadius = true;
        this.mRadius = f;
        Paint paint0 = new Paint(5);
        this.mPaint = paint0;
        paint0.setColor(v);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
    }

    @Override  // android.graphics.drawable.Drawable
    public void draw(Canvas canvas0) {
        canvas0.drawRoundRect(this.mBoundsF, this.mRadius, this.mRadius, this.mPaint);
    }

    @Override  // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override  // android.graphics.drawable.Drawable
    public void getOutline(Outline outline0) {
        if(Build.VERSION.SDK_INT >= 21) {
            outline0.setRoundRect(this.mBoundsI, this.mRadius);
        }
    }

    float getPadding() {
        return this.mPadding;
    }

    public float getRadius() {
        return this.mRadius;
    }

    @Override  // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect0) {
        super.onBoundsChange(rect0);
        this.updateBounds(rect0);
    }

    @Override  // android.graphics.drawable.Drawable
    public void setAlpha(int v) {
    }

    public void setColor(int v) {
        this.mPaint.setColor(v);
        this.invalidateSelf();
    }

    @Override  // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter0) {
    }

    void setPadding(float f, boolean z, boolean z1) {
        if(f == this.mPadding && this.mInsetForPadding == z && this.mInsetForRadius == z1) {
            return;
        }
        this.mPadding = f;
        this.mInsetForPadding = z;
        this.mInsetForRadius = z1;
        this.updateBounds(null);
        this.invalidateSelf();
    }

    void setRadius(float f) {
        if(f == this.mRadius) {
            return;
        }
        this.mRadius = f;
        this.updateBounds(null);
        this.invalidateSelf();
    }

    private void updateBounds(Rect rect0) {
        if(rect0 == null) {
            rect0 = this.getBounds();
        }
        this.mBoundsF.set(((float)rect0.left), ((float)rect0.top), ((float)rect0.right), ((float)rect0.bottom));
        this.mBoundsI.set(rect0);
        if(this.mInsetForPadding) {
            float f = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            int v = (int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius));
            this.mBoundsI.inset(v, ((int)Math.ceil(f)));
            this.mBoundsF.set(this.mBoundsI);
        }
    }
}

