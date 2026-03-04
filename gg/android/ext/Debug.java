package android.ext;

import android.os.Build.VERSION;
import android.os.Build;
import android.sup.ArrayListResults;
import android.sup.LongSparseArrayChecked;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Debug {
    public static void collectRegionLog() {
        String s = new File(Tools.getSdcardPath(), "GG_" + Build.VERSION.RELEASE + "_API_" + Build.VERSION.SDK_INT + ".log").getAbsolutePath();
        int resId = 0x7F0700AB;  // string:failed_save "Failed to save to __s__"
        try {
            BufferedWriter bufferedWriter0 = new BufferedWriter(new FileWriter(s, false));
            bufferedWriter0.write("Wed Mar 04 04:36:14 CST 2026");
            bufferedWriter0.write("\n");
            bufferedWriter0.write("Release: ");
            bufferedWriter0.write(Build.VERSION.RELEASE);
            bufferedWriter0.write("\n");
            bufferedWriter0.write("SDK: ");
            bufferedWriter0.write(Build.VERSION.SDK_INT);
            bufferedWriter0.write("\n");
            try {
                Process process0 = RootDetector.tryRoot("echo \"list:\"; ls -l /proc/; for i in /proc/*; do echo \"cat $i/maps:\"; cat $i/maps; done; exit;", null, true);
                if(process0 == null) {
                    bufferedWriter0.write("");
                }
                else {
                    BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(process0.getInputStream()));
                    String line;
                    while((line = bufferedReader0.readLine()) != null) {
                        bufferedWriter0.write(line);
                        bufferedWriter0.write("\n");
                    }
                    bufferedReader0.close();
                }
                RootDetector.debug = "";
                bufferedWriter0.write("current pid: ");
                ProcessInfo info = MainService.instance.processInfo;
                String s2 = info == null ? "0" : info.pid;
                bufferedWriter0.write(s2);
                bufferedWriter0.write("\n");
                bufferedWriter0.write("search results:\n");
                ArrayListResults mAddressList = MainService.instance.mAddressList;
                AddressItem item = new AddressItem();
                synchronized(mAddressList) {
                    int v2 = mAddressList.size();
                    for(int i = 0; i < v2; ++i) {
                        mAddressList.get(i, item);
                        bufferedWriter0.write(ToolsLight.prefixLongHex(8, item.address) + ' ' + item.getStringDataTrim() + ' ' + item.getNameShort() + " (" + Integer.toBinaryString(item.flags) + ")\n");
                    }
                }
                bufferedWriter0.write("saved list:\n");
                LongSparseArrayChecked longSparseArrayChecked0 = MainService.instance.savedList.getList();
                for(int i = 0; i < longSparseArrayChecked0.size(); ++i) {
                    SavedItem item = (SavedItem)longSparseArrayChecked0.valueAt(i);
                    if(item != null) {
                        bufferedWriter0.write(ToolsLight.prefixLongHex(8, item.address) + ' ' + item.getStringDataTrim() + ' ' + item.getNameShort() + " (" + Integer.toBinaryString(item.flags) + ")\n");
                    }
                }
            }
            catch(Exception e) {
                bufferedWriter0.write(e.getMessage());
                bufferedWriter0.write("\n");
            }
            bufferedWriter0.close();
            resId = 0x7F0700AA;  // string:log_saved "Log saved to __s__"
        }
        catch(Exception e) {
            Log.e(("Error opening file to save: " + s), e);
        }
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700AC)).setMessage(Tools.stringFormat(Re.s(resId), new Object[]{s})).setNegativeButton(Re.s(0x7F07009D), null));  // string:region_log "Region log"
    }

    public static String getAndroidVersion() {
        StringBuilder out = new StringBuilder();
        out.append(" Android(");
        Class[] arr_class = {Build.VERSION.class, Build.class};
        for(int v = 0; v < 2; ++v) {
            Field[] arr_field = arr_class[v].getFields();
            for(int v1 = 0; v1 < arr_field.length; ++v1) {
                Field field = arr_field[v1];
                try {
                    int v2 = field.getModifiers();
                    if(Modifier.isPublic(v2) && Modifier.isStatic(v2)) {
                        out.append(field.getName());
                        out.append(": ");
                        try {
                            Object object0 = field.get(null);
                            if(object0 instanceof Object[]) {
                                out.append(Arrays.toString(((Object[])object0)));
                            }
                            else {
                                out.append(object0);
                            }
                        }
                        catch(Throwable e) {
                            out.append(e.getMessage());
                        }
                        out.append("; ");
                    }
                }
                catch(Throwable e) {
                    out.append(e.getMessage());
                }
            }
        }
        out.append(')');
        return out.toString();
    }

    // 去混淆评级： 低(30)
    public static String getInfo() {
        return "96.0 (" + 0x3E79 + ") " + Installer.getHashes() + " on " + Debug.getAndroidVersion();
    }

    public static int getIntVersion() {
        return Debug.getIntVersion(96.0f);
    }

    public static int getIntVersion(String version) {
        String[] arr_s = version.replaceAll("[^\\d.]", "").split("\\.", -1);
        int out = 0;
        for(int i = 0; i < arr_s.length; ++i) {
            try {
                out = out * 100 + Integer.parseInt(arr_s[i]);
            }
            catch(NumberFormatException e) {
                Log.w(("bad part of version: " + arr_s[i] + '(' + version + ')'), e);
            }
        }
        return out;
    }

    public static String getLogcat(boolean withTime) {
        return Debug.getLogcat(withTime, 0);
    }

    private static String getLogcat(boolean withTime, int limit) {
        StringBuilder log = new StringBuilder();
        try {
            Process process0 = RootDetector.tryRoot((withTime ? "exec logcat -d -v threadtime " : "exec logcat -d -v brief "), null, true);
            if(process0 == null) {
                log.append("");
            }
            else {
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(process0.getInputStream()));
                String[] buffer = limit <= 0 ? null : new String[limit];
            alab1:
                while(true) {
                    int i = 0;
                    while(true) {
                        if(Thread.interrupted()) {
                            break alab1;
                        }
                        boolean add = false;
                        String line = bufferedReader0.readLine();
                        if(line == null) {
                            break alab1;
                        }
                        if(line.contains("AndroidService")) {
                            add = true;
                        }
                        else if(line.contains("android-daemon")) {
                            add = true;
                        }
                        if(!add && line.contains("AndroidRuntime")) {
                            add = true;
                        }
                        if(!add && line.contains("libc")) {
                            add = true;
                        }
                        if(!add && line.contains("Vold")) {
                            add = true;
                        }
                        if(!add && line.contains("DEBUG")) {
                            add = true;
                        }
                        if(!add && line.contains("CRASH")) {
                            add = true;
                        }
                        if(!add && line.contains("*** *** *** *** *** *** ***")) {
                            add = true;
                        }
                        if(!add && line.contains(Tools.getPackageName())) {
                            add = true;
                        }
                        if(!add && line.contains("--- beginning of ")) {
                            add = true;
                        }
                        if(!add && android.os.Process.myPid() > 0 && line.contains("5258")) {
                            add = true;
                        }
                        int pid = MainService.instance.mDaemonManager.getDaemonPid();
                        if(!add && pid > 0 && line.contains(Integer.toString(pid))) {
                            add = true;
                        }
                        int pid = MainService.instance.mDaemonManager.getSuPid();
                        if(!add && pid > 0 && line.contains(Integer.toString(pid))) {
                            add = true;
                        }
                        if(!add) {
                        }
                        else if(buffer == null) {
                            log.append(line);
                            log.append('\n');
                        }
                        else {
                            buffer[i] = line;
                            if(i + 1 >= limit) {
                                break;
                            }
                            ++i;
                        }
                    }
                }
                if(buffer != null) {
                    for(int j = i; !Thread.interrupted(); j = j) {
                        int j = j + 1;
                        String s = buffer[j];
                        if(s != null) {
                            log.append(s);
                            log.append('\n');
                        }
                        if(j >= limit) {
                            j = 0;
                        }
                        if(j == i) {
                            break;
                        }
                    }
                }
            }
        }
        catch(OutOfMemoryError unused_ex) {
            try {
                StringBuilder log = new StringBuilder();
                log.append(Debug.getLogcat(withTime, 500));
                log = log;
            }
            catch(OutOfMemoryError unused_ex) {
                log = new StringBuilder();
                log.append("OutOfMemoryError\n");
            }
        }
        catch(Throwable e) {
            log.append(e.getMessage());
            log.append('\n');
            Log.d("getLogcat fail.", e);
        }
        try {
            RootDetector.debug = "";
            return log.toString();
        }
        catch(OutOfMemoryError unused_ex) {
            return "OutOfMemoryError\n";
        }
    }

    public static boolean isCalledFromMethod(String method) {
        if(method != null) {
            StackTraceElement[] arr_stackTraceElement = Thread.currentThread().getStackTrace();
            if(arr_stackTraceElement != null) {
                for(int v = 0; v < arr_stackTraceElement.length; ++v) {
                    StackTraceElement el = arr_stackTraceElement[v];
                    if(el != null && method.equals(el.getMethodName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void runCollectRegionLog() {
        Tools.showToast(0x7F0700A9);  // string:collect_data_to_region_log "Started collection of data to write to the region 
                                      // log..."
        new DaemonThread("runCollectRegionLog") {
            @Override
            public void run() {
                super.run();
                Debug.collectRegionLog();
            }
        }.start();
    }

    public static void showInfo() {
        new DaemonThread("showInfo") {
            @Override
            public void run() {
                Log.iFull(("GG started: " + Debug.getInfo()));
            }
        }.start();
    }
}

