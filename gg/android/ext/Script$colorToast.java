package android.ext;

import android.text.Html;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.colorToast extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        android.ext.Script.colorToast.1.showToast(Html.fromHtml(varargs0.checkjstring(1)), varargs0.optint(2, 0));
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.colorToast(string text [, int time = 0]) -> nil";
    }
}

