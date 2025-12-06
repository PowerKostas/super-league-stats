package com.slgr.Controllers;

import com.slgr.Utils.CreateRowLogging;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LogController {
    @FXML
    private TableView<CreateRowLogging> logTable;

    // TableView columns
    @FXML
    private TableColumn<CreateRowLogging, String> timestampColumn;

    @FXML
    private TableColumn<CreateRowLogging, String> descriptionColumn;

    @FXML
    private TableColumn<CreateRowLogging, String> actionTypeColumn;

    @FXML
    private TableColumn<CreateRowLogging, String> tableNameColumn;

    @FXML
    private TableColumn<CreateRowLogging, String> affectedDataIdColumn;

    @FXML
    private TableColumn<CreateRowLogging, String> affectedTeamIdColumn;

    private Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    // Have to do it
    public void initialize() {
        timestampColumn.setCellValueFactory(cellData -> cellData.getValue().timestampColumnProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionColumnProperty());
        actionTypeColumn.setCellValueFactory(cellData -> cellData.getValue().actionTypeColumnProperty());
        tableNameColumn.setCellValueFactory(cellData -> cellData.getValue().tableNameColumnProperty());
        affectedDataIdColumn.setCellValueFactory(cellData -> cellData.getValue().affectedDataIdColumnProperty());
        affectedTeamIdColumn.setCellValueFactory(cellData -> cellData.getValue().affectedTeamIdColumnProperty());
    }


    public void addRowsLogging() {
        try {
            // Executes queries to get data from the database
            String query = "SELECT * FROM get_logging()";
            Statement statement = connection.createStatement();
            ResultSet loggingTableResults = statement.executeQuery(query);

            // Extracts data row by row to set up TableView
            while (loggingTableResults.next()) {
                String timestamp = loggingTableResults.getString(2);
                timestamp = timestamp.substring(0, timestamp.indexOf(".")); // Cuts milliseconds

                String description = loggingTableResults.getString(3);
                String actionType = loggingTableResults.getString(4);
                String tableName = loggingTableResults.getString(5);
                String affectedDataId = loggingTableResults.getString(6);
                String affectedTeamId = loggingTableResults.getString(7);

                logTable.getItems().add(new CreateRowLogging(timestamp, description, actionType, tableName, affectedDataId, affectedTeamId));
            }
        }

        catch (SQLException e) {

        }
    }


    public void backButton(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/slgr/Views/menu-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }
}
