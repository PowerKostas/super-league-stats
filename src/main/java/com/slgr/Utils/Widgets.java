package com.slgr.Utils;

import com.slgr.Controllers.InfoController;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class Widgets {
    public static Label createDeleteButton(String tooltip_text) {
        Label deleteButton = new Label();
        deleteButton.setText("🗑");
        deleteButton.setStyle("-fx-font-size: 48px; -fx-text-fill: red;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        deleteButton.setTooltip(tooltip);
        deleteButton.setCursor(Cursor.HAND);

        // Click event to delete row from the VBox and the track of checked teams
        deleteButton.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            HBox row = (HBox) source.getParent();
            VBox leftVBox = (VBox) row.getParent();
            leftVBox.getChildren().remove(row);

            for (Node widget : row.getChildren()) {
                if (widget instanceof CheckBox) {
                    CheckBox checkbox = (CheckBox) widget;
                    InfoController.selectedTeamIds.remove((Integer) checkbox.getUserData());
                }
            }
        });

        return deleteButton;
    }


    public static Label createCreateButton(String tooltip_text) {
        Label createButton = new Label();
        createButton.setText("✚");
        createButton.setStyle("-fx-font-size: 48px; -fx-text-fill: green;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        createButton.setTooltip(tooltip);
        createButton.setCursor(Cursor.HAND);

        return createButton;
    }


    public static Label createPushToDatabaseButton(String tooltip_text) {
        Label pushToDatabaseButton = new Label();
        pushToDatabaseButton.setText("\uD83D\uDCE4");
        pushToDatabaseButton.setStyle("-fx-font-size: 48px; -fx-text-fill: blue;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        pushToDatabaseButton.setTooltip(tooltip);
        pushToDatabaseButton.setCursor(Cursor.HAND);

        pushToDatabaseButton.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            HBox tempRow = (HBox) source.getParent();
            VBox leftVBox = (VBox) tempRow.getParent();

            for (Node rowNode : leftVBox.getChildren()) {
                HBox row = (HBox) rowNode;

                for (Node widget : row.getChildren()) {
                    if (widget instanceof TextField) {
                        String text = ((TextField) widget).getText();
                        System.out.println("Found Input: " + text);
                    }

                    else if (widget instanceof Label) {
                        String labelText = ((Label) widget).getText();

                        if (!labelText.equals("🗑")) {
                            System.out.println("Found Label: " + labelText);
                        }
                    }

                    else if (widget instanceof CheckBox) {
                        CheckBox cb = (CheckBox) widget;
                        boolean isChecked = cb.isSelected();
                        Object data = cb.getUserData(); // Gets your ID
                        System.out.println("Found Checkbox: Checked=" + isChecked + ", ID=" + data);
                    }

                    else if (widget instanceof ImageView) {
                        ImageView iv = (ImageView) widget;
                        String url = iv.getImage().getUrl();
                        System.out.println("Found Image: " + url);
                    }
                }
            }
        });

        return pushToDatabaseButton;
    }
}
