package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DayPlanTest {
    private DayPlan testDayPlan;
    private Clothing top;
    private Clothing bottom;
    private Clothing shoes;
    private List<Clothing> testCloset;

    @BeforeEach
    void setUp() {
        testCloset = new ArrayList<>();
        top = new Clothing("Hoodie", ClothingCategory.TOP, Color.WH, true);
        bottom = new Clothing("Skirt", ClothingCategory.BOT, Color.BK, false);
        shoes = new Clothing("Sandal", ClothingCategory.SHOES, Color.RD, false);

        testCloset.add(top);
        testCloset.add(bottom);

        testDayPlan = new DayPlan(Day.WED, testCloset);
    }

    @Test
    void testAddClothingItem() {
        testDayPlan.addClothingItem(shoes);
        assertTrue(testDayPlan.getClothingItems().contains(shoes));
    }

    @Test
    void testRemoveClothingItem() {
        testDayPlan.removeClothingItem(bottom);
        assertFalse(testDayPlan.getClothingItems().contains(bottom));
    }

    @Test
    void testMeetRequirements() {
        assertFalse(testDayPlan.meetRequirements());

        testDayPlan.addClothingItem(top);
        testDayPlan.addClothingItem(bottom);
        testDayPlan.addClothingItem(shoes);
        assertTrue(testDayPlan.meetRequirements());

        testDayPlan.removeClothingItem(bottom);
        assertFalse(testDayPlan.meetRequirements());
    }
}
