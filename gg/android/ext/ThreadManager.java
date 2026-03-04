package android.ext;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class ThreadManager {
    static class HandlerThreadEx extends HandlerThread {
        public HandlerThreadEx(String name) {
            super(name);
            ExceptionHandler.install(this);
        }
    }

    private static final int THREAD_COUNT = 4;
    private static final int THREAD_LOG = 2;
    private static final int THREAD_MAIN = 1;
    private static final int THREAD_UI = 0;
    private static final int THREAD_WRITE = 3;
    private static final Handler[] handlers;
    private static final Looper[] loopers;

    static {
        Looper[] loopers_ = new Looper[4];
        Handler[] handlers_ = new Handler[4];
        loopers_[0] = Looper.getMainLooper();
        handlers_[0] = new Handler(loopers_[0]);
        HandlerThreadEx threadManager$HandlerThreadEx0 = new HandlerThreadEx("Main");
        threadManager$HandlerThreadEx0.start();
        loopers_[1] = threadManager$HandlerThreadEx0.getLooper();
        handlers_[1] = new Handler(loopers_[1]);
        HandlerThreadEx threadManager$HandlerThreadEx1 = new HandlerThreadEx("Log");
        threadManager$HandlerThreadEx1.start();
        loopers_[2] = threadManager$HandlerThreadEx1.getLooper();
        handlers_[2] = new Handler(loopers_[2]);
        HandlerThreadEx threadManager$HandlerThreadEx2 = new HandlerThreadEx("Write");
        threadManager$HandlerThreadEx2.start();
        loopers_[3] = threadManager$HandlerThreadEx2.getLooper();
        handlers_[3] = new Handler(loopers_[3]);
        ThreadManager.loopers = loopers_;
        ThreadManager.handlers = handlers_;
    }

    @TargetApi(18)
    public static void exit() {
        for(int i = 0; i < ThreadManager.loopers.length; ++i) {
            if(i != 0) {
                Looper looper = ThreadManager.loopers[i];
                try {
                    looper.quitSafely();
                }
                catch(NoSuchMethodError unused_ex) {
                    looper.quit();
                }
            }
        }
    }

    private static Handler getHandler(int thread) {
        return ThreadManager.handlers[thread];
    }

    public static Handler getHandlerLogThread() {
        return ThreadManager.getHandler(2);
    }

    public static Handler getHandlerMainThread() {
        return ThreadManager.getHandler(1);
    }

    public static Handler getHandlerUiThread() {
        return ThreadManager.getHandler(0);
    }

    public static Handler getHandlerWriteThread() {
        return ThreadManager.getHandler(3);
    }

    private static Looper getLooper(int thread) {
        return ThreadManager.loopers[thread];
    }

    public static Looper getLooperLogThread() {
        return ThreadManager.getLooper(2);
    }

    public static Looper getLooperMainThread() {
        return ThreadManager.getLooper(1);
    }

    public static Looper getLooperUiThread() {
        return ThreadManager.getLooper(0);
    }

    public static Looper getLooperWriteThread() {
        return ThreadManager.getLooper(3);
    }

    public static boolean isInLogThread() {
        return ThreadManager.isInThread(2);
    }

    public static boolean isInMainThread() {
        return ThreadManager.isInThread(1);
    }

    private static boolean isInThread(int thread) {
        return Looper.myLooper() == ThreadManager.loopers[thread];
    }

    public static boolean isInUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isInWriteThread() {
        return ThreadManager.isInThread(3);
    }

    private static void run(int thread, Runnable task, boolean post) {
        if(ThreadManager.loopers == null) {
            LogWrapper.e("AndroidService", "Bad implementation", new NullPointerException("loopers"));
            return;
        }
        if(ThreadManager.loopers[thread] == null) {
            LogWrapper.e("AndroidService", "Bad implementation", new NullPointerException("loopers[" + thread + ']'));
            return;
        }
        if(Looper.myLooper() == ThreadManager.loopers[thread] && !post) {
            task.run();
            return;
        }
        ThreadManager.handlers[thread].post(task);
    }

    public static void runOnLogThread(Runnable task) {
        try {
            ThreadManager.run(2, task, false);
        }
        catch(Throwable e) {
            LogWrapper.e("AndroidService", "Bad implementation", e);
        }
    }

    public static void runOnMainThread(Runnable task) {
        ThreadManager.run(1, task, false);
    }

    public static void runOnUiThread(Runnable task) {
        ThreadManager.run(0, task, false);
    }

    public static void runOnWriteThread(Runnable task) {
        ThreadManager.run(3, task, false);
    }
}

