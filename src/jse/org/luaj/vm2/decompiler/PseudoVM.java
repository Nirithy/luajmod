package org.luaj.vm2.decompiler;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.StringInterceptor;
import org.luaj.vm2.lib.jse.JsePlatform;

public class PseudoVM {

    /**
     * 主入口
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java org.luaj.vm2.decompiler.PseudoVM <luac文件> [输出文件]");
            System.exit(1);
        }

        String luacFile = args[0];
        String outputFile = args.length > 1 ? args[1] : luacFile + ".strings.txt";
        
        System.out.println("分析文件: " + luacFile);
        System.out.println("输出文件: " + outputFile);
        
        StringInterceptor.enable(outputFile);
        
        try {
            Globals globals = JsePlatform.standardGlobals();
            
            InputStream is = new FileInputStream(luacFile);
            Prototype p = LoadState.undump(is, luacFile);
            is.close();
            
            if (p == null) {
                System.err.println("无法加载文件");
                StringInterceptor.disable();
                return;
            }
            
            analyzePrototype(p, 0);
            
            StringInterceptor.disable();
            System.out.println("分析完成，结果已保存到: " + outputFile);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 递归分析Prototype及其子函数
     */
    private static void analyzePrototype(Prototype p, int depth) {
        if (p == null || p.code == null) return;
        
        System.out.println("\n" + "  ".repeat(depth) + "分析函数 (深度 " + depth + ", 指令数: " + p.code.length);
        
        // 分析当前函数
        PseudoExecution.analyze(p, null);
        
        // 递归分析子函数
        if (p.p != null) {
            for (int i = 0; i < p.p.length; i++) {
                analyzePrototype(p.p[i], depth + 1);
            }
        }
    }
}
