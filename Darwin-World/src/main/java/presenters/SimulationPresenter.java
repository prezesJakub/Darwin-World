package presenters;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import maps.WaterMap;
import maps.WorldMap;
import model.MapChangeListener;
import model.Vector2d;
import simulation.Simulation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPresenter implements MapChangeListener {
    private static final int MAP_WIDTH = 700;
    private static final int MAP_HEIGHT = 500;
    private WorldMap map;
    private int mapWidth;
    private int mapHeight;
    private int cellSize;
    private Simulation simulation;

    @FXML
    private GridPane mapGrid;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);

    public void setWorldMap(WorldMap map) {
        this.map = map;
        map.addObserver(this);
        configureScreen();
    }

    private void configureScreen() {
        mapWidth=map.getWidth();
        mapHeight=map.getHeight();
        cellSize = Math.round(Math.min(MAP_WIDTH/(mapWidth+1), MAP_HEIGHT/(mapHeight+1)));
        Platform.runLater(() -> drawMap());
    }

    private void drawMap() {
        clearGrid();
        headerLabel();
        generateTable();
        addPlants();
        addAnimals();
        addWater();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void headerLabel() {
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void generateTable() {
        for(int i=0; i<mapWidth; i++) {
            Label label1 = new Label(Integer.toString(i));
            GridPane.setHalignment(label1, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            mapGrid.add(label1, i+1, 0);
        }
        for(int i=0; i<mapHeight; i++) {
            Label label2 = new Label(Integer.toString(mapHeight-i-1));
            GridPane.setHalignment(label2, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            mapGrid.add(label2, 0, i+1);
        }
    }

    private void addAnimals() {
        List<Vector2d> animalPositions = map.getAnimalPositions();
        for(Vector2d pos : animalPositions) {
            int x = pos.getX();
            int y = pos.getY();
            mapGrid.add(new Label(map.getAnimal(pos).toString()), x+1, mapHeight-y);
            mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
        }
       // System.out.println(map.getAnimalPositions());
    }

    private void addPlants() {
        List<Vector2d> plantPositions = map.getPlantPositions();
        for(Vector2d pos : plantPositions) {
            int x = pos.getX();
            int y = pos.getY();
            mapGrid.add(new Label(map.getPlant(pos).toString()), x+1, mapHeight-y);
            mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
        }
    }

    private void addWater() {
        for(int i=0; i<mapWidth; i++) {
            for(int j=0; j<mapHeight; j++) {
                Vector2d pos = new Vector2d(i, j);
                if(map.isWater(pos)) {
                    mapGrid.add(new Label("W"), i+1, mapHeight-j);
                    mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
                }
            }
        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        this.map = worldMap;
        Platform.runLater(() -> {
            drawMap();
        });
    }

    public void onPauseClicked() {
        simulation.changeStateOfSimulation();
    }
}
