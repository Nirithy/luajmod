package android.ext;

import android.database.sqlite.SQLiteDatabase;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.execSQL extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        SQLiteDatabase sQLiteDatabase0 = SQLiteDatabase.openOrCreateDatabase(varargs0.checkjstring(1), null);
        sQLiteDatabase0.execSQL(varargs0.checkjstring(2));
        sQLiteDatabase0.close();
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.execSQL(string file,string sql) -> nil";
    }
}

