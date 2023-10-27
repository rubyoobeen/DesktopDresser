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
        boolean hasDress = false;
        boolean hasAcc = false;
        boolean hasOuter = false;
        boolean hasShoes = false;

        for (Clothing item : clothingItems) {
            switch (item.getCategory()) {
                case TOP:
                    hasTop = true;
                    break;
                case BOT:
                    hasBot = true;
                    break;
                case DRESS:
                    hasDress = true;
                    break;
                case ACC:
                    hasAcc = true;
                    break;
                case OUTER:
                    hasOuter = true;
                    break;
                case SHOES:
                    hasShoes = true;
                    break;
            }

        }

        // top has to be always paired with a bottom, dress can be
        boolean pairClothing = (hasTop && hasBot) || hasDress;

        // always require shoes;
        // dress, acc, outer
        return pairClothing && hasShoes;
    }
    
}
