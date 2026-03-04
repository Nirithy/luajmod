package android.ext;

import android.content.Context;
import android.content.SharedPreferences;
import android.fix.ContextWrapper;
import android.os.Build.VERSION;
import android.sup.ContainerHelpers;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public class AppLocale {
    private static String CRASHED_VM;
    private static String CRASHED_VM2;
    private static String LOCALE;
    private static volatile String lastLocale;
    private static volatile Locale numberLocale;
    private static final Pattern p;
    private static List updaters;

    static {
        AppLocale.LOCALE = "locale";
        AppLocale.lastLocale = null;
        AppLocale.CRASHED_VM = "crashed-vm";
        AppLocale.CRASHED_VM2 = "crashed-vm2";
        AppLocale.numberLocale = null;
        AppLocale.updaters = new ArrayList();
        Pattern pt = null;
        try {
            pt = Pattern.compile("~~~([-_A-Za-z0-9]{1,10})~~~");
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        AppLocale.p = pt;
    }

    public static void changeLocale(String newLocale) {
        Locale locale0 = AppLocale.getLocaleObject(newLocale);
        Locale.setDefault(locale0);
        ContextWrapper.setOverride(null, locale0);
        AppLocale.update(BaseActivity.context);
        AppLocale.update(BaseActivity.appContext);
        AppLocale.update(MainService.context);
        Log.d(("Loaded locale: " + newLocale));
        AppLocale.updateLocale();
        if(!newLocale.equals(AppLocale.lastLocale)) {
            AppLocale.lastLocale = newLocale;
            Re.clearCache();
        }
    }

    public static String getLocale() {
        try {
            return Tools.getSharedPreferences().getString("locale", Locale.getDefault().toString());
        }
        catch(Throwable e) {
            Log.e("Failed get locale", e);
            return "";
        }
    }

    public static Locale getLocaleObject() {
        return AppLocale.getLocaleObject(AppLocale.getLocale());
    }

    public static Locale getLocaleObject(String strLocale) {
        String[] arr_s = strLocale.split("[-_]");
        return arr_s.length <= 1 ? new Locale(strLocale) : new Locale(arr_s[0], arr_s[1]);
    }

    public static String[] getLocales() {
        Locale[] locs;
        String[] list = ContainerHelpers.EMPTY_STRINGS;
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        if(!sharedPreferences0.getBoolean("crashed-vm", false)) {
            sharedPreferences0.edit().putBoolean("crashed-vm", true).commit();
            list = Tools.getContext().getAssets().getLocales();
            sharedPreferences0.edit().remove("crashed-vm").commit();
        }
        ArrayList locales = new ArrayList();
        locales.add("");
        for(int v = 0; true; ++v) {
            locs = null;
            if(v >= list.length) {
                break;
            }
            String item = list[v];
            if(item != null && !locales.contains(item)) {
                locales.add(item);
            }
        }
        if(!sharedPreferences0.getBoolean("crashed-vm2", false)) {
            sharedPreferences0.edit().putBoolean("crashed-vm2", true).commit();
            locs = Locale.getAvailableLocales();
            sharedPreferences0.edit().remove("crashed-vm2").commit();
        }
        if(locs != null) {
            for(int v1 = 0; v1 < locs.length; ++v1) {
                Locale loc = locs[v1];
                if(loc != null) {
                    String s1 = loc.getCountry();
                    String s2 = loc.getLanguage();
                    String item1 = s1 != null && s1.length() != 0 ? String.valueOf(s2) + '_' + s1 : s2;
                    String item2 = s1 != null && s1.length() != 0 ? String.valueOf(s2) + '-' + s1 : s2;
                    if(!locales.contains(item1) && !locales.contains(item2)) {
                        if(Build.VERSION.SDK_INT >= 21) {
                            item1 = item2;
                        }
                        locales.add(item1);
                    }
                }
            }
        }
        if(AppLocale.p != null) {
            try {
                ZipFile zipFile0 = new ZipFile(Tools.getContext().getPackageResourcePath());
                InputStream inputStream0 = zipFile0.getInputStream(zipFile0.getEntry("resources.arsc"));
                byte[] buffer = new byte[0x400];
                int count;
                for(String prefix = ""; (count = inputStream0.read(buffer)) != -1; prefix = s6.substring(0, Math.min(40, s6.length()))) {
                    String s6 = new String(buffer, 0, count, Charset.forName("US-ASCII"));
                    if(s6.indexOf(0x7E) != -1) {
                        Matcher matcher0 = AppLocale.p.matcher(prefix + s6);
                        while(matcher0.find()) {
                            String item1 = matcher0.group(1);
                            String s8 = item1.replace('_', '-');
                            if(!locales.contains(item1) && !locales.contains(s8)) {
                                if(Build.VERSION.SDK_INT >= 21) {
                                    item1 = s8;
                                }
                                locales.add(item1);
                            }
                        }
                    }
                }
                inputStream0.close();
                zipFile0.close();
                return (String[])locales.toArray(new String[locales.size()]);
            }
            catch(Throwable e) {
                Log.e("Failed load locales from apk", e);
                return (String[])locales.toArray(new String[locales.size()]);
            }
        }
        return (String[])locales.toArray(new String[locales.size()]);
    }

    public static Locale getNumberLocale() {
        Locale locale = AppLocale.numberLocale;
        if(locale == null) {
            locale = AppLocale.getLocaleObject();
            AppLocale.numberLocale = locale;
        }
        return locale;
    }

    public static void loadLocale() {
        try {
            AppLocale.changeLocale(AppLocale.getLocale());
        }
        catch(Throwable e) {
            Log.e("Failed load locale", e);
        }
    }

    public static void registerClass(Class cl) {
        AppLocale.updaters.add(cl);
    }

    public static void saveLocale(String locale) {
        new SPEditor().putString("locale", locale).commit();
        AppLocale.changeLocale(locale);
    }

    public static void setNumberLocale(Locale locale) {
        AppLocale.numberLocale = locale;
    }

    private static void update(Context context) {
        if(context == null) {
            return;
        }
        context.getResources();
    }

    private static void updateLocale() {
        for(Object cl: AppLocale.updaters) {
            Class cl = (Class)cl;
            try {
                cl.getMethod("updateLocale", null).invoke(null, null);
            }
            catch(Throwable e) {
                Log.w(("Failed call updateLocale for " + cl.getName()), e);
            }
        }
    }
}

