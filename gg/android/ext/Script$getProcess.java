package android.ext;

import java.util.List;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.getProcess extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 0;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        LuaTable luaTable0 = new LuaTable();
        AppDetector.processListWindow = Boolean.FALSE;
        MainService.instance.mAppDetector.detectApp(true);
        List list0 = Script.processList;
        if(list0 == null) {
            return LuaValue.FALSE;
        }
        int v = 0;
        for(Object object0: list0) {
            LuaTable luaTable1 = new LuaTable();
            luaTable1.rawset("process", ((ProcessInfo)object0).toString());
            luaTable1.rawset("cmdLine", ((ProcessInfo)object0).cmdline);
            luaTable0.rawset(v, luaTable1);
            ++v;
        }
        return luaTable0;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getProcess() -> boolean || table";
    }
}

