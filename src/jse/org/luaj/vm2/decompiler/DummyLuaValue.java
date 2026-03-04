package org.luaj.vm2.decompiler;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * A tolerant implementation of LuaValue that never throws an exception
 * when missing properties or methods are accessed. 
 * This is used by the PseudoExecution engine to prevent failures
 * when evaluating incomplete or garbage instructions.
 */
public class DummyLuaValue extends LuaValue {

    private final String name;

    public DummyLuaValue(String name) {
        this.name = name;
    }

    @Override
    public int type() {
        return LuaValue.TTABLE; // Return TTABLE to avoid errors in OP_GETTABLE etc if checked
    }

    @Override
    public boolean istable() { return true; }

    @Override
    public boolean isfunction() { return true; }

    @Override
    public String typename() {
        return "dummy_" + name;
    }

    @Override
    public String tojstring() {
        return typename();
    }

    @Override
    public boolean toboolean() { return true; }

    @Override
    public LuaValue not() { return LuaValue.FALSE; }

    @Override
    public int checkint() { return 0; }

    @Override
    public double checkdouble() { return 0.0; }

    @Override
    public org.luaj.vm2.LuaString checkstring() { return org.luaj.vm2.LuaString.valueOf(tojstring()); }

    @Override
    public int length() { return 0; }

    @Override
    public LuaValue len() { return LuaValue.valueOf(0); }

    @Override
    public LuaValue add(LuaValue rhs) { return new DummyLuaValue("(" + name + " + " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue sub(LuaValue rhs) { return new DummyLuaValue("(" + name + " - " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue mul(LuaValue rhs) { return new DummyLuaValue("(" + name + " * " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue div(LuaValue rhs) { return new DummyLuaValue("(" + name + " / " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue mod(LuaValue rhs) { return new DummyLuaValue("(" + name + " % " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue pow(LuaValue rhs) { return new DummyLuaValue("(" + name + " ^ " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue concat(LuaValue rhs) { return new DummyLuaValue("(" + name + " .. " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue concatTo(LuaValue lhs) { return new DummyLuaValue("(" + lhs.tojstring() + " .. " + name + ")"); }

    @Override
    public LuaValue idiv(LuaValue rhs) { return new DummyLuaValue("(" + name + " // " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue band(LuaValue rhs) { return new DummyLuaValue("(" + name + " & " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue bor(LuaValue rhs) { return new DummyLuaValue("(" + name + " | " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue bxor(LuaValue rhs) { return new DummyLuaValue("(" + name + " ~ " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue shl(LuaValue rhs) { return new DummyLuaValue("(" + name + " << " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue shr(LuaValue rhs) { return new DummyLuaValue("(" + name + " >> " + rhs.tojstring() + ")"); }
    @Override
    public LuaValue bnot() { return new DummyLuaValue("(~" + name + ")"); }

    @Override
    public boolean eq_b(LuaValue val) { return false; }
    @Override
    public LuaValue eq(LuaValue val) { return LuaValue.FALSE; }
    @Override
    public boolean lt_b(LuaValue val) { return false; }
    @Override
    public boolean lteq_b(LuaValue val) { return false; }
    @Override
    public int strcmp(org.luaj.vm2.LuaString rhs) { return 0; }

    // Always return a DummyLuaValue to prevent errors chaining
    @Override
    public LuaValue get(LuaValue key) {
        return new DummyLuaValue(name + "." + key.tojstring());
    }

    @Override
    public LuaValue get(int key) {
        return new DummyLuaValue(name + "[" + key + "]");
    }

    @Override
    public LuaValue get(String key) {
        return new DummyLuaValue(name + "." + key);
    }

    @Override
    public void set(LuaValue key, LuaValue value) {
        // Do nothing, tolerant
    }

    @Override
    public void set(int key, LuaValue value) {
        // Do nothing, tolerant
    }

    @Override
    public void set(String key, LuaValue value) {
        // Do nothing, tolerant
    }

    @Override
    public LuaValue call() {
        return new DummyLuaValue(name + "()");
    }

    @Override
    public LuaValue call(LuaValue arg) {
        return new DummyLuaValue(name + "(arg)");
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return new DummyLuaValue(name + "(arg1, arg2)");
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        return new DummyLuaValue(name + "(arg1, arg2, arg3)");
    }

    @Override
    public Varargs invoke(Varargs args) {
        return new DummyLuaValue(name + "(...)");
    }

    @Override
    public LuaValue arg(int n) {
        if (n == 1) return this;
        return new DummyLuaValue(name + "_ret" + n);
    }

    @Override
    public LuaValue arg1() {
        return this;
    }

    @Override
    public LuaValue method(String name) {
        return new DummyLuaValue(this.name + ":" + name + "()");
    }
    
    @Override
    public LuaValue method(LuaValue name) {
        return new DummyLuaValue(this.name + ":" + name.tojstring() + "()");
    }
}
