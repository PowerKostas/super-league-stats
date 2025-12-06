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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class MenuController {
    @FXML
    private Text loadingText;

    // Static variable for connection, so it stays the same when entering and exiting views
    public static Connection connection;


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


    public void statsButton(Event event) {
        loadingText.setText("Loading...");

        // Small pause before connecting to the database because it needs time drawing the loading text
        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> {
            connection = connectToDatabase();

            try {
                if (connection != null) { // Successful connection to the database
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/stats-view.fxml"));
                    Parent root = loader.load();

                    // Calls the starting functions, after the initialize function is done
                    StatsController statsController = loader.getController();
                    statsController.setConnection(connection);
                    statsController.addRowsTeams();

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


    public void logButton(Event event) {
        loadingText.setText("Loading...");

        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> {
            connection = connectToDatabase();

            try {
                if (connection != null) { // Successful connection to the database
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/log-view.fxml"));
                    Parent root = loader.load();

                    // Calls the starting functions, after the initialize function is done
                    LogController logController = loader.getController();
                    logController.setConnection(connection);
                    logController.addRowsLogging();

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

            catch (IOException ex) {

            }
        });

        pause.play();
    }


    public void infoButton(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/info-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }


    public void exitButton() {
        loadingText.setText("Loading...");

        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> {
            connection = connectToDatabase();

            if (connection != null) { // Successful connection to the database
                // Resets the logging table, when the user exits the app
                String query = "CALL reset_logging()";
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.executeUpdate();
                } catch (SQLException ex) {

                }

                Platform.exit();
            } else { // Unsuccessful connection
                Platform.exit();
            }
        });

        pause.play();
    }
}
