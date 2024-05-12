package persistence;

import org.json.JSONObject;

// this interface is adapted from the sample demo,
// includes the method that convert String, double and object to json data
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
