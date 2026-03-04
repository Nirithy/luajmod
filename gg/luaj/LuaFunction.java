package luaj;

import java.util.Locale;

public abstract class LuaFunction extends LuaValue {
    private static final String GG_LIB;
    public static LuaValue s_metatable;

    static {
        LuaFunction.GG_LIB = "android.ext.Script";
    }

    @Override  // luaj.LuaValue
    public LuaFunction checkfunction() {
        return this;
    }

    public String classnamestub() [...] // 潜在的解密器

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return LuaFunction.s_metatable;
    }

    @Override  // luaj.LuaValue
    public boolean isfunction() [...] // Inlined contents

    @Override  // luaj.LuaValue
    public static void main(String[] args) throws Throwable {
    }

    public static String name(String className) {
        int v = className.lastIndexOf(36);
        int v1 = className.lastIndexOf(46);
        int offset = Math.max(v1, v) + 1;
        String ret = className.substring((className.charAt(offset) == 0x5F ? offset + 1 : Math.max(v1, v) + 1));
        if(className.startsWith("android.ext.Script")) {
            return "gg." + ret;
        }
        if(v > v1 && className.contains("Lib$") && !className.contains("BaseLib$")) {
            String lib = className.substring(v1 + 1, v - 3).toLowerCase(Locale.ENGLISH) + '.';
            return lib == null ? ret : lib + ret;
        }
        return ret;
    }

    // 去混淆评级： 低(20)
    public String name() {
        return "LuaFunction";
    }

    @Override  // luaj.LuaValue
    public LuaFunction optfunction(LuaFunction defval) {
        return this;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public LuaString strvalue() {
        return LuaFunction.valueOf("function: LuaFunction");
    }

    // 去混淆评级： 低(40)
    @Override  // luaj.LuaValue
    public String tojstring() [...] // 潜在的解密器

    @Override  // luaj.LuaValue
    public int type() {
        return 6;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "function";
    }
}

