package android.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.util.LuaBitmap;
import com.util.LuaGcable;
import java.io.IOException;

public class NineBitmapDrawable extends Drawable implements LuaGcable {
    private Bitmap mBitmap;
    private boolean mGc;
    private Paint mPaint;
    private Rect mRect1;
    private Rect mRect2;
    private Rect mRect3;
    private Rect mRect4;
    private Rect mRect5;
    private Rect mRect6;
    private Rect mRect7;
    private Rect mRect8;
    private Rect mRect9;
    private int mX1;
    private int mX2;
    private int mY1;
    private int mY2;

    public NineBitmapDrawable(Bitmap bitmap0) {
        int v5;
        int v = 0;
        super();
        this.mPaint = new Paint();
        int v1 = bitmap0.getWidth();
        int v2 = bitmap0.getHeight();
        int v3;
        for(v3 = 0; true; ++v3) {
            if(v3 >= v1) {
                v3 = 0;
                break;
            }
            if(bitmap0.getPixel(v3, 0) == 0xFF000000) {
                break;
            }
        }
        if(v3 == 0 || v3 == v1 - 1) {
            throw new IllegalArgumentException("not found x1");
        }
        for(int v4 = v3; true; ++v4) {
            if(v4 >= v1) {
                v5 = 0;
            }
            else if(bitmap0.getPixel(v4, 0) == 0xFF000000) {
                continue;
            }
            else {
                v5 = v1 - v4;
            }
            if(v5 == 0 || v5 == 1) {
                throw new IllegalArgumentException("not found x2");
            }
            int v6;
            for(v6 = 0; true; ++v6) {
                if(v6 >= v2) {
                    v6 = 0;
                    break;
                }
                if(bitmap0.getPixel(0, v6) == 0xFF000000) {
                    break;
                }
            }
            if(v6 == 0 || v6 == v2 - 1) {
                throw new IllegalArgumentException("not found y1");
            }
            for(int v7 = v6; v7 < v2; ++v7) {
                if(bitmap0.getPixel(0, v7) != 0xFF000000) {
                    v = v2 - v7;
                    break;
                }
            }
            if(v == 0 || v == 1) {
                throw new IllegalArgumentException("not found y2");
            }
            this.init(bitmap0, v3, v6, v5, v);
            return;
        }
    }

    public NineBitmapDrawable(Bitmap bitmap0, int v, int v1, int v2, int v3) {
        this.mPaint = new Paint();
        this.init(bitmap0, v, v1, v2, v3);
    }

    public NineBitmapDrawable(String s) throws IOException {
        this(LuaBitmap.getLocalBitmap(s));
    }

    @Override  // android.graphics.drawable.Drawable
    public void draw(Canvas canvas0) {
        Rect rect0 = this.getBounds();
        int v = rect0.right;
        int v1 = rect0.bottom;
        Rect rect1 = new Rect(0, 0, this.mX1, this.mY1);
        Rect rect2 = new Rect(this.mX1, 0, v - this.mX2, this.mY1);
        Rect rect3 = new Rect(v - this.mX2, 0, v, this.mY1);
        Rect rect4 = new Rect(0, this.mY1, this.mX1, v1 - this.mY2);
        Rect rect5 = new Rect(this.mX1, this.mY1, v - this.mX2, v1 - this.mY2);
        Rect rect6 = new Rect(v - this.mX2, this.mY1, v, v1 - this.mY2);
        Rect rect7 = new Rect(0, v1 - this.mY2, this.mX1, v1);
        Rect rect8 = new Rect(this.mX1, v1 - this.mY2, v - this.mX2, v1);
        Rect rect9 = new Rect(v - this.mX2, v1 - this.mY2, v, v1);
        canvas0.drawBitmap(this.mBitmap, this.mRect1, rect1, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect2, rect2, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect3, rect3, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect4, rect4, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect5, rect5, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect6, rect6, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect7, rect7, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect8, rect8, this.mPaint);
        canvas0.drawBitmap(this.mBitmap, this.mRect9, rect9, this.mPaint);
    }

    @Override  // com.util.LuaGcable
    public void gc() {
        try {
            this.mBitmap.recycle();
            this.mGc = true;
        }
        catch(Exception exception0) {
            exception0.printStackTrace();
        }
    }

    @Override  // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    private void init(Bitmap bitmap0, int v, int v1, int v2, int v3) {
        this.mBitmap = bitmap0;
        int v4 = bitmap0.getWidth();
        int v5 = bitmap0.getHeight();
        this.mX1 = v;
        this.mY1 = v1;
        this.mX2 = v2;
        this.mY2 = v3;
        int v6 = v4 - v2;
        int v7 = v5 - v3;
        this.mRect1 = new Rect(1, 1, v, v1);
        this.mRect2 = new Rect(v, 1, v6, v1);
        this.mRect3 = new Rect(v6, 1, v4 - 1, v1);
        this.mRect4 = new Rect(1, v1, v, v7);
        this.mRect5 = new Rect(v, v1, v6, v7);
        this.mRect6 = new Rect(v6, v1, v4 - 1, v7);
        this.mRect7 = new Rect(1, v7, v, v5 - 1);
        this.mRect8 = new Rect(v, v7, v6, v5 - 1);
        this.mRect9 = new Rect(v6, v7, v4 - 1, v5 - 1);
    }

    @Override  // com.util.LuaGcable
    public boolean isGc() {
        return this.mGc;
    }

    @Override  // android.graphics.drawable.Drawable
    public void setAlpha(int v) {
        this.mPaint.setAlpha(v);
    }

    @Override  // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter0) {
        this.mPaint.setColorFilter(colorFilter0);
    }
}

