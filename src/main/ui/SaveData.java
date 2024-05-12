package ui;

import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

//a class represent the action after clicking save button
// this class is modified from sample demo:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/ui/WorkRoomApp.java
public class SaveData extends AbstractAction {

    private ExpenseCalculatorGUI expGUI;

    private JsonWriter jsonWriter;
    private static final String JSON_STORE = "./data/report.json";
    private static final String SUCCESS_MESSAGE = "Your report is saved to" + JSON_STORE;
    private static final String ERROR_MESSAGE = "Unable to write to file: " + JSON_STORE;

    // MODIFIES: this
    // EFFECTS: set button display, initialize expGUI to be the parameter, initialize json writer
    public SaveData(ExpenseCalculatorGUI expGUI) {
        super("Save");
        this.expGUI = expGUI;
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: once " save " button is clicked,
    // user data from expGUI.rp will save to JSON_STORE and pop up message that indicates success;
    // If catches IOException, an error message will pop up
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            jsonWriter.open();
            jsonWriter.write(this.expGUI.rp);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    SUCCESS_MESSAGE,
                    "Nice", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(null,
                    ERROR_MESSAGE,
                    "Oops", JOptionPane.ERROR_MESSAGE);
        }
    }
}
