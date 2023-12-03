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
import java.util.List;

// Represents application's main menu window frame
public class ClothingMenu extends JInternalFrame {
    private static final String JSON_STORE = "./data/closet.json";
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

        add(createNorthPanel(), BorderLayout.NORTH);
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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        panel.add(new JButton(new AddClothingAction()));
        panel.add(new JButton(new DeleteClothingAction()));
        panel.add(new JButton(new SaveExitAction()));

        return panel;
    }

    // creates NORTH panel of the frame
    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.add(new JButton(new ViewAll()));
        panel.add(new JButton(new ViewTop()));
        panel.add(new JButton(new ViewBot()));
        panel.add(new JButton(new ViewDress()));
        panel.add(new JButton(new ViewOuter()));
        panel.add(new JButton(new ViewAcc()));
        panel.add(new JButton(new ViewShoes()));

        return panel;
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
                ClothingCategory selectedCategory = (ClothingCategory) categoryComboBox.getSelectedItem();
                Color selectedColor = (Color) colorComboBox.getSelectedItem();

                Clothing newClothing = new Clothing(itemName, selectedCategory, selectedColor);
                closet.addClothingToCloset(newClothing);

                updateClothingTable(closet);

                JOptionPane.showMessageDialog(ClothingMenu.this, "Success: Item Added");
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(ClothingMenu.this, "Error: Item Not Added",
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
                JOptionPane.showMessageDialog(null,"Closet Saved to: " + JSON_STORE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed Saving to " + JSON_STORE,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            ClothingMenu.this.dispose();
        }
    }

    // view all items in the list in added order
    private class ViewAll extends AbstractAction {

        ViewAll() {
            super("ALL");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            updateClothingTable(closet);
        }
    }

    // makes new list of clothing items in TOP category and display on table
    private class ViewTop extends AbstractAction {
        private List<Clothing> topList;

        ViewTop() {
            super("TOP");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            topList = closet.getClothingsByCategory(ClothingCategory.TOP);
            updateDifferentTable(topList);
        }
    }

    // makes new list of clothing items in BOT category and display on table
    private class ViewBot extends AbstractAction {
        private List<Clothing> botList;

        ViewBot() {
            super("BOT");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            botList = closet.getClothingsByCategory(ClothingCategory.BOT);
            updateDifferentTable(botList);
        }
    }

    // makes new list of clothing items in DRESS category and display on table
    private class ViewDress extends AbstractAction {
        private List<Clothing> dressList;

        ViewDress() {
            super("DRESS");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            dressList = closet.getClothingsByCategory(ClothingCategory.DRESS);
            updateDifferentTable(dressList);
        }
    }

    // makes new list of clothing items in OUTER category and display on table
    private class ViewOuter extends AbstractAction {
        private List<Clothing> outerList;

        ViewOuter() {
            super("OUTER");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            outerList = closet.getClothingsByCategory(ClothingCategory.OUTER);
            updateDifferentTable(outerList);
        }
    }

    // makes new list of clothing items in ACC category and display on table
    private class ViewAcc extends AbstractAction {
        private List<Clothing> accList;

        ViewAcc() {
            super("ACC");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            accList = closet.getClothingsByCategory(ClothingCategory.ACC);
            updateDifferentTable(accList);
        }
    }

    // makes new list of clothing items in SHOES category and display on table
    private class ViewShoes extends AbstractAction {
        private List<Clothing> shoesList;

        ViewShoes() {
            super("SHOES");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            shoesList = closet.getClothingsByCategory(ClothingCategory.SHOES);
            updateDifferentTable(shoesList);
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
