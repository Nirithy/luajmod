package android.ext;

import android.content.SharedPreferences;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RootDetector {
    public static final String SH_KEY = "sh";
    public static final String SU_KEY = "su";
    public static String debug;
    static volatile int runCmdPid;
    private static String suGlobal;
    private static boolean useSh;

    static {
        RootDetector.debug = "";
        RootDetector.useSh = false;
        RootDetector.suGlobal = "su";
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        RootDetector.suGlobal = sharedPreferences0.getString("su", "su");
        RootDetector.useSh = sharedPreferences0.getBoolean("sh", false);
        RootDetector.runCmdPid = 0;
    }

    private static String addPath() {
        ArrayList arrayList0 = new ArrayList(33);
        for(int v = 0; v < 11; ++v) {
            String path = new String[]{"/vendor/bin", "/system/bin", "/bin", "/data/local", "/data/local/bin", "/system/sd/bin", "/system/bin/failsafe", "/system/bin/bstk", "/su/bin", "/magisk/.core/bin", "/magisk/phh/bin"}[v];
            arrayList0.add(path);
            if(path.contains("/bin")) {
                arrayList0.add(path.replace("/bin", "/xbin"));
                arrayList0.add(path.replace("/bin", "/sbin"));
            }
        }
        return TextUtils.join(":", arrayList0);
    }

    private static String getData(String name, InputStream is) {
        StringBuilder data = new StringBuilder();
        data.append(name);
        data.append(": ");
        try {
            int v = is.available();
            if(v > 0) {
                byte[] buf = new byte[v];
                int v1 = is.read(buf);
                if(v1 > 0) {
                    data.append(new String(Arrays.copyOf(buf, v1)));
                }
            }
        }
        catch(IOException e) {
            data.append(e.toString());
        }
        data.append('\n');
        return data.toString();
    }

    private static String[] getEnv() {
        Map map0 = System.getenv();
        int size = map0.size();
        int i = 0;
        String path = (String)map0.get("PATH");
        if(path == null) {
            ++size;
        }
        String[] envp = new String[size];
        for(Object object0: map0.entrySet()) {
            String key = (String)((Map.Entry)object0).getKey();
            String value = (String)((Map.Entry)object0).getValue();
            if("PATH".equals(key)) {
                value = String.valueOf(value) + ':' + RootDetector.addPath();
            }
            envp[i] = String.valueOf(key) + '=' + value;
            ++i;
        }
        if(path == null) {
            envp[i] = "PATH=" + RootDetector.addPath();
        }
        return envp;
    }

    public static String getInfo(Process process) {
        StringBuilder dbg = new StringBuilder();
        if(process != null) {
            dbg.append(RootDetector.getData("stdout", process.getInputStream()));
            dbg.append(RootDetector.getData("stderr", process.getErrorStream()));
            dbg.append("exit value: ");
            try {
                dbg.append(process.exitValue());
            }
            catch(IllegalThreadStateException unused_ex) {
                dbg.append("already run");
            }
            dbg.append('\n');
            return dbg.toString();
        }
        dbg.append("process is null\n");
        return dbg.toString();
    }

    static String getSu(String su) [...] // 潜在的解密器

    public static String runCmd(String cmd, int timeout) throws IOException, InterruptedException {
        String s1 = RootDetector.runCmd(cmd, timeout, null);
        RootDetector.debug = "";
        return s1;
    }

    public static String runCmd(String cmd, int timeout, String suCmd) throws IOException, InterruptedException {
        StringBuilder ret = new StringBuilder();
        RootDetector.runCmdPid = 0;
        FutureTask futureTask0 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return this.call();
            }

            public String call() throws Exception {
                int v;
                Log.d(("cmd (" + cmd + ") start"));
                String su = RootDetector.getSu(suCmd);
                Log.d(("su (" + su + ") used"));
                Process process0 = su == "" ? Tools.exec(new String[]{cmd}) : RootDetector.tryRoot(cmd, su, true);
                if(process0 == null) {
                    Log.e("cmd fail: ");
                    return "cmd fail - got null";
                }
                v = Tools.getPid(process0);
                RootDetector.runCmdPid = v;
                try(BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(process0.getInputStream()))) {
                    String line;
                    while((line = bufferedReader0.readLine()) != null) {
                        Log.d(("cmd (" + cmd + ") out: " + line));
                        ret.append(line);
                        ret.append('\n');
                    }
                }
                ret.append("exit code: ");
                int v1 = process0.waitFor();
                ret.append(v1);
                Log.d(("cmd (" + cmd + ") exit: " + process0.exitValue()));
                if(v > 0) {
                    try {
                        Log.d(("Kill at end: " + v));
                        android.os.Process.killProcess(v);
                        Log.d("Kill at end: ok");
                    }
                    catch(Throwable e) {
                        Log.d("Failed kill at end", e);
                    }
                }
                RootDetector.runCmdPid = 0;
                return null;
            }
        });
        new ThreadEx(futureTask0, "runCmd: " + cmd).start();
        try {
            futureTask0.get(((long)timeout), TimeUnit.SECONDS);
        }
        catch(ExecutionException e) {
            Log.e(("cmd (" + cmd + ") expection"), e);
            ret.append("expection: ");
            ret.append(e.getMessage());
        }
        catch(TimeoutException e) {
            Log.d(("cmd (" + cmd + ") exit: timeout " + timeout), e);
            ret.append("timeout: ");
            ret.append(timeout);
        }
        int pid = RootDetector.runCmdPid;
        if(pid > 0) {
            try {
                Log.d(("Kill: " + pid));
                android.os.Process.killProcess(pid);
                Log.d("Kill: ok");
            }
            catch(Throwable e) {
                Log.d("Failed kill", e);
            }
        }
        RootDetector.runCmdPid = 0;
        return ret.toString();
    }

    public static Process tryRoot(String cmd) {
        return RootDetector.tryRoot(cmd, null, false);
    }

    public static Process tryRoot(String cmd, String suCmd, boolean useDebug) {
        String runBin;
        Process process = null;
        StringBuilder dbg = new StringBuilder();
        Log.d(("try: " + cmd));
        if(!cmd.contains("exec") && !cmd.contains("exit")) {
            throw new RuntimeException("You forgot exec or exit");
        }
        if(cmd.contains("/emulated/0")) {
            Log.w(("Possible bugged call to /emulated/0 from root: " + cmd));
        }
        dbg.append("try: ");
        dbg.append(cmd);
        dbg.append('\n');
        String s2 = RootDetector.getSu(suCmd);
        dbg.append("su: ");
        dbg.append(s2);
        dbg.append('\n');
        if(RootDetector.useSh) {
            runBin = "sh";
            cmd = "exec " + s2 + '\n' + cmd;
        }
        else {
            runBin = s2;
        }
        String[] arr_s = {runBin};
        Log.d(("suCmd: \'" + suCmd + "\'; su: \'" + s2 + "\'; runBin: \'" + runBin + '\''));
        for(int i = 0; i < 2; ++i) {
            try {
                switch(i) {
                    case 0: {
                        Log.d("exec 0 start");
                        process = Tools.exec(arr_s);
                        Log.d(("exec 0 end: " + process));
                        break;
                    }
                    case 1: {
                        Log.d("exec 1 start");
                        process = Tools.exec(arr_s, RootDetector.getEnv());
                        Log.d(("exec 1 end: " + process));
                        break;
                    }
                    default: {
                        throw new IllegalAccessException();
                    }
                }
                DataOutputStream dataOutputStream0 = new DataOutputStream(process.getOutputStream());
                dataOutputStream0.writeBytes(Tools.getRealPath(cmd) + '\n');
                dataOutputStream0.flush();
                dbg.append("process started: " + i + '\n');
            }
            catch(Throwable t) {
                for(Throwable ex = t; ex != null; ex = ex.getCause()) {
                    dbg.append(ex.toString());
                    dbg.append('\n');
                }
                dbg.append('\n');
                dbg.append(RootDetector.getInfo(process));
            }
            if(process != null) {
                break;
            }
        }
        Log.d("tryRoot end");
        if(useDebug) {
            RootDetector.debug = dbg.toString();
        }
        return process;
    }
}

