package android.ext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferReader {
    private final ByteBuffer buffer;
    private final byte[] data;

    public BufferReader(byte[] data) {
        this.data = data;
        this.buffer = ByteBuffer.wrap(data);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int getPosition() {
        return this.buffer.position();
    }

    public byte getSeq() {
        int v = this.buffer.position();
        this.buffer.position(1);
        byte ret = -1;
        try {
            ret = this.readByte();
        }
        catch(IOException e) {
            Log.e("getSeq fail", e);
        }
        this.buffer.position(v);
        return ret;
    }

    public void read(byte[] arr) throws IOException {
        this.buffer.get(arr);
    }

    public void read(byte[] arr, int dstOffset, int byteCount) throws IOException {
        this.buffer.get(arr, dstOffset, byteCount);
    }

    public byte readByte() throws IOException {
        return this.buffer.get();
    }

    public int readInt() throws IOException {
        return this.buffer.getInt();
    }

    // 去混淆评级： 低(20)
    public long readLong() throws IOException {
        return InOut.x64 ? this.buffer.getLong() : ((long)this.readInt()) & 0xFFFFFFFFL;
    }

    public long readLongLong() throws IOException {
        return this.buffer.getLong();
    }

    public String readString(int count, String old) throws IOException {
        if(count == 0) {
            return "";
        }
        int v1 = this.buffer.position();
        String ret = null;
        byte[] data = this.data;
        if(old != null && old.length() == count) {
            boolean same = true;
            for(int i = 0; i < count; ++i) {
                if(old.charAt(i) != data[v1 + i]) {
                    same = false;
                    break;
                }
            }
            if(same) {
                ret = old;
            }
        }
        if(ret == null) {
            ret = new String(data, v1, count);
        }
        this.buffer.position(v1 + count);
        return ret;
    }

    public void reset() {
        this.buffer.position(2);
    }

    public int size() {
        return this.buffer.capacity();
    }

    public void skip(int count) {
        this.buffer.position(this.buffer.position() + count);
    }
}

