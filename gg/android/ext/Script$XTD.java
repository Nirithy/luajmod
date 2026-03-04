package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.XTD extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        long v = varargs0.checklong(1);
        String s = AddressItem.getStringDataTrim(v, MainService.instance.mDaemonManager.getMemoryContent(v, 0x20), 8);
        if(s.indexOf("") > 0) {
            s = s.replace(",", "");
        }
        return LuaValue.valueOf(LuaValue.valueOf(s).tolong() ^ v);
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.XTD(long address,long xor) -> number";
    }
}

