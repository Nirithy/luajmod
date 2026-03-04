package android.ext;

import android.util.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UsageStats {
    private static final byte NULL;
    public static boolean callInit;

    static {
        UsageStats.callInit = false;
        TrustManager[] arr_trustManager = {new X509TrustManager() {
            @Override  // javax.net.ssl.X509TrustManager
            public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            @Override  // javax.net.ssl.X509TrustManager
            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            @Override  // javax.net.ssl.X509TrustManager
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        android.ext.UsageStats.2 trustAllHostnames = new HostnameVerifier() {
            @Override  // javax.net.ssl.HostnameVerifier
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sSLContext0 = SSLContext.getInstance("SSL");
            sSLContext0.init(null, arr_trustManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext0.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        }
        catch(GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public byte[] b(int i) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
    }

    public byte[] b(String data) {
        byte[] arr_b = data.getBytes();
        return ByteBuffer.allocate(arr_b.length + 1).put(arr_b).put(0).array();
    }

    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream(data.length);
        GZIPOutputStream gos = new GZIPOutputStream(byteArrayOutputStream0);
        gos.write(data);
        gos.close();
        byte[] arr_b1 = byteArrayOutputStream0.toByteArray();
        byteArrayOutputStream0.close();
        return Base64.encode(arr_b1, 0);
    }

    static boolean send(byte[] data) {
        try {
            URLConnection uRLConnection0 = new URL("http://gameguardian.net/GG_logs/usage.php").openConnection();
            uRLConnection0.setDoOutput(true);
            OutputStream outputStream0 = uRLConnection0.getOutputStream();
            byte[] arr_b1 = UsageStats.compress(data);
            outputStream0.write(arr_b1);
            outputStream0.close();
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(uRLConnection0.getInputStream()));
            StringBuilder total = new StringBuilder();
            while(true) {
                String s = bufferedReader0.readLine();
                if(s == null) {
                    boolean z = "OK".equals(total.toString());
                    Log.d(("UsageStats compress from " + data.length + " to " + arr_b1.length + ", sended: " + z));
                    return z;
                }
                total.append(s);
            }
        }
        catch(Exception e) {
            Log.d("UsageStats send fail", e);
            return false;
        }
    }

    public static boolean sendLog(String log) {
        return UsageStats.sendLog(log, false);
    }

    public static boolean sendLog(String log, boolean blocking) {
        if(blocking) {
            Future future0 = Executors.newSingleThreadExecutor().submit(new Callable() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(UsageStats.send(log.getBytes()));
                }

                @Override
                public Object call() throws Exception {
                    return this.call();
                }
            });
            try {
                return ((Boolean)future0.get()).booleanValue();
            }
            catch(Throwable e) {
                Log.w("sendLog", e);
                return false;
            }
        }
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                synchronized(this) {
                    UsageStats.send(log.getBytes());
                }
            }
        }, "sendLog").start();
        return true;
    }
}

