package android.lang;

import android.ext.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class UNIXProcess extends Process {
    static class NullInputStream extends InputStream {
        static final NullInputStream INSTANCE;

        static {
            NullInputStream.INSTANCE = new NullInputStream();
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public int read() {
            return -1;
        }
    }

    static class NullOutputStream extends OutputStream {
        static final NullOutputStream INSTANCE;

        static {
            NullOutputStream.INSTANCE = new NullOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            throw new IOException("Stream closed");
        }
    }

    static class ProcessPipeInputStream extends BufferedInputStream {
        ProcessPipeInputStream(int fd) {
            super(UNIXProcess.getFileInputStream(fd));
        }

        private static byte[] drainInputStream(InputStream in) throws IOException {
            if(in == null) {
                return null;
            }
            int n = 0;
            byte[] a = null;
            int v1;
            while((v1 = in.available()) > 0) {
                a = a == null ? new byte[v1] : Arrays.copyOf(a, n + v1);
                n += in.read(a, n, v1);
            }
            return a == null || n == a.length ? a : Arrays.copyOf(a, n);
        }

        void processExited() {
            synchronized(this) {
                InputStream in = this.in;
                if(in != null) {
                    try {
                        byte[] arr_b = ProcessPipeInputStream.drainInputStream(in);
                        in.close();
                        NullInputStream uNIXProcess$NullInputStream0 = arr_b == null ? NullInputStream.INSTANCE : new ByteArrayInputStream(arr_b);
                        this.in = uNIXProcess$NullInputStream0;
                        if(this.buf == null) {
                            this.in = null;
                        }
                    }
                    catch(IOException unused_ex) {
                    }
                }
            }
        }
    }

    static class ProcessPipeOutputStream extends BufferedOutputStream {
        ProcessPipeOutputStream(int fd) {
            super(UNIXProcess.getFileOutputStream(fd));
        }

        void processExited() {
            synchronized(this) {
                OutputStream out = this.out;
                if(out != null) {
                    try {
                        out.close();
                    }
                    catch(IOException unused_ex) {
                    }
                    this.out = NullOutputStream.INSTANCE;
                }
            }
        }
    }

    static class ProcessReaperThreadFactory implements ThreadFactory {
        private static final ThreadGroup group;

        static {
            ProcessReaperThreadFactory.group = ProcessReaperThreadFactory.getRootThreadGroup();
        }

        private ProcessReaperThreadFactory() {
        }

        ProcessReaperThreadFactory(ProcessReaperThreadFactory uNIXProcess$ProcessReaperThreadFactory0) {
        }

        private static ThreadGroup getRootThreadGroup() {
            return (ThreadGroup)UNIXProcess.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    return this.run();
                }

                public ThreadGroup run() {
                    ThreadGroup root;
                    for(root = Thread.currentThread().getThreadGroup(); true; root = threadGroup1) {
                        ThreadGroup threadGroup1 = root.getParent();
                        if(threadGroup1 == null || root == threadGroup1) {
                            break;
                        }
                    }
                    return root;
                }
            });
        }

        @Override
        public Thread newThread(Runnable grimReaper) {
            Thread thread0 = new Thread(ProcessReaperThreadFactory.group, grimReaper, "process reaper", 0x8000L);
            thread0.setDaemon(true);
            thread0.setPriority(10);
            return thread0;
        }
    }

    private int exitcode;
    private boolean hasExited;
    private final int pid;
    private static final Executor processReaperExecutor;
    private InputStream stderr;
    private OutputStream stdin;
    private InputStream stdout;

    static {
        UNIXProcess.processReaperExecutor = (Executor)UNIXProcess.doPrivileged(new PrivilegedAction() {
            @Override
            public Object run() {
                return this.run();
            }

            public Executor run() {
                return Executors.newCachedThreadPool(new ProcessReaperThreadFactory(null));
            }
        });
        UNIXProcess.initIDs();
    }

    UNIXProcess(byte[] prog, byte[] argBlock, int argc, byte[] envBlock, int envc, byte[] dir, int[] fds, boolean redirectErrorStream) throws IOException {
        this.pid = this.forkAndExec(prog, argBlock, argc, envBlock, envc, dir, fds, redirectErrorStream);
        try {
            UNIXProcess.doPrivileged(new PrivilegedExceptionAction() {
                @Override
                public Object run() throws Exception {
                    return this.run();
                }

                public Void run() throws IOException {
                    UNIXProcess.this.initStreams(fds);
                    return null;
                }
            });
        }
        catch(PrivilegedActionException ex) {
            throw (IOException)ex.getException();
        }
    }

    @Override
    public void destroy() {
        synchronized(this) {
            if(!this.hasExited) {
                UNIXProcess.destroyProcess(this.pid);
            }
        }
        try {
            this.stdin.close();
        }
        catch(IOException unused_ex) {
        }
        try {
            this.stdout.close();
        }
        catch(IOException unused_ex) {
        }
        try {
            this.stderr.close();
        }
        catch(IOException unused_ex) {
        }
    }

    private static native void destroyProcess(int arg0) {
    }

    public static Object doPrivileged(PrivilegedAction privilegedAction0) {
        return AccessController.doPrivileged(privilegedAction0);
    }

    public static Object doPrivileged(PrivilegedExceptionAction privilegedExceptionAction0) throws PrivilegedActionException {
        return AccessController.doPrivileged(privilegedExceptionAction0);
    }

    @Override
    public int exitValue() {
        synchronized(this) {
            if(!this.hasExited) {
                throw new IllegalThreadStateException("process hasn\'t exited");
            }
            return this.exitcode;
        }
    }

    private native int forkAndExec(byte[] arg1, byte[] arg2, int arg3, byte[] arg4, int arg5, byte[] arg6, int[] arg7, boolean arg8) throws IOException {
    }

    @Override
    public InputStream getErrorStream() {
        return this.stderr;
    }

    private static FileInputStream getFileInputStream(int fd) {
        Field field0;
        FileInputStream fileInputStream0 = new FileInputStream(UNIXProcess.newFileDescriptor(fd));
        try {
            try {
                field0 = FileInputStream.class.getDeclaredField("isFdOwner");
            }
            catch(NoSuchFieldException unused_ex) {
                field0 = FileInputStream.class.getDeclaredField("shouldClose");
            }
            field0.setAccessible(true);
            field0.set(fileInputStream0, Boolean.TRUE);
        }
        catch(Throwable e) {
            Log.d("Failed set isFdOwner", e);
        }
        return fileInputStream0;
    }

    private static FileOutputStream getFileOutputStream(int fd) {
        Field field0;
        FileOutputStream fileOutputStream0 = new FileOutputStream(UNIXProcess.newFileDescriptor(fd));
        try {
            try {
                field0 = FileOutputStream.class.getDeclaredField("isFdOwner");
            }
            catch(NoSuchFieldException unused_ex) {
                field0 = FileOutputStream.class.getDeclaredField("shouldClose");
            }
            field0.setAccessible(true);
            field0.set(fileOutputStream0, Boolean.TRUE);
        }
        catch(Throwable e) {
            Log.d("Failed set isFdOwner", e);
        }
        return fileOutputStream0;
    }

    @Override
    public InputStream getInputStream() {
        return this.stdout;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.stdin;
    }

    private static native void initIDs() {
    }

    void initStreams(int[] fds) throws IOException {
        NullOutputStream uNIXProcess$NullOutputStream0 = fds[0] == -1 ? NullOutputStream.INSTANCE : new ProcessPipeOutputStream(fds[0]);
        this.stdin = uNIXProcess$NullOutputStream0;
        NullInputStream uNIXProcess$NullInputStream0 = fds[1] == -1 ? NullInputStream.INSTANCE : new ProcessPipeInputStream(fds[1]);
        this.stdout = uNIXProcess$NullInputStream0;
        NullInputStream uNIXProcess$NullInputStream1 = fds[2] == -1 ? NullInputStream.INSTANCE : new ProcessPipeInputStream(fds[2]);
        this.stderr = uNIXProcess$NullInputStream1;
        android.lang.UNIXProcess.3 uNIXProcess$30 = new Runnable() {
            @Override
            public void run() {
                int v = UNIXProcess.this.pid;
                int v1 = UNIXProcess.this.waitForProcessExit(v);
                UNIXProcess.this.processExited(v1);
            }
        };
        UNIXProcess.processReaperExecutor.execute(uNIXProcess$30);
    }

    static FileDescriptor newFileDescriptor(int fd) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        try {
            Field field0 = FileDescriptor.class.getDeclaredField("descriptor");
            field0.setAccessible(true);
            field0.set(fileDescriptor, fd);
            return fileDescriptor;
        }
        catch(Throwable e) {
            throw new RuntimeException("Failed set fd", e);
        }
    }

    void processExited(int exitcode) {
        synchronized(this) {
            this.exitcode = exitcode;
            this.hasExited = true;
            this.notifyAll();
        }
        if(this.stdout instanceof ProcessPipeInputStream) {
            ((ProcessPipeInputStream)this.stdout).processExited();
        }
        if(this.stderr instanceof ProcessPipeInputStream) {
            ((ProcessPipeInputStream)this.stderr).processExited();
        }
        if(this.stdin instanceof ProcessPipeOutputStream) {
            ((ProcessPipeOutputStream)this.stdin).processExited();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Process[pid=");
        sb.append(this.pid);
        if(this.hasExited) {
            sb.append(", hasExited=true, exitcode=");
            sb.append(this.exitcode);
            sb.append(']');
            return sb.toString();
        }
        sb.append(", hasExited=false]");
        return sb.toString();
    }

    @Override
    public int waitFor() throws InterruptedException {
        synchronized(this) {
            while(!this.hasExited) {
                this.wait();
            }
            return this.exitcode;
        }
    }

    private native int waitForProcessExit(int arg1) {
    }
}

