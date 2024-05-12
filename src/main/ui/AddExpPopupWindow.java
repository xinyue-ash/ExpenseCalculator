package ui;

import model.Category;
import model.SingleExpenseEntry;
import model.exceptions.MonthNotInRangeException;
import model.exceptions.NegativeAmountException;
import model.exceptions.YearNotInRangeException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.Objects;

// represent a pop-up window which allows user to
// input date,amount,description to a text field, and also select a category
// from a drop-down list for a single expense entry
// then add this single expense entry to the expense table
// this class is modified from the online tutorial using JDialog
//http://www.java2s.com/Tutorials/Java/Swing/JDialog/Create_a_JDialog_to_get_input_for_address_in_Java.htm
public class AddExpPopupWindow extends JDialog {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;
    private static final int ROW = 7;
    private static final int COL = 2;
    private static final int TEXT_LENGTH = 20;

    // labels for text fields
    private static final JLabel YEAR_LABEL = new JLabel("Year : ");
    private static final JLabel MONTH_LABEL = new JLabel("Month:  ");
    private static final JLabel DAY_LABEL = new JLabel("Day: ");
    private static final JLabel CATEGORY_LABEL = new JLabel("Category: ");
    private static final JLabel AMOUNT_LABEL = new JLabel("Amount: ");
    private static final JLabel DESCRIPTION_LABEL = new JLabel("Description: ");

    // a drop-down menu
    private JComboBox<String> categoryMenu;
    private String errorMessage;

    // text fields for all properties
    private JTextField yearField = new JTextField(TEXT_LENGTH);
    private JTextField monthField = new JTextField();
    private JTextField dayField = new JTextField();
    private JTextField amountField = new JTextField();
    private JTextField descriptionField = new JTextField();

    private JButton okButton;

    private ExpenseCalculatorGUI expGUI;
    private SingleExpenseEntry curEntry;
    private DefaultTableModel model;  // a model for expense table


    // MODIFIES: this
    // EFFECTS: initialize pop up window (formatting, add elements),
    // pass expGUI, table model from main frame
    public AddExpPopupWindow(Frame parent, boolean modal, ExpenseCalculatorGUI expGUI, DefaultTableModel model) {
        super(parent, modal);
        this.expGUI = expGUI;
        this.model = model;
        this.errorMessage = "";
        curEntry = new SingleExpenseEntry();
        addElements();
        setSize(WIDTH, HEIGHT);
    }

    // Add elements on this popup window
    // MODIFIES: this
    // EFFECTS: add labels, text fields, drop down menu, and buttons
    private void addElements() {
        setTitle("Please Add a New Expense");
        setLayout(new GridLayout(ROW, COL));
        add(YEAR_LABEL);
        add(yearField);

        add(MONTH_LABEL);
        add(monthField);

        add(DAY_LABEL);
        add(dayField);

        add(AMOUNT_LABEL);
        add(amountField);

        add(CATEGORY_LABEL);
        setCategoryChoices();

        add(DESCRIPTION_LABEL);
        add(descriptionField);

        addOkButton();
    }

    // set category selection menu
    // MODIFIES: this
    // EFFECT: create a selection drop down menu with different categories
    private void setCategoryChoices() {
        LinkedList<Category> categories = expGUI.rp.getCategories().getCategoryList();
        String[] categoryNames = new String[categories.size() + 1];
        categoryNames[0] = "Please select one"; // set first chose  to be empty
        int i = 1;
        for (Category c : categories) {
            categoryNames[i] = c.getCategoryName();
            i++;
        }
        categoryMenu = new JComboBox<>(categoryNames);
        add(categoryMenu);

    }

    // MODIFIES: this
    // EFFECTS: initialize ok button with its action, add it to JDialog
    private void addOkButton() {
        okButton = new JButton(new StoreExpenseInfo());
        add(okButton);
    }

    // all class that has structure that extends AbstractAction is modified from sample demo:
    //https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/34ffaafee0810f67aa7801a30ee03807de21f039/src/main/ca/ubc/cpsc210/alarm/ui/AlarmControllerUI.java
    // a class representing the action once clicking OK button
    private class StoreExpenseInfo extends AbstractAction {

        public StoreExpenseInfo() {
            super("Ok: store expense info");
        }

        // MODIFIES: ExpenseCalculatorGUI,SingleExpenseEntry
        // EFFECTS: parse user input from each text fields or drop down menu,
        //          if catches NumberFormatException, YearNotInRangeException, MonthNotInRangeException,
        //          and NegativeAmountException, then pop up corresponding warning messages corresponding
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean validEntry = false;
            try {
                getDateInput();
                getAmount();
                getCategory();
                getDescription();
                upDateTable();
                validEntry = true;
                dispose();
            } catch (NumberFormatException ne) {
                errorMessage = "Please Enter Integer For Date or Amount Field " + ne.getMessage();
            } catch (YearNotInRangeException ye) {
                errorMessage = "Please Enter Year with in 2015-2050";
            } catch (MonthNotInRangeException me) {
                errorMessage = "Please Enter Month with in 1-12";
            } catch (NegativeAmountException ne) {
                errorMessage = "Please Enter Positive Amount";
            }
            if (!validEntry) {
                JOptionPane.showMessageDialog(null, errorMessage, "Oops", JOptionPane.ERROR_MESSAGE);
            } else {
                addCurExpense();
            }
        }

        // MODIFIES: curEntry
        // EFFECTS: parse user input entered in year,month,day field,
        //          if user did not enter an integer, throws NumberFormatException;
        //          if user's year or month entry is not within range,
        //          throws YearNotInRangeException or MonthNotInRangeException;
        //          if user's date message is not in range of the month that user put,
        //          a pop-up window shows the waring message;
        //          otherwise, sets currents expense's day, month, day to user's input
        private void getDateInput() throws NumberFormatException, YearNotInRangeException, MonthNotInRangeException {

            int yearInput = Integer.parseInt(yearField.getText());
            int monthInput = Integer.parseInt(monthField.getText());
            int dayInput = Integer.parseInt(dayField.getText());

            curEntry.setYear(yearInput);
            curEntry.setMonth(monthInput);
            // set date
            YearMonth yearMonthObject = YearMonth.of(curEntry.getYear(), curEntry.getMonth());
            int daysInMonth = yearMonthObject.lengthOfMonth();
            if (dayInput >= 1 && dayInput <= daysInMonth) {
                curEntry.setDay(dayInput);
            } else {
                String message = "\nThere is no day in the month that you entered"
                        + " . Please enter a valid day ";
                JOptionPane.showMessageDialog(null,
                        message, "Oops", JOptionPane.ERROR_MESSAGE);
            }
        }

        // MODIFIES: curEntry
        // EFFECTS: parse user's input entered amount field,
        //          if user did not enter an integer, throws NumberFormatException;
        //          if user enters a negative amount, throws NegativeAmountException;
        //          Otherwise, set curExp's amount to be user's input
        private void getAmount() throws NegativeAmountException, NumberFormatException {
            Double amountInput = Double.parseDouble(amountField.getText());
            curEntry.setAmount(amountInput);

        }

        // MODIFIES: curEntry
        // EFFECTS: parse category choice from the drop-down menu;
        //          if user did not select a choice, pop up a warning message;
        //          Otherwise, set curExp's category to be user's choice
        private void getCategory() {
            boolean validSelection = false;
            String categoryInput = (String) categoryMenu.getSelectedItem();
            for (Category c : expGUI.rp.getCategories().getCategoryList()) {
                if (Objects.equals(categoryInput, c.getCategoryName())) {
                    curEntry.setCategory(c);
                    validSelection = true;
                    break;
                }
            }
            if (!validSelection) {
                JOptionPane.showMessageDialog(null,
                        "Please select a category",
                        "Oops", JOptionPane.ERROR_MESSAGE);
            }
        }

        // MODIFIES: curEntry
        // EFFECTS: parse user's description input from text field; set curExp's description to be user's input
        private void getDescription() {
            String descriptionInput = descriptionField.getText();
            curEntry.setDescription(descriptionInput);
        }

        // MODIFIES: expGUI
        // EFFECTS: add a single expense to expenseList after parsing from user's input in the pop-up menu
        private void addCurExpense() {
            expGUI.rp.addEntry(curEntry);
        }

        // MODIFIES: model
        // EFFECTS: update table after user input complete information of an expense
        private void upDateTable() {
            model.addRow(curEntry.toStringArray());
        }
    }
}



