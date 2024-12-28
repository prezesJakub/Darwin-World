package model;

import java.util.StringJoiner;

public class Configuration {
    private final String configName;
    private final int mapWidth;
    private final int mapHeight;
    private final String mapType;
    private final int waterAmount;
    private final int maxWaterRange;
    private final int plantsAmount;
    private final int animalsAmount;
    private final int dailyPlantsGrow;
    private final int startEnergy;
    private final int energyFromEating;
    private final int reproductionMinEnergy;
    private final int reproductionCost;
    private final int minMutations;
    private final int maxMutations;
    private final int genomeLength;
    private final String mutationType;
    private final boolean exportStatsToCSV;

    public Configuration(String[] configTable) {
        this.configName = configTable[0];
        this.mapWidth = Integer.parseInt(configTable[1]);
        this.mapHeight = Integer.parseInt(configTable[2]);
        this.mapType = configTable[3];
        this.waterAmount = Integer.parseInt(configTable[4]);
        this.maxWaterRange = Integer.parseInt(configTable[5]);
        this.plantsAmount = Integer.parseInt(configTable[6]);
        this.animalsAmount = Integer.parseInt(configTable[7]);
        this.dailyPlantsGrow = Integer.parseInt(configTable[8]);
        this.startEnergy = Integer.parseInt(configTable[9]);
        this.energyFromEating = Integer.parseInt(configTable[10]);
        this.reproductionMinEnergy = Integer.parseInt(configTable[11]);
        this.reproductionCost = Integer.parseInt(configTable[12]);
        this.minMutations = Integer.parseInt(configTable[13]);
        this.maxMutations = Integer.parseInt(configTable[14]);
        this.genomeLength = Integer.parseInt(configTable[15]);
        this.mutationType = configTable[16];
        this.exportStatsToCSV = Boolean.parseBoolean(configTable[17]);
    }
    public String[] getConfigTable() {
        return new String[]{
              configName,
              String.valueOf(mapWidth),
              String.valueOf(mapHeight),
              mapType,
              String.valueOf(waterAmount),
              String.valueOf(maxWaterRange),
              String.valueOf(plantsAmount),
              String.valueOf(animalsAmount),
              String.valueOf(dailyPlantsGrow),
              String.valueOf(startEnergy),
              String.valueOf(energyFromEating),
              String.valueOf(reproductionMinEnergy),
              String.valueOf(reproductionCost),
              String.valueOf(minMutations),
              String.valueOf(maxMutations),
              String.valueOf(genomeLength),
              mutationType,
              String.valueOf(exportStatsToCSV)
        };
    }

    public String getConfigName() {
        return this.configName;
    }
}
