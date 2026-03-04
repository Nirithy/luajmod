package lasm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class SimpleCharStream {
    int available;
    protected int[] bufcolumn;
    protected char[] buffer;
    protected int[] bufline;
    public int bufpos;
    int bufsize;
    protected int column;
    protected int inBuf;
    protected Reader inputStream;
    protected int line;
    protected int maxNextCharInd;
    protected boolean prevCharIsCR;
    protected boolean prevCharIsLF;
    public static final boolean staticFlag;
    protected int tabSize;
    int tokenBegin;

    public SimpleCharStream(InputStream dstream) {
        this(dstream, 1, 1, 0x1000);
    }

    public SimpleCharStream(InputStream dstream, int startline, int startcolumn) {
        this(dstream, startline, startcolumn, 0x1000);
    }

    public SimpleCharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
        this(new InputStreamReader(dstream), startline, startcolumn, buffersize);
    }

    public SimpleCharStream(InputStream dstream, String encoding) throws UnsupportedEncodingException {
        this(dstream, encoding, 1, 1, 0x1000);
    }

    public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
        this(dstream, encoding, startline, startcolumn, 0x1000);
    }

    public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
        this((encoding == null ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding)), startline, startcolumn, buffersize);
    }

    public SimpleCharStream(Reader dstream) {
        this(dstream, 1, 1, 0x1000);
    }

    public SimpleCharStream(Reader dstream, int startline, int startcolumn) {
        this(dstream, startline, startcolumn, 0x1000);
    }

    public SimpleCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
        this.bufpos = -1;
        this.prevCharIsCR = false;
        this.prevCharIsLF = false;
        this.maxNextCharInd = 0;
        this.inBuf = 0;
        this.tabSize = 8;
        this.inputStream = dstream;
        this.line = startline;
        this.column = startcolumn - 1;
        this.bufsize = buffersize;
        this.available = buffersize;
        this.buffer = new char[buffersize];
        this.bufline = new int[buffersize];
        this.bufcolumn = new int[buffersize];
    }

    public char BeginToken() throws IOException {
        this.tokenBegin = -1;
        char c = this.readChar();
        this.tokenBegin = this.bufpos;
        return c;
    }

    public void Done() {
        this.buffer = null;
        this.bufline = null;
        this.bufcolumn = null;
    }

    protected void ExpandBuff(boolean wrapAround) {
        char[] newbuffer = new char[this.bufsize + 0x800];
        int[] newbufline = new int[this.bufsize + 0x800];
        int[] newbufcolumn = new int[this.bufsize + 0x800];
        try {
            if(wrapAround) {
                System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
                this.buffer = newbuffer;
                System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufline = newbufline;
                System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufcolumn = newbufcolumn;
                int v = this.bufpos + (this.bufsize - this.tokenBegin);
                this.bufpos = v;
                this.maxNextCharInd = v;
            }
            else {
                System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
                this.buffer = newbuffer;
                System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
                this.bufline = newbufline;
                System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
                this.bufcolumn = newbufcolumn;
                int v1 = this.bufpos - this.tokenBegin;
                this.bufpos = v1;
                this.maxNextCharInd = v1;
            }
        }
        catch(Throwable t) {
            throw new Error(t.getMessage());
        }
        this.bufsize += 0x800;
        this.available = this.bufsize;
        this.tokenBegin = 0;
    }

    protected void FillBuff() throws IOException {
        if(this.maxNextCharInd == this.available) {
            if(this.available != this.bufsize) {
                if(this.available > this.tokenBegin) {
                    this.available = this.bufsize;
                }
                else if(this.tokenBegin - this.available < 0x800) {
                    this.ExpandBuff(true);
                }
                else {
                    this.available = this.tokenBegin;
                }
            }
            else if(this.tokenBegin > 0x800) {
                this.maxNextCharInd = 0;
                this.bufpos = 0;
                this.available = this.tokenBegin;
            }
            else if(this.tokenBegin < 0) {
                this.maxNextCharInd = 0;
                this.bufpos = 0;
            }
            else {
                this.ExpandBuff(false);
            }
        }
        try {
            int v = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd);
            if(v == -1) {
                this.inputStream.close();
                throw new IOException();
            }
            this.maxNextCharInd += v;
        }
        catch(IOException e) {
            --this.bufpos;
            this.backup(0);
            if(this.tokenBegin == -1) {
                this.tokenBegin = this.bufpos;
            }
            throw e;
        }
    }

    public String GetImage() {
        return this.bufpos < this.tokenBegin ? new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1) : new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
    }

    public char[] GetSuffix(int len) {
        char[] ret = new char[len];
        if(this.bufpos + 1 >= len) {
            System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
            return ret;
        }
        System.arraycopy(this.buffer, this.bufsize - (len - this.bufpos - 1), ret, 0, len - this.bufpos - 1);
        System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
        return ret;
    }

    public void ReInit(InputStream dstream) {
        this.ReInit(dstream, 1, 1, 0x1000);
    }

    public void ReInit(InputStream dstream, int startline, int startcolumn) {
        this.ReInit(dstream, startline, startcolumn, 0x1000);
    }

    public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
        this.ReInit(new InputStreamReader(dstream), startline, startcolumn, buffersize);
    }

    public void ReInit(InputStream dstream, String encoding) throws UnsupportedEncodingException {
        this.ReInit(dstream, encoding, 1, 1, 0x1000);
    }

    public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
        this.ReInit(dstream, encoding, startline, startcolumn, 0x1000);
    }

    public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
        this.ReInit((encoding == null ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding)), startline, startcolumn, buffersize);
    }

    public void ReInit(Reader dstream) {
        this.ReInit(dstream, 1, 1, 0x1000);
    }

    public void ReInit(Reader dstream, int startline, int startcolumn) {
        this.ReInit(dstream, startline, startcolumn, 0x1000);
    }

    public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
        this.inputStream = dstream;
        this.line = startline;
        this.column = startcolumn - 1;
        if(this.buffer == null || buffersize != this.buffer.length) {
            this.bufsize = buffersize;
            this.available = buffersize;
            this.buffer = new char[buffersize];
            this.bufline = new int[buffersize];
            this.bufcolumn = new int[buffersize];
        }
        this.prevCharIsCR = false;
        this.prevCharIsLF = false;
        this.maxNextCharInd = 0;
        this.inBuf = 0;
        this.tokenBegin = 0;
        this.bufpos = -1;
    }

    protected void UpdateLineColumn(char c) {
        ++this.column;
        if(this.prevCharIsLF) {
            this.prevCharIsLF = false;
            this.column = 1;
            ++this.line;
        }
        else if(this.prevCharIsCR) {
            this.prevCharIsCR = false;
            if(c == 10) {
                this.prevCharIsLF = true;
            }
            else {
                this.column = 1;
                ++this.line;
            }
        }
        switch(c) {
            case 9: {
                this.column += this.tabSize - this.column % this.tabSize;
                break;
            }
            case 10: {
                this.prevCharIsLF = true;
                break;
            }
            case 13: {
                this.prevCharIsCR = true;
            }
        }
        this.bufline[this.bufpos] = this.line;
        this.bufcolumn[this.bufpos] = this.column;
    }

    public void adjustBeginLineColumn(int newLine, int newCol) {
        int start = this.tokenBegin;
        int len = this.bufpos < this.tokenBegin ? this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf : this.bufpos - this.tokenBegin + this.inBuf + 1;
        int i = 0;
        int j = 0;
        int columnDiff = 0;
        while(i < len) {
            int[] arr_v = this.bufline;
            j = start % this.bufsize;
            int v7 = arr_v[j];
            int[] arr_v1 = this.bufline;
            ++start;
            int k = start % this.bufsize;
            if(v7 != arr_v1[k]) {
                break;
            }
            this.bufline[j] = newLine;
            int nextColDiff = this.bufcolumn[k] + columnDiff - this.bufcolumn[j];
            this.bufcolumn[j] = newCol + columnDiff;
            columnDiff = nextColDiff;
            ++i;
        }
        if(i < len) {
            int newLine = newLine + 1;
            this.bufline[j] = newLine;
            this.bufcolumn[j] = newCol + columnDiff;
            int i = i;
            while(i < len) {
                int[] arr_v2 = this.bufline;
                j = start % this.bufsize;
                ++start;
                if(arr_v2[j] == this.bufline[start % this.bufsize]) {
                    this.bufline[j] = newLine;
                    ++i;
                }
                else {
                    this.bufline[j] = newLine;
                    ++i;
                    ++newLine;
                }
            }
        }
        this.line = this.bufline[j];
        this.column = this.bufcolumn[j];
    }

    public void backup(int amount) {
        this.inBuf += amount;
        int v1 = this.bufpos - amount;
        this.bufpos = v1;
        if(v1 < 0) {
            this.bufpos += this.bufsize;
        }
    }

    public int getBeginColumn() {
        return this.bufcolumn[this.tokenBegin];
    }

    public int getBeginLine() {
        return this.bufline[this.tokenBegin];
    }

    @Deprecated
    public int getColumn() {
        return this.bufcolumn[this.bufpos];
    }

    public int getEndColumn() {
        return this.bufcolumn[this.bufpos];
    }

    public int getEndLine() {
        return this.bufline[this.bufpos];
    }

    @Deprecated
    public int getLine() {
        return this.bufline[this.bufpos];
    }

    protected int getTabSize(int i) {
        return this.tabSize;
    }

    public char readChar() throws IOException {
        if(this.inBuf > 0) {
            --this.inBuf;
            int v = this.bufpos + 1;
            this.bufpos = v;
            if(v == this.bufsize) {
                this.bufpos = 0;
            }
            return this.buffer[this.bufpos];
        }
        int v1 = this.bufpos + 1;
        this.bufpos = v1;
        if(v1 >= this.maxNextCharInd) {
            this.FillBuff();
        }
        char c = this.buffer[this.bufpos];
        this.UpdateLineColumn(c);
        return c;
    }

    protected void setTabSize(int i) {
        this.tabSize = i;
    }
}

