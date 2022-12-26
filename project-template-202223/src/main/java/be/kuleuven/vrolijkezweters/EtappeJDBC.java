package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import be.kuleuven.vrolijkezweters.properties.Etappe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static be.kuleuven.vrolijkezweters.connection.ConnectionManager.returnConnection;

public class EtappeJDBC {
    private static Connection connection = returnConnection();
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
}
