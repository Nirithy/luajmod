package android.ext;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.getAppInfo extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        int v = 1;
        String s = varargs0.checkjstring(1);
        try {
            PackageInfo packageInfo0 = Tools.getPackageInfo(s, 1);
            if(packageInfo0 == null) {
                return LuaValue.NIL;
            }
            LuaTable luaTable0 = new LuaTable();
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
                goto label_23;
            }
            LuaTable luaTable1 = new LuaTable();
            ActivityInfo[] arr_activityInfo = packageInfo0.activities;
            for(int v1 = 0; true; ++v1) {
                if(v1 >= arr_activityInfo.length) {
                    luaTable0.rawset("activities", luaTable1);
                label_23:
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
                ActivityInfo activityInfo0 = arr_activityInfo[v1];
                if(activityInfo0 != null) {
                    try {
                        LuaTable luaTable2 = new LuaTable();
                        if(activityInfo0.name != null) {
                            luaTable2.set("name", activityInfo0.name);
                        }
                        luaTable2.set("label", activityInfo0.loadLabel(packageManager0).toString());
                        try {
                            luaTable1.set(v, luaTable2);
                        }
                        catch(Throwable throwable0) {
                            Log.w("Failed get activity info", throwable0);
                            ++v1;
                        }
                        ++v;
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

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getAppInfo([string cmdLine]) -> nil || table";
    }
}

