package com.slgr.Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.slgr.Utils.*;
import java.util.ArrayList;
import java.util.Date;

public class InfoController {
    @FXML
    private Text backButton;

    @FXML
    private VBox leftVBox;

    @FXML
    private HBox ownersColumns;

    @FXML
    private VBox ownersVBox;

    @FXML
    private HBox ownersButton;

    @FXML
    private HBox coachesColumns;

    @FXML
    private VBox coachesVBox;

    @FXML
    private HBox coachesButton;

    private ArrayList<Integer> selectedTeamIds = new ArrayList<>();

    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public void initialize() {
        // Removes ugly focusing behavior when starting up the app
        Platform.runLater(() -> {
           leftVBox.requestFocus();
        });

        backButton.setCursor(Cursor.HAND);
    }


    // Makes the whole teams VBox
    public void makeTeamsVBox() throws SQLException {
        // Executes queries to get data from the database
        String query = "SELECT * FROM get_team_logo_name_id()";
        Statement statement = connection.createStatement();
        ResultSet teamLogoNameIdResults = statement.executeQuery(query);


        // Extracts data row by row and sets up logos, team names, checkboxes and CRUD buttons on the left side of the page
        while (teamLogoNameIdResults.next()) {
            String tempLogoLink = teamLogoNameIdResults.getString(1);
            String tempTeamName = teamLogoNameIdResults.getString(2);
            int tempTeamId = teamLogoNameIdResults.getInt(3);

            // Puts the HBox row in the scrollable VBox
            HBox row = createTeamsRow.get(tempLogoLink, tempTeamName, tempTeamId, selectedTeamIds, this);
            leftVBox.getChildren().add(row);
        }


        // Continues the code now that a connection has been established
        addCreateButton();
        callAddColumnsOwnersCoaches();
    }


    // Adds to the owners, coaches VBox the corresponding rows when the teams checkbox is checked
    public void addRows(int teamId) throws SQLException {
        // Repeats the process for the owners, coaches table
        String query = "SELECT * FROM get_owners_logo(" + teamId + ")";
        Statement statement = connection.createStatement();
        ResultSet ownersTableResults = statement.executeQuery(query);

        while (ownersTableResults.next()) {
            String tempLogoLink = ownersTableResults.getString(6);
            String tempOwnerName = ownersTableResults.getString(3);
            String tempNationality = ownersTableResults.getString(4);
            Date tempDOB = ownersTableResults.getDate(5);
            int tempOwnerId = ownersTableResults.getInt(1);

            HBox row = createOwnersCoachesRow.get(tempLogoLink, tempOwnerName, tempNationality, tempDOB, tempOwnerId, teamId, connection, "owners");
            HelperMethods.addRowSorted(ownersVBox, row);
        }


        ownersTableResults = statement.executeQuery("SELECT * FROM get_coaches_logo(" + teamId + ")");
        while (ownersTableResults.next()) {
            String tempLogoLink = ownersTableResults.getString(6);
            String tempCoachName = ownersTableResults.getString(3);
            String tempNationality = ownersTableResults.getString(4);
            Date tempDOB = ownersTableResults.getDate(5);
            int tempCoachId = ownersTableResults.getInt(1);

            HBox row = createOwnersCoachesRow.get(tempLogoLink, tempCoachName, tempNationality, tempDOB, tempCoachId, teamId, connection, "coaches");
            HelperMethods.addRowSorted(coachesVBox, row);
        }
    }

    //  Removes from the owners, coaches VBox the corresponding rows when the teams checkbox is unchecked
    public void removeRows(int teamId) {
        ownersVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(1); // keys.get(1) = team id of the row
        });

        coachesVBox.getChildren().removeIf(node -> {
            ArrayList<Integer> keys = (ArrayList<Integer>) node.getUserData();
            return teamId == keys.get(1); // keys.get(1) = team id of the row
        });
    }


    // Hacky way to add headers to the owners, coaches VBox
    public void addColumnsOwnersCoaches(HBox targetColumns) {
        Image image = new Image(com.slgr.Utils.createTeamsRow.class.getResource("/com/slgr/Images/Logos/" + "1.png").toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);

        TextField textField1 = HelperMethods.makeHeaderTextField("Name");
        TextField textField2 = HelperMethods.makeHeaderTextField("Nationality");
        TextField textField3 = HelperMethods.makeHeaderTextField("DOB");

        Label deleteButton = Widgets.createDeleteButton("Delete Owner", connection, null);

        imageView.setVisible(false);
        deleteButton.setVisible(false);

        targetColumns.getChildren().addAll(imageView, textField1, textField2, textField3, deleteButton);
    }

    public void callAddColumnsOwnersCoaches() {
        addColumnsOwnersCoaches(ownersColumns);
        addColumnsOwnersCoaches(coachesColumns);
    }


    public void addCreateButton() {
        Label createButtonOwners = Widgets.createCreateButton("Add Owner", "owners", connection, selectedTeamIds);
        ownersButton.getChildren().add(createButtonOwners);

        Label createButtonCoaches = Widgets.createCreateButton("Add Coach", "coaches", connection, selectedTeamIds);
        coachesButton.getChildren().add(createButtonCoaches);
    }


    public void backButton(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/menu-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }
}
