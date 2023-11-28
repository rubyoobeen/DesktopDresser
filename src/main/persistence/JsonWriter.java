// This code is adapted from the JsonSerializationDemo project by UBC CPSC 210.
// Source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

package persistence;

import model.Closet;

import org.json.JSONObject;

import java.io.*;
import java.util.List;

// Represents a writer that writes JSON representation of closet to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        try {
            writer = new PrintWriter(new File(destination));
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("error opening file");
        }
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of closet to file
    public void write(Closet c) {
        if (c != null) {
            JSONObject json = c.toJson();
            saveToFile(json.toString(TAB));
        } else {
            throw new IllegalArgumentException("cannot write null Closet to file");
        }
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}