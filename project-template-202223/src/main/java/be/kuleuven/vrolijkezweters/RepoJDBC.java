package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
import java.sql.SQLException;

public class RepoJDBC {
    private static ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection = connectionManager.getConnection();

    public static void voegWedstrijdToe(Wedstrijd wedstrijd) {

    try
    {
        var s = connection.createStatement();
        s.executeUpdate("INSERT INTO Wedstrijd(WedstrijdId, Plaats, Afstand, InschrijvingsGeld, Datum, BeginUur) VALUES (" + wedstrijd.getWedstrijdId() + ",'" + wedstrijd.getPlaats() + "'," + wedstrijd.getAfstand() + "," + wedstrijd.getInschrijvingsGeld() + ",'" + wedstrijd.getDatum() + "'," + wedstrijd.getBeginUur() + ");");
        connection.commit();
        s.close();
    } catch(
    SQLException e)

    {
        e.printStackTrace();
    }
}
}
