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
                System.out.println("  flowchart <id>    - Reconstruct and print Control Flow Graph (CFG)");
                System.out.println("  ast <id>          - Reconstruct AST (Recursive statements, jump statements, table indices)");
                System.out.println("  quit              - Exit");
                System.out.print("\nEnter command: ");

                String input = scanner.nextLine().trim();
                if (input.equals("quit") || input.equals("exit")) {
                    break;
                } else if (input.equals("funcs")) {
                    listFunctions();
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
}
