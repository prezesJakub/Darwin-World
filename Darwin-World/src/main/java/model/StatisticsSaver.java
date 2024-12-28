package model;

import information.MapStatistics;
import maps.WorldMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsSaver implements MapChangeListener {

    public StatisticsSaver(WorldMap map) throws IOException {
        File file = new File(String.format("statistics/map_%s.csv", map.getId()));
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        try(writer) {
            writer.write("Dzień,");
            writer.write( "Żyjące zwierzęta,");
            writer.write("Wszystkie zwierzęta,");
            writer.write("Ilość roślin,");
            writer.write( "Wolne pola,");
            writer.write("Najpopularniejszy genotyp,");
            writer.write("Średnia energia,");
            writer.write("Średni czas życia,");
            writer.write( "Średnia ilość dzieci");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        File file = new File(String.format("statistics/map_%s.csv", worldMap.getId()));
        MapStatistics stats = worldMap.getMapStats();
        try {
            saveStats(stats.getStatsTable(), file);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void saveStats(String[] stats, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        try(writer) {
            writer.write(String.join(",", stats));
            writer.newLine();
        }
    }
}
