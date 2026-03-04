package luaj;

public final class LuaBoolean extends LuaValue {
    public static LuaValue s_metatable;
    public final boolean v;

    LuaBoolean(boolean b) {
        this.v = b;
    }

    public boolean booleanValue() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public boolean checkboolean() {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return LuaBoolean.s_metatable;
    }

    @Override  // luaj.LuaValue
    public boolean isboolean() {
        return true;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public LuaValue not() {
        return this.v ? LuaBoolean.FALSE : LuaValue.TRUE;
    }

    @Override  // luaj.LuaValue
    public boolean optboolean(boolean defval) {
        return this.v;
    }

    @Override  // luaj.LuaValue
    public boolean toboolean() {
        return this.v;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public String tojstring() {
        return this.v ? "true" : "false";
    }

    @Override  // luaj.LuaValue
    public int type() {
        return 1;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "boolean";
    }
}

