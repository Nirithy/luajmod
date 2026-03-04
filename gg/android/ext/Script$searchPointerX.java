package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.searchPointerX extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 4;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        LuaValue luaValue0 = Script.instanceGlobals.get("gg");
        luaValue0.get("setRanges").checkfunction().invoke(varargs0.checkvalue(4));
        luaValue0.get("clearResults").checkfunction().invoke();
        luaValue0.get("searchNumber").checkfunction().invoke(LuaValue.varargsOf(LuaValue.valueOf(varargs0.checktable(1).get(varargs0.checkint(2)).get("address").tolong() + varargs0.checklong(3)), LuaValue.valueOf(4)));
        return luaValue0.get("getResults").checkfunction().invoke(luaValue0.get("getResultsCount").checkfunction().invoke());
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.searchPointerX(table _address_table,int _index,long _offset,int _ranges) -> table";
    }
}

