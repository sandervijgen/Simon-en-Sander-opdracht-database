package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
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


public class WedstrijdToevoegenController {

    @FXML
    private Button btnEtappes;
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
    private Text statusBalk_text;



    public void initialize() {
        voeg_toe.setOnAction(e -> voegToe());
        btnEtappes.setOnAction(e -> gaNaarEtappesScherm());
    }

    private void gaNaarEtappesScherm() {
        Wedstrijd wedstrijd = maakWedstrijd();

        try {
            var stage = new Stage();
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("bewerkettappe.fxml"));
            //EtappeBewerkenController etappeBewerkenController = loader.getController();
            //etappeBewerkenController.setWedstrijd(wedstrijd);
            var root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("bewerkettappe.fxml"));
            var scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("etappe bewerken");
            stage.initOwner(ProjectMain.getRootStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException("Kan beheerscherm etappe bewerken niet vinden", e);
        }
    }

    private void voegToe() {
        try {
            Wedstrijd nieuweWedstrijd =  maakWedstrijd();
            nieuweWedstrijd = new Wedstrijd(nieuweWedstrijd.getWedstrijdId(), nieuweWedstrijd.getPlaats(), nieuweWedstrijd.getAfstand(), nieuweWedstrijd.getInschrijvingsGeld(), nieuweWedstrijd.getDatum(), nieuweWedstrijd.getBeginUur());
            if (RepoJDBC.voegWedstrijdToe(nieuweWedstrijd) == false) {
                statusBalk_text.setText("deze id bestaat al");
            }
            else{
                statusBalk_text.setText("Wedstrijd succesvol toegevoegd");
            }
        }
        catch(NumberFormatException n){
            statusBalk_text.setText("Gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusBalk_text.setText("Gelieve voor alle velden een keuze op te geven");
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
