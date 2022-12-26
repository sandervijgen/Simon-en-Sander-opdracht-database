package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.MedewerkerJDBC;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Medewerker;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BeheerMedewerkersController {

    @FXML
    private Button btnVoeg_toe;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView tblConfigs;

    private static Medewerker selectedMedewerker;

    public static Medewerker getSelectedWedstrijd() {
        return selectedMedewerker;
    }

    public void initialize() {
        initTable();
        btnVoeg_toe.setOnAction(e -> addNewMedewerker());
        btnRefresh.setOnAction(e -> initTable());
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        /*btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow();
        });*/// weet niet zeker of dit moet, indien niet code verwijderen !!!
    }

    private void addNewMedewerker() {
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toevoegenMedewerker.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Medewerker Toevoegen");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm loper toevoegen niet vinden", e);
        }
    }

    /*private void modifyCurrentRow() {
        this.selectedMedewerker = (Medewerker) tblConfigs.getSelectionModel().getSelectedItem();
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("veranderMedewerker.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("medewerker bewerken");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm wedstrijd bewerken niet vinden", e);
        }
    }*/

    private void deleteCurrentRow() {
        Medewerker selectedItem = (Medewerker) tblConfigs.getSelectionModel().getSelectedItem();
        MedewerkerJDBC.verwijderMedewerker(selectedItem.getMedewerkerId());
        initTable();
    }

    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();
        TableColumn medewerkerId = new TableColumn("Medewerker Id");
        TableColumn naam = new TableColumn("Naam");
        TableColumn functie = new TableColumn("functie");
        TableColumn leeftijd = new TableColumn("leeftijd");
        TableColumn uurloon = new TableColumn("uurloon");

        tblConfigs.getColumns().addAll(medewerkerId,naam,functie,leeftijd,uurloon);


        medewerkerId.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("medewerkerId"));
        naam.setCellValueFactory(new PropertyValueFactory<Wedstrijd, String>("naam"));
        leeftijd.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("leeftijd"));
        functie.setCellValueFactory(new PropertyValueFactory<Wedstrijd, String>("functie"));
        uurloon.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("uurloon"));

        ObservableList<Medewerker> medewerkersLijst = FXCollections.observableArrayList(MedewerkerJDBC.getMedewerker());
        tblConfigs.setItems(medewerkersLijst);
    }

    public void showAlert(String title, String content) {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void verifyOneRowSelected() {
        if(tblConfigs.getSelectionModel().getSelectedCells().size() == 0) {
            showAlert("Hela!", "Eerst een record selecteren hé.");
        }
    }
}
