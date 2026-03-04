package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;

public class SuperDecompilerEngine {
    private static List<InteractiveDecompiler.PrototypeInfo> functionList = new ArrayList<>();
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("=       Super Decompiler Engine (Lua IDA)       =");
        System.out.println("=================================================");

        if (args.length < 1) {
            System.out.println("Usage: java SuperDecompilerEngine <file.luac>");
            return;
        }

        String filePath = args[0];
        System.out.println("Loading file: " + filePath);

        try {
            LuaValue.s_tolerantMode = true; // Use tolerant execution engine if pseudo-eval is needed

            InputStream is = new FileInputStream(filePath);
            Prototype mainProto = LoadState.undump(is, filePath);
            is.close();

            if (mainProto == null) {
                System.out.println("Failed to load file.");
                return;
            }

            collectPrototypes(mainProto, 0, "main", "");
            System.out.println("Loaded successfully! " + functionList.size() + " functions discovered.");

            scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n========== Commands ==========");
                System.out.println("  funcs             - List all function structures");
                System.out.println("  info <id>         - Show detailed function information");
                System.out.println("  disasm <id>       - Disassemble instructions");
                System.out.println("  flowchart <id>    - Reconstruct and print Control Flow Graph (CFG)");
                System.out.println("  ast <id>          - Reconstruct AST (Recursive statements, jump statements, table indices)");
                System.out.println("  analyze <id>      - Run disasm, flowchart, and ast");
                System.out.println("  run <id>          - Pseudo-execute the function");
                System.out.println("  strings           - Show captured strings");
                System.out.println("  quit              - Exit");
                System.out.print("\nEnter command: ");

                String input = scanner.nextLine().trim();
                if (input.equals("quit") || input.equals("exit")) {
                    break;
                } else if (input.equals("funcs")) {
                    listFunctions();
                } else if (input.equals("strings")) {
                    printStrings();
                } else if (input.startsWith("info ")) {
                    try {
                        int id = Integer.parseInt(input.substring(5).trim());
                        printInfo(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.startsWith("disasm ")) {
                    try {
                        int id = Integer.parseInt(input.substring(7).trim());
                        printDisasm(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.startsWith("flowchart ")) {
                    try {
                        int id = Integer.parseInt(input.substring(10).trim());
                        printFlowchart(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.startsWith("ast ")) {
                    try {
                        int id = Integer.parseInt(input.substring(4).trim());
                        printAst(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.startsWith("analyze ")) {
                    try {
                        int id = Integer.parseInt(input.substring(8).trim());
                        printDisasm(id);
                        printFlowchart(id);
                        printAst(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.startsWith("run ")) {
                    try {
                        int id = Integer.parseInt(input.substring(4).trim());
                        runFunction(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                } else if (input.isEmpty()) {
                    continue;
                } else {
                    System.out.println("Unknown command: " + input);
                }
            }

            scanner.close();
            System.out.println("Exiting Super Decompiler Engine.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void collectPrototypes(Prototype p, int depth, String name, String parentName) {
        if (p == null) return;

        int id = functionList.size();
        functionList.add(new InteractiveDecompiler.PrototypeInfo(id, depth, name, p, parentName));

        if (p.p != null) {
            for (int i = 0; i < p.p.length; i++) {
                collectPrototypes(p.p[i], depth + 1, name + "_sub" + i, name);
            }
        }
    }

    private static void listFunctions() {
        System.out.println("\n========== Function Structures ==========");
        System.out.printf("%-4s %-6s %-20s %s\n", "ID", "Depth", "Name", "Parent");
        System.out.println("--------------------------------------------------");

        for (InteractiveDecompiler.PrototypeInfo info : functionList) {
            String indent = "  ".repeat(info.depth);
            System.out.printf("%-4d %-6d %s%s (Parent: %s)\n",
                info.id, info.depth, indent, info.name, info.parentName.isEmpty() ? "None" : info.parentName);
        }
    }

    private static void printFlowchart(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("Invalid function ID.");
            return;
        }

        InteractiveDecompiler.PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== Flowchart for #" + id + ": " + info.name + " ==========");
        CFGBuilder.printFlowchart(info.prototype);
    }

    private static void printAst(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("Invalid function ID.");
            return;
        }

        InteractiveDecompiler.PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== AST for #" + id + ": " + info.name + " ==========");
        AstDecompiler.Block ast = AstDecompiler.parseCFG(info.prototype);
        System.out.println(ast.format(0));
    }

    private static void printInfo(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("Invalid function ID.");
            return;
        }

        InteractiveDecompiler.PrototypeInfo info = functionList.get(id);
        Prototype p = info.prototype;

        System.out.println("\n========== Info for #" + id + ": " + info.name + " ==========");
        System.out.println("Name: " + info.name);
        System.out.println("Depth: " + info.depth);
        System.out.println("Parent: " + (info.parentName.isEmpty() ? "None" : info.parentName));
        System.out.println("Instructions: " + (p.code != null ? p.code.length : 0));
        System.out.println("Max Stack Size: " + p.maxstacksize);
        System.out.println("Parameters: " + p.numparams);
        System.out.println("Constants: " + (p.k != null ? p.k.length : 0));
        System.out.println("Upvalues: " + (p.upvalues != null ? p.upvalues.length : 0));
        System.out.println("Child Prototypes: " + (p.p != null ? p.p.length : 0));

        if (p.k != null && p.k.length > 0) {
            System.out.println("\nConstants:");
            for (int i = 0; i < Math.min(p.k.length, 50); i++) {
                String val = p.k[i].tojstring();
                if (val.length() > 80) val = val.substring(0, 80) + "...";
                System.out.println("  [" + i + "] " + val);
            }
            if (p.k.length > 50) {
                System.out.println("  ... and " + (p.k.length - 50) + " more constants");
            }
        }
    }

    private static void printDisasm(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("Invalid function ID.");
            return;
        }

        InteractiveDecompiler.PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== Disassembly for #" + id + ": " + info.name + " ==========");
        Disassembler.disassemble(info.prototype);
    }

    private static void runFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("Invalid function ID.");
            return;
        }

        InteractiveDecompiler.PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== Pseudo-executing #" + id + ": " + info.name + " ==========");

        try {
            boolean oldTolerant = LuaValue.s_tolerantMode;
            LuaValue.s_tolerantMode = true;
            org.luaj.vm2.LuaClosure closure = new org.luaj.vm2.LuaClosure(info.prototype, org.luaj.vm2.lib.jse.JsePlatform.standardGlobals());
            closure.call();
            LuaValue.s_tolerantMode = oldTolerant;
            System.out.println("Execution completed without fatal crash (tolerant mode).");
        } catch (Exception e) {
            System.out.println("Execution threw an exception: " + e.getMessage());
        }
    }

    private static void printStrings() {
        System.out.println("\n========== Strings found in all prototypes ==========");
        java.util.Set<String> uniqueStrings = new java.util.HashSet<>();
        for (InteractiveDecompiler.PrototypeInfo info : functionList) {
            if (info.prototype.k != null) {
                for (LuaValue k : info.prototype.k) {
                    if (k != null && k.isstring()) {
                        String s = k.tojstring();
                        if (!s.isEmpty()) {
                            uniqueStrings.add(s);
                        }
                    }
                }
            }
        }

        for (String s : uniqueStrings) {
            String truncated = s.length() > 200 ? s.substring(0, 200) + "..." : s;
            System.out.println("  " + truncated);
        }
    }
}
