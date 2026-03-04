package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.StringInterceptor;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * 交互式反编译器 - 支持选择执行特定函数
 */
public class InteractiveDecompiler {

    private static List<PrototypeInfo> functionList = new ArrayList<>();
    private static Globals globals;
    private static Scanner scanner;

    /**
     * 函数信息
     */
    static class PrototypeInfo {
        int id;
        int depth;
        String name;
        Prototype prototype;
        String parentName;
        
        PrototypeInfo(int id, int depth, String name, Prototype p, String parentName) {
            this.id = id;
            this.depth = depth;
            this.name = name;
            this.prototype = p;
            this.parentName = parentName;
        }
    }

    /**
     * 主入口
     */
    public static void main(String[] args) {
        System.out.println("========== LuaJ 交互式反编译器 ==========\n");
        
        if (args.length < 1) {
            System.out.println("用法: java InteractiveDecompiler <luac文件>");
            return;
        }
        
        String filePath = args[0];
        System.out.println("加载文件: " + filePath);
        
        try {
            globals = JsePlatform.standardGlobals();
            scanner = new Scanner(System.in);
            
            InputStream is = new FileInputStream(filePath);
            Prototype mainProto = LoadState.undump(is, filePath);
            is.close();
            
            if (mainProto == null) {
                System.out.println("无法加载文件");
                return;
            }
            
            // 收集所有函数
            collectPrototypes(mainProto, 0, "main", "");
            
            System.out.println("\n加载完成！共发现 " + functionList.size() + " 个函数\n");
            
            // 主循环
            while (true) {
                System.out.println("\n========== 命令菜单 ==========");
                System.out.println("  list     - 列出所有函数");
                System.out.println("  run <id> - 执行指定函数");
                System.out.println("  info <id>- 显示函数详情");
                System.out.println("  strings  - 显示捕获的字符串");
                System.out.println("  quit     - 退出");
                System.out.print("\n请输入命令: ");
                
                String input = scanner.nextLine().trim();
                
                if (input.equals("quit") || input.equals("exit")) {
                    break;
                } else if (input.equals("list")) {
                    listFunctions();
                } else if (input.equals("strings")) {
                    showStrings();
                } else if (input.startsWith("run ")) {
                    try {
                        int id = Integer.parseInt(input.substring(4).trim());
                        runFunction(id);
                    } catch (NumberFormatException e) {
                        System.out.println("无效的函数ID");
                    }
                } else if (input.startsWith("info ")) {
                    try {
                        int id = Integer.parseInt(input.substring(5).trim());
                        showInfo(id);
                    } catch (NumberFormatException e) {
                        System.out.println("无效的函数ID");
                    }
                } else if (input.isEmpty()) {
                    continue;
                } else {
                    System.out.println("未知命令: " + input);
                }
            }
            
            scanner.close();
            System.out.println("退出");
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 收集所有Prototype
     */
    private static void collectPrototypes(Prototype p, int depth, String name, String parentName) {
        if (p == null) return;
        
        int id = functionList.size();
        functionList.add(new PrototypeInfo(id, depth, name, p, parentName));
        
        if (p.p != null) {
            for (int i = 0; i < p.p.length; i++) {
                collectPrototypes(p.p[i], depth + 1, name + "_sub" + i, name);
            }
        }
    }
    
    /**
     * 列出所有函数
     */
    private static void listFunctions() {
        System.out.println("\n========== 函数列表 ==========");
        System.out.printf("%-4s %-6s %-20s %s\n", "ID", "深度", "名称", "指令数/常量数");
        System.out.println("-".repeat(50));
        
        for (PrototypeInfo info : functionList) {
            String indent = "  ".repeat(info.depth);
            int codeLen = info.prototype.code != null ? info.prototype.code.length : 0;
            int kLen = info.prototype.k != null ? info.prototype.k.length : 0;
            System.out.printf("%-4d %-6d %s%s (%d/%d)\n", 
                info.id, info.depth, indent, info.name, codeLen, kLen);
        }
    }
    
    /**
     * 显示函数详情
     */
    private static void showInfo(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }
        
        PrototypeInfo info = functionList.get(id);
        Prototype p = info.prototype;
        
        System.out.println("\n========== 函数 #" + id + " 详情 ==========");
        System.out.println("名称: " + info.name);
        System.out.println("深度: " + info.depth);
        System.out.println("父函数: " + (info.parentName.isEmpty() ? "无" : info.parentName));
        System.out.println("指令数: " + (p.code != null ? p.code.length : 0));
        System.out.println("栈大小: " + p.maxstacksize);
        System.out.println("参数数量: " + p.numparams);
        System.out.println("常量数量: " + (p.k != null ? p.k.length : 0));
        System.out.println("Upvalue数量: " + (p.upvalues != null ? p.upvalues.length : 0));
        System.out.println("子函数数量: " + (p.p != null ? p.p.length : 0));
        
        if (p.k != null && p.k.length > 0) {
            System.out.println("\n常量列表:");
            for (int i = 0; i < Math.min(p.k.length, 50); i++) {
                String val = p.k[i].tojstring();
                if (val.length() > 80) val = val.substring(0, 80) + "...";
                System.out.println("  [" + i + "] " + val);
            }
            if (p.k.length > 50) {
                System.out.println("  ... 还有 " + (p.k.length - 50) + " 个常量");
            }
        }
    }
    
    /**
     * 执行指定函数
     */
    private static void runFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }
        
        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 执行函数 #" + id + ": " + info.name + " ==========");
        
        StringInterceptor.enableMemory();
        
        try {
            pseudoExecute(info.prototype);
        } catch (Exception e) {
            System.out.println("执行出错: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n执行完成！");
        showStrings();
    }
    
    /**
     * 显示捕获的字符串
     */
    private static void showStrings() {
        System.out.println("\n========== 捕获的字符串 ==========");
        
        var chineseStrings = StringInterceptor.getChineseStrings();
        if (!chineseStrings.isEmpty()) {
            System.out.println("\n【中文字符串】(" + chineseStrings.size() + " 个)");
            for (String s : chineseStrings) {
                System.out.println("  " + s);
            }
        }
        
        var allStrings = StringInterceptor.getAllStrings();
        System.out.println("\n【所有有意义字符串】(" + allStrings.size() + " 个)");
        for (String s : allStrings) {
            String prefix = chineseStrings.contains(s) ? "[中文] " : "";
            String truncated = s.length() > 200 ? s.substring(0, 200) + "..." : s;
            System.out.println("  " + prefix + truncated);
        }
    }
    
    /**
     * 伪执行Prototype
     */
    private static void pseudoExecute(Prototype p) {
        if (p == null || p.code == null) return;
        
        int[] code = p.code;
        LuaValue[] k = p.k;
        int maxStack = p.maxstacksize > 0 ? p.maxstacksize : 256;
        
        LuaValue[] stack = new LuaValue[maxStack];
        for (int j = 0; j < stack.length; j++) {
            stack[j] = LuaValue.NIL;
        }
        
        LuaValue[] upvalues = new LuaValue[p.upvalues != null ? p.upvalues.length : 0];
        for (int j = 0; j < upvalues.length; j++) {
            upvalues[j] = new DummyLuaValue("U" + j);
        }
        
        int pc = 0;
        int execCount = 0;
        int maxExec = code.length * 5;
        
        while (pc < code.length && execCount < maxExec) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);
            
            try {
                switch (opcode) {
                    case Lua.OP_MOVE:
                        stack[a] = stack[b];
                        break;
                        
                    case Lua.OP_LOADK:
                        stack[a] = k[bx];
                        if (k[bx].isstring()) {
                            StringInterceptor.onStringCreate(
                                ((LuaString)k[bx]).m_bytes,
                                ((LuaString)k[bx]).m_offset,
                                ((LuaString)k[bx]).m_length
                            );
                        }
                        break;
                        
                    case Lua.OP_LOADKX:
                        int nextI = code[pc + 1];
                        if (Lua.GET_OPCODE(nextI) == Lua.OP_EXTRAARG) {
                            int ax = Lua.GETARG_Ax(nextI);
                            stack[a] = k[ax];
                            pc++;
                        }
                        break;
                        
                    case Lua.OP_LOADBOOL:
                        stack[a] = (b != 0) ? LuaValue.TRUE : LuaValue.FALSE;
                        if (c != 0) pc++;
                        break;
                        
                    case Lua.OP_LOADNIL:
                        for (int j = a; j <= a + b; j++) {
                            stack[j] = LuaValue.NIL;
                        }
                        break;
                        
                    case Lua.OP_GETUPVAL:
                        stack[a] = upvalues[b];
                        break;
                        
                    case Lua.OP_GETTABUP:
                        LuaValue key1 = c > 0xff ? k[c & 0xff] : stack[c];
                        stack[a] = new DummyLuaValue("U" + b + "." + key1.tojstring());
                        break;
                        
                    case Lua.OP_GETTABLE:
                        LuaValue key2 = c > 0xff ? k[c & 0xff] : stack[c];
                        stack[a] = new DummyLuaValue("R" + b + "." + key2.tojstring());
                        break;
                        
                    case Lua.OP_SETTABUP:
                    case Lua.OP_SETUPVAL:
                    case Lua.OP_SETTABLE:
                    case Lua.OP_NEWTABLE:
                    case Lua.OP_SELF:
                    case Lua.OP_ADD:
                    case Lua.OP_SUB:
                    case Lua.OP_MUL:
                    case Lua.OP_DIV:
                    case Lua.OP_MOD:
                    case Lua.OP_POW:
                    case Lua.OP_UNM:
                    case Lua.OP_NOT:
                    case Lua.OP_LEN:
                        if (opcode != Lua.OP_SETTABUP && opcode != Lua.OP_SETUPVAL && opcode != Lua.OP_SETTABLE) {
                            stack[a] = new DummyLuaValue("Result");
                        }
                        break;
                        
                    case Lua.OP_CONCAT:
                        StringBuilder sb = new StringBuilder();
                        for (int j = b; j <= c; j++) {
                            if (stack[j] != null && !stack[j].isnil()) {
                                sb.append(stack[j].tojstring());
                            }
                        }
                        String concatResult = sb.toString();
                        stack[a] = LuaValue.valueOf(concatResult);
                        StringInterceptor.onStringCreate(concatResult.getBytes(), 0, concatResult.length());
                        break;
                        
                    case Lua.OP_JMP:
                        pc += sbx;
                        break;
                        
                    case Lua.OP_EQ:
                    case Lua.OP_LT:
                    case Lua.OP_LE:
                    case Lua.OP_TEST:
                    case Lua.OP_TESTSET:
                        break;
                        
                    case Lua.OP_CALL:
                        for (int j = 0; j < c - 1; j++) {
                            stack[a + j] = new DummyLuaValue("Ret" + j);
                        }
                        break;
                        
                    case Lua.OP_TAILCALL:
                    case Lua.OP_RETURN:
                        break;
                        
                    case Lua.OP_FORLOOP:
                    case Lua.OP_FORPREP:
                        if (opcode == Lua.OP_FORPREP) {
                            pc += sbx;
                        }
                        break;
                        
                    case Lua.OP_TFORCALL:
                    case Lua.OP_TFORLOOP:
                        break;
                        
                    case Lua.OP_SETLIST:
                        break;
                        
                    case Lua.OP_CLOSURE:
                        stack[a] = new DummyLuaValue("Closure_" + bx);
                        break;
                        
                    case Lua.OP_VARARG:
                        stack[a] = new DummyLuaValue("VarArg");
                        break;
                        
                    case Lua.OP_EXTRAARG:
                        break;
                        
                    default:
                        break;
                }
            } catch (Exception e) {
                // 忽略错误继续执行
            }
            
            pc++;
            execCount++;
        }
        
        System.out.println("执行了 " + execCount + " 步指令");
    }
}
