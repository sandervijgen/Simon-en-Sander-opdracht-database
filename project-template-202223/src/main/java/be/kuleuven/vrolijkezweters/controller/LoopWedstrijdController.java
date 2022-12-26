package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.WedstrijdJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class LoopWedstrijdController {

    @FXML
    private AnchorPane scherm;
    @FXML
    private Button btn_volgendeLoper;
    @FXML
    private Text statusBalk_text;

    private static Wedstrijd wedstrijd;
    public static Wedstrijd getWedstrijd() {
        return wedstrijd;
    }
    int aantalEtappes, aantalLopers;
    ArrayList<Integer> etappeIds, loperIds;
    Text header = new Text();
    ArrayList<TextField> tijden = new ArrayList<TextField>();
    int teller = 0;

    public void initialize() {
        this.wedstrijd = BeheerWedstrijdenController.getSelectedWedstrijd();
        etappeIds  = RepoJDBC.getAantalEtappes(wedstrijd.getWedstrijdId());
        loperIds = RepoJDBC.getAantalLopers(wedstrijd.getWedstrijdId());
        aantalEtappes = etappeIds.size();
        aantalLopers = loperIds.size();
        btn_volgendeLoper.setOnAction(e -> volgendeLoper());
        laatZien();
    }

    private void volgendeLoper(){
        boolean gelukt = true;
        for (int i = 0; i < tijden.size(); i++) {
            try {
                if ((Integer.parseInt(tijden.get(i).getText())) < 0 || tijden.get(i).getText() == "") {
                    throw new NumberFormatException();
                }
                int etappeId = etappeIds.get(i);
                int loperId = loperIds.get(teller);
                int tijd = Integer.parseInt(tijden.get(i).getText());
                WedstrijdJDBC.tijdIngeven(etappeId, loperId, tijd);
            } catch (NumberFormatException e) {
                statusBalk_text.setText("gelieve bij alle velden een geldig getal in te vullen");
                gelukt = false;
            }
        }
        if (gelukt == true) {
            teller++;
            if (teller != aantalLopers){laatZien();}
            else{
                alleLopersIngevuld();
            }
        }
    }

    private void alleLopersIngevuld(){
        btn_volgendeLoper.setDisable(true);
        btn_volgendeLoper.setVisible(false);
        header.setVisible(false);
        for (int i = 0; i < tijden.size(); i++) {
            tijden.get(i).setVisible(false);
        }
        statusBalk_text.setText("alle lopers ingevuld");
        WedstrijdJDBC.loopWedstrijd(wedstrijd.getWedstrijdId());
        WedstrijdJDBC.wedstrijdKlassement(wedstrijd.getWedstrijdId());
    }
    private void laatZien(){
        scherm.getChildren().removeAll(tijden);
        tijden.clear();
        header.setLayoutX(261.0);
        header.setLayoutY(100);
        header.setText("Etappe tijden van loper met loperId: " + loperIds.get(teller));
        if (teller == 0) {
            scherm.getChildren().add(header);
        }
        for (int i = 1; i < aantalEtappes + 1; i++) {
            TextField textField = new TextField();
            tijden.add(textField);
            textField.setPromptText("tijd etappe " + i + " in seconden");
            textField.setLayoutX(261.0);
            textField.setLayoutY(120 + (30 * i));
            scherm.getChildren().add(textField);
        }
    }
}