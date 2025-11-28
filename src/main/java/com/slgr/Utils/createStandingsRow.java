package com.slgr.Utils;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.ArrayList;

public class createStandingsRow {
    public static HBox get(String tempLogoLink, String tempWins, String tempDraws, String tempLosses, String tempPoints, int teamId) {
        Image image = new Image(com.slgr.Utils.createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + tempLogoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        imageView.setPreserveRatio(true);


        TextField textField1 = HelperMethods.makeTextField(tempWins);
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField2 = HelperMethods.makeTextField(tempDraws);
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField3 = HelperMethods.makeTextField(tempLosses);
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField4 = HelperMethods.makeTextField(tempPoints);
        textField4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");


        HBox row = new HBox(100);

        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(teamId);
        keys.add(Integer.parseInt(tempPoints));
        row.setUserData(keys);

        row.getChildren().addAll(imageView, textField1, textField2, textField3, textField4);
        return row;
    }
}
