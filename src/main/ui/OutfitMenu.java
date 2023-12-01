package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Color;

import javax.swing.*;
import java.awt.*;

public class OutfitMenu extends JFrame {
    private Closet closet;
    private JList<Clothing> clothingList;
    private DefaultListModel<Clothing> clothingListModel;
    private JComboBox<ClothingCategory> categoryComboBox;
    private JComboBox<Color> colorComboBox;
    private JTextField textField;
    private JButton addButton;
    private JButton deleteButton;

    // Constructor that takes a Closet object and initialize the frame
    public OutfitMenu(Closet closet) {
        this.closet = closet;
        initialize();
    }

    // initializes clothing tab
    private void initialize() {
        setTitle("OUTFIT");
        setSize(800,600);

    }

}
