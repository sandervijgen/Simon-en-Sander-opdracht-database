package be.kuleuven.vrolijkezweters.properties;

public class WedstrijdMedewerker {
    private int wedstrijdMedewerkerId, wedstrijdId, medewerkerId, beginUur, eindUur;
    private String positie;

    public WedstrijdMedewerker(int wedstrijdMedewerkerId, int wedstrijdId, int medewerkerId, int beginUur, int eindUur, String positie) {
        this.wedstrijdMedewerkerId = wedstrijdMedewerkerId;
        this.wedstrijdId = wedstrijdId;
        this.medewerkerId = medewerkerId;
        this.beginUur = beginUur;
        this.eindUur = eindUur;
        this.positie = positie;
    }

    public int getWedstrijdMedewerkerId() {
        return wedstrijdMedewerkerId;
    }

    public void setWedstrijdMedewerkerId(int wedstrijdMedewerkerId) {
        this.wedstrijdMedewerkerId = wedstrijdMedewerkerId;
    }

    public int getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(int wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }

    public int getMedewerkerId() {
        return medewerkerId;
    }

    public void setMedewerkerId(int medewerkerId) {
        this.medewerkerId = medewerkerId;
    }

    public int getBeginUur() {
        return beginUur;
    }

    public void setBeginUur(int beginUur) {
        this.beginUur = beginUur;
    }

    public int getEindUur() {
        return eindUur;
    }

    public void setEindUur(int eindUur) {
        this.eindUur = eindUur;
    }

    public String getPositie() {
        return positie;
    }

    public void setPositie(String positie) {
        this.positie = positie;
    }
}
