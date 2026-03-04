package android.ext;

import java.io.FileInputStream;

public class Cat {
    public static void main(String[] args) throws Exception {
        byte[] buf = new byte[0x2000];
        FileInputStream is = new FileInputStream(args[0]);
        int v;
        while((v = is.read(buf)) >= 0) {
            System.out.write(buf, 0, v);
        }
        is.close();
    }
}

