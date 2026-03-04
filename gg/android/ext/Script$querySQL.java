package android.ext;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import luaj.LuaTable;
import luaj.Varargs;

final class Script.querySQL extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 3;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        SQLiteDatabase sQLiteDatabase0 = SQLiteDatabase.openOrCreateDatabase(varargs0.checkjstring(1), null);
        Cursor cursor0 = sQLiteDatabase0.rawQuery(varargs0.checkjstring(2), null);
        Varargs varargs1 = new LuaTable();
        int v = 0;
        while(cursor0.moveToNext()) {
            ++v;
            ((LuaTable)varargs1).rawset(v, cursor0.getString(varargs0.checkint(3)));
        }
        cursor0.close();
        sQLiteDatabase0.close();
        return varargs1;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.querySQL(string file,string sql,int id) -> table";
    }
}

