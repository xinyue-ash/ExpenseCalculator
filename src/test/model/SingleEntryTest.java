
package model;

import model.exceptions.MonthNotInRangeException;
import model.exceptions.NegativeAmountException;
import model.exceptions.YearNotInRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// test the functions of a single expense entry
public class SingleEntryTest {

    private SingleExpenseEntry testEmptyEntry;
    private SingleExpenseEntry testEntry;
    private Category food;
    private Category utilities;


    @BeforeEach
    public void runBefore() {
        food = new Category("Food & Grocery");
        utilities = new Category("Utilities");
        testEmptyEntry = new SingleExpenseEntry();
        testEntry = new SingleExpenseEntry(3.5,"Milk",food,2021,9,1);

    }

    @Test
    public void testEmptyConstructor(){
        assertEquals(0, testEmptyEntry.getAmount());
        assertEquals("", testEmptyEntry.getDescription());
        assertEquals(null, testEmptyEntry.getCategory());
        assertEquals(0, testEmptyEntry.getYear());
        assertEquals(0, testEmptyEntry.getMonth());
        assertEquals(0, testEmptyEntry.getDay());

    }

    @Test
    public void testSingleEntryConstructor(){
        assertEquals(3.5, testEntry.getAmount());
        assertEquals("Milk", testEntry.getDescription());
        assertEquals("Food & Grocery", testEntry.getCategory().getCategoryName());
        assertEquals(2021, testEntry.getYear());
        assertEquals(9, testEntry.getMonth());
        assertEquals(1,testEntry.getDay());
    }


    @Test
    public void testModifyAmount(){
        try {
            testEmptyEntry.setAmount(10.0);
        } catch (NegativeAmountException e) {
            fail("Should not throw NegativeAmountException");
        }
        assertEquals(10.0,testEmptyEntry.getAmount());

        try {
            testEntry.setAmount(10.0);
        } catch (NegativeAmountException e) {
            fail("Should not throw NegativeAmountException");
        }
        assertEquals(10.0,testEntry.getAmount());
    }

    @Test
    public void testNegativeAmount(){
        try {
            testEmptyEntry.setAmount(-1);
            fail("Should throw NegativeAmountException");
        } catch (NegativeAmountException e) {
            //expected
        }
        assertEquals(0.0,testEmptyEntry.getAmount());
    }

    @Test
    public void testModifyCategory(){
        testEmptyEntry.setCategory(utilities);
        assertEquals("Utilities",testEmptyEntry.getCategory().getCategoryName());

        testEntry.setCategory(utilities);
        assertEquals("Utilities",testEntry.getCategory().getCategoryName());
    }

    @Test
    public void testModifyDescription(){
        testEmptyEntry.setDescription("koodo");
        assertEquals("koodo",testEmptyEntry.getDescription());

        testEntry.setDescription("i forget what is this");
        assertEquals("i forget what is this",testEntry.getDescription());
    }

    @Test
    public void testModifyDay(){

        testEmptyEntry.setDay(10);
        assertEquals(10,testEmptyEntry.getDay());

        testEntry.setDay(13);
        assertEquals(13,testEntry.getDay());
    }

    @Test
    public void testModifyMonth(){
        try {
            testEmptyEntry.setMonth(11);
        } catch (MonthNotInRangeException e) {
            fail("Should not throw MonthNotInRangeException here");
        }
        assertEquals(11,testEmptyEntry.getMonth());

        try {
            testEntry.setMonth(12);
        } catch (MonthNotInRangeException me) {
            fail("Should not throw MonthNotInRangeException here");
        }
        assertEquals(12,testEntry.getMonth());
    }

    @Test
    public void testOutOfRangeMonthBig(){
        try {
            testEmptyEntry.setMonth(0);
            fail("Should throw MonthNotInRangeException");
        } catch (MonthNotInRangeException me) {
            // expected
        }
        assertEquals(0,testEmptyEntry.getMonth());
    }

    @Test
    public void testOutOfRangeMonthSmall(){
        try {
            testEmptyEntry.setMonth(13);
            fail("Should throw YearNotInRangeException");
        } catch (MonthNotInRangeException me) {
            // expected
        }
        assertEquals(0,testEmptyEntry.getMonth());


    }



    @Test
    public void testModifyYear(){
        try {
            testEmptyEntry.setYear(2019);
        } catch (YearNotInRangeException e) {
            fail("Should not throw YearNotInRangeException here");
        }
        assertEquals(2019,testEmptyEntry.getYear());
        try {
            testEntry.setYear(2018);
        } catch (YearNotInRangeException e) {
            fail("Should not throw exception here");
        }
        assertEquals(2018,testEntry.getYear());

    }

    @Test
    public void testOutOfRangeYearBig(){
        try {
            testEmptyEntry.setYear(2051);
            fail("Should throw YearNotInRangeException");
        } catch (YearNotInRangeException e) {
            // expected
        }
        assertEquals(0,testEmptyEntry.getYear());
    }

    @Test
    public void testOutOfRangeYearSmall(){
        try {
            testEmptyEntry.setYear(2014);
            fail("Should throw YearNotInRangeException");
        } catch (YearNotInRangeException e) {
            // expected
        }
        assertEquals(0,testEmptyEntry.getYear());

    }

    @Test
    public void testToString(){

        String s = "2021,9,1 : amount ($) : 3.5, category :Food & Grocery, description: Milk\n";
        assertEquals(s ,testEntry.toString());
    }

    @Test
    public void testToStringArray(){
        String[] stringArray = {"2021","9","1", "3.5", "Food & Grocery", "Milk"};
        assertEquals(Arrays.toString(stringArray) ,Arrays.toString(testEntry.toStringArray()));
    }


}
