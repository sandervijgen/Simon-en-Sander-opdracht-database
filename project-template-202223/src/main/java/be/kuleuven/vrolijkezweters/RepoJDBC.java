package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RepoJDBC {
    private static ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection = connectionManager.getConnection();

    public static int voegWedstrijdToe(Wedstrijd wedstrijd) {
        int wedstrijdId = -1;
    try
    {
        var s = connection.createStatement();
        s.executeUpdate("INSERT INTO Wedstrijd(Plaats, Afstand, InschrijvingsGeld, Datum, BeginUur) VALUES ('" + wedstrijd.getPlaats() + "'," + wedstrijd.getAfstand() + "," + wedstrijd.getInschrijvingsGeld() + ",'" + wedstrijd.getDatum() + "'," + wedstrijd.getBeginUur() + ");");
        ResultSet rs = s.executeQuery("SELECT WedstrijdId FROM  Wedstrijd WHERE plaats = '" + wedstrijd.getPlaats() + "' AND Afstand = " + wedstrijd.getAfstand() + " AND Datum = '" + wedstrijd.getDatum() + "';");
        connection.commit();
        wedstrijdId = rs.getInt("WedstrijdId");
        s.close();
    } catch(SQLException e)
    {
        return wedstrijdId;
    }
    return wedstrijdId;
    }

    public static boolean verwijderWedstrijd(int wedstrijdId) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("Delete From wedstrijd where wedstrijdId = "+wedstrijdId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

    public static ArrayList<Wedstrijd> getWedstrijden() {
        ArrayList<Wedstrijd> wedstrijds = new ArrayList<Wedstrijd>();

        try {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from Wedstrijd;");
            connection.commit();

            while(rs.next()) {

                int wedstrijdId = rs.getInt("wedstrijdId");
                String plaats = rs.getString("plaats");
                int afstand = rs.getInt("afstand");
                int inschrijvingsGeld = rs.getInt("inschrijvingsGeld");
                String datum = rs.getString("datum");
                int beginUur = rs.getInt("beginUur");


                Wedstrijd wedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand, inschrijvingsGeld, datum, beginUur);
                wedstrijds.add(wedstrijd);
            }

            s.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return wedstrijds;
    }

    public static Boolean bewerkWedstrijd(Wedstrijd wedstrijd) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("UPDATE Wedstrijd SET Plaats = '"+wedstrijd.getPlaats()+"', Afstand = "+wedstrijd.getAfstand()+", InschrijvingsGeld = "+wedstrijd.getInschrijvingsGeld()+", Datum  = '"+wedstrijd.getDatum()+"', BeginUur= "+wedstrijd.getBeginUur()+" WHERE WedstrijdId = "+wedstrijd.getWedstrijdId() +";");

            ResultSet rs = s.executeQuery("Select max(EtappeId) FROM Etappe WHERE WedstrijdId = "+wedstrijd.getWedstrijdId()+";");
            int etappeId = rs.getInt("max(EtappeId)");
            rs = s.executeQuery("Select BeginKm FROM Etappe WHERE EtappeId = "+etappeId+";");
            int beginKm = rs.getInt("BeginKm");
            int afstandEtappe = wedstrijd.getAfstand() - beginKm;
            s.executeUpdate("UPDATE Etappe SET afstand = "+afstandEtappe+" WHERE EtappeId = "+ etappeId +";");//dit is om de laatste etappe van de wedstrijd een grotere of kleinere afstand te geven moest de afstand van de volledige wedstrijd veranderen
            //Toevoeging = Etappe verwijderen als zijn afstand nu negatief wordt, met eenvoudige for loop

            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

    public static boolean voegLoperToe(Loper loper) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("INSERT INTO Loper( Naam, Leeftijd, Geslacht, Gewicht, Fysiek, Club, ContactMedewerkerId, Punten) VALUES ('" + loper.getNaam() + "'," + loper.getLeeftijd() + ",'" + loper.getGeslacht() + "'," + loper.getGewicht() + ",'" + loper.getFysiek() + "','" + loper.getClub() + "'," + loper.getContactMedewerkerId() + "," + loper.getPunten() + ");");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

    public static ArrayList<Loper> getLoper() {

            ArrayList<Loper> wedstrijds = new ArrayList<Loper>();

            try {
                var s = connection.createStatement();
                ResultSet rs = s.executeQuery("select * from Loper;");
                connection.commit();

                while(rs.next()) {

                    int loperId = rs.getInt("loperId");
                    String naam = rs.getString("naam");
                    int leeftijd = rs.getInt("leeftijd");
                    String geslacht = rs.getString("geslacht");
                    int gewicht = rs.getInt("gewicht");
                    String fysiek = rs.getString("fysiek");
                    String club  = rs.getString("club");
                    int contactMedewerkerId = rs.getInt("contactMedewerkerId");
                    int punten = rs.getInt("punten");


                    Loper loper = new Loper(loperId, naam, leeftijd, geslacht, gewicht, fysiek,club,contactMedewerkerId,punten);
                    wedstrijds.add(loper);
                }

                s.close();

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
            return wedstrijds;
        }



    public static boolean voegEtappeToe(Etappe etappe) {

        try
        {
            var s = connection.createStatement();
            s.executeUpdate("INSERT INTO Etappe( WedstrijdId, Afstand, BeginKm) VALUES (" + etappe.getWedstrijdId() + "," + etappe.getAfstand() + "," + etappe.getBeginKm() + ");");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

    public static boolean bewerkEtappes(int wedstrijdId, ArrayList<Etappe> etappesLijst) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("DELETE FROM Etappe WHERE WedstrijdId = "+wedstrijdId+";");
            for(int i = 0; i < etappesLijst.size(); i++){
                Etappe etappe = etappesLijst.get(i);
                s.executeUpdate("INSERT INTO Etappe( WedstrijdId, Afstand, BeginKm) VALUES (" + etappe.getWedstrijdId() + "," + etappe.getAfstand() + "," + etappe.getBeginKm() + ");");
            }
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

    }


