package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class WedstrijdToevoegenController {

    @FXML
    private Button btnApply;
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
    @FXML
    private TextField textEtappes;
    @FXML
    private Text statusBalk_text;
    @FXML
    private AnchorPane scherm;
    @FXML
    private Text wedstrijdText2;

    ArrayList<TextField> afstandWaardes = new ArrayList<TextField>();

    public void initialize() {
        voeg_toe.setOnAction(e -> voegToe());
        btnApply.setOnAction(e -> applyEtappe());
    }

    private void applyEtappe() {
        scherm.getChildren().removeAll(afstandWaardes);
        afstandWaardes.clear();
        afstand_text.setLayoutY(190);
        wedstrijdText2.setLayoutY(175);
        int aantal = Integer.parseInt(textEtappes.getText());
        for (int i = 1; i < aantal; i++) {
            TextField textField = new TextField();
            textField.setLayoutX(461.0);
            textField.setLayoutY(120 + (30 * i));
            wedstrijdText2.setLayoutY(wedstrijdText2.getLayoutY() + 30);
            afstand_text.setLayoutY(afstand_text.getLayoutY() + 30);
            afstandWaardes.add(textField);
            scherm.getChildren().add(textField);
        }
    }

    private void voegToe() {

        try {
            Wedstrijd nieuweWedstrijd =  maakWedstrijd();
            nieuweWedstrijd = new Wedstrijd(nieuweWedstrijd.getWedstrijdId(), nieuweWedstrijd.getPlaats(), nieuweWedstrijd.getAfstand(), nieuweWedstrijd.getInschrijvingsGeld(), nieuweWedstrijd.getDatum(), nieuweWedstrijd.getBeginUur());
            int wedstrijdId  = RepoJDBC.voegWedstrijdToe(nieuweWedstrijd);

            if (wedstrijdId == -1) {
                statusBalk_text.setText("deze id bestaat al");
            }
            else{
                statusBalk_text.setText("Wedstrijd succesvol toegevoegd");
            }
            voegEtappesToe(wedstrijdId);
        }
        catch(NumberFormatException n){
            statusBalk_text.setText("Gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusBalk_text.setText("Gelieve voor alle velden een keuze op te geven");
        }




    }

        private void voegEtappesToe(int wedstrijdId) {
            for (int i = 0; i <= afstandWaardes.size(); i++) {
                if (afstandWaardes.size() == 0){
                    int beginKm = 0;
                    int eindKm = Integer.parseInt(afstand_text.getText());
                    Etappe etappe = new Etappe(i, wedstrijdId, (eindKm -beginKm) , beginKm);
                    if(!RepoJDBC.voegEtappeToe(etappe)){
                        System.out.println("Er is iets mis met etappe" + 1);
                    };
                    }
                else if(i == 0){
                    TextField textFieldEinde = afstandWaardes.get(i);
                    int beginKm = 0;
                    int eindKm = Integer.parseInt(textFieldEinde.getText());
                    Etappe etappe = new Etappe(i, wedstrijdId, (eindKm -beginKm) , beginKm);
                    if(!RepoJDBC.voegEtappeToe(etappe)){
                        System.out.println("Er is iets mis met etappe" + (i+1));
                    };


                    //TODO if beginKM > eindKM catchen
                }
                else if(i == afstandWaardes.size()){
                    TextField textFieldBegin = afstandWaardes.get(i-1);
                    int beginKm = Integer.parseInt(textFieldBegin.getText());
                    int eindKm = Integer.parseInt(afstand_text.getText());;
                    Etappe etappe = new Etappe(i, wedstrijdId, (eindKm -beginKm) , beginKm);
                    if(!RepoJDBC.voegEtappeToe(etappe)){
                        System.out.println("Er is iets mis met etappe" + (i+1));
                    };
                }
                else {
                    TextField textFieldBegin = afstandWaardes.get(i-1);
                    TextField textFieldEinde = afstandWaardes.get(i);
                    int beginKm = Integer.parseInt(textFieldBegin.getText());
                    int eindKm = Integer.parseInt(textFieldEinde.getText());
                    Etappe etappe = new Etappe(i, wedstrijdId, (eindKm -beginKm) , beginKm);
                    if(!RepoJDBC.voegEtappeToe(etappe)){
                        System.out.println("Er is iets mis met etappe" + (i+1));
                    };
                }
        }
    }


        private Wedstrijd maakWedstrijd() {
            int wedstrijdId, afstand, inschrijvingsGeld, beginUur;
            String plaats, datum;

                wedstrijdId = Integer.parseInt(wedstrijd_id_text.getText());
                afstand = Integer.parseInt(afstand_text.getText());
                inschrijvingsGeld = Integer.parseInt(inschrijvingsgeld_text.getText());
                beginUur = Integer.parseInt(begin_uur_text.getText());
                plaats = plaats_text.getText();
                datum = datum_text.getText();
                if (plaats == "" || datum == "") {
                    throw new NullPointerException("veld leeg gelaten");
                }
            Wedstrijd nieuweWedstrijd;
            return nieuweWedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand, inschrijvingsGeld, datum, beginUur);

        }

}
