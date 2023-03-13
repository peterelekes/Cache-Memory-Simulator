package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewController {
    private SimulationView simulationView;
    private Cache cache;
    private Memory memory;
    int missCounter = 0;
    int hitCounter = 0;
    double hitRate=0;
    JFrame frame = new JFrame("Cache Simulation");

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public ViewController(SimulationView simulationView) {
        this.simulationView = simulationView;
        simulationView.addCreateSystemListener(new CreateSystem());
        simulationView.addReadMemoryListener(new ReadMemory());
        simulationView.addWriteMemoryListener(new WriteMemory());
        simulationView.addFlushMemoryListener(new FlushMemory());
    }

    public boolean validateParameters() {
        String cacheSize = simulationView.getCacheSizeComboBox().getSelectedItem().toString();
        String blockSize = simulationView.getBlockSizeComboBox().getSelectedItem().toString();
        String associativity = simulationView.getAssociativityComboBox().getSelectedItem().toString();
        String replacementPolicy = simulationView.getReplacementPolicyComboBox().getSelectedItem().toString();
        String placementPolicy = simulationView.getPlacementPolicyComboBox().getSelectedItem().toString();
        String memorySize = simulationView.getMemorySizeComboBox().getSelectedItem().toString();

        if (cacheSize.equals("") || blockSize.equals("") || associativity.equals("") || replacementPolicy.equals("")
                || placementPolicy.equals("") || memorySize.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please fill in all parameters",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Integer.parseInt(cacheSize) < Integer.parseInt(blockSize)) {
            JOptionPane.showMessageDialog(frame, "Cache size cannot be smaller than block size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Integer.parseInt(memorySize) < Integer.parseInt(blockSize)) {
            JOptionPane.showMessageDialog(frame, "Memory size cannot be smaller than block size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Integer.parseInt(memorySize) < Integer.parseInt(cacheSize)) {
            JOptionPane.showMessageDialog(frame, "Memory size cannot be smaller than cache size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Integer.parseInt(cacheSize) % Integer.parseInt(blockSize) != 0) {
            JOptionPane.showMessageDialog(frame, "Cache size must be a multiple of block size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Integer.parseInt(memorySize) % Integer.parseInt(blockSize) != 0) {
            JOptionPane.showMessageDialog(frame, "Memory size must be a multiple of block size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //block size must be smaller than cache size
        if (Integer.parseInt(blockSize) >= Integer.parseInt(cacheSize)) {
            JOptionPane.showMessageDialog(frame, "Block size cannot be equal to or larger than cache size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //cache size must be smaller than memory size
        if (Integer.parseInt(cacheSize) >= Integer.parseInt(memorySize)) {
            JOptionPane.showMessageDialog(frame, "Cache size cannot be equal to or larger than memory size", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }


    class CreateSystem implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateParameters())
                return;
            int cacheSize = Integer.parseInt(simulationView.getCacheSizeComboBox().getSelectedItem().toString());
            int blockSize = Integer.parseInt(simulationView.getBlockSizeComboBox().getSelectedItem().toString());
            int associativity = Integer
                    .parseInt(simulationView.getAssociativityComboBox().getSelectedItem().toString());
            ReplacementStrategy replacementStrategy = ReplacementStrategy
                    .valueOf(simulationView.getReplacementPolicyComboBox().getSelectedItem().toString());
            PlacementStrategy placementStrategy = PlacementStrategy
                    .valueOf(simulationView.getPlacementPolicyComboBox().getSelectedItem().toString());
            WriteStrategy writeStrategy = WriteStrategy
                    .valueOf(simulationView.getWritePolicyComboBox().getSelectedItem().toString());
            int memorySize = Integer.parseInt(simulationView.getMemorySizeComboBox().getSelectedItem().toString());
            memory = new Memory(memorySize);
            cache = new Cache(cacheSize, blockSize, associativity, placementStrategy, replacementStrategy,
                    writeStrategy, memory);
            setMemoryVisualisation();
            setCacheVisualisation();
            missCounter=0;
            hitCounter=0;
            simulationView.getHitCounter().setText("Hits:" + String.valueOf(hitCounter));
            simulationView.getMissCounter().setText("Misses: " + String.valueOf(missCounter));
            simulationView.getHitRate().setText("Hit rate: " + String.format("%.1f", hitRate * 100) + "%");
            simulationView.getHistoryTextArea().setText("");
            simulationView.getWriteAddress().setText("");
            simulationView.getReadAddress().setText("");
            simulationView.getWriteByte().setText("");
        }
    }

    public boolean validateRead() {
        // check if it is a valid integer and smaller than memory size
        String address = simulationView.getReadAddress().getText();
        int memorySize = Integer.parseInt(simulationView.getMemorySizeComboBox().getSelectedItem().toString());
        int blockSize = Integer.parseInt(simulationView.getBlockSizeComboBox().getSelectedItem().toString());
        if (address.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please fill in memory address",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int addressValue = Integer.parseInt(address);
            if (addressValue < 0 || addressValue >= memorySize) {
                JOptionPane.showMessageDialog(frame, "Address is out of memory range", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Address must be an integer",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    class ReadMemory implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cache == null || memory == null){
                JOptionPane.showMessageDialog(frame, "Please create system first",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!validateRead())
                return;
            String address = simulationView.getReadAddress().getText();
            int addressInt = Integer.parseInt(address);
            boolean hit = cache.read(addressInt);
            if (hit)
                hitCounter++;
            else
                missCounter++;
            hitRate = (double) hitCounter / (hitCounter + missCounter);
            String[] toDisplay = { "r(0x" + Integer.toHexString(addressInt) + ") = ", hit ? "hit" : "miss" };
            simulationView.getHistoryTextArea().append(toDisplay[0] + toDisplay[1] + "\n");
            simulationView.getHitCounter().setText("Hits:" + String.valueOf(hitCounter));
            simulationView.getMissCounter().setText("Misses: " + String.valueOf(missCounter));
            simulationView.getHitRate().setText("Hit rate: " + String.format("%.1f", hitRate * 100) + "%");
            setMemoryVisualisation();
            setCacheVisualisation();
        }
    }

    public boolean validateWrite() {
        String address = simulationView.getWriteAddress().getText();
        String data = simulationView.getWriteByte().getText();
        //data must be a byte, address must be an integer
        //address must not be larger than size
        int memorySize = Integer.parseInt(simulationView.getMemorySizeComboBox().getSelectedItem().toString());
        if (address.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please fill in memory address",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (data.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please fill in data to write",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int addressValue = Integer.parseInt(address);
            if (addressValue < 0 || addressValue >= memorySize) {
                JOptionPane.showMessageDialog(frame, "Address is out of memory range", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Address must be an integer",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int dataValue = Integer.parseInt(data);
            if (dataValue < 0 || dataValue > 127) {
                JOptionPane.showMessageDialog(frame, "Data must be a byte",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Data must be a byte",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    class WriteMemory implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cache == null || memory == null){
                JOptionPane.showMessageDialog(frame, "Please create system first",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!validateWrite())
                return;
            int address = Integer.parseInt(simulationView.getWriteAddress().getText());
            String dataString = simulationView.getWriteByte().getText();
            byte dataByte = Byte.parseByte(dataString);
            boolean hit = cache.write(address, dataByte);
            if (hit)
                hitCounter++;
            else
                missCounter++;
            hitRate = (double) hitCounter / (hitCounter + missCounter);
            String[] toDisplay = { "w(0x" + Integer.toHexString(address) +
                    ", 0x" + dataString + ") = ", hit ? "hit" : "miss" };
            simulationView.getHistoryTextArea().append(toDisplay[0] + toDisplay[1] + "\n");
            simulationView.getHitCounter().setText("Hits:" + String.valueOf(hitCounter));
            simulationView.getMissCounter().setText("Misses: " + String.valueOf(missCounter));
            simulationView.getHitRate().setText("Hit rate: " + String.format("%.1f", hitRate * 100) + "%");
            setMemoryVisualisation();
            setCacheVisualisation();
        }
    }

    class FlushMemory implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cache == null)
                return;
            cache.flush();
            setCacheVisualisation();
            setMemoryVisualisation();
            simulationView.getHistoryTextArea().setText("");
            simulationView.getHitCounter().setText("Hits: ");
            simulationView.getMissCounter().setText("Misses: ");
            simulationView.getHitRate().setText("Hit Rate: ");
            missCounter=0;
            hitCounter=0;
            hitRate=0;
        }
    }

    public void setMemoryVisualisation() {
        String[][] memoryData = new String[memory.getMemorySize()][2];
        for (int i = 0; i < memory.getMemorySize(); i++) {
            memoryData[i][0] = "0x" + i;
            memoryData[i][1] = Integer.toHexString(memory.getMemoryData()[i]);
        }
        String[] columnNames = { "Address", "Data" };
        JTable memoryTable = new JTable(memoryData, columnNames);
        memoryTable.setEnabled(false);
        JScrollPane memoryScrollPane = new JScrollPane(memoryTable);
        simulationView.getMemoryVisualisation().removeAll();
        simulationView.getMemoryVisualisation().add(memoryScrollPane);
        simulationView.getMemoryVisualisation().revalidate();
        simulationView.getMemoryVisualisation().repaint();
    }

    public void setCacheVisualisation() {
        String[][] cacheData = new String[cache.getSize() / cache.getBlockSize()][7];
        Set[] sets = cache.getSets();
        for (int i = 0; i < cache.getSize() / cache.getBlockSize(); i++) {
            for (int j = 0; j < sets.length; j++) {
                CacheLine[] lines = sets[j].getCacheLines();
                for (CacheLine line : lines) {
                    cacheData[i][0] = Integer.toString(line.getSetNumber());
                    cacheData[i][1] = Integer.toString(line.getTag());
                    cacheData[i][2] = Boolean.toString(line.isDirty());
                    cacheData[i][3] = Boolean.toString(line.isValid());
                    cacheData[i][4] = Integer.toString(line.getNumAccesses());
                    cacheData[i][5] = Long.toString(line.getLastUsed());
                    byte[] displayData = line.getData();
                    String dataString = "";
                    for (int k = 0; k < displayData.length; k++) {
                        dataString += Integer.toHexString(displayData[k]) + " ";
                    }
                    cacheData[i][6] = dataString;
                    i++;
                }
            }
        }
        String[] columnNames = { "Set", "Tag", "Dirty", "Valid", "Accesses", "Last Used", "Data" };
        JTable cacheTable = new JTable(cacheData, columnNames);
        cacheTable.setEnabled(false);
        JScrollPane cacheScrollPane = new JScrollPane(cacheTable);
        simulationView.getCacheVisualisation().removeAll();
        simulationView.getCacheVisualisation().add(cacheScrollPane);
        simulationView.getCacheVisualisation().revalidate();
        simulationView.getCacheVisualisation().repaint();
    }
}
