package View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;


public class SimulationView extends JFrame{
    private JPanel main = new JPanel();
    private JPanel parameters = new JPanel();
    private JPanel memoryInput = new JPanel();
    private JPanel History = new JPanel();
    private JPanel memoryVisualisation = new JPanel();
    private JPanel cacheVisualisation = new JPanel();

    //parameter panel
    private JLabel cacheSize = new JLabel("Cache Size (Bytes)");
    private JComboBox cacheSizeComboBox = new JComboBox<>();
    private JLabel blockSize = new JLabel("Block Size (Bytes)");
    private JComboBox blockSizeComboBox = new JComboBox<>();
    private JLabel associativity = new JLabel("Set Size");
    private JComboBox associativityComboBox = new JComboBox<>();
    private JLabel replacementPolicy = new JLabel("Replacement Policy");
    private JComboBox replacementPolicyComboBox = new JComboBox<>();
    private JLabel placementPolicy = new JLabel("Placement Policy");
    private JComboBox writePolicyComboBox = new JComboBox<>();
    private JLabel writeStrategy = new JLabel("Write Strategy");
    private JComboBox placementPolicyComboBox = new JComboBox<>();
    private JLabel memorySize = new JLabel("Memory Size (Bytes)");
    private JComboBox memorySizeComboBox = new JComboBox<>();
    private JButton createSystem = new JButton();

    //memory input panel
    private JButton readMemory = new JButton();
    private JLabel readFromAdressLabel = new JLabel();
    private JTextField readAddress = new JTextField();
    private JButton writeMemory = new JButton();
    private JLabel writeToAdressLabel = new JLabel();
    private JTextField writeAddress = new JTextField();
    private JLabel writeDataLabel = new JLabel();
    private JTextField writeByte = new JTextField();
    private JButton flushMemory = new JButton();
    private JLabel missCounter = new JLabel("Misses: ");
    private JLabel hitCounter = new JLabel("Hits: ");
    private JLabel hitRate = new JLabel("Hit Rate: ");
    
    //history panel
    private JTextArea historyTextArea = new JTextArea();
    private JScrollPane historyScrollPane = new JScrollPane(historyTextArea);

    //memory visualisation panel
    private JLabel memoryVisualisationLabel = new JLabel();
    private JTextArea memoryVisualisationTextArea = new JTextArea();
    private JScrollPane memoryVisualisationScrollPane = new JScrollPane(memoryVisualisationTextArea);

    //cache visualisation panel
    private JLabel cacheVisualisationLabel = new JLabel();
    private JTextArea cacheVisualisationTextArea = new JTextArea();
    private JScrollPane cacheVisualisationScrollPane = new JScrollPane(cacheVisualisationTextArea);

    public SimulationView(){
        cacheSizeComboBox.addItem("4");
        cacheSizeComboBox.addItem("8");
        cacheSizeComboBox.addItem("16");
        cacheSizeComboBox.addItem("32");
        blockSizeComboBox.addItem("2");
        blockSizeComboBox.addItem("4");
        blockSizeComboBox.addItem("8");
        blockSizeComboBox.addItem("16");
        associativityComboBox.addItem("1");
        associativityComboBox.addItem("2");
        associativityComboBox.addItem("4");
        associativityComboBox.addItem("8");
        replacementPolicyComboBox.addItem("LRU");
        replacementPolicyComboBox.addItem("LFU");
        replacementPolicyComboBox.addItem("FIFO");
        placementPolicyComboBox.addItem("DIRECT_MAPPED");
        placementPolicyComboBox.addItem("FULLY_ASSOCIATIVE");
        placementPolicyComboBox.addItem("SET_ASSOCIATIVE");
        writePolicyComboBox.addItem("WRITE_THROUGH");
        writePolicyComboBox.addItem("WRITE_BACK");
        memorySizeComboBox.addItem("16");
        memorySizeComboBox.addItem("32");
        memorySizeComboBox.addItem("64");
        memorySize.setText("Memory Size (Bytes)");
        createSystem.setText("Create");
        readMemory.setText("Read");
        readFromAdressLabel.setText("Address");
        writeMemory.setText("Write");
        writeToAdressLabel.setText("Address");
        writeDataLabel.setText("Data");
        flushMemory.setText("Flush");
        memoryVisualisationLabel.setText("Memory Visualisation");
        cacheVisualisationLabel.setText("Cache Visualisation");

        parameters.setLayout(new GridLayout(8, 2));
        memoryInput.setLayout(new GridLayout(4, 4));
        History.setLayout(new GridLayout(1, 1));
        memoryVisualisation.setLayout(new GridLayout(1, 1));
        cacheVisualisation.setLayout(new GridLayout(1, 1));

        parameters.add(cacheSize);
        parameters.add(cacheSizeComboBox);
        parameters.add(blockSize);
        parameters.add(blockSizeComboBox);
        parameters.add(associativity);
        parameters.add(associativityComboBox);
        parameters.add(replacementPolicy);
        parameters.add(replacementPolicyComboBox);
        parameters.add(placementPolicy);
        parameters.add(placementPolicyComboBox);
        parameters.add(writeStrategy);
        parameters.add(writePolicyComboBox);
        parameters.add(memorySize);
        parameters.add(memorySizeComboBox);
        parameters.add(createSystem);
        parameters.add(flushMemory);

        memoryInput.add(readFromAdressLabel);
        memoryInput.add(readAddress);
        memoryInput.add(readMemory);
        memoryInput.add(writeToAdressLabel);
        memoryInput.add(writeAddress);
        memoryInput.add(new JLabel());
        memoryInput.add(writeDataLabel);
        memoryInput.add(writeByte);
        memoryInput.add(writeMemory);
        memoryInput.add(missCounter);
        memoryInput.add(hitCounter);
        memoryInput.add(hitRate);

        History.add(historyScrollPane);
        memoryVisualisation.add(memoryVisualisationScrollPane);
        cacheVisualisation.add(cacheVisualisationScrollPane);

        main.setLayout(new GroupLayout(main));
        main.add(parameters);
        main.add(memoryInput);
        main.add(History);
        main.add(memoryVisualisation);
        main.add(cacheVisualisation);


        GroupLayout mainLayout = new GroupLayout(main);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(parameters, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                .addComponent(memoryInput, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                .addComponent(History, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(cacheVisualisation, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                .addComponent(memoryVisualisation, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(parameters, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    .addComponent(memoryInput, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                    .addComponent(History, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(cacheVisualisation, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                    .addComponent(memoryVisualisation, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)))
        );


        Font font = new Font("Arial", Font.BOLD, 18);
        parameters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Cache Parameters", TitledBorder.LEFT, TitledBorder.TOP, font));
        memoryInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Memory Input", TitledBorder.LEFT, TitledBorder.TOP, font));
        History.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "History", TitledBorder.LEFT, TitledBorder.TOP, font));
        memoryVisualisation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Memory Visualisation", TitledBorder.LEFT, TitledBorder.TOP, font));
        cacheVisualisation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                "Cache Visualisation", TitledBorder.LEFT, TitledBorder.TOP, font));

        main.setLayout(mainLayout);

        this.setTitle("Cache Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1215, 740);
        this.setContentPane(main);
        this.setVisible(true);
    }

    //region action listeners
    public void addCreateSystemListener(ActionListener listener){
        createSystem.addActionListener(listener);
    }

    public void addReadMemoryListener(ActionListener listener){
        readMemory.addActionListener(listener);
    }

    public void addWriteMemoryListener(ActionListener listener){
        writeMemory.addActionListener(listener);
    }

    public void addFlushMemoryListener(ActionListener listener){
        flushMemory.addActionListener(listener);
    }
    //endregion

    //region get&set
    public JComboBox getCacheSizeComboBox() {
        return cacheSizeComboBox;
    }

    public JComboBox getBlockSizeComboBox() {
        return blockSizeComboBox;
    }

    public JComboBox getAssociativityComboBox() {
        return associativityComboBox;
    }

    public JComboBox getReplacementPolicyComboBox() {
        return replacementPolicyComboBox;
    }

    public JComboBox getPlacementPolicyComboBox() {
        return placementPolicyComboBox;
    }

    public JComboBox getMemorySizeComboBox() {
        return memorySizeComboBox;
    }

    public JButton getCreateSystem() {
        return createSystem;
    }

    public JButton getReadMemory() {
        return readMemory;
    }

    public JTextField getReadAddress() {
        return readAddress;
    }

    public JButton getWriteMemory() {
        return writeMemory;
    }

    public JTextField getWriteAddress() {
        return writeAddress;
    }

    public JTextField getWriteByte() {
        return writeByte;
    }

    public JButton getFlushMemory() {
        return flushMemory;
    }

    public JTextArea getHistoryTextArea() {
        return historyTextArea;
    }

    public JTextArea getMemoryVisualisationTextArea() {
        return memoryVisualisationTextArea;
    }

    public JComboBox getWritePolicyComboBox() {
        return writePolicyComboBox;
    }

    public JPanel getParameters() {
        return parameters;
    }

    public JPanel getMemoryInput() {
        return memoryInput;
    }

    public JPanel getHistory() {
        return History;
    }

    public JPanel getMemoryVisualisation() {
        return memoryVisualisation;
    }

    public JPanel getCacheVisualisation() {
        return cacheVisualisation;
    }

    public JTextArea getCacheVisualisationTextArea() {
        return cacheVisualisationTextArea;
    }

    public JLabel getMissCounter() {
        return missCounter;
    }

    public JLabel getHitCounter() {
        return hitCounter;
    }

    public JLabel getHitRate() {
        return hitRate;
    }
    //endregion
}