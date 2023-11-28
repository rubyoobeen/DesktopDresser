//package ui;
//
//import model.*;
//
//import model.exception.ClothingException;
//import persistence.JsonReader;
//import persistence.JsonWriter;
//
//import java.util.InputMismatchException;
//import java.util.List;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import java.util.Scanner;
//
//// Represents the Closet application
//public class ClosetApp {
//    private static final String JSON_STORE = "./data/closet.json";
//    private Scanner input;
//    private Closet closet;
//    private JsonWriter jsonWriter;
//    private JsonReader jsonReader;
//
//    // EFFECTS: constructs closet and runs application
//    public ClosetApp() {
//        input = new Scanner(System.in);
//        closet = new Closet("my closet");
//        jsonWriter = new JsonWriter(JSON_STORE);
//        jsonReader = new JsonReader(JSON_STORE);
//        openCloset();
//    }
//
//    // EFFECTS: processes user input
//    public void openCloset() {
//        boolean run = true;
//        String choice = null;
//        input = new Scanner(System.in);
//
//        while (run) {
//            showMenu();
//            choice = input.next();
//            choice = choice.toLowerCase();
//
//            if (choice.equals("q")) {
//                run = false;
//            } else {
//                processChoice(choice);
//            }
//        }
//
//        System.out.println("exit");
//    }
//
//    // EFFECTS: display closet application menu for user
//    public void showMenu() {
//        System.out.println("menu:");
//        System.out.println("a. add clothing item");
//        System.out.println("d. delete clothing item");
//        System.out.println("b. build outfit");
//        System.out.println("l. load closet");
//        System.out.println("v. view closet");
//        System.out.println("s. save closet");
//        System.out.println("q. quit");
//    }
//
//    // EFFECTS: process user choice
//    private void processChoice(String choice) {
//        if (choice.equals("a")) {
//            addClothing();
//        } else if (choice.equals("d")) {
//            deleteFromCloset();
//        } else if (choice.equals("b")) {
//            buildOutfit();
//        } else if (choice.equals("l")) {
//            loadCloset();
//        } else if (choice.equals("v")) {
//            viewCloset();
//        } else if (choice.equals("s")) {
//            saveCloset();
//        } else {
//            System.out.println("invalid choice");
//        }
//    }
//
//    // EFFECTS: adding given new clothing item to the closet
//    public void addClothing() {
//        System.out.println("enter clothing item to add: ");
//        input.nextLine();
//        String name = input.nextLine();
//
//        ClothingCategory category = whatCategory();
//        Color color = whatColor();
//
//        closet.addClothingToCloset(new Clothing(name, category, color));
//    }
//
//    // EFFECTS: prompts user to choose clothing category and return it
//    public ClothingCategory whatCategory() {
//        System.out.println("choose clothing category: ");
//
//        int label = 1;
//        for (ClothingCategory c : ClothingCategory.values()) {
//            System.out.println(label + ": " + c);
//            label++;
//        }
//
//        int select = input.nextInt();
//        return ClothingCategory.values()[select - 1];
//    }
//
//    // EFFECTS: prompts user to choose clothing color and return it
//    public Color whatColor() {
//        System.out.println("choose clothing color: ");
//
//        int label = 1;
//        for (Color c : Color.values()) {
//            System.out.println(label + ": " + c);
//            label++;
//        }
//
//        int select = input.nextInt();
//        return Color.values()[select - 1];
//    }
//
//    // EFFECTS: asks user if they want to delete clothing items
//    public void deleteFromCloset() {
//        while (true) {
//            displayClothingOptions(closet.getClothingsFromCloset());
//            System.out.println("enter 1 to delete a clothing item, 0 to exit: ");
//
//            int choice = getValidChoice(1);
//
//            if (choice == 1) {
//                deleteClothingItem();
//            } else if (choice == 0) {
//                break;
//            } else {
//                System.out.println("invalid choice. please enter 1 or 0.");
//            }
//        }
//    }
//
//    // EFFECTS: deletes clothing items from closet
//    private void deleteClothingItem() {
//        System.out.println("enter the number of the clothing item to delete: ");
//        int itemNumber = getValidChoice(closet.getClothingsFromCloset().size());
//
//        if (itemNumber != 0) {
//            closet.getClothingsFromCloset().remove(itemNumber - 1);
//            System.out.println("item removed.");
//        }
//    }
//
//    // EFFECTS: prompts user to build an outfit from existing clothing items
//    public void buildOutfit() {
//        System.out.println("enter name of outfit: ");
//        String outfitName = input.next();
//        Outfit newOutfit = new Outfit(outfitName);
//
//        List<Clothing> available = closet.getClothingsFromCloset();
//
//        while (true) {
//            displayClothingOptions(available);
//
//            System.out.println("select a clothing item (1 - " + available.size() + " or 0 to finish)");
//            int choice = getValidChoice(available.size());
//
//            if (choice == 0) {
//                if (newOutfit.getCollection().isEmpty()) {
//                    System.out.println("outfit must have at least one clothing item. please add a item.");
//                } else {
//                    break;
//                }
//            }
//            Clothing selected = available.remove(choice);
//            try {
//                newOutfit.addClothingToOutfit(selected);
//            } catch (ClothingException ex) {
//                System.out.println("error adding clothing to outfit");
//            }
//        }
//        closet.addOutfitToCloset(newOutfit);
//        System.out.println("outfit [" + outfitName + "] created and added to the closet");
//    }
//
//    private void displayClothingOptions(List<Clothing> clothings) {
//        System.out.println("clothing items: ");
//        int i = 1;
//        for (Clothing c : clothings) {
//            System.out.println(i + ". " + c.toString());
//            i++;
//        }
//    }
//
//    private int getValidChoice(int max) {
//        while (true) {
//            try {
//                int choice = input.nextInt();
//
//                if (choice >= 0 && choice <= max) {
//                    return choice;
//                } else {
//                    System.out.println("invalid input. enter valid number from 0 to " + max + ": ");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("invalid input. enter valid number: ");
//                input.next();
//            }
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: loads closet from file
//    public void loadCloset() {
//        try {
//            closet = jsonReader.read();
//            System.out.println("loaded " + closet.getName() + " from " + JSON_STORE);
//        } catch (IOException ex) {
//            System.out.println("unable to read from file: " + JSON_STORE);
//            ex.printStackTrace();
//        }
//    }
//
//    public void loadClosetAndThrowException() throws ClothingException {
//        try {
//            closet = jsonReader.read();
//            System.out.println("loaded " + closet.getName() + " from " + JSON_STORE);
//        } catch (IOException ex) {
//            System.out.println("unable to read from file: " + JSON_STORE);
//            ex.printStackTrace();
//            throw new ClothingException("Error loading closet: " + ex.getMessage());
//        }
//    }
//
//    public void viewCloset() {
//        List<Clothing> clothings = closet.getClothingsFromCloset();
//        List<Outfit> outfits = closet.getOutfitsFromCloset();
//
//        System.out.println("enter 1 to view clothing items");
//        System.out.println("enter 2 to view outfits items");
//        int choice = input.nextInt();
//
//        int i = 1;
//        if (choice == 1) {
//            for (Clothing c : clothings) {
//                System.out.println(i + ". " + c);
//                i++;
//            }
//        } else if (choice == 2) {
//            for (Outfit o : outfits) {
//                System.out.println(i + ". " + o);
//                i++;
//            }
//        } else {
//            System.out.println("no items to view");
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: saves closet to file
//    public void saveCloset() {
//        try {
//            jsonWriter.open();
//            jsonWriter.write(closet);
//            jsonWriter.close();
//            System.out.println("saved " + closet.getName() + " to " + JSON_STORE);
//        } catch (FileNotFoundException e) {
//            System.out.println("unable to write to file: " + JSON_STORE);
//        }
//    }
//}








