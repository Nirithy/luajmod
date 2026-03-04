package android.lang;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public final class ProcessImpl {
    static final boolean $assertionsDisabled;

    static {
        ProcessImpl.$assertionsDisabled = !ProcessImpl.class.desiredAssertionStatus();
    }

    public static int getFD(FileDescriptor fileDescriptor) {
        try {
            Field field0 = FileDescriptor.class.getDeclaredField("descriptor");
            field0.setAccessible(true);
            return (int)(((Integer)field0.get(fileDescriptor)));
        }
        catch(Throwable e) {
            throw new RuntimeException("Failed get fd", e);
        }
    }

    static Process start(String[] cmdarray, Map map0, String dir, boolean redirectErrorStream) throws IOException {
        if(!ProcessImpl.$assertionsDisabled && (cmdarray == null || cmdarray.length <= 0)) {
            throw new AssertionError();
        }
        byte[][] args = new byte[cmdarray.length - 1][];
        int size = args.length;
        for(int i = 0; i < args.length; ++i) {
            args[i] = cmdarray[i + 1].getBytes();
            size += args[i].length;
        }
        byte[] argBlock = new byte[size];
        int i = 0;
        for(int v3 = 0; v3 < args.length; ++v3) {
            byte[] arg = args[v3];
            System.arraycopy(arg, 0, argBlock, i, arg.length);
            i += arg.length + 1;
        }
        int[] envc = new int[1];
        byte[] arr_b2 = ProcessEnvironment.toEnvironmentBlock(map0, envc);
        byte[] arr_b3 = ProcessImpl.toCString(cmdarray[0]);
        int v4 = envc[0];
        byte[] arr_b4 = ProcessImpl.toCString(dir);
        return new UNIXProcess(arr_b3, argBlock, args.length, arr_b2, v4, arr_b4, new int[]{-1, -1, -1}, redirectErrorStream);
    }

    private static byte[] toCString(String s) {
        if(s == null) {
            return null;
        }
        byte[] arr_b = s.getBytes();
        byte[] result = new byte[arr_b.length + 1];
        System.arraycopy(arr_b, 0, result, 0, arr_b.length);
        result[result.length - 1] = 0;
        return result;
    }
}

