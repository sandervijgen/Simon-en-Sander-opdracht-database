package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
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
                Loper loper = new Loper(loperId, naam, leeftijd, geslacht, gewicht, fysiek,club,contactMedewerkerId,punten);
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
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = "+wedstrijdId+";");
            while(rs.next()) {
                int loperId = rs.getInt("LoperId");
                if(!loperIds.contains(loperId)){
                    loperIds.add(loperId);
                }
            }
            s.close();
        } catch(SQLException e)
        {
        }
        return loperIds;
    }
    public static boolean schrijfLoperIn(Wedstrijd wedstrijd, int loperId) {
        int wedstrijdId = wedstrijd.getWedstrijdId();
        ArrayList<Integer> etappeIds = new ArrayList<Integer>();
        try
        {
            var s = connection.createStatement();
            ResultSet rs_LoperId = s.executeQuery("select Naam from Loper where LoperId = " + loperId+ ";");
            if (!rs_LoperId.next()){
                throw new SQLException("loper bestaat niet");
            }
            ResultSet rs = s.executeQuery("select EtappeId from Etappe where WedstrijdId = "+wedstrijdId+";");
            while(rs.next()) {
                int etappeId = rs.getInt("EtappeId");
                etappeIds.add(etappeId);
                //gaf enkel eerste id terug als we de etappe loper direct terug gaven, wrsch mag zoeken en schrijven in db niet samen
            }
            for(int i = 0; i< etappeIds.size(); i++) {
                int etappeId = etappeIds.get(i);
                ResultSet rs_bestaatAl = s.executeQuery("select Tijd from EtappeLoper where LoperId = " + loperId + " AND etappeId = " + etappeId + ";");
                if (rs_bestaatAl.next()){
                    throw new SQLException("loper al ingeschreven");
                }
                s.executeUpdate("INSERT INTO EtappeLoper( LoperId, EtappeId, Tijd) VALUES (" + loperId + "," + etappeId + "," + 0 + " );");
            }
            connection.commit();
            s.close();

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
            var s = connection.createStatement();
            s.executeUpdate("Delete From Loper where loperId = "+loperId+";");
            ResultSet rs = s.executeQuery("Select etappeLoperId From EtappeLoper where LoperId = "+loperId+";");
            while(rs.next()) {
                int etappeLoperId = rs.getInt("EtappeLoperId");
                etappeLoperIds.add(etappeLoperId);
            }
            for(int i=0; i < etappeLoperIds.size(); i ++ ){
                s.executeUpdate("Delete From EtappeLoper where EtappeLoperId = "+etappeLoperIds.get(i)+";");
            }
            s.executeUpdate("Delete From EtappeLoper where LoperId = "+loperId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }

}
