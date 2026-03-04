package android.ext;

class DaemonManager.36 implements Runnable {
    final DaemonManager a;
    private final byte b;
    private final short c;
    private final int d;
    private final long e;
    private final long f;
    private final long g;

    DaemonManager.36(DaemonManager daemonManager0, byte b, short v, int v1, long v2, long v3, long v4) {
        this.a = daemonManager0;
        this.b = b;
        this.c = v;
        this.d = v1;
        this.e = v2;
        this.f = v3;
        this.g = v4;
        super();
    }

    @Override
    public void run() {
        this.a.inOut.startMessage(this.b, 0x40, 0);
        this.a.inOut.writeInt(((int)this.c));
        this.a.inOut.writeInt(this.d);
        this.a.inOut.writeLong(this.e);
        this.a.inOut.writeLong(this.f);
        this.a.inOut.writeLong(this.g);
        this.a.send();
        DaemonManager.lastSearchFuzzy = false;
    }
}

