package luaj.compiler;

import lasm.LasmPrototype;

public class LasmLua extends FuncState {
    final LasmPrototype p;

    public LasmLua(LasmPrototype p) {
        this.p = p;
        this.ls = new LexState(null, null);
    }

    @Override  // luaj.compiler.FuncState
    int code(int instruction, int line) {
        return this.p.addOp(instruction);
    }

    @Override  // luaj.compiler.FuncState
    public int codeABC(int o, int a, int b, int c) {
        return super.codeABC(o, a, b, c);
    }

    @Override  // luaj.compiler.FuncState
    public int codeABx(int o, int a, int bc) {
        return super.codeABx(o, a, bc);
    }

    @Override  // luaj.compiler.FuncState
    public int codeAsBx(int o, int A, int sBx) {
        return super.codeAsBx(o, A, sBx);
    }

    @Override  // luaj.compiler.FuncState
    public int codeK(int reg, int k) {
        return super.codeK(reg, k);
    }

    public void codeList(int a, int b, int c) {
        if(c <= 0x1FF) {
            this.codeABC(36, a, b, c);
            return;
        }
        this.codeABC(36, a, b, 0);
        this.code(c, this.ls.lastline);
    }
}

