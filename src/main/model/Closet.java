package model;


import model.exception.ClothingException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a closet having name, items, and outfits
public class Closet implements Writable {
    private String name;
    private List<Clothing> items;
    private List<Outfit> outfits;

    // EFFECTS: constructs a closet with given name, empty items, and empty outfits
    public Closet(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.outfits = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Clothing> getItems() {
        return items;
    }

    public List<Outfit> getOutfits() {
        return outfits;
    }

    // EFFECTS: returns clothing item from closet
    public List<Clothing> getClothingsFromCloset() {
        return items;
    }

    // EFFECTS: returns outfit from closet
    public List<Outfit> getOutfitsFromCloset() {
        return outfits;
    }

    // EFFECTS: returns a list of clothing items grouped by given category
    public List<Clothing> getClothingsByCategory(ClothingCategory category) {
        List<Clothing> categoryClothings = new ArrayList<>();

        for (Clothing clothing : items) {
            if (clothing.getCategory() == category) {
                categoryClothings.add(clothing);
            }
        }

        return categoryClothings;
    }

    // EFFECTS: returns a list of clothing items grouped by given color
    public List<Clothing> getClothingByColor(Color color) {
        List<Clothing> colorClothings = new ArrayList<>();

        for (Clothing clothing : items) {
            if (clothing.getColor() == color) {
                colorClothings.add(clothing);
            }
        }

        return colorClothings;
    }

    // EFFECTS: returns the outfit with the given name, or null if not found
    public Outfit findOutfitByName(String outfitName) {
        return outfits.stream()
                .filter(outfit -> outfit.getName().equalsIgnoreCase(outfitName))
                .findFirst()
                .orElse(null);
    }

    // MODIFIES: this
    // EFFECTS: adds clothing item to closet if there is no duplicate in the list, exception thrown otherwise
    public void addClothingToCloset(Clothing clothing) throws ClothingException {
        if (!items.contains(clothing)) {
            items.add(clothing);
        } else {
            throw new ClothingException("duplicate clothing item");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes clothing item from closet if item exists in the list, exception thrown otherwise
    public void removeClothingFromCloset(Clothing clothing) throws ClothingException {
        if (items.contains(clothing)) {
            items.remove(clothing);
        } else {
            throw new ClothingException("item not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds outfit to closet if there's no same outfit, exception thrown otherwise
    public void addOutfitToCloset(Outfit outfit) throws ClothingException {
        if (!outfits.contains(outfit)) {
            outfits.add(outfit);
        } else {
            throw new ClothingException("duplicate outfit");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes outfit to closet if there's no same outfit, exception thrown otherwise
    public void removeOutfitFromCloset(Outfit outfit) throws ClothingException {
        if (outfits.contains(outfit)) {
            outfits.remove(outfit);
        } else {
            throw new ClothingException("outfit not found");
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("clothing items", clothingsToJson());
        json.put("outfits", outfitsToJson());
        return json;
    }

    // EFFECTS: returns clothings in this closet as a JSON array
    private JSONArray clothingsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Clothing c : items) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns outfits in this closet as a JSON array
    private JSONArray outfitsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Outfit o : outfits) {
            jsonArray.put(o.toJson());
        }

        return jsonArray;
    }
}
