package android.ext;

import java.io.File;

public class Shell {
    private static final String[] systemDirs;

    static {
        Shell.systemDirs = new String[]{"/system/bin/", "/system/xbin/"};
    }

    public static boolean isCmdPresent(String cmd) {
        String[] arr_s = Shell.systemDirs;
        for(int v = 0; v < arr_s.length && !new File(arr_s[v] + cmd).exists(); ++v) {
        }
        return false;
    }
}

