package android.ext;

class GotoAddress {
    long address;
    long source;

    public GotoAddress(long address, long source) {
        this.address = address;
        this.source = source;
    }

    public String getAddress() {
        return AddressItem.getStringAddress(this.address, 0x20);
    }

    public long getSource() {
        return this.source;
    }
}

