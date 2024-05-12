package model;

import model.exceptions.MonthNotInRangeException;
import model.exceptions.NegativeAmountException;
import model.exceptions.YearNotInRangeException;
import org.json.JSONObject;
import persistence.Writable;

/*
 represent a single expense entry with amount(in dollars), a string description, categories, year, month and day
*/

public class SingleExpenseEntry implements Writable {

    private double amount;                // the amount of this expense
    private String description;           // string description of the expense
    private Category category;          // the category this expense is under
    private int year;                     // the year of this expense
    private int month;                    // the month of this expense
    private int day;                      // the date of this expense

    // EFFECTS: initialize an empty expense by setting all field to be empty
    public SingleExpenseEntry() {
        this.amount = 0;
        this.description = "";
        this.category = null;
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }

    // REQUIRES: year must > 2015 and < 2050;
    //          month must from 1 to 12 inclusive;
    //          day must from 1 to 31 inclusive (corresponding to the month)
    //          category if this expense  must be one of the categories that is provided in the app
    // EFFECTS: set amount of this expense to double; set description to be the given description
    //          set category to corresponding category classes; set year, month, day into positive integers
    public SingleExpenseEntry(double amount, String description,
                              Category category, int year, int month, int day) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.year = year;
        this.month = month;
        this.day = day;

    }

    public double getAmount() {
        return amount;
    }

    // REQUIRES: amount must > 0
    public void setAmount(double amount) throws NegativeAmountException {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new NegativeAmountException();
        }
    }

    public Category getCategory() {
        return this.category;
    }

    //REQUIRES : the category must include in the default categories in the app
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return this.year;
    }

    // MODIFIES: this
    //EFFECTS: set year if 2015 <= year input <= 2050,otherwise throws YearNotInRangeException
    public void setYear(int year) throws YearNotInRangeException {
        if (year >= 2015 && year <= 2050) {
            this.year = year;
        } else {
            throw new YearNotInRangeException();
        }
    }


    public int getMonth() {
        return this.month;
    }

    // MODIFIES: this
    // EFFECTS: set month if 1 <= year input <= 12,otherwise throws MonthNotInRangeException
    public void setMonth(int month) throws MonthNotInRangeException {
        if (month >= 1 && month <= 12) {
            this.month = month;
        } else {
            throw new MonthNotInRangeException();
        }
    }

    public int getDay() {
        return this.day;
    }

    //REQUIRES: 1 <= day <= 31 corresponding to the month of the expense
    public void setDay(int day) {
        this.day = day;
    }

    //EFFECTS: prints out all fields of an expense in String format
    @Override
    public String toString() {

        return year + "," + month + "," + day + " : "
                + "amount ($) : " + amount + ", category :"
                + category.getCategoryName() + ", description: " + description + "\n";
    }


    // EFFECTS: returns a string array that contains all the fields in this class
    public String[] toStringArray() {
        String[] string = {Integer.toString(year), Integer.toString(month), Integer.toString(day),
                Double.toString(amount), category.getCategoryName(), description};
        return string;
    }

    // EFFECT: convert each fields in this class to key-value pairs and store in a json object and return it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("month", month);
        json.put("day", day);
        json.put("amount ($)", amount);
        json.put("category", category.getCategoryName());
        json.put("description", description);

        return json;
    }
}
