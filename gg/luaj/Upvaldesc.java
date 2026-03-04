package luaj;

public class Upvaldesc {
    public final short idx;
    public final boolean instack;
    public LuaString name;

    public Upvaldesc(LuaString name, boolean instack, int idx) {
        this.name = name;
        this.instack = instack;
        this.idx = (short)idx;
    }

    // 去混淆评级： 低(40)
    @Override
    public String toString() {
        return this.instack ? "v" + ((int)this.idx) + " \"" + this.name + "\"" : "u" + ((int)this.idx) + " \"" + this.name + "\"";
    }
}

