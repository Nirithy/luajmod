package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.FTD extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        double f = varargs0.checkdouble(1);
        String s = "" + ((f - Math.pow(2.0, Math.floor(Math.log(f) / 0.693147))) * Math.pow(2.0, 23.0 - Math.floor(Math.log(f) / 0.693147)) + (1065353216.0 + Math.floor(Math.log(f) / 0.693147) * 8388608.0));
        return s.indexOf("E") < 0 ? LuaValue.valueOf(s).tonumber() : LuaValue.valueOf((s.substring(0, 1) + s.substring(2, 11)).replace("E", "0")).tonumber();
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.FTD(float f) -> number";
    }
}

