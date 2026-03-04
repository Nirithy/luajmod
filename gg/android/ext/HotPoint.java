package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HotPoint extends FloatPanel implements View.OnClickListener, View.OnLongClickListener {
    private static final String ICON_SIZE = "icon-size";
    private static final int ICON_SIZE_DEFAULT = 0;
    public static final int ICON_SIZE_MIN = 12;
    private static final String ICON_VANISHING_TIME = "vanishing-time";
    private static final int ICON_VANISHING_TIME_DEFAULT = -1;
    ProgressBar busyProgress;
    ProgressBar busyProgressStage;
    TextView busyProgressText;
    View hotFrame;
    private static volatile HotPoint instance;
    private final Runnable mClearSprite;
    ImageView mDecButton;
    ImageView mHotPointIcon;
    ImageView mIncButton;
    int mSize;
    int mVanishingTime;
    View progressLayout;
    TextView speedPanel;
    private WindowManager.LayoutParams speedPanelParams;
    private boolean speedPanelVisible;

    static {
        HotPoint.instance = null;
    }

    public HotPoint(Context context) {
        super(context);
        this.mClearSprite = new Runnable() {
            @Override
            public void run() {
                HotPoint.this.hidePanel();
                Tools.setImageAlpha(HotPoint.this.mHotPointIcon, 0.0f);
            }
        };
    }

    public HotPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mClearSprite = new Runnable() {
            @Override
            public void run() {
                HotPoint.this.hidePanel();
                Tools.setImageAlpha(HotPoint.this.mHotPointIcon, 0.0f);
            }
        };
    }

    @TargetApi(11)
    public HotPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mClearSprite = new Runnable() {
            @Override
            public void run() {
                HotPoint.this.hidePanel();
                Tools.setImageAlpha(HotPoint.this.mHotPointIcon, 0.0f);
            }
        };
    }

    @TargetApi(21)
    public HotPoint(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mClearSprite = new Runnable() {
            @Override
            public void run() {
                HotPoint.this.hidePanel();
                Tools.setImageAlpha(HotPoint.this.mHotPointIcon, 0.0f);
            }
        };
    }

    @Override  // android.ext.FloatPanel
    protected String getDebug(String action) {
        return super.getDebug(action) + ' ' + this.mVanishingTime + ' ' + this.mSize;
    }

    public static HotPoint getInstance() {
        HotPoint inst = HotPoint.instance;
        if(inst == null) {
            inst = (HotPoint)LayoutInflater.inflateStatic(0x7F040005, null);  // layout:hot_point_view
            inst.init();
            HotPoint.instance = inst;
        }
        return inst;
    }

    @Override  // android.ext.FloatPanel
    protected String getPrefName() {
        return "pos";
    }

    public int getSize() {
        return this.mSize;
    }

    public int getSizePx() {
        return (int)Tools.dp2px(0x30 - this.mSize * 2);
    }

    public int getVanishingTime() {
        return this.mVanishingTime;
    }

    public void hidePanel() {
        this.setVisibilitySpeedhack(8);
    }

    private void init() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HotPoint.this.hotFrame = HotPoint.this.findViewById(0x7F0B0020);  // id:hot_frame
                HotPoint.this.hotFrame.setOnClickListener(MainService.instance);
                ImageView imageView0 = (ImageView)HotPoint.this.findViewById(0x7F0B0021);  // id:hot_point_icon
                HotPoint.this.mHotPointIcon = imageView0;
                ((AnimationDrawable)imageView0.getDrawable()).start();
                HotPoint.this.progressLayout = HotPoint.this.findViewById(0x7F0B0022);  // id:progress_layout
                HotPoint.this.busyProgressText = (TextView)HotPoint.this.findViewById(0x7F0B0023);  // id:progress_bar_text
                HotPoint.this.busyProgress = (ProgressBar)HotPoint.this.findViewById(0x7F0B0024);  // id:progress_bar
                HotPoint.this.busyProgressStage = (ProgressBar)HotPoint.this.findViewById(0x7F0B0025);  // id:progress_bar_stage
                HotPoint.this.speedPanel = (TextView)Tools.getViewForAttach(HotPoint.this.findViewById(0x7F0B0027));  // id:speed_text
                HotPoint.this.mIncButton = (ImageView)HotPoint.this.findViewById(0x7F0B0026);  // id:speed_inc
                HotPoint.this.mDecButton = (ImageView)HotPoint.this.findViewById(0x7F0B001F);  // id:speed_dec
                HotPoint.this.mIncButton.setOnClickListener(HotPoint.this);
                HotPoint.this.mDecButton.setOnClickListener(HotPoint.this);
                HotPoint.this.speedPanel.setOnLongClickListener(HotPoint.this);
                if(Build.VERSION.SDK_INT >= 11) {
                    HotPoint.this.speedPanel.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override  // android.view.View$OnLayoutChangeListener
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            HotPoint.this.updateLayout();
                        }
                    });
                }
                HotPoint.this.onProgress(2, -1L, 1L, 0, 1, "");
                HotPoint.this.initSpeedPanel();
                HotPoint.this.hidePanel();
            }
        });
    }

    void initSpeedPanel() {
        this.speedPanelParams = new WindowManager.LayoutParams();
        this.speedPanelParams.gravity = 51;
        this.speedPanelParams.type = 0x7F6;
        this.speedPanelParams.format = -2;
        this.speedPanelParams.flags = 0x200;
        this.speedPanelParams.alpha = 1.0f;
        this.speedPanelParams.width = -2;
        this.speedPanelParams.height = -2;
    }

    @Override  // android.ext.FloatPanel
    protected void load(SharedPreferences prefs) {
        super.load(prefs);
        this.mVanishingTime = prefs.getInt("vanishing-time", -1);
        this.mSize = Math.max(0, Math.min(12, prefs.getInt("icon-size", 0)));
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(v == this.mIncButton) {
            MainService.instance.nextSpeed();
            return;
        }
        if(v == this.mDecButton) {
            MainService.instance.prevSpeed();
        }
    }

    @Override  // android.ext.FloatPanel
    protected void onHide() {
        super.onHide();
        this.postCallback(false);
        this.setSpeedPanelVisibility(false);
        TimeJumpPanel.toggle(false);
    }

    @Override  // android.widget.LinearLayout
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.setMargins();
    }

    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View v) {
        MainService.instance.resetSpeed();
        return true;
    }

    public void onProgress(byte use, long progress, long progressMax, int stage, int stageMax, String progressText) {
        if(ThreadManager.isInUiThread()) {
            this.onProgressUi(use, progress, progressMax, stage, stageMax, progressText);
            return;
        }
        ThreadManager.runOnUiThread(() -> {
            if(this.val$use != 0) {
                HotPoint.this.progressLayout.setVisibility((this.val$use == 1 ? 0 : 8));
            }
            if(this.val$progress >= 0L && this.val$progressMax != 0L) {
                HotPoint.this.busyProgress.setIndeterminate(false);
                HotPoint.this.busyProgress.setMax(((int)this.val$progressMax));
                HotPoint.this.busyProgress.setProgress(((int)this.val$progress));
                HotPoint.this.busyProgressText.setText(this.val$progressText);
            }
            else {
                HotPoint.this.busyProgressText.setText(this.val$progressText);
                HotPoint.this.busyProgress.setIndeterminate(true);
            }
            if(this.val$stage >= 0) {
                if(this.val$stageMax <= 1) {
                    HotPoint.this.busyProgressStage.setVisibility(8);
                    return;
                }
                HotPoint.this.busyProgressStage.setMax(this.val$stageMax);
                HotPoint.this.busyProgressStage.setProgress(this.val$stage);
                HotPoint.this.busyProgressStage.setVisibility(0);
            }
        });

        class android.ext.HotPoint.3 implements Runnable {
            android.ext.HotPoint.3(byte b, long v, long v1, int v2, int v3, String s) {
            }

            @Override
            public void run() {
                HotPoint.this.onProgressUi(this.val$use, this.val$progress, this.val$progressMax, this.val$stage, this.val$stageMax, this.val$progressText);
            }
        }

    }

    // 检测为 Lambda 实现
    void onProgressUi(byte use, long progress, long progressMax, int stage, int stageMax, String progressText) [...]

    @Override  // android.ext.FloatPanel
    protected void onShow() {
        super.onShow();
        int v = this.getSizePx();
        Config.setIconSizeEx(this.mIncButton, v);
        Config.setIconSizeEx(this.mDecButton, v);
        Config.setIconSizeEx(this.mHotPointIcon, v);
        this.hotFrame.setMinimumWidth(v);
        this.hotFrame.setMinimumHeight(v);
        Tools.setWidth(this.busyProgressText, ((float)v));
        Tools.setHeight(this.busyProgressText, ((float)(v * 20 / 0x30)));
        Tools.setWidth(this.busyProgress, ((float)v));
        Tools.setHeight(this.busyProgress, ((float)(v * 14 / 0x30)));
        Tools.setWidth(this.busyProgressStage, ((float)v));
        Tools.setHeight(this.busyProgressStage, ((float)(v * 14 / 0x30)));
        this.busyProgressText.setTextSize(0, ((float)(v * 18 / 0x30)));
        this.speedPanel.setTextSize(0, ((float)(v * 18 / 0x30)));
        if(this.mVanishingTime < 0) {
            Tools.setImageAlpha(this.mHotPointIcon, 255.0f);
        }
        else {
            this.postCallback(true);
        }
        this.setSpeedPanelVisibility(true);
        TimeJumpPanel.toggle(true);
    }

    void postCallback(boolean post) {
        try {
            ThreadManager.getHandlerUiThread().removeCallbacks(this.mClearSprite);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        if(post) {
            Tools.setImageAlpha(this.mHotPointIcon, 255.0f);
            try {
                ThreadManager.getHandlerUiThread().postDelayed(this.mClearSprite, ((long)(this.mVanishingTime * 1000)));
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                Tools.setImageAlpha(this.mHotPointIcon, 0.0f);
            }
        }
    }

    @Override  // android.ext.FloatPanel
    public void setLayoutAlpha(float alpha) {
        this.speedPanelParams.alpha = alpha;
        super.setLayoutAlpha(alpha);
        TimeJumpPanel panel = MainService.instance.timeJumpPanel;
        if(panel != null) {
            panel.setLayoutAlpha(alpha);
        }
    }

    private void setMargins() {
        if(this.hotFrame != null) {
            int v = this.getMarginX();
            int newX = 0;
            for(View v = this.hotFrame; v != this && v != null; v = (View)viewParent0) {
                newX -= v.getLeft();
                ViewParent viewParent0 = v.getParent();
                if(!(viewParent0 instanceof View)) {
                    break;
                }
            }
            if(v != newX) {
                this.setMarginX(newX);
                this.setLayoutXY();
                this.updateLayout();
            }
        }
    }

    public void setSize(int size) {
        int v1 = Math.max(0, Math.min(12, size));
        if(this.mSize == v1) {
            return;
        }
        new SPEditor().putInt("icon-size", v1, 0).commit();
        this.mSize = v1;
    }

    void setSpeedPanelVisibility(boolean show) {
        boolean visible = this.mIncButton.getVisibility() == 0;
        if(this.speedPanelVisible) {
            try {
                Tools.removeViewWithWrapper(this.speedPanel);
            }
            catch(Throwable e) {
                Log.w("Failed hide speed panel", e);
            }
            this.speedPanelVisible = false;
        }
        if(show && visible) {
            try {
                this.speedPanelVisible = true;
                Tools.addViewWithWrapper(this.speedPanel, this.speedPanelParams);
            }
            catch(Throwable e) {
                Log.w("Failed add speed panel", e);
            }
        }
    }

    public void setVanishingTime(int time) {
        if(this.mVanishingTime == time) {
            return;
        }
        new SPEditor().putInt("vanishing-time", time, -1).commit();
        this.mVanishingTime = time;
    }

    private void setVisibilitySpeedhack(int vis) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = 8;
                if(vis != -1) {
                    v = vis;
                }
                else if(HotPoint.this.mIncButton.getVisibility() == 8) {
                    v = 0;
                }
                HotPoint.this.mIncButton.setVisibility(v);
                HotPoint.this.mDecButton.setVisibility(v);
                HotPoint.this.setSpeedPanelVisibility(true);
                HotPoint.this.updateLayout();
                if(v == 0 && HotPoint.this.mVanishingTime >= 0) {
                    HotPoint.this.postCallback(true);
                }
            }
        });
    }

    public void showPanel() {
        this.setVisibilitySpeedhack(0);
    }

    public void togglePanel() {
        this.setVisibilitySpeedhack(-1);
    }

    public void updateIcon(Drawable icon) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.addIconToTextView(HotPoint.this.speedPanel, icon, 24 - HotPoint.this.mSize);
            }
        });
    }

    @Override  // android.ext.FloatPanel
    public void updateLayout() {
        super.updateLayout();
        if(this.speedPanelVisible) {
            Point screen = new Point(0, 0);
            Point point1 = this.fixCoords(new Point(this.layoutParams.x, this.layoutParams.y), false, screen);
            if(point1.x < 0) {
                point1.x = 0;
            }
            if(point1.x > screen.x + this.getMarginX()) {
                point1.x = screen.x + this.getMarginX();
            }
            WindowManager.LayoutParams windowManager$LayoutParams0 = this.speedPanelParams;
            windowManager$LayoutParams0.x = point1.x + this.getWidth() / 2 - this.speedPanel.getWidth() / 2;
            WindowManager.LayoutParams windowManager$LayoutParams1 = this.speedPanelParams;
            windowManager$LayoutParams1.y = point1.y + this.getHeight();
            if(screen.y != 0) {
                int v = this.speedPanel.getHeight();
                if(screen.y < point1.y + v) {
                    this.speedPanelParams.y = point1.y - v;
                }
            }
            Tools.updateViewLayout(this.speedPanel, this.speedPanelParams);
        }
    }

    public void updatePanels() {
        TimeJumpPanel.toggle(this.isVisible());
    }

    public void updateSpeed(String format) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HotPoint.this.speedPanel.setText(format);
                HotPoint.this.updateLayout();
            }
        });
    }
}

