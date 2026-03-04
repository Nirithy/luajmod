package android.fix;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.ext.Log;
import android.ext.Tools;
import android.os.Build.VERSION;
import android.view.ContextThemeWrapper;
import android.view.InflateException;
import android.view.LayoutInflater.Factory2;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class LayoutInflater extends android.view.LayoutInflater {
    private android.view.LayoutInflater base;
    private Context context;
    private static final int[] themes;

    static {
        ArrayList th = new ArrayList();
        th.add(0);
        th.add(1);
        if(Build.VERSION.SDK_INT >= 14) {
            th.add(0x1030128);
        }
        if(Build.VERSION.SDK_INT >= 21) {
            th.add(0x1030224);
        }
        if(Build.VERSION.SDK_INT >= 11) {
            th.add(0x103006B);
        }
        th.add(0x1030005);
        th.add(0x1030008);
        Field[] arr_field = R.class.getFields();
        for(int v = 0; v < arr_field.length; ++v) {
            Field field = arr_field[v];
            int v1 = field.getModifiers();
            if(Modifier.isStatic(v1) && Modifier.isFinal(v1) && field.getName().startsWith("Theme") && field.getType().equals(Integer.TYPE)) {
                try {
                    th.add(field.getInt(null));
                }
                catch(IllegalAccessException | IllegalArgumentException unused_ex) {
                }
            }
        }
        LayoutInflater.themes = new int[th.size()];
        int v3 = th.size();
        for(int i = 0; i < v3; ++i) {
            LayoutInflater.themes[i] = (int)(((Integer)th.get(i)));
        }
    }

    public LayoutInflater(android.view.LayoutInflater parent) {
        super(parent, parent.getContext());
        this.base = parent;
        this.context = parent.getContext();
    }

    private void checkBase(int theme) {
        if(theme != 0 && this.getThemeResId(this.base.getContext()) != theme) {
            this.base = this.base.cloneInContext(this.getContext(theme));
        }
    }

    @Override  // android.view.LayoutInflater
    public android.view.LayoutInflater cloneInContext(Context newContext) {
        return new LayoutInflater(this.base.cloneInContext(newContext));
    }

    @Override
    public boolean equals(Object o) {
        return this.base == null ? super.equals(o) : this.base.equals(o);
    }

    private Context getContext(int themeResId) {
        return themeResId == 1 ? this.context : new ContextThemeWrapper(this.context, themeResId);
    }

    @Override  // android.view.LayoutInflater
    public Context getContext() {
        return this.base == null ? super.getContext() : this.base.getContext();
    }

    @Override  // android.view.LayoutInflater
    public LayoutInflater.Filter getFilter() {
        return this.base == null ? super.getFilter() : this.base.getFilter();
    }

    public static LayoutInflater getInflater() {
        return (LayoutInflater)SystemService.wrap(LayoutInflater.from(Tools.getContext()));
    }

    private int getThemeResId(Context curr) {
        if(curr == this.context) {
            return 1;
        }
        if(curr instanceof ContextThemeWrapper) {
            try {
                Object object0 = ContextThemeWrapper.class.getDeclaredMethod("getThemeResId").invoke(curr);
                return object0 instanceof Integer ? ((int)(((Integer)object0))) : 0;
            }
            catch(Throwable e) {
            }
        }
        else {
            return 0;
        }
        Log.e("getThemeResId failed call", e);
        return 0;
    }

    @Override
    public int hashCode() {
        return this.base == null ? super.hashCode() : this.base.hashCode();
    }

    private View inflate(int resource, XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        if(this.base == null) {
            return parser == null ? super.inflate(resource, root, attachToRoot) : super.inflate(parser, root, attachToRoot);
        }
        try {
            return this.inflateTry(resource, parser, root, attachToRoot);
        }
        catch(InflateException e) {
            if(Build.VERSION.SDK_INT < 14 || !this.isCausedBy(e, "tab_indicator_holo") && !this.isCausedBy(e, "tab_selected_holo")) {
                throw e;
            }
            Log.e("Try fix", e);
            return this.inflateTry(0x7F040027, null, root, attachToRoot);  // layout:tab_indicator_holo
        }
    }

    @Override  // android.view.LayoutInflater
    public View inflate(int resource, ViewGroup root) {
        return root == null ? this.inflate(resource, null, false) : this.inflate(resource, root, true);
    }

    @Override  // android.view.LayoutInflater
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return this.inflate(resource, null, root, attachToRoot);
    }

    @Override  // android.view.LayoutInflater
    public View inflate(XmlPullParser parser, ViewGroup root) {
        return root == null ? this.inflate(parser, null, false) : this.inflate(parser, root, true);
    }

    @Override  // android.view.LayoutInflater
    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        return this.inflate(0, parser, root, attachToRoot);
    }

    public static View inflateStatic(int resource, ViewGroup root) {
        return LayoutInflater.getInflater().inflate(resource, root);
    }

    public static View inflateStatic(int resource, ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.getInflater().inflate(resource, root, attachToRoot);
    }

    public static View inflateStatic(XmlPullParser parser, ViewGroup root) {
        return LayoutInflater.getInflater().inflate(parser, root);
    }

    public static View inflateStatic(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.getInflater().inflate(parser, root, attachToRoot);
    }

    private View inflateTry(int resource, XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        android.view.LayoutInflater prev;
        Throwable ex = null;
        int[] arr_v = LayoutInflater.themes;
        int v1 = 0;
        while(v1 < arr_v.length) {
            int theme = arr_v[v1];
            try {
                prev = this.base;
                this.checkBase(theme);
                return parser == null ? this.base.inflate(resource, root, attachToRoot) : this.base.inflate(parser, root, attachToRoot);
            }
            catch(Exception e) {
                if(ex == null) {
                    ex = e instanceof RuntimeException ? ((RuntimeException)e) : new RuntimeException(e);
                }
                if(!(e instanceof InflateException) && !(e instanceof InvocationTargetException)) {
                    break;
                }
                Log.e(("Exception on inflate with theme: " + theme), e);
                this.base = prev;
                ++v1;
            }
        }
        throw ex;
    }

    private boolean isCausedBy(Throwable e, String check) {
        if(check == null || e == null) {
            return false;
        }
        String s1 = e.getMessage();
        return s1 == null || !s1.contains(check) ? this.isCausedBy(e.getCause(), check) : true;
    }

    @Override  // android.view.LayoutInflater
    public void setFactory(LayoutInflater.Factory factory) {
        if(this.base == null) {
            super.setFactory(factory);
            return;
        }
        this.base.setFactory(factory);
    }

    @Override  // android.view.LayoutInflater
    @TargetApi(11)
    public void setFactory2(LayoutInflater.Factory2 factory) {
        if(this.base == null) {
            super.setFactory2(factory);
            return;
        }
        this.base.setFactory2(factory);
    }

    @Override  // android.view.LayoutInflater
    public void setFilter(LayoutInflater.Filter filter) {
        if(this.base == null) {
            super.setFilter(filter);
            return;
        }
        this.base.setFilter(filter);
    }

    @TargetApi(11)
    public void setPrivateFactory(LayoutInflater.Factory2 factory) {
        android.view.LayoutInflater receiver = this.base == null ? this : this.base;
        try {
            android.view.LayoutInflater.class.getDeclaredMethod("setPrivateFactory", LayoutInflater.Factory2.class).invoke(receiver, factory);
        }
        catch(Throwable e) {
            Log.e("setPrivateFactory failed call", e);
        }
    }

    @Override
    public String toString() {
        return this.base == null ? super.toString() : this.base.toString();
    }
}

