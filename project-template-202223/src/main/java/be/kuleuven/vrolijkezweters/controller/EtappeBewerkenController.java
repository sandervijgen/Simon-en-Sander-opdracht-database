package be.kuleuven.vrolijkezweters.controller;

import be.kuleuven.vrolijkezweters.RepoJDBC;
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

    private Wedstrijd wedstrijd;

    ArrayList<TextField> afstandWaardes = new ArrayList<TextField>();

    public void initialize(){
        btnPasToe.setOnAction(e -> PasToe());
        btnVoegToe.setOnAction(e -> voegToe());
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }

    public void setWedstrijd(Wedstrijd wedstrijd) {
        this.wedstrijd = wedstrijd;
    }

    private void PasToe() {

        int aantal = Integer.parseInt(aantalEtappes.getText());
        for (int i = 1; i < aantal; i++) {
            TextField textField = new TextField();
            textField.setLayoutX(135.0 + (60 * i));
            textField.setLayoutY(224);
            afstandWaardes.add(textField);
            scherm.getChildren().add(textField);
        }

    }
    private void voegToe(){
        wedstrijd = new Wedstrijd(1,"1",10,1,"1",1);//moet vervangen door wedstrijd die die meekrijgt

        for (int i = 0; i <= afstandWaardes.size(); i++) {
            if(i == 0){
                TextField textFieldEinde = afstandWaardes.get(i);
                int beginKm = 0;
                int eindKm = Integer.parseInt(textFieldEinde.getText());
                Etappe etappe = new Etappe(i, wedstrijd.getWedstrijdId(), (eindKm -beginKm) , beginKm);
                if(!RepoJDBC.voegEtappeToe(etappe)){
                    System.out.println("Er is iets mis met etappe" + (i+1));
                };


                //TODO if beginKM > eindKM catchen
            }
            else if(i == afstandWaardes.size()){
                TextField textFieldBegin = afstandWaardes.get(i-1);
                int beginKm = Integer.parseInt(textFieldBegin.getText());
                int eindKm = wedstrijd.getAfstand();
                Etappe etappe = new Etappe(i, wedstrijd.getWedstrijdId(), (eindKm -beginKm) , beginKm);
                if(!RepoJDBC.voegEtappeToe(etappe)){
                    System.out.println("Er is iets mis met etappe" + (i+1));
                };
            }
            else {
                TextField textFieldBegin = afstandWaardes.get(i-1);
                TextField textFieldEinde = afstandWaardes.get(i);
                int beginKm = Integer.parseInt(textFieldBegin.getText());
                int eindKm = Integer.parseInt(textFieldEinde.getText());
                Etappe etappe = new Etappe(i, wedstrijd.getWedstrijdId(), (eindKm -beginKm) , beginKm);
                if(!RepoJDBC.voegEtappeToe(etappe)){
                    System.out.println("Er is iets mis met etappe" + (i+1));
                };
            }

    }
    }

}
