package be.kuleuven.vrolijkezweters.connection;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;

    private static ConnectionManager connectionManager;
    public static final String ConnectionString = "jdbc:sqlite:DatabaseLoopwedstrijd.db";

    public static Connection returnConnection() {
        if (connectionManager == null){
            connectionManager = new ConnectionManager();
        }
        return connectionManager.getConnection();
    }

    private Connection getConnection(){
        return connection;
    }

    public void flushConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
        try {
            connection = DriverManager.getConnection(ConnectionString);
            connection.setAutoCommit(false);
            initTables();
        } catch (Exception e) {
            System.out.println("Db connection handle failure");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initTables() throws Exception {

        var uri = ConnectionManager.class.getResource("/dbcreate.sql").toURI();
        var sql = new String(Files.readAllBytes(Paths.get(uri)));

        var s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }


}