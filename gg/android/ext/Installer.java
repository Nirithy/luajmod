package android.ext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import android.util.Base64;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ggdqo.ActivityMain;
import com.ggdqo.AnalyticsService;
import com.ggdqo.Application;
import com.ggdqo.BuildConfig;
import com.ggdqo.FileProvider;
import com.ggdqo.Instrumentation;
import com.ggdqo.MainActivity;
import com.ggdqo.R.attr;
import com.ggdqo.R.color;
import com.ggdqo.R.dimen;
import com.ggdqo.R.drawable;
import com.ggdqo.R.id;
import com.ggdqo.R.layout;
import com.ggdqo.R.mipmap;
import com.ggdqo.R.raw;
import com.ggdqo.R.string;
import com.ggdqo.R.style;
import com.ggdqo.R.xml;
import com.ggdqo.R;
import com.ggdqo.Receiver;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jeb.synthetic.FIN;
import jeb.synthetic.TWR;

public class Installer {
    static class Chunk {
        short headerSize;
        int size;
        short type;

        Chunk(ByteBuffer bb) throws IOException {
            bb.position(0);
            this.type = bb.getShort();
            this.headerSize = bb.getShort();
            this.size = bb.getInt();
        }
    }

    static class InstallException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public InstallException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class Item {
        public ZipEntry entry;
        public String extractHash;
        public String putHash;

        public Item(ZipEntry entry, String extractHash) {
            this.entry = entry;
            this.extractHash = extractHash;
        }

        @Override
        public String toString() {
            return "Item [entry=" + this.entry + ", extractHash=" + this.extractHash + ", putHash=" + this.putHash + ']';
        }
    }

    static class NullOutputStream extends OutputStream {
        private NullOutputStream() {
        }

        NullOutputStream(NullOutputStream installer$NullOutputStream0) {
        }

        @Override
        public void write(int b) throws IOException {
        }
    }

    static class RAFInputStream extends InputStream {
        private final RandomAccessFile raf;

        private RAFInputStream(RandomAccessFile raf) throws IOException {
            this.raf = raf;
            raf.seek(0L);
        }

        RAFInputStream(RandomAccessFile randomAccessFile0, RAFInputStream installer$RAFInputStream0) throws IOException {
            this(randomAccessFile0);
        }

        @Override
        public int available() throws IOException {
            int rest = (int)(this.raf.length() - this.raf.getFilePointer());
            return rest <= 0 ? 0 : rest;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void mark(int readlimit) {
            synchronized(this) {
            }
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public int read() throws IOException {
            return this.raf.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return this.raf.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return this.raf.read(b, off, len);
        }

        @Override
        public void reset() throws IOException {
            synchronized(this) {
            }
        }

        @Override
        public long skip(long n) throws IOException {
            long v1 = this.raf.getFilePointer();
            this.raf.seek(v1 + n);
            return n;
        }
    }

    static class RAFOutputStream extends OutputStream {
        private final RandomAccessFile raf;

        private RAFOutputStream(RandomAccessFile raf) throws IOException {
            this.raf = raf;
            raf.seek(0L);
            raf.setLength(0L);
        }

        RAFOutputStream(RandomAccessFile randomAccessFile0, RAFOutputStream installer$RAFOutputStream0) throws IOException {
            this(randomAccessFile0);
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void write(int b) throws IOException {
            this.raf.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            this.raf.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.raf.write(b, off, len);
        }
    }

    class RandomAccessData {
        private ByteBuffer bb;
        private RandomAccessFile raf;
        private final boolean useMem;

        private RandomAccessData(int index) throws IOException {
            this.raf = null;
            this.bb = null;
            this.useMem = Installer.useMemCache[index];
            if(this.useMem) {
                if(Installer.memCache == null || Installer.memCache[index] == null) {
                    throw new InstallException("memCache is null 3: " + index + ' ' + Installer.memCache);
                }
                this.bb = ByteBuffer.wrap(Installer.memCache[index]);
                return;
            }
            this.raf = installer0.getRAF(index);
        }

        RandomAccessData(int v, RandomAccessData installer$RandomAccessData0) throws IOException {
            this(v);
        }

        public void close() throws IOException {
        }

        public int read(byte[] buffer) throws IOException {
            return this.read(buffer, 0, buffer.length);
        }

        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            if(this.useMem) {
                int ret = Math.min(byteCount, this.bb.remaining());
                if(ret != 0) {
                    this.bb.get(buffer, byteOffset, ret);
                    return ret;
                }
                return -1;
            }
            return this.raf.read(buffer, byteOffset, byteCount);
        }

        public void seek(long offset) throws IOException {
            if(this.useMem) {
                this.bb.position(((int)offset));
                return;
            }
            this.raf.seek(offset);
        }

        public void skip(int count) throws IOException {
            if(this.useMem) {
                this.bb.position(this.bb.position() + count);
                return;
            }
            this.raf.skipBytes(count);
        }

        public void write(byte[] buffer) throws IOException {
            if(this.useMem) {
                this.bb.put(buffer);
                return;
            }
            this.raf.write(buffer);
        }

        public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            if(this.useMem) {
                this.bb.put(buffer, byteOffset, byteCount);
                return;
            }
            this.raf.write(buffer, byteOffset, byteCount);
        }
    }

    public static class State implements Parcelable {
        public static final Parcelable.Creator CREATOR;
        String apk;
        String dbg;
        boolean isInstaller;
        boolean manualInstall;
        String newLabel;
        String newPackage;
        int step;
        int waitResult;

        static {
            State.CREATOR = new Parcelable.Creator() {
                public State createFromParcel(Parcel in) {
                    return new State(in);
                }

                @Override  // android.os.Parcelable$Creator
                public Object createFromParcel(Parcel parcel0) {
                    return this.createFromParcel(parcel0);
                }

                public State[] newArray(int size) {
                    return new State[size];
                }

                @Override  // android.os.Parcelable$Creator
                public Object[] newArray(int v) {
                    return this.newArray(v);
                }
            };
        }

        State() {
            this.isInstaller = false;
            this.step = 0;
            this.apk = null;
            this.newPackage = null;
            this.newLabel = null;
            this.manualInstall = false;
            this.waitResult = 0;
            this.dbg = "";
        }

        protected State(Parcel in) {
            boolean z = true;
            super();
            this.isInstaller = false;
            this.step = 0;
            this.apk = null;
            this.newPackage = null;
            this.newLabel = null;
            this.manualInstall = false;
            this.waitResult = 0;
            this.dbg = "";
            this.isInstaller = in.readByte() != 0;
            this.step = in.readInt();
            this.apk = in.readString();
            this.newPackage = in.readString();
            this.newLabel = in.readString();
            if(in.readByte() == 0) {
                z = false;
            }
            this.manualInstall = z;
            this.waitResult = in.readInt();
            this.dbg = in.readString();
        }

        @Override  // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override  // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            int v1 = 1;
            dest.writeByte(((byte)(this.isInstaller ? 1 : 0)));
            dest.writeInt(this.step);
            dest.writeString(this.apk);
            dest.writeString(this.newPackage);
            dest.writeString(this.newLabel);
            if(!this.manualInstall) {
                v1 = 0;
            }
            dest.writeByte(((byte)v1));
            dest.writeInt(this.waitResult);
            dest.writeString(this.dbg);
        }
    }

    static class a {
        public final String a;
        public final String b;
        public final String c;
        public final String d;
        public final String e;
        public final String f;
        public final String g;
        public final String h;
        public final String i;
        public final String k;
        public final String l;
        public final String m;
        public final String n;
        public final String o;
        public final String p;
        public final String r;
        public final String s;
        public final String t;
        public final String u;
        public final String y;

        private a() {
            this.a = "Lcom/ggdqo/ActivityMain===============================";
            this.b = "Lcom/ggdqo/AnalyticsService===============================";
            this.c = "Lcom/ggdqo/Application===============================";
            this.d = "Lcom/ggdqo/BuildConfig===============================";
            this.e = "Lcom/ggdqo/FileProvider===============================";
            this.f = "Lcom/ggdqo/Instrumentation===============================";
            this.g = "Lcom/ggdqo/MainActivity===============================";
            this.h = "Lcom/ggdqo/R$attr===============================";
            this.i = "Lcom/ggdqo/R$color===============================";
            this.k = "Lcom/ggdqo/R$dimen===============================";
            this.l = "Lcom/ggdqo/R$drawable===============================";
            this.m = "Lcom/ggdqo/R$id===============================";
            this.n = "Lcom/ggdqo/R$layout===============================";
            this.o = "Lcom/ggdqo/R$mipmap===============================";
            this.p = "Lcom/ggdqo/R$raw===============================";
            this.r = "Lcom/ggdqo/R$string===============================";
            this.s = "Lcom/ggdqo/R$style===============================";
            this.t = "Lcom/ggdqo/R$xml===============================";
            this.u = "Lcom/ggdqo/R===============================";
            this.y = "Lcom/ggdqo/Receiver===============================";
        }
    }

    private static final int BUFFER_SIZE = 0x10000;
    private static final int DEX_CHECKSUM_OFFSET = 8;
    private static final int DEX_SHA_OFFSET = 12;
    private static final int DEX_SHA_SIZE = 20;
    private static final int FILE_ANDROID_MANIFEST_XML = 1;
    private static final int FILE_CERT_RSA = 5;
    private static final int FILE_CERT_SF = 4;
    private static final int FILE_CLASSES_DEX = 0;
    private static final int FILE_MANIFEST_MF = 3;
    private static final int FILE_NOT_FOUND = -1;
    private static final int FILE_RESOURCES_ARSC = 2;
    private static final String[] FIX_FILES = null;
    private static final String INSTALLER_FILE = "installer.fail";
    private static final int INSTALL_APK = 1;
    private static final String INTENT_FROM_INSTALLER = ".fromInstaller";
    private static final int LABEL = 1;
    private static final int MAX_STEP = 1000;
    public static final byte MODE_32 = 1;
    public static final byte MODE_64 = 2;
    public static final byte MODE_DEFAULT = 0;
    private static final int PACKAGE = 0;
    private static final int REMOVE_APK = 2;
    private static final int SKIP_END = 11604;
    private static final int SKIP_START = 4700;
    private static final int STRING_IDS_OFFSET = 56;
    private static final String TEMP_APK = "temp.apk";
    private static final int VERSION = 2;
    File apk;
    private byte[] buf;
    private static final String chunk3a = "res/raw/chunk3a";
    private static final String chunk3x = "res/raw/chunk3x";
    volatile AlertDialog dialog;
    private static String hash = null;
    static Installer instance = null;
    private final File intDir;
    private static final String lib3a = "lib/armeabi/libAndroid.so";
    private static final String lib3x = "lib/x86/libAndroid.so";
    private static final String lib6a = "lib/arm64-v8a/libAndroid.so";
    private static final String lib6x = "lib/x86_64/libAndroid.so";
    private final Item[] list;
    static byte[][] memCache;
    private Manifest mf;
    static byte mode;
    private final String newLabel;
    private final String newPackage;
    static volatile int nextInstallIntent;
    private final RandomAccessFile[] rafs;
    private static volatile boolean sGrant;
    private final MessageDigest sha1;
    private final MessageDigest sha1_2;
    private static volatile WeakReference source;
    static volatile State state;
    private static final Object syncGetHashes;
    private static final Runnable updater;
    static final boolean[] useMemCache;
    private static volatile int wait;
    public static volatile WeakReference weakBar;
    public static volatile WeakReference weakBarText;
    public static volatile WeakReference weakCancel;
    public static volatile WeakReference weakMsg;

    static {
        Installer.FIX_FILES = new String[]{"classes.dex", "AndroidManifest.xml", "resources.arsc", "META-INF/MANIFEST.MF", "META-INF/CERT.SF", "META-INF/CERT.RSA"};
        Installer.mode = 0;
        Installer.memCache = null;
        Installer.useMemCache = new boolean[]{false, true, false, true, true, true};
        Installer.state = null;
        Installer.weakCancel = new WeakReference(null);
        Installer.updater = new Runnable() {
            @Override
            public void run() {
                Installer.updateProgress();
                Handler handler0 = ThreadManager.getHandlerUiThread();
                handler0.removeCallbacks(this);
                handler0.postDelayed(this, 500L);
            }
        };
        Installer.weakBar = new WeakReference(null);
        Installer.weakBarText = new WeakReference(null);
        Installer.weakMsg = new WeakReference(null);
        Installer.wait = 0;
        Installer.sGrant = false;
        Installer.nextInstallIntent = 0;
        Installer.source = new WeakReference(null);
        Installer.hash = null;
        Installer.syncGetHashes = new Object();
    }

    private Installer() throws NoSuchAlgorithmException {
        this.rafs = new RandomAccessFile[Installer.FIX_FILES.length];
        this.list = new Item[Installer.FIX_FILES.length];
        this.dialog = null;
        this.sha1 = MessageDigest.getInstance("SHA1");
        this.sha1_2 = MessageDigest.getInstance("SHA1");
        File file0 = this.getDir(true);
        file0.mkdirs();
        this.intDir = file0;
        Log.d(("Int dir: " + file0.getAbsolutePath()));
        if(Installer.state.apk == null) {
            File file1 = this.getDir(false);
            file1.mkdirs();
            State installer$State0 = Installer.state;
            installer$State0.apk = new File(file1, "temp.apk").getAbsolutePath();
        }
        this.apk = new File(Installer.state.apk);
        Log.d(("Apk: " + this.apk.getAbsolutePath()));
        if(Installer.state.newPackage == null) {
            State installer$State1 = Installer.state;
            installer$State1.newPackage = this.getNewPackage();
        }
        if(Installer.state.newLabel == null) {
            Installer.state.newLabel = "Gmvm";
        }
        this.newPackage = Installer.state.newPackage;
        this.newLabel = Installer.state.newLabel;
        Log.d(("package: catch_.me_.if_.you_.can_ -> " + this.newPackage));
    }

    private boolean _install() throws Throwable {
        if(Tools.isPackageInstalled(this.newPackage)) {
            Installer.setStep(800);
        }
        if(Installer.state.step < 600) {
            Installer.setStep(0);
            this.extractEntries();
            Installer.setStep(200);
            this.fixEntries();
            Installer.setStep(400);
            this.copyApk();
            Installer.setStep(600);
        }
        if(Installer.state.step < 800) {
            Installer.setStep(600);
            this.deleteFiles();
            State installer$State0 = Installer.state;
            installer$State0.dbg = installer$State0.dbg + this.instApk();
            Installer.setStep(800);
        }
        if(!Tools.isPackageInstalled(this.newPackage)) {
            throw new InstallException("package not installed: " + this.newPackage + '\n' + Installer.state.dbg);
        }
        if(Installer.state.step < 1000) {
            Installer.setStep(800);
            this.setInstaller();
            try {
                this.runNewPackage();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            Installer.setStep(1000);
        }
        try {
            this.uninstallOldPackage();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        return true;
    }

    static void cancelInstall(boolean restart) {
        try {
            Installer.getFile("installer.fail").createNewFile();
        }
        catch(IOException e) {
            Log.e("Failed set install flag", e);
        }
        if(restart) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.restartApp();
                }
            });
        }
    }

    private void checkHash(ZipEntry ze, int index, String hash, String readHash) throws IOException {
        this.sha1.reset();
        DigestInputStream digestInputStream0 = new DigestInputStream(this.getInputStream(index), this.sha1);
        try {
            byte[] arr_b = this.getBuf();
            while(digestInputStream0.read(arr_b) != -1) {
            }
        }
        catch(Throwable throwable0) {
            digestInputStream0.close();
            String check = Base64.encodeToString(this.sha1.digest(), 2);
            Log.d(("check hash for " + ze.getName() + " [" + index + "]: " + check + " : " + readHash + " -> " + hash));
            throw throwable0;
        }
        digestInputStream0.close();
        String check = Base64.encodeToString(this.sha1.digest(), 2);
        Log.d(("check hash for " + ze.getName() + " [" + index + "]: " + check + " : " + readHash + " -> " + hash));
        if(!hash.equals(check)) {
            throw new InstallException("check hash for " + ze.getName() + " [" + index + "]: " + check + " != " + readHash + " -> " + hash);
        }
    }

    private static void closeEntry(StringBuilder hashes, ZipEntry prev) {
        if(prev == null) {
            return;
        }
        hashes.append('-');
        hashes.append(prev.getSize());
        hashes.append(' ');
    }

    private void copyApk() throws IOException {
        FileOutputStream fos;
        FileOutputStream fos = null;
        int i = 0;
        while(true) {
            try {
                if(this.apk.exists()) {
                    this.apk.delete();
                }
                fos = new FileOutputStream(this.apk);
            }
            catch(Throwable e) {
                fos = fos;
                goto label_12;
            }
            try {
                Tools.chmod(this.apk, 0x180);
                break;
            }
            catch(Throwable e) {
            }
        label_12:
            if(i == 1) {
                throw e;
            }
            Log.badImplementation(e);
            State installer$State0 = Installer.state;
            installer$State0.apk = new File(this.intDir, "temp.apk").getAbsolutePath();
            this.apk = new File(Installer.state.apk);
            ++i;
            fos = fos;
        }
        try(ZipInputStream zipInputStream0 = Installer.getZipInputStream()) {
            AlignedZipOutputStream zos = new AlignedZipOutputStream(fos);
            try {
                ZipEntry zipEntry0;
                while((zipEntry0 = zipInputStream0.getNextEntry()) != null) {
                    String s = zipEntry0.getName();
                    boolean isSignFile = false;
                    int v2 = 0;
                label_33:
                    if(v2 < 2) {
                        if(!s.startsWith(new String[]{"res/raw/", "assets/"}[v2])) {
                            ++v2;
                            goto label_33;
                        }
                        else if(!s.startsWith("res/raw/chunk")) {
                            isSignFile = true;
                        }
                        else {
                            ++v2;
                            goto label_33;
                        }
                    }
                    if(!isSignFile) {
                        this.fixEntry(zipEntry0, zipInputStream0, zos);
                    }
                }
            }
            finally {
                zos.close();
            }
        }
        ZipAlign.fixFile(this.apk);
        for(int i = 0; i < this.list.length; ++i) {
            Log.d(("copy " + i + ' ' + this.list[i]));
        }
    }

    private void deleteFiles() throws FileNotFoundException, IOException {
        for(int i = 0; i < this.list.length; ++i) {
            this.getFile(i).delete();
            RandomAccessFile raf = this.rafs[i];
            if(raf != null) {
                try {
                    Log.d(("Close: " + i));
                    raf.close();
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
        }
    }

    // 检测为 Lambda 实现
    void dismissDialog() [...]

    private void extractEntries() throws IOException {
        try(ZipInputStream zipInputStream0 = Installer.getZipInputStream()) {
            ZipEntry zipEntry0;
            while((zipEntry0 = zipInputStream0.getNextEntry()) != null) {
                int v = Installer.getIndex(zipEntry0);
                if(v != -1) {
                    this.list[v] = new Item(new ZipEntry(zipEntry0), this.extractEntry(zipEntry0, zipInputStream0, v));
                    Log.d(("list: " + v));
                }
            }
        }
    }

    private String extractEntry(ZipEntry ze, ZipInputStream zis, int index) throws IOException {
        this.sha1.reset();
        DigestOutputStream digestOutputStream0 = new DigestOutputStream(this.getOutputStream(index), this.sha1);
        this.sha1_2.reset();
        DigestInputStream digestInputStream0 = new DigestInputStream(zis, this.sha1_2);
        try {
            byte[] arr_b = this.getBuf();
            int v1;
            while((v1 = digestInputStream0.read(arr_b)) != -1) {
                digestOutputStream0.write(arr_b, 0, v1);
            }
            digestOutputStream0.flush();
        }
        catch(Throwable throwable0) {
            digestOutputStream0.close();
            String hash = Base64.encodeToString(this.sha1.digest(), 2);
            String readHash = Base64.encodeToString(this.sha1_2.digest(), 2);
            Log.d(("extract " + ze.getName() + ": " + readHash + " -> " + hash));
            throw throwable0;
        }
        digestOutputStream0.close();
        String hash = Base64.encodeToString(this.sha1.digest(), 2);
        String readHash = Base64.encodeToString(this.sha1_2.digest(), 2);
        Log.d(("extract " + ze.getName() + ": " + readHash + " -> " + hash));
        this.checkHash(ze, index, hash, readHash);
        return hash;
    }

    private void fixDex(RandomAccessData raf) throws IOException {
        byte[] buf = new byte[8];
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        raf.seek(56L);
        raf.read(buf, 0, 8);
        byteBuffer0.position(0);
        int v = byteBuffer0.getInt();
        int v1 = byteBuffer0.getInt();
        byte[] arr_b1 = new byte[(v - 0x3FB0) * 4];
        raf.seek(((long)(v1 + 18800)));
        raf.read(arr_b1);
        ByteBuffer bb = ByteBuffer.wrap(arr_b1).order(ByteOrder.LITTLE_ENDIAN);
        int[] arr_v = new int[v - 0x3FB0];
        for(int i = 0; i < v - 0x3FB0; ++i) {
            arr_v[i] = bb.getInt();
        }
        byte[] arr_b2 = new byte[arr_v[arr_v.length - 1] - arr_v[0]];
        raf.seek(((long)arr_v[0]));
        raf.read(arr_b2);
        int v3 = Tools.indexOf(arr_b2, "\u0017Lcar$".getBytes(ParserNumbers.getCharset(false)));
        if(v3 < 0) {
            Log.d("fixDex fail 1");
            return;
        }
        byte[] arr_b3 = (String.valueOf(this.newLabel) + '~').getBytes(ParserNumbers.getCharset(false));
        System.arraycopy(arr_b3, 0, arr_b2, v3 + 6, arr_b3.length);
        int v4 = Arrays.binarySearch(arr_v, arr_v[0] + v3);
        if(v4 < 0) {
            Log.d("fixDex fail 2");
            return;
        }
        int diff = 24 - this.newPackage.length();
        Class[] arr_class = Installer.getCls();
        for(int i = 0; i < arr_class.length; ++i) {
            String s = arr_class[i].getName().replace(Apk.PACKAGE, this.newPackage);
            int v7 = s.length();
            String s1 = s.replace('.', '/');
            bb.putInt((v4 + 2 + 2 * i) * 4, arr_v[v4 + 2 + 2 * i] - diff);
            byte[] put = (((char)(v7 + 2)) + 'L' + s1 + ';' + '\u0000' + ((char)(diff * 2 + (v7 + 2))) + 'L' + s1 + '=' + new String(new char[diff * 2]).replace('\u0000', '=') + '\u0000').getBytes(ParserNumbers.getCharset(false));
            System.arraycopy(put, 0, arr_b2, arr_v[v4 + 1 + i * 2] - arr_v[0], put.length);
        }
        byte[] put = (this.newPackage.replace('.', '/') + new String(new char[diff]).replace('\u0000', '~')).getBytes(ParserNumbers.getCharset(false));
        System.arraycopy(put, 0, arr_b2, arr_v[arr_class.length * 2 + (v4 + 1)] - arr_v[0] + 2, put.length);
        raf.seek(((long)(v1 + v4 * 4 + 18804)));
        raf.write(arr_b1, (v4 + 1) * 4, arr_class.length * 8);
        raf.seek(((long)arr_v[v4]));
        raf.write(arr_b2, arr_v[v4] - arr_v[0], arr_v[arr_class.length * 2 + (v4 + 1) + 1] - arr_v[v4]);
        byte[] arr_b6 = this.getBuf();
        raf.seek(0x20L);
        this.sha1.reset();
        int v8;
        while((v8 = raf.read(arr_b6)) != -1) {
            this.sha1.update(arr_b6, 0, v8);
        }
        raf.seek(12L);
        raf.write(this.sha1.digest());
        Adler32 a32 = new Adler32();
        raf.seek(12L);
        int count;
        while((count = raf.read(arr_b6)) != -1) {
            a32.update(arr_b6, 0, count);
        }
        raf.seek(8L);
        bb.putInt(0, ((int)a32.getValue()));
        raf.write(bb.array(), 0, 4);
    }

    private void fixEntries() throws IOException, GeneralSecurityException {
        for(int i = 0; i < this.list.length; ++i) {
            if(this.list[i] != null) {
                this.fixEntry(this.list[i].entry, i);
            }
        }
    }

    private void fixEntry(ZipEntry ze, int index) throws IOException, GeneralSecurityException {
        OutputStream os = null;
        try {
            switch(index) {
                case 1: {
                    this.fixManifest();
                    break;
                }
                case 0: 
                case 2: {
                    RandomAccessData installer$RandomAccessData0 = new RandomAccessData(this, index, null);
                    if(index == 0) {
                        this.fixDex(installer$RandomAccessData0);
                    }
                    else {
                        this.fixResources(installer$RandomAccessData0);
                    }
                    break;
                }
                case 3: {
                    this.mf = this.getManifest(ze);
                    Map map0 = this.mf.getEntries();
                    for(int i = 0; i < Installer.FIX_FILES.length; ++i) {
                        Attributes attr = (Attributes)map0.get(Installer.FIX_FILES[i]);
                        if(attr != null) {
                            attr.putValue("SHA1-Digest", this.getSHA1(this.getInputStream(i)));
                        }
                    }
                    if(Installer.mode != 0) {
                        map0.put((Installer.mode == 1 ? "lib/armeabi/libAndroid.so" : "lib/arm64-v8a/libAndroid.so"), ((Attributes)map0.remove("res/raw/chunk3a")));
                        map0.put((Installer.mode == 1 ? "lib/x86/libAndroid.so" : "lib/x86_64/libAndroid.so"), ((Attributes)map0.remove("res/raw/chunk3x")));
                    }
                    Manifest manifest0 = this.mf;
                    os = this.getOutputStream(3);
                    manifest0.write(os);
                    break;
                }
                case 4: {
                    if(this.mf != null) {
                        Manifest manifest1 = this.mf;
                        os = this.getOutputStream(4);
                        this.generateSignatureFile(manifest1, os);
                    }
                    break;
                }
                case 5: {
                    os = this.getOutputStream(5);
                    this.writeSignatureBlock(os);
                    break;
                }
                default: {
                    throw new IOException("unknown index: " + index);
                }
            }
        }
        catch(Throwable throwable0) {
            TWR.safeClose$NT(os, throwable0);
            throw throwable0;
        }
        if(os != null) {
            os.close();
        }
    }

    private void fixEntry(ZipEntry ze, ZipInputStream zis, ZipOutputStream zos) throws IOException {
        int v = Installer.getIndex(ze);
        ZipEntry ze = new ZipEntry(ze);
        if(v != -1) {
            this.updateEntry(ze, v);
            if(this.list[v] == null) {
                Log.w(("List: " + v + " is null!"));
                return;
            }
            Item installer$Item0 = this.list[v];
            installer$Item0.putHash = this.putEntry(ze, zos, v);
            return;
        }
        this.putEntry(ze, zis, zos);
    }

    private void fixManifest() throws IOException {
        byte[] data = Installer.memCache[1];
        if(data == null) {
            throw new InstallException("memCache is null 4: 1 " + Installer.useMemCache[1]);
        }
        byte[][] replaces = new byte[14][];
        int[] diff = new int[14];
        int v = 24 - this.newPackage.length();
        diff[0] = v;
        replaces[0] = ("\u0018catch_.me_.if_.you_.can_" + '\u0000' + '\u0005').getBytes(ParserNumbers.getCharset(true));
        replaces[1] = (((char)this.newPackage.length()) + this.newPackage + '\u0000' + ((char)(v + 5)) + new String(new char[v]).replace('\u0000', 'b')).getBytes(ParserNumbers.getCharset(true));
        int ds = 12 - this.newLabel.length();
        diff[2] = ds;
        replaces[2] = ("\fGameGuardian" + '\u0000' + '\u0005').getBytes(ParserNumbers.getCharset(true));
        replaces[3] = (((char)this.newLabel.length()) + this.newLabel + '\u0000' + ((char)(ds + 5)) + new String(new char[ds]).replace('\u0000', 'w')).getBytes(ParserNumbers.getCharset(true));
        replaces[10] = (((char)0x42C00000.length()) + 0x42C00000 + '\u0000').getBytes(ParserNumbers.getCharset(true));
        replaces[11] = ("\u000450.1" + '\u0000').getBytes(ParserNumbers.getCharset(true));
        replaces[12] = new byte[]{8, 0, 0, 16, 0x79, 62, 0, 0};
        replaces[13] = new byte[]{8, 0, 0, 16, -92, 84, 0, 0};
        if(Installer.mode != 0) {
            replaces[4] = new byte[]{44, 0, 2, 0x7F};
            replaces[5] = Installer.intToByteArray(null, 0, (Installer.mode == 1 ? 0x7F030000 : 0x7F030001));  // mipmap:ic_32_48dp
            replaces[6] = new byte[]{4, 0, 3, 0x7F};
            replaces[7] = Installer.intToByteArray(null, 0, (Installer.mode == 1 ? 0x7F030002 : 0x7F030003));  // mipmap:ic_h2_48dp
            replaces[8] = new byte[]{7, 0, 3, 0x7F};
            replaces[9] = Installer.intToByteArray(null, 0, (Installer.mode == 1 ? 0x7F030005 : 0x7F030006));  // mipmap:ic_s2_48dp
        }
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < 14; i += 2) {
            byte[] src = replaces[i];
            if(src != null) {
                int v3 = Tools.indexOf(data, src);
                if(v3 >= 0) {
                    byte[] repl = replaces[i + 1];
                    System.arraycopy(repl, 0, data, v3, repl.length);
                    if(diff[i] != 0) {
                        int v4 = byteBuffer0.getInt(16);
                        int offset = v3 - (byteBuffer0.getInt(28) + 8);
                        for(int j = 0; j < v4; ++j) {
                            int v7 = byteBuffer0.getInt(j * 4 + 36);
                            if(v7 > offset) {
                                break;
                            }
                            if(v7 == offset) {
                                byteBuffer0.putInt((j + 1) * 4 + 36, byteBuffer0.getInt((j + 1) * 4 + 36) - diff[i] * 2);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void fixResources(RandomAccessData raf) throws IOException {
        byte[] buf = new byte[8];
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        raf.seek(0L);
        while(true) {
            if(raf.read(buf) == -1) {
                return;
            }
            Chunk chunk = new Chunk(byteBuffer0);
            if(chunk.type == 0x200) {
                raf.skip(4);
                Charset charset0 = ParserNumbers.getCharset(true);
                raf.write(Arrays.copyOf(this.newPackage.getBytes(charset0), 60));
                return;
            }
            if(chunk.type == 2) {
                raf.skip(chunk.headerSize - 8);
            }
            else {
                raf.skip(chunk.size - 8);
            }
        }
    }

    private void generateSignatureFile(Manifest manifest, OutputStream out) throws IOException {
        out.write("Signature-Version: 1.0\r\n".getBytes());
        out.write("Created-By: 1.0 (Android SignApk)\r\n".getBytes());
        this.sha1.reset();
        PrintStream printStream0 = new PrintStream(new DigestOutputStream(new NullOutputStream(null), this.sha1), true, "UTF-8");
        manifest.write(printStream0);
        printStream0.flush();
        out.write(("SHA1-Digest-Manifest: " + Base64.encodeToString(this.sha1.digest(), 2) + "\r\n\r\n").getBytes());
        for(Object object0: manifest.getEntries().entrySet()) {
            String s = "Name: " + ((String)((Map.Entry)object0).getKey()) + "\r\n";
            printStream0.print(s);
            for(Object object1: ((Attributes)((Map.Entry)object0).getValue()).entrySet()) {
                printStream0.print(((Map.Entry)object1).getKey() + ": " + ((Map.Entry)object1).getValue() + "\r\n");
            }
            printStream0.print("\r\n");
            printStream0.flush();
            out.write(s.getBytes());
            out.write(("SHA1-Digest: " + Base64.encodeToString(this.sha1.digest(), 2) + "\r\n\r\n").getBytes());
        }
        out.flush();
    }

    private byte[] getBuf() {
        byte[] buf = this.buf;
        if(buf == null) {
            buf = new byte[0x10000];
            this.buf = buf;
        }
        return buf;
    }

    private static Class[] getCls() {
        return new Class[]{ActivityMain.class, AnalyticsService.class, Application.class, BuildConfig.class, FileProvider.class, Instrumentation.class, MainActivity.class, attr.class, color.class, dimen.class, drawable.class, id.class, layout.class, mipmap.class, raw.class, string.class, style.class, xml.class, R.class, Receiver.class};
    }

    @SuppressLint({"SdCardPath"})
    @TargetApi(19)
    private File getDir(boolean internal) {
        ArrayList list = new ArrayList();
        if(internal) {
            list.add(Tools.getFilesDir());
            list.add(Tools.getCacheDir());
        }
        try {
            list.add(BaseActivity.context.getExternalCacheDir());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        if(Build.VERSION.SDK_INT >= 19) {
            try {
                File[] arr_file = BaseActivity.context.getExternalCacheDirs();
            label_12:
                for(int v1 = 0; v1 < arr_file.length; ++v1) {
                    list.add(arr_file[v1]);
                }
            }
            catch(Throwable e) {
                Log.e("Fail get path", e);
                if(true) {
                    goto label_19;
                }
                goto label_12;
            }
        }
        try {
        label_19:
            list.add(BaseActivity.context.getExternalFilesDir(null));
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        if(Build.VERSION.SDK_INT >= 19) {
            try {
                File[] arr_file1 = BaseActivity.context.getExternalFilesDirs(null);
            label_25:
                for(int v = 0; v < arr_file1.length; ++v) {
                    list.add(arr_file1[v]);
                }
            }
            catch(Throwable e) {
                Log.e("Fail get path", e);
                if(true) {
                    goto label_32;
                }
                goto label_25;
            }
        }
        try {
        label_32:
            list.add(Environment.getExternalStorageDirectory());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        try {
            list.add(new File("/sdcard/Android/data/" + Tools.getPackageName() + "/files"));
            list.add(new File("/mnt/sdcard/Android/data/" + Tools.getPackageName() + "/files"));
            list.add(new File("/sdcard/Android/data/" + Tools.getPackageName() + "/cache"));
            list.add(new File("/mnt/sdcard/Android/data/" + Tools.getPackageName() + "/cache"));
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        list.add(new File("/sdcard"));
        list.add(new File("/mnt/sdcard"));
        list.add(new File("/data/local/tmp"));
        list.add(Tools.getCacheDir());
        list.add(Tools.getFilesDir());
        StringBuilder debug = new StringBuilder();
        debug.append("Failed getDir: ");
        Iterator iterator0 = list.iterator();
        while(true) {
            if(!iterator0.hasNext()) {
                throw new RuntimeException(debug.toString());
            }
            Object object0 = iterator0.next();
            File dir = (File)object0;
            String s = this.isGoodDir(dir);
            if(s == null) {
                return dir;
            }
            if(dir != null) {
                debug.append(dir.getAbsolutePath());
                debug.append(": ");
                debug.append(s);
                debug.append("; ");
            }
        }
    }

    private File getFile(int index) {
        File file0 = new File(this.intDir, Installer.FIX_FILES[index].replace('/', '@'));
        if(file0.exists()) {
            Tools.chmod(file0, 0x180);
        }
        return file0;
    }

    private static File getFile(String name) {
        File file0 = Tools.getFilesDirHidden();
        file0.mkdirs();
        return new File(file0, name.replace('/', '@'));
    }

    public static String getHashes() {
        String s;
        if(Installer.hash == null) {
            synchronized(Installer.syncGetHashes) {
                if(Installer.hash == null) {
                    long v1 = System.currentTimeMillis();
                    StringBuilder hashes = new StringBuilder();
                    try {
                        ZipFile zipFile0 = new ZipFile(Installer.getSource());
                        ZipEntry prev = null;
                        ZipEntry other = null;
                        long totalSize = 0L;
                        int totalCount = 0;
                        long totalCrc = 0L;
                        int v5 = FIN.finallyOpen$NT();
                        Enumeration enumeration0 = zipFile0.entries();
                        for(ZipEntry last = null; true; last = z ? null : ze) {
                            if(!enumeration0.hasMoreElements()) {
                                Installer.closeEntry(hashes, prev);
                                if(last != null) {
                                    totalSize += last.getSize();
                                    ++totalCount;
                                }
                                if(other != null) {
                                    totalCrc += other.getCrc();
                                }
                                hashes.append(totalSize);
                                hashes.append(' ');
                                hashes.append(totalCount);
                                hashes.append(' ');
                                hashes.append(Long.toHexString(totalCrc));
                                FIN.finallyCodeBegin$NT(v5);
                                zipFile0.close();
                                FIN.finallyCodeEnd$NT(v5);
                                break;
                            }
                            ZipEntry ze = (ZipEntry)enumeration0.nextElement();
                            boolean z = ze.getName().contains("META-INF");
                            int v6 = Installer.getIndex(ze);
                            Installer.closeEntry(hashes, prev);
                            if(last != null) {
                                totalSize += last.getSize();
                                ++totalCount;
                            }
                            if(other != null) {
                                totalCrc += other.getCrc();
                            }
                            prev = null;
                            if(v6 != 0 && v6 != 1 && v6 != 2) {
                                other = z ? null : ze;
                            }
                            else {
                                hashes.append(v6);
                                hashes.append('-');
                                MessageDigest messageDigest0 = MessageDigest.getInstance("SHA-384");
                                long v7 = System.currentTimeMillis();
                                InputStream inputStream0 = zipFile0.getInputStream(ze);
                                switch(v6) {
                                    case 0: {
                                        s = Installer.hashDex(inputStream0, messageDigest0);
                                        break;
                                    }
                                    case 2: {
                                        s = Installer.hashResources(inputStream0, messageDigest0);
                                        break;
                                    }
                                    default: {
                                        s = Installer.hashManifest(inputStream0, messageDigest0);
                                    }
                                }
                                Log.d(("HH: " + v6 + " = " + (System.currentTimeMillis() - v7)));
                                hashes.append(ze.getMethod());
                                hashes.append('-');
                                hashes.append(ze.getTime());
                                hashes.append('-');
                                hashes.append(s);
                                prev = ze;
                                other = null;
                            }
                        }
                    }
                    catch(Throwable e) {
                        Log.e("Failed get hashes", e);
                        hashes.append("e:");
                        hashes.append(e);
                    }
                    Installer.hash = hashes.toString();
                    Log.d(("HH: " + (System.currentTimeMillis() - v1)));
                }
            }
        }
        return Installer.hash;
    }

    private static int getIndex(ZipEntry ze) {
        String s = ze.getName();
        for(int i = 0; true; ++i) {
            if(i >= Installer.FIX_FILES.length) {
                return -1;
            }
            if(Installer.FIX_FILES[i].equals(s)) {
                return i;
            }
        }
    }

    private InputStream getInputStream(int index) throws IOException {
        if(Installer.useMemCache[index]) {
            if(Installer.memCache == null || Installer.memCache[index] == null) {
                throw new InstallException("memCache is null 1: " + index + ' ' + Installer.memCache);
            }
            return new ByteArrayInputStream(Installer.memCache[index]);
        }
        Log.d(("getInputStream: " + index));
        return new RAFInputStream(this.getRAF(index), null);
    }

    private Intent[] getInstallIntent() {
        return Installer.getInstallIntent(this.apk);
    }

    @TargetApi(14)
    public static Intent[] getInstallIntent(File apk) {
        int i;
        boolean newAPI = Build.VERSION.SDK_INT >= 14;
        Intent[] arr = new Intent[(newAPI ? 4 : 2)];
        int Q = Build.VERSION.SDK_INT < 24 ? 0 : 1;
        boolean grant = Installer.sGrant;
        int i = 0;
        for(int j = 0; j < 2; ++j) {
            int file = (j == 0 ? 1 : 0) ^ Q;
            Uri uri0 = file == 0 ? FileProvider.getUriForFile(Tools.getContext(), Apk.PACKAGE, apk) : Uri.fromFile(apk);
            if(newAPI) {
                Intent intent0 = new Intent("android.intent.action.INSTALL_PACKAGE");
                intent0.setDataAndType(uri0, "application/vnd.android.package-archive");
                intent0.addFlags(1);
                intent0.putExtra("android.intent.extra.NOT_UNKNOWN_SOURCE", true);
                intent0.putExtra("android.intent.extra.RETURN_RESULT", true);
                i = i + 1;
                arr[i] = Installer.grant(intent0, !grant && file == 0);
            }
            else {
                i = i;
            }
            Intent intent1 = new Intent("android.intent.action.VIEW");
            intent1.setDataAndType(uri0, "application/vnd.android.package-archive");
            intent1.addFlags(1);
            i = i + 1;
            arr[i] = Installer.grant(intent1, !grant && file == 0);
        }
        Installer.sGrant = true;
        return arr;
    }

    private long getLength(int index) throws IOException {
        if(Installer.useMemCache[index]) {
            if(Installer.memCache == null || Installer.memCache[index] == null) {
                throw new InstallException("memCache is null 2: " + index + ' ' + Installer.memCache);
            }
            return (long)Installer.memCache[index].length;
        }
        return this.getRAF(index).length();
    }

    private Manifest getManifest(ZipEntry ze) throws FileNotFoundException, IOException {
        Manifest m;
        try {
            this.sha1.reset();
            DigestInputStream digestInputStream0 = new DigestInputStream(this.getInputStream(3), this.sha1);
            try {
                m = new Manifest(digestInputStream0);
            }
            finally {
                digestInputStream0.close();
            }
            Log.d(("Manifest: " + Base64.encodeToString(this.sha1.digest(), 2) + " -> " + this.list[3]));
            return m;
        }
        catch(IOException e) {
            throw new IOException("Failed get manifest: " + Base64.encodeToString(this.sha1.digest(), 2) + " -> " + this.list[3], e);
        }
    }

    private String getNewPackage() {
        while(Tools.isPackageInstalled("com.qpabrrgny")) {
        }
        return "com.qpabrrgny";
    }

    private static String getNewString(int mode) [...] // 潜在的解密器

    private OutputStream getOutputStream(int index) throws IOException {
        OutputStream os = null;
        boolean useMem = Installer.useMemCache[index];
        if(!useMem) {
            try {
                Log.d(("getOutputStream: " + index));
                os = new RAFOutputStream(this.getRAF(index), null);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            if(os == null) {
                Log.d(("Use mem cache for " + index));
                useMem = true;
                Installer.useMemCache[index] = true;
            }
        }
        if(useMem) {
            switch(index) {
                case 1: {
                    return new ByteArrayOutputStream(9000) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            byte[][] cache = Installer.memCache;
                            if(cache != null) {
                                byte[] arr_b = this.toByteArray();
                                if(arr_b == null) {
                                    Log.badImplementation(new RuntimeException("toByteArray return null"));
                                }
                                cache[1] = arr_b;
                            }
                        }
                    };
                }
                case 3: {
                    return new ByteArrayOutputStream(75000) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            byte[][] cache = Installer.memCache;
                            if(cache != null) {
                                byte[] arr_b = this.toByteArray();
                                if(arr_b == null) {
                                    Log.badImplementation(new RuntimeException("toByteArray return null"));
                                }
                                cache[3] = arr_b;
                            }
                        }
                    };
                }
                case 4: {
                    return new ByteArrayOutputStream(75000) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            byte[][] cache = Installer.memCache;
                            if(cache != null) {
                                byte[] arr_b = this.toByteArray();
                                if(arr_b == null) {
                                    Log.badImplementation(new RuntimeException("toByteArray return null"));
                                }
                                cache[4] = arr_b;
                            }
                        }
                    };
                }
                case 5: {
                    return new ByteArrayOutputStream(2000) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            byte[][] cache = Installer.memCache;
                            if(cache != null) {
                                byte[] arr_b = this.toByteArray();
                                if(arr_b == null) {
                                    Log.badImplementation(new RuntimeException("toByteArray return null"));
                                }
                                cache[5] = arr_b;
                            }
                        }
                    };
                }
                default: {
                    return new ByteArrayOutputStream(0x2000) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            byte[][] cache = Installer.memCache;
                            if(cache != null) {
                                byte[] arr_b = this.toByteArray();
                                if(arr_b == null) {
                                    Log.badImplementation(new RuntimeException("toByteArray return null"));
                                }
                                cache[index] = arr_b;
                            }
                        }
                    };
                }
            }
        }
        return os;
    }

    private static PrivateKey getPrivateKey() throws GeneralSecurityException {
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec0 = new PKCS8EncodedKeySpec(Base64.decode("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWkxkE3sYLJLHtx2Lg2dglPj7NbOsd4v8GjKjovKjNa9N4bqcKp2zmDrsPmTVZ/9k+d6lD5+g9S2S45P6i0+ZW8eJnqBu/sjC1eMIEQ75Mchi4RvUhFYbwOKFOicK+OH+Ovs+PysPaHuMwyeqT0KfD3ErzUCINUAgHMuCAlxfuagUzWeamlOwss/KEoKRmyHqU2DsxCTpnNy4vZBLAbm1C8VgY3/4DgcwM1ETabN3DuCRYGUgBsyVkE0+/3pjJKHdI2/VnalQNgVTIu8oHueJHVTMRxGua92/e7MyOaefIotCOeCYglD+Zcn08BP5ymR2Z35uuOKCyF3+jHVtq/ukfAgEDAoIBAQCPDLtYlIQHbcvz2kHrO+VuKX8znfIT7KoEXcXwfcXeR+JQScSxxPNECdIKZiORVTt++nDX7/Ao3O3QmKnB4pmPS+xFGr0qdssjpdatgn7doWXQL04WDln1exY0W9cpev+0fzUKhy08FJd12/G34G/X6DH3isFeNVqvd0BVug/0RXWihnmONcUztAJ25E5YNqHadWSt+vU4pJOpvxDyE6ZXrBIpHBvlaZf8atJ7maf8iXfSZUzrqnx1O5zaTGRnGo7o/UdrfuLDfpVXnXBEHm+rk6QTq2ZKyZj6JZQ/K1LB+cXqZO9KG8oBSecXohQBeJYIDEikB9xHdsvelr1MoYR7AoGBAOrAmRccm5UnjAe/npdFGIVXkXaep7Ur9rqT4NaoSMSnDRim6Kii2lNoZ2szvvKYuxRNmvi1u60iRvQsLM10duqyG+FKdx+S5632ALWTKvdH97l3VYcRCrDYAyMYdotYavF8bcT9QKgYHoWHb18KLL27A4afIXmrVXCnWXp1e2GbAoGBAOn+9xk0qK83mecSq5edXgJ1lq2NaRVmSZYc5KKtCC8YYiQ0TSuIiRSpzJ3tR28wLtxO5lvqd72R8vBMPzS6CbY5RCj7tOBVW8bPTuwOYUN+AAN87csZvlmPsUsXMmBNQTYycvo0Keh/ZR0RIoFmN37SyagZC1ybj90t4cUCkUDNAoGBAJyAZg9oZ7jFCAUqabouEFjlC6RpxSNypHxileRwMIMaCLsZ8HBskYzwRPIif0xl0g2JEfsj0nNsL01yyIj4T0chZ+uG+hUMmnP5Vc5iHKTapSZPjloLXHXlV2y6+bI68fZS89io1cVlaa5aSj9cHdPSAlm/a6ZyOPXE5lGjp5ZnAoGBAJv/T2YjGx96ZpoMcmUTlAGjuckI8Lju27lomGxzWsoQQW14M3JbBg3GiGlI2kogHz2J7ufxpSkL90rdf3h8Bnl7gsX9I0A459nfifK0QNepVVeonodmfuZfy4dkzEAzgM7MTKbNcUWqQ2i2FwDuz6nh28VmB5MSX+jJQS4BtiszAoGAYyqt2RrdpGLZlaZyYlsFzalGIfTpWXPuj5ot63Ghwawb0xoN1qKJdYcbanvrblVhtKEsYKOkae96d1grNcf4Vbm3bMrPwHdIRf6pRS+x46mMBfuap1JoGcXESY4NwdsbpYo71PuBgykeNHaO2nq0BYcm/RyNFHuJZd+PFfOevDc=", 0));
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(pKCS8EncodedKeySpec0);
        }
        catch(InvalidKeySpecException unused_ex) {
            return KeyFactory.getInstance("DSA").generatePrivate(pKCS8EncodedKeySpec0);
        }
    }

    RandomAccessFile getRAF(int index) throws IOException {
        RandomAccessFile raf = this.rafs[index];
        if(raf == null) {
            raf = new RandomAccessFile(this.getFile(index), "rw");
            Log.d(("RAF new: " + index + ' ' + raf.getFD()));
            this.rafs[index] = raf;
        }
        Log.d(("RAF get: " + index + ' ' + raf.getFD()));
        return raf;
    }

    private String getSHA1(InputStream is) throws IOException {
        this.sha1.reset();
        try(DigestInputStream digestInputStream0 = new DigestInputStream(is, this.sha1)) {
            byte[] arr_b = this.getBuf();
            while(true) {
                if(digestInputStream0.read(arr_b) == -1) {
                    break;
                }
            }
        }
        return Base64.encodeToString(this.sha1.digest(), 2);
    }

    private static File getSource() {
        File file = (File)Installer.source.get();
        if(file == null) {
            file = new File(Tools.getApkPath());
            Installer.source = new WeakReference(file);
        }
        return file;
    }

    static State getState() {
        if(Installer.state == null) {
            State st = new State();
            st.isInstaller = Installer.needInstall();
            Installer.state = st;
        }
        return Installer.state;
    }

    @TargetApi(14)
    public static Intent[] getUninstallIntent(String pkg) {
        boolean newAPI = Build.VERSION.SDK_INT >= 14;
        Intent[] arr = new Intent[(newAPI ? 2 : 1)];
        int i = 0;
        if(newAPI) {
            Intent intent0 = new Intent("android.intent.action.UNINSTALL_PACKAGE");
            intent0.setData(Uri.fromParts("package", pkg, null));
            intent0.putExtra("android.intent.extra.RETURN_RESULT", true);
            intent0.putExtra("android.intent.extra.UNINSTALL_ALL_USERS", true);
            arr[0] = intent0;
            i = 1;
        }
        Intent intent1 = new Intent("android.intent.action.DELETE");
        intent1.setData(Uri.fromParts("package", pkg, null));
        arr[i] = intent1;
        return arr;
    }

    private static ZipInputStream getZipInputStream() throws FileNotFoundException {
        return new ZipInputStream(new BufferedInputStream(new FileInputStream(Installer.getSource()), 0x10000));
    }

    private static Intent grant(Intent intent, boolean grant) {
        if(grant) {
            Context context0 = Tools.getContext();
            try {
                for(Object object0: Tools.getPackageManager().queryIntentActivities(intent, 0x10000)) {
                    ResolveInfo resolveInfo = (ResolveInfo)object0;
                    try {
                        context0.grantUriPermission(resolveInfo.activityInfo.packageName, intent.getData(), 1);
                    }
                    catch(Throwable e) {
                        Log.w(("Failed grant 0 " + intent + "; " + resolveInfo), e);
                    }
                }
            }
            catch(Throwable e) {
                Log.w(("Failed grant 1 " + intent), e);
            }
            try {
                context0.grantUriPermission("com.android.packageinstaller", intent.getData(), 1);
            }
            catch(Throwable e) {
                Log.w(("Failed grant 2 " + intent), e);
            }
            return intent;
        }
        return intent;
    }

    private static int hash(InputStream is, MessageDigest md, byte[] buffer, int count) throws IOException {
        int ret = count;
        while(count > 0) {
            int v2 = is.read(buffer, 0, (count <= buffer.length ? count : buffer.length));
            if(v2 <= 0) {
                break;
            }
            count -= v2;
            md.update(buffer, 0, v2);
        }
        return ret;
    }

    public static final String hashDex(InputStream is, MessageDigest md) {
        try {
            byte[] buffer = new byte[0x2000];
            ByteBuffer byteBuffer0 = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
            int v = Installer.read(is, buffer, 0, 0x40);
            md.update(buffer, 56, 8);
            byteBuffer0.position(56);
            int v1 = byteBuffer0.getInt();
            int v2 = Installer.hash(is, md, buffer, byteBuffer0.getInt() + 18800 - v);
            byte[] string_ids_buf = new byte[(v1 - 0x3FB0) * 4];
            int v3 = Installer.read(is, string_ids_buf);
            ByteBuffer bb = ByteBuffer.wrap(string_ids_buf).order(ByteOrder.LITTLE_ENDIAN);
            int v4 = bb.getInt(0);
            int v5 = bb.getInt((v1 - 0x3FB1) * 4);
            Log.d(("DBG: " + v3 + "; " + string_ids_buf.length + ", " + v4 + "; " + v5));
            byte[] string_data_items = new byte[v5 - v4];
            Installer.hash(is, md, buffer, v4 - (v + v2 + v3));
            Installer.read(is, string_data_items);
            int v6 = Tools.indexOf(string_data_items, "\u0017Lcar$".getBytes(ParserNumbers.getCharset(false)));
            md.update(string_data_items, 0, v6);
            if(string_data_items.length > v6 + 1700) {
                md.update(string_data_items, v6 + 1700, string_data_items.length - (v6 + 1700));
            }
            Installer.hash(is, md, buffer, 0x7FFFFFFF);
            return Hash.toString(md);
        }
        catch(Throwable e) {
            Log.e("hash fail", e);
            return e.toString();
        }
    }

    public static final String hashManifest(InputStream is, MessageDigest md) {
        int len;
        String version;
        int build;
        try {
            byte[] data = new byte[0x4000];
            int v = Installer.read(is, data);
            try {
                PackageInfo packageInfo0 = Tools.getPackageInfo(Tools.getPackageName());
                build = packageInfo0.versionCode;
                version = packageInfo0.versionName;
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                build = 0x3E79;
                version = 96.0f;
            }
            byte[][] arr2_b = {("\u0000" + ((char)Apk.PACKAGE.length()) + Apk.PACKAGE + '\u0000').getBytes(ParserNumbers.getCharset(true)), ("\u0000" + ((char)Apk.LABEL.length()) + Apk.LABEL + '\u0000').getBytes(ParserNumbers.getCharset(true)), ("\u0000" + ((char)version.length()) + version + '\u0000').getBytes(ParserNumbers.getCharset(true)), new byte[]{44, 0, 2, 0x7F}, new byte[]{0, 0, 3, 0x7F}, new byte[]{1, 0, 3, 0x7F}, new byte[]{4, 0, 3, 0x7F}, new byte[]{2, 0, 3, 0x7F}, new byte[]{3, 0, 3, 0x7F}, new byte[]{7, 0, 3, 0x7F}, new byte[]{5, 0, 3, 0x7F}, new byte[]{6, 0, 3, 0x7F}, Installer.intToByteArray(Installer.intToByteArray(null, 4, build), 0, 0x10000008)};
            for(int i = 0; true; ++i) {
                if(i >= 13) {
                    int v3 = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getInt(16);
                    System.arraycopy(new byte[v3 * 4], 0, data, 36, v3 * 4);
                    md.update(data, 0, v);
                    return Hash.toString(md);
                }
                byte[] src = arr2_b[i];
                int v4 = Tools.indexOf(data, src);
                if(v4 >= 0) {
                    switch(i) {
                        case 0: {
                            len = 56;
                            break;
                        }
                        case 1: {
                            len = 0x20;
                            break;
                        }
                        default: {
                            len = src.length;
                        }
                    }
                    System.arraycopy(new byte[len], 0, data, v4, len);
                }
            }
        }
        catch(Throwable e) {
            Log.e("hash fail", e);
            return e.toString();
        }
    }

    public static final String hashResources(InputStream is, MessageDigest md) {
        int read;
        try {
            byte[] buffer = new byte[0x2000];
            ByteBuffer byteBuffer0 = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
            while(true) {
                if(Installer.read(is, buffer, 0, 8) == -1) {
                    return Hash.toString(md);
                }
                md.update(buffer, 0, 8);
                Chunk chunk = new Chunk(byteBuffer0);
                if(chunk.type == 0x200) {
                    byte[] name = new byte[260];
                    Installer.read(is, name, 0, 4);
                    md.update(name);
                    Installer.read(is, name, 4, 0x100);
                    read = chunk.size - 0x10C;
                }
                else {
                    read = chunk.type == 2 ? chunk.headerSize - 8 : chunk.size - 8;
                }
                Installer.hash(is, md, buffer, read);
            }
        }
        catch(Throwable e) {
            Log.e("hash fail", e);
            return e.toString();
        }
    }

    private static void initProgressBar() {
        Button cancel = (Button)Installer.weakCancel.get();
        if(cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override  // android.view.View$OnClickListener
                public void onClick(View v) {
                    Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F070159)).setPositiveButton(0x7F07009B, new DialogInterface.OnClickListener() {  // string:install_cancel_prompt "__app_name__ can be detected when not installed. Do 
                                                                                                                                                                      // you really want to cancel?"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Installer.cancelInstall(true);
                        }
                    }).setNegativeButton(0x7F07009C, null).setCancelable(false), BaseActivity.instance);  // string:no "No"
                }
            });
        }
        ProgressBar bar = (ProgressBar)Installer.weakBar.get();
        if(bar != null) {
            bar.setIndeterminate(false);
        }
        Installer.updater.run();
    }

    private String instApk() throws IOException, InterruptedException {
        int v = 0;
        String s = this.apk.getAbsolutePath();
        String[] arr_s = s.contains("/emulated/0") ? new String[]{s.replace("/emulated/0", "/emulated/legacy"), s} : new String[]{s};
        for(int v1 = 0; v1 < arr_s.length; ++v1) {
            String path = arr_s[v1];
            try {
                Tools.chmod(new File(path), 493);
            }
            catch(Throwable e) {
                Log.w(("Failed allow execute 1: " + path), e);
            }
            try {
                Tools.chmod(path, "0755");
            }
            catch(Throwable e) {
                Log.w(("Failed allow execute 2: " + path), e);
            }
        }
        String dbg = "Install:";
        for(int v2 = 0; v2 < arr_s.length; ++v2) {
            String path = arr_s[v2];
            try {
                dbg = String.valueOf(dbg) + '\n' + RootDetector.runCmd(("exec pm install -f " + path), 45);
            }
            catch(Throwable e) {
                Log.e("run cmd fail", e);
                dbg = String.valueOf(dbg) + '\n' + e.getMessage();
            }
            if(Tools.isPackageInstalled(this.newPackage)) {
                break;
            }
        }
        if(!Tools.isPackageInstalled(this.newPackage)) {
            Installer.state.manualInstall = true;
            Intent[] arr_intent = this.getInstallIntent();
            Installer.nextInstallIntent = 0;
            while(v < arr_intent.length) {
                Intent intent = arr_intent[v];
                ++Installer.nextInstallIntent;
                dbg = dbg + "\nManual: " + intent;
                try {
                    this.waitForResult(intent, 1);
                    return dbg;
                }
                catch(Throwable e) {
                    dbg = String.valueOf(dbg) + '\n' + e.getMessage();
                    ++v;
                }
            }
        }
        return dbg;
    }

    // 检测为 Lambda 实现
    static void install() [...]

    private static byte[] intToByteArray(byte[] ret, int offset, int a) [...] // 潜在的解密器

    private String isGoodDir(File dir) {
        if(dir == null) {
            Log.d(("path: " + null + " - null"));
            return "is null";
        }
        dir.mkdirs();
        try {
            File file1 = new File(dir, "temp.apk");
            file1.delete();
            byte[] arr_b = "We need test read/write to this file".getBytes();
            byte[] read = new byte[arr_b.length];
            FileOutputStream os = new FileOutputStream(file1);
            Tools.chmod(file1, 0x180);
            os.write(arr_b);
            os.close();
            FileInputStream is = new FileInputStream(file1);
            int v = is.read(read);
            is.close();
            if(v != arr_b.length) {
                return "Length mismatch: " + v + " != " + arr_b.length;
            }
            if(!Arrays.equals(read, arr_b)) {
                return "Data mismatch: \'" + new String(read) + "\' != \'" + new String(arr_b) + '\'';
            }
            file1.delete();
            Log.d(("path: " + dir + " - success"));
            return null;
        }
        catch(IOException e) {
            Log.d(("path: " + dir + " - fail: " + e.getMessage()));
            return e.getMessage();
        }
    }

    public static boolean needInstall() {
        Installer.setVersion();
        try {
            if(new File("/data/GG.skip.install").exists()) {
                return false;
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return "catch_.me_.if_.you_.can_".equals(Tools.getPackageName()) && !Installer.getFile("installer.fail").exists();
        }
        return "catch_.me_.if_.you_.can_".equals(Tools.getPackageName()) && !Installer.getFile("installer.fail").exists();
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        State state = Installer.state;
        Log.d(("instance: " + Installer.instance + "; " + state));
        if(state != null) {
            state.dbg = state.dbg + "\nget: " + resultCode + " for: " + requestCode + " with: " + intent;
            if(Installer.instance != null) {
                switch(requestCode) {
                    case 1: {
                        if(Tools.isPackageInstalled(Installer.instance.newPackage)) {
                            state.waitResult &= -2;
                            return;
                        }
                        Installer.promptRetry((Re.s(0x7F070153) + "\n\n" + Tools.stripColon(0x7F070257) + ":\n" + Installer.instance.apk.getAbsolutePath()), 1, resultCode, Installer.instance.getInstallIntent());  // string:failed_install "Failed to install app. Retry?"
                        return;
                    }
                    case 2: {
                        Installer.promptRetry(Re.s(0x7F070154), 2, resultCode, Installer.getUninstallIntent("catch_.me_.if_.you_.can_"));  // string:failed_uninstall "Failed to uninstall app. Retry?"
                    }
                }
            }
        }
    }

    private static void promptRetry(String message, int requestCode, int resultCode, Intent[] intents) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.ext.Installer.7.1 installer$7$10 = new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        int i = -1;
                        switch(which) {
                            case -2: {
                                Installer.state.waitResult &= ~this.val$requestCode;
                                break;
                            }
                            case -1: {
                                int next = this.val$requestCode == 1 ? Installer.nextInstallIntent % this.val$intents.length : 0;
                                Intent[] arr_intent = this.val$intents;
                                for(int v2 = 0; v2 < arr_intent.length; ++v2) {
                                    Intent intent = arr_intent[v2];
                                    ++i;
                                    if(i >= next) {
                                        try {
                                            if(this.val$requestCode == 1) {
                                                ++Installer.nextInstallIntent;
                                            }
                                            BaseActivity.instance.startActivityForResult(intent, this.val$requestCode);
                                            break;
                                        }
                                        catch(Throwable e) {
                                            Log.e("intent fail", e);
                                        }
                                    }
                                }
                            }
                        }
                        Installer.instance.dialog = null;
                    }
                };
                Installer.instance.dismissDialog();
                AlertDialog alertDialog0 = Alert.create(BaseActivity.context).setMessage("0: [" + resultCode + "] " + message).setPositiveButton(0x7F07009B, installer$7$10).setNegativeButton(0x7F07009C, installer$7$10).setCancelable(false).create();  // string:yes "Yes"
                Alert.show(alertDialog0);
                Installer.instance.dialog = alertDialog0;
            }
        });
    }

    private String putEntry(ZipEntry ze, ZipOutputStream zos, int index) throws IOException {
        int v2;
        byte[] arr_b;
        zos.putNextEntry(ze);
        this.sha1.reset();
        DigestInputStream digestInputStream0 = new DigestInputStream(this.getInputStream(index), this.sha1);
        long write = 0L;
        try {
            arr_b = this.getBuf();
            while(true) {
            label_5:
                v2 = digestInputStream0.read(arr_b);
                switch(v2) {
                    case -1: {
                        goto label_8;
                    }
                    case 0: {
                    }
                }
            }
        }
        catch(Throwable throwable0) {
            goto label_18;
        }
        goto label_14;
    label_8:
        digestInputStream0.close();
        String hash = Base64.encodeToString(this.sha1.digest(), 2);
        Log.d(("put " + ze.getName() + ": " + hash + "; " + write));
        zos.flush();
        zos.closeEntry();
        return hash;
        try {
        label_14:
            zos.write(arr_b, 0, v2);
            write += (long)v2;
        }
        catch(Throwable throwable0) {
        label_18:
            digestInputStream0.close();
            String hash = Base64.encodeToString(this.sha1.digest(), 2);
            Log.d(("put " + ze.getName() + ": " + hash + "; " + write));
            throw throwable0;
        }
        goto label_5;
    }

    private void putEntry(ZipEntry ze, ZipInputStream zis, ZipOutputStream zos) throws IOException {
        if(ze.getMethod() != 0) {
            ze.setCompressedSize(-1L);
        }
        if(Installer.mode != 0) {
            String s = ze.getName();
            String replace = null;
            if("res/raw/chunk3a".equals(s)) {
                replace = Installer.mode == 1 ? "lib/armeabi/libAndroid.so" : "lib/arm64-v8a/libAndroid.so";
            }
            if("res/raw/chunk3x".equals(s)) {
                replace = Installer.mode == 1 ? "lib/x86/libAndroid.so" : "lib/x86_64/libAndroid.so";
            }
            if(replace != null) {
                try {
                    Field field0 = ZipEntry.class.getDeclaredField("name");
                    field0.setAccessible(true);
                    field0.set(ze, replace);
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
        }
        zos.putNextEntry(ze);
        byte[] arr_b = this.getBuf();
        int v;
        while((v = zis.read(arr_b)) != -1) {
            zos.write(arr_b, 0, v);
        }
        zos.flush();
        zos.closeEntry();
    }

    private static int read(InputStream is, byte[] buffer) throws IOException {
        return Installer.read(is, buffer, 0, buffer.length);
    }

    private static int read(InputStream is, byte[] buffer, int offset, int count) throws IOException {
        for(int read = 0; true; read += v3) {
            if(read >= count) {
                return read;
            }
            int v3 = is.read(buffer, offset + read, count - read);
            if(v3 <= 0) {
                return read == 0 ? -1 : read;
            }
        }
    }

    public static void removeInstaller(Runnable callback) {
        boolean fromIntent = false;
        Intent intent0 = BaseActivity.instance.getIntent();
        if(intent0 != null && intent0.getBooleanExtra("catch_.me_.if_.you_.can_.fromInstaller", false)) {
            fromIntent = true;
        }
        Log.d(("removeInstaller: " + fromIntent + " + " + BaseActivity.removeInstaller));
        if(BaseActivity.removeInstaller || fromIntent) {
            BaseActivity.removeInstaller = true;
            Log.d((callback + ": 30"));
            Uninstaller.uninstallPackage("catch_.me_.if_.you_.can_", callback);
            Log.d((callback + ": 40"));
            return;
        }
        callback.run();
    }

    private void runNewPackage() {
        Context context0 = BaseActivity.context;
        String s = "com.ggdqo.ActivityMain".replace(Tools.getPackageName(), this.newPackage);
        Intent intent0 = Tools.getStartIntent(context0, this.newPackage, s);
        if(!Uninstaller.isInstalled()) {
            Log.d("removeInstaller: just installer");
            intent0.putExtra("catch_.me_.if_.you_.can_.fromInstaller", true);
        }
        BaseActivity.context.startActivity(intent0);
    }

    private void setInstaller() {
        if(Build.VERSION.SDK_INT < 11) {
            return;
        }
        try {
            Tools.getPackageManager().setInstallerPackageName(this.newPackage, null);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    static void setState(State st) {
        Installer.state = st;
    }

    private static void setStep(int step) {
        if(Installer.state.step != step) {
            Installer.wait = 0;
        }
        Installer.state.step = step;
    }

    private static void setVersion() {
        try {
            File file0 = Installer.getFile("version.gg");
            byte[] arr_b = ("15993" + ':' + 96.0f).getBytes();
            if(file0.exists()) {
                try {
                    FileInputStream is = new FileInputStream(file0);
                    try {
                        byte[] buffer = new byte[arr_b.length];
                        if(is.read(buffer) != buffer.length || !Arrays.equals(buffer, arr_b)) {
                            Installer.getFile("installer.fail").delete();
                            File file1 = Tools.getFilesDirHidden();
                            String[] arr_s = file1.list();
                            if(arr_s != null) {
                                for(int v1 = 0; v1 < arr_s.length; ++v1) {
                                    String old = arr_s[v1];
                                    if(old.startsWith("lib") && old.endsWith(".so") || "gr".equals(old)) {
                                        new File(file1, old).delete();
                                    }
                                }
                            }
                        }
                    }
                    finally {
                        is.close();
                    }
                }
                catch(Throwable e) {
                    Log.e("Fail get version", e);
                }
            }
            FileOutputStream os = new FileOutputStream(file0);
            Tools.chmod(file0, 0x180);
            try {
                os.write(arr_b);
            }
            finally {
                os.close();
            }
        }
        catch(Throwable e) {
            Log.e("Fail set version", e);
        }
    }

    public static void startInstall() {
        Installer.initProgressBar();
        new ThreadEx(() -> {
            Installer.memCache = new byte[Installer.FIX_FILES.length][];
            try {
                Installer.instance = new Installer();
                Installer.instance._install();
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseActivity.instance.finish();
                    }
                });
            }
            catch(Throwable e) {
                Log.e("Failed install", e);
                String s = e.getMessage();
                if(s != null && (s.contains("ENOSPC") || s.contains("o space left"))) {
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String file = Installer.instance == null || Installer.instance.apk == null ? null : Installer.instance.apk.getAbsolutePath();
                            Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F070158) + '\n' + s + '\n' + file).setPositiveButton(0x7F07009D, new DialogInterface.OnClickListener() {  // string:no_space "No space left on your device"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    BaseActivity.restartApp();
                                }
                            }).setCancelable(false).create());
                        }
                    });
                    return;
                }
                if(!(e instanceof InstallException)) {
                    ExceptionHandler.sendException(Thread.currentThread(), e, false);
                }
                Installer.cancelInstall(false);
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        android.ext.Installer.5.1 listener = new DialogInterface.OnClickListener() {
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == -1) {
                                    new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:6<;0jdwkhulqj0lqirupdwlrq0derxw0jj0huuruv2").onClick(dialog, -1);
                                    ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("Kill by Installer");
                                            Main.die();
                                        }
                                    }, 5000L);
                                    return;
                                }
                                Installer.cancelInstall(true);
                            }
                        };
                        Alert.show(Alert.create(BaseActivity.context).setMessage(Tools.stripColon(0x7F0702FD) + ":\n\n" + Log.getStackTraceString(e)).setPositiveButton(0x7F070156, listener).setNegativeButton(0x7F07009D, listener).setCancelable(false).create());  // string:reinstall_fail "Unable to reinstall __app_name__ with random name.\n\n__app_name__ 
                                                                                                                                                                                                                                                                       // can be easily detected by games.\n\nYou can record __logcat__, during installation, 
                                                                                                                                                                                                                                                                       // and contact the official forum for help.\n\nRejection reason:"
                    }
                });
            }
        }, "Installer").start();

        class android.ext.Installer.2 implements Runnable {
            @Override
            public void run() {
                Installer.install();
            }
        }

    }

    @TargetApi(14)
    private void uninstallOldPackage() throws IOException, InterruptedException {
        if(!Uninstaller.isInstalled()) {
            Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F0702A7)).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:need_uninstall "You need to uninstall the installer, otherwise you will have 
                                                                                                                                                                    // 4 icons, not 2. Also games will detect it."
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Uninstaller.uninstallPackageWithIntent("catch_.me_.if_.you_.can_");
                }
            }));
            return;
        }
        Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F0702E2)).setPositiveButton(Re.s(0x7F07009D), new ExitListener(1600)));  // string:install_finished "The installation was successful.\nRun the installed copy 
                                                                                                                                                  // manually so that it can migrate your data.\nAfter that, the installer can be removed."
    }

    private void updateEntry(ZipEntry ze, int index) throws IOException {
        int v2;
        byte[] arr_b;
        InputStream inputStream0;
        long read;
        CRC32 crc;
        try {
            Log.d(("updateEntry f: " + AlignedZipOutputStream.toString(ze)));
            crc = new CRC32();
            read = 0L;
            try(inputStream0 = this.getInputStream(index)) {
                arr_b = this.getBuf();
                while(true) {
                label_7:
                    v2 = inputStream0.read(arr_b);
                    switch(v2) {
                        case -1: {
                            goto label_11;
                        }
                        case 0: {
                        }
                    }
                }
            }
        }
        catch(Throwable throwable0) {
            goto label_22;
        }
        goto label_17;
    label_11:
        ze.setCrc(crc.getValue());
        long v3 = this.getLength(index);
        ze.setSize(v3);
        ze.setCompressedSize((ze.getMethod() == 0 ? v3 : -1L));
        Log.d(("updateEntry t: " + AlignedZipOutputStream.toString(ze) + ' ' + v3 + ' ' + read));
        return;
        try {
        label_17:
            TWR.useResource$NT(inputStream0);
            read += (long)v2;
            crc.update(arr_b, 0, v2);
        }
        catch(Throwable throwable0) {
        label_22:
            TWR.moot$NT();
            throw throwable0;
        }
        goto label_7;
    }

    static void updateProgress() {
        int step = Installer.state.step + Installer.wait <= 1000 ? Installer.state.step + Installer.wait : 1000;
        ProgressBar bar = (ProgressBar)Installer.weakBar.get();
        if(bar != null) {
            bar.setMax(1000);
            bar.setProgress(step);
        }
        TextView barText = (TextView)Installer.weakBarText.get();
        if(barText != null) {
            barText.setText(Tools.stringFormat("%.1f %%", new Object[]{((double)(100.0 * ((double)step) / 1000.0))}));
        }
        if(Installer.wait < 0xC7) {
            ++Installer.wait;
        }
        if(Installer.state.step == 600) {
            if(Installer.wait == 60) {
                TextView msg = (TextView)Installer.weakMsg.get();
                String apk = Installer.state.apk;
                if(msg != null && apk != null) {
                    msg.setText(Re.s(0x7F070152) + "\n\n" + Tools.stripColon(0x7F070257) + ":\n" + apk);  // string:installing "__app_name__ performs a reinstallation of itself with a random 
                                                                                                          // name to exclude detection by games. Wait."
                }
            }
            if(Installer.wait == 0xC6 && Installer.instance != null) {
                Installer.promptRetry((Re.s(0x7F070153) + "\n\n" + Tools.stripColon(0x7F070257) + ":\n" + Installer.instance.apk.getAbsolutePath()), 1, 0xFFFFFF85, Installer.instance.getInstallIntent());  // string:failed_install "Failed to install app. Retry?"
            }
        }
    }

    private void waitForResult(Intent intent, int requestCode) {
        Installer.state.waitResult |= requestCode;
        Handler handler0 = ThreadManager.getHandlerUiThread();
        android.ext.Installer.10 fail = new Runnable() {
            @Override
            public void run() {
                Installer.state.waitResult &= ~requestCode;
            }
        };
        handler0.postDelayed(fail, 300000L);
        BaseActivity.instance.startActivityForResult(intent, requestCode);
        while((Installer.state.waitResult & requestCode) == requestCode) {
            try {
                Thread.sleep(500L);
                if(requestCode != 1 || !Tools.isPackageInstalled(this.newPackage)) {
                    continue;
                }
                Installer.state.waitResult &= -2;
            }
            catch(InterruptedException e) {
                Log.e("Interrupted wait", e);
                if(true) {
                    break;
                }
            }
        }
        handler0.removeCallbacks(fail);
        ThreadManager.runOnUiThread(() -> if(Installer.this.dialog != null) {
            try {
                Tools.dismiss(Installer.this.dialog);
                Installer.this.dialog = null;
            }
            catch(Throwable e) {
                Log.w("Exception on dismiss", e);
            }
        });

        class android.ext.Installer.11 implements Runnable {
            @Override
            public void run() {
                Installer.this.dismissDialog();
            }
        }

    }

    private void writeSignatureBlock(OutputStream out) throws IOException, GeneralSecurityException {
        byte[] signatureFileBytes;
        signatureFileBytes = new byte[((int)this.getLength(4))];
        try(InputStream inputStream0 = this.getInputStream(4)) {
            int read = 0;
            for(int i = 0; true; ++i) {
                if(i >= 10 || read >= signatureFileBytes.length) {
                    break;
                }
                int v2 = inputStream0.read(signatureFileBytes, read, signatureFileBytes.length - read);
                if(v2 < 0) {
                    Log.w(("Failed read cert 1: " + v2 + ' ' + read + ' ' + signatureFileBytes.length));
                    break;
                }
                read += v2;
            }
            if(read < signatureFileBytes.length) {
                Log.w(("Failed read cert 2: " + read + ' ' + signatureFileBytes.length));
            }
        }
        ZipSignature signature = new ZipSignature();
        signature.initSign(Installer.getPrivateKey());
        signature.update(signatureFileBytes);
        byte[] arr_b1 = signature.sign();
        out.write(Base64.decode("MIIGrgYJKoZIhvcNAQcCoIIGnzCCBpsCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHAaCCBKwwggSoMIIDkKADAgECAgkAk26svgfyAd8wDQYJKoZIhvcNAQEFBQAwgZQxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRAwDgYDVQQKEwdBbmRyb2lkMRAwDgYDVQQLEwdBbmRyb2lkMRAwDgYDVQQDEwdBbmRyb2lkMSIwIAYJKoZIhvcNAQkBFhNhbmRyb2lkQGFuZHJvaWQuY29tMB4XDTA4MDIyOTAxMzM0NloXDTM1MDcxNzAxMzM0NlowgZQxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRAwDgYDVQQKEwdBbmRyb2lkMRAwDgYDVQQLEwdBbmRyb2lkMRAwDgYDVQQDEwdBbmRyb2lkMSIwIAYJKoZIhvcNAQkBFhNhbmRyb2lkQGFuZHJvaWQuY29tMIIBIDANBgkqhkiG9w0BAQEFAAOCAQ0AMIIBCAKCAQEA1pMZBN7GCySx7cdi4NnYJT4+zWzrHeL/Boyo6LyozWvTeG6nCqds5g67D5k1Wf/ZPnepQ+foPUtkuOT+otPmVvHiZ6gbv7IwtXjCBEO+THIYuEb1IRWG8DihTonCvjh/jr7Pj8rD2h7jMMnqk9Cnw9xK81AiDVAIBzLggJcX7moFM1nmppTsLLPyhKCkZsh6lNg7MQk6ZzcuL2QSwG5tQvFYGN/+A4HMDNRE2mzdw7gkWBlIAbMlZBNPv96YySh3SNv1Z2pUDYFUyLvKB7niR1UzEcRrmvdv3uzMjmnnyKLQjngmIJQ/mXJ9PAT+cpkdmd+brjigshd/ox1bav7pHwIBA6OB/DCB+TAdBgNVHQ4EFgQUSFkAVj0nLEauEYYFpHQZrAnKjBEwgckGA1UdIwSBwTCBvoAUSFkAVj0nLEauEYYFpHQZrAnKjBGhgZqkgZcwgZQxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRAwDgYDVQQKEwdBbmRyb2lkMRAwDgYDVQQLEwdBbmRyb2lkMRAwDgYDVQQDEwdBbmRyb2lkMSIwIAYJKoZIhvcNAQkBFhNhbmRyb2lkQGFuZHJvaWQuY29tggkAk26svgfyAd8wDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAeq+WjOtQxEEFURjQ2quvAVuKdlonpxWiwrRPIhQV/9rOAwlav6Qt9wcIcmwgaeXDbt2uBAC+KUUsCEvCfrahfqydvhgsIE6xUxH0Vdgktlbb5NwiQJEtdYb+iJUdAaj+ta5aQmBTXfg0MQUkIkaMNuIsKl75lNYd1zBq5Mn2lRujwS8dGRTdxh8aYtot+Cf2A/6lYDssVA29fAGcNrqymkJxwRffUjzbxfOBekng76YMvX90F356Txk9Q/QiB3JmbkxNg+G9WoYIfPNPLewh4kXKbCuwFuaDY4BQ0sQw7qfCahxJ03YKWKt/GoLMk4tIMThDJL0EAfoSFjpQVw5oTTGCAcowggHGAgEBMIGiMIGUMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzEQMA4GA1UEChMHQW5kcm9pZDEQMA4GA1UECxMHQW5kcm9pZDEQMA4GA1UEAxMHQW5kcm9pZDEiMCAGCSqGSIb3DQEJARYTYW5kcm9pZEBhbmRyb2lkLmNvbQIJAJNurL4H8gHfMAkGBSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEggEA", 0));
        out.write(arr_b1);
        out.flush();
    }
}

