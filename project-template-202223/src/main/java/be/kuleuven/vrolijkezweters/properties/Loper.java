package be.kuleuven.vrolijkezweters.properties;

public class Loper {
    private int loperId, leeftijd, gewicht, contactMedewerkerId, punten;
    private String naam, geslacht, fysiek, club;
    public Loper(int loperId, String naam, int leeftijd, String geslacht, int gewicht, String fysiek, String club, int contactMedewerkerId, int punten) {
        this.loperId = loperId;
        this.naam = naam;
        this.leeftijd = leeftijd;
        this.geslacht = geslacht;
        this.gewicht = gewicht;
        this.fysiek = fysiek;
        this.club = club;
        this.contactMedewerkerId = contactMedewerkerId;
        this.punten = punten;
    }

    public int getLoperId() {
        return loperId;
    }

    public void setLoperId(int loperId) {
        this.loperId = loperId;
    }

    public int getLeeftijd() {
        return leeftijd;
    }

    public void setLeeftijd(int leeftijd) {
        this.leeftijd = leeftijd;
    }

    public int getGewicht() {
        return gewicht;
    }

    public void setGewicht(int gewicht) {
        this.gewicht = gewicht;
    }

    public int getContactMedewerkerId() {
        return contactMedewerkerId;
    }

    public void setContactMedewerkerId(int contactMedewerkerId) {
        this.contactMedewerkerId = contactMedewerkerId;
    }

    public int getPunten() {
        return punten;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getGeslacht() {
        return geslacht;
    }

    public void setGeslacht(String geslacht) {
        this.geslacht = geslacht;
    }

    public String getFysiek() {
        return fysiek;
    }

    public void setFysiek(String fysiek) {
        this.fysiek = fysiek;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
