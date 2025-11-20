package com.slgr.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.geometry.Pos;
import io.github.cdimascio.dotenv.Dotenv;

public class InfoController {
    @FXML
    private VBox leftVBox;


    public void initialize() throws SQLException {
        // Gets sensitive information from a .env file
        Dotenv dotenv = Dotenv.load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");


        // Connects to the database and executes queries to get data from the database
        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM get_team_name_logo()";
        ResultSet team_name_logo_results = statement.executeQuery(query);


        // Dynamically sets up the page
        while (team_name_logo_results.next()) { // Extracts data row by row
            // Sets up logos, team names and checkboxes on the left side of the page
            String tempTeamName = team_name_logo_results.getString("team_name");
            String tempLogoLink = team_name_logo_results.getString("logo_link");

            Image image = new Image(getClass().getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            imageView.setPreserveRatio(true);

            Label label = new Label();
            label.setText(tempTeamName);
            label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px;");

            CheckBox checkBox = new CheckBox();

            // Puts the 3 widgets in an HBox and then puts the HBox in the scrollable VBox
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(imageView, label, checkBox);

            leftVBox.getChildren().add(row);
        }
    }
}
