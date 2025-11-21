package com.slgr.Utils;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class Widgets {
    public static Label createDeleteButton(String tooltip_text) {
        Label deleteButton = new Label();
        deleteButton.setText("🗑");
        deleteButton.setStyle("-fx-font-size: 48px; -fx-text-fill: red;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        deleteButton.setTooltip(tooltip);

        return deleteButton;
    }


    public static Label createCreateButton(String tooltip_text) {
        Label createButton = new Label();
        createButton.setText("✚");
        createButton.setStyle("-fx-font-size: 48px; -fx-text-fill: green;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        createButton.setTooltip(tooltip);

        return createButton;
    }


    public static Label createPushToDatabaseButton(String tooltip_text) {
        Label pushToDatabaseButton = new Label();
        pushToDatabaseButton.setText("\uD83D\uDCE4");
        pushToDatabaseButton.setStyle("-fx-font-size: 48px; -fx-text-fill: blue;");
        Tooltip tooltip = new Tooltip(tooltip_text);
        tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        pushToDatabaseButton.setTooltip(tooltip);

        return pushToDatabaseButton;
    }
}
