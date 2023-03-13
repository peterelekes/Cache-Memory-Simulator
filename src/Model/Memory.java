package Model;

public class Memory {
    private int memorySize;
    private byte[] memoryData;

    public Memory(int memorySize) {
        this.memorySize = memorySize;
        memoryData = new byte[memorySize];
        for (int i = 0; i < memorySize; i++) {
            memoryData[i] = (byte) (Math.random() * Byte.MAX_VALUE);
        }
    }

    public byte[] read(int address, int lineSize) {
        byte[] data = new byte[lineSize];

        if(address == memorySize-1){
            data[0] = memoryData[address];
            return data;}

        for (int i = 0; i < lineSize; i++) {
            if (address + i == memorySize - 1) {
                data[i] = memoryData[address + i];
                return data;
            }
            data[i] = memoryData[address + i];
        }
        return data;
    }

    public void write(int address, byte data) {
        memoryData[address] = data;
    }

    //region get&set
    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public byte[] getMemoryData() {
        return memoryData;
    }

    public void setMemoryData(byte[] memoryData) {
        this.memoryData = memoryData;
    }
    //endregion
}