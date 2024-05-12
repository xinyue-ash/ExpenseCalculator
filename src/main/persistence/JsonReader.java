package persistence;

import model.Category;
import model.GenerateReport;
import model.SingleExpenseEntry;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// this class is modifies from the demon app in the instruction
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
// Represents a reader that reads expense lists from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads expense list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GenerateReport read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseReport(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }


     //EFFECTS: parses expense and budge from JSON object and returns it
    private GenerateReport parseReport(JSONObject jsonObject) {
        GenerateReport report = new GenerateReport();
        JSONObject jsonReport = jsonObject.getJSONObject("report data");
        addExpenses(report,jsonObject);

        double budget = jsonReport.getDouble("budget");
        report.setBudget(budget);
        report.getRemainedBudget();
        report.getIfExceeded();

        return report;
    }



    // MODIFIES: report
    // EFFECTS: parses expenses from JSON object and adds them to expense list
    private void addExpenses(GenerateReport report, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Expenses");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            parseExpense(report, nextExpense);
        }
    }

    // MODIFIES: rp
    // EFFECTS: parses properties of an expense from JSON object
    // and adds them to an expense JSON object then to the expense list
    private void parseExpense(GenerateReport report, JSONObject jsonObject) {
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        int day = jsonObject.getInt("day");
        String description = jsonObject.getString("description");
        String category = jsonObject.getString("category");
        Category catObj = new Category(category);
        double amount = jsonObject.getDouble("amount ($)");

        SingleExpenseEntry expense = new SingleExpenseEntry(
                amount,description, catObj,year,month,day);
        report.addEntry(expense);
    }
}
