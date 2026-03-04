package luaj;

import java.lang.ref.WeakReference;

public class LuaThread extends LuaValue {
    public static class State implements Runnable {
        Varargs args;
        public int bytecodes;
        String error;
        public final LuaValue function;
        private final Globals globals;
        public boolean hookcall;
        public int hookcount;
        public LuaFunction hookfunc;
        public boolean hookline;
        public boolean hookrtrn;
        public int inhook;
        public int lastline;
        final WeakReference lua_thread;
        public int oldpc;
        Varargs result;
        public int status;

        State(Globals globals, LuaThread lua_thread, LuaValue function) {
            this.args = LuaValue.NONE;
            this.result = LuaValue.NONE;
            this.error = null;
            this.status = 0;
            this.globals = globals;
            this.lua_thread = new WeakReference(lua_thread);
            this.function = function;
        }

        public Varargs lua_resume(LuaThread luaThread0, Varargs varargs0) {
            Varargs varargs1;
            LuaThread luaThread1;
            synchronized(this) {
                try {
                    luaThread1 = this.globals.running;
                    try {
                        this.globals.running = luaThread0;
                        this.args = varargs0;
                        if(this.status == 0) {
                            this.status = 2;
                            new StringBuffer().append("Coroutine-");
                            ++LuaThread.coroutine_count;
                            new Thread(this, "Coroutine-1").start();
                        }
                        else {
                            this.notify();
                        }
                        if(luaThread1 != null) {
                            luaThread1.state.status = 3;
                        }
                        this.status = 2;
                        this.wait();
                        if(this.error == null) {
                            varargs1 = LuaValue.varargsOf(LuaValue.TRUE, this.result);
                        }
                        else {
                            LuaString luaString0 = LuaValue.valueOf(this.error);
                            varargs1 = LuaValue.varargsOf(LuaValue.FALSE, luaString0);
                        }
                        this.args = LuaValue.NONE;
                        this.result = LuaValue.NONE;
                        this.error = null;
                        this.globals.running = luaThread1;
                        if(luaThread1 != null) {
                            this.globals.running.state.status = 2;
                        }
                        return varargs1;
                    }
                    catch(InterruptedException unused_ex) {
                        throw new OrphanedThread();
                    }
                }
                catch(Throwable throwable0) {
                    this.args = LuaValue.NONE;
                    this.result = LuaValue.NONE;
                    this.error = null;
                    this.globals.running = luaThread1;
                    if(luaThread1 != null) {
                        this.globals.running.state.status = 2;
                    }
                    throw throwable0;
                }
            }
        }

        public Varargs lua_yield(Varargs varargs0) {
            Varargs varargs1;
            __monitor_enter(this);
            try {
                try {
                    this.result = varargs0;
                    this.status = 1;
                    this.notify();
                    do {
                        this.wait(LuaThread.thread_orphan_check_interval);
                        if(this.lua_thread.get() == null) {
                            this.status = 4;
                            throw new OrphanedThread();
                        }
                    }
                    while(this.status == 1);
                    varargs1 = this.args;
                    this.args = LuaValue.NONE;
                    this.result = LuaValue.NONE;
                    goto label_22;
                }
                catch(InterruptedException unused_ex) {
                }
                this.status = 4;
                throw new OrphanedThread();
            }
            catch(Throwable throwable0) {
                try {
                    this.args = LuaValue.NONE;
                    this.result = LuaValue.NONE;
                    throw throwable0;
                }
                catch(Throwable throwable1) {
                    __monitor_exit(this);
                    throw throwable1;
                }
            }
        label_22:
            __monitor_exit(this);
            return varargs1;
        }

        @Override
        public void run() {
            synchronized(this) {
                try {
                    Varargs a = this.args;
                    this.args = LuaValue.NONE;
                    this.result = this.function.invoke(a);
                }
                catch(Throwable t) {
                    try {
                        this.error = t.getMessage();
                    }
                    catch(Throwable throwable1) {
                        this.status = 4;
                        this.notify();
                        throw throwable1;
                    }
                }
            }
            this.status = 4;
            this.notify();
        }
    }

    public static final int MAX_CALLSTACK = 0x100;
    public static final int STATUS_DEAD = 4;
    public static final int STATUS_INITIAL = 0;
    public static final String[] STATUS_NAMES = null;
    public static final int STATUS_NORMAL = 3;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_SUSPENDED = 1;
    public Object callstack;
    public static int coroutine_count = 0;
    public LuaValue errorfunc;
    public final Globals globals;
    public static LuaValue s_metatable = null;
    public final State state;
    public static final long thread_orphan_check_interval = 5000L;

    static {
        LuaThread.coroutine_count = 0;
        LuaThread.STATUS_NAMES = new String[]{"suspended", "suspended", "running", "normal", "dead"};
    }

    public LuaThread(Globals globals) {
        this.state = new State(globals, this, null);
        this.state.status = 2;
        this.globals = globals;
    }

    public LuaThread(Globals globals, LuaValue func) {
        LuaValue.assert_(func != null, "function cannot be null");
        this.state = new State(globals, this, func);
        this.globals = globals;
    }

    @Override  // luaj.LuaValue
    public LuaThread checkthread() {
        return this;
    }

    public String getStatus() {
        return LuaThread.STATUS_NAMES[this.state.status];
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return LuaThread.s_metatable;
    }

    public boolean isMainThread() {
        return this.state.function == null;
    }

    @Override  // luaj.LuaValue
    public boolean isthread() {
        return true;
    }

    @Override  // luaj.LuaValue
    public LuaThread optthread(LuaThread defval) {
        return this;
    }

    public Varargs resume(Varargs varargs0) {
        State luaThread$State0 = this.state;
        if(luaThread$State0.status > 1) {
            new StringBuffer();
            new StringBuffer().append("cannot resume ");
            return luaThread$State0.status == 4 ? LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf("cannot resume dead coroutine")) : LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf("cannot resume non-suspended coroutine"));
        }
        return luaThread$State0.lua_resume(this, varargs0);
    }

    @Override  // luaj.LuaValue
    public int type() {
        return 8;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "thread";
    }
}

