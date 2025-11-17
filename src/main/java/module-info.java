module com.slgr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;


    opens com.slgr to javafx.fxml;
    opens com.slgr.controllers to javafx.fxml;
    exports com.slgr;
}