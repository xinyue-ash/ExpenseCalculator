package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
// this class is testing the functions of a category list
public class CategoriesTest {
    private Categories categories;

    @BeforeEach
    public void runBefore(){
        categories = new Categories();
    }

    @Test
    public void testGetters(){
        assertEquals("Food & Grocery", categories.getFood().getCategoryName());
        assertEquals("Utilities", categories.getUtilities().getCategoryName());
        assertEquals("Daily Necessities", categories.getNecessities().getCategoryName());
        assertEquals("Others", categories.getOthers().getCategoryName());
    }

    @Test
    public void testCatogeryList(){
        assertEquals(4, categories.getCategoryList().size());
        assertEquals("Food & Grocery", categories.getFood().getCategoryName());
        assertEquals("Utilities", categories.getUtilities().getCategoryName());
        assertEquals("Daily Necessities", categories.getNecessities().getCategoryName());
        assertEquals("Others", categories.getOthers().getCategoryName());
    }










}
