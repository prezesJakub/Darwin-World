package presenters;

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


    private WorldMap configureMap() {
        MapSpecification mapSpec = new MapSpecification(mapWidthField.getValue(),
                mapHeightField.getValue(), plantsAmountField.getValue(), animalsAmountField.getValue(),
                dailyPlantsGrowField.getValue(), 0);
        return new Earth(mapSpec);
    }

    @FXML
    public void onSimulationStartClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/simulation.fxml"));

        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        presenter.setWorldMap(configureMap());

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
