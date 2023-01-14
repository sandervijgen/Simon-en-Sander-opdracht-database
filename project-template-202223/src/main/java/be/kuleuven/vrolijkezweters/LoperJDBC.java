package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class LoperJDBC {

    private static Connection connection = returnConnection();
    public static boolean voegLoperToe(Loper loper) {
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT MedewerkerId from Medewerker where Functie = 'Contact medewerker';");
            ArrayList<Integer> medewerkersIds = new ArrayList<Integer>();
            while(rs.next()) {
                    medewerkersIds.add(rs.getInt("medewerkerId"));
            }
            int random_int = (int)Math.floor(Math.random()*(medewerkersIds.size()));
            int medewerkersId = medewerkersIds.get(random_int);
            s.close();

            String sql = "INSERT INTO Loper( Naam, Leeftijd, Geslacht, Gewicht, Fysiek, Club, ContactMedewerkerId, Punten) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setString(1,loper.getNaam());
            p.setInt(2,loper.getLeeftijd());
            p.setString(3,loper.getGeslacht());
            p.setInt(4,loper.getGewicht());
            p.setString(5,loper.getFysiek());
            p.setString(6,loper.getClub());
            p.setInt(7, medewerkersId);
            p.setInt(8,loper.getPunten());
            p.executeUpdate();

            connection.commit();
            p.close();
        } catch(SQLException e)
        {
            return false;
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }
    public static ArrayList<Loper> getLoper() {

        ArrayList<Loper> lopers = new ArrayList<Loper>();
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
                Loper loper = new Loper(0,loperId, naam, leeftijd, geslacht, gewicht, fysiek,club,contactMedewerkerId,punten);
                lopers.add(loper);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lopers;
    }
    public static ArrayList getAantalLopers(int wedstrijdId) {
        ArrayList<Integer> loperIds = new ArrayList<>();
        try
        {
            String sql = "SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int loperId = rs.getInt("LoperId");
                if(!loperIds.contains(loperId)){
                    loperIds.add(loperId);
                }
            }
            p.close();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return loperIds;
    }
    public static boolean schrijfLoperIn(Wedstrijd wedstrijd, int loperId) {
        int wedstrijdId = wedstrijd.getWedstrijdId();
        ArrayList<Integer> etappeIds = new ArrayList<Integer>();
        try
        {
            String sql = "select Naam from Loper where LoperId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, loperId);
            ResultSet rs_LoperId = p.executeQuery();
            if (!rs_LoperId.next()){
                throw new SQLException("loper bestaat niet");
            }
            sql = "select EtappeId from Etappe where WedstrijdId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int etappeId = rs.getInt("EtappeId");
                etappeIds.add(etappeId);
                //gaf enkel eerste id terug als we de etappe loper direct terug gaven, wrsch mag zoeken en schrijven in db niet samen
            }
            for(int i = 0; i< etappeIds.size(); i++) {
                int etappeId = etappeIds.get(i);
                sql = "select * from EtappeLoper where LoperId = ? AND etappeId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, loperId);
                p.setInt(2, etappeId);
                ResultSet rs_bestaatAl = p.executeQuery();
                if (rs_bestaatAl.next()){
                    throw new SQLException("loper al ingeschreven");
                }
                sql = "INSERT INTO EtappeLoper( LoperId, EtappeId, Tijd) VALUES (?,?,?)";
                p = connection.prepareStatement(sql);
                p.setInt(1, loperId);
                p.setInt(2, etappeId);
                p.setInt(3,0);
                p.executeUpdate();
            }
            connection.commit();
            p.close();

        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static boolean verwijderLoper(int loperId) {
        ArrayList<Integer> etappeLoperIds = new ArrayList<Integer>();
        try
        {
            String sql = "Delete From Loper where loperId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1,loperId);
            p.executeUpdate();
            sql = "Select etappeLoperId From EtappeLoper where LoperId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, loperId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int etappeLoperId = rs.getInt("EtappeLoperId");
                etappeLoperIds.add(etappeLoperId);
            }
            for(int i=0; i < etappeLoperIds.size(); i ++ ) {
                sql = "Delete From EtappeLoper where EtappeLoperId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, etappeLoperIds.get(i));
                p.executeUpdate();
                sql = "Delete From EtappeLoper where LoperId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, loperId);
                p.executeUpdate();
                connection.commit();
                p.close();
            }
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
}
