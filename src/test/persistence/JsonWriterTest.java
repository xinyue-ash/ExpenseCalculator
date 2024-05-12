package persistence;

import model.Categories;
import model.Category;
import model.GenerateReport;
import model.SingleExpenseEntry;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// this class tests the function of saving data to text file
// the method of testing is adapted from sample demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            GenerateReport rp = new GenerateReport();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }
    @Test
    void testWriterEmptyReport() {
        try {
            GenerateReport rp = new GenerateReport();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyReport.json");
            writer.open();
            writer.write(rp);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyReport.json");
            rp = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            GenerateReport rp = new GenerateReport();
            rp.addEntry(new SingleExpenseEntry(10, "Milk", rp.getCategories().getFood(), 2021, 9, 1));
            rp.addEntry(new SingleExpenseEntry(20, "Milk", rp.getCategories().getUtilities(), 2021, 9, 1));
            rp.setBudget(100);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralReport.json");
            writer.open();
            writer.write(rp);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralReport.json");
            rp = reader.read();

            ArrayList<SingleExpenseEntry> expList = rp.getExpenses();
            assertEquals(2, expList.size());
            assertEquals(30, rp.getTotalExpense());
            assertEquals(100 - 30, rp.getRemainedBudget());
            assertFalse(rp.getIfExceeded());
            assertEquals(100, rp.getBudget());

            Categories cat = rp.getCategories();
            assertEquals(10, cat.getFood().getAmount());
            assertEquals(0, cat.getNecessities().getAmount());
            assertEquals(20, cat.getUtilities().getAmount());
            assertEquals(0, cat.getOthers().getAmount());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}