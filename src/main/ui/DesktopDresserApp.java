package ui;

import model.Closet;
import model.EventLog;
import model.exception.ClothingException;
import model.exception.LogException;
import persistence.JsonReader;

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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setPreferredSize(new Dimension(100, 50));
        buttonPanel.add(new JButton(new LoadClosetAction()));
        buttonPanel.add(new JButton(new NewClosetAction()));
        return buttonPanel;
    }

    // loads closet from JSON_STORE and initialize main menu
    private class LoadClosetAction extends AbstractAction {

        LoadClosetAction() {
            super("Load Closet");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                JsonReader jsonReader = new JsonReader(JSON_STORE);
                Closet loadedCLoset = jsonReader.read();

                if (loadedCLoset != null) {
                    setupMenu.setVisible(false);
                    mainCloset = loadedCLoset;
                    initialMainMenu();
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

        NewClosetAction() {
            super("New Closet");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String newClosetName = JOptionPane.showInputDialog("Enter Name:");

            if (newClosetName != null && !newClosetName.trim().isEmpty() && !newClosetName.equals(" ")) {
                mainCloset = new Closet(newClosetName);

                if (setupMenu.isVisible()) {
                    setupMenu.setVisible(false);
                }

                initialMainMenu();

            } else {
                JOptionPane.showMessageDialog(null, "Invalid name. Please enter valid name.");
            }
        }
    }

    // creates main menu
    private JPanel createMainMenu() {
        JPanel mainMenuPanel = new JPanel(new GridLayout(6, 1));
        mainMenuPanel.add(new JButton(new ClothingAction()));
        mainMenuPanel.add(new JButton(new OutfitAction()));
        mainMenuPanel.add(new JButton(new FilePrintLogAction()));
        mainMenuPanel.add(new JButton(new ScreenPrintLogAction()));
        mainMenuPanel.add(new JButton(new ClearLogAction()));
        mainMenuPanel.add(new JButton(new ExitAction()));

        return mainMenuPanel;
    }

    // initialize main menu
    private void initialMainMenu() {
        mainMenu = new JInternalFrame("MAIN");
        mainMenu.setSize(150, 500);
        mainMenu.setLocation(0, 150);

        JPanel mainMenuPanel = createMainMenu();

        mainMenu.add(mainMenuPanel, BorderLayout.CENTER);
        mainMenu.setVisible(true);

        desktop.add(mainMenu, BorderLayout.WEST);
    }

    // opens a new window of clothing menu
    private class ClothingAction extends AbstractAction {

        ClothingAction() {
            super("Clothings");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ClothingMenu clothingMenu = new ClothingMenu(DesktopDresserApp.this, mainCloset);
            clothingMenu.setVisible(true);
            desktop.add(clothingMenu);
        }
    }

    // opens a new window of outfit menu
    private class OutfitAction extends AbstractAction {

        OutfitAction() {
            super("Outfits");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            OutfitMenu outfitMenu = new OutfitMenu(DesktopDresserApp.this, mainCloset);
            outfitMenu.setVisible(true);
            desktop.add(outfitMenu);
        }
    }

    // clears all log events occur in application
    private class ClearLogAction extends AbstractAction {

        ClearLogAction() {
            super("Clear Log");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            EventLog.getInstance().clear();
            JOptionPane.showMessageDialog(DesktopDresserApp.this, "Event logs cleared",
                    "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // prints all log events occur in application to file
    private class FilePrintLogAction extends AbstractAction {

        FilePrintLogAction() {
            super("Logs to File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                LogPrinter lp = new FilePrinter();
                lp.printLog(EventLog.getInstance());
            } catch (LogException ex) {
                JOptionPane.showMessageDialog(null, "Error: cannot print to file",
                        "FILE ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // prints all log events occur in application to screen
    private class ScreenPrintLogAction extends AbstractAction {

        ScreenPrintLogAction() {
            super("Logs to Screen");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            LogPrinter lp;
            try {
                lp = new ScreenPrinter(DesktopDresserApp.this);
                desktop.add((ScreenPrinter) lp);
                lp.printLog(EventLog.getInstance());
            } catch (LogException ex) {
                JOptionPane.showMessageDialog(DesktopDresserApp.this,
                        "Error: cannot print to screen", "PRINT ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // asks user if they want to exit application, and exits or not
    private class ExitAction extends AbstractAction {

        ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int result = JOptionPane.showConfirmDialog(DesktopDresserApp.this,
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