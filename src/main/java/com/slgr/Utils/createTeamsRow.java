package com.slgr.Utils;

import com.slgr.Controllers.InfoController;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import java.util.ArrayList;

public class createTeamsRow {
    public static HBox get(String tempLogoLink, String tempTeamName, int tempTeamId, ArrayList<Integer> selectedTeamIds, InfoController infoController) {
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

        // Make the starting state of all checkboxes logic checked
        showSelectedCheckBox.setSelected(true);
        selectedTeamIds.add(tempTeamId);

        try {
            infoController.addRowsOwnersVBox(tempTeamId);
        }

        catch (SQLException e) {

        }

        // Every checkbox keeps the team id beneath it
        showSelectedCheckBox.setUserData(tempTeamId);

        // Adds a click event to the checkbox to keep track of checked teams and modify corresponding rows in the info view
        showSelectedCheckBox.setOnAction(e -> {
            int teamId = (int) showSelectedCheckBox.getUserData();
            if (showSelectedCheckBox.isSelected()) {
                selectedTeamIds.add(teamId);

                try {
                    infoController.addRowsOwnersVBox(teamId);
                }

                catch (SQLException ex) {

                }
            }

            else {
                selectedTeamIds.remove(Integer.valueOf(teamId));
                infoController.removeRowsOwnersVBox(teamId);
            }
        });


        // Puts the 3 widgets in an HBox
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(imageView, textField, showSelectedCheckBox);

        return row;
    }
}
