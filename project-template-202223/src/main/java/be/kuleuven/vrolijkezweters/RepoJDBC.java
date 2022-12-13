package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RepoJDBC {
    private static ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection = connectionManager.getConnection();

    public static boolean voegWedstrijdToe(Wedstrijd wedstrijd) {
    try
    {
        var s = connection.createStatement();
        s.executeUpdate("INSERT INTO Wedstrijd(WedstrijdId, Plaats, Afstand, InschrijvingsGeld, Datum, BeginUur) VALUES (" + wedstrijd.getWedstrijdId() + ",'" + wedstrijd.getPlaats() + "'," + wedstrijd.getAfstand() + "," + wedstrijd.getInschrijvingsGeld() + ",'" + wedstrijd.getDatum() + "'," + wedstrijd.getBeginUur() + ");");
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
            ResultSet wedstrijdLijst = s.executeQuery("select * from Wedstrijd;");
            connection.commit();


            int wedstrijdId = wedstrijdLijst.getInt("wedstrijdId");
            String plaats = wedstrijdLijst.getString("plaats");
            int afstand = wedstrijdLijst.getInt("afstand");
            int inschrijvingsGeld = wedstrijdLijst.getInt("inschrijvingsGeld");
            String datum = wedstrijdLijst.getString("datum");
            int beginUur = wedstrijdLijst.getInt("beginUur");

            Wedstrijd wedstrijd = new Wedstrijd(wedstrijdId,plaats,afstand,inschrijvingsGeld,datum,beginUur);
            wedstrijds.add(wedstrijd);
            System.out.println(wedstrijd.getWedstrijdId());

            s.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return wedstrijds;
    }

    public static boolean voegLoperToe(Loper loper) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("INSERT INTO Loper(LoperId, Naam, Leeftijd, Geslacht, Gewicht, Fysiek, Club, ContactMedewerkerId, Punten) VALUES (" + loper.getLoperId() + ",'" + loper.getNaam() + "'," + loper.getLeeftijd() + ",'" + loper.getGeslacht() + "'," + loper.getGewicht() + ",'" + loper.getFysiek() + "','" + loper.getClub() + "'," + loper.getContactMedewerkerId() + "," + loper.getPunten() + ");");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
}
