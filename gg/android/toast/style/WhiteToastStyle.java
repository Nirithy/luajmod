package android.toast.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

public class WhiteToastStyle extends BlackToastStyle {
    @Override  // android.toast.style.BlackToastStyle
    protected Drawable getBackgroundDrawable(Context context0) {
        Drawable drawable0 = new GradientDrawable();
        ((GradientDrawable)drawable0).setColor(0xFFEAEAEA);
        ((GradientDrawable)drawable0).setCornerRadius(TypedValue.applyDimension(1, 10.0f, context0.getResources().getDisplayMetrics()));
        return drawable0;
    }

    @Override  // android.toast.style.BlackToastStyle
    protected int getTextColor(Context context0) {
        return 0xBB000000;
    }
}

