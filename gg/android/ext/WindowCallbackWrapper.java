package android.ext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.view.ActionMode.Callback;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window.Callback;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;

public class WindowCallbackWrapper implements Window.Callback {
    private boolean calledFinished;
    private boolean calledStarted;
    private boolean calledStarting;
    private InternalKeyboard kbdView;
    Window.Callback source;

    public WindowCallbackWrapper(Window.Callback source) {
        this.kbdView = null;
        this.calledStarting = false;
        this.calledStarted = false;
        this.calledFinished = false;
        this.source = source;
    }

    @Override  // android.view.Window$Callback
    @TargetApi(12)
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        try {
            return this.source.dispatchGenericMotionEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.Window$Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            return this.source.dispatchKeyEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.Window$Callback
    @TargetApi(11)
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        try {
            return this.source.dispatchKeyShortcutEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.Window$Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return this.source.dispatchPopulateAccessibilityEvent(event);
    }

    @Override  // android.view.Window$Callback
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            return this.source.dispatchTouchEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.Window$Callback
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return this.source.dispatchTrackballEvent(event);
    }

    @Override  // android.view.Window$Callback
    @TargetApi(11)
    public void onActionModeFinished(ActionMode mode) {
        if(this.calledFinished) {
            return;
        }
        try {
            this.calledFinished = true;
            this.source.onActionModeFinished(mode);
            mode.finish();
            this.calledFinished = false;
        }
        catch(Throwable throwable0) {
            this.calledFinished = false;
            throw throwable0;
        }
    }

    @Override  // android.view.Window$Callback
    @TargetApi(11)
    public void onActionModeStarted(ActionMode mode) {
        if(this.calledStarted) {
            return;
        }
        try {
            this.calledStarted = true;
            this.source.onActionModeStarted(mode);
            this.calledStarted = false;
        }
        catch(Throwable throwable0) {
            this.calledStarted = false;
            throw throwable0;
        }
    }

    @Override  // android.view.Window$Callback
    public void onAttachedToWindow() {
        this.source.onAttachedToWindow();
    }

    @Override  // android.view.Window$Callback
    public void onContentChanged() {
        this.source.onContentChanged();
    }

    @Override  // android.view.Window$Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return this.source.onCreatePanelMenu(featureId, menu);
    }

    @Override  // android.view.Window$Callback
    public View onCreatePanelView(int featureId) {
        return this.source.onCreatePanelView(featureId);
    }

    @Override  // android.view.Window$Callback
    @SuppressLint({"MissingSuperCall"})
    public void onDetachedFromWindow() {
        this.source.onDetachedFromWindow();
    }

    @Override  // android.view.Window$Callback
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return this.source.onMenuItemSelected(featureId, item);
    }

    @Override  // android.view.Window$Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        return this.source.onMenuOpened(featureId, menu);
    }

    @Override  // android.view.Window$Callback
    public void onPanelClosed(int featureId, Menu menu) {
        this.source.onPanelClosed(featureId, menu);
    }

    @Override  // android.view.Window$Callback
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return this.source.onPreparePanel(featureId, view, menu);
    }

    @Override  // android.view.Window$Callback
    public boolean onSearchRequested() {
        return this.source.onSearchRequested();
    }

    @Override  // android.view.Window$Callback
    @TargetApi(23)
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return this.source.onSearchRequested(searchEvent);
    }

    @Override  // android.view.Window$Callback
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        try {
            this.source.onWindowAttributesChanged(attrs);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.Window$Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        try {
            this.source.onWindowFocusChanged(hasFocus);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
        }
        if(this.kbdView != null) {
            this.kbdView.onWindowFocusChanged();
        }
    }

    @Override  // android.view.Window$Callback
    @TargetApi(11)
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        ActionMode mode;
        if(this.calledStarting) {
            return null;
        }
        try {
            this.calledStarting = true;
            mode = null;
            mode = this.source.onWindowStartingActionMode(callback);
            if(mode == null) {
                ActionModeImpl actionMode = new ActionModeImpl(callback);
                mode = actionMode;
                if(callback.onCreateActionMode(mode, mode.getMenu())) {
                    actionMode.invalidate();
                    actionMode.show();
                }
            }
            this.calledStarting = false;
            return mode;
        }
        catch(Throwable e) {
            try {
                Log.e("Fail on onWindowStartingActionMode", e);
                this.calledStarting = false;
                return mode;
            }
            catch(Throwable throwable1) {
                this.calledStarting = false;
                throw throwable1;
            }
        }
    }

    @Override  // android.view.Window$Callback
    @TargetApi(23)
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        ActionMode mode = null;
        try {
            mode = this.source.onWindowStartingActionMode(callback, type);
            return mode != null || type != 0 ? mode : this.onWindowStartingActionMode(callback);
        }
        catch(Throwable e) {
            Log.e("Fail on onWindowStartingActionMode", e);
            return mode;
        }
    }

    public void setKeyboard(InternalKeyboard kbdView) {
        this.kbdView = kbdView;
    }
}

