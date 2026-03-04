package android.ext;

import com.ggdqo.MainActivity;

public class Apk {
    public static float ARM_VERSION = 0.0f;
    public static final int BUILD = 0x3E5D;
    public static final String INSTALLER_LABEL = null;
    public static final String INSTALLER_PACKAGE = null;
    public static final String LABEL = null;
    public static final String LABEL_DELIMITER = "Lcar$";
    private static final String LABEL_INTERNAL = "Lcar$Nqmvlepftz~n~Lcar$";
    public static final String PACKAGE = null;
    public static final float VERSION = 95.0f;

    static {
        String pkg;
        try {
            Apk.ARM_VERSION = 95.0f;
            Apk.INSTALLER_PACKAGE = "catch_.me_.if_.you_.can_";
            Apk.INSTALLER_LABEL = "GameGuardian";
            pkg = MainActivity.class.getPackage().getName();
        }
        catch(Throwable e) {
            pkg = "com.ggdqo~~~~~~~~~~~~~~~";
            Log.badImplementation(e);
        }
        Apk.PACKAGE = pkg.intern();
        Apk.LABEL = "Nqmvlepftz";
    }

    public static final void init() {
    }
}

