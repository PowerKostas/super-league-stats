package com.slgr.Utils;

import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
                    HBox row = createOwnersCoachesRow.get(logoLink, null, null, null, dataId, teamId, connection, table);
                    VBox parentVBox = (VBox) buttonRow.getParent();
                    VBox ownersCoachesVBox = (VBox) parentVBox.getChildren().get(1); // 1 = VBox with fx id: ownersVBox or VBox with fx id: coachesVBox
                    HelperMethods.addRowSorted(ownersCoachesVBox, row, 1);
                }

                // Fixes layout
                buttonRow.getChildren().remove(teamComboBox);
                createButton.setVisible(true);
                createButton.requestFocus();
            });
        });

        return createButton;
    }
}
