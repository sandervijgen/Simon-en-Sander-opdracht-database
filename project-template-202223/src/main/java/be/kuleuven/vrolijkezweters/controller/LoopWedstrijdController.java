package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.EtappeJDBC;
import be.kuleuven.vrolijkezweters.LoperJDBC;
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
    private Button btn_opslaan;

    private static Wedstrijd wedstrijd;
    public static Wedstrijd getWedstrijd() {
        return wedstrijd;
    }
    int aantalEtappes, aantalLopers;
    ArrayList<Integer> etappeIds, loperIds;
    Text header = new Text();
    TextField textField = new TextField();
    int teller = 0;

    public void initialize() {
        this.wedstrijd = BeheerWedstrijdenController.getSelectedWedstrijd();
        etappeIds  = EtappeJDBC.getAantalEtappes(wedstrijd.getWedstrijdId());
        loperIds = LoperJDBC.getAantalLopers(wedstrijd.getWedstrijdId());
        aantalEtappes = etappeIds.size();
        aantalLopers = loperIds.size();
        btn_opslaan.setVisible(false);
        btn_opslaan.setDisable(true);
        btn_volgendeLoper.setOnAction(e -> volgendeLoper());
        laatZien();
    }

    private void volgendeLoper(){
        //als alles is ingevuld wegschrijven naar database
        teller++;
        if (teller == aantalLopers+1){
            btn_volgendeLoper.setDisable(true);
            btn_volgendeLoper.setVisible(false);
            btn_opslaan.setDisable(false);
            btn_opslaan.setVisible(true);
        }
        initialize();
    }

    private void laatZien(){
        header.setLayoutX(261.0);
        header.setLayoutY(100);
        header.setText("Etappe tijden van loper met loperId: " + loperIds.get(teller));
        if (teller == 0) {
            scherm.getChildren().add(header);
        }
        for (int i = 1; i < aantalEtappes + 1; i++) {
            TextField textField = new TextField();
            textField.setPromptText("tijd etappe " + i + " in seconden");
            textField.setLayoutX(261.0);
            textField.setLayoutY(120 + (30 * i));
            if (teller == 0) {
                scherm.getChildren().add(textField);
            }
        }
    }
}
