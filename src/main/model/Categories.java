package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// the representation of all default categories in the app
public class Categories implements Writable {

    private Category food;
    private Category utilities;
    private Category necessities;
    private Category others;

    private LinkedList<Category> categoryList;

    // MODIFIES: this
    // EFFECTS: initialize all category object and a linkedlist, add all object to a linkedlist
    public Categories() {
        food = new Category("Food & Grocery");
        utilities = new Category("Utilities");
        necessities = new Category("Daily Necessities");
        others = new Category("Others");
        categoryList = new LinkedList<>();

        categoryList.add(food);
        categoryList.add(utilities);
        categoryList.add(necessities);
        categoryList.add(others);
    }


    public LinkedList<Category> getCategoryList() {
        return categoryList;
    }

    public Category getFood() {
        return food;
    }


    public Category getUtilities() {
        return utilities;
    }


    public Category getNecessities() {
        return necessities;
    }


    public Category getOthers() {
        return others;
    }


    // EFFECTS: returns string representation of each category
    @Override
    public String toString() {
        String s = "";
        for (Category c : categoryList) {
            s +=  c.toString();
        }
        return s;
    }

    // EFFECTS: convert each Category objects in category list into Json and put into a Json object
    //          returns this json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (Category c : categoryList) {
            json.put(c.getCategoryName(), c.getAmount());
        }
        return json;
    }

}