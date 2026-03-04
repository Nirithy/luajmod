package android.lang;

import android.ext.DaemonLoader;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class ProcessBuilder {
    static final boolean $assertionsDisabled;
    private String[] command;
    private File directory;
    private Map environment;
    private static volatile boolean loaded;

    static {
        ProcessBuilder.$assertionsDisabled = !ProcessBuilder.class.desiredAssertionStatus();
        ProcessBuilder.loaded = false;
    }

    public ProcessBuilder(String[] command) {
        this.command = command;
    }

    public ProcessBuilder directory(File directory) {
        this.directory = directory;
        return this;
    }

    ProcessBuilder environment(String[] envp) {
        if(!ProcessBuilder.$assertionsDisabled && this.environment != null) {
            throw new AssertionError();
        }
        if(envp != null) {
            this.environment = ProcessEnvironment.emptyEnvironment(envp.length);
            if(!ProcessBuilder.$assertionsDisabled && this.environment == null) {
                throw new AssertionError();
            }
            for(int v = 0; v < envp.length; ++v) {
                String envstring = envp[v];
                if(envstring.indexOf(0) != -1) {
                    envstring = envstring.replaceFirst("\u0000.*", "");
                }
                int v1 = envstring.indexOf(61, 0);
                if(v1 != -1) {
                    this.environment.put(envstring.substring(0, v1), envstring.substring(v1 + 1));
                }
            }
        }
        return this;
    }

    public static Process exec(String[] cmdarray, String[] envp, File dir) throws IOException {
        if(!ProcessBuilder.loaded) {
            ProcessBuilder.loaded = true;
            DaemonLoader.initNative();
        }
        return new ProcessBuilder(cmdarray).environment(envp).directory(dir).start();
    }

    public static native boolean loaded() {
    }

    public Process start() throws IOException {
        String[] cmdarray = (String[])this.command.clone();
        for(int v = 0; true; ++v) {
            if(v >= cmdarray.length) {
                String prog = cmdarray[0];
                SecurityManager securityManager0 = System.getSecurityManager();
                if(securityManager0 != null) {
                    securityManager0.checkExec(prog);
                }
                String dir = this.directory == null ? null : this.directory.toString();
                for(int i = 1; true; ++i) {
                    if(i >= cmdarray.length) {
                        try {
                            return ProcessImpl.start(cmdarray, this.environment, dir, false);
                        }
                        catch(IOException | IllegalArgumentException e) {
                            String exceptionInfo = ": " + e.getMessage();
                            IOException cause = e;
                            if(e instanceof IOException && securityManager0 != null) {
                                try {
                                    securityManager0.checkRead(prog);
                                }
                                catch(SecurityException se) {
                                    exceptionInfo = "";
                                    cause = se;
                                }
                            }
                            throw new IOException("Cannot run program \"" + prog + '\"' + (dir == null ? "" : " (in directory \"" + dir + "\")") + exceptionInfo, cause);
                        }
                    }
                    if(cmdarray[i].indexOf(0) >= 0) {
                        throw new IOException("invalid null character in command");
                    }
                }
            }
            if(cmdarray[v] == null) {
                throw new NullPointerException();
            }
        }
    }
}

