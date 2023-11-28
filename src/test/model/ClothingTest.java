package model;

import model.exception.ClothingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClothingTest {
    private Clothing testClothing;

    @BeforeEach
    void runBefore() {
        testClothing = new Clothing("test clothing", ClothingCategory.TOP, Color.WHITE);
    }

    @Test
    void testConstructor() {
        assertEquals("test clothing", testClothing.getItem());
        assertEquals(ClothingCategory.TOP, testClothing.getCategory());
        assertEquals(Color.WHITE, testClothing.getColor());
        assertTrue(testClothing.isClean());
        assertEquals(0, testClothing.getTimesUsed());
    }

    @Test
    void testSetDirty() {
        try {
            testClothing.setDirty();
            assertFalse(testClothing.isClean());
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }

        try {
            testClothing.setDirty();
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }
    }

    @Test
    void testUse() {
        try {
            testClothing.use();
            assertEquals(1, testClothing.getTimesUsed());
            testClothing.use();
            testClothing.use();
            testClothing.use();
            testClothing.use();
            assertEquals(5, testClothing.getTimesUsed());
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }
    }

    @Test
    void testWash() {
        try {
            testClothing.setDirty();
            assertFalse(testClothing.isClean());
            testClothing.setClean();
            assertTrue(testClothing.isClean());
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }

        try {
            testClothing.setClean();
            assertTrue(testClothing.isClean());
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }
    }

    @Test
    void testEquals() {
        Clothing testDifferentItem = new Clothing("test clothing1", ClothingCategory.TOP, Color.WHITE);
        assertFalse(testClothing.equals(testDifferentItem));

        Clothing testDifferentCategory = new Clothing("test clothing", ClothingCategory.BOT, Color.WHITE);
        assertFalse(testClothing.equals(testDifferentCategory));

        Clothing testDifferentColor = new Clothing("test clothing", ClothingCategory.TOP, Color.GREEN);
        assertFalse(testClothing.equals(testDifferentColor));

        Clothing testSame = new Clothing("test clothing", ClothingCategory.TOP, Color.WHITE);
        assertTrue(testClothing.equals(testSame));
    }
}
