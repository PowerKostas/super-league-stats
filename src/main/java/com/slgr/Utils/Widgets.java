package com.slgr.Utils;

import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    HelperMethods.addRowSorted(ownersCoachesPlayersVBox, row, 1, 1);
                }


                // Fixes layout
                buttonRow.getChildren().remove(teamComboBox);
                createButton.setVisible(true);
                createButton.requestFocus();
            });
        });

        return createButton;
    }


    public static void addDynamicQueries(HBox dynamicQueries1, HBox dynamicQueries2, HBox dynamicQueries3, VBox playersVBox, Connection connection, Label createButtonPlayers, ArrayList<Integer> selectedOrders) {
        // First row
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter name...");
        nameTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        nameTextField.setPrefWidth(0);
        nameTextField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameTextField, Priority.ALWAYS);

        MenuButton positionMenuButton = new MenuButton();
        positionMenuButton.setText("Positions selected...");
        positionMenuButton.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px; -fx-base: white");
        positionMenuButton.setPrefWidth(0);
        positionMenuButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(positionMenuButton, Priority.ALWAYS);
        positionMenuButton.setCursor(Cursor.HAND);

        String[] positions = {
                "Goalkeeper", "Centre-Back", "Left-Back", "Right-Back", "Defensive Midfield", "Central Midfield",
                "Right Midfield", "Left Midfield",  "Attacking Midfield", "Left Winger", "Right Winger",
                "Second Striker", "Centre-Forward"
        };

        for (String pos : positions) {
            CheckBox checkBox = new CheckBox(pos);
            checkBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
            checkBox.setSelected(true);

            CustomMenuItem menuItem = new CustomMenuItem(checkBox);
            menuItem.setHideOnClick(false);

            positionMenuButton.getItems().add(menuItem);
        }

        TextField nationalityTextField = new TextField();
        nationalityTextField.setPromptText("Enter nationality...");
        nationalityTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        nationalityTextField.setPrefWidth(0);
        nationalityTextField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nationalityTextField, Priority.ALWAYS);

        dynamicQueries1.getChildren().addAll(nameTextField, positionMenuButton, nationalityTextField);


        // Second row
        Label ageFromLabel = new Label("Age from");
        ageFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        ageFromLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField ageFromTextField = new TextField();
        ageFromTextField.setText("0");
        ageFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        Label ageToLabel = new Label("Age to");
        ageToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        ageToLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField ageToTextField = new TextField();
        ageToTextField.setText("100");
        ageToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        Label appsFromLabel = new Label("Apps from");
        appsFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        appsFromLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField appsFromTextField = new TextField();
        appsFromTextField.setText("0");
        appsFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        Label appsToLabel = new Label("Apps to");
        appsToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        appsToLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField appsToTextField = new TextField();
        appsToTextField.setText("100");
        appsToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        Label goalsFromLabel = new Label("Goals from");
        goalsFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        goalsFromLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField goalsFromTextField = new TextField();
        goalsFromTextField.setText("0");
        goalsFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        Label goalsToLabel = new Label("Goals to");
        goalsToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        goalsToLabel.setMinWidth(Region.USE_PREF_SIZE);

        TextField goalsToTextField = new TextField();
        goalsToTextField.setText("100");
        goalsToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        dynamicQueries2.getChildren().addAll(ageFromLabel, ageFromTextField, ageToLabel, ageToTextField, appsFromLabel, appsFromTextField, appsToLabel, appsToTextField, goalsFromLabel, goalsFromTextField, goalsToLabel, goalsToTextField);


        // Third row
        Label assistsFromLabel = new Label("Assists from");
        assistsFromLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        TextField assistsFromTextField = new TextField();
        assistsFromTextField.setText("0");
        assistsFromTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        assistsFromTextField.setPrefWidth(50);

        Label assistsToLabel = new Label("Assists To");
        assistsToLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        TextField assistsToTextField = new TextField();
        assistsToTextField.setText("100");
        assistsToTextField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");
        assistsToTextField.setPrefWidth(50);

        Label orderLabel = new Label("Order by");
        orderLabel.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px;");

        ComboBox<String> orderChoiceComboBox = new ComboBox<>();
        orderChoiceComboBox.setValue("Team");
        orderChoiceComboBox.getItems().addAll("Team", "Age", "Appearances", "Goals", "Assists");
        orderChoiceComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px; -fx-base: white");
        orderChoiceComboBox.setPrefWidth(0);
        orderChoiceComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(orderChoiceComboBox, Priority.ALWAYS);
        orderChoiceComboBox.setCursor(Cursor.HAND);

        ComboBox<String> orderTypeComboBox = new ComboBox<>();
        orderTypeComboBox.setValue("Ascending");
        orderTypeComboBox.getItems().addAll("Ascending", "Descending");
        orderTypeComboBox.setStyle("-fx-font-family: Rockwell; -fx-font-size: 16px; -fx-base: white");
        orderTypeComboBox.setPrefWidth(0);
        orderTypeComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(orderTypeComboBox, Priority.ALWAYS);
        orderTypeComboBox.setCursor(Cursor.HAND);

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-base: lightgreen");
        searchButton.setPrefWidth(0);
        searchButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchButton, Priority.ALWAYS);
        searchButton.setCursor(Cursor.HAND);

        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-base: tomato");
        clearButton.setPrefWidth(0);
        clearButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(clearButton, Priority.ALWAYS);
        clearButton.setCursor(Cursor.HAND);

        dynamicQueries3.getChildren().addAll(assistsFromLabel, assistsFromTextField, assistsToLabel, assistsToTextField, orderLabel, orderChoiceComboBox, orderTypeComboBox, searchButton, clearButton);


        searchButton.setOnMouseClicked(e -> {
            // Gets the selected order choice (+ 1, to match the player row user data)
            selectedOrders.set(0, orderChoiceComboBox.getSelectionModel().getSelectedIndex() + 1);

            // Gets the selected order type and turns in to 1 for ascending order and -1 for descending order
            if (orderTypeComboBox.getSelectionModel().getSelectedIndex() == 0) {
                selectedOrders.set(1, 1);
            }

            else {
                selectedOrders.set(1, -1);
            }

            // Counts the number of checked position checkboxes (default is 13) and puts the text of the checked ones
            // in an array
            int selectedCount = 0;
            List<String> tempSelectedPositions1 = new ArrayList<>();
            for (MenuItem tempMenuItem : positionMenuButton.getItems()) {
                CustomMenuItem menuItem = (CustomMenuItem) tempMenuItem;
                CheckBox checkBox = (CheckBox) menuItem.getContent();

                if (checkBox.isSelected()) {
                    selectedCount += 1;
                    tempSelectedPositions1.add(checkBox.getText());
                }
            }

            // Checks if any widget's value is different from the default one, if yes, it hides the create button, so
            // the user can't create new players if filters are applied
            if (!nameTextField.getText().isEmpty() || !(selectedCount == 13) ||
                    !nationalityTextField.getText().isEmpty() || !ageFromTextField.getText().equals("0") ||
                    !ageToTextField.getText().equals("100") || !appsFromTextField.getText().equals("0") ||
                    !appsToTextField.getText().equals("100") || !goalsFromTextField.getText().equals("0") ||
                    !goalsToTextField.getText().equals("100") || !assistsFromTextField.getText().equals("0") ||
                    !assistsToTextField.getText().equals("100") || !orderChoiceComboBox.getValue().equals("Team") ||
                    !orderTypeComboBox.getValue().equals("Ascending")) {

                createButtonPlayers.setVisible(false);
                createButtonPlayers.setManaged(false);
            }

            else {
                createButtonPlayers.setVisible(true);
                createButtonPlayers.setManaged(true);
            }

            // Clears the VBox, puts the widgets' values in a function, renews the VBox with the returned filtered players
            playersVBox.getChildren().clear();
            try {
                String query = "SELECT * FROM search_filters(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setString(1, nameTextField.getText().trim());

                String[] tempSelectedPositions2 = tempSelectedPositions1.toArray(new String[0]);
                java.sql.Array selectedPositions = connection.createArrayOf("varchar", tempSelectedPositions2);
                statement.setArray(2, selectedPositions);

                statement.setInt(3, Integer.parseInt(ageFromTextField.getText()));
                statement.setInt(4, Integer.parseInt(ageToTextField.getText()));
                statement.setString(5, nationalityTextField.getText().trim());
                statement.setInt(6, Integer.parseInt(appsFromTextField.getText()));
                statement.setInt(7, Integer.parseInt(appsToTextField.getText()));
                statement.setInt(8, Integer.parseInt(goalsFromTextField.getText()));
                statement.setInt(9, Integer.parseInt(goalsToTextField.getText()));
                statement.setInt(10, Integer.parseInt(assistsFromTextField.getText()));
                statement.setInt(11, Integer.parseInt(assistsToTextField.getText()));
                statement.setInt(12, orderChoiceComboBox.getSelectionModel().getSelectedIndex() + 1);

                if (orderTypeComboBox.getSelectionModel().getSelectedIndex() == 0) {
                    statement.setInt(13, 0);
                }

                else {
                    statement.setInt(13, 1);
                }

                ResultSet playersTableResults = statement.executeQuery();
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
                    playersVBox.getChildren().add(row);
                }
            }

            catch (SQLException ex) {

            }
        });


        // Puts the default value for every widget
        clearButton.setOnMouseClicked(e -> {
            nameTextField.setText("");

            for (MenuItem tempMenuItem : positionMenuButton.getItems()) {
                CustomMenuItem menuItem = (CustomMenuItem) tempMenuItem;
                CheckBox checkBox = (CheckBox) menuItem.getContent();
                checkBox.setSelected(true);
            }

            nationalityTextField.setText("");
            ageFromTextField.setText("0");
            ageToTextField.setText("100");
            appsFromTextField.setText("0");
            appsToTextField.setText("100");
            goalsFromTextField.setText("0");
            goalsToTextField.setText("100");
            assistsFromTextField.setText("0");
            assistsToTextField.setText("100");
            orderChoiceComboBox.setValue("Team");
            orderTypeComboBox.setValue("Ascending");
        });
    }
}
