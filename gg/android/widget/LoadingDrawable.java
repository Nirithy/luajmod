package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint.Style;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class LoadingDrawable extends Drawable {
    static final int STATE_FAIL = -1;
    static final int STATE_LOADING = 0;
    static final int STATE_SUCCESS = 1;
    private final DisplayMetrics dm;
    private int m;
    private int mState;
    private int n;
    private Paint p;
    private int sm;
    private int sn;
    private int x;
    private int y;

    public LoadingDrawable(Context context0) {
        this.n = 0;
        this.m = 0;
        this.x = 0;
        this.y = 0;
        this.sn = 3;
        this.sm = 1;
        this.dm = context0.getResources().getDisplayMetrics();
        this.p = new Paint();
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setAntiAlias(true);
        this.p.setStrokeWidth(((float)this.dp(8.0f)));
        this.p.setColor(-2004318072);
    }

    private int dp(float f) {
        return (int)TypedValue.applyDimension(1, f, this.dm);
    }

    @Override  // android.graphics.drawable.Drawable
    public void draw(Canvas canvas0) {
        Rect rect0 = new Rect(this.getBounds());
        int v = (int)(((float)Math.min(rect0.right, rect0.bottom)));
        int v1 = rect0.right - v;
        int v2 = rect0.bottom - v;
        rect0.right = v;
        rect0.bottom = v;
        canvas0.save();
        canvas0.translate(((float)(v1 / 2)), ((float)(v2 / 2)));
        RectF rectF0 = new RectF(((float)v) * 0.15f, ((float)v) * 0.15f, ((float)v) * 0.85f, ((float)v) * 0.85f);
        if(this.n >= 360 && this.mState == 0) {
            this.sm = 8;
            this.sn = -6;
        }
        else if(this.n <= 6) {
            this.sn = 6;
            this.sm = 2;
        }
        if(this.n < 360 || this.mState == 0) {
            this.n += (this.mState == 0 ? this.sn : this.sn * 2);
            this.m %= 360;
        }
        canvas0.drawArc(rectF0, ((float)this.m), ((float)this.n), false, this.p);
        if(this.n >= 360) {
            this.sn = -6;
            this.sm = 8;
            if(this.mState == 1) {
                Path path0 = new Path();
                path0.moveTo(((float)rect0.right) * 0.3f, ((float)rect0.bottom) * 0.5f);
                path0.lineTo(((float)rect0.right) * 0.45f, ((float)rect0.bottom) * 0.7f);
                path0.lineTo(((float)rect0.right) * 0.75f, ((float)rect0.bottom) * 0.4f);
                canvas0.drawPath(path0, this.p);
            }
            else if(this.mState == -1) {
                canvas0.drawLine(((float)(rect0.right / 2)), 0.25f * ((float)rect0.bottom), ((float)(rect0.right / 2)), 0.65f * ((float)rect0.bottom), this.p);
                canvas0.drawLine(((float)(rect0.right / 2)), ((float)rect0.bottom) * 0.7f, ((float)(rect0.right / 2)), ((float)rect0.bottom) * 0.75f, this.p);
            }
        }
        canvas0.restore();
        this.invalidateSelf();
    }

    void fail() {
        this.mState = -1;
    }

    @Override  // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    void loading() {
        this.reset();
    }

    void reset() {
        this.mState = 0;
        this.sn = 3;
        this.sm = 1;
        this.n = 0;
        this.m = 0;
        this.x = 0;
        this.y = 0;
        this.invalidateSelf();
    }

    @Override  // android.graphics.drawable.Drawable
    public void setAlpha(int v) {
        this.p.setAlpha(v);
    }

    public void setColor(int v) {
        this.p.setColor(v);
    }

    @Override  // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter0) {
        this.p.setColorFilter(colorFilter0);
    }

    public void setState(int v) {
        this.mState = v;
    }

    public void setStrokeWidth(float f) {
        this.p.setStrokeWidth(f);
    }

    void succe() {
        this.mState = 1;
    }
}

