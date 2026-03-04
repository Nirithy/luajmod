package android.ext;

import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.fix.ToolbarButton;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

public abstract class MenuItem implements View.OnClickListener {
    public static final int NO_INDEX = -1;
    public static final String SP_USAGE = "m-";
    public static final int TAB_BITS = 0x40;
    public static final int TAB_COUNT = 3;
    public static final long[] USAGE;
    private int icon;
    private WeakReference iconCache;
    private int index;
    protected String title;

    static {
        MenuItem.USAGE = new long[3];
    }

    public MenuItem(int title) {
        this.icon = 0;
        this.iconCache = new WeakReference(null);
        this.index = -1;
        this.title = Re.s(title);
    }

    public MenuItem(int title, int resId) {
        this.icon = 0;
        this.iconCache = new WeakReference(null);
        this.index = -1;
        this.title = Re.s(title);
        this.icon = resId;
    }

    public MenuItem(String title) {
        this.icon = 0;
        this.iconCache = new WeakReference(null);
        this.index = -1;
        this.title = title;
    }

    public MenuItem(String title, int resId) {
        this.icon = 0;
        this.iconCache = new WeakReference(null);
        this.index = -1;
        this.title = title;
        this.icon = resId;
    }

    public final ImageView getButton() {
        return this.getButton(false);
    }

    public ImageView getButton(boolean fromUI) {
        ImageView imageView0 = new ToolbarButton(Tools.getContext());
        ((ToolbarButton)imageView0).setIcon(this.icon);
        MenuItem listener = fromUI ? (View v) -> {
            int index = MenuItem.this.index;
            if(index != -1 && index / 0x40 >= 0 && index / 0x40 < 3) {
                long bit = 1L << index % 0x40;
                if((MenuItem.USAGE[index / 0x40] & bit) == 0L) {
                    MenuItem.USAGE[index / 0x40] |= bit;
                    MenuItem.saveUsage();
                }
            }
            MenuItem.this.onClick(v);
        } : this;
        ((ToolbarButton)imageView0).setOnClickListener(listener);
        ((ToolbarButton)imageView0).setTag(this);
        return imageView0;

        class android.ext.MenuItem.1 implements View.OnClickListener {
            @Override  // android.view.View$OnClickListener
            public void onClick(View v) {
                MenuItem.this.onClickFromUI(v);
            }
        }

    }

    public Drawable getDrawable() {
        if(this.icon == 0) {
            return null;
        }
        Drawable cache = (Drawable)this.iconCache.get();
        if(cache == null) {
            cache = Tools.getDrawable(this.icon);
            this.iconCache = new WeakReference(cache);
            return cache;
        }
        return cache;
    }

    public static String getUsage() {
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        SharedPreferences.Editor sharedPreferences$Editor0 = sharedPreferences0.edit();
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < 3; ++i) {
            long v1 = sharedPreferences0.getLong("m-" + i, 0L);
            sharedPreferences$Editor0.remove("m-" + i);
            if(v1 != 0L) {
                out.append("&m");
                out.append(i);
                out.append('=');
                out.append(Long.toHexString(v1));
            }
        }
        sharedPreferences$Editor0.commit();
        return out.toString();
    }

    @Override  // android.view.View$OnClickListener
    public abstract void onClick(View arg1);

    // 检测为 Lambda 实现
    public void onClickFromUI(View v) [...]

    private static void saveUsage() {
        SPEditor ed = new SPEditor();
        for(int i = 0; i < 3; ++i) {
            ed.putLong("m-" + i, MenuItem.USAGE[i], 0L);
        }
        ed.commit();
    }

    public void setIndex(int tab, int offset) {
        this.index = tab * 0x40 + offset;
    }

    @Override
    public String toString() {
        return this.title;
    }
}

