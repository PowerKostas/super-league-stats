package com.slgr.Utils;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class createOwnersRow {
    public static HBox get(String tempLogoLink, String tempOwnerName, String tempNationality, Date tempDOB, int tempOwnerId, int teamId, Connection connection) {
        Image image = new Image(com.slgr.Utils.createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);


        TextField textField1 = new TextField();
        textField1.setText(tempOwnerName);
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-background-color: transparent;");
        HBox.setHgrow(textField1, Priority.ALWAYS);

        textField1.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField1.setUserData(textField1.getText());
            }

            else {
                String originalValue = (String) textField1.getUserData();
                String currentValue = textField1.getText();

                if (!java.util.Objects.equals(originalValue, currentValue)) {
                    try {
                        String query = "CALL update_row_owners(?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, tempOwnerId);
                        statement.setInt(2, teamId);
                        statement.setString(3, currentValue);
                        statement.setNull(4, Types.VARCHAR);
                        statement.setNull(5, Types.DATE);
                        statement.executeUpdate();
                    }

                    catch (SQLException ex) {

                    }
                }
            }
        });


        TextField textField2 = new TextField();
        textField2.setText(tempNationality);
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-background-color: transparent;");
        HBox.setHgrow(textField2, Priority.ALWAYS);

        textField2.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField2.setUserData(textField2.getText());
            }

            else {
                String originalValue = (String) textField2.getUserData();
                String currentValue = textField2.getText();

                if (!java.util.Objects.equals(originalValue, currentValue)) {
                    try {
                        String query = "CALL update_row_owners(?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, tempOwnerId);
                        statement.setInt(2, teamId);
                        statement.setNull(3, Types.VARCHAR);
                        statement.setString(4, currentValue);
                        statement.setNull(5, Types.DATE);
                        statement.executeUpdate();
                    }

                    catch (SQLException ex) {

                    }
                }
            }
        });


        TextField textField3 = new TextField();

        if (tempDOB != null) {
            textField3.setText(tempDOB.toString());
        }

        else {
            textField3.setText("");
        }

        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-background-color: transparent;");
        HBox.setHgrow(textField3, Priority.ALWAYS);

        textField3.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField3.setUserData(textField3.getText());
            }

            else {
                String originalValue = (String) textField3.getUserData();
                String currentValue = textField3.getText();

                if (!originalValue.equals(currentValue)) {
                    try {
                        String query = "CALL update_row_owners(?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, tempOwnerId);
                        statement.setInt(2, teamId);
                        statement.setNull(3, Types.VARCHAR);
                        statement.setNull(4, Types.VARCHAR);
                        statement.setDate(5, java.sql.Date.valueOf(currentValue));
                        statement.executeUpdate();
                    }

                    catch (SQLException | IllegalArgumentException ex) {

                    }
                }
            }
        });


        Label deleteButton = Widgets.createDeleteButton("Delete Owner", connection);


        // Puts the 4 widgets in an HBox
        HBox row = new HBox();

        // Every row keeps the owner id and team id beneath it
        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(tempOwnerId);
        keys.add(teamId);
        row.setUserData(keys);

        row.getChildren().addAll(imageView, textField1, textField2, textField3, deleteButton);
        return row;
    }
}
