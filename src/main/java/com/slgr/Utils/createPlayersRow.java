package com.slgr.Utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

public class createPlayersRow {
    public static HBox get(String tempLogoLink, String playerName, String playerPosition, Integer age, String nationality, Integer appearances, Integer goals, Integer assists, int playerId, int teamId, Connection connection) {
        HBox row = new HBox();


        Image image = new Image(com.slgr.Utils.createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);


        TextField textField1 = HelperMethods.makeTextField(playerName);
        textField1.prefWidthProperty().bind(row.widthProperty().multiply(0.275)); // Percentage of the HBox row every text field takes

        textField1.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField1.setUserData(textField1.getText());
            }

            else {
                String originalValue = (String) textField1.getUserData();
                String currentValue = textField1.getText();

                if (!java.util.Objects.equals(originalValue, currentValue)) {
                    try {
                        String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, playerId);
                        statement.setInt(2, teamId);
                        statement.setString(3, currentValue);
                        statement.setNull(4, Types.VARCHAR);
                        statement.setNull(5, Types.INTEGER);
                        statement.setNull(6, Types.VARCHAR);
                        statement.setNull(7, Types.INTEGER);
                        statement.setNull(8, Types.INTEGER);
                        statement.setNull(9, Types.INTEGER);
                        statement.executeUpdate();
                    }

                    catch (SQLException ex) {

                    }
                }
            }
        });


        TextField textField2 = HelperMethods.makeTextField(playerPosition);
        textField2.prefWidthProperty().bind(row.widthProperty().multiply(0.2));

        textField2.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField2.setUserData(textField2.getText());
            }

            else {
                String originalValue = (String) textField2.getUserData();
                String currentValue = textField2.getText();

                if (!java.util.Objects.equals(originalValue, currentValue)) {
                    try {
                        String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, playerId);
                        statement.setInt(2, teamId);
                        statement.setNull(3, Types.VARCHAR);
                        statement.setString(4, currentValue);
                        statement.setNull(5, Types.INTEGER);
                        statement.setNull(6, Types.VARCHAR);
                        statement.setNull(7, Types.INTEGER);
                        statement.setNull(8, Types.INTEGER);
                        statement.setNull(9, Types.INTEGER);
                        statement.executeUpdate();
                    }

                    catch (SQLException ex) {

                    }
                }
            }
        });


        TextField textField3 = HelperMethods.makeTextField(Objects.toString(age, "")); // If age = null, turns it to ""
        textField3.prefWidthProperty().bind(row.widthProperty().multiply(0.075));

        textField3.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField3.setUserData(textField3.getText());
            }

            else {
                String originalValue = (String) textField3.getUserData();
                String currentValue = textField3.getText();

                // Check if the user actually inputted an integer
                boolean flag = true;
                try {
                    Integer.parseInt(currentValue);
                }

                catch (NumberFormatException e) {
                    flag = false;
                }

                if (flag) {
                    if (!java.util.Objects.equals(originalValue, currentValue)) {
                        try {
                            String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, playerId);
                            statement.setInt(2, teamId);
                            statement.setNull(3, Types.VARCHAR);
                            statement.setNull(4, Types.VARCHAR);
                            statement.setInt(5, Integer.parseInt(currentValue));
                            statement.setNull(6, Types.VARCHAR);
                            statement.setNull(7, Types.INTEGER);
                            statement.setNull(8, Types.INTEGER);
                            statement.setNull(9, Types.INTEGER);
                            statement.executeUpdate();
                        }

                        catch (SQLException ex) {

                        }
                    }
                }

                // If invalid input, keep the original value
                else {
                    textField3.setText(originalValue);
                }
            }
        });


        TextField textField4 = HelperMethods.makeTextField(nationality);
        textField4.prefWidthProperty().bind(row.widthProperty().multiply(0.2));

        textField4.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField4.setUserData(textField4.getText());
            }

            else {
                String originalValue = (String) textField4.getUserData();
                String currentValue = textField4.getText();

                if (!java.util.Objects.equals(originalValue, currentValue)) {
                    try {
                        String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, playerId);
                        statement.setInt(2, teamId);
                        statement.setNull(3, Types.VARCHAR);
                        statement.setNull(4, Types.VARCHAR);
                        statement.setNull(5, Types.INTEGER);
                        statement.setString(6, currentValue);
                        statement.setNull(7, Types.INTEGER);
                        statement.setNull(8, Types.INTEGER);
                        statement.setNull(9, Types.INTEGER);
                        statement.executeUpdate();
                    }

                    catch (SQLException ex) {

                    }
                }
            }
        });


        TextField textField5 = HelperMethods.makeTextField(Objects.toString(appearances, ""));
        textField5.prefWidthProperty().bind(row.widthProperty().multiply(0.1));

        textField5.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField5.setUserData(textField5.getText());
            }

            else {
                String originalValue = (String) textField5.getUserData();
                String currentValue = textField5.getText();

                boolean flag = true;
                try {
                    Integer.parseInt(currentValue);
                }

                catch (NumberFormatException e) {
                    flag = false;
                }

                if (flag) {
                    if (!java.util.Objects.equals(originalValue, currentValue)) {
                        try {
                            String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, playerId);
                            statement.setInt(2, teamId);
                            statement.setNull(3, Types.VARCHAR);
                            statement.setNull(4, Types.VARCHAR);
                            statement.setNull(5, Types.INTEGER);
                            statement.setNull(6, Types.VARCHAR);
                            statement.setInt(7, Integer.parseInt(currentValue));
                            statement.setNull(8, Types.INTEGER);
                            statement.setNull(9, Types.INTEGER);
                            statement.executeUpdate();
                        } catch (SQLException ex) {

                        }
                    }
                }

                else {
                    textField5.setText(originalValue);
                }
            }
        });


        TextField textField6 = HelperMethods.makeTextField(Objects.toString(goals, ""));
        textField6.prefWidthProperty().bind(row.widthProperty().multiply(0.05));

        textField6.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField6.setUserData(textField6.getText());
            }

            else {
                String originalValue = (String) textField6.getUserData();
                String currentValue = textField6.getText();

                boolean flag = true;
                try {
                    Integer.parseInt(currentValue);
                }

                catch (NumberFormatException e) {
                    flag = false;
                }

                if (flag) {
                    if (!java.util.Objects.equals(originalValue, currentValue)) {
                        try {
                            String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, playerId);
                            statement.setInt(2, teamId);
                            statement.setNull(3, Types.VARCHAR);
                            statement.setNull(4, Types.VARCHAR);
                            statement.setNull(5, Types.INTEGER);
                            statement.setNull(6, Types.VARCHAR);
                            statement.setNull(7, Types.INTEGER);
                            statement.setInt(8, Integer.parseInt(currentValue));
                            statement.setNull(9, Types.INTEGER);
                            statement.executeUpdate();
                        } catch (SQLException ex) {

                        }
                    }
                }

                else {
                    textField6.setText(originalValue);
                }
            }
        });


        TextField textField7 = HelperMethods.makeTextField(Objects.toString(assists, ""));
        textField7.prefWidthProperty().bind(row.widthProperty().multiply(0.05));

        textField7.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField7.setUserData(textField7.getText());
            }

            else {
                String originalValue = (String) textField7.getUserData();
                String currentValue = textField7.getText();

                boolean flag = true;
                try {
                    Integer.parseInt(currentValue);
                }

                catch (NumberFormatException e) {
                    flag = false;
                }

                if (flag) {
                    if (!java.util.Objects.equals(originalValue, currentValue)) {
                        try {
                            String query = "CALL update_row_players(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, playerId);
                            statement.setInt(2, teamId);
                            statement.setNull(3, Types.VARCHAR);
                            statement.setNull(4, Types.VARCHAR);
                            statement.setNull(5, Types.INTEGER);
                            statement.setNull(6, Types.VARCHAR);
                            statement.setNull(7, Types.INTEGER);
                            statement.setNull(8, Types.INTEGER);
                            statement.setInt(9, Integer.parseInt(currentValue));
                            statement.executeUpdate();
                        } catch (SQLException ex) {

                        }
                    }
                }

                else {
                    textField7.setText(originalValue);
                }
            }
        });


        Label deleteButton = Widgets.createDeleteButton("Delete Player", connection, "players");


        // Every row keeps the player id and team id beneath it
        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(playerId);
        keys.add(teamId);
        row.setUserData(keys);

        // Puts the widgets in the HBox
        row.getChildren().addAll(imageView, textField1, textField2, textField3, textField4, textField5, textField6, textField7, deleteButton);
        return row;
    }
}
