package android.ext;

import android.content.Intent;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.jumpAPP extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        Intent intent0 = Tools.getPackageManager().getLaunchIntentForPackage(varargs0.checkjstring(1));
        if(intent0 == null) {
            return LuaValue.FALSE;
        }
        Tools.getContext().startActivity(intent0);
        return LuaValue.TRUE;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.jumpAPP(string package) -> bool";
    }
}

