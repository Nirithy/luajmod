package android.ext;

import android.content.res.Resources;
import android.fix.SparseArray;
import android.os.Handler;
import android.sup.ContainerHelpers;
import com.ggdqo.R.string;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Re {
    static volatile SparseArray cache;
    private static final Runnable clear;
    static int[] fix;
    static volatile long lastUse;
    private static Pattern p;
    private static volatile WeakReference weakRes;

    static {
        Re.weakRes = new WeakReference(null);
        Re.cache = new SparseArray();
        Re.fix = ContainerHelpers.EMPTY_INTS;
        Re.lastUse = 0L;
        Re.clear = new Runnable() {
            @Override
            public void run() {
                if(Re.lastUse != 0L && System.currentTimeMillis() - Re.lastUse > 5000L) {
                    Re.clearCache();
                }
                Handler handler0 = ThreadManager.getHandlerUiThread();
                try {
                    handler0.removeCallbacks(this);
                }
                catch(NullPointerException e) {
                    Log.badImplementation(e);
                }
                handler0.postDelayed(this, 1000L);
            }
        };
        Re.p = Pattern.compile("__([a-z0-9_]+?)__");
    }

    public static void clearCache() {
        SparseArray old = Re.cache;
        if(old.size() <= Re.fix.length) {
            return;
        }
        SparseArray cache = new SparseArray();
        int[] arr_v = Re.fix;
        for(int v = 0; v < arr_v.length; ++v) {
            int i = arr_v[v];
            String val = (String)old.syncGet(i);
            if(val != null) {
                cache.put(i, val);
            }
        }
        Re.cache = cache;
        Re.lastUse = 0L;
    }

    public static String getName(int id) {
        try {
            return Re.getRes().getResourceEntryName(id);
        }
        catch(Throwable e) {
            String out = "Resource name for id 0x" + ToolsLight.prefixLongHex(8, ((long)id)) + " not found";
            Log.e(out, e);
            return out;
        }
    }

    private static Resources getRes() {
        Resources ret = (Resources)Re.weakRes.get();
        if(ret == null) {
            ret = Tools.getContext().getResources();
            Re.weakRes = new WeakReference(ret);
            Re.clear.run();
            SparseArray cache = new SparseArray();
            ArrayList fix = Re.fix.length == 0 ? new ArrayList() : null;
            int v = Re.i(Tools.removeNewLinesChars("dssbqdph"), string.class);
            String s = Tools.removeNewLinesChars("JdphJxdugldq").intern();
            if(fix != null) {
                fix.add(v);
            }
            cache.put(v, s);
            try {
                AppLocale.getNumberLocale();
                Re.s_(0x7F070006);  // string:d "%,d"
            }
            catch(Exception e) {
                Log.e("Check format", e);
                if(fix != null) {
                    fix.add(0x7F070006);  // string:d "%,d"
                }
                cache.put(0x7F070006, "%d");  // string:d "%,d"
            }
            String s1 = Tools.removeNewLinesChars("kwws=22jdphjxdugldq1qhw2iruxp");
            if(fix != null) {
                fix.add(0x7F07003C);  // string:forum "http://gameguardian.net/forum"
            }
            cache.put(0x7F07003C, s1);  // string:forum "http://gameguardian.net/forum"
            if(fix != null) {
                fix.add(0x7F07004E);  // string:decimal "."
            }
            cache.put(0x7F07004E, String.valueOf(ParserNumbers.decimalSeparator));  // string:decimal "."
            if(fix != null) {
                fix.add(0x7F07004F);  // string:thousands ","
            }
            cache.put(0x7F07004F, ParserNumbers.thousandSeparator);  // string:thousands ","
            if(fix != null) {
                Re.fix = Tools.intArray(fix);
            }
            Re.cache = cache;
        }
        return ret;
    }

    public static int i(String name, Class class0) {
        try {
            Object object0 = class0.getDeclaredField(name).get(null);
            if(object0 instanceof Integer) {
                return (int)(((Integer)object0));
            }
        }
        catch(Throwable e) {
            Log.w(("Resource id for name \'" + name + "\' not found"), e);
        }
        return 0;
    }

    private static String replaceIncludes(String input) {
        if(input == null) {
            input = "null";
        }
        String out = input;
        if(out.indexOf(0x5F) >= 0) {
            try {
                Class cl = string.class;
                Matcher matcher0 = Re.p.matcher(input);
                while(matcher0.find()) {
                    try {
                        int v = Re.i(matcher0.group(1), cl);
                        if(v == 0) {
                            continue;
                        }
                        out = out.replace(matcher0.group(0), Re.s_(v));
                    }
                    catch(Throwable e) {
                        Log.e(("Exception for: " + input), e);
                    }
                }
            }
            catch(Throwable e) {
                Log.e(("Exception on: " + input), e);
                return out;
            }
        }
        return out;
    }

    public static String s(int id) {
        return Re.s_(id);
    }

    public static String s(String s) {
        return Re.replaceIncludes(s);
    }

    private static String s_(int id) {
        String out;
        if(id == -1 || id == 0) {
            return "(Invalid resource id " + id + ")";
        }
        SparseArray cache = Re.cache;
        Object object0 = cache.syncGet(id);
        String replace = object0 == null ? null : object0.toString();
        if(replace == null) {
            try {
                out = Re.getRes().getString(id);
            }
            catch(Throwable e) {
                out = "Resource for id 0x" + ToolsLight.prefixLongHex(8, ((long)id)) + " not found";
                Log.e(out, e);
            }
            replace = Re.replaceIncludes(out);
            try {
                cache.syncPut(id, replace);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Re.lastUse = System.currentTimeMillis();
            return replace;
        }
        return replace;
    }
}

