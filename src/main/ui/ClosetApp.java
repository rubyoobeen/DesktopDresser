package ui;

import model.*;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

// Represents the DesktopDresser application
public class ClosetApp {
    private static final String JSON_STORE = "./data/closet.json";
    private Scanner input;
    private Closet closet;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs closet and runs application
    public ClosetApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        closet = new Closet("my closet");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        openCloset();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void openCloset() {
        boolean run = true;
        String choice = null;
        input = new Scanner(System.in);

        while (run) {
            showMenu();
            choice = input.next();
            choice = choice.toLowerCase();

            if (choice.equals("q")) {
                run = false;
            } else {
                processChoice(choice);
            }
        }

        System.out.println("exit");
    }

    // EFFECTS: display closet application menu for user
    public void showMenu() {
        System.out.println("menu:");
        System.out.println("a. add clothing item");
        System.out.println("d. delete clothing item");
        System.out.println("b. build outfit");
        System.out.println("l. load closet");
        System.out.println("s. save closet");
        System.out.println("q. quit");
    }

    // MODIFIES: this
    // EFFECTS: process user choice
    private void processChoice(String choice) {
        if (choice.equals("a")) {
            addClothing();
        } else if (choice.equals("d")) {
            deleteFromCloset();
        } else if (choice.equals("b")) {
            buildOutfit();
        } else if (choice.equals("l")) {
            loadCloset();
        } else if (choice.equals("s")) {
            saveCloset();
        } else {
            System.out.println("invalid choice");
        }
    }

    // EFFECTS: adding given new clothing item to the closet
    private void addClothing() {
        System.out.println("enter clothing item to add: ");
        input.nextLine();
        String name = input.nextLine();

        ClothingCategory category = whatCategory();
        Color color = whatColor();

        closet.addClothingToCloset(new Clothing(name, category, color));
    }

    // EFFECTS: prompts user to choose clothing category and return it
    private ClothingCategory whatCategory() {
        System.out.println("choose clothing category: ");

        int label = 1;
        for (ClothingCategory c : ClothingCategory.values()) {
            System.out.println(label + ": " + c);
            label++;
        }

        int select = input.nextInt();
        return ClothingCategory.values()[select - 1];
    }

    // EFFECTS: prompts user to choose clothing color and return it
    private Color whatColor() {
        System.out.println("choose clothing color: ");

        int label = 1;
        for (Color c : Color.values()) {
            System.out.println(label + ": " + c);
            label++;
        }

        int select = input.nextInt();
        return Color.values()[select - 1];
    }

    // EFFECTS: deleting given item from the closet
    private void deleteFromCloset() {
        System.out.println("enter clothing item to delete: ");
        String removeName = input.next();

        if (closet.deleteClothing(removeName)) {
            System.out.println("item removed");
        } else {
            System.out.println("item not found");
        }
    }

    // EFFECTS: prompts user to build an outfit from existing clothing items
    private void buildOutfit() {
        System.out.println("enter name of outfit: ");
        String outfitName = input.nextLine();
        Outfit newOutfit = new Outfit(outfitName);

        List<Clothing> available = closet.getClothingsFromCloset();

        while (true) {
            available.forEach(item -> System.out.println(item.getItem()));
            System.out.println("select a clothing to add (o to " + (available.size() - 1) + ")");
            int choice = input.nextInt();

            if (choice == 0 || (choice <= (available.size() - 1))) {
                break;
            }

            Clothing selected = available.remove(choice - 1);
            newOutfit.addClothingToOutFit(selected);
            System.out.println("added [" + "] to the outfit");
        }

        closet.addOutfitToCloset(newOutfit);
        System.out.println("outfit [" + outfitName + "] created and added to the closet");
    }

    // MODIFIES: this
    // EFFECTS: loads closet from file
    private void loadCloset() {
        try {
            closet = jsonReader.read();
            System.out.println("loaded " + closet.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("unable to read from file: " + JSON_STORE);
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: saves closet to file
    private void saveCloset() {
        try {
            jsonWriter.open();
            jsonWriter.write(closet);
            jsonWriter.close();
            System.out.println("saved " + closet.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("unable to write to file: " + JSON_STORE);
        }
    }
}








