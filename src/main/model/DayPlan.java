package model;

import java.util.List;

public class DayPlan {
    private final Day day;
    private List<Clothing> clothingItems;

    // EFFECTS: initialize a DayPlan object with given day and
    //         empty list of clothing items.
    public DayPlan(Day day, List<Clothing> closet) {
        this.day = day;
        this.clothingItems = closet;
    }

    public Day getDay() {
        return day;
    }

    public List<Clothing> getClothingItems() {
        return clothingItems;
    }

    // MODIFIES: this
    // EFFECTS: removes specified clothing item from this DayPlan
    public void addClothingItem(Clothing clothing) {
        clothingItems.add(clothing);
    }

    // MODIFIES: this
    // EFFECTS: removes specified clothing item from this DayPlan
    public void removeClothingItem(Clothing clothing) {
        clothingItems.remove(clothing);
    }

    // REQUIRES: clothing items in this DayPlan must not be null
    // EFFECTS: returns true if the DayPlan meets the restrictions.
    public boolean meetRequirements() {
        boolean hasTop = false;
        boolean hasBot = false;
        boolean hasShoes = false;

        for (Clothing item : clothingItems) {
            if (item.getCategory() == ClothingCategory.TOP) {
                hasTop = true;
            } else if (item.getCategory() == ClothingCategory.BOT) {
                hasBot = true;
            } else if (item.getCategory() == ClothingCategory.SHOES) {
                hasShoes = true;
            }
        }

        return hasTop && hasBot && hasShoes;
    }
    
}
