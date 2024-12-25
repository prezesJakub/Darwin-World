package presenters;

import information.AnimalSpecification;
import information.GenomeSpecification;
import information.MapSpecification;
import information.WaterSpecification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import maps.AbstractWorldMap;
import maps.Earth;
import maps.WaterMap;
import maps.WorldMap;
import model.Boundary;
import model.MapType;
import model.MutationType;
import objects.Water;
import simulation.Simulation;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigurationPresenter {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);

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
            waterAmountBox.setVisible(false);
            waterAmountBox.setManaged(false);
            waterRangeBox.setVisible(false);
            waterRangeBox.setManaged(false);
        }
        else if(selectedOption.equals("Przypływy i odpływy")) {
            waterAmountBox.setVisible(true);
            waterAmountBox.setManaged(true);
            waterRangeBox.setVisible(true);
            waterRangeBox.setManaged(true);
        }
    }
}
