package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//test the functions of a single Category object
public class SingleCategoryTest {

    double amount;
    private Category category;

    @BeforeEach
    public void runBefore() {
        amount = 1.1;
        category = new Category("Food");
    }

    @Test
    void getAmount() {
        assertEquals(0.0,category.getAmount());
    }

    @Test
    void addAmount() {
        category.addAmount(amount);
        assertEquals(1.1,category.getAmount());
    }

    @Test
    void deleteAmount() {
        category.addAmount(amount);
        category.addAmount(amount);
        category.deleteAmount(amount);
        category.deleteAmount(amount);
        assertEquals(0.0,category.getAmount());
    }

    @Test
    void getCategoryName() {
        assertEquals("Food",category.getCategoryName());
    }

    @Test
    void testEquals() {
        Category category1 = new Category("Food");
        category1.addAmount(10);
        Category category2 = new Category("Drinks");
        category2.addAmount(0.0);
        assertTrue(category.equals(category1));
        assertFalse(category.equals(category2));

    }

    @Test
    void testToString() {
        assertEquals("Food : $0.0 \n",category.toString());
    }



}
