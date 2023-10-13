package ui;

import model.Clothing;
import model.ClothingCategory;
import model.Day;
import model.DayPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// DesktopDresser application
public class Dresser {
    private Scanner scanner;
    private List<Clothing> closet;
    private List<DayPlan> weeklyPlans;

    // REQUIRES: non-empty closet
    // EFFECTS: open closet
    public Dresser() {
        this.scanner = new Scanner(System.in);
        this.closet = new ArrayList<>();
        this.weeklyPlans = new ArrayList<>();
        initialWeeklyPlans();
        openCloset();
    }

    // EFFECTS: set initial weekly plans
    public void initialWeeklyPlans() {
        for (Day day : Day.values()) {
            DayPlan dayPlan = new DayPlan(day, closet);
            weeklyPlans.add(dayPlan);
        }
    }

    // EFFECTS: open closet by showing the closet menu that users can choose
    public void openCloset() {
        boolean closetMenu = true;
        while (closetMenu) {
            displayClosetMenu();
            int choice = getUserChoice();
            closetMenu = menuChoice(choice);
        }
    }

    // EFFECTS: display closet application menu
    public void displayClosetMenu() {
        System.out.println("Closet Menu");
        System.out.println("1. Add New Clothing Item");
        System.out.println("2. Check Clothing Item");
        System.out.println("3. Delete Clothing Item");
        System.out.println("4. Back to Main Menu");
        System.out.println("What do you want to do?: ");
    }

    // EFFECTS: get user choice for application menu
    private int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    // EFFECTS: guide the choice from the menu
    private boolean menuChoice(int choice) {
        switch (choice) {
            case 1:
                addClothingToCloset();
                return true;
            case 2:
                checkClothingFromCloset();
                return true;
            case 3:
                deleteClothingFromCloset();
                return true;
            case 4:
                System.out.println("Returning to the main menu...");
                return true;
            default:
                return true;
        }
    }

    // EFFECTS: adding given new clothing item to the closet
    private void addClothingToCloset() {
        System.out.println("Enter what's a new clothing item: ");
        String name = scanner.nextLine();
        System.out.println("Enter the clothing's category from [TOP BOT DRESS OUTER ACC SHOES]: ");
        ClothingCategory category = ClothingCategory.valueOf(scanner.nextLine().toUpperCase());
    }

    // EFFECTS: checking given clothing item from the closet if it's available
    private void checkClothingFromCloset() {
        System.out.println("Check clothing item");
        for (Clothing item : closet) {
            System.out.println(item.getItem() + " - Category: " + item.getCategory());
        }
    }

    // EFFECTS: deleting given item from the closet
    private void deleteClothingFromCloset() {
        System.out.println("Enter clothing item you would like to delete");
        String removeName = scanner.nextLine();
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

}








