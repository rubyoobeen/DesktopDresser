package persistence;

import model.*;
import model.exception.ClothingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() throws Exception {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Closet c = reader.read();
            fail("Exception expected");
        } catch (Exception ex) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCloset() throws ClothingException {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCloset.json");
        try {
            Closet c = reader.read();
            assertEquals("my closet", c.getName());
            assertEquals(0, c.getClothingsFromCloset().size());
        } catch (Exception ex) {
            fail("couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCloset() throws ClothingException{
        JsonReader reader = new JsonReader("./data/testReaderGeneralCloset.json");
        try {
            Closet c = reader.read();
            assertEquals("my closet", c.getName());
            List<Clothing> clothings = c.getClothingsFromCloset();
        } catch (Exception ex) {
            fail("could't read from file");
        }
    }
}
