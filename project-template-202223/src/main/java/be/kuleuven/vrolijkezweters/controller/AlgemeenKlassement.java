package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.LoperJDBC;
import be.kuleuven.vrolijkezweters.WedstrijdJDBC;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class AlgemeenKlassement {
    @FXML
    private TableView tblConfigs;
    public void initialize() {
        initTable();
    }
    private void initTable() {
        tblConfigs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblConfigs.getColumns().clear();

        TableColumn plaats = new TableColumn("Plaats");
        TableColumn punten = new TableColumn("Punten");
        TableColumn loperId = new TableColumn("Loper Id");
        TableColumn naam = new TableColumn("Naam");
        TableColumn leeftijd = new TableColumn("Leeftijd");
        TableColumn geslacht = new TableColumn("Geslacht");
        TableColumn gewicht = new TableColumn("Gewicht");
        TableColumn fysiek = new TableColumn("Fysiek");
        TableColumn club = new TableColumn("Club");
        TableColumn contactMedewerkerId = new TableColumn("Contact Medewerker Id");

        tblConfigs.getColumns().addAll(plaats, punten, loperId,naam,leeftijd,geslacht,gewicht,fysiek,club,contactMedewerkerId);

        plaats.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("plaats"));
        punten.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("punten"));
        loperId.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("loperId"));
        naam.setCellValueFactory(new PropertyValueFactory<Loper, String>("naam"));
        leeftijd.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("leeftijd"));
        geslacht.setCellValueFactory(new PropertyValueFactory<Loper, String>("geslacht"));
        gewicht.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("gewicht"));
        fysiek.setCellValueFactory(new PropertyValueFactory<Loper, String>("fysiek"));
        club.setCellValueFactory(new PropertyValueFactory<Loper, String>("club"));
        contactMedewerkerId.setCellValueFactory(new PropertyValueFactory<Loper, Integer>("contactMedewerkerId"));

        ObservableList<Loper> loperLijst = FXCollections.observableArrayList(WedstrijdJDBC.algemeenKlassement());
        tblConfigs.setItems(loperLijst);
    }
}
