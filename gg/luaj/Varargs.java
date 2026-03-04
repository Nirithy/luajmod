package luaj;

import java.math.BigDecimal;
import luaj.lib.VarArgFunction;

public abstract class Varargs {
    static final class ArrayPartVarargs extends Varargs {
        private final int length;
        private final Varargs more;
        private final int offset;
        private final LuaValue[] v;

        ArrayPartVarargs(LuaValue[] v, int offset, int length) {
            this.v = v;
            this.offset = offset;
            this.length = length;
            this.more = LuaValue.NONE;
        }

        public ArrayPartVarargs(LuaValue[] v, int offset, int length, Varargs more) {
            this.v = v;
            this.offset = offset;
            this.length = length;
            this.more = more;
        }

        @Override  // luaj.Varargs
        public LuaValue arg(int i) {
            if(i < 1) {
                return LuaValue.NIL;
            }
            return i > this.length ? this.more.arg(i - this.length) : this.v[this.offset + i - 1];
        }

        @Override  // luaj.Varargs
        public LuaValue arg1() {
            return this.length <= 0 ? this.more.arg1() : this.v[this.offset];
        }

        @Override  // luaj.Varargs
        void copyto(LuaValue[] dest, int offset, int length) {
            int v2 = Math.min(this.length, length);
            System.arraycopy(this.v, this.offset, dest, offset, v2);
            this.more.copyto(dest, offset + v2, length - v2);
        }

        @Override  // luaj.Varargs
        public int narg() {
            int v = this.more.narg();
            return this.length + v;
        }

        @Override  // luaj.Varargs
        public Varargs subargs(int start) {
            if(start <= 0) {
                LuaValue.argerror(1, "start must be > 0");
            }
            if(start == 1) {
                return this;
            }
            return start <= this.length ? LuaValue.varargsOf(this.v, this.offset + start - 1, this.length - (start - 1), this.more) : this.more.subargs(start - this.length);
        }
    }

    static final class ArrayVarargs extends Varargs {
        private final Varargs r;
        private final LuaValue[] v;

        ArrayVarargs(LuaValue[] v, Varargs r) {
            this.v = v;
            this.r = r;
        }

        @Override  // luaj.Varargs
        public LuaValue arg(int i) {
            if(i < 1) {
                return LuaValue.NIL;
            }
            return i > this.v.length ? this.r.arg(i - this.v.length) : this.v[i - 1];
        }

        @Override  // luaj.Varargs
        public LuaValue arg1() {
            return this.v.length <= 0 ? this.r.arg1() : this.v[0];
        }

        @Override  // luaj.Varargs
        void copyto(LuaValue[] dest, int offset, int length) {
            int v2 = Math.min(this.v.length, length);
            System.arraycopy(this.v, 0, dest, offset, v2);
            this.r.copyto(dest, offset + v2, length - v2);
        }

        @Override  // luaj.Varargs
        public int narg() {
            int v = this.r.narg();
            return this.v.length + v;
        }

        @Override  // luaj.Varargs
        public Varargs subargs(int start) {
            if(start <= 0) {
                LuaValue.argerror(1, "start must be > 0");
            }
            if(start == 1) {
                return this;
            }
            return start <= this.v.length ? LuaValue.varargsOf(this.v, start - 1, this.v.length - (start - 1), this.r) : this.r.subargs(start - this.v.length);
        }
    }

    static final class PairVarargs extends Varargs {
        private final LuaValue v1;
        private final Varargs v2;

        PairVarargs(LuaValue v1, Varargs v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override  // luaj.Varargs
        public LuaValue arg(int i) {
            return i == 1 ? this.v1 : this.v2.arg(i - 1);
        }

        @Override  // luaj.Varargs
        public LuaValue arg1() {
            return this.v1;
        }

        @Override  // luaj.Varargs
        public int narg() {
            return this.v2.narg() + 1;
        }

        @Override  // luaj.Varargs
        public Varargs subargs(int start) {
            switch(start) {
                case 1: {
                    return this;
                }
                case 2: {
                    return this.v2;
                }
                default: {
                    return start > 2 ? this.v2.subargs(start - 1) : LuaValue.argerror(1, "start must be > 0");
                }
            }
        }
    }

    static class SubVarargs extends Varargs {
        private final int end;
        private final int start;
        private final Varargs v;

        public SubVarargs(Varargs varargs, int start, int end) {
            this.v = varargs;
            this.start = start;
            this.end = end;
        }

        @Override  // luaj.Varargs
        public LuaValue arg(int i) {
            int v1 = i + (this.start - 1);
            return v1 < this.start || v1 > this.end ? LuaValue.NIL : this.v.arg(v1);
        }

        @Override  // luaj.Varargs
        public LuaValue arg1() {
            return this.v.arg(this.start);
        }

        @Override  // luaj.Varargs
        public int narg() {
            return this.end + 1 - this.start;
        }

        @Override  // luaj.Varargs
        public Varargs subargs(int start) {
            if(start == 1) {
                return this;
            }
            int newstart = this.start + start - 1;
            if(start > 0) {
                if(newstart >= this.end) {
                    return LuaValue.NONE;
                }
                if(newstart == this.end) {
                    return this.v.arg(this.end);
                }
                return newstart == this.end - 1 ? new PairVarargs(this.v.arg(this.end - 1), this.v.arg(this.end)) : new SubVarargs(this.v, newstart, this.end);
            }
            return new SubVarargs(this.v, newstart, this.end);
        }
    }

    public static final ThreadLocal curFunc;

    static {
        Varargs.curFunc = new ThreadLocal();
    }

    public abstract LuaValue arg(int arg1);

    public abstract LuaValue arg1();

    public void argcheck(boolean test, int i, String msg) {
        if(!test) {
            LuaValue.argerror(i, msg);
        }
    }

    private LuaError argerr(int iarg, LuaError source) {
        String msg = source.getMessage();
        if(msg.startsWith("bad argument: ")) {
            msg = msg.substring(14);
        }
        String msg = (iarg > this.narg() ? "no value" : this.arg(iarg).typename()) + ": " + msg;
        VarArgFunction current = (VarArgFunction)Varargs.curFunc.get();
        return current == null ? new LuaArgError(iarg, null, msg) : new LuaArgError(iarg, current.name(), msg);
    }

    public BigDecimal checkbignum(int v) {
        try {
            return this.arg(v).checkbignum();
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber checkbignumber(int v) {
        try {
            return this.arg(v).checkbignumber();
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public boolean checkboolean(int i) {
        try {
            return this.arg(i).checkboolean();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaClosure checkclosure(int i) {
        try {
            return this.arg(i).checkclosure();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public double checkdouble(int i) {
        try {
            return this.arg(i).checkdouble();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaFunction checkfunction(int i) {
        try {
            return this.arg(i).checkfunction();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public int checkint(int i) {
        try {
            return this.arg(i).checkint();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaLong checkinteger(int i) {
        try {
            return this.arg(i).checkinteger();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaValue checkinteger(int v) {
        return this.arg(v).checkinteger();
    }

    public String checkjstring(int i) {
        try {
            return this.arg(i).checkjstring();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public long checklong(int i) {
        try {
            return this.arg(i).checklong();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaValue checknotnil(int i) {
        try {
            return this.arg(i).checknotnil();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaNumber checknumber(int i) {
        try {
            return this.arg(i).checknumber();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaString checkstring(int i) {
        try {
            return this.arg(i).checkstring();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaTable checktable(int i) {
        try {
            return this.arg(i).checktable();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaThread checkthread(int i) {
        try {
            return this.arg(i).checkthread();
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public Object checkuserdata(int v) {
        return this.arg(v).checkuserdata();
    }

    public Object checkuserdata(int v, Class class0) {
        return this.arg(v).checkuserdata(class0);
    }

    public LuaValue checkvalue(int i) {
        return i > this.narg() ? LuaValue.argerror(i, "value expected") : this.arg(i);
    }

    void copyto(LuaValue[] dest, int offset, int length) {
        for(int i = 0; i < length; ++i) {
            dest[offset + i] = this.arg(i + 1);
        }
    }

    public Varargs dealias() {
        int v = this.narg();
        switch(v) {
            case 0: {
                return LuaValue.NONE;
            }
            case 1: {
                return this.arg1();
            }
            case 2: {
                return new PairVarargs(this.arg1(), this.arg(2));
            }
            default: {
                LuaValue[] v = new LuaValue[v];
                this.copyto(v, 0, v);
                return new ArrayVarargs(v, LuaValue.NONE);
            }
        }
    }

    public Varargs eval() {
        return this;
    }

    public boolean isTailcall() {
        return false;
    }

    public boolean isbignumber(int v) {
        return this.arg(v).isbignumber();
    }

    public boolean isfunction(int i) {
        return this.arg(i).isfunction();
    }

    public boolean isnil(int i) {
        return this.arg(i).isnil();
    }

    public boolean isnoneornil(int i) {
        return i > this.narg() || this.arg(i).isnil();
    }

    public boolean isnumber(int i) {
        return this.arg(i).isnumber();
    }

    public boolean isstring(int i) {
        return this.arg(i).isstring();
    }

    public boolean istable(int i) {
        return this.arg(i).istable();
    }

    public boolean isthread(int i) {
        return this.arg(i).isthread();
    }

    public boolean isuserdata(int v) {
        return this.arg(v).isuserdata();
    }

    public boolean isvalue(int i) {
        return i > 0 && i <= this.narg();
    }

    public static void main(String[] args) throws Throwable {
    }

    public abstract int narg();

    public BigDecimal optbignum(int v, double f) {
        try {
            return this.arg(v).optbignum(f);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public BigDecimal optbignum(int v, int v1) {
        try {
            return this.arg(v).optbignum(v1);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public BigDecimal optbignum(int v, long v1) {
        try {
            return this.arg(v).optbignum(v1);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public BigDecimal optbignum(int v, String s) {
        try {
            return this.arg(v).optbignum(s);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public BigDecimal optbignum(int v, BigDecimal bigDecimal0) {
        try {
            return this.arg(v).optbignum(bigDecimal0);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber optbignumber(int v, double f) {
        try {
            return this.arg(v).optbignumber(f);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber optbignumber(int v, int v1) {
        try {
            return this.arg(v).optbignumber(v1);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber optbignumber(int v, long v1) {
        try {
            return this.arg(v).optbignumber(v1);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber optbignumber(int v, String s) {
        try {
            return this.arg(v).optbignumber(s);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public LuaBigNumber optbignumber(int v, BigDecimal bigDecimal0) {
        try {
            return this.arg(v).optbignumber(bigDecimal0);
        }
        catch(LuaError luaError0) {
            throw this.argerr(v, luaError0);
        }
    }

    public boolean optboolean(int i, boolean defval) {
        try {
            return this.arg(i).optboolean(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaClosure optclosure(int i, LuaClosure defval) {
        try {
            return this.arg(i).optclosure(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public double optdouble(int i, double defval) {
        try {
            return this.arg(i).optdouble(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaFunction optfunction(int i, LuaFunction defval) {
        try {
            return this.arg(i).optfunction(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public int optint(int i, int defval) {
        try {
            return this.arg(i).optint(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaLong optinteger(int i, LuaLong defval) {
        try {
            return this.arg(i).optinteger(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaValue optinteger(int v, LuaValue luaValue0) {
        return this.arg(v).optinteger(luaValue0);
    }

    public String optjstring(int i, String defval) {
        try {
            return this.arg(i).optjstring(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public long optlong(int i, long defval) {
        try {
            return this.arg(i).optlong(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaNumber optnumber(int i, LuaNumber defval) {
        try {
            return this.arg(i).optnumber(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaString optstring(int i, LuaString defval) {
        try {
            return this.arg(i).optstring(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaTable opttable(int i, LuaTable defval) {
        try {
            return this.arg(i).opttable(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public LuaThread optthread(int i, LuaThread defval) {
        try {
            return this.arg(i).optthread(defval);
        }
        catch(LuaError e) {
            throw this.argerr(i, e);
        }
    }

    public Object optuserdata(int v, Class class0, Object object0) {
        return this.arg(v).optuserdata(class0, object0);
    }

    public Object optuserdata(int v, Object object0) {
        return this.arg(v).optuserdata(object0);
    }

    public LuaValue optvalue(int i, LuaValue defval) {
        return i <= 0 || i > this.narg() ? defval : this.arg(i);
    }

    public abstract Varargs subargs(int arg1);

    @Override
    public String toString() {
        return this.tojstring();
    }

    public BigDecimal tobignum(int v) {
        return this.arg(v).tobignum();
    }

    public LuaBigNumber tobignumber(int v) {
        return this.arg(v).tobignumber();
    }

    public boolean toboolean(int i) {
        return this.arg(i).toboolean();
    }

    public byte tobyte(int i) {
        return this.arg(i).tobyte();
    }

    public char tochar(int i) {
        return this.arg(i).tochar();
    }

    public double todouble(int i) {
        return this.arg(i).todouble();
    }

    public float tofloat(int i) {
        return this.arg(i).tofloat();
    }

    public int toint(int i) {
        return this.arg(i).toint();
    }

    public String tojstring() {
        Buffer sb = new Buffer();
        sb.append("(");
        int v1 = this.narg();
        for(int i = 1; i <= v1; ++i) {
            if(i > 1) {
                sb.append(",");
            }
            sb.append(this.arg(i).tojstring());
        }
        sb.append(")");
        return "";
    }

    public String tojstring(int i) {
        return this.arg(i).tojstring();
    }

    public long tolong(int i) {
        return this.arg(i).tolong();
    }

    public short toshort(int i) {
        return this.arg(i).toshort();
    }

    public Object touserdata(int v) {
        return this.arg(v).touserdata();
    }

    public Object touserdata(int v, Class class0) {
        return this.arg(v).touserdata(class0);
    }

    public int type(int i) {
        return this.arg(i).type();
    }
}

