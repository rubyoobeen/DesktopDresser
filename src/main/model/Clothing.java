package model;

import model.exception.ClothingException;
import org.json.JSONObject;
import persistence.Writable;

// Represents a clothing having an item, category, color, isClean, and timesUsed
public class Clothing implements Writable {
    private static final int LIMIT = 5;
    private String item;
    private ClothingCategory category;
    private Color color;
    private boolean isClean;
    private int timesUsed;

    // EFFECTS: constructs a clothing with given item, given category, given color, isClean, and timesUsed
    public Clothing(String clothingItem, ClothingCategory clothingCategory, Color clothingColor) {
        this.item = clothingItem;
        this.category = clothingCategory;
        this.color = clothingColor;
        this.isClean = true;
        this.timesUsed = 0;
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

    public int getTimesUsed() {
        return timesUsed;
    }

    // MODIFIES: this
    // EFFECTS: set isClean to dirty = false, throw exception otherwise
    public void setDirty() throws ClothingException {
        if (isClean) {
            isClean = false;
        } else {
            throw new ClothingException("item is already dirty");
        }
    }

    // MODIFIES: this
    // EFFECTS: use clothing and updates its timesUsed, throw exception otherwise
    public void use() throws ClothingException {
        if (timesUsed < LIMIT) {
            timesUsed++;
        } else {
            throw new ClothingException("item was used too many times");
        }
    }

    // MODIFIES: this
    // EFFECTS: set isClean to clean = true, throw exception otherwise
    public void setClean() throws ClothingException {
        if (!isClean) {
            isClean = true;
        } else {
            throw new ClothingException("item is already clean");
        }
    }

    // EFFECTS: returns string representation of this clothing, throw exception otherwise
    public String toString() {
        return "[" + category + "] " + color + " " + item + " | washed: " + isClean + " | [" + timesUsed + "] used";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Clothing clothing = (Clothing) obj;

        return item.equals(clothing.item) && category == clothing.category && color == clothing.color;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("item", item);
        json.put("category", category);
        json.put("color", color);
        json.put("clean?", isClean);
        json.put("times worn", timesUsed);
        return json;
    }
}
