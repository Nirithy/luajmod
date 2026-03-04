package luaj;

public class LocVars {
    public int endpc;
    public int startpc;
    public LuaString varname;

    public LocVars(LuaString varname, int startpc, int endpc) {
        this.varname = varname;
        this.startpc = startpc;
        this.endpc = endpc;
    }

    public static void main(String[] args) throws Throwable {
    }

    @Override
    public String toString() {
        return this.tojstring();
    }

    public String tojstring() {
        return this.varname + ' ' + this.startpc + '-' + this.endpc;
    }
}

