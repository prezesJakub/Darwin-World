package information;

import maps.WorldMap;
import model.Boundary;

public record MapSpecification(
        Boundary bounds,
        int startingPlantsAmount,
        int startingAnimalsAmount,
        int dailyPlantsGrowth,
        int mapType
) {
}
