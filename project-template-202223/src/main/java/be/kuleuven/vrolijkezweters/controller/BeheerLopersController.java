package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BeheerLopersController {

    @FXML
    private Button voeg_toe;

    public void initialize() {
        voeg_toe.setOnAction(e -> addNewLoper());

    }

    private void addNewLoper() {
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("loperToevoegen.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Loper Toeveogen");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm loper toevoegen niet vinden", e);
        }
    }
}
