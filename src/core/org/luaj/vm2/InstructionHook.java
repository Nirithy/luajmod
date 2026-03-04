package org.luaj.vm2;

public interface InstructionHook {
    /**
     * Hook called before an instruction is executed.
     * @param pc The program counter.
     * @param instruction The instruction code.
     * @param closure The current LuaClosure executing the code.
     * @param stack The current execution stack.
     * @return true to continue executing the original instruction, false to skip it.
     */
    boolean onPreInstruction(int pc, int instruction, LuaClosure closure, LuaValue[] stack);

    /**
     * Hook called after an instruction is executed.
     * @param pc The program counter that was just executed.
     * @param instruction The instruction code that was executed.
     * @param closure The current LuaClosure executing the code.
     * @param stack The current execution stack.
     */
    void onPostInstruction(int pc, int instruction, LuaClosure closure, LuaValue[] stack);
}
