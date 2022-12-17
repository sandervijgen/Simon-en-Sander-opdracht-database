package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class LoperToevoegenController {

    @FXML
    private ListView geslacht_text;
    @FXML
    private ListView fysiek_text;
    @FXML
    private TextField loper_id_text;
    @FXML
    private TextField leeftijd_text;
    @FXML
    private TextField naam_text;
    @FXML
    private TextField gewicht_text;
    @FXML
    private TextField club_text;
    @FXML
    private TextField contact_medewerkers_id_text;
    @FXML
    private TextField punten_text;
    @FXML
    private Button voeg_toe;
    @FXML
    private Text statusBalk_text;

    public void initialize(){
        geslacht_text.getItems().addAll("man","vrouw", "x");
        fysiek_text.getItems().addAll("beginnende loper","middelmatige loper", "gevorderde loper");
        voeg_toe.setOnAction(e -> voegToe());
    }


    private void voegToe() {
        int loperId, leeftijd, gewicht, contactMedewerkerId, punten;
        String naam, geslacht, fysiek, club;
        try {
            loperId = Integer.parseInt(loper_id_text.getText());
            leeftijd = Integer.parseInt(leeftijd_text.getText());
            gewicht = Integer.parseInt(gewicht_text.getText());
            contactMedewerkerId = Integer.parseInt(contact_medewerkers_id_text.getText());
            punten = Integer.parseInt(punten_text.getText());
            naam = naam_text.getText();
            geslacht = geslacht_text.getSelectionModel().getSelectedItem().toString();
            fysiek = fysiek_text.getSelectionModel().getSelectedItem().toString();
            club = club_text.getText();
            if (naam == "" || geslacht == "" || fysiek == "" || club == ""){
                throw new NullPointerException("veld leeg gelaten");
            }
            Loper nieuweLoper = new Loper(loperId, naam, leeftijd, geslacht, gewicht, fysiek, club, contactMedewerkerId, punten);

            if (RepoJDBC.voegLoperToe(nieuweLoper) == false) {
                statusBalk_text.setText("deze id bestaat al");
            }
            else{
                statusBalk_text.setText("Loper succesvol toegevoegd");
            }
        }
        catch(NumberFormatException n){
            statusBalk_text.setText("gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusBalk_text.setText("gelieve voor alle selectievelden een keuze op te geven");
        }

    }
}
