package android.ext;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.widget.TextView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessList {
    public static class AppInfo {
        public boolean isGame;
        public boolean isStub;
        public boolean isSystem;
        public String libsPath;
        public String name;
        public String pkg;
        public int pkgUid;
        public int uid;

        public AppInfo(int uid, String pkg) {
            this.libsPath = "";
            this.isSystem = false;
            this.isGame = false;
            this.isStub = false;
            this.uid = uid;
            this.pkg = pkg;
            this.name = pkg;
            this.pkgUid = uid;
        }

        @Override
        public String toString() {
            return "AppInfo [uid=" + this.uid + ", pkg=" + this.pkg + ", name=" + this.name + ", isSystem=" + this.isSystem + ", isGame=" + this.isGame + ']';
        }
    }

    public static class ProcessInfo implements Comparable {
        private static WeakReference EMPTY;
        public String cmdline;
        public static final int colorName;
        public static final int colorPid;
        public static final int colorSize;
        public volatile Drawable icon;
        private volatile boolean iconLoaded;
        public boolean isGame;
        public boolean isSystem;
        public String libsPath;
        public boolean main;
        public String name;
        public int order;
        public String packageName;
        public int pid;
        public int pkgUid;
        public int rss;
        public int uid;
        public long weight;
        public boolean x64;

        static {
            ProcessInfo.EMPTY = null;
            ProcessInfo.colorPid = Tools.getColor(0x7F0A001D, 0xFFAAFFFF);  // color:process_pid
            ProcessInfo.colorName = Tools.getColor(0x7F0A001E, -1);  // color:process_name
            ProcessInfo.colorSize = Tools.getColor(0x7F0A001F, 0xFFAAFFAA);  // color:process_size
        }

        public ProcessInfo(AppInfo appInfo, int pid, int uid, String cmdline, int order, boolean x64, int rss) {
            this.iconLoaded = false;
            this.cmdline = cmdline;
            this.name = appInfo.name;
            this.packageName = appInfo.pkg;
            this.libsPath = appInfo.libsPath == null ? "" : appInfo.libsPath;
            this.icon = null;
            this.pid = pid;
            this.uid = uid;
            if(!appInfo.isStub) {
                uid = appInfo.pkgUid;
            }
            this.pkgUid = uid;
            this.main = cmdline.equals(appInfo.pkg);
            this.isSystem = appInfo.isSystem;
            this.isGame = appInfo.isGame;
            this.order = order;
            this.x64 = x64;
            this.rss = rss;
            if(appInfo.isStub) {
                this.iconLoaded = true;
            }
        }

        public int compareTo(ProcessInfo another) {
            int v = 0;
            if(this.order != another.order) {
                return this.order <= another.order ? 1 : -1;
            }
            if(this.isGame != another.isGame) {
                return this.isGame ? -1 : 1;
            }
            if(this.isSystem != another.isSystem) {
                return this.isSystem ? 1 : -1;
            }
            if(this.weight != another.weight) {
                return this.weight <= another.weight ? 1 : -1;
            }
            if(this.main != another.main) {
                return this.main ? -1 : 1;
            }
            int v1 = this.getTrace() <= 0 ? 0 : 1;
            if(another.getTrace() > 0) {
                v = 1;
            }
            if(v1 != v) {
                return this.getTrace() <= 0 ? -1 : 1;
            }
            if(this.pid != another.pid) {
                return this.pid <= another.pid ? 1 : -1;
            }
            return 0;
        }

        @Override
        public int compareTo(Object object0) {
            return this.compareTo(((ProcessInfo)object0));
        }

        public String dump() {
            return "ProcessInfo [cmdline=" + this.cmdline + ", name=" + this.name + ", packageName=" + this.packageName + ", icon=" + this.icon + ", pid=" + this.pid + ", uid=" + this.uid + ", isSystem=" + this.isSystem + ", isGame=" + this.isGame + ", weight=" + this.weight + ", main=" + this.main + ", order=" + this.order + ", x64=" + this.x64 + ", rss=" + this.rss + ", getTracer()=" + this.getTracer() + ", getTrace()=" + this.getTrace() + ']';
        }

        // 去混淆评级： 低(20)
        private String getCmdline() {
            return this.main ? "" : " (" + this.cmdline.replace(String.valueOf(this.packageName) + ':', "") + ')';
        }

        public int getTrace() {
            int index = ProcessList.tracers.indexOfValue(this.pid);
            return index < 0 ? index : ProcessList.tracers.keyAt(index);
        }

        public int getTracer() {
            return ProcessList.tracers.get(this.pid);
        }

        public void loadIcon() {
            if(!this.iconLoaded) {
                try {
                    this.icon = Tools.tryCloneIcon(Tools.getResized(Tools.getApplicationIcon(Tools.getApplicationInfo(this.packageName)), Tools.dp2px48()));
                }
                catch(Throwable unused_ex) {
                    Log.w(("Failed load icon for " + this.packageName));
                }
                this.iconLoaded = true;
            }
        }

        public void loadIcon(TextView tv) {
            if(tv != null) {
                boolean iconLoaded = this.iconLoaded;
                Drawable icon_ = this.icon;
                if(icon_ == null) {
                    WeakReference wrEmpty = ProcessInfo.EMPTY;
                    if(wrEmpty != null) {
                        icon_ = (Drawable)wrEmpty.get();
                    }
                    if(icon_ == null) {
                        icon_ = Tools.getDrawable(0x7F02000B);  // drawable:empty
                        ProcessInfo.EMPTY = new WeakReference(icon_);
                    }
                }
                Tools.addIconToTextView(tv, icon_, 0x30);
                if(!iconLoaded) {
                    ThreadManager.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ProcessInfo.this.loadIcon();
                            if(ProcessInfo.this.icon != null) {
                                ThreadManager.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(this.val$tv.getTag() == ProcessInfo.this) {
                                            ProcessInfo processList$ProcessInfo0 = ProcessInfo.this;
                                            Tools.addIconToTextView(this.val$tv, processList$ProcessInfo0.icon, 0x30);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }

        public void resolveLibs() {
            if(this.uid == this.pkgUid) {
                return;
            }
            else {
                Log.w(("vs app: " + this.pkgUid + " != " + this.uid));
                try {
                    String s = ProcessList.lastInstance.getPackagesForUid(this.uid);
                    if(s != null) {
                        ApplicationInfo applicationInfo0 = Tools.getApplicationInfo(s);
                        if(applicationInfo0 != null && applicationInfo0.nativeLibraryDir != null) {
                            Log.w(("vs: " + this.libsPath + " => " + applicationInfo0.nativeLibraryDir));
                            this.libsPath = applicationInfo0.nativeLibraryDir;
                            return;
                        }
                    }
                    return;
                }
                catch(Throwable e) {
                }
            }
            Log.w(("Failed get vs info for " + this.uid), e);
        }

        public CharSequence toCharSequence() {
            CharSequenceBuilder csb = new CharSequenceBuilder();
            StringBuilder out = new StringBuilder();
            if(this.getTracer() > 0) {
                out.append('#');
            }
            if(this.getTrace() > 0) {
                out.append('!');
            }
            if(this.uid != this.pkgUid) {
                out.append('v');
            }
            if(this.pid > 1) {
                out.append('[');
                out.append(Integer.toString(this.pid));
                out.append("] ");
            }
            csb.append(Tools.colorize(out.toString(), ProcessInfo.colorPid));
            csb.append(Tools.colorize((this.name + this.getCmdline()), ProcessInfo.colorName));
            StringBuilder out = new StringBuilder();
            if(this.x64) {
                out.append(" [x64]");
            }
            if(this.rss != 0) {
                out.append(" [");
                out.append(Tools.formatFileSize(Tools.getContext(), 0x400L * ((long)this.rss)));
                out.append(']');
            }
            csb.append(Tools.colorize(out.toString(), ProcessInfo.colorSize));
            return csb.toCharSequence();
        }

        @Override
        public String toString() {
            return this.toCharSequence().toString();
        }
    }

    private static final int BUGGED_UID = 0;
    private static final int WEIGHT_STEP = 1000;
    private static final Map appCache;
    private static final SparseIntArray buggedUids;
    private static ProcessList lastInstance;
    private ActivityManager mActivityManager;
    private PackageManager mPackageManager;
    static HashMap realUids;
    static final SparseIntArray tracers;

    static {
        ProcessList.BUGGED_UID = -1;
        SparseIntArray sparseIntArray0 = new SparseIntArray();
        ProcessList.buggedUids = sparseIntArray0;
        ProcessList.appCache = new HashMap();
        CheckNativeCrash.loadLastCrash();
        ProcessList.addBuggedPackages();
        String[] arr_s = CheckNativeCrash.getBuggedUids();
        for(int v = 0; v < arr_s.length; ++v) {
            String bad = arr_s[v];
            if(bad != null && bad.length() != 0) {
                try {
                    sparseIntArray0.put(Integer.parseInt(bad), 1);
                }
                catch(NumberFormatException e) {
                    Log.e(("Failed load bugged uid: " + bad), e);
                }
            }
        }
        ProcessList.tracers = new SparseIntArray();
        ProcessList.lastInstance = null;
        ProcessList.realUids = null;
    }

    public ProcessList(ActivityManager am, PackageManager pm) {
        this.mActivityManager = am;
        this.mPackageManager = pm;
        ProcessList.lastInstance = this;
    }

    private static void addBuggedPackages() {
        Map cache = ProcessList.appCache;
        String[] arr_s = CheckNativeCrash.getBuggedPackages();
        for(int v = 0; v < arr_s.length; ++v) {
            String bad = arr_s[v];
            if(bad != null && bad.length() != 0) {
                cache.put(bad, ProcessList.getBuggedPackage(bad, null));
            }
        }
    }

    public static void clearCache() {
        int v = ProcessList.appCache.size();
        if(v == 0) {
            return;
        }
        ProcessList.appCache.clear();
        ProcessList.addBuggedPackages();
        Log.d(("PL: " + v + " -> " + ProcessList.appCache.size()));
    }

    private AppInfo getAppInfo(int pid, int uid, String key, List list0, SparseArray sparseArray0) {
        if(key.indexOf(58) != -1) {
            key = key.split(":", -1)[0];
        }
        String pkg = key;
        boolean isPkg = pkg.indexOf(46) != -1 && pkg.indexOf(0x2F) == -1;
        AppInfo processList$AppInfo0 = isPkg ? this.getAppInfo(pkg) : null;
        if(processList$AppInfo0 != null) {
            if(processList$AppInfo0.uid == ProcessList.BUGGED_UID) {
                processList$AppInfo0.uid = uid;
            }
            return processList$AppInfo0;
        }
        ApplicationInfo info = null;
        boolean fixed = false;
        if(isPkg) {
            try {
                info = Tools.getApplicationInfo(pkg);
            }
            catch(PackageManager.NameNotFoundException unused_ex) {
                goto label_20;
            }
            catch(NoSuchMethodError e) {
                Log.badImplementation(e);
                goto label_20;
            }
            if(info != null) {
                fixed = true;
            }
        }
    label_20:
        if(info == null) {
            if(list0 != null) {
                for(Object object0: list0) {
                    ActivityManager.RunningAppProcessInfo rapi = (ActivityManager.RunningAppProcessInfo)object0;
                    if(rapi != null && rapi.pid == pid && rapi.pkgList != null && rapi.pkgList.length > 0) {
                        pkg = rapi.pkgList[0];
                        fixed = true;
                        break;
                    }
                    if(false) {
                        break;
                    }
                }
            }
            if(!fixed) {
                String p = (String)sparseArray0.get(uid);
                if(p != null) {
                    pkg = p;
                    fixed = true;
                }
            }
            if(!fixed) {
                String s3 = this.getPackagesForUid(uid);
                if(s3 != null) {
                    pkg = s3;
                    sparseArray0.put(uid, pkg);
                    fixed = true;
                }
            }
            if(fixed) {
                AppInfo aInfo = this.getAppInfo(pkg);
                if(aInfo != null) {
                    if(aInfo.uid == ProcessList.BUGGED_UID) {
                        aInfo.uid = uid;
                    }
                    if(isPkg) {
                        ProcessList.appCache.put(key, aInfo);
                    }
                    return aInfo;
                }
            }
        }
        AppInfo processList$AppInfo2 = new AppInfo(uid, pkg);
        if(fixed) {
            try {
                if(info == null) {
                    info = Tools.getApplicationInfo(pkg);
                }
                if(info != null) {
                    processList$AppInfo2.name = this.getApplicationLabel(pkg, info);
                    if(processList$AppInfo2.name != null) {
                        processList$AppInfo2.name = String.valueOf(processList$AppInfo2.name);
                    }
                    processList$AppInfo2.isSystem = info.sourceDir.startsWith("/system/") || (info.flags & 0x80) != 0 || (info.flags & 1) != 0;
                    processList$AppInfo2.isGame = (info.flags & 0x2000000) != 0;
                    processList$AppInfo2.libsPath = info.nativeLibraryDir;
                    processList$AppInfo2.pkgUid = info.uid;
                    HashMap realUids = ProcessList.realUids;
                    if(realUids != null) {
                        Integer realUid = (Integer)realUids.get(pkg);
                        if(realUid != null && ((int)realUid) != info.uid) {
                            Log.w(("real uid: " + info.uid + " != " + realUid));
                            processList$AppInfo2.pkgUid = (int)realUid;
                        }
                    }
                    if(processList$AppInfo2.pkgUid != uid) {
                        Log.w(("vs app: " + processList$AppInfo2.pkgUid + " != " + uid));
                    }
                }
            }
            catch(PackageManager.NameNotFoundException e) {
                Log.e(("Package not found: " + processList$AppInfo2.pkg + " [" + processList$AppInfo2.uid + ']'), e);
            }
            catch(NoSuchMethodError e) {
                Log.badImplementation(e);
            }
        }
        ProcessList.appCache.put(pkg, processList$AppInfo2);
        if(isPkg && !key.equals(pkg)) {
            ProcessList.appCache.put(key, processList$AppInfo2);
        }
        return processList$AppInfo2;
    }

    private AppInfo getAppInfo(String pkg) {
        String s1 = Tools.getPackageName();
        if(pkg.equals(s1)) {
            return ProcessList.getBuggedPackage(s1, "GG");
        }
        return pkg.startsWith("com.bluestacks.") ? ProcessList.getBuggedPackage(pkg, pkg) : ((AppInfo)ProcessList.appCache.get(pkg));
    }

    private String getApplicationLabel(String pkg, ApplicationInfo info) {
        String label;
        try {
            label = info.packageName;
            String s2 = Tools.getApplicationLabel(info);
            return s2 == null ? label : s2;
        }
        catch(Throwable e) {
            Log.e(("Failed load label for: " + info.packageName), e);
            return label;
        }
    }

    private static AppInfo getBuggedPackage(String pkg, String name) {
        AppInfo processList$AppInfo0 = new AppInfo(ProcessList.BUGGED_UID, pkg);
        if(name != null) {
            processList$AppInfo0.name = name;
        }
        processList$AppInfo0.isStub = true;
        return processList$AppInfo0;
    }

    private String getPackagesForUid(int uid) {
        String p = null;
        if(ProcessList.buggedUids.get(uid) != 1) {
            try {
                PackageManager packageManager = this.mPackageManager;
                boolean z = CheckNativeCrash.enter(("uid:" + uid), "uid");
                try {
                    String[] arr_s = packageManager.getPackagesForUid(uid);
                    if(arr_s != null && arr_s.length > 0) {
                        p = arr_s[0];
                    }
                }
                finally {
                    CheckNativeCrash.exit(z);
                }
            }
            catch(Throwable e) {
                Log.e("Failed getPackagesForUid", e);
            }
            return p;
        }
        return null;
    }

    private void load(BufferReader reader) {
        AppInfo processList$AppInfo0;
        String s;
        int v8;
        int v7;
        int v6;
        int v5;
        boolean x64;
        int v4;
        int v3;
        reader.reset();
        ArrayList listProcesses = new ArrayList();
        ProcessList.tracers.clear();
        List run = null;
        try {
            Log.d("getRunningAppProcesses: start");
            run = this.mActivityManager.getRunningAppProcesses();
            Log.d(("getRunningAppProcesses: end " + run.size()));
        }
        catch(Throwable e) {
            Log.e("Failed getRunningAppProcesses", e);
        }
        try {
            int v = Process.myPid();
            int v1 = MainService.instance.mDaemonManager.getDaemonPid();
            int v2 = MainService.instance.mDaemonManager.getSuPid();
            android.fix.SparseArray cacheUid = new android.fix.SparseArray();
        alab1:
            while(true) {
                do {
                    do {
                        do {
                        label_14:
                            v3 = reader.readInt();
                            if(v3 == 0) {
                                break alab1;
                            }
                            v4 = reader.readInt();
                            x64 = reader.readByte() != 0;
                            v5 = reader.readInt();
                            v6 = reader.readInt();
                            v7 = reader.readInt();
                            v8 = reader.readInt();
                            if(v8 < 0 || v8 > 200) {
                                goto label_32;
                            }
                            s = reader.readString(v8, null);
                        }
                        while(v3 == v || v3 == v1 || v3 == v2);
                        processList$AppInfo0 = this.getAppInfo(v3, v4, s, run, cacheUid);
                    }
                    while(processList$AppInfo0 == null || processList$AppInfo0.pkg.equals(Tools.getPackageName()));
                    listProcesses.add(new ProcessInfo(processList$AppInfo0, v3, v4, s, v6, x64, v5));
                }
                while(v7 == 0);
                ProcessList.tracers.put(v3, v7);
                Log.d(("Tracer: " + v7 + " -> " + v3));
                goto label_14;
            label_32:
                Log.e(("Bad cmdline length: " + v8), new RuntimeException());
                break;
            }
        }
        catch(IOException e) {
            Log.e("???", e);
        }
        MainService.instance.mAppDetector.detectAppResume(this.sort(listProcesses, run));
    }

    public static void loadData(BufferReader reader) {
        ProcessList.lastInstance.load(reader);
    }

    private List sort(List list0, List list1) {
        String[] arr_s1;
        long mul;
        String[] pkgs;
        List[] lists;
        ProcessInfo[] arr = (ProcessInfo[])list0.toArray(new ProcessInfo[list0.size()]);
        for(int j = 0; j < arr.length; ++j) {
            arr[j].weight = 0L;
        }
        try {
            lists = new List[]{this.mActivityManager.getRunningTasks(50), null};
        }
        catch(Throwable e) {
            Log.e("Failed getRunningTasks", e);
        }
        lists[1] = list1;
        int v1 = 0;
        while(v1 < 2) {
            List list = lists[v1];
            if(list != null) {
                boolean[] used = new boolean[arr.length];
                int k = 0;
                while(k < list.size()) {
                    Object object0 = list.get(k);
                    if(object0 != null) {
                        try {
                            pkgs = null;
                            mul = 0L;
                            if(object0 instanceof ActivityManager.RunningTaskInfo) {
                                arr_s1 = new String[2];
                                String s = ((ActivityManager.RunningTaskInfo)object0).topActivity == null ? null : ((ActivityManager.RunningTaskInfo)object0).topActivity.getPackageName();
                                arr_s1[0] = s;
                                String s1 = ((ActivityManager.RunningTaskInfo)object0).baseActivity == null ? null : ((ActivityManager.RunningTaskInfo)object0).baseActivity.getPackageName();
                                arr_s1[1] = s1;
                                goto label_37;
                            }
                        }
                        catch(NoClassDefFoundError e) {
                            Log.w("Class not found", e);
                        }
                        goto label_46;
                        try {
                        label_37:
                            if(arr_s1[0] != null && arr_s1[0].equals(arr_s1[1])) {
                                arr_s1[1] = null;
                            }
                        }
                        catch(NoClassDefFoundError e) {
                            pkgs = arr_s1;
                            Log.w("Class not found", e);
                            goto label_46;
                        }
                        mul = 1000000L;
                        pkgs = arr_s1;
                    label_46:
                        if(object0 instanceof ActivityManager.RunningAppProcessInfo) {
                            try {
                                pkgs = ((ActivityManager.RunningAppProcessInfo)object0).pkgList;
                                mul = 1000L;
                            }
                            catch(NoClassDefFoundError e) {
                                Log.w("Class not found", e);
                            }
                        }
                        if(pkgs != null) {
                            int v4 = pkgs.length;
                            for(int v5 = 0; v5 < v4; ++v5) {
                                String pkg = pkgs[v5];
                                if(pkg != null) {
                                    for(int j = 0; j < arr.length; ++j) {
                                        if(!used[j]) {
                                            ProcessInfo pi = arr[j];
                                            if(pi != null && pkg.equals(pi.packageName)) {
                                                pi.weight += ((long)(list.size() - k)) * mul;
                                                used[j] = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ++k;
                }
            }
            ++v1;
        }
        try {
            Arrays.sort(arr);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        List list3 = new ArrayList(arr.length);
        for(int j = 0; j < arr.length; ++j) {
            list3.add(arr[j]);
        }
        return list3;
    }
}

