package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.InputStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.InstructionHook;
import org.luaj.vm2.lib.jse.JsePlatform;

public class AllInOneEngine {

    public static void main(String[] args) {
        String scriptPath = args.length > 0 ? args[0] : "test_engine.lua";
        System.out.println("====== AllInOneEngine Started ======");
        System.out.println("Loading script: " + scriptPath);

        Globals globals = JsePlatform.standardGlobals();

        globals.instructionHook = new InstructionHook() {
            @Override
            public boolean onPreInstruction(int pc, int instruction, LuaClosure closure, LuaValue[] stack) {
                System.out.println("[PRE] PC: " + pc + " | OP: " + (instruction & 0x3f) + " | Function: " + closure.name());
                return true;
            }

            @Override
            public void onPostInstruction(int pc, int instruction, LuaClosure closure, LuaValue[] stack) {
                System.out.println("[POST] PC: " + pc + " executed.");
            }
        };

        try {
            LuaValue chunk = globals.loadfile(scriptPath);
            System.out.println("Script loaded successfully. Decompiling...");

            if (chunk instanceof LuaClosure) {
                LuaClosure closure = (LuaClosure) chunk;
                AstDecompiler.Block ast = AstDecompiler.parseCFG(closure.p);
                System.out.println("\n--- AST Decompiled Source ---");
                System.out.println(ast.format(0));
                System.out.println("-----------------------------\n");
            }

            System.out.println("Executing Script with Instruction Hooks...\n");
            chunk.call();
            System.out.println("\nExecution Finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
