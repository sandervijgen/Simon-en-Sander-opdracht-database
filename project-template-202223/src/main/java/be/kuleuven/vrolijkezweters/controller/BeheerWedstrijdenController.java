package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

public class BeheerWedstrijdenController {

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClose;
    @FXML
    private TableView tblConfigs;

    public void initialize() {
        initTable();
        btnAdd.setOnAction(e -> addNewRow());
        btnModify.setOnAction(e -> {
            verifyOneRowSelected();
            modifyCurrentRow();
        });
        btnDelete.setOnAction(e -> {
            verifyOneRowSelected();
            deleteCurrentRow();
        });
        
        btnClose.setOnAction(e -> {
            var stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }

    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();
        TableColumn wedstrijdId = new TableColumn("WedstrijdId");
        TableColumn plaats = new TableColumn("Plaats");
        TableColumn afstand = new TableColumn("Afstand");
        TableColumn inschrijvingsgeld = new TableColumn("Inschrijvingsgeld");
        TableColumn datum = new TableColumn("Datum");
        TableColumn beginUur = new TableColumn("Begin uur");
        tblConfigs.getColumns().addAll(wedstrijdId,plaats,afstand,inschrijvingsgeld,datum,beginUur);


        wedstrijdId.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("WestrijdId"));
        plaats.setCellValueFactory(new PropertyValueFactory<Wedstrijd, String>("plaats"));
        afstand.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("afstand"));
        inschrijvingsgeld.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("inschrijvingsGeld"));
        datum.setCellValueFactory(new PropertyValueFactory<Wedstrijd, String>("datum"));
        beginUur.setCellValueFactory(new PropertyValueFactory<Wedstrijd, Integer>("beginUur"));

        RepoJDBC.getWedstrijden();
        //tblConfigs.setItems(RepoJDBC.getWedstrijden());

    }


    private void addNewRow() {
            try {
                var stage = new Stage();
                var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("toevoegenWedstrijd.fxml"));
                var scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("wedstrijd toevoegen");
                stage.initOwner(ProjectMain.getRootStage());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();

            } catch (Exception e) {
                throw new RuntimeException("Kan beheerscherm wedstrijd toevoegen niet vinden", e);
            }
    }

    private void deleteCurrentRow() {
    }

    private void modifyCurrentRow() {
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
