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
}
