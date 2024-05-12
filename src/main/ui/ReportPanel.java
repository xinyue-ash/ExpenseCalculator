package ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.Color.PINK;

// a class represent a report including a Category distribution barchart and budget report
public class ReportPanel extends JPanel {
    private static final String DEFAULT_TEXT = " Click 'Calculate' button to see report";
    private static final Color BAR_COLOR = PINK;
    private JButton calculate;
    private JButton save;
    private JPanel reportDisplay;
    private ReportBarChart categoryReport;
    private JTextArea budgetReport;

    private ExpenseCalculatorGUI expGUI;


    // MODIFIES: this
    // EFFECTS: passes in parameter formats current panel, add elements to current panel
    public ReportPanel(ExpenseCalculatorGUI expGUI) {
        this.expGUI = expGUI;

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        addButtons();
        addReportDisplay();

    }

    // MODIFIES: this
    //EFFECTS: add report panel that has bar chart for categories and text report for budget& total expense
    private void addReportDisplay() {
        reportDisplay = new JPanel();
        reportDisplay.setLayout(new GridLayout(1,2));
        reportDisplay.setBorder(BorderFactory.createTitledBorder("Report"));
        setUpBudgetReport();
        setUpBarChart();
        add(reportDisplay);

    }

    // MODIFIES: this
    // EFFECTS: set up and add bar chart to report panel
    private void setUpBarChart() {
        categoryReport = new ReportBarChart(expGUI);
        categoryReport.setBorder(BorderFactory.createTitledBorder("Category Report"));
        //addCategoryBars(categoryReport);
        reportDisplay.add(categoryReport);
    }


    // MODIFIES: this
    // EFFECTS: add budget report segment to parent panel
    public void setUpBudgetReport() {
        budgetReport = new JTextArea(DEFAULT_TEXT);
        budgetReport.setEditable(false);
        budgetReport.setBorder(BorderFactory.createTitledBorder("Budget Report"));
        reportDisplay.add(budgetReport);
    }

    // MODIFIES: this
    // EFFECTS: add "calculation" and "save" button to a button panel, add button panel to parent panel
    private void addButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2));
        buttons.add(setCalculationButton());
        buttons.add(setSaveButton());
        add(buttons);

    }

    // MODIFIES: this
    // EFFECTS : sets up and returns calculation button with its action class
    private JButton setCalculationButton() {
        calculate = new JButton(new CalculateButton());
        return calculate;
    }

    // MODIFIES: this
    // EFFECTS:  sets up and returns a save button at the button with its action class
    private JButton setSaveButton() {
        save = new JButton(new SaveData(expGUI));
        return save;
    }


    // a class that represents the action once clicking calculate button
    private class CalculateButton extends AbstractAction {

        public CalculateButton() {
            super("Calculate");
        }

        // MODIFIES: categoryReport,budgetReport
        // EFFECTS: once buttons is clicked, reset chart and update text report according to the change in user data
        @Override
        public void actionPerformed(ActionEvent e) {
            categoryReport.resetChart();
            updateTextReport();
        }


        // MODIFIES: budgetReport
        // EFFECTS: update text report
        private void updateTextReport() {
            String budgetReportString = expGUI.rp.toString();
            budgetReport.setText(budgetReportString);
        }

    }

    // MODIFIES: budgetReport,categoryReport
    // EFFECTS: reset budget report text field and category chart to initial state
    public void resetReportPanel() {
        budgetReport.setText(DEFAULT_TEXT);
        categoryReport.resetChart();
    }

}
