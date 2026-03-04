package android.ext;

import luaj.Varargs;

final class Script.searchChoice extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        return new Script.searchChoice.1().invoke(varargs0, 1);
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.searchChoice(table t, string item) -> boolean table";
    }
}

