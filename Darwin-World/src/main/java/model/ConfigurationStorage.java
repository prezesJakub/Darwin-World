package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationStorage {
    private static final String CSV_FILE = "src/main/resources/configurations.csv";

    public static void saveConfiguration(String[] config) throws IOException {
        FileWriter fileWriter = new FileWriter(CSV_FILE, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        try(writer) {
            writer.write(String.join(",", config));
            writer.newLine();
        }
    }

    public static List<String[]> loadAllConfigurations() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));

        List<String[]> configurations = new ArrayList<>();

        try(reader) {
            String line;
            while ((line = reader.readLine()) != null) {
                configurations.add(line.split(","));
            }
        }
        return configurations;
    }

    public static String[] findConfig(String name) throws IOException {
        List<String[]> configs = loadAllConfigurations();
        for(String[] config : configs) {
            if(config[0].equals(name)) {
                return config;
            }
        }
        return null;
    }

    public static String[] getConfigNames() throws IOException {
        List<String[]> configs = loadAllConfigurations();
        return configs.stream().map(config -> config[0]).toArray(String[]::new);
    }
}
