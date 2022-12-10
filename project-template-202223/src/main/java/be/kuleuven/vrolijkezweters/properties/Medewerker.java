package be.kuleuven.vrolijkezweters.properties;

public class Medewerker {
    private int medewerkerId, leeftijd, uurloon;
    private String naam, functie;

    public Medewerker(int medewerkerId, String naam, String functie, int leeftijd, int uurloon) {
        this.medewerkerId = medewerkerId;
        this.naam = naam;
        this.functie = functie;
        this.leeftijd = leeftijd;
        this.uurloon = uurloon;
    }

    public int getMedewerkerId() {
        return medewerkerId;
    }

    public void setMedewerkerId(int medewerkerId) {
        this.medewerkerId = medewerkerId;
    }

    public int getLeeftijd() {
        return leeftijd;
    }

    public void setLeeftijd(int leeftijd) {
        this.leeftijd = leeftijd;
    }

    public int getUurloon() {
        return uurloon;
    }

    public void setUurloon(int uurloon) {
        this.uurloon = uurloon;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }
}
