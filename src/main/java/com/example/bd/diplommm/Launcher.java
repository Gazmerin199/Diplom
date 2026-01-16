package com.example.bd.diplommm;
import com.example.bd.diplommm.service.DatabaseService;
import javafx.application.Application;

import java.sql.SQLException;

public class Launcher {
    static void main(String[] args) throws SQLException {
        DatabaseService dbService = DatabaseService.getInstance();
        dbService.createTable();
        Application.launch(LoginApplication.class, args);
    }
}

