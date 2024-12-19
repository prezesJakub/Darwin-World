package presenters;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import maps.WorldMap;
import model.MapChangeListener;
import model.MapElement;
import model.Vector2d;
import objects.Animal;
import simulation.Simulation;

import java.util.Arrays;
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
    private Label[][] cells;
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
        cells = new Label[mapWidth][mapHeight];
        Platform.runLater(() -> drawMap());
    }

    private void drawMap() {
        clearGrid();
        headerLabel();
        generateTable();
        addAnimals();
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
        for(int i=mapHeight-1; i>=0; i--) {
            Label label2 = new Label(Integer.toString(i));
            GridPane.setHalignment(label2, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            mapGrid.add(label2, 0, i+1);
        }
    }

    private void addAnimals() {
        System.out.println(map.getElementPositions());
        List<Vector2d> elementPositions = map.getElementPositions();
        for(Vector2d pos : elementPositions) {
            int x = pos.getX();
            int y = pos.getY();
            mapGrid.add(new Label(map.getElement(pos).toString()), x+1, y+1);
            mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
        }
        List<Animal> animaltest = map.getAnimals();
        for(Animal element : animaltest) {
            int[] genes = element.getGenome().getGenes();
            System.out.println(Arrays.toString(genes));
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
}
