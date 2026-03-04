package android.ext;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ZipSignature {
    byte[] afterAlgorithmIdBytes;
    byte[] algorithmIdBytes;
    byte[] beforeAlgorithmIdBytes;
    Cipher cipher;
    MessageDigest md;

    public ZipSignature() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException {
        this.beforeAlgorithmIdBytes = new byte[]{0x30, 33};
        this.algorithmIdBytes = new byte[]{0x30, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0};
        this.afterAlgorithmIdBytes = new byte[]{4, 20};
        this.md = MessageDigest.getInstance("SHA1");
        this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

    public void initSign(PrivateKey privateKey) throws InvalidKeyException {
        this.cipher.init(1, privateKey);
    }

    public byte[] sign() throws BadPaddingException, IllegalBlockSizeException {
        this.cipher.update(this.beforeAlgorithmIdBytes);
        this.cipher.update(this.algorithmIdBytes);
        this.cipher.update(this.afterAlgorithmIdBytes);
        this.cipher.update(this.md.digest());
        return this.cipher.doFinal();
    }

    public void update(byte[] data) {
        this.md.update(data);
    }

    public void update(byte[] data, int offset, int count) {
        this.md.update(data, offset, count);
    }
}

