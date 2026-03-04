package android.ext;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import java.util.List;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.getProcessInfo extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        String s;
        List list0 = Script.processList;
        ProcessInfo processList$ProcessInfo0 = list0 == null ? MainService.instance.processInfo : null;
        if(varargs0.isstring(1)) {
            for(Object object0: list0) {
                ProcessInfo processList$ProcessInfo1 = (ProcessInfo)object0;
                if(processList$ProcessInfo1.cmdline.equals(varargs0.checkjstring(1))) {
                    processList$ProcessInfo0 = processList$ProcessInfo1;
                    break;
                }
                if(false) {
                    break;
                }
            }
        }
        if(processList$ProcessInfo0 != null) {
            try {
                s = processList$ProcessInfo0.packageName;
                PackageInfo packageInfo0 = Tools.getPackageInfo(s, 1);
                if(packageInfo0 == null) {
                    return LuaValue.NIL;
                }
                LuaTable luaTable0 = new LuaTable();
                luaTable0.rawset("packageName", processList$ProcessInfo0.packageName);
                luaTable0.rawset("cmdLine", processList$ProcessInfo0.cmdline);
                luaTable0.rawset("name", processList$ProcessInfo0.name);
                luaTable0.rawset("nativeLibraryDir", processList$ProcessInfo0.libsPath);
                luaTable0.rawset("pid", processList$ProcessInfo0.pid);
                luaTable0.rawset("uid", processList$ProcessInfo0.uid);
                luaTable0.rawset("x64", (processList$ProcessInfo0.x64 ? LuaValue.TRUE : LuaValue.FALSE));
                luaTable0.rawset("RSS", processList$ProcessInfo0.rss);
                luaTable0.rawset("colorName", ProcessInfo.colorName);
                luaTable0.rawset("colorPid", ProcessInfo.colorPid);
                luaTable0.rawset("colorSize", ProcessInfo.colorSize);
                luaTable0.rawset("dump", processList$ProcessInfo0.dump());
                luaTable0.rawset("trace", processList$ProcessInfo0.getTrace());
                luaTable0.rawset("tracer", processList$ProcessInfo0.getTracer());
                luaTable0.rawset("isgame", LuaValue.valueOf(processList$ProcessInfo0.isGame));
                luaTable0.rawset("issystem", LuaValue.valueOf(processList$ProcessInfo0.isSystem));
                luaTable0.rawset("main", LuaValue.valueOf(processList$ProcessInfo0.main));
                luaTable0.rawset("order", processList$ProcessInfo0.order);
                luaTable0.rawset("pkgUid", processList$ProcessInfo0.pkgUid);
                luaTable0.rawset("weight", ((double)processList$ProcessInfo0.weight));
                luaTable0.rawset("firstInstallTime", ((double)packageInfo0.firstInstallTime));
                luaTable0.rawset("lastUpdateTime", ((double)packageInfo0.lastUpdateTime));
                if(packageInfo0.packageName != null) {
                    luaTable0.rawset("packageName", packageInfo0.packageName);
                }
                if(packageInfo0.sharedUserId != null) {
                    luaTable0.rawset("sharedUserId", packageInfo0.sharedUserId);
                }
                luaTable0.rawset("sharedUserLabel", packageInfo0.sharedUserLabel);
                luaTable0.rawset("versionCode", packageInfo0.versionCode);
                if(packageInfo0.versionName != null) {
                    luaTable0.rawset("versionName", packageInfo0.versionName);
                }
                PackageManager packageManager0 = Tools.getPackageManager();
                if(packageInfo0.activities == null || packageManager0 == null) {
                    goto label_55;
                }
                LuaTable luaTable1 = new LuaTable();
                ActivityInfo[] arr_activityInfo = packageInfo0.activities;
                int v1 = 1;
                for(int v = 0; true; ++v) {
                    if(v >= arr_activityInfo.length) {
                        luaTable0.rawset("activities", luaTable1);
                    label_55:
                        if(packageManager0 != null) {
                            luaTable0.rawset("installer", packageManager0.getInstallerPackageName(s));
                            luaTable0.rawset("enabledSetting", packageManager0.getApplicationEnabledSetting(s));
                        }
                        ApplicationInfo applicationInfo0 = packageInfo0.applicationInfo;
                        if(applicationInfo0 != null) {
                            if(applicationInfo0.backupAgentName != null) {
                                luaTable0.rawset("backupAgentName", applicationInfo0.backupAgentName);
                            }
                            if(applicationInfo0.className != null) {
                                luaTable0.rawset("className", applicationInfo0.className);
                            }
                            if(applicationInfo0.dataDir != null) {
                                luaTable0.rawset("dataDir", applicationInfo0.dataDir);
                            }
                            luaTable0.rawset("descriptionRes", applicationInfo0.descriptionRes);
                            luaTable0.rawset("flags", applicationInfo0.flags);
                            luaTable0.rawset("icon", applicationInfo0.icon);
                            luaTable0.rawset("labelRes", applicationInfo0.labelRes);
                            luaTable0.rawset("logo", applicationInfo0.logo);
                            try {
                                if(applicationInfo0.appComponentFactory != null) {
                                    luaTable0.rawset("appComponentFactory", applicationInfo0.appComponentFactory);
                                }
                                if(applicationInfo0.deviceProtectedDataDir != null) {
                                    luaTable0.rawset("deviceProtectedDataDir", applicationInfo0.deviceProtectedDataDir);
                                }
                            }
                            catch(Exception unused_ex) {
                            }
                            if(applicationInfo0.manageSpaceActivityName != null) {
                                luaTable0.rawset("manageSpaceActivityName", applicationInfo0.manageSpaceActivityName);
                            }
                            if(applicationInfo0.name != null) {
                                luaTable0.rawset("name", applicationInfo0.name);
                            }
                            if(applicationInfo0.nativeLibraryDir != null) {
                                luaTable0.rawset("nativeLibraryDir", applicationInfo0.nativeLibraryDir);
                            }
                            if(applicationInfo0.packageName != null) {
                                luaTable0.rawset("packageName", applicationInfo0.packageName);
                            }
                            if(applicationInfo0.permission != null) {
                                luaTable0.rawset("permission", applicationInfo0.permission);
                            }
                            if(applicationInfo0.processName != null) {
                                luaTable0.rawset("processName", applicationInfo0.processName);
                            }
                            if(applicationInfo0.publicSourceDir != null) {
                                luaTable0.rawset("publicSourceDir", applicationInfo0.publicSourceDir);
                            }
                            if(applicationInfo0.sourceDir != null) {
                                luaTable0.rawset("sourceDir", applicationInfo0.sourceDir);
                            }
                            luaTable0.rawset("targetSdkVersion", 26);
                            if(applicationInfo0.taskAffinity != null) {
                                luaTable0.rawset("taskAffinity", applicationInfo0.taskAffinity);
                            }
                            luaTable0.rawset("theme", applicationInfo0.theme);
                            luaTable0.rawset("uid", applicationInfo0.uid);
                            String s1 = Tools.getApplicationLabel(applicationInfo0);
                            if(s1 != null) {
                                luaTable0.rawset("label", s1);
                            }
                        }
                        return luaTable0;
                    }
                    ActivityInfo activityInfo0 = arr_activityInfo[v];
                    if(activityInfo0 != null) {
                        try {
                            LuaTable luaTable2 = new LuaTable();
                            if(activityInfo0.name != null) {
                                luaTable2.set("name", activityInfo0.name);
                            }
                            luaTable2.set("label", activityInfo0.loadLabel(packageManager0).toString());
                            try {
                                luaTable1.set(v1, luaTable2);
                            }
                            catch(Throwable throwable0) {
                                Log.w("Failed get activity info", throwable0);
                                ++v;
                            }
                            ++v1;
                        }
                        catch(Throwable unused_ex) {
                        }
                    }
                }
            }
            catch(PackageManager.NameNotFoundException packageManager$NameNotFoundException0) {
                Log.w(("Pkg not found: " + s), packageManager$NameNotFoundException0);
                return LuaValue.NIL;
            }
        }
        return LuaValue.NIL;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getProcessInfo([string cmdLine]) -> nil || table";
    }
}

