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
}
