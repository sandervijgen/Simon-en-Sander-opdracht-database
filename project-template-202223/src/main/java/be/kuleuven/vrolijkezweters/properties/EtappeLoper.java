package be.kuleuven.vrolijkezweters.properties;

public class EtappeLoper {
    private int etappeLoperID, etappeID, wedstrijdId, tijd;

    public EtappeLoper(int etappeLoperID, int etappeID, int wedstrijdId, int tijd) {
        this.etappeLoperID = etappeLoperID;
        this.etappeID = etappeID;
        this.wedstrijdId = wedstrijdId;
        this.tijd = tijd;
    }

    public int getEtappeLoperID() {
        return etappeLoperID;
    }

    public void setEtappeLoperID(int etappeLoperID) {
        this.etappeLoperID = etappeLoperID;
    }

    public int getEtappeID() {
        return etappeID;
    }

    public void setEtappeID(int etappeID) {
        this.etappeID = etappeID;
    }

    public int getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(int wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }

    public int getTijd() {
        return tijd;
    }

    public void setTijd(int tijd) {
        this.tijd = tijd;
    }
}
