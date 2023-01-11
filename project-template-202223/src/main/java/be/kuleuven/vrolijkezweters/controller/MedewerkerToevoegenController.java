package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.MedewerkerJDBC;
import be.kuleuven.vrolijkezweters.properties.Medewerker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MedewerkerToevoegenController {

    //@FXML
    //private TextField medewerker_id_text;

    @FXML
    private TextField naam_text;

    @FXML
    private ListView functies;

    @FXML
    private TextField leeftijd_text;

    @FXML
    private TextField uurloon_text;

    @FXML
    private Button voeg_toe_button;

    @FXML
    private Text statusbalk_text;

    public void initialize(){
        statusbalk_text.setTextAlignment(TextAlignment.CENTER);
        functies.getItems().addAll("Contact medewerker","Sein gever", "Publicatie manager", "Wedstrijd verantwoordelijke");
        voeg_toe_button.setOnAction(e -> voegToe());
    }

    private void voegToe() {
        int leeftijd, uurloon;
        String naam, functie;
        try {
            leeftijd = Integer.parseInt(leeftijd_text.getText());
            uurloon = Integer.parseInt(uurloon_text.getText());
            naam = naam_text.getText();
            functie = functies.getSelectionModel().getSelectedItem().toString();
            if (naam == "" || functie == ""){
                throw new NullPointerException("veld leeg gelaten");
            }
            Medewerker nieuweMedewerker = new Medewerker(0, naam, functie, leeftijd, uurloon,0);

            if (MedewerkerJDBC.voegMedewerkerToe(nieuweMedewerker) == false) {
                statusbalk_text.setText("error");
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
