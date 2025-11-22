package com.slgr.Utils;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.ArrayList;

public class createTeamsRow {
    public static HBox get(String tempLogoLink, String tempTeamName, int tempTeamId, ArrayList<Integer> selectedTeamIds) {
        Image image = new Image(createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);


        TextField textField = new TextField();
        textField.setText(tempTeamName);
        textField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        textField.setPrefWidth(250);
        textField.setEditable(false);


        // Shows selected teams data, when checked
        CheckBox showSelectedCheckBox = new CheckBox();
        Tooltip tooltip1 = new Tooltip("Show Team Data");
        tooltip1.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-border-color: #767676;");
        showSelectedCheckBox.setTooltip(tooltip1);

        showSelectedCheckBox.setUserData(tempTeamId); // Every checkbox keeps the team id beneath it

        // Adds a click event to the checkbox to keep track of checked teams
        showSelectedCheckBox.setOnAction(e -> {
            int teamId = (int) showSelectedCheckBox.getUserData();
            if (showSelectedCheckBox.isSelected()) {
                selectedTeamIds.add(teamId);
            }

            else {
                selectedTeamIds.remove(Integer.valueOf(teamId));
            }
        });


        // Puts the 3 widgets in an HBox
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(imageView, textField, showSelectedCheckBox);

        return row;
    }
}
