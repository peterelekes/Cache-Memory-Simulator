package Model;


public class Cache {
    private int size;
    private int blockSize;
    private int associativity;
    private Memory memory;
    private PlacementStrategy placementStrategy;
    private ReplacementStrategy replacementStrategy;
    private WriteStrategy writeStrategy;
    private Set[] sets;

    public Cache(int size, int blockSize, int associativity, PlacementStrategy placementStrategy,
            ReplacementStrategy replacementStrategy, WriteStrategy writeStrategy, Memory memory) {
        this.size = size;
        this.blockSize = blockSize;
        this.placementStrategy = placementStrategy;
        this.replacementStrategy = replacementStrategy;
        this.writeStrategy = writeStrategy;
        this.memory = memory;
        switch (placementStrategy) {
            case FULLY_ASSOCIATIVE:
                this.associativity = size / blockSize;
                break;
            case DIRECT_MAPPED:
                this.associativity = 1;
                break;
            case SET_ASSOCIATIVE:
                this.associativity = associativity;
                break;
            default:
                break;
        }
        sets = new Set[size / (this.associativity * blockSize)];
        for (int i = 0; i < sets.length; i++)
            sets[i] = new Set(this.associativity, blockSize);
    }

    private int getTag(int address) {
        return (address / (sets.length * blockSize));
    }

    private boolean isInMemory(int address) {
        int index = (address / blockSize) % sets.length;
        return sets[index].findCacheLine(getTag(address)) != null;
    }

    private void allocate(int address) {
        int index = (address / blockSize) % sets.length;
        Set set = sets[index];
        CacheLine toReplace;
        switch (replacementStrategy) {
            case LRU:
                toReplace = set.getLRU();
                break;
            case LFU:
                toReplace = set.getLFU();
                break;
            case FIFO:
                toReplace = set.getFIFO();
                break;
            default:
                toReplace = null;
                break;
        }
        if (toReplace.isDirty()) {
            byte[] lineData = toReplace.getData();
            int replaceAddress = (toReplace.getTag() * sets.length + index) * blockSize;
            for (int i = 0; i < blockSize; i++)
                memory.write(replaceAddress + i, lineData[i]);
        }
        byte[] writeData = new byte[blockSize];
        int startAddress = address - address % blockSize;
        writeData = memory.read(startAddress, blockSize);
        toReplace.writeLine(getTag(address), writeData, index);
    }

    public boolean read(int address) {
        boolean toReturn=true;
        if (!isInMemory(address)) {
            allocate(address);
            toReturn=false;
        }
        Set set = sets[(address/blockSize)%sets.length];
        int blockOffset = address % blockSize;
        set.read(getTag(address), blockOffset);
        return toReturn;
    }

    public boolean write(int address, byte data){
        boolean toReturn = true;
        if(!isInMemory(address)){
            allocate(address);
            toReturn = false;
        }
        int index = (address/blockSize)%sets.length;
        int blockOffset = address%blockSize;
        Set set = sets[index];
        set.write(getTag(address), blockOffset, data);
        if(writeStrategy==WriteStrategy.WRITE_THROUGH)
            memory.write(address, data);
        return toReturn;
    }

    public void flush() {
        for(Set set : sets){
            for(CacheLine line : set.getCacheLines()){
                line.setBlockSize(blockSize);
                line.setDirty(false);
                line.setLastUsed(0);
                line.setNumAccesses(0);
                line.setTag(-1);
                line.setValid(false);
                line.setData(new byte[blockSize]);

            }
        }
    }

    // region get&set
    public ReplacementStrategy getReplacementStrategy() {
        return replacementStrategy;
    }

    public void setReplacementStrategy(ReplacementStrategy replacementStrategy) {
        this.replacementStrategy = replacementStrategy;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public PlacementStrategy getPlacementStrategy() {
        return placementStrategy;
    }

    public void setPlacementStrategy(PlacementStrategy placementStrategy) {
        this.placementStrategy = placementStrategy;
    }

    public WriteStrategy getWriteStrategy() {
        return writeStrategy;
    }

    public void setWriteStrategy(WriteStrategy writeStrategy) {
        this.writeStrategy = writeStrategy;
    }

    public Set[] getSets() {
        return sets;
    }

    public void setSets(Set[] sets) {
        this.sets = sets;
    }

    public int getAssociativity() {
        return associativity;
    }

    public void setAssociativity(int associativity) {
        this.associativity = associativity;
    }
    // endregion
}