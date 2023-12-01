package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Color;
import model.exception.ClothingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Represents application's main menu window frame
public class ClothingMenu extends JFrame {
    private Closet closet;
    private JList<Clothing> clothingList;
    private DefaultListModel<Clothing> clothingListModel;
    private JComboBox<ClothingCategory> categoryComboBox;
    private JComboBox<Color> colorComboBox;
    private JTextField textField;
    private JButton addButton;
    private JButton deleteButton;

    // Constructor that takes a Closet object and initialize the frame
    public ClothingMenu(Closet closet) {
        this.closet = closet;
        initialize();
    }

    // initializes clothing tab
    private void initialize() {
        setTitle("CLOTHING");
        setSize(800,600);

        createClothingList();
        setLayout(new BorderLayout());
        add(new JScrollPane(clothingList), BorderLayout.CENTER);
        createWestPanel();
        add(createWestPanel(), BorderLayout.WEST);
    }

    // creates clothing list
    private void createClothingList() {
        clothingListModel = new DefaultListModel<>();
        clothingList = new JList<>(clothingListModel);

        updateClothingList();

        clothingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clothingList.addMouseListener(new ClothingListMouseAdapter(clothingList));
        add(new JScrollPane(clothingList), BorderLayout.CENTER);
    }

    // creates panel for WEST of frame
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        textField = createTextField();
        addComponentWithLabel(westPanel, "Enter Item:", textField);
        categoryComboBox = createComboBox(ClothingCategory.values());
        addComponentWithLabel(westPanel, "Category:", categoryComboBox);
        colorComboBox = createComboBox(Color.values());
        addComponentWithLabel(westPanel, "Color:", categoryComboBox);

        addButton = createButton("Add", this::addClothing);
        westPanel.add(addButton);
        deleteButton = createButton("Delete", this::deleteClothing);
        westPanel.add(deleteButton);

        return westPanel;
    }

    // updates clothing to the list
    private void updateClothingList() {
        List<Clothing> closetItems = closet.getClothingsFromCloset();

        for (Clothing clothing : closetItems) {
            clothingListModel.addElement(clothing);
        }
    }

    // adds new clothing to closet and updates the clothing list
    private void addClothing(ActionEvent evt) {
        try {
            String itemName = textField.getText();
            ClothingCategory selectedCategory = (ClothingCategory) categoryComboBox.getSelectedItem();
            Color selectedColor = (Color) colorComboBox.getSelectedItem();

            Clothing newClothing = new Clothing(itemName, selectedCategory, selectedColor);
            closet.addClothingToCloset(newClothing);

            updateClothingList();

            JOptionPane.showMessageDialog(this, "Success: Item Added");
        } catch (ClothingException ex) {
            JOptionPane.showMessageDialog(this, "Error: Item Not Added",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // deletes clothing from the closet and updates the clothing list
    private void deleteClothing(ActionEvent evt) {
        int selectedIndex = clothingList.getSelectedIndex();

        if (selectedIndex != -1) {
            Clothing selectedClothing = clothingListModel.getElementAt(selectedIndex);

            try {
                closet.removeClothingFromCloset(selectedClothing);
                updateClothingList();
                JOptionPane.showMessageDialog(this, "Success: Item Deleted");
            } catch (ClothingException ex) {
                JOptionPane.showMessageDialog(this, "Error: Item Not Deleted",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: No Item Selected",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
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

    // creates buttons
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(80, 25));
        return button;
    }

    // add component with label
    private void addComponentWithLabel(JPanel panel, String labelText, JComponent component) {
        panel.add(createLabel(labelText));
        panel.add(component);
    }

    private class ClothingListMouseAdapter extends MouseAdapter {
        private JList<Clothing> clothingJList;

        public ClothingListMouseAdapter(JList<Clothing> list) {
            this.clothingJList = list;
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 1) {
                int index = clothingList.locationToIndex(evt.getPoint());
                clothingList.setSelectedIndex(index);
            }
        }
    }

    // Represents actions to be taken when user clicks desktop to switch focus
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            ClothingMenu.this.requestFocusInWindow();
        }
    }
}
