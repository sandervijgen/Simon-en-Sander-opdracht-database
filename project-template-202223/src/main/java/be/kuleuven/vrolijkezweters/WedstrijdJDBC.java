package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.KlassementLoper;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class WedstrijdJDBC {
    private static Connection connection = returnConnection();
    public static boolean voegWedstrijdToe(Wedstrijd wedstrijd, ArrayList<Etappe> etappes) {
        try
        {
            String sql = "INSERT INTO Wedstrijd(Plaats, Afstand, InschrijvingsGeld, Datum, BeginUur, Gelopen) VALUES (?,?,?,?,?,?)";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setString(1, wedstrijd.getPlaats());
            p.setInt(2, wedstrijd.getAfstand());
            p.setInt(3, wedstrijd.getInschrijvingsGeld());
            p.setString(4, wedstrijd.getDatum());
            p.setInt(5, wedstrijd.getBeginUur());
            p.setBoolean(6, wedstrijd.isGelopen());
            p.executeUpdate();
            sql = "SELECT WedstrijdId FROM Wedstrijd WHERE Plaats = ? AND Afstand = ? AND Datum = ?";
            p = connection.prepareStatement(sql);
            p.setString(1, wedstrijd.getPlaats());
            p.setInt(2, wedstrijd.getAfstand());
            p.setString(3, wedstrijd.getDatum());
            int wedstrijdId = p.executeQuery().getInt("WedstrijdId");
            for(int i=0; i<etappes.size();i++) {
                Etappe etappe = etappes.get(i);
                sql = "INSERT INTO Etappe( WedstrijdId, Afstand, BeginKm) VALUES (?,?,?)";
                p = connection.prepareStatement(sql);
                p.setInt(1, wedstrijdId);
                p.setInt(2, etappe.getAfstand());
                p.setInt(3, etappe.getBeginKm());
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
                boolean gelopen = rs.getBoolean("Gelopen");

                Wedstrijd wedstrijd = new Wedstrijd(wedstrijdId, plaats, afstand, inschrijvingsGeld, datum, beginUur, gelopen);
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
            String sql = "UPDATE Wedstrijd SET Plaats = ?, Afstand = ?, InschrijvingsGeld = ?, Datum  = ?, BeginUur= ? WHERE WedstrijdId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setString(1, wedstrijd.getPlaats());
            p.setInt(2, wedstrijd.getAfstand());
            p.setInt(3, wedstrijd.getInschrijvingsGeld());
            p.setString(4, wedstrijd.getDatum());
            p.setInt(5, wedstrijd.getBeginUur());
            p.setInt(6, wedstrijd.getWedstrijdId());
            p.executeUpdate();
            sql = "Select EtappeId, Max(BeginKm) FROM Etappe WHERE WedstrijdId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijd.getWedstrijdId());
            ResultSet rs = p.executeQuery();
            int etappeId = rs.getInt("EtappeId");
            int beginKm = rs.getInt("Max(BeginKm)");
            int afstandEtappe = wedstrijd.getAfstand() - beginKm;

            while(afstandEtappe <=0){
                sql = "DELETE FROM Etappe WHERE EtappeId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, etappeId);
                p.executeUpdate();
                sql = "DELETE FROM EtappeLoper WHERE EtappeId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, wedstrijd.getWedstrijdId());
                p.executeUpdate();
                //s.executeUpdate("DELETE FROM EtappeLoper WHERE EtappeId = "+ etappeId +";");
                sql = "Select EtappeId, Max(BeginKm) FROM Etappe WHERE WedstrijdId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, wedstrijd.getWedstrijdId());
                rs = p.executeQuery();
                etappeId = rs.getInt("EtappeId");
                beginKm = rs.getInt("max(BeginKm)");
                afstandEtappe = wedstrijd.getAfstand() - beginKm;
            }
            sql = "UPDATE Etappe SET afstand = ? WHERE EtappeId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, afstandEtappe);
            p.setInt(1, etappeId);
            p.executeUpdate();
            //dit is om de laatste etappe van de wedstrijd een grotere of kleinere afstand te geven moest de afstand van de volledige wedstrijd veranderen

            connection.commit();
            p.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static boolean verwijderWedstrijd(int wedstrijdId) {
        ArrayList<Integer> etappeIds = new ArrayList<Integer>();
        try
        {
            String sql = "Delete From wedstrijd where wedstrijdId = ?";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            p.executeUpdate();
            sql = "Select EtappeId From Etappe where wedstrijdId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int etappeId = rs.getInt("EtappeId");
                etappeIds.add(etappeId);
            }
            for(int i=0; i < etappeIds.size(); i ++ ){
                sql = "Delete From EtappeLoper where EtappeId = ?";
                p = connection.prepareStatement(sql);
                p.setInt(1, etappeIds.get(i));
                p.executeUpdate();
            }
            sql = "Delete From Etappe where wedstrijdId = ?";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            p.executeUpdate();
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static ArrayList wedstrijdKlassement(int wedstrijdId, boolean puntenGeven) {
        ArrayList<Integer> nietUitgelopenLopersId = new ArrayList<Integer>();
        ArrayList<Integer> uitgelopenLopersId = new ArrayList<Integer>();
        ArrayList<KlassementLoper> wedstrijdKlassement = new ArrayList<KlassementLoper>();
        try
        {
            String sql = "SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = ? AND Tijd = 0;";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1,wedstrijdId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                int nietUitgelopen = rs.getInt("LoperId");
                if(!nietUitgelopenLopersId.contains(nietUitgelopen)){
                    nietUitgelopenLopersId.add(nietUitgelopen);
                }
            }
            sql = "SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = ?;";
            p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs2 = p.executeQuery();
            while(rs2.next()) {
                int uitgelopen = rs2.getInt("LoperId");
                if(!nietUitgelopenLopersId.contains(uitgelopen) && !uitgelopenLopersId.contains(uitgelopen)){
                    uitgelopenLopersId.add(uitgelopen);
                }
            }
            for(int i = 0; i<uitgelopenLopersId.size();i++){
                int lopersId = uitgelopenLopersId.get(i);

                if(!nietUitgelopenLopersId.contains(i)){
                    sql = "SELECT naam FROM Loper WHERE LoperId = ?;";
                    p = connection.prepareStatement(sql);
                    p.setInt(1, lopersId);
                    ResultSet rs3 = p.executeQuery();
                    String naam = rs3.getString("naam");

                    sql = "SELECT sum(Tijd) FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = ? AND LoperId = ?;";
                    p = connection.prepareStatement(sql);
                    p.setInt(1, wedstrijdId);
                    p.setInt(2, lopersId);
                    rs3 = p.executeQuery();

                    int punt = 50-i*5;
                    if(punt < 0 ){
                        punt = 0;
                    }

                    KlassementLoper loper = new KlassementLoper(i+1, punt, lopersId,naam, rs3.getInt("sum(Tijd)"));
                    wedstrijdKlassement.add(loper);
                }
            }
            Collections.sort(wedstrijdKlassement, Comparator.comparing(KlassementLoper::getTijd)); //sorteren op tijd
            connection.commit();
            p.close();
            if(puntenGeven){geeftPunten(wedstrijdId,wedstrijdKlassement);}
        } catch(SQLException e)
        {
        }
        return wedstrijdKlassement;
    }
    public static void geeftPunten(int wedstrijdId, ArrayList<KlassementLoper> wedstrijdKlassement) {
        try
        {
            for(int i=0;i<wedstrijdKlassement.size(); i++){
                int loperId = wedstrijdKlassement.get(i).getLoperId();
                int punt =  wedstrijdKlassement.get(i).getPunten();
                String sql = "SELECT Punten FROM Loper WHERE LoperId = ?;";
                PreparedStatement p = connection.prepareStatement(sql);
                p.setInt(1, loperId);
                ResultSet rs = p.executeQuery();
                punt = rs.getInt("Punten") + punt;
                sql = "UPDATE Loper SET punten = "+punt+" WHERE loperId = ?;";
                p = connection.prepareStatement(sql);
                p.setInt(1, loperId);
                p.executeUpdate();
                connection.commit();
                p.close();
            }
        } catch(SQLException e) {
        }

    }
    public static void tijdIngeven(int etappeId, int loperId, int tijd) {
        try
        {
            String sql = "UPDATE EtappeLoper SET TIJD = ? WHERE EtappeId  = ? AND LoperId = ?;";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, tijd);
            p.setInt(2, etappeId);
            p.setInt(3,loperId);
            p.executeUpdate();
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
        }
    }
    public static int getGelopen(int wedstrijdId) {
        int isGelopen = 1;
        try
        {
            String sql = "Select Gelopen FROM Wedstrijd WHERE WedstrijdId = ?;";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1, wedstrijdId);
            ResultSet rs = p.executeQuery();
            isGelopen =  rs.getInt("Gelopen");
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
        }
        return isGelopen;
    }
    public static void loopWedstrijd(int wedstrijdId){
        try
        {
            String sql = "UPDATE Wedstrijd SET Gelopen = 1 WHERE WedstrijdId = ?;";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setInt(1,wedstrijdId);
            p.executeUpdate();
            connection.commit();
            p.close();
        } catch(SQLException e)
        {
        }
    }
    public static ArrayList<Loper> algemeenKlassement() {
        ArrayList<Loper> lopers = new ArrayList<Loper>();
        int i = 1;
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Loper Order by punten DESC ");
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
                Loper loper = new Loper(i, loperId, naam, leeftijd, geslacht, gewicht, fysiek,club,contactMedewerkerId,punten);
                lopers.add(loper);
                i++;
            }
            connection.commit();
            s.close();
        }
        catch(SQLException e)
        {

        }
        return lopers;
    }
}
