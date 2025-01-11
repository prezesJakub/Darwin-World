package objects;

import model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterTest {

    @Test
    void changeWaterStateTest() {
        Water water = new Water(new Vector2d(3,3), 3);

        water.changeWaterState();

        assertEquals(1, water.getActualState());

        water.changeWaterState();
        water.changeWaterState();

        assertEquals(3, water.getActualState());

        water.changeWaterState();

        assertEquals(2, water.getActualState());
    }
}