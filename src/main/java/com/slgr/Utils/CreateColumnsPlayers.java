package com.slgr.Utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.sql.Connection;

public class CreateColumnsPlayers {
    public static void get(HBox playersColumns, Connection connection) {
        Image image = new Image(CreateRowTeams.class.getResource("/com/slgr/Images/Logos/" + "1.png").toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);

        TextField textField1 = HelperMethods.makeTextField("Name");
        textField1.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField1.setEditable(false);
        textField1.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.275));

        TextField textField2 = HelperMethods.makeTextField("Position");
        textField2.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField2.setEditable(false);
        textField2.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.2));

        TextField textField3 = HelperMethods.makeTextField("Age");
        textField3.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField3.setEditable(false);
        textField3.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.075));

        TextField textField4 = HelperMethods.makeTextField("Nationality");
        textField4.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField4.setEditable(false);
        textField4.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.2));

        TextField textField5 = HelperMethods.makeTextField("Apps");
        textField5.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField5.setEditable(false);
        textField5.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.1));

        TextField textField6 = HelperMethods.makeTextField("G");
        textField6.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField6.setEditable(false);
        textField6.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.05));

        TextField textField7 = HelperMethods.makeTextField("A");
        textField7.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: transparent;");
        textField7.setEditable(false);
        textField7.prefWidthProperty().bind(playersColumns.widthProperty().multiply(0.05));

        Label deleteButton = Widgets.createDeleteButton("", connection, null);

        imageView.setVisible(false);
        deleteButton.setVisible(false);

        playersColumns.getChildren().addAll(imageView, textField1, textField2, textField3, textField4, textField5, textField6, textField7, deleteButton);
    }
}
