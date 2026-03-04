package luaj;

public class LuaArgError extends LuaError {
    public final String funcname;
    public final int iarg;
    public final String msg;
    private static final long serialVersionUID = 0xD16588C9D6028961L;

    public LuaArgError(int iarg, String funcname, String msg) {
        super(null);
        this.iarg = iarg;
        this.msg = msg;
        this.funcname = this.funcname(funcname);
        this.detailMessage = this.msg();
    }

    LuaArgError(LuaArgError src) {
        super(null, src);
        this.iarg = src.iarg - 1;
        this.msg = src.msg;
        this.funcname = src.funcname;
        this.detailMessage = this.msg();
    }

    private String funcname(String funcname) {
        if(funcname == null) {
            StackTraceElement[] arr_stackTraceElement = this.getStackTrace();
            if(arr_stackTraceElement != null) {
                for(int i = 1; i < arr_stackTraceElement.length; ++i) {
                    String s1 = arr_stackTraceElement[i].getClassName();
                    if(s1 != null) {
                        if(s1.endsWith("LuaClosure")) {
                            break;
                        }
                        if(s1.contains("Lib$")) {
                            return LuaFunction.name(s1);
                        }
                    }
                }
            }
        }
        return funcname;
    }

    private String msg() {
        if(this.funcname == null) {
            return this.iarg == 0 ? "calling method on bad self" + this.msg : "bad argument #" + this.iarg + ": " + this.msg;
        }
        return this.iarg == 0 ? "calling \'" + this.funcname + "\' on bad self" + " (" + this.msg + ")" : "bad argument #" + this.iarg + " to \'" + this.funcname + "\'" + " (" + this.msg + ")";
    }
}

