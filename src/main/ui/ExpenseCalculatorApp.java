/*package ui;

import model.Categories;
import model.GenerateReport;
import model.SingleExpenseEntry;

import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.exceptions.MonthNotInRangeException;
import model.exceptions.NegativeAmountException;
import model.exceptions.YearNotInRangeException;
import persistence.JsonReader;
import persistence.JsonWriter;


// an Expense calculator app that allows user to
// add expenses with properties,
// see expense lists,
// delete expense from the list
// obtain a report that calculates how much expends are under different categories
// and compare total expense to a customized budget
// also save data and load data from previous round of calculation
// Note: some methods using while loop is adapted from the example project TellerApp
// at https://github.students.cs.ubc.ca/CPSC210/TellerApp
// load and save data from/to json file is learned from sample demo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/ui/WorkRoomApp.java

public class ExpenseCalculatorApp {

    private GenerateReport report = new GenerateReport(); // a new report of calculation is instantiated

    private Scanner input; // keep track of user input

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/report.json";

    // EFFECTS: runs the expense calculator app
    public ExpenseCalculatorApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runMainMenu();
    }


    // MODIFIES: this
    // EFFECTS: initializes default categories
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: initialize Categories and processes user input from the main menu
    //          If user entry is not the letter on the menu, keep asking.
    private void runMainMenu() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;

            } else {
                processMainMenuCommand(command);
            }
        }
        System.out.println("\n Time for making adjustment!");

    }


    //EFFECTS: displays options of  main menu to user
    private void displayMainMenu() {
        System.out.println("\nWelcome to Expense Distribution Calculator");
        System.out.println("\nSelect from:");
        System.out.println("\tn -> start new calculation");
        System.out.println("\tl -> load from file and continue calculation");
        System.out.println("\tq -> quit");
    }

    //EFFECTS: Process user input for the main menu options
    //          Go the corresponding methods if users entry is the letter in the menu.
    //          Otherwise, keep asking.

    private void processMainMenuCommand(String command) {
        if (command.equals("n")) {
            report = new GenerateReport();
            goToActionChoices();
        } else if (command.equals("l")) {
            loadExpenses();
            goToActionChoices();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    //EFFECTS: displays options of actions to user
    //          Parses user input if user's entry is valid. Otherwise, keeps asking.
    private void goToActionChoices() {
        boolean keepAsking = true;

        String command;

        while (keepAsking) {
            displayActionMenu();
            command = input.next();
            command = command.toLowerCase();
            keepAsking = parseAction(command);
        }

    }


    //EFFECTS: displays options of  main menu to user
    private void displayActionMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new expenses");
        System.out.println("\tl -> display list");
        System.out.println("\tr -> display report and save");
    }

    //EFFECTS: Parse user choices of actions on the user action
    //          if user's entry in on the letter in the menu, then return false.
    //          Otherwise, return true to indicate the need of keep asking.
    private boolean parseAction(String command) {
        boolean ifKeepAsking = false;
        switch (command) {
            case "a":
                addNewExpense();
                break;
            case "l":
                displayExpenseList();
                goToExpListMenu();
                break;
            case "r":
                setBudget();
                displayReport();
                saveReport();

                break;
            default:
                System.out.println("Selection not valid...");
                ifKeepAsking = true;
                break;
        }
        return ifKeepAsking;

    }



    // MODIFIES: this
    // EFFECTS: prompt user instructions to set properties of an expense entry,
    //         process user input and store the information to current expense
    private void addNewExpense() {
        SingleExpenseEntry curExpense = new SingleExpenseEntry();
        System.out.println("\nYou are about to adding a new expense");

        parseEntryAmount(curExpense);
        processCategory(curExpense);

        System.out.println("\nPlease add any description");
        curExpense.setDescription(input.next());

        processYear(curExpense);
        processMonth(curExpense);
        processDay(curExpense);

        report.addEntry(curExpense);

        System.out.println("\n Great! A new entry is added: ");
        printCurExpense(curExpense);

        goToActionChoices();

    }

    //REQUIRES : the user input must be a double
    //MODIFIES: this
    //EFFECTS: If user enters a number, get amount of the current expense from user input
    //          store the amount to the current expense entry. Otherwise, keep asking.
    private void parseEntryAmount(SingleExpenseEntry curExpense) {
        System.out.println("Please entry the amount of your expense");
        boolean keepAsking = true;
        while (keepAsking) {
            try {
                double amount = input.nextDouble();
                curExpense.setAmount(amount);
                keepAsking = false;

            } catch (InputMismatchException ie) {
                input.next();
                System.out.println("\nThis is not a number. Please enter the amount in numbers");
            } catch (NegativeAmountException e) {
                System.out.println("\n Please enter a number > 0");

            }

        }
    }

    //MODIFIES: this
    //EFFECTS: If user enters a valid input, get choices of category of cur expense from user input
    //         store selected category to current expense entry. Otherwise, keep asking.
    private void processCategory(SingleExpenseEntry curExpense) {
        System.out.println("\nPlease select your category");
        boolean keepAsking = true;
        String command;

        while (keepAsking) {
            displayCategoryChoices();
            command = input.next();
            command = command.toLowerCase();
            keepAsking = parseCategoryCommand(command, curExpense);
        }
    }

    //EFFECTS: displays category choices to user
    private void displayCategoryChoices() {
        System.out.println("\nSelect from:");
        System.out.println("\tf -> " + "Food & Grocery");
        System.out.println("\tu -> " + "Utilities");
        System.out.println("\tn -> " + "Daily Necessities");
        System.out.println("\to -> " + "Others");
    }

    //EFFECTS: parse choice of category from use input, and set the category of current expense of that choice
    //          returns a boolean shows whether the app should keep asking user based on the validity of choices.
    //          return false if user entry a letter that is on the menu. Return true otherwise.
    private boolean parseCategoryCommand(String command, SingleExpenseEntry curExp) {

        Categories categories = report.getCategories();
        boolean ifKeepAsking = false;
        switch (command) {
            case "f":
                curExp.setCategory(categories.getFood());

                break;
            case "u":
                curExp.setCategory(categories.getUtilities());

                break;
            case "n":
                curExp.setCategory(categories.getNecessities());

                break;
            case "o":
                curExp.setCategory(categories.getOthers());
                break;
            default:
                System.out.println("Selection not valid...");
                ifKeepAsking = true;
                break;
        }
        return ifKeepAsking;
    }

    //REQUIRES: user must enter an integer
    // MODIFIES: this
    //EFFECTS: If user enter integer from 2015 to 2050 inclusive,
    // get year from user input, and store year in current expense entry.
    // Otherwise, keep asking.
    private void processYear(SingleExpenseEntry curExpense) {
        boolean keepAsking = true;
        int year = 0;
        System.out.println("\nPlease enter year between 2015 - 2050");

        while (keepAsking) {
            try {
                year = input.nextInt();
                curExpense.setYear(year);
                keepAsking = false;

            } catch (InputMismatchException ie) {
                input.next();
                System.out.println("This is not a number,please enter a year between 2015 - 2050 ");
            } catch (YearNotInRangeException e) {
                System.out.println("\nEntry not in range, please enter a year between 2015 - 2050");
            }

        }
    }

    // MODIFIES: this
    //EFFECTS: If user enter integer from 1 to 12 inclusive,
    // then get month from user input, and store input in current expense entry.
    // Otherwise, keep asking.
    private void processMonth(SingleExpenseEntry curExpense) {

        boolean keepAsking = true;
        int month = 0;

        System.out.println("\nPlease enter month between 1 to 12");
        while (keepAsking) {
            try {
                month = input.nextInt();
                curExpense.setMonth(month);
                keepAsking = false;

            } catch (InputMismatchException ie) {
                input.next();
                System.out.println("\nThis is not a number. Please enter a number between 1 to 12");
            } catch (MonthNotInRangeException e) {
                System.out.println("\nEntry not in range. Please enter a number between 1 to 12 ");

            }
        }

    }

    // MODIFIES: this
    // EFFECTS: If user entry a correct integer within the day range of the input month
    // then, get day from user input, and stores input in current expense entry. Otherwise, keep asking.
    private void processDay(SingleExpenseEntry curExpense) {
        boolean keepAsking = true;
        int day = 0;
        YearMonth yearMonthObject = YearMonth.of(curExpense.getYear(), curExpense.getMonth());
        int daysInMonth = yearMonthObject.lengthOfMonth();

        System.out.println("\nPlease enter day");
        while (keepAsking) {
            try {

                day = input.nextInt();
                if (day >= 1 && day <= daysInMonth) {
                    keepAsking = false;
                    curExpense.setDay(day);
                } else {
                    System.out.println("\nThere is no day:  " + day + " in month: " + curExpense.getMonth()
                            + " . Please enter a valid day ");
                }
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Your entry is not a number. Please enter a number for the day");
            }
        }
    }

    // EFFECT: prints out the expense entry added in the expense list
    private void printCurExpense(SingleExpenseEntry curExpense) {
        System.out.println(curExpense.toString());
        System.out.println("\n");
    }

    //EFFECT: print out all the expended that is entered in the expense list lead its index
    private void displayExpenseList() {
        ArrayList<SingleExpenseEntry> list = report.getExpenses();
        if (list.size() == 0) {
            System.out.println("No entry in the list");
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ": " + list.get(i).toString());
        }

    }

    // EFFECT: display user the action on the expense list, and parse user's command
    //          Parse user choices  if user entry a valid letter displayed on the menu
    //           Otherwise, keep asking.
    private void goToExpListMenu() {
        boolean keepAsking = true;

        String command;
        while (keepAsking) {
            expenseListChoice();
            command = input.next();
            command = command.toLowerCase();
            keepAsking = parseExpenseListChoice(command);
        }

    }

    //EFFECTS: display choices to user to delete entry or calculate the report
    private void expenseListChoice() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add an expense entry");
        System.out.println("\td -> delete an expense entry");
        System.out.println("\tr -> see report and save");
    }

    //EFFECTS: parse user choices of actions on the expense list if user entry a valid letter displayed on the menu
    //         Otherwise, keep asking.
    private boolean parseExpenseListChoice(String command) {
        boolean ifKeepAsking = false;
        switch (command) {
            case "a":
                addNewExpense();
                break;
            case "d":
                parseDeleteAction();
                break;
            case "r":
                setBudget();
                displayReport();
                saveReport();
                break;
            default:
                System.out.println("Selection not valid...");
                ifKeepAsking = true;
                break;
        }
        return ifKeepAsking;

    }

    //REQUIRES: user entry of index must be an integer
    //MODIFIES: this
    // EFFECTS: if there is no entry in the list, then return to previous menu.
    //          Otherwise, go the deleteEntry method if user entry a valid number and the number is within range.
    //          If user does not enter an integer or an integer that is not in range, keep asking.
    private void parseDeleteAction() {
        boolean keepAsking = true;
        while (keepAsking) {
            if (report.getExpenses().size() == 0) {
                System.out.println("No entry in the list.");
                keepAsking = false;
            } else {
                try {
                    keepAsking = deleteEntry();
                } catch (InputMismatchException ie) {
                    input.next();
                    System.out.println("\nThis is not a number. Please enter a number for the index");
                }
            }
        }
        goToActionChoices();
    }


    //MODIFIES: this
    // EFFECTS: ask user the index of the entry to be deleted
    //          and delete this entry from the expense list.
    //          return true if user enter an index that is not exist or not an integer,
    //          otherwise return false.
    private boolean deleteEntry() {
        int index = 0;
        SingleExpenseEntry toBeDeleted;
        boolean keepAsking = true;
        System.out.println("please type in the index number of the entry you want to delete");
        index = input.nextInt();
        if (index >= 0 && index < this.report.getExpenses().size()) {
            keepAsking = false;
            toBeDeleted = this.report.getExpenseFromIndex(index);
            this.report.deleteOneEntry(toBeDeleted);
            System.out.println("An entry has been deleted");
        } else {
            System.out.println("\nEntry not valid");
        }
        return keepAsking;
    }

    //EFFECT: display a calculated report in String format including:
    //              amount under each category,
    //              total expense, budget, and remained budget, indicate whether user exceed the budge or not
    private void displayReport() {
        System.out.println("\n Here is the report:\n");
        System.out.println(report.toString());
    }

    // MODIFIES: this
    // EFFECT: set budget for the calculation if user enters a number that is greater than 0
    //          Otherwise, keeps asking.
    private void setBudget() {
        boolean keepAsking = true;
        double budget = 0;
        System.out.println("please enter a budget");

        while (keepAsking) {
            try {
                budget = input.nextInt();
                if (budget > 0) {
                    keepAsking = false;
                    report.setBudget(budget);
                } else {
                    System.out.println("\nplease enter a budge greater than 0");
                }
            } catch (InputMismatchException ie) {
                input.next();
                System.out.println("Your entry is not a number. Please enter a number for the budget");
            }
        }

    }

    //EFFECTS: save expenses and current report in file
    private void saveReport() {
        try {
            jsonWriter.open();
            jsonWriter.write(report);
            jsonWriter.close();
            System.out.println("Your report is saved to" + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

     //MODIFIES: this
     //EFFECTS: loads expenses from file
    private void loadExpenses() {
        try {
            report = jsonReader.read();
            System.out.println("Loaded expense list" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}

*/
