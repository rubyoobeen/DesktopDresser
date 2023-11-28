package ui;

import model.Closet;
import model.exception.ClothingException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

// Represents DesktopDresser application
public class ClosetUI {
    private static final String JSON_STORE = "./data/closet.json";
    private Scanner input;
    private Closet closet;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ClothingUI clothingUI;
    private OutfitUI outfitUI;

    // EFFECTS: opens closet application
    public ClosetUI() {
        input = new Scanner(System.in);
        closet = null;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runCloset();
    }

    // EFFECTS: runs application by opening closet and displaying menu
    public void runCloset() {
        openDresser();
        clothingUI = new ClothingUI(input, closet);
        outfitUI = new OutfitUI(input, closet);
        showMainMenu();
    }

    // MODIFIES: this
    // EFFECTS: load or start new closet
    private void openDresser() {
        System.out.println("Welcome to My Closet App");
        System.out.println("1. Load saved Closet");
        System.out.println("2. Start new Closet");
        int choice = getValidChoice(1,2);

        if (choice == 1) {
            loadDresser();
        } else if (choice == 2) {
            startNewDresser();
        } else {
            System.out.println("Error: Invalid Input");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads closet data from file, exception thrown otherwise
    private void loadDresser() {
        jsonReader = new JsonReader(JSON_STORE);

        try {
            closet = jsonReader.read();
            System.out.println("Loaded " + closet.getName() + " from " + JSON_STORE);
        } catch (IOException | ClothingException ex) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            ex.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: starts a new closet for new data
    private void startNewDresser() {
        System.out.println("Enter name for new Closet: ");
        String closetName = input.nextLine();

        closet = new Closet(closetName);
        System.out.println("Started a new Closet named: " + closetName);
    }

    // EFFECTS: displays main menu options
    public void showMainMenu() {
        System.out.println("Main Menu: ");
        System.out.println("\t1. Clothing Menu");
        System.out.println("\t2. Outfit Menu");
        System.out.println("\t3. Save Closet");
        System.out.println("\t4. Quit");
        displayMenu();
    }

    // EFFECTS: processes user input
    private void displayMenu() {
        int choice = getValidChoice(1, 4);

        if (choice == 1) {
            clothingUI.displayMenu();
        } else if (choice == 2) {
            outfitUI.showMenu();
        } else if (choice == 3) {
            saveCloset();
        } else {
            System.out.println("Exit Closet");
        }
    }

    // EFFECTS: saves closet data to file
    private void saveCloset() {
        try {
            jsonWriter.open();
            jsonWriter.write(closet);
            jsonWriter.close();
            System.out.println("Saved Closet to: " + JSON_STORE);
        } catch (IOException ex) {
            System.out.println("Error: Cannot Save Closet to: " + JSON_STORE);
            ex.printStackTrace();
        }
    }

    // EFFECTS: returns user's input if it's a valid input
    private int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = input.nextInt();
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    throw new InputMismatchException("Error: Invalid Input");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: Invalid Input");
            } finally {
                input.nextLine();
            }
        }
    }
}
