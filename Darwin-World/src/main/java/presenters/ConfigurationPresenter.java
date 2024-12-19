package presenters;

import information.AnimalSpecification;
import information.GenomeSpecification;
import information.MapSpecification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import maps.AbstractWorldMap;
import maps.Earth;
import maps.WorldMap;
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


    private WorldMap configureMap() {
        MapSpecification mapSpec = new MapSpecification(mapWidthField.getValue(),
                mapHeightField.getValue(), plantsAmountField.getValue(), animalsAmountField.getValue(),
                dailyPlantsGrowField.getValue(), 0);
        return new Earth(mapSpec);
    }
    private GenomeSpecification configureGenome() {
        GenomeSpecification genomeSpec = new GenomeSpecification(minMutationsField.getValue(),
                maxMutationsField.getValue(), genomeLengthField.getValue(), 0);
        return genomeSpec;
    }
    private AnimalSpecification configureAnimal() {
        AnimalSpecification animalSpec = new AnimalSpecification(startingEnergyField.getValue(),
                energyFromEatingField.getValue(), reproductionMinEnergyField.getValue(),
                reproductionCostField.getValue(), configureGenome());
        return animalSpec;
    }

    @FXML
    public void onSimulationStartClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/simulation.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        WorldMap map = configureMap();
        presenter.setWorldMap(map);
        Simulation simulation = new Simulation(map, map.getStartingAnimalsAmount(), configureAnimal());
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
}
