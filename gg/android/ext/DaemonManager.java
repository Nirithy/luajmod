package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.SparseArray;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Handler;
import android.sup.ContainerHelpers;
import com.ggdqo.R.raw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class DaemonManager {
    static class AddressFlags {
        long[] address;
        int allFlags;
        int[] flags;

        public AddressFlags(long[] address, int[] flags) {
            this.address = address;
            this.flags = flags;
        }
    }

    static class AlterHolder {
        final AddressItem item;
        final int xor;

        public AlterHolder(AddressItem item, int xor) {
            this.item = item;
            this.xor = xor;
        }
    }

    static class CheckException extends Exception {
        private static final long serialVersionUID = 0x460EEDA37C6D0EE1L;

        public CheckException(String detailMessage) {
            super(detailMessage);
        }
    }

    static class ItemInfo implements Comparable {
        long address;
        int flags;

        public ItemInfo(long address, int flags) {
            this.address = address;
            this.flags = flags;
        }

        public int compareTo(ItemInfo rhs) {
            long lhsA = this.address;
            long rhsA = rhs.address;
            if(lhsA < rhsA) {
                return -1;
            }
            return lhsA == rhsA ? 0 : 1;
        }

        @Override
        public int compareTo(Object object0) {
            return this.compareTo(((ItemInfo)object0));
        }
    }

    static final class Proc {
        Process process;
        InputStream stderr;
        OutputStream stdin;
        InputStream stdout;

    }

    public static final byte CLIENT = 0;
    public static final String LABEL = "android-daemon";
    private volatile int M;
    public static final int MAX_PATH_SIZE = 4000;
    private volatile int N;
    public static final String ROOT_OK = "root-ok";
    private static final int SIGBUS = 7;
    private static final int SIGKILL = 9;
    private static final int SIGKILL_OOM = 0x89;
    private static final int SIGTERM = 15;
    public static final long TIME_JUMP_MUL = 1000000000L;
    public static final int TIME_JUMP_MUL_INT = 1000;
    public static volatile boolean bypassDaemonFail;
    private volatile int countFuzzyEqualRun;
    private volatile int countFuzzyEqualRunMax;
    volatile String daemonName;
    volatile int daemonPid;
    private volatile long fuzzyEqualMemoryFrom;
    private volatile long fuzzyEqualMemoryTo;
    final InOut inOut;
    static volatile String lastReadError;
    public static volatile boolean lastSearchFuzzy;
    private static volatile String lastSendError;
    Exchanger mMemItemExchanger;
    Proc mProcess;
    Thread mReaderThread;
    private volatile boolean needCancel;
    private Integer oldDataInRam;
    long prevConfigCrc;
    static Proc sProcess;
    volatile int suPid;
    private volatile long timeJump;
    private final ArrayList waitAlter;
    private static boolean waitForDaemon;
    private static final List waitForDaemonStart;
    private final ArrayList waitFreeze;
    private final ArrayList waitUnfreeze;
    public static volatile boolean warnAboutLags;

    static {
        DaemonManager.bypassDaemonFail = false;
        DaemonManager.lastSearchFuzzy = false;
        DaemonManager.warnAboutLags = false;
        DaemonManager.sProcess = null;
        DaemonManager.waitForDaemonStart = new ArrayList();
        DaemonManager.waitForDaemon = true;
        DaemonManager.lastReadError = null;
        DaemonManager.lastSendError = null;
    }

    public DaemonManager() {
        this.mProcess = null;
        this.mMemItemExchanger = new Exchanger();
        this.needCancel = false;
        this.daemonPid = -1;
        this.suPid = -1;
        this.daemonName = "???";
        this.countFuzzyEqualRun = 0;
        this.countFuzzyEqualRunMax = 0;
        this.fuzzyEqualMemoryFrom = 0L;
        this.fuzzyEqualMemoryTo = -1L;
        this.mReaderThread = new DaemonThread("mReaderThread") {
            private void handleMemoryItem(byte[] buffer) {
                try {
                    Exchanger mMemItemExchanger = DaemonManager.this.mMemItemExchanger;
                    while(true) {
                        if(android.ext.DaemonManager.1.interrupted()) {
                            return;
                        }
                        try {
                            mMemItemExchanger.exchange(buffer, 500L, TimeUnit.MILLISECONDS);
                            return;
                        }
                        catch(InterruptedException unused_ex) {
                        }
                    }
                }
                catch(Throwable e) {
                    Log.e("handleMemoryItem failed", e);
                }
            }

            private void postMessage(byte[] buffer) throws IOException {
                if(buffer[0] == 20 && buffer[1] == 0) {
                    this.handleMemoryItem(buffer);
                    return;
                }
                ThreadManager.getHandlerUiThread().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DaemonManager.this.processMessage(buffer);
                        }
                        catch(IOException e) {
                            Log.e(("Failed process message: " + ((int)buffer[0]) + ' ' + buffer.length), e);
                        }
                    }
                });
            }

            @Override
            public void run() {
                InOut inOut = DaemonManager.this.inOut;
                try {
                    while(true) {
                        if(android.ext.DaemonManager.1.interrupted()) {
                            return;
                        }
                        byte[] arr_b = inOut.readPacket();
                        DaemonManager.lastReadError = null;
                        if(arr_b != null) {
                            this.postMessage(arr_b);
                        }
                    }
                }
                catch(Throwable e) {
                    Log.e("Read error", e);
                    DaemonManager.lastReadError = e.toString();
                    ThreadManager.getHandlerUiThread().post(new Runnable() {
                        @Override
                        public void run() {
                            DaemonManager.this.messageFailed();
                        }
                    });
                }
            }
        };
        this.M = 1;
        this.N = 1;
        this.timeJump = 0L;
        this.waitAlter = new ArrayList();
        this.waitFreeze = new ArrayList();
        this.waitUnfreeze = new ArrayList();
        this.prevConfigCrc = 0L;
        this.oldDataInRam = null;
        this.inOut = new InOut();
    }

    public void addForAlter(AddressItem item, int xor) {
        AddressItem obj = xor == 0 ? item : new AlterHolder(item, xor);
        synchronized(this.waitAlter) {
            this.waitAlter.add(obj);
        }
    }

    public void addForFreeze(SavedItem item) {
        synchronized(this.waitFreeze) {
            this.waitFreeze.add(item);
        }
    }

    public void addForUnfreeze(SavedItem item) {
        synchronized(this.waitUnfreeze) {
            this.waitUnfreeze.add(item);
        }
    }

    public void addTimeJump(byte seq, long time) {
        this.timeJump += time;
        this.setSpeed(seq);
    }

    public void addTimeJump(long time) {
        this.addTimeJump(0, time);
    }

    public void allocatePage(byte seq, long address, int mode) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 0x20, 0);
                DaemonManager.this.inOut.writeInt(mode);
                DaemonManager.this.inOut.writeLong(address);
                DaemonManager.this.send();
            }
        });
    }

    public void allocatePage(long address, int mode) {
        this.allocatePage(0, address, mode);
    }

    public void cancel() {
        this.needCancel = true;
        this.doCancel();
    }

    void checkBinary(String filename, boolean fixed) throws CheckException {
        File file = new File(filename);
        int err = 0;
        if(!file.exists()) {
            err = 0x7F0701F8;  // string:file_not_found "File not found:"
        }
        else if(file.isFile()) {
            String[] arr_s = {"ls", "-l", Tools.getRealPath(filename)};
            try {
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(Tools.exec(arr_s).getInputStream()));
                Log.w(("ls for \'" + filename + "\' got: " + bufferedReader0.readLine()));
                bufferedReader0.close();
            }
            catch(Exception e) {
                Log.e(("run \'" + Arrays.toString(arr_s) + "\' error"), e);
            }
            if(!file.canExecute()) {
                err = 0x7F0702F6;  // string:not_execute "Can\'t execute:"
            }
        }
        else {
            err = 0x7F0702F5;  // string:not_file "Path is not a file:"
        }
        if(err != 0) {
            throw new CheckException(Tools.stripColon(err) + ": \'" + filename + "\'\n\n" + Re.s(0x7F0702F7) + (err == 0x7F0702F6 ? "\n\n" + Re.s(0x7F0702F8) : ""));  // string:check_install "Check that the __app_name__ is installed correctly."
        }
    }

    private static boolean checkDaemon(Proc proc, File dir) throws IOException {
        if(proc == null) {
            return false;
        }
        if(proc.process == null) {
            return false;
        }
        InputStream out = proc.stdout;
        boolean ret = DaemonManager.checkInputStream(proc.stderr, "android-daemon err") && DaemonManager.checkInputStream(out, "android-daemon out");
        if(!Config.vSpace && ret) {
            new SPEditor().putInt("root-ok", 0x3E79).commit();
        }
        if(ret) {
            int v = out.read();
            if(v == 0x30 || v == 49) {
                out.read();
            }
            switch(v) {
                case 0x30: {
                    break;
                }
                case 49: {
                    try {
                        boolean use = false;
                        OutputStream old = proc.stdin;
                        try {
                            Log.d("IF 0");
                            FileInputStream fileInputStream0 = new FileInputStream(new File(dir, "o"));
                            Log.d("IF 1");
                            FileInputStream fileInputStream1 = new FileInputStream(new File(dir, "e"));
                            Log.d("IF 2");
                            FileOutputStream fileOutputStream0 = new FileOutputStream(new File(dir, "i"));
                            Log.d("IF 3");
                            proc.stdin = fileOutputStream0;
                            proc.stdout = fileInputStream0;
                            proc.stderr = fileInputStream1;
                            use = true;
                        }
                        catch(Throwable e) {
                            Log.e("Fifo fail", e);
                        }
                        old.write((use ? 89 : 78));
                        old.write(10);
                        old.flush();
                        InOut.fifo = use;
                    }
                    catch(Throwable e) {
                        Log.e("Fifo fail 2", e);
                    }
                    return true;
                }
                default: {
                    Log.w(("Odd fifo: " + v));
                    return true;
                }
            }
        }
        return ret;
    }

    private static boolean checkInputStream(InputStream is, String checkString) throws IOException {
        byte[] arr_b = checkString.getBytes();
        byte[] skipBytes = new byte[0x1000];
        int index = 0;
        int skip = 0;
        Log.d(("Start: " + checkString));
        while(true) {
            int v2 = is.read();
            if(v2 == -1 || skip >= 0x1000) {
                break;
            }
            skipBytes[skip] = (byte)v2;
            if(((byte)v2) == arr_b[index]) {
                ++index;
                if(index == arr_b.length) {
                    Log.d(("Good: " + checkString));
                    return true;
                }
            }
            else {
                index = 0;
            }
            if(is.available() == 0) {
                for(int i = 0; i < 30; ++i) {
                    try {
                        Thread.sleep(100L);
                    }
                    catch(InterruptedException unused_ex) {
                    }
                    if(is.available() != 0) {
                        break;
                    }
                }
            }
            if(is.available() == 0) {
                RootDetector.debug = "No more data: " + (skip + 1) + '\n';
                ++skip;
                break;
            }
            ++skip;
        }
        String s1 = "Fail: \'" + checkString + "\' " + skip + " \'" + new String(skipBytes, 0, skip) + '\'';
        RootDetector.debug = "" + s1 + '\n';
        Log.d(s1);
        return false;
    }

    public void clear(byte seq) {
        this.send(seq, 44);
        SearchButtonListener.lastTextSearch = null;
    }

    public void clearCountFuzzyEqualRun() {
        this.countFuzzyEqualRun = 0;
        this.countFuzzyEqualRunMax = 0;
    }

    public void clearLockList() {
        this.clearLockList(0);
    }

    public void clearLockList(byte seq) {
        this.send(seq, 46);
    }

    public void copyMemory(byte seq, long memoryFrom, long memoryTo, int bytes) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 62, 0);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.inOut.writeInt(bytes);
                DaemonManager.this.send();
            }
        });
    }

    public void copyMemory(long memoryFrom, long memoryTo, int bytes) {
        this.copyMemory(0, memoryFrom, memoryTo, bytes);
    }

    static void daemonStarted() {
        synchronized(DaemonManager.waitForDaemonStart) {
            DaemonManager.waitForDaemon = false;
            for(Object run: DaemonManager.waitForDaemonStart) {
                new DaemonThread(((Runnable)run), "waitForDaemonStart").start();
            }
        }
    }

    public void disableProtection() {
        this.disableProtection(0);
    }

    public void disableProtection(byte seq) {
        this.send(seq, 49);
    }

    private int doAlter(byte seq) {
        Object[] arr_object;
        synchronized(this.waitAlter) {
            arr_object = this.waitAlter.toArray(new Object[this.waitAlter.size()]);
            this.waitAlter.clear();
            this.waitAlter.trimToSize();
        }
        if(arr_object.length != 0) {
            ThreadManager.runOnWriteThread(new Runnable() {
                private static final int BATCH_COUNT = 100000000;

                @Override
                public void run() {
                    int xor;
                    AddressItem item;
                    InOut inOut = DaemonManager.this.inOut;
                    for(int offset = 0; offset < arr_object.length; offset += len) {
                        int len = arr_object.length - offset <= 100000000 ? arr_object.length - offset : 100000000;
                        inOut.startMessage(seq, 43, (InOut.longSize + 16) * len);
                        inOut.writeInt(len);
                        int i = offset;
                        int n = offset + len;
                        while(i < n) {
                            Object obj = arr_object[i];
                            if(obj instanceof AlterHolder) {
                                item = ((AlterHolder)obj).item;
                                xor = ((AlterHolder)obj).xor;
                            }
                            else {
                                item = (AddressItem)obj;
                                xor = 0;
                            }
                            int flags = item.flags;
                            if(flags != 0) {
                                inOut.writeInt(flags);
                                inOut.writeLong(item.address);
                                inOut.writeLongLong(item.data);
                                inOut.writeInt(xor);
                            }
                            ++i;
                        }
                        inOut.writeInt(0);
                        DaemonManager.this.send();
                    }
                }
            });
        }
        return arr_object.length;
    }

    private void doCancel() {
        File file0;
        if(!this.needCancel || this.daemonPid <= 0) {
            return;
        }
        else {
            file0 = new File(Tools.getFilesDirHidden(), "stop.tmp");
            try {
                FileOutputStream writer = new FileOutputStream(file0);
                writer.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.daemonPid).array());
                writer.close();
                if(!file0.renameTo(new File(Tools.getFilesDirHidden(), "stop"))) {
                    Log.w(("Failed rename stop file: " + file0), new RuntimeException());
                    this.needCancel = false;
                    file0.delete();
                    return;
                }
                return;
            }
            catch(IOException e) {
            }
        }
        Log.w(("Failed make stop file: " + file0), e);
        this.needCancel = false;
    }

    public void doDump(byte seq, long memoryFrom, long memoryTo, int flags, String path, String prefix) {
        String pathes = path;
        if(path.contains("/emulated/legacy")) {
            pathes = String.valueOf(pathes) + '|' + path.replace("/emulated/legacy", "/emulated/0");
        }
        else if(path.contains("/emulated/0")) {
            pathes = String.valueOf(pathes) + '|' + path.replace("/emulated/0", "/emulated/legacy");
        }
        byte[] arr_b = pathes.getBytes();
        if(arr_b.length == 0) {
            return;
        }
        if(arr_b.length > 4000) {
            Log.e(("path big: " + arr_b.length + " > " + 4000));
            return;
        }
        byte[] arr_b1 = prefix.getBytes();
        if(arr_b1.length > 4000) {
            Log.e(("pkg big: " + arr_b1.length + " > " + 4000));
            return;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 61, 0);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeInt(arr_b.length);
                DaemonManager.this.inOut.writeBytes(arr_b, arr_b.length);
                DaemonManager.this.inOut.writeInt(arr_b1.length);
                DaemonManager.this.inOut.writeBytes(arr_b1, arr_b1.length);
                DaemonManager.this.send();
                MainService.instance.onProgress(Tools.stringFormat("%s: %s - %s > %s", new Object[]{Re.s(0x7F0701A9), AddressItem.getStringAddress(memoryFrom, 4), AddressItem.getStringAddress(memoryTo, 4), path}));  // string:dump_memory "Dump memory"
            }
        });
    }

    public void doDump(long memoryFrom, long memoryTo, int flags, String path, String prefix) {
        this.doDump(0, memoryFrom, memoryTo, flags, path, prefix);
    }

    private int doFreeze(byte seq) {
        SavedItem[] list;
        synchronized(this.waitFreeze) {
            list = (SavedItem[])this.waitFreeze.toArray(new SavedItem[this.waitFreeze.size()]);
            this.waitFreeze.clear();
            this.waitFreeze.trimToSize();
        }
        if(list.length != 0) {
            ThreadManager.runOnWriteThread(new Runnable() {
                private static final int BATCH_COUNT = 100000000;

                @Override
                public void run() {
                    InOut inOut = DaemonManager.this.inOut;
                    for(int offset = 0; offset < list.length; offset += len) {
                        int len = list.length - offset <= 100000000 ? list.length - offset : 100000000;
                        inOut.startMessage(seq, 40, (InOut.longSize + 29) * len);
                        int i = offset;
                        int n = offset + len;
                        while(i < n) {
                            SavedItem item = list[i];
                            if(item.flags != 0) {
                                inOut.writeInt(item.flags);
                                inOut.writeLong(item.address);
                                inOut.writeLongLong(item.data);
                                inOut.writeByte(item.freezeType);
                                inOut.writeLongLong(item.freezeFrom);
                                inOut.writeLongLong(item.freezeTo);
                            }
                            ++i;
                        }
                        inOut.writeInt(0);
                        DaemonManager.this.send();
                    }
                }
            });
            if((Config.configDaemon & 0x80) != 0 && !DaemonManager.warnAboutLags) {
                DaemonManager.warnAboutLags = true;
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070182)).setMessage(Re.s(0x7F070183)).setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:bad_settings "Bad settings"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                Config.get(0x7F0B0087).value = 0;  // id:config_hide_from_game
                                Config.save();
                            }
                        }).setNegativeButton(Re.s(0x7F0700B9), null));  // string:skip "Ignore"
                    }
                });
            }
        }
        return list.length;
    }

    public void doOOM() {
        File file0;
        this.inOut.truncate();
        if(this.daemonPid > 0) {
            file0 = new File(Tools.getFilesDirHidden(), "OOM.tmp");
            try {
                FileOutputStream writer = new FileOutputStream(file0);
                writer.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.daemonPid).array());
                writer.close();
                if(!file0.renameTo(new File(Tools.getFilesDirHidden(), "OOM"))) {
                    Log.w(("Failed rename OOM file: " + file0), new RuntimeException());
                    file0.delete();
                    return;
                }
                return;
            }
            catch(IOException e) {
            }
        }
        else {
            return;
        }
        Log.w(("Failed make OOM file: " + file0), e);
    }

    private int doUnfreeze(byte seq) {
        SavedItem[] list;
        synchronized(this.waitUnfreeze) {
            list = (SavedItem[])this.waitUnfreeze.toArray(new SavedItem[this.waitUnfreeze.size()]);
            this.waitUnfreeze.clear();
            this.waitUnfreeze.trimToSize();
        }
        if(list.length != 0) {
            ThreadManager.runOnWriteThread(new Runnable() {
                private static final int BATCH_COUNT = 100000000;

                @Override
                public void run() {
                    InOut inOut = DaemonManager.this.inOut;
                    for(int offset = 0; offset < list.length; offset += len) {
                        int len = list.length - offset <= 100000000 ? list.length - offset : 100000000;
                        inOut.startMessage(seq, 41, (InOut.longSize + 4) * len);
                        int i = offset;
                        int n = offset + len;
                        while(i < n) {
                            SavedItem item = list[i];
                            if(item.flags != 0) {
                                inOut.writeInt(item.flags);
                                inOut.writeLong(item.address);
                            }
                            ++i;
                        }
                        inOut.writeInt(0);
                        DaemonManager.this.send();
                    }
                }
            });
        }
        return list.length;
    }

    public void doWaited() {
        this.doWaited(0);
    }

    public void doWaited(byte seq) {
        if(this.doAlter(seq) + this.doFreeze(seq) + this.doUnfreeze(seq) > 0) {
            MainService.instance.refreshTabLazy();
        }
    }

    public void fuzzyRefine(byte seq, long number, long number2, int flags, int sign, int counts, long memoryFrom, long memoryTo) {
        String s2;
        CharSequence charSequence0 = AddressItem.getNameShort(flags & 0x7F);
        boolean special = false;
        boolean range = (0x800000 & flags) != 0;
        boolean zero = !range && number == 0L;
        String str = AddressItem.getStringDataTrim(0L, number, flags & 0x7F);
        if(range) {
            str = String.valueOf(str) + '~' + AddressItem.getStringDataTrim(0L, number2, flags & 0x7F);
        }
        String diffType = "N ≠ O+D";
        switch(sign) {
            case 0x4000000: {
                s2 = zero ? Re.s(0x7F0700CC) : "N > O+D";  // string:fuzzy_larger "Value increased"
                break;
            }
            case 0x8000000: {
                s2 = zero ? Re.s(0x7F0700CD) : "N < O+D";  // string:fuzzy_smaller "Value decreased"
                break;
            }
            case 0x10000000: {
                s2 = zero ? Re.s(0x7F0700CB) : "N ≠ O+D";  // string:fuzzy_inequal "Value changed"
                break;
            }
            default: {
                if(this.countFuzzyEqualRunMax == 0) {
                    this.countFuzzyEqualRunMax = counts;
                }
                this.countFuzzyEqualRun = counts - 1;
                this.fuzzyEqualMemoryFrom = memoryFrom;
                this.fuzzyEqualMemoryTo = memoryTo;
                special = true;
                diffType = "N = O+D";
                s2 = zero ? Re.s(0x7F0700CA) : "N = O+D";  // string:fuzzy_equal "Value unchanged"
            }
        }
        CharSequence tmp = new CharSequenceBuilder().appendFormat("%s %s", new CharSequence[]{(zero ? s2 + " (" + diffType.replace("+D", "") + ')' : s2.replace("+D", " + (" + str + ')')), charSequence0}).toCharSequence();
        if(special) {
            MainService.instance.onProgress(tmp, -1L, 1L, this.countFuzzyEqualRunMax - this.countFuzzyEqualRun, this.countFuzzyEqualRunMax, 0L);
        }
        else {
            MainService.instance.onProgress(tmp);
        }
        this.sendSearchNumber(seq, number, number2, 0x80000000 | flags | sign, memoryFrom, memoryTo);
    }

    public void fuzzyRefine(long number, long number2, int flags, int sign, int counts, long memoryFrom, long memoryTo) {
        this.fuzzyRefine(0, number, number2, flags, sign, counts, memoryFrom, memoryTo);
    }

    public void fuzzyStart(byte seq, int flags, long memoryFrom, long memoryTo) {
        MainService.instance.onProgress(new CharSequenceBuilder().appendFormat("%s %s", new CharSequence[]{Re.s(0x7F070101), AddressItem.getNameShort(flags & 0x7F)}).toCharSequence());  // string:search_unknown_value "Unknown (fuzzy) search"
        this.sendSearchNumber(seq, 0L, 0L, flags | 0x80000000, memoryFrom, memoryTo);
    }

    public void fuzzyStart(int flags, long memoryFrom, long memoryTo) {
        this.fuzzyStart(0, flags, memoryFrom, memoryTo);
    }

    public void getAppList() {
        this.getAppList(0);
    }

    public void getAppList(byte seq) {
        this.send(seq, 57);
    }

    private boolean[] getConfig(int type) {
        return type == 1 ? MainService.instance.randEditor.getData() : MainService.instance.timersEditor.getData();
    }

    public int getDaemonPid() {
        return this.daemonPid;
    }

    private String[] getExec(boolean direct, String execPath, File dir) {
        String[] arr_s = {execPath, Tools.getPackageName(), dir.getAbsolutePath(), DaemonManager.getNativeCheck(), (Config.vSpace ? "1" : "0")};
        if(direct) {
            return arr_s;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("exec");
        for(int v = 0; v < 5; ++v) {
            String part = arr_s[v];
            sb.append(' ');
            sb.append(part);
        }
        return new String[]{sb.toString()};
    }

    public void getMem() {
        this.getMem(0);
    }

    public void getMem(byte seq) {
        this.send(seq, 58);
    }

    public void getMemory(byte seq, long address, int size) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.send(seq, 58);
                DaemonManager.this.inOut.startMessage(seq, 59, 0);
                DaemonManager.this.inOut.writeLong(address);
                DaemonManager.this.inOut.writeInt(size);
                DaemonManager.this.send();
            }
        });
    }

    public void getMemory(long address, int size) {
        this.getMemory(0, address, size);
    }

    public long getMemoryContent(byte seq, long address, int flags) {
        if(!this.inOut.isStarted()) {
            return -1L;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 50, 0);
                DaemonManager.this.inOut.writeLong(address);
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeByte(0);
                DaemonManager.this.send();
            }
        });
        Exchanger mMemItemExchanger = this.mMemItemExchanger;
        while(true) {
            try {
            label_4:
                BufferReader reader = new BufferReader(((byte[])mMemItemExchanger.exchange(null, 500L, TimeUnit.MILLISECONDS)));
                reader.skip(3);
                if(reader.readLong() != address) {
                    goto label_4;
                }
                reader.skip(4);
                return reader.readLongLong();
            }
            catch(TimeoutException unused_ex) {
            }
            catch(InterruptedException unused_ex) {
                continue;
            }
            catch(Throwable e) {
                break;
            }
            return -1L;
        }
        Log.e("getMemoryContent failed", e);
        return -1L;
    }

    public long getMemoryContent(long address, int flags) {
        return this.getMemoryContent(0, address, flags);
    }

    public void getMemoryItems(byte seq, int[] flags, long[] address) {
        if(flags.length != address.length) {
            throw new RuntimeException("Size mismatch: " + flags.length + " != " + address.length);
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            private static final int BATCH_COUNT = 100000000;

            @Override
            public void run() {
                InOut inOut = DaemonManager.this.inOut;
                for(int offset = 0; offset < flags.length; offset += len) {
                    int len = flags.length - offset <= 100000000 ? flags.length - offset : 100000000;
                    inOut.startMessage(seq, 36, (InOut.longSize + 4) * len);
                    inOut.writeInt(len);
                    int i = offset;
                    int n = offset + len;
                    while(i < n) {
                        int fl = flags[i];
                        if(fl != 0) {
                            inOut.writeInt(fl);
                            inOut.writeLong(address[i]);
                        }
                        ++i;
                    }
                    inOut.writeInt(0);
                    DaemonManager.this.send();
                }
            }
        });
    }

    public void getMemoryItems(int[] flags, long[] address) {
        this.getMemoryItems(0, flags, address);
    }

    public static String getNativeCheck() {
        return Hash.hash(Debug.getInfo(), "SHA-384");
    }

    public void getProcessList() {
        this.getProcessList(0);
    }

    public void getProcessList(byte seq) {
        this.send(seq, 56);
    }

    public void getRegionList() {
        this.getRegionList(0);
    }

    public void getRegionList(byte seq) {
        this.send(seq, 60);
    }

    public void getResultList(byte seq, int count, int offset, int filters, long minAddr, long maxAddr, long minValue, int minValueFlags, long maxValue, int maxValueFlags, int type, int fractionalSign, double fractional, int pointer) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 39, 0);
                DaemonManager.this.inOut.writeInt(count);
                DaemonManager.this.inOut.writeInt(offset);
                DaemonManager.this.inOut.writeInt(filters);
                DaemonManager.this.inOut.writeLongLong(minAddr);
                DaemonManager.this.inOut.writeLongLong(maxAddr);
                DaemonManager.this.inOut.writeLongLong(minValue);
                DaemonManager.this.inOut.writeInt(minValueFlags);
                DaemonManager.this.inOut.writeLongLong(maxValue);
                DaemonManager.this.inOut.writeInt(maxValueFlags);
                DaemonManager.this.inOut.writeInt(type);
                DaemonManager.this.inOut.writeInt(fractionalSign);
                DaemonManager.this.inOut.writeLongLong(Double.doubleToRawLongBits(fractional));
                DaemonManager.this.inOut.writeInt(pointer);
                DaemonManager.this.send();
            }
        });
    }

    public void getResultList(int count, int offset, int filters, long minAddr, long maxAddr, long minValue, int minValueFlags, long maxValue, int maxValueFlags, int type, int fractionalSign, double fractional, int pointer) {
        this.getResultList(0, count, offset, filters, minAddr, maxAddr, minValue, minValueFlags, maxValue, maxValueFlags, type, fractionalSign, fractional, pointer);
    }

    public double getSpeed() {
        return ((double)this.M) / ((double)this.N);
    }

    public CharSequence getStatus() {
        if(!this.inOut.isStarted()) {
            if(this.mProcess == null || this.mProcess.process == null) {
                if(Config.vSpace) {
                    return Tools.colorize("!", Tools.getColor(0x7F0A0018));  // color:daemon_bad
                }
                return Config.vSpaceReal ? Tools.colorize("I", Tools.getColor(0x7F0A0018)) : Tools.colorize("?", Tools.getColor(0x7F0A0018));  // color:daemon_bad
            }
            if(Config.vSpace) {
                return Tools.colorize("V", Tools.getColor(0x7F0A0019));  // color:daemon_wait
            }
            return Config.vSpaceReal ? Tools.colorize("U", Tools.getColor(0x7F0A0019)) : Tools.colorize("W", Tools.getColor(0x7F0A0019));  // color:daemon_wait
        }
        if(Config.vSpace) {
            return Tools.colorize("$", Tools.getColor(0x7F0A001B));  // color:daemon_user
        }
        return Config.vSpaceReal ? Tools.colorize("@", Tools.getColor(0x7F0A001C)) : Tools.colorize("#", Tools.getColor(0x7F0A001A));  // color:daemon_vroot
    }

    public int getSuPid() {
        return this.suPid;
    }

    private void initSh() {
        MainService.instance.lockApp();
        MainService.instance.checkLibs();
    }

    public boolean isDaemonRun() {
        boolean isStop = !DaemonManager.bypassDaemonFail && !this.inOut.isStarted();
        if(isStop) {
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700F5)).setMessage(Re.s(0x7F0700F4)).setNegativeButton(Re.s(0x7F07009D), null));  // string:daemon_fail "Daemon is not running"
        }
        return !isStop;
    }

    public boolean isProcessValid() {
        String line;
        Integer exitCode;
        Process process;
        Proc proc;
        try {
            proc = DaemonManager.sProcess;
            if(proc == null) {
                return false;
            }
            process = proc.process;
            if(process == null) {
                return false;
            }
            exitCode = process.exitValue();
        }
        catch(IllegalThreadStateException unused_ex) {
            exitCode = null;
        }
        String special = null;
        if(exitCode == null && DaemonManager.lastSendError == null && DaemonManager.lastReadError == null) {
            return true;
        }
        if(!BaseActivity.waitExit && !Main.exit) {
            if(exitCode != null) {
                switch(((int)exitCode)) {
                    case 9: 
                    case 15: 
                    case 0x89: {
                        if(Config.dataInRam != 0) {
                            Config.get(0x7F0B0093).value = 0;  // id:config_ram
                            Config.save();
                            special = "Found SIGKILL/SIGTERM/SIGKILL_OOM: " + exitCode;
                        }
                    }
                }
            }
            if(special != null) {
                Log.e(special);
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainService.instance.notifyDataSetChanged(MainService.instance.mConfigListView);
                    }
                });
            }
            StringBuilder log = new StringBuilder();
            int v = Tools.getPid(process);
            log.append('\n');
            log.append(this.daemonName);
            log.append(" ( ");
            log.append(android.os.Process.myPid());
            log.append(" / ");
            log.append(v);
            log.append(" / ");
            log.append(this.daemonPid);
            log.append(" )\n");
            log.append(exitCode);
            log.append(" / ");
            log.append(DaemonManager.lastSendError);
            log.append(" / ");
            log.append(DaemonManager.lastReadError);
            log.append('\n');
            log.append(MainService.instance.memFree / 0x400);
            log.append(" / ");
            log.append(Tools.getFreeMem());
            log.append(" / ");
            log.append(MainService.instance.memTotal / 0x400);
            log.append('\n');
            String s1 = DaemonLoader.getDaemonPath();
            log.append(s1);
            log.append('\n');
            log.append(Hash.hash(s1, "SHA-384"));
            log.append('\n');
            File file = new File(s1);
            log.append(file.length());
            log.append('\n');
            log.append(Hash.hash(file, "SHA-384"));
            if(exitCode != null) {
                log.append("\nOutput:\n");
                BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(proc.stderr));
                while(true) {
                    try {
                    label_58:
                        line = bufferedReader0.readLine();
                        if(line != null) {
                            goto label_60;
                        }
                    }
                    catch(IOException e) {
                        Log.e("checkProcess", e);
                    }
                    catch(OutOfMemoryError e) {
                        Log.w("Failed get err for daemon exit", e);
                        log.append(e);
                        log.append('\n');
                    }
                    break;
                    try {
                    label_60:
                        log.append(line);
                        log.append('\n');
                    }
                    catch(OutOfMemoryError e) {
                        try {
                            Log.w("Failed get err for daemon exit", e);
                            log.append(e);
                            log.append('\n');
                            goto label_58;
                        }
                        catch(IOException e) {
                            Log.e("checkProcess", e);
                            break;
                        }
                        catch(OutOfMemoryError e) {
                        }
                        Log.w("Failed get err for daemon exit", e);
                        log.append(e);
                        log.append('\n');
                        break;
                    }
                    catch(IOException e) {
                        Log.e("checkProcess", e);
                        break;
                    }
                }
            }
            log.append("\nDEBUG:\n");
            log.append(Debug.getLogcat(false));
            String log_err = log.toString();
            try {
                Log.e(("Daemon closed" + log_err));
            }
            catch(OutOfMemoryError e) {
                Log.w("Failed out string for daemon exit", e);
            }
            if(!ExceptionHandler.checkReasonCrash(log_err) && special == null) {
                try {
                    if(log_err.contains("backtrace:") && log_err.contains(this.daemonName) && !log_err.contains("SIGPIPE")) {
                        ExceptionHandler.sendMessage(log_err);
                    }
                }
                catch(OutOfMemoryError e) {
                    Log.w("Failed send string for daemon exit", e);
                }
            }
            try {
                log_err = Re.s(0x7F0700A5) + '\n' + log_err;  // string:daemon_unexpected_closed "Daemon unexpectedly closed. Info:"
            }
            catch(OutOfMemoryError e) {
                Log.w("Failed out string for daemon exit", e);
            }
            BaseActivity.waitExit = true;
            Log.e("Show error alert");
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700A6)).setMessage(log_err).setPositiveButton(Re.s(0x7F0700B8), new ExitListener(500)).setNegativeButton(Re.s(0x7F0700B9), null));  // string:daemon_exited "Daemon exited"
            this.inOut.setStarted(false);
            MainService.instance.onStatusChanged();
            if(exitCode != null) {
                switch(((int)exitCode)) {
                    case 9: 
                    case 15: 
                    case 0x89: {
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700A6)).setMessage(Re.s((((int)exitCode) == 0x89 ? 0x7F0702BE : 0x7F070181))).setNegativeButton(Re.s(0x7F07009D), null));  // string:daemon_exited "Daemon exited"
                        break;
                    }
                    default: {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void loadResults(byte seq, List list0) {
        AddressFlags daemonManager$AddressFlags0 = this.sort(list0);
        if(daemonManager$AddressFlags0.address.length == 0) {
            return;
        }
        if(daemonManager$AddressFlags0.allFlags == 0) {
            this.clear(seq);
            return;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            private static final int BATCH_COUNT = 100000000;

            @Override
            public void run() {
                InOut inOut = DaemonManager.this.inOut;
                for(int offset = 0; offset < daemonManager$AddressFlags0.address.length; offset += len) {
                    int len = daemonManager$AddressFlags0.address.length - offset <= 100000000 ? daemonManager$AddressFlags0.address.length - offset : 100000000;
                    inOut.startMessage(seq, 33, (InOut.longSize + 4) * len);
                    inOut.writeInt(len);
                    int[] flags = daemonManager$AddressFlags0.flags;
                    long[] address = daemonManager$AddressFlags0.address;
                    int i = offset;
                    int n = offset + len;
                    while(i < n) {
                        inOut.writeInt(flags[i]);
                        inOut.writeLong(address[i]);
                        ++i;
                    }
                    DaemonManager.this.send();
                }
            }
        });
        MainService.setLastFlags(daemonManager$AddressFlags0.allFlags, seq);
        SearchButtonListener.lastTextSearch = null;
    }

    public void loadResults(List list0) {
        this.loadResults(0, list0);
    }

    // 检测为 Lambda 实现
    public void messageFailed() [...]

    void processMessage(byte[] buffer) throws IOException {
        TimersEditor editor;
        int cnt;
        int type = 0;
        try {
            BufferReader bufferReader0 = new BufferReader(buffer);
            type = bufferReader0.readByte();
            int v1 = bufferReader0.readByte();
            switch(type) {
                case 16: {
                    MainService.instance.onResults(bufferReader0);
                    return;
                }
                case 17: {
                    long v5 = bufferReader0.readLong();
                    long v6 = bufferReader0.readLong();
                    int v7 = bufferReader0.readInt();
                    if(v7 == 0 && this.countFuzzyEqualRun > 0 && v1 == 0) {
                        this.fuzzyRefine(0L, 0L, MainService.getLastFlags() & 0x7F, 0x20000000, this.countFuzzyEqualRun, this.fuzzyEqualMemoryFrom, this.fuzzyEqualMemoryTo);
                        return;
                    }
                    MainService.instance.onSearchDone(((byte)v1), v5, v6, v7);
                    if(v7 != 0) {
                        return;
                    }
                    MainService.instance.notifyScript(((byte)v1), null);
                    return;
                }
                case 18: {
                    this.clearCountFuzzyEqualRun();
                    MainService.instance.onTargetDead();
                    return;
                }
                case 19: {
                    this.clearCountFuzzyEqualRun();
                    MainService.instance.onReportError(bufferReader0.readInt());
                    return;
                }
                case 21: {
                    MainService.instance.onProgress(null, bufferReader0.readLong(), bufferReader0.readLong(), -1, -1, bufferReader0.readLong());
                    this.doCancel();
                    return;
                }
                case 22: {
                    MainService.instance.savedList.loadData(bufferReader0);
                    if(v1 == 0) {
                        return;
                    }
                    Script.loadData(bufferReader0);
                    MainService.instance.notifyScript(((byte)v1), null);
                    return;
                }
                case 23: {
                    AddressItemSet revertMap = MainService.instance.revertMap;
                    new Result();
                    boolean updated = false;
                    while(true) {
                        int v8 = bufferReader0.readInt();
                        if(v8 == 0) {
                            if(!updated) {
                                return;
                            }
                            MainService.instance.onMemoryChanged();
                            return;
                        }
                        if((0x1000000 & v8) == 0 && revertMap.put(bufferReader0.readLong(), v8, bufferReader0.readLongLong())) {
                            updated = true;
                        }
                    }
                }
                case 24: {
                    ProcessList.loadData(bufferReader0);
                    return;
                }
                case 25: {
                    Uninstaller.uninstallPreviousVersions(bufferReader0, Config.vSpaceReal && !Config.vSpace);
                    return;
                }
                case 26: {
                    MainService.instance.setMem(bufferReader0.readInt(), bufferReader0.readInt());
                    return;
                }
                case 27: {
                    MainService.instance.setMemory(bufferReader0);
                    return;
                }
                case 28: {
                    int v9 = bufferReader0.readInt();
                    int v10 = bufferReader0.readByte();
                    int[] info = v10 <= 0 ? null : new int[v10];
                    for(int i = 0; true; ++i) {
                        if(i >= v10) {
                            MainService.instance.onSendCode(((byte)v1), v9, info);
                            if(v9 == 7) {
                                this.stopCancel();
                                return;
                            }
                            return;
                        }
                        info[i] = bufferReader0.readInt();
                    }
                }
                case 29: {
                    RegionList.loadData(bufferReader0);
                    return;
                }
                case 30: {
                    if(bufferReader0.readInt() == 1) {
                        cnt = 19;
                        editor = MainService.instance.randEditor;
                    }
                    else {
                        cnt = 44;
                        editor = MainService.instance.timersEditor;
                    }
                    boolean[] arr_z = new boolean[cnt * 4];
                    for(int i = 0; true; ++i) {
                        if(i >= cnt * 4) {
                            editor.updateUsage(arr_z);
                            MainService.instance.notifyScript(((byte)v1), null);
                            return;
                        }
                        arr_z[i] = bufferReader0.readByte() != 0;
                    }
                }
                case 0x1F: {
                    long v4 = bufferReader0.readLong();
                    if(v1 != 0) {
                        Script.allocatedPage(v4);
                        MainService.instance.notifyScript(((byte)v1), null);
                        return;
                    }
                    MainService.instance.allocatedPage(v4);
                    return;
                }
                default: {
                    Log.w(("Unknown message: " + type + ", size: " + buffer.length));
                }
            }
        }
        catch(BufferUnderflowException e) {
            Log.e(("Failed process message: " + type + ", size: " + buffer.length), e);
        }
    }

    public void remove(byte seq, List list0) {
        AddressFlags daemonManager$AddressFlags0 = this.sort(list0);
        if(daemonManager$AddressFlags0.address.length == 0) {
            return;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            private static final int BATCH_COUNT = 100000000;

            @Override
            public void run() {
                InOut inOut = DaemonManager.this.inOut;
                for(int offset = 0; offset < daemonManager$AddressFlags0.address.length; offset += len) {
                    int len = daemonManager$AddressFlags0.address.length - offset <= 100000000 ? daemonManager$AddressFlags0.address.length - offset : 100000000;
                    inOut.startMessage(seq, 52, (InOut.longSize + 4) * len);
                    inOut.writeInt(len);
                    int[] flags = daemonManager$AddressFlags0.flags;
                    long[] address = daemonManager$AddressFlags0.address;
                    int i = offset;
                    int n = offset + len;
                    while(i < n) {
                        inOut.writeInt(flags[i]);
                        inOut.writeLong(address[i]);
                        ++i;
                    }
                    DaemonManager.this.send();
                }
            }
        });
    }

    public void remove(List list0) {
        this.remove(0, list0);
    }

    public void reset() {
        this.reset(0);
    }

    public void reset(byte seq) {
        this.M = 1;
        this.N = 1;
        this.timeJump = 0L;
        Unrandomizer.reset();
        this.inOut.truncate();
        this.send(seq, 42);
        SearchButtonListener.lastTextSearch = null;
    }

    public static void runAfterDaemonStart(Runnable run) {
        synchronized(DaemonManager.waitForDaemonStart) {
            if(DaemonManager.waitForDaemon) {
                DaemonManager.waitForDaemonStart.add(run);
            }
            else {
                new DaemonThread(run, "runAfterDaemonStart").start();
            }
        }
    }

    public void searchGroup(byte seq, int general_flags, int range, int[] flags, long[] from, long[] to, long memoryFrom, long memoryTo) {
        boolean isText = true;
        if((0x400000 & general_flags) == 0 || range == from.length) {
            for(int v4 = 0; v4 < flags.length; ++v4) {
                if(((flags[v4] | general_flags) & 0xFFBFFFFE) != 0) {
                    isText = false;
                    break;
                }
            }
        }
        if(isText) {
            byte[] text = new byte[from.length];
            for(int i = 0; i < from.length; ++i) {
                text[i] = (byte)(((int)from[i]));
            }
            this.searchText(seq, 1, text, memoryFrom, memoryTo);
            return;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                InOut inOut = DaemonManager.this.inOut;
                CharSequenceBuilder out = new CharSequenceBuilder();
                out.append(Re.s(0x7F070118));  // string:union "Group search"
                out.append(" = ");
                inOut.startMessage(seq, 55, 0);
                inOut.writeInt(general_flags);
                inOut.writeInt(range);
                inOut.writeLong(memoryFrom);
                inOut.writeLong(memoryTo);
                inOut.writeInt(flags.length);
                for(int i = 0; i < flags.length; ++i) {
                    from[i] = AddressItem.fixValue(from[i], flags[i]);
                    to[i] = AddressItem.fixValue(to[i], flags[i]);
                    inOut.writeInt(flags[i]);
                    inOut.writeLongLong(from[i]);
                    inOut.writeLongLong(to[i]);
                    String str = AddressItem.getStringDataTrim(0L, from[i], flags[i]);
                    if((flags[i] & 0x30000000) != 0) {
                        str = String.valueOf(str) + '~' + AddressItem.getStringDataTrim(0L, to[i], flags[i]);
                    }
                    if((flags[i] & 0x10000000) != 0) {
                        str = "≠ " + str;
                    }
                    if(i != 0) {
                        out.append("; ");
                    }
                    out.append(Tools.colorize((String.valueOf(str) + AddressItem.getShortName(flags[i])), AddressItem.getColor(flags[i])));
                }
                DaemonManager.this.send();
                out.append(" :");
                if((general_flags & 0x400000) != 0) {
                    out.append(":");
                }
                out.append(Tools.stringFormat("%,d", new Object[]{range}));
                MainService.instance.onProgress(out.toCharSequence());
                DaemonManager.lastSearchFuzzy = false;
            }
        });
        SearchButtonListener.lastTextSearch = null;
    }

    public void searchGroup(int general_flags, int range, int[] flags, long[] from, long[] to, long memoryFrom, long memoryTo) {
        this.searchGroup(0, general_flags, range, flags, from, to, memoryFrom, memoryTo);
    }

    public void searchMask(byte seq, long addr, long mask, int flags, long memoryFrom, long memoryTo) {
        Log.d(("searchMask: " + Long.toHexString(addr) + ':' + Long.toHexString(mask) + " as " + Integer.toHexString(flags) + " in " + Long.toHexString(memoryFrom) + '-' + Long.toHexString(memoryTo)));
        CharSequence sign = null;
        SparseArray sparseArray0 = AddressItem.getSignNames();
        for(int i = 0; i < sparseArray0.size(); ++i) {
            int v6 = sparseArray0.keyAt(i);
            if(v6 != 0 && (flags & v6) == v6) {
                sign = (CharSequence)sparseArray0.valueAt(i);
            }
        }
        String strAddr = Long.toHexString(addr).toUpperCase(Locale.US);
        String strMask = Long.toHexString(mask).toUpperCase(Locale.US);
        if(strAddr.length() < strMask.length()) {
            strAddr = new String(new char[strMask.length() - strAddr.length()]).replace('\u0000', '0') + strAddr;
        }
        else if(strAddr.length() > strMask.length()) {
            strMask = new String(new char[strAddr.length() - strMask.length()]).replace('\u0000', '0') + strMask;
        }
        MainService.instance.onProgress(new CharSequenceBuilder().appendFormat("%s %s %s; %s: %s; %s: %s", new CharSequence[]{Tools.stripColon(0x7F07008E), sign, strAddr, Tools.stripColon(0x7F0701FD), strMask, Tools.stripColon(0x7F070087), AddressItem.getNameShort(flags & 0x7F)}).toCharSequence());  // string:address "Address:"
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 0x3F, 0);
                DaemonManager.this.inOut.writeLong(addr);
                DaemonManager.this.inOut.writeLong(mask);
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.send();
                DaemonManager.lastSearchFuzzy = false;
            }
        });
        SearchButtonListener.lastTextSearch = null;
    }

    public void searchMask(long addr, long mask, int flags, long memoryFrom, long memoryTo) {
        this.searchMask(0, addr, mask, flags, memoryFrom, memoryTo);
    }

    public void searchNumber(byte seq, long number, int x, int flags, long memoryFrom, long memoryTo) {
        CharSequence sign = null;
        SparseArray sparseArray0 = AddressItem.getSignNames();
        if((0x2000000 & flags) == 0) {
            for(int i = 0; i < sparseArray0.size(); ++i) {
                int v6 = sparseArray0.keyAt(i);
                if(v6 != 0 && (flags & v6) == v6) {
                    sign = (CharSequence)sparseArray0.valueAt(i);
                }
            }
        }
        else {
            sign = sparseArray0.get(0x20000000) + '*';
        }
        AddressItem addressItem0 = new AddressItem(0L, number, flags & 0x7F);
        CharSequenceBuilder out = new CharSequenceBuilder();
        out.appendFormat("%s %s %s", new CharSequence[]{Re.s(0x7F0700F0), sign, Tools.colorize(Tools.stringFormat("%s%s %s", new Object[]{addressItem0.getStringDataTrim(), (x == 0 ? "" : " X" + x), addressItem0.getNameShort()}), addressItem0.getColor())});  // string:value "Value"
        MainService.instance.onProgress(out.toCharSequence());
        this.sendSearchNumber(seq, number, 0L, x, flags, memoryFrom, memoryTo);
    }

    public void searchNumber(long number, int xor, int flags, long memoryFrom, long memoryTo) {
        this.searchNumber(0, number, xor, flags, memoryFrom, memoryTo);
    }

    public void searchPointer(byte seq, short maxOffset, int flags, long memoryFrom, long memoryTo) {
        String s = Integer.toHexString(maxOffset);
        Log.d(("searchPointer: " + s + " as " + Integer.toHexString(flags) + " in " + Long.toHexString(memoryFrom) + '-' + Long.toHexString(memoryTo)));
        MainService.instance.onProgress("-> " + s);
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 0x40, 0);
                DaemonManager.this.inOut.writeInt(((int)maxOffset));
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.send();
                DaemonManager.lastSearchFuzzy = false;
            }
        });
        SearchButtonListener.lastTextSearch = null;
    }

    public void searchPointer(byte b, short v, int v1, long v2, long v3, long v4) {
        String s = Integer.toHexString(v);
        Log.d(("searchPointer: " + s + " as " + Integer.toHexString(v1) + " in " + Long.toHexString(v2) + '-' + Long.toHexString(v3) + ", " + Long.toString(v4)));
        MainService.instance.onProgress("-> " + s);
        ThreadManager.runOnWriteThread(new DaemonManager.36(this, b, v, v1, v2, v3, v4));
        SearchButtonListener.lastTextSearch = null;
    }

    public void searchRangeNumber(byte seq, long number, long number2, int x, int flags, long memoryFrom, long memoryTo) {
        CharSequence sign = null;
        SparseArray sparseArray0 = AddressItem.getSignNames();
        if((0x2000000 & flags) != 0) {
            flags &= 0xFDFFFFFF;
        }
        for(int i = 0; i < sparseArray0.size(); ++i) {
            int v7 = sparseArray0.keyAt(i);
            if(v7 != 0 && (flags & v7) == v7) {
                sign = (CharSequence)sparseArray0.valueAt(i);
            }
        }
        AddressItem addressItem0 = new AddressItem(0L, number, flags & 0x7F);
        AddressItem addressItem1 = new AddressItem(0L, number2, flags & 0x7F);
        CharSequenceBuilder out = new CharSequenceBuilder();
        out.appendFormat("%s %s %s", new CharSequence[]{Re.s(0x7F0700F0), sign, Tools.colorize(Tools.stringFormat("%s ~ %s%s %s", new Object[]{addressItem0.getStringDataTrim(), addressItem1.getStringDataTrim(), (x == 0 ? "" : " X" + x), addressItem0.getNameShort()}), addressItem0.getColor())});  // string:value "Value"
        MainService.instance.onProgress(out.toCharSequence());
        this.sendSearchNumber(seq, number, number2, x, flags | 0x800000, memoryFrom, memoryTo);
    }

    public void searchRangeNumber(long number, long number2, int x, int flags, long memoryFrom, long memoryTo) {
        this.searchRangeNumber(0, number, number2, x, flags, memoryFrom, memoryTo);
    }

    public void searchText(byte seq, int flags, byte[] text, long memoryFrom, long memoryTo) {
        String printText = HexText.getText(null, flags, text, text.length, true, true, null).toString();
        if(printText.length() > 0x30) {
            printText = printText.substring(0, 0x30) + "...";
        }
        Log.d(("searchText: [" + text.length + "] = " + printText + " in " + Long.toHexString(memoryFrom) + '-' + Long.toHexString(memoryTo)));
        if(text.length == 0) {
            return;
        }
        if(text.length == 1) {
            this.searchNumber(seq, ((long)text[0]), 0, 0x20000001, memoryFrom, memoryTo);
            return;
        }
        MainService.instance.onProgress(Tools.stringFormat("%s[%d] = %s", new Object[]{Tools.stripColon(0x7F0702B9), ((int)text.length), printText}));  // string:text "Text"
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 34, 0);
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.inOut.writeInt(text.length);
                DaemonManager.this.inOut.writeBytes(text, text.length);
                DaemonManager.this.send();
                DaemonManager.lastSearchFuzzy = false;
            }
        });
        SearchButtonListener.lastTextSearch = null;
    }

    public void searchText(int flags, byte[] text, long memoryFrom, long memoryTo) {
        this.searchText(0, flags, text, memoryFrom, memoryTo);
    }

    void send() {
        InOut inOut = this.inOut;
        if(DaemonManager.lastSendError != null) {
            Log.e(("Last send error: " + DaemonManager.lastSendError));
            inOut.clear();
            return;
        }
        try {
            inOut.send();
            DaemonManager.lastSendError = null;
        }
        catch(Throwable e) {
            inOut.clear();
            Log.e("Send error", e);
            DaemonManager.lastSendError = e.toString();
            ThreadManager.getHandlerUiThread().post(() -> {
                Log.d("messageFailed");
                DaemonManager.this.isProcessValid();
                MainService.instance.dismissBusyDialog();
            });
        }

        class android.ext.DaemonManager.33 implements Runnable {
            @Override
            public void run() {
                DaemonManager.this.messageFailed();
            }
        }

    }

    void send(byte seq, byte cmd) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, cmd, 0);
                DaemonManager.this.send();
            }
        });
    }

    void sendClientPid(byte seq) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 0x2F, 0);
                int v = android.os.Process.myPid();
                DaemonManager.this.inOut.writeInt(v);
                DaemonManager.this.inOut.writeInt(DaemonManager.this.suPid);
                DaemonManager.this.send();
            }
        });
    }

    public void sendConfig() {
        this.sendConfig(0);
    }

    public void sendConfig(byte seq) {
        if(this.oldDataInRam == null || Config.dataInRam != ((int)this.oldDataInRam)) {
            if(this.oldDataInRam != null) {
                MainService.instance.clear();
            }
            this.oldDataInRam = Config.dataInRam;
        }
        String s = Tools.getFilesDirHidden().getAbsolutePath();
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 54, 0);
                DaemonManager.this.inOut.writeInt(Config.dataInRam);
                DaemonManager.this.inOut.writeInt(Config.configDaemon);
                DaemonManager.this.inOut.writeLongLong(Config.interceptTimers);
                DaemonManager.this.inOut.writeInt(Config.freezeInterval);
                DaemonManager.this.inOut.writeInt(Config.usedRanges);
                DaemonManager.this.inOut.writeInt(s.getBytes().length);
                DaemonManager.this.inOut.writeBytes(s.getBytes(), s.getBytes().length);
                long v = DaemonManager.this.inOut.crc();
                if(v != DaemonManager.this.prevConfigCrc) {
                    DaemonManager.this.prevConfigCrc = v;
                    DaemonManager.this.send();
                    Log.d(("Sended config: " + Config.dataInRam + ' ' + "0" + ' ' + "0" + ' ' + Config.freezeInterval + ' ' + Config.usedRanges + ' ' + s + "; " + s.getBytes().length));
                }
            }
        });
    }

    private void sendSearchNumber(byte seq, long number, long number2, int xor, int flags, long memoryFrom, long memoryTo) {
        long v6 = AddressItem.fixValue(number, flags);
        long v7 = AddressItem.fixValue(number2, flags);
        Log.d(("sendSearchNumber: " + v6 + "; " + v7 + " x" + xor + " as " + Integer.toHexString(flags) + " in " + Long.toHexString(memoryFrom) + '-' + Long.toHexString(memoryTo)));
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                boolean z = false;
                DaemonManager.this.inOut.startMessage(seq, 38, 0);
                DaemonManager.this.inOut.writeLongLong(v6);
                DaemonManager.this.inOut.writeLongLong(v7);
                DaemonManager.this.inOut.writeInt(xor);
                DaemonManager.this.inOut.writeInt(flags);
                DaemonManager.this.inOut.writeLong(memoryFrom);
                DaemonManager.this.inOut.writeLong(memoryTo);
                DaemonManager.this.send();
                if((flags & 0x80000000) != 0) {
                    z = true;
                }
                DaemonManager.lastSearchFuzzy = z;
            }
        });
        SearchButtonListener.lastTextSearch = null;
    }

    private void sendSearchNumber(byte seq, long number, long number2, int flags, long memoryFrom, long memoryTo) {
        this.sendSearchNumber(seq, number, number2, 0, flags, memoryFrom, memoryTo);
    }

    public void setPath(byte seq, String path) {
        byte[] arr_b = path.getBytes();
        if(arr_b.length == 0) {
            return;
        }
        if(arr_b.length > 4000) {
            Log.e(("path big: " + arr_b.length + " > " + 4000));
            return;
        }
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 0x30, arr_b.length + 4);
                DaemonManager.this.inOut.writeInt(arr_b.length);
                DaemonManager.this.inOut.writeBytes(arr_b, arr_b.length);
                DaemonManager.this.send();
            }
        });
    }

    public void setPath(String path) {
        this.setPath(0, path);
    }

    public void setPid(byte seq, int pid, String packageName, String libPath) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                byte[] arr_b = packageName.getBytes();
                int pkgLen = arr_b.length <= 0xFF ? arr_b.length : 0xFF;
                byte[] lib = libPath == null ? ContainerHelpers.EMPTY_BYTES : libPath.getBytes();
                int libLen = lib.length <= 0xFF ? lib.length : 0xFF;
                DaemonManager.this.inOut.startMessage(seq, 37, 0);
                DaemonManager.this.inOut.writeInt(pid);
                DaemonManager.this.inOut.writeByte(((byte)pkgLen));
                DaemonManager.this.inOut.writeBytes(arr_b, pkgLen);
                DaemonManager.this.inOut.writeByte(((byte)libLen));
                DaemonManager.this.inOut.writeBytes(lib, libLen);
                DaemonManager.this.send();
            }
        });
        this.sendConfig(seq);
    }

    public void setPid(int pid, String packageName, String libPath) {
        this.setPid(0, pid, packageName, libPath);
    }

    public void setSpeed() {
        this.setSpeed(0);
    }

    public void setSpeed(byte seq) {
        this.setSpeed(seq, this.M, this.N);
    }

    public void setSpeed(byte seq, int M, int N) {
        this.initSh();
        boolean[] arr_z = this.getConfig(0);
        this.M = M;
        this.N = N;
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 35, 0);
                DaemonManager.this.inOut.writeInt(16);
                DaemonManager.this.writeConfig(arr_z);
                DaemonManager.this.inOut.writeInt(M);
                DaemonManager.this.inOut.writeInt(N);
                DaemonManager.this.inOut.writeLongLong(this.timeJump);
                DaemonManager.this.send();
            }
        });
        this.timeJump = 0L;
    }

    public void setSpeed(int M, int N) {
        this.setSpeed(0, M, N);
    }

    public void shConfig(byte seq, int type) {
        this.initSh();
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 35, 0);
                DaemonManager.this.inOut.writeInt(type | 0x20);
                DaemonManager.this.writeConfig(this.getConfig(type));
                DaemonManager.this.send();
            }
        });
    }

    public void shConfig(int type) {
        this.shConfig(0, type);
    }

    public void shUsage(byte seq, int type) {
        this.initSh();
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 35, 0);
                DaemonManager.this.inOut.writeInt(type | 8);
                DaemonManager.this.writeConfig(this.getConfig(type));
                DaemonManager.this.send();
            }
        });
    }

    public void shUsage(int type) {
        this.shUsage(0, type);
    }

    public void signal(byte signal) {
        this.signal(0, signal);
    }

    public void signal(byte seq, byte signal) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 53, 0);
                DaemonManager.this.inOut.writeByte(signal);
                DaemonManager.this.send();
            }
        });
    }

    AddressFlags sort(List list0) {
        ArrayList arrayList0 = new ArrayList(list0.size());
        int v = list0.size();
        for(int i = 0; i < v; ++i) {
            AddressItem item = (AddressItem)list0.get(i);
            if(item != null) {
                arrayList0.add(new ItemInfo(item.address, item.flags));
            }
        }
        ItemInfo[] arr = (ItemInfo[])arrayList0.toArray(new ItemInfo[arrayList0.size()]);
        Arrays.sort(arr);
        ArrayList arrayList1 = new ArrayList(arr.length);
        ItemInfo last = null;
        for(int i = 0; i < arr.length; ++i) {
            ItemInfo item = arr[i];
            if(item != null) {
                if(last != null && last.address == item.address) {
                    last.flags |= item.flags;
                }
                else {
                    arrayList1.add(item);
                    last = item;
                }
            }
        }
        int size = arrayList1.size();
        long[] address = new long[size];
        int[] flags = new int[size];
        int allFlags = 0;
        for(int i = 0; i < size; ++i) {
            ItemInfo item = (ItemInfo)arrayList1.get(i);
            address[i] = item.address;
            flags[i] = item.flags;
            allFlags |= item.flags;
        }
        AddressFlags ret = new AddressFlags(address, flags);
        ret.allFlags = allFlags & 0x7F;
        return ret;
    }

    public void start() {
        new DaemonThread("DaemonLoader") {
            @Override
            public void run() {
                int v4;
                int v3;
                InOut inOut;
                try {
                    inOut = DaemonManager.this.inOut;
                    String s = DaemonLoader.getDaemonPath();
                    Proc daemonManager$Proc0 = DaemonManager.this.startDaemon(s);
                    DaemonManager.this.mProcess = daemonManager$Proc0;
                    RootDetector.debug = "";
                    inOut.setStreams(daemonManager$Proc0.stdout, daemonManager$Proc0.stdin);
                    inOut.setStarted(true);
                    MainService.instance.onStatusChanged();
                    Log.runLogOnProcessErrStream(daemonManager$Proc0.process, daemonManager$Proc0.stderr);
                    byte[] arr_b = inOut.readPacket();
                    if(arr_b == null) {
                        Log.d("DI: null");
                    }
                    else if(arr_b.length == 7) {
                        BufferReader bufferReader0 = new BufferReader(arr_b);
                        int v = bufferReader0.readByte();
                        InOut.setX64(v == 8);
                        int v1 = bufferReader0.readByte();
                        if(v1 != 4) {
                            Log.e(("DI: QE " + v1));
                        }
                        DaemonManager.this.daemonPid = bufferReader0.readInt();
                        Log.d(("DI: " + v + " " + v1 + " " + DaemonManager.this.daemonPid));
                    }
                    else {
                        Log.d(("DI: " + arr_b.length));
                    }
                    BufferReader bufferReader1 = new BufferReader(inOut.readPacket());
                    bufferReader1.readByte();
                    byte[] name = new byte[bufferReader1.readByte()];
                    bufferReader1.read(name);
                    DaemonManager.this.daemonName = new String(name);
                    Log.d(("InOut: x64: " + InOut.x64 + "; QE_ALIGN: " + 4 + "; " + DaemonManager.this.daemonPid + "; " + DaemonManager.this.daemonName));
                    DaemonManager.this.mReaderThread.start();
                    MainService.instance.onStatusChanged();
                    inOut.sendWait();
                    DaemonManager.this.sendClientPid(0);
                }
                catch(CheckException e) {
                    Log.e("start daemon failed", e);
                    String msg = e.getMessage();
                    StringBuilder abis = new StringBuilder();
                    boolean needArm = false;
                    try {
                        abis.append("arm64-v8a");
                        abis.append("; ");
                        abis.append("arm64-v8a");
                        abis.append("; ");
                    }
                    catch(Throwable e2) {
                        Log.w("Failed get ABI", e2);
                    }
                    if(Build.VERSION.SDK_INT >= 21) {
                        try {
                            String[] arr_s = Build.SUPPORTED_ABIS;
                            for(int v2 = 0; v2 < arr_s.length; ++v2) {
                                String abi = arr_s[v2];
                                abis.append(abi);
                                abis.append("; ");
                                if("armeabi".equals(abi)) {
                                    needArm = true;
                                }
                            }
                        }
                        catch(Throwable e2) {
                            Log.w("Failed get ABI", e2);
                        }
                        try {
                            String[] arr_s1 = Build.SUPPORTED_32_BIT_ABIS;
                            v3 = 0;
                            while(true) {
                            label_63:
                                if(v3 >= arr_s1.length) {
                                    goto label_75;
                                }
                                String abi = arr_s1[v3];
                                abis.append(abi);
                                abis.append("; ");
                                if("armeabi".equals(abi)) {
                                    break;
                                }
                                ++v3;
                            }
                        }
                        catch(Throwable e2) {
                            Log.w("Failed get ABI", e2);
                            goto label_75;
                        }
                        needArm = true;
                        ++v3;
                        goto label_63;
                        try {
                        label_75:
                            String[] arr_s2 = Build.SUPPORTED_64_BIT_ABIS;
                            v4 = 0;
                            while(true) {
                            label_77:
                                if(v4 >= arr_s2.length) {
                                    goto label_89;
                                }
                                String abi = arr_s2[v4];
                                abis.append(abi);
                                abis.append("; ");
                                if("armeabi".equals(abi)) {
                                    break;
                                }
                                ++v4;
                            }
                        }
                        catch(Throwable e2) {
                            Log.w("Failed get ABI", e2);
                            goto label_89;
                        }
                        needArm = true;
                        ++v4;
                        goto label_77;
                    }
                label_89:
                    if(needArm && Re.i("chunka", raw.class) == 0) {
                        msg = msg + "\n\n" + Tools.stripColon(0x7F070249) + ": " + Apk.ARM_VERSION;  // string:try_arm_version "Try using the version of __app_name__ with support for older 
                                                                                                     // ARM processors:"
                    }
                    String s5 = abis.toString();
                    if(s5.contains("mips")) {
                        msg = msg + "\n\n" + Re.s(0x7F07027B);  // string:mips_arch "This application does not support the MIPS architecture."
                    }
                    String msg = msg + "\n\nABIs: " + s5 + '\n' + DaemonLoader.TESTED + '\n' + Build.VERSION.SDK_INT + '\n' + DaemonLoader.DEBUG;
                    Log.d(("Supported ABIs: " + s5));
                    Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700A7)).setMessage(msg).setNegativeButton(Re.s(0x7F07009D), null));  // string:failed_load_daemon "Failed to load daemon"
                    inOut.setStarted(false);
                    MainService.instance.onStatusChanged();
                }
                catch(Throwable e) {
                    Log.e("start daemon failed", e);
                    ThreadManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String s = Re.s(0x7F0700A8) + "\n\n" + Re.s(0x7F07015C);  // string:is_device_rooted "Unable to obtain root access."
                                AlertDialog alertDialog0 = Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700A7)).setMessage(s).setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:failed_load_daemon "Failed to load daemon"
                                    @Override  // android.content.DialogInterface$OnClickListener
                                    public void onClick(DialogInterface dialog, int which) {
                                        ConfigListAdapter.showHelp(0x7F07028A);  // string:work_without_root "__app_name__ can work without root, through a virtual 
                                                                                 // environment. For example, through __vspace_list__ and many others.\n__app_name__ 
                                                                                 // must be installed in a virtual environment. The game must also be installed there.\nNot 
                                                                                 // all features of the program are supported, but most functions should work.\n\nYou 
                                                                                 // will not be able to crack games that are not installed in a virtual environment.\n\nThe 
                                                                                 // first launch of __app_name__, which performs a reinstallation with a random package 
                                                                                 // name, must not be performed through a virtual environment.\nThen you can add __app_name__ 
                                                                                 // to the virtual environment and use."
                                    }
                                }).setNegativeButton(Re.s(0x7F07009D), null).create();  // string:ok "OK"
                                Alert.show(alertDialog0);
                                Tools.setClickableText(alertDialog0);
                            }
                            catch(Throwable e) {
                                Log.badImplementation(e);
                            }
                        }
                    });
                    inOut.setStarted(false);
                    MainService.instance.onStatusChanged();
                }
                DaemonManager.daemonStarted();
            }
        }.start();
    }

    public Proc startDaemon(String execPath) throws CheckException {
        Proc proc;
        Process process0;
        try {
            boolean direct = Config.vSpace;
            File file0 = Tools.getFilesDirHidden();
            String[] exec = this.getExec(direct, execPath, file0);
            boolean fixed = false;
            boolean system = false;
            while(true) {
                for(int v = 0; v < 3; ++v) {
                    new File(file0, new String[]{"i", "o", "e"}[v]).delete();
                }
                if(direct) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("try: direct");
                    int v1 = exec.length;
                    for(int v2 = 0; v2 < v1; ++v2) {
                        String part = exec[v2];
                        sb.append(' ');
                        sb.append(part);
                    }
                    Log.d(sb.toString());
                }
                process0 = direct ? Tools.exec(exec) : RootDetector.tryRoot(exec[0], null, true);
                StringBuilder stringBuilder1 = new StringBuilder("Daemon process: ").append(process0).append("; pid=");
                String s2 = process0 == null ? "n/a" : Tools.getPid(process0);
                Log.d(stringBuilder1.append(s2).toString());
                proc = new Proc();
                proc.process = process0;
                if(process0 != null) {
                    proc.stdin = process0.getOutputStream();
                    proc.stdout = process0.getInputStream();
                    proc.stderr = process0.getErrorStream();
                }
                if(DaemonManager.checkDaemon(proc, file0)) {
                    break;
                }
                if(fixed) {
                    if(Config.vSpace) {
                        if(!direct) {
                            goto label_45;
                        }
                    }
                    else if(!system) {
                        system = true;
                        String s3 = DaemonManager.system(execPath);
                        if(s3 != null) {
                            exec = this.getExec(direct, s3, file0);
                            continue;
                        }
                    }
                    else {
                    label_45:
                        this.checkBinary(execPath, true);
                        Thread.sleep(1000L);
                        RootDetector.debug = "" + RootDetector.getInfo(process0);
                        Log.d("");
                        BaseActivity.waitExit = true;
                        Log.e("Show error alert");
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700A3)).setMessage(Re.s(0x7F0700A4) + "\n\n" + "").setPositiveButton(Re.s(0x7F0700B8), new ExitListener(400)).setNeutralButton("SU", new DialogInterface.OnClickListener() {  // string:daemon_failed "Daemon failed"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                ConfigListAdapter.changeSu();
                            }
                        }).setNegativeButton(Re.s(0x7F0700B9), null));  // string:skip "Ignore"
                        return null;
                    }
                    direct = false;
                    exec = this.getExec(false, execPath, file0);
                }
                else {
                    fixed = true;
                    String s4 = "exec id";
                    Log.d("Test 1 start");
                    if(direct) {
                        s4 = "id";
                    }
                    RootDetector.runCmd(s4, 45, (direct ? "" : null));
                    RootDetector.debug = "";
                    Log.d("Test 1 end");
                    Tools.chmod(new File(execPath), 493);
                    Tools.chmod(execPath, "0755");
                }
            }
            DaemonManager.sProcess = proc;
            this.suPid = Tools.getPid(process0);
            if(this.isProcessValid()) {
                ThreadManager.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler0 = ThreadManager.getHandlerMainThread();
                        handler0.removeCallbacks(this);
                        if(DaemonManager.this.isProcessValid()) {
                            handler0.postDelayed(this, 1000L);
                        }
                    }
                });
                return proc;
            }
        }
        catch(CheckException ex) {
            throw ex;
        }
        catch(Exception ex) {
            Log.e(ex.getMessage(), ex);
            Tools.showToast(ex.getMessage());
        }
        return null;
    }

    public void stop() {
        this.stop(0);
    }

    public void stop(byte seq) {
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.signal(seq, 1);
                DaemonManager.this.send(seq, 45);
                DaemonManager.this.inOut.setStarted(false);
                MainService.instance.onStatusChanged();
            }
        });
        try {
            Thread.sleep(3L);
        }
        catch(InterruptedException unused_ex) {
        }
    }

    private void stopCancel() {
        this.needCancel = false;
    }

    public static String system(String execPath) {
        String dbg = null;
        try {
            File file0 = new File(execPath);
            if(!file0.exists() || !file0.canExecute()) {
                return null;
            }
            String s2 = file0.getName();
            String[] arr_s = {"/system/bin", "/system/xbin", "/sbin/.magisk/mirror/data", "/sbin/.magisk/modules", "/sbin"};
            try {
                for(int v = 0; v < 5; ++v) {
                    File file = new File(arr_s[v], s2);
                    if(file.exists() && file.canExecute() && file0.length() == file.length()) {
                        return file.getAbsolutePath();
                    }
                }
                String type = null;
                String dev = null;
                String[] arr_s1 = RootDetector.runCmd("exec mount", 10).split("\n");
                for(int v1 = 0; v1 < arr_s1.length; ++v1) {
                    String line = arr_s1[v1];
                    if(line != null && line.contains(" /system ")) {
                        String[] arr_s2 = line.trim().split(" ");
                        for(int v2 = 0; v2 < arr_s2.length; ++v2) {
                            String part = arr_s2[v2].trim();
                            if(!"on".equals(part) && !"type".equals(part) && !"/system".equals(part)) {
                                if(dev != null) {
                                    type = part;
                                    break;
                                }
                                dev = part;
                            }
                        }
                        break;
                    }
                }
                if(dev == null) {
                    return null;
                }
                StringBuilder cmd = new StringBuilder();
                for(int v3 = 0; v3 < 2; ++v3) {
                    String mode = new String[]{"rw", "ro"}[v3];
                    for(int v4 = 0; v4 < 4; ++v4) {
                        String bin = new String[]{"", "/system/bin/", "/system/xbin/", "/system/sbin/"}[v4];
                        String[] arr_s3 = {mode + ",remount", "remount," + mode};
                        for(int v5 = 0; v5 < 2; ++v5) {
                            String opt = arr_s3[v5];
                            String[] arr_s4 = {null, type};
                            for(int v6 = 0; v6 < 2; ++v6) {
                                String t = arr_s4[v6];
                                cmd.append(bin);
                                cmd.append("mount -o ");
                                cmd.append(opt);
                                if(t != null) {
                                    cmd.append(" -t ");
                                    cmd.append(t);
                                }
                                cmd.append(" ");
                                cmd.append(dev);
                                cmd.append(" /system; ");
                            }
                        }
                    }
                    cmd.append("blockdev --set");
                    cmd.append(mode);
                    cmd.append(" ");
                    cmd.append(dev);
                    cmd.append("; ");
                    if(mode.equals("rw")) {
                        for(int v7 = 0; v7 < 5; ++v7) {
                            String dir = arr_s[v7];
                            if(dir.startsWith("/system")) {
                                File file = new File(dir, s2);
                                cmd.append("cp -f ");
                                cmd.append(execPath);
                                cmd.append(" ");
                                cmd.append(file.getAbsolutePath());
                                cmd.append("; chmod 0755 ");
                                cmd.append(file.getAbsolutePath());
                                cmd.append("; ");
                            }
                        }
                    }
                }
                cmd.append("exit");
                dbg = cmd.toString();
                dbg = dbg + "\n" + RootDetector.runCmd(dbg, 15);
                for(int v8 = 0; v8 < 5; ++v8) {
                    File file = new File(arr_s[v8], s2);
                    if(file.exists() && file.canExecute() && file0.length() == file.length()) {
                        return file.getAbsolutePath();
                    }
                }
                Log.e(("Failed remount 1 " + execPath + "\n" + dbg));
            }
            catch(Throwable e) {
                Log.e(("Failed remount 2 " + execPath + "\n" + dbg), e);
            }
            StringBuilder cmd = new StringBuilder();
            for(int v9 = 0; true; ++v9) {
                if(v9 >= 5) {
                    cmd.append("exit");
                    dbg = cmd.toString();
                    dbg = dbg + "\n" + RootDetector.runCmd(dbg, 15);
                    for(int v10 = 0; true; ++v10) {
                        if(v10 >= 5) {
                            Log.e(("Failed remount 3 " + execPath + "\n" + dbg));
                            return null;
                        }
                        String dir = arr_s[v10];
                        if(dir.startsWith("/sbin")) {
                            File file = new File(dir, s2);
                            if(file.exists() && file.canExecute() && file0.length() == file.length()) {
                                return file.getAbsolutePath();
                            }
                        }
                    }
                }
                String dir = arr_s[v9];
                if(dir.startsWith("/sbin")) {
                    File file = new File(dir, s2);
                    cmd.append("cp -f ");
                    cmd.append(execPath);
                    cmd.append(" ");
                    cmd.append(file.getAbsolutePath());
                    cmd.append("; chmod 0755 ");
                    cmd.append(file.getAbsolutePath());
                    cmd.append("; ");
                }
            }
        }
        catch(Throwable e) {
            Log.e(("Failed remount 4 " + execPath + "\n" + dbg), e);
            return null;
        }
    }

    public void takeScreenshot() {
        this.send(0, 51);
    }

    public void unrand(byte seq, int flags, long qBase, long qInc, double dBase, double dInc) {
        this.initSh();
        ThreadManager.runOnWriteThread(new Runnable() {
            @Override
            public void run() {
                DaemonManager.this.inOut.startMessage(seq, 35, 0);
                DaemonManager.this.inOut.writeInt(flags | 17);
                DaemonManager.this.writeConfig(this.getConfig(1));
                DaemonManager.this.inOut.writeLongLong(qBase);
                DaemonManager.this.inOut.writeLongLong(qInc);
                DaemonManager.this.inOut.writeLongLong(Double.doubleToRawLongBits(dBase));
                DaemonManager.this.inOut.writeLongLong(Double.doubleToRawLongBits(dInc));
                DaemonManager.this.send();
            }
        });
    }

    public void unrand(int flags, long qBase, long qInc, double dBase, double dInc) {
        this.unrand(0, flags, qBase, qInc, dBase, dInc);
    }

    void writeConfig(boolean[] config) {
        InOut inOut = this.inOut;
        for(int i = 0; i < config.length; ++i) {
            inOut.writeByte(((byte)(config[i] ? 1 : 0)));
        }
    }
}

