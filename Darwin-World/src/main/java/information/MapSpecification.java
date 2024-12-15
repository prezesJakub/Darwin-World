package information;

import maps.WorldMap;

public record MapSpecification(
        int mapWidth,
        int mapHeight,
        int startingPlantsAmount,
        int startingAnimalsAmount,
        int dailyPlantsGrowth,
        int mapType
) {
}
