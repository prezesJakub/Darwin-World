package information;

import maps.WorldMap;
import model.Boundary;
import model.MapType;

public record MapSpecification(
        Boundary bounds,
        int startingPlantsAmount,
        int startingAnimalsAmount,
        int dailyPlantsGrowth,
        MapType mapType
) {
}
