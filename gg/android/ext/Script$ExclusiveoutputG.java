package android.ext;

import java.io.FileWriter;
import luaj.LuaTable.Iterator;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

public final class Script.ExclusiveoutputG extends ApiFunction {
    Script.ExclusiveoutputG(Script script0) {
        Script.this = script0;
    }

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        try {
            int v = 0;
            int v1 = 0;
            FileWriter fileWriter0 = new FileWriter(varargs0.checkjstring(1), true);
            LuaValue luaValue0 = Script.instanceGlobals.get("gg");
            while(true) {
                LuaTable luaTable0 = luaValue0.get("getResults").checkfunction().invoke(LuaValue.valueOf(400000L), LuaValue.valueOf(v)).checktable(1);
                Iterator luaTable$Iterator0 = luaTable0.iterator();
                if(luaTable0.get(1).isnil(1)) {
                    break;
                }
                v += 400000;
                for(int v2 = 1; luaTable$Iterator0.next(); ++v2) {
                    if(luaTable$Iterator0.intkey() != 0) {
                        ++v1;
                        LuaValue luaValue1 = luaTable0.get(v2);
                        fileWriter0.write("Pointer|" + luaValue1.get("address") + "|" + luaValue1.get("value") + "\n");
                    }
                }
            }
            fileWriter0.close();
            return LuaValue.valueOf(v1);
        }
        catch(Exception unused_ex) {
            return LuaValue.valueOf("false");
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.ExclusiveoutputG(String outputpath) -> int";
    }
}

