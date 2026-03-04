package android.ext;

import java.io.FileOutputStream;

public class De {
    private static int CLI = 0;
    public static final boolean bug = false;
    public static final boolean bug_1017 = false;
    public static final boolean bug_1143 = false;
    private static FileOutputStream fos = null;
    public static final boolean off = false;
    public static final boolean on = true;
    public static final boolean test;

    static {
        De.fos = null;
        De.CLI = -1;
    }

    public static boolean CLI() {
        return false;
    }

    public static void log(String msg) {
    }

    public static void main(String[] args) throws Throwable {
    }
}

