package android.ext;

public class SystemConstants {
    public static final int JUMP_ITEM_COLOR = 0x80949442;
    public static final int SELECTED_ITEM_COLOR = 0x80744294;
    public static final boolean useEdgeGlow = true;
    public static volatile boolean useFloatWindows = false;
    public static final boolean useInstrumentation = true;

    static {
        SystemConstants.useFloatWindows = true;
    }

    public static int getFlags() [...] // 潜在的解密器

    public static int getTypePhone() [...] // 潜在的解密器

    public static int getTypeSystemAlert() [...] // 潜在的解密器
}

