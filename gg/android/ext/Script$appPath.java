package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.appPath extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        try {
            return LuaValue.valueOf(Tools.getContext().getPackageManager().getApplicationInfo(varargs0.checkjstring(1), 0).sourceDir);
        }
        catch(Exception unused_ex) {
            return LuaValue.NIL;
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.appPath() -> nil";
    }
}

