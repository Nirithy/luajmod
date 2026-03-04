package android.ext;

public class DaemonThread extends Thread {
    public DaemonThread(Runnable runnable, String threadName) {
        super(runnable, threadName);
        this.setDaemon(true);
        ExceptionHandler.install(this);
    }

    public DaemonThread(String threadName) {
        super(threadName);
        this.setDaemon(true);
        ExceptionHandler.install(this);
    }
}

