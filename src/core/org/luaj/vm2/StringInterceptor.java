package org.luaj.vm2;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 字符串拦截器 - 在底层拦截所有Lua字符串创建
 */
public class StringInterceptor {
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]+");
    private static final Pattern USEFUL_PATTERN = Pattern.compile("[\u4e00-\u9fa5]{2,}|[a-zA-Z]{4,}");
    
    private static Set<String> allStrings = new LinkedHashSet<>();
    private static Set<String> chineseStrings = new LinkedHashSet<>();
    private static PrintWriter writer;
    private static boolean enabled = false;
    private static boolean memoryMode = false;
    private static String outputPath = null;
    
    /**
     * 启用字符串拦截（文件模式）
     */
    public static void enable(String output) {
        outputPath = output;
        enabled = true;
        memoryMode = false;
        allStrings.clear();
        chineseStrings.clear();
        try {
            writer = new PrintWriter(new FileWriter(output), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 启用字符串拦截（内存模式）
     */
    public static void enableMemory() {
        enabled = true;
        memoryMode = true;
        allStrings.clear();
        chineseStrings.clear();
        writer = null;
    }
    
    /**
     * 禁用并输出结果
     */
    public static void disable() {
        enabled = false;
        memoryMode = false;
        if (writer != null) {
            printResults();
            writer.close();
            writer = null;
        }
    }
    
    /**
     * 字符串创建时调用
     */
    public static void onStringCreate(byte[] bytes, int offset, int length) {
        if (!enabled) return;
        if (length < 3) return;
        
        try {
            String s = LuaString.decodeAsUtf8(bytes, offset, length);
            if (s == null || s.isEmpty()) return;
            
            // 过滤纯控制字符
            boolean hasPrintable = false;
            for (char c : s.toCharArray()) {
                if (c > 31 && c < 127 || c > 127) {
                    hasPrintable = true;
                    break;
                }
            }
            if (!hasPrintable) return;
            
            // 检查是否有用
            if (USEFUL_PATTERN.matcher(s).find()) {
                allStrings.add(s);
                if (CHINESE_PATTERN.matcher(s).find()) {
                    chineseStrings.add(s);
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }
    
    private static void printResults() {
        if (writer == null) return;
        
        writer.println("========== 字符串拦截结果 ==========\n");
        
        if (!chineseStrings.isEmpty()) {
            writer.println("【中文字符串】(" + chineseStrings.size() + " 个)");
            for (String s : chineseStrings) {
                writer.println("  " + s);
            }
            writer.println();
        }
        
        writer.println("【所有有意义的字符串】(" + allStrings.size() + " 个)");
        for (String s : allStrings) {
            String prefix = CHINESE_PATTERN.matcher(s).find() ? "[中文] " : "";
            String truncated = s.length() > 300 ? s.substring(0, 300) + "..." : s;
            writer.println("  " + prefix + truncated);
        }
        
        writer.println("\n========== 拦截完成 ==========");
        writer.flush();
    }
    
    public static Set<String> getChineseStrings() {
        return chineseStrings;
    }
    
    public static Set<String> getAllStrings() {
        return allStrings;
    }
}
