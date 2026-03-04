package luaj;

import luaj.lib.DebugLib;

public class TailcallVarargs extends Varargs {
    private Varargs args;
    private LuaValue func;
    private final Globals globals;
    private Varargs result;

    public TailcallVarargs(Globals globals, LuaValue f, Varargs args) {
        this.globals = globals;
        this.func = f;
        this.args = args;
    }

    @Override  // luaj.Varargs
    public LuaValue arg(int i) {
        if(this.result == null) {
            this.eval();
        }
        return this.result.arg(i);
    }

    @Override  // luaj.Varargs
    public LuaValue arg1() {
        if(this.result == null) {
            this.eval();
        }
        return this.result.arg1();
    }

    @Override  // luaj.Varargs
    public Varargs eval() {
        Varargs result = this.result;
        if(result == null) {
            DebugLib debuglib = this.globals == null ? null : this.globals.debuglib;
            LuaValue func = this.func;
            Varargs args = this.args;
            while(result == null) {
                if(debuglib != null && !func.isclosure() && func.isfunction()) {
                    debuglib.onCall(func.checkfunction(), true);
                }
                Varargs varargs2 = func.onInvoke(args, true);
                if(varargs2.isTailcall()) {
                    func = ((TailcallVarargs)varargs2).func;
                    this.func = func;
                    args = ((TailcallVarargs)varargs2).args;
                    this.args = args;
                }
                else {
                    result = varargs2;
                    this.result = varargs2;
                    this.func = null;
                    this.args = null;
                }
            }
        }
        return result;
    }

    @Override  // luaj.Varargs
    public boolean isTailcall() {
        return true;
    }

    @Override  // luaj.Varargs
    public int narg() {
        if(this.result == null) {
            this.eval();
        }
        return this.result.narg();
    }

    @Override  // luaj.Varargs
    public Varargs subargs(int start) {
        if(this.result == null) {
            this.eval();
        }
        return this.result.subargs(start);
    }
}

