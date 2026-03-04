package android.widget;

import android.content.Context;

interface CardViewImpl {
    float getElevation(CardViewDelegate arg1);

    float getMaxElevation(CardViewDelegate arg1);

    float getMinHeight(CardViewDelegate arg1);

    float getMinWidth(CardViewDelegate arg1);

    float getRadius(CardViewDelegate arg1);

    void initStatic();

    void initialize(CardViewDelegate arg1, Context arg2, int arg3, float arg4, float arg5, float arg6);

    void onCompatPaddingChanged(CardViewDelegate arg1);

    void onPreventCornerOverlapChanged(CardViewDelegate arg1);

    void setBackgroundColor(CardViewDelegate arg1, int arg2);

    void setElevation(CardViewDelegate arg1, float arg2);

    void setMaxElevation(CardViewDelegate arg1, float arg2);

    void setRadius(CardViewDelegate arg1, float arg2);

    void updatePadding(CardViewDelegate arg1);
}

