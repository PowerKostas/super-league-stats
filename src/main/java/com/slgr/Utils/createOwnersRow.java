package com.slgr.Utils;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.Date;

public class createOwnersRow {
    public static HBox get(String tempLogoLink, String tempOwnerName, String tempNationality, Date tempDOB, int tempOwnerId, int teamId) {
        Image image = new Image(com.slgr.Utils.createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageView.setPreserveRatio(true);


        TextField textField1 = new TextField();
        textField1.setText(tempOwnerName);
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");


        TextField textField2 = new TextField();
        textField2.setText(tempNationality);
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");


        TextField textField3 = new TextField();
        textField3.setText(tempDOB.toString());
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");


        // Puts the 4 widgets in an HBox
        HBox row = new HBox();

        // Every row keeps the owner id and team id beneath it
        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(tempOwnerId);
        keys.add(teamId);
        row.setUserData(keys);

        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(imageView, textField1, textField2, textField3);

        return row;
    }
}
