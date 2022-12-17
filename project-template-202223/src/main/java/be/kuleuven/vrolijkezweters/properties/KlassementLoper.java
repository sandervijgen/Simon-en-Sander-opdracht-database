package be.kuleuven.vrolijkezweters.properties;

import java.util.Comparator;

public class KlassementLoper {
    private int loperId;
    private int tijd;
    public KlassementLoper(int loperId, int tijd){
        this.loperId = loperId;
        this.tijd = tijd;
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
