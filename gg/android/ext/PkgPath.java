package android.ext;

import java.io.File;

public class PkgPath {
    public static String load(String s, String s1, String s2) {
        ProcessInfo processList$ProcessInfo0 = MainService.instance.processInfo;
        String s3 = processList$ProcessInfo0 == null ? Apk.PACKAGE : processList$ProcessInfo0.packageName;
        String s4 = Tools.getSharedPreferences().getString(s3 + s1, null);
        return s4 == null ? PkgPath.path(null, s) + '/' + s3 + s2 : s4;
    }

    public static String path(String s, String s1) {
        String s2 = Tools.getSdcardPath() + "/Notes";
        if(s == null) {
            return Tools.getSharedPreferences().getString(s1, s2);
        }
        new SPEditor().putString(s1, s, s2).commit();
        return null;
    }

    public static void save(String s, String s1, String s2, String s3) {
        File file0 = new File(s.trim());
        String s4 = file0.getParent();
        if(s1 != null && s4 != null && s4.length() > 0) {
            PkgPath.path(s4, s1);
        }
        ProcessInfo processList$ProcessInfo0 = MainService.instance.processInfo;
        String s5 = processList$ProcessInfo0 == null ? Apk.PACKAGE : processList$ProcessInfo0.packageName;
        new SPEditor().putString(s5 + s2, file0.getAbsolutePath(), PkgPath.path(null, s1) + '/' + s5 + s3).commit();
    }
}

