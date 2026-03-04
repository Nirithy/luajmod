package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * 智能反编译器 - 能够解密混淆的Lua字节码
 * 功能：
 * 1. 真正执行CONCAT指令合并拆分的字符串
 * 2. 过滤垃圾信息，只保留有用内容
 * 3. 提取字符串常量、函数名、全局变量访问等
 */
public class SmartDecompiler {

    private static PrintWriter writer;
    private static Set<String> allStrings = new LinkedHashSet<>();
    private static Set<String> chineseStrings = new LinkedHashSet<>();
    private static Set<String> globalVars = new LinkedHashSet<>();
    private static Set<String> functionDefs = new LinkedHashSet<>();
    private static Set<String> tableFields = new LinkedHashSet<>();
    private static Set<String> functionCalls = new LinkedHashSet<>();
    private static Map<String, Set<String>> tableContents = new HashMap<>();
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]+");
    
    /**
     * 主入口
     */
    public static void main(String[] args) {
        System.out.println("========== LuaJ 智能反编译器 ==========\n");
        
        Globals globals = JsePlatform.standardGlobals();
        
        if (args.length > 0) {
            String filePath = args[0];
            String outputPath = args.length > 1 ? args[1] : filePath + ".smart.txt";
            
            System.out.println("分析文件: " + filePath);
            System.out.println("输出文件: " + outputPath);
            
            try {
                writer = new PrintWriter(new FileWriter(outputPath), true);
                analyzeFile(filePath);
                writer.close();
                System.out.println("分析完成，结果已保存到: " + outputPath);
            } catch (Exception e) {
                System.err.println("错误: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("用法: java SmartDecompiler <luac文件> [输出文件]");
        }
        
        System.out.println("========== 完成 ==========");
    }
    
    /**
     * 分析Lua字节码文件
     */
    private static void analyzeFile(String filePath) {
        try {
            InputStream is = new FileInputStream(filePath);
            Prototype p = LoadState.undump(is, filePath);
            is.close();
            
            if (p == null) {
                log("无法加载文件");
                return;
            }
            
            log("========== 智能反编译结果 ==========\n");
            
            analyzePrototype(p, "main", 0);
            
            printSummary();
            
        } catch (Exception e) {
            log("分析失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 分析单个Prototype
     */
    private static void analyzePrototype(Prototype p, String name, int depth) {
        if (p == null || p.code == null) return;
        
        log(String.format("\n[%s] 函数: %s (深度: %d)", "=".repeat(20), name, depth));
        
        int[] code = p.code;
        LuaValue[] k = p.k;
        int maxStack = Math.max(p.maxstacksize, 250);
        
        LuaValue[] stack = new LuaValue[maxStack];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = LuaValue.NIL;
        }
        
        LuaValue[] upvalues = new LuaValue[p.upvalues != null ? p.upvalues.length : 0];
        for (int i = 0; i < upvalues.length; i++) {
            upvalues[i] = LuaValue.valueOf("U" + i);
        }
        
        int tableCounter = 0;
        int closureCounter = 0;
        
        for (int pc = 0; pc < code.length; pc++) {
            int i = code[pc];
            int opcode = Lua.GET_OPCODE(i);
            int a = Lua.GETARG_A(i);
            int b = Lua.GETARG_B(i);
            int c = Lua.GETARG_C(i);
            int bx = Lua.GETARG_Bx(i);
            
            switch (opcode) {
                case Lua.OP_LOADK:
                    if (k != null && bx >= 0 && bx < k.length) {
                        stack[a] = k[bx];
                        String str = k[bx].tojstring();
                        if (isUsefulString(str)) {
                            allStrings.add(str);
                            if (containsChinese(str)) {
                                chineseStrings.add(str);
                            }
                        }
                    }
                    break;
                    
                case Lua.OP_LOADBOOL:
                    stack[a] = (b != 0) ? LuaValue.TRUE : LuaValue.FALSE;
                    break;
                    
                case Lua.OP_LOADNIL:
                    for (int j = a; j <= a + b && j < stack.length; j++) {
                        stack[j] = LuaValue.NIL;
                    }
                    break;
                    
                case Lua.OP_MOVE:
                    if (b >= 0 && b < stack.length) {
                        stack[a] = stack[b];
                    }
                    break;
                    
                case Lua.OP_CONCAT:
                    StringBuilder sb = new StringBuilder();
                    for (int j = b; j <= c && j < stack.length; j++) {
                        if (stack[j] != null && !stack[j].isnil()) {
                            sb.append(stack[j].tojstring());
                        }
                    }
                    String concatResult = sb.toString();
                    stack[a] = LuaValue.valueOf(concatResult);
                    if (isUsefulString(concatResult)) {
                        allStrings.add(concatResult);
                        if (containsChinese(concatResult)) {
                            chineseStrings.add(concatResult);
                        }
                    }
                    break;
                    
                case Lua.OP_GETTABUP:
                    String key1 = getRKValue(k, stack, c);
                    if (isUsefulString(key1)) {
                        String globalAccess = "_ENV[" + key1 + "]";
                        globalVars.add(globalAccess);
                        allStrings.add(key1);
                    }
                    stack[a] = LuaValue.valueOf("global_" + key1);
                    break;
                    
                case Lua.OP_SETTABUP:
                    String key2 = getRKValue(k, stack, b);
                    String val2 = getRKValue(k, stack, c);
                    if (isUsefulString(key2)) {
                        String globalWrite = "_ENV[" + key2 + "] = " + val2;
                        globalVars.add(globalWrite);
                        allStrings.add(key2);
                    }
                    break;
                    
                case Lua.OP_GETTABLE:
                    String key3 = getRKValue(k, stack, c);
                    if (isUsefulString(key3)) {
                        tableFields.add("table[" + key3 + "]");
                        allStrings.add(key3);
                    }
                    stack[a] = LuaValue.valueOf("field_" + key3);
                    break;
                    
                case Lua.OP_SETTABLE:
                    String key4 = getRKValue(k, stack, b);
                    String val4 = getRKValue(k, stack, c);
                    if (isUsefulString(key4)) {
                        tableFields.add(key4 + " = " + val4);
                        allStrings.add(key4);
                    }
                    break;
                    
                case Lua.OP_SELF:
                    String method = getRKValue(k, stack, c);
                    if (isUsefulString(method)) {
                        functionCalls.add("obj:" + method);
                        allStrings.add(method);
                    }
                    stack[a] = LuaValue.valueOf("method_" + method);
                    break;
                    
                case Lua.OP_NEWTABLE:
                    tableCounter++;
                    stack[a] = LuaValue.valueOf("Table#" + tableCounter);
                    break;
                    
                case Lua.OP_CLOSURE:
                    closureCounter++;
                    String closureName = "Closure#" + closureCounter;
                    stack[a] = LuaValue.valueOf(closureName);
                    functionDefs.add(closureName + " (P" + bx + ")");
                    break;
                    
                case Lua.OP_CALL:
                    String funcName = stack[a] != null ? stack[a].tojstring() : "unknown";
                    functionCalls.add(funcName + "(" + (b - 1) + " args)");
                    break;
                    
                case Lua.OP_TAILCALL:
                    String funcName2 = stack[a] != null ? stack[a].tojstring() : "unknown";
                    functionCalls.add(funcName2 + "(" + (b - 1) + " args) [tail]");
                    break;
            }
        }
        
        if (p.p != null) {
            for (int i = 0; i < p.p.length; i++) {
                analyzePrototype(p.p[i], name + "_sub" + i, depth + 1);
            }
        }
    }
    
    /**
     * 获取RK值（寄存器或常量）
     */
    private static String getRKValue(LuaValue[] k, LuaValue[] stack, int rk) {
        if (rk > 0xFF) {
            int idx = rk & 0xFF;
            if (k != null && idx >= 0 && idx < k.length) {
                return k[idx].tojstring();
            }
            return "K(" + idx + ")";
        }
        if (rk >= 0 && rk < stack.length && stack[rk] != null) {
            return stack[rk].tojstring();
        }
        return "R(" + rk + ")";
    }
    
    /**
     * 判断字符串是否有用
     */
    private static boolean isUsefulString(String s) {
        if (s == null || s.isEmpty()) return false;
        if (s.startsWith("dummy_")) return false;
        if (s.startsWith("DummyLuaValue")) return false;
        if (s.startsWith("Table#")) return false;
        if (s.startsWith("Closure#")) return false;
        if (s.startsWith("global_")) return false;
        if (s.startsWith("field_")) return false;
        if (s.startsWith("method_")) return false;
        if (s.startsWith("U") && s.length() <= 3) return false;
        if (s.equals("true") || s.equals("false") || s.equals("nil")) return false;
        if (s.matches("^[RUK]\\d+$")) return false;
        if (s.matches("^Ret\\d+$")) return false;
        if (s.matches("^\\d+$")) return false;
        if (s.length() == 1 && !Character.isLetterOrDigit(s.charAt(0))) return false;
        return true;
    }
    
    /**
     * 检查是否包含中文
     */
    private static boolean containsChinese(String s) {
        return CHINESE_PATTERN.matcher(s).find();
    }
    
    /**
     * 打印摘要
     */
    private static void printSummary() {
        log("\n\n========== 摘要 ==========\n");
        
        if (!chineseStrings.isEmpty()) {
            log("【中文字符串】(" + chineseStrings.size() + " 个)");
            for (String s : chineseStrings) {
                log("  " + s);
            }
            log("");
        }
        
        log("【所有有意义的字符串】(" + allStrings.size() + " 个)");
        List<String> sortedStrings = new ArrayList<>(allStrings);
        sortedStrings.sort((a, b) -> {
            boolean aChinese = containsChinese(a);
            boolean bChinese = containsChinese(b);
            if (aChinese && !bChinese) return -1;
            if (!aChinese && bChinese) return 1;
            return Integer.compare(b.length(), a.length());
        });
        for (String s : sortedStrings) {
            String prefix = containsChinese(s) ? "[中文] " : "";
            log("  " + prefix + s);
        }
        
        if (!globalVars.isEmpty()) {
            log("\n【全局变量访问】(" + globalVars.size() + " 次)");
            for (String g : globalVars) {
                log("  " + g);
            }
        }
        
        if (!functionDefs.isEmpty()) {
            log("\n【函数定义】(" + functionDefs.size() + " 个)");
            for (String f : functionDefs) {
                log("  " + f);
            }
        }
        
        if (!functionCalls.isEmpty()) {
            log("\n【函数调用】(" + functionCalls.size() + " 次)");
            for (String f : functionCalls) {
                log("  " + f);
            }
        }
        
        if (!tableFields.isEmpty()) {
            log("\n【表字段操作】(" + tableFields.size() + " 次)");
            for (String t : tableFields) {
                log("  " + t);
            }
        }
    }
    
    private static void log(String msg) {
        System.out.println(msg);
        if (writer != null) {
            writer.println(msg);
            writer.flush();
        }
    }
}
