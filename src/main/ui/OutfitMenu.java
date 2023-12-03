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
                    rowData[columnIndex] = clothing.getItem();
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
        JPanel firstComboRow = createTopBotDress();
        eastPanel.add(firstComboRow);
        JPanel secondComboRow = createOuterAccShoes();
        eastPanel.add(secondComboRow);
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
    private JPanel createTopBotDress() {
        JPanel comboPanel = new JPanel();
        comboPanel.setPreferredSize(new Dimension(150, 300));
        comboPanel.setLayout(new BoxLayout(comboPanel,BoxLayout.Y_AXIS));

        addLabelAndComboBox(comboPanel, "TOP", closet.getClothingsByCategory(ClothingCategory.TOP));
        comboPanel.add(new JButton(new AddOrRemoveAction(topCombo)));
        addLabelAndComboBox(comboPanel, "BOT", closet.getClothingsByCategory(ClothingCategory.BOT));
        comboPanel.add(new JButton(new AddOrRemoveAction(botCombo)));
        addLabelAndComboBox(comboPanel, "DRESS", closet.getClothingsByCategory(ClothingCategory.DRESS));
        comboPanel.add(new JButton(new AddOrRemoveAction(dressCombo)));

        return comboPanel;
    }

    // creates a panel for Outer, Acc, Shoes combo boxes to add and delete
    private JPanel createOuterAccShoes() {
        JPanel comboPanel = new JPanel();
        comboPanel.setPreferredSize(new Dimension(150, 300));
        comboPanel.setLayout(new BoxLayout(comboPanel,BoxLayout.Y_AXIS));

        addLabelAndComboBox(comboPanel, "OUTER", closet.getClothingsByCategory(ClothingCategory.OUTER));
        comboPanel.add(new JButton(new AddOrRemoveAction(outerCombo)));
        addLabelAndComboBox(comboPanel, "ACC", closet.getClothingsByCategory(ClothingCategory.ACC));
        comboPanel.add(new JButton(new AddOrRemoveAction(accCombo)));
        addLabelAndComboBox(comboPanel, "SHOES", closet.getClothingsByCategory(ClothingCategory.SHOES));
        comboPanel.add(new JButton(new AddOrRemoveAction(shoesCombo)));

        return comboPanel;
    }

    // add label and combo box pair to panel
    private void addLabelAndComboBox(JPanel panel, String labelText, List<Clothing> clothings) {
        JPanel pairPanel = new JPanel();
        pairPanel.setLayout(new GridLayout(1, 1));

        JLabel label = createLabel(labelText);
        JComboBox<Clothing> comboBox = createComboBox(clothings);

        pairPanel.add(label);
        pairPanel.add(comboBox);

        panel.add(pairPanel);
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

    // adds or removes item from selected outfit from closet and updates the outfit table
    private class AddOrRemoveAction extends AbstractAction {
        private JComboBox comboBox;

        AddOrRemoveAction(JComboBox selectedBox) {
            super("Add/Remove");
            this.comboBox = selectedBox;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = outfitTable.getSelectedRow();
            Clothing newClothing = (Clothing) comboBox.getSelectedItem();
            Outfit updateOutfit = getOutfitAtRow(selectedRow);

            if (selectedRow != 1) {
                existOutfit(updateOutfit, newClothing);
            } else {
                JOptionPane.showMessageDialog(null, "Error: Outfit Not Selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            updateOutfitTable();
        }

        // if clothing exits in outfit remove from outfit, add item to outfit otherwise
        private void existOutfit(Outfit outfit, Clothing clothing) {
            if (closet.getOutfitsFromCloset().contains(outfit)) {
                if (outfit.getCollection().contains(clothing)) {
                    removeClothingFromOutfit(outfit, clothing);
                } else {
                    addClothingToOutfit(outfit, clothing);
                }
            }
        }

        // adds clothing to the outfit
        private void addClothingToOutfit(Outfit outfit, Clothing clothing) {
            try {
                outfit.addClothingToOutfit(clothing);
                JOptionPane.showMessageDialog(OutfitMenu.this, "Success: Item Added");
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(null, "Error: Item Not Added");
            }
        }

        // removes clothing from the outfit
        private void removeClothingFromOutfit(Outfit outfit, Clothing clothing) {
            try {
                outfit.removeClothingFromOutfit(clothing);
                JOptionPane.showMessageDialog(OutfitMenu.this, "Success: Item Added");
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(null, "Error: Item Not Added");
            }
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
        textField.setPreferredSize(new Dimension(150, 25));
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