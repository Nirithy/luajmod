package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.view.View;

public class CardView extends FrameLayout implements CardViewDelegate {
    static class CardViewApi21 implements CardViewImpl {
        @Override  // android.widget.CardViewImpl
        public float getElevation(CardViewDelegate cardViewDelegate0) {
            return ((View)cardViewDelegate0).getElevation();
        }

        @Override  // android.widget.CardViewImpl
        public float getMaxElevation(CardViewDelegate cardViewDelegate0) {
            return ((RoundRectDrawable)cardViewDelegate0.getBackground()).getPadding();
        }

        @Override  // android.widget.CardViewImpl
        public float getMinHeight(CardViewDelegate cardViewDelegate0) {
            return this.getRadius(cardViewDelegate0) * 2.0f;
        }

        @Override  // android.widget.CardViewImpl
        public float getMinWidth(CardViewDelegate cardViewDelegate0) {
            return this.getRadius(cardViewDelegate0) * 2.0f;
        }

        @Override  // android.widget.CardViewImpl
        public float getRadius(CardViewDelegate cardViewDelegate0) {
            return ((RoundRectDrawable)cardViewDelegate0.getBackground()).getRadius();
        }

        @Override  // android.widget.CardViewImpl
        public void initStatic() {
        }

        @Override  // android.widget.CardViewImpl
        public void initialize(CardViewDelegate cardViewDelegate0, Context context0, int v, float f, float f1, float f2) {
            cardViewDelegate0.setBackgroundDrawable(new RoundRectDrawable(v, f));
            ((View)cardViewDelegate0).setClipToOutline(true);
            ((View)cardViewDelegate0).setElevation(f1);
            this.setMaxElevation(cardViewDelegate0, f2);
        }

        @Override  // android.widget.CardViewImpl
        public void onCompatPaddingChanged(CardViewDelegate cardViewDelegate0) {
            this.setMaxElevation(cardViewDelegate0, this.getMaxElevation(cardViewDelegate0));
        }

        @Override  // android.widget.CardViewImpl
        public void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate0) {
            this.setMaxElevation(cardViewDelegate0, this.getMaxElevation(cardViewDelegate0));
        }

        @Override  // android.widget.CardViewImpl
        public void setBackgroundColor(CardViewDelegate cardViewDelegate0, int v) {
            ((RoundRectDrawable)cardViewDelegate0.getBackground()).setColor(v);
        }

        @Override  // android.widget.CardViewImpl
        public void setElevation(CardViewDelegate cardViewDelegate0, float f) {
            ((View)cardViewDelegate0).setElevation(f);
        }

        @Override  // android.widget.CardViewImpl
        public void setMaxElevation(CardViewDelegate cardViewDelegate0, float f) {
            ((RoundRectDrawable)cardViewDelegate0.getBackground()).setPadding(f, cardViewDelegate0.getUseCompatPadding(), cardViewDelegate0.getPreventCornerOverlap());
            this.updatePadding(cardViewDelegate0);
        }

        @Override  // android.widget.CardViewImpl
        public void setRadius(CardViewDelegate cardViewDelegate0, float f) {
            ((RoundRectDrawable)cardViewDelegate0.getBackground()).setRadius(f);
        }

        @Override  // android.widget.CardViewImpl
        public void updatePadding(CardViewDelegate cardViewDelegate0) {
            if(!cardViewDelegate0.getUseCompatPadding()) {
                cardViewDelegate0.setShadowPadding(0, 0, 0, 0);
                return;
            }
            float f = this.getMaxElevation(cardViewDelegate0);
            float f1 = this.getRadius(cardViewDelegate0);
            int v = (int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(f, f1, cardViewDelegate0.getPreventCornerOverlap()));
            int v1 = (int)Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(f, f1, cardViewDelegate0.getPreventCornerOverlap()));
            cardViewDelegate0.setShadowPadding(v, v1, v, v1);
        }
    }

    static class CardViewEclairMr1 implements CardViewImpl {
        final RectF sCornerRect;

        CardViewEclairMr1() {
            this.sCornerRect = new RectF();
        }

        RoundRectDrawableWithShadow createBackground(Context context0, int v, float f, float f1, float f2) {
            return new RoundRectDrawableWithShadow(context0.getResources(), v, f, f1, f2);
        }

        @Override  // android.widget.CardViewImpl
        public float getElevation(CardViewDelegate cardViewDelegate0) {
            return this.getShadowBackground(cardViewDelegate0).getShadowSize();
        }

        @Override  // android.widget.CardViewImpl
        public float getMaxElevation(CardViewDelegate cardViewDelegate0) {
            return this.getShadowBackground(cardViewDelegate0).getMaxShadowSize();
        }

        @Override  // android.widget.CardViewImpl
        public float getMinHeight(CardViewDelegate cardViewDelegate0) {
            return this.getShadowBackground(cardViewDelegate0).getMinHeight();
        }

        @Override  // android.widget.CardViewImpl
        public float getMinWidth(CardViewDelegate cardViewDelegate0) {
            return this.getShadowBackground(cardViewDelegate0).getMinWidth();
        }

        @Override  // android.widget.CardViewImpl
        public float getRadius(CardViewDelegate cardViewDelegate0) {
            return this.getShadowBackground(cardViewDelegate0).getCornerRadius();
        }

        private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate cardViewDelegate0) {
            return (RoundRectDrawableWithShadow)cardViewDelegate0.getBackground();
        }

        @Override  // android.widget.CardViewImpl
        public void initStatic() {
            RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectHelper() {
                @Override  // android.widget.RoundRectDrawableWithShadow$RoundRectHelper
                public void drawRoundRect(Canvas canvas0, RectF rectF0, float f, Paint paint0) {
                    float f3;
                    float f1 = rectF0.width() - 2.0f * f - 1.0f;
                    float f2 = rectF0.height();
                    if(f >= 1.0f) {
                        f3 = f + 0.5f;
                        CardViewEclairMr1.this.sCornerRect.set(-f3, -f3, f3, f3);
                        int v = canvas0.save();
                        canvas0.translate(rectF0.left + f3, rectF0.top + f3);
                        canvas0.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint0);
                        canvas0.translate(f1, 0.0f);
                        canvas0.rotate(90.0f);
                        canvas0.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint0);
                        canvas0.translate(f2 - 2.0f * f - 1.0f, 0.0f);
                        canvas0.rotate(90.0f);
                        canvas0.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint0);
                        canvas0.translate(f1, 0.0f);
                        canvas0.rotate(90.0f);
                        canvas0.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint0);
                        canvas0.restoreToCount(v);
                        canvas0.drawRect(rectF0.left + f3 - 1.0f, rectF0.top, rectF0.right - f3 + 1.0f, rectF0.top + f3, paint0);
                        canvas0.drawRect(rectF0.left + f3 - 1.0f, rectF0.bottom - f3 + 1.0f, rectF0.right - f3 + 1.0f, rectF0.bottom, paint0);
                    }
                    else {
                        f3 = f;
                    }
                    canvas0.drawRect(rectF0.left, Math.max(0.0f, f3 - 1.0f) + rectF0.top, rectF0.right, rectF0.bottom - f3 + 1.0f, paint0);
                }
            };
        }

        @Override  // android.widget.CardViewImpl
        public void initialize(CardViewDelegate cardViewDelegate0, Context context0, int v, float f, float f1, float f2) {
            RoundRectDrawableWithShadow roundRectDrawableWithShadow0 = this.createBackground(context0, v, f, f1, f2);
            roundRectDrawableWithShadow0.setAddPaddingForCorners(cardViewDelegate0.getPreventCornerOverlap());
            cardViewDelegate0.setBackgroundDrawable(roundRectDrawableWithShadow0);
            this.updatePadding(cardViewDelegate0);
        }

        @Override  // android.widget.CardViewImpl
        public void onCompatPaddingChanged(CardViewDelegate cardViewDelegate0) {
        }

        @Override  // android.widget.CardViewImpl
        public void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate0) {
            this.getShadowBackground(cardViewDelegate0).setAddPaddingForCorners(cardViewDelegate0.getPreventCornerOverlap());
            this.updatePadding(cardViewDelegate0);
        }

        @Override  // android.widget.CardViewImpl
        public void setBackgroundColor(CardViewDelegate cardViewDelegate0, int v) {
            this.getShadowBackground(cardViewDelegate0).setColor(v);
        }

        @Override  // android.widget.CardViewImpl
        public void setElevation(CardViewDelegate cardViewDelegate0, float f) {
            this.getShadowBackground(cardViewDelegate0).setShadowSize(f);
        }

        @Override  // android.widget.CardViewImpl
        public void setMaxElevation(CardViewDelegate cardViewDelegate0, float f) {
            this.getShadowBackground(cardViewDelegate0).setMaxShadowSize(f);
            this.updatePadding(cardViewDelegate0);
        }

        @Override  // android.widget.CardViewImpl
        public void setRadius(CardViewDelegate cardViewDelegate0, float f) {
            this.getShadowBackground(cardViewDelegate0).setCornerRadius(f);
            this.updatePadding(cardViewDelegate0);
        }

        @Override  // android.widget.CardViewImpl
        public void updatePadding(CardViewDelegate cardViewDelegate0) {
            Rect rect0 = new Rect();
            this.getShadowBackground(cardViewDelegate0).getMaxShadowAndCornerPadding(rect0);
            ((View)cardViewDelegate0).setMinimumHeight(((int)Math.ceil(this.getMinHeight(cardViewDelegate0))));
            ((View)cardViewDelegate0).setMinimumWidth(((int)Math.ceil(this.getMinWidth(cardViewDelegate0))));
            cardViewDelegate0.setShadowPadding(rect0.left, rect0.top, rect0.right, rect0.bottom);
        }
    }

    static class CardViewJellybeanMr1 extends CardViewEclairMr1 {
        @Override  // android.widget.CardView$CardViewEclairMr1
        public void initStatic() {
            RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectHelper() {
                @Override  // android.widget.RoundRectDrawableWithShadow$RoundRectHelper
                public void drawRoundRect(Canvas canvas0, RectF rectF0, float f, Paint paint0) {
                    canvas0.drawRoundRect(rectF0, f, f, paint0);
                }
            };
        }
    }

    private static final CardViewImpl IMPL;
    private DisplayMetrics dm;
    private boolean mCompatPadding;
    private final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    private final Rect mShadowBounds;

    static {
        if(Build.VERSION.SDK_INT >= 21) {
            CardView.IMPL = new CardViewApi21();
        }
        else if(Build.VERSION.SDK_INT >= 17) {
            CardView.IMPL = new CardViewJellybeanMr1();
        }
        else {
            CardView.IMPL = new CardViewEclairMr1();
        }
        CardView.IMPL.initStatic();
    }

    public CardView(Context context0) {
        super(context0);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context0, null, 0);
    }

    public CardView(Context context0, AttributeSet attributeSet0) {
        super(context0, attributeSet0);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context0, attributeSet0, 0);
    }

    public CardView(Context context0, AttributeSet attributeSet0, int v) {
        super(context0, attributeSet0, v);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context0, attributeSet0, v);
    }

    private float dp(float f) {
        return TypedValue.applyDimension(1, f, this.dm);
    }

    public float getCardElevation() {
        return CardView.IMPL.getElevation(this);
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public float getMaxCardElevation() {
        return CardView.IMPL.getMaxElevation(this);
    }

    @Override  // android.widget.CardViewDelegate
    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }

    @Override  // android.widget.CardViewDelegate
    public float getRadius() {
        return CardView.IMPL.getRadius(this);
    }

    @Override  // android.widget.CardViewDelegate
    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    private void initialize(Context context0, AttributeSet attributeSet0, int v) {
        this.dm = context0.getResources().getDisplayMetrics();
        TypedArray typedArray0 = context0.getTheme().obtainStyledAttributes(new int[]{0x1010031});
        int v1 = typedArray0.getColor(0, 0xFFFAFAFA);
        typedArray0.recycle();
        float f = this.dp(2.0f);
        float f1 = this.dp(2.0f);
        float f2 = this.dp(2.0f);
        this.mCompatPadding = false;
        this.mPreventCornerOverlap = true;
        this.mContentPadding.left = 0;
        this.mContentPadding.top = 0;
        this.mContentPadding.right = 0;
        this.mContentPadding.bottom = 0;
        CardView.IMPL.initialize(this, context0, v1, f, f1, (f1 > f2 ? f1 : f2));
    }

    @Override  // android.widget.FrameLayout
    protected void onMeasure(int v, int v1) {
        CardViewImpl cardViewImpl0 = CardView.IMPL;
        if(!(cardViewImpl0 instanceof CardViewApi21)) {
            int v2 = View.MeasureSpec.getMode(v);
            if(v2 == 0x80000000 || v2 == 0x40000000) {
                v = View.MeasureSpec.makeMeasureSpec(Math.max(((int)Math.ceil(cardViewImpl0.getMinWidth(this))), View.MeasureSpec.getSize(v)), v2);
            }
            int v3 = View.MeasureSpec.getMode(v1);
            if(v3 == 0x80000000 || v3 == 0x40000000) {
                v1 = View.MeasureSpec.makeMeasureSpec(Math.max(((int)Math.ceil(cardViewImpl0.getMinHeight(this))), View.MeasureSpec.getSize(v1)), v3);
            }
            super.onMeasure(v, v1);
            return;
        }
        super.onMeasure(v, v1);
    }

    @Override  // android.view.View
    public void setBackgroundColor(int v) {
        CardView.IMPL.setBackgroundColor(this, v);
    }

    public void setCardBackgroundColor(int v) {
        CardView.IMPL.setBackgroundColor(this, v);
    }

    public void setCardElevation(float f) {
        CardView.IMPL.setElevation(this, f);
    }

    public void setContentPadding(int v, int v1, int v2, int v3) {
        this.mContentPadding.set(v, v1, v2, v3);
        CardView.IMPL.updatePadding(this);
    }

    public void setMaxCardElevation(float f) {
        CardView.IMPL.setMaxElevation(this, f);
    }

    @Override  // android.view.View
    public void setPadding(int v, int v1, int v2, int v3) {
    }

    @Override  // android.view.View
    public void setPaddingRelative(int v, int v1, int v2, int v3) {
    }

    public void setPreventCornerOverlap(boolean z) {
        if(z == this.mPreventCornerOverlap) {
            return;
        }
        this.mPreventCornerOverlap = z;
        CardView.IMPL.onPreventCornerOverlapChanged(this);
    }

    public void setRadius(float f) {
        CardView.IMPL.setRadius(this, f);
    }

    @Override  // android.widget.CardViewDelegate
    public void setShadowPadding(int v, int v1, int v2, int v3) {
        this.mShadowBounds.set(v, v1, v2, v3);
        super.setPadding(v + this.mContentPadding.left, v1 + this.mContentPadding.top, v2 + this.mContentPadding.right, v3 + this.mContentPadding.bottom);
    }

    public void setUseCompatPadding(boolean z) {
        if(this.mCompatPadding == z) {
            return;
        }
        this.mCompatPadding = z;
        CardView.IMPL.onCompatPaddingChanged(this);
    }
}

