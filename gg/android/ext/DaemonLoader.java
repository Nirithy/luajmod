package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.lang.ProcessBuilder;
import android.os.Build.VERSION;
import android.system.ErrnoException;
import android.system.Os;
import com.ggdqo.R.raw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class DaemonLoader {
    private static final int ARCH_ARM = 103;
    private static final int ARCH_ARM_64 = 105;
    private static final int ARCH_ARM_V7A = 104;
    private static final int ARCH_UNKNOWN = 100;
    private static final int ARCH_X86 = 101;
    private static final int ARCH_X86_64 = 102;
    private final String DAEMON_PATH;
    public static StringBuilder DEBUG = null;
    private static final char LIB_ARM = 'a';
    private static final char LIB_ARM64_CLIENT = 'r';
    private static final char LIB_ARM64_PATCH = 'l';
    private static final char LIB_ARM64_PIE = '5';
    private static final char LIB_ARM64_SH = '6';
    private static final char LIB_ARM64_STUB = 'g';
    private static final char LIB_ARM_CLIENT = 't';
    private static final char LIB_ARM_PATCH = 'o';
    private static final char LIB_ARM_PIE = 'b';
    private static final char LIB_ARM_SH = 'd';
    private static final char LIB_ARM_STUB = 'i';
    private static final char LIB_ARM_V7A = '7';
    private static final char LIB_ARM_V7A_CLIENT = 's';
    private static final char LIB_ARM_V7A_PATCH = 'n';
    private static final char LIB_ARM_V7A_PIE = '8';
    private static final char LIB_ARM_V7A_SH = '9';
    private static final char LIB_ARM_V7A_STUB = 'h';
    private static final char LIB_X86 = '0';
    private static final char LIB_X86_64_CLIENT = 'q';
    private static final char LIB_X86_64_PATCH = 'k';
    private static final char LIB_X86_64_PIE = '2';
    private static final char LIB_X86_64_SH = '4';
    private static final char LIB_X86_64_STUB = 'f';
    private static final char LIB_X86_CLIENT = 'p';
    private static final char LIB_X86_PATCH = 'j';
    private static final char LIB_X86_PIE = '1';
    private static final char LIB_X86_SH = '3';
    private static final char LIB_X86_STUB = 'e';
    private static final String LOADER = "0";
    public static StringBuilder TESTED;
    private static volatile int arch;
    private static volatile String dir;
    private static boolean fixer;
    private static volatile String path;
    private static volatile char stub32;
    private static volatile char stub64;

    static {
        DaemonLoader.path = null;
        DaemonLoader.dir = null;
        DaemonLoader.arch = 100;
        DaemonLoader.stub32 = 'i';
        DaemonLoader.stub64 = 'g';
        DaemonLoader.TESTED = new StringBuilder();
        DaemonLoader.DEBUG = new StringBuilder();
        DaemonLoader.fixer = false;
    }

    public DaemonLoader() {
        this.DAEMON_PATH = this.getDaemonPath_();
    }

    private static void addFiles(ArrayList arrayList0, String dir, String hidden, boolean ifExists) {
        File check = new File(dir);
        if(!ifExists || check.exists()) {
            arrayList0.add(new File(dir + "/files", hidden));
            arrayList0.add(new File(dir + "/cache", hidden));
            arrayList0.add(new File(dir + "/files"));
            arrayList0.add(new File(dir + "/cache"));
            arrayList0.add(check);
        }
    }

    private static void extractLib(char libId) {
        DaemonLoader.extractLib(Character.toString(libId));
    }

    private static void extractLib(String libId) {
        File file0 = new File(DaemonLoader.getDaemonDir(), "lib" + libId + ".so");
        if(!file0.isFile()) {
            Log.d(("Extract file " + file0));
            Resources resources0 = Tools.getContext().getResources();
            int v = Re.i(("chunk" + libId), raw.class);
            if(v == 0) {
                Log.d(("Nothing extract file " + libId + ' ' + file0.getAbsolutePath()));
                return;
            }
            try {
                byte[] buf = new byte[0x2000];
                InputStream inputStream0 = resources0.openRawResource(v);
                FileOutputStream os = new FileOutputStream(file0);
                while(true) {
                    int read = inputStream0.read(buf);
                    if(read <= 0) {
                        os.close();
                        inputStream0.close();
                        return;
                    }
                    os.write(buf, 0, read);
                }
            }
            catch(Throwable e) {
                Log.d(("Failed extract file " + libId + ' ' + file0.getAbsolutePath()), e);
            }
        }
    }

    private void extractShLibs(char libId) {
        DaemonLoader.extractLib(libId);
        switch(libId) {
            case 0x30: 
            case 49: {
            label_8:
                DaemonLoader.extractLib('3');
                DaemonLoader.extractLib('e');
                DaemonLoader.stub32 = 'e';
                DaemonLoader.extractLib('j');
                break;
            }
            case 50: {
                DaemonLoader.extractLib("01");
                DaemonLoader.extractLib('4');
                DaemonLoader.extractLib('f');
                DaemonLoader.stub64 = 'f';
                DaemonLoader.extractLib('k');
                goto label_8;
            }
            case 53: {
                DaemonLoader.extractLib("08");
                DaemonLoader.extractLib('6');
                DaemonLoader.extractLib('g');
                DaemonLoader.stub64 = 'g';
                DaemonLoader.extractLib('l');
                goto label_18;
            }
            case 55: 
            case 56: {
            label_18:
                DaemonLoader.extractLib('9');
                DaemonLoader.extractLib('h');
                DaemonLoader.stub32 = 'h';
                DaemonLoader.extractLib('n');
                break;
            }
            case 97: 
            case 98: {
                DaemonLoader.extractLib('d');
                DaemonLoader.extractLib('i');
                DaemonLoader.stub32 = 'i';
                DaemonLoader.extractLib('o');
            }
        }
        try {
            Tools.extractFile("sh", new File(Tools.getFilesDirHidden(), "sh.jar"));
        }
        catch(Throwable e) {
            Log.e("JVM fail", e);
        }
    }

    private int getArch(String filename) {
        int result = 100;
        Tools.allowExecute(filename);
        boolean i = false;
        while(true) {
            try {
                try {
                    Process process0 = Tools.exec(new String[]{Tools.getRealPath(filename), (Config.vSpaceReal ? Config.vSpacePkg : "1")});
                    if(!Tools.waitForTimeout(process0, 30)) {
                        throw new RuntimeException("timeout: " + 30);
                    }
                    result = process0.exitValue();
                    break;
                }
                catch(IOException e) {
                    if(i) {
                        throw e;
                    }
                    String s1 = e.getMessage();
                    if(s1 == null || s1.indexOf("denied") == -1) {
                        throw e;
                    }
                    Tools.chmod(filename, "0755");
                    i = true;
                }
            }
            catch(Throwable e) {
                Log.w(("getArch: " + filename), e);
                DaemonLoader.DEBUG.append(e.toString());
                break;
            }
        }
        if(Config.vSpace && (Config.vSpace64 == 1 || Config.vSpace64 == 0 && Config.vSpacePkg.contains("64")) && !DaemonLoader.fixer && (result & 0x7F) < 100) {
            try {
                DaemonLoader.fixer = true;
                File file0 = new File(Tools.getSdcardPath(), "gg_fixer.apk");
                Tools.extractFile("fx", file0);
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Alert.show(Alert.create().setMessage(Tools.stringFormat(Re.s(0x7F070353), new Object[]{file0.getAbsolutePath()})).setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:need_fixer "A problem was detected with this 64-bit virtual space.\n\nTo 
                                                                                                                                                                                                                       // fix it:\n\n1. Install the __fixer__ application. (From this dialog, either manually 
                                                                                                                                                                                                                       // along the path: \n__s__\n).\n\n2. Add __fixer__ to the virtual space.\n\n3. Run 
                                                                                                                                                                                                                       // __fixer__ in virtual space.\n\n4. Remove __fixer__.\n\n5. Restart the __app_name__ 
                                                                                                                                                                                                                       // in virtual space."
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                Intent[] arr_intent = Installer.getInstallIntent(this.val$output);
                                int v1 = 0;
                                while(v1 < arr_intent.length) {
                                    Intent intent = arr_intent[v1];
                                    try {
                                        Tools.getContext().startActivity(intent);
                                        return;
                                    }
                                    catch(Throwable e) {
                                        Log.badImplementation(e);
                                        ++v1;
                                    }
                                }
                            }
                        }).setNegativeButton(Re.s(0x7F07009D), null));  // string:ok "OK"
                    }
                });
            }
            catch(Throwable e) {
                Log.w("Failed extract fx", e);
            }
            return result;
        }
        return result;
    }

    public static int getDaemonArch() {
        if(DaemonLoader.arch == 100) {
            new DaemonLoader();
        }
        return DaemonLoader.arch;
    }

    public static String getDaemonDir() {
        if(DaemonLoader.dir == null) {
            File newDir = DaemonLoader.getInternalDir();
            if(newDir == null) {
                newDir = Tools.getFilesDirHidden();
            }
            DaemonLoader.dir = DaemonLoader.makeExecutable(newDir.getAbsolutePath());
        }
        return DaemonLoader.dir;
    }

    public static String getDaemonPath() {
        if(DaemonLoader.path == null) {
            DaemonLoader.path = new DaemonLoader().DAEMON_PATH;
        }
        return DaemonLoader.path;
    }

    private String getDaemonPath_() {
        int v3;
        char[] arr_c;
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < 16) {
            arr_c = new char[]{'\u0000', '0', '7', 'a', '2', '1', '5', '8', 'b'};
        }
        else {
            arr_c = sdk >= 21 ? new char[]{'\u0000', '2', '1', '5', '8', 'b', '0', '7', 'a'} : new char[]{'\u0000', '1', '0', '8', '7', 'b', 'a', '2', '5'};
        }
        String s = DaemonLoader.getDaemonDir();
        int i = 0;
        while(true) {
            if(i >= arr_c.length) {
                DaemonLoader.TESTED.append(';');
                String dir = Tools.getRealPath(s);
                try {
                    DaemonLoader.DEBUG.append("\n\n");
                    DaemonLoader.DEBUG.append("ls -l ");
                    DaemonLoader.DEBUG.append(dir);
                    DaemonLoader.DEBUG.append(":\n");
                    BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(Tools.exec(new String[]{"ls", "-l", dir}).getInputStream()));
                    String s2;
                    while((s2 = bufferedReader0.readLine()) != null) {
                        if(s2.indexOf("lib") != -1 && s2.indexOf(".so") != -1) {
                            Log.d(("ls: " + s2));
                            DaemonLoader.DEBUG.append(s2);
                            DaemonLoader.DEBUG.append('\n');
                        }
                    }
                    bufferedReader0.close();
                }
                catch(Exception e) {
                    Log.w(("ls: " + dir), e);
                    DaemonLoader.DEBUG.append(e);
                    DaemonLoader.DEBUG.append('\n');
                }
                DaemonLoader.DEBUG.append('\n');
                return "no_binary_for_your_arch";
            }
            if(i == 0) {
                for(int j = 1; j < arr_c.length; ++j) {
                    if(new File(s, DaemonLoader.libName(arr_c[j])).exists()) {
                        arr_c[0] = arr_c[j];
                        break;
                    }
                }
                if(arr_c[0] != 0) {
                    goto label_39;
                }
            }
            else {
            label_39:
                DaemonLoader.DEBUG.append(arr_c[i]);
                DaemonLoader.DEBUG.append(": ");
                String s3 = "0" + arr_c[i];
                File file0 = new File(s, "lib" + s3 + ".so");
                if(!file0.isFile()) {
                    DaemonLoader.extractLib(s3);
                }
                if(file0.isFile()) {
                    DaemonLoader.TESTED.append(arr_c[i]);
                    v3 = this.getArch(file0.getAbsolutePath());
                    if(this.isValid(arr_c[i], v3)) {
                        break;
                    }
                    Log.d(("getArch " + arr_c[i] + ' ' + v3));
                    DaemonLoader.DEBUG.append("; ");
                    DaemonLoader.DEBUG.append(v3);
                    DaemonLoader.DEBUG.append('\n');
                }
                else {
                    DaemonLoader.DEBUG.append(file0);
                    DaemonLoader.DEBUG.append(" NF.\n");
                    Log.d(("Not a file " + file0 + ' ' + file0.getAbsolutePath()));
                }
            }
            ++i;
        }
        InOut.setX64(v3 == 102 || v3 == 105);
        DaemonLoader.TESTED = new StringBuilder(0);
        DaemonLoader.DEBUG = new StringBuilder(0);
        String s4 = new File(s, DaemonLoader.libName(arr_c[i])).getAbsolutePath();
        Log.d(("Daemon: " + s4));
        DaemonLoader.arch = v3;
        this.extractShLibs(arr_c[i]);
        Tools.allowExecute(s4);
        return s4;
    }

    private static File getInternalDir() {
        File dir;
        File file0 = Tools.getFilesDir();
        File file1 = Tools.getCacheDir();
        String s = Tools.getHiddenDir();
        ArrayList arrayList0 = new ArrayList(0x40);
        arrayList0.add(Tools.getFilesDirHidden());
        arrayList0.add(Tools.getCacheDirHidden());
        arrayList0.add(file0);
        arrayList0.add(file1);
        arrayList0.add(new File(file0.getParentFile(), s));
        arrayList0.add(new File(file1.getParentFile(), s));
        arrayList0.add(file0.getParentFile());
        arrayList0.add(file1.getParentFile());
        String s1 = file0.getAbsolutePath();
        String data = s1.startsWith("/data/user/") ? "/data/user/" + s1.split("/", -1)[3] + "/" : "/data/data/";
        if(Config.vSpaceReal) {
            String data = data + Config.vSpacePkg;
            DaemonLoader.addFiles(arrayList0, data + "/parallel_intl/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/parallel_lite/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/virtual/data/user/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/gaia/data/user/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/com.vmos.glb/osimg/r/ot01/data/data/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/mopen/data/user/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/vbox/data/user/0/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/sandboxdata/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/gameplugins/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data + "/Plugin/0/" + Apk.PACKAGE + "/data/" + Apk.PACKAGE, s, true);
            DaemonLoader.addFiles(arrayList0, data, s, false);
        }
        else {
            DaemonLoader.addFiles(arrayList0, data + Apk.PACKAGE, s, false);
            arrayList0.add(new File("/data/data"));
            arrayList0.add(new File("/data"));
        }
        Iterator iterator0 = arrayList0.iterator();
        while(true) {
            if(!iterator0.hasNext()) {
                Log.w("Failed get dir for fix");
                return null;
            }
            Object object0 = iterator0.next();
            dir = (File)object0;
            if(dir == null) {
                continue;
            }
            if(!dir.exists()) {
                dir.mkdirs();
            }
            if(dir.exists()) {
                break;
            }
            try {
                if(Build.VERSION.SDK_INT < 21) {
                    continue;
                }
                String s4 = dir.getAbsolutePath();
                try {
                    if((Os.stat(s4).st_mode & 0x4000) == 0) {
                        continue;
                    }
                }
                catch(Exception e) {
                    if(e instanceof ErrnoException) {
                        Log.w(("Errno for path \'" + s4 + "\': " + ((ErrnoException)e).errno), e);
                        continue;
                    }
                    Log.w(("Exception for path \'" + s4 + "\'"), e);
                    continue;
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                continue;
            }
            return dir;
        }
        return dir;
    }

    // 去混淆评级： 低(30)
    public static String getStubLib(boolean x64) {
        new StringBuilder("/lib");
        return x64 ? "/libg.so" : "/libi.so";
    }

    public static void initNative() {
        char[] arr_c = Build.VERSION.SDK_INT >= 21 ? new char[]{'\u0000', 'q', 'p', 'r', 's', 't'} : new char[]{'\u0000', 'p', 's', 't', 'q', 'r'};
        String s = DaemonLoader.getDaemonDir();
        int i = 0;
        while(i < 6) {
            if(i == 0) {
                for(int j = 1; j < 6; ++j) {
                    if(new File(s, DaemonLoader.libName(arr_c[j])).exists()) {
                        arr_c[0] = arr_c[j];
                        break;
                    }
                }
                if(arr_c[0] != 0) {
                    goto label_13;
                }
            }
            else {
            label_13:
                File file0 = new File(s, DaemonLoader.libName(arr_c[i]));
                if(!file0.isFile()) {
                    DaemonLoader.extractLib(arr_c[i]);
                }
                if(!file0.isFile()) {
                    Log.d(("Not a file " + file0 + ' ' + file0.getAbsolutePath()));
                    goto label_29;
                }
                String s1 = file0.getAbsolutePath();
                try {
                    System.load(s1);
                }
                catch(Throwable e) {
                    Log.d(("Failed load " + file0 + ' ' + file0.getAbsolutePath()), e);
                    goto label_29;
                }
                try {
                    ProcessBuilder.loaded();
                    return;
                }
                catch(Throwable e) {
                    Log.d(("Failed check " + file0 + ' ' + file0.getAbsolutePath()), e);
                }
            }
        label_29:
            ++i;
        }
    }

    private boolean isValid(char lib, int result) {
        switch(lib) {
            case 0x30: {
                return 101 == result;
            }
            case 49: {
                return 101 == result;
            }
            case 50: {
                return 102 == result;
            }
            case 53: {
                return 105 == result;
            }
            case 55: {
                return 104 == result;
            }
            case 56: {
                return 104 == result;
            }
            case 97: {
                return 103 == result;
            }
            case 98: {
                return 103 == result;
            }
            default: {
                throw new RuntimeException("Unknown lib: " + lib);
            }
        }
    }

    public static boolean isX86() {
        switch(DaemonLoader.getDaemonArch()) {
            case 101: 
            case 102: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private static String libName(char lib) {
        return "lib" + Character.toString(lib) + ".so";
    }

    private static String libName(String lib) [...] // Inlined contents

    private static String makeExecutable(String libPath) {
        for(int v = 0; v < 13; ++v) {
            File file = new File(libPath, DaemonLoader.libName(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'd'}[v]));
            if(file.exists()) {
                String name = file.getAbsolutePath();
                if(!Tools.allowExecute(name)) {
                    Log.d(("Can not execute " + name));
                }
            }
        }
        return libPath;
    }
}

