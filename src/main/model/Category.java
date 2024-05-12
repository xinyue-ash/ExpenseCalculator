package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// represent a single category provide its name,
// the amount of expense under this category can be added or deleted
public class Category {

    private double amount;
    private String categoryName;

    //REQUIRES: name cannot be empty
    public Category(String name) {
        this.amount = 0;
        this.categoryName = name;
    }

    public double getAmount() {
        return amount;
    }

    // REQUIRES: amount >= 0
    //MODIFIES: this
    //EFFECTS: add amount under that category
    public double addAmount(double amount) {
        this.amount += amount;
        return this.amount;
    }

    // MODIFIES : this
    // EFFECTS: delete amount of money if corresponding entry is deleted
    public double deleteAmount(double amount) {
        this.amount -= amount;
        return this.amount;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    // EFFECTS: if given category has same category name as this.category, returns true, otherwise false
    public boolean equals(Category other) {
        return Objects.equals(categoryName, other.categoryName);
    }

    // EFFECTS: prints out categories' name and its corresponding amount
    @Override
    public String toString() {
        return categoryName + " : $"  + amount +   " \n";
    }


}
