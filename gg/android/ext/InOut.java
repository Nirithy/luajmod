package android.ext;

import android.os.Build.VERSION;
import android.os.Build;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;

public class InOut {
    static class DecodeInputStream extends InputStream {
        private static final int EMPTY = -2017;
        private final InputStream base;
        private static final int last = 0x70;
        private int prev;

        public DecodeInputStream(InputStream base) {
            this.base = base;
            this.prev = -2017;
        }

        @Override
        public int read() throws IOException {
            int prev = this.prev;
            if(prev != -2017) {
                this.prev = -2017;
                return prev;
            }
            InputStream base = this.base;
            int first = base.read();
            if(first >= 97 && first <= 0x70) {
                int v2 = base.read();
                if(v2 >= 97 && v2 <= 0x70) {
                    return first + v2 * 16 - 0x671;
                }
                this.prev = v2;
                return first;
            }
            return first;
        }

        @Override
        public int read(byte[] buffer, int offset, int size) throws IOException {
            int data;
            int read = offset;
            int prev = this.prev;
            InputStream base = this.base;
            while(read < offset + size) {
                while(true) {
                    if(prev != -2017) {
                        data = prev;
                        prev = -2017;
                        if(data == -1) {
                            goto label_8;
                        }
                        break;
                    }
                label_8:
                    int v5 = base.read();
                    if(v5 >= 97 && v5 <= 0x70) {
                        int v6 = base.read();
                        if(v6 < 97 || v6 > 0x70) {
                            prev = v6;
                            data = v5;
                        }
                        else {
                            data = v5 + v6 * 16 - 0x671;
                        }
                        break;
                    }
                    if(v5 != -1) {
                        data = v5;
                        break;
                    }
                }
                buffer[read] = (byte)data;
                ++read;
            }
            this.prev = prev;
            return 0;
        }
    }

    static class EncodeOutputStream extends OutputStream {
        private final OutputStream base;

        public EncodeOutputStream(OutputStream base) {
            this.base = base;
        }

        @Override
        public void flush() throws IOException {
            this.base.flush();
        }

        private byte[] getBuf(byte[] b, int off, int len) {
            byte[] send = new byte[len * 2 + 4];
            send[0] = 59;
            send[1] = 59;
            for(int i = 0; i < len; ++i) {
                int ch = b[off + i];
                send[i * 2 + 2] = (byte)this.getChar(ch & 15);
                send[i * 2 + 3] = (byte)this.getChar((ch & 0xF0) >> 4);
            }
            send[len * 2 + 2] = 10;
            send[len * 2 + 3] = 10;
            return send;
        }

        private int getChar(int hex) {
            return hex < 0 || hex > 15 ? hex : hex + 97;
        }

        @Override
        public void write(int b) throws IOException {
            this.base.write(this.getChar(b & 15));
            this.base.write(this.getChar((b & 0xF0) >> 4));
        }

        @Override
        public void write(byte[] b) throws IOException {
            byte[] arr_b1 = this.getBuf(b, 0, b.length);
            this.base.write(arr_b1);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] arr_b1 = this.getBuf(b, off, len);
            this.base.write(arr_b1);
        }
    }

    private static final int BUFFER_LEN = 0xA00000;
    private static final int CURRENT_INC = 0x400;
    private static final int MAX_SIZE_WAIT = 40;
    public static final int QE_ALIGN = 4;
    public static volatile int byteOrderMask;
    private CRC32 crc;
    private byte[] current;
    private int currentUsed;
    static boolean fifo;
    private InputStream in;
    public static volatile int longSize;
    private OutputStream out;
    private ByteBuffer readBuffer;
    private byte[] readData;
    private volatile boolean started;
    private boolean truncate;
    private final ArrayList wait;
    private ByteBuffer writeBuffer;
    private byte[] writeData;
    public static volatile boolean x64;

    static {
        InOut.byteOrderMask = 15;
        InOut.setX64(Build.VERSION.SDK_INT >= 21 && (Build.SUPPORTED_64_BIT_ABIS != null && Build.SUPPORTED_64_BIT_ABIS.length > 0));
        InOut.fifo = false;
    }

    public InOut() {
        this.crc = null;
        this.in = null;
        this.out = null;
        this.readData = new byte[4];
        this.readBuffer = ByteBuffer.wrap(this.readData);
        this.readBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.writeData = new byte[8];
        this.writeBuffer = ByteBuffer.wrap(this.writeData);
        this.writeBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.current = new byte[0x400];
        this.wait = new ArrayList();
        this.started = false;
    }

    static String bytesToHex(byte[] bytes, int start, int length) {
        if(bytes.length - 1 < start) {
            Log.w(("bytesToHex failed: " + bytes.length + ' ' + start + ' ' + length));
            return "";
        }
        if(bytes.length < start + length) {
            Log.w(("bytesToHex fix len: " + bytes.length + ' ' + start + ' ' + length));
            length = bytes.length - start;
        }
        char[] hexChars = new char[length * 3];
        char[] hexArr = HexText.hexArray;
        for(int j = 0; j < length; ++j) {
            int v = bytes[start + j] & 0xFF;
            hexChars[j * 3] = hexArr[v >>> 4];
            hexChars[j * 3 + 1] = hexArr[v & 15];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    private void checkSize(int size, boolean truncate) {
        byte[] current = this.current;
        int currentUsed = this.currentUsed;
        if(currentUsed + size > current.length || truncate) {
            byte[] curr = new byte[currentUsed + size + 0x400];
            if(currentUsed > 0) {
                System.arraycopy(current, 0, curr, 0, currentUsed);
            }
            this.current = curr;
        }
    }

    public void clear() {
        this.currentUsed = 0;
    }

    public long crc() {
        if(this.currentUsed < 2) {
            return 0L;
        }
        CRC32 crc = this.crc;
        if(crc == null) {
            crc = new CRC32();
            this.crc = crc;
        }
        else {
            crc.reset();
        }
        crc.update(this.current, 2, this.currentUsed);
        return crc.getValue();
    }

    private void debug(String name, byte[] bytes, int len) {
        Log.d((name + ": [" + len + "] " + InOut.bytesToHex(bytes, 0, len)));
    }

    public static String dump(byte[] data, int start, int length) {
        if(data.length - 1 < start) {
            Log.w(("dump failed: " + data.length + ' ' + start + ' ' + length));
            return "";
        }
        if(data.length < start + length) {
            Log.w(("dump fix len: " + data.length + ' ' + start + ' ' + length));
            length = data.length - start;
        }
        int index = start;
        StringBuilder out = new StringBuilder();
        while(index < length) {
            int count = length - index <= 0x20 ? length - index : 0x20;
            out.append(InOut.bytesToHex(data, start + index, count));
            out.append('\'');
            out.append(new String(data, start + index, count));
            out.append("\'\n");
            index += count;
        }
        return out.toString();
    }

    public boolean isStarted() {
        return this.started;
    }

    private byte readByte() throws IOException {
        int v;
        do {
            v = this.in.read();
        }
        while(v == -1);
        return (byte)v;
    }

    private int readInt() throws IOException {
        for(int read = 0; read < 4; read += v1) {
            int v1 = this.in.read(this.readData, read, 4 - read);
            if(v1 <= 0) {
                break;
            }
        }
        return this.readBuffer.getInt(0);
    }

    public byte[] readPacket() throws IOException {
        byte[] skip = null;
        int skipped = 0;
        byte b;
        while((b = this.readByte()) != 2) {
            if(skip != null || b != 10 && b != 59) {
                if(skip == null) {
                    skip = new byte[0x1000];
                }
                if(skipped < skip.length) {
                    skip[skipped] = b;
                }
                ++skipped;
                if(skipped >= skip.length || skipped % 10 == 0) {
                    Log.d(("Bad input from daemon: " + ((int)b) + '\n' + InOut.dump(skip, 0, skipped)));
                }
            }
        }
        if(skip != null) {
            Log.d(("Bad input from daemon 2: " + skipped + '\n' + InOut.dump(skip, 0, skipped)));
        }
        int v1 = this.readInt();
        if(v1 > 0xA00000 || v1 < 2) {
            Log.d(("Bad len from daemon: " + v1 + ' ' + InOut.dump(this.readData, 0, 4)));
            return null;
        }
        byte[] buffer = new byte[v1];
        int read;
        for(read = 0; read < v1; read += v3) {
            int v3 = this.in.read(buffer, read, v1 - read);
            if(v3 <= 0) {
                if(read != 0) {
                    break;
                }
                read = -1;
                break;
            }
        }
        if(buffer[v1 - 1] != 3) {
            Log.d(("Bad end from daemon: " + ((int)buffer[v1 - 1]) + "; " + ((int)buffer[0]) + "; " + (v1 - 1) + "; " + read));
            return null;
        }
        return buffer;
    }

    public void send() throws IOException {
        synchronized(this) {
            if(this.started) {
                this.sendWait();
                this.out.write(this.current, 0, this.currentUsed);
                this.out.flush();
            }
            else if(this.currentUsed > 0) {
                ArrayList arrayList0 = this.wait;
                synchronized(arrayList0) {
                    byte[] arr_b = Arrays.copyOf(this.current, this.currentUsed);
                    this.wait.add(arr_b);
                    if(this.wait.size() > 40) {
                        byte[] arr_b1 = (byte[])this.wait.remove(0);
                    }
                }
            }
            this.currentUsed = 0;
        }
    }

    public void sendWait() throws IOException {
        ArrayList wait = this.wait;
        synchronized(wait) {
            if(wait.size() > 0) {
                OutputStream out = this.out;
                for(Object object0: wait) {
                    out.write(((byte[])object0));
                    out.flush();
                }
                wait.clear();
                wait.trimToSize();
            }
        }
    }

    public void setStarted(boolean value) {
        this.started = value;
    }

    public void setStreams(InputStream in, OutputStream out) {
        DecodeInputStream inOut$DecodeInputStream0;
        BufferedInputStream bufferedInputStream0 = new BufferedInputStream(in, 0x10000);
        if(InOut.fifo) {
            inOut$DecodeInputStream0 = bufferedInputStream0;
        }
        else {
            EncodeOutputStream out = new EncodeOutputStream(out);
            inOut$DecodeInputStream0 = new DecodeInputStream(bufferedInputStream0);
            out = out;
        }
        this.in = inOut$DecodeInputStream0;
        this.out = out;
    }

    static void setX64(boolean value) {
        InOut.x64 = value;
        InOut.longSize = value ? 8 : 4;
    }

    public void startMessage(byte seq, byte cmd, int capacity) {
        this.checkSize(capacity + 2, this.truncate);
        this.truncate = false;
        byte[] current = this.current;
        current[0] = cmd;
        current[1] = seq;
        this.currentUsed = 2;
    }

    public void truncate() {
        if(this.current.length > 0x10000) {
            this.truncate = true;
        }
    }

    public void writeByte(byte value) {
        this.checkSize(1, false);
        int v = this.currentUsed;
        this.currentUsed = v + 1;
        this.current[v] = value;
    }

    public void writeBytes(byte[] buffer, int size) {
        this.checkSize(size, false);
        System.arraycopy(buffer, 0, this.current, this.currentUsed, size);
        this.currentUsed += size;
    }

    public void writeInt(int value) {
        this.writeBuffer.putInt(0, InOut.byteOrderMask ^ value);
        this.writeBytes(this.writeData, 4);
    }

    public void writeLong(long value) {
        if(InOut.x64) {
            this.writeBuffer.putLong(0, value);
            this.writeBytes(this.writeData, 8);
            return;
        }
        this.writeInt(((int)value));
    }

    public void writeLongLong(long value) {
        this.writeBuffer.putLong(0, value);
        this.writeBytes(this.writeData, 8);
    }
}

