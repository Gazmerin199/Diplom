package com.example.bd.diplommm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("admin".equals(username) && "password".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1366, 720));
                stage.setTitle("Банк данных детей с инвалидностью");
            } catch (IOException e) {
                errorLabel.setText("Ошибка загрузки страницы: " + e.getMessage());
            }
        } else {
            errorLabel.setText("Неверные учётные данные!");
        }
    }
}
