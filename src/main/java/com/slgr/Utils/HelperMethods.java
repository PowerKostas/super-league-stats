package com.slgr.Utils;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class HelperMethods {
    public static void addRowSorted(VBox tempVBox, HBox row, int modifier) {
        // Does ordered insertion so when adding a row in the VBox, the table still appears in team id or points order
        ArrayList<Integer> keys = (ArrayList<Integer>) row.getUserData();
        int teamIdPoints = keys.get(1);
        boolean inserted = false;

        for (int i = 0; i < tempVBox.getChildren().size(); i += 1) {
            Node tempRow = tempVBox.getChildren().get(i);
            keys = (ArrayList<Integer>) tempRow.getUserData();
            int key = keys.get(1);

            if (teamIdPoints * modifier < key * modifier) { // Modifier: 1 = Ascending Order | -1 = Descending Order
                tempVBox.getChildren().add(i, row);
                inserted = true;
                break;
            }
        }

        if (!inserted) {
            tempVBox.getChildren().add(row);
        }
    }


    public static TextField makeTextField(String text) {
        TextField textField = new TextField(text);
        textField.setStyle("-fx-font-family: Rockwell; -fx-font-size: 20px; -fx-background-color: transparent;");
        HBox.setHgrow(textField, Priority.ALWAYS);

        return textField;
    }
}
