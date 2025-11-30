package com.slgr.Utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.sql.Connection;

public class CreateColumnsOwnersCoaches {
    public static void get(HBox targetColumns, Connection connection) {
        Image image = new Image(CreateRowTeams.class.getResource("/com/slgr/Images/Logos/" + "1.png").toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);

        TextField textField1 = HelperMethods.makeTextField("Name");
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField1.setEditable(false);

        TextField textField2 = HelperMethods.makeTextField("Nationality");
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField2.setEditable(false);

        TextField textField3 = HelperMethods.makeTextField("DOB");
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField3.setEditable(false);

        Label deleteButton = Widgets.createDeleteButton("", connection, null);

        imageView.setVisible(false);
        deleteButton.setVisible(false);

        targetColumns.getChildren().addAll(imageView, textField1, textField2, textField3, deleteButton);
    }
}
