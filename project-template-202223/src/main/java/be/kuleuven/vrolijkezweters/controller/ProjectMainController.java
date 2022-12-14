package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.WedstrijdJDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectMainController {

    @FXML
    private Button btnWedstrijden;
    @FXML
    private Button btnBeheerLopers;
    @FXML
    private Button btnAlgemeenKlassement;
    @FXML
    private Button btnMedewerkers;

    public void initialize() {
        btnBeheerLopers.setOnAction(e -> showBeheerScherm("lopers"));
        btnWedstrijden.setOnAction(e -> showBeheerScherm("wedstrijden"));
        btnAlgemeenKlassement.setOnAction(e -> showBeheerScherm("algemeenklassement"));
        btnMedewerkers.setOnAction(e -> showBeheerScherm("medewerkers"));

    }

    private void showBeheerScherm(String id) {
        var resourceName = "beheer" + id + ".fxml";
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(resourceName));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin " + id);
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm " + resourceName + " niet vinden", e);
        }
    }
}
