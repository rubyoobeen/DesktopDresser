package model;

import model.exception.ClothingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class OutfitTest {
    private Outfit testOutfit;
    private Clothing testTop;
    private Clothing testBot;
    private Clothing testDress;

    @BeforeEach
    void runBefore() {
        testOutfit = new Outfit("test outfit");
        testTop = new Clothing("test shirt", ClothingCategory.TOP, Color.BLACK);
        testBot = new Clothing("test pants", ClothingCategory.BOT, Color.ORANGE);
        testDress = new Clothing("test dress", ClothingCategory.DRESS, Color.PINK);
    }

    @Test
    void testConstructor() {
        assertEquals("test outfit", testOutfit.getName());
        assertTrue(testOutfit.getCollection().isEmpty());
        assertTrue(testOutfit.getUsedCategories().isEmpty());
    }

    @Test
    void testRemoveClothingFromOutfit() {
        Clothing testAcc = new Clothing("test acc", ClothingCategory.ACC, Color.WHITE);

        try {
            testOutfit.addClothingToOutfit(testAcc);
            assertEquals(1, testOutfit.getCollection().size());
            testOutfit.removeClothingFromOutfit(testAcc);
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }

        try {
            testOutfit.removeClothingFromOutfit(testAcc);
            assertEquals(0, testOutfit.getCollection().size());
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // unexpected ClothingException
        }
    }

    @Test
    void testAddAllCategoriesToOutfit() {
        Clothing testOuter = new Clothing("test outer", ClothingCategory.OUTER, Color.BLACK);
        Clothing testAcc = new Clothing("test acc", ClothingCategory.ACC, Color.GREEN);
        Clothing testShoes = new Clothing("test shoes", ClothingCategory.SHOES, Color.MIX);

        try {
            testOutfit.addClothingToOutfit(testTop);
            testOutfit.addClothingToOutfit(testBot);
            testOutfit.addClothingToOutfit(testOuter);
            testOutfit.addClothingToOutfit(testAcc);
            testOutfit.addClothingToOutfit(testShoes);
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }
    }

    @Test
    void testAddDuplicateToOutfit() {
        Clothing otherColorTop = new Clothing("test shirt", ClothingCategory.TOP, Color.MIX);
        Clothing otherNameTop = new Clothing("test shirt1", ClothingCategory.TOP, Color.BLACK);

        try {
            testOutfit.addClothingToOutfit(testTop);
            assertTrue(testOutfit.getUsedCategories().contains(ClothingCategory.TOP));
            assertTrue(testOutfit.getCollection().contains(testTop));
            testOutfit.addClothingToOutfit(otherColorTop);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }

        try {
            testOutfit.addClothingToOutfit(otherNameTop);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }
    }

    @Test
    void testAddTopRestriction() {
        try {
            testOutfit.addClothingToOutfit(testDress);
            testOutfit.addClothingToOutfit(testTop);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected fail("expected ClothingException");
        }

        try {
            testOutfit.addClothingToOutfit(testBot);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected fail("expected ClothingException");
        }
    }

    @Test
    void testAddBotRestriction(){
        try {
            testOutfit.addClothingToOutfit(testTop);
            testOutfit.addClothingToOutfit(testDress);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected fail("expected ClothingException");
        }
    }

    @Test
    void testEquals() {
        Outfit testSameNameOutfit = new Outfit("test outfit");
        assertTrue(testOutfit.equals(testSameNameOutfit));
        Outfit testDifferentOutfit = new Outfit("test outfit1");
        assertFalse(testOutfit.equals(testDifferentOutfit));
    }
}
