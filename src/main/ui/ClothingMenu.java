package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Color;
import model.exception.ClothingException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

// Represents application's main menu window frame
public class ClothingMenu extends JInternalFrame {
    private Closet closet;
    private JTable clothingTable;
    private DefaultTableModel tableModel;
    private JTextField textField;
    private JComboBox<Color> colorComboBox;
    private JComboBox<ClothingCategory> categoryComboBox;

    // Constructor that takes a Closet object and initialize the frame
    public ClothingMenu(Closet loadedCloset) {
        this.closet = loadedCloset;
        initialize();
    }

    // initializes clothing tab
    private void initialize() {
        setTitle("CLOTHING");
        setSize(800,600);
        setLayout(new BorderLayout());

        createClothingTable();
        add(new JScrollPane(clothingTable), BorderLayout.CENTER);

        createSouthPanel();
        add(createSouthPanel(), BorderLayout.SOUTH);
//        add(createEastPanel(), BorderLayout.EAST);
    }

    // creates a main clothing table
    private void createClothingTable() {
        String[] columnNames = {"Item", "Category", "Color", "Clean?"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clothingTable = new JTable(tableModel);

        clothingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = clothingTable.getColumnModel();
        columnModel.getColumn(3).setCellRenderer(new CheckBoxRenderer());

        clothingTable.addMouseListener(new ClothingTableMouseAdapter(clothingTable));

        updateClothingTable(closet);
    }

    // updates clothing to the list
    private void updateClothingTable(Closet closet) {
        tableModel.setRowCount(0);
        List<Clothing> closetItems = closet.getClothingsFromCloset();

        for (Clothing clothing : closetItems) {
            Object[] rowData = {clothing.getItem(), clothing.getCategory(), clothing.getColor(),
                    clothing.isClean()};
            tableModel.addRow(rowData);
        }
    }

//    // updates clothing list by same categories
//    private void updateCategoryTable(Closet closet) {
//        tableModel.setRowCount(0);
//        List<Clothing> byCategories = closet.getClothingsByCategory();
//
//        for (Clothing clothing : byCategories) {
//            Object[] rowData = {clothing.getItem(), clothing.getCategory(), clothing.getColor(),
//                    clothing.isClean()};
//            tableModel.addRow(rowData);
//        }
//    }

//    // updates clothing list by same colors
//    private void updateColorTable(Closet closet) {
//        tableModel.setRowCount(0);
//        List<Clothing> byColors = closet.getClothingByColor();
//
//        for (Clothing clothing : byColors) {
//            Object[] rowData = {clothing.getItem(), clothing.getCategory(), clothing.getColor(),
//                    clothing.isClean()};
//            tableModel.addRow(rowData);
//        }
//    }

//    // creates panel for EAST of frame
//    private JPanel createEastPanel() {
//        JPanel eastPanel = new JPanel();
//        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.X_AXIS));
//
//        eastPanel.add(new JButton(new ViewByCategory()));
//        eastPanel.add(new JButton(new DeleteClothingAction()));
//
//        return eastPanel;
//    }

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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        textField = createTextField();
        categoryComboBox = createComboBox(ClothingCategory.values());
        colorComboBox = createComboBox(Color.values());

        panel.add(createLabel("Item:"));
        panel.add(textField);
        panel.add(createLabel("Category:"));
        panel.add(categoryComboBox);
        panel.add(createLabel("Color:"));
        panel.add(colorComboBox);

        return panel;
    }

    // creates second row of the south panel
    private JPanel createSecondRowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(new JButton(new AddClothingAction()));
        panel.add(new JButton(new DeleteClothingAction()));

        return panel;
    }

    // adds new clothing to closet and updates the clothing list
    private class AddClothingAction extends AbstractAction {
        private Closet updatedCloset;

        AddClothingAction() {
            super("Add");
            this.updatedCloset = closet;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                String itemName = textField.getText();
                ClothingCategory selectedCategory = (ClothingCategory) categoryComboBox.getSelectedItem();
                Color selectedColor = (Color) colorComboBox.getSelectedItem();

                Clothing newClothing = new Clothing(itemName, selectedCategory, selectedColor);
                updatedCloset.addClothingToCloset(newClothing);

                updateClothingTable(updatedCloset);

                JOptionPane.showMessageDialog(ClothingMenu.this, "Success: Item Added");
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: Item Not Added",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // deletes selected clothing item from the table
    private class DeleteClothingAction extends AbstractAction {
        private Closet updatedCloset;

        DeleteClothingAction() {
            super("Delete");
            this.updatedCloset = closet;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int selectedIndex = clothingTable.getSelectedRow();

            if (selectedIndex != -1) {
                Clothing selectedClothing = getClothingAtRow(selectedIndex);

                try {
                    updatedCloset.removeClothingFromCloset(selectedClothing);
                    updateClothingTable(updatedCloset);
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Success: Item Deleted");
                } catch (ClothingException ex) {
                    JOptionPane.showMessageDialog(ClothingMenu.this, "Error: Item Not Deleted",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: No Item Selected",
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

    // creates text fields
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 25));
        return textField;
    }

    // creates labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.RIGHT);
        return label;
    }

    // creates combo boxes
    private <T> JComboBox<T> createComboBox(T[] enumConstants) {
        JComboBox<T> comboBox = new JComboBox<>(enumConstants);
        comboBox.setPreferredSize(new Dimension(150, 25));
        return comboBox;
    }

    // custom cell renderer for rendering a checkbox without label text
    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        CheckBoxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setSelected((Boolean) value);
            return this;
        }
    }

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
