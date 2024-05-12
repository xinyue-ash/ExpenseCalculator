package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// a class represent a panel that display the budget section of the main frame
public class BudgetPanel extends JPanel {

    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int TEXT_FIELD_HEIGHT = 20;

    private JTextField budgetField;
    private JLabel budgeLabel;
    private JButton budgetButton;
    private Double budgetInput;
    private ExpenseCalculatorGUI expGUI;

    // MODIFIES: this
    // EFFECTS: initialize expGUI, format current panel, add elements to panel
    public BudgetPanel(ExpenseCalculatorGUI expGUI) {
        this.expGUI = expGUI;
        setBudgetPanel();
        setBorder(BorderFactory.createTitledBorder("Please set a budget!!!"));
    }

    // MODIFIES: this
    // EFFECTS: add label, text field, and button to the panel
    private void setBudgetPanel() {
        addBudgetLabel();
        addBudgetTextField();
        addButton();
    }


    // MODIFIES: this
    // EFFECTS: set up a label for budget text field
    private void addBudgetLabel() {
        budgeLabel = new JLabel("Budget: ");
        add(budgeLabel);
    }

    // MODIFIES: this
    // EFFECTS: initialize, format, and add text field for user to input expense amount
    private void addBudgetTextField() {
        budgetField = new JTextField();
        Dimension size = new Dimension();
        size.width =  TEXT_FIELD_WIDTH;
        size.height = TEXT_FIELD_HEIGHT;
        budgetField.setPreferredSize(size);
        setVisible(true);
        add(budgetField);
    }

    // MODIFIES: this
    // EFFECTS: set up a button with its action class, add it to budget panel
    private void addButton() {
        budgetButton = new JButton(new GetBudget());
        add(budgetButton);
    }

    // a class that represent the action after "set budget" button is pressed
    private class GetBudget extends AbstractAction {

        public GetBudget() {
            super("Set Budget");
        }

        // MODIFIES: expGUI
        //EFFECT: update budget once button is pressed, if catch NumberFormatException
        //       pop up a warning message
        @Override
        public void actionPerformed(ActionEvent e) {
            String errorMessage = "";
            boolean isInputValid = false;
            try {
                budgetInput = Double.parseDouble(budgetField.getText());
                if (budgetInput < 0) {
                    errorMessage = "Please enter a positive number";
                }
                isInputValid = true;
                expGUI.rp.setBudget(budgetInput);
            } catch (NumberFormatException ne) {
                errorMessage = "Please enter a number";
            }
            if (!isInputValid) {
                JOptionPane.showMessageDialog(null, errorMessage, "Oops", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // EFFECTS: returns budget Input
    public double getBudget() {
        return budgetInput;

    }

    // MODIFIES: budgetField, expGUI
    // EFFECTS: reset budget of report and budget text field
    public void resetBudget() {
        expGUI.rp.setBudget(0.0);
        budgetField.setText("");
    }
}
