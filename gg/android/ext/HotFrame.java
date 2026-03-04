package android.ext;

import android.content.Context;
import android.fix.FrameLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.view.ViewConfiguration;

public class HotFrame extends FrameLayout implements View.OnLongClickListener {
    private static final int LONG_PRESS_TIMEOUT = 750;
    private final Runnable makeLongClick;

    public HotFrame(Context context) {
        super(context);
        this.setHapticFeedbackEnabled(false);
        this.setOnLongClickListener(this);
        this.makeLongClick = new Runnable() {
            @Override
            public void run() {
                boolean feedback = false;
                try {
                    HotFrame.this.setHapticFeedbackEnabled(true);
                    feedback = HotFrame.this.performHapticFeedback(0, 1);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                catch(Throwable e) {
                    Log.d("Failed make HapticFeedback", e);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                Log.d(("Real long click: " + feedback));
                MainService.instance.onLongClick(null);
            }
        };
    }

    public HotFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setHapticFeedbackEnabled(false);
        this.setOnLongClickListener(this);
        this.makeLongClick = new Runnable() {
            @Override
            public void run() {
                boolean feedback = false;
                try {
                    HotFrame.this.setHapticFeedbackEnabled(true);
                    feedback = HotFrame.this.performHapticFeedback(0, 1);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                catch(Throwable e) {
                    Log.d("Failed make HapticFeedback", e);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                Log.d(("Real long click: " + feedback));
                MainService.instance.onLongClick(null);
            }
        };
    }

    public HotFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setHapticFeedbackEnabled(false);
        this.setOnLongClickListener(this);
        this.makeLongClick = new Runnable() {
            @Override
            public void run() {
                boolean feedback = false;
                try {
                    HotFrame.this.setHapticFeedbackEnabled(true);
                    feedback = HotFrame.this.performHapticFeedback(0, 1);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                catch(Throwable e) {
                    Log.d("Failed make HapticFeedback", e);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                Log.d(("Real long click: " + feedback));
                MainService.instance.onLongClick(null);
            }
        };
    }

    public HotFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setHapticFeedbackEnabled(false);
        this.setOnLongClickListener(this);
        this.makeLongClick = new Runnable() {
            @Override
            public void run() {
                boolean feedback = false;
                try {
                    HotFrame.this.setHapticFeedbackEnabled(true);
                    feedback = HotFrame.this.performHapticFeedback(0, 1);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                catch(Throwable e) {
                    Log.d("Failed make HapticFeedback", e);
                    HotFrame.this.setHapticFeedbackEnabled(false);
                }
                Log.d(("Real long click: " + feedback));
                MainService.instance.onLongClick(null);
            }
        };
    }

    @Override  // android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            int v = event.getAction();
            if(v == 0 || v == 1 || v == 3) {
                this.removeCallbacks(this.makeLongClick);
            }
        }
        catch(NoSuchMethodError e) {
            Log.badImplementation(e);
        }
        try {
            return super.dispatchTouchEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View v) {
        int time = ViewConfiguration.getLongPressTimeout();
        if(time < 0) {
            time = 0;
        }
        Log.d(("Fake long click: " + (750 - time)));
        if(750 - time > 0) {
            this.removeCallbacks(this.makeLongClick);
            this.postDelayed(this.makeLongClick, ((long)(750 - time)));
            return true;
        }
        this.makeLongClick.run();
        return true;
    }
}

