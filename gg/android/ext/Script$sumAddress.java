package android.ext;

import luaj.LuaTable;
import luaj.Varargs;

final class Script.sumAddress extends ApiFunction {
    private static int flags;
    private static long result;
    private static String textResult;
    private static String valueByte;
    private static String valueDouble;
    private static String valueDword;
    private static String valueFloat;
    private static String valueHex;
    private static String valueJava;
    private static String valueQword;
    private static String valueRHex;
    private static String valueString;
    private static String valueWord;
    private static String valueXor;

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 3;
    }

    public int getSize(long v, long v1, int v2, String s, int v3, int v4, int v5, boolean z, String s1) {
        String s2;
        if(!z && (((long)(v5 - 1)) & v) == 0L) {
            s2 = AddressItem.getStringDataTrim(v, v1, v3);
            if(v2 == 0) {
                v2 = v4;
            }
        }
        else {
            s2 = "-";
        }
        String s3 = String.valueOf(s2) + AddressItem.getShortName(v3) + ';';
        if(s1.equals("valueByte")) {
            Script.sumAddress.valueByte = s3;
        }
        if(s1.equals("valueDouble")) {
            Script.sumAddress.valueDouble = s3;
        }
        if(s1.equals("valueDword")) {
            Script.sumAddress.valueDword = s3;
        }
        if(s1.equals("valueFloat")) {
            Script.sumAddress.valueFloat = s3;
        }
        if(s1.equals("valueHex")) {
            Script.sumAddress.valueHex = s3;
        }
        if(s1.equals("valueJava")) {
            Script.sumAddress.valueJava = s3;
        }
        if(s1.equals("valueQword")) {
            Script.sumAddress.valueQword = s3;
        }
        if(s1.equals("valueRHex")) {
            Script.sumAddress.valueRHex = s3;
        }
        if(s1.equals("valueString")) {
            Script.sumAddress.valueString = s3;
        }
        if(s1.equals("valueWord")) {
            Script.sumAddress.valueWord = s3;
        }
        if(s1.equals("valueXor")) {
            Script.sumAddress.valueXor = s3;
        }
        return v2;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        if(Script.sumAddress.result != 0L) {
            Script.sumAddress.result = 0L;
        }
        try {
            Thread.sleep(200L);
        }
        catch(InterruptedException unused_ex) {
        }
        Script.sumAddress.result = varargs0.checklong(1) + varargs0.checklong(2);
        this.updateValue(Script.sumAddress.result, MainService.instance.mDaemonManager.getMemoryContent(Script.sumAddress.result, AddressItem.getTypeForAddress(Script.sumAddress.result, true)), false);
        Varargs varargs1 = new LuaTable();
        ((LuaTable)varargs1).rawset("address", "0");
        ((LuaTable)varargs1).rawset("h", Script.sumAddress.valueHex);
        ((LuaTable)varargs1).rawset("r", Script.sumAddress.valueRHex);
        ((LuaTable)varargs1).rawset("S", Script.sumAddress.valueString);
        ((LuaTable)varargs1).rawset("J", Script.sumAddress.valueJava);
        ((LuaTable)varargs1).rawset("D", Script.sumAddress.valueDword);
        ((LuaTable)varargs1).rawset("F", Script.sumAddress.valueFloat);
        ((LuaTable)varargs1).rawset("E", Script.sumAddress.valueDouble);
        ((LuaTable)varargs1).rawset("W", Script.sumAddress.valueWord);
        ((LuaTable)varargs1).rawset("B", Script.sumAddress.valueByte);
        ((LuaTable)varargs1).rawset("Q", Script.sumAddress.valueQword);
        ((LuaTable)varargs1).rawset("X", Script.sumAddress.valueXor);
        if(varargs0.checkboolean(3)) {
            try {
                Script.sumAddress.flags = varargs0.optint(4, 4);
                this.save();
            }
            catch(Exception unused_ex) {
            }
            return varargs1;
        }
        return varargs1;
    }

    public void save() {
        SavedListAdapter savedListAdapter0 = MainService.instance.savedList;
        SavedItem savedItem0 = new SavedItem(Script.sumAddress.result, 0L, Script.sumAddress.flags);
        if(!savedItem0.isPossibleItem()) {
            savedItem0.flags = AddressItem.getTypeForAddress(savedItem0.address, true);
        }
        SavedItem savedItem1 = savedListAdapter0.getItemByAddress(Script.sumAddress.result);
        if(savedItem1 == null || savedItem1.flags != savedItem0.flags) {
            savedListAdapter0.add(savedItem0);
            savedListAdapter0.reloadData();
        }
    }

    public void updateValue(long v, long v1, boolean z) {
        String s;
        int v2 = this.getSize(v, v1, this.getSize(v, v1, this.getSize(v, v1, this.getSize(v, v1, this.getSize(v, v1, this.getSize(v, v1, this.getSize(v, v1, 0, Script.sumAddress.valueDouble, 0x40, 8, 4, z, "valueDouble"), Script.sumAddress.valueQword, 0x20, 8, 4, z, "valueQword"), Script.sumAddress.valueFloat, 16, 4, 4, z, "valueFloat"), Script.sumAddress.valueDword, 4, 4, 4, z, "valueDword"), Script.sumAddress.valueXor, 8, 4, 4, z, "valueXor"), Script.sumAddress.valueWord, 2, 2, 2, z, "valueWord"), Script.sumAddress.valueByte, 1, 1, 1, z, "valueByte");
        int v3 = v2 == 0 ? 1 : v2;
        if(z) {
            s = "-h;";
        }
        else {
            if(v3 != 8) {
                v1 &= (long)((1 << v3 * 8) - 1);
            }
            s = ToolsLight.prefixLongHex(v3 * 2, v1) + "h;";
        }
        Script.sumAddress.valueHex = s;
        Script.sumAddress.valueRHex = z ? "-r;" : ToolsLight.prefixLongHex(v3 * 2, (v3 == 8 ? Long.reverseBytes(v1) >> 0x40 - v3 * 8 : Long.reverseBytes(v1) >> 0x40 - v3 * 8 & ((long)((1 << v3 * 8) - 1)))) + "r;";
        Script.sumAddress.valueString = z ? "-;" : "\'" + MemoryContentAdapter.longToString(v1, v3) + "\';";
        Script.sumAddress.valueJava = z ? "-;" : "\"" + MemoryContentAdapter.longToJava(v1, v3) + "\";";
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.sumAddress(string address,string offset,boolean save,int flags) -> string";
    }
}

