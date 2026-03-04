package luaj.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import luaj.Globals.Compiler;
import luaj.Globals.Loader;
import luaj.Globals;
import luaj.LuaClosure;
import luaj.LuaFunction;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Prototype;

public class LuaC extends Constants implements Compiler, Loader {
    static class CompileState {
        int nCcalls;
        private Hashtable strings;

        protected CompileState() {
            this.nCcalls = 0;
            this.strings = new Hashtable();
        }

        public LuaString cachedLuaString(LuaString s) {
            LuaString c = (LuaString)this.strings.get(s);
            if(c != null) {
                return c;
            }
            this.strings.put(s, s);
            return s;
        }

        Prototype luaY_parser(InputStream z, String name) throws IOException {
            boolean z = false;
            LexState lexstate = new LexState(this, z);
            FuncState funcstate = new FuncState();
            lexstate.fs = funcstate;
            lexstate.setinput(this, z.read(), z, LuaValue.valueOf(name));
            funcstate.f = new Prototype();
            Prototype prototype0 = funcstate.f;
            prototype0.source = LuaValue.valueOf(name);
            lexstate.mainfunc(funcstate);
            LuaC._assert(funcstate.prev == null);
            if(lexstate.dyd == null || lexstate.dyd.n_actvar == 0 && lexstate.dyd.n_gt == 0 && lexstate.dyd.n_label == 0) {
                z = true;
            }
            LuaC._assert(z);
            return funcstate.f;
        }

        public LuaString newTString(String s) {
            return this.cachedLuaString(LuaString.valueOf(s));
        }

        public LuaString newTString(LuaString s) {
            return this.cachedLuaString(s);
        }

        public String pushfstring(String string) [...] // Inlined contents
    }

    public static final LuaC instance;

    static {
        LuaC.instance = new LuaC();
    }

    @Override  // luaj.Globals$Compiler
    public Prototype compile(InputStream stream, String chunkname) throws IOException {
        return new CompileState().luaY_parser(stream, chunkname);
    }

    public static void install(Globals globals) {
        globals.compiler = LuaC.instance;
        globals.loader = LuaC.instance;
    }

    @Override  // luaj.Globals$Loader
    public LuaFunction load(Prototype prototype, String chunkname, LuaValue env) throws IOException {
        return new LuaClosure(prototype, env, true);
    }
}

