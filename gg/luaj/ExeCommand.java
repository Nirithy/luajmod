package luaj;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExeCommand {
    private boolean bRunning;
    private boolean bSynchronous;
    private BufferedReader errorResult;
    ReadWriteLock lock;
    private DataOutputStream os;
    private Process process;
    private StringBuffer result;
    private BufferedReader successResult;

    public ExeCommand() {
        this.bRunning = false;
        this.lock = new ReentrantReadWriteLock();
        this.result = new StringBuffer();
        this.bSynchronous = true;
    }

    public ExeCommand(boolean synchronous) {
        this.bRunning = false;
        this.lock = new ReentrantReadWriteLock();
        this.result = new StringBuffer();
        this.bSynchronous = synchronous;
    }

    public String getResult() {
        Lock lock0 = this.lock.readLock();
        lock0.lock();
        try {
            Log.i("auto", "getResult");
            return new String(this.result);
        }
        finally {
            lock0.unlock();
        }
    }

    public boolean isRunning() {
        return this.bRunning;
    }

    public ExeCommand run(String command, int maxTime) {
        Log.i("auto", "run command:" + command + ",maxtime:" + maxTime);
        if(command == null || command.length() == 0) {
            return this;
        }
        else {
            try {
                this.process = Runtime.getRuntime().exec("sh");
            }
            catch(Exception unused_ex) {
                return this;
            }
            this.bRunning = true;
            this.successResult = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
            this.errorResult = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
            this.os = new DataOutputStream(this.process.getOutputStream());
            try {
                this.os.write(command.getBytes());
                this.os.writeBytes("\n");
                this.os.flush();
                this.os.writeBytes("exit\n");
                this.os.flush();
                this.os.close();
                if(maxTime > 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(maxTime);
                            }
                            catch(Exception unused_ex) {
                            }
                            try {
                                Log.i("auto", "exitValue Stream over" + ExeCommand.this.process.exitValue());
                            }
                            catch(IllegalThreadStateException unused_ex) {
                                Log.i("auto", "take maxTime,forced to destroy process");
                                ExeCommand.this.process.destroy();
                            }
                        }
                    }).start();
                }
                Thread thread0 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Lock lock0 = ExeCommand.this.lock.writeLock();
                        try {
                            try {
                                while(true) {
                                    String s = ExeCommand.this.successResult.readLine();
                                    if(s == null) {
                                        goto label_22;
                                    }
                                    lock0.lock();
                                    ExeCommand.this.result.append(s + "\n");
                                    lock0.unlock();
                                }
                            }
                            catch(Exception e) {
                            }
                            Log.i("auto", "read InputStream exception:" + e.toString());
                        }
                        catch(Throwable throwable0) {
                            goto label_16;
                        }
                        try {
                            ExeCommand.this.successResult.close();
                            Log.i("auto", "read InputStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "close InputStream exception:" + e.toString());
                        }
                        return;
                        try {
                        label_16:
                            ExeCommand.this.successResult.close();
                            Log.i("auto", "read InputStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "close InputStream exception:" + e.toString());
                        }
                        throw throwable0;
                        try {
                        label_22:
                            ExeCommand.this.successResult.close();
                            Log.i("auto", "read InputStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "close InputStream exception:" + e.toString());
                        }
                    }
                });
                thread0.start();
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Lock lock0 = ExeCommand.this.lock.writeLock();
                        try {
                            try {
                                while(true) {
                                    String s = ExeCommand.this.errorResult.readLine();
                                    if(s == null) {
                                        goto label_22;
                                    }
                                    lock0.lock();
                                    ExeCommand.this.result.append(s + "\n");
                                    lock0.unlock();
                                }
                            }
                            catch(Exception e) {
                            }
                            Log.i("auto", "read ErrorStream exception:" + e.toString());
                        }
                        catch(Throwable throwable0) {
                            goto label_16;
                        }
                        try {
                            ExeCommand.this.errorResult.close();
                            Log.i("auto", "read ErrorStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "read ErrorStream exception:" + e.toString());
                        }
                        return;
                        try {
                        label_16:
                            ExeCommand.this.errorResult.close();
                            Log.i("auto", "read ErrorStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "read ErrorStream exception:" + e.toString());
                        }
                        throw throwable0;
                        try {
                        label_22:
                            ExeCommand.this.errorResult.close();
                            Log.i("auto", "read ErrorStream over");
                        }
                        catch(Exception e) {
                            Log.i("auto", "read ErrorStream exception:" + e.toString());
                        }
                    }
                });
                thread1.start();
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            thread0.join();
                            thread1.join();
                            ExeCommand.this.process.waitFor();
                        }
                        catch(Exception unused_ex) {
                        }
                        finally {
                            ExeCommand.this.bRunning = false;
                            Log.i("auto", "run command process end");
                        }
                    }
                });
                thread2.start();
                if(this.bSynchronous) {
                    Log.i("auto", "run is go to end");
                    thread2.join();
                    Log.i("auto", "run is end");
                    return this;
                }
                return this;
            }
            catch(Exception e) {
            }
        }
        Log.i("auto", "run command process exception:" + e.toString());
        return this;
    }
}

