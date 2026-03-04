package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Field;

public class EditText extends android.fix.EditText implements EditTextExt {
    static class CheckContrast {
        int bg;
        float brigh;
        float diff;
        int fg;
        float ratio;

    }

    private static volatile CheckContrast[] checkedContrast;
    private int dataType;

    static {
        EditText.checkedContrast = new CheckContrast[0];
    }

    public EditText(Context context) {
        super(context);
        this.dataType = -1;
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.dataType = -1;
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dataType = -1;
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
    }

    @TargetApi(21)
    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.dataType = -1;
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
    }

    static void checkContrast(android.widget.EditText edit) {
        Bitmap bitmap0;
        int[] state = View.ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
        int bg = 0xFF000000;
        Drawable drawable0 = edit.getBackground();
        if(drawable0 != null) {
            int size = (int)Tools.dp2px(36.0f);
            Rect rect0 = drawable0.copyBounds();
            int[] arr_v1 = drawable0.getState();
            drawable0.setBounds(0, 0, size, size);
            drawable0.setState(state);
            try {
                bitmap0 = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            catch(OutOfMemoryError e) {
                Log.w("Failed checkContrast", e);
                return;
            }
            drawable0.draw(new Canvas(bitmap0));
            bg = bitmap0.getPixel(size / 2, size / 2);
            drawable0.setBounds(rect0);
            drawable0.setState(arr_v1);
        }
        int v2 = edit.getTextColors().getColorForState(state, edit.getCurrentTextColor());
        int v3 = EditText.getHighlightColor(edit);
        if(EditText.checkContrast(EditText.removeAlpha(v2, bg), bg, EditText.removeAlpha(v3, bg))) {
            edit.setTextColor(-1);
            edit.setHighlightColor(0xFFA07070);
            edit.setBackgroundResource(0x7F020000);  // drawable:abc_textfield_default_mtrl_alpha
        }
    }

    private static boolean checkContrast(int fg, int bg, int hg) {
        float[] arr_f = {2.0f, 50.0f, 150.0f};
        return EditText.checkContrast(fg, bg, new float[]{4.5f, 125.0f, 500.0f}) || EditText.checkContrast(fg, hg, arr_f) || EditText.checkContrast(hg, bg, arr_f);
    }

    private static boolean checkContrast(int fg, int bg, float[] limits) {
        CheckContrast[] checkedContrast = EditText.checkedContrast;
        CheckContrast found = null;
        for(int v2 = 0; v2 < checkedContrast.length; ++v2) {
            CheckContrast check = checkedContrast[v2];
            if(check.fg == fg && check.bg == bg) {
                found = check;
                break;
            }
        }
        if(found == null) {
            found = new CheckContrast();
            found.fg = fg;
            found.bg = bg;
            EditText.checkContrast_(found);
            CheckContrast[] newArray = new CheckContrast[checkedContrast.length + 1];
            System.arraycopy(checkedContrast, 0, newArray, 0, checkedContrast.length);
            newArray[checkedContrast.length] = found;
            EditText.checkedContrast = newArray;
        }
        return found.ratio < limits[0] || found.brigh < limits[1] || found.diff < limits[2];
    }

    private static void checkContrast_(CheckContrast check) {
        int fg = check.fg;
        int bg = check.bg;
        Log.d(("checkContrast(fg, bg): " + Integer.toHexString(fg) + ' ' + Integer.toHexString(bg)));
        check.ratio = (float)EditText.getContrastRatio(fg, bg);
        check.brigh = (float)Math.abs(EditText.getColorBrightness(fg) - EditText.getColorBrightness(bg));
        check.diff = (float)EditText.getColorDifference(fg, bg);
        try {
            Log.d(("checkContrast(fg, bg): " + check.ratio + ", " + check.brigh + ", " + check.diff));
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    private static double getColorBrightness(int color) {
        return ((double)(Color.red(color) * 299 + Color.green(color) * 587 + Color.blue(color) * 0x72)) / 1000.0;
    }

    private static double getColorDifference(int colorA, int colorB) {
        int v2 = Color.red(colorA);
        int v3 = Color.green(colorA);
        int v4 = Color.blue(colorA);
        int v5 = Color.red(colorB);
        int v6 = Color.green(colorB);
        int v7 = Color.blue(colorB);
        return (double)(Math.max(v2, v5) - Math.min(v2, v5) + (Math.max(v3, v6) - Math.min(v3, v6)) + (Math.max(v4, v7) - Math.min(v4, v7)));
    }

    private static double getContrastRatio(int colorA, int colorB) {
        double f = EditText.getRelativeluminance(colorA);
        double f1 = EditText.getRelativeluminance(colorB);
        return f + 0.05 > f1 + 0.05 ? (f + 0.05) / (f1 + 0.05) : (f1 + 0.05) / (f + 0.05);
    }

    @Override  // android.ext.EditTextExt
    public int getDataType() {
        return this.dataType;
    }

    private static int getHighlightColor(android.widget.EditText edit) {
        try {
            if(Build.VERSION.SDK_INT >= 16) {
                return edit.getHighlightColor();
            }
        }
        catch(Throwable e) {
            Log.e("Failed getHighlightColor", e);
        }
        try {
            Field field0 = TextView.class.getDeclaredField("mHighlightColor");
            field0.setAccessible(true);
            return field0.getInt(edit);
        }
        catch(Throwable e2) {
            Log.e("Failed getHighlightColor2", e2);
            return 0;
        }
    }

    private static double getRelativeluminance(int color) {
        double g;
        double r = ((double)Color.red(color)) / 255.0;
        double g = ((double)Color.green(color)) / 255.0;
        double b = ((double)Color.blue(color)) / 255.0;
        double r = r <= 0.03928 ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        if(g <= 0.03928) {
            g = g / 12.92;
            return b <= 0.03928 ? 0.2126 * r + 0.7152 * g + 0.0722 * (b / 12.92) : 0.2126 * r + 0.7152 * g + 0.0722 * Math.pow((b + 0.055) / 1.055, 2.4);
        }
        g = Math.pow((g + 0.055) / 1.055, 2.4);
        return b <= 0.03928 ? 0.2126 * r + 0.7152 * g + 0.0722 * (b / 12.92) : 0.2126 * r + 0.7152 * g + 0.0722 * Math.pow((b + 0.055) / 1.055, 2.4);
    }

    private static int removeAlpha(int fg, int bg) {
        int v2 = Color.alpha(fg);
        int ret = 0xFF000000;
        for(int i = 0; i < 3; ++i) {
            ret |= ((fg >> i * 8 & 0xFF) * v2 + (bg >> i * 8 & 0xFF) * (0xFF - v2)) / 0xFF << i * 8;
        }
        return ret;
    }

    @Override  // android.ext.EditTextExt
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}

