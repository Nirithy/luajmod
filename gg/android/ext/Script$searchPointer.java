package android.ext;

import luaj.LuaValue;
import luaj.Varargs;

final class Script.searchPointer extends BusyApiFunction {
    Script.searchPointer(Script script0) {
        Script.this = script0;
        super();
    }

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 4;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invokeUi(Varargs varargs0) {
        try {
            Script.this.resultsLoaded = false;
            int v = (short)varargs0.checkint(1);
            return SearchButtonListener.doSearch(Script.this.getSeq(), ((short)v), varargs0.optlong(2, 0L), varargs0.optlong(3, -1L), varargs0.optlong(4, 0L)) ? LuaValue.TRUE : null;
        }
        catch(NumberFormatException numberFormatException0) {
            return LuaValue.valueOf(Script.toString(numberFormatException0));
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.searchPointer(int maxOffset [, long memoryFrom = 0 [, long memoryTo = -1 [, long limit = 0]]]) -> true || string with error";
    }
}

