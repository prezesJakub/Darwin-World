package model;

public class Boundary {
    private final int width;
    private final int height;
    private final int upperEquatorBound;
    private final int lowerEquatorBound;

    public Boundary(int width, int height) {
        this.width = width;
        this.height = height;
        this.upperEquatorBound = (int) (0.6 * height);
        this.lowerEquatorBound = (int) (0.4 * height);
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getUpperEquatorBound() {
        return this.upperEquatorBound;
    }
    public int getLowerEquatorBound() {
        return this.lowerEquatorBound;
    }
}
