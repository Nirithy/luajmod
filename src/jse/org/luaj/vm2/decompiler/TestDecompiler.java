package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.StringInterceptor;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * LuaJ 反编译器 - 主入口
 * 功能：
 * 1. 反汇编Lua字节码
 * 2. 伪执行分析
 * 3. 字符串捕获
 * 4. 交互式函数执行
 */
public class TestDecompiler {

    private static PrintWriter writer;
    private static Scanner scanner;
    private static List<PrototypeInfo> functionList = new ArrayList<>();
    private static Prototype mainPrototype;
    private static Globals globals;
    private static Globals execGlobals;
    private static Map<String, LuaValue> userFunctions = new HashMap<>();
    private static Map<String, Map<String, LuaValue>> tableMethods = new HashMap<>();
    private static boolean inRealExecution = false;
    private static String currentMissingFunc = null;
    
    private static List<String> createdTables = new ArrayList<>();
    private static List<String> createdClosures = new ArrayList<>();
    private static List<String> globalAccesses = new ArrayList<>();
    private static List<String> tableAccesses = new ArrayList<>();
    private static List<String> functionCalls = new ArrayList<>();
    private static Map<String, Set<String>> tableContents = new HashMap<>();
    private static int tableCounter = 0;
    private static int closureCounter = 0;

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
        System.out.println("========== LuaJ 反编译器 ==========\n");
        
        globals = JsePlatform.standardGlobals();
        
        if (args.length > 0) {
            String filePath = args[0];
            
            try {
                InputStream is = new FileInputStream(filePath);
                mainPrototype = LoadState.undump(is, filePath);
                is.close();
                
                if (mainPrototype == null) {
                    System.err.println("无法加载文件");
                    return;
                }
                
                // 收集所有函数
                collectPrototypes(mainPrototype, 0, "main", "");
                
                System.out.println("加载文件: " + filePath);
                System.out.println("共发现 " + functionList.size() + " 个函数\n");
                
                LuaValue.s_tolerantMode = true; // 开启底层极度宽容模式

                System.out.println("========== 自动全量分析 ==========");
                for (PrototypeInfo info : functionList) {
                    System.out.println("\n--- 反汇编函数 #" + info.id + ": " + info.name + " ---");
                    disassembleToConsole(info.prototype, info.name);

                    System.out.println("\n--- 还原伪代码 #" + info.id + ": " + info.name + " ---");
                    System.out.println(PseudoCompiler.decompile(info.prototype));
                }

                if (scanner == null) scanner = new Scanner(System.in);
                if (execGlobals == null) {
                    execGlobals = JsePlatform.standardGlobals();
                    setupExecGlobals();
                }

                System.out.println("\n========== 自动伪执行 (捕获行为和字符串) ==========");
                runAllFunctions();

                System.out.println("\n========== 自动真执行 (宽容模式) ==========");
                execAllFunctions();

                // 进入交互模式
                runInteractiveMode(filePath);
                
            } catch (Exception e) {
                System.err.println("错误: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            runBuiltInTests(globals);
        }
        
        System.out.println("========== 完成 ==========");
    }
    
    /**
     * 从InputStream分析字节码（供主解释器调用）
     */
    public static void analyzeBytecode(InputStream is, String chunkname) {
        System.out.println("========== LuaJ 反编译器 ==========\n");
        System.out.println("分析文件: " + chunkname);
        
        globals = JsePlatform.standardGlobals();
        StringInterceptor.enableMemory();
        
        try {
            mainPrototype = LoadState.undump(is, chunkname);
            
            if (mainPrototype == null) {
                System.err.println("无法加载文件");
                return;
            }
            
            // 收集所有函数
            collectPrototypes(mainPrototype, 0, "main", "");
            
            System.out.println("共发现 " + functionList.size() + " 个函数\n");
            
            LuaValue.s_tolerantMode = true; // 开启底层极度宽容模式

            System.out.println("========== 自动全量分析 ==========");
            for (PrototypeInfo info : functionList) {
                System.out.println("\n--- 反汇编函数 #" + info.id + ": " + info.name + " ---");
                disassembleToConsole(info.prototype, info.name);

                System.out.println("\n--- 还原伪代码 #" + info.id + ": " + info.name + " ---");
                System.out.println(PseudoCompiler.decompile(info.prototype));
            }

            // 进入交互模式
            if (scanner == null) scanner = new Scanner(System.in);
            if (execGlobals == null) {
                execGlobals = JsePlatform.standardGlobals();
                setupExecGlobals();
            }

            System.out.println("\n========== 自动真执行 (宽容模式) ==========");
            execAllFunctions();

            runInteractiveMode(chunkname);
            
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
     * 交互模式
     */
    private static void runInteractiveMode(String filePath) {
        if (scanner == null) scanner = new Scanner(System.in);
        if (execGlobals == null) {
            execGlobals = JsePlatform.standardGlobals();
            setupExecGlobals();
        }
        
        while (true) {
            System.out.println("\n========== 命令菜单 ==========");
            System.out.println("  list              - 列出所有函数");
            System.out.println("  info <id>         - 显示函数详情");
            System.out.println("  run <id>          - 伪执行指定函数");
            System.out.println("  runall            - 伪执行所有函数");
        System.out.println("  exec <id>         - 真执行指定函数(容错)");
        System.out.println("  execall           - 真执行所有函数(容错)");
            System.out.println("  disasm <id>       - 反汇编指定函数");
        System.out.println("  decompile <id>    - 尝试还原伪代码(含CFG)");
        System.out.println("  cfg <id>          - 显示控制流图(Basic Blocks)");
            System.out.println("  strings           - 显示捕获的字符串");
            System.out.println("  funcs             - 显示已添加的用户函数");
            System.out.println("  env               - 显示环境变量和已补全的表/方法");
            System.out.println("  add <名称>        - 添加用户函数");
            System.out.println("  edit <名称>       - 编辑/替换环境变量或函数");
            System.out.println("  del <名称>        - 删除用户函数");
            System.out.println("  analyze           - 完整分析(反汇编+伪执行)");
            System.out.println("  export <文件>     - 导出完整分析到文件");
            System.out.println("  quit              - 退出");
            System.out.print("\n请输入命令: ");
            
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();
            String arg = parts.length > 1 ? parts[1] : "";
            
            switch (cmd) {
                case "quit":
                case "exit":
                    return;
                    
                case "list":
                    listFunctions();
                    break;
                    
                case "info":
                    try {
                        showInfo(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: info <函数ID>");
                    }
                    break;
                    
                case "run":
                    try {
                        runFunction(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: run <函数ID>");
                    }
                    break;
                    
                case "runall":
                    runAllFunctions();
                    break;
                    
                case "exec":
                    try {
                        execFunction(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: exec <函数ID>");
                    }
                    break;
                    
                case "execall":
                    execAllFunctions();
                    break;
                    
                case "disasm":
                    try {
                        disasmFunction(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: disasm <函数ID>");
                    }
                    break;
                    
                case "decompile":
                    try {
                        decompileFunction(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: decompile <函数ID>");
                    }
                    break;

                case "cfg":
                    try {
                        showCFG(Integer.parseInt(arg.trim()));
                    } catch (Exception e) {
                        System.out.println("用法: cfg <函数ID>");
                    }
                    break;

                case "strings":
                    showStrings();
                    break;
                    
                case "funcs":
                    listUserFunctions();
                    break;
                    
                case "env":
                    showEnvironment();
                    break;
                    
                case "add":
                    if (arg.isEmpty()) {
                        System.out.println("用法: add <函数名>");
                    } else {
                        addUserFunction(arg.trim());
                    }
                    break;
                    
                case "edit":
                    if (arg.isEmpty()) {
                        System.out.println("用法: edit <变量名>");
                    } else {
                        editEnvironment(arg.trim());
                    }
                    break;
                    
                case "del":
                    if (arg.isEmpty()) {
                        System.out.println("用法: del <函数名>");
                    } else {
                        delUserFunction(arg.trim());
                    }
                    break;
                    
                case "analyze":
                    analyzeAll();
                    break;
                    
                case "export":
                    if (arg.isEmpty()) {
                        System.out.println("用法: export <输出文件>");
                    } else {
                        exportAnalysis(arg, filePath);
                    }
                    break;
                    
                default:
                    if (!input.isEmpty()) {
                        System.out.println("未知命令: " + cmd);
                    }
                    break;
            }
        }
    }
    
    /**
     * 列出所有函数
     */
    private static void listFunctions() {
        System.out.println("\n========== 函数列表 ==========");
        System.out.printf("%-4s %-4s %-30s %s\n", "ID", "深度", "名称", "指令数/常量数");
        System.out.println("-".repeat(60));
        
        for (PrototypeInfo info : functionList) {
            String indent = "  ".repeat(info.depth);
            int codeLen = info.prototype.code != null ? info.prototype.code.length : 0;
            int kLen = info.prototype.k != null ? info.prototype.k.length : 0;
            System.out.printf("%-4d %-4d %s%s (%d/%d)\n", 
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
            for (int i = 0; i < Math.min(p.k.length, 30); i++) {
                String val = p.k[i].tojstring();
                if (val.length() > 80) val = val.substring(0, 80) + "...";
                System.out.println("  [" + i + "] (" + getTypeName(p.k[i]) + ") " + val);
            }
            if (p.k.length > 30) {
                System.out.println("  ... 还有 " + (p.k.length - 30) + " 个常量");
            }
        }
    }
    
    /**
     * 伪执行指定函数
     */
    private static void runFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }
        
        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 伪执行函数 #" + id + ": " + info.name + " ==========");
        
        StringInterceptor.enableMemory();
        clearAnalysisData();
        
        try {
            detailedPseudoExecute(info.prototype, info.name);
        } catch (Exception e) {
            System.out.println("执行出错: " + e.getMessage());
        }
        
        System.out.println("\n执行完成！");
        showStrings();
    }
    
    /**
     * 伪执行所有函数
     */
    private static void runAllFunctions() {
        System.out.println("\n========== 伪执行所有函数 ==========");
        
        StringInterceptor.enableMemory();
        clearAnalysisData();
        
        for (PrototypeInfo info : functionList) {
            System.out.println("\n--- 执行: " + info.name + " ---");
            try {
                detailedPseudoExecute(info.prototype, info.name);
            } catch (Exception e) {
                System.out.println("执行出错: " + e.getMessage());
            }
        }
        
        System.out.println("\n全部执行完成！");
        showStrings();
    }
    
    /**
     * 设置执行环境
     */
    private static void setupExecGlobals() {
        // 设置字符串拦截
        StringInterceptor.enableMemory();
        
        // 设置容错环境（缺省返回 DummyLuaValue）
        LuaTable metatable = new LuaTable();
        metatable.set(LuaValue.INDEX, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (execGlobals.get("TOLERANT_MODE").toboolean()) {
                    LuaValue key = args.arg(2);
                    String keyStr = key.tojstring();
                    System.out.println("[TolerantVM] 自动补全全局变量: " + keyStr);
                    DummyLuaValue dummy = new DummyLuaValue(keyStr);
                    execGlobals.rawset(key, dummy);
                    return dummy;
                }
                return LuaValue.NIL;
            }
        });
        execGlobals.setmetatable(metatable);

        // 拦截string.char来捕获解密后的字符串
        LuaTable stringLib = (LuaTable) execGlobals.get("string");
        stringLib.set("char", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= args.narg(); i++) {
                    int c = args.checkint(i);
                    sb.append((char) c);
                }
                String result = sb.toString();
                StringInterceptor.onStringCreate(result.getBytes(), 0, result.length());
                return LuaValue.valueOf(result);
            }
        });
        
        // 拦截table.concat
        LuaTable tableLib = (LuaTable) execGlobals.get("table");
        tableLib.set("concat", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaTable t = args.checktable(1);
                String sep = args.isnoneornil(2) ? "" : args.checkjstring(2);
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= t.length(); i++) {
                    if (i > 1) sb.append(sep);
                    sb.append(t.get(i).tojstring());
                }
                String result = sb.toString();
                StringInterceptor.onStringCreate(result.getBytes(), 0, result.length());
                return LuaValue.valueOf(result);
            }
        });
    }
    
    /**
     * 真执行指定函数
     */
    private static void execFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }
        
        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 真执行函数 #" + id + ": " + info.name + " (容错模式) ==========");
        
        inRealExecution = true;
        StringInterceptor.enableMemory();
        
        // 启用容错模式
        execGlobals.set("TOLERANT_MODE", LuaValue.TRUE);

        boolean success = tryExecute(info.prototype, 3);
        
        if (success) {
            System.out.println("\n执行完成！");
        } else {
            System.out.println("\n执行失败！");
        }
        
        showStrings();
    }
    
    /**
     * 尝试执行，遇到错误自动处理并重试
     */
    private static boolean tryExecute(Prototype p, int maxRetries) {
        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                LuaClosure closure = new LuaClosure(p, execGlobals);
                closure.call();
                return true;
            } catch (LuaError e) {
                if (execGlobals.get("TOLERANT_MODE").toboolean()) {
                    System.out.println("执行结束 (容错模式下遇到不可恢复错误或结束): " + e.getMessage());
                    return true;
                }
                String msg = e.getMessage();
                if (msg == null) {
                    System.out.println("执行出错: (未知错误)");
                    e.printStackTrace();
                    return false;
                }
                
                System.out.println("\n========== 错误详情 ==========");
                System.out.println("错误信息: " + msg);
                
                String missingName = extractMissingName(msg);
                
                if (missingName != null) {
                    System.out.println("缺失变量/方法: " + missingName);
                    
                    if (userFunctions.containsKey(missingName)) {
                        System.out.println("注意: '" + missingName + "' 已存在于环境中，跳过重复创建");
                        System.out.println("可能原因: 方法调用链中某个中间结果为nil");
                        System.out.print("是否继续尝试? [y/n]: ");
                        String choice = scanner.nextLine().trim().toLowerCase();
                        if (choice.equals("y") || choice.equals("yes")) {
                            continue;
                        }
                        return false;
                    }
                    
                    if (!handleMissingValue(missingName, msg)) {
                        return false;
                    }
                } else {
                    System.out.println("无法自动识别缺失的变量，请手动处理");
                    System.out.print("是否继续尝试? [y/n]: ");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (!choice.equals("y") && !choice.equals("yes")) {
                        return false;
                    }
                }
            } catch (Exception e) {
                System.out.println("\n========== 异常详情 ==========");
                System.out.println("异常类型: " + e.getClass().getName());
                System.out.println("异常信息: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("\n执行失败原因: 达到最大重试次数 (" + maxRetries + ")");
        return false;
    }
    
    /**
     * 从错误消息中提取缺失的名称
     */
    private static String extractMissingName(String msg) {
        if (msg.contains("attempt to call")) {
            int idx = msg.indexOf("global '");
            if (idx > 0) {
                int start = idx + 8;
                int end = msg.indexOf("'", start);
                if (end > start) return msg.substring(start, end);
            }
            
            idx = msg.indexOf("field '");
            if (idx > 0) {
                int start = idx + 7;
                int end = msg.indexOf("'", start);
                if (end > start) {
                    String fieldName = msg.substring(start, end);
                    return tryFindTableForField(fieldName, msg);
                }
            }
            
            idx = msg.indexOf("method '");
            if (idx > 0) {
                int start = idx + 8;
                int end = msg.indexOf("'", start);
                if (end > start) {
                    String methodName = msg.substring(start, end);
                    return tryFindTableForMethod(methodName, msg);
                }
            }
        }
        
        if (msg.contains("attempt to index")) {
            int idx = msg.indexOf("with key '");
            if (idx > 0) {
                int start = idx + 10;
                int end = msg.indexOf("'", start);
                if (end > start) {
                    String key = msg.substring(start, end);
                    System.out.print("\n请输入缺失的变量名 (如 gg): ");
                    String varName = scanner.nextLine().trim();
                    if (!varName.isEmpty()) {
                        return varName;
                    }
                    return key;
                }
            }
        }
        
        if (msg.contains("attempt to perform arithmetic") || msg.contains("attempt to compare")) {
            System.out.print("\n请输入缺失的变量名: ");
            String varName = scanner.nextLine().trim();
            if (!varName.isEmpty()) {
                return varName;
            }
        }
        
        return null;
    }
    
    /**
     * 尝试找到字段所属的表
     */
    private static String tryFindTableForField(String fieldName, String errorMsg) {
        for (String tableName : userFunctions.keySet()) {
            LuaValue value = userFunctions.get(tableName);
            if (value.istable()) {
                System.out.println("\n检测到可能的表: " + tableName);
                System.out.print("是否在 " + tableName + " 中添加方法 " + fieldName + "? [y/n]: ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("y") || choice.equals("yes")) {
                    addMethodToTable(tableName, fieldName);
                    return null;
                }
            }
        }
        
        System.out.print("\n请输入要添加方法的表名 (或输入新表名): ");
        String tableName = scanner.nextLine().trim();
        if (!tableName.isEmpty()) {
            if (!userFunctions.containsKey(tableName)) {
                LuaTable newTable = createSmartTable(tableName);
                execGlobals.set(tableName, newTable);
                userFunctions.put(tableName, newTable);
                tableMethods.put(tableName, new HashMap<>());
                System.out.println("已创建新表: " + tableName);
            }
            addMethodToTable(tableName, fieldName);
            return null;
        }
        
        return fieldName;
    }
    
    /**
     * 尝试找到方法所属的表
     */
    private static String tryFindTableForMethod(String methodName, String errorMsg) {
        return tryFindTableForField(methodName, errorMsg);
    }
    
    /**
     * 向表添加方法
     */
    private static void addMethodToTable(String tableName, String methodName) {
        LuaValue tableValue = userFunctions.get(tableName);
        if (tableValue == null || !tableValue.istable()) {
            System.out.println("表不存在或不是表类型");
            return;
        }
        
        LuaTable table = (LuaTable) tableValue;
        
        System.out.println("\n添加方法: " + tableName + "." + methodName);
        System.out.println("选择方法类型:");
        System.out.println("  1. 自动返回表本身 (支持链式调用)");
        System.out.println("  2. 返回nil");
        System.out.println("  3. 自定义Lua代码");
        System.out.print("选择 [1-3]: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                LuaValue autoReturnFunc = createAutoReturnFunction(table, tableName, methodName);
                table.set(methodName, autoReturnFunc);
                tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, autoReturnFunc);
                System.out.println("已添加方法: " + methodName + " (自动返回表本身)");
                break;
                
            case "2":
                LuaValue nilFunc = new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        return LuaValue.NIL;
                    }
                };
                table.set(methodName, nilFunc);
                tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, nilFunc);
                System.out.println("已添加方法: " + methodName + " (返回nil)");
                break;
                
            case "3":
                System.out.println("输入方法实现 (Lua代码，输入空行结束):");
                System.out.println("提示: 使用 '...' 接收参数，使用 'return' 返回值");
                StringBuilder code = new StringBuilder();
                code.append("function ").append(tableName).append(".").append(methodName).append("(...)\n");
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    code.append("  ").append(line).append("\n");
                }
                code.append("end");
                
                try {
                    execGlobals.load(code.toString()).call();
                    LuaValue newMethod = table.get(methodName);
                    tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, newMethod);
                    System.out.println("已添加方法: " + methodName);
                } catch (Exception e) {
                    System.out.println("添加失败: " + e.getMessage());
                    LuaValue fallback = createAutoReturnFunction(table, tableName, methodName);
                    table.set(methodName, fallback);
                    tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, fallback);
                }
                break;
                
            default:
                LuaValue defaultFunc = createAutoReturnFunction(table, tableName, methodName);
                table.set(methodName, defaultFunc);
                tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, defaultFunc);
                System.out.println("已添加方法: " + methodName + " (自动返回表本身)");
                break;
        }
    }
    
    /**
     * 处理缺失的值/函数
     */
    private static boolean handleMissingValue(String name, String errorMsg) {
        System.out.println("\n请选择如何定义 '" + name + "':");
        System.out.println("  1. 智能表 {} (自动补全缺失方法，推荐)");
        System.out.println("  2. 空表 {} (仅创建空表)");
        System.out.println("  3. 空函数 function() end");
        System.out.println("  4. 返回nil");
        System.out.println("  5. 返回自定义值");
        System.out.println("  6. 输入Lua代码定义");
        System.out.println("  7. 跳过 (停止执行)");
        System.out.print("选择 [1-7]: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                LuaTable smartTable = createSmartTable(name);
                execGlobals.set(name, smartTable);
                userFunctions.put(name, smartTable);
                tableMethods.put(name, new HashMap<>());
                System.out.println("已添加: " + name + " = {} (智能表，支持动态方法补全)");
                return true;
                
            case "2":
                LuaTable table = new LuaTable();
                execGlobals.set(name, table);
                userFunctions.put(name, table);
                System.out.println("已添加: " + name + " = {}");
                return true;
                
            case "3":
                LuaValue emptyFunc = new VarArgFunction() {
                    public Varargs invoke(Varargs args) { return LuaValue.NIL; }
                };
                execGlobals.set(name, emptyFunc);
                userFunctions.put(name, emptyFunc);
                System.out.println("已添加: " + name + " = function() end");
                return true;
                
            case "4":
                execGlobals.set(name, LuaValue.NIL);
                System.out.println("已设置: " + name + " = nil");
                return true;
                
            case "5":
                System.out.print("输入返回值 (字符串/数字): ");
                String val = scanner.nextLine().trim();
                LuaValue lv;
                try {
                    if (val.contains(".")) {
                        lv = LuaValue.valueOf(Double.parseDouble(val));
                    } else {
                        lv = LuaValue.valueOf(Long.parseLong(val));
                    }
                } catch (NumberFormatException e) {
                    lv = LuaValue.valueOf(val);
                }
                execGlobals.set(name, lv);
                userFunctions.put(name, lv);
                System.out.println("已添加: " + name + " = " + val);
                return true;
                
            case "6":
                System.out.println("输入Lua代码 (输入空行结束):");
                StringBuilder code = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    code.append(line).append("\n");
                }
                try {
                    execGlobals.load(code.toString()).call();
                    userFunctions.put(name, execGlobals.get(name));
                    System.out.println("已执行代码");
                    return true;
                } catch (Exception e) {
                    System.out.println("执行失败: " + e.getMessage());
                    LuaTable defaultTable = createSmartTable(name);
                    execGlobals.set(name, defaultTable);
                    userFunctions.put(name, defaultTable);
                    return true;
                }
                
            case "7":
                System.out.println("跳过");
                return false;
                
            default:
                LuaTable defaultSmartTable = createSmartTable(name);
                execGlobals.set(name, defaultSmartTable);
                userFunctions.put(name, defaultSmartTable);
                tableMethods.put(name, new HashMap<>());
                System.out.println("已添加: " + name + " = {} (智能表)");
                return true;
        }
    }
    
    /**
     * 创建智能表，支持动态方法补全（使用元表__index）
     */
    private static LuaTable createSmartTable(String tableName) {
        LuaTable table = new LuaTable();
        
        LuaTable metatable = new LuaTable();
        
        metatable.set(LuaValue.INDEX, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaTable self = (LuaTable) args.arg(1);
                LuaValue key = args.arg(2);
                String keyStr = key.tojstring();
                
                LuaValue existingValue = self.rawget(key);
                if (!existingValue.isnil()) {
                    return existingValue;
                }
                
                if (key.isnumber()) {
                    System.out.println("\n[调试] 访问 " + tableName + "[" + key.toint() + "] 返回 nil");
                    return LuaValue.NIL;
                }
                
                if (key.isstring()) {
                    String methodName = key.tojstring();
                    System.out.println("\n[调试] 动态创建方法: " + tableName + "." + methodName + "()");
                    
                    LuaValue newMethod = createAutoReturnFunction(self, tableName, methodName);
                    self.rawset(key, newMethod);
                    
                    tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, newMethod);
                    
                    return newMethod;
                }
                
                return LuaValue.NIL;
            }
        });
        
        table.setmetatable(metatable);
        
        return table;
    }
    
    /**
     * 创建自动返回自身的函数（支持链式调用）
     */
    private static LuaValue createAutoReturnFunction(LuaTable table, String tableName, String methodName) {
        VarArgFunction func = new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                StringBuilder argStr = new StringBuilder();
                for (int i = 1; i <= args.narg(); i++) {
                    if (i > 1) argStr.append(", ");
                    argStr.append(args.arg(i).tojstring());
                }
                
                String key = tableName + "." + methodName;
                tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, this);
                
                return table;
            }
        };
        return func;
    }
    
    /**
     * 真执行所有函数
     */
    private static void execAllFunctions() {
        System.out.println("\n========== 真执行所有函数 (容错模式) ==========");
        
        StringInterceptor.enableMemory();
        inRealExecution = true;
        
        // 启用容错模式
        execGlobals.set("TOLERANT_MODE", LuaValue.TRUE);

        for (PrototypeInfo info : functionList) {
            System.out.println("\n--- 执行: " + info.name + " ---");
            try {
                LuaClosure closure = new LuaClosure(info.prototype, execGlobals);
                closure.call();
            } catch (LuaError e) {
                String msg = e.getMessage();
                if (msg != null && msg.contains("attempt to call")) {
                    String funcName = extractMissingFunction(msg);
                    if (funcName != null) {
                        System.out.println("!!! 缺失函数: " + funcName);
                        handleMissingFunction(funcName);
                        // 重试
                        try {
                            LuaClosure closure2 = new LuaClosure(info.prototype, execGlobals);
                            closure2.call();
                        } catch (Exception e2) {
                            System.out.println("执行失败: " + e2.getMessage());
                        }
                    }
                } else {
                    System.out.println("执行出错: " + msg);
                }
            } catch (Exception e) {
                System.out.println("执行出错: " + e.getMessage());
            }
        }
        
        inRealExecution = false;
        System.out.println("\n全部执行完成！");
        showStrings();
    }
    
    /**
     * 从错误消息中提取缺失的函数名
     */
    private static String extractMissingFunction(String errorMsg) {
        // 尝试匹配 "attempt to call a nil value (global 'xxx')"
        int idx = errorMsg.indexOf("'");
        if (idx > 0) {
            int endIdx = errorMsg.indexOf("'", idx + 1);
            if (endIdx > idx) {
                return errorMsg.substring(idx + 1, endIdx);
            }
        }
        // 尝试匹配 "attempt to call field 'xxx'"
        idx = errorMsg.indexOf("field '");
        if (idx > 0) {
            int start = idx + 7;
            int end = errorMsg.indexOf("'", start);
            if (end > start) {
                return errorMsg.substring(start, end);
            }
        }
        return null;
    }
    
    /**
     * 处理缺失函数
     */
    private static void handleMissingFunction(String funcName) {
        System.out.println("\n函数 '" + funcName + "' 不存在！");
        System.out.println("请选择操作:");
        System.out.println("  1. 返回nil (默认)");
        System.out.println("  2. 返回空表 {}");
        System.out.println("  3. 返回空函数 function() end");
        System.out.println("  4. 返回自定义值");
        System.out.println("  5. 输入Lua代码定义函数");
        System.out.println("  6. 跳过");
        System.out.print("选择 [1-6]: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "2":
                execGlobals.set(funcName, new LuaTable());
                System.out.println("已添加: " + funcName + " = {}");
                break;
            case "3":
                execGlobals.set(funcName, new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        return LuaValue.NIL;
                    }
                });
                System.out.println("已添加: " + funcName + " = function() end");
                break;
            case "4":
                System.out.print("输入返回值 (字符串): ");
                String val = scanner.nextLine();
                execGlobals.set(funcName, LuaValue.valueOf(val));
                System.out.println("已添加: " + funcName + " = \"" + val + "\"");
                break;
            case "5":
                System.out.println("输入Lua函数定义 (输入空行结束):");
                StringBuilder code = new StringBuilder();
                code.append("function ").append(funcName).append("(...)\n");
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    code.append("  ").append(line).append("\n");
                }
                code.append("end");
                try {
                    LuaValue func = execGlobals.load(code.toString());
                    func.call();
                    System.out.println("已定义函数: " + funcName);
                } catch (Exception e) {
                    System.out.println("定义失败: " + e.getMessage());
                    execGlobals.set(funcName, LuaValue.NIL);
                }
                break;
            case "6":
                System.out.println("跳过");
                break;
            default:
                execGlobals.set(funcName, LuaValue.NIL);
                System.out.println("已添加: " + funcName + " = nil");
                break;
        }
    }
    
    /**
     * 列出用户函数
     */
    private static void listUserFunctions() {
        System.out.println("\n========== 已添加的用户函数 ==========");
        if (userFunctions.isEmpty()) {
            System.out.println("(无)");
        } else {
            for (String name : userFunctions.keySet()) {
                System.out.println("  " + name);
            }
        }
    }
    
    /**
     * 添加用户函数
     */
    private static void addUserFunction(String name) {
        System.out.println("添加函数: " + name);
        System.out.println("选择类型:");
        System.out.println("  1. 返回nil的空函数");
        System.out.println("  2. 返回空表");
        System.out.println("  3. 自定义Lua代码");
        System.out.print("选择 [1-3]: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                LuaValue emptyFunc = new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        return LuaValue.NIL;
                    }
                };
                execGlobals.set(name, emptyFunc);
                userFunctions.put(name, emptyFunc);
                System.out.println("已添加: " + name + " = function() return nil end");
                break;
            case "2":
                LuaTable table = new LuaTable();
                execGlobals.set(name, table);
                userFunctions.put(name, table);
                System.out.println("已添加: " + name + " = {}");
                break;
            case "3":
                System.out.println("输入Lua代码 (输入空行结束):");
                StringBuilder code = new StringBuilder();
                code.append(name).append(" = function(...)\n");
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    code.append("  ").append(line).append("\n");
                }
                code.append("end");
                try {
                    execGlobals.load(code.toString()).call();
                    userFunctions.put(name, execGlobals.get(name));
                    System.out.println("已添加: " + name);
                } catch (Exception e) {
                    System.out.println("添加失败: " + e.getMessage());
                }
                break;
            default:
                System.out.println("已取消");
                break;
        }
    }
    
    /**
     * 删除用户函数
     */
    private static void delUserFunction(String name) {
        if (userFunctions.containsKey(name)) {
            userFunctions.remove(name);
            tableMethods.remove(name);
            execGlobals.set(name, LuaValue.NIL);
            System.out.println("已删除: " + name);
        } else {
            System.out.println("函数不存在: " + name);
        }
    }
    
    /**
     * 显示环境变量和已补全的表/方法
     */
    private static void showEnvironment() {
        System.out.println("\n========== 环境变量 ==========");
        
        if (userFunctions.isEmpty()) {
            System.out.println("(无自定义变量)");
        } else {
            for (Map.Entry<String, LuaValue> entry : userFunctions.entrySet()) {
                String name = entry.getKey();
                LuaValue value = entry.getValue();
                
                System.out.println("\n【" + name + "】");
                
                if (value.istable()) {
                    System.out.println("  类型: 表 (table)");
                    LuaTable table = (LuaTable) value;
                    
                    if (tableMethods.containsKey(name)) {
                        System.out.println("  已补全的方法:");
                        Map<String, LuaValue> methods = tableMethods.get(name);
                        for (String methodName : methods.keySet()) {
                            System.out.println("    - " + methodName + "()");
                        }
                    }
                    
                    System.out.println("  表字段:");
                    LuaValue k = LuaValue.NIL;
                    int count = 0;
                    while (true) {
                        Varargs n = table.next(k);
                        if ((k = n.arg1()).isnil()) break;
                        LuaValue v = n.arg(2);
                        System.out.println("    " + k.tojstring() + " = " + truncateStr(v.tojstring()));
                        count++;
                        if (count > 20) {
                            System.out.println("    ... (还有更多字段)");
                            break;
                        }
                    }
                    if (count == 0) {
                        System.out.println("    (空表)");
                    }
                } else if (value.isfunction()) {
                    System.out.println("  类型: 函数 (function)");
                } else {
                    System.out.println("  类型: " + getTypeName(value));
                    System.out.println("  值: " + truncateStr(value.tojstring()));
                }
            }
        }
        
        System.out.println("\n========== 补全统计 ==========");
        System.out.println("自定义变量数量: " + userFunctions.size());
        int totalMethods = 0;
        for (Map<String, LuaValue> methods : tableMethods.values()) {
            totalMethods += methods.size();
        }
        System.out.println("已补全方法数量: " + totalMethods);
    }
    
    /**
     * 编辑环境变量或函数
     */
    private static void editEnvironment(String name) {
        if (!userFunctions.containsKey(name)) {
            System.out.println("变量 '" + name + "' 不存在");
            System.out.print("是否创建新变量? [y/n]: ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                addUserFunction(name);
            }
            return;
        }
        
        LuaValue currentValue = userFunctions.get(name);
        
        System.out.println("\n========== 编辑: " + name + " ==========");
        System.out.println("当前类型: " + getTypeName(currentValue));
        System.out.println("当前值: " + truncateStr(currentValue.tojstring()));
        
        System.out.println("\n选择操作:");
        System.out.println("  1. 替换为新值");
        System.out.println("  2. 修改表方法 (当前是表)");
        System.out.println("  3. 添加新方法 (当前是表)");
        System.out.println("  4. 删除方法 (当前是表)");
        System.out.println("  5. 输入Lua代码修改");
        System.out.println("  6. 删除此变量");
        System.out.println("  7. 取消");
        System.out.print("选择 [1-7]: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                System.out.println("输入新值 (支持Lua代码):");
                String newVal = scanner.nextLine().trim();
                try {
                    LuaValue loaded = execGlobals.load("return " + newVal).call();
                    execGlobals.set(name, loaded);
                    userFunctions.put(name, loaded);
                    System.out.println("已更新: " + name + " = " + truncateStr(loaded.tojstring()));
                } catch (Exception e) {
                    System.out.println("执行失败: " + e.getMessage());
                }
                break;
                
            case "2":
                if (currentValue.istable()) {
                    editTableMethod(name, (LuaTable) currentValue);
                } else {
                    System.out.println("当前变量不是表");
                }
                break;
                
            case "3":
                if (currentValue.istable()) {
                    addTableMethod(name, (LuaTable) currentValue);
                } else {
                    System.out.println("当前变量不是表");
                }
                break;
                
            case "4":
                if (currentValue.istable()) {
                    deleteTableMethod(name, (LuaTable) currentValue);
                } else {
                    System.out.println("当前变量不是表");
                }
                break;
                
            case "5":
                System.out.println("输入Lua代码 (输入空行结束):");
                StringBuilder code = new StringBuilder();
                code.append(name).append(" = ");
                String line;
                while (!(line = scanner.nextLine()).isEmpty()) {
                    code.append(line).append("\n");
                }
                try {
                    execGlobals.load(code.toString()).call();
                    userFunctions.put(name, execGlobals.get(name));
                    System.out.println("已更新: " + name);
                } catch (Exception e) {
                    System.out.println("执行失败: " + e.getMessage());
                }
                break;
                
            case "6":
                delUserFunction(name);
                break;
                
            case "7":
                System.out.println("已取消");
                break;
                
            default:
                System.out.println("无效选择");
                break;
        }
    }
    
    /**
     * 编辑表方法
     */
    private static void editTableMethod(String tableName, LuaTable table) {
        Map<String, LuaValue> methods = tableMethods.getOrDefault(tableName, new HashMap<>());
        
        if (methods.isEmpty()) {
            System.out.println("该表没有已补全的方法");
            return;
        }
        
        System.out.println("\n已补全的方法:");
        int idx = 1;
        Map<Integer, String> methodIndex = new HashMap<>();
        for (String methodName : methods.keySet()) {
            System.out.println("  " + idx + ". " + methodName + "()");
            methodIndex.put(idx, methodName);
            idx++;
        }
        
        System.out.print("\n选择要编辑的方法编号: ");
        try {
            int selected = Integer.parseInt(scanner.nextLine().trim());
            String methodName = methodIndex.get(selected);
            if (methodName == null) {
                System.out.println("无效选择");
                return;
            }
            
            System.out.println("\n编辑方法: " + methodName);
            System.out.println("输入新的方法实现 (Lua代码，输入空行结束):");
            System.out.println("提示: 使用 '...' 接收参数，使用 'return' 返回值");
            
            StringBuilder code = new StringBuilder();
            code.append("function ").append(tableName).append(".").append(methodName).append("(...)\n");
            String line;
            while (!(line = scanner.nextLine()).isEmpty()) {
                code.append("  ").append(line).append("\n");
            }
            code.append("end");
            
            try {
                execGlobals.load(code.toString()).call();
                LuaValue newMethod = table.get(methodName);
                methods.put(methodName, newMethod);
                System.out.println("已更新方法: " + methodName);
            } catch (Exception e) {
                System.out.println("更新失败: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("请输入数字");
        }
    }
    
    /**
     * 添加表方法
     */
    private static void addTableMethod(String tableName, LuaTable table) {
        System.out.print("\n输入新方法名称: ");
        String methodName = scanner.nextLine().trim();
        
        if (methodName.isEmpty()) {
            System.out.println("方法名不能为空");
            return;
        }
        
        System.out.println("输入方法实现 (Lua代码，输入空行结束):");
        System.out.println("提示: 使用 '...' 接收参数，使用 'return' 返回值");
        System.out.println("提示: 使用 'self' 或 '" + tableName + "' 引用表本身");
        
        StringBuilder code = new StringBuilder();
        code.append("function ").append(tableName).append(".").append(methodName).append("(...)\n");
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            code.append("  ").append(line).append("\n");
        }
        code.append("end");
        
        try {
            execGlobals.load(code.toString()).call();
            LuaValue newMethod = table.get(methodName);
            tableMethods.computeIfAbsent(tableName, k -> new HashMap<>()).put(methodName, newMethod);
            System.out.println("已添加方法: " + methodName);
        } catch (Exception e) {
            System.out.println("添加失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除表方法
     */
    private static void deleteTableMethod(String tableName, LuaTable table) {
        Map<String, LuaValue> methods = tableMethods.getOrDefault(tableName, new HashMap<>());
        
        if (methods.isEmpty()) {
            System.out.println("该表没有已补全的方法");
            return;
        }
        
        System.out.println("\n已补全的方法:");
        int idx = 1;
        Map<Integer, String> methodIndex = new HashMap<>();
        for (String methodName : methods.keySet()) {
            System.out.println("  " + idx + ". " + methodName + "()");
            methodIndex.put(idx, methodName);
            idx++;
        }
        
        System.out.print("\n选择要删除的方法编号: ");
        try {
            int selected = Integer.parseInt(scanner.nextLine().trim());
            String methodName = methodIndex.get(selected);
            if (methodName == null) {
                System.out.println("无效选择");
                return;
            }
            
            table.set(methodName, LuaValue.NIL);
            methods.remove(methodName);
            System.out.println("已删除方法: " + methodName);
            
        } catch (NumberFormatException e) {
            System.out.println("请输入数字");
        }
    }
    
    /**
     * 反汇编指定函数
     */
    private static void disasmFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }
        
        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 反汇编函数 #" + id + ": " + info.name + " ==========");
        disassembleToConsole(info.prototype, info.name);
    }
    
    /**
     * 还原伪代码
     */
    private static void decompileFunction(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }

        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 还原伪代码 #" + id + ": " + info.name + " ==========");
        System.out.println(PseudoCompiler.decompile(info.prototype));
    }

    /**
     * 显示控制流图
     */
    private static void showCFG(int id) {
        if (id < 0 || id >= functionList.size()) {
            System.out.println("无效的函数ID");
            return;
        }

        PrototypeInfo info = functionList.get(id);
        System.out.println("\n========== 函数 #" + id + ": " + info.name + " 控制流图 ==========");

        List<CFGBuilder.BasicBlock> blocks = CFGBuilder.buildCFG(info.prototype);
        if (blocks.isEmpty()) {
            System.out.println("无控制流信息 (空函数或无指令)。");
            return;
        }

        for (CFGBuilder.BasicBlock b : blocks) {
            System.out.println("Block #" + b.id + " [PC: " + b.startPc + " - " + b.endPc + "]");

            System.out.print("  InEdges:  ");
            if (b.inEdges.isEmpty()) System.out.print("(none)");
            for (CFGBuilder.BasicBlock in : b.inEdges) System.out.print("Block#" + in.id + " ");
            System.out.println();

            System.out.print("  OutEdges: ");
            if (b.outEdges.isEmpty()) System.out.print("(none)");
            for (CFGBuilder.BasicBlock out : b.outEdges) System.out.print("Block#" + out.id + " ");
            System.out.println();
            System.out.println();
        }
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
     * 完整分析
     */
    private static void analyzeAll() {
        System.out.println("\n========== 完整分析 ==========");
        
        StringInterceptor.enableMemory();
        clearAnalysisData();
        
        System.out.println("\n--- 反汇编主函数 ---");
        disassembleToConsole(mainPrototype, "main");
        
        System.out.println("\n--- 伪执行所有函数 ---");
        for (PrototypeInfo info : functionList) {
            try {
                detailedPseudoExecute(info.prototype, info.name);
            } catch (Exception e) {
                // 忽略错误
            }
        }
        
        showStrings();
    }
    
    /**
     * 导出分析到文件
     */
    private static void exportAnalysis(String outputPath, String filePath) {
        System.out.println("导出分析到: " + outputPath);
        
        try {
            writer = new PrintWriter(new FileWriter(outputPath), true);
            
            log("========== LuaJ 反编译分析报告 ==========");
            log("源文件: " + filePath);
            log("函数数量: " + functionList.size());
            
            log("\n========== 函数列表 ==========");
            for (PrototypeInfo info : functionList) {
                int codeLen = info.prototype.code != null ? info.prototype.code.length : 0;
                int kLen = info.prototype.k != null ? info.prototype.k.length : 0;
                String indent = "  ".repeat(info.depth);
                log(String.format("#%d %s%s (%d指令, %d常量)", info.id, indent, info.name, codeLen, kLen));
            }
            
            StringInterceptor.enableMemory();
            clearAnalysisData();
            
            log("\n========== 反汇编输出 ==========");
            disassembleToFile(mainPrototype, "main");
            
            log("\n========== 伪执行分析 ==========");
            for (PrototypeInfo info : functionList) {
                log("\n--- 函数: " + info.name + " ---");
                detailedPseudoExecute(info.prototype, info.name);
            }
            
            log("\n========== 捕获的字符串 ==========");
            var chineseStrings = StringInterceptor.getChineseStrings();
            var allStrings = StringInterceptor.getAllStrings();
            
            if (!chineseStrings.isEmpty()) {
                log("\n【中文字符串】(" + chineseStrings.size() + " 个)");
                for (String s : chineseStrings) {
                    log("  " + s);
                }
            }
            
            log("\n【所有有意义字符串】(" + allStrings.size() + " 个)");
            for (String s : allStrings) {
                String prefix = chineseStrings.contains(s) ? "[中文] " : "";
                String truncated = s.length() > 300 ? s.substring(0, 300) + "..." : s;
                log("  " + prefix + truncated);
            }
            
            writer.close();
            writer = null;
            
            System.out.println("导出完成！");
            
        } catch (Exception e) {
            System.err.println("导出失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 清空分析数据
     */
    private static void clearAnalysisData() {
        createdTables.clear();
        createdClosures.clear();
        globalAccesses.clear();
        tableAccesses.clear();
        functionCalls.clear();
        tableContents.clear();
        tableCounter = 0;
        closureCounter = 0;
    }
    
    /**
     * 输出日志（同时打印到控制台和文件）
     */
    private static void log(String msg) {
        System.out.println(msg);
        if (writer != null) {
            writer.println(msg);
            writer.flush();
        }
    }
    
    /**
     * 获取Lua值的类型名称
     */
    private static String getTypeName(LuaValue v) {
        if (v.isnil()) return "nil";
        if (v.isboolean()) return "boolean";
        if (v.isnumber()) return v.isint() ? "int" : "double";
        if (v.isstring()) return "string";
        if (v.istable()) return "table";
        if (v.isfunction()) return "function";
        if (v.isuserdata()) return "userdata";
        if (v.isthread()) return "thread";
        return "unknown";
    }
    
    /**
     * 反汇编到控制台
     */
    private static void disassembleToConsole(Prototype p, String name) {
        if (p == null || p.code == null) {
            System.out.println("No instructions found.");
            return;
        }
        
        System.out.println("; Disassembly of " + name + " (" + p.code.length + " instructions)");
        
        int[] code = p.code;
        LuaValue[] k = p.k;
        
        for (int pc = 0; pc < code.length; pc++) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            String opname = opcode < 50 ? getOpName(opcode) : "OP_" + opcode;
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);
            
            String line = formatInstruction(pc, opname, opcode, a, b, c, bx, sbx, k, code);
            System.out.println(line);
        }
    }
    
    /**
     * 格式化单条指令
     */
    private static String formatInstruction(int pc, String opname, int opcode, int a, int b, int c, int bx, int sbx, LuaValue[] k, int[] code) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%06d] %-12s ", pc, opname));

        switch (opcode) {
            case Lua.OP_MOVE:
            case Lua.OP_UNM:
            case Lua.OP_NOT:
            case Lua.OP_LEN:
            case Lua.OP_RETURN:
            case Lua.OP_GETUPVAL:
            case Lua.OP_SETUPVAL:
                sb.append(String.format("R(%d) R(%d)", a, b));
                break;
            case Lua.OP_LOADBOOL:
                sb.append(String.format("R(%d) %s %s", a, b != 0 ? "true" : "false", c != 0 ? "[skip]" : ""));
                break;
            case Lua.OP_LOADK:
                sb.append(String.format("R(%d) K(%d)=%s", a, bx, getConstantStr(k, bx)));
                break;
            case Lua.OP_LOADKX:
                sb.append(String.format("R(%d) [extraarg]", a));
                break;
            case Lua.OP_LOADNIL:
                sb.append(String.format("R(%d)..R(%d) = nil", a, a + b));
                break;
            case Lua.OP_GETTABUP:
                sb.append(String.format("R(%d) U(%d)[%s]", a, b, getRKStr(k, c)));
                break;
            case Lua.OP_GETTABLE:
                sb.append(String.format("R(%d) R(%d)[%s]", a, b, getRKStr(k, c)));
                break;
            case Lua.OP_SETTABUP:
                sb.append(String.format("U(%d)[%s] = %s", a, getRKStr(k, b), getRKStr(k, c)));
                break;
            case Lua.OP_SETTABLE:
                sb.append(String.format("R(%d)[%s] = %s", a, getRKStr(k, b), getRKStr(k, c)));
                break;
            case Lua.OP_NEWTABLE:
                sb.append(String.format("R(%d) array=%d hash=%d", a, b, c));
                break;
            case Lua.OP_SELF:
                sb.append(String.format("R(%d) R(%d)[%s]", a, b, getRKStr(k, c)));
                break;
            case Lua.OP_ADD:
            case Lua.OP_SUB:
            case Lua.OP_MUL:
            case Lua.OP_DIV:
            case Lua.OP_MOD:
            case Lua.OP_POW:
                sb.append(String.format("R(%d) = %s %s %s", a, getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c)));
                break;
            case Lua.OP_CONCAT:
                sb.append(String.format("R(%d) = R(%d)..R(%d)", a, b, c));
                break;
            case Lua.OP_EQ:
            case Lua.OP_LT:
            case Lua.OP_LE:
                sb.append(String.format("if (%s %s %s) then jmp %+d", getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c), sbx));
                break;
            case Lua.OP_TEST:
                sb.append(String.format("if R(%d) %s then jmp %+d", a, c != 0 ? "is falsy" : "is truthy", sbx));
                break;
            case Lua.OP_TESTSET:
                sb.append(String.format("if R(%d) %s then R(%d)=R(%d); jmp %+d", b, c != 0 ? "is falsy" : "is truthy", a, b, sbx));
                break;
            case Lua.OP_JMP:
                sb.append(String.format("jmp %+d (to %d)", sbx, pc + sbx + 1));
                break;
            case Lua.OP_CALL:
                sb.append(String.format("R(%d)(%d args) -> %d results", a, b - 1, c - 1));
                break;
            case Lua.OP_TAILCALL:
                sb.append(String.format("tailcall R(%d)(%d args)", a, b - 1));
                break;
            case Lua.OP_FORLOOP:
                sb.append(String.format("for R(%d) step R(%d) limit R(%d) -> jmp %+d", a, a + 1, a + 2, sbx));
                break;
            case Lua.OP_FORPREP:
                sb.append(String.format("for prep R(%d) -> jmp %+d (to %d)", a, sbx, pc + sbx + 1));
                break;
            case Lua.OP_TFORCALL:
                sb.append(String.format("for iterator call R(%d),R(%d),R(%d) -> %d results", a, a + 1, a + 2, c));
                break;
            case Lua.OP_TFORLOOP:
                sb.append(String.format("for loop R(%d) -> jmp %+d", a, sbx));
                break;
            case Lua.OP_SETLIST:
                sb.append(String.format("R(%d)[%d..] = R(%d)..R(%d)", a, c == 0 ? (pc + 1 < code.length ? code[pc+1] : 0) : c * 50, a + 1, a + b));
                break;
            case Lua.OP_CLOSURE:
                sb.append(String.format("R(%d) = closure(P(%d))", a, bx));
                break;
            case Lua.OP_VARARG:
                sb.append(String.format("R(%d) = vararg(%d)", a, c - 1));
                break;
            case Lua.OP_EXTRAARG:
                sb.append(String.format("extraarg %d", Lua.GETARG_Ax(code[pc])));
                break;
            default:
                sb.append(String.format("A=%d B=%d C=%d", a, b, c));
                break;
        }
        
        return sb.toString();
    }
    
    /**
     * 反汇编并输出到文件
     */
    private static void disassembleToFile(Prototype p, String name) {
        if (p == null || p.code == null) {
            log("No instructions found in prototype.");
            return;
        }

        log("; Disassembly of " + name);
        log("; Instructions: " + p.code.length);
        log("; Source: " + (p.source != null ? p.source.tojstring() : "unknown"));

        int[] code = p.code;
        LuaValue[] k = p.k;
        
        int reportInterval = Math.max(1, code.length / 20);
        
        for (int pc = 0; pc < code.length; pc++) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            
            String opname = (opcode >= 0 && opcode < 50) 
                          ? getOpName(opcode) 
                          : "OP_" + opcode;

            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[%06d] %-12s ", pc, opname));

            switch (opcode) {
                case Lua.OP_MOVE:
                case Lua.OP_UNM:
                case Lua.OP_NOT:
                case Lua.OP_LEN:
                case Lua.OP_RETURN:
                case Lua.OP_GETUPVAL:
                case Lua.OP_SETUPVAL:
                    sb.append(String.format("R(%d) R(%d)", a, b));
                    break;
                case Lua.OP_LOADBOOL:
                    sb.append(String.format("R(%d) %s %s", a, b != 0 ? "true" : "false", c != 0 ? "[skip]" : ""));
                    break;
                case Lua.OP_LOADK:
                    String kval = getConstantStr(k, bx);
                    sb.append(String.format("R(%d) K(%d)=%s", a, bx, kval));
                    break;
                case Lua.OP_LOADKX:
                    sb.append(String.format("R(%d) [extraarg]", a));
                    break;
                case Lua.OP_LOADNIL:
                    sb.append(String.format("R(%d)..R(%d) = nil", a, a + b));
                    break;
                case Lua.OP_GETTABUP:
                    sb.append(String.format("R(%d) U(%d)[%s]", a, b, getRKStr(k, c)));
                    break;
                case Lua.OP_GETTABLE:
                    sb.append(String.format("R(%d) R(%d)[%s]", a, b, getRKStr(k, c)));
                    break;
                case Lua.OP_SETTABUP:
                    sb.append(String.format("U(%d)[%s] = %s", a, getRKStr(k, b), getRKStr(k, c)));
                    break;
                case Lua.OP_SETTABLE:
                    sb.append(String.format("R(%d)[%s] = %s", a, getRKStr(k, b), getRKStr(k, c)));
                    break;
                case Lua.OP_NEWTABLE:
                    sb.append(String.format("R(%d) array=%d hash=%d", a, b, c));
                    break;
                case Lua.OP_SELF:
                    sb.append(String.format("R(%d) R(%d)[%s]", a, b, getRKStr(k, c)));
                    break;
                case Lua.OP_ADD:
                case Lua.OP_SUB:
                case Lua.OP_MUL:
                case Lua.OP_DIV:
                case Lua.OP_MOD:
                case Lua.OP_POW:
                    sb.append(String.format("R(%d) = %s %s %s", a, getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c)));
                    break;
                case Lua.OP_CONCAT:
                    sb.append(String.format("R(%d) = R(%d)..R(%d)", a, b, c));
                    break;
                case Lua.OP_EQ:
                case Lua.OP_LT:
                case Lua.OP_LE:
                    sb.append(String.format("if (%s %s %s) then jmp %+d", getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c), sbx));
                    break;
                case Lua.OP_TEST:
                    sb.append(String.format("if R(%d) %s then jmp %+d", a, c != 0 ? "is falsy" : "is truthy", sbx));
                    break;
                case Lua.OP_TESTSET:
                    sb.append(String.format("if R(%d) %s then R(%d)=R(%d); jmp %+d", b, c != 0 ? "is falsy" : "is truthy", a, b, sbx));
                    break;
                case Lua.OP_JMP:
                    sb.append(String.format("jmp %+d (to %d)", sbx, pc + sbx + 1));
                    break;
                case Lua.OP_CALL:
                    sb.append(String.format("R(%d)(%d args) -> %d results", a, b - 1, c - 1));
                    break;
                case Lua.OP_TAILCALL:
                    sb.append(String.format("tailcall R(%d)(%d args)", a, b - 1));
                    break;
                case Lua.OP_FORLOOP:
                    sb.append(String.format("for R(%d) step R(%d) limit R(%d) -> jmp %+d", a, a + 1, a + 2, sbx));
                    break;
                case Lua.OP_FORPREP:
                    sb.append(String.format("for prep R(%d) -> jmp %+d (to %d)", a, sbx, pc + sbx + 1));
                    break;
                case Lua.OP_TFORCALL:
                    sb.append(String.format("for iterator call R(%d),R(%d),R(%d) -> %d results", a, a + 1, a + 2, c));
                    break;
                case Lua.OP_TFORLOOP:
                    sb.append(String.format("for loop R(%d) -> jmp %+d", a, sbx));
                    break;
                case Lua.OP_SETLIST:
                    sb.append(String.format("R(%d)[%d..] = R(%d)..R(%d)", a, c == 0 ? code[pc+1] : c * 50, a + 1, a + b));
                    break;
                case Lua.OP_CLOSURE:
                    sb.append(String.format("R(%d) = closure(P(%d))", a, bx));
                    break;
                case Lua.OP_VARARG:
                    sb.append(String.format("R(%d) = vararg(%d)", a, c - 1));
                    break;
                case Lua.OP_EXTRAARG:
                    sb.append(String.format("extraarg %d", Lua.GETARG_Ax(i)));
                    break;
                default:
                    sb.append(String.format("A=%d B=%d C=%d", a, b, c));
                    break;
            }
            
            log(sb.toString());
            
            if ((pc + 1) % reportInterval == 0) {
                System.out.println("反汇编进度: " + (pc + 1) + "/" + code.length);
            }
        }
        
        log("; End of " + name);
    }
    
    /**
     * 详细伪执行分析
     */
    private static void detailedPseudoExecute(Prototype p, String name) {
        if (p == null || p.code == null) {
            log("No instructions found in prototype.");
            return;
        }
        
        createdTables.clear();
        createdClosures.clear();
        globalAccesses.clear();
        tableAccesses.clear();
        functionCalls.clear();
        tableContents.clear();
        tableCounter = 0;
        closureCounter = 0;
        
        log("; ========== 详细伪执行分析: " + name + " ==========");
        log("; 指令数量: " + p.code.length);
        
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
        int maxExec = code.length * 10;
        int reportInterval = Math.max(1, code.length / 10);
        
        Set<Integer> visitedPc = new HashSet<>();
        Map<Integer, String> registerTypes = new HashMap<>();
        Map<Integer, List<String>> registerHistory = new HashMap<>();
        
        while (pc < code.length && execCount < maxExec) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            int sbx = Lua.GETARG_sBx(i);
            
            visitedPc.add(pc);
            
            try {
                String result = executeDetailedInstruction(pc, opcode, a, b, c, bx, sbx, code, k, stack, upvalues,
                    registerTypes, registerHistory);
                if (result != null) {
                    log(String.format("[PC:%06d] %s", pc, result));
                }
            } catch (Exception e) {
                log(String.format("[PC:%06d] ERROR: %s", pc, e.getMessage()));
            }
            
            pc++;
            execCount++;
            
            if (execCount % reportInterval == 0) {
                System.out.println("伪执行进度: " + execCount + " 步, PC=" + pc);
            }
        }
        
        log("\n; ========== 执行摘要 ==========");
        log("; 总执行步数: " + execCount);
        log("; 访问的指令: " + visitedPc.size() + "/" + code.length);
        log("; 最终PC: " + pc);
        
        log("\n; ========== 创建的表 (" + createdTables.size() + " 个) ==========");
        for (String t : createdTables) {
            log(";   " + t);
        }
        
        log("\n; ========== 创建的闭包 (" + createdClosures.size() + " 个) ==========");
        for (String c : createdClosures) {
            log(";   " + c);
        }
        
        log("\n; ========== 全局变量访问 (" + globalAccesses.size() + " 次) ==========");
        Set<String> uniqueGlobals = new HashSet<>(globalAccesses);
        for (String g : uniqueGlobals) {
            int count = 0;
            for (String s : globalAccesses) if (s.equals(g)) count++;
            log(";   " + g + " (访问 " + count + " 次)");
        }
        
        log("\n; ========== 表访问记录 (" + tableAccesses.size() + " 次) ==========");
        for (String t : tableAccesses) {
            log(";   " + t);
        }
        
        log("\n; ========== 函数调用记录 (" + functionCalls.size() + " 次) ==========");
        for (String f : functionCalls) {
            log(";   " + f);
        }
        
        log("\n; ========== 表内容详情 ==========");
        for (Map.Entry<String, Set<String>> entry : tableContents.entrySet()) {
            log(";   " + entry.getKey() + ":");
            for (String content : entry.getValue()) {
                log(";     " + content);
            }
        }
        
        log("\n; ========== 寄存器类型分析 ==========");
        Map<String, Integer> typeCount = new HashMap<>();
        for (String type : registerTypes.values()) {
            typeCount.merge(type, 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            log(";   " + entry.getKey() + ": " + entry.getValue() + " 个寄存器");
        }
        
        log("\n; ========== 最终栈状态 ==========");
        int nonNilCount = 0;
        for (int j = 0; j < stack.length; j++) {
            if (!stack[j].isnil()) {
                String val = stack[j].tojstring();
                String type = getTypeName(stack[j]);
                if (val.length() > 100) val = val.substring(0, 100) + "...";
                log(String.format(";   R(%d) = (%s) %s", j, type, val));
                nonNilCount++;
            }
        }
        if (nonNilCount == 0) {
            log(";   (所有寄存器为nil)");
        }
        
        log("\n; ========== 寄存器写入历史 ==========");
        for (Map.Entry<Integer, List<String>> entry : registerHistory.entrySet()) {
            if (entry.getValue().size() > 0) {
                log(";   R(" + entry.getKey() + "):");
                int limit = Math.min(5, entry.getValue().size());
                for (int i = 0; i < limit; i++) {
                    log(";     " + entry.getValue().get(i));
                }
                if (entry.getValue().size() > 5) {
                    log(";     ... 还有 " + (entry.getValue().size() - 5) + " 次写入");
                }
            }
        }
        
        log("\n; ========== 详细伪执行分析结束 ==========");
    }
    
    /**
     * 执行单条指令（详细版）
     */
    private static String executeDetailedInstruction(int pc, int opcode, int a, int b, int c, int bx, int sbx,
                                                     int[] code, LuaValue[] k, LuaValue[] stack, LuaValue[] upvalues,
                                                     Map<Integer, String> registerTypes, Map<Integer, List<String>> registerHistory) {
        String result = null;
        LuaValue newValue = null;
        String typeInfo = null;
        
        switch (opcode) {
            case Lua.OP_MOVE:
                stack[a] = stack[b];
                newValue = stack[a];
                typeInfo = registerTypes.getOrDefault(b, "unknown");
                result = String.format("MOVE: R(%d) <- R(%d) [%s]", a, b, truncateStr(stack[a].tojstring()));
                break;
                
            case Lua.OP_LOADK:
                stack[a] = k[bx];
                newValue = k[bx];
                typeInfo = getTypeName(k[bx]);
                result = String.format("LOADK: R(%d) <- K(%d) (%s) [%s]", a, bx, typeInfo, truncateStr(k[bx].tojstring()));
                break;
                
            case Lua.OP_LOADKX:
                result = String.format("LOADKX: R(%d) <- [extraarg]", a);
                break;
                
            case Lua.OP_LOADBOOL:
                stack[a] = (b != 0) ? LuaValue.TRUE : LuaValue.FALSE;
                newValue = stack[a];
                typeInfo = "boolean";
                result = String.format("LOADBOOL: R(%d) <- %s", a, stack[a].tojstring());
                break;
                
            case Lua.OP_LOADNIL:
                for (int j = a; j <= a + b; j++) {
                    stack[j] = LuaValue.NIL;
                    registerTypes.put(j, "nil");
                }
                result = String.format("LOADNIL: R(%d)..R(%d) <- nil", a, a + b);
                break;
                
            case Lua.OP_GETUPVAL:
                stack[a] = upvalues[b];
                newValue = stack[a];
                typeInfo = "upvalue";
                result = String.format("GETUPVAL: R(%d) <- U(%d)", a, b);
                break;
                
            case Lua.OP_GETTABUP:
                LuaValue key1 = c > 0xff ? k[c & 0xff] : stack[c];
                String keyStr1 = key1.tojstring();
                stack[a] = new DummyLuaValue("U" + b + "." + keyStr1);
                newValue = stack[a];
                typeInfo = "global_ref";
                globalAccesses.add("U" + b + "[" + keyStr1 + "]");
                result = String.format("GETTABUP: R(%d) <- U(%d)[%s] (全局变量访问)", a, b, keyStr1);
                break;
                
            case Lua.OP_GETTABLE:
                LuaValue key2 = c > 0xff ? k[c & 0xff] : stack[c];
                String keyStr2 = key2.tojstring();
                stack[a] = new DummyLuaValue("R" + b + "." + keyStr2);
                newValue = stack[a];
                typeInfo = "table_ref";
                tableAccesses.add("R(" + b + ")[" + keyStr2 + "]");
                result = String.format("GETTABLE: R(%d) <- R(%d)[%s] (表字段读取)", a, b, keyStr2);
                break;
                
            case Lua.OP_SETTABUP:
                LuaValue key3 = b > 0xff ? k[b & 0xff] : stack[b];
                LuaValue val3 = c > 0xff ? k[c & 0xff] : stack[c];
                String keyStr3 = key3.tojstring();
                String valStr3 = truncateStr(val3.tojstring());
                globalAccesses.add("U" + a + "[" + keyStr3 + "] = " + valStr3);
                result = String.format("SETTABUP: U(%d)[%s] <- %s (全局变量写入)", a, keyStr3, valStr3);
                break;
                
            case Lua.OP_SETUPVAL:
                upvalues[b] = stack[a];
                result = String.format("SETUPVAL: U(%d) <- R(%d)", b, a);
                break;
                
            case Lua.OP_SETTABLE:
                LuaValue key4 = b > 0xff ? k[b & 0xff] : stack[b];
                LuaValue val4 = c > 0xff ? k[c & 0xff] : stack[c];
                String keyStr4 = key4.tojstring();
                String valStr4 = truncateStr(val4.tojstring());
                tableAccesses.add("R(" + a + ")[" + keyStr4 + "] = " + valStr4);
                String tableName = "Table_" + a;
                tableContents.computeIfAbsent(tableName, x -> new HashSet<>()).add(keyStr4 + " = " + valStr4);
                result = String.format("SETTABLE: R(%d)[%s] <- %s (表字段写入)", a, keyStr4, valStr4);
                break;
                
            case Lua.OP_NEWTABLE:
                tableCounter++;
                String newTableName = "Table#" + tableCounter;
                stack[a] = new DummyLuaValue(newTableName);
                newValue = stack[a];
                typeInfo = "table";
                createdTables.add(newTableName + " (array=" + b + ", hash=" + c + ") at R(" + a + ")");
                tableContents.put(newTableName, new HashSet<>());
                result = String.format("NEWTABLE: R(%d) <- {} (array=%d, hash=%d) [创建表#%d]", a, b, c, tableCounter);
                break;
                
            case Lua.OP_SELF:
                LuaValue key5 = c > 0xff ? k[c & 0xff] : stack[c];
                stack[a] = new DummyLuaValue("Method_" + key5.tojstring());
                stack[a + 1] = stack[b];
                typeInfo = "method";
                result = String.format("SELF: R(%d)=method(%s), R(%d)=self", a, key5.tojstring(), a + 1);
                break;
                
            case Lua.OP_ADD:
            case Lua.OP_SUB:
            case Lua.OP_MUL:
            case Lua.OP_DIV:
            case Lua.OP_MOD:
            case Lua.OP_POW:
                stack[a] = new DummyLuaValue("MathResult");
                newValue = stack[a];
                typeInfo = "number";
                result = String.format("%s: R(%d) <- %s %s %s", getOpName(opcode), a, 
                    getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c));
                break;
                
            case Lua.OP_UNM:
            case Lua.OP_NOT:
            case Lua.OP_LEN:
                stack[a] = new DummyLuaValue(getOpName(opcode) + "Result");
                newValue = stack[a];
                typeInfo = opcode == Lua.OP_LEN ? "number" : (opcode == Lua.OP_NOT ? "boolean" : "number");
                result = String.format("%s: R(%d) <- op(R(%d))", getOpName(opcode), a, b);
                break;
                
            case Lua.OP_CONCAT:
                StringBuilder concatSb = new StringBuilder();
                for (int j = b; j <= c; j++) {
                    if (stack[j] != null && !stack[j].isnil()) {
                        concatSb.append(stack[j].tojstring());
                    }
                }
                String concatResult = concatSb.toString();
                stack[a] = LuaValue.valueOf(concatResult);
                newValue = stack[a];
                typeInfo = "string";
                StringInterceptor.onStringCreate(concatResult.getBytes(), 0, concatResult.length());
                result = String.format("CONCAT: R(%d) <- R(%d)..R(%d) = \"%s\"", a, b, c, 
                    concatResult.length() > 100 ? concatResult.substring(0, 100) + "..." : concatResult);
                break;
                
            case Lua.OP_JMP:
                result = String.format("JMP: pc %+d -> %d", sbx, pc + sbx + 1);
                break;
                
            case Lua.OP_EQ:
            case Lua.OP_LT:
            case Lua.OP_LE:
                result = String.format("%s: compare %s %s %s", getOpName(opcode), 
                    getRKStr(k, b), getOpSymbol(opcode), getRKStr(k, c));
                break;
                
            case Lua.OP_TEST:
                result = String.format("TEST: if R(%d) %s", a, c != 0 ? "is falsy" : "is truthy");
                break;
                
            case Lua.OP_TESTSET:
                result = String.format("TESTSET: if R(%d) truthy then R(%d)=R(%d)", b, a, b);
                break;
                
            case Lua.OP_CALL:
                String funcName = stack[a].tojstring();
                functionCalls.add("R(" + a + ")(" + (b - 1) + " args) -> " + (c - 1) + " results");
                for (int j = 0; j < c - 1; j++) {
                    stack[a + j] = new DummyLuaValue("Ret" + j);
                    registerTypes.put(a + j, "return_value");
                }
                result = String.format("CALL: R(%d)[%s](%d args) -> %d results [函数调用]", a, funcName, b - 1, c - 1);
                break;
                
            case Lua.OP_TAILCALL:
                functionCalls.add("TAILCALL R(" + a + ")(" + (b - 1) + " args)");
                result = String.format("TAILCALL: R(%d)(%d args) [尾调用]", a, b - 1);
                break;
                
            case Lua.OP_RETURN:
                result = String.format("RETURN: R(%d)..R(%d)", a, a + b - 2);
                break;
                
            case Lua.OP_FORLOOP:
                result = String.format("FORLOOP: R(%d) [循环迭代]", a);
                break;
                
            case Lua.OP_FORPREP:
                result = String.format("FORPREP: R(%d) [循环准备]", a);
                break;
                
            case Lua.OP_TFORCALL:
                result = String.format("TFORCALL: R(%d) [迭代器调用]", a);
                break;
                
            case Lua.OP_TFORLOOP:
                result = String.format("TFORLOOP: R(%d) [迭代器循环]", a);
                break;
                
            case Lua.OP_SETLIST:
                result = String.format("SETLIST: R(%d)[...] <- R(%d)..R(%d) [批量设置表元素]", a, a + 1, a + b);
                break;
                
            case Lua.OP_CLOSURE:
                closureCounter++;
                String closureName = "Closure#" + closureCounter + "(P" + bx + ")";
                stack[a] = new DummyLuaValue(closureName);
                newValue = stack[a];
                typeInfo = "function";
                createdClosures.add(closureName + " at R(" + a + ")");
                result = String.format("CLOSURE: R(%d) <- function#%d [创建闭包]", a, bx);
                break;
                
            case Lua.OP_VARARG:
                stack[a] = new DummyLuaValue("VarArg");
                newValue = stack[a];
                typeInfo = "vararg";
                result = String.format("VARARG: R(%d) <- ...", a);
                break;
                
            case Lua.OP_EXTRAARG:
                result = String.format("EXTRAARG: %d", Lua.GETARG_Ax(code[pc]));
                break;
                
            default:
                result = String.format("UNKNOWN_OPCODE_%d", opcode);
                break;
        }
        
        if (newValue != null && typeInfo != null) {
            registerTypes.put(a, typeInfo);
            registerHistory.computeIfAbsent(a, x -> new ArrayList<>())
                .add("PC=" + pc + " " + typeInfo + ": " + truncateStr(newValue.tojstring()));
        }
        
        return result;
    }
    
    private static String truncateStr(String s) {
        if (s == null) return "null";
        if (s.length() > 50) return s.substring(0, 50) + "...";
        return s;
    }
    
    private static String getOpName(int opcode) {
        String[] names = {
            "MOVE", "LOADK", "LOADKX", "LOADBOOL", "LOADNIL", "GETUPVAL", "GETTABUP", "GETTABLE",
            "SETTABUP", "SETUPVAL", "SETTABLE", "NEWTABLE", "SELF", "ADD", "SUB", "MUL",
            "DIV", "MOD", "POW", "UNM", "NOT", "LEN", "CONCAT", "EQ",
            "LT", "LE", "TEST", "TESTSET", "CALL", "TAILCALL", "RETURN", "FORLOOP",
            "FORPREP", "TFORCALL", "TFORLOOP", "SETLIST", "CLOSURE", "VARARG", "EXTRAARG"
        };
        return opcode < names.length ? names[opcode] : "OP_" + opcode;
    }
    
    private static String getOpSymbol(int opcode) {
        switch (opcode) {
            case Lua.OP_ADD: return "+";
            case Lua.OP_SUB: return "-";
            case Lua.OP_MUL: return "*";
            case Lua.OP_DIV: return "/";
            case Lua.OP_MOD: return "%";
            case Lua.OP_POW: return "^";
            case Lua.OP_EQ: return "==";
            case Lua.OP_LT: return "<";
            case Lua.OP_LE: return "<=";
            default: return "?";
        }
    }
    
    private static String getConstantStr(LuaValue[] k, int idx) {
        if (k == null || idx < 0 || idx >= k.length) {
            return "K(" + idx + ")";
        }
        String s = k[idx].tojstring();
        if (s.length() > 50) {
            s = s.substring(0, 50) + "...";
        }
        return s;
    }
    
    private static String getRKStr(LuaValue[] k, int rk) {
        if (rk > 0xFF) {
            return getConstantStr(k, rk & 0xFF);
        }
        return "R(" + rk + ")";
    }
    
    private static void runBuiltInTests(Globals globals) {
        String[] testScripts = {
            "local a = 1 + 2\nprint(a)",
            "function factorial(n)\n    if n <= 1 then return 1 end\n    return n * factorial(n - 1)\nend\nprint(factorial(5))",
            "for i = 1, 10 do\n    print(i)\nend",
            "local t = {x = 1, y = 2}\nprint(t.x + t.y)"
        };
        
        String[] scriptNames = {
            "简单加法运算",
            "递归阶乘函数",
            "for循环",
            "表操作"
        };
        
        for (int idx = 0; idx < testScripts.length; idx++) {
            String script = testScripts[idx];
            String name = scriptNames[idx];
            
            System.out.println("========== 测试 " + (idx + 1) + ": " + name + " ==========");
            System.out.println("Lua源码:\n" + script);
            System.out.println();
            
            try {
                LuaValue value = globals.load(script, "test_" + idx);
                
                if (value instanceof LuaClosure) {
                    LuaClosure closure = (LuaClosure) value;
                    Prototype p = closure.p;
                    
                    System.out.println("指令数: " + (p.code != null ? p.code.length : 0));
                    System.out.println("栈大小: " + p.maxstacksize);
                    
                    System.out.println("\n--- 反汇编输出 ---");
                    disassembleToConsole(p, "test_" + idx);
                }
            } catch (Exception e) {
                System.err.println("错误: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("\n");
        }
    }
}
