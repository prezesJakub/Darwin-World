package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundaryTest {

    @Test
    void getUpperEquatorBoundTest() {
        Boundary bounds = new Boundary(10, 10);
        assertEquals(6, bounds.getUpperEquatorBound());
    }

    @Test
    void getLowerEquatorBoundTest() {
        Boundary bounds = new Boundary(10, 10);

        assertEquals(4, bounds.getLowerEquatorBound());
    }

    @Test
    void isEquatorTest() {
        Boundary bounds = new Boundary(10, 10);
        Vector2d pos1 = new Vector2d(5, 5);
        Vector2d pos2 = new Vector2d(3, 6);
        Vector2d pos3 = new Vector2d(3, 4);
        Vector2d pos4 = new Vector2d(3, 2);
        Vector2d pos5 = new Vector2d(2, 9);

        assertTrue(bounds.isEquator(pos1));
        assertTrue(bounds.isEquator(pos2));
        assertTrue(bounds.isEquator(pos3));
        assertFalse(bounds.isEquator(pos4));
        assertFalse(bounds.isEquator(pos5));
    }
}