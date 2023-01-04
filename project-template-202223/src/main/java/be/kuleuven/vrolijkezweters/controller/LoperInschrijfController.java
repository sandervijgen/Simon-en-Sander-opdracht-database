package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.LoperJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoperInschrijfController {
    @FXML
    private Button btnSchrijfIn;
    @FXML
    private Text textWedstrijd;
    @FXML
    private TextField textLoopNr;
    @FXML
    private Text statusBalk_text;
    @FXML
    private CheckBox isBetaald_checkbox;

    private boolean isBetaald = false;

    private Wedstrijd wedstrijd;

    public void initialize() {
        this.wedstrijd = BeheerWedstrijdenController.getSelectedWedstrijd();
        textWedstrijd.setText("wedstrijd in " +wedstrijd.getPlaats()+", op "+wedstrijd.getDatum());
        btnSchrijfIn.setOnAction(e->schrijfin());
        isBetaald_checkbox.setOnAction(e -> setIsBetaald());
    }

    private void setIsBetaald() {
        isBetaald = !isBetaald;
    }

    private void schrijfin() {

        try {
            int loperId = Integer.parseInt(textLoopNr.getText());
            if (isBetaald){
                if (LoperJDBC.schrijfLoperIn(wedstrijd, loperId) == true) {
                    statusBalk_text.setText("Loper succesvol ingeschreven!");
                }
                else{
                    statusBalk_text.setText("Er bestaat geen loper met dit loopnummer of deze is al ingeschreven");
                }
            }
            else {
                statusBalk_text.setText("gelieve eerst te betalen");
            }
        }
        catch(Exception e){
            statusBalk_text.setText("gelieve een geldige waarde voor het loop nummer op te geven");
        }
    }
}
