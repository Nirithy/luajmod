package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.setProcessX extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 0;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        AppDetector.processListWindow = Boolean.TRUE;
        MainService.instance.detectApp(true);
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.setProcessX() -> nil";
    }
}

