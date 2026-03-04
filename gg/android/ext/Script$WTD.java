package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.WTD extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        long v = varargs0.checklong(1);
        String s = AddressItem.getStringDataTrim(v + 2L, MainService.instance.mDaemonManager.getMemoryContent(v + 2L, 0x20), 2);
        if(s.indexOf(",") > 0) {
            s = s.replace(",", "");
        }
        return LuaValue.valueOf(LuaValue.valueOf(s).tolong() * 0x10000L + varargs0.checklong(2));
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.WTD(long address,long word) -> number";
    }
}

