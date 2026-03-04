package android.ext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.fix.ImageView;
import android.fix.TextView;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressLint({"ClickableViewAccessibility"})
public class ShowApp extends ImageView {
    static class WindowStatus {
        float dimAmount;
        WeakReference ref;
        boolean useDim;

        public WindowStatus(WeakReference weakReference0, boolean useDim, float dimAmount) {
            this.ref = weakReference0;
            this.useDim = useDim;
            this.dimAmount = dimAmount;
        }
    }

    final Runnable hideScriptStop;
    volatile boolean hintShowed;
    TextView icon;
    WindowManager.LayoutParams iconParams;
    volatile boolean iconShowed;
    WindowManager.LayoutParams layoutParams;
    volatile View recordShowed;
    WindowManager.LayoutParams scriptParams;
    volatile View scriptShowed;
    volatile View scriptUiButtonShowed;
    private static ArrayList wins;

    static {
        ShowApp.wins = new ArrayList();
    }

    public ShowApp(Context context) {
        super(context);
        this.hintShowed = false;
        this.iconShowed = false;
        this.scriptShowed = null;
        this.scriptUiButtonShowed = null;
        this.recordShowed = null;
        if(!this.isInEditMode()) {
            WindowManager.LayoutParams windowManager$LayoutParams0 = new WindowManager.LayoutParams();
            this.scriptParams = windowManager$LayoutParams0;
            windowManager$LayoutParams0.type = 0x7F6;
            windowManager$LayoutParams0.width = -2;
            windowManager$LayoutParams0.height = -2;
            windowManager$LayoutParams0.format = -2;
            windowManager$LayoutParams0.gravity = 51;
            windowManager$LayoutParams0.x = -10000;
            windowManager$LayoutParams0.y = -10000;
            windowManager$LayoutParams0.flags = 8;
            WindowManager.LayoutParams windowManager$LayoutParams1 = new WindowManager.LayoutParams();
            this.layoutParams = windowManager$LayoutParams1;
            windowManager$LayoutParams1.type = 0x7F6;
            windowManager$LayoutParams1.width = (int)Tools.dp2px(60.0f);
            windowManager$LayoutParams1.height = (int)Tools.dp2px(20.0f);
            windowManager$LayoutParams1.format = -2;
            windowManager$LayoutParams1.gravity = 51;
            windowManager$LayoutParams1.x = 10000;
            windowManager$LayoutParams1.y = 10000;
            windowManager$LayoutParams1.flags = 0x108;
            WindowManager.LayoutParams windowManager$LayoutParams2 = new WindowManager.LayoutParams();
            this.iconParams = windowManager$LayoutParams2;
            windowManager$LayoutParams2.type = 0x7F6;
            windowManager$LayoutParams2.width = -2;
            windowManager$LayoutParams2.height = -2;
            windowManager$LayoutParams2.format = -2;
            windowManager$LayoutParams2.gravity = 49;
            windowManager$LayoutParams2.x = 0;
            windowManager$LayoutParams2.y = -1000;
            windowManager$LayoutParams2.flags = 24;
            this.setOnTouchListener(new View.OnTouchListener() {
                @Override  // android.view.View$OnTouchListener
                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case 0: {
                            ShowApp.this.makeTransparentUI(true);
                            return true;
                        }
                        case 1: 
                        case 3: {
                            ShowApp.this.makeTransparentUI(false);
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
            });
        }
        this.hideScriptStop = () -> {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = ShowApp.this.scriptShowed;
                    if(view != null && (MainService.instance.scriptInterrupt == null || MainService.instance.mainDialog == null)) {
                        ShowApp.this.hideScript(view);
                    }
                }
            });
        };
    }

    public ShowApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.hintShowed = false;
        this.iconShowed = false;
        this.scriptShowed = null;
        this.scriptUiButtonShowed = null;
        this.recordShowed = null;
        if(!this.isInEditMode()) {
            WindowManager.LayoutParams windowManager$LayoutParams0 = new WindowManager.LayoutParams();
            this.scriptParams = windowManager$LayoutParams0;
            windowManager$LayoutParams0.type = 0x7F6;
            windowManager$LayoutParams0.width = -2;
            windowManager$LayoutParams0.height = -2;
            windowManager$LayoutParams0.format = -2;
            windowManager$LayoutParams0.gravity = 51;
            windowManager$LayoutParams0.x = -10000;
            windowManager$LayoutParams0.y = -10000;
            windowManager$LayoutParams0.flags = 8;
            WindowManager.LayoutParams windowManager$LayoutParams1 = new WindowManager.LayoutParams();
            this.layoutParams = windowManager$LayoutParams1;
            windowManager$LayoutParams1.type = 0x7F6;
            windowManager$LayoutParams1.width = (int)Tools.dp2px(60.0f);
            windowManager$LayoutParams1.height = (int)Tools.dp2px(20.0f);
            windowManager$LayoutParams1.format = -2;
            windowManager$LayoutParams1.gravity = 51;
            windowManager$LayoutParams1.x = 10000;
            windowManager$LayoutParams1.y = 10000;
            windowManager$LayoutParams1.flags = 0x108;
            WindowManager.LayoutParams windowManager$LayoutParams2 = new WindowManager.LayoutParams();
            this.iconParams = windowManager$LayoutParams2;
            windowManager$LayoutParams2.type = 0x7F6;
            windowManager$LayoutParams2.width = -2;
            windowManager$LayoutParams2.height = -2;
            windowManager$LayoutParams2.format = -2;
            windowManager$LayoutParams2.gravity = 49;
            windowManager$LayoutParams2.x = 0;
            windowManager$LayoutParams2.y = -1000;
            windowManager$LayoutParams2.flags = 24;
            this.setOnTouchListener(new View.OnTouchListener() {
                @Override  // android.view.View$OnTouchListener
                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case 0: {
                            ShowApp.this.makeTransparentUI(true);
                            return true;
                        }
                        case 1: 
                        case 3: {
                            ShowApp.this.makeTransparentUI(false);
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
            });
        }
        this.hideScriptStop = () -> {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = ShowApp.this.scriptShowed;
                    if(view != null && (MainService.instance.scriptInterrupt == null || MainService.instance.mainDialog == null)) {
                        ShowApp.this.hideScript(view);
                    }
                }
            });
        };
    }

    public ShowApp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.hintShowed = false;
        this.iconShowed = false;
        this.scriptShowed = null;
        this.scriptUiButtonShowed = null;
        this.recordShowed = null;
        if(!this.isInEditMode()) {
            WindowManager.LayoutParams windowManager$LayoutParams0 = new WindowManager.LayoutParams();
            this.scriptParams = windowManager$LayoutParams0;
            windowManager$LayoutParams0.type = 0x7F6;
            windowManager$LayoutParams0.width = -2;
            windowManager$LayoutParams0.height = -2;
            windowManager$LayoutParams0.format = -2;
            windowManager$LayoutParams0.gravity = 51;
            windowManager$LayoutParams0.x = -10000;
            windowManager$LayoutParams0.y = -10000;
            windowManager$LayoutParams0.flags = 8;
            WindowManager.LayoutParams windowManager$LayoutParams1 = new WindowManager.LayoutParams();
            this.layoutParams = windowManager$LayoutParams1;
            windowManager$LayoutParams1.type = 0x7F6;
            windowManager$LayoutParams1.width = (int)Tools.dp2px(60.0f);
            windowManager$LayoutParams1.height = (int)Tools.dp2px(20.0f);
            windowManager$LayoutParams1.format = -2;
            windowManager$LayoutParams1.gravity = 51;
            windowManager$LayoutParams1.x = 10000;
            windowManager$LayoutParams1.y = 10000;
            windowManager$LayoutParams1.flags = 0x108;
            WindowManager.LayoutParams windowManager$LayoutParams2 = new WindowManager.LayoutParams();
            this.iconParams = windowManager$LayoutParams2;
            windowManager$LayoutParams2.type = 0x7F6;
            windowManager$LayoutParams2.width = -2;
            windowManager$LayoutParams2.height = -2;
            windowManager$LayoutParams2.format = -2;
            windowManager$LayoutParams2.gravity = 49;
            windowManager$LayoutParams2.x = 0;
            windowManager$LayoutParams2.y = -1000;
            windowManager$LayoutParams2.flags = 24;
            this.setOnTouchListener(new View.OnTouchListener() {
                @Override  // android.view.View$OnTouchListener
                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case 0: {
                            ShowApp.this.makeTransparentUI(true);
                            return true;
                        }
                        case 1: 
                        case 3: {
                            ShowApp.this.makeTransparentUI(false);
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
            });
        }
        this.hideScriptStop = () -> {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = ShowApp.this.scriptShowed;
                    if(view != null && (MainService.instance.scriptInterrupt == null || MainService.instance.mainDialog == null)) {
                        ShowApp.this.hideScript(view);
                    }
                }
            });
        };
    }

    @TargetApi(21)
    public ShowApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.hintShowed = false;
        this.iconShowed = false;
        this.scriptShowed = null;
        this.scriptUiButtonShowed = null;
        this.recordShowed = null;
        if(!this.isInEditMode()) {
            WindowManager.LayoutParams windowManager$LayoutParams0 = new WindowManager.LayoutParams();
            this.scriptParams = windowManager$LayoutParams0;
            windowManager$LayoutParams0.type = 0x7F6;
            windowManager$LayoutParams0.width = -2;
            windowManager$LayoutParams0.height = -2;
            windowManager$LayoutParams0.format = -2;
            windowManager$LayoutParams0.gravity = 51;
            windowManager$LayoutParams0.x = -10000;
            windowManager$LayoutParams0.y = -10000;
            windowManager$LayoutParams0.flags = 8;
            WindowManager.LayoutParams windowManager$LayoutParams1 = new WindowManager.LayoutParams();
            this.layoutParams = windowManager$LayoutParams1;
            windowManager$LayoutParams1.type = 0x7F6;
            windowManager$LayoutParams1.width = (int)Tools.dp2px(60.0f);
            windowManager$LayoutParams1.height = (int)Tools.dp2px(20.0f);
            windowManager$LayoutParams1.format = -2;
            windowManager$LayoutParams1.gravity = 51;
            windowManager$LayoutParams1.x = 10000;
            windowManager$LayoutParams1.y = 10000;
            windowManager$LayoutParams1.flags = 0x108;
            WindowManager.LayoutParams windowManager$LayoutParams2 = new WindowManager.LayoutParams();
            this.iconParams = windowManager$LayoutParams2;
            windowManager$LayoutParams2.type = 0x7F6;
            windowManager$LayoutParams2.width = -2;
            windowManager$LayoutParams2.height = -2;
            windowManager$LayoutParams2.format = -2;
            windowManager$LayoutParams2.gravity = 49;
            windowManager$LayoutParams2.x = 0;
            windowManager$LayoutParams2.y = -1000;
            windowManager$LayoutParams2.flags = 24;
            this.setOnTouchListener(new View.OnTouchListener() {
                @Override  // android.view.View$OnTouchListener
                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case 0: {
                            ShowApp.this.makeTransparentUI(true);
                            return true;
                        }
                        case 1: 
                        case 3: {
                            ShowApp.this.makeTransparentUI(false);
                            return true;
                        }
                        default: {
                            return false;
                        }
                    }
                }
            });
        }
        this.hideScriptStop = () -> {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = ShowApp.this.scriptShowed;
                    if(view != null && (MainService.instance.scriptInterrupt == null || MainService.instance.mainDialog == null)) {
                        ShowApp.this.hideScript(view);
                    }
                }
            });
        };
    }

    public void hide(boolean hideScript) {
        if(hideScript) {
            this.hideScript();
        }
        else {
            ThreadManager.getHandlerUiThread().postDelayed(this.hideScriptStop, 2000L);
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.hide(ShowApp.this)) {
                    ShowApp.this.iconShowed = false;
                }
                if(!hideScript && ShowApp.this.hide(ShowApp.this.scriptUiButtonShowed)) {
                    ShowApp.this.scriptUiButtonShowed = null;
                }
            }
        });
    }

    boolean hide(View view) {
        try {
            if(view != null) {
                if(view instanceof FloatPanel) {
                    ((FloatPanel)view).hide();
                    return true;
                }
                Tools.removeViewWithWrapper(view);
                return true;
            }
        }
        catch(Throwable e) {
            Log.w(("Failed remove view " + view), e);
        }
        return false;
    }

    public void hideHint() {
        this.hideScript();
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.hintShowed && ShowApp.this.hide(ShowApp.this.icon)) {
                    ShowApp.this.hintShowed = false;
                }
            }
        });
    }

    public void hideRecord() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.hide(ShowApp.this.recordShowed)) {
                    ShowApp.this.recordShowed = null;
                }
            }
        });
    }

    // 检测为 Lambda 实现
    public void hideScript() [...]

    void hideScript(View view) {
        if(view != null) {
            if(this.hide(view)) {
                this.scriptShowed = null;
            }
            if(this.hide(this.scriptUiButtonShowed)) {
                this.scriptUiButtonShowed = null;
            }
        }
    }

    public void hideUi() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.scriptShowed == null) {
                    ShowApp.this.showScript();
                    ThreadManager.getHandlerUiThread().postDelayed(ShowApp.this.hideScriptStop, 2000L);
                }
            }
        });
    }

    void makeTransparentUI(boolean transparent) {
        float f = 0.0f;
        for(Object object0: ShowApp.wins) {
            WindowStatus win = (WindowStatus)object0;
            Window window = (Window)win.ref.get();
            if(window != null) {
                try {
                    WindowManager.LayoutParams windowManager$LayoutParams0 = window.getAttributes();
                    float f1 = transparent ? 0.0f : 1.0f;
                    windowManager$LayoutParams0.alpha = f1;
                    if(win.useDim) {
                        windowManager$LayoutParams0.flags = transparent ? windowManager$LayoutParams0.flags & -3 : windowManager$LayoutParams0.flags | 2;
                        windowManager$LayoutParams0.dimAmount = transparent ? 0.0f : win.dimAmount;
                    }
                    window.setAttributes(windowManager$LayoutParams0);
                }
                catch(Throwable e) {
                    Log.w("Failed set transparency", e);
                }
            }
        }
        if(!transparent) {
            f = 1.0f;
        }
        if(this.hintShowed) {
            this.setTransparency(this.icon, f);
        }
        this.setTransparency(this.scriptShowed, f);
        this.setTransparency(this.scriptUiButtonShowed, f);
        this.setTransparency(this.recordShowed, f);
    }

    public static void register(Window w) {
        WindowManager.LayoutParams windowManager$LayoutParams0 = w.getAttributes();
        ArrayList oldWins = ShowApp.wins;
        ArrayList newWins = new ArrayList();
        for(Object object0: oldWins) {
            WindowStatus win = (WindowStatus)object0;
            Window window = (Window)win.ref.get();
            if(window != null && window != w) {
                newWins.add(win);
            }
        }
        newWins.add(new WindowStatus(new WeakReference(w), (windowManager$LayoutParams0.flags & 2) != 0, windowManager$LayoutParams0.dimAmount));
        ShowApp.wins = newWins;
        oldWins.clear();
        oldWins.trimToSize();
    }

    private void setTransparency(View view, float alpha) {
        if(view == null) {
            return;
        }
        else {
            try {
                if(Build.VERSION.SDK_INT >= 11) {
                    view.setAlpha(alpha);
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        Log.w("Failed set transparency", e);
    }

    public void show() {
        this.showScript();
        this.showRecord();
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.iconShowed && ShowApp.this.hide(ShowApp.this)) {
                    ShowApp.this.iconShowed = false;
                }
                if(ShowApp.this.show(ShowApp.this, ShowApp.this.layoutParams)) {
                    ShowApp.this.iconShowed = true;
                }
            }
        });
    }

    boolean show(View view, WindowManager.LayoutParams params) {
        boolean ret = false;
        try {
            if(view != null) {
                if(view instanceof FloatPanel) {
                    ((FloatPanel)view).show();
                }
                else {
                    Tools.addViewWithWrapper(view, params);
                }
                ret = true;
                if(Build.VERSION.SDK_INT >= 11) {
                    view.setAlpha(1.0f);
                }
            }
        }
        catch(Throwable e) {
            Log.w(("Failed add view " + view), e);
        }
        return ret;
    }

    public void showHint() {
        if(this.scriptShowed == null) {
            this.showScript();
        }
        if(this.recordShowed == null) {
            this.showRecord();
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.hintShowed && ShowApp.this.hide(ShowApp.this.icon)) {
                    ShowApp.this.hintShowed = false;
                }
                if(ShowApp.this.show(ShowApp.this.icon, ShowApp.this.iconParams)) {
                    ShowApp.this.hintShowed = true;
                }
            }
        });
    }

    public void showRecord() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ShowApp.this.hide(ShowApp.this.recordShowed)) {
                    ShowApp.this.recordShowed = null;
                }
                MainService service = MainService.instance;
                if(service != null) {
                    View view = service.recordInterrupt;
                    if(ShowApp.this.show(view, ShowApp.this.scriptParams)) {
                        ShowApp.this.recordShowed = view;
                    }
                }
            }
        });
    }

    public void showScript() {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ThreadManager.getHandlerUiThread().removeCallbacks(ShowApp.this.hideScriptStop);
                ShowApp.this.hideScript(ShowApp.this.scriptShowed);
                MainService service = MainService.instance;
                if(service != null) {
                    View view = service.scriptInterrupt;
                    if(view != null) {
                        View uiButton = service.scriptUiButton;
                        if(uiButton != null && MainService.instance.mainDialog != null && ShowApp.this.show(uiButton, ShowApp.this.scriptParams)) {
                            ShowApp.this.scriptUiButtonShowed = uiButton;
                        }
                        if(ShowApp.this.show(view, ShowApp.this.scriptParams)) {
                            ShowApp.this.scriptShowed = view;
                        }
                    }
                }
            }
        });
    }

    public void update() {
        if(this.scriptShowed != null) {
            this.showScript();
        }
        if(this.recordShowed != null) {
            this.showRecord();
        }
        if(this.iconShowed) {
            this.show();
        }
    }

    class android.ext.ShowApp.2 implements Runnable {
        @Override
        public void run() {
            ShowApp.this.hideScript();
        }
    }

}

