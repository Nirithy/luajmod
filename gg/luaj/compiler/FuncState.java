package luaj.compiler;

import java.util.Hashtable;
import luaj.LocVars;
import luaj.Lua;
import luaj.LuaString;
import luaj.LuaValue;
import luaj.Prototype;
import luaj.Upvaldesc;

public class FuncState extends Constants {
    static class BlockCnt {
        short firstgoto;
        short firstlabel;
        boolean isloop;
        short nactvar;
        BlockCnt previous;
        boolean upval;

        @Override
        public String toString() {
            return "BlockCnt [firstlabel=" + ((int)this.firstlabel) + ", firstgoto=" + ((int)this.firstgoto) + ", nactvar=" + ((int)this.nactvar) + ", upval=" + this.upval + ", isloop=" + this.isloop + ']';
        }
    }

    BlockCnt bl;
    Prototype f;
    int firstlocal;
    short freereg;
    Hashtable h;
    IntPtr jpc;
    int lasttarget;
    LexState ls;
    short nactvar;
    int nk;
    short nlocvars;
    int np;
    short nups;
    int pc;
    FuncState prev;

    int addk(LuaValue v) {
        Hashtable h = this.h;
        if(h == null) {
            h = new Hashtable();
            this.h = h;
        }
        else {
            Integer i = (Integer)h.get(v);
            if(i != null) {
                return (int)i;
            }
        }
        int idx = this.nk;
        h.put(v, idx);
        Prototype f = this.f;
        if(f.k == null || this.nk + 1 >= f.k.length) {
            f.k = FuncState.realloc(f.k, this.nk * 2 + 1);
        }
        int v1 = this.nk;
        this.nk = v1 + 1;
        f.k[v1] = v;
        return idx;
    }

    // 去混淆评级： 低(20)
    int boolK(boolean b) {
        return b ? this.addk(LuaValue.TRUE) : this.addk(LuaValue.FALSE);
    }

    void checklimit(int v, int l, String msg) {
        if(v > l) {
            this.errorlimit(l, msg);
        }
    }

    void checkrepeated(Labeldesc[] ll, int ll_n, LuaString label) {
        for(int i = this.bl.firstlabel; i < ll_n; ++i) {
            if(label.eq_b(ll[i].name)) {
                this.ls.semerror(this.ls.L.pushfstring("label \'" + label + "\' already defined on line " + ll[i].line));
            }
        }
    }

    void checkstack(int n) {
        int newstack = this.freereg + n;
        if(newstack > this.f.maxstacksize) {
            if(newstack >= 0xFA) {
                this.ls.syntaxerror("function or expression needs too many registers");
            }
            this.f.maxstacksize = newstack;
        }
    }

    void closelistfield(ConsControl cc) {
        if(cc.v.k != 0) {
            this.exp2nextreg(cc.v);
            cc.v.k = 0;
            if(cc.tostore == 50) {
                this.setlist(cc.t.u.info, cc.na, cc.tostore);
                cc.tostore = 0;
            }
        }
    }

    int code(int instruction, int line) {
        Prototype f = this.f;
        this.dischargejpc();
        if(f.code == null || this.pc + 1 > f.code.length) {
            f.code = LuaC.realloc(f.code, this.pc * 2 + 1);
        }
        f.code[this.pc] = instruction;
        if(f.lineinfo == null || this.pc + 1 > f.lineinfo.length) {
            f.lineinfo = LuaC.realloc(f.lineinfo, this.pc * 2 + 1);
        }
        f.lineinfo[this.pc] = line;
        int v2 = this.pc;
        this.pc = v2 + 1;
        return v2;
    }

    int codeABC(int o, int a, int b, int c) {
        boolean z = false;
        FuncState._assert(FuncState.getOpMode(o) == 0);
        FuncState._assert(FuncState.getBMode(o) != 0 || b == 0);
        if(FuncState.getCMode(o) != 0 || c == 0) {
            z = true;
        }
        FuncState._assert(z);
        return this.code(FuncState.CREATE_ABC(o, a, b, c), this.ls.lastline);
    }

    int codeABx(int o, int a, int bc) {
        boolean z1;
        boolean z = true;
        switch(FuncState.getOpMode(o)) {
            case 1: 
            case 2: {
                z1 = true;
                break;
            }
            default: {
                z1 = false;
            }
        }
        FuncState._assert(z1);
        FuncState._assert(FuncState.getCMode(o) == 0);
        if(bc < 0 || bc > 0x3FFFF) {
            z = false;
        }
        FuncState._assert(z);
        return this.code(FuncState.CREATE_ABx(o, a, bc), this.ls.lastline);
    }

    int codeAsBx(int o, int A, int sBx) {
        return this.codeABx(o, A, sBx + 0x1FFFF);
    }

    int codeK(int reg, int k) {
        if(k <= 0x3FFFF) {
            return this.codeABx(1, reg, k);
        }
        int v2 = this.codeABx(2, reg, 0);
        this.codeextraarg(k);
        return v2;
    }

    int code_label(int A, int b, int jump) {
        this.getlabel();
        return this.codeABC(3, A, b, jump);
    }

    void codearith(int op, expdesc e1, expdesc e2, int line) {
        if(this.constfolding(op, e1, e2)) {
            return;
        }
        int v2 = op == 19 || op == 21 || op == 41 ? 0 : this.exp2RK(e2);
        int v3 = this.exp2RK(e1);
        if(v3 > v2) {
            this.freeexp(e1);
            this.freeexp(e2);
        }
        else {
            this.freeexp(e2);
            this.freeexp(e1);
        }
        e1.u.info = this.codeABC(op, 0, v3, v2);
        e1.k = 11;
        this.fixline(line);
    }

    void codecomp(int op, int cond, expdesc e1, expdesc e2) {
        int o1 = this.exp2RK(e1);
        int o2 = this.exp2RK(e2);
        this.freeexp(e2);
        this.freeexp(e1);
        if(cond == 0 && op != 24) {
            int temp = o1;
            o1 = o2;
            o2 = temp;
            cond = 1;
        }
        e1.u.info = this.condjump(op, cond, o1, o2);
        e1.k = 10;
    }

    int codeextraarg(int a) {
        FuncState._assert(a <= 0x3FFFFFF);
        return this.code(FuncState.CREATE_Ax(39, a), this.ls.lastline);
    }

    void codenot(expdesc e) {
        this.dischargevars(e);
        switch(e.k) {
            case 1: 
            case 3: {
                e.k = 2;
                break;
            }
            case 2: 
            case 4: 
            case 5: {
                e.k = 3;
                break;
            }
            case 10: {
                this.invertjump(e);
                break;
            }
            case 6: 
            case 11: {
                this.discharge2anyreg(e);
                this.freeexp(e);
                e.u.info = this.codeABC(20, 0, e.u.info, 0);
                e.k = 11;
                break;
            }
            default: {
                FuncState._assert(false);
            }
        }
        int temp = e.f.i;
        e.f.i = e.t.i;
        e.t.i = temp;
        this.removevalues(e.f.i);
        this.removevalues(e.t.i);
    }

    void concat(IntPtr l1, int l2) {
        if(l2 == -1) {
            return;
        }
        if(l1.i == -1) {
            l1.i = l2;
            return;
        }
        int list = l1.i;
        int v2;
        while((v2 = this.getjump(list)) != -1) {
            list = v2;
        }
        this.fixjump(list, l2);
    }

    int condjump(int op, int A, int B, int C) {
        this.codeABC(op, A, B, C);
        return this.jump();
    }

    boolean constfolding(int op, expdesc e1, expdesc e2) {
        LuaValue r;
        if(e1.isnumeral() && e2.isnumeral() && (op != 16 && op != 17 && op != 40 || !e2.u.nval().eq_b(LuaValue.ZERO))) {
            LuaValue luaValue0 = e1.u.nval();
            LuaValue luaValue1 = e2.u.nval();
            switch(op) {
                case 13: {
                    r = luaValue0.add(luaValue1);
                    goto label_40;
                }
                case 14: {
                    r = luaValue0.sub(luaValue1);
                    goto label_40;
                }
                case 15: {
                    r = luaValue0.mul(luaValue1);
                    goto label_40;
                }
                case 16: {
                    r = luaValue0.div(luaValue1);
                    goto label_40;
                }
                case 17: {
                    r = luaValue0.mod(luaValue1);
                    goto label_40;
                }
                case 18: {
                    r = luaValue0.pow(luaValue1);
                    goto label_40;
                }
                case 19: {
                    r = luaValue0.neg();
                    goto label_40;
                }
                case 21: {
                    break;
                }
                case 40: {
                    r = luaValue0.idiv(luaValue1);
                    goto label_40;
                }
                case 41: {
                    if(luaValue0.islongnumber()) {
                        r = luaValue0.bnot();
                        goto label_40;
                    }
                    break;
                }
                case 42: {
                    if(luaValue0.islongnumber() && luaValue1.islongnumber()) {
                        r = luaValue0.band(luaValue1);
                        goto label_40;
                    }
                    break;
                }
                case 43: {
                    if(luaValue0.islongnumber() && luaValue1.islongnumber()) {
                        r = luaValue0.bor(luaValue1);
                        goto label_40;
                    }
                    break;
                }
                case 44: {
                    if(luaValue0.islongnumber() && luaValue1.islongnumber()) {
                        r = luaValue0.bxor(luaValue1);
                        goto label_40;
                    }
                    break;
                }
                case 45: {
                    if(luaValue0.islongnumber() && luaValue1.islongnumber()) {
                        r = luaValue0.shl(luaValue1);
                        goto label_40;
                    }
                    break;
                }
                case 46: {
                    if(luaValue0.islongnumber() && luaValue1.islongnumber()) {
                        r = luaValue0.shr(luaValue1);
                    label_40:
                        if(r != null && !Double.isNaN(r.todouble())) {
                            e1.u.setNval(r);
                            return true;
                        }
                    }
                    break;
                }
                default: {
                    FuncState._assert(false);
                    r = null;
                    goto label_40;
                }
            }
        }
        return false;
    }

    void discharge2anyreg(expdesc e) {
        if(e.k != 6) {
            this.reserveregs(1);
            this.discharge2reg(e, this.freereg - 1);
        }
    }

    void discharge2reg(expdesc e, int reg) {
        int v1 = 1;
        boolean z = false;
        this.dischargevars(e);
        switch(e.k) {
            case 1: {
                this.nil(reg, 1);
                break;
            }
            case 2: 
            case 3: {
                if(e.k != 2) {
                    v1 = 0;
                }
                this.codeABC(3, reg, v1, 0);
                break;
            }
            case 4: {
                this.codeK(reg, e.u.info);
                break;
            }
            case 5: {
                this.codeK(reg, this.numberK(e.u.nval()));
                break;
            }
            case 6: {
                if(reg != e.u.info) {
                    this.codeABC(0, reg, e.u.info, 0);
                }
                break;
            }
            case 11: {
                FuncState.SETARG_A(this.getcodePtr(e), reg);
                break;
            }
            default: {
                if(e.k == 0 || e.k == 10) {
                    z = true;
                }
                FuncState._assert(z);
                return;
            }
        }
        e.u.info = reg;
        e.k = 6;
    }

    void dischargejpc() {
        this.patchlistaux(this.jpc.i, this.pc, 0xFF, this.pc);
        this.jpc.i = -1;
    }

    public void dischargevars(expdesc lexState$expdesc0) {
        int v;
        switch(lexState$expdesc0.k) {
            case 7: {
                lexState$expdesc0.k = 6;
                return;
            }
            case 8: {
                lexState$expdesc0.u.info = this.codeABC(5, 0, lexState$expdesc0.u.info, 0);
                lexState$expdesc0.k = 11;
                return;
            }
            case 9: {
                this.freereg(((int)lexState$expdesc0.u.ind_idx));
                if(lexState$expdesc0.u.ind_vt == 7) {
                    this.freereg(((int)lexState$expdesc0.u.ind_t));
                    v = 7;
                }
                else {
                    v = 6;
                }
                lexState$expdesc0.u.info = this.codeABC(v, 0, ((int)lexState$expdesc0.u.ind_t), ((int)lexState$expdesc0.u.ind_idx));
                lexState$expdesc0.k = 11;
                return;
            }
            case 12: 
            case 13: {
                this.setoneret(lexState$expdesc0);
                return;
            }
            case 14: {
                int v1 = 0x2F;
                this.freereg(((int)lexState$expdesc0.u.ind_idx));
                if(lexState$expdesc0.u.ind_vt == 7) {
                    this.freereg(((int)lexState$expdesc0.u.ind_t));
                    v1 = 0x30;
                }
                lexState$expdesc0.u.info = this.codeABC(v1, 0, ((int)lexState$expdesc0.u.ind_t), ((int)lexState$expdesc0.u.ind_idx));
                lexState$expdesc0.k = 11;
            }
        }
    }

    void enterblock(BlockCnt bl, boolean isloop) {
        boolean z1 = false;
        bl.isloop = isloop;
        bl.nactvar = this.nactvar;
        bl.firstlabel = (short)this.ls.dyd.n_label;
        bl.firstgoto = (short)this.ls.dyd.n_gt;
        bl.upval = false;
        bl.previous = this.bl;
        this.bl = bl;
        if(this.freereg == this.nactvar) {
            z1 = true;
        }
        FuncState._assert(z1);
    }

    void errorlimit(int limit, String what) {
        this.ls.lexerror((this.f.linedefined == 0 ? "main function has more than " + limit + ' ' + what : "function at line " + this.f.linedefined + " has more than " + limit + ' ' + what), 0);
    }

    int exp2RK(expdesc e) {
        int v;
        boolean z = true;
        this.exp2val(e);
        switch(e.k) {
            case 1: 
            case 2: 
            case 3: {
                if(this.nk <= 0xFF) {
                    U lexState$expdesc$U0 = e.u;
                    if(e.k == 1) {
                        v = this.nilK();
                    }
                    else {
                        if(e.k != 2) {
                            z = false;
                        }
                        v = this.boolK(z);
                    }
                    lexState$expdesc$U0.info = v;
                    e.k = 4;
                    return FuncState.RKASK(e.u.info);
                }
                return this.exp2anyreg(e);
            }
            case 4: {
                break;
            }
            case 5: {
                e.u.info = this.numberK(e.u.nval());
                e.k = 4;
                break;
            }
            default: {
                return this.exp2anyreg(e);
            }
        }
        return e.u.info > 0xFF ? this.exp2anyreg(e) : FuncState.RKASK(e.u.info);
    }

    int exp2anyreg(expdesc e) {
        this.dischargevars(e);
        if(e.k == 6) {
            if(!e.hasjumps()) {
                return e.u.info;
            }
            if(e.u.info >= this.nactvar) {
                this.exp2reg(e, e.u.info);
                return e.u.info;
            }
        }
        this.exp2nextreg(e);
        return e.u.info;
    }

    void exp2anyregup(expdesc e) {
        if(e.k != 8 || e.hasjumps()) {
            this.exp2anyreg(e);
        }
    }

    void exp2nextreg(expdesc e) {
        this.dischargevars(e);
        this.freeexp(e);
        this.reserveregs(1);
        this.exp2reg(e, this.freereg - 1);
    }

    void exp2reg(expdesc e, int reg) {
        this.discharge2reg(e, reg);
        if(e.k == 10) {
            this.concat(e.t, e.u.info);
        }
        if(e.hasjumps()) {
            int p_f = -1;
            int p_t = -1;
            if(this.need_value(e.t.i) || this.need_value(e.f.i)) {
                int fj = e.k == 10 ? -1 : this.jump();
                p_f = this.code_label(reg, 0, 1);
                p_t = this.code_label(reg, 1, 0);
                this.patchtohere(fj);
            }
            int v4 = this.getlabel();
            this.patchlistaux(e.f.i, v4, reg, p_f);
            this.patchlistaux(e.t.i, v4, reg, p_t);
        }
        e.t.i = -1;
        e.f.i = -1;
        e.u.info = reg;
        e.k = 6;
    }

    void exp2val(expdesc e) {
        if(e.hasjumps()) {
            this.exp2anyreg(e);
            return;
        }
        this.dischargevars(e);
    }

    void field(expdesc lexState$expdesc0, expdesc lexState$expdesc1) {
        lexState$expdesc0.u.ind_t = (short)lexState$expdesc0.u.info;
        lexState$expdesc0.u.ind_idx = (short)this.exp2RK(lexState$expdesc1);
        int v = 8;
        LuaC._assert(lexState$expdesc0.k == 8 || FuncState.vkisinreg(lexState$expdesc0.k));
        U lexState$expdesc$U0 = lexState$expdesc0.u;
        if(lexState$expdesc0.k != 8) {
            v = 7;
        }
        lexState$expdesc$U0.ind_vt = (short)v;
        lexState$expdesc0.k = 14;
    }

    void fixjump(int pc, int dest) {
        InstructionPtr instructionPtr0 = new InstructionPtr(this.f.code, pc);
        int offset = dest - (pc + 1);
        FuncState._assert(dest != -1);
        if(Math.abs(offset) > 0x1FFFF) {
            this.ls.syntaxerror("control structure too long");
        }
        FuncState.SETARG_sBx(instructionPtr0, offset);
    }

    void fixline(int line) {
        this.f.lineinfo[this.pc - 1] = line;
    }

    void freeexp(expdesc e) {
        if(e.k == 6) {
            this.freereg(e.u.info);
        }
    }

    void freereg(int reg) {
        if(!FuncState.ISK(reg) && reg >= this.nactvar) {
            this.freereg = (short)(this.freereg - 1);
            FuncState._assert(reg == this.freereg);
        }
    }

    int getcode(expdesc e) {
        return this.f.code[e.u.info];
    }

    InstructionPtr getcodePtr(expdesc e) {
        return new InstructionPtr(this.f.code, e.u.info);
    }

    int getjump(int pc) {
        int v1 = FuncState.GETARG_sBx(this.f.code[pc]);
        return v1 == -1 ? -1 : pc + 1 + v1;
    }

    InstructionPtr getjumpcontrol(int pc) {
        InstructionPtr instructionPtr0 = new InstructionPtr(this.f.code, pc);
        return pc < 1 || !FuncState.testTMode(FuncState.GET_OPCODE(instructionPtr0.code[instructionPtr0.idx - 1])) ? instructionPtr0 : new InstructionPtr(instructionPtr0.code, instructionPtr0.idx - 1);
    }

    int getlabel() {
        this.lasttarget = this.pc;
        return this.pc;
    }

    LocVars getlocvar(int i) {
        int idx = this.ls.dyd.actvar[this.firstlocal + i].idx;
        FuncState._assert(idx < this.nlocvars);
        return this.f.locvars[idx];
    }

    void goiffalse(expdesc e) {
        int pc;
        this.dischargevars(e);
        switch(e.k) {
            case 1: 
            case 3: {
                pc = -1;
                break;
            }
            case 10: {
                pc = e.u.info;
                break;
            }
            default: {
                pc = this.jumponcond(e, 1);
            }
        }
        this.concat(e.t, pc);
        this.patchtohere(e.f.i);
        e.f.i = -1;
    }

    void goiftrue(expdesc e) {
        int pc;
        this.dischargevars(e);
        switch(e.k) {
            case 2: 
            case 4: 
            case 5: {
                pc = -1;
                break;
            }
            case 10: {
                this.invertjump(e);
                pc = e.u.info;
                break;
            }
            default: {
                pc = this.jumponcond(e, 0);
            }
        }
        this.concat(e.f, pc);
        this.patchtohere(e.t.i);
        e.t.i = -1;
    }

    boolean hasmultret(int k) {
        return k == 12 || k == 13;
    }

    void indexed(expdesc t, expdesc k) {
        t.u.ind_t = (short)t.u.info;
        t.u.ind_idx = (short)this.exp2RK(k);
        LuaC._assert(t.k == 8 || FuncState.vkisinreg(t.k));
        t.u.ind_vt = (short)(t.k == 8 ? 8 : 7);
        t.k = 9;
    }

    void infix(int op, expdesc v) {
        switch(op) {
            case 6: {
                this.exp2nextreg(v);
                return;
            }
            case 13: {
                this.goiftrue(v);
                return;
            }
            case 14: {
                this.goiffalse(v);
                return;
            }
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: {
                if(!v.isnumeral()) {
                    this.exp2RK(v);
                    return;
                }
                return;
            }
            default: {
                this.exp2RK(v);
            }
        }
    }

    void invertjump(expdesc e) {
        InstructionPtr instructionPtr0 = this.getjumpcontrol(e.u.info);
        FuncState._assert(FuncState.testTMode(FuncState.GET_OPCODE(instructionPtr0.get())) && FuncState.GET_OPCODE(instructionPtr0.get()) != 28 && Lua.GET_OPCODE(instructionPtr0.get()) != 27);
        FuncState.SETARG_A(instructionPtr0, (FuncState.GETARG_A(instructionPtr0.get()) == 0 ? 1 : 0));
    }

    int jump() {
        int jpc = this.jpc.i;
        this.jpc.i = -1;
        IntPtr intPtr0 = new IntPtr(this.codeAsBx(23, 0, -1));
        this.concat(intPtr0, jpc);
        return intPtr0.i;
    }

    int jumponcond(expdesc e, int cond) {
        if(e.k == 11) {
            int v1 = this.getcode(e);
            if(FuncState.GET_OPCODE(v1) == 20) {
                --this.pc;
                int v2 = FuncState.GETARG_B(v1);
                return cond == 0 ? this.condjump(27, v2, 0, 1) : this.condjump(27, v2, 0, 0);
            }
        }
        this.discharge2anyreg(e);
        this.freeexp(e);
        return this.condjump(28, 0xFF, e.u.info, cond);
    }

    void lastlistfield(ConsControl cc) {
        if(cc.tostore == 0) {
            return;
        }
        if(this.hasmultret(cc.v.k)) {
            this.setmultret(cc.v);
            this.setlist(cc.t.u.info, cc.na, -1);
            --cc.na;
            return;
        }
        if(cc.v.k != 0) {
            this.exp2nextreg(cc.v);
        }
        this.setlist(cc.t.u.info, cc.na, cc.tostore);
    }

    void leaveblock() {
        BlockCnt bl = this.bl;
        if(bl.previous != null && bl.upval) {
            int v = this.jump();
            this.patchclose(v, ((int)bl.nactvar));
            this.patchtohere(v);
        }
        if(bl.isloop) {
            this.ls.breaklabel();
        }
        this.bl = bl.previous;
        this.removevars(((int)bl.nactvar));
        FuncState._assert(bl.nactvar == this.nactvar);
        this.freereg = this.nactvar;
        this.ls.dyd.n_label = bl.firstlabel;
        if(bl.previous != null) {
            this.movegotosout(bl);
            return;
        }
        if(bl.firstgoto < this.ls.dyd.n_gt) {
            this.ls.undefgoto(this.ls.dyd.gt[bl.firstgoto], this, bl);
        }
    }

    void markupval(int level) {
        BlockCnt bl;
        for(bl = this.bl; bl.nactvar > level; bl = bl.previous) {
        }
        bl.upval = true;
    }

    void movegotosout(BlockCnt bl) {
        int i = bl.firstgoto;
        Labeldesc[] gl = this.ls.dyd.gt;
        while(i < this.ls.dyd.n_gt) {
            Labeldesc gt = gl[i];
            if(gt.nactvar > bl.nactvar) {
                if(bl.upval) {
                    this.patchclose(gt.pc, ((int)bl.nactvar));
                }
                gt.nactvar = bl.nactvar;
            }
            if(!this.ls.findlabel(i)) {
                ++i;
            }
        }
    }

    boolean need_value(int list) {
        while(true) {
            if(list == -1) {
                return false;
            }
            if(FuncState.GET_OPCODE(this.getjumpcontrol(list).get()) != 28) {
                return true;
            }
            list = this.getjump(list);
        }
    }

    int newupvalue(LuaString name, expdesc v) {
        boolean z = true;
        this.checklimit(this.nups + 1, 0xFF, "upvalues");
        if(this.f.upvalues == null || this.nups + 1 > this.f.upvalues.length) {
            Prototype prototype0 = this.f;
            prototype0.upvalues = FuncState.realloc(this.f.upvalues, (this.nups <= 0 ? 1 : this.nups * 2));
        }
        Upvaldesc[] arr_upvaldesc = this.f.upvalues;
        int v = this.nups;
        if(v.k != 7) {
            z = false;
        }
        arr_upvaldesc[v] = new Upvaldesc(name, z, v.u.info);
        int v1 = this.nups;
        this.nups = (short)(v1 + 1);
        return v1;
    }

    void nil(int from, int n) {
        int l = from + n - 1;
        if(this.pc > this.lasttarget && this.pc > 0) {
            int previous_code = this.f.code[this.pc - 1];
            if(FuncState.GET_OPCODE(previous_code) == 4) {
                int v4 = FuncState.GETARG_A(previous_code);
                int pl = v4 + FuncState.GETARG_B(previous_code);
                if(v4 <= from && from <= pl + 1 || from <= v4 && v4 <= l + 1) {
                    if(v4 < from) {
                        from = v4;
                    }
                    if(pl > l) {
                        l = pl;
                    }
                    InstructionPtr instructionPtr0 = new InstructionPtr(this.f.code, this.pc - 1);
                    FuncState.SETARG_A(instructionPtr0, from);
                    FuncState.SETARG_B(instructionPtr0, l - from);
                    return;
                }
            }
        }
        this.codeABC(4, from, n - 1, 0);
    }

    int nilK() {
        return this.addk(LuaValue.NIL);
    }

    int numberK(LuaValue r) {
        return this.addk(r);
    }

    void patchclose(int list, int level) {
        while(list != -1) {
            int v2 = this.getjump(list);
            FuncState._assert(FuncState.GET_OPCODE(this.f.code[list]) == 23 && (FuncState.GETARG_A(this.f.code[list]) == 0 || FuncState.GETARG_A(this.f.code[list]) >= level + 1));
            FuncState.SETARG_A(this.f.code, list, level + 1);
            list = v2;
        }
    }

    void patchlist(int list, int target) {
        if(target == this.pc) {
            this.patchtohere(list);
            return;
        }
        FuncState._assert(target < this.pc);
        this.patchlistaux(list, target, 0xFF, target);
    }

    void patchlistaux(int list, int vtarget, int reg, int dtarget) {
        while(list != -1) {
            int v4 = this.getjump(list);
            if(this.patchtestreg(list, reg)) {
                this.fixjump(list, vtarget);
            }
            else {
                this.fixjump(list, dtarget);
            }
            list = v4;
        }
    }

    boolean patchtestreg(int node, int reg) {
        InstructionPtr instructionPtr0 = this.getjumpcontrol(node);
        if(FuncState.GET_OPCODE(instructionPtr0.get()) != 28) {
            return false;
        }
        if(reg != 0xFF && reg != FuncState.GETARG_B(instructionPtr0.get())) {
            FuncState.SETARG_A(instructionPtr0, reg);
            return true;
        }
        instructionPtr0.set(FuncState.CREATE_ABC(27, FuncState.GETARG_B(instructionPtr0.get()), 0, Lua.GETARG_C(instructionPtr0.get())));
        return true;
    }

    void patchtohere(int list) {
        this.getlabel();
        this.concat(this.jpc, list);
    }

    void posfix(int op, expdesc e1, expdesc e2, int line) {
        boolean z = true;
        switch(op) {
            case 0: {
                this.codearith(13, e1, e2, line);
                return;
            }
            case 1: {
                this.codearith(14, e1, e2, line);
                return;
            }
            case 2: {
                this.codearith(15, e1, e2, line);
                return;
            }
            case 3: {
                this.codearith(16, e1, e2, line);
                return;
            }
            case 4: {
                this.codearith(17, e1, e2, line);
                return;
            }
            case 5: {
                this.codearith(18, e1, e2, line);
                return;
            }
            case 6: {
                this.exp2val(e2);
                if(e2.k == 11 && FuncState.GET_OPCODE(this.getcode(e2)) == 22) {
                    if(e1.u.info != FuncState.GETARG_B(this.getcode(e2)) - 1) {
                        z = false;
                    }
                    FuncState._assert(z);
                    this.freeexp(e1);
                    FuncState.SETARG_B(this.getcodePtr(e2), e1.u.info);
                    e1.k = 11;
                    e1.u.info = e2.u.info;
                    return;
                }
                this.exp2nextreg(e2);
                this.codearith(22, e1, e2, line);
                return;
            }
            case 7: {
                this.codecomp(24, 0, e1, e2);
                return;
            }
            case 8: {
                this.codecomp(24, 1, e1, e2);
                return;
            }
            case 9: {
                this.codecomp(25, 1, e1, e2);
                return;
            }
            case 10: {
                this.codecomp(26, 1, e1, e2);
                return;
            }
            case 11: {
                this.codecomp(25, 0, e1, e2);
                return;
            }
            case 12: {
                this.codecomp(26, 0, e1, e2);
                return;
            }
            case 13: {
                if(e1.t.i != -1) {
                    z = false;
                }
                FuncState._assert(z);
                this.dischargevars(e2);
                this.concat(e2.f, e1.f.i);
                e1.setvalue(e2);
                return;
            }
            case 14: {
                if(e1.f.i != -1) {
                    z = false;
                }
                FuncState._assert(z);
                this.dischargevars(e2);
                this.concat(e2.t, e1.t.i);
                e1.setvalue(e2);
                return;
            }
            case 15: {
                this.codearith(40, e1, e2, line);
                return;
            }
            case 16: {
                this.codearith(42, e1, e2, line);
                return;
            }
            case 17: {
                this.codearith(43, e1, e2, line);
                return;
            }
            case 18: {
                this.codearith(44, e1, e2, line);
                return;
            }
            case 19: {
                this.codearith(45, e1, e2, line);
                return;
            }
            case 20: {
                this.codearith(46, e1, e2, line);
                return;
            }
            default: {
                FuncState._assert(false);
            }
        }
    }

    void prefix(int op, expdesc e, int line) {
        expdesc e2 = new expdesc();
        e2.init(5, 0);
        switch(op) {
            case 0: {
                if(e.isnumeral()) {
                    LuaValue luaValue0 = e.u.nval().neg();
                    e.u.setNval(luaValue0);
                    return;
                }
                this.exp2anyreg(e);
                this.codearith(19, e, e2, line);
                return;
            }
            case 1: {
                this.codenot(e);
                return;
            }
            case 2: {
                this.exp2anyreg(e);
                this.codearith(21, e, e2, line);
                return;
            }
            case 4: {
                if(e.isnumeral()) {
                    LuaValue luaValue1 = e.u.nval().bnot();
                    e.u.setNval(luaValue1);
                    return;
                }
                this.exp2anyreg(e);
                this.codearith(41, e, e2, line);
                return;
            }
            default: {
                FuncState._assert(false);
            }
        }
    }

    void removevalues(int list) {
        while(list != -1) {
            this.patchtestreg(list, 0xFF);
            list = this.getjump(list);
        }
    }

    void removevars(int tolevel) {
        this.ls.dyd.n_actvar -= this.nactvar - tolevel;
        while(this.nactvar > tolevel) {
            short v1 = (short)(this.nactvar - 1);
            this.nactvar = v1;
            LocVars locVars0 = this.getlocvar(((int)v1));
            locVars0.endpc = this.pc;
        }
    }

    void reserveregs(int n) {
        this.checkstack(n);
        this.freereg = (short)(this.freereg + n);
    }

    void ret(int first, int nret) {
        this.codeABC(0x1F, first, nret + 1, 0);
    }

    int searchupvalue(LuaString name) {
        Upvaldesc[] up = this.f.upvalues;
        int i;
        for(i = 0; true; ++i) {
            if(i >= this.nups) {
                return -1;
            }
            if(up[i].name.eq_b(name)) {
                break;
            }
        }
        return i;
    }

    int searchvar(LuaString n) {
        int i;
        for(i = this.nactvar - 1; true; --i) {
            if(i < 0) {
                return -1;
            }
            if(n.eq_b(this.getlocvar(i).varname)) {
                break;
            }
        }
        return i;
    }

    void self(expdesc e, expdesc key) {
        this.exp2anyreg(e);
        this.freeexp(e);
        int func = this.freereg;
        this.reserveregs(2);
        this.codeABC(12, func, e.u.info, this.exp2RK(key));
        this.freeexp(key);
        e.u.info = func;
        e.k = 6;
    }

    void setlist(int base, int nelems, int tostore) {
        int c = (nelems - 1) / 50 + 1;
        int b = tostore == -1 ? 0 : tostore;
        if(c <= 0x1FF) {
            this.codeABC(36, base, b, c);
        }
        else {
            this.codeABC(36, base, b, 0);
            this.code(c, this.ls.lastline);
        }
        this.freereg = (short)(base + 1);
    }

    void setmultret(expdesc e) {
        this.setreturns(e, -1);
    }

    void setoneret(expdesc e) {
        switch(e.k) {
            case 12: {
                e.k = 6;
                e.u.info = FuncState.GETARG_A(this.getcode(e));
                return;
            }
            case 13: {
                FuncState.SETARG_B(this.getcodePtr(e), 2);
                e.k = 11;
            }
        }
    }

    void setreturns(expdesc e, int nresults) {
        switch(e.k) {
            case 12: {
                FuncState.SETARG_C(this.getcodePtr(e), nresults + 1);
                return;
            }
            case 13: {
                FuncState.SETARG_B(this.getcodePtr(e), nresults + 1);
                FuncState.SETARG_A(this.getcodePtr(e), ((int)this.freereg));
                this.reserveregs(1);
            }
        }
    }

    static int singlevaraux(FuncState fs, LuaString n, expdesc var, int base) {
        if(fs != null) {
            int v1 = fs.searchvar(n);
            if(v1 >= 0) {
                var.init(7, v1);
                if(base == 0) {
                    fs.markupval(v1);
                }
                return 7;
            }
            int idx = fs.searchupvalue(n);
            boolean z = false;
            if(idx >= 0) {
                z = true;
            }
            else if(FuncState.singlevaraux(fs.prev, n, var, 0) != 0) {
                z = true;
                idx = fs.newupvalue(n, var);
            }
            if(z) {
                var.init(8, idx);
                return 8;
            }
        }
        return 0;
    }

    void storevar(expdesc var, expdesc ex) {
        switch(var.k) {
            case 7: {
                this.freeexp(ex);
                this.exp2reg(ex, var.u.info);
                return;
            }
            case 8: {
                this.codeABC(9, this.exp2anyreg(ex), var.u.info, 0);
                break;
            }
            case 9: {
                int op = var.u.ind_vt == 7 ? 10 : 8;
                int v1 = this.exp2RK(ex);
                this.codeABC(op, ((int)var.u.ind_t), ((int)var.u.ind_idx), v1);
                break;
            }
            default: {
                FuncState._assert(false);
                break;
            }
        }
        this.freeexp(ex);
    }

    void storevar(expdesc lexState$expdesc0, expdesc lexState$expdesc1, int v) {
        if(v != 21) {
            expdesc lexState$expdesc2 = lexState$expdesc0.clone2();
            if(lexState$expdesc0.k == 9) {
                this.dischargevars(lexState$expdesc2);
            }
            if(v == 6) {
                this.reserveregs(2);
                this.exp2reg(lexState$expdesc1, this.freereg - 1);
                this.exp2reg(lexState$expdesc2, this.freereg - 2);
            }
            this.posfix(v, lexState$expdesc2, lexState$expdesc1, this.ls.linenumber);
            lexState$expdesc1 = lexState$expdesc2;
        }
        switch(lexState$expdesc0.k) {
            case 7: {
                this.freeexp(lexState$expdesc1);
                this.exp2reg(lexState$expdesc1, lexState$expdesc0.u.info);
                return;
            }
            case 8: {
                this.codeABC(9, this.exp2anyreg(lexState$expdesc1), lexState$expdesc0.u.info, 0);
                break;
            }
            case 9: {
                int v1 = lexState$expdesc0.u.ind_vt == 7 ? 10 : 8;
                int v2 = this.exp2RK(lexState$expdesc1);
                this.codeABC(v1, ((int)lexState$expdesc0.u.ind_t), ((int)lexState$expdesc0.u.ind_idx), v2);
                break;
            }
            default: {
                FuncState._assert(false);
                break;
            }
        }
        this.freeexp(lexState$expdesc1);
    }

    int stringK(LuaString s) {
        return this.addk(s);
    }

    @Override
    public String toString() {
        return "FuncState [pc=" + this.pc + ", lasttarget=" + this.lasttarget + ", nk=" + this.nk + ", np=" + this.np + ", firstlocal=" + this.firstlocal + ", nlocvars=" + ((int)this.nlocvars) + ", nactvar=" + ((int)this.nactvar) + ", nups=" + ((int)this.nups) + ", freereg=" + ((int)this.freereg) + ']';
    }

    static boolean vkisinreg(int k) {
        return k == 6 || k == 7;
    }
}

