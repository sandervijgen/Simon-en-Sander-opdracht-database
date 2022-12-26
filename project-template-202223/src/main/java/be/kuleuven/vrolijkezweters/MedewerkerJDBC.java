package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Medewerker;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import be.kuleuven.vrolijkezweters.properties.WedstrijdMedewerker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class MedewerkerJDBC {
    private static Connection connection = returnConnection();
    public static boolean voegMedewerkerToe(Medewerker medewerker) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("INSERT INTO Medewerker( Naam, Functie, Leeftijd, Uurloon) VALUES ('" + medewerker.getNaam() + "','" + medewerker.getFunctie() + "'," + medewerker.getLeeftijd() + "," + medewerker.getUurloon() + ");");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static ArrayList<Medewerker> getMedewerker() {

        ArrayList<Medewerker> medewerkers = new ArrayList<Medewerker>();
        try {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from Medewerker;");
            connection.commit();
            while(rs.next()) {
                int medewerkerId = rs.getInt("medewerkerId");
                String naam = rs.getString("naam");
                int leeftijd = rs.getInt("leeftijd");
                String functie = rs.getString("functie");
                int uurloon = rs.getInt("uurloon");

                Medewerker medewerker = new Medewerker(medewerkerId, naam, functie, leeftijd, uurloon);
                medewerkers.add(medewerker);
            }
            s.close();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return medewerkers;
    }
    public static boolean schrijfMedewerkerIn(Wedstrijd wedstrijd, WedstrijdMedewerker medewerker) {
        int wedstrijdId = wedstrijd.getWedstrijdId();
        try
        {
            var s = connection.createStatement();
            ResultSet rs_MedewerkerId = s.executeQuery("select Naam from Medewerker where MedewerkerId = " + medewerker.getMedewerkerId()+ ";");
            if (!rs_MedewerkerId.next()){
                throw new SQLException("medewerker bestaat niet");
            }
            ResultSet rs_bestaatAl = s.executeQuery("select Positie from WedstrijdMedewerker where MedewerkerId = " + medewerker.getMedewerkerId() + " AND WedstrijdId = " + wedstrijdId + ";");
            if (rs_bestaatAl.next()){
                throw new SQLException("medewerker al ingeschreven");
            }
            s.executeUpdate("INSERT INTO WedstrijdMedewerker( WedstrijdId, MedewerkerId, BeginUur, EindUur, Positie) VALUES (" + wedstrijdId + "," + medewerker.getMedewerkerId() + "," + medewerker.getBeginUur() + "," + medewerker.getEindUur() + ",'" + medewerker.getPositie() + "');");
            connection.commit();
            s.close();

        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static boolean verwijderMedewerker(int medewerkerId) {
        //TODO: alle lopers die deze medewerker als contact medewerker hadden moeten een nieuwe toegewezen krijgen
        ArrayList<Integer> wedstrijdMedewerkersIds = new ArrayList<Integer>();
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("Delete From Medewerker where medewerkerId = "+medewerkerId+";");
            ResultSet rs = s.executeQuery("Select WedstrijdMedewerkerId From WedstrijdMedewerker where medewerkerId = "+medewerkerId+";");
            while(rs.next()) {
                int wedstrijdMedewerkerId = rs.getInt("WedstrijdMedewerkerId");
                wedstrijdMedewerkersIds.add(wedstrijdMedewerkerId);
            }
            for(int i=0; i < wedstrijdMedewerkersIds.size(); i ++ ){
                s.executeUpdate("Delete From WedstrijdMedewerker where WedstrijdMedewerkerId = "+wedstrijdMedewerkersIds.get(i)+";");
            }
            s.executeUpdate("Delete From WedstrijdMedewerker where MedewerkerId = "+medewerkerId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

}
