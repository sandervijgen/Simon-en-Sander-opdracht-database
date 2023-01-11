package be.kuleuven.vrolijkezweters.controller;
import be.kuleuven.vrolijkezweters.WedstrijdJDBC;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;


public class WedstrijdToevoegenController {

    @FXML
    private Button btnApply;
    @FXML
    private Button voeg_toe;
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
        statusBalk_text.setTextAlignment(TextAlignment.CENTER);
        voeg_toe.setOnAction(e -> voegToe());
        btnApply.setOnAction(e -> applyEtappe());
    }

    private void applyEtappe() {
        scherm.getChildren().removeAll(afstandWaardes);
        afstandWaardes.clear();
        afstand_text.setLayoutY(190);
        wedstrijdText2.setLayoutY(175);
        int aantal = Integer.parseInt(textEtappes.getText());
        if (aantal > 8){
            statusBalk_text.setText("een wedstrijd kan maximaal 8 etappes bevatten!");
        }
        else {
            for (int i = 1; i < aantal + 1; i++) {
                TextField textField = new TextField();
                textField.setPromptText("afstand etappe " + i + " in km");
                textField.setLayoutX(461.0);
                textField.setLayoutY(120 + (30 * i));
                wedstrijdText2.setLayoutY(wedstrijdText2.getLayoutY() + 30);
                afstand_text.setLayoutY(afstand_text.getLayoutY() + 30);
                afstandWaardes.add(textField);
                scherm.getChildren().add(textField);
            }
        }
    }

    private void voegToe() {
        Wedstrijd wedstrijd;
        try {
            wedstrijd =  maakWedstrijd();
            WedstrijdJDBC.voegWedstrijdToe(wedstrijd,voegEtappesToe(0));
            statusBalk_text.setText("Wedstrijd succesvol toegevoegd");
        }
        catch(NumberFormatException n){
            statusBalk_text.setText("Gelieve een geldig getal in te geven waar dit nodig is");
        }
        catch(NullPointerException e){
            statusBalk_text.setText("Gelieve voor alle velden een keuze op te geven");
        }
        catch(IllegalArgumentException i){
            statusBalk_text.setText("het totaal van de etappes komt niet overeen met de totale afstand of niet bij elke etappe een positief getal opgegeven");
        }
    }

    private ArrayList<Etappe> voegEtappesToe(int wedstrijdId) {
        int totaal = 0;
        ArrayList<Etappe> etappes = new ArrayList<Etappe>();
        if (afstandWaardes.size() == 0){
            int aantalEtappes = Integer.parseInt(textEtappes.getText());
            int eindKm = Integer.parseInt(afstand_text.getText());
            int deling = eindKm/aantalEtappes;
            int rest = eindKm%aantalEtappes;
            for (int i = 0; i < aantalEtappes - 1; i++){
                if (i == aantalEtappes - 2){
                    etappes.add(new Etappe(aantalEtappes, wedstrijdId, rest+deling, totaal));
                    totaal += rest+deling;
                }
                etappes.add(new Etappe(i+1, wedstrijdId, deling, totaal));
                totaal += deling;
            }
        }
        else {
            for (int i = 0; i < afstandWaardes.size(); i++) {
                try{
                    Integer.parseInt(afstandWaardes.get(i).getText());
                }
                catch (NumberFormatException n){
                    throw new IllegalArgumentException("fout");
                }
                if (i == 0) {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int beginKm = 0;
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    System.out.println(afstand);
                    Etappe etappe = new Etappe(1, wedstrijdId, afstand, beginKm);
                    etappes.add(etappe);
                    totaal += afstand;

                } else if (i == afstandWaardes.size()-1) {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    Etappe etappe = new Etappe(i, wedstrijdId, afstand , totaal);
                    totaal += afstand;
                    etappes.add(etappe);
                } else {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    Etappe etappe = new Etappe(i, wedstrijdId, afstand, totaal);
                    etappes.add(etappe);
                    totaal += afstand;
                }
            }
        }
        System.out.println(totaal + ", " + Integer.parseInt(afstand_text.getText()));
        for (int i = 0; i < etappes.size(); i++){
            if (etappes.get(i).getAfstand() <= 0 ){
                throw new IllegalArgumentException("negatieve afstand voor etappe");
            }
        }
        if (totaal != Integer.parseInt(afstand_text.getText())){
            throw new IllegalArgumentException("totaal klopt niet");
        }
        return etappes;
    }

    private Wedstrijd maakWedstrijd() {
        int afstand, etappes, inschrijvingsGeld, beginUur;
        String plaats, datum;
        afstand = Integer.parseInt(afstand_text.getText());
        inschrijvingsGeld = Integer.parseInt(inschrijvingsgeld_text.getText());
        beginUur = Integer.parseInt(begin_uur_text.getText());
        plaats = plaats_text.getText();
        datum = datum_text.getText();
        etappes = Integer.parseInt(textEtappes.getText());
        if (plaats == "" || datum == "" || afstand <= 0 || inschrijvingsGeld < 0 || beginUur <= 0 || beginUur > 24 || etappes <= 0 ) {
            throw new NullPointerException("veld leeg gelaten");
        }
        return new Wedstrijd(1, plaats, afstand, inschrijvingsGeld, datum, beginUur, false);

    }
}
