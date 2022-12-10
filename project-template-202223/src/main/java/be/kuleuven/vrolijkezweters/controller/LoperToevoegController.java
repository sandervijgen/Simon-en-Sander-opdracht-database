package be.kuleuven.vrolijkezweters.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LoperToevoegController {

    @FXML
    private ListView geslacht_text;
    @FXML
    private Button voeg_toe;

    public void initialize(){
        geslacht_text.getItems().addAll("man","vrouw", "x");
        voeg_toe.setOnAction(e -> voegToe());
    }

    private void voegToe() {

    }


}
