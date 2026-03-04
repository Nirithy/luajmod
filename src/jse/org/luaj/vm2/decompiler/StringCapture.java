package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * 运行时字符串捕获器
 * 通过Hook Lua运行时来捕获解密后的字符串
 */
public class StringCapture {

    private static PrintWriter writer;
    private static Set<String> capturedStrings = new LinkedHashSet<>();
    private static Set<String> chineseStrings = new LinkedHashSet<>();
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]+");
    private static final Pattern USEFUL_PATTERN = Pattern.compile("[\u4e00-\u9fa5]{2,}|[a-zA-Z]{3,}");

    /**
     * 主入口
     */
    public static void main(String[] args) {
        System.out.println("========== LuaJ 运行时字符串捕获器 ==========\n");
        
        if (args.length == 0) {
            System.out.println("用法: java StringCapture <luac文件> [输出文件]");
            return;
        }
        
        String filePath = args[0];
        String outputPath = args.length > 1 ? args[1] : filePath + ".strings.txt";
        
        System.out.println("分析文件: " + filePath);
        System.out.println("输出文件: " + outputPath);
        
        try {
            writer = new PrintWriter(new FileWriter(outputPath), true);
            
            Globals globals = createHookedGlobals();
            
            InputStream is = new FileInputStream(filePath);
            Prototype p = LoadState.undump(is, filePath);
            is.close();
            
            if (p == null) {
                log("无法加载文件");
                return;
            }
            
            log("开始执行并捕获字符串...\n");
            
            try {
                LuaClosure closure = new LuaClosure(p, globals);
                closure.call();
            } catch (Exception e) {
                log("执行出错(可能正常): " + e.getMessage());
            }
            
            log("\n========== 捕获完成 ==========\n");
            printResults();
            
            writer.close();
            System.out.println("结果已保存到: " + outputPath);
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 创建带有Hook的全局环境
     */
    private static Globals createHookedGlobals() {
        Globals globals = JsePlatform.standardGlobals();
        
        LuaTable stringLib = (LuaTable) globals.get("string");
        if (stringLib != null) {
            hookFunction(stringLib, "char", new HookedChar());
            hookFunction(stringLib, "sub", new HookedSub());
            hookFunction(stringLib, "format", new HookedFormat());
            hookFunction(stringLib, "rep", new HookedRep());
            hookFunction(stringLib, "reverse", new HookedReverse());
            hookFunction(stringLib, "lower", new HookedLower());
            hookFunction(stringLib, "upper", new HookedUpper());
        }
        
        LuaTable tableLib = (LuaTable) globals.get("table");
        if (tableLib != null) {
            hookFunction(tableLib, "concat", new HookedConcat());
        }
        
        globals.set("print", new HookedPrint());
        globals.set("tostring", new HookedToString());
        
        return globals;
    }
    
    private static void hookFunction(LuaTable lib, String name, VarArgFunction hook) {
        lib.set(name, hook);
    }
    
    /**
     * 捕获并记录字符串
     */
    private static void captureString(String s) {
        if (s == null || s.isEmpty()) return;
        if (s.length() < 2) return;
        if (s.matches("^[\\x00-\\x1F\\x7F]+$")) return;
        
        if (USEFUL_PATTERN.matcher(s).find()) {
            capturedStrings.add(s);
            if (CHINESE_PATTERN.matcher(s).find()) {
                chineseStrings.add(s);
            }
        }
    }
    
    private static void captureLuaString(LuaValue v) {
        if (v != null && v.isstring()) {
            captureString(v.tojstring());
        }
    }
    
    /**
     * 打印结果
     */
    private static void printResults() {
        if (!chineseStrings.isEmpty()) {
            log("【中文字符串】(" + chineseStrings.size() + " 个)");
            for (String s : chineseStrings) {
                log("  " + s);
            }
            log("");
        }
        
        log("【所有有意义的字符串】(" + capturedStrings.size() + " 个)");
        for (String s : capturedStrings) {
            String prefix = CHINESE_PATTERN.matcher(s).find() ? "[中文] " : "";
            log("  " + prefix + truncate(s, 200));
        }
    }
    
    private static String truncate(String s, int maxLen) {
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen) + "...";
    }
    
    private static void log(String msg) {
        System.out.println(msg);
        if (writer != null) {
            writer.println(msg);
            writer.flush();
        }
    }
    
    static class HookedChar extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= args.narg(); i++) {
                int c = args.checkint(i);
                sb.append((char) c);
            }
            String result = sb.toString();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedSub extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String s = args.checkjstring(1);
            int start = args.checkint(2);
            int end = args.isnoneornil(3) ? s.length() : args.checkint(3);
            
            if (start < 0) start = s.length() + start + 1;
            if (end < 0) end = s.length() + end + 1;
            start = Math.max(1, start);
            end = Math.min(s.length(), end);
            
            String result = start <= end ? s.substring(start - 1, end) : "";
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedFormat extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String format = args.checkjstring(1);
            Object[] fmtArgs = new Object[args.narg() - 1];
            for (int i = 0; i < fmtArgs.length; i++) {
                LuaValue v = args.arg(i + 2);
                if (v.isnumber()) fmtArgs[i] = v.todouble();
                else fmtArgs[i] = v.tojstring();
            }
            try {
                String result = String.format(format, fmtArgs);
                captureString(result);
                return LuaValue.valueOf(result);
            } catch (Exception e) {
                return LuaValue.valueOf(format);
            }
        }
    }
    
    static class HookedRep extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String s = args.checkjstring(1);
            int n = args.checkint(2);
            String sep = args.isnoneornil(3) ? "" : args.checkjstring(3);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (i > 0) sb.append(sep);
                sb.append(s);
            }
            String result = sb.toString();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedReverse extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String s = args.checkjstring(1);
            String result = new StringBuilder(s).reverse().toString();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedLower extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String s = args.checkjstring(1);
            String result = s.toLowerCase();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedUpper extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            String s = args.checkjstring(1);
            String result = s.toUpperCase();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedConcat extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            LuaTable t = args.checktable(1);
            String sep = args.isnoneornil(2) ? "" : args.checkjstring(2);
            int i = args.isnoneornil(3) ? 1 : args.checkint(3);
            int j = args.isnoneornil(4) ? t.length() : args.checkint(4);
            
            StringBuilder sb = new StringBuilder();
            for (int k = i; k <= j; k++) {
                if (k > i) sb.append(sep);
                LuaValue v = t.get(k);
                sb.append(v.tojstring());
            }
            String result = sb.toString();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
    
    static class HookedPrint extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= args.narg(); i++) {
                if (i > 1) sb.append("\t");
                String s = args.arg(i).tojstring();
                sb.append(s);
                captureString(s);
            }
            System.out.println(sb.toString());
            return LuaValue.NIL;
        }
    }
    
    static class HookedToString extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            LuaValue v = args.arg(1);
            String result = v.tojstring();
            captureString(result);
            return LuaValue.valueOf(result);
        }
    }
}
