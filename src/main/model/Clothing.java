package model;

// Represents a clothing having an item, category, color, isClean
public class Clothing {
    private final String item;                 // clothing item
    private final ClothingCategory category;   // clothing category
    private final Color color;                 // clothing color
    private boolean isClean;                   // clothing cleanliness


    /*
     * REQUIRES: name has non-zero length
     * EFFECTS : item on clothing is set to clothingItem;
     *           category on clothing is set to clothingCategory;
     *           color on clothing is set to clothingColor;
     *           isClean on clothing is set to True, assuming
     *           new clothing items are clean.
     */
    public Clothing(String clothingItem, ClothingCategory clothingCategory, Color clothingColor, boolean isClean) {
        this.item = clothingItem;
        this.category = clothingCategory;
        this.color = clothingColor;
        this.isClean = isClean;
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
    // EFFECTS: sets cleanliness status of the clothing item to true
    public void setClean() {
        isClean = true;
    }

    // MODIFIES: this
    // EFFECTS: sets cleanliness status of the clothing item to false
    public void setDirty() {
        isClean = false;
    }

}
