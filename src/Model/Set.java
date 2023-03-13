package Model;

import java.util.ArrayList;
import java.util.List;

public class Set {
    private int associativity;
    private int blockSize;
    private CacheLine[] cacheLines;

    public Set(int associativity, int blockSize) {
        this.associativity = associativity;
        this.blockSize = blockSize;
        cacheLines = new CacheLine[associativity];
        for (int i = 0; i < cacheLines.length; i++) {
            cacheLines[i] = new CacheLine(-1, false, false,
                    new byte[blockSize], blockSize, 0, 0);
        }
    }

    public CacheLine findCacheLine(int tag) {
        for (int i = 0; i < cacheLines.length; i++) {
            if (cacheLines[i].getTag() == tag && cacheLines[i].isValid() ){
                return cacheLines[i];
            }
        }
        return null;
    }

    public int read(int tag, int offset) {
        CacheLine cacheLine = findCacheLine(tag);
        if ( cacheLine != null ) {

            return cacheLine.read(offset);
        } else {
            return 0;
        }
    }

    public void write(int tag, int offset, byte data) {
        CacheLine cacheLine = findCacheLine(tag);
        if ( cacheLine != null ) {
            cacheLine.write(offset, data);
        }
    }

    public CacheLine getLRU(){
        CacheLine leastRecentlyUsed = cacheLines[0];
        for (int j = 1; j < cacheLines.length; j++) {
            if (cacheLines[j].getLastUsed() < leastRecentlyUsed.getLastUsed()) {
                leastRecentlyUsed = cacheLines[j];
            }
        }
        return leastRecentlyUsed;
    }

    public CacheLine getLFU(){
        CacheLine leastFrequentlyUsed = cacheLines[0];
        for (int j = 1; j < cacheLines.length; j++) {
            if (cacheLines[j].getNumAccesses() < leastFrequentlyUsed.getNumAccesses()) {
                leastFrequentlyUsed = cacheLines[j];
            }
        }
        return leastFrequentlyUsed;
    }

    public CacheLine getFIFO(){
        for (int j = 0; j < cacheLines.length; j++) {
            if (cacheLines[j].getTag() == -1) {
                return cacheLines[j];
            }
        }
        return cacheLines[0];
    }
    
    //region get&set
    public int getAssociativity() {
        return associativity;
    }

    public void setAssociativity(int associativity) {
        this.associativity = associativity;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public CacheLine[] getCacheLines() {
        return cacheLines;
    }

    public void setCacheLines(CacheLine[] cacheLines) {
        this.cacheLines = cacheLines;
    }
    //endregion
}
