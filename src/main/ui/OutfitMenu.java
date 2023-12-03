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

    // Constructor that takes a closet object and initialize the frame
    public OutfitMenu(Closet loadCloset) {
        this.closet = loadCloset;
        initialize();
    }

    // initializes outfit tab
    private void initialize() {
        setTitle("OUTFIT");
        setSize(800,600);
        setLayout(new BorderLayout());

        createOutfitTable();
        add(new JScrollPane(outfitTable), BorderLayout.CENTER);

        add(createSouthPanel(), BorderLayout.SOUTH);
    }

    // creates a main outfit table
    private void createOutfitTable() {
        String[] columnNames = {"Name", "Collection"};
        tableModel = new DefaultTableModel(columnNames, 0);
        outfitTable = new JTable(tableModel);

        outfitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        outfitTable.addMouseListener(new OutfitTableMouseAdapter(outfitTable));

        updateOutfitTable(closet);
    }

    // updates the outfit table with the provided list of outfits
    private void updateOutfitTable(Closet closet) {
        List<Outfit> outfits = closet.getOutfitsFromCloset();

        tableModel.setRowCount(0);

        for (Outfit outfit : outfits) {
            Object[] rowData = {outfit.getName(), outfit.getCollection()};
            tableModel.addRow(rowData);
        }
    }

    // create panel for SOUTH of frame
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel nameRow = createNameRow();
        southPanel.add(nameRow);
        JPanel comboRow = createComboBoxRow();
        southPanel.add(comboRow);
        JPanel buttonRow = createButtonRow();
        southPanel.add(buttonRow);

        return southPanel;
    }

    // creates the name row with a text field
    private JPanel createNameRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        textField = createTextField();
        panel.add(createLabel("Name:"));
        panel.add(textField);

        return panel;
    }

    // creates a row for a clothing list of categories of combo box
    private JPanel createComboBoxRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        topCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.TOP));
        botCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.BOT));
        dressCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.DRESS));
        outerCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.OUTER));
        accCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.ACC));
        shoesCombo = createComboBox(closet.getClothingsByCategory(ClothingCategory.SHOES));

        panel.add(topCombo);
        panel.add(botCombo);
        panel.add(dressCombo);
        panel.add(outerCombo);
        panel.add(accCombo);
        panel.add(shoesCombo);

        return panel;
    }

    // creates button row
    private JPanel createButtonRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton makeOutfitButton = new JButton(new MakeOutfitAction());
        JButton deleteOutfitButton = new JButton(new DeleteOutfitAction());
        JButton saveOutfitButton = new JButton(new SaveExitAction());
        panel.add(makeOutfitButton);
        panel.add(deleteOutfitButton);
        panel.add(saveOutfitButton);

        return panel;
    }

    // makes outfit from closet and updates the outfit list
    private class MakeOutfitAction extends AbstractAction {

        MakeOutfitAction() {
            super("Make Outfit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                Outfit newOutfit = new Outfit(textField.getText());
                newOutfit.addClothingToOutfit((Clothing) topCombo.getSelectedItem());
                newOutfit.addClothingToOutfit((Clothing) botCombo.getSelectedItem());
                newOutfit.addClothingToOutfit((Clothing) dressCombo.getSelectedItem());
                newOutfit.addClothingToOutfit((Clothing) outerCombo.getSelectedItem());
                newOutfit.addClothingToOutfit((Clothing) accCombo.getSelectedItem());
                newOutfit.addClothingToOutfit((Clothing) shoesCombo.getSelectedItem());
                closet.addOutfitToCloset(newOutfit);
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(OutfitMenu.this, "Error: Outfit Not Added",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            updateOutfitTable(closet);
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
                updateOutfitTable(closet);
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

    // creates combo boxes
    private JComboBox<Clothing> createComboBox(List<Clothing> clothingList) {
        clothingList.add(0,null);
        JComboBox<Clothing> comboBox = new JComboBox<>(clothingList.toArray(new Clothing[0]));
        comboBox.setPreferredSize(new Dimension(150, 25));
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
