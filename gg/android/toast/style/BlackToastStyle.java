package android.toast.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.toast.config.IToastStyle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class BlackToastStyle implements IToastStyle {
    @Override  // android.toast.config.IToastStyle
    public View createView(Context context0) {
        View view0 = new TextView(context0);
        ((TextView)view0).setId(0x102000B);
        ((TextView)view0).setGravity(17);
        ((TextView)view0).setTextColor(this.getTextColor(context0));
        ((TextView)view0).setTextSize(0, this.getTextSize(context0));
        int v = this.getHorizontalPadding(context0);
        int v1 = this.getVerticalPadding(context0);
        ((TextView)view0).setPaddingRelative(v, v1, v, v1);
        ((TextView)view0).setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        ((TextView)view0).setBackground(this.getBackgroundDrawable(context0));
        ((TextView)view0).setZ(this.getTranslationZ(context0));
        return view0;
    }

    protected Drawable getBackgroundDrawable(Context context0) {
        Drawable drawable0 = new GradientDrawable();
        ((GradientDrawable)drawable0).setColor(0xB3000000);
        ((GradientDrawable)drawable0).setCornerRadius(TypedValue.applyDimension(1, 10.0f, context0.getResources().getDisplayMetrics()));
        return drawable0;
    }

    protected int getHorizontalPadding(Context context0) {
        return (int)TypedValue.applyDimension(1, 24.0f, context0.getResources().getDisplayMetrics());
    }

    protected int getTextColor(Context context0) {
        return 0xEEFFFFFF;
    }

    protected int getTextGravity(Context context0) [...] // Inlined contents

    protected float getTextSize(Context context0) {
        return TypedValue.applyDimension(2, 14.0f, context0.getResources().getDisplayMetrics());
    }

    protected float getTranslationZ(Context context0) {
        return TypedValue.applyDimension(1, 3.0f, context0.getResources().getDisplayMetrics());
    }

    protected int getVerticalPadding(Context context0) {
        return (int)TypedValue.applyDimension(1, 16.0f, context0.getResources().getDisplayMetrics());
    }
}

