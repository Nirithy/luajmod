package luaj;

public class Prototype {
    private static final Prototype[] NOSUBPROTOS;
    private static final Upvaldesc[] NOUPVALUES;
    public int[] code;
    public int is_vararg;
    public LuaValue[] k;
    public int lastlinedefined;
    public int linedefined;
    public int[] lineinfo;
    public LocVars[] locvars;
    public int maxstacksize;
    public int numparams;
    public Prototype[] p;
    public LuaString source;
    int tailPos;
    public Upvaldesc[] upvalues;

    static {
        Prototype.NOUPVALUES = new Upvaldesc[0];
        Prototype.NOSUBPROTOS = new Prototype[0];
    }

    public Prototype() {
        this.tailPos = -1;
        this.p = Prototype.NOSUBPROTOS;
        this.upvalues = Prototype.NOUPVALUES;
    }

    public Prototype(int n_upvalues) {
        this.tailPos = -1;
        this.p = Prototype.NOSUBPROTOS;
        this.upvalues = new Upvaldesc[n_upvalues];
    }

    public LuaString getlocalname(int number, int pc) {
        for(int i = 0; true; ++i) {
            if(i >= this.locvars.length || this.locvars[i].startpc > pc) {
                return null;
            }
            if(pc < this.locvars[i].endpc) {
                --number;
                if(number == 0) {
                    return this.locvars[i].varname;
                }
            }
        }
    }

    public String shortsource() [...] // 潜在的解密器

    @Override
    public String toString() [...] // 潜在的解密器
}

