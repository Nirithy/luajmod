package luaj;

import android.ext.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LuaError extends RuntimeException {
    public static class Internal extends LuaError {
        public Internal(Throwable cause) {
            super(cause);
        }
    }

    public static class StackOverflow extends LuaError {
        public StackOverflow(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final int MAX_STRING_LEN = 0x7C;
    protected Throwable cause;
    protected String detailMessage;
    protected String file;
    protected String fileline;
    protected int level;
    protected int line;
    LuaValue object;
    public String prefix;
    private static final long serialVersionUID = 1L;
    protected String traceback;

    public LuaError(String message) {
        super(message);
        this.prefix = null;
        this.level = 1;
    }

    public LuaError(String message, int level) {
        super(message);
        this.prefix = null;
        this.level = level;
    }

    public LuaError(String file, int line, String message) {
        super(message);
        this.prefix = null;
        this.level = 1;
        this.file = file;
        this.line = line;
        this.setFileLine();
    }

    public LuaError(String message, Throwable cause) {
        super(message);
        this.prefix = null;
        this.cause = cause;
        this.level = 1;
    }

    public LuaError(Throwable cause) {
        super("vm error: " + cause);
        this.prefix = null;
        this.cause = cause;
        this.level = 1;
    }

    public LuaError(LuaValue message_object) {
        super(message_object.tojstring());
        this.prefix = null;
        this.object = message_object;
        this.level = 1;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    private String getFileLine() {
        if(this.file == null || this.line < 0) {
            return String.valueOf(this.fileline) + '\n';
        }
        try {
            String file = this.file.length() <= 0 || this.file.charAt(0) != 0x40 ? this.file : this.file.substring(1);
            File f = new File(file);
            BufferedInputStream bufferedInputStream0 = f.exists() ? new BufferedInputStream(new FileInputStream(f)) : new ByteArrayInputStream(file.getBytes());
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(bufferedInputStream0));
            int i;
            for(i = 1; i < this.line && bufferedReader0.readLine() != null; ++i) {
            }
            if(i == this.line) {
                String srcLine = this.limitStr(bufferedReader0.readLine());
                return String.valueOf(this.fileline) + '\n' + '`' + srcLine + '`';
            }
            return String.valueOf(this.fileline) + '\n' + '`' + "failed read line" + '`';
        }
        catch(Throwable e) {
            Log.e("Failed read source", e);
            return String.valueOf(this.fileline) + '\n' + '`' + "failed read source" + '`';
        }
    }

    @Override
    public String getMessage() {
        if(this.traceback != null) {
            return this.prefix != null && !this.traceback.startsWith(this.prefix) ? this.prefix + this.traceback : this.traceback;
        }
        String m = this.detailMessage == null ? super.getMessage() : this.detailMessage;
        if(m == null) {
            return null;
        }
        if(this.fileline != null) {
            return this.prefix != null && !m.startsWith(this.prefix) ? this.prefix + this.getFileLine() + '\n' + m : "" + this.getFileLine() + '\n' + m;
        }
        return this.prefix != null && !m.startsWith(this.prefix) ? this.prefix + m : m;
    }

    public LuaValue getMessageObject() {
        if(this.object != null) {
            return this.object;
        }
        String s = this.getMessage();
        return s != null ? LuaValue.valueOf(s) : null;
    }

    private String limitStr(String ret) {
        return ret == null || ret.length() <= 0x7C ? ret : ret.substring(0, 0x7C) + "...";
    }

    public static void main(String[] args) throws Throwable {
    }

    public void setFileLine() {
        this.fileline = this.limitStr(this.file) + ":" + this.line;
    }
}

