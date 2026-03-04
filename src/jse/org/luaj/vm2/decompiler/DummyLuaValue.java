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
        return LuaValue.TUSERDATA;
    }

    @Override
    public String typename() {
        return "dummy_" + name;
    }

    @Override
    public String tojstring() {
        return typename();
    }

    // Always return a DummyLuaValue to prevent errors chaining
    @Override
    public LuaValue get(LuaValue key) {
        return new DummyLuaValue(name + "." + key.tojstring());
    }

    @Override
    public void set(LuaValue key, LuaValue value) {
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
    public LuaValue method(String name) {
        return new DummyLuaValue(this.name + ":" + name + "()");
    }
    
    @Override
    public LuaValue method(LuaValue name) {
        return new DummyLuaValue(this.name + ":" + name.tojstring() + "()");
    }
}
