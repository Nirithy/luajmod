package luaj;

interface Metatable {
    LuaValue arrayget(LuaValue[] arg1, int arg2);

    Slot entry(LuaValue arg1, LuaValue arg2);

    LuaValue toLuaValue();

    boolean useWeakKeys();

    boolean useWeakValues();

    LuaValue wrap(LuaValue arg1);
}

