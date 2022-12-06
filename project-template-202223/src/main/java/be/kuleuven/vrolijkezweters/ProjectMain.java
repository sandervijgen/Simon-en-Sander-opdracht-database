package be.kuleuven.vrolijkezweters;

import be.kuleuven.vrolijkezweters.connection.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;


public class ProjectMain extends Application {

    private static ConnectionManager connectionManager = new ConnectionManager();
    private static Connection connection = connectionManager.getConnection();

    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("De Vrolijke Zweters Administratie hoofdscherm");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            var s = connection.createStatement();
            //s.executeUpdate("CREATE TABLE Loper (studnr INT);");
            s.executeQuery("INSERT INTO EtappeLoper(EtappeLoperId, LoperId, EtappeId,Tijd) VALUES (2,2,2,2);");
            connection.commit();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        launch();
    }
}
