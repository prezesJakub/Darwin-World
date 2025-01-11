package maps;

import information.MapSpecification;
import model.Boundary;
import model.MapType;
import model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {
    @Test
    void canMoveToTest() {
        MapSpecification mapSpec = new MapSpecification(new Boundary(10, 10),
                10, 10, 10, MapType.EARTH);
        AbstractWorldMap map = new AbstractWorldMap(mapSpec);

        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(9, 9)));
        assertFalse(map.canMoveTo(new Vector2d(10, 2)));
        assertFalse(map.canMoveTo(new Vector2d(2, 10)));
    }
}