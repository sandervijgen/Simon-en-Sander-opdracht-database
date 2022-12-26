package be.kuleuven.vrolijkezweters.properties;

public class Wedstrijd {
    private int wedstrijdId, afstand, inschrijvingsGeld, beginUur;
    private String plaats, datum;
    private boolean gelopen;

    public boolean isGelopen() {
        return gelopen;
    }

    public void setGelopen(boolean gelopen) {
        this.gelopen = gelopen;
    }

    public Wedstrijd(int wedstrijdId, String plaats, int afstand, int inschrijvingsGeld, String datum, int beginUur, boolean gelopen) {
        this.wedstrijdId = wedstrijdId;
        this.plaats = plaats;
        this.afstand = afstand;
        this.inschrijvingsGeld = inschrijvingsGeld;
        this.datum = datum;
        this.beginUur = beginUur;
        this.gelopen = gelopen;
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

    public int getInschrijvingsGeld() {
        return inschrijvingsGeld;
    }

    public void setInschrijvingsGeld(int inschrijvingsGeld) {
        this.inschrijvingsGeld = inschrijvingsGeld;
    }

    public int getBeginUur() {
        return beginUur;
    }

    public void setBeginUur(int beginUur) {
        this.beginUur = beginUur;
    }

    public String getPlaats() {
        return plaats;
    }

    public void setPlaats(String plaats) {
        this.plaats = plaats;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
