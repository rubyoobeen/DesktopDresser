package ui;

import model.Closet;
import model.exception.ClothingException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

// Represents application's main window frame
public class DesktopDresserApp extends JFrame {
    private static final String JSON_STORE = "./data/closet.json";
    private Closet mainCloset;
    private JDesktopPane desktop;
    private JInternalFrame setupMenu;
    private JInternalFrame mainMenu;

    // Constructor sets up menu panel and window
    public DesktopDresserApp() {
        this.mainCloset = null;
        mainDesktopFrame();
        initialSetUpMenu();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // initialize main desktop frame
    private void mainDesktopFrame() {
        setTitle("DESKTOP DRESSER");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        desktop.setBackground(Color.pink);
        setContentPane(desktop);
    }

    // initialize setup buttons and title
    private void initialSetUpMenu() {
        setupMenu = new JInternalFrame("SETUP", false, false, false, false);
        setupMenu.setSize(350, 200);
        setupMenu.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setupMenu.add(createSetupTitle(), BorderLayout.CENTER);
        setupMenu.add(createSetupButton(), BorderLayout.SOUTH);
        setupMenu.setVisible(true);

        setupMenu.setBounds(300, 300,400, 200);

        desktop.add(setupMenu);
    }

    // creates title for setup window
    private JLabel createSetupTitle() {
        JLabel titleLabel = new JLabel("Welcome to DesktopDresser Application!");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        return titleLabel;
    }

    // creates setup buttons on button panel
    private JPanel createSetupButton() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(new JButton(new LoadClosetAction()));
        buttonPanel.add(new JButton(new NewClosetAction()));
        return buttonPanel;
    }

    // loads closet from JSON_STORE and initialize main menu
    private class LoadClosetAction extends AbstractAction {
        private Closet newCloset;

        LoadClosetAction() {
            super("Load Closet");
            this.newCloset = null;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                JsonReader jsonReader = new JsonReader(JSON_STORE);
                Closet loadedCLoset = jsonReader.read();

                if (loadedCLoset != null) {
                    setupMenu.setVisible(false);
                    newCloset = loadedCLoset;
                    initialMainMenu(newCloset);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to load closet from file " + JSON_STORE,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | ClothingException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot load closet from file " + JSON_STORE,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // makes new closet model
    private class NewClosetAction extends AbstractAction {
        private Closet closet;

        NewClosetAction() {
            super("New Closet");
            this.closet = null;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String newClosetName = JOptionPane.showInputDialog(this, "Enter Name of New Closet:");

            if (newClosetName != null && !newClosetName.trim().isEmpty() && !newClosetName.equals(" ")) {
                Closet newCloset = new Closet(newClosetName);

                if (setupMenu.isVisible()) {
                    setupMenu.setVisible(false);
                }
                initialMainMenu(closet);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid name. Please enter valid name.");
            }
        }
    }

    // creates main menu
    private JPanel createMainMenu() {
        JPanel mainMenuPanel = new JPanel(new GridLayout(4, 1));
        mainMenuPanel.add(new JButton(new ClothingAction()));
        mainMenuPanel.add(new JButton(new OutfitAction()));
        mainMenuPanel.add(new JButton(new SaveAction()));
        mainMenuPanel.add(new JButton(new ExitAction()));

        return mainMenuPanel;
    }

    // initialize main menu
    private void initialMainMenu(Closet closet) {
        mainCloset = closet;
        mainMenu = new JInternalFrame("MAIN");
        mainMenu.setSize(200, 300);
        mainMenu.setLocation(400, 250);

        JPanel mainMenuPanel = createMainMenu();

        mainMenu.add(mainMenuPanel, BorderLayout.WEST);
        mainMenu.setVisible(true);

        desktop.add(mainMenu);
    }

    // opens a new window of clothing menu
    private class ClothingAction extends AbstractAction {
        private Closet closet;

        ClothingAction() {
            super("Clothings");
            this.closet = mainCloset;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ClothingMenu clothingMenu = new ClothingMenu(closet);
            clothingMenu.setVisible(true);
            desktop.add(clothingMenu, BorderLayout.EAST);
        }
    }

    // opens a new window of outfit menu
    private class OutfitAction extends AbstractAction {
        private Closet closet;

        OutfitAction() {
            super("View Outfits");
            this.closet = mainCloset;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            OutfitMenu outfitMenu = new OutfitMenu(closet);
            outfitMenu.setVisible(true);
            desktop.add(outfitMenu, BorderLayout.EAST);
        }
    }

    // saves closet to file JSON_STORE
    private class SaveAction extends AbstractAction {
        private Closet closet;

        SaveAction() {
            super("Save Closet");
            this.closet = mainCloset;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
            jsonWriter.write(closet);
            JOptionPane.showMessageDialog(null, "Closet saved successfully!",
                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // exits application
    private class ExitAction extends AbstractAction {

        ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int result = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit application?",
                    "EXIT",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // Represents actions to be taken when user clicks desktop to switch focus
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            DesktopDresserApp.this.requestFocusInWindow();
        }
    }
}