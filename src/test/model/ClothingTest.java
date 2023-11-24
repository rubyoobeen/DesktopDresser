package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClothingTest {
    private Clothing testClothing;

    @BeforeEach
    void runBefore() {
        testClothing = new Clothing("T-Shirt", ClothingCategory.TOP, Color.WHITE);
    }

    @Test
    void testConstructor() {
        assertEquals("T-Shirt", testClothing.getItem());
        assertEquals(ClothingCategory.TOP, testClothing.getCategory());
        assertEquals(Color.WHITE, testClothing.getColor());
        assertTrue(testClothing.isClean());
    }

    @Test
    void testUse() {
        Clothing clothing = new Clothing("Shirt", ClothingCategory.TOP, Color.ORANGE);

        // item is clean
        assertTrue(clothing.isClean());

        // using item 1 time
        clothing.worn();
        assertEquals(1, clothing.getTimesWorn());
        clothing.worn();
        clothing.worn();
        clothing.worn();
        clothing.worn();

        // using item 5 times
        assertEquals(5, clothing.getTimesWorn());

        // try using more than 5 times
        clothing.worn();
        assertFalse(clothing.isClean());

        // timesUsed stay the same
        assertEquals(5, clothing.getTimesWorn());
    }

    @Test
    void testWashClean() {
        Clothing clothing = new Clothing("Sneakers", ClothingCategory.SHOES, Color.MIX);

        // use item less than 5 times
        assertTrue(clothing.isClean());
        clothing.worn();
        clothing.worn();
        // item is still clean
        assertTrue(clothing.isClean());
        assertEquals(2, clothing.getTimesWorn());
        // cannot wash item and timesUsed stays same
        clothing.wash();
        assertEquals(2, clothing.getTimesWorn());
    }

    @Test
    void testWashDirty() {
        Clothing clothing = new Clothing("Jeans", ClothingCategory.BOT, Color.BLACK);

        // item is clean
        assertTrue(clothing.isClean());
        clothing.worn();
        clothing.worn();
        clothing.worn();
        clothing.worn();
        clothing.worn();

        // item is dirty
        assertFalse(clothing.isClean());

        // item is clean after wash
        clothing.wash();
        assertTrue(clothing.isClean());
    }
}
