package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.fix.ContextWrapper;
import android.fix.FrameLayout;
import android.fix.LinearLayout;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;

public class KeyboardLayout extends LinearLayout {
    private FrameLayout frame;
    private InternalKeyboard kbdView;
    private ScrollView scroll;
    private int usedOrientation;

    public KeyboardLayout(Context context) {
        super(context);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        if(this.scroll != null) {
            return;
        }
        this.usedOrientation = 0;
        this.scroll = (ScrollView)this.findViewById(0x7F0B0028);  // id:kbd_scroll
        this.frame = (FrameLayout)this.findViewById(0x7F0B0029);  // id:kbd_frame
        this.kbdView = (InternalKeyboard)this.findViewById(0x7F0B002A);  // id:keyboard
    }

    @Override  // android.fix.LinearLayout
    protected void onConfigurationChanged(Configuration newConfig) {
        Log.d(("KeyboardLayout onConfigurationChanged: " + newConfig));
        Configuration configuration1 = ContextWrapper.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(configuration1);
        this.updateOrientation();
        MainService.onConfigurationChanged(configuration1);
    }

    private void setSize(View v, int w, int h) {
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = v.getLayoutParams();
        viewGroup$LayoutParams0.width = w;
        viewGroup$LayoutParams0.height = h;
        v.setLayoutParams(viewGroup$LayoutParams0);
    }

    public void updateOrientation() {
        Configuration configuration0 = Tools.getContext().getResources().getConfiguration();
        int orientation = configuration0.orientation;
        if(orientation == this.usedOrientation) {
            return;
        }
        this.usedOrientation = orientation;
        if(Build.VERSION.SDK_INT >= 13) {
            Log.d(("Screen: " + configuration0.screenWidthDp + "dp x " + configuration0.screenHeightDp + "dp"));
        }
        this.init();
        int[] arr_v = orientation == 1 ? new int[]{1, -1, 0} : new int[]{0, -1, -2};
        this.setOrientation(arr_v[0]);
        ScrollView scrollView0 = this.scroll.getVisibility() == 0 ? this.scroll : this.frame;
        this.setSize(scrollView0, arr_v[1], arr_v[2]);
        this.kbdView.updateOrientation();
    }
}

