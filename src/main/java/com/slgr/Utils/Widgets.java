package com.slgr.Utils;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Widgets {
    public static Label createDeleteButton(String tooltip_text, Connection connection) {
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
            String query = "CALL delete_row_owners(" + keys.get(0) + "," + keys.get(1) + ")"; // keys.get(0) = owner id of the row, keys.get(1) = team id of the row
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate();
            }

            catch (SQLException ex) {

            }
        });

        return deleteButton;
    }


    public static Label createCreateButton(String tooltip_text, Connection connection) {
        Label createButton = new Label();
        createButton.setText("✚");
        createButton.setStyle("-fx-font-size: 32px; -fx-text-fill: green;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        createButton.setTooltip(tooltip);
        createButton.setCursor(Cursor.HAND);

        return createButton;
    }
}
