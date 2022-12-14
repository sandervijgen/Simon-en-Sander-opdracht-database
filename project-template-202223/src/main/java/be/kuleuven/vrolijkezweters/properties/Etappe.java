package be.kuleuven.vrolijkezweters.properties;

public class Etappe {
    private int etappeID, wedstrijdId, afstand, beginKm;

    public Etappe(Integer etappeID, int wedstrijdId, int afstand, int beginKm) {
        this.etappeID = etappeID;
        this.wedstrijdId = wedstrijdId;
        this.afstand = afstand;
        this.beginKm = beginKm;
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

    public int getBeginKm() {
        return beginKm;
    }

    public void setBeginKm(int beginKm) {
        this.beginKm = beginKm;
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
