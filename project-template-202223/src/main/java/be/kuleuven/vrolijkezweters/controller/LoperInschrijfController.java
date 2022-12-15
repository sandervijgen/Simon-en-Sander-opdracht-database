package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoperInschrijfController {
    @FXML
    private Button btnSchrijfIn;
    @FXML
    private Button btnIsBetaald;
    @FXML
    private Text textWedstrijd;
    @FXML
    private TextField textLoopNr;

    private boolean isBetaald;

    private Wedstrijd wedstrijd;

    public void initialize() {
        this.wedstrijd = BeheerWedstrijdenController.getSelectedWedstrijd();
        textWedstrijd.setText("wedstrijd in " +wedstrijd.getPlaats()+", op "+wedstrijd.getDatum());
        btnSchrijfIn.setOnAction(e->schrijfin());
        btnIsBetaald.setOnAction(e-> setIsBetaald());

    }

    private void setIsBetaald() {
        isBetaald = true;
    }

    private void schrijfin() {
        int loperId = Integer.parseInt(textLoopNr.getText());
        //todo checken of niet null en of loper bestaat
        if (isBetaald){
            RepoJDBC.schrijfLoperIn(wedstrijd, loperId);
        }
        //TODO Als niet betaalt eror
    }
}
