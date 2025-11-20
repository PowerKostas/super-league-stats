package com.slgr.Controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import com.slgr.Utils.SetupInfoTeams;
import java.util.ArrayList;

public class InfoController {
    @FXML
    private VBox leftVBox;

    // Static variable and method so they can also be accessed outside of this function
    private static Connection connection;
    public static void setConnection(Connection p_connection) {
        connection = p_connection;
    }

    public static ArrayList<Integer> selectedTeamIds = new ArrayList<>();


    public void initialize() throws SQLException {
        // Executes queries to get data from the database
        String query = "SELECT * FROM get_team_logo_name_id()";
        Statement statement = connection.createStatement();
        ResultSet team_logo_name_id_results = statement.executeQuery(query);


        // Extracts data row by row and sets up logos, team names and checkboxes on the left side of the page
        while (team_logo_name_id_results.next()) {
            String tempLogoLink = team_logo_name_id_results.getString("logo_link");
            String tempTeamName = team_logo_name_id_results.getString("team_name");
            int tempTeamId = team_logo_name_id_results.getInt("team_id");

            // Puts the HBox row in the scrollable VBox
            HBox row = SetupInfoTeams.getRow(tempLogoLink, tempTeamName, tempTeamId);
            leftVBox.getChildren().add(row);
        }
    }
}
