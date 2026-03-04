package android.ext;

import android.content.SharedPreferences;
import android.os.Process;
import android.sup.ContainerHelpers;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class CheckNativeCrash {
    private static final String BUGGED_PACKAGES;
    private static final String BUGGED_UIDS;
    private static final String DELIMITER;
    private static final String FILENAME;
    private static final boolean IS_CM13;
    private static volatile String buggedPackages;
    private static volatile String buggedUids;

    static {
        CheckNativeCrash.buggedPackages = ";;";
        CheckNativeCrash.buggedUids = ";;";
        CheckNativeCrash.IS_CM13 = CheckNativeCrash.isCm13();
        CheckNativeCrash.BUGGED_PACKAGES = "bugged-packages";
        CheckNativeCrash.BUGGED_UIDS = "bugged-uids";
        CheckNativeCrash.FILENAME = "native_crash.txt";
        CheckNativeCrash.DELIMITER = " _QAZ_WSX_EDC_RFV_TGB_YHN_UJM_ ";
    }

    public static boolean enter(String pkg, String info) {
        if(CheckNativeCrash.IS_CM13 && (!Apk.PACKAGE.equals(pkg) && (!pkg.startsWith("uid:") || !pkg.equals("uid:" + Process.myUid())))) {
            try {
                Log.d(("CNC[" + ((char)(CheckNativeCrash.IS_CM13 ? 49 : 0x30)) + "] - n: " + info + "; " + pkg));
                if(CheckNativeCrash.IS_CM13) {
                    FileOutputStream fileOutputStream0 = new FileOutputStream(CheckNativeCrash.getFile());
                    fileOutputStream0.write(pkg.getBytes());
                    fileOutputStream0.write(" _QAZ_WSX_EDC_RFV_TGB_YHN_UJM_ ".getBytes());
                    fileOutputStream0.write((String.valueOf(info) + '\n' + Debug.getInfo()).getBytes());
                    fileOutputStream0.flush();
                    fileOutputStream0.close();
                }
            }
            catch(Throwable e) {
                Log.e("Failed n CNC", e);
            }
            return true;
        }
        return false;
    }

    public static void exit(boolean entered) {
        if(CheckNativeCrash.IS_CM13 && entered) {
            try {
                Log.d(("CNC[" + ((char)(CheckNativeCrash.IS_CM13 ? 49 : 0x30)) + "] - x"));
                if(CheckNativeCrash.IS_CM13) {
                    CheckNativeCrash.getFile().delete();
                    return;
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        else {
            return;
        }
        Log.e("Failed x CNC", e);
    }

    public static String[] getBuggedPackages() {
        if(CheckNativeCrash.IS_CM13) {
            String s = CheckNativeCrash.getBuggedPackagesString();
            if(s.length() > 0) {
                Log.d(("BUGGED_PACKAGES: " + s));
                return s.split(";");
            }
        }
        return ContainerHelpers.EMPTY_STRINGS;
    }

    private static String getBuggedPackagesString() {
        String s = CheckNativeCrash.unique(Tools.getSharedPreferences().getString("bugged-packages", ""));
        if(s.length() > 0) {
            CheckNativeCrash.buggedPackages = ";" + s + ';';
        }
        return s;
    }

    public static String[] getBuggedUids() {
        if(CheckNativeCrash.IS_CM13) {
            String s = CheckNativeCrash.getBuggedUidsString();
            if(s.length() > 0) {
                Log.d(("BUGGED_UIDS: " + s));
                return s.split(";");
            }
        }
        return ContainerHelpers.EMPTY_STRINGS;
    }

    private static String getBuggedUidsString() {
        String s = CheckNativeCrash.unique(Tools.getSharedPreferences().getString("bugged-uids", ""));
        if(s.length() > 0) {
            CheckNativeCrash.buggedUids = ";" + s + ';';
        }
        return s;
    }

    private static File getFile() {
        File file0 = new File(Tools.getFilesDirHidden(), "5897native_crash.txt");
        try {
            file0.getParentFile().mkdirs();
        }
        catch(Throwable e) {
            Log.e("Failed mkdirs CNC", e);
        }
        return file0;
    }

    private static String[] getLastCrash() {
        String[] ret = null;
        File file0 = CheckNativeCrash.getFile().getParentFile();
        if(file0 != null) {
            File[] arr_file = file0.listFiles();
            if(arr_file != null) {
                ret = new String[arr_file.length * 2];
                for(int i = 0; i < arr_file.length; ++i) {
                    File file = arr_file[i];
                    if(file != null && file.getName().endsWith("native_crash.txt")) {
                        try {
                            FileInputStream is = new FileInputStream(file);
                            byte[] data = new byte[Math.min(((int)file.length()) + 0x2000, 0x19000)];
                            int v1 = is.read(data);
                            is.close();
                            if(v1 > 0) {
                                String[] arr_s1 = new String(data, 0, v1).split(" _QAZ_WSX_EDC_RFV_TGB_YHN_UJM_ ", 2);
                                if(arr_s1.length == 2) {
                                    ret[i * 2] = arr_s1[0];
                                    ret[i * 2 + 1] = arr_s1[1];
                                }
                            }
                        }
                        catch(Throwable e) {
                            Log.e("Failed read native crash", e);
                        }
                        file.delete();
                    }
                }
            }
        }
        return ret;
    }

    // 去混淆评级： 中等(50)
    public static boolean isBuggedPackage(String pkg) {
        return CheckNativeCrash.IS_CM13 ? ";;".contains(";" + pkg + ';') : false;
    }

    // 去混淆评级： 中等(50)
    public static boolean isBuggedUid(String uid) {
        return CheckNativeCrash.IS_CM13 ? ";;".contains(";" + uid + ';') : false;
    }

    private static boolean isCm13() {
        int size;
        boolean ret = false;
        try {
            File file0 = new File("/system/lib/libandroidfw.so");
            if(file0.exists()) {
                FileInputStream is = new FileInputStream(file0);
                byte[] arr_b = "_ZN7android12AssetManager10getPkgNameEPKc".getBytes();
                byte[] buffer = new byte[0x2000];
                int offset = 0;
                while(true) {
                    int v1 = is.read(buffer, offset, 0x2000 - offset);
                    if(v1 > 0) {
                        size = offset + v1;
                        if(size >= arr_b.length) {
                            int check = size - arr_b.length + 1;
                            int i = 0;
                            while(i < check) {
                                boolean found = true;
                                int j = 0;
                                while(j < arr_b.length) {
                                    if(buffer[i + j] == arr_b[j]) {
                                        ++j;
                                    }
                                    else {
                                        found = false;
                                        if(true) {
                                            break;
                                        }
                                    }
                                }
                                if(found) {
                                    ret = true;
                                    break;
                                }
                                else {
                                    ++i;
                                    continue;
                                }
                                goto label_35;
                            }
                            if(!ret) {
                                offset = arr_b.length - 1;
                                for(int j = 0; j < offset; ++j) {
                                    buffer[j] = buffer[size - offset + j];
                                }
                                continue;
                            }
                        }
                    }
                    else {
                    label_35:
                        is.close();
                        break;
                    }
                    offset = size;
                }
            }
        }
        catch(Throwable e) {
            Log.w("Failed detect CM13", e);
        }
        Log.d(("CM13: " + ret));
        return ret;
    }

    public static void loadLastCrash() {
        Log.d("lLC - 1");
        CheckNativeCrash.getBuggedPackagesString();
        CheckNativeCrash.getBuggedUidsString();
        String[] arr_s = CheckNativeCrash.getLastCrash();
        if(arr_s != null) {
            for(int i = 0; i < arr_s.length; i += 2) {
                String bad = arr_s[i];
                if(bad != null) {
                    Log.e(("Bad package: \'" + bad + "\' " + arr_s[i + 1]));
                    String key = "bugged-packages";
                    if(bad.contains("uid:")) {
                        key = "bugged-uids";
                        bad = bad.split(":", -1)[1];
                    }
                    SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
                    String bads = sharedPreferences0.getString(key, "");
                    if(bads == null || bads.length() == 0) {
                        bads = bad;
                    }
                    else if(!(";" + bads + ';').contains(";" + bad + ';')) {
                        bads = String.valueOf(bads) + ';' + bad;
                    }
                    sharedPreferences0.edit().putString(key, CheckNativeCrash.unique(bads)).commit();
                }
            }
        }
        CheckNativeCrash.getBuggedPackagesString();
        CheckNativeCrash.getBuggedUidsString();
        Log.d("lLC - 0");
    }

    private static String unique(String list) {
        ArrayList arrayList0 = new ArrayList(new HashSet(Arrays.asList(list.split(";"))));
        return TextUtils.join(";", arrayList0.toArray(new String[arrayList0.size()]));
    }
}

