package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.ProjectMain;
import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WedstrijdBewerkController {
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnEtappes;
    @FXML
    private Text wedstrijd_id_text;
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

    private static Wedstrijd wedstrijd;

    public static Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void initialize() {
        this.wedstrijd = BeheerWedstrijdenController.getSelectedWedstrijd();
        wedstrijd_id_text.setText(String.valueOf(wedstrijd.getWedstrijdId()));
        plaats_text.setText(String.valueOf(wedstrijd.getPlaats()));
        afstand_text.setText(String.valueOf(wedstrijd.getAfstand()));
        inschrijvingsgeld_text.setText(String.valueOf(wedstrijd.getInschrijvingsGeld()));
        begin_uur_text.setText(String.valueOf(wedstrijd.getBeginUur()));
        datum_text.setText(String.valueOf(wedstrijd.getDatum()));

        btnEtappes.setOnAction(e -> gaNaarEtappesScherm(wedstrijd));
        btnBewerk.setOnAction(e -> bewerk());
    }

    private void gaNaarEtappesScherm(Wedstrijd wedstrijd) {
        try {
            bewerk();
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

    private void bewerk() {
        int wedstrijdId, afstand, inschrijvingsGeld, beginUur;
        String plaats, datum;
        try {
            wedstrijdId = wedstrijd.getWedstrijdId();
            afstand = Integer.parseInt(afstand_text.getText());
            inschrijvingsGeld = Integer.parseInt(inschrijvingsgeld_text.getText());
            beginUur = Integer.parseInt(begin_uur_text.getText());
            plaats = plaats_text.getText();
            datum = datum_text.getText();
            this.wedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand, inschrijvingsGeld, datum, beginUur);
            if (plaats == "" || datum == ""){
                throw new NullPointerException("veld leeg gelaten");
            }
            Wedstrijd bewerkteWedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand, inschrijvingsGeld, datum, beginUur);
            RepoJDBC.bewerkWedstrijd(bewerkteWedstrijd);
            statusBalk_text.setText("Wedstrijd succesvol bewerkt");

        }
        catch(NumberFormatException n){
            statusBalk_text.setText("Gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusBalk_text.setText("Gelieve voor alle velden een keuze op te geven");
        }

    }
}
