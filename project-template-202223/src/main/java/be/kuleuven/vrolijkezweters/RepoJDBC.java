package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class RepoJDBC {
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
    public static ArrayList getAantalEtappes(int wedstrijdId) {
        ArrayList<Integer> etappeIds = new ArrayList<>();
        try
        {
            var s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT EtappeId from Etappe WHERE WedstrijdId = " + wedstrijdId+";");
            while(rs.next()) {
                int etappeId = rs.getInt("EtappeId");
                if(!etappeIds.contains(etappeId)){
                    etappeIds.add(etappeId);
                }
            }
            s.close();
        } catch(SQLException e)
        {
        }
        return etappeIds;
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
}


