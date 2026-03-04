package org.luaj.vm2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lua调试器 - 完整版
 * 支持动态拦截、修改变量、替换函数、断点、单步执行等高级调试功能
 */
public class LuaDebugger {
    
    private static LuaDebugger instance = new LuaDebugger();
    
    public static LuaDebugger getInstance() {
        return instance;
    }
    
    // ==================== 调试器状态 ====================
    private boolean enabled = false;
    private boolean breakOnCall = false;
    private boolean breakOnLine = false;
    private boolean breakOnError = true;
    private boolean breakOnReturn = false;
    private boolean interactiveMode = true;
    private boolean stepMode = false;
    private boolean nextMode = false;
    private boolean breakOnStart = true;  // 脚本开始时中断
    private int stepCount = 0;
    private int currentDepth = 0;
    private int breakDepth = 0;
    
    // ==================== 输入输出 ====================
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
    // ==================== 执行环境 ====================
    private Globals globalEnv = null;
    private LinkedList<CallFrame> callStack = new LinkedList<>();
    private Map<String, LuaValue> watchList = new HashMap<>();
    private Map<String, LuaValue> replacements = new HashMap<>();
    private Map<String, LuaValue> variableOverrides = new HashMap<>();
    private Set<String> breakPoints = new HashSet<>();
    private Map<String, List<Integer>> lineBreakPoints = new HashMap<>();
    
    // ==================== 当前执行状态 ====================
    private int currentLine = -1;
    private String currentFile = "?";
    private LuaClosure currentClosure = null;
    private int currentPc = -1;
    private LuaValue[] currentStack = null;
    private Varargs currentVarargs = null;
    
    // ==================== 历史记录 ====================
    private List<String> commandHistory = new ArrayList<>();
    private int historyIndex = 0;
    
    // ==================== 执行日志 ====================
    private boolean loggingEnabled = false;
    private List<String> executionLog = new ArrayList<>();
    private int maxLogSize = 1000;
    
    // ==================== 函数调用拦截 ====================
    private Map<String, CallInterceptor> callInterceptors = new HashMap<>();
    
    public interface CallInterceptor {
        LuaValue onCall(String name, Varargs args);
        void onReturn(String name, LuaValue returnValue);
    }
    
    private LuaDebugger() {}
    
    // ==================== 初始化和配置 ====================
    
    public void setGlobalEnv(Globals env) {
        this.globalEnv = env;
    }
    
    public Globals getGlobalEnv() {
        return this.globalEnv;
    }
    
    public void enable(boolean enable) {
        this.enabled = enable;
        if (enable) {
            printBanner();
        }
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setInteractiveMode(boolean interactive) {
        this.interactiveMode = interactive;
    }
    
    private void printBanner() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Lua 调试器 v1.0                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  命令:                                                     ║");
        System.out.println("║    help          - 显示帮助信息                            ║");
        System.out.println("║    step / s      - 单步执行 (进入函数)                     ║");
        System.out.println("║    next / n      - 下一步 (不进入函数)                     ║");
        System.out.println("║    continue / c  - 继续执行                                ║");
        System.out.println("║    finish        - 执行到当前函数返回                      ║");
        System.out.println("║    var [name]    - 显示变量                                ║");
        System.out.println("║    set name=val  - 设置变量                                ║");
        System.out.println("║    replace name  - 替换函数/变量                           ║");
        System.out.println("║    watch name    - 添加监视                                ║");
        System.out.println("║    stack / bt    - 显示调用栈                              ║");
        System.out.println("║    break         - 设置断点                                ║");
        System.out.println("║    eval code     - 执行Lua代码                             ║");
        System.out.println("║    log           - 查看执行日志                            ║");
        System.out.println("║    intercept     - 拦截函数调用                            ║");
        System.out.println("║    quit / q      - 退出调试器                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    // ==================== 执行钩子 ====================
    
    public void onFunctionCall(LuaClosure closure, Varargs args) {
        if (!enabled) return;
        
        currentDepth++;
        CallFrame frame = new CallFrame(closure, args, currentDepth);
        callStack.push(frame);
        currentClosure = closure;
        currentVarargs = args;
        
        if (closure.p != null && closure.p.source != null) {
            currentFile = closure.p.source.tojstring();
        }
        
        String funcName = getFunctionName(closure);
        
        // 记录日志
        if (loggingEnabled) {
            addLog("CALL " + funcName + " (" + currentFile + ")");
        }
        
        // 检查拦截器
        if (callInterceptors.containsKey(funcName)) {
            CallInterceptor interceptor = callInterceptors.get(funcName);
            interceptor.onCall(funcName, args);
        }
        
        // 脚本开始时中断（第一个调用）
        if (breakOnStart && currentDepth == 1) {
            breakOnStart = false;  // 只中断一次
            breakPoint("脚本开始", args);
        }
        
        // 检查断点
        if (breakOnCall || breakPoints.contains("func:" + funcName)) {
            breakPoint("函数调用: " + funcName, args);
        }
        
        // 单步模式
        if (stepMode) {
            breakPoint("单步执行", args);
        }
        
        // next模式检查深度
        if (nextMode && currentDepth > breakDepth) {
            // 不中断，继续执行
        } else if (nextMode && currentDepth <= breakDepth) {
            nextMode = false;
            breakPoint("下一步", args);
        }
    }
    
    public void onFunctionReturn(LuaClosure closure, LuaValue returnValue) {
        if (!enabled) return;
        
        currentDepth--;
        
        String funcName = getFunctionName(closure);
        
        // 记录日志
        if (loggingEnabled) {
            addLog("RETURN " + funcName + " -> " + truncate(returnValue.tojstring()));
        }
        
        // 检查拦截器
        if (callInterceptors.containsKey(funcName)) {
            CallInterceptor interceptor = callInterceptors.get(funcName);
            interceptor.onReturn(funcName, returnValue);
        }
        
        if (!callStack.isEmpty()) {
            callStack.pop();
        }
        
        if (breakOnReturn) {
            breakPoint("函数返回: " + funcName + " -> " + truncate(returnValue.tojstring()), null);
        }
    }
    
    public void onLineChange(LuaClosure closure, int line) {
        if (!enabled) return;
        
        currentLine = line;
        currentClosure = closure;
        if (closure.p != null && closure.p.source != null) {
            currentFile = closure.p.source.tojstring();
        }
        
        // 记录日志
        if (loggingEnabled) {
            addLog("LINE " + currentFile + ":" + line);
        }
        
        // 检查行断点
        String fileKey = currentFile.replaceAll("^@", "");
        if (lineBreakPoints.containsKey(fileKey)) {
            List<Integer> lines = lineBreakPoints.get(fileKey);
            if (lines.contains(line)) {
                breakPoint("行断点: " + currentFile + ":" + line, null);
            }
        }
        
        if (breakOnLine) {
            breakPoint("行: " + currentFile + ":" + line, null);
        }
        
        if (stepMode) {
            breakPoint("单步执行: " + currentFile + ":" + line, null);
        }
    }
    
    public void onInstruction(int pc, LuaValue[] stack, Varargs varargs) {
        if (!enabled) return;
        
        currentPc = pc;
        currentStack = stack;
        currentVarargs = varargs;
    }
    
    public void onError(LuaError error, LuaClosure closure, int pc) {
        if (!enabled || !breakOnError) return;
        
        breakPoint("错误: " + error.getMessage(), null);
    }
    
    // ==================== 变量拦截 ====================
    
    public LuaValue onGlobalGet(String name) {
        if (!enabled) return null;
        
        // 检查变量覆盖
        if (variableOverrides.containsKey(name)) {
            System.out.println("[调试] 拦截全局变量读取: " + name);
            System.out.println("       原值被替换为: " + truncate(variableOverrides.get(name).tojstring()));
            return variableOverrides.get(name);
        }
        return null;
    }
    
    public LuaValue onGlobalSet(String name, LuaValue value) {
        if (!enabled) return value;
        
        // 检查监视列表
        if (watchList.containsKey(name)) {
            System.out.println("[监视] " + name + " = " + truncate(value.tojstring()));
        }
        
        if (interactiveMode) {
            System.out.println("\n[调试] 设置全局变量: " + name + " = " + truncate(value.tojstring()));
            System.out.print("    是否修改? (输入新值或回车跳过): ");
            String input = readLine();
            if (!input.isEmpty()) {
                return parseValue(input);
            }
        }
        return value;
    }
    
    public LuaValue onFunctionInvoke(String name, LuaValue func, Varargs args) {
        if (!enabled) return null;
        
        String key = "func:" + name;
        if (replacements.containsKey(key)) {
            System.out.println("[调试] 拦截函数调用: " + name);
            System.out.println("       参数数量: " + args.narg());
            for (int i = 1; i <= args.narg(); i++) {
                System.out.println("       arg" + i + " = " + truncate(args.arg(i).tojstring()));
            }
            
            LuaValue replacement = replacements.get(key);
            if (replacement.isfunction()) {
                System.out.println("       使用替换函数执行...");
                return replacement.invoke(args).arg1();
            }
            System.out.println("       返回替换值: " + truncate(replacement.tojstring()));
            return replacement;
        }
        return null;
    }
    
    // ==================== 断点处理 ====================
    
    private void breakPoint(String reason, Varargs args) {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [断点] " + padRight(truncate(reason, 48), 48) + " ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        // 显示位置信息
        System.out.println("║ 文件: " + padRight(currentFile, 51) + "║");
        System.out.println("║ 行号: " + padRight(String.valueOf(currentLine), 51) + "║");
        System.out.println("║ 深度: " + padRight(String.valueOf(currentDepth), 51) + "║");
        
        // 显示参数
        if (args != null && args.narg() > 0) {
            System.out.println("╠────────────────────────────────────────────────────────────╣");
            System.out.println("║ 参数:");
            for (int i = 1; i <= Math.min(args.narg(), 5); i++) {
                String argStr = truncate(args.arg(i).tojstring(), 45);
                System.out.println("║   [" + i + "] " + padRight(argStr, 49) + "║");
            }
            if (args.narg() > 5) {
                System.out.println("║   ... 还有 " + (args.narg() - 5) + " 个参数");
            }
        }
        
        // 显示监视变量
        if (!watchList.isEmpty()) {
            System.out.println("╠────────────────────────────────────────────────────────────╣");
            System.out.println("║ 监视:");
            for (Map.Entry<String, LuaValue> entry : watchList.entrySet()) {
                LuaValue val = globalEnv != null ? globalEnv.get(entry.getKey()) : LuaValue.NIL;
                System.out.println("║   " + padRight(entry.getKey() + " = " + truncate(val.tojstring(), 35), 54) + "║");
            }
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        commandLoop();
    }
    
    // ==================== 命令循环 ====================
    
    private void commandLoop() {
        while (true) {
            System.out.print("\n[调试@" + currentFile + ":" + currentLine + "] > ");
            String input = readLine();
            
            if (input.isEmpty()) {
                // 重复上一个命令
                if (!commandHistory.isEmpty()) {
                    input = commandHistory.get(commandHistory.size() - 1);
                    System.out.println("(重复: " + input + ")");
                } else {
                    continue;
                }
            } else {
                commandHistory.add(input);
            }
            
            String[] parts = splitCommand(input);
            String cmd = parts[0].toLowerCase();
            String arg = parts.length > 1 ? parts[1] : "";
            
            try {
                switch (cmd) {
                    case "h":
                    case "help":
                        showHelp();
                        break;
                        
                    case "c":
                    case "continue":
                    case "cont":
                        stepMode = false;
                        nextMode = false;
                        return;
                        
                    case "s":
                    case "step":
                        stepMode = true;
                        nextMode = false;
                        return;
                        
                    case "n":
                    case "next":
                        stepMode = false;
                        nextMode = true;
                        breakDepth = currentDepth;
                        return;
                        
                    case "finish":
                        breakOnReturn = true;
                        return;
                        
                    case "r":
                    case "run":
                        stepMode = false;
                        nextMode = false;
                        breakOnLine = false;
                        breakOnCall = false;
                        return;
                        
                    case "var":
                    case "vars":
                    case "v":
                        showVariables(arg);
                        break;
                        
                    case "set":
                        setVariable(arg);
                        break;
                        
                    case "replace":
                        replaceFunction(arg);
                        break;
                        
                    case "override":
                        overrideVariable(arg);
                        break;
                        
                    case "watch":
                    case "w":
                        addWatch(arg);
                        break;
                        
                    case "unwatch":
                        removeWatch(arg);
                        break;
                        
                    case "stack":
                    case "bt":
                    case "where":
                        showStack();
                        break;
                        
                    case "env":
                    case "globals":
                        showEnv();
                        break;
                        
                    case "break":
                    case "bp":
                        manageBreakPoints(arg);
                        break;
                        
                    case "eval":
                    case "e":
                    case "=":
                        evalCode(arg);
                        break;
                        
                    case "log":
                        showLog();
                        break;
                        
                    case "intercept":
                        addInterceptor(arg);
                        break;
                        
                    case "dump":
                        dumpState();
                        break;
                        
                    case "history":
                        showHistory();
                        break;
                        
                    case "reset":
                        resetDebugger();
                        return;
                        
                    case "q":
                    case "quit":
                    case "exit":
                        enabled = false;
                        return;
                        
                    default:
                        // 尝试作为Lua代码执行
                        try {
                            evalCode(input);
                        } catch (Exception e) {
                            System.out.println("未知命令: " + cmd + " (输入 help 查看帮助)");
                        }
                }
            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
            }
        }
    }
    
    // ==================== 命令实现 ====================
    
    private void showHelp() {
        System.out.println("\n========== 调试命令详解 ==========");
        System.out.println();
        System.out.println("【执行控制】");
        System.out.println("  continue / c      - 继续执行直到下一个断点");
        System.out.println("  step / s          - 单步执行，进入函数内部");
        System.out.println("  next / n          - 下一步，不进入函数内部");
        System.out.println("  finish            - 执行到当前函数返回");
        System.out.println("  run               - 继续执行，清除所有临时断点");
        System.out.println();
        System.out.println("【变量操作】");
        System.out.println("  var [name]        - 显示所有变量或指定变量");
        System.out.println("  set name=value    - 设置变量值");
        System.out.println("  override name=val - 覆盖变量(每次读取时替换)");
        System.out.println("  watch name        - 添加变量到监视列表");
        System.out.println("  unwatch name      - 从监视列表移除变量");
        System.out.println();
        System.out.println("【函数操作】");
        System.out.println("  replace name      - 替换函数实现或返回值");
        System.out.println("  intercept name    - 拦截函数调用");
        System.out.println();
        System.out.println("【断点管理】");
        System.out.println("  break             - 显示所有断点");
        System.out.println("  break func:name   - 在函数入口设置断点");
        System.out.println("  break file:line   - 在指定行设置断点");
        System.out.println("  break call        - 在所有函数调用时断点");
        System.out.println("  break line        - 在每行断点");
        System.out.println();
        System.out.println("【信息查看】");
        System.out.println("  stack / bt        - 显示调用栈");
        System.out.println("  env               - 显示全局环境");
        System.out.println("  eval code         - 执行Lua代码");
        System.out.println("  log               - 显示执行日志");
        System.out.println("  dump              - 导出当前状态");
        System.out.println("  history           - 显示命令历史");
        System.out.println();
        System.out.println("【其他】");
        System.out.println("  reset             - 重置调试器状态");
        System.out.println("  quit / q          - 退出调试器");
        System.out.println("==================================");
    }
    
    private void showVariables(String name) {
        if (globalEnv == null) {
            System.out.println("全局环境未设置");
            return;
        }
        
        if (name.isEmpty()) {
            System.out.println("\n=== 全局变量 ===");
            LuaValue k = LuaValue.NIL;
            int count = 0;
            while (count < 30) {
                Varargs n = globalEnv.next(k);
                if ((k = n.arg1()).isnil()) break;
                LuaValue v = n.arg(2);
                String keyStr = k.tojstring();
                // 跳过内部变量
                if (!keyStr.startsWith("_")) {
                    System.out.println("  " + keyStr + " = " + truncate(v.tojstring(), 50));
                    count++;
                }
            }
            if (count == 0) {
                System.out.println("  (无变量)");
            }
        } else {
            // 支持多级访问，如 gg.setVisible
            String[] parts = name.split("\\.");
            LuaValue val = globalEnv;
            for (String part : parts) {
                if (val.istable()) {
                    val = val.get(part);
                } else {
                    val = LuaValue.NIL;
                    break;
                }
            }
            
            if (val.isnil()) {
                System.out.println("变量 '" + name + "' 不存在或为nil");
            } else {
                System.out.println(name + " = " + val.tojstring());
                System.out.println("类型: " + val.typename());
                
                if (val.istable()) {
                    LuaTable t = (LuaTable) val;
                    System.out.println("\n  表内容:");
                    LuaValue k = LuaValue.NIL;
                    int count = 0;
                    while (count < 15) {
                        Varargs n = t.next(k);
                        if ((k = n.arg1()).isnil()) break;
                        LuaValue v = n.arg(2);
                        System.out.println("    " + k.tojstring() + " = " + truncate(v.tojstring(), 40));
                        count++;
                    }
                    if (count == 15) {
                        System.out.println("    ... (还有更多)");
                    }
                } else if (val.isfunction()) {
                    System.out.println("  (函数)");
                }
            }
        }
        
        // 显示局部变量（如果有栈帧）
        if (!callStack.isEmpty() && currentStack != null) {
            System.out.println("\n=== 局部变量 ===");
            CallFrame frame = callStack.peek();
            if (frame.closure.p != null) {
                Prototype p = frame.closure.p;
                if (p.locvars != null) {
                    for (int i = 0; i < p.locvars.length; i++) {
                        LocVars lv = p.locvars[i];
                        if (lv.varname != null) {
                            int slot = lv.startpc;
                            if (slot >= 0 && slot < currentStack.length) {
                                System.out.println("  " + lv.varname.tojstring() + " = " + 
                                    truncate(currentStack[slot].tojstring(), 50));
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void setVariable(String arg) {
        if (arg.isEmpty()) {
            System.out.println("用法: set name=value");
            System.out.println("示例: set x=123");
            System.out.println("      set name=\"hello\"");
            System.out.println("      set t.field=value");
            return;
        }
        
        int eqPos = arg.indexOf('=');
        if (eqPos <= 0) {
            System.out.println("用法: set name=value");
            return;
        }
        
        String name = arg.substring(0, eqPos).trim();
        String value = arg.substring(eqPos + 1).trim();
        
        // 解析变量路径
        String[] parts = name.split("\\.");
        
        if (parts.length == 1) {
            // 直接设置全局变量
            LuaValue val = parseValue(value);
            globalEnv.set(name, val);
            System.out.println("已设置: " + name + " = " + val.tojstring());
        } else {
            // 设置表的字段
            LuaValue table = globalEnv;
            for (int i = 0; i < parts.length - 1; i++) {
                table = table.get(parts[i]);
                if (!table.istable()) {
                    System.out.println("错误: " + parts[i] + " 不是表");
                    return;
                }
            }
            LuaValue val = parseValue(value);
            table.set(parts[parts.length - 1], val);
            System.out.println("已设置: " + name + " = " + val.tojstring());
        }
    }
    
    private void overrideVariable(String arg) {
        if (arg.isEmpty()) {
            System.out.println("用法: override name=value");
            System.out.println("覆盖后，每次读取该变量时都会返回指定值");
            return;
        }
        
        int eqPos = arg.indexOf('=');
        if (eqPos <= 0) {
            System.out.println("用法: override name=value");
            return;
        }
        
        String name = arg.substring(0, eqPos).trim();
        String value = arg.substring(eqPos + 1).trim();
        
        LuaValue val = parseValue(value);
        variableOverrides.put(name, val);
        System.out.println("已覆盖: " + name + " -> " + val.tojstring());
        System.out.println("每次读取 " + name + " 时都会返回 " + val.tojstring());
    }
    
    private void replaceFunction(String arg) {
        if (arg.isEmpty()) {
            System.out.println("用法: replace funcName");
            System.out.println("      然后选择替换方式:");
            System.out.println("      1. 输入返回值 - 函数直接返回该值");
            System.out.println("      2. 输入 'func' - 定义新的函数实现");
            System.out.println("      3. 输入 'intercept' - 拦截并手动处理每次调用");
            return;
        }
        
        String name = arg.trim();
        System.out.println("替换函数: " + name);
        System.out.println("选择替换方式:");
        System.out.println("  1. 返回固定值 - 输入值");
        System.out.println("  2. 定义新函数 - 输入 'func'");
        System.out.println("  3. 交互拦截   - 输入 'intercept'");
        System.out.println("  4. 取消       - 输入 'cancel'");
        System.out.print("选择: ");
        
        String input = readLine().trim();
        
        if (input.equalsIgnoreCase("cancel")) {
            System.out.println("已取消");
            return;
        }
        
        if (input.equalsIgnoreCase("func")) {
            System.out.println("输入函数定义 (输入空行结束):");
            System.out.println("格式: function(arg1, arg2, ...) ... end");
            System.out.println("或直接输入函数体 (自动包装为function(...))");
            System.out.println();
            
            StringBuilder code = new StringBuilder();
            String line;
            while (!(line = readLine()).isEmpty()) {
                code.append(line).append("\n");
            }
            
            String codeStr = code.toString().trim();
            if (!codeStr.startsWith("function")) {
                codeStr = "function(...) " + codeStr + " end";
            }
            
            try {
                LuaValue func = globalEnv.load(codeStr, "debugger");
                replacements.put("func:" + name, func);
                System.out.println("函数已替换: " + name);
            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
            }
        } else if (input.equalsIgnoreCase("intercept")) {
            System.out.println("已启用交互拦截: " + name);
            System.out.println("每次调用时会询问处理方式");
            replacements.put("func:" + name + ":intercept", LuaValue.TRUE);
        } else {
            LuaValue val = parseValue(input);
            replacements.put("func:" + name, val);
            System.out.println("已替换: " + name + " -> " + val.tojstring());
        }
    }
    
    private void addWatch(String arg) {
        if (arg.isEmpty()) {
            if (watchList.isEmpty()) {
                System.out.println("监视列表为空");
            } else {
                System.out.println("监视列表:");
                for (String name : watchList.keySet()) {
                    LuaValue val = globalEnv != null ? globalEnv.get(name) : LuaValue.NIL;
                    System.out.println("  " + name + " = " + truncate(val.tojstring(), 50));
                }
            }
        } else {
            watchList.put(arg.trim(), LuaValue.NIL);
            System.out.println("已添加监视: " + arg);
        }
    }
    
    private void removeWatch(String arg) {
        if (arg.isEmpty()) {
            watchList.clear();
            System.out.println("已清空监视列表");
        } else {
            watchList.remove(arg.trim());
            System.out.println("已移除监视: " + arg);
        }
    }
    
    private void showStack() {
        if (callStack.isEmpty()) {
            System.out.println("调用栈为空");
            return;
        }
        
        System.out.println("\n=== 调用栈 ===");
        int i = 0;
        for (CallFrame frame : callStack) {
            String name = getFunctionName(frame.closure);
            String file = frame.closure.p != null && frame.closure.p.source != null ? 
                frame.closure.p.source.tojstring() : "?";
            int line = frame.closure.p != null && frame.closure.p.lineinfo != null && frame.closure.p.lineinfo.length > 0 ?
                frame.closure.p.lineinfo[0] : -1;
            
            String marker = (i == 0) ? "=>" : "  ";
            System.out.println(marker + " #" + i + " " + name + " (" + file + ":" + line + ")");
            
            // 显示参数
            if (frame.args != null && frame.args.narg() > 0) {
                StringBuilder args = new StringBuilder("      参数: ");
                for (int j = 1; j <= Math.min(frame.args.narg(), 3); j++) {
                    if (j > 1) args.append(", ");
                    args.append(truncate(frame.args.arg(j).tojstring(), 20));
                }
                if (frame.args.narg() > 3) {
                    args.append(", ...");
                }
                System.out.println(args.toString());
            }
            
            i++;
        }
        System.out.println("==============");
    }
    
    private void showEnv() {
        if (globalEnv == null) {
            System.out.println("全局环境未设置");
            return;
        }
        
        System.out.println("\n=== 全局环境 ===");
        System.out.println("类型: " + globalEnv.typename());
        System.out.println("条目数: " + countTable(globalEnv));
        
        System.out.println("\n=== 变量覆盖 ===");
        if (variableOverrides.isEmpty()) {
            System.out.println("(无)");
        } else {
            for (Map.Entry<String, LuaValue> entry : variableOverrides.entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + truncate(entry.getValue().tojstring(), 40));
            }
        }
        
        System.out.println("\n=== 函数替换 ===");
        if (replacements.isEmpty()) {
            System.out.println("(无)");
        } else {
            for (Map.Entry<String, LuaValue> entry : replacements.entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + truncate(entry.getValue().tojstring(), 40));
            }
        }
        
        System.out.println("\n=== 断点 ===");
        if (breakPoints.isEmpty() && lineBreakPoints.isEmpty()) {
            System.out.println("(无)");
        } else {
            for (String bp : breakPoints) {
                System.out.println("  " + bp);
            }
            for (Map.Entry<String, List<Integer>> entry : lineBreakPoints.entrySet()) {
                for (int line : entry.getValue()) {
                    System.out.println("  " + entry.getKey() + ":" + line);
                }
            }
        }
    }
    
    private void manageBreakPoints(String arg) {
        if (arg.isEmpty()) {
            // 显示所有断点
            System.out.println("\n=== 断点列表 ===");
            if (breakPoints.isEmpty() && lineBreakPoints.isEmpty()) {
                System.out.println("(无断点)");
            } else {
                int i = 1;
                for (String bp : breakPoints) {
                    System.out.println("  " + i + ". " + bp);
                    i++;
                }
                for (Map.Entry<String, List<Integer>> entry : lineBreakPoints.entrySet()) {
                    for (int line : entry.getValue()) {
                        System.out.println("  " + i + ". " + entry.getKey() + ":" + line);
                        i++;
                    }
                }
            }
            System.out.println("\n全局断点设置:");
            System.out.println("  breakOnCall: " + breakOnCall);
            System.out.println("  breakOnLine: " + breakOnLine);
            System.out.println("  breakOnReturn: " + breakOnReturn);
            System.out.println("  breakOnError: " + breakOnError);
            return;
        }
        
        String[] parts = arg.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String param = parts.length > 1 ? parts[1] : "";
        
        switch (cmd) {
            case "call":
                breakOnCall = !breakOnCall;
                System.out.println("函数调用断点: " + (breakOnCall ? "开启" : "关闭"));
                break;
                
            case "line":
                breakOnLine = !breakOnLine;
                System.out.println("行断点: " + (breakOnLine ? "开启" : "关闭"));
                break;
                
            case "return":
                breakOnReturn = !breakOnReturn;
                System.out.println("返回断点: " + (breakOnReturn ? "开启" : "关闭"));
                break;
                
            case "error":
                breakOnError = !breakOnError;
                System.out.println("错误断点: " + (breakOnError ? "开启" : "关闭"));
                break;
                
            case "clear":
                breakPoints.clear();
                lineBreakPoints.clear();
                System.out.println("已清除所有断点");
                break;
                
            case "del":
            case "delete":
                try {
                    int index = Integer.parseInt(param) - 1;
                    // 删除指定断点
                    System.out.println("断点删除功能待实现");
                } catch (Exception e) {
                    System.out.println("用法: break delete <编号>");
                }
                break;
                
            default:
                // 设置断点
                if (arg.startsWith("func:")) {
                    breakPoints.add(arg);
                    System.out.println("已添加函数断点: " + arg);
                } else if (arg.contains(":")) {
                    // 文件:行 格式
                    String[] bpParts = arg.split(":");
                    if (bpParts.length == 2) {
                        try {
                            String file = bpParts[0];
                            int line = Integer.parseInt(bpParts[1]);
                            lineBreakPoints.computeIfAbsent(file, k -> new ArrayList<>()).add(line);
                            System.out.println("已添加行断点: " + file + ":" + line);
                        } catch (NumberFormatException e) {
                            System.out.println("无效的行号: " + bpParts[1]);
                        }
                    }
                } else {
                    // 默认作为函数断点
                    breakPoints.add("func:" + arg);
                    System.out.println("已添加函数断点: func:" + arg);
                }
        }
    }
    
    private void evalCode(String code) {
        if (code.isEmpty()) {
            System.out.println("用法: eval <Lua代码>");
            System.out.println("示例: eval print('hello')");
            System.out.println("      eval return 1+2+3");
            return;
        }
        
        try {
            String codeToRun = code.trim();
            if (!codeToRun.contains("return") && !codeToRun.contains("print") && 
                !codeToRun.contains("=") && !codeToRun.startsWith("function")) {
                codeToRun = "return " + codeToRun;
            }
            
            LuaValue result = globalEnv.load(codeToRun, "debugger").call();
            if (!result.isnil()) {
                System.out.println("结果: " + result.tojstring());
            }
        } catch (Exception e) {
            // 尝试作为语句执行
            try {
                globalEnv.load(code, "debugger").call();
                System.out.println("执行成功");
            } catch (Exception e2) {
                System.out.println("错误: " + e2.getMessage());
            }
        }
    }
    
    private void showLog() {
        if (executionLog.isEmpty()) {
            System.out.println("执行日志为空 (使用 'log on' 开启日志)");
            return;
        }
        
        System.out.println("\n=== 执行日志 (最近" + Math.min(50, executionLog.size()) + "条) ===");
        int start = Math.max(0, executionLog.size() - 50);
        for (int i = start; i < executionLog.size(); i++) {
            System.out.println("  " + executionLog.get(i));
        }
    }
    
    private void addInterceptor(String arg) {
        if (arg.isEmpty()) {
            System.out.println("用法: intercept <函数名>");
            System.out.println("拦截后，每次调用该函数时可以:");
            System.out.println("  - 查看参数");
            System.out.println("  - 修改参数");
            System.out.println("  - 跳过执行");
            System.out.println("  - 修改返回值");
            return;
        }
        
        final String funcName = arg.trim();
        callInterceptors.put(funcName, new CallInterceptor() {
            @Override
            public LuaValue onCall(String name, Varargs args) {
                System.out.println("\n[拦截] 函数调用: " + name);
                System.out.println("参数:");
                for (int i = 1; i <= args.narg(); i++) {
                    System.out.println("  arg" + i + " = " + args.arg(i).tojstring());
                }
                
                System.out.print("\n选择操作: [c]继续 [s]跳过 [m]修改返回值: ");
                String input = readLine().trim().toLowerCase();
                
                switch (input) {
                    case "s":
                        return LuaValue.NIL;
                    case "m":
                        System.out.print("输入返回值: ");
                        String val = readLine().trim();
                        return parseValue(val);
                    default:
                        return null; // 继续执行原函数
                }
            }
            
            @Override
            public void onReturn(String name, LuaValue returnValue) {
                System.out.println("[拦截] 函数返回: " + name + " -> " + returnValue.tojstring());
            }
        });
        
        System.out.println("已添加拦截器: " + funcName);
    }
    
    private void dumpState() {
        System.out.println("\n=== 调试器状态导出 ===");
        System.out.println("enabled: " + enabled);
        System.out.println("currentFile: " + currentFile);
        System.out.println("currentLine: " + currentLine);
        System.out.println("currentDepth: " + currentDepth);
        System.out.println("callStackSize: " + callStack.size());
        System.out.println("watchListSize: " + watchList.size());
        System.out.println("replacementsSize: " + replacements.size());
        System.out.println("variableOverridesSize: " + variableOverrides.size());
        System.out.println("breakPointsSize: " + breakPoints.size());
        System.out.println("lineBreakPointsSize: " + lineBreakPoints.size());
        System.out.println("logSize: " + executionLog.size());
        System.out.println("======================");
    }
    
    private void showHistory() {
        if (commandHistory.isEmpty()) {
            System.out.println("命令历史为空");
            return;
        }
        
        System.out.println("\n=== 命令历史 ===");
        int start = Math.max(0, commandHistory.size() - 20);
        for (int i = start; i < commandHistory.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + commandHistory.get(i));
        }
    }
    
    private void resetDebugger() {
        callStack.clear();
        watchList.clear();
        replacements.clear();
        variableOverrides.clear();
        breakPoints.clear();
        lineBreakPoints.clear();
        callInterceptors.clear();
        executionLog.clear();
        commandHistory.clear();
        stepMode = false;
        nextMode = false;
        breakOnCall = false;
        breakOnLine = false;
        breakOnReturn = false;
        currentDepth = 0;
        System.out.println("调试器已重置");
    }
    
    // ==================== 工具方法 ====================
    
    private String getFunctionName(LuaClosure closure) {
        if (closure == null) return "<null>";
        if (closure.p == null) return "<unknown>";
        // 从source中提取名称
        if (closure.p.source != null) {
            String src = closure.p.source.tojstring();
            if (src.startsWith("@")) {
                src = src.substring(1);
            }
            return src;
        }
        return "<anonymous>";
    }
    
    private LuaValue parseValue(String input) {
        input = input.trim();
        
        if (input.equals("nil")) return LuaValue.NIL;
        if (input.equals("true")) return LuaValue.TRUE;
        if (input.equals("false")) return LuaValue.FALSE;
        
        // 字符串
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return LuaValue.valueOf(input.substring(1, input.length() - 1));
        }
        if (input.startsWith("'") && input.endsWith("'")) {
            return LuaValue.valueOf(input.substring(1, input.length() - 1));
        }
        
        // 表
        if (input.startsWith("{") && input.endsWith("}")) {
            return parseTableLiteral(input);
        }
        
        // 数字
        try {
            if (input.contains(".") || input.toLowerCase().contains("e")) {
                return LuaValue.valueOf(Double.parseDouble(input));
            }
            // 支持十六进制
            if (input.startsWith("0x") || input.startsWith("0X")) {
                return LuaValue.valueOf(Long.parseLong(input.substring(2), 16));
            }
            return LuaValue.valueOf(Long.parseLong(input));
        } catch (NumberFormatException e) {
            // 作为字符串
            return LuaValue.valueOf(input);
        }
    }
    
    private LuaTable parseTableLiteral(String input) {
        LuaTable table = new LuaTable();
        // 简单解析 {a=1, b=2, "item"}
        String content = input.substring(1, input.length() - 1).trim();
        if (content.isEmpty()) return table;
        
        String[] items = splitTableItems(content);
        int arrayIndex = 1;
        
        for (String item : items) {
            item = item.trim();
            if (item.isEmpty()) continue;
            
            if (item.contains("=")) {
                String[] kv = item.split("=", 2);
                String key = kv[0].trim();
                String value = kv[1].trim();
                table.set(parseValue(key), parseValue(value));
            } else {
                table.set(arrayIndex++, parseValue(item));
            }
        }
        
        return table;
    }
    
    private String[] splitTableItems(String content) {
        List<String> items = new ArrayList<>();
        int depth = 0;
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        char stringChar = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (inString) {
                current.append(c);
                if (c == stringChar && (i == 0 || content.charAt(i-1) != '\\')) {
                    inString = false;
                }
            } else if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                current.append(c);
            } else if (c == '{' || c == '(' || c == '[') {
                depth++;
                current.append(c);
            } else if (c == '}' || c == ')' || c == ']') {
                depth--;
                current.append(c);
            } else if (c == ',' && depth == 0) {
                items.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            items.add(current.toString());
        }
        
        return items.toArray(new String[0]);
    }
    
    private int countTable(LuaTable table) {
        int count = 0;
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = table.next(k);
            if ((k = n.arg1()).isnil()) break;
            count++;
        }
        return count;
    }
    
    private String truncate(String s) {
        return truncate(s, 60);
    }
    
    private String truncate(String s, int max) {
        if (s == null) return "null";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }
    
    private String padRight(String s, int len) {
        if (s.length() >= len) return s.substring(0, len);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < len) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
    private String readLine() {
        try {
            return reader.readLine();
        } catch (Exception e) {
            return "";
        }
    }
    
    private String[] splitCommand(String input) {
        input = input.trim();
        int spacePos = input.indexOf(' ');
        if (spacePos <= 0) {
            return new String[] { input, "" };
        }
        return new String[] { 
            input.substring(0, spacePos), 
            input.substring(spacePos + 1).trim() 
        };
    }
    
    private void addLog(String message) {
        executionLog.add(message);
        if (executionLog.size() > maxLogSize) {
            executionLog.remove(0);
        }
    }
    
    // ==================== 调用帧内部类 ====================
    
    private static class CallFrame {
        final LuaClosure closure;
        final Varargs args;
        final int depth;
        
        CallFrame(LuaClosure closure, Varargs args, int depth) {
            this.closure = closure;
            this.args = args;
            this.depth = depth;
        }
    }
}
