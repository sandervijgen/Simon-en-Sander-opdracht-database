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
}
