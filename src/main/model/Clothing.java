package model;

import model.exception.ClothingException;
import org.json.JSONObject;
import persistence.Writable;

// Represents a clothing having an item, category, color, isClean, and timesUsed
public class Clothing implements Writable {
    private final String item;
    private final ClothingCategory category;
    private final Color color;
    private boolean isClean;

    // EFFECTS: constructs a clothing with given item, given category, given color, and set to clean
    public Clothing(String clothingItem, ClothingCategory clothingCategory, Color clothingColor) {
        this.item = clothingItem;
        this.category = clothingCategory;
        this.color = clothingColor;
        this.isClean = true;
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

    // MODIFIES: this
    // EFFECTS: set isClean to dirty = false, throw exception otherwise
    public void setDirty() throws ClothingException {
        if (isClean) {
            isClean = false;
            EventLog.getInstance().logEvent(new Event("New item added " + this.toString()));
        } else {
            throw new ClothingException("item is already dirty");
        }
    }

    // MODIFIES: this
    // EFFECTS: set isClean to clean = true, throw exception otherwise
    public void setClean() throws ClothingException {
        if (!isClean) {
            isClean = true;
            EventLog.getInstance().logEvent(new Event("Item [" + this.toString() + "] is washed"));
        } else {
            throw new ClothingException("item is already clean");
        }
    }

    // EFFECTS: returns string representation of this clothing, throw exception otherwise
    public String toString() {
        return "[" + category + "] " + color + " " + item + " | clean?: " + isClean;
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
        return json;
    }
}
