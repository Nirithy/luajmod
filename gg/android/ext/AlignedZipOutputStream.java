package android.ext;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class AlignedZipOutputStream extends ZipOutputStream {
    static class CountedOutputStream extends OutputStream {
        static CountedOutputStream lastInstance;
        int length;
        private OutputStream os;

        public CountedOutputStream(OutputStream os) {
            this.length = 0;
            this.os = os;
            CountedOutputStream.lastInstance = this;
        }

        @Override
        public void close() throws IOException {
            this.os.close();
        }

        @Override
        public boolean equals(Object o) {
            return this.os.equals(o);
        }

        @Override
        public void flush() throws IOException {
            this.os.flush();
        }

        @Override
        public int hashCode() {
            return this.os.hashCode();
        }

        @Override
        public String toString() {
            return this.os.toString();
        }

        @Override
        public void write(int oneByte) throws IOException {
            this.os.write(oneByte);
            ++this.length;
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            this.os.write(buffer);
            this.length += buffer.length;
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.os.write(buffer, offset, count);
            this.length += count;
        }
    }

    private static final int ALIGN_BASE = 4;
    private byte[] buf;
    private int bytes;
    private CountedOutputStream cos;
    private final CRC32 crc;
    private ZipEntry current;
    private boolean entryOpen;

    public AlignedZipOutputStream(OutputStream os) {
        super(new CountedOutputStream(new BufferedOutputStream(os, 0x10000)));
        this.entryOpen = false;
        this.current = null;
        this.crc = new CRC32();
        this.bytes = 0;
        this.buf = null;
        this.cos = CountedOutputStream.lastInstance;
        this.setComment("");
    }

    @Override
    public void closeEntry() throws IOException {
        try {
            if(this.current != null && this.current.getMethod() == 0) {
                if(this.current.getCrc() != this.crc.getValue()) {
                    Log.w(("CRC mismatch: " + AlignedZipOutputStream.toString(this.current) + " != " + this.crc.getValue()));
                    this.current.setCrc(this.crc.getValue());
                }
                if(this.current.getSize() != ((long)this.bytes)) {
                    Log.w(("Size mismatch: " + AlignedZipOutputStream.toString(this.current) + " != " + this.bytes));
                    this.current.setSize(((long)this.bytes));
                }
                if(this.current.getCompressedSize() != this.current.getSize()) {
                    Log.w(("CompressedSize mismatch: " + AlignedZipOutputStream.toString(this.current) + " != " + this.current.getSize()));
                    this.current.setCompressedSize(this.current.getSize());
                }
            }
            super.closeEntry();
            this.entryOpen = false;
            this.current = null;
            this.crc.reset();
            this.bytes = 0;
        }
        catch(ZipException e) {
            throw new RuntimeException("Failed close entry: " + AlignedZipOutputStream.toString(this.current), e);
        }
    }

    @Override
    public void putNextEntry(ZipEntry ze) throws IOException {
        ze.setComment("");
        this.current = ze;
        if(this.entryOpen) {
            this.closeEntry();
        }
        if(ze.getMethod() == 0) {
            this.flush();
            ze.setExtra(new byte[(4 - (this.cos.length + 30 + ze.getName().length()) % 4) % 4]);
            ze.setCompressedSize(ze.getSize());
        }
        super.putNextEntry(ze);
        this.entryOpen = true;
        this.crc.reset();
        this.bytes = 0;
    }

    @Override
    public void setComment(String comment) {
        super.setComment("");
    }

    public static String toString(ZipEntry ze) {
        return ze == null ? "null" : ze.getName() + ", method: " + ze.getMethod() + ", crc: 0x" + Long.toHexString(ze.getCrc()) + ", size: " + ze.getSize() + ", compressed size: " + ze.getCompressedSize();
    }

    @Override
    public void write(int i) throws IOException {
        byte[] buf = this.buf;
        if(buf == null) {
            buf = new byte[1];
            this.buf = buf;
        }
        buf[0] = (byte)(i & 0xFF);
        this.write(buf, 0, 1);
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        this.write(buffer, 0, buffer.length);
    }

    @Override
    public void write(byte[] buffer, int offset, int byteCount) throws IOException {
        super.write(buffer, offset, byteCount);
        this.crc.update(buffer, offset, byteCount);
        this.bytes += byteCount;
    }
}

