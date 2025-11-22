package com.slgr.Controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import com.slgr.Utils.createTeamsRow;
import java.util.ArrayList;

public class InfoController {
    @FXML
    private VBox leftVBox;

    private ArrayList<Integer> selectedTeamIds = new ArrayList<>();


    public void initialize() {
        // Removes ugly focusing behavior when starting up the app
        Platform.runLater(() -> {
            leftVBox.requestFocus();
        });
    }


    public void start(Connection connection) throws SQLException {
        // Executes queries to get data from the database
        String query = "SELECT * FROM get_team_logo_name_id()";
        Statement statement = connection.createStatement();
        ResultSet team_logo_name_id_results = statement.executeQuery(query);


        // Extracts data row by row and sets up logos, team names, checkboxes and CRUD buttons on the left side of the page
        while (team_logo_name_id_results.next()) {
            String tempLogoLink = team_logo_name_id_results.getString("logo_link");
            String tempTeamName = team_logo_name_id_results.getString("team_name");
            int tempTeamId = team_logo_name_id_results.getInt("team_id");

            // Puts the HBox row in the scrollable VBox
            HBox row = createTeamsRow.get(tempLogoLink, tempTeamName, tempTeamId, selectedTeamIds);
            leftVBox.getChildren().add(row);
        }
    }
}
