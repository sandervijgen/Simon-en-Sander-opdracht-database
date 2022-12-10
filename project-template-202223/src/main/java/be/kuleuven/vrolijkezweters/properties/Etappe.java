package be.kuleuven.vrolijkezweters.properties;

public class Etappe {
    private int etappeID, wedstrijdId, afstand;

    public Etappe(int etappeID, int wedstrijdId, int afstand) {
        this.etappeID = etappeID;
        this.wedstrijdId = wedstrijdId;
        this.afstand = afstand;
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

    public int getAfstand() {
        return afstand;
    }

    public void setAfstand(int afstand) {
        this.afstand = afstand;
    }
}
