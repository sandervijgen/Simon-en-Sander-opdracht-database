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
    public static final String ConnectionString = "jdbc:sqlite:DatabaseLoopwedstrijd.db";

    public Connection getConnection() {
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

    public ConnectionManager() {
        try {
            // auto-creates if not exists
            connection = DriverManager.getConnection(ConnectionString);
            connection.setAutoCommit(false);
            //initTables();
        } catch (Exception e) {
            System.out.println("Db connection handle failure");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initTables() throws Exception {
        /*URI uri = ClassLoader.getSystemResource("be.kuleuven.vrolijkezweter/resources/dbcreate.sql").toURI();
        String mainPath = Paths.get(uri).toString();
        Path path = Paths.get(mainPath ,"dbcreate.sql");*/
        var sql = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("dbcreate.sql").getPath())));
        System.out.println(sql);

        var s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }


}