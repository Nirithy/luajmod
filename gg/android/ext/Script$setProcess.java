package android.ext;

import java.util.List;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.setProcess extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        Varargs varargs1;
        AppDetector appDetector1;
        String s;
        AppDetector.processListWindow = Boolean.FALSE;
        MainService.instance.mAppDetector.detectApp(true);
        List list0 = Script.processList;
        LuaValue luaValue0 = varargs0.arg(1);
        if(list0 == null) {
            varargs1 = LuaValue.FALSE;
        }
        else {
            if(luaValue0.isint()) {
                ProcessInfo processList$ProcessInfo0 = (ProcessInfo)list0.get(luaValue0.checkint());
                AppDetector appDetector0 = MainService.instance.mAppDetector;
                try {
                    appDetector0.setAppInfo(processList$ProcessInfo0);
                }
                catch(Exception unused_ex) {
                }
                MainService.instance.updateStatusBar();
                appDetector0.appSelectByUser = true;
                s = processList$ProcessInfo0.cmdline;
            }
            else {
                String s1 = luaValue0.checkjstring();
                for(Object object0: list0) {
                    ProcessInfo processList$ProcessInfo1 = (ProcessInfo)object0;
                    if(processList$ProcessInfo1.cmdline.equals(s1)) {
                        try {
                            appDetector1 = MainService.instance.mAppDetector;
                            appDetector1.setAppInfo(processList$ProcessInfo1);
                        }
                        catch(Exception unused_ex) {
                        }
                        MainService.instance.updateStatusBar();
                        appDetector1.appSelectByUser = true;
                    }
                }
                s = s1;
            }
            try {
                ProcessInfo processList$ProcessInfo2 = MainService.instance.processInfo;
                if(processList$ProcessInfo2 == null) {
                    varargs1 = LuaValue.TRUE;
                }
                else if(processList$ProcessInfo2.cmdline.equals(s)) {
                    varargs1 = LuaValue.TRUE;
                }
                else {
                    varargs1 = LuaValue.FALSE;
                }
            }
            catch(Exception unused_ex) {
                varargs1 = LuaValue.FALSE;
            }
        }
        MainService.instance.mAppDetector.detectApp(true);
        MainService.instance.updateStatusBar();
        return varargs1;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.setProcess(int id or string process_nsme) -> boolean || string";
    }
}

