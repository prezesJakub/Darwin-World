package presenters;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Animal;

import java.util.List;
import java.util.function.Consumer;

public class AnimalChooser {

    public static void showAnimalChooser(List<Animal> animals, Consumer<Animal> onAnimalChosen) {
        Stage stage = new Stage();

        ListView<Animal> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(animals));

        Button chooseButton = new Button("Wybierz");
        chooseButton.setDisable(true);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chooseButton.setDisable(newValue == null);
        });

        chooseButton.setOnAction(event -> {
            Animal choosenAnimal = listView.getSelectionModel().getSelectedItem();
            if (choosenAnimal != null) {
                onAnimalChosen.accept(choosenAnimal);
                stage.close();
            }
        });

        VBox vbox = new VBox(10, listView, chooseButton);
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Wybierz zwierzę");
        stage.show();
    }
}
