package com.slgr.Utils;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.sql.Connection;

public class CreateColumnsStandings {
    public static void get(HBox standingsColumns, Connection connection) {
        Image image = new Image(CreateRowTeams.class.getResource("/com/slgr/Images/Logos/" + "1.png").toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        imageView.setPreserveRatio(true);

        TextField textField1 = HelperMethods.makeTextField("Wins");
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField1.setEditable(false);

        TextField textField2 = HelperMethods.makeTextField("Draws");
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField2.setEditable(false);

        TextField textField3 = HelperMethods.makeTextField("Losses");
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField3.setEditable(false);

        TextField textField4 = HelperMethods.makeTextField("Points");
        textField4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField4.setEditable(false);

        imageView.setVisible(false);
        standingsColumns.setSpacing(100);
        standingsColumns.getChildren().addAll(imageView, textField1, textField2, textField3, textField4);
    }
}
