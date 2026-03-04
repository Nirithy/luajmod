package org.luaj.vm2.lib.jse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * GameGuardian模拟库 - JSE版本
 * 模拟gg库的核心功能，用于在PC环境中测试Lua脚本
 * 支持交互式输入返回值
 */
public class GgLib extends TwoArgFunction {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static List<LuaTable> results = new ArrayList<>();
    private static Map<Long, Long> memory = new HashMap<>();
    private static boolean visible = true;
    private static int posX = 0, posY = 0;
    private static int sizeX = 100, sizeY = 100;
    private static boolean interactiveMode = true;
    private static int currentRange = 0xFFFFFFFF;
    private static String targetPackage = "com.example.game";
    private static int targetPid = 12345;
    private static boolean bannerPrinted = false;
    
    public GgLib() {
        if (!bannerPrinted) {
            bannerPrinted = true;
            System.out.println("\n========================================");
            System.out.println("  GG模拟库已加载 (交互模式)");
            System.out.println("  输入 'auto' 使用自动模拟");
            System.out.println("  输入 'manual' 手动输入返回值");
            System.out.println("  输入 'help' 查看帮助");
            System.out.println("========================================\n");
        }
    }
    
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable gg = new LuaTable();
        
        // 常量定义
        gg.set("VISIBLE", LuaValue.valueOf(1));
        gg.set("TYPE_BYTE", LuaValue.valueOf(1));
        gg.set("TYPE_WORD", LuaValue.valueOf(2));
        gg.set("TYPE_DWORD", LuaValue.valueOf(4));
        gg.set("TYPE_XOR", LuaValue.valueOf(8));
        gg.set("TYPE_QWORD", LuaValue.valueOf(0x20));
        gg.set("TYPE_FLOAT", LuaValue.valueOf(16));
        gg.set("TYPE_DOUBLE", LuaValue.valueOf(0x40));
        gg.set("TYPE_AUTO", LuaValue.valueOf(0x7F));
        gg.set("SIGN_EQUAL", LuaValue.valueOf(0x20000000));
        gg.set("SIGN_NOT_EQUAL", LuaValue.valueOf(0x10000000));
        gg.set("SIGN_LESS_OR_EQUAL", LuaValue.valueOf(0x8000000));
        gg.set("SIGN_GREATER_OR_EQUAL", LuaValue.valueOf(0x4000000));
        
        // UI控制
        gg.set("setVisible", new setVisible());
        gg.set("setVisible_", new setVisible_());
        gg.set("setPosition", new setPosition());
        gg.set("setSize", new setSize());
        gg.set("show", new show());
        gg.set("hide", new hide());
        gg.set("isVisible", new isVisible());
        
        // 搜索功能
        gg.set("searchNumber", new searchNumber());
        gg.set("searchString", new searchString());
        gg.set("getResults", new getResults());
        gg.set("getResultsCount", new getResultsCount());
        gg.set("clearResults", new clearResults());
        gg.set("addListItems", new addListItems());
        gg.set("getValues", new getValues());
        gg.set("setValues", new setValues());
        gg.set("editAll", new editAll());
        gg.set("refineNumber", new refineNumber());
        gg.set("refineString", new refineString());
        
        // 对话框
        gg.set("alert", new alert());
        gg.set("toast", new toast());
        gg.set("choice", new choice());
        gg.set("multiChoice", new multiChoice());
        gg.set("input", new input());
        gg.set("prompt", new prompt());
        gg.set("confirm", new confirm());
        
        // 进程管理
        gg.set("getRanges", new getRanges());
        gg.set("setRanges", new setRanges());
        gg.set("getProcess", new getProcess());
        gg.set("setProcess", new setProcess());
        gg.set("getTargetPackage", new getTargetPackage());
        gg.set("getTargetInfo", new getTargetInfo());
        gg.set("getTargetProcess", new getTargetProcess());
        gg.set("getTargetPID", new getTargetPID());
        
        // 时间/速度
        gg.set("sleep", new sleep());
        gg.set("getTime", new getTime());
        gg.set("copyText", new copyText());
        gg.set("getText", new getText());
        gg.set("getSpeed", new getSpeed());
        gg.set("setSpeed", new setSpeed());
        
        // 冻结/监视
        gg.set("skipFrozenState", new skipFrozenState());
        gg.set("unfreezeAll", new unfreezeAll());
        gg.set("addWatch", new addWatch());
        gg.set("removeWatch", new removeWatch());
        gg.set("freeze", new freeze());
        gg.set("isFrozen", new isFrozen());
        
        // 文件操作
        gg.set("getFile", new getFile());
        gg.set("makeDir", new makeDir());
        gg.set("getFileList", new getFileList());
        gg.set("loadList", new loadList());
        gg.set("saveList", new saveList());
        gg.set("loadVariable", new loadVariable());
        gg.set("saveVariable", new saveVariable());
        
        // 字节操作
        gg.set("bytes", new bytes());
        gg.set("tostring", new tostring_());
        
        // 其他常用
        gg.set("print", new ggprint());
        gg.set("require", new ggrequire());
        
        // UI按钮
        gg.set("showUiButton", new showUiButton());
        gg.set("isClickedUiButton", new isClickedUiButton());
        gg.set("hideUiButton", new hideUiButton());
        
        env.set("gg", gg);
        if (!env.get("package").isnil()) {
            env.get("package").get("loaded").set("gg", gg);
        }
        
        return gg;
    }
    
    // ==================== 工具方法 ====================
    
    private static void log(String msg) {
        System.out.println("[GG] " + msg);
    }
    
    private static LuaValue askReturnValue(String funcName, String description) {
        if (!interactiveMode) {
            return LuaValue.NIL;
        }
        System.out.println("\n>>> gg." + funcName + "()");
        if (description != null) {
            System.out.println("    " + description);
        }
        System.out.print("    返回值 (输入值/table/nil/auto): ");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty() || input.equals("nil")) {
            return LuaValue.NIL;
        }
        if (input.equals("auto")) {
            return null; // 使用自动模拟
        }
        if (input.startsWith("table:") || input.startsWith("{")) {
            return parseTable(input);
        }
        if (input.startsWith("[")) {
            return parseArray(input);
        }
        
        // 尝试解析为数字或字符串
        try {
            if (input.contains(".")) {
                return LuaValue.valueOf(Double.parseDouble(input));
            } else {
                return LuaValue.valueOf(Long.parseLong(input));
            }
        } catch (NumberFormatException e) {
            return LuaValue.valueOf(input);
        }
    }
    
    private static LuaTable parseTable(String input) {
        LuaTable table = new LuaTable();
        if (input.startsWith("{") && input.endsWith("}")) {
            String content = input.substring(1, input.length() - 1);
            String[] pairs = content.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    String key = kv[0].trim();
                    String value = kv[1].trim();
                    setTableValue(table, key, value);
                }
            }
        }
        return table;
    }
    
    private static LuaTable parseArray(String input) {
        LuaTable table = new LuaTable();
        if (input.startsWith("[") && input.endsWith("]")) {
            String content = input.substring(1, input.length() - 1);
            String[] items = content.split(",");
            for (int i = 0; i < items.length; i++) {
                String value = items[i].trim();
                if (value.startsWith("\"") || value.startsWith("'")) {
                    table.set(i + 1, LuaValue.valueOf(value.replaceAll("^['\"]|['\"]$", "")));
                } else {
                    try {
                        table.set(i + 1, LuaValue.valueOf(Long.parseLong(value)));
                    } catch (NumberFormatException e) {
                        table.set(i + 1, LuaValue.valueOf(value));
                    }
                }
            }
        }
        return table;
    }
    
    private static void setTableValue(LuaTable table, String key, String value) {
        LuaValue k = key.matches("\\d+") ? LuaValue.valueOf(Integer.parseInt(key)) : LuaValue.valueOf(key);
        if (value.startsWith("\"") || value.startsWith("'")) {
            table.set(k, LuaValue.valueOf(value.replaceAll("^['\"]|['\"]$", "")));
        } else if (value.equals("true")) {
            table.set(k, LuaValue.TRUE);
        } else if (value.equals("false")) {
            table.set(k, LuaValue.FALSE);
        } else {
            try {
                if (value.contains(".")) {
                    table.set(k, LuaValue.valueOf(Double.parseDouble(value)));
                } else {
                    table.set(k, LuaValue.valueOf(Long.parseLong(value)));
                }
            } catch (NumberFormatException e) {
                table.set(k, LuaValue.valueOf(value));
            }
        }
    }
    
    private static LuaTable createResultItem(long address, Object value, int flags) {
        LuaTable item = new LuaTable();
        item.set("address", LuaValue.valueOf(address));
        item.set("value", LuaValue.valueOf(String.valueOf(value)));
        item.set("flags", LuaValue.valueOf(flags));
        return item;
    }
    
    // ==================== UI控制 ====================
    
    static class setVisible extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            visible = args.checkboolean(1);
            log("setVisible(" + visible + ")");
            return LuaValue.NIL;
        }
    }
    
    static class setVisible_ extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            visible = args.checkboolean(1);
            log("setVisible_(" + visible + ")");
            return LuaValue.NIL;
        }
    }
    
    static class setPosition extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            posX = args.checkint(1);
            posY = args.checkint(2);
            log("setPosition(" + posX + ", " + posY + ")");
            return LuaValue.NIL;
        }
    }
    
    static class setSize extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            sizeX = args.checkint(1);
            sizeY = args.checkint(2);
            log("setSize(" + sizeX + ", " + sizeY + ")");
            return LuaValue.NIL;
        }
    }
    
    static class show extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            visible = true;
            log("show() - UI已显示");
            return LuaValue.NIL;
        }
    }
    
    static class hide extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            visible = false;
            log("hide() - UI已隐藏");
            return LuaValue.NIL;
        }
    }
    
    static class isVisible extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(visible);
        }
    }
    
    // ==================== 搜索功能 ====================
    
    static class searchNumber extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String searchValue = args.optjstring(1, "0");
            int type = args.optint(2, 4);
            boolean encrypted = args.optboolean(3, false);
            int sign = args.optint(4, 0);
            
            log("searchNumber(\"" + searchValue + "\", " + type + ", " + encrypted + ", " + sign + ")");
            
            LuaValue custom = askReturnValue("searchNumber", "返回找到的结果数量");
            if (custom != null) {
                return custom;
            }
            
            // 自动模拟
            results.clear();
            int count = random.nextInt(10) + 1;
            for (int i = 0; i < count; i++) {
                LuaTable result = new LuaTable();
                result.set("address", LuaValue.valueOf(0x12340000L + i * 4));
                result.set("flags", LuaValue.valueOf(type));
                result.set("value", LuaValue.valueOf(searchValue));
                results.add(result);
            }
            log("自动返回: " + count + " 个结果");
            return LuaValue.valueOf(count);
        }
    }
    
    static class searchString extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String searchValue = args.checkjstring(1);
            log("searchString(\"" + searchValue + "\")");
            
            LuaValue custom = askReturnValue("searchString", "返回找到的结果数量");
            if (custom != null) {
                return custom;
            }
            
            results.clear();
            int count = random.nextInt(5) + 1;
            for (int i = 0; i < count; i++) {
                LuaTable result = new LuaTable();
                result.set("address", LuaValue.valueOf(0xABC00000L + i * 0x100));
                result.set("flags", LuaValue.valueOf(0));
                result.set("value", LuaValue.valueOf(searchValue));
                results.add(result);
            }
            return LuaValue.valueOf(count);
        }
    }
    
    static class refineNumber extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String searchValue = args.optjstring(1, "0");
            int type = args.optint(2, 4);
            log("refineNumber(\"" + searchValue + "\", " + type + ")");
            
            LuaValue custom = askReturnValue("refineNumber", "返回剩余结果数量");
            if (custom != null) {
                return custom;
            }
            
            // 过滤一部分结果
            int newCount = Math.max(1, results.size() / 2);
            while (results.size() > newCount) {
                results.remove(random.nextInt(results.size()));
            }
            return LuaValue.valueOf(results.size());
        }
    }
    
    static class refineString extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String searchValue = args.checkjstring(1);
            log("refineString(\"" + searchValue + "\")");
            return LuaValue.valueOf(results.size());
        }
    }
    
    static class getResults extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            int max = args.optint(1, results.size());
            log("getResults(" + max + ")");
            
            LuaValue custom = askReturnValue("getResults", "返回结果表");
            if (custom != null) {
                return custom;
            }
            
            LuaTable resultTable = new LuaTable();
            int count = Math.min(max, results.size());
            for (int i = 0; i < count; i++) {
                resultTable.set(i + 1, results.get(i));
            }
            return resultTable;
        }
    }
    
    static class getResultsCount extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(results.size());
        }
    }
    
    static class clearResults extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            results.clear();
            log("clearResults()");
            return LuaValue.NIL;
        }
    }
    
    static class addListItems extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable items = args.checktable(1);
            log("addListItems() - 添加 " + items.length() + " 个项目");
            return LuaValue.TRUE;
        }
    }
    
    static class getValues extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable items = args.checktable(1);
            log("getValues() - 获取 " + items.length() + " 个值");
            
            LuaValue custom = askReturnValue("getValues", "返回修改后的表");
            if (custom != null) {
                return custom;
            }
            
            return items;
        }
    }
    
    static class setValues extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable items = args.checktable(1);
            log("setValues() - 设置 " + items.length() + " 个值");
            return LuaValue.TRUE;
        }
    }
    
    static class editAll extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String value = args.checkjstring(1);
            int type = args.optint(2, 4);
            log("editAll(\"" + value + "\", " + type + ")");
            return LuaValue.valueOf(results.size());
        }
    }
    
    // ==================== 对话框 ====================
    
    static class alert extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String message = args.optjstring(1, "");
            String title = args.optjstring(2, "Alert");
            boolean cancelable = args.optboolean(3, true);
            
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║ " + title);
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ " + message);
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("按回车键继续...");
            scanner.nextLine();
            
            return LuaValue.valueOf(true);
        }
    }
    
    static class toast extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String message = args.checkjstring(1);
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│ [TOAST] " + message);
            System.out.println("└─────────────────────────────────────┘");
            return LuaValue.NIL;
        }
    }
    
    static class choice extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable items = args.checktable(1);
            LuaValue selected = args.optvalue(2, LuaValue.NIL);
            String title = args.optjstring(3, "");
            
            System.out.println("\n╔══════════════════════════════════════╗");
            if (!title.isEmpty()) {
                System.out.println("║ " + title);
                System.out.println("╠──────────────────────────────────────╣");
            }
            System.out.println("║ 请选择:");
            System.out.println("╠──────────────────────────────────────╣");
            
            LuaValue k = LuaValue.NIL;
            int displayIndex = 1;
            Map<Integer, LuaValue> indexMap = new HashMap<>();
            int selectedIndex = -1;
            
            while (true) {
                Varargs n = items.next(k);
                if ((k = n.arg1()).isnil()) break;
                LuaValue v = n.arg(2);
                
                String marker = "  ";
                if (!selected.isnil() && k.eq_b(selected)) {
                    marker = "> ";
                    selectedIndex = displayIndex;
                }
                
                System.out.println("║ " + marker + displayIndex + ". " + v.tojstring());
                indexMap.put(displayIndex, k);
                displayIndex++;
            }
            
            System.out.println("╠──────────────────────────────────────╣");
            System.out.println("║   0. 取消");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请选择 [0-" + (displayIndex - 1) + "]: ");
            
            String input = scanner.nextLine().trim();
            
            try {
                int choice = Integer.parseInt(input);
                if (choice == 0) {
                    return LuaValue.valueOf(-1);
                }
                if (choice >= 1 && choice < displayIndex) {
                    return indexMap.get(choice);
                }
            } catch (NumberFormatException e) {
            }
            
            return LuaValue.valueOf(-1);
        }
    }
    
    static class multiChoice extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable items = args.checktable(1);
            LuaTable selected = args.opttable(2, null);
            String title = args.optjstring(3, "");
            
            System.out.println("\n╔══════════════════════════════════════╗");
            if (!title.isEmpty()) {
                System.out.println("║ " + title);
                System.out.println("╠──────────────────────────────────────╣");
            }
            System.out.println("║ 多选 (用逗号分隔多个选项):");
            System.out.println("╠──────────────────────────────────────╣");
            
            LuaValue k = LuaValue.NIL;
            int displayIndex = 1;
            Map<Integer, LuaValue> indexMap = new HashMap<>();
            Map<LuaValue, Integer> keyToIndex = new HashMap<>();
            
            while (true) {
                Varargs n = items.next(k);
                if ((k = n.arg1()).isnil()) break;
                LuaValue v = n.arg(2);
                
                String marker = "  ";
                if (selected != null) {
                    LuaValue sel = selected.get(k);
                    if (!sel.isnil() && sel.toboolean()) {
                        marker = "> ";
                    }
                }
                
                System.out.println("║ " + marker + displayIndex + ". " + v.tojstring());
                indexMap.put(displayIndex, k);
                keyToIndex.put(k, displayIndex);
                displayIndex++;
            }
            
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请选择 (如: 1,3,5 或 0 取消): ");
            
            String input = scanner.nextLine().trim();
            
            if (input.equals("0") || input.isEmpty()) {
                return LuaValue.NIL;
            }
            
            // 解析选择
            LuaTable result = new LuaTable();
            String[] choices = input.split(",");
            for (String choice : choices) {
                try {
                    int idx = Integer.parseInt(choice.trim());
                    if (idx >= 1 && idx < displayIndex) {
                        LuaValue key = indexMap.get(idx);
                        result.set(key, LuaValue.TRUE);
                    }
                } catch (NumberFormatException e) {
                }
            }
            
            return result;
        }
    }
    
    static class input extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String title = args.optjstring(1, "Input");
            String message = args.optjstring(2, "");
            String defaultValue = args.optjstring(3, "");
            
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║ " + title);
            if (!message.isEmpty()) {
                System.out.println("║ " + message);
            }
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请输入 [" + defaultValue + "]: ");
            
            String input = scanner.nextLine().trim();
            return LuaValue.valueOf(input.isEmpty() ? defaultValue : input);
        }
    }
    
    static class prompt extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable labels = args.checktable(1);
            LuaTable defaults = args.opttable(2, null);
            LuaTable types = args.opttable(3, null);
            
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║ 输入");
            System.out.println("╠──────────────────────────────────────╣");
            
            LuaTable result = new LuaTable();
            LuaValue k = LuaValue.NIL;
            int index = 1;
            
            while (true) {
                Varargs n = labels.next(k);
                if ((k = n.arg1()).isnil()) break;
                
                LuaValue label = n.arg(2);
                String defaultVal = "";
                String type = "text";
                
                if (defaults != null) {
                    LuaValue def = defaults.get(k);
                    if (!def.isnil()) {
                        defaultVal = def.tojstring();
                    }
                }
                
                if (types != null) {
                    LuaValue t = types.get(k);
                    if (!t.isnil()) {
                        type = t.tojstring();
                    }
                }
                
                System.out.println("║ " + label.tojstring() + ":");
                if (!defaultVal.isEmpty()) {
                    System.out.println("║   默认: " + defaultVal);
                }
                System.out.println("║   类型: " + type);
                System.out.print("║ 输入: ");
                
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    input = defaultVal;
                }
                
                result.set(k, LuaValue.valueOf(input));
                index++;
            }
            
            System.out.println("╚══════════════════════════════════════╝");
            
            return result;
        }
    }
    
    static class confirm extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String message = args.optjstring(1, "确认?");
            String title = args.optjstring(2, "Confirm");
            
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║ " + title);
            System.out.println("╠──────────────────────────────────────╣");
            System.out.println("║ " + message);
            System.out.println("╠──────────────────────────────────────╣");
            System.out.println("║   [Y] 是    [N] 否");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("确认? [y/n]: ");
            
            String input = scanner.nextLine().trim().toLowerCase();
            return LuaValue.valueOf(input.equals("y") || input.equals("yes"));
        }
    }
    
    // ==================== 进程管理 ====================
    
    static class getRanges extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(currentRange);
        }
    }
    
    static class setRanges extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            currentRange = args.checkint(1);
            log("setRanges(" + currentRange + ")");
            return LuaValue.NIL;
        }
    }
    
    static class getProcess extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable info = new LuaTable();
            info.set("processName", LuaValue.valueOf(targetPackage));
            info.set("processId", LuaValue.valueOf(targetPid));
            return info;
        }
    }
    
    static class setProcess extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String packageName = args.optjstring(1, "");
            log("setProcess(\"" + packageName + "\")");
            if (!packageName.isEmpty()) {
                targetPackage = packageName;
            }
            return LuaValue.TRUE;
        }
    }
    
    static class getTargetPackage extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(targetPackage);
        }
    }
    
    static class getTargetInfo extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable info = new LuaTable();
            info.set("packageName", LuaValue.valueOf(targetPackage));
            info.set("processName", LuaValue.valueOf(targetPackage));
            info.set("pid", LuaValue.valueOf(targetPid));
            info.set("uid", LuaValue.valueOf(10000));
            return info;
        }
    }
    
    static class getTargetProcess extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(targetPackage);
        }
    }
    
    static class getTargetPID extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(targetPid);
        }
    }
    
    // ==================== 时间/速度 ====================
    
    static class sleep extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            long millis = (long)(args.checkdouble(1) * 1000);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
            }
            return LuaValue.NIL;
        }
    }
    
    static class getTime extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(System.currentTimeMillis());
        }
    }
    
    static class copyText extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String text = args.checkjstring(1);
            log("copyText(\"" + text + "\")");
            return LuaValue.TRUE;
        }
    }
    
    static class getText extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf("");
        }
    }
    
    static class getSpeed extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.valueOf(1.0);
        }
    }
    
    static class setSpeed extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            double speed = args.checkdouble(1);
            log("setSpeed(" + speed + ")");
            return LuaValue.TRUE;
        }
    }
    
    // ==================== 冻结/监视 ====================
    
    static class skipFrozenState extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.TRUE;
        }
    }
    
    static class unfreezeAll extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            log("unfreezeAll()");
            return LuaValue.TRUE;
        }
    }
    
    static class addWatch extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            long address = args.checklong(1);
            log("addWatch(0x" + Long.toHexString(address) + ")");
            return LuaValue.TRUE;
        }
    }
    
    static class removeWatch extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            long address = args.checklong(1);
            log("removeWatch(0x" + Long.toHexString(address) + ")");
            return LuaValue.TRUE;
        }
    }
    
    static class freeze extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            long address = args.checklong(1);
            String value = args.optjstring(2, "0");
            int type = args.optint(3, 4);
            log("freeze(0x" + Long.toHexString(address) + ", \"" + value + "\", " + type + ")");
            return LuaValue.TRUE;
        }
    }
    
    static class isFrozen extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaValue.FALSE;
        }
    }
    
    // ==================== 文件操作 ====================
    
    static class getFile extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String path = args.checkjstring(1);
            log("getFile(\"" + path + "\")");
            return LuaValue.valueOf(path);
        }
    }
    
    static class makeDir extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String path = args.checkjstring(1);
            log("makeDir(\"" + path + "\")");
            return LuaValue.TRUE;
        }
    }
    
    static class getFileList extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String path = args.checkjstring(1);
            LuaTable list = new LuaTable();
            list.set(1, LuaValue.valueOf(path + "/file1.lua"));
            list.set(2, LuaValue.valueOf(path + "/file2.lua"));
            return list;
        }
    }
    
    static class loadList extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String path = args.optjstring(1, "");
            log("loadList(\"" + path + "\")");
            
            LuaValue custom = askReturnValue("loadList", "返回加载的列表");
            if (custom != null) {
                return custom;
            }
            
            LuaTable list = new LuaTable();
            list.set(1, createResultItem(0x12340000, 123, 4));
            return list;
        }
    }
    
    static class saveList extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable list = args.checktable(1);
            String path = args.optjstring(2, "");
            log("saveList(table, \"" + path + "\") - 保存 " + list.length() + " 项");
            return LuaValue.TRUE;
        }
    }
    
    static class loadVariable extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String path = args.optjstring(1, "");
            log("loadVariable(\"" + path + "\")");
            
            LuaValue custom = askReturnValue("loadVariable", "返回加载的变量");
            if (custom != null) {
                return custom;
            }
            
            return new LuaTable();
        }
    }
    
    static class saveVariable extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaValue value = args.checkvalue(1);
            String path = args.optjstring(2, "");
            log("saveVariable(value, \"" + path + "\")");
            return LuaValue.TRUE;
        }
    }
    
    // ==================== 字节操作 ====================
    
    static class bytes extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String text = args.checkjstring(1);
            String encoding = args.optjstring(2, "UTF-8");
            try {
                byte[] byteArr = text.getBytes(encoding);
                LuaTable result = new LuaTable();
                for (int i = 0; i < byteArr.length; i++) {
                    result.set(i + 1, LuaValue.valueOf(byteArr[i] & 0xFF));
                }
                log("bytes(\"" + text + "\", \"" + encoding + "\") -> " + byteArr.length + " bytes");
                return result;
            } catch (Exception e) {
                return LuaValue.NIL;
            }
        }
    }
    
    static class tostring_ extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable byteTable = args.checktable(1);
            String encoding = args.optjstring(2, "UTF-8");
            try {
                int len = byteTable.length();
                byte[] byteArr = new byte[len];
                for (int i = 0; i < len; i++) {
                    byteArr[i] = (byte) byteTable.get(i + 1).toint();
                }
                String result = new String(byteArr, encoding);
                log("tostring(table, \"" + encoding + "\") -> \"" + result + "\"");
                return LuaValue.valueOf(result);
            } catch (Exception e) {
                return LuaValue.NIL;
            }
        }
    }
    
    // ==================== 其他 ====================
    
    static class ggprint extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= args.narg(); i++) {
                if (i > 1) sb.append("\t");
                sb.append(args.arg(i).tojstring());
            }
            System.out.println("[GG-PRINT] " + sb.toString());
            return LuaValue.NIL;
        }
    }
    
    static class ggrequire extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            String module = args.checkjstring(1);
            log("require(\"" + module + "\")");
            return new LuaTable();
        }
    }
    
    // ==================== UI按钮 ====================
    
    private static boolean uiButtonVisible = false;
    private static boolean uiButtonClicked = false;
    
    static class showUiButton extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            uiButtonVisible = true;
            if (!uiButtonClicked) {
                System.out.print(".");
            }
            return LuaValue.NIL;
        }
    }
    
    static class isClickedUiButton extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            if (!uiButtonClicked) {
                System.out.println("\n┌─────────────────────────────────────┐");
                System.out.println("│ [GG悬浮按钮] 点击此处模拟按钮点击    │");
                System.out.println("└─────────────────────────────────────┘");
                System.out.print("按回车点击按钮，或输入 'q' 退出: ");
                try {
                    String input = scanner.nextLine().trim();
                    if (input.equals("q") || input.equals("quit")) {
                        System.out.println("[GG] 退出脚本");
                        System.exit(0);
                    }
                    // 回车或其他输入都视为点击
                    uiButtonClicked = true;
                    System.out.println("[GG] 按钮被点击!");
                } catch (Exception e) {
                }
            }
            
            boolean clicked = uiButtonClicked;
            if (clicked) {
                uiButtonClicked = false;  // 重置状态
            }
            return LuaValue.valueOf(clicked);
        }
    }
    
    static class hideUiButton extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            uiButtonVisible = false;
            log("hideUiButton()");
            return LuaValue.NIL;
        }
    }
}
