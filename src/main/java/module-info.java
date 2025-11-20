module com.slgr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires javafx.base;
    requires javafx.graphics;


    opens com.slgr to javafx.fxml;
    opens com.slgr.Controllers to javafx.fxml;
    exports com.slgr;
}