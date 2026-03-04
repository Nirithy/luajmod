package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.fix.Button;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.menu.ActionMenu;
import android.menu.ActionMenuItem;
import android.view.ActionMode.Callback;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

@TargetApi(11)
public class ActionModeImpl extends ActionMode implements View.OnClickListener {
    private ActionMode.Callback callback;
    private Context context;
    private View customView;
    private boolean finished;
    private WindowManager.LayoutParams mLayoutParams;
    private ActionMenu menu;
    private boolean showed;
    private CharSequence subtitle;
    private CharSequence title;
    private LinearLayout view;

    public ActionModeImpl(ActionMode.Callback callback) {
        this.title = null;
        this.subtitle = null;
        this.customView = null;
        this.view = null;
        this.showed = false;
        this.finished = false;
        this.context = MainService.context;
        this.menu = new ActionMenu(this.context);
        this.view = new android.fix.LinearLayout(this.context);
        this.view.setAlpha(1.0f);
        int color = 0;
        Drawable drawable0 = this.view.getBackground();
        if(drawable0 instanceof ColorDrawable) {
            color = ((ColorDrawable)drawable0).getColor();
        }
        this.view.setBackgroundColor(color | 0xFF000000);
        this.mLayoutParams = new WindowManager.LayoutParams();
        this.mLayoutParams.type = 0x7F6;
        this.mLayoutParams.width = -1;
        this.mLayoutParams.height = -2;
        this.mLayoutParams.format = -2;
        this.mLayoutParams.flags = 0;
        this.mLayoutParams.gravity = 55;
        this.mLayoutParams.y = 0;
        this.mLayoutParams.alpha = 1.0f;
        this.callback = callback;
    }

    @Override  // android.view.ActionMode
    public void finish() {
        if(this.finished) {
            return;
        }
        this.finished = true;
        this.hide();
        this.callback.onDestroyActionMode(this);
    }

    @Override  // android.view.ActionMode
    public View getCustomView() {
        return this.customView;
    }

    @Override  // android.view.ActionMode
    public Menu getMenu() {
        return this.menu;
    }

    @Override  // android.view.ActionMode
    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.context);
    }

    @Override  // android.view.ActionMode
    public CharSequence getSubtitle() {
        return this.subtitle;
    }

    @Override  // android.view.ActionMode
    public CharSequence getTitle() {
        return this.title;
    }

    public void hide() {
        try {
            if(!this.showed) {
                return;
            }
            Tools.removeView(this.view);
        }
        catch(Throwable e) {
            Log.e("removeView failed", e);
        }
        this.showed = false;
    }

    @Override  // android.view.ActionMode
    @TargetApi(16)
    public void invalidate() {
        try {
            this.callback.onPrepareActionMode(this, this.menu);
        }
        finally {
            this.view.removeAllViews();
            int v1 = this.menu.size();
            for(int i = 0; i < v1; ++i) {
                MenuItem menuItem0 = this.menu.getItem(i);
                Button button0 = new Button(this.context);
                button0.setAlpha(1.0f);
                button0.setFocusable(false);
                Drawable drawable0 = menuItem0.getIcon();
                Tools.addIconToTextView(button0, drawable0, 0x20);
                if(drawable0 == null) {
                    button0.setText(menuItem0.getTitle());
                }
                button0.setTag(menuItem0);
                button0.setOnClickListener(this);
                this.view.addView(button0);
            }
            if(this.customView != null) {
                this.view.addView(this.customView);
            }
            this.view.invalidate();
            Log.d("invalidate");
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        MenuItem item = (MenuItem)v.getTag();
        if(item instanceof ActionMenuItem) {
            ((ActionMenuItem)item).invoke();
        }
        this.callback.onActionItemClicked(this, item);
    }

    @Override  // android.view.ActionMode
    public void setCustomView(View view) {
        this.customView = view;
        this.invalidate();
    }

    @Override  // android.view.ActionMode
    public void setSubtitle(int resId) {
        this.subtitle = Re.s(resId);
    }

    @Override  // android.view.ActionMode
    public void setSubtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
    }

    @Override  // android.view.ActionMode
    public void setTitle(int resId) {
        this.title = Re.s(resId);
    }

    @Override  // android.view.ActionMode
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void show() {
        if(this.showed) {
            return;
        }
        try {
            Tools.addView(this.view, this.mLayoutParams);
            this.showed = true;
        }
        catch(Throwable e) {
            Log.e("addView failed", e);
        }
    }
}

