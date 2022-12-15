package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TijdIngeefController {
    @FXML
    private Button btnVoegToe;
    @FXML
    private TextField textLoperId;
    @FXML
    private TextField textTijd;
    @FXML
    private TextField textEtappeId;

    public void initialize(){
        btnVoegToe.setOnAction(e -> voegToe());
    }

    private void voegToe() {
        int etappeId  = Integer.parseInt(textEtappeId.getText());
        int loperId  = Integer.parseInt(textLoperId.getText());
        int tijd  = Integer.parseInt(textTijd.getText());

        RepoJDBC.tijdIngeven(etappeId,loperId,tijd);
    }
}


