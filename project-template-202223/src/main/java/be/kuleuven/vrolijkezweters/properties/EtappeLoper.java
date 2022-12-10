package be.kuleuven.vrolijkezweters.properties;

public class EtappeLoper {
    private int etappeLoperID, etappeID, wedstrijdId, tijd;

    public EtappeLoper(int etappeLoperID, int etappeID, int wedstrijdId, int tijd) {
        this.etappeLoperID = etappeLoperID;
        this.etappeID = etappeID;
        this.wedstrijdId = wedstrijdId;
        this.tijd = tijd;
    }
}
