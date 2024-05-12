package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
// test GenerateReport class
class GenerateReportTest {

    private SingleExpenseEntry foodEntrySmall1;
    private SingleExpenseEntry foodEntrySmall2;
    private SingleExpenseEntry utilityEntrySmall;
    private SingleExpenseEntry otherEntryBig;

    private GenerateReport testReport;
    private Categories categories;

    @BeforeEach
    public void runBefore() {
        testReport = new GenerateReport();
        categories = testReport.getCategories();

        foodEntrySmall1 = new SingleExpenseEntry(3.5,"Milk",categories.getFood(),2021,9,1);
        foodEntrySmall2 = new SingleExpenseEntry(9.9,"lunch at ams",categories.getFood(),2021,9,12);
        utilityEntrySmall = new SingleExpenseEntry(61.01,"phone bill Aug",categories.getUtilities(),2021,9,1);
        otherEntryBig = new SingleExpenseEntry(1250, "aug rent", categories.getOthers(), 2021,9,4);


    }


    @Test
    public void testSetBudget() {
        assertEquals(0.0, testReport.getBudget());

        testReport.setBudget(50.0);
        assertEquals(50.0, testReport.getBudget());

        testReport.setBudget(1900.49);
        assertEquals(1900.49, testReport.getBudget());
    }

    @Test
    public void testAddSingleEntry(){
        testReport.addEntry(foodEntrySmall1);
        testReport.setBudget(50.0);
        //test monthly report
        assertEquals(3.5, testReport.getTotalExpense());
        assertEquals(50.0 - 3.5, testReport.getRemainedBudget());
        // test Category
        assertEquals(3.5, categories.getFood().getAmount());
        assertEquals(0.0,categories.getUtilities().getAmount());

    }

    @Test
    public void testAddMultEntry() {

        testReport.addEntry(foodEntrySmall1);
        testReport.addEntry(foodEntrySmall2);
        testReport.addEntry(utilityEntrySmall);

        testReport.setBudget(1000.0);

        //test monthly report
        assertEquals(3.5 + 9.9 + 61.01, testReport.getTotalExpense());
        assertEquals(1000.0 - 3.5 - 9.9 - 61.01, testReport.getRemainedBudget());
        // test Category
        Categories curCats = testReport.getCategories();
        assertEquals(3.5 + 9.9, curCats.getFood().getAmount());
        assertEquals(61.01, curCats.getUtilities().getAmount());

        assertFalse(testReport.getIfExceeded());
        assertEquals("Nice! You are under budget!",testReport.getReminder());

        assertEquals("Report:\n"+ "Food & Grocery : $13.4 " + "\nUtilities : $61.01 " + "\nDaily Necessities : $0.0 " + "\nOthers : $0.0 \n" +
         "\ntotalExpense : $74.41\n" + "budget : $1000.0\n" + "remainedBudget : $925.59\n" +
                "\nNice! You are under budget!", testReport.toString());
        assertEquals("Food & Grocery : $13.4 \n", categories.getFood().toString());
    }

    @Test
    public void testDeleteMultEntryExceeded(){

        testReport.addEntry(foodEntrySmall1);
        testReport.addEntry(otherEntryBig);
        testReport.addEntry(utilityEntrySmall);


        assertEquals(foodEntrySmall1, testReport.getExpenseFromIndex(0));
        assertEquals(otherEntryBig, testReport.getExpenseFromIndex(1));
        assertEquals(utilityEntrySmall, testReport.getExpenseFromIndex(2));
        assertEquals(3,testReport.getExpenses().size());

        testReport.setBudget(1200.0);
        assertEquals(1200.0 - 3.5 - 61.01 - 1250, testReport.getRemainedBudget());
        assertTrue(testReport.getIfExceeded());
        assertEquals("Oh no, better keep on eye on your expenses!",testReport.getReminder());

        assertEquals("Report:\n"+ "Food & Grocery : $3.5 " + "\nUtilities : $61.01 " + "\nDaily Necessities : $0.0 " + "\nOthers : $1250.0 \n" +
                        "\ntotalExpense : $1314.51\n" + "budget : $1200.0\n" + "remainedBudget : $-114.51\n" +
                        "\nOh no, better keep on eye on your expenses!", testReport.toString());

        testReport.deleteOneEntry(otherEntryBig);
        assertEquals(foodEntrySmall1, testReport.getExpenseFromIndex(0));
        assertEquals(utilityEntrySmall, testReport.getExpenseFromIndex(1));
        assertEquals(2,testReport.getExpenses().size());

        //test monthly report
        assertEquals(3.5 + 61.01, testReport.getTotalExpense());
        assertEquals(1200.0 - 3.5 - 61.01, testReport.getRemainedBudget());
        // test Category
        assertEquals(3.5, categories.getFood().getAmount());
        assertEquals(61.01,categories.getUtilities().getAmount());
        assertEquals(0.0,testReport.getCategories().getOthers().getAmount());

    }

}