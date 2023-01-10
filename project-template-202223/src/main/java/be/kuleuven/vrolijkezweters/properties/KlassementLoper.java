package be.kuleuven.vrolijkezweters.properties;

import java.util.Comparator;

public class KlassementLoper {
    private int plaats;
    private int punten;
    private int loperId;
    private String naam;
    private int tijd;

    public KlassementLoper(int plaats, int punten, int loperId, String naam, int tijd){
        this.plaats = plaats;
        this.punten = punten;
        this.loperId = loperId;
        this.naam = naam;
        this.tijd = tijd;
    }

    public int getPlaats() {
        return plaats;
    }

    public void setPlaats(int plaats) {
        this.plaats = plaats;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
    public int getPunten() {
        return punten;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public int getLoperId() {
        return loperId;
    }

    public void setLoperId(int loperId) {
        this.loperId = loperId;
    }

    public int getTijd() {
        return tijd;
    }

    public void setTijd(int tijd) {
        this.tijd = tijd;
    }
}
