package com.slgr.Utils;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.util.ArrayList;

public class CreateRowStandings {
    public static HBox get(String logoLink, String wins, String draws, String losses, String points, int teamId) {
        Image image = new Image(CreateRowTeams.class.getResource("/com/slgr/Images/Logos/" + logoLink).toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        imageView.setPreserveRatio(true);


        TextField textField1 = HelperMethods.makeTextField(wins);
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField2 = HelperMethods.makeTextField(draws);
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField3 = HelperMethods.makeTextField(losses);
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");
        TextField textField4 = HelperMethods.makeTextField(points);
        textField4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-background-color: transparent;");


        HBox row = new HBox(100);

        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(teamId);
        keys.add(Integer.parseInt(points));
        row.setUserData(keys);

        row.getChildren().addAll(imageView, textField1, textField2, textField3, textField4);
        return row;
    }
}
