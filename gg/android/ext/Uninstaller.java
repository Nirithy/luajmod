package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.fix.SparseArray;
import android.internal.XmlUtils;
import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

public class Uninstaller {
    static class Info {
        int code;
        String pkg;
        String ver;

        public Info(String pkg, int code, String ver) {
            this.pkg = pkg;
            this.code = code;
            this.ver = ver;
        }
    }

    static class XmlInputStream extends InputStream {
        private final InputStream base;
        private final byte[] buf;
        private int offset;
        private int total;

        public XmlInputStream(InputStream base) {
            this.base = base;
            this.total = 0;
            this.offset = 0;
            this.buf = new byte[0x200];
        }

        @Override
        public int available() throws IOException {
            return this.base.available();
        }

        @Override
        public void close() throws IOException {
            this.base.close();
        }

        @Override
        public boolean equals(Object obj) {
            return this.base.equals(obj);
        }

        public String getBuf() {
            String s = "buf[" + this.total + "]: ";
            return this.total > this.buf.length ? s + new String(this.buf, this.offset, this.buf.length - this.offset) + new String(this.buf, 0, this.offset) : s + new String(this.buf, 0, this.offset);
        }

        @Override
        public int hashCode() {
            return this.base.hashCode();
        }

        @Override
        public void mark(int readlimit) {
            this.base.mark(readlimit);
        }

        @Override
        public boolean markSupported() {
            return this.base.markSupported();
        }

        @Override
        public int read() throws IOException {
            int v = this.base.read();
            if(v > 0) {
                ++this.total;
                this.buf[this.offset] = (byte)v;
                this.offset = (this.offset + 1) % this.buf.length;
            }
            return v;
        }

        @Override
        public void reset() throws IOException {
            this.base.reset();
        }

        @Override
        public long skip(long n) throws IOException {
            return this.base.skip(n);
        }

        @Override
        public String toString() {
            return this.base.toString();
        }
    }

    private static final String INSTALLED_KEY = "installed";
    public static final int METHODS = 10;
    private static final int MIN_POSSIBLE_APP_LEN = 9;
    private static final String SKIP_OLD = "skip-old";

    public static InputStream cat(String file, int method) {
        String s1 = Tools.getSdcardPath() + "/tmp.file.tmp";
        File file0 = Tools.getContext().getExternalFilesDir(null);
        String tmp = null;
        if(file0 != null) {
            file0.mkdirs();
            if(file0.exists()) {
                tmp = file0.getAbsolutePath() + "/tmp.file.tmp";
            }
        }
        if(tmp == null) {
            tmp = s1;
        }
        String[][] arr2_s = {new String[]{"exec cat " + file, null}, new String[]{"exec dalvikvm -cp " + Tools.getApkPath() + ' ' + "android.ext.Cat" + ' ' + file, null}, new String[]{"exec cp " + file + ' ' + tmp, tmp}, new String[]{"exec cp " + file + ' ' + s1, s1}, new String[]{"exec cp " + file + ' ' + tmp.replace("/emulated/0", "/emulated/legacy"), tmp}};
        if(method >= 5) {
            return null;
        }
        String[] cmd = arr2_s[method];
        Process process0 = RootDetector.tryRoot(cmd[0]);
        if(process0 == null) {
            Log.w(("Failed run " + Arrays.toString(cmd)));
            return null;
        }
        try {
            if(cmd[1] == null) {
                return process0.getInputStream();
            }
            try {
                process0.waitFor();
            }
            catch(InterruptedException e) {
                Log.w(("Wait fail: " + cmd[0]), e);
            }
            return new FileInputStream(cmd[1]) {
                @Override
                public void close() throws IOException {
                    super.close();
                    new File(cmd[1]).delete();
                }

                @Override
                protected void finalize() throws IOException {
                    super.finalize();
                    new File(cmd[1]).delete();
                }
            };
        }
        catch(IOException e) {
            Log.w("Failed read xml", e);
            return null;
        }
    }

    private static void copySharedPrefs(File fromDir, File toDir, String ignore) {
        int cat;
        File file3;
        File from;
        int ls = 0;
        while(ls < 10) {
            InputStream inputStream0 = Uninstaller.ls(fromDir.getAbsolutePath(), ls);
            if(inputStream0 != null) {
                int files = 0;
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(inputStream0));
                try {
                alab1:
                    while(true) {
                        do {
                            do {
                                do {
                                    do {
                                        String s1 = bufferedReader0.readLine();
                                        if(s1 == null) {
                                            break alab1;
                                        }
                                        ++files;
                                        if(s1.endsWith("_preferences.xml") || !s1.endsWith(".xml")) {
                                            continue alab1;
                                        }
                                        from = s1.startsWith("/") ? new File(s1) : new File(fromDir, s1);
                                        String s2 = from.getName();
                                    }
                                    while(s2.equals(ignore) || s2.equals("DefaultStorage.xml"));
                                    file3 = new File(toDir, from.getName());
                                    toDir.mkdirs();
                                }
                                while(file3.exists());
                                cat = 0;
                            label_17:
                            }
                            while(cat >= 10);
                            InputStream inputStream1 = Uninstaller.cat(from.getAbsolutePath(), cat);
                            if(inputStream1 == null) {
                                break;
                            }
                            try {
                                int total = 0;
                                byte[] buffer = new byte[0x2000];
                                FileOutputStream fos = new FileOutputStream(file3);
                                try {
                                    int v5;
                                    while((v5 = inputStream1.read(buffer)) != -1) {
                                        fos.write(buffer, 0, v5);
                                        total += v5;
                                    }
                                }
                                finally {
                                    fos.close();
                                }
                                inputStream1.close();
                            }
                            catch(IOException e) {
                                Log.w(("Failed read data from " + from), e);
                            }
                        }
                        while(total > 0);
                        ++cat;
                        goto label_17;
                    }
                    bufferedReader0.close();
                label_41:
                    while(files > 0) {
                        return;
                    }
                }
                catch(Throwable e) {
                    Log.w(("Failed read list from " + fromDir), e);
                    goto label_41;
                }
            }
            ++ls;
        }
    }

    private static boolean copySharedPrefs(SparseArray sparseArray0) {
        File file0 = Uninstaller.getSharedPrefsFile("null_preferences");
        if(Uninstaller.isInstalled()) {
            return false;
        }
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        HashMap hashMap0 = new HashMap(sharedPreferences0.getAll());
        SharedPreferences.Editor sharedPreferences$Editor0 = sharedPreferences0.edit();
        boolean success = false;
        String s = file0.getAbsolutePath();
        String s1 = Tools.getPackageName();
        File file1 = Uninstaller.getSharedPrefsFile("DefaultStorage");
        if(file1.exists()) {
            if(Uninstaller.copySharedPrefs(s, s1, file0, hashMap0, sharedPreferences$Editor0, s1, true)) {
                success = true;
            }
            file1.delete();
        }
        for(int i = sparseArray0.size() - 1; i >= 0; --i) {
            if(Uninstaller.copySharedPrefs(s, s1, file0, hashMap0, sharedPreferences$Editor0, ((Info)sparseArray0.valueAt(i)).pkg, false)) {
                success = true;
            }
        }
        Log.i(("New: " + Uninstaller.saveMap(sharedPreferences$Editor0, hashMap0) + " from " + hashMap0.size()));
        sharedPreferences$Editor0.putBoolean("installed", true).commit();
        return success;
    }

    private static boolean copySharedPrefs(String currentFile, String currentPackageName, File current, Map map0, SharedPreferences.Editor edit, String packageName, boolean ownOld) {
        String s3 = currentFile.replace(currentPackageName, packageName);
        if(ownOld || !currentFile.equals(s3)) {
            if(!ownOld) {
                Uninstaller.copySharedPrefs(new File(s3).getParentFile(), current.getParentFile(), "null_preferences".replace(currentPackageName, packageName));
            }
            HashMap oldMap = ownOld ? null : Uninstaller.readXml(s3);
            if(oldMap != null) {
                goto label_8;
            }
            oldMap = Uninstaller.readXml(currentFile.replace("null_preferences", "DefaultStorage").replace(currentPackageName, packageName));
            if(oldMap != null) {
            label_8:
                int newKeys = 0;
                for(Object object0: oldMap.entrySet()) {
                    if(!map0.containsKey(((Map.Entry)object0).getKey())) {
                        ++newKeys;
                    }
                }
                Log.i(("Copy: " + Uninstaller.saveMap(edit, oldMap) + " from " + oldMap.size() + " new " + newKeys));
                return true;
            }
        }
        return false;
    }

    public static File getSharedPrefsFile(String name) {
        Context context = MainService.context;
        File file = null;
        if(Build.VERSION.SDK_INT >= 24) {
            try {
                file = (File)context.getClass().getMethod("getSharedPreferencesPath", String.class).invoke(context, name);
                return file == null ? ((File)context.getClass().getMethod("getSharedPrefsFile", String.class).invoke(context, name)) : file;
            }
            catch(Throwable e) {
                try {
                    Log.w("Failed call getSharedPreferencesPath", e);
                    return file == null ? ((File)context.getClass().getMethod("getSharedPrefsFile", String.class).invoke(context, name)) : file;
                }
                catch(Throwable e) {
                }
            }
        }
        else {
            return file == null ? ((File)context.getClass().getMethod("getSharedPrefsFile", String.class).invoke(context, name)) : file;
        }
        Log.w("Failed call getSharedPrefsFile", e);
        return new File(Tools.getFilesDir(), "../shared_prefs/" + name + ".xml");
    }

    public static boolean isInstalled() {
        return Tools.getSharedPreferences().getBoolean("installed", false);
    }

    public static InputStream ls(String path, int method) {
        String[] arr_s = {"exec ls " + path, "exec dalvikvm -cp " + Tools.getApkPath() + ' ' + Ls.class.getName() + ' ' + path};
        if(method >= 2) {
            return null;
        }
        String cmd = arr_s[method];
        Process process0 = RootDetector.tryRoot(cmd);
        if(process0 == null) {
            Log.w(("Failed run " + cmd));
            return null;
        }
        return process0.getInputStream();
    }

    private static final String md5(String s) {
        try {
            MessageDigest messageDigest0 = MessageDigest.getInstance("MD5");
            messageDigest0.update(s.getBytes());
            return Tools.byteArrayToHexString(messageDigest0.digest());
        }
        catch(Throwable e) {
            Log.w("Failed check old versions", e);
            return null;
        }
    }

    public static HashMap readXml(String oldFile) {
        XmlInputStream str;
        HashMap ret = null;
        int method = 0;
        while(method < 10) {
            try {
                InputStream inputStream0 = Uninstaller.cat(oldFile, method);
                if(inputStream0 != null) {
                    str = new XmlInputStream(inputStream0);
                    ret = XmlUtils.readMapXml(str);
                    goto label_11;
                }
                ++method;
                continue;
            }
            catch(IOException e) {
                goto label_16;
            }
            try {
                ret = XmlUtils.readMapXml(str);
                goto label_11;
            }
            catch(Throwable e) {
                try {
                    Log.w(("Fail: " + oldFile), e);
                    Log.w(("Last data: " + str.getBuf()));
                label_11:
                    str.close();
                    goto label_17;
                }
                catch(IOException e) {
                }
            }
        label_16:
            Log.w("Failed read xml", e);
        label_17:
            if(ret != null) {
                break;
            }
            try {
                ++method;
            }
            catch(IOException e) {
                goto label_16;
            }
        }
        if(ret == null) {
            try {
                RootDetector.runCmd(("exec ls -l " + oldFile), 10);
            }
            catch(Throwable e) {
                Log.w("Failed check xml", e);
            }
            return null;
        }
        return ret;
    }

    @TargetApi(11)
    private static int saveMap(SharedPreferences.Editor edit, Map map0) {
        int copy = 0;
        for(Object object0: map0.entrySet()) {
            Map.Entry e = (Map.Entry)object0;
            Object object1 = e.getValue();
            if(object1 instanceof Boolean) {
                edit.putBoolean(((String)e.getKey()), ((Boolean)e.getValue()).booleanValue());
                ++copy;
            }
            if(object1 instanceof Float) {
                edit.putFloat(((String)e.getKey()), ((float)(((Float)e.getValue()))));
                ++copy;
            }
            if(object1 instanceof Integer) {
                edit.putInt(((String)e.getKey()), ((int)(((Integer)e.getValue()))));
                ++copy;
            }
            if(object1 instanceof Long) {
                edit.putLong(((String)e.getKey()), ((long)(((Long)e.getValue()))));
                ++copy;
            }
            if(object1 instanceof String) {
                edit.putString(((String)e.getKey()), ((String)e.getValue()));
                ++copy;
            }
            if(Build.VERSION.SDK_INT >= 11 && object1 instanceof Set) {
                edit.putStringSet(((String)e.getKey()), ((Set)e.getValue()));
                ++copy;
            }
        }
        return copy;
    }

    public static void test() {
    }

    public static void uninstallPackage(String pkg, Runnable callback) {
        if(!Tools.isPackageInstalled(pkg)) {
            Log.d((callback + ": 50"));
            if(callback != null) {
                callback.run();
            }
            return;
        }
        if(callback != null) {
            Log.d((callback + ": 60"));
            if(Build.VERSION.SDK_INT >= 21) {
                try {
                    RootDetector.runCmd(("exec pm uninstall --user -1 " + pkg), 45);
                }
                catch(Throwable e) {
                    Log.e("run cmd fail", e);
                }
            }
            Log.d((callback + ": 70"));
            if(Build.VERSION.SDK_INT < 21 || Tools.isPackageInstalled(pkg)) {
                try {
                    RootDetector.runCmd(("exec pm uninstall " + pkg), 45);
                }
                catch(Throwable e) {
                    Log.e("run cmd fail", e);
                }
            }
            Log.d((callback + ": 80"));
            if(Tools.isPackageInstalled(pkg)) {
                Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F0702A7)).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:need_uninstall "You need to uninstall the installer, otherwise you will have 
                                                                                                                                                                        // 4 icons, not 2. Also games will detect it."
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d((callback + ": 120"));
                        Uninstaller.uninstallPackageWithIntent(pkg);
                        Log.d((callback + ": 130"));
                    }
                }).create(), callback, true);
                return;
            }
            Log.d((callback + ": 110"));
            callback.run();
            return;
        }
        Uninstaller.uninstallPackageWithIntent(pkg);
    }

    static void uninstallPackageWithIntent(String pkg) {
        Intent[] arr_intent = Installer.getUninstallIntent(pkg);
        int v = 0;
        while(v < arr_intent.length) {
            Intent intent = arr_intent[v];
            try {
                Tools.getContext().startActivity(intent);
                return;
            }
            catch(Throwable e) {
                Log.e("intent fail", e);
                ++v;
            }
        }
    }

    public static void uninstallPreviousVersions(BufferReader reader, boolean setRealUids) {
        SparseArray found = new SparseArray();
        reader.reset();
        String s = Tools.getPackageName();
        int v = reader.size();
        int p = 0;
        HashMap realUids = setRealUids ? new HashMap() : null;
        try {
            while(true) {
                int v2 = reader.readInt();
                if(v2 < 0 || v2 > 0x400) {
                    Log.e(("Bad pkg length: " + v2), new RuntimeException());
                    break;
                }
                if(v2 == 0) {
                    break;
                }
                int v3 = reader.readInt();
                int v4 = reader.readInt();
                String s1 = reader.readString(v2, null);
                String s2 = reader.readString(reader.readByte(), null);
                if(setRealUids) {
                    realUids.put(s1, v4);
                }
                if(s.equals(s1)) {
                }
                else if(v3 <= 0) {
                    if(s1.equals("catch_.me_.if_.you_.can_")) {
                        found.put(v / 9 * 0x3E79 + 1, new Info(s1, v3, s2));
                    }
                    String s3 = Uninstaller.md5(s1);
                    for(int i = 33; i >= 0; --i) {
                        if(new String[]{"df1e68bf2355bb69260b6efaa7fdacc1", "f63d4ea576f8d71e0963de6834e0c219", "b333d385ec3aeaaf7598031a42f99961", "f6d10cdcc825ab580f2fbb8ae2752990", "6c188a31fe500c3ada0914215e7df3b3", "4aa610afe8a9f409d98e62b4ed2ef780", "bbc0c87ac43ea5b61f59d6607a1eaf84", "70fb9cca0e11cb09250a11ba0c23b4c2", "b4d8929ffb0484d60369dd2c2e5c919d", "1517a1932503e400deef335e1d331593", "761cf7c032752696b26cd60ea3f3d74d", "1027157cbd6831cfebb80321570bb784", "8791855c8c65cb6a950f34ec0b093668", "a517cf7078e7e9b25ae975b4bdb2cc65", "b4ab3afbb70f23f600d048be0e8d49b2", "bd2173c6afb552fae3d9428756430123", "9bcf2de327544b17352830f9b004b229", "94d8ee3a7d26aa70f473e0ac8845b040", "d6683c02b361fed6c0ece0338921cf2b", "2cba6ddcf12910bc651455907da1fa00", "95760d2d26320be5d407e9823fd089ea", "db2aaa81ea92a69ae45642d0f5142c24", "6e513de086b0378f970968445a5da263", "60ee44a5be7a862f6615d5b68ca0d017", "0e41efb62a276c61f7b2253a1cb70c96", "3534ed8e94149e09012244b0053f387c", "7e146f369544a77f5bd3dcaa498a1e93", "5ff0619a03b5424a273491e365b6356a", "7abdd4c0904fe8f0a78424f971ff52f4", "4be2ea55d617fc0362440a93ae8a0bda", "25a4c4b1be1822ea6b990582e5038364", "b3ea34ef077e6fb86332e03b5321b28c", "ff748cbd537fd83894360f37ac9ccb03", "1353d43dff30877182f5d709e282d224"}[i].equalsIgnoreCase(s3)) {
                            found.put(35 - i + v / 9 * 0x3E79, new Info(s1, v3, s2));
                            break;
                        }
                    }
                }
                else if(v3 <= 0x3E79) {
                    found.put((0x3E79 - v3) * (v / 9) + p, new Info(s1, v3, s2));
                    ++p;
                }
            }
        }
        catch(IOException e) {
            Log.e("???", e);
            if(found.size() == 0) {
                Log.d("Un: what?");
                return;
            }
        }
        if(setRealUids) {
            ProcessList.realUids = realUids;
        }
    }
}

