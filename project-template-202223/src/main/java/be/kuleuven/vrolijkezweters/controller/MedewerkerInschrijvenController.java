package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.MedewerkerJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import be.kuleuven.vrolijkezweters.properties.WedstrijdMedewerker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MedewerkerInschrijvenController {
    @FXML
    private Button btnSchrijfIn;
    @FXML
    private Text textWedstrijd;
    @FXML
    private TextField textMedewerkerNr;
    @FXML
    private TextField textBeginUur;
    @FXML
    private TextField textEindUur;
    @FXML
    private TextField textPositie;
    @FXML
    private Text statusBalk_text;

    private Wedstrijd wedstrijd;

    public void initialize(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
        textWedstrijd.setText("wedstrijd in " +wedstrijd.getPlaats()+", op "+wedstrijd.getDatum());
        btnSchrijfIn.setOnAction(e->schrijfin());
    }

    private void schrijfin() {

        try {
            int medewerkerId = Integer.parseInt(textMedewerkerNr.getText());
            int beginUur = Integer.parseInt(textBeginUur.getText());
            int eindUur = Integer.parseInt(textEindUur.getText());
            String positie = textPositie.getText();
            if (beginUur <= 0 || beginUur > 23 || eindUur <= 0 || eindUur > 23 || eindUur < beginUur){
                throw new Exception("geen geldige input");
            }

            WedstrijdMedewerker nieuweWedstrijdMedewerker = new WedstrijdMedewerker(1,wedstrijd.getWedstrijdId(),medewerkerId,beginUur,eindUur,positie);

            if (MedewerkerJDBC.schrijfMedewerkerIn(wedstrijd, nieuweWedstrijdMedewerker) == true) {
                statusBalk_text.setText("Medewerker succesvol ingeschreven!");
            }
            else{
                statusBalk_text.setText("Er bestaat geen medewerker met dit medewerkersnummer of deze is al ingeschreven");
            }
        }
        catch(Exception e){
            statusBalk_text.setText("gelieve een geldige waarde bij alle velden op te geven");
        }
    }
}
