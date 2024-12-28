package presenters;

import information.AnimalSpecification;
import information.GenomeSpecification;
import information.MapSpecification;
import information.WaterSpecification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import maps.Earth;
import maps.WaterMap;
import maps.WorldMap;
import model.*;
import simulation.Simulation;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigurationPresenter {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(8);

    @FXML
    private TextField configNameField;
    @FXML
    private Spinner<Integer> mapWidthField;
    @FXML
    private Spinner<Integer> mapHeightField;
    @FXML
    private ComboBox<String> mapTypeField;
    @FXML
    private Spinner<Integer> waterAmountField;
    @FXML
    private Spinner<Integer> waterRangeField;
    @FXML
    private Spinner<Integer> plantsAmountField;
    @FXML
    private Spinner<Integer> animalsAmountField;
    @FXML
    private Spinner<Integer> dailyPlantsGrowField;
    @FXML
    private Spinner<Integer> startingEnergyField;
    @FXML
    private Spinner<Integer> energyFromEatingField;
    @FXML
    private Spinner<Integer> reproductionMinEnergyField;
    @FXML
    private Spinner<Integer> reproductionCostField;
    @FXML
    private Spinner<Integer> minMutationsField;
    @FXML
    private Spinner<Integer> maxMutationsField;
    @FXML
    private Spinner<Integer> genomeLengthField;
    @FXML
    private ComboBox<String> mutationTypeField;
    @FXML
    private CheckBox exportStatsToCSVField;

    @FXML
    private HBox waterHBox;
    @FXML
    private HBox waterAmountBox;
    @FXML
    private HBox waterRangeBox;


    private WorldMap configureMap() {
        Boundary bounds = new Boundary(mapWidthField.getValue(), mapHeightField.getValue());
        MapType mapType = switch(mapTypeField.getValue()) {
            case "Kula ziemska" -> MapType.EARTH;
            case "Przypływy i odpływy" -> MapType.WATER_MAP;
            default -> MapType.EARTH;
        };
        MapSpecification mapSpec = new MapSpecification(bounds, plantsAmountField.getValue(), animalsAmountField.getValue(),
                dailyPlantsGrowField.getValue(), mapType);
        if(mapType == MapType.EARTH) {
            return new Earth(mapSpec);
        } else {
            WaterSpecification waterSpec = configureWater();
            return new WaterMap(mapSpec, waterSpec);
        }
    }
    private GenomeSpecification configureGenome() {
        MutationType mutationType = switch(mutationTypeField.getValue()) {
            case "Pełna losowość" -> MutationType.COMPLETE_RANDOMNESS;
            case "Lekka korekta" -> MutationType.SLIGHT_CORRECTION;
            default -> MutationType.COMPLETE_RANDOMNESS;
        };
        GenomeSpecification genomeSpec = new GenomeSpecification(minMutationsField.getValue(),
                maxMutationsField.getValue(), genomeLengthField.getValue(), mutationType);
        return genomeSpec;
    }
    private AnimalSpecification configureAnimal() {
        AnimalSpecification animalSpec = new AnimalSpecification(startingEnergyField.getValue(),
                energyFromEatingField.getValue(), reproductionMinEnergyField.getValue(),
                reproductionCostField.getValue(), configureGenome());
        return animalSpec;
    }
    private WaterSpecification configureWater() {
        WaterSpecification waterSpec = new WaterSpecification(waterAmountField.getValue(), waterRangeField.getValue());
        return waterSpec;
    }

    @FXML
    public void onSimulationStartClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/simulation.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        WorldMap map = configureMap();
        presenter.setWorldMap(map);
        if(exportStatsToCSVField.isSelected()) {
            map.addObserver(new StatisticsSaver(map));
        }
        Simulation simulation = new Simulation(map, configureAnimal());
        presenter.setSimulation(simulation);

        threadPool.submit(simulation);
        Stage stage = new Stage();
        configureStage(stage, viewRoot);
    }
    private void configureStage(Stage stage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.setTitle("New simulation");
        stage.minWidthProperty().bind(viewRoot.widthProperty());
        stage.minHeightProperty().bind(viewRoot.heightProperty());
        stage.show();
    }

    public void onMapSelected() {
        String selectedOption = mapTypeField.getValue();

        if(selectedOption.equals("Kula ziemska")) {
            waterHBox.setVisible(false);
            waterHBox.setManaged(false);
            waterAmountBox.setVisible(false);
            waterAmountBox.setManaged(false);
            waterRangeBox.setVisible(false);
            waterRangeBox.setManaged(false);
        }
        else if(selectedOption.equals("Przypływy i odpływy")) {
            waterHBox.setVisible(true);
            waterHBox.setManaged(true);
            waterAmountBox.setVisible(true);
            waterAmountBox.setManaged(true);
            waterRangeBox.setVisible(true);
            waterRangeBox.setManaged(true);
        }
    }

    public void onSaveConfigClicked() {
        Configuration config;
        try {
            String[] configTable = {
                    configNameField.getText(),
                    String.valueOf(mapWidthField.getValue()),
                    String.valueOf(mapHeightField.getValue()),
                    mapTypeField.getValue(),
                    String.valueOf(waterAmountField.getValue()),
                    String.valueOf(waterRangeField.getValue()),
                    String.valueOf(plantsAmountField.getValue()),
                    String.valueOf(animalsAmountField.getValue()),
                    String.valueOf(dailyPlantsGrowField.getValue()),
                    String.valueOf(startingEnergyField.getValue()),
                    String.valueOf(energyFromEatingField.getValue()),
                    String.valueOf(reproductionMinEnergyField.getValue()),
                    String.valueOf(reproductionCostField.getValue()),
                    String.valueOf(minMutationsField.getValue()),
                    String.valueOf(maxMutationsField.getValue()),
                    String.valueOf(genomeLengthField.getValue()),
                    mutationTypeField.getValue(),
                    String.valueOf(exportStatsToCSVField.isSelected())
            };
            config = new Configuration(configTable);

            if(config.getConfigName().isEmpty()) {
                return;
            }
            if(ConfigurationStorage.findConfig(config.getConfigName()) != null) {
                return;
            }

            ConfigurationStorage.saveConfiguration(config.getConfigTable());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void onLoadConfigClicked() {
        try {
            String[] configs = ConfigurationStorage.getConfigNames();

            if(configs.length == 0) {
                return;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(configs[0], configs);
            dialog.setTitle("Wczytaj konfigurację");
            dialog.setHeaderText("Wczytaj konfigurację");
            dialog.setContentText("Wybierz konfigurację: ");

            Optional<String> selectedOption = dialog.showAndWait();

            selectedOption.ifPresent(selectedConfig -> {
                try {
                    String[] config = ConfigurationStorage.findConfig(selectedConfig);
                    if(config != null) {
                        loadConfig(config);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadConfig(String[] configTable) {
        configNameField.setText(configTable[0]);
        mapWidthField.getValueFactory().setValue(Integer.parseInt(configTable[1]));
        mapHeightField.getValueFactory().setValue(Integer.parseInt(configTable[2]));
        mapTypeField.setValue(configTable[3]);
        waterAmountField.getValueFactory().setValue(Integer.parseInt(configTable[4]));
        waterRangeField.getValueFactory().setValue(Integer.parseInt(configTable[5]));
        plantsAmountField.getValueFactory().setValue(Integer.parseInt(configTable[6]));
        animalsAmountField.getValueFactory().setValue(Integer.parseInt(configTable[7]));
        dailyPlantsGrowField.getValueFactory().setValue(Integer.parseInt(configTable[8]));
        startingEnergyField.getValueFactory().setValue(Integer.parseInt(configTable[9]));
        energyFromEatingField.getValueFactory().setValue(Integer.parseInt(configTable[10]));
        reproductionMinEnergyField.getValueFactory().setValue(Integer.parseInt(configTable[11]));
        reproductionCostField.getValueFactory().setValue(Integer.parseInt(configTable[12]));
        minMutationsField.getValueFactory().setValue(Integer.parseInt(configTable[13]));
        maxMutationsField.getValueFactory().setValue(Integer.parseInt(configTable[14]));
        genomeLengthField.getValueFactory().setValue(Integer.parseInt(configTable[15]));
        mutationTypeField.setValue(configTable[16]);
        exportStatsToCSVField.setSelected(Boolean.parseBoolean(configTable[17]));
    }
}
