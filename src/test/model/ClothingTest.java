package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClothingTest {
    private Clothing testClothing;

    @BeforeEach
    void runBefore() {
        testClothing = new Clothing("Shirt", ClothingCategory.TOP, Color.WH, true);
    }

    @Test
    void testConstructor() {
        assertEquals("Shirt", testClothing.getItem());
        assertEquals(ClothingCategory.TOP, testClothing.getCategory());
        assertEquals(Color.WH, testClothing.getColor());
        assertTrue(testClothing.isClean());
    }

    @Test
    void testSetClean() {
        testClothing.setDirty();
        assertFalse(testClothing.isClean());
        testClothing.setClean();
        assertTrue(testClothing.isClean());
    }

    @Test
    void testSetDirty() {
        testClothing.setClean();
        assertTrue(testClothing.isClean());
        testClothing.setDirty();
        assertFalse(testClothing.isClean());
    }
}
