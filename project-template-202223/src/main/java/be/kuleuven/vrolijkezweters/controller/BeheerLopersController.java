package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.LoperJDBC;
import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.properties.Loper;
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

public class BeheerLopersController {

    @FXML
    private Button btnVoeg_toe;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        initTable();
        btnVoeg_toe.setOnAction(e -> addNewLoper());
        btnRefresh.setOnAction(e -> initTable());
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
    }

    private void addNewLoper() {
        try {
            var stage = new Stage();
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toevoegenLoper.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Loper Toevoegen");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm loper toevoegen niet vinden", e);
        }
    }

    private void deleteCurrentRow() {
        Loper selectedItem = (Loper) tblConfigs.getSelectionModel().getSelectedItem();
        LoperJDBC.verwijderLoper(selectedItem.getLoperId());
        initTable();
    }

    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();
        TableColumn loperId = new TableColumn("Loper Id");
        TableColumn naam = new TableColumn("Naam");
        TableColumn leeftijd = new TableColumn("Leeftijd");
        TableColumn geslacht = new TableColumn("Geslacht");
        TableColumn gewicht = new TableColumn("Gewicht");
        TableColumn fysiek = new TableColumn("Fysiek");
        TableColumn club = new TableColumn("Club");
        TableColumn contactMedewerkerId = new TableColumn("Contact Medewerker Id");
        TableColumn punten = new TableColumn("Punten");

        tblConfigs.getColumns().addAll(loperId,naam,leeftijd,geslacht,gewicht,fysiek,club,contactMedewerkerId,punten);

        loperId.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("loperId"));
        naam.setCellValueFactory(new PropertyValueFactory<Loper, String>("naam"));
        leeftijd.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("leeftijd"));
        geslacht.setCellValueFactory(new PropertyValueFactory<Loper, String>("geslacht"));
        gewicht.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("gewicht"));
        fysiek.setCellValueFactory(new PropertyValueFactory<Loper, String>("fysiek"));
        club.setCellValueFactory(new PropertyValueFactory<Loper, String>("club"));
        contactMedewerkerId.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("contactMedewerkerId"));
        punten.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("punten"));

        ObservableList<Loper> loperLijst = FXCollections.observableArrayList(LoperJDBC.getLoper());
        tblConfigs.setItems(loperLijst);
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
            showAlert("FOUT", "gelieve een loper te selecteren.");
        }
    }
}
