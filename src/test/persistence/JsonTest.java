package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkClothing(String name, ClothingCategory category, Color color, Clothing clothing) {
        assertEquals(name, clothing.getItem());
        assertEquals(category, clothing.getCategory());
        assertEquals(color, clothing.getColor());
    }
}
