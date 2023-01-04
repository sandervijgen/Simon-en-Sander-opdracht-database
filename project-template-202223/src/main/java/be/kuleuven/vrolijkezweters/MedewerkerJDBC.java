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
            s.executeUpdate("INSERT INTO Medewerker( Naam, Functie, Leeftijd, Uurloon, GeldTegoed) VALUES ('" + medewerker.getNaam() + "','" + medewerker.getFunctie() + "'," + medewerker.getLeeftijd() + "," + medewerker.getUurloon() + "," + medewerker.getGeldTegoed() + " );");
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
                int geldTegoed = rs.getInt("geldTegoed");

                Medewerker medewerker = new Medewerker(medewerkerId, naam, functie, leeftijd, uurloon, geldTegoed);
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
    public static void berekenVergoeding(int wedstrijdId) {
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("Select MedewerkerId, BeginUur, EindUur From WedstrijdMedewerker where WedstrijdId = "+wedstrijdId+";");
            ArrayList<WedstrijdMedewerker> wedstrijdMedewerkers = new ArrayList<WedstrijdMedewerker>();
            while(rs.next()) {
                int medewerkerId = rs.getInt("medewerkerId");
                int beginUur = rs.getInt("BeginUur");
                int eindUur = rs.getInt("EindUur");
                wedstrijdMedewerkers.add(new WedstrijdMedewerker(0,0,medewerkerId,beginUur,eindUur,"" ));
            }
            for(var i = 0; i < wedstrijdMedewerkers.size(); i++ ) {
                WedstrijdMedewerker medewerker = wedstrijdMedewerkers.get(i);
                ResultSet rs2 = s.executeQuery("Select Uurloon, GeldTegoed From Medewerker where MedewerkerId = " + medewerker.getMedewerkerId() + ";");
                int uurloon = rs2.getInt("Uurloon");
                int geldTegoed = rs2.getInt("GeldTegoed");
                geldTegoed = geldTegoed + (uurloon * (medewerker.getEindUur()- medewerker.getBeginUur()));
                s.executeUpdate("Update Medewerker SET GeldTegoed = " + geldTegoed + " where MedewerkerId = " + medewerker.getMedewerkerId() + ";");
            }
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
        }

    }
}
