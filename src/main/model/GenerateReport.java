package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import java.util.ArrayList;
import java.util.LinkedList;

// this class represents a monthly report by total expense, budget, remained budget
//  and a boolean variable represents whether the amount id exceeded
// also converts all fields to json objects
public class GenerateReport implements Writable {

    private double totalExpense; // keeps track of the total expense
    private double budget;   // keep track of the remained budget of this month
    private double remainedBudget; // whether this month budget is exceeded
    private boolean ifExceeded; //an expense list includes all the expenses in this month
    private String reminder;
    private Categories categories;  // a collection of categories
    private ArrayList<SingleExpenseEntry> expenses; //list of expenses entries


    public GenerateReport() {
        this.totalExpense = 0.0;
        this.budget = 0.0;
        this.remainedBudget = 0.0;
        ifExceeded = false;
        this.expenses = new ArrayList<>();
        this.categories = new Categories();
        EventLog.getInstance().logEvent(new Event("A new round of calculation started."));

    }

    // REQUIRES : budges >= 0;
    //MODIFIES: this
    // EFFECT: update budget and recalculate remaining budget
    public void setBudget(double newBudget) {
        double oldBudget = this.budget;
        this.budget = newBudget;
        if (this.remainedBudget != oldBudget) {
            this.remainedBudget = budget - this.totalExpense;
        } else {
            // no expense yet
            remainedBudget = budget;
        }
        getIfExceeded();
        EventLog.getInstance().logEvent(new Event("Budget has been set to " + newBudget));
    }

    public double getBudget() {
        return budget;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public Categories getCategories() {
        return categories;
    }

    // MODIFIES: this
    // EFFECTS:add one entry to the expense list
    //         update the total amount for this month by adding current entry's amount
    //         update the remained budget by deducting current entry's amount
    //         update the amount under corresponding category report
    //         keep track of if exceeds the budget
    public void addEntry(SingleExpenseEntry expense) {
        double curExpense = expense.getAmount();
        this.expenses.add(expense);  // add expense to the expense list
        this.totalExpense += curExpense; // update total amount
        getRemainedBudget(); // update remained budge

        //update data in under corresponding category
        String addedCategoryName = "";
        for (Category c : categories.getCategoryList()) {
            if (c.equals(expense.getCategory())) {
                c.addAmount(curExpense);
                addedCategoryName = c.getCategoryName();
            }
        }
        getIfExceeded();

        //log event
        String logDescription = "A single entry has been added : " + expense;
        logDescription = logDescription.replace("\n","");
        EventLog.getInstance().logEvent(new Event(logDescription));
        EventLog.getInstance().logEvent(new Event(addedCategoryName
                + "'s amount has increased to: $" + curExpense));


    }

    // MODIFIES : this
    // EFFECTS :  delete one entry from the expense list
    //             update the total amount by deducting current entry's amount
    //             update remained budget
    //             update the amount under category
    //             keep track of the budget if exceeded
    public void deleteOneEntry(SingleExpenseEntry toBeDeleted) {
        double curExpense = toBeDeleted.getAmount();
        this.expenses.remove(toBeDeleted);
        this.totalExpense -= curExpense;
        getRemainedBudget();

        String deletedCategoryName = "";
        for (Category c : categories.getCategoryList()) {
            if (c.equals(toBeDeleted.getCategory())) {
                c.deleteAmount(curExpense);
                deletedCategoryName = c.getCategoryName();
            }
        }
        getIfExceeded();

        //log event
        String logDescription = "A single entry has been deleted : " + toBeDeleted;
        logDescription = logDescription.replace("\n","");
        EventLog.getInstance().logEvent(new Event(logDescription));
        EventLog.getInstance().logEvent(new Event(deletedCategoryName
                + "'s amount has reduced to: $" + curExpense));
    }

    // MODIFIES: this
    // EFFECTS: calculates and returns remained budget
    public double getRemainedBudget() {
        this.remainedBudget = this.budget - this.totalExpense;
        return this.remainedBudget;
    }

    // MODIFIES: this
    // EFFECTS: if total expense exceeds the budge, return true
    //          otherwise, returns false
    public boolean getIfExceeded() {
        if (this.totalExpense > this.budget) {
            this.ifExceeded = true;
        } else {
            this.ifExceeded = false;
        }
        return this.ifExceeded;
    }

    // REQuIRES: index must >=0 and < the size of expense list
    // EFFECTS : return the index of an entry in and expense list, index starts with 0
    public SingleExpenseEntry getExpenseFromIndex(int index) {
        return this.expenses.get(index);

    }

    public ArrayList<SingleExpenseEntry> getExpenses() {
        return this.expenses;
    }

    // EFFECT: print out fields of this class into string format
    @Override
    public String toString() {

        String totalExpenseStr = String.format("%.2f", totalExpense);
        String remainedBudgetStr = String.format("%.2f", remainedBudget);
        getReminder();
        EventLog.getInstance().logEvent(new Event("A report has been generated. "));
        EventLog.getInstance().logEvent(new Event("Remained budget has been updated to  $" + this.remainedBudget));
        EventLog.getInstance().logEvent(new Event("Total expense has been updated to $" + this.totalExpense));

        return "Report:\n" + categories.toString() + "\n"
                + "totalExpense : $" + totalExpenseStr + "\n"
                + "budget : $" + budget + "\n"
                + "remainedBudget : $" + remainedBudgetStr + "\n"
                + "\n" + reminder;
    }

    // MODIFIES: this
    // EFFECTS: set up different reminders to corresponding remained budget
    public String getReminder() {
        if (this.ifExceeded) {
            reminder = "Oh no, better keep on eye on your expenses!";
        } else {
            reminder = "Nice! You are under budget!";
        }
        return reminder;
    }

    // EFFECT: return expense list and report inside a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("report data", reportToJson());
        json.put("Expenses", expensesToJson());
        EventLog.getInstance().logEvent(new Event("User has saved the report"));

        return json;
    }

    // EFFECT: returns all the properties in the report inside a JSON object
    private JSONObject reportToJson() {
        JSONObject json = new JSONObject();
        json.put("Category data", categories.toJson());
        json.put("budget", budget);
        json.put("total exp", totalExpense);
        json.put("remained budget", remainedBudget);
        json.put("ifExceeded", ifExceeded);
        return json;
    }

    // EFFECTS: put every expense in a Json array and returns this JSON array
    private JSONArray expensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (SingleExpenseEntry curExp : expenses) {
            jsonArray.put(curExp.toJson());
        }
        return jsonArray;
    }
}
