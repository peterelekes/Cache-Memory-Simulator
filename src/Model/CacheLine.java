package Model;

public class CacheLine {
    private int tag;
    private int blockSize;
    private boolean dirty;
    private boolean valid;
    private byte[] data;
    private int numAccesses;
    private long lastUsed;
    private int setNumber = 0;

    public CacheLine(int tag, boolean dirty, boolean valid, byte[] data, int blockSize,
            int numAccesses, long lastUsed) {
        this.tag = tag;
        this.dirty = dirty;
        this.valid = valid;
        this.data = data;
        this.blockSize = blockSize;
        this.numAccesses = numAccesses;
        this.lastUsed = lastUsed;
    }

    protected int read(int address) {
        lastUsed = System.currentTimeMillis();
        numAccesses++;
        return data[(address) % (blockSize)];
    }

    public void write(int offset, byte data) {
        lastUsed = System.currentTimeMillis();
        numAccesses++;
        this.data[offset] = data;
        valid = true;
        dirty = true;
    }

    public void writeLine(int tag, byte[] data, int setNumber) {
        this.tag = tag;
        this.data = data;
        valid = true;
        dirty = false;
        this.numAccesses = 0;
        this.lastUsed = System.currentTimeMillis();
        this.setNumber = setNumber;
    }

    // region get&set
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getNumAccesses() {
        return numAccesses;
    }

    public void setNumAccesses(int numAccesses) {
        this.numAccesses = numAccesses;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    // endregion
}