package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.MedewerkerJDBC;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Medewerker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MedewerkerToevoegenController {

    @FXML
    private TextField medewerker_id_text;

    @FXML
    private TextField naam_text;

    @FXML
    private TextField functie_text;

    @FXML
    private TextField leeftijd_text;

    @FXML
    private TextField uurloon_text;

    @FXML
    private Button voeg_toe_button;

    @FXML
    private Text statusbalk_text;

    public void initialize(){
        voeg_toe_button.setOnAction(e -> voegToe());
    }

    private void voegToe() {
        int medewerkerId, leeftijd, uurloon;
        String naam, functie;
        try {
            medewerkerId = Integer.parseInt(medewerker_id_text.getText());
            leeftijd = Integer.parseInt(leeftijd_text.getText());
            uurloon = Integer.parseInt(uurloon_text.getText());
            naam = naam_text.getText();
            functie = functie_text.getText();
            if (naam == "" || functie == ""){
                throw new NullPointerException("veld leeg gelaten");
            }
            Medewerker nieuweMedewerker = new Medewerker(medewerkerId, naam, functie, leeftijd, uurloon);

            if (MedewerkerJDBC.voegMedewerkerToe(nieuweMedewerker) == false) {
                statusbalk_text.setText("deze id bestaat al");
            }
            else{
                statusbalk_text.setText("Medewerker succesvol toegevoegd");
            }
        }
        catch(NumberFormatException n){
            statusbalk_text.setText("gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusbalk_text.setText("gelieve voor alle selectievelden een keuze op te geven");
        }
    }
}
