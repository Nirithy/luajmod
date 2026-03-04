package android.widget;

import android.graphics.drawable.Drawable;

interface CardViewDelegate {
    Drawable getBackground();

    boolean getPreventCornerOverlap();

    float getRadius();

    boolean getUseCompatPadding();

    void setBackgroundDrawable(Drawable arg1);

    void setShadowPadding(int arg1, int arg2, int arg3, int arg4);
}

