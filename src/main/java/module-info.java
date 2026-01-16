module com.example.bd.diplommm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires hsqldb;
    requires ucanaccess;
    requires org.apache.poi.poi;
    requires kernel;
    requires layout;
    requires io;
    requires org.apache.poi.ooxml;

    opens com.example.bd.diplommm to javafx.fxml;
    exports com.example.bd.diplommm;
    exports com.example.bd.diplommm.service;
    opens com.example.bd.diplommm.service to javafx.fxml;
}