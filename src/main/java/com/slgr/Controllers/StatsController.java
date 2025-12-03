package com.slgr.Controllers;

import java.io.IOException;
import java.sql.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.slgr.Utils.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatsController {
    @FXML
    private Text backButton;

    @FXML
    private Text forwardButton;

    @FXML
    private VBox firstPage;

    @FXML
    private VBox secondPage;

    @FXML
    private VBox teamsVBox;

    @FXML
    private HBox ownersColumns;

    @FXML
    private VBox ownersVBox;

    @FXML
    private HBox ownersButton;

    @FXML
    private HBox coachesColumns;

    @FXML
    private VBox coachesVBox;

    @FXML
    private HBox coachesButton;

    @FXML
    private HBox standingsColumns;

    @FXML
    private VBox standingsVBox;

    @FXML
    private HBox playersColumns;

    @FXML
    private VBox playersVBox;

    @FXML
    private HBox playersButton;

    @FXML
    private Label createButtonPlayers;

    @FXML
    private HBox dynamicQueries1;

    @FXML
    private HBox dynamicQueries2;

    @FXML
    private HBox dynamicQueries3;

    private ArrayList<Integer> selectedTeamIds = new ArrayList<>();

    private ArrayList<Object> selectedFilters = new ArrayList<>();

    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public void initialize() {
        // Removes ugly focusing behavior
        Platform.runLater(() -> {
           teamsVBox.requestFocus();
        });

        // First page = owners, coaches, standings table | Second page = players table
        secondPage.setVisible(false);
        secondPage.setManaged(false);

        backButton.setCursor(Cursor.HAND);
        forwardButton.setCursor(Cursor.HAND);

        // Initializes the array that keeps track of all dynamic queries values
        selectedFilters.add("");
        selectedFilters.add(new ArrayList<>(List.of(
                "Goalkeeper", "Centre-Back", "Left-Back", "Right-Back", "Defensive Midfield",
                "Central Midfield", "Right Midfield", "Left Midfield", "Attacking Midfield",
                "Left Winger", "Right Winger", "Second Striker", "Centre-Forward", "Other"
        )));
        selectedFilters.add(0);
        selectedFilters.add(100);
        selectedFilters.add("");
        selectedFilters.add(0);
        selectedFilters.add(100);
        selectedFilters.add(0);
        selectedFilters.add(100);
        selectedFilters.add(0);
        selectedFilters.add(100);
        selectedFilters.add(1); // 1 = Team order, 2 = Age order, 3 = Apps order, 4 = Goals order, 5 = Assists order
        selectedFilters.add(1); // 1 = Ascending order, 2 = Descending order
    }


    public void addRowsTeams() throws SQLException {
        // Executes queries to get data from the database
        String query = "SELECT * FROM get_team_logo_name_id()";
        Statement statement = connection.createStatement();
        ResultSet teamLogoNameIdResults = statement.executeQuery(query);


        // Extracts data row by row and sets up logos, team names and checkboxes on the left side of the page
        while (teamLogoNameIdResults.next()) {
            String tempLogoLink = teamLogoNameIdResults.getString(1);
            String tempTeamName = teamLogoNameIdResults.getString(2);
            int tempTeamId = teamLogoNameIdResults.getInt(3);

            // Puts the HBox row in the scrollable VBox
            HBox row = CreateRowTeams.get(tempLogoLink, tempTeamName, tempTeamId, selectedTeamIds, this);
            teamsVBox.getChildren().add(row);
        }


        // Continues the code now that a connection has been established
        addColumns();
        addCreateButtons();
        addDynamicQueries();
    }


    // Adds to the owners, coaches, standings, players VBox the corresponding rows when the teams checkbox is checked
    public void addRows(int teamId) throws SQLException {
        // Repeats the process from the teams VBox, for the other tables
        String query = "SELECT * FROM get_owners_logo(" + teamId + ")";
        Statement statement = connection.createStatement();
        ResultSet ownersTableResults = statement.executeQuery(query);

        while (ownersTableResults.next()) {
            String tempLogoLink = ownersTableResults.getString(6);
            String tempOwnerName = ownersTableResults.getString(3);
            String tempNationality = ownersTableResults.getString(4);
            Date tempDOB = ownersTableResults.getDate(5);
            int tempOwnerId = ownersTableResults.getInt(1);

            HBox row = CreateRowOwnersCoaches.get(tempLogoLink, tempOwnerName, tempNationality, tempDOB, tempOwnerId, teamId, connection, "owners");
            HelperMethods.addRowSorted(ownersVBox, row, 1, 1);
        }


        ResultSet coachesTableResults = statement.executeQuery("SELECT * FROM get_coaches_logo(" + teamId + ")");
        while (coachesTableResults.next()) {
            String tempLogoLink = coachesTableResults.getString(6);
            String tempCoachName = coachesTableResults.getString(3);
            String tempNationality = coachesTableResults.getString(4);
            Date tempDOB = coachesTableResults.getDate(5);
            int tempCoachId = coachesTableResults.getInt(1);

            HBox row = CreateRowOwnersCoaches.get(tempLogoLink, tempCoachName, tempNationality, tempDOB, tempCoachId, teamId, connection, "coaches");
            HelperMethods.addRowSorted(coachesVBox, row, 1, 1);
        }


        ResultSet standingsTableResults = statement.executeQuery("SELECT * FROM get_standings_logo(" + teamId + ")");
        while (standingsTableResults.next()) {
            String tempLogoLink = standingsTableResults.getString(6);
            String tempWins = standingsTableResults.getString(2);
            String tempDraws = standingsTableResults.getString(3);
            String tempLosses = standingsTableResults.getString(4);
            String tempPoints = standingsTableResults.getString(5);

            HBox row = CreateRowStandings.get(tempLogoLink, tempWins, tempDraws, tempLosses, tempPoints, teamId);
            HelperMethods.addRowSorted(standingsVBox, row, 1, -1);
        }


        // Gets the dynamic queries values and extracts player data based on those
        query = "SELECT * FROM search_filters_team(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement2 = connection.prepareStatement(query);

        statement2.setString(1, (String) selectedFilters.get(0));

        List<String> tempSelectedPositions1 = (List<String>) selectedFilters.get(1);
        String[] tempSelectedPositions2 = tempSelectedPositions1.toArray(new String[0]);
        java.sql.Array selectedPositions = connection.createArrayOf("varchar", tempSelectedPositions2);
        statement2.setArray(2, selectedPositions);

        statement2.setInt(3, (int) selectedFilters.get(2));
        statement2.setInt(4, (int) selectedFilters.get(3));
        statement2.setString(5, (String) selectedFilters.get(4));
        statement2.setInt(6, (int) selectedFilters.get(5));
        statement2.setInt(7, (int) selectedFilters.get(6));
        statement2.setInt(8, (int) selectedFilters.get(7));
        statement2.setInt(9, (int) selectedFilters.get(8));
        statement2.setInt(10, (int) selectedFilters.get(9));
        statement2.setInt(11, (int) selectedFilters.get(10));
        statement2.setInt(12, (int) selectedFilters.get(11));
        statement2.setInt(13, (int) selectedFilters.get(12));
        statement2.setInt(14, teamId);

        ResultSet playersTableResults = statement2.executeQuery();

        while (playersTableResults.next()) {
            String tempLogoLink = playersTableResults.getString(10);
            String tempPlayerName = playersTableResults.getString(3);
            String tempPlayerPosition = playersTableResults.getString(4);
            int tempAge = playersTableResults.getInt(5);
            String tempNationality = playersTableResults.getString(6);
            int tempAppearances = playersTableResults.getInt(7);
            int tempGoals = playersTableResults.getInt(8);
            int tempAssists = playersTableResults.getInt(9);
            int tempPlayerId = playersTableResults.getInt(1);
            int tempTeamId = playersTableResults.getInt(2);

            HBox row = CreateRowPlayers.get(tempLogoLink, tempPlayerName, tempPlayerPosition, tempAge, tempNationality, tempAppearances, tempGoals, tempAssists, tempPlayerId, tempTeamId, connection);
            HelperMethods.addRowSorted(playersVBox, row, (int) selectedFilters.get(11), (int) selectedFilters.get(12)); // 12 = Order choice, 13 = Order type
        }
    }


    //  Removes from the owners, coaches VBox the corresponding rows when the teams checkbox is unchecked
    public void removeRows(int teamId) {
        ownersVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(1); // keys.get(1) = team id of the row
        });

        coachesVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(1);
        });

        standingsVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(0);
        });

        playersVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(1);
        });
    }


    // Hacky way (it copies the structure of the actual HBox row) to add headers to the tables
    public void addColumns() {
        CreateColumnsOwnersCoaches.get(ownersColumns, connection);
        CreateColumnsOwnersCoaches.get(coachesColumns, connection);
        CreateColumnsStandings.get(standingsColumns, connection);
        CreateColumnsPlayers.get(playersColumns, connection);
    }


    public void addCreateButtons() {
        Label createButtonOwners = Widgets.createCreateButton("Add Owner", "owners", connection, selectedTeamIds);
        ownersButton.getChildren().add(createButtonOwners);

        Label createButtonCoaches = Widgets.createCreateButton("Add Coach", "coaches", connection, selectedTeamIds);
        coachesButton.getChildren().add(createButtonCoaches);

        createButtonPlayers = Widgets.createCreateButton("Add Player", "players", connection, selectedTeamIds);
        playersButton.getChildren().add(createButtonPlayers);
    }


    public void addDynamicQueries() {
        Widgets.addDynamicQueries(dynamicQueries1, dynamicQueries2, dynamicQueries3, playersVBox, connection, createButtonPlayers, selectedFilters);
    }


    public void backButton(Event event) throws IOException {
        // If we are on the first page, go to the menu
        if (firstPage.isVisible()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(root);
        }

        // If we are on the second page, go to the first page
        else {
            teamsVBox.requestFocus();
            forwardButton.setVisible(true);
            forwardButton.setManaged(true);
            secondPage.setVisible(false);
            secondPage.setManaged(false);
            firstPage.setVisible(true);
            firstPage.setManaged(true);
        }
    }


    // Go to the second page
    public void forwardButton()  {
        forwardButton.setVisible(false);
        forwardButton.setManaged(false);
        firstPage.setVisible(false);
        firstPage.setManaged(false);
        secondPage.setVisible(true);
        secondPage.setManaged(true);
    }
}
