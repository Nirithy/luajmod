package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.getWindowOrientation extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        return LuaValue.valueOf(Tools.isLandscape());
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getWindowOrientation() -> int";
    }
}

