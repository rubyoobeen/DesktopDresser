package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Color;
import model.exception.ClothingException;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Represents application's main menu window frame
public class ClothingMenu extends JInternalFrame {
    private static final String JSON_STORE = "./data/closet.json";
    private Closet closet;
    private JTable clothingTable;
    private DefaultTableModel tableModel;
    private JTextField textField;
    private JComboBox<Color> colorCombo;
    private JComboBox<ClothingCategory> categoryCombo;

    // Constructor that takes a Closet object and initialize the frame to given parent frame
    public ClothingMenu(Component parent, Closet loadedCloset) {
        this.closet = loadedCloset;
        initialize();
        setPosition(parent);
    }

    // initializes clothing tab
    private void initialize() {
        setTitle("CLOTHING");
        setSize(800,600);
        setLayout(new BorderLayout());
        setResizable(false);
        setClosable(true);

        createClothingTable();
        add(new JScrollPane(clothingTable), BorderLayout.CENTER);

        createSouthPanel();
        add(createSouthPanel(), BorderLayout.SOUTH);

        add(createNorthPanel(), BorderLayout.NORTH);
    }

    // creates a main clothing table
    private void createClothingTable() {
        String[] columnNames = {"ITEM NAME", "CATEGORY", "COLOR", "CLEAN?"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clothingTable = new JTable(tableModel);

        clothingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clothingTable.addMouseListener(new ClothingTableMouseAdapter(clothingTable));

        updateClothingTable(closet);
    }

    // updates clothing to the list
    private void updateClothingTable(Closet closet) {
        List<Clothing> closetItems = closet.getClothingsFromCloset();

        tableModel.setRowCount(0);

        for (Clothing clothing : closetItems) {
            Object[] rowData = {clothing.getItem(), clothing.getCategory(), clothing.getColor(),
                    clothing.isClean()};
            tableModel.addRow(rowData);
        }
    }

    // updates new top/bot/dress/outer/acc/shoes list to the table in the frame
    private void updateDifferentTable(List<Clothing> clothingList) {
        tableModel.setRowCount(0);

        for (Clothing clothing : clothingList) {
            Object[] rowData = {clothing.getItem(), clothing.getCategory(), clothing.getColor(),
                    clothing.isClean()};
            tableModel.addRow(rowData);
        }
    }

    // creates panel for WEST of frame
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        JPanel firstRow = createFirstRowPanel();
        southPanel.add(firstRow);
        JPanel secondRow = createSecondRowPanel();
        southPanel.add(secondRow);

        return southPanel;
    }

    // creates first row of the south panel
    private JPanel createFirstRowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        textField = createTextField();
        categoryCombo = createComboBox(ClothingCategory.values());
        colorCombo = createComboBox(Color.values());

        panel.add(createLabel("Item:"));
        panel.add(textField);
        panel.add(createLabel("Category:"));
        panel.add(categoryCombo);
        panel.add(createLabel("Color:"));
        panel.add(colorCombo);

        return panel;
    }

    // creates second row of the south panel
    private JPanel createSecondRowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JButton(new AddClothingAction()));
        panel.add(new JButton(new DeleteClothingAction()));
        panel.add(new JButton(new View()));
        panel.add(new JButton(new SaveExitAction()));

        return panel;
    }

    // creates NORTH panel of the frame
    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(createLabel("Wash or mark Dirty:"));
        panel.add(new JButton(new MarkClean()));
        panel.add(new JButton(new MarkDirty()));

        return panel;
    }

    // set position on parent frame
    private void setPosition(Component parent) {
        setLocation(100, 100);
    }

    // adds new clothing to closet and updates the clothing list
    private class AddClothingAction extends AbstractAction {

        AddClothingAction() {
            super("Add");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                String itemName = textField.getText();
                ClothingCategory selectedCategory = (ClothingCategory) categoryCombo.getSelectedItem();
                Color selectedColor = (Color) colorCombo.getSelectedItem();

                Clothing newClothing = new Clothing(itemName, selectedCategory, selectedColor);
                closet.addClothingToCloset(newClothing);

                updateClothingTable(closet);

                JOptionPane.showMessageDialog(ClothingMenu.this, "Success: item added",
                        "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: item not added",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // deletes selected clothing item from the table
    private class DeleteClothingAction extends AbstractAction {

        DeleteClothingAction() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = clothingTable.getSelectedRow();

            if (selectedRow != -1) {
                Clothing selectedClothing = getClothingAtRow(selectedRow);

                try {
                    closet.removeClothingFromCloset(selectedClothing);
                    updateClothingTable(closet);
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Success: item deleted",
                            "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Error: item not deleted",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: no item selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SaveExitAction extends AbstractAction {

        SaveExitAction() {
            super("Exit/Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
            try {
                jsonWriter.open();
                jsonWriter.write(closet);
                jsonWriter.close();
                JOptionPane.showMessageDialog(ClothingMenu.this,"Success: closet saved to: " + JSON_STORE,
                        "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: failed saving to " + JSON_STORE,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            ClothingMenu.this.dispose();
        }
    }

    // view items by type of given choice
    private class View extends AbstractAction {
        private List<Clothing> viewList;

        View() {
            super("View");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ClothingCategory selectedCategory = (ClothingCategory) categoryCombo.getSelectedItem();
            Color selectedColor = (Color) colorCombo.getSelectedItem();

            if (!(selectedColor != null && selectedCategory != null)) {
                if (selectedColor == null && selectedCategory == null) {
                    updateClothingTable(closet);
                } else {
                    if (selectedColor == null && selectedCategory != null) {
                        viewList = closet.getClothingsByCategory(selectedCategory);
                        updateDifferentTable(viewList);
                    } else if (selectedColor != null && selectedCategory == null) {
                        viewList = closet.getClothingByColor(selectedColor);
                        updateDifferentTable(viewList);
                    } else {
                        JOptionPane.showMessageDialog(ClothingMenu.this, "Error: error occurred",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: select one type to view",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // mark item as clean
    private class MarkClean extends AbstractAction {

        MarkClean() {
            super("Wash");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = clothingTable.getSelectedRow();

            if (selectedRow != -1) {
                Clothing selectedClothing = closet.getClothingsFromCloset().get(selectedRow);

                try {
                    selectedClothing.setClean();
                    updateClothingTable(closet);
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Success: item is clean");
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(ClothingMenu.this,
                            "Error: item is already clean", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: no item selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // mark item as dirty
    private class MarkDirty extends AbstractAction {

        MarkDirty() {
            super("Dirty");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedRow = clothingTable.getSelectedRow();

            if (selectedRow != -1) {
                Clothing selectedClothing = closet.getClothingsFromCloset().get(selectedRow);

                try {
                    selectedClothing.setDirty();
                    updateClothingTable(closet);
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Success: item is dirty");
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(ClothingMenu.this,
                            "Error: item is already dirty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: no item selected",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // gets clothing object at specific row
    private Clothing getClothingAtRow(int row) {
        String itemName = (String) tableModel.getValueAt(row, 0);
        ClothingCategory category = (ClothingCategory) tableModel.getValueAt(row, 1);
        Color color = (Color) tableModel.getValueAt(row, 2);

        return new Clothing(itemName, category, color);
    }

    // creates text field
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 25));
        return textField;
    }

    // creates label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.RIGHT);
        return label;
    }

    // creates combo box for enums with a blank option
    private <T> JComboBox<T> createComboBox(T[] enumConstants) {
        T[] itemBlank = Arrays.copyOf(enumConstants,enumConstants.length + 1);
        System.arraycopy(enumConstants, 0, itemBlank, 1, enumConstants.length);
        itemBlank[0] = null;

        JComboBox<T> comboBox = new JComboBox<>(itemBlank);
        comboBox.setPreferredSize(new Dimension(150, 25));
        return comboBox;
    }

    // mouse adapter for handling clicks on JTable
    private class ClothingTableMouseAdapter extends MouseAdapter {
        private JTable clothingTable;

        public ClothingTableMouseAdapter(JTable table) {
            this.clothingTable = table;
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 1) {
                int row = clothingTable.rowAtPoint(evt.getPoint());
                int col = clothingTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col >= 0) {
                    clothingTable.getSelectionModel().setSelectionInterval(row, row);
                }
            }
        }
    }

}
