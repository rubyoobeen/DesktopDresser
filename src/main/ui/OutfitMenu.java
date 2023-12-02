package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Represents application's main menu window frame
public class OutfitMenu extends JInternalFrame {
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

        createWestPanel();
        add(createWestPanel(), BorderLayout.WEST);
    }

    // creates a main outfit table
    private void createOutfitTable() {
        String[] columnNames = {"Name", "Collection"};
        tableModel = new DefaultTableModel(columnNames, 0);
        outfitTable = new JTable(tableModel);

        outfitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = outfitTable.getColumnModel();

        outfitTable.addMouseListener(new OutfitTableMouseAdapter(outfitTable));

        updateOutfitTable(closet);
    }

    // updates the outfit table with the provided list of outfits
    private void updateOutfitTable(Closet closet) {
        tableModel.setRowCount(0);
        List<Outfit> outfits = closet.getOutfitsFromCloset();

        for (Outfit outfit : outfits) {
            Object[] rowData = {outfit.getName(), outfit.getCollection()};
            tableModel.addRow(rowData);
        }
    }

    // create panel for WEST of frame
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        JPanel nameRow = createNameRow();
        westPanel.add(nameRow);
        JPanel comboRow = createComboBoxRow();
        westPanel.add(comboRow);
        JPanel makeOutfitRow = createMakeOutfitRow();
        westPanel.add(makeOutfitRow);

        return westPanel;
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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

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

    // creates make outfit button row
    private JPanel createMakeOutfitRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton makeOutfitButton = new JButton(new MakeOutfitAction());
        panel.add(makeOutfitButton);

        return panel;
    }

    // deletes outfit from closet and updates the outfit list
    private class MakeOutfitAction extends AbstractAction {
        private Closet updatedCloset;

        MakeOutfitAction() {
            super("Make Outfit");
            this.updatedCloset = closet;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {

        }
    }

    // creates combo boxes
    private JComboBox<Clothing> createComboBox(List<Clothing> clothingList) {
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
