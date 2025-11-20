package com.slgr.Controllers;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ErrorController {
    public void backButton(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/menu-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }
}
