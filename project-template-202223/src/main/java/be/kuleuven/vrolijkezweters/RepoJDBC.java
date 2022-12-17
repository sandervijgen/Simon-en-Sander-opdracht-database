package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;
import be.kuleuven.vrolijkezweters.properties.KlassementLoper;
import be.kuleuven.vrolijkezweters.properties.Loper;
import be.kuleuven.vrolijkezweters.properties.Wedstrijd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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
        //Werkt niet als twee de zelfde wedstrijden gemaakt zijn want dan geeft die id door van de eerste
        //Todo check dat er geen twee de zelfde wedstrijden zijn
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
            s.executeUpdate("UPDATE Etappe SET afstand = "+afstandEtappe+" WHERE EtappeId = "+ etappeId +";");
            //dit is om de laatste etappe van de wedstrijd een grotere of kleinere afstand te geven moest de afstand van de volledige wedstrijd veranderen
            //TODO Toevoeging = Etappe verwijderen als zijn afstand nu negatief wordt, met eenvoudige for loop
            //TODO EtappeLoper OOk AANPASSEN

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

    public static void schrijfLoperIn(Wedstrijd wedstrijd, int loperId) {
        int wedstrijdId = wedstrijd.getWedstrijdId();
        ArrayList<Integer> etappeIds = new ArrayList<Integer>();
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("select EtappeId from Etappe where WedstrijdId = "+wedstrijdId+";");
            while(rs.next()) {
                System.out.println(rs.getInt("EtappeId"));
                int etappeId = rs.getInt("EtappeId");
                etappeIds.add(etappeId);
                //gaf enkel eerste id terug als we de etappe loper direct terug gaven, wrsch mag zoeken en schrijven in db niet samen
            }
            for(int i = 0; i< etappeIds.size(); i++) {
                int etappeId = etappeIds.get(i);
                s.executeUpdate("INSERT INTO EtappeLoper( LoperId, EtappeId, Tijd) VALUES (" + loperId + "," + etappeId + "," + 0 + " );");
            }
            connection.commit();
            s.close();

        } catch(SQLException e)
        {

        }

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
            while(rs.next()) {
                int uitgelopen = rs.getInt("LoperId");
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
}


