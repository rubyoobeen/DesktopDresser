package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DayPlanTest {
    private DayPlan testDayPlan;
    private Clothing top;
    private Clothing bottom;
    private Clothing shoes;

    @BeforeEach
    void setUp() {
        testDayPlan = new DayPlan(Day.FRI);
        top = new Clothing("Hoodie", ClothingCategory.TOP, Color.WH);
        bottom = new Clothing("Skirt", ClothingCategory.BOT, Color.BK);
        shoes = new Clothing("Sandal", ClothingCategory.SHOES, Color.RD);
    }

    @Test
    void testAddClothingItem() {
        testDayPlan.addClothingItem(top);
        assertTrue(testDayPlan.getClothingItems().contains(top));
    }

    @Test
    void testRemoveClothingItem() {
        testDayPlan.addClothingItem(top);
        testDayPlan.addClothingItem(bottom);
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
