package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.WedstrijdJDBC;
import be.kuleuven.vrolijkezweters.properties.KlassementLoper;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class WedstrijdKlassementController {
    Wedstrijd wedstrijd;

    @FXML
    private TableView tblConfigs;

    public void initialize(Wedstrijd selectedWedstrijd) {
        this.wedstrijd = selectedWedstrijd;
        initTable();
    }
    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        TableColumn plaats = new TableColumn("Plaats");
        TableColumn punten = new TableColumn("Punten");
        TableColumn loperId = new TableColumn("Loper Id");
        TableColumn naam = new TableColumn("Naam");
        TableColumn tijd = new TableColumn("Tijd");

        tblConfigs.getColumns().addAll(plaats, punten, loperId,naam,tijd);

        plaats.setCellValueFactory(new PropertyValueFactory<KlassementLoper, Integer>("plaats"));
        punten.setCellValueFactory(new PropertyValueFactory<KlassementLoper, Integer>("punten"));
        loperId.setCellValueFactory(new PropertyValueFactory<KlassementLoper, Integer>("loperId"));
        naam.setCellValueFactory(new PropertyValueFactory<KlassementLoper, String>("naam"));
        tijd.setCellValueFactory(new PropertyValueFactory<KlassementLoper, Integer>("tijd"));

        ObservableList<Loper> loperLijst = FXCollections.observableArrayList(WedstrijdJDBC.wedstrijdKlassement(wedstrijd.getWedstrijdId(), false));
        tblConfigs.setItems(loperLijst);
    }
}
