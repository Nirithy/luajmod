package android.ext;

import luaj.LuaString;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.getConfig extends ApiFunction {
    private int[] ids;
    private String[] names;

    public Script.getConfig() {
        this.ids = new int[]{0x7F0B0081, 0x7F0B0082, 0x7F0B0084, 0x7F0B0085, 0x7F0B0087, 0x7F0B0088, 0x7F0B0089, 0x7F0B008A, 0x7F0B008B, 0x7F0B008C, 0x7F0B008D, 0x7F0B008E, 0x7F0B0090, 0x7F0B0091, 0x7F0B0092, 0x7F0B0093, 0x7F0B0094, 0x7F0B0095, 0x7F0B0096, 0x7F0B0097, 0x7F0B009A, 0x7F0B009B, 0x7F0B009C, 0x7F0B009D, 0x7F0B009E, 0x7F0B009F, 0x7F0B00A0, 0x7F0B00A1, 0x7F0B00A2, 0x7F0B00A3, 0x7F0B00A4, 0x7F0B00A5, 0x7F0B00A6, 0x7F0B00A8, 0x7F0B00A9, 0x7F0B00AE, 0x7F0B00AF, 0x7F0B00B1};  // id:config_ranges
        this.names = new String[]{"选择内存范围", "变速:拦截", "时间跳跃面板", "反随机数生成器", "对游戏隐藏", "旁路模式", "跳过内存区域", "快速冻结", "冻结间隔", "自动暂停游戏", "搜索助手", "保存列表更新间隔", "变速:排序并删除重复项", "变速和反随机数退出时重置", "检测游戏库架构", "使用内存缓冲", "内存访问", "深度读取", "系统调用模式", "waitpid模式", "在虚拟空间中使用root", "运行守护", "使用通知", "热键", "历史记录条数", "内外键盘", "允许输入法提示", "忽略未知字符", "状态栏缩进", "可见数据类型", "小列表项目", "黑色背景", "工具栏布局", "工具栏填充方式", "悬浮窗大小", "界面加速", "启用音效", "数字格式"};
    }

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        int v = 0;
        LuaValue luaValue0 = varargs0.arg(1);
        if(luaValue0.isnil()) {
            LuaTable luaTable0 = new LuaTable();
            while(v < this.ids.length) {
                LuaTable luaTable1 = new LuaTable();
                Option config$Option0 = Config.get(this.ids[v]);
                luaTable1.rawset("id", this.ids[v]);
                luaTable1.rawset("text", config$Option0.toString());
                luaTable1.rawset("value", config$Option0.value);
                luaTable0.rawset(v + 1, luaTable1);
                ++v;
            }
            return luaTable0;
        }
        if(luaValue0 instanceof LuaString) {
            String s = luaValue0.checkjstring();
            for(int v1 = 0; true; ++v1) {
                if(v1 >= this.ids.length) {
                    if(!s.equals("id") && !s.equals("name") && !s.equals("ls")) {
                        break;
                    }
                    LuaTable luaTable2 = new LuaTable();
                    while(v < this.ids.length) {
                        luaTable2.rawset(this.ids[v], this.names[v]);
                        ++v;
                    }
                    return luaTable2;
                }
                if(s.equals(this.names[v1])) {
                    Option config$Option1 = Config.get(this.ids[v1]);
                    return LuaValue.valueOf(("[状态]：" + config$Option1 + "\n[值]：" + config$Option1.value));
                }
            }
        }
        else if(luaValue0.isnumber()) {
            int v2 = luaValue0.checkint();
            while(v < this.ids.length) {
                if(v2 == this.ids[v]) {
                    Option config$Option2 = Config.get(v2);
                    return LuaValue.valueOf(("[状态]：" + config$Option2 + "\n[值]：" + config$Option2.value));
                }
                ++v;
            }
        }
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getConfig(id or name or nil) -> string || nil || table";
    }
}

