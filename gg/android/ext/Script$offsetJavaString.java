package android.ext;

import luaj.LuaError;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.offsetJavaString extends ApiFunction {
    public long j;
    public long result;
    public String s;

    public Script.offsetJavaString(Script script0) {
        Script.this = script0;
    }

    static Script access$0(Script.offsetJavaString script$offsetJavaString0) {
        return Script.this;
    }

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invokeUi(Varargs varargs0) {
        try {
            this.j = varargs0.checklong(1);
            this.result = varargs0.checklong(2) + this.j;
            this.j = this.result;
            ThreadManager.runOnUiThread(new Runnable() {
                {
                    Script.offsetJavaString.this = script$offsetJavaString0;
                }

                static Script.offsetJavaString access$0(android.ext.Script.offsetJavaString.1 script$offsetJavaString$10) {
                    return Script.offsetJavaString.this;
                }

                @Override
                public void run() {
                    Script.offsetJavaString.this.s = MemoryContentAdapter.longToJava(MainService.instance.mDaemonManager.getMemoryContent(Script.offsetJavaString.this.j, AddressItem.getTypeForAddress(Script.offsetJavaString.this.j, true)), 8);
                }
            });
            return this.s != null ? LuaValue.valueOf(this.s) : LuaValue.NIL;
        }
        catch(LuaError luaError0) {
            throw luaError0;
        }
        catch(Throwable throwable0) {
            return LuaValue.valueOf(Script.toString(throwable0));
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.offsetString(string address,string offset) -> string";
    }
}

