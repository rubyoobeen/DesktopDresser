package persistence;


import model.Clothing;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

// Represents a reader that reads closet from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads clothing item from file and return it
    // throws IOException if an error occurs reading data from file
    public Clothing read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseClothing(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
    }

    // EFFECTS: parses closet from JSON object and return it
    private List<Clothing> parseClothing(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getString("closet");
        List<Clothing> c = new Clothing(item);
        addClothing(c, jsonObject);
        return c;
    }

    // MODIFIES: c
    // EFFECTS: parses clothing from JSON object and adds it to closet
    private void addClothing(Clothing c, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("")
    }
}
