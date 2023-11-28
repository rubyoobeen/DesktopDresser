package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Color;
import model.exception.ClothingException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Represents Clothing application
public class ClothingUI {
    private Scanner input;
    private Closet closet;
    private ClosetUI closetUI;

    // EFFECTS: constructor that takes input and closet as parameters
    public ClothingUI(Scanner input, Closet closet) {
        this.input = input;
        this.closet = closet;
    }

    // EFFECTS: displays clothing menu
    public void showMenu() {
        System.out.println("Clothing Menu: ");
        System.out.println("\t1. Add Clothing Item");
        System.out.println("\t2. Remove Clothing Item");
        System.out.println("\t3. View Clothing Items");
        System.out.println("\t4. View Clothing Items by Categories");
        System.out.println("\t5. View Clothing Items by Colors");
        System.out.println("\t6. Back to Main Menu");
    }

    // EFFECTS: processes user input
    public void displayMenu() {
        int choice;

        do {
            showMenu();
            choice = getValidChoice(1, 6);

            if (choice == 1) {
                addClothing();
            } else if (choice == 2) {
                deleteClothing();
            } else if (choice == 3) {
                viewClothings(closet);
            } else if (choice == 4) {
                viewClothingsByCategories();
            } else if (choice == 5) {
                viewClothingsByColors();
            } else if (choice == 6) {
                System.out.println("Back to Main Menu");
                closetUI.showMainMenu();
            } else {
                System.out.println("Invalid Input");
            }
        } while (choice != 6);
    }

    // MODIFIES: closet
    // EFFECTS: adds new clothing item to the closet
    public void addClothing() {
        System.out.println("Enter Clothing Item: ");
        String itemName = input.nextLine();

        if (itemName.trim().isEmpty()) {
            System.out.println("Error: Empty Item Name");
            return;
        }

        ClothingCategory category = whatCategory();
        Color color = whatColor();

        try {
            closet.addClothingToCloset(new Clothing(itemName, category, color));
            System.out.println("Success: Item [" + itemName + "] Added");
        } catch (ClothingException ex) {
            System.out.println("Error: Cannot Add Item [" + itemName + "]");
        }
    }

    // EFFECTS: delete clothing item from the closet
    private void deleteClothing() {
        System.out.println("Enter Clothing Item: ");
        String itemName = input.nextLine();

        ClothingCategory category = whatCategory();
        Color color = whatColor();

        try {
            closet.removeClothingFromCloset(new Clothing(itemName, category, color));
            System.out.println("Success: Item [" + itemName + "] Removed");
        } catch (ClothingException ex) {
            System.out.println("Error: Item [" + itemName + "] Doesn't Exist");
        }
    }

    // EFFECTS: prints the list of clothing items in the closet
    public void viewClothings(Closet closet) {
        System.out.println("Clothing Items in Your Closet: ");

        List<Clothing> clothingItems = closet.getClothingsFromCloset();

        if (clothingItems.isEmpty()) {
            System.out.println("Your Closet is Empty");
        } else {
            int index = 1;
            for (Clothing c : clothingItems) {
                System.out.println(index + ". " + c.toString());
                index++;
            }
        }
        System.out.println();
    }

    // EFFECTS: prints the list of clothing items in the closet by categories
    public void viewClothingsByCategories() {
        for (ClothingCategory category : ClothingCategory.values()) {
            System.out.println("Clothing Items in Category [" + category + "]: ");

            List<Clothing> categoryItems = closet.getClothingsByCategory(category);

            if (categoryItems.isEmpty()) {
                System.out.println("No Items in Category [" + category + "]");
            } else {
                int index = 1;
                for (Clothing c : categoryItems) {
                    System.out.println(index + ". " + c.toString());
                    index++;
                }
            }
            System.out.println();
        }
    }

    // EFFECTS: prints the list of clothing items in the closet by colors
    public void viewClothingsByColors() {
        for (Color color : Color.values()) {
            System.out.println("Clothing Items in Color [" + color + "]: ");

            List<Clothing> categoryItems = closet.getClothingByColor(color);

            if (categoryItems.isEmpty()) {
                System.out.println("No Items in Color [" + color + "]");
            } else {
                int index = 1;
                for (Clothing c : categoryItems) {
                    System.out.println(index + ". " + c.toString());
                    index++;
                }
            }
            System.out.println();
        }
    }

    // EFFECTS: prompts user to choose clothing category and return it
    private ClothingCategory whatCategory() {
        System.out.println("Choose Category: ");

        int label = 1;
        for (ClothingCategory c : ClothingCategory.values()) {
            System.out.println(label + ": " + c);
            label++;
        }

        int choice = getValidChoice(1,ClothingCategory.values().length) - 1;
        return ClothingCategory.values()[choice];
    }

    // EFFECTS: prompts user to choose clothing color and return it
    private Color whatColor() {
        System.out.println("Choose Color: ");

        int label = 1;
        for (Color c : Color.values()) {
            System.out.println(label + ": " + c);
            label++;
        }

        int choice = getValidChoice(1, Color.values().length) - 1;
        return Color.values()[choice];
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
