package com.slgr.Controllers;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class MenuController {
    @FXML
    private Text loadingText;


    public Connection connectToDatabase() {
        // Gets sensitive information from a .env file
        Dotenv dotenv = Dotenv.load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");


        // Connects to the database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            return connection;
        }

        catch (SQLException e) {
            return connection;
        }
    }

    public void infoButton(Event event) {
        loadingText.setText("Loading...");

        // Small pause before connecting to the database because it needs time drawing the loading text
        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> {
           Connection connection = connectToDatabase();

           try {
               if (connection != null) { // Successful connection to the database
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/info-view.fxml"));
                   Parent root = loader.load();

                   // Calls the start function, after the initialize function is done
                   InfoController infoController = loader.getController();
                   infoController.start(connection);

                   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                   Scene scene = stage.getScene();
                   scene.setRoot(root);
               }

               else { // Unsuccessful connection
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/error-view.fxml"));
                   Parent root = loader.load();
                   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                   Scene scene = stage.getScene();
                   scene.setRoot(root);
               }
           }

           catch (IOException | SQLException ex) {

           }
        });

        pause.play();
    }


    public void exitButton() {
        Platform.exit();
    }
}
