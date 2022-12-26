package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.KlassementLoper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;

import java.sql.Connection;
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
            var s = connection.createStatement();
            int rs = s.executeUpdate("INSERT INTO Wedstrijd(Plaats, Afstand, InschrijvingsGeld, Datum, BeginUur) VALUES ('" + wedstrijd.getPlaats() + "'," + wedstrijd.getAfstand() + "," + wedstrijd.getInschrijvingsGeld() + ",'" + wedstrijd.getDatum() + "'," + wedstrijd.getBeginUur() + ");");
            int wedstrijdId = s.executeQuery("SELECT WedstrijdId FROM Wedstrijd WHERE Plaats = '"+wedstrijd.getPlaats()+"' AND Afstand = "+wedstrijd.getAfstand()+" AND Datum = '"+wedstrijd.getDatum()+"';").getInt("WedstrijdId");
            for(int i=0; i<etappes.size();i++) {
                Etappe etappe = etappes.get(i);
                s.executeUpdate("INSERT INTO Etappe( WedstrijdId, Afstand, BeginKm) VALUES (" + wedstrijdId + "," + etappe.getAfstand() + "," + etappe.getBeginKm() + ");");
            }
            //Werkt niet als twee de zelfde wedstrijden gemaakt zijn want dan geeft die id door van de eerste
            //Todo check dat er geen twee de zelfde wedstrijden zijn
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

            ResultSet rs = s.executeQuery("Select EtappeId, Max(BeginKm) FROM Etappe WHERE WedstrijdId = "+wedstrijd.getWedstrijdId()+";");
            int etappeId = rs.getInt("EtappeId");
            int beginKm = rs.getInt("Max(BeginKm)");
            int afstandEtappe = wedstrijd.getAfstand() - beginKm;

            while(afstandEtappe <=0){
                s.executeUpdate("DELETE FROM Etappe WHERE EtappeId = "+ etappeId +";");
                s.executeUpdate("DELETE FROM EtappeLoper WHERE EtappeId = "+ etappeId +";");
                rs = s.executeQuery("Select EtappeId, Max(BeginKm) FROM Etappe WHERE WedstrijdId = "+wedstrijd.getWedstrijdId()+";");
                etappeId = rs.getInt("EtappeId");
                beginKm = rs.getInt("max(BeginKm)");
                afstandEtappe = wedstrijd.getAfstand() - beginKm;
            }
            s.executeUpdate("UPDATE Etappe SET afstand = "+afstandEtappe+" WHERE EtappeId = "+ etappeId +";");
            //dit is om de laatste etappe van de wedstrijd een grotere of kleinere afstand te geven moest de afstand van de volledige wedstrijd veranderen

            connection.commit();
            s.close();
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
            var s = connection.createStatement();
            s.executeUpdate("Delete From wedstrijd where wedstrijdId = "+wedstrijdId+";");
            ResultSet rs = s.executeQuery("Select EtappeId From Etappe where wedstrijdId = "+wedstrijdId+";");
            while(rs.next()) {
                int etappeId = rs.getInt("EtappeId");
                etappeIds.add(etappeId);
            }
            for(int i=0; i < etappeIds.size(); i ++ ){
                s.executeUpdate("Delete From EtappeLoper where EtappeId = "+etappeIds.get(i)+";");
            }
            s.executeUpdate("Delete From Etappe where wedstrijdId = "+wedstrijdId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
            return false;
        }
        return true;
    }
    public static ArrayList wedstrijdKlassement(int wedstrijdId) {
        ArrayList<Integer> nietUitgelopenLopersId = new ArrayList<Integer>();
        ArrayList<Integer> uitgelopenLopersId = new ArrayList<Integer>();
        ArrayList<KlassementLoper> wedstrijdKlassement = new ArrayList<KlassementLoper>();
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = "+wedstrijdId+" AND Tijd = 0;");
            while(rs.next()) {
                int nietUitgelopen = rs.getInt("LoperId");
                if(!nietUitgelopenLopersId.contains(nietUitgelopen)){
                    nietUitgelopenLopersId.add(nietUitgelopen);
                }
            }
            ResultSet rs2 = s.executeQuery("SELECT LoperId FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = "+wedstrijdId+";");
            while(rs2.next()) {
                int uitgelopen = rs2.getInt("LoperId");
                if(!nietUitgelopenLopersId.contains(uitgelopen) && !uitgelopenLopersId.contains(uitgelopen)){
                    uitgelopenLopersId.add(uitgelopen);
                }
            }
            for(int i = 0; i<uitgelopenLopersId.size();i++){
                int lopersId = uitgelopenLopersId.get(i);
                if(!nietUitgelopenLopersId.contains(i)){
                    ResultSet rs3 = s.executeQuery("SELECT sum(Tijd) FROM EtappeLoper inner join Etappe on Etappe.EtappeId = EtappeLoper.EtappeId WHERE WedstrijdId = "+wedstrijdId+" AND LoperId = "+lopersId+" ;");
                    KlassementLoper loper = new KlassementLoper(lopersId, rs3.getInt("sum(Tijd)"));
                    wedstrijdKlassement.add(loper);
                }
            }
            Collections.sort(wedstrijdKlassement, Comparator.comparing(KlassementLoper::getTijd)); //sorteren op tijd
            connection.commit();
            s.close();
            geeftPunten(wedstrijdId,wedstrijdKlassement);
        } catch(SQLException e)
        {
        }
        return wedstrijdKlassement;
    }
    public static void geeftPunten(int wedstrijdId, ArrayList<KlassementLoper> wedstrijdKlassement) {
        try
        {
            var s = connection.createStatement();
            for(int i=0;i<wedstrijdKlassement.size(); i++){
                int loperId = wedstrijdKlassement.get(i).getLoperId();
                int punt = 50-i*5;
                if(punt < 0 ){
                    punt = 0;
                }
                ResultSet rs = s.executeQuery("SELECT Punten FROM Loper WHERE LoperId = "+loperId+";");
                punt = rs.getInt("Punten") + punt;
                s.executeUpdate("UPDATE Loper SET punten = "+punt+" WHERE loperId = "+loperId+";");
            }
            connection.commit();
            s.close();
        } catch(SQLException e) {
        }

    }
    public static void tijdIngeven(int etappeId, int loperId, int tijd) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("UPDATE EtappeLoper SET TIJD = "+tijd+" WHERE EtappeId  = "+etappeId+" AND LoperId = "+loperId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
        }
    }
    public static void getGelopen(int etappeId, int loperId, int tijd) {
        try
        {
            var s = connection.createStatement();
            s.executeUpdate("UPDATE EtappeLoper SET TIJD = "+tijd+" WHERE EtappeId  = "+etappeId+" AND LoperId = "+loperId+";");
            connection.commit();
            s.close();
        } catch(SQLException e)
        {
        }
    }


}
