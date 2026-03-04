package android.ext;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Hash {
    public static final String hash(Object o, String alg) {
        try {
            MessageDigest messageDigest0 = MessageDigest.getInstance(alg);
            if(o instanceof File) {
                FileInputStream fis = new FileInputStream(((File)o));
                byte[] dataBytes = new byte[0x400];
                while(true) {
                    int nread = fis.read(dataBytes);
                    if(nread == -1) {
                        fis.close();
                        break;
                    }
                    messageDigest0.update(dataBytes, 0, nread);
                }
            }
            else {
                messageDigest0.update(o.toString().getBytes());
            }
            return Hash.toString(messageDigest0);
        }
        catch(Throwable e) {
            Log.e("hash fail", e);
            return e.toString();
        }
    }

    public static final String toString(MessageDigest digest) {
        byte[] arr_b = digest.digest();
        byte[] out = new byte[8];
        System.arraycopy(arr_b, 0, out, 0, 4);
        System.arraycopy(arr_b, arr_b.length - 4, out, 4, 4);
        return Tools.byteArrayToHexString(out);
    }
}

