package persistence;

import model.Categories;
import model.Category;
import model.GenerateReport;
import model.SingleExpenseEntry;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

// this class tests the function of loading from a JSON file
// most methods are adapted from sample demo at
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java
class JsonReaderTest extends JsonTest {
    private Category food = new Category("Food & Grocery");
    private Category utilities = new Category("Utilities");


    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GenerateReport report = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
    @Test
    void testReaderEmptyReport() {
        JsonReader reader = new JsonReader("./data/testReadEmptyReport.json");
        try {
            GenerateReport rp = reader.read();
            ArrayList<SingleExpenseEntry> expList = rp.getExpenses();
            assertEquals(0, expList.size());
            assertEquals(0.0, rp.getTotalExpense());
            assertEquals(0.0, rp.getRemainedBudget());
            assertFalse(rp.getIfExceeded());
            assertEquals(0.0, rp.getBudget());

            for (Category c : rp.getCategories().getCategoryList()){
                assertEquals(0,c.getAmount());
            }

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralReport() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralReport.json");
        try {
            GenerateReport rp = reader.read();
            ArrayList<SingleExpenseEntry> expList = rp.getExpenses();
            assertEquals(2, expList.size());
            checkSingleExpense(1, "test1", food, 2021,1,1, expList.get(0));
            checkSingleExpense(1, "test2", utilities, 2021,1,2, expList.get(1));


            assertEquals(2, rp.getTotalExpense());
            assertEquals(121, rp.getRemainedBudget());
            assertFalse(rp.getIfExceeded());
            assertEquals(123, rp.getBudget());

            Categories cat = rp.getCategories();
            assertEquals(1, cat.getFood().getAmount());
            assertEquals(0, cat.getNecessities().getAmount());
            assertEquals(1, cat.getUtilities().getAmount());
            assertEquals(0, cat.getOthers().getAmount());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
