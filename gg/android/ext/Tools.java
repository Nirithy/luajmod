package android.ext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.fix.FrameLayout;
import android.fix.LayoutInflater;
import android.fix.ScrollView;
import android.fix.ViewHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.lang.ProcessBuilder;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.sup.ArrayListResults;
import android.sup.ContainerHelpers;
import android.sup.LongSparseArrayChecked;
import android.system.Os;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.toast.Toaster;
import android.util.StateSet;
import android.view.ContextThemeWrapper;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ggdqo.R.raw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jeb.synthetic.FIN;

public class Tools {
    static class Colorize {
        public final int color;
        public final CharSequence word;

        public Colorize(CharSequence word, int color) {
            this.word = word;
            this.color = color;
        }
    }

    static class FocusChangeListener implements View.OnFocusChangeListener {
        private final ArrayList list;

        private FocusChangeListener() {
            this.list = new ArrayList();
        }

        FocusChangeListener(FocusChangeListener tools$FocusChangeListener0) {
        }

        void add(View.OnFocusChangeListener l) {
            if(l == null || this.list.contains(l)) {
                return;
            }
            this.list.add(l);
        }

        @Override  // android.view.View$OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            for(Object object0: this.list) {
                ((View.OnFocusChangeListener)object0).onFocusChange(v, hasFocus);
            }
        }
    }

    public static class HandlerWrapper extends Handler {
        final Handler base;

        public HandlerWrapper(Handler base) {
            super(base.getLooper());
            this.base = base;
        }

        @Override  // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                this.base.handleMessage(msg);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }

        public static Handler wrap(Handler base) {
            return !(base instanceof HandlerWrapper) ? new HandlerWrapper(base) : base;
        }
    }

    static class MethodDescr {
        Class cl;
        Class[] defs;
        String meth;
        Method method;
        Object obj;
        Object[] params;

        public MethodDescr(Class class0, String meth, Class[] defs, Object obj, Object[] params) {
            this.cl = class0;
            this.meth = meth;
            this.defs = defs;
            this.obj = obj;
            this.params = params;
            this.method = null;
        }

        public MethodDescr check() throws NoSuchMethodException, SecurityException {
            this.method = this.cl.getMethod(this.meth, this.defs);
            return this;
        }

        public Object invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return this.method.invoke(this.obj, this.params);
        }
    }

    public static class RunnableWrapper implements Runnable {
        final Runnable base;

        public RunnableWrapper(Runnable base) {
            this.base = base;
        }

        @Override
        public void run() {
            try {
                this.base.run();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }

        public static Runnable wrap(Runnable base) {
            return !(base instanceof RunnableWrapper) ? new RunnableWrapper(base) : base;
        }
    }

    public static class ScrollPos {
        public int offset;
        public int top;

        public ScrollPos(int top, int offset) {
            this.top = top;
            this.offset = offset;
        }
    }

    static class Semaphore {
        public static final int S0 = 1;
        public static final int S1 = 2;
        public static final int S2 = 4;
        public static final int S3 = 8;
        public static final int S4 = 16;
        public static final int S5 = 0x20;
        public static final int S6 = 0x40;
        public static final int S7 = 0x80;
        public static final int S8 = 0x100;
        public static final int S9 = 0x200;
        private static final ThreadLocal store;

        static {
            Semaphore.store = new ThreadLocal() {
                protected Integer initialValue() {
                    return 0;
                }

                @Override
                protected Object initialValue() {
                    return this.initialValue();
                }
            };
        }

        private Semaphore() {
        }

        Semaphore(Semaphore tools$Semaphore0) {
        }

        public void free(int bit) {
            Integer integer0 = (int)(((int)(((Integer)Semaphore.store.get()))) & ~bit);
            Semaphore.store.set(integer0);
        }

        public boolean use(int bit) {
            int v1 = (int)(((Integer)Semaphore.store.get()));
            if((v1 & bit) == 0) {
                Semaphore.store.set(((int)(v1 | bit)));
                return true;
            }
            return false;
        }
    }

    public static final class ViewWrapper extends FrameLayout {
        public ViewWrapper(Context context) {
            super(context);
        }
    }

    public static final int AID_USER = 100000;
    public static final int CLEAR = 0;
    public static final int COLORIZE = 4;
    public static final int DISABLE_COPY = 2;
    public static final char ERR_MARK = '⁣';
    private static final String HIDDEN_PREFIX = "GG-";
    private static final int ICON_END = 4;
    private static final int ICON_LEFT = 1;
    private static final int ICON_RIGHT = 2;
    private static final int ICON_START = 3;
    private static final int ICON_UNKNOWN = 0;
    public static final int INVERT = 2;
    public static final Random RANDOM = null;
    public static final int SCROLL_TO_END = 1;
    public static final int SET = 1;
    private static int allowFloatWindows;
    private static String cacheDir;
    private static volatile String dataDir;
    private static float density;
    private static volatile int dp2px48;
    static volatile int dp5;
    static volatile String[] fakeToReal;
    private static String filesDir;
    private static int fixToLegacy;
    private static String hiddenDir;
    private static volatile Object iPm;
    static InputMethodManager imm;
    private static volatile int isLandscape;
    private static volatile boolean overrideExec;
    private static volatile byte overrideNative;
    private static Pattern p;
    private static String pkg;
    private static volatile PackageManager pm;
    private static final Semaphore sePM;
    private static int[] unitTime;
    private static volatile WeakReference weakColorize;
    private static WeakReference weakSp;

    static {
        Tools.RANDOM = new Random(System.currentTimeMillis());
        Tools.allowFloatWindows = Build.VERSION.SDK_INT < 23 ? 1 : -1;
        Tools.density = 0.0f;
        Tools.dp2px48 = -1;
        Tools.dp5 = -1;
        Tools.p = Pattern.compile("(\\d+)");
        Tools.pkg = null;
        Tools.filesDir = null;
        Tools.cacheDir = null;
        Tools.pm = null;
        Tools.dataDir = null;
        Tools.hiddenDir = null;
        Tools.sePM = new Semaphore(null);
        Tools.unitTime = new int[]{0x7F070176, 60, 0x7F070175, 60, 0x7F070174, 24, 0x7F070173, 365, 0x7F070172, -1};  // string:unit_seconds_short "sec"
        Tools.iPm = null;
        Tools.weakColorize = new WeakReference(null);
        Tools.fixToLegacy = 0;
        Tools.weakSp = new WeakReference(null);
        Tools.overrideExec = false;
        Tools.overrideNative = 0;
        Tools.fakeToReal = null;
        Tools.isLandscape = -1;
    }

    @SuppressLint({"SdCardPath"})
    private static void _init(Context context) {
        if(Tools.pm == null) {
            try {
                Tools.pm = context.getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(Tools.pkg == null) {
            Tools.pkg = "com.ggdqo";
        }
        if(Tools.pkg == null) {
            Tools.pkg = Apk.PACKAGE;
        }
        if(Tools.filesDir == null) {
            try {
                Tools.filesDir = context.getFilesDir().getAbsolutePath();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(Tools.cacheDir == null) {
            try {
                Tools.cacheDir = context.getCacheDir().getAbsolutePath();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(Tools.filesDir == null && Tools.pkg != null) {
            Tools.filesDir = Tools.getDataDirSafe() + "/files";
        }
        if(Tools.cacheDir == null && Tools.pkg != null) {
            Tools.cacheDir = Tools.getDataDirSafe() + "/cache";
        }
        if(Tools.filesDir != null) {
            try {
                File file0 = new File(Tools.filesDir);
                if(!file0.exists()) {
                    file0.mkdirs();
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(Tools.cacheDir == null) {
            return;
        }
        else {
            try {
                File file1 = new File(Tools.cacheDir);
                if(!file1.exists()) {
                    file1.mkdirs();
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        Log.badImplementation(e);
    }

    public static void addIconToTextView(TextView tv, Drawable icon, int iconSize) {
        if(tv == null) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = 2;
                if(Tools.dp5 == -1) {
                    Tools.dp5 = (int)(Tools.dp2px(12.0f) + 0.5f);
                }
                if(icon != null) {
                    int is = (int)Tools.dp2px(iconSize);
                    try {
                        icon.setBounds(0, 0, is, is);
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }
                try {
                    Drawable[] old = null;
                    Object object0 = tv.getTag(0x7F0B0050);  // id:icon
                    int own = object0 instanceof Integer ? ((int)(((Integer)object0))) : 0;
                    if(own == 0) {
                        Drawable[] arr_drawable1 = tv.getCompoundDrawables();
                        old = arr_drawable1;
                        if(Build.VERSION.SDK_INT >= 17) {
                            Drawable[] arr_drawable2 = tv.getCompoundDrawablesRelative();
                            if(arr_drawable2[0] != null) {
                                own = 4;
                                old = arr_drawable2;
                            }
                            else if(arr_drawable1[0] != null) {
                                own = 2;
                            }
                            else if(Build.VERSION.SDK_INT < 18) {
                                own = 1;
                            }
                            else {
                                own = 3;
                                old = arr_drawable2;
                            }
                        }
                        else {
                            own = arr_drawable1[0] == null ? 1 : 2;
                        }
                        tv.setTag(0x7F0B0050, own);  // id:icon
                    }
                    boolean abs = own == 1 || own == 2;
                    if(old == null) {
                        old = abs ? tv.getCompoundDrawables() : tv.getCompoundDrawablesRelative();
                    }
                    if(own == 1 || own == 3) {
                        v = 0;
                    }
                    old[v] = icon;
                    if(abs) {
                        tv.setCompoundDrawables(old[0], old[1], old[2], old[3]);
                    }
                    else {
                        tv.setCompoundDrawablesRelative(old[0], old[1], old[2], old[3]);
                    }
                }
                catch(StringIndexOutOfBoundsException e) {
                    Log.badImplementation(e);
                }
                tv.setCompoundDrawablePadding(Tools.dp5);
            }
        });
    }

    public static void addIconsToListViewItems(ListView listView, CharSequence[] list, Drawable[] icons, int iconSize, int textResId) {
        try {
            listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override  // android.view.ViewGroup$OnHierarchyChangeListener
                public void onChildViewAdded(View parent, View child) {
                    try {
                        View view2 = child.findViewById(0x1020014);
                        if(view2 instanceof TextView) {
                            if(textResId != 0) {
                                Tools.setTextAppearance(((TextView)view2), textResId);
                            }
                            if(((TextView)view2).getTag() == null) {
                                android.ext.Tools.9.1 tools$9$10 = new TextWatcher() {
                                    @Override  // android.text.TextWatcher
                                    public void afterTextChanged(Editable s) {
                                        this.onTextChanged(s, 0, 0, 0);
                                    }

                                    @Override  // android.text.TextWatcher
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override  // android.text.TextWatcher
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        try {
                                            String s = s.toString();
                                            for(int i = 0; true; ++i) {
                                                if(i >= this.val$list.length) {
                                                    return;
                                                }
                                                if(this.val$list[i].toString().equals(s)) {
                                                    Tools.addIconToTextView(((TextView)view2), this.val$icons[i], this.val$iconSize);
                                                    return;
                                                }
                                            }
                                        }
                                        catch(Throwable e) {
                                            Log.badImplementation(e);
                                        }
                                    }
                                };
                                ((TextView)view2).addTextChangedListener(tools$9$10);
                                ((TextView)view2).setTag(tools$9$10);
                                tools$9$10.onTextChanged(((TextView)view2).getText(), 0, 0, 0);
                            }
                        }
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }

                @Override  // android.view.ViewGroup$OnHierarchyChangeListener
                public void onChildViewRemoved(View parent, View child) {
                }
            });
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static void addView(View view, WindowManager.LayoutParams params) {
        Log.d(("dbg2: " + view + ' ' + params));
        Tools.setView(view, params, true);
    }

    public static void addViewWithWrapper(View view, WindowManager.LayoutParams params) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.removeViewWithWrapper(view);
                ViewWrapper tools$ViewWrapper0 = new ViewWrapper(view.getContext());
                tools$ViewWrapper0.addView(view);
                Tools.addView(tools$ViewWrapper0, params);
            }
        });
    }

    public static void alertBigText(String bigText, int flags) {
        Tools.alertBigText(bigText, flags, null, null);
    }

    public static void alertBigText(String bigText, int flags, String action, DialogInterface.OnClickListener listener) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view0 = LayoutInflater.inflateStatic(0x7F040008, null);  // layout:logcat
                TextView text = (TextView)view0.findViewById(0x7F0B002F);  // id:logcat_text
                text.setText(bigText);
                if((flags & 4) != 0) {
                    int v = bigText.indexOf(0x2063);
                    if(v >= 0) {
                        SpannableString spannableString0 = new SpannableString(bigText);
                        spannableString0.setSpan(new BackgroundColorSpan(0xFF880000), v, v + 4, 33);
                        text.setText(spannableString0);
                    }
                }
                ScrollView view = (ScrollView)view0.findViewById(0x7F0B002E);  // id:logcat_page
                view.setFastScrollEnabled(true);
                AlertDialog.Builder alertDialog$Builder0 = Alert.create().setPositiveButton(Re.s(0x7F07009D), null).setView(view0);  // string:ok "OK"
                if((flags & 2) == 0) {
                    alertDialog$Builder0.setNegativeButton(0x7F070161, new DialogInterface.OnClickListener() {  // string:copy "Copy"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Tools.copyText(text.getText().toString().trim(), false);
                        }
                    });
                }
                if(action != null) {
                    alertDialog$Builder0.setNeutralButton(action, listener);
                }
                AlertDialog alertDialog0 = alertDialog$Builder0.create();
                if((flags & 1) != 0) {
                    Alert.setOnShowListener(alertDialog0, new DialogInterface.OnShowListener() {
                        @Override  // android.content.DialogInterface$OnShowListener
                        public void onShow(DialogInterface dialog) {
                            android.ext.Tools.22.2.1 tools$22$2$10 = new Runnable() {
                                @Override
                                public void run() {
                                    this.val$view.fullScroll(130);
                                }
                            };
                            view.postDelayed(tools$22$2$10, 100L);
                        }
                    });
                }
                Alert.show(alertDialog0);
                Window window0 = alertDialog0.getWindow();
                WindowManager.LayoutParams windowManager$LayoutParams0 = window0.getAttributes();
                windowManager$LayoutParams0.width = -1;
                window0.setAttributes(windowManager$LayoutParams0);
            }
        });
    }

    public static boolean allowExecute(String filename) {
        File file = new File(filename);
        if(!file.canExecute()) {
            String[] arr_s = {"chmod", "0755", Tools.getRealPath(filename)};
            try {
                if(!Tools.waitForTimeout(Tools.exec(arr_s), 15)) {
                    Log.w(("timeout fail 1: " + Arrays.toString(arr_s)), new RuntimeException());
                }
            }
            catch(Exception e) {
                Log.w("exec", e);
            }
            if(!file.canExecute()) {
                try {
                    if(!Tools.waitForTimeout(RootDetector.tryRoot(("exec " + Tools.concat(arr_s, " "))), 15)) {
                        Log.w(("timeout fail 2: " + Arrays.toString(arr_s)), new RuntimeException());
                        return file.canExecute();
                    }
                }
                catch(Exception e) {
                    Log.w("root", e);
                    return file.canExecute();
                }
                return file.canExecute();
            }
        }
        return true;
    }

    public static boolean[] booleanArray(List list0) {
        boolean[] ret = new boolean[list0.size()];
        int i = 0;
        for(Object object0: list0) {
            ret[i] = ((Boolean)object0).booleanValue();
            ++i;
        }
        return ret;
    }

    public static final String byteArrayToHexString(byte[] arr) {
        StringBuilder hexString = new StringBuilder();
        for(int v = 0; v < arr.length; ++v) {
            String h;
            for(h = Integer.toHexString(arr[v] & 0xFF).toUpperCase(Locale.US); h.length() < 2; h = "0" + h) {
            }
            hexString.append(h);
        }
        return hexString.toString();
    }

    public static boolean canDrawOverlays(Context context) {
        if(Build.VERSION.SDK_INT >= 23) {
            try {
                return Settings.canDrawOverlays(context);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        return true;
    }

    public static void changeSelection(Object obj, int mode) {
        boolean z2;
        boolean z1;
        boolean z;
        if(obj != null) {
            if(obj instanceof ArrayListResults) {
                int v1 = ((ArrayListResults)obj).size();
                for(int i = 0; i < v1; ++i) {
                    if(mode != 2) {
                        z = mode == 1;
                    }
                    else if(((ArrayListResults)obj).checked(i)) {
                        z = false;
                    }
                    else {
                        z = true;
                    }
                    ((ArrayListResults)obj).checked(i, z);
                }
                return;
            }
            if(obj instanceof LongSparseArrayChecked) {
                int v3 = ((LongSparseArrayChecked)obj).size();
                for(int i = 0; i < v3; ++i) {
                    if(mode != 2) {
                        z1 = mode == 1;
                    }
                    else if(((LongSparseArrayChecked)obj).checkAt(i)) {
                        z1 = false;
                    }
                    else {
                        z1 = true;
                    }
                    ((LongSparseArrayChecked)obj).setCheckAt(i, z1);
                }
                return;
            }
            if(obj instanceof boolean[]) {
                int s = ((boolean[])obj).length - 1;
                for(int i = 1; true; ++i) {
                    if(i >= s) {
                        return;
                    }
                    if(mode != 2) {
                        z2 = mode == 1;
                    }
                    else if(((boolean[])obj)[i]) {
                        z2 = false;
                    }
                    else {
                        z2 = true;
                    }
                    ((boolean[])obj)[i] = z2;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static void checkForBadContext() {
        new DaemonThread(new Runnable() {
            @Override
            @TargetApi(19)
            public void run() {
                RuntimeException check;
                ArrayList list = new ArrayList();
                StringBuilder report = new StringBuilder();
                report.append("Context info. act - ");
                report.append("Context: null");
                report.append("; app - ");
                report.append("Context: null");
                report.append("; dae - ");
                report.append("Context: null");
                report.append("; bad - ");
                int bad = 0;
                Throwable check = Tools.isGoodContext(BaseActivity.context);
                if(check != null) {
                    bad = 1;
                    list.add(check);
                }
                Throwable check = Tools.isGoodContext(BaseActivity.appContext);
                if(check != null) {
                    bad += 10;
                    list.add(check);
                }
                if(MainService.context != null) {
                    Throwable check = Tools.isGoodContext(MainService.context);
                    if(check != null) {
                        bad += 100;
                        list.add(check);
                    }
                }
                report.append(bad);
                String msg = report.toString();
                if(bad > 0) {
                    if(Build.VERSION.SDK_INT < 19) {
                        for(Object object0: list) {
                            msg = msg + "\nSupressed: " + Log.getStackTraceString(((Throwable)object0));
                        }
                        check = new RuntimeException(msg);
                    }
                    else {
                        check = new RuntimeException(msg);
                        for(Object object1: list) {
                            check.addSuppressed(((Throwable)object1));
                        }
                    }
                    ExceptionHandler.sendException(Thread.currentThread(), check, false);
                }
                Log.d(msg);
            }
        }, "checkForBadContext").start();
    }

    @TargetApi(23)
    static void checkPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            try {
                if(!Tools.canDrawOverlays(Tools.getContext())) {
                    Log.w("canDrawOverlays = false");
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        else {
            return;
        }
        Log.badImplementation(e);
    }

    public static void chmod(File path, int mode) {
        boolean z = true;
        path.setExecutable((mode & 0x40) != 0, (mode & 9) == 0);
        path.setWritable((mode & 0x80) != 0, (mode & 18) == 0);
        if((mode & 36) != 0) {
            z = false;
        }
        path.setReadable((mode & 0x100) != 0, z);
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                Os.chmod(path.getAbsolutePath(), mode);
            }
            catch(Throwable e) {
                Log.w(("Failed chmod " + Integer.toOctalString(mode) + " \'" + path + "\'"), e);
            }
        }
    }

    public static void chmod(String path, String mode) {
        try {
            if(Build.VERSION.SDK_INT >= 21) {
                Os.chmod(path, Integer.parseInt(mode, 8));
            }
            else if(Build.VERSION.SDK_INT >= 12) {
                Object object0 = Class.forName("libcore.io.Libcore").getField("os").get(null);
                Class.forName("libcore.io.Os").getMethod("chmod", String.class, Integer.TYPE).invoke(object0, path, Integer.parseInt(mode, 8));
            }
        }
        catch(Throwable e) {
            Log.w(("Failed chmod " + mode + " \'" + path + "\'"), e);
        }
        String[] arr_s = {"chmod", mode, Tools.getRealPath(path)};
        try {
            if(!Tools.waitForTimeout(Tools.exec(arr_s), 15)) {
                Log.w(("timeout fail 1: " + Arrays.toString(arr_s)), new RuntimeException());
            }
        }
        catch(Exception e) {
            Log.w("exec", e);
        }
        try {
            if(!Tools.waitForTimeout(RootDetector.tryRoot(("exec " + Tools.concat(arr_s, " "))), 15)) {
                Log.w(("timeout fail 2: " + Arrays.toString(arr_s)), new RuntimeException());
            }
        }
        catch(Exception e) {
            Log.w("root", e);
        }
    }

    public static boolean click(View v) {
        if(v == null) {
            return false;
        }
        return Build.VERSION.SDK_INT >= 15 ? v.callOnClick() : v.performClick();
    }

    public static CharSequence colorize(CharSequence text, int color) {
        Spannable sp;
        try {
            if(text instanceof Spannable) {
                sp = (Spannable)text;
            }
            else {
                sp = new SpannableString(text);
                if(!(sp instanceof CharSequence)) {
                    Log.badImplementation(new RuntimeException("Class \'android.text.SpannableString\' does not implement interface \'java.lang.CharSequence\'"));
                    return text;
                }
            }
            sp.setSpan(new ForegroundColorSpan(color), 0, text.length(), 33);
            return sp;
        }
        catch(Throwable e) {
            Log.w("Failed colorize", e);
            return text;
        }
    }

    public static CharSequence colorizeHelp(CharSequence help) {
        if(help instanceof Spannable) {
            Colorize[] colorize = (Colorize[])Tools.weakColorize.get();
            if(colorize == null) {
                colorize = new Colorize[]{new Colorize(AddressItem.getNameShort(1), AddressItem.getColor(1)), new Colorize(AddressItem.getNameShort(2), AddressItem.getColor(2)), new Colorize(AddressItem.getNameShort(4), AddressItem.getColor(4)), new Colorize(AddressItem.getNameShort(8), AddressItem.getColor(8)), new Colorize(AddressItem.getNameShort(16), AddressItem.getColor(16)), new Colorize(AddressItem.getNameShort(0x20), AddressItem.getColor(0x20)), new Colorize(AddressItem.getNameShort(0x40), AddressItem.getColor(0x40)), new Colorize(Re.s(0x7F070242), Tools.getColor(0x7F0A000E)), new Colorize(Re.s(0x7F070243), Tools.getColor(0x7F0A000F)), new Colorize(Re.s(0x7F070244), Tools.getColor(0x7F0A0010)), new Colorize(Re.s(0x7F070245), Tools.getColor(0x7F0A000D))};  // string:executable "executable"
                Tools.weakColorize = new WeakReference(colorize);
            }
            for(int i = 0; i < colorize.length; ++i) {
                Tools.colorizeText(help, colorize[i].word, colorize[i].color);
            }
        }
        return help;
    }

    public static CharSequence colorizeText(CharSequence haystack, CharSequence needle, int color) {
        try {
            if(haystack instanceof Spannable) {
                int v1 = needle.length();
                int offset = 0;
                String s = haystack.toString();
                String s1 = needle.toString();
                int v3;
                while((v3 = s.indexOf(s1, offset)) >= 0) {
                    offset = v3 + v1;
                    ((Spannable)haystack).setSpan(new ForegroundColorSpan(color), v3, v3 + v1, 33);
                }
            }
        }
        catch(Throwable e) {
            Log.w("Failed colorize text", e);
        }
        return haystack;
    }

    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];
        int j = 0;
        for(int i = 1; i < pattern.length; ++i) {
            while(j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if(pattern[j] == pattern[i]) {
                ++j;
            }
            failure[i] = j;
        }
        return failure;
    }

    public static CharSequence concat(CharSequence[] text) {
        try {
            return TextUtils.concat(text);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            StringBuilder sb = new StringBuilder();
            for(int v = 0; v < text.length; ++v) {
                sb.append(text[v]);
            }
            return sb.toString();
        }
    }

    public static String concat(String[] parts, String delimiter) {
        StringBuilder out = new StringBuilder();
        for(int v = 0; v < parts.length; ++v) {
            String part = parts[v];
            if(out.length() != 0) {
                out.append(delimiter);
            }
            out.append(part);
        }
        return out.toString();
    }

    public static void copy(String src, String dst) throws IOException {
        try(FileInputStream in = new FileInputStream(src)) {
            FileOutputStream out = new FileOutputStream(dst);
            int v = FIN.finallyOpen$NT();
            byte[] buf = new byte[0x2000];
            while(true) {
                int v1 = in.read(buf);
                if(v1 <= 0) {
                    FIN.finallyCodeBegin$NT(v);
                    out.close();
                    FIN.finallyCodeEnd$NT(v);
                    return;
                }
                out.write(buf, 0, v1);
            }
        }
    }

    public static void copyText(String text) {
        Tools.copyText(text, true);
    }

    @TargetApi(11)
    public static void copyText(String text_, boolean withHistory) {
        if(text_ == null) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = text_.replace(" ", " ");
                if(withHistory) {
                    History.add(s, 1);
                }
                try {
                    Object object0 = MainService.context.getSystemService("clipboard");
                    if(Build.VERSION.SDK_INT < 11) {
                        ((ClipboardManager)object0).setText(s);
                    }
                    else {
                        ((android.content.ClipboardManager)object0).setPrimaryClip(ClipData.newPlainText(s, s));
                    }
                    Tools.showToast(0x7F0700FD);  // string:copied "Copied"
                }
                catch(Throwable e) {
                    Log.e("Failed to copy text to the clipboard", e);
                    Log.d(("Text: " + s));
                    Tools.showToast((Re.s(0x7F0700FC) + ' ' + e.getMessage()));  // string:failed_copy_text "Failed to copy text to the clipboard:"
                }
            }
        });
    }

    public static void describeActivities(String packageName, StringBuilder out) {
        PackageInfo pInfo;
        StringBuilder msg = out == null ? new StringBuilder() : out;
        msg.append("\nActivities:\n");
        if(out == null) {
            Log.d(msg.toString().trim());
            msg = new StringBuilder();
        }
        try {
            pInfo = null;
            pInfo = Tools.getPackageInfo(packageName, 1);
        }
        catch(Throwable unused_ex) {
        }
        if(pInfo == null) {
            msg.append("Null for: " + packageName);
            if(out == null) {
                Log.d(msg.toString().trim());
                new StringBuilder();
            }
        }
        else if(pInfo.activities == null) {
            msg.append("Null");
            if(out == null) {
                Log.d(msg.toString().trim());
                new StringBuilder();
            }
        }
        else {
            ActivityInfo[] arr_activityInfo = pInfo.activities;
            int v = 0;
            while(v < arr_activityInfo.length) {
                ActivityInfo ai = arr_activityInfo[v];
                try {
                    msg.append(ai);
                    if(ai != null) {
                        msg.append('\n');
                        Field[] arr_field = ActivityInfo.class.getFields();
                        int v1 = arr_field.length;
                    label_29:
                        for(int v2 = 0; v2 < v1; ++v2) {
                            Field f = arr_field[v2];
                            int v3 = f.getModifiers();
                            if(Modifier.isPublic(v3) && !Modifier.isStatic(v3)) {
                                msg.append(f.getName());
                                msg.append(": ");
                                try {
                                    msg.append(f.get(ai));
                                }
                                catch(Throwable e) {
                                    msg.append(e);
                                }
                                msg.append("; ");
                            }
                        }
                    }
                }
                catch(Throwable e) {
                    msg.append(e);
                    if(true) {
                        goto label_45;
                    }
                    goto label_29;
                }
                try {
                label_45:
                    ComponentName componentName0 = new ComponentName(packageName, ai.name);
                    msg.append("enabled: ");
                    msg.append(Tools.getPackageManager().getComponentEnabledSetting(componentName0));
                    msg.append("; ");
                }
                catch(Throwable e) {
                    msg.append(e);
                }
                msg.append('\n');
                if(out == null) {
                    Log.d(msg.toString().trim());
                    msg = new StringBuilder();
                }
                ++v;
            }
        }
    }

    private static void disableSoundEffects(ViewGroup group) {
        if(group != null) {
            int i = 0;
            while(i < group.getChildCount()) {
                try {
                    View view0 = group.getChildAt(i);
                    if(view0 != null) {
                        view0.setSoundEffectsEnabled(false);
                        if(view0 instanceof ViewGroup) {
                            Tools.disableSoundEffects(((ViewGroup)view0));
                        }
                    }
                    ++i;
                    continue;
                }
                catch(ArrayIndexOutOfBoundsException unused_ex) {
                }
                ++i;
            }
        }
    }

    public static void dismiss(DialogInterface dialog) {
        if(dialog == null) {
            throw new NullPointerException();
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog.dismiss();
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
        });
    }

    public static void dismiss(WeakReference weakReference0) {
        if(weakReference0 != null) {
            AlertDialog dialog = (AlertDialog)weakReference0.get();
            if(dialog != null) {
                Tools.dismiss(dialog);
            }
        }
    }

    public static String doubleToTime(double t) {
        long show;
        StringBuilder out = new StringBuilder();
        long time = Math.abs(((long)t));
        for(int i = 0; i < Tools.unitTime.length; i += 2) {
            int size = Tools.unitTime[i + 1];
            if(size <= 0) {
                show = time;
                time = 0L;
            }
            else {
                show = time % ((long)size);
                time /= (long)size;
            }
            if(show >= 0L) {
                if(out.length() > 0) {
                    out.insert(0, ":");
                }
                if(i == 0) {
                    out.insert(0, Tools.stringFormat("%f", new Object[]{((double)(Math.abs(t) % ((double)size)))}).replaceFirst("\\D?0+$", ""));
                }
                else {
                    out.insert(0, show);
                }
            }
            if(time == 0L) {
                break;
            }
        }
        if(out.length() == 0) {
            out.insert(0, "0");
        }
        if(t < 0.0) {
            out.insert(0, "-");
        }
        return out.toString();
    }

    public static float dp2px(float dp) {
        float dens = Tools.density;
        if(dens == 0.0f) {
            Context context0 = Tools.getContext();
            if(context0 != null) {
                dens = context0.getResources().getDisplayMetrics().density;
            }
        }
        if(dens == 0.0f) {
            dens = 1.0f;
        }
        return dp * dens;
    }

    public static int dp2px48() {
        if(Tools.dp2px48 < 0) {
            Tools.dp2px48 = (int)Tools.dp2px(48.0f);
        }
        return Tools.dp2px48;
    }

    private static void dump(String indent, View view) {
        Log.d((String.valueOf(indent) + view));
        if(view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            int i = 0;
            while(i < group.getChildCount()) {
                try {
                    View view1 = group.getChildAt(i);
                    if(view1 != null) {
                        Tools.dump((indent + "  "), view1);
                    }
                    ++i;
                    continue;
                }
                catch(ArrayIndexOutOfBoundsException unused_ex) {
                }
                ++i;
            }
        }
    }

    public static void dumpHierarchy(View view) {
        Tools.dump("", view);
    }

    public static Process exec(String[] cmdarray) throws IOException {
        return Tools.exec(cmdarray, null, null);
    }

    public static Process exec(String[] cmdarray, String[] envp) throws IOException {
        return Tools.exec(cmdarray, envp, null);
    }

    public static Process exec(String[] cmdarray, String[] envp, File dir) throws IOException {
        if(!Tools.overrideExec) {
            try {
                return Runtime.getRuntime().exec(cmdarray, envp, dir);
            }
            catch(Throwable e) {
                String s = e.getMessage();
                if(!s.contains("nknown error") && !s.contains("xec failed") && !s.contains("ead failed")) {
                    throw e;
                }
                Log.w(("Failed exec: " + Arrays.toString(cmdarray) + "; " + envp + "; " + dir), e);
            }
        }
        Process process0 = ProcessBuilder.exec(cmdarray, envp, dir);
        Tools.overrideExec = true;
        return process0;
    }

    public static void externalKeyboard(Window window, View v, boolean show) {
        InputMethodManager imm = (InputMethodManager)Tools.getContext().getSystemService("input_method");
        int mode = window.getAttributes().softInputMode & -16;
        if(show) {
            window.clearFlags(0x20000);
            window.setSoftInputMode(mode | 5);
            imm.showSoftInput(v, 0);
            return;
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        window.addFlags(0x20000);
        window.setSoftInputMode(mode | 2);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void extractFile(String fileId, File output) {
        Resources resources0 = Tools.getContext().getResources();
        int v = Re.i(("chunk" + fileId), raw.class);
        if(v == 0) {
            Log.d(("Nothing extract file " + fileId + ' ' + output.getAbsolutePath()));
            throw new RuntimeException("Nothing extract");
        }
        try {
            byte[] buf = new byte[0x2000];
            FileOutputStream os = new FileOutputStream(output);
            InputStream inputStream0 = resources0.openRawResource(v);
            while(true) {
                int read = inputStream0.read(buf);
                if(read <= 0) {
                    inputStream0.close();
                    os.close();
                    return;
                }
                os.write(buf, 0, read);
            }
        }
        catch(IOException e) {
            Log.d(("Failed extract file " + fileId + ' ' + output.getAbsolutePath()), e);
            throw new RuntimeException(e);
        }
    }

    public static void fixDialogButtonsSize(AlertDialog dialog) {
        boolean changed;
        if(dialog != null) {
            Button[] arr_button = {dialog.getButton(-1), dialog.getButton(-2), dialog.getButton(-3)};
            ViewGroup parent = null;
            for(int v = 0; v < 3; ++v) {
                Button btn = arr_button[v];
                if(btn != null) {
                    parent = (ViewGroup)btn.getParent();
                    if(parent == null) {
                        continue;
                    }
                    break;
                }
            }
            if(parent != null) {
                int max = parent.getWidth();
                if(max == 0) {
                    parent.requestLayout();
                    max = parent.getWidth();
                    if(max == 0) {
                        max = parent.getMeasuredWidth();
                    }
                    if(max == 0) {
                        parent.measure(-1, -2);
                        max = parent.getMeasuredWidth();
                    }
                }
                if(max != 0 && !Tools.goodSize(max, parent, 0)) {
                    for(int v2 = 0; v2 < 3; ++v2) {
                        Button btn = arr_button[v2];
                        if(btn != null && Build.VERSION.SDK_INT >= 14) {
                            btn.setAllCaps(false);
                        }
                    }
                    if(!Tools.goodSize(max, parent, 5)) {
                        for(int v3 = 0; v3 < 3; ++v3) {
                            Button btn = arr_button[v3];
                            if(btn != null) {
                                ColorStateList colorStateList0 = btn.getTextColors();
                                Tools.setTextAppearance(btn, 0x7F090002);  // style:SmallText
                                btn.setTextColor(colorStateList0);
                            }
                        }
                        if(!Tools.goodSize(max, parent, 10)) {
                            String[][] arr2_s = {new String[]{Re.s(0x7F0700A1), Re.s(0x7F07009C)}, new String[]{Re.s(0x7F07008C), Re.s(0x7F07009B), Re.s(0x7F07009D)}, new String[]{Re.s(0x7F0701A3), Re.s(0x7F070220)}};  // string:cancel "Cancel"
                            for(int s = 0; true; ++s) {
                                if(s >= 3) {
                                    boolean changed = false;
                                    for(int v5 = 0; true; ++v5) {
                                        changed = false;
                                        if(v5 >= 3) {
                                            break;
                                        }
                                        Button btn = arr_button[v5];
                                        if(btn != null && btn.getText().toString().indexOf(0x20) != -1) {
                                            btn.setText(btn.getText().toString().replace(' ', '\n'));
                                            changed = true;
                                        }
                                    }
                                    if(changed && Tools.goodSize(max, parent, 40)) {
                                        break;
                                    }
                                    if(parent instanceof LinearLayout && ((LinearLayout)parent).getOrientation() == 0) {
                                        ((LinearLayout)parent).setOrientation(1);
                                        changed = true;
                                    }
                                    if(changed && Tools.goodSize(max, parent, 100)) {
                                        break;
                                    }
                                    Tools.goodSize(max, parent, -1);
                                    return;
                                }
                                boolean changed = false;
                                String[] same = arr2_s[s];
                                if(same != null) {
                                    int shortest = 0;
                                    for(int i = 0; i < same.length; ++i) {
                                        if(same[shortest].length() >= same[i].length()) {
                                            shortest = i;
                                        }
                                    }
                                    for(int v8 = 0; v8 < 3; ++v8) {
                                        Button btn = arr_button[v8];
                                        if(btn != null) {
                                            String s = btn.getText().toString();
                                            for(int i = 0; i < same.length; ++i) {
                                                if(i != shortest && same[i].equals(s)) {
                                                    btn.setText(same[shortest]);
                                                    changed = true;
                                                }
                                            }
                                        }
                                    }
                                    if(changed && Tools.goodSize(max, parent, s + 20)) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void fixEdgeGlow(View v) {
    }

    private static void fixEdgeGlow_(View view) {
        boolean fix = false;
        if(view != null) {
            if(!(view instanceof android.widget.ScrollView)) {
                if(!(view instanceof HorizontalScrollView)) {
                    if(view instanceof AbsListView && !(view instanceof android.fix.ListView)) {
                        fix = true;
                    }
                }
                else if(!(view instanceof android.fix.HorizontalScrollView)) {
                    fix = true;
                }
            }
            else if(!(view instanceof ScrollView)) {
                fix = true;
            }
            if(fix) {
                int v = view.getOverScrollMode();
                if(v != 2) {
                    Log.d(("Clear OverScrollMode: " + v + " for " + view));
                }
                view.setOverScrollMode(2);
            }
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int v1 = viewGroup.getChildCount();
                for(int i = 0; i < v1; ++i) {
                    try {
                        Tools.fixEdgeGlow_(viewGroup.getChildAt(i));
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                }
            }
        }
    }

    public static void fixLeak(TextView text) {
        try {
            ViewTreeObserver viewTreeObserver0 = text.getViewTreeObserver();
            if(viewTreeObserver0 != null) {
                viewTreeObserver0.removeOnPreDrawListener(text);
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static WindowManager.LayoutParams fixViewParams(WindowManager.LayoutParams params) {
        if((Config.configClient & 2) == 0) {
            params.flags &= 0xFEFFFFFF;
        }
        else {
            params.flags |= 0x1000000;
        }
        boolean useFloatWindows = SystemConstants.useFloatWindows;
        if(useFloatWindows) {
            if(Tools.allowFloatWindows == -1) {
                Tools.allowFloatWindows = Tools.canDrawOverlays(Tools.getContext()) ? 1 : 0;
            }
            if(Tools.allowFloatWindows == 0) {
                useFloatWindows = false;
                Log.d("canDrawOverlays = false; fixed");
            }
        }
        if(!useFloatWindows) {
            params.type = 2;
            IBinder token = BaseActivity.instance.getWindow().getAttributes().token;
            if(token == null) {
                token = BaseActivity.instance.getWindow().getDecorView().getRootView().getWindowToken();
            }
            params.token = token;
            return params;
        }
        if(params.type == 0x7F6 && params.token != null) {
            Log.d(("token: null " + params.token));
            params.token = null;
            return params;
        }
        return params;
    }

    public static void fixViewParams(Window window) {
        window.setAttributes(Tools.fixViewParams(window.getAttributes()));
    }

    public static void fixViews(View v) {
        Tools.setUseSoundEffects(Tools.getRootView(v));
    }

    private static String formatFileSize(long number, boolean shorter) {
        float result = (float)number;
        String suffix = "B";
        if(result > 900.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if(result > 900.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if(result > 900.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if(result > 900.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if(result > 900.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        if(result < 1.0f) {
            return Tools.stringFormat("%.2f", new Object[]{result}) + ' ' + suffix;
        }
        if(result < 10.0f) {
            return shorter ? Tools.stringFormat("%.1f", new Object[]{result}) + ' ' + suffix : Tools.stringFormat("%.2f", new Object[]{result}) + ' ' + suffix;
        }
        return result < 100.0f && !shorter ? Tools.stringFormat("%.2f", new Object[]{result}) + ' ' + suffix : Tools.stringFormat("%.0f", new Object[]{result}) + ' ' + suffix;
    }

    public static String formatFileSize(Context context, long number) {
        try {
            return Formatter.formatFileSize(context, number);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            try {
                return Tools.formatFileSize(number, false);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                return Long.toString(number);
            }
        }
    }

    public static int gcd(int a, int b) {
        while(b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    public static String getApkPath() {
        String ret = null;
        try {
            Context context0 = Tools.getContext();
            if(context0 != null) {
                ret = context0.getPackageCodePath();
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        if(ret == null) {
            try {
                ApplicationInfo applicationInfo0 = Tools.getApplicationInfo(Apk.PACKAGE);
                if(applicationInfo0 != null) {
                    ret = applicationInfo0.sourceDir;
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(ret == null) {
            try {
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/self/maps")));
                String odex = null;
            alab1:
                while(true) {
                    while(true) {
                        String s2 = bufferedReader0.readLine();
                        if(s2 == null) {
                            break alab1;
                        }
                        if(!s2.contains(Apk.PACKAGE) || !s2.contains(".apk")) {
                            break;
                        }
                        String line = s2.split("\\.apk", 2)[0] + ".apk";
                        if(line.indexOf(0x40) != -1) {
                            String[] arr_s = line.trim().split("/");
                            odex = "/" + arr_s[arr_s.length - 1].trim().replace('@', '/');
                            break;
                        }
                        ret = "/" + line.split("/", 2)[1].trim();
                        break alab1;
                    }
                }
                bufferedReader0.close();
                return ret != null || odex == null ? ret : odex;
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                return ret;
            }
        }
        return ret;
    }

    public static Drawable getApplicationIcon(ApplicationInfo info) {
        Drawable icon;
        PackageManager packageManager = Tools.pm;
        if(info == null || packageManager == null) {
            return null;
        }
        try {
            icon = null;
            boolean z = CheckNativeCrash.enter(info.packageName, "icon");
            try {
                icon = info.loadIcon(packageManager);
            }
            finally {
                CheckNativeCrash.exit(z);
            }
            if(icon == null) {
                boolean entered = CheckNativeCrash.enter(info.packageName, "icon2");
                try {
                    return packageManager.getApplicationIcon(info);
                }
                finally {
                    CheckNativeCrash.exit(entered);
                }
            }
        }
        catch(Throwable e) {
            Log.e(("Failed load icon for: " + info.packageName), e);
        }
        return icon;
    }

    public static ApplicationInfo getApplicationInfo(String packageName) throws PackageManager.NameNotFoundException {
        return Tools.getApplicationInfo(packageName, 0);
    }

    public static ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        ApplicationInfo ret = null;
        if(CheckNativeCrash.isBuggedPackage(packageName)) {
            Log.e(("Failed getApplicationInfo, isBuggedPackage: " + packageName));
            return null;
        }
        PackageManager packageManager0 = Tools.getPackageManager();
        if(packageManager0 != null) {
            try {
                boolean z = CheckNativeCrash.enter(packageName, "app_info");
                try {
                    ret = packageManager0.getApplicationInfo(packageName, flags);
                }
                finally {
                    CheckNativeCrash.exit(z);
                }
            }
            catch(Throwable e) {
                if(e instanceof PackageManager.NameNotFoundException) {
                    throw (PackageManager.NameNotFoundException)e;
                }
                Log.e("Failed getApplicationInfo", e);
            }
        }
        if(ret == null) {
            Object object0 = Tools.getIPackageManager();
            if(object0 != null) {
                try {
                    Object ApplicationInfo = null;
                    Class class0 = object0.getClass();
                    MethodDescr tools$MethodDescr0 = Tools.getMethodDescr(class0, new MethodDescr[]{new MethodDescr(class0, "getApplicationInfo", new Class[]{String.class, Integer.TYPE, Integer.TYPE}, object0, new Object[]{packageName, flags, 0}), new MethodDescr(class0, "getApplicationInfo", new Class[]{String.class, Integer.TYPE}, object0, new Object[]{packageName, flags})});
                    if(tools$MethodDescr0 != null) {
                        boolean z1 = CheckNativeCrash.enter(packageName, "app_info2");
                        try {
                            ApplicationInfo = tools$MethodDescr0.invoke();
                        }
                        finally {
                            CheckNativeCrash.exit(z1);
                        }
                        if(ApplicationInfo == null) {
                            throw new PackageManager.NameNotFoundException(packageName);
                        }
                    }
                    return ApplicationInfo instanceof ApplicationInfo ? ((ApplicationInfo)ApplicationInfo) : null;
                }
                catch(Throwable e) {
                }
            }
        }
        else {
            return ret;
        }
        if(e instanceof PackageManager.NameNotFoundException) {
            throw (PackageManager.NameNotFoundException)e;
        }
        Log.e("Failed getApplicationInfo", e);
        return null;
    }

    public static String getApplicationLabel(ApplicationInfo info) {
        CharSequence label;
        PackageManager packageManager = Tools.pm;
        if(info == null || packageManager == null) {
            return null;
        }
        try {
            label = null;
            boolean z = CheckNativeCrash.enter(info.packageName, "label");
            try {
                label = info.loadLabel(packageManager);
            }
            finally {
                CheckNativeCrash.exit(z);
            }
            if(label == null) {
                boolean entered = CheckNativeCrash.enter(info.packageName, "label2");
                try {
                    label = packageManager.getApplicationLabel(info);
                }
                finally {
                    CheckNativeCrash.exit(entered);
                }
                return label == null ? null : label.toString();
            }
        }
        catch(Throwable e) {
            Log.e(("Failed load label for: " + info.packageName), e);
            return label == null ? null : label.toString();
        }
        return label == null ? null : label.toString();
    }

    public static File getCacheDir() {
        String ret = Tools.cacheDir == null ? Tools.getDataDir("/cache") : Tools.cacheDir;
        return ret == null ? null : new File(ret);
    }

    public static File getCacheDirHidden() {
        File ret = Tools.getCacheDir();
        if(ret != null) {
            File file1 = new File(ret, Tools.getHiddenDir());
            file1.mkdirs();
            return file1;
        }
        return null;
    }

    public static int getColor(int resId) {
        return Tools.getColor(resId, -1);
    }

    public static int getColor(int resId, int defaultColor) {
        try {
            Context context0 = Tools.getContext();
            if(Build.VERSION.SDK_INT >= 23) {
                try {
                    return context0.getColor(resId);
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
            return context0.getResources().getColor(resId);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return defaultColor;
        }
    }

    public static Context getContext() {
        Context context = MainService.context;
        if(context == null && BaseActivity.context != null) {
            return Tools.removeContextWrappers(BaseActivity.context) == null ? BaseActivity.appContext : BaseActivity.context;
        }
        return context;
    }

    public static View getCustomTitle(int resId) {
        return Tools.getCustomTitle(Re.s(resId));
    }

    public static View getCustomTitle(String title) {
        return Tools.getCustomTitle(title, null);
    }

    public static View getCustomTitle(String title, String subTitle) {
        return Tools.getCustomTitle(title, subTitle, -1);
    }

    public static View getCustomTitle(String title, String subTitle, int helpRes) {
        View view0 = LayoutInflater.inflateStatic(0x7F040001, null);  // layout:dialog_title
        TextView tv = (TextView)view0.findViewById(0x7F0B0006);  // id:alertTitle
        tv.setText(title);
        if(title != null) {
            tv.setVisibility(0);
        }
        if(helpRes != -1 && helpRes != 0) {
            Tools.setButtonHelpBackground(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override  // android.view.View$OnClickListener
                public void onClick(View v) {
                    AlertDialog alertDialog0 = Alert.create().setMessage(Re.s(title) + '\n' + Re.s(helpRes)).setNegativeButton(Re.s(0x7F07009D), null).create();  // string:ok "OK"
                    Alert.show(alertDialog0);
                    Alert.setOnShowListener(alertDialog0, new DialogInterface.OnShowListener() {
                        @Override  // android.content.DialogInterface$OnShowListener
                        public void onShow(DialogInterface d) {
                            Tools.setClickableText(alertDialog0);
                        }
                    });
                }
            });
        }
        if(subTitle != null) {
            TextView sub = (TextView)view0.findViewById(0x7F0B0007);  // id:subTitle
            sub.setText(subTitle);
            sub.setVisibility(0);
        }
        return view0;
    }

    public static Context getDarkContext() {
        return Tools.getDarkContext(Tools.getContext());
    }

    public static Context getDarkContext(Context context) {
        return context != null && Build.VERSION.SDK_INT < 11 ? new ContextThemeWrapper(context, 0x7F090005) : context;  // style:DarkFixTheme
    }

    private static String getDataDir(String inner) {
        if(Tools.dataDir == null) {
            try {
                ApplicationInfo applicationInfo0 = Tools.getApplicationInfo(Apk.PACKAGE);
                if(applicationInfo0 != null) {
                    Tools.dataDir = applicationInfo0.dataDir;
                }
                return Tools.dataDir != null && inner != null ? "null" + inner : Tools.dataDir;
            }
            catch(Throwable e) {
            }
        }
        else {
            return Tools.dataDir != null && inner != null ? "null" + inner : Tools.dataDir;
        }
        Log.badImplementation(e);
        return Tools.dataDir != null && inner != null ? "null" + inner : Tools.dataDir;
    }

    public static String getDataDirSafe() {
        StringBuilder d = new StringBuilder();
        d.append("/data/");
        int v = android.os.Process.myUid();
        if(v / 100000 == 0) {
            d.append("data");
        }
        else {
            d.append("user/");
            d.append(v / 100000);
        }
        d.append('/');
        d.append((Tools.pkg == null ? Apk.PACKAGE : Tools.pkg));
        return d.toString();
    }

    public static Drawable getDrawable(int resId) {
        if(Build.VERSION.SDK_INT >= 21) {
            try {
                return Tools.getContext().getDrawable(resId);
            }
            catch(NoSuchMethodError unused_ex) {
                return Tools.getContext().getResources().getDrawable(resId);
            }
        }
        return Tools.getContext().getResources().getDrawable(resId);
    }

    public static File getFilesDir() {
        String ret = Tools.filesDir == null ? Tools.getDataDir("/files") : Tools.filesDir;
        return ret == null ? null : new File(ret);
    }

    public static File getFilesDirHidden() {
        File ret = Tools.getFilesDir();
        if(ret != null) {
            File file1 = new File(ret, Tools.getHiddenDir());
            file1.mkdirs();
            return file1;
        }
        return null;
    }

    public static long getFreeMem() {
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager)Tools.getContext().getSystemService("activity");
            if(activityManager != null) {
                activityManager.getMemoryInfo(mi);
                return mi.availMem / 0x100000L;
            }
        }
        catch(Throwable e) {
            Log.e("Failed get free mem", e);
        }
        return -1L;
    }

    public static String getHiddenDir() {
        String suffix;
        if(Tools.hiddenDir == null) {
            try {
                suffix = null;
                SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
                if(!sharedPreferences0.contains("hidden-dir")) {
                    String val = Tools.getHiddenDirFallback();
                    if(val == null) {
                        val = "kUDo";
                    }
                    sharedPreferences0.edit().putString("hidden-dir", val).commit();
                }
                try {
                    suffix = sharedPreferences0.getString("hidden-dir", null);
                }
                catch(ClassCastException unused_ex) {
                }
                if(suffix == null) {
                    int v = sharedPreferences0.getInt("hidden-dir", 0);
                    if(v == 0) {
                        sharedPreferences0.edit().remove("hidden-dir").commit();
                    }
                    else {
                        suffix = Integer.toString(v);
                        sharedPreferences0.edit().putString("hidden-dir", suffix).commit();
                    }
                }
            }
            catch(Throwable e) {
                Log.w("Failed get num", e);
                suffix = Tools.getHiddenDirFallback();
            }
            if(suffix == null) {
                suffix = "kUDo";
            }
            Tools.hiddenDir = "GG-" + suffix;
        }
        return Tools.hiddenDir;
    }

    private static String getHiddenDirFallback() {
        try {
            File file0 = Tools.getFilesDir();
            if(file0 != null) {
                String[] arr_s = file0.list();
                if(arr_s != null) {
                    int v = 0;
                    while(v < arr_s.length) {
                        String file = arr_s[v];
                        if(file == null || !file.startsWith("GG-")) {
                            ++v;
                            continue;
                        }
                        return file.substring(3);
                    }
                }
            }
        }
        catch(Throwable e) {
            Log.w("Failed get dir", e);
        }
        return null;
    }

    public static Object getIPackageManager() {
        if(Tools.iPm == null) {
            try {
                Method method0 = Class.forName("android.os.ServiceManager").getDeclaredMethod("getIServiceManager");
                method0.setAccessible(true);
                Object object0 = method0.invoke(null, ContainerHelpers.EMPTY_OBJECTS);
                Method method1 = object0.getClass().getDeclaredMethod("getService", String.class);
                method1.setAccessible(true);
                Object object1 = method1.invoke(object0, "package");
                Method method2 = Class.forName("android.content.pm.IPackageManager$Stub").getDeclaredMethod("asInterface", IBinder.class);
                method2.setAccessible(true);
                Tools.iPm = method2.invoke(null, object1);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            return Tools.iPm;
        }
        return Tools.iPm;
    }

    public static String getInfoAboutContext(Context context) [...] // 潜在的解密器

    public static ListView getListView(View view) {
        while(view != null) {
            if(view instanceof ListView) {
                return (ListView)view;
            }
            ViewParent viewParent0 = view.getParent();
            if(!(viewParent0 instanceof View)) {
                break;
            }
            view = (View)viewParent0;
        }
        return null;
    }

    private static MethodDescr getMethodDescr(Class class0, MethodDescr[] meths) {
        MethodDescr meth;
        try {
            int preffered = Build.VERSION.SDK_INT <= 15 ? 1 : 0;
            meth = meths[preffered].check();
        }
        catch(NoSuchMethodException unused_ex) {
            meth = null;
            for(int i = 0; i < meths.length; ++i) {
                if(i != preffered) {
                    try {
                        meth = meths[i].check();
                    }
                    catch(NoSuchMethodException unused_ex) {
                    }
                }
            }
        }
        if(meth == null) {
            Log.d(("Methods: " + Arrays.toString(class0.getMethods())));
        }
        return meth;
    }

    public static String getNativePath(String path) {
        try {
            if(Tools.overrideNative == 0 || Tools.overrideNative == 1) {
                File file = new File(path);
                String s1 = "/" + file.getName();
                FileInputStream is = new FileInputStream(file);
                MappedByteBuffer mappedByteBuffer0 = is.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, 0x1000L);
                BufferedReader bufferedReader0 = new BufferedReader(new FileReader("/proc/self/maps"));
            alab1:
                String s2;
                while((s2 = bufferedReader0.readLine()) != null) {
                    int v = s2.indexOf(s1);
                    if(v >= 0) {
                        for(int start = v; true; --start) {
                            if(start <= 0 || s2.charAt(start) <= 0x20) {
                                if(start >= v) {
                                    break alab1;
                                }
                                path = s2.substring(start, s2.length()).trim();
                                if(Tools.overrideNative != 0) {
                                    break alab1;
                                }
                                Tools.overrideNative = 1;
                                break alab1;
                            }
                        }
                    }
                }
                bufferedReader0.close();
                if(Tools.overrideNative == 0) {
                    Tools.overrideNative = 2;
                }
                mappedByteBuffer0.capacity();
                is.close();
            }
        }
        catch(IOException e) {
            Log.w(("Failed getNativePath: " + path), e);
        }
        return path;
    }

    public static PackageInfo getPackageInfo(String packageName) throws PackageManager.NameNotFoundException {
        return Tools.getPackageInfo(packageName, 0);
    }

    public static PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        PackageInfo ret = null;
        if(CheckNativeCrash.isBuggedPackage(packageName)) {
            Log.e(("Failed getPackageInfo, isBuggedPackage: " + packageName));
            return null;
        }
        PackageManager packageManager0 = Tools.getPackageManager();
        if(packageManager0 != null) {
            try {
                boolean z = CheckNativeCrash.enter(packageName, "pkg_info");
                try {
                    ret = packageManager0.getPackageInfo(packageName, flags);
                }
                finally {
                    CheckNativeCrash.exit(z);
                }
            }
            catch(Throwable e) {
                if(e instanceof PackageManager.NameNotFoundException) {
                    throw (PackageManager.NameNotFoundException)e;
                }
                Log.e("Failed getPackageInfo", e);
            }
        }
        if(ret == null) {
            Object object0 = Tools.getIPackageManager();
            if(object0 != null) {
                try {
                    Object PackageInfo = null;
                    Class class0 = object0.getClass();
                    MethodDescr tools$MethodDescr0 = Tools.getMethodDescr(class0, new MethodDescr[]{new MethodDescr(class0, "getPackageInfo", new Class[]{String.class, Integer.TYPE, Integer.TYPE}, object0, new Object[]{packageName, flags, 0}), new MethodDescr(class0, "getPackageInfo", new Class[]{String.class, Integer.TYPE}, object0, new Object[]{packageName, flags})});
                    if(tools$MethodDescr0 != null) {
                        boolean z1 = CheckNativeCrash.enter(packageName, "pkg_info2");
                        try {
                            PackageInfo = tools$MethodDescr0.invoke();
                        }
                        finally {
                            CheckNativeCrash.exit(z1);
                        }
                        if(PackageInfo == null) {
                            throw new PackageManager.NameNotFoundException(packageName);
                        }
                    }
                    return PackageInfo instanceof PackageInfo ? ((PackageInfo)PackageInfo) : null;
                }
                catch(Throwable e) {
                }
            }
        }
        else {
            return ret;
        }
        if(e instanceof PackageManager.NameNotFoundException) {
            throw (PackageManager.NameNotFoundException)e;
        }
        Log.e("Failed getPackageInfo", e);
        return null;
    }

    public static PackageManager getPackageManager() {
        if(Tools.pm == null && Tools.sePM.use(1)) {
            try {
                Tools.pm = BaseActivity.instance.getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Tools.sePM.free(1);
        }
        if(Tools.pm == null && Tools.sePM.use(2)) {
            try {
                Tools.pm = BaseActivity.instance.getApplication().getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Tools.sePM.free(2);
        }
        if(Tools.pm == null && Tools.sePM.use(4)) {
            try {
                Tools.pm = BaseActivity.instance.getApplicationContext().getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Tools.sePM.free(4);
        }
        if(Tools.pm == null && Tools.sePM.use(8)) {
            try {
                Tools.pm = MainService.context.getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Tools.sePM.free(8);
        }
        if(Tools.pm == null && Tools.sePM.use(16)) {
            try {
                Tools.pm = MainService.context.getApplicationContext().getPackageManager();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Tools.sePM.free(16);
        }
        return Tools.pm;
    }

    public static String getPackageName() {
        return Tools.pkg == null ? Apk.PACKAGE : Tools.pkg;
    }

    public static int getPid(Process p) {
        try {
            Field field0 = p.getClass().getDeclaredField("pid");
            field0.setAccessible(true);
            int pid = field0.getInt(p);
            field0.setAccessible(false);
            return pid;
        }
        catch(Throwable unused_ex) {
            return -1;
        }
    }

    public static int getRamSizeKb() {
        try {
            RandomAccessFile randomAccessFile0 = new RandomAccessFile("/proc/meminfo", "r");
            Matcher matcher0 = Tools.p.matcher(randomAccessFile0.readLine());
            for(String value = "-2"; true; value = matcher0.group(1)) {
                if(!matcher0.find()) {
                    randomAccessFile0.close();
                    return Integer.parseInt(value);
                }
            }
        }
        catch(Throwable ex) {
            Log.w("Failed get RAM size", ex);
            return -1;
        }
    }

    public static String getRealPath(String str) {
        return Tools.fakeToReal == null ? str : str.replace(Tools.fakeToReal[0], Tools.fakeToReal[1]);
    }

    public static Drawable getResized(Drawable icon, int px) {
        BitmapDrawable bitmapDrawable0;
        Bitmap bitmap0;
        try {
            if(!(icon instanceof BitmapDrawable)) {
                return icon;
            }
            bitmap0 = ((BitmapDrawable)icon).getBitmap();
            if(bitmap0 == null || bitmap0.getWidth() <= px + 4 && bitmap0.getHeight() <= px + 4) {
                return icon;
            }
            bitmapDrawable0 = new BitmapDrawable(Tools.getContext().getResources(), Bitmap.createScaledBitmap(bitmap0, px, px, false));
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return icon;
        }
        try {
            Log.d(("getResized: " + bitmap0.getWidth() + 'x' + bitmap0.getHeight() + " -> " + px + 'x' + px + " in " + 0L));
            return bitmapDrawable0;
        }
        catch(Throwable e) {
            icon = bitmapDrawable0;
        }
        Log.badImplementation(e);
        return icon;
    }

    private static String getRnd() [...] // 潜在的解密器

    public static View getRootView(View view) {
        View root = view.getRootView();
        if(root == null) {
            for(root = view; true; root = (View)viewParent0) {
                ViewParent viewParent0 = root.getParent();
                if(viewParent0 == null || !(viewParent0 instanceof View)) {
                    break;
                }
            }
        }
        return root;
    }

    public static ScrollPos getScrollPos(ListView list) {
        int v = list.getFirstVisiblePosition();
        try {
            View v = list.getChildAt(0);
            return v == null ? new ScrollPos(v, 0) : new ScrollPos(v, v.getTop() - list.getPaddingTop());
        }
        catch(ArrayIndexOutOfBoundsException unused_ex) {
            return new ScrollPos(v, 0);
        }
    }

    public static String getSdcardPath() {
        String ret;
        try {
            ret = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        catch(Throwable e) {
            ret = null;
            Log.e("Fail get sdcard path", e);
        }
        if(ret == null) {
            return "/sdcard";
        }
        if(ret.startsWith("/storage/emulated/0")) {
            if(Tools.fixToLegacy == 0) {
                Tools.fixToLegacy = Tools.isGoodDir(ret) || !Tools.isGoodDir(("/storage/emulated/legacy" + ret.substring(19))) ? -1 : 1;
            }
            return Tools.fixToLegacy <= 0 ? ret : "/storage/emulated/legacy" + ret.substring(19);
        }
        return ret;
    }

    public static int getSelectedCount(Object obj) {
        int count = 0;
        if(obj != null) {
            if(obj instanceof ArrayListResults) {
                ArrayListResults list = (ArrayListResults)obj;
                int v1 = list.size();
                for(int i = 0; i < v1; ++i) {
                    try {
                        if(list.checked(i)) {
                            ++count;
                        }
                    }
                    catch(IndexOutOfBoundsException unused_ex) {
                    }
                }
                return count;
            }
            if(obj instanceof LongSparseArrayChecked) {
                int v3 = ((LongSparseArrayChecked)obj).size();
                for(int i = 0; i < v3; ++i) {
                    if(((LongSparseArrayChecked)obj).checkAt(i)) {
                        ++count;
                    }
                }
                return count;
            }
            if(!(obj instanceof boolean[])) {
                throw new IllegalArgumentException();
            }
            int s = ((boolean[])obj).length - 1;
            for(int i = 1; i < s; ++i) {
                if(((boolean[])obj)[i]) {
                    ++count;
                }
            }
            return count;
        }
        return 0;
    }

    public static SharedPreferences getSharedPreferences() {
        SharedPreferences ret = (SharedPreferences)Tools.weakSp.get();
        if(ret == null) {
            ret = Tools.getContext().getSharedPreferences("null_preferences", 0);
            Tools.weakSp = new WeakReference(ret);
        }
        return ret;
    }

    public static Intent getStartIntent(Context context, String packageName, String className) {
        if(context == null) {
            context = Tools.getContext();
        }
        Intent intent0 = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent0 == null) {
            intent0 = new Intent("android.intent.action.MAIN");
        }
        intent0.setPackage(packageName);
        if(className != null) {
            intent0.setClassName(packageName, className);
        }
        intent0.setFlags(0x10000000);
        return intent0;
    }

    public static View getViewForAttach(View view) {
        if(view != null) {
            ViewParent viewParent0 = view.getParent();
            if(viewParent0 != null && viewParent0 instanceof ViewGroup) {
                try {
                    ((ViewGroup)viewParent0).removeView(view);
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
                return view;
            }
        }
        return view;
    }

    private static boolean goodSize(int max, ViewGroup parent, int check) {
        parent.requestLayout();
        parent.measure(-2, -2);
        int v2 = parent.getMeasuredWidth();
        boolean good = v2 <= max;
        if(check != 0 || !good) {
            Log.d(("fixDialogButtonsSize: " + v2 + " <= " + max + " = " + good + " [" + check + ']'));
        }
        return good;
    }

    public static boolean hasEditText(View view) {
        if(view != null && view.getVisibility() == 0) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int v = viewGroup.getChildCount();
                for(int i = 0; i < v; ++i) {
                    try {
                        View view1 = viewGroup.getChildAt(i);
                        if(view1 != null && view1.getVisibility() == 0 && (view1 instanceof EditText || view1 instanceof ViewGroup && Tools.hasEditText(view1))) {
                            return true;
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                }
                return false;
            }
            return view instanceof EditText;
        }
        return false;
    }

    public static void hideKeyboard(AlertDialog dialog) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Tools.hideKeyboard(dialog.getWindow());
                }
                catch(Throwable e) {
                    Log.e("hideSoftInputFromWindow", e);
                }
            }
        });
    }

    public static void hideKeyboard(View view) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Tools.imm == null) {
                        Tools.imm = (InputMethodManager)MainService.context.getSystemService("input_method");
                    }
                    if(view != null) {
                        Tools.imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                catch(Throwable e) {
                    Log.e("hideSoftInputFromWindow", e);
                }
            }
        });
    }

    public static void hideKeyboard(Window window) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    View view = window.getCurrentFocus();
                    if(view == null) {
                        view = window.peekDecorView();
                    }
                    if(view != null) {
                        Tools.hideKeyboard(view);
                    }
                }
                catch(Throwable e) {
                    Log.e("hideSoftInputFromWindow", e);
                }
            }
        });
    }

    public static void hideKeyboard(WeakReference weakReference0) {
        if(weakReference0 != null) {
            AlertDialog dialog = (AlertDialog)weakReference0.get();
            if(dialog != null) {
                Tools.hideKeyboard(dialog);
            }
        }
    }

    @TargetApi(16)
    public static void hideStatusBar(Dialog dialog) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window0 = dialog.getWindow();
                WindowManager.LayoutParams windowManager$LayoutParams0 = window0.getAttributes();
                windowManager$LayoutParams0.flags &= 0xFFFFF7FF;
                if(Build.VERSION.SDK_INT >= 11) {
                    windowManager$LayoutParams0.systemUiVisibility = 1;
                    if(Build.VERSION.SDK_INT >= 16) {
                        View view0 = window0.peekDecorView();
                        if(view0 != null) {
                            try {
                                view0.setSystemUiVisibility(4);
                            }
                            catch(Throwable e) {
                                Log.badImplementation(e);
                            }
                        }
                        windowManager$LayoutParams0.systemUiVisibility = 4;
                        ActionBar actionBar0 = dialog.getActionBar();
                        if(actionBar0 != null) {
                            actionBar0.hide();
                        }
                    }
                }
                window0.setAttributes(windowManager$LayoutParams0);
            }
        });
    }

    public static int indexOf(byte[] data, byte[] pattern) {
        if(data.length != 0) {
            int[] arr_v = Tools.computeFailure(pattern);
            int j = 0;
            for(int i = 0; i < data.length; ++i) {
                while(j > 0 && pattern[j] != data[i]) {
                    j = arr_v[j - 1];
                }
                if(pattern[j] == data[i]) {
                    ++j;
                }
                if(j == pattern.length) {
                    return i - pattern.length + 1;
                }
            }
        }
        return -1;
    }

    public static void init(Activity activity) {
        try {
            Tools._init(activity);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        try {
            Tools._init(activity.getApplication());
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        try {
            Tools._init(activity.getApplicationContext());
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static void init(Context context) {
        try {
            Tools._init(context);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static int[] intArray(List list0) {
        int[] ret = new int[list0.size()];
        int i = 0;
        for(Object object0: list0) {
            ret[i] = (int)(((Integer)object0));
            ++i;
        }
        return ret;
    }

    public static boolean isExited(Process process) {
        try {
            process.exitValue();
            return true;
        }
        catch(IllegalThreadStateException unused_ex) {
            return false;
        }
    }

    public static boolean isFile(Object filename) {
        FileInputStream is;
        String msg;
        File file = filename instanceof File ? ((File)filename) : new File(filename.toString());
        int err = 0;
        if(!file.exists()) {
            err = 0x7F0701F8;  // string:file_not_found "File not found:"
        }
        else if(!file.isFile()) {
            err = 0x7F0702F5;  // string:not_file "Path is not a file:"
        }
        if(err != 0) {
            msg = Tools.stripColon(err) + ": " + filename;
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(msg).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
            return false;
        }
        try {
            is = null;
            is = new FileInputStream(file);
            is.read();
        }
        catch(IOException e) {
            msg = filename + "\n\n" + e.getMessage();
            if(is != null) {
                try {
                    is.close();
                }
                catch(IOException unused_ex) {
                }
            }
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(msg).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
            return false;
        }
        catch(Throwable throwable0) {
            if(is != null) {
                try {
                    is.close();
                }
                catch(IOException unused_ex) {
                }
            }
            throw throwable0;
        }
        try {
            is.close();
        }
        catch(IOException unused_ex) {
        }
        return true;
    }

    public static Throwable isGoodContext(Context context) {
        try {
            context.getPackageName();
            context.getPackageManager();
            context.getCacheDir();
            context.getFilesDir();
            context.getExternalCacheDir();
            context.getExternalFilesDir(null);
            return null;
        }
        catch(Throwable e) {
            return e;
        }
    }

    private static boolean isGoodDir(String path) {
        try {
            File dir = new File(path);
            String[] list = dir.list();
            if(list != null) {
                if(list.length == 0) {
                    File file = new File(dir, ".gg." + System.nanoTime());
                    file.createNewFile();
                    list = dir.list();
                    file.delete();
                }
                return list.length > 0;
            }
        }
        catch(Throwable e) {
            Log.w(("isGoodDir: \'" + path + "\'"), e);
        }
        return false;
    }

    public static int isLandscape() {
        int isLandscape = Tools.isLandscape;
        if(isLandscape == -1) {
            Tools.updateOrientation(null);
            return Tools.isLandscape;
        }
        return isLandscape;
    }

    public static boolean isPackageInstalled(String pkg) {
        try {
            Tools.getApplicationInfo(pkg, 0);
            return true;
        }
        catch(PackageManager.NameNotFoundException unused_ex) {
            return false;
        }
    }

    public static boolean isPath(String path) {
        if(path.length() < 2 || path.charAt(0) != 0x2F) {
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AE)).setMessage(path + "\n\n" + Re.s(0x7F070163)).setPositiveButton(Re.s(0x7F07009D), null));  // string:error "Error"
            return false;
        }
        return true;
    }

    public static long[] longArray(List list0) {
        long[] ret = new long[list0.size()];
        int i = 0;
        for(Object object0: list0) {
            ret[i] = (long)(((Long)object0));
            ++i;
        }
        return ret;
    }

    public static String longToTime(long t) {
        long show;
        StringBuilder out = new StringBuilder();
        long time = Math.abs(t);
        for(int i = 0; i < Tools.unitTime.length; i += 2) {
            int size = Tools.unitTime[i + 1];
            if(size <= 0) {
                show = time;
                time = 0L;
            }
            else {
                show = time % ((long)size);
                time /= (long)size;
            }
            if(show > 0L) {
                if(out.length() > 0) {
                    out.insert(0, " ");
                }
                out.insert(0, Re.s(Tools.unitTime[i]));
                out.insert(0, " ");
                out.insert(0, show);
            }
            if(time == 0L) {
                break;
            }
        }
        if(out.length() == 0) {
            out.insert(0, Re.s(Tools.unitTime[0]));
            out.insert(0, "0 ");
        }
        if(t < 0L) {
            out.insert(0, "-");
        }
        return out.toString();
    }

    public static boolean openAppInfo(String pkg) {
        Intent intent0;
        try {
            Context context0 = Tools.getContext();
            try {
                intent0 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", pkg, null));
            }
            catch(ActivityNotFoundException e) {
                Log.w(("Failed open app info for: " + pkg), e);
                intent0 = new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS");
            }
            intent0.setFlags(0x10000000);
            context0.startActivity(intent0);
            return true;
        }
        catch(Throwable e) {
            Log.w(("Failed open app info for: " + pkg), e);
            return false;
        }
    }

    public static double parseTime(String str) throws NumberFormatException {
        String s1 = str.trim();
        double mul = 1.0;
        if(s1.startsWith("-")) {
            s1 = s1.substring(1);
            mul = -1.0;
        }
        String[] arr_s = s1.split(":", Tools.unitTime.length / 2);
        double out = 0.0;
        Result result = new Result();
        for(int i = 0; i < arr_s.length; ++i) {
            int index = (arr_s.length - 1 - i) * 2 + 1;
            if(index < Tools.unitTime.length) {
                double f2 = Double.longBitsToDouble(ParserNumbers.parseDouble(result, arr_s[i], s1).value);
                out = ((double)Tools.unitTime[index]) * out + f2;
            }
        }
        return out * mul;
    }

    public static String quoteFilter(String text) {
        return Pattern.quote(text).replace("^", "\\E^\\Q").replace("$", "\\E$\\Q").replace("?", "\\E.\\Q").replace("*", "\\E.*\\Q").replace("\\Q\\E", "");
    }

    public static Context removeContextWrappers(Context context) {
        while(context instanceof ContextWrapper) {
            context = ((ContextWrapper)context).getBaseContext();
        }
        return context;
    }

    public static String removeNewLinesChars(String text) [...] // 潜在的解密器

    public static void removeView(View view) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainService.instance.mWindowManager.removeView(view);
                }
                catch(Throwable e) {
                    Log.e(("Failed removeView: " + view), e);
                }
            }
        });
    }

    public static void removeViewImmediate(View view) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainService.instance.mWindowManager.removeViewImmediate(view);
                }
                catch(Throwable e) {
                    Log.e(("Failed removeViewImmediate: " + view), e);
                }
            }
        });
    }

    public static void removeViewWithWrapper(View view) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewParent viewParent0 = view.getParent();
                if(viewParent0 instanceof ViewWrapper) {
                    Tools.removeView(((ViewWrapper)viewParent0));
                    ((ViewWrapper)viewParent0).removeAllViews();
                }
            }
        });
    }

    public static String runTest() {
        String out = "";
        long[] arr_v = {0L, 1L, 10L, 80L, 800L, 8000L, 80000L, 800000L, 8000000L, 80000000L, 800000000L, 8000000000L};
        for(int i = 0; i < 12; ++i) {
            out = String.valueOf(out) + arr_v[i] + ": " + Tools.longToTime(arr_v[i]) + '\n';
        }
        return out;
    }

    public static void selectAll(EditText edit) {
        if(edit == null) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Editable editable0 = edit.getText();
                if(editable0.length() > 0) {
                    try {
                        editable0.replace(0, 1, editable0.subSequence(0, 1), 0, 1);
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                    edit.selectAll();
                }
            }
        });
    }

    public static void setButtonBackground(View view) {
        Tools.setButtonBackground(view, view.getBackground());
    }

    public static void setButtonBackground(View view, Drawable background) {
        try {
            if(background == null) {
                background = new ColorDrawable(0);
            }
            view.setFocusable(true);
            StateListDrawable replace = new StateListDrawable();
            Drawable drawable1 = new ImageButton(view.getContext()).getBackground();
            replace.addState(ViewHelper.FOCUSED_STATE_SET_, drawable1);
            replace.addState(ViewHelper.SELECTED_STATE_SET_, drawable1);
            replace.addState(ViewHelper.PRESSED_STATE_SET_, drawable1);
            replace.addState(StateSet.WILD_CARD, background);
            if(Build.VERSION.SDK_INT >= 16) {
                view.setBackground(background);
            }
            else {
                view.setBackgroundDrawable(background);
            }
            int v = view.getPaddingLeft();
            int v1 = view.getPaddingRight();
            int v2 = view.getPaddingTop();
            int v3 = view.getPaddingBottom();
            if(Build.VERSION.SDK_INT >= 16) {
                view.setBackground(replace);
            }
            else {
                view.setBackgroundDrawable(replace);
            }
            view.setPadding(v, v2, v1, v3);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static void setButtonHelpBackground(View view) {
        Tools.setButtonBackground(view, Tools.getDrawable(0x7F020033));  // drawable:ic_information_white_12dp
    }

    public static boolean setButtonListener(DialogInterface dialog, int button, Object tag, View.OnClickListener listener) {
        if(dialog instanceof AlertDialog) {
            Button button0 = ((AlertDialog)dialog).getButton(button);
            if(button0 != null) {
                button0.setTag(tag);
                button0.setOnClickListener(listener);
                return true;
            }
        }
        return false;
    }

    public static boolean setButtonListener(DialogInterface dialog, int button, Object tag, View.OnClickListener listener, android.ext.EditText edit) {
        if(edit != null) {
            edit.requestFocus();
            Tools.selectAll(edit);
        }
        return Tools.setButtonListener(dialog, button, tag, listener);
    }

    public static AlertDialog setClickableText(AlertDialog dialog) {
        try {
            TextView message = (TextView)dialog.findViewById(0x102000B);
            if(message != null) {
                Tools.setClickableText(message, message.getText().toString());
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        return dialog;
    }

    public static void setClickableText(TextView view, String text) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while(true) {
                    try {
                        view.setLinksClickable(true);
                        view.setAutoLinkMask(1);
                        view.setMovementMethod(LinkMovementMethod.getInstance());
                        view.setText(text);
                        break;
                    }
                    catch(Throwable e) {
                        Log.e(("Failed set clickable text " + i), e);
                        ++i;
                    }
                }
                DefensiveURLSpan.fixTextView(view);
            }
        });
    }

    public static void setComponentEnabledSetting(int label, ComponentName cn, int state) {
        try {
            PackageManager packageManager0 = Tools.getPackageManager();
            Log.d(("hide " + label + ": " + state + ' ' + cn));
            Tools.describeActivities(cn.getPackageName(), null);
            packageManager0.setComponentEnabledSetting(cn, state, 1);
            Log.d(("hide " + (label + 1) + ": " + state + ' ' + cn));
            Tools.describeActivities(cn.getPackageName(), null);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static void setHeight(View v, float height) {
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = v.getLayoutParams();
        if(viewGroup$LayoutParams0 != null) {
            viewGroup$LayoutParams0.height = (int)height;
            v.setLayoutParams(viewGroup$LayoutParams0);
        }
    }

    @TargetApi(16)
    public static void setImageAlpha(ImageView image, float alpha) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Build.VERSION.SDK_INT < 16) {
                        image.setAlpha(((int)alpha));
                        return;
                    }
                    try {
                        image.setImageAlpha(((int)alpha));
                    }
                    catch(NoSuchMethodError e) {
                        Log.w("setImageAlpha 1", e);
                        image.setAlpha(((int)alpha));
                    }
                }
                catch(NoSuchMethodError e) {
                    Log.w("setImageAlpha 2", e);
                }
            }
        });
    }

    public static void setOnFocusChangeListener(View v, View.OnFocusChangeListener l) {
        FocusChangeListener p = (FocusChangeListener)v.getTag(0x7F04000D);  // layout:memory_range
        if(p == null) {
            p = new FocusChangeListener(null);
            v.setOnFocusChangeListener(p);
            v.setTag(0x7F04000D, p);  // layout:memory_range
        }
        p.add(l);
    }

    public static void setScrollPos(ListView list, ScrollPos scrollPos) {
        if(scrollPos == null) {
            return;
        }
        Tools.setSelectionFromTop(list, scrollPos.top, scrollPos.offset);
    }

    public static void setSelectionFromTop(ListView list, int pos, int offset) {
        list.setSelectionFromTop(pos, offset);
        list.post(new Runnable() {
            @Override
            public void run() {
                list.setSelectionFromTop(pos, offset);
            }
        });
    }

    public static void setTextAppearance(ListView listView, int textResId) {
        listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override  // android.view.ViewGroup$OnHierarchyChangeListener
            public void onChildViewAdded(View parent, View child) {
                try {
                    View view2 = child.findViewById(0x1020014);
                    if(view2 instanceof TextView && textResId != 0) {
                        Tools.setTextAppearance(((TextView)view2), textResId);
                    }
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }

            @Override  // android.view.ViewGroup$OnHierarchyChangeListener
            public void onChildViewRemoved(View parent, View child) {
            }
        });
    }

    public static void setTextAppearance(TextView tv, int resId) {
        try {
            if(Build.VERSION.SDK_INT >= 23) {
                tv.setTextAppearance(resId);
                return;
            }
            tv.setTextAppearance(tv.getContext(), resId);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    public static void setUseSoundEffects(View view) {
        if((Config.configClient & 0x20) == 0 && view != null) {
            View view1 = Tools.getRootView(view);
            view1.setSoundEffectsEnabled(false);
            if(view1 instanceof ViewGroup) {
                Tools.disableSoundEffects(((ViewGroup)view1));
            }
        }
    }

    private static void setView(View view, WindowManager.LayoutParams params, boolean add) {
        Tools.fixViewParams(params);
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int old;
                Tools.checkPermission();
                try {
                    if(add) {
                        MainService.instance.mWindowManager.addView(view, params);
                    }
                    else {
                        MainService.instance.mWindowManager.updateViewLayout(view, params);
                    }
                }
                catch(WindowManager.BadTokenException e) {
                    String s = e.getMessage();
                    if(SystemConstants.useFloatWindows && s != null && s.contains("permission denied") && params.type != 0x7F6) {
                        try {
                            old = params.type;
                            params.type = 0x7F6;
                            if(add) {
                                MainService.instance.mWindowManager.addView(view, params);
                            }
                            else {
                                MainService.instance.mWindowManager.updateViewLayout(view, params);
                            }
                            goto label_28;
                        }
                        catch(WindowManager.BadTokenException e2) {
                            params.type = old;
                            throw e2;
                        }
                    }
                    throw e;
                }
                catch(IllegalArgumentException e) {
                    Log.w("Fail", e);
                }
                catch(NullPointerException e) {
                    Log.badImplementation(e);
                }
                catch(Throwable e) {
                    Log.w("Fail", e);
                }
            label_28:
                if(add) {
                    Tools.fixViews(view);
                }
            }
        });
    }

    public static void setWidth(View v, float width) {
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = v.getLayoutParams();
        if(viewGroup$LayoutParams0 != null) {
            viewGroup$LayoutParams0.width = (int)width;
            v.setLayoutParams(viewGroup$LayoutParams0);
        }
    }

    public static void showToast(int resId) {
        Tools.showToast(Re.s(resId), 1);
    }

    public static void showToast(int resId, int time) {
        Tools.showToast(Re.s(resId), time);
    }

    public static void showToast(String text) {
        Tools.showToast(text, 1);
    }

    public static void showToast(String text, int time) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = time;
                if(v == 0) {
                    Toaster.showShort(text);
                    return;
                }
                if(v == 1) {
                    Toaster.showLong(text);
                    return;
                }
                Toaster.show(text);
            }
        });
    }

    public static String stringFormat(String format, Object[] args) {
        String result;
        try {
            result = String.format(AppLocale.getNumberLocale(), format.replace('，', ','), args);
            return result == null ? "Failed call String.format" : result;
        }
        catch(Throwable e) {
            String s2 = "Failed String.format(\'" + format + "\', " + Arrays.toString(args) + ") with \'" + AppLocale.getNumberLocale() + "\' (" + Re.s(0x7F070083) + ") " + Config.get(0x7F0B00B1).value;  // string:lang_code "~~~en_US~~~"
            Log.e(s2, e);
            ExceptionHandler.sendMessage((s2 + "\n\n" + Log.getStackTraceString(e)));
            if(format != null && args != null) {
                String[] arr_s = format.split("%(\\d+\\$)?([\\-#+ 0;\\(])?(\\d+)?(\\.\\d+)?([bBhHsScCdoxXeEfgGaA]|[tT][HIklMSLNpzZsQBbhAaCYyjmdeRTrDFc])", args.length + 1);
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < args.length; ++i) {
                    if(i < arr_s.length) {
                        sb.append(arr_s[i]);
                    }
                    sb.append(args[i]);
                }
                if(args.length < arr_s.length) {
                    sb.append(arr_s[args.length]);
                }
                result = sb.toString().replace("%n", "\n").replace("%%", "%");
                return result == null ? "Failed call String.format" : result;
            }
            return format == null ? "Failed call String.format" : format;
        }
    }

    public static String stringFormatOne(String format, String format2, String def, Object value) {
        String s;
        try {
            s = String.format(AppLocale.getNumberLocale(), format, value);
        }
        catch(Throwable e) {
            s = null;
            Log.w("Format fail 1", e);
        }
        if(s == null) {
            try {
                s = String.format(AppLocale.getNumberLocale(), format2, value);
            }
            catch(Throwable e) {
                Log.w("Format fail 2", e);
            }
            return s == null ? def : s;
        }
        return s == null ? def : s;
    }

    public static String stripColon(int res) {
        return Tools.stripColon(Re.s(res));
    }

    public static String stripColon(String text) {
        return Tools.stripEnd(text, ":");
    }

    private static String stripEnd(String str, String stripChars) {
        if(str != null) {
            int end = str.length();
            if(end != 0) {
                if(stripChars == null) {
                    while(end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                        --end;
                    }
                    return str.substring(0, end);
                }
                if(!stripChars.isEmpty()) {
                    while(end != 0) {
                        int v1 = str.charAt(end - 1);
                        if(stripChars.indexOf(v1) == -1 && !Character.isWhitespace(((char)v1))) {
                            break;
                        }
                        --end;
                    }
                    return str.substring(0, end);
                }
            }
        }
        return str;
    }

    public static String trimDirPath(String text) {
        String s1 = text.trim();
        int last = s1.length();
        for(int i = last - 1; i > 0 && s1.charAt(i) == 0x2F; --i) {
            --last;
        }
        return last >= s1.length() ? s1 : s1.substring(0, last);
    }

    public static Drawable tryCloneIcon(Drawable icon) {
        if(icon == null) {
            return null;
        }
        else {
            try {
                Drawable.ConstantState drawable$ConstantState0 = icon.getConstantState();
                return drawable$ConstantState0 == null ? icon : drawable$ConstantState0.newDrawable();
            }
            catch(Throwable e) {
            }
        }
        Log.badImplementation(e);
        return icon;
    }

    public static boolean unsignedLess(long left, long right) {
        int v2 = left >= right ? 0 : 1;
        if(left < 0L) {
            return right >= 0L ? 1 ^ v2 : v2;
        }
        return right >= 0L ? v2 : 1 ^ v2;
    }

    public static boolean unsignedLessOrEqual(long left, long right) {
        return left == right || Tools.unsignedLess(left, right);
    }

    public static void updateOrientation(Configuration newConfig) {
        int isLandscape = 0;
        if(newConfig == null) {
            try {
                newConfig = Tools.getContext().getResources().getConfiguration();
                goto label_7;
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        else {
        label_7:
            if(newConfig.orientation == 2) {
                isLandscape = 1;
            }
        }
        Tools.isLandscape = isLandscape;
    }

    public static void updateViewLayout(View view, WindowManager.LayoutParams params) {
        View target = view;
        ViewParent viewParent0 = view.getParent();
        if(viewParent0 instanceof ViewWrapper) {
            target = (ViewWrapper)viewParent0;
        }
        Tools.setView(target, params, false);
    }

    public static boolean waitForTimeout(Process process, int seconds) {
        FutureTask futureTask0 = new FutureTask(new Callable() {
            public Boolean call() throws Exception {
                process.waitFor();
                return true;
            }

            @Override
            public Object call() throws Exception {
                return this.call();
            }
        });
        new ThreadEx(futureTask0, "waitForTimeout: " + process).start();
        try {
            return ((Boolean)futureTask0.get(((long)seconds), TimeUnit.SECONDS)).booleanValue();
        }
        catch(Exception unused_ex) {
            return Tools.isExited(process);
        }
    }
}

