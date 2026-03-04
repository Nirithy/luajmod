package android.ext;

import android.content.Intent;
import android.net.Uri;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.gotoBrowser extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs args) {
        Tools.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(args.checkjstring(1))));
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.gotoBrowser(string url) -> nil";
    }
}

