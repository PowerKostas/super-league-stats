package com.slgr.Utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateRowLogging {
    private final StringProperty timestampColumn;
    private final StringProperty descriptionColumn;
    private final StringProperty actionTypeColumn;
    private final StringProperty tableNameColumn;
    private final StringProperty affectedDataIdColumn;
    private final StringProperty affectedTeamIdColumn;

    // Constructor
    public CreateRowLogging(String timestampColumn, String descriptionColumn, String actionTypeColumn, String tableNameColumn, String affectedDataIdColumn, String affectedTeamIdColumn) {
        this.timestampColumn = new SimpleStringProperty(timestampColumn);
        this.descriptionColumn = new SimpleStringProperty(descriptionColumn);
        this.actionTypeColumn = new SimpleStringProperty(actionTypeColumn);
        this.tableNameColumn = new SimpleStringProperty(tableNameColumn);
        this.affectedDataIdColumn = new SimpleStringProperty(affectedDataIdColumn);
        this.affectedTeamIdColumn = new SimpleStringProperty(affectedTeamIdColumn);
    }

    // Have to do it
    public StringProperty timestampColumnProperty() { return timestampColumn; }
    public StringProperty descriptionColumnProperty() { return descriptionColumn; }
    public StringProperty actionTypeColumnProperty() { return actionTypeColumn; }
    public StringProperty tableNameColumnProperty() { return tableNameColumn; }
    public StringProperty affectedDataIdColumnProperty() { return affectedDataIdColumn; }
    public StringProperty affectedTeamIdColumnProperty() { return affectedTeamIdColumn; }
}
