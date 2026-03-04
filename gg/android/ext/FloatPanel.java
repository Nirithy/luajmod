package android.ext;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.fix.ContextWrapper;
import android.fix.LinearLayout;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import java.lang.reflect.Method;

public abstract class FloatPanel extends LinearLayout {
    private static float DELTA = 0.0f;
    private static final float DELTA_INIT = 36.0f;
    private static final String ICON_OPACITY = "opacity";
    private static final float ICON_OPACITY_DEFAULT = 1.0f;
    private int dragX;
    private int dragY;
    private boolean isClick;
    protected WindowManager.LayoutParams layoutParams;
    private int marginX;
    public final String prefName;
    private float startX;
    private float startY;
    boolean stop;
    boolean visible;
    private final WindowManager wm;
    private int x;
    private int y;

    static {
        FloatPanel.DELTA = 36.0f;
    }

    public FloatPanel(Context context) {
        super(context);
        this.marginX = 0;
        this.isClick = true;
        this.visible = false;
        this.stop = false;
        this.prefName = this.getPrefName();
        this.wm = (WindowManager)this.getContext().getSystemService("window");
        Context context1 = this.getContext();
        this.layoutParams = new WindowManager.LayoutParams();
        this.layoutParams.gravity = 51;
        this.layoutParams.type = 0x7F6;
        this.layoutParams.format = -2;
        this.layoutParams.flags = 0;
        SharedPreferences sharedPreferences0 = context1.getSharedPreferences("null_preferences", 0);
        this.layoutParams.alpha = 0.0f;
        this.load(sharedPreferences0);
        this.layoutParams.width = -2;
        this.layoutParams.height = -2;
        Class[] arr_class = {"checkPermission".getClass(), Integer.TYPE, Integer.TYPE};
        Object a = Context.class.getMethod("checkPermission", arr_class).invoke(context1, "android.permission.INTERNET", Process.myPid(), Process.myUid());
        WindowManager.LayoutParams windowManager$LayoutParams0 = this.layoutParams;
        windowManager$LayoutParams0.alpha = this.isInternal() ? 1.0f : sharedPreferences0.getFloat("opacity", 1.0f);
        Integer.valueOf(Process.myPid() / ((int)(((Integer)a))));
        this.layoutParams.flags |= 0x100;
        MainService.instance = null;
        WindowManager.LayoutParams windowManager$LayoutParams1 = this.layoutParams;
        windowManager$LayoutParams1.width = (int)Tools.dp2px(40.0f);
        WindowManager.LayoutParams windowManager$LayoutParams2 = this.layoutParams;
        windowManager$LayoutParams2.height = (int)Tools.dp2px(40.0f);
        if(!this.isInEditMode()) {
            FloatPanel.DELTA = Tools.dp2px(36.0f);
        }
    }

    public FloatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.marginX = 0;
        this.isClick = true;
        this.visible = false;
        this.stop = false;
        this.prefName = this.getPrefName();
        this.wm = (WindowManager)this.getContext().getSystemService("window");
        Context context1 = this.getContext();
        this.layoutParams = new WindowManager.LayoutParams();
        this.layoutParams.gravity = 51;
        this.layoutParams.type = 0x7F6;
        this.layoutParams.format = -2;
        this.layoutParams.flags = 0;
        SharedPreferences sharedPreferences0 = context1.getSharedPreferences("null_preferences", 0);
        this.layoutParams.alpha = 0.0f;
        this.load(sharedPreferences0);
        this.layoutParams.width = -2;
        this.layoutParams.height = -2;
        Class[] arr_class = {"checkPermission".getClass(), Integer.TYPE, Integer.TYPE};
        Object a = Context.class.getMethod("checkPermission", arr_class).invoke(context1, "android.permission.INTERNET", Process.myPid(), Process.myUid());
        WindowManager.LayoutParams windowManager$LayoutParams0 = this.layoutParams;
        windowManager$LayoutParams0.alpha = this.isInternal() ? 1.0f : sharedPreferences0.getFloat("opacity", 1.0f);
        Integer.valueOf(Process.myPid() / ((int)(((Integer)a))));
        this.layoutParams.flags |= 0x100;
        MainService.instance = null;
        WindowManager.LayoutParams windowManager$LayoutParams1 = this.layoutParams;
        windowManager$LayoutParams1.width = (int)Tools.dp2px(40.0f);
        WindowManager.LayoutParams windowManager$LayoutParams2 = this.layoutParams;
        windowManager$LayoutParams2.height = (int)Tools.dp2px(40.0f);
        if(!this.isInEditMode()) {
            FloatPanel.DELTA = Tools.dp2px(36.0f);
        }
    }

    public FloatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.marginX = 0;
        this.isClick = true;
        this.visible = false;
        this.stop = false;
        this.prefName = this.getPrefName();
        this.wm = (WindowManager)this.getContext().getSystemService("window");
        Context context1 = this.getContext();
        this.layoutParams = new WindowManager.LayoutParams();
        this.layoutParams.gravity = 51;
        this.layoutParams.type = 0x7F6;
        this.layoutParams.format = -2;
        this.layoutParams.flags = 0;
        SharedPreferences sharedPreferences0 = context1.getSharedPreferences("null_preferences", 0);
        this.layoutParams.alpha = 0.0f;
        this.load(sharedPreferences0);
        this.layoutParams.width = -2;
        this.layoutParams.height = -2;
        Class[] arr_class = {"checkPermission".getClass(), Integer.TYPE, Integer.TYPE};
        Object a = Context.class.getMethod("checkPermission", arr_class).invoke(context1, "android.permission.INTERNET", Process.myPid(), Process.myUid());
        WindowManager.LayoutParams windowManager$LayoutParams0 = this.layoutParams;
        windowManager$LayoutParams0.alpha = this.isInternal() ? 1.0f : sharedPreferences0.getFloat("opacity", 1.0f);
        Integer.valueOf(Process.myPid() / ((int)(((Integer)a))));
        this.layoutParams.flags |= 0x100;
        MainService.instance = null;
        WindowManager.LayoutParams windowManager$LayoutParams1 = this.layoutParams;
        windowManager$LayoutParams1.width = (int)Tools.dp2px(40.0f);
        WindowManager.LayoutParams windowManager$LayoutParams2 = this.layoutParams;
        windowManager$LayoutParams2.height = (int)Tools.dp2px(40.0f);
        if(!this.isInEditMode()) {
            FloatPanel.DELTA = Tools.dp2px(36.0f);
        }
    }

    public FloatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.marginX = 0;
        this.isClick = true;
        this.visible = false;
        this.stop = false;
        this.prefName = this.getPrefName();
        this.wm = (WindowManager)this.getContext().getSystemService("window");
        Context context1 = this.getContext();
        this.layoutParams = new WindowManager.LayoutParams();
        this.layoutParams.gravity = 51;
        this.layoutParams.type = 0x7F6;
        this.layoutParams.format = -2;
        this.layoutParams.flags = 0;
        SharedPreferences sharedPreferences0 = context1.getSharedPreferences("null_preferences", 0);
        this.layoutParams.alpha = 0.0f;
        this.load(sharedPreferences0);
        this.layoutParams.width = -2;
        this.layoutParams.height = -2;
        Class[] arr_class = {"checkPermission".getClass(), Integer.TYPE, Integer.TYPE};
        Object a = Context.class.getMethod("checkPermission", arr_class).invoke(context1, "android.permission.INTERNET", Process.myPid(), Process.myUid());
        WindowManager.LayoutParams windowManager$LayoutParams0 = this.layoutParams;
        windowManager$LayoutParams0.alpha = this.isInternal() ? 1.0f : sharedPreferences0.getFloat("opacity", 1.0f);
        Integer.valueOf(Process.myPid() / ((int)(((Integer)a))));
        this.layoutParams.flags |= 0x100;
        MainService.instance = null;
        WindowManager.LayoutParams windowManager$LayoutParams1 = this.layoutParams;
        windowManager$LayoutParams1.width = (int)Tools.dp2px(40.0f);
        WindowManager.LayoutParams windowManager$LayoutParams2 = this.layoutParams;
        windowManager$LayoutParams2.height = (int)Tools.dp2px(40.0f);
        if(!this.isInEditMode()) {
            FloatPanel.DELTA = Tools.dp2px(36.0f);
        }
    }

    @Override  // android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            float f = event.getRawX();
            float f1 = event.getRawY();
            float dx = f - this.startX;
            float dy = f1 - this.startY;
            switch(event.getAction()) {
                case 0: {
                    this.startX = f;
                    this.startY = f1;
                    this.isClick = true;
                    break;
                }
                case 2: {
                    if(!this.isClick || Math.abs(dx) > FloatPanel.DELTA || Math.abs(dy) > FloatPanel.DELTA) {
                        if(this.isClick) {
                            this.setLayoutXY();
                            this.dragX = this.x;
                            this.dragY = this.y;
                        }
                        this.isClick = false;
                        this.updatePosition(dx, dy, false);
                        event.setAction(3);
                    }
                    break;
                }
                case 1: 
                case 3: {
                    if(!this.isClick) {
                        this.updatePosition(dx, dy, true);
                    }
                }
            }
        }
        catch(NoSuchMethodError e) {
            Log.badImplementation(e);
        }
        try {
            return super.dispatchTouchEvent(event);
        }
        catch(ClassCastException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    private void fixCoords(boolean load) {
        Point screen = new Point(0, 0);
        Point point1 = this.fixCoords(new Point(this.x + this.marginX, this.y), load, screen);
        this.x = point1.x - this.marginX;
        this.y = point1.y;
    }

    protected Point fixCoords(Point coords, boolean load, Point screen) {
        if(coords.x < this.marginX) {
            coords.x = this.marginX;
        }
        if(coords.y < 0) {
            coords.y = 0;
        }
        Point point2 = this.getAvailScreenSize(screen);
        if(point2 != null) {
            if(load) {
                int max = point2.x <= point2.y ? point2.y : point2.x;
                if(coords.x > max) {
                    coords.x = max;
                }
                if(coords.y > max) {
                    coords.y = max;
                    return coords;
                }
            }
            else {
                if(coords.x > point2.x) {
                    coords.x = point2.x;
                }
                if(coords.y > point2.y) {
                    coords.y = point2.y;
                    return coords;
                }
            }
        }
        return coords;
    }

    protected Point getAvailScreenSize(Point out) {
        try {
            Display display0 = this.wm.getDefaultDisplay();
            int width = -1;
            int height = -1;
            try {
                if(Build.VERSION.SDK_INT >= 17) {
                    Point size = new Point();
                    display0.getRealSize(size);
                    width = size.x;
                    height = size.y;
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            try {
                if(Build.VERSION.SDK_INT >= 17 && height == -1) {
                    DisplayMetrics m = new DisplayMetrics();
                    display0.getRealMetrics(m);
                    width = m.widthPixels;
                    height = m.heightPixels;
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            if(Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT <= 16 && height == -1) {
                try {
                    Method method0 = Display.class.getMethod("getRawHeight");
                    width = (int)(((Integer)Display.class.getMethod("getRawWidth").invoke(display0)));
                    height = (int)(((Integer)method0.invoke(display0)));
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            if(height == -1) {
                try {
                    width = display0.getWidth();
                    height = display0.getHeight();
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            if(Build.VERSION.SDK_INT >= 13 && height == -1) {
                try {
                    Point size = new Point();
                    display0.getSize(size);
                    width = size.x;
                    height = size.y;
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            if(height == -1) {
                try {
                    DisplayMetrics m = new DisplayMetrics();
                    display0.getMetrics(m);
                    width = m.widthPixels;
                    height = m.heightPixels;
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            if(height != -1) {
                try {
                    width -= this.getWidth() + this.marginX;
                    height -= this.getHeight();
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
                if(out != null) {
                    out.x = width;
                    out.y = height;
                    return out;
                }
                return new Point(width, height);
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        return null;
    }

    protected String getDebug(String action) {
        return "FloatPanel " + this.prefName + ' ' + action + ": " + this.stop + ' ' + this.visible;
    }

    protected int getDefX() {
        return 0;
    }

    protected int getDefY() {
        return 0;
    }

    public float getLayoutAlpha() {
        return this.layoutParams.alpha;
    }

    public int getMarginX() {
        return this.marginX;
    }

    protected abstract String getPrefName();

    public void hide() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(FloatPanel.this.getDebug("hide"));
                if(!FloatPanel.this.visible) {
                    return;
                }
                FloatPanel.this.visible = false;
                FloatPanel.this.onHide();
            }
        });
    }

    protected boolean isInternal() {
        return false;
    }

    public boolean isVisible() {
        return this.visible;
    }

    protected void load(SharedPreferences prefs) {
        this.x = prefs.getInt(this.prefName + "-x", this.getDefX());
        this.y = prefs.getInt(this.prefName + "-y", this.getDefY());
        this.setLayoutXY(true);
    }

    @Override  // android.fix.LinearLayout
    protected void onConfigurationChanged(Configuration newConfig) {
        Log.d(("FloatPanel onConfigurationChanged: " + newConfig));
        Configuration configuration1 = ContextWrapper.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(configuration1);
        MainService.onConfigurationChanged(configuration1);
    }

    protected void onHide() {
        try {
            Tools.removeViewWithWrapper(this);
        }
        catch(Throwable e) {
            Log.w("Failed hide float icon", e);
        }
    }

    protected void onShow() {
        if(this.visible) {
            try {
                Tools.removeViewWithWrapper(this);
            }
            catch(Throwable e) {
                Log.w("Failed hide float icon", e);
            }
        }
        try {
            this.visible = true;
            Tools.addViewWithWrapper(this, this.layoutParams);
        }
        catch(Throwable e) {
            Log.w("Failed add float icon", e);
        }
    }

    public void setLayoutAlpha(float alpha) {
        if(this.layoutParams.alpha != alpha) {
            this.layoutParams.alpha = alpha;
            this.updateLayout();
            if(!this.isInternal()) {
                new SPEditor().putFloat("opacity", alpha, 1.0f).commit();
            }
        }
    }

    private void setLayoutXY(boolean load) {
        this.fixCoords(load);
        this.layoutParams.x = this.x + this.marginX;
        this.layoutParams.y = this.y;
    }

    protected void setLayoutXY() {
        this.setLayoutXY(false);
    }

    public void setMarginX(int marginX) {
        this.marginX = marginX;
    }

    public void show() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(FloatPanel.this.getDebug("show"));
                if(FloatPanel.this.stop) {
                    return;
                }
                FloatPanel.this.onShow();
            }
        });
    }

    public void stop() {
        this.stop = true;
        this.hide();
    }

    protected void updateLayout() {
        if(!this.visible) {
            return;
        }
        Tools.updateViewLayout(this, this.layoutParams);
    }

    private void updatePosition(float dx, float dy, boolean save) {
        this.x = this.dragX + ((int)dx);
        this.y = this.dragY + ((int)dy);
        this.setLayoutXY();
        this.updateLayout();
        if(save) {
            try {
                new SPEditor().putInt(this.prefName + "-x", this.x, this.getDefX()).putInt(this.prefName + "-y", this.y, this.getDefY()).commit();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
    }
}

