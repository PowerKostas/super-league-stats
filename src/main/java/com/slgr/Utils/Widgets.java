package com.slgr.Utils;

import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.ArrayList;

public class Widgets {
    public static Label createDeleteButton(String tooltip_text, Connection connection, String table) {
        Label deleteButton = new Label();
        deleteButton.setText("🗑");
        deleteButton.setStyle("-fx-font-size: 28px; -fx-text-fill: red;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        deleteButton.setTooltip(tooltip);
        deleteButton.setCursor(Cursor.HAND);


        // Click event to delete row from the VBox and also from the database
        deleteButton.setOnMouseClicked(e -> {
            Label deleteLabel = (Label) e.getSource();
            HBox row = (HBox) deleteLabel.getParent();
            VBox parentVBox = (VBox) row.getParent();
            parentVBox.getChildren().remove(row);

            ArrayList<Integer> keys = (ArrayList<Integer>) row.getUserData();
            String query = "CALL delete_row_" + table + "(" + keys.get(0) + "," + keys.get(1) + ")"; // keys.get(0) = owner id of the row, keys.get(1) = team id of the row
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate();
            }

            catch (SQLException ex) {

            }
        });

        return deleteButton;
    }


    public static Label createCreateButton(String tooltip_text, String table, Connection connection, ArrayList<Integer> selectedTeamIds) {
        Label createButton = new Label();
        createButton.setText("✚");
        createButton.setStyle("-fx-font-size: 32px; -fx-text-fill: green;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        createButton.setTooltip(tooltip);
        createButton.setCursor(Cursor.HAND);


        // Click event to create a dropdown menu with all the available team names, now the user can only assign data
        // to these chosen teams
        createButton.setOnMouseClicked(e -> {
            ComboBox<Label> teamComboBox = new ComboBox<>();
            teamComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px; -fx-base: white");
            teamComboBox.setPromptText("Choose Team");

            // Fixes layout
            createButton.setVisible(false);
            HBox buttonRow = (HBox) createButton.getParent();
            buttonRow.getChildren().add(teamComboBox);

            try {
                String query = "SELECT * FROM get_team_logo_name_id()";
                Statement statement = connection.createStatement();
                ResultSet teamLogoNameIdResults = statement.executeQuery(query);

                while (teamLogoNameIdResults.next()) {
                    String tempTeamName = teamLogoNameIdResults.getString(2);
                    Label item = new Label(tempTeamName);
                    teamComboBox.getItems().add(item);

                    // Every combobox item has a logo link, team id and coach id behind it
                    ArrayList<Object> keys = new ArrayList<>();
                    keys.add(teamLogoNameIdResults.getString(1)); // Logo link
                    keys.add(teamLogoNameIdResults.getInt(3)); // Team ID
                    item.setUserData(keys);
                }
            }

            catch (SQLException ex) {

            }

            // When clicking a combobox item, it adds a new empty row to the VBox with just the selected team's logo
            teamComboBox.setOnAction(ev -> {
                Label selectedItem = teamComboBox.getValue();
                ArrayList<Object> keys = (ArrayList<Object>) selectedItem.getUserData();
                String logoLink = (String) keys.get(0);
                int teamId = (int) keys.get(1);

                // Creates the new data id by get the current max data id for the selected team in the combobox and adds 1
                int dataId = 0;
                try {
                    String query = "SELECT create_id_" + table + "(" + teamId + ")";
                    Statement statement = connection.createStatement();
                    ResultSet dataIdResult = statement.executeQuery(query);

                    while (dataIdResult.next()) {
                        dataId = dataIdResult.getInt(1);
                    }
                }

                catch (SQLException ex) {

                }

                // Adds the new row in the database
                try {
                    String query = "CALL create_row_" + table + "(?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, dataId);
                    statement.setInt(2, teamId);
                    statement.executeUpdate();
                }

                catch (SQLException ex) {

                }

                // Only adds the new row to the VBox if the team's checkbox is checked
                if (selectedTeamIds.contains(teamId)) {
                    HBox row;
                    if (!table.equals("players")) {
                        row = CreateRowOwnersCoaches.get(logoLink, null, null, null, dataId, teamId, connection, table);
                    }

                    else {
                        row = CreateRowPlayers.get(logoLink, null, null, null, null, null, null, null, dataId, teamId, connection);
                    }

                    VBox parentVBox = (VBox) buttonRow.getParent();
                    VBox ownersCoachesPlayersVBox = (VBox) parentVBox.getChildren().get(1); // 1 = VBox with fx id: ownersVBox or VBox with fx id: coachesVBox or VBox with fx id: playersVBox
                    HelperMethods.addRowSorted(ownersCoachesPlayersVBox, row, 1);
                }


                // Fixes layout
                buttonRow.getChildren().remove(teamComboBox);
                createButton.setVisible(true);
                createButton.requestFocus();
            });
        });

        return createButton;
    }


    public static void addDynamicQueries(HBox dynamicQueries1, HBox dynamicQueries2, HBox dynamicQueries3, VBox playersVBox, Connection connection, Label createButtonPlayers) {
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter name...");
        nameTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        nameTextField.setPrefWidth(0);
        nameTextField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameTextField, Priority.ALWAYS);

        ComboBox<String> positionComboBox = new ComboBox<>();
        positionComboBox.setValue("All Positions");
        positionComboBox.getItems().addAll("All Positions", "Goalkeeper", "Centre-Back", "Left-Back", "Right-Back",
                                           "Defensive Midfield", "Central Midfield", "Right Midfield", "Left Midfield",
                                           "Attacking Midfield", "Left Winger", "Right Winger", "Second Striker",
                                           "Centre-Forward");
        positionComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px; -fx-base: white");
        positionComboBox.setPrefWidth(0);
        positionComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(positionComboBox, Priority.ALWAYS);
        positionComboBox.setMaxHeight(Double.MAX_VALUE);
        positionComboBox.setCursor(Cursor.HAND);

        TextField nationalityTextField = new TextField();
        nationalityTextField.setPromptText("Enter nationality...");
        nationalityTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        nationalityTextField.setPrefWidth(0);
        nationalityTextField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nationalityTextField, Priority.ALWAYS);

        dynamicQueries1.getChildren().addAll(nameTextField, positionComboBox, nationalityTextField);


        Label ageFromLabel = new Label("Age from");
        ageFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        ageFromLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField ageFromTextField = new TextField();
        ageFromTextField.setText("0");
        ageFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label ageToLabel = new Label("Age to");
        ageToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        ageToLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField ageToTextField = new TextField();
        ageToTextField.setText("100");
        ageToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label apperancesFromLabel = new Label("Appearances from");
        apperancesFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        apperancesFromLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField appearancesFromTextField = new TextField();
        appearancesFromTextField.setText("0");
        appearancesFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label appearancesToLabel = new Label("Age To");
        appearancesToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        appearancesToLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField appearancesToTextField = new TextField();
        appearancesToTextField.setText("100");
        appearancesToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        dynamicQueries2.getChildren().addAll(ageFromLabel, ageFromTextField, ageToLabel, ageToTextField, apperancesFromLabel, appearancesFromTextField, appearancesToLabel, appearancesToTextField);


        Label goalsFromLabel = new Label("Goals from");
        goalsFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        goalsFromLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField goalsFromTextField = new TextField();
        goalsFromTextField.setText("0");
        goalsFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label goalsToLabel = new Label("Goals to");
        goalsToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        goalsToLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField goalsToTextField = new TextField();
        goalsToTextField.setText("100");
        goalsToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label assistsFromLabel = new Label("Assists from");
        assistsFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        assistsFromLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField assistsFromTextField = new TextField();
        assistsFromTextField.setText("0");
        assistsFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Label assistsToLabel = new Label("Assists To");
        assistsToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");
        assistsToLabel.setMinWidth(Region.USE_PREF_SIZE);
        TextField assistsToTextField = new TextField();
        assistsToTextField.setText("100");
        assistsToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-base: lightgreen");
        searchButton.setMinWidth(Region.USE_PREF_SIZE);
        searchButton.setCursor(Cursor.HAND);

        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-base: tomato");
        clearButton.setMinWidth(Region.USE_PREF_SIZE);
        clearButton.setCursor(Cursor.HAND);

        dynamicQueries3.getChildren().addAll(goalsFromLabel, goalsFromTextField, goalsToLabel, goalsToTextField, assistsFromLabel, assistsFromTextField, assistsToLabel, assistsToTextField, searchButton, clearButton);


        searchButton.setOnMouseClicked(e -> {
            if (!nameTextField.getText().isEmpty() || !positionComboBox.getValue().equals("All Positions") ||
                !nationalityTextField.getText().isEmpty() || !ageFromTextField.getText().equals("0") ||
                !ageToTextField.getText().equals("100") || !appearancesFromTextField.getText().equals("0") ||
                !appearancesToTextField.getText().equals("100") || !goalsFromTextField.getText().equals("0") ||
                !goalsToTextField.getText().equals("100") || !assistsFromTextField.getText().equals("0") ||
                !assistsToTextField.getText().equals("100")) {

                createButtonPlayers.setVisible(false);
                createButtonPlayers.setManaged(false);
            }

            else {
                createButtonPlayers.setVisible(true);
                createButtonPlayers.setManaged(true);
            }

            playersVBox.getChildren().clear();
            try {
                String query = "SELECT * FROM search_filters(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, nameTextField.getText().trim());

                if (positionComboBox.getValue().equals("All Positions")) {
                    statement.setString(2, "");
                }

                else {
                    statement.setString(2, positionComboBox.getValue());
                }

                statement.setInt(3, Integer.parseInt(ageFromTextField.getText()));
                statement.setInt(4, Integer.parseInt(ageToTextField.getText()));
                statement.setString(5, nationalityTextField.getText().trim());
                statement.setInt(6, Integer.parseInt(appearancesFromTextField.getText()));
                statement.setInt(7, Integer.parseInt(appearancesToTextField.getText()));
                statement.setInt(8, Integer.parseInt(goalsFromTextField.getText()));
                statement.setInt(9, Integer.parseInt(goalsToTextField.getText()));
                statement.setInt(10, Integer.parseInt(assistsFromTextField.getText()));
                statement.setInt(11, Integer.parseInt(assistsToTextField.getText()));

                ResultSet playersTableResults = statement.executeQuery();
                HelperMethods.addRowPlayers(playersTableResults, playersVBox, connection);
            }

            catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        clearButton.setOnMouseClicked(e -> {
            nameTextField.setText("");
            positionComboBox.setValue("All Positions");
            nationalityTextField.setText("");
            ageFromTextField.setText("0");
            ageToTextField.setText("100");
            appearancesFromTextField.setText("0");
            appearancesToTextField.setText("100");
            goalsFromTextField.setText("0");
            goalsToTextField.setText("100");
            assistsFromTextField.setText("0");
            assistsToTextField.setText("100");
        });
    }
}
