package android.ext;

import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.sumAddressX extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 4;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        long v = varargs0.checktable(1).rawget(varargs0.checkint(2)).rawget("address").tolong() + varargs0.checklong(3) & 0xFFFFFFFFL;
        int v1 = varargs0.checkint(4);
        long v2 = MainService.instance.mDaemonManager.getMemoryContent(v, 0x20);
        AddressItem addressItem0 = new AddressItem(v, v2, 4);
        String s = AddressItem.getStringDataTrim(v, v2, v1);
        if(s.indexOf(",") > 0) {
            s = s.replace(",", "");
        }
        LuaTable luaTable0 = new LuaTable();
        Varargs varargs1 = new LuaTable();
        luaTable0.rawset("address", ((double)addressItem0.address));
        luaTable0.rawset("flags", v1);
        luaTable0.rawset("value", LuaValue.numberOf(s));
        ((LuaTable)varargs1).rawset(1, luaTable0);
        return varargs1;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.sumAddressX(table _address_table,int _index,long _offset,int _flags) -> table";
    }
}

