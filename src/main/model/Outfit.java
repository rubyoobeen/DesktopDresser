package model;

import model.exception.ClothingException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents an outfit having a name, collection and usedCategories
public class Outfit implements Writable {
    private String name;
    private List<Clothing> collection;
    private Set<ClothingCategory> usedCategories;

    // EFFECTS: constructs an outfit with given name, empty collection and empty usedCategories
    public Outfit(String name) {
        this.name = name;
        this.collection = new ArrayList<>();
        this.usedCategories = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public List<Clothing> getCollection() {
        return collection;
    }

    public Set<ClothingCategory> getUsedCategories() {
        return usedCategories;
    }

    // MODIFIES: this
    // EFFECTS: removes clothing item from collection, throws exception otherwise
    public void removeClothingFromOutfit(Clothing clothing) throws ClothingException {
        if (collection.contains(clothing)) {
            collection.remove(clothing);
            usedCategories.remove(clothing.getCategory());
        } else {
            throw new ClothingException("clothing doesn't exist in this collection");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds clothing item to collection if it meets criteria, throw exception otherwise
    public void addClothingToOutfit(Clothing clothing) throws ClothingException {
        if (noDuplicateItem(clothing) && outfitRestrictions(clothing.getCategory())) {
            collection.add(clothing);
            usedCategories.add(clothing.getCategory());
        } else {
            throw new ClothingException("cannot add item due to restrictions");
        }
    }

    // EFFECTS: returns true if the clothing category doesn't exist in usedCategories, false otherwise
    private boolean noDuplicateItem(Clothing clothing) {
        return !usedCategories.contains(clothing.getCategory());
    }

    // EFFECTS: returns true if new item doesn't violate the restriction;
    //          if new item category is TOP or BOT and
    //              if there is DRESS item in the collection, returns false.
    //          if new item category is DRESS and
    //              if there is TOP or BOT item in the collection, returns false.
    private boolean outfitRestrictions(ClothingCategory newCategory) {
        return !topRestriction(newCategory) && !botRestriction(newCategory) && !dressRestriction(newCategory);
    }

    // EFFECTS: returns true if newCategory is Top and usedCategories contains Dress, false otherwise
    private boolean topRestriction(ClothingCategory newCategory) {
        return newCategory == ClothingCategory.TOP && usedCategories.contains(ClothingCategory.DRESS);
    }

    // EFFECTS: returns true if newCategory is Bot and usedCategories contains Dress, false otherwise
    private boolean botRestriction(ClothingCategory newCategory) {
        return newCategory == ClothingCategory.BOT && usedCategories.contains(ClothingCategory.DRESS);
    }

    // EFFECTS: returns true if newCategory is Dress and usedCategories contains Top or Bot, false otherwise
    private boolean dressRestriction(ClothingCategory newCategory) {
        return newCategory == ClothingCategory.DRESS
                && (usedCategories.contains(ClothingCategory.TOP) || usedCategories.contains(ClothingCategory.BOT));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Outfit outfit = (Outfit) obj;

        return name.equals(outfit.name);
    }

    @Override
    public String toString() {
        return  name + " : " + collection;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("collection", collectionToJson());
        return json;
    }

    // EFFECTS: returns clothing items in this outfit as a JSON array
    private JSONArray collectionToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Clothing c : collection) {
            JSONObject clothingJson = new JSONObject();
            clothingJson.put("item", c.getItem());
            clothingJson.put("category", c.getCategory());
            clothingJson.put("color", c.getColor());
            jsonArray.put(clothingJson);
        }

        return jsonArray;
    }
}
