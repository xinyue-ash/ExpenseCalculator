package persistence;

import model.Category;
import model.SingleExpenseEntry;

import static org.junit.jupiter.api.Assertions.*;

// this class tests the function of converting an expense entry to JSON object
// this class is adapted from sample demo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonTest.java
public class JsonTest {
    protected void checkSingleExpense(double amount, String description,
                                      Category category, int year, int month, int day, SingleExpenseEntry curExp){
        assertEquals(amount, curExp.getAmount());
        assertEquals(description, curExp.getDescription());
        assertEquals(category.getCategoryName(), curExp.getCategory().getCategoryName());
        assertEquals(year, curExp.getYear());
        assertEquals(month, curExp.getMonth());
        assertEquals(day, curExp.getDay());
    }
}
