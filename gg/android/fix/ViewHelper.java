package android.fix;

import android.content.Context;
import android.view.View;

public final class ViewHelper extends View {
    public static final int[] FOCUSED_STATE_SET_;
    public static final int[] PRESSED_STATE_SET_;
    public static final int[] SELECTED_STATE_SET_;

    static {
        ViewHelper.FOCUSED_STATE_SET_ = ViewHelper.FOCUSED_STATE_SET;
        ViewHelper.SELECTED_STATE_SET_ = ViewHelper.SELECTED_STATE_SET;
        ViewHelper.PRESSED_STATE_SET_ = ViewHelper.PRESSED_STATE_SET;
    }

    private ViewHelper(Context context) {
        super(context);
    }
}

