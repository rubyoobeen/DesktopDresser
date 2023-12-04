package model;

import model.exception.ClothingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClosetTest {
    private Closet testCloset;

    @BeforeEach
    void runBefore() {
        testCloset = new Closet("test closet");
    }

    @Test
    void testConstructor() {
        assertEquals("test closet", testCloset.getName());
        assertTrue(testCloset.getClothingsFromCloset().isEmpty());
        assertTrue(testCloset.getOutfitsFromCloset().isEmpty());
    }

    @Test
    void testAddClothingToCloset() {
        Clothing testClothing = new Clothing("test clothing1", ClothingCategory.TOP, Color.WHITE);
        Clothing testSameClothing = new Clothing("test clothing1", ClothingCategory.TOP, Color.WHITE);
        Clothing testDifferentClothing = new Clothing("test clothing2", ClothingCategory.BOT, Color.RED);

        // add same clothing item to closet items
        try {
            testCloset.addClothingToCloset(testClothing);
            testCloset.addClothingToCloset(testSameClothing);
            assertEquals(1, testCloset.getClothingsFromCloset().size());
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }

        // add different clothing item to closet items
        try {
            testCloset.addClothingToCloset(testDifferentClothing);
            assertEquals(2, testCloset.getClothingsFromCloset().size());
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }
    }

    @Test
    void testRemoveClothingFromCloset() {
        Clothing testClothing1 = new Clothing("test clothing1", ClothingCategory.TOP, Color.WHITE);

        // remove existing clothing from closet
        try {
            testCloset.addClothingToCloset(testClothing1);
            assertEquals(1, testCloset.getClothingsFromCloset().size());
            testCloset.removeClothingFromCloset(testClothing1);
            assertEquals(0, testCloset.getClothingsFromCloset().size());
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }

        // remove non-existing clothing from closet
        try {
            testCloset.removeClothingFromCloset(testClothing1);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }
    }

    @Test
    void testAddOutfitToCloset() {
        Outfit testOutfit1 = new Outfit("test outfit1");
        Outfit testSameOutfit = new Outfit("test outfit1");
        Outfit testDifferentOutfit = new Outfit("test outfit2");

        // add same outfit to outfits
        try {
            testCloset.addOutfitToCloset(testOutfit1);
            testCloset.addOutfitToCloset(testSameOutfit);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }

        // add different outfit to outfits
        try {
            testCloset.addOutfitToCloset(testDifferentOutfit);
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }
    }

    @Test
    void testRemoveOutfitFromCloset() {
        Outfit testOutfit = new Outfit("test outfit");

        // remove existing outfit from closet
        try {
            testCloset.addOutfitToCloset(testOutfit);
            testCloset.removeOutfitFromCloset(testOutfit);
        } catch (ClothingException ex) {
            fail("unexpected ClothingException");
        }

        // remove non-existing outfit from closet
        try {
            testCloset.removeOutfitFromCloset(testOutfit);
            fail("expected ClothingException");
        } catch (ClothingException ex) {
            // expected ClothingException
        }
    }
}
