package presenters;

import information.MapStatistics;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maps.WaterMap;
import maps.WorldMap;
import model.MapChangeListener;
import model.StatisticsSaver;
import model.TileType;
import model.Vector2d;
import objects.Animal;
import simulation.Simulation;

import java.util.List;
import java.util.Map;
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

    @FXML
    private Label dayStat, aliveAnimalsStat, allAnimalCountStat, plantCountStat, freeTilesStat,
            mostPopularGenomeStat, averageEnergyStat, averageLifetimeStat, averageChildrenAmountStat;

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
       // addWater();
        updateStats();
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
        colorMap();
    }

    private void addAnimals() {
        List<Vector2d> animalPositions = map.getAnimalPositions();
        for(Vector2d pos : animalPositions) {
            Label label = new Label();
            int x = pos.getX();
            int y = pos.getY();
            Animal animal = map.getAnimal(pos).getFirst();
            label.setGraphic(drawAnimal(animal));
            mapGrid.add(label, x+1, mapHeight-y);
            mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
        }
       // System.out.println(map.getAnimalPositions());
    }

    private void addPlants() {
        List<Vector2d> plantPositions = map.getPlantPositions();
        for(Vector2d pos : plantPositions) {
            Label label = new Label();
            int x = pos.getX();
            int y = pos.getY();
            label.setText(map.getPlant(pos).toString());
            label.setTextFill(Color.GREENYELLOW);
            label.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-font-size: " + (cellSize * 0.7) + "px;");
            mapGrid.add(label, x+1, mapHeight-y);
            mapGrid.setHalignment(label, HPos.CENTER);
        }
    }

    private Circle drawAnimal(Animal animal) {
        Circle circle = new Circle();
        circle.setRadius(cellSize * 0.3);
        return circle;
    }

  /*  private void addWater() {
        for(int i=0; i<mapWidth; i++) {
            for(int j=0; j<mapHeight; j++) {
                Vector2d pos = new Vector2d(i, j);
                if(map.isWater(pos)) {
                    mapGrid.add(new Label("W"), i+1, mapHeight-j);
                    mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
                }
            }
        }
    }*/

    private void colorMap() {
        Map<Vector2d, TileType> tiles = map.getTiles();

        for(int i=0; i<mapWidth; i++) {
            for(int j=mapHeight-1; j>=0; j--) {
                Vector2d pos = new Vector2d(i, j);
                TileType tile = tiles.get(pos);
                Label label = new Label();
                label.setMinSize(cellSize, cellSize);
                label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, null, new BorderWidths(0.4))));
                label.setStyle("-fx-alignment: CENTER;");

                if(map.isWater(pos)) {
                    label.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else if(tile == TileType.EQUATOR) {
                    label.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    label.setBackground(new Background(new BackgroundFill(Color.SANDYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                mapGrid.add(label, i+1, mapHeight-j);
            }
        }
    }

    private void updateStats() {
        MapStatistics currentStats = map.getMapStats();

        dayStat.setText(String.valueOf(currentStats.getDay()));
        aliveAnimalsStat.setText(String.valueOf(currentStats.getAliveAnimals()));
        allAnimalCountStat.setText(String.valueOf(currentStats.getAllAnimalCount()));
        plantCountStat.setText(String.valueOf(currentStats.getPlantCount()));
        freeTilesStat.setText(String.valueOf(currentStats.getFreeTiles()));
        mostPopularGenomeStat.setText(currentStats.getMostPopularGenomeDetails());
        averageEnergyStat.setText(String.valueOf(currentStats.getAverageEnergy()));
        averageLifetimeStat.setText(String.valueOf(currentStats.getAverageLifetime()));
        averageChildrenAmountStat.setText(String.valueOf(currentStats.getAverageChildrenAmount()));
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
