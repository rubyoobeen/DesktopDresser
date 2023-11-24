// This code is adapted from the JsonSerializationDemo project by UBC CPSC 210.
// Source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

package persistence;

import org.json.JSONObject;

public interface Writable {
    // EFFECTS: returns this as JSON object

    JSONObject toJson();
}