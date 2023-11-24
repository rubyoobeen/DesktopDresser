package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a clothing having an item, category, color, isClean, timesUsed
public class Clothing implements Writable {
    private static final int LIMIT = 5;
    private String item;
    private ClothingCategory category;
    private Color color;
    private boolean isClean;
    private int timesWorn;

    // EFFECTS: constructs a clothing with item, category, color, and
    public Clothing(String clothingItem, ClothingCategory clothingCategory, Color clothingColor) {
        this.item = clothingItem;
        this.category = clothingCategory;
        this.color = clothingColor;
        this.isClean = true;
        this.timesWorn = 0;
    }

    public String getItem() {
        return item;
    }

    public ClothingCategory getCategory() {
        return category;
    }

    public Color getColor() {
        return color;
    }

    public boolean isClean() {
        return isClean;
    }

    public int getTimesWorn() {
        return timesWorn;
    }

    // MODIFIES: this
    // EFFECTS: stimulates using the clothing and updates its cleanliness
    public void worn() {
        if (timesWorn >= LIMIT) {
            System.out.println("item has to be washed");
            isClean = false;
        } else {
            System.out.println("item: [" + item + "] worn");
            timesWorn++;
            if (timesWorn == 5) {
                isClean = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: simulates washing the clothing and sets it to clean, true
    public void wash() {
        if (!isClean) {
            isClean = true;
            System.out.println("item : [" + item + "] is washed");
        } else {
            System.out.println("item is already clean");
        }
    }

    // EFFECTS: returns string representation of this clothing
    public String toString() {
        return "item: " + item + " | category: " + category + " | color: " + color + " | clean?: " + isClean;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", item);
        json.put("category", category);
        json.put("color", color);
        json.put("clean?", isClean);
        return json;
    }
}
