// This code is adapted from the JsonSerializationDemo project by UBC CPSC 210.
// Source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

package persistence;

import model.*;
import model.exception.ClothingException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads closet from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads closet from file and return it
    // throws IOException if an error occurs reading data from file
    public Closet read() throws IOException, ClothingException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCloset(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses closet from JSON object and return it
    private Closet parseCloset(JSONObject jsonObject) throws ClothingException {
        String name = jsonObject.getString("name");
        Closet c = new Closet(name);
        addClothings(c, jsonObject);
        addOutfits(c, jsonObject);
        return c;
    }

    // MODIFIES: closet
    // EFFECTS: parses clothing items from JSON object and adds them to closet
    private void addClothings(Closet c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("clothing items");
        for (Object json : jsonArray) {
            JSONObject nextClothing = (JSONObject) json;
            addClothing(c, nextClothing);
        }
    }

    // MODIFIES: closet
    // EFFECTS: parses clothing from JSON object and adds it to closet
    private void addClothing(Closet c, JSONObject jsonObject) {
        try {
            String name = jsonObject.getString("item");
            ClothingCategory category = ClothingCategory.valueOf(jsonObject.getString("category"));
            Color color = Color.valueOf(jsonObject.getString("color"));
            Boolean isClean = jsonObject.getBoolean("clean?");
            Clothing clothing = new Clothing(name, category, color);
            c.addClothingToCloset(clothing);
        } catch (ClothingException ex) {
            System.out.println("error adding clothing item to closet");
        }
    }

    // MODIFIES: closet
    // EFFECTS: parses outfits from JSON object and adds them to closet
    private void addOutfits(Closet c, JSONObject jsonObject) throws ClothingException {
        JSONArray jsonArray = jsonObject.getJSONArray("outfits");
        for (Object json : jsonArray) {
            JSONObject nextOutfit = (JSONObject) json;
            addOutfit(c, nextOutfit);
        }
    }

    // MODIFIES: closet
    // EFFECTS: parses clothing from JSON object and adds it to closet
    private void addOutfit(Closet c, JSONObject jsonObject) throws ClothingException {
        String outfitName = jsonObject.getString("name");
        Outfit outfit = new Outfit(outfitName);

        JSONArray clothesArray = jsonObject.getJSONArray("collection");
        for (Object json : clothesArray) {
            JSONObject nextClothing = (JSONObject) json;
            Clothing clothing;
            try {
                clothing = parseClothing(nextClothing);
                outfit.addClothingToOutfit(clothing);
            } catch (ClothingException ex) {
                System.out.println("error adding clothing to outfit");
            }
        }
        c.addOutfitToCloset(outfit);
    }

    // EFFECTS: parses closet from JSON object and return it
    private Clothing parseClothing(JSONObject jsonObject) {
        String name = jsonObject.getString("item");
        ClothingCategory category = ClothingCategory.valueOf(jsonObject.getString("category"));
        Color color = Color.valueOf(jsonObject.getString("color"));

        return new Clothing(name, category, color);
    }
}

