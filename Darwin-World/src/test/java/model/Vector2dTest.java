package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void precedesTest() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);

        assertTrue(v1.precedes(v2));
        assertFalse(v2.precedes(v1));
    }

    @Test
    void followsTest() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);

        assertFalse(v1.follows(v2));
        assertTrue(v2.follows(v1));
    }

    @Test
    void addTest() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);

        assertEquals(new Vector2d(4, 6), v1.add(v2));
    }

    @Test
    void subtractTest() {
        Vector2d v1 = new Vector2d(5,5);
        Vector2d v2 = new Vector2d(2,4);

        assertEquals(new Vector2d(3, 1), v1.subtract(v2));
    }

    @Test
    void opposite() {
        Vector2d v1 = new Vector2d(1,2);

        assertEquals(new Vector2d(-1, -2), v1.opposite());
    }

    @Test
    void upperRight() {
        Vector2d v1 = new Vector2d(3,2);
        Vector2d v2 = new Vector2d(1,4);

        assertEquals(new Vector2d(3, 4), v1.upperRight(v2));
    }

    @Test
    void lowerLeft() {
        Vector2d v1 = new Vector2d(3,2);
        Vector2d v2 = new Vector2d(1,4);

        assertEquals(new Vector2d(1, 2), v1.lowerLeft(v2));
    }
}