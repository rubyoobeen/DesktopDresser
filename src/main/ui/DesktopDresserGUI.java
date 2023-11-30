//package ui;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//// represents DesktopDresser application's main window frame
//public class DesktopDresserGUI extends JFrame {
//    private static final String JSON_STORE = "./data/closet.json";
//    private JDesktopPane mainFrame;
//    private JInternalFrame closetMenu;
//
//    // constructor sets up button
//    public DesktopDresserGUI() {
//        mainFrame = new JDesktopPane();
//        mainFrame.addMouseListener(new DesktopFocusAction());
//
//        setContentPane(mainFrame);
//        setTitle("DESKTOP DRESSER");
//        setSize(800,600);
//
//        addMenu();
//        addButtonPanel();
//
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setVisible(true);
//    }
//
//    // adds a menu bar
//    private void addMenu() {
//        JMenuBar menuBar = new JMenuBar();
//
//        JMenu fileMenu = new JMenu("File");
//        fileMenu.add(new JMenu("Load file"));
//        fileMenu.add(new JMenu("Save file"));
//        menuBar.add(fileMenu);
//
//        JMenu editMenu = new JMenu("Edit");
//        editMenu.add(new JMenu("Add Clothing"));
//        editMenu.add(new JMenu("Delete Clothing"));
//        editMenu.add(new JMenu("Make Outfit"));
//        editMenu.add(new JMenu("Delete Outfit"));
//        menuBar.add(editMenu);
//
//        JMenu viewMenu = new JMenu("View");
//        viewMenu.add(new JMenu("View Clothings"));
//        viewMenu.add(new JMenu("View Outfits"));
//        menuBar.add(viewMenu);
//
//        setJMenuBar(menuBar);
//    }
//
//    // adds a button panel
//    private void addButtonPanel() {
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(1, 4));
//
//        buttonPanel.add(new JButton(new ClothingMenuAction()));
//        buttonPanel.add(new JButton(new OutfitMenuAction()));
//        buttonPanel.add(new JButton(new SaveAction()));
//        buttonPanel.add(new JButton(new QuitAction()));
//
//        closetMenu.add(buttonPanel, BorderLayout.CENTER);
//    }
//
//    // represents action to be taken when user wants to open clothing menu
//    private class ClothingMenuAction extends AbstractAction {
//        private JInternalFrame clothingMenu;
//
//        ClothingMenuAction() {
//            super("Clothing Menu");
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            if (clothingMenu == nul || !clothingMenu.isV
//        }
//    }
//
//
//    // represents action to be taken when user clicks desktop to switch focus
//    public class DesktopFocusAction extends MouseAdapter {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            DesktopDresserGUI.this.requestFocusInWindow();
//        }
//    }
//
//    // starts DesktopDresser application
//    public static void main(String[] args) {
//        new DesktopDresserGUI();
//    }
//}
