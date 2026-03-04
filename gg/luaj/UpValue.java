package luaj;

public final class UpValue {
    LuaValue[] array;
    int index;

    public UpValue(LuaValue[] stack, int index) {
        this.array = stack;
        this.index = index;
    }

    public final void close() {
        LuaValue[] old = this.array;
        this.array = new LuaValue[]{old[this.index]};
        old[this.index] = null;
        this.index = 0;
    }

    public final LuaValue getValue() {
        return this.array[this.index];
    }

    public final void setValue(LuaValue value) {
        this.array[this.index] = value;
    }

    @Override
    public String toString() {
        return this.index + '/' + this.array.length + ' ' + this.array[this.index];
    }

    public String tojstring() {
        return this.array[this.index].tojstring();
    }
}

