package android.ext;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.getSignatures extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 0;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        try {
            Context context0 = Tools.getContext();
            return LuaValue.valueOf(context0.getApplicationContext().getPackageManager().getPackageInfo(context0.getApplicationInfo().packageName, 0x40).signatures[0].hashCode());
        }
        catch(PackageManager.NameNotFoundException unused_ex) {
            return LuaValue.NIL;
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getSignatures() -> long or nil";
    }
}

