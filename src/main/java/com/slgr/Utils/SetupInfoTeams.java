package com.slgr.Utils;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import com.slgr.Controllers.InfoController;

public class SetupInfoTeams {
    public static HBox getRow(String tempLogoLink, String tempTeamName, int tempTeamId) {
        Image image = new Image(SetupInfoTeams.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);

        Label label = new Label();
        label.setText(tempTeamName);
        label.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px;");

        CheckBox checkBox = new CheckBox();
        checkBox.setUserData(tempTeamId);

        checkBox.setOnAction(e -> {
            int teamId = (int) checkBox.getUserData();
            if (checkBox.isSelected()) {
                InfoController.selectedTeamIds.add(teamId);
            }

            else {
                InfoController.selectedTeamIds.remove(Integer.valueOf(teamId));
            }
        });


        // Puts the 3 widgets in an HBox
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(imageView, label, checkBox);

        return row;
    }
}
