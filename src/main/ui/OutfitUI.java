package ui;

import model.Closet;
import model.Clothing;
import model.ClothingCategory;
import model.Outfit;
import model.exception.ClothingException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Represents Outfit application
public class OutfitUI {
    private Scanner input;
    private Closet closet;
    private ClosetUI closetUI;

    // EFFECTS: constructor that takes input and closet as parameters
    public OutfitUI(Scanner input, Closet closet, ClosetUI closetUI) {
        this.input = input;
        this.closet = closet;
        this.closetUI = closetUI;
    }

    // EFFECTS: displays outfit menu
    public void showMenu() {
        System.out.println("Outfit Menu: ");
        System.out.println("\t1. Build Outfit");
        System.out.println("\t2. Delete Outfit");
        System.out.println("\t3. View Outfits");
        System.out.println("\t4. Back to Main Menu");
    }

    // EFFECTS: processes user input
    public void displayMenu() {
        int choice = 0;

        do {
            showMenu();
            choice = getValidChoice(4);

            if (choice == 1) {
                buildOutfit();
            } else if (choice == 2) {
                deleteOutfit();
            } else if (choice == 3) {
                viewOutfit();
            } else if (choice == 4) {
                System.out.println("Back to Main Menu");
                closetUI.showMainMenu();
            } else {
                System.out.println("Invalid Input");
            }
        } while (choice != 4);
    }

    // MODIFIES: this
    // EFFECTS: build new outfit with given name, exception thrown otherwise
    private void buildOutfit() {
        System.out.println("Enter Outfit Name: ");
        String outfitName = input.nextLine();

        if (!containsOutfit(outfitName)) {
            tryMakeOutfit(outfitName);
        } else {
            System.out.println("Error: Same Outfit Name Exists");
        }
    }

    // EFFECTS: tries to make a new outfit and returns true if successful, false otherwise
    private boolean tryMakeOutfit(String outfitName) {
        int outfitTypeChoice = selectedOutfitType();
        List<Clothing> selectedClothings = new ArrayList<>();

        if (outfitTypeChoice == 1) {
            return tryTopBotOutfit(outfitName, selectedClothings);
        } else {
            return tryDressOutfit(outfitName, selectedClothings);
        }
    }

    // EFFECTS: tries to make a new top-bottom outfit and returns true if successful, false otherwise
    private boolean tryTopBotOutfit(String outfitName, List<Clothing> selectedClothings) {
        return tryMakeOutfitWithCategories(outfitName, selectedClothings,
                ClothingCategory.TOP, ClothingCategory.BOT,
                ClothingCategory.OUTER, ClothingCategory.ACC, ClothingCategory.SHOES);
    }

    // EFFECTS: tries to make a new dress outfit and returns true if successful, false otherwise
    private boolean tryDressOutfit(String outfitName, List<Clothing> selectedClothings) {
        return tryMakeOutfitWithCategories(outfitName, selectedClothings,
                ClothingCategory.DRESS, ClothingCategory.OUTER, ClothingCategory.ACC, ClothingCategory.SHOES);
    }

    // EFFECTS: tries to make a new outfit with categories and returns true, false otherwise
    private boolean tryMakeOutfitWithCategories(String outfitName, List<Clothing> selectedClothings,
                                                ClothingCategory... categories) {
        selectedClothingsForCategories(selectedClothings, categories);

        if (!selectedClothings.isEmpty()) {
            makeNewOutfit(outfitName, selectedClothings);
            return true;
        } else {
            System.out.println("Error: Cannot Make Outfit - No Items Selected");
            return false;
        }
    }

    // EFFECTS: display outfit types for user to choose and get user choice
    private int selectedOutfitType() {
        System.out.println("Choose the type of Outfit: ");
        System.out.println("1. Top and Bottom");
        System.out.println("2. Dress | Full-Body");

        return getValidChoice(2);
    }

    // MODIFIES: selectedClothings
    // EFFECTS: prompts user to select clothing items from specified categories
    private void selectedClothingsForCategories(List<Clothing> selectedClothings, ClothingCategory... categories) {
        for (ClothingCategory category : categories) {
            List<Clothing> availableItems = closet.getClothingsByCategory(category);

            if (availableItems.isEmpty()) {
                System.out.println("No Items in Category [" + category + "]");
                continue;
            }

            System.out.println("Choose [" + category + "] from Option (or 0 for skip): ");
            for (int i = 1; i <= availableItems.size(); i++) {
                System.out.println(i + ": " + availableItems.get(i - 1).toString());
            }

            int choice = getValidChoice(availableItems.size());

            if (choice > 0) {
                selectedClothings.add(availableItems.get(choice - 1));
                System.out.println("Chosen [" + selectedClothings.get(selectedClothings.size() - 1).getItem() + "]");
            } else {
                System.out.println("Skipped Category [" + category + "]");
            }
        }
    }

    private void makeNewOutfit(String outfitName, List<Clothing> selectedClothings) {
        Outfit newOutfit = new Outfit(outfitName);

        for (Clothing selectedClothing : selectedClothings) {
            try {
                newOutfit.addClothingToOutfit(selectedClothing);
                System.out.println("Item [" + selectedClothing.getItem() + "] Added to Outfit [" + outfitName + "]");
            } catch (ClothingException ex) {
                System.out.println("Error: Cannot Add Item + [" + selectedClothing.getItem() + "]");
            }
        }

        try {
            closet.addOutfitToCloset(newOutfit);
            System.out.println("Outfit [" + outfitName + "] Added to Closet");
        } catch (ClothingException ex) {
            System.out.println("Error: Cannot Add Outfit to Closet");
        }
    }

    // EFFECTS: returns true if an outfit with the given name exists in the closet, false otherwise
    private boolean containsOutfit(String outfitName) {
        return closet.getOutfitsFromCloset().stream().anyMatch(outfit -> outfit.getName().equalsIgnoreCase(outfitName));
    }

    // MODIFIES: this
    // EFFECTS: deletes an outfit from the closet
    private void deleteOutfit() {
        if (!closet.getOutfitsFromCloset().isEmpty()) {
            System.out.println("Enter Name of Outfit: ");
            String outfitName = input.nextLine();

            Outfit outfitToDelete = closet.findOutfitByName(outfitName);

            if (outfitToDelete != null) {
                try {
                    closet.removeOutfitFromCloset(outfitToDelete);
                    System.out.println("Outfit [" + outfitName + "] Deleted");
                } catch (ClothingException ex) {
                    System.out.println("Error: Outfit Not Found");
                }
            } else {
                System.out.println("Error: Outfit Not Found");
            }
        } else {
            System.out.println("There are no Outfits");
        }
    }

    // EFFECTS: prints list of outfits in the closet and allows the user to view clothing items
    private void viewOutfit() {
        List<Outfit> outfits = closet.getOutfitsFromCloset();

        if (!outfits.isEmpty()) {
            System.out.println("All Outfits in Closet: ");

            for (Outfit outfit : outfits) {
                System.out.println("\nOutfit: " + outfit.getName());
                List<Clothing> clothingItems = outfit.getCollection();

                if (!clothingItems.isEmpty()) {
                    int index = 1;
                    for (Clothing clothing : clothingItems) {
                        System.out.println(index + "." + clothing.toString());
                        index++;
                    }
                }
            }
        } else {
            System.out.println("There are no Outfits");
        }
    }

    // EFFECTS: returns user's input if it's a valid input
    private int getValidChoice(int max) {
        while (true) {
            try {
                int choice = input.nextInt();
                if (choice >= 0 && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Error: Invalid Input");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: Invalid Input");
            } finally {
                input.nextLine();
            }
        }
    }
}
