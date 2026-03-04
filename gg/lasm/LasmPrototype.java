package lasm;

import android.sup.GrowingArrayUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import luaj.LocVars;
import luaj.Lua;
import luaj.LuaValue;
import luaj.Prototype;
import luaj.Upvaldesc;
import luaj.compiler.Constants;
import luaj.compiler.InstructionPtr;
import luaj.compiler.LasmLua;

public class LasmPrototype extends Prototype {
    static class Link {
        Token name;
        int pc;

        Link(Token name, int pc) {
            this.name = name;
            this.pc = pc;
        }
    }

    private static final int INITIAL_CAPACITY = 8;
    int codeSize;
    HashMap consts;
    int currLine;
    Link[] dests;
    int destsSize;
    Link[] funcs;
    int funcsSize;
    int kSize;
    HashMap labels;
    LasmLua lasmLua;
    int lineinfoSize;
    int locvarsSize;
    HashMap pNames;
    int pSize;
    Link[] stack;
    int upvaluesSize;

    LasmPrototype() {
        this.kSize = 0;
        this.codeSize = 0;
        this.currLine = 0;
        this.pSize = 0;
        this.lineinfoSize = 0;
        this.locvarsSize = 0;
        this.upvaluesSize = 0;
        this.destsSize = 0;
        this.funcsSize = 0;
        this.k = new LuaValue[8];
        this.code = new int[8];
        this.p = new Prototype[8];
        this.lineinfo = new int[8];
        this.locvars = new LocVars[8];
        this.upvalues = new Upvaldesc[8];
        this.dests = new Link[8];
        this.funcs = new Link[8];
        this.consts = new HashMap();
        this.labels = new HashMap();
        this.pNames = new HashMap();
        this.lasmLua = new LasmLua(this);
    }

    // 去混淆评级： 低(20)
    int RK(LuaValue a) {
        return a instanceof Internal ? a.type() : Lua.RKASK(this.getConst(a));
    }

    void addFunc(Token name, LasmPrototype f) throws ParseException {
        String s = name.image.toLowerCase(Locale.ENGLISH);
        Link prev = (Link)this.pNames.get(s);
        if(prev != null) {
            throw new LasmParseException(name, "Duplicate function name \'" + name.image + "\' (used at line " + prev.name.beginLine + ")");
        }
        this.pNames.put(s, new Link(name, this.pSize));
        int v = this.pSize;
        this.pSize = v + 1;
        this.p = (Prototype[])GrowingArrayUtils.append(this.p, v, f);
    }

    void addLabel(Token name) throws ParseException {
        String s = name.image.toLowerCase(Locale.ENGLISH);
        Link prev = (Link)this.labels.get(s);
        if(prev != null) {
            throw new LasmParseException(name, "Try redefine label \'" + name.image + "\' (already defined at line " + prev.name.beginLine + ")");
        }
        this.labels.put(s, new Link(name, this.codeSize));
    }

    void addLine(Token n) throws ParseException {
        this.currLine = Lasm.parseInt(n);
    }

    public int addOp(int op) {
        if(this.currLine != 0) {
            if(this.lineinfo.length <= this.codeSize) {
                this.lineinfo = Arrays.copyOf(this.lineinfo, GrowingArrayUtils.growSize(this.codeSize + 1));
            }
            this.lineinfo[this.codeSize] = this.currLine;
            this.lineinfoSize = this.codeSize + 1;
        }
        int v1 = this.codeSize;
        this.codeSize = v1 + 1;
        this.code = GrowingArrayUtils.append(this.code, v1, op);
        return 0;
    }

    void addOp(Token op, LuaValue a, LuaValue b, LuaValue c, Token d, Token e) throws ParseException {
        int v5;
        int rb;
        int rc;
        try {
            int o = -1;
            switch(op.kind) {
                case 5: {
                label_105:
                    if(o < 0) {
                        o = 0;
                    }
                    this.lasmLua.codeABC(o, a.type(), b.type(), 0);
                    return;
                }
                case 6: {
                    this.lasmLua.codeK(a.type(), this.getConst(b));
                    return;
                }
                case 7: {
                    this.lasmLua.codeABC(3, a.type(), Lasm.parseInt(d), (e == null ? 0 : 1));
                    return;
                }
                case 8: {
                    this.lasmLua.codeABC(4, a.type(), b.type() - a.type(), 0);
                    return;
                }
                case 9: {
                    o = 5;
                    goto label_41;
                }
                case 10: {
                    o = 6;
                    goto label_38;
                }
                case 11: {
                label_38:
                    if(o < 0) {
                        o = 7;
                    }
                    goto label_44;
                }
                case 12: {
                label_46:
                    if(o < 0) {
                        o = 8;
                    }
                    goto label_48;
                }
                case 13: {
                label_41:
                    if(o >= 0) {
                        goto label_61;
                    }
                    o = 9;
                    goto label_61;
                }
                case 14: {
                label_119:
                    if(o < 0) {
                        o = 10;
                    }
                    break;
                }
                case 15: {
                    this.lasmLua.codeABC(11, a.type(), Lasm.parseInt(d), Lasm.parseInt(e));
                    return;
                }
                case 16: {
                label_44:
                    if(o < 0) {
                        o = 12;
                    }
                    goto label_46;
                }
                case 17: {
                label_48:
                    if(o < 0) {
                        o = 13;
                    }
                    goto label_50;
                }
                case 18: {
                label_50:
                    if(o < 0) {
                        o = 14;
                    }
                    goto label_52;
                }
                case 19: {
                label_52:
                    if(o < 0) {
                        o = 15;
                    }
                    goto label_54;
                }
                case 20: {
                label_54:
                    if(o < 0) {
                        o = 16;
                    }
                    goto label_56;
                }
                case 21: {
                label_56:
                    if(o < 0) {
                        o = 17;
                    }
                    goto label_58;
                }
                case 22: {
                label_58:
                    if(o < 0) {
                        o = 18;
                    }
                    goto label_100;
                }
                case 23: {
                label_61:
                    if(o < 0) {
                        o = 19;
                    }
                    goto label_63;
                }
                case 24: {
                label_63:
                    if(o < 0) {
                        o = 20;
                    }
                    goto label_65;
                }
                case 25: {
                label_65:
                    if(o >= 0) {
                        goto label_103;
                    }
                    o = 21;
                    goto label_103;
                }
                case 26: {
                    break;
                }
                case 27: {
                    o = 23;
                    goto label_87;
                }
                case 28: {
                    o = 24;
                    goto label_71;
                }
                case 29: {
                label_71:
                    if(o < 0) {
                        o = 25;
                    }
                    goto label_73;
                }
                case 30: {
                label_73:
                    if(o < 0) {
                        o = 26;
                    }
                    this.lasmLua.codeABC(o, Lasm.parseInt(d), this.RK(a), this.RK(b));
                    return;
                }
                case 0x1F: {
                    o = 27;
                    goto label_78;
                }
                case 0x20: {
                label_78:
                    if(o < 0) {
                        o = 28;
                    }
                    this.lasmLua.codeABC(o, a.type(), (b == null ? 0 : b.type()), Lasm.parseInt(d));
                    return;
                }
                case 33: {
                    int rb = b == null ? 0 : b.type() - a.type() + 1;
                    if(d == null) {
                        rc = c == null ? 1 : c.type() - a.type() + 2;
                    }
                    else {
                        rc = 0;
                    }
                    this.lasmLua.codeABC(29, a.type(), rb, rc);
                    return;
                }
                case 34: {
                    o = 30;
                    goto label_83;
                }
                case 35: {
                    int v3 = a == null ? 0 : a.type();
                    if(a == null) {
                        rb = 1;
                    }
                    else {
                        rb = b == null ? 0 : b.type() - a.type() + 2;
                    }
                    this.lasmLua.codeABC(0x1F, v3, rb, 0);
                    return;
                }
                case 36: {
                label_87:
                    if(o < 0) {
                        o = 0x20;
                    }
                    goto label_89;
                }
                case 37: {
                label_89:
                    if(o < 0) {
                        o = 33;
                    }
                    goto label_91;
                }
                case 38: {
                    this.lasmLua.codeABC(34, a.type(), 0, b.type() - a.type() - 2);
                    return;
                }
                case 39: {
                label_91:
                    if(o < 0) {
                        o = 35;
                    }
                    LasmLua lasmLua0 = this.lasmLua;
                    if(o == 23) {
                        v5 = a == null ? 0 : a.type() + 1;
                    }
                    else {
                        v5 = a.type();
                    }
                    lasmLua0.codeAsBx(o, v5, this.getDest(d));
                    return;
                }
                case 40: {
                    if(b == null) {
                        b = a;
                    }
                    this.lasmLua.codeList(a.type(), b.type() - a.type(), Lasm.parseInt(d));
                    return;
                }
                case 41: {
                    this.lasmLua.codeABx(37, a.type(), this.getFunc(d));
                    return;
                }
                case 42: {
                label_83:
                    if(o < 0) {
                        o = 38;
                    }
                    this.lasmLua.codeABC(o, a.type(), (b == null ? 0 : b.type() - a.type() + 1), 0);
                    return;
                }
                case 43: {
                label_100:
                    if(o < 0) {
                        o = 40;
                    }
                    goto label_109;
                }
                case 44: {
                label_103:
                    if(o < 0) {
                        o = 41;
                    }
                    goto label_105;
                }
                case 45: {
                label_109:
                    if(o < 0) {
                        o = 42;
                    }
                    goto label_111;
                }
                case 46: {
                label_111:
                    if(o < 0) {
                        o = 43;
                    }
                    goto label_113;
                }
                case 0x2F: {
                label_113:
                    if(o < 0) {
                        o = 44;
                    }
                    goto label_115;
                }
                case 0x30: {
                label_115:
                    if(o < 0) {
                        o = 45;
                    }
                    goto label_117;
                }
                case 49: {
                label_117:
                    if(o < 0) {
                        o = 46;
                    }
                    goto label_119;
                }
                case 0x4F: {
                    this.addOp(Lasm.parseHex(d));
                    return;
                }
                default: {
                    throw new LasmParseException(op, "Unknow token for OP: \'" + op.image + "\'");
                }
            }
            if(o < 0) {
                o = 22;
            }
            this.lasmLua.codeABC(o, a.type(), this.RK(b), this.RK(c));
            return;
        }
        catch(Throwable ex) {
            if(ex instanceof LasmParseException) {
                throw ex;
            }
            throw new LasmParseException(op, "Failed assemble OP \'" + op.image + "\'", ex);
        }
    }

    void addUpval(Internal vu, Token name) throws ParseException {
        int v = this.upvaluesSize;
        this.upvaluesSize = v + 1;
        this.upvalues = (Upvaldesc[])GrowingArrayUtils.append(this.upvalues, v, new Upvaldesc(Lasm.parseLuaString(name), vu instanceof V, vu.code));
    }

    LasmPrototype build() throws ParseException {
        Link local;
        if(this.k.length != this.kSize) {
            this.k = (LuaValue[])Arrays.copyOf(this.k, this.kSize);
        }
        if(this.code.length != this.codeSize) {
            this.code = Arrays.copyOf(this.code, this.codeSize);
        }
        if(this.p.length != this.pSize) {
            this.p = (Prototype[])Arrays.copyOf(this.p, this.pSize);
        }
        if(this.lineinfo.length != this.lineinfoSize) {
            this.lineinfo = Arrays.copyOf(this.lineinfo, this.lineinfoSize);
        }
        if(this.locvars.length != this.locvarsSize) {
            this.locvars = (LocVars[])Arrays.copyOf(this.locvars, this.locvarsSize);
        }
        if(this.upvalues.length != this.upvaluesSize) {
            this.upvalues = (Upvaldesc[])Arrays.copyOf(this.upvalues, this.upvaluesSize);
        }
        int in = this.destsSize;
        for(int i = 0; true; ++i) {
            if(i >= in) {
                int in = this.funcsSize;
                for(int i = 0; true; ++i) {
                    if(i >= in) {
                        int i;
                        for(i = 0; true; ++i) {
                            if(i >= this.stack.length) {
                                this.dests = null;
                                this.funcs = null;
                                this.consts = null;
                                this.labels = null;
                                this.pNames = null;
                                this.lasmLua = null;
                                this.stack = null;
                                return this;
                            }
                            local = this.stack[i];
                            if(local != null) {
                                break;
                            }
                        }
                        throw new LasmParseException(local.name, "No \'.end local\' for local variable v" + i);
                    }
                    Link func = this.funcs[i];
                    Link proto = (Link)this.pNames.get(func.name.image.toLowerCase(Locale.ENGLISH));
                    if(proto == null) {
                        throw new LasmParseException(func.name, "The function " + func.name.image + " is missing");
                    }
                    Constants.SETARG_Bx(new InstructionPtr(this.code, func.pc), proto.pc);
                }
            }
            Link dest = this.dests[i];
            Link label = (Link)this.labels.get(dest.name.image.toLowerCase(Locale.ENGLISH));
            if(label == null) {
                throw new LasmParseException(dest.name, "The label " + dest.name.image + " is missing");
            }
            Constants.SETARG_sBx(new InstructionPtr(this.code, dest.pc), label.pc - dest.pc - 1);
        }
    }

    void check(LuaValue r) throws ParseException {
    }

    void endLocal(V v, Token name, int offset) throws ParseException {
        int v1 = v.type();
        if(this.stack[v1] == null) {
            throw new LasmParseException(v.token, "Try end not defined local variable v" + v1);
        }
        if(!this.stack[v1].name.image.equals(name.image)) {
            throw new LasmParseException(v.token, "Try end local variable v" + v1 + " with different name (v" + v1 + " defined at line " + this.stack[v1].name.beginLine + " as \'" + this.stack[v1].name.image + "\')");
        }
        this.locvars[this.stack[v1].pc].endpc = this.codeSize + offset;
        this.stack[v1] = null;
    }

    int getConst(LuaValue v) {
        if(v instanceof Const) {
            return v.type();
        }
        Integer pos = (Integer)this.consts.get(v);
        if(pos == null) {
            pos = this.kSize;
            this.consts.put(v, pos);
            int v = this.kSize;
            this.kSize = v + 1;
            this.k = (LuaValue[])GrowingArrayUtils.append(this.k, v, v);
        }
        return (int)pos;
    }

    int getDest(Token d) throws ParseException {
        int offset = d.kind == 60 ? Lasm.parseInt(d) : -1;
        if(d.kind != 60) {
            Link label = (Link)this.labels.get(d.image.toLowerCase(Locale.ENGLISH));
            if(label != null) {
                return label.pc - this.codeSize - 1;
            }
            int v1 = this.destsSize;
            this.destsSize = v1 + 1;
            this.dests = (Link[])GrowingArrayUtils.append(this.dests, v1, new Link(d, this.codeSize));
            return offset;
        }
        return offset;
    }

    int getFunc(Token d) throws ParseException {
        if(d.kind == 0x4F) {
            return Lasm.parseInt(d);
        }
        int v = this.funcsSize;
        this.funcsSize = v + 1;
        this.funcs = (Link[])GrowingArrayUtils.append(this.funcs, v, new Link(d, this.codeSize));
        return 0x3FFFF;
    }

    void init(Token n) throws ParseException {
        this.maxstacksize = Math.max(2, LasmBase.parseIntMax(n, 0xFF));
        if(this.maxstacksize < this.numparams) {
            throw new LasmParseException(n, ".maxstacksize (" + this.maxstacksize + ") must be not less .numparams (" + this.numparams + ")");
        }
        this.stack = new Link[this.maxstacksize];
    }

    void startLocal(V v, Token name) throws ParseException {
        int v = v.type();
        if(this.stack[v] != null) {
            throw new LasmParseException(v.token, "Try redefine not ended local variable v" + v + " (v" + v + " defined at line " + this.stack[v].name.beginLine + " as \'" + this.stack[v].name.image + "\')");
        }
        Link[] arr_lasmPrototype$Link = this.stack;
        arr_lasmPrototype$Link[v] = new Link(name, this.locvarsSize);
        int v1 = this.locvarsSize;
        this.locvarsSize = v1 + 1;
        this.locvars = (LocVars[])GrowingArrayUtils.append(this.locvars, v1, new LocVars(Lasm.parseLuaString(name), this.codeSize, this.codeSize + 1));
    }
}

