package maps;

import information.MapSpecification;
import model.Vector2d;

public class Earth extends AbstractWorldMap {

    public Earth(MapSpecification mapSpec) {
        super(mapSpec);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        MapSpecification mapSpec = this.getMapSpec();
        return position.precedes(new Vector2d(Integer.MAX_VALUE, mapSpec.bounds().getHeight()-1)) &&
                position.follows(new Vector2d(Integer.MIN_VALUE,0));
    }

    @Override
    public Vector2d fixPosition(Vector2d position) {
        if(position.getX()<0) {
            return new Vector2d(this.getWidth()-1, position.getY());
        }
        else if(position.getX()>this.getWidth()-1) {
            return new Vector2d(0, position.getY());
        }
        return position;
    }
}
