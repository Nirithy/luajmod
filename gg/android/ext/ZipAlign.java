package android.ext;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ZipAlign {
    public static void fixFile(File apk) throws IOException {
        int i;
        long writeOffset;
        long readOffset;
        ByteArrayOutputStream byteArrayOutputStream0;
        long maxOffset;
        int v3;
        int v2;
        int v1;
        long length;
        RandomAccessFile randomAccessFile1;
        RandomAccessFile raf;
        boolean fileCorrupted = false;
        try {
            raf = null;
            randomAccessFile1 = new RandomAccessFile(apk, "rw");
        }
        catch(IOException iOException0) {
            goto label_133;
        }
        catch(Throwable throwable0) {
            goto label_147;
        }
        try {
            length = randomAccessFile1.length();
            if(length == 0L) {
                length = apk.length();
            }
            if(length >= 22L) {
                randomAccessFile1.seek(length - 12L);
                v1 = ZipAlign.readShort(randomAccessFile1);
                v2 = ZipAlign.readInt(randomAccessFile1);
                v3 = ZipAlign.readInt(randomAccessFile1);
                if(v1 <= 0 || v2 <= 0 || ((long)v3) <= 0L) {
                    Log.w(("bad values in EOCD: " + v1 + ", " + v2 + ", " + ((long)v3)));
                    goto label_13;
                }
                goto label_18;
            }
            goto label_129;
        }
        catch(IOException iOException0) {
            goto label_132;
        }
        catch(Throwable throwable0) {
            goto label_146;
        }
        try {
        label_13:
            randomAccessFile1.close();
        }
        catch(Throwable e) {
            Log.e("ZipAlign close fail", e);
        }
        return;
        try {
        label_18:
            maxOffset = ((long)v3) + ((long)v2);
            if(length - 22L != maxOffset) {
                Log.w(("Bad EOCD offset: " + (length - 22L) + " != " + maxOffset + " = " + ((long)v3) + " + " + v2));
                goto label_21;
            }
            goto label_26;
        }
        catch(IOException iOException0) {
            goto label_132;
        }
        catch(Throwable throwable0) {
            goto label_146;
        }
        try {
        label_21:
            randomAccessFile1.close();
        }
        catch(Throwable e) {
            Log.e("ZipAlign close fail", e);
        }
        return;
        try {
        label_26:
            byteArrayOutputStream0 = new ByteArrayOutputStream(v2 + 22);
            readOffset = (long)v3;
            writeOffset = (long)v3;
            i = 0;
        }
        catch(Throwable e) {
            try {
                Log.w(("ZipAlign failed alloc: " + (v2 + 22)), e);
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
                randomAccessFile1.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
            return;
        }
        while(true) {
            try {
            label_38:
                if(i >= v1) {
                    if(readOffset == maxOffset) {
                        if(readOffset != writeOffset) {
                            byte[] buf = new byte[22];
                            randomAccessFile1.seek(readOffset);
                            randomAccessFile1.read(buf);
                            int v = (int)(writeOffset - ((long)v3));
                            buf[12] = (byte)(v & 0xFF);
                            buf[13] = (byte)(v >>> 8 & 0xFF);
                            buf[14] = (byte)(v >>> 16 & 0xFF);
                            buf[15] = (byte)(v >>> 24 & 0xFF);
                            byteArrayOutputStream0.write(buf);
                            readOffset += 22L;
                            writeOffset += 22L;
                            if(readOffset == length) {
                                if(writeOffset <= readOffset) {
                                    byte[] arr_b1 = byteArrayOutputStream0.toByteArray();
                                    if(writeOffset == ((long)arr_b1.length) + ((long)v3)) {
                                        randomAccessFile1.seek(((long)v3));
                                        fileCorrupted = true;
                                        randomAccessFile1.write(arr_b1);
                                        randomAccessFile1.setLength(writeOffset);
                                        goto label_79;
                                    }
                                    Log.w(("Fail check buffer length: " + writeOffset + " != " + ((long)v3) + arr_b1.length));
                                    goto label_62;
                                }
                                goto label_67;
                            }
                            goto label_73;
                        }
                        goto label_79;
                    }
                    goto label_84;
                }
                goto label_90;
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
            label_62:
                randomAccessFile1.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
            return;
            try {
            label_67:
                Log.w(("Fail check new length: " + writeOffset + " > " + readOffset));
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
                randomAccessFile1.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
            return;
            try {
            label_73:
                Log.w(("Fail check end: " + readOffset + " != " + length));
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
                randomAccessFile1.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
            return;
            try {
            label_79:
                randomAccessFile1.close();
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
                Log.d(("ZipAlign fix: " + readOffset + " => " + writeOffset + " (" + (readOffset - writeOffset) + ')'));
                return;
            }
            catch(IOException iOException0) {
            }
            catch(Throwable throwable0) {
                goto label_147;
            }
            goto label_133;
            try {
            label_84:
                Log.w(("Not all CD reads: " + readOffset + " != " + maxOffset + " = " + ((long)v3) + " + " + v2));
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
            try {
                randomAccessFile1.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
            return;
            try {
            label_90:
                if(readOffset >= maxOffset) {
                    goto label_123;
                }
                randomAccessFile1.seek(readOffset + 28L);
                int v9 = ZipAlign.readShort(randomAccessFile1);
                int v10 = ZipAlign.readShort(randomAccessFile1);
                if(v9 >= 0 && v10 >= 0) {
                    byte[] buf = new byte[v9 + 46];
                    randomAccessFile1.seek(readOffset);
                    int read = 0;
                    int len = buf.length;
                    int j = 0;
                    while(j < 10 && read < len) {
                        int v14 = randomAccessFile1.read(buf, read, len - read);
                        if(v14 >= 0) {
                            read += v14;
                            ++j;
                        }
                        else {
                            Log.w(("Failed read buf 1: " + i + "; " + v14 + ' ' + read + ' ' + len));
                            if(true) {
                                break;
                            }
                        }
                    }
                    if(read < len) {
                        Log.w(("Failed read buf 2: " + i + "; " + read + ' ' + len));
                    }
                    buf[30] = 0;
                    buf[0x1F] = 0;
                    byteArrayOutputStream0.write(buf);
                    readOffset += (long)(buf.length + v10);
                    writeOffset += (long)buf.length;
                    ++i;
                    goto label_38;
                }
                Log.w(("bad len for " + i + ": " + v9 + ", " + v10));
                break;
            }
            catch(IOException iOException0) {
                goto label_132;
            }
            catch(Throwable throwable0) {
                goto label_146;
            }
        }
        try {
            randomAccessFile1.close();
        }
        catch(Throwable e) {
            Log.e("ZipAlign close fail", e);
        }
        return;
        try {
        label_123:
            Log.w(("Out from CD: " + i + " from " + v1 + "; " + readOffset + " > " + maxOffset + " = " + ((long)v3) + " + " + v2));
        }
        catch(IOException iOException0) {
            goto label_132;
        }
        catch(Throwable throwable0) {
            goto label_146;
        }
        try {
            randomAccessFile1.close();
        }
        catch(Throwable e) {
            Log.e("ZipAlign close fail", e);
        }
        return;
        try {
        label_129:
            Log.w(("ZipAlign small size: " + length + ' ' + apk.getAbsolutePath()));
            goto label_153;
        }
        catch(IOException iOException0) {
        label_132:
            raf = randomAccessFile1;
            try {
            label_133:
                Log.e("ZipAlign fail", iOException0);
                if(fileCorrupted) {
                    throw iOException0;
                }
            }
            catch(Throwable throwable0) {
                goto label_147;
            }
            if(raf != null) {
                try {
                    raf.close();
                }
                catch(Throwable e) {
                    Log.e("ZipAlign close fail", e);
                }
                return;
            }
            return;
        }
        catch(Throwable throwable0) {
        label_146:
            raf = randomAccessFile1;
        }
    label_147:
        if(raf != null) {
            try {
                raf.close();
            }
            catch(Throwable e) {
                Log.e("ZipAlign close fail", e);
            }
        }
        throw throwable0;
        try {
        label_153:
            randomAccessFile1.close();
        }
        catch(Throwable e) {
            Log.e("ZipAlign close fail", e);
        }
    }

    public static int readInt(RandomAccessFile raf) throws IOException {
        int v = raf.read();
        int v1 = raf.read();
        int v2 = raf.read();
        int v3 = raf.read();
        if((v | v1 | v2 | v3) < 0) {
            throw new EOFException();
        }
        return (v3 << 24) + (v2 << 16) + (v1 << 8) + v;
    }

    private static short readShort(RandomAccessFile raf) throws IOException {
        int v = raf.read();
        int v1 = raf.read();
        if((v | v1) < 0) {
            throw new EOFException();
        }
        return (short)((v1 << 8) + v);
    }
}

