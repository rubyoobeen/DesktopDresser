package ui;

import model.*;
import model.exception.ClothingException;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Represents application's main menu window frame
public class OutfitMenu extends JInternalFrame {
    private static final String JSON_STORE = "./data/closet.json";
    private Closet closet;
    private JTable outfitTable;
    private DefaultTableModel tableModel;
    private JTextField textField;
    private JComboBox<Clothing> topCombo;
    private JComboBox<Clothing> botCombo;
    private JComboBox<Clothing> dressCombo;
    private JComboBox<Clothing> outerCombo;
    private JComboBox<Clothing> accCombo;
    private JComboBox<Clothing> shoesCombo;
    private String[] columnNames;

    // Constructor that takes a closet object and initialize the frame
    public OutfitMenu(Closet loadCloset) {
        this.closet = loadCloset;
        this.columnNames = new String[]{"name", "TOP", "BOT", "OUTER", "DRESS", "ACC", "SHOES"};

        initialize();
    }

    // initializes outfit tab
    private void initialize() {
        setTitle("OUTFIT");
        setSize(800,600);
        setLayout(new BorderLayout());

        createOutfitTable();
        JScrollPane scrollPane = new JScrollPane(outfitTable);
        add(scrollPane, BorderLayout.CENTER);

        add(createEastPanel(), BorderLayout.EAST);
    }

    // creates a main outfit table
    private void createOutfitTable() {
        List<String> categories = Arrays.asList("TOP", "BOT", "OUTER", "DRESS", "ACC", "SHOES");
        String[] columnNames = new String[categories.size() + 1];
        columnNames[0] = "Outfit Name";

        for (int i = 0; i < categories.size(); i++) {
            columnNames[i + 1] = categories.get(i);
        }

        tableModel = new DefaultTableModel(columnNames, 0);

        outfitTable = new JTable(tableModel);
        outfitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outfitTable.addMouseListener(new OutfitTableMouseAdapter(outfitTable));

        updateOutfitTable();
    }

    // updates the outfit table with the provided list of outfits
    private void updateOutfitTable() {
        List<Outfit> outfits = closet.getOutfitsFromCloset();

        tableModel.setRowCount(0);

        for (Outfit outfit : outfits) {
            Object[] rowData = new Object[columnNames.length];
            rowData[0] = outfit.getName();

            List<Clothing> collection = outfit.getCollection();
            for (Clothing clothing : collection) {
                String category = clothing.getCategory().toString();
                int columnIndex = getColumnIndex(category);

                if (columnIndex != -1) {
                    rowData[columnIndex] = clothing.getItem() + " " + clothing.getColor();
                }
            }

            tableModel.addRow(rowData);
        }
    }

    // gets column index for corresponding category
    private int getColumnIndex(String category) {
        for (int i = 1; i < columnNames.length; i++) {
            if (columnNames[i].equals(category)) {
                return i;
            }
        }
        return -1;
    }

    // create EAST panel for frame
    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        JPanel nameRow = createNameRow();
        eastPanel.add(nameRow);
        JPanel comboRow = createComboPanel();
        eastPanel.add(comboRow);
        eastPanel.add(new JButton(new SaveExitAction()));

        return eastPanel;
    }

    // creates the name row with a text field
    private JPanel createNameRow() {
        JPanel namePanel = new JPanel();
        namePanel.setPreferredSize(new Dimension(150, 50));
        namePanel.setLayout(new GridLayout(2, 2));

        namePanel.add(createLabel("Name:"));
        textField = createTextField();
        namePanel.add(textField);
        namePanel.add(new JButton(new MakeNewOutfitAction()));
        namePanel.add(new JButton(new DeleteOutfitAction()));

        return namePanel;
    }

    // creates a panel for Top, Bot, Dress combo boxes to add and delete
    private JPanel createComboPanel() {
        JPanel comboPanel = new JPanel();
        comboPanel.setPreferredSize(new Dimension(150, 600));
        comboPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        initializeComboBoxes();

        comboPanel.add(createTopPanel());
        comboPanel.add(createBotPanel());
        comboPanel.add(createDressPanel());
        comboPanel.add(createOuterPanel());
        comboPanel.add(createAccPanel());
        comboPanel.add(createShoesPanel());
        comboPanel.add(createBotPanel());

        return comboPanel;
    }

    // initializes all category combo boxes
    private void initializeComboBoxes() {
        shoesCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.SHOES));
    }

    // creates a panel for top label, combo box and add/delete button
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));

        topPanel.add(createLabel("TOP"));
        topCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.TOP));
        topPanel.add(topCombo);
        topPanel.add(new JButton(new AddAction(topCombo)));
        topPanel.add(new JButton(new RemoveAction(topCombo)));

        return topPanel;
    }

    // creates a panel for bot label, combo box and add/delete button
    private JPanel createBotPanel() {
        JPanel botPanel = new JPanel();
        botPanel.setLayout(new GridLayout(2, 2));

        botPanel.add(createLabel("BOT"));
        botCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.BOT));
        botPanel.add(botCombo);
        botPanel.add(new JButton(new AddAction(botCombo)));
        botPanel.add(new JButton(new RemoveAction(botCombo)));
        return botPanel;
    }

    // creates a panel for dress label, combo box and add/delete button
    private JPanel createDressPanel() {
        JPanel dressPanel = new JPanel();
        dressPanel.setLayout(new GridLayout(2, 2));

        dressPanel.add(createLabel("DRESS"));
        dressCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.DRESS));
        dressPanel.add(dressCombo);
        dressPanel.add(new JButton(new AddAction(dressCombo)));
        dressPanel.add(new JButton(new RemoveAction(dressCombo)));

        return dressPanel;
    }


    // creates a panel for outer label, combo box and add/delete button
    private JPanel createOuterPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(2, 2));

        outerPanel.add(createLabel("OUTER"));
        outerCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.OUTER));
        outerPanel.add(outerCombo);
        outerPanel.add(new JButton(new AddAction(outerCombo)));
        outerPanel.add(new JButton(new RemoveAction(outerCombo)));
        return outerPanel;
    }

    // creates a panel for acc label, combo box and add/delete button
    private JPanel createAccPanel() {
        JPanel accPanel = new JPanel();
        accPanel.setLayout(new GridLayout(2, 2));

        accPanel.add(createLabel("OUTER"));
        accCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.ACC));
        accPanel.add(accCombo);
        accPanel.add(new JButton(new AddAction(accCombo)));
        accPanel.add(new JButton(new RemoveAction(accCombo)));
        return accPanel;
    }

    // creates a panel for shoes label, combo box and add/delete button
    private JPanel createShoesPanel() {
        JPanel shoesPanel = new JPanel();
        shoesPanel.setLayout(new GridLayout(2, 2));

        shoesPanel.add(createLabel("SHOES"));
        accCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.SHOES));
        shoesPanel.add(shoesCombo);
        shoesPanel.add(new JButton(new AddAction(shoesCombo)));
        shoesPanel.add(new JButton(new RemoveAction(shoesCombo)));
        return shoesPanel;
    }

    // makes outfit from closet and updates the outfit list
    private class MakeNewOutfitAction extends AbstractAction {

        MakeNewOutfitAction() {
            super("Make");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                Outfit newOutfit = new Outfit(textField.getText());
                closet.addOutfitToCloset(newOutfit);
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(OutfitMenu.this, "Error: Outfit Not Added",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            updateOutfitTable();
        }
    }

    // adds selected item to selected outfit
    private class AddAction extends AbstractAction {
        private JComboBox comboBox;

        AddAction(JComboBox selectedCombo) {
            super("Add");
            this.comboBox = selectedCombo;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = outfitTable.getSelectedRow();
            Clothing newItem = (Clothing) comboBox.getSelectedItem();
            Outfit updateOutfit = getOutfitAtRow(selectedRow);

            if (selectedRow != -1 && newItem != null) {
                try {
                    updateOutfit.addClothingToOutfit(newItem);
                    JOptionPane.showMessageDialog(null, "Success: Item Added to Outfit");
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Restriction",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: Outfit/Item Not Selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            updateOutfitTable();
        }
    }

    // removes selected item to selected outfit
    private class RemoveAction extends AbstractAction {
        private JComboBox comboBox;

        RemoveAction(JComboBox selectedCombo) {
            super("Remove");
            this.comboBox = selectedCombo;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = outfitTable.getSelectedRow();
            Clothing removeItem = (Clothing) comboBox.getSelectedItem();
            Outfit updateOutfit = getOutfitAtRow(selectedRow);

            if (selectedRow != -1 && removeItem != null) {
                try {
                    updateOutfit.removeClothingFromOutfit(removeItem);
                    JOptionPane.showMessageDialog(null, "Success: Item deleted from outfit");
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Restriction",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: outfit/item not selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            updateOutfitTable();
        }
    }

    // deletes outfit from closet and updates the outfit list
    private class DeleteOutfitAction extends AbstractAction {

        DeleteOutfitAction() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String outfitNameToDelete = textField.getText();
            Outfit outfitToDelete = new Outfit(outfitNameToDelete);

            try {
                closet.removeOutfitFromCloset(outfitToDelete);
                updateOutfitTable();
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(null,"Error: Outfit Not Found",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // saves outfit to closet and close the frame
    private class SaveExitAction extends AbstractAction {

        SaveExitAction() {
            super("Save/Exit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
            try {
                jsonWriter.open();
                jsonWriter.write(closet);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,"Closet Saved to: " + JSON_STORE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed Saving to " + JSON_STORE,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            OutfitMenu.this.dispose();
        }
    }

    // gets outfit object at specific row
    private Outfit getOutfitAtRow(int row) {
        String outfitName = (String) tableModel.getValueAt(row,0);
        return closet.findOutfitByName(outfitName);
    }

    // creates combo boxes
    private JComboBox<Clothing> createComboBox(List<Clothing> clothingList) {
        clothingList.add(0,null);
        JComboBox<Clothing> comboBox = new JComboBox<>(clothingList.toArray(new Clothing[0]));
        comboBox.setPreferredSize(new Dimension(80, 25));
        return comboBox;
    }

    // creates labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.RIGHT);
        return label;
    }

    // creates text fields
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 25));
        return textField;
    }

    private class OutfitTableMouseAdapter extends MouseAdapter {
        private JTable outfitTable;

        public OutfitTableMouseAdapter(JTable table) {
            this.outfitTable = table;
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 1) {
                int row = outfitTable.rowAtPoint(evt.getPoint());
                int col = outfitTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col >= 0) {
                    outfitTable.getSelectionModel().setSelectionInterval(row, row);
                }
            }
        }
    }
}