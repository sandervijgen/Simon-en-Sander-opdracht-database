package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.LoperJDBC;
import be.kuleuven.vrolijkezweters.properties.Loper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LoperToevoegenController {

    @FXML
    private ListView geslacht_text;
    @FXML
    private ListView fysiek_text;
    //@FXML
    //private TextField loper_id_text;
    @FXML
    private TextField leeftijd_text;
    @FXML
    private TextField naam_text;
    @FXML
    private TextField gewicht_text;
    @FXML
    private TextField club_text;
    @FXML
    private Button voeg_toe;
    @FXML
    private Text statusBalk_text;

    public void initialize(){
        statusBalk_text.setTextAlignment(TextAlignment.CENTER);
        geslacht_text.getItems().addAll("man","vrouw", "x");
        fysiek_text.getItems().addAll("beginnende loper","middelmatige loper", "gevorderde loper");
        voeg_toe.setOnAction(e -> voegToe());
    }

    private void voegToe() {
        int leeftijd, gewicht, contactMedewerkerId, punten;
        String naam, geslacht, fysiek, club;
        try {
            leeftijd = Integer.parseInt(leeftijd_text.getText());
            gewicht = Integer.parseInt(gewicht_text.getText());
            naam = naam_text.getText();
            geslacht = geslacht_text.getSelectionModel().getSelectedItem().toString();
            fysiek = fysiek_text.getSelectionModel().getSelectedItem().toString();
            club = club_text.getText();
            if (naam == "" || geslacht == "" || fysiek == "" || club == ""){
                throw new NullPointerException("veld leeg gelaten");
            }
            if (leeftijd < 3 ){
                throw new IllegalArgumentException("Leeftijd moet groter zijn dan 3 jaar");
            }
            if (gewicht < 30){
                throw new IllegalArgumentException("Gewicht moet groter zijn dan 30 kg");
            }

            Loper nieuweLoper = new Loper(0, 0, naam, leeftijd, geslacht, gewicht, fysiek, club, 0, 0);

            if (LoperJDBC.voegLoperToe(nieuweLoper) == 1) {
                statusBalk_text.setText("error er is iets misgelopen met de connectie");
            }
            else if (LoperJDBC.voegLoperToe(nieuweLoper) == 2) {
                statusBalk_text.setText("error er zijn nog geen medewerkers dus de inschrijvingen zijn nog niet open");
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
        catch(IllegalArgumentException e){
            statusBalk_text.setText("Leeftijd moet groter zijn dan 3 jaar of gewicht moet groter zijn dan 30 kg");
        }
    }
}
