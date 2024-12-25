package objects;

import model.MapElement;
import model.Vector2d;

public class Water implements MapElement {
    private final Vector2d position;
    private final int maxRange;
    private int actualState = 0;
    private boolean inflow = true;

    public Water(Vector2d position, int maxRange) {
        this.position = position;
        this.maxRange = maxRange;
    }

    public void changeWaterState() {
        if (inflow) {
            actualState++;
            if (actualState == maxRange) {
                inflow = false;
            }
        } else {
            actualState--;
            if (actualState == 0) {
                inflow = true;
            }
        }
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
    public int getActualState() {
        return this.actualState;
    }
}
