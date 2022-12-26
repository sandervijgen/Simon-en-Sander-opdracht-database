package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.EtappeJDBC;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class EtappeBewerkenController {
    @FXML
    private AnchorPane scherm;
    @FXML
    private Button btnPasToe;
    @FXML
    private Button btnVoegToe;
    @FXML
    private Text textEinde;
    @FXML
    private TextField aantalEtappes;
    @FXML
    private Text statusBalk_text;


    private Wedstrijd wedstrijd = WedstrijdBewerkController.getWedstrijd();

    ArrayList<TextField> afstandWaardes = new ArrayList<TextField>();

    public void initialize(){
        btnPasToe.setOnAction(e -> PasToe());
        btnVoegToe.setOnAction(e -> bewerk());
        textEinde.setText(String.valueOf(wedstrijd.getAfstand()));
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    private void PasToe() {

        int aantal = Integer.parseInt(aantalEtappes.getText());
        for (int i = 1; i < aantal + 1; i++) {
            TextField textField = new TextField();
            textField.setPromptText("afstand etappe " + i + " in km");
            textField.setLayoutX(260);
            textField.setLayoutY(120 + (30 * i));
            afstandWaardes.add(textField);
            scherm.getChildren().add(textField);
        }
    }

    private void bewerk(){
        boolean klopt = true;
        int totaal = 0;
        ArrayList<Etappe> etappes = new ArrayList<Etappe>();
        if (afstandWaardes.size() == 0){
            int aantal = Integer.parseInt(aantalEtappes.getText());
            int eindKm = wedstrijd.getAfstand();
            int deling = eindKm/aantal;
            int rest = eindKm%aantal;
            for (int i = 0; i < aantal-1; i++){
                if (i == aantal-2){
                    etappes.add(new Etappe(aantal, wedstrijd.getWedstrijdId(), deling+rest, totaal));
                    totaal += (deling+rest);
                }
                etappes.add(new Etappe(i+1, wedstrijd.getWedstrijdId(), deling, totaal));
                totaal += deling;
            }
        }
        else {
            for (int i = 0; i < afstandWaardes.size(); i++) {
                if (i == 0) {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int beginKm = 0;
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    Etappe etappe = new Etappe(1, wedstrijd.getWedstrijdId(), afstand, beginKm);
                    etappes.add(etappe);
                    totaal += afstand;
                    //TODO if beginKM > eindKM catchen
                } else if (i == afstandWaardes.size()-1) {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    Etappe etappe = new Etappe(i, wedstrijd.getWedstrijdId(), afstand , totaal);
                    totaal += afstand;
                    etappes.add(etappe);
                } else {
                    TextField textFieldAfstand = afstandWaardes.get(i);
                    int afstand = Integer.parseInt(textFieldAfstand.getText());
                    Etappe etappe = new Etappe(i, wedstrijd.getWedstrijdId(), afstand, totaal);
                    etappes.add(etappe);
                    totaal += afstand;
                }
            }
        }
        System.out.println(totaal + ", " + wedstrijd.getAfstand());
        for (int i = 0; i < etappes.size(); i++){
            if (etappes.get(i).getAfstand() <= 0){
                statusBalk_text.setText("totaal klopt niet of negatieve afstend voor etappe");
                klopt = false;
            }
        }
        if (totaal != wedstrijd.getAfstand() || klopt == false){
            statusBalk_text.setText("totaal klopt niet of negatieve afstend voor etappe");
        }
        else {
            for (int i = 0; i < etappes.size(); i++) {
                EtappeJDBC.bewerkEtappes(wedstrijd.getWedstrijdId(), etappes);
                statusBalk_text.setText("succesvol gewijzigd!");
            }
        }
    }
}
