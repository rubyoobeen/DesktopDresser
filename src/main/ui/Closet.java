package ui;

import model.Clothing;
import model.ClothingCategory;
import model.Day;
import model.DayPlan;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Represents the DesktopDresser application
public class Closet {
    private static final String JSON_STORE = "./data/closet.json";
    private Scanner input;
    private List<Clothing> closet;
    private List<DayPlan> weeklyPlans;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs closet and runs application
    public Closet() throws FileNotFoundException {
        input = new Scanner(System.in);
        this.closet = new ArrayList<>();
        this.weeklyPlans = new ArrayList<>();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        openCloset();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void openCloset() {
        boolean going = true;
        String choice = null;
        input = new Scanner(System.in);

        while (going) {
            displayMenu();
            choice = input.next();
            choice = choice.toLowerCase();

            if (choice.equals("q")) {
                going = false;
            } else {
                processChoice(choice);
            }
        }

        System.out.println("Exit");
    }

    // EFFECTS: display closet application menu for user
    public void displayMenu() {
        System.out.println("Closet Menu:");
        System.out.println("a. add clothing item to closet");
        System.out.println("d. delete clothing item from closet");
        System.out.println("c. check clothing item in closet");
        System.out.println("l. load all clothing items");
        System.out.println("s. save closet");
        System.out.println("w. build a weekly planner");
        System.out.println("q. quit");
    }

    // MODIFIES: this
    // EFFECTS: process user choice
    private void processChoice(String choice) {
        if (choice.equals("a")) {
            addClothingToCloset();
        } else if (choice.equals("d")) {
            deleteClothingFromCloset();
        } else if (choice.equals("c")) {
            checkClothingFromCloset();
        } else if (choice.equals("l")) {
            loadCloset();
        } else if (choice.equals("s")) {
            saveCloset();
        } else if (choice.equals("w")) {
            getSelectedDay();
        } else {
            System.out.println("Invalid choice");
        }
    }

    // EFFECTS: adding given new clothing item to the closet
    private void addClothingToCloset() {
        System.out.println("Enter what's a new clothing item: ");
        String name = input.nextLine();
        System.out.println("Enter the clothing's category from [TOP BOT DRESS OUTER ACC SHOES]: ");
        ClothingCategory category = ClothingCategory.valueOf(input.nextLine().toUpperCase());
    }

    // EFFECTS: deleting given item from the closet
    private void deleteClothingFromCloset() {
        System.out.println("Enter clothing item you would like to delete");
        String removeName = input.nextLine();
        boolean removed = false;

        for (int i = 0; i < closet.size(); i++) {
            Clothing item = closet.get(i);
            if (item.getItem().equalsIgnoreCase(removeName)) {
                closet.remove(i);
                removed = true;
            }
        }

        if (removed) {
            System.out.println("Clothing item removed from your closet!");
        } else {
            System.out.println("Clothing item not found in your closet...");
        }
    }

    // EFFECTS: checking given clothing item from the closet if it's available
    private void checkClothingFromCloset() {
        System.out.println("Check clothing item");
        for (Clothing item : closet) {
            System.out.println(item.getItem() + " - Category: " + item.getCategory());
        }
    }

    // MODIFIES: this
    // EFFECTS: loads closet from file
    private void loadCloset() {
        try {
            closet = jsonReader.readCloset();
            System.out.println("Closet loaded from ... " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves closet to file
    private void saveCloset() {
        try {
            jsonWriter.open();
            jsonWriter.write(closet);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private Day getSelectedDay() {
        Scanner scanner = new Scanner(System.in);
        int selectedDay = 0;

        System.out.println("Select a Day: ");
        System.out.println("1.Sunday");
        System.out.println("2. Monday");
        System.out.println("3. Tuesday");
        System.out.println("4. Wednesday");
        System.out.println("5. Thursday");
        System.out.println("6. Friday");
        System.out.println("7. Saturday");

        boolean isValidInput = false;

        while (!isValidInput) {
            if (scanner.hasNextInt()) {
                selectedDay = scanner.nextInt();
                if (selectedDay >= 1 && selectedDay <= 7) {
                    isValidInput = true;
                } else {
                    System.out.println("Invalid Input.");
                }
            }
        }

    }
}








