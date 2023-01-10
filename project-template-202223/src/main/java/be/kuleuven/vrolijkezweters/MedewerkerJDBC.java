package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Medewerker;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import be.kuleuven.vrolijkezweters.properties.WedstrijdMedewerker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class MedewerkerJDBC {
    private static Connection connection = returnConnection();
    public static boolean voegMedewerkerToe(Medewerker medewerker) {
        try
        {
            String sql = "INSERT INTO Medewerker( Naam, Functie, Leeftijd, Uurloon, GeldTegoed) VALUES (?,?,?,?,?)";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setString(1, medewerker.getNaam());
            p.setString(2, medewerker.getFunctie());
            p.setInt(3, medewerker.getLeeftijd());
            p.setInt(4, medewerker.getUurloon());
            p.setInt(5, medewerker.getGeldTegoed());
            p.executeUpdate();
            connection.commit();
            p.close();
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
            String sql = "select Naam from Medewerker where MedewerkerId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, medewerker.getMedewerkerId());
            ResultSet rs_MedewerkerId = p.executeQuery();
            if (!rs_MedewerkerId.next()){
                throw new SQLException("medewerker bestaat niet");
            }
            sql = "select Positie from WedstrijdMedewerker where MedewerkerId = ? AND WedstrijdId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, medewerker.getMedewerkerId());
            p.setInt(2, wedstrijdId);
            ResultSet rs_bestaatAl = p.executeQuery();
            if (rs_bestaatAl.next()){
                throw new SQLException("medewerker al ingeschreven");
            }
            sql = "INSERT INTO WedstrijdMedewerker( WedstrijdId, MedewerkerId, BeginUur, EindUur, Positie) VALUES (?,?,?,?,?)";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            p.setInt(2, medewerker.getMedewerkerId());
            p.setInt(3, medewerker.getBeginUur());
            p.setInt(4, medewerker.getEindUur());
            p.setString(5, medewerker.getPositie());
            p.executeUpdate();
            connection.commit();
            p.close();

        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static boolean verwijderMedewerker(int medewerkerId) {
        ArrayList<Integer> wedstrijdMedewerkersIds = new ArrayList<Integer>();
        try
        {
            String sql = "Delete From Medewerker where medewerkerId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, medewerkerId);
            p.executeUpdate();
            sql = "Select WedstrijdMedewerkerId From WedstrijdMedewerker where medewerkerId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, medewerkerId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int wedstrijdMedewerkerId = rs.getInt("WedstrijdMedewerkerId");
                wedstrijdMedewerkersIds.add(wedstrijdMedewerkerId);
            }
            for(int i=0; i < wedstrijdMedewerkersIds.size(); i ++ ){
                sql = "Delete From WedstrijdMedewerker where WedstrijdMedewerkerId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, wedstrijdMedewerkersIds.get(i));
                p.executeUpdate();
            }
            sql = "Delete From WedstrijdMedewerker where MedewerkerId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, medewerkerId);
            p.executeUpdate();
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static void berekenVergoeding(int wedstrijdId) {
        try
        {
            String sql = "Select MedewerkerId, BeginUur, EindUur From WedstrijdMedewerker where WedstrijdId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs = p.executeQuery();
            ArrayList<WedstrijdMedewerker> wedstrijdMedewerkers = new ArrayList<WedstrijdMedewerker>();
            while(rs.next()) {
                int medewerkerId = rs.getInt("medewerkerId");
                int beginUur = rs.getInt("BeginUur");
                int eindUur = rs.getInt("EindUur");
                wedstrijdMedewerkers.add(new WedstrijdMedewerker(0,0,medewerkerId,beginUur,eindUur,"" ));
            }
            for(var i = 0; i < wedstrijdMedewerkers.size(); i++ ) {
                WedstrijdMedewerker medewerker = wedstrijdMedewerkers.get(i);
                sql = "Select Uurloon, GeldTegoed From Medewerker where MedewerkerId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, medewerker.getMedewerkerId());
                ResultSet rs2 = p.executeQuery();
                int uurloon = rs2.getInt("Uurloon");
                int geldTegoed = rs2.getInt("GeldTegoed");
                geldTegoed = geldTegoed + (uurloon * (medewerker.getEindUur()- medewerker.getBeginUur()));
                sql = "Update Medewerker SET GeldTegoed = ? where MedewerkerId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, geldTegoed);
                p.setInt(2,medewerker.getMedewerkerId());
                p.executeUpdate();
            }
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
        }

    }
}
