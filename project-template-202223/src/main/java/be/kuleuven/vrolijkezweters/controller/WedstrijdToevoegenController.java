package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;



public class WedstrijdToevoegenController {


    @FXML
    private Button voeg_toe;
    @FXML
    private TextField wedstrijd_id_text;
    @FXML
    private TextField plaats_text;
    @FXML
    private TextField afstand_text;
    @FXML
    private TextField inschrijvingsgeld_text;
    @FXML
    private TextField begin_uur_text;
    @FXML
    private TextField datum_text;

    public void initialize() {
        voeg_toe.setOnAction(e -> voegToe());
    }

    private void voegToe() {
        int wedstrijdId, afstand, inschrijvingsGeld, beginUur;
        String plaats, datum;

            wedstrijdId = Integer.parseInt(wedstrijd_id_text.getText());
            afstand = Integer.parseInt(afstand_text.getText());
            inschrijvingsGeld = Integer.parseInt(inschrijvingsgeld_text.getText());
            beginUur = Integer.parseInt(begin_uur_text.getText());
            plaats = plaats_text.getText();
            datum = datum_text.getText();

        Wedstrijd nieuweWedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand,inschrijvingsGeld, datum, beginUur);

        RepoJDBC.voegWedstrijdToe(nieuweWedstrijd);
    }

}
