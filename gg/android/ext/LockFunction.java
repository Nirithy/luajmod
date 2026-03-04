package android.ext;

import luaj.LuaError;
import luaj.LuaFunction;
import luaj.LuaTable.Iterator;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

public class LockFunction {
    private static Varargs end;
    public static Object obj;
    private static Varargs v;

    static {
        LockFunction.v = LuaValue.NONE;
        LockFunction.end = LuaValue.NONE;
        LockFunction.obj = new Object();
    }

    public static Varargs LockLog(LuaFunction luaFunction0, LuaValue luaValue0) {
        LockFunction.obj = new Object();
        try {
            synchronized(LockFunction.obj) {
                try {
                    ThreadManager.runOnLogThread(LockFunction.createRun(luaFunction0, luaValue0));
                }
                catch(Exception exception1) {
                    try {
                        LockFunction.obj.notifyAll();
                    }
                    catch(Exception exception2) {
                        throw new LuaError(exception2);
                    }
                    Thread.currentThread().interrupt();
                    throw new LuaError(exception1);
                }
            }
            try {
                LockFunction.obj.wait();
                return LockFunction.v;
            }
            catch(Exception exception3) {
                try {
                    LockFunction.obj.notifyAll();
                }
                catch(Exception exception4) {
                    throw new LuaError(exception4);
                }
                Thread.currentThread().interrupt();
                throw new LuaError(exception3);
            }
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs LockMain(LuaFunction luaFunction0, LuaValue luaValue0) {
        LockFunction.obj = new Object();
        try {
            synchronized(LockFunction.obj) {
                try {
                    ThreadManager.runOnMainThread(LockFunction.createRun(luaFunction0, luaValue0));
                }
                catch(Exception exception1) {
                    try {
                        LockFunction.obj.notifyAll();
                    }
                    catch(Exception exception2) {
                        throw new LuaError(exception2);
                    }
                    Thread.currentThread().interrupt();
                    throw new LuaError(exception1);
                }
            }
            try {
                LockFunction.obj.wait();
                return LockFunction.v;
            }
            catch(Exception exception3) {
                try {
                    LockFunction.obj.notifyAll();
                }
                catch(Exception exception4) {
                    throw new LuaError(exception4);
                }
                Thread.currentThread().interrupt();
                throw new LuaError(exception3);
            }
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs LockUi(LuaFunction luaFunction0, LuaValue luaValue0) {
        LockFunction.obj = new Object();
        try {
            synchronized(LockFunction.obj) {
                try {
                    ThreadManager.runOnUiThread(LockFunction.createRun(luaFunction0, luaValue0));
                }
                catch(Exception exception1) {
                    try {
                        LockFunction.obj.notifyAll();
                    }
                    catch(Exception exception2) {
                        throw new LuaError(exception2);
                    }
                    Thread.currentThread().interrupt();
                    throw new LuaError(exception1);
                }
            }
            try {
                LockFunction.obj.wait();
                return LockFunction.v;
            }
            catch(Exception exception3) {
                try {
                    LockFunction.obj.notifyAll();
                }
                catch(Exception exception4) {
                    throw new LuaError(exception4);
                }
                Thread.currentThread().interrupt();
                throw new LuaError(exception3);
            }
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs LockWrite(LuaFunction luaFunction0, LuaValue luaValue0) {
        LockFunction.obj = new Object();
        try {
            synchronized(LockFunction.obj) {
                try {
                    ThreadManager.runOnWriteThread(LockFunction.createRun(luaFunction0, luaValue0));
                }
                catch(Exception exception1) {
                    try {
                        LockFunction.obj.notifyAll();
                    }
                    catch(Exception exception2) {
                        throw new LuaError(exception2);
                    }
                    Thread.currentThread().interrupt();
                    throw new LuaError(exception1);
                }
            }
            try {
                LockFunction.obj.wait();
                return LockFunction.v;
            }
            catch(Exception exception3) {
                try {
                    LockFunction.obj.notifyAll();
                }
                catch(Exception exception4) {
                    throw new LuaError(exception4);
                }
                Thread.currentThread().interrupt();
                throw new LuaError(exception3);
            }
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs Log(LuaFunction luaFunction0, LuaValue luaValue0) {
        try {
            ThreadManager.runOnLogThread(LockFunction.createRun(luaFunction0, luaValue0));
            return LockFunction.v;
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs Main(LuaFunction luaFunction0, LuaValue luaValue0) {
        try {
            ThreadManager.runOnMainThread(LockFunction.createRun(luaFunction0, luaValue0));
            return LockFunction.v;
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs Ui(LuaFunction luaFunction0, LuaValue luaValue0) {
        try {
            ThreadManager.runOnUiThread(LockFunction.createRun(luaFunction0, luaValue0));
            return LockFunction.v;
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    public static Varargs Write(LuaFunction luaFunction0, LuaValue luaValue0) {
        try {
            ThreadManager.runOnWriteThread(LockFunction.createRun(luaFunction0, luaValue0));
            return LockFunction.v;
        }
        catch(Exception exception0) {
            throw new LuaError(exception0);
        }
    }

    static Varargs access$L1000000() {
        return LockFunction.v;
    }

    public static Runnable createRun(LuaFunction luaFunction0, LuaValue luaValue0) {
        return new Runnable() {
            private final LuaFunction val$f;
            private final LuaValue val$t;

            {
                LuaFunction luaFunction0 = luaFunction0;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                LuaValue luaValue0 = luaValue0;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                this.val$f = luaFunction0;
                this.val$t = luaValue0;
            }

            @Override
            public void run() {
                if(this.val$f == null || this.val$f.isnil()) {
                    LockFunction.v = LuaValue.NIL;
                    return;
                }
                if(this.val$t == null || this.val$t.isnil()) {
                    try {
                        LockFunction.v = this.val$f.invoke();
                        return;
                    }
                    catch(Exception exception0) {
                        Thread.currentThread().interrupt();
                        throw new LuaError(exception0);
                    }
                }
                if(this.val$t.istable()) {
                    LuaTable luaTable0 = this.val$t.checktable(1);
                    LuaValue[] arr_luaValue = new LuaValue[luaTable0.length()];
                    Iterator luaTable$Iterator0 = luaTable0.iterator();
                    int v = -1;
                    while(luaTable$Iterator0.next() && v < luaTable0.length()) {
                        ++v;
                        arr_luaValue[v] = luaTable$Iterator0.value();
                    }
                    try {
                        Varargs varargs0 = LuaValue.varargsOf(arr_luaValue);
                        LockFunction.v = this.val$f.invoke(varargs0);
                        return;
                    }
                    catch(Exception exception1) {
                        Thread.currentThread().interrupt();
                        throw new LuaError(exception1);
                    }
                }
                try {
                    LockFunction.v = this.val$f.invoke(this.val$t);
                }
                catch(Exception exception2) {
                    Thread.currentThread().interrupt();
                    throw new LuaError(exception2);
                }
            }
        };
    }

    public static Varargs unLockLog(LuaFunction luaFunction0, LuaValue luaValue0) {
        return LockFunction.unLockUi(luaFunction0, luaValue0);
    }

    public static Varargs unLockMain(LuaFunction luaFunction0, LuaValue luaValue0) {
        return LockFunction.unLockUi(luaFunction0, luaValue0);
    }

    public static Varargs unLockUi(LuaFunction luaFunction0, LuaValue luaValue0) {
        if(luaFunction0 != null) {
            try {
                if(luaFunction0.isnil()) {
                    Object object0 = LockFunction.obj;
                    synchronized(object0) {
                        LockFunction.obj.notifyAll();
                        return LuaValue.NIL;
                    }
                }
                if(luaValue0 == null || luaValue0.isnil()) {
                    try {
                        LockFunction.end = luaFunction0.invoke();
                    }
                    catch(Exception exception3) {
                        throw new LuaError(exception3);
                    }
                }
                else if(luaValue0.istable()) {
                    LuaTable luaTable0 = luaValue0.checktable(1);
                    LuaValue[] arr_luaValue = new LuaValue[luaTable0.length()];
                    Iterator luaTable$Iterator0 = luaTable0.iterator();
                    int v1 = -1;
                    while(luaTable$Iterator0.next() && v1 < luaTable0.length()) {
                        ++v1;
                        arr_luaValue[v1] = luaTable$Iterator0.value();
                    }
                    try {
                        LockFunction.end = luaFunction0.invoke(LuaValue.varargsOf(arr_luaValue));
                    }
                    catch(Exception exception1) {
                        throw new LuaError(exception1);
                    }
                }
                else {
                    try {
                        LockFunction.end = luaFunction0.invoke(luaValue0);
                    }
                    catch(Exception exception2) {
                        throw new LuaError(exception2);
                    }
                }
                Object object1 = LockFunction.obj;
                synchronized(object1) {
                    LockFunction.obj.notifyAll();
                    return LockFunction.end;
                }
            }
            catch(Exception exception0) {
                throw new LuaError(exception0);
            }
        }
        return LuaValue.NIL;
    }

    public static Varargs unLockWrite(LuaFunction luaFunction0, LuaValue luaValue0) {
        return LockFunction.unLockUi(luaFunction0, luaValue0);
    }
}

