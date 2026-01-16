package com.example.bd.diplommm;

import com.example.bd.diplommm.models.ChildModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChildViewController implements Initializable {

    @FXML private Label idLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label patronymicLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label ageLabel;
    @FXML private Label genderLabel;

    @FXML private Label residenceLabel;
    @FXML private Label actualResidenceLabel;

    @FXML private Label representativeNameLabel;
    @FXML private Label representativeResidenceLabel;
    @FXML private Label representativeActualResidenceLabel;
    @FXML private Label representativePhoneLabel;

    @FXML private Label examinationNumberLabel;
    @FXML private Label examinationDateLabel;
    @FXML private Label firstTimeLabel;
    @FXML private Label repeatLabel;
    @FXML private Label untilEighteenLabel;

    @FXML private Label educationPlaceLabel;
    @FXML private Label educationProgramLabel;
    @FXML private Label educationConditionsLabel;

    @FXML private Label healthLossDegreeLabel;
    @FXML private Label healthLossTermLabel;
    @FXML private Label disabilityReasonLabel;
    @FXML private TextArea expertDecisionAdditionArea;

    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;

    private ChildModel child;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("ChildViewController инициализирован");
    }

    public void setChildData(ChildModel child) {
        this.child = child;
        if (child != null) {
            System.out.println("Получены данные ребенка: ID=" + child.getId() +
                    ", ФИО=" + child.getFullName());
            displayChildData();
        } else {
            System.err.println("Ошибка: получен null вместо данных ребенка");
            statusLabel.setText("Ошибка: данные ребенка не получены");
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void displayChildData() {
        if (child == null) {
            statusLabel.setText("Ошибка: данные ребенка не загружены");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        try {
            System.out.println("Отображение данных для ребенка: " + child.getFullName());

            // Основная информация
            idLabel.setText(child.getId() != null ? child.getId().toString() : "Не указано");
            lastNameLabel.setText(child.getLastName() != null ? child.getLastName() : "Не указано");
            firstNameLabel.setText(child.getFirstName() != null ? child.getFirstName() : "Не указано");
            patronymicLabel.setText(child.getPatronymic() != null ? child.getPatronymic() : "Не указано");

            if (child.getBirthDate() != null) {
                LocalDate birthDate = child.getBirthDate().toLocalDate();
                birthDateLabel.setText(birthDate.format(dateFormatter));

                // Вычисляем возраст
                LocalDate now = LocalDate.now();
                int age = Period.between(birthDate, now).getYears();
                ageLabel.setText(age + " лет");
            } else {
                birthDateLabel.setText("Не указано");
                ageLabel.setText("Не указано");
            }

            genderLabel.setText(formatGender(child.getGender()));

            // Место жительства
            residenceLabel.setText(formatText(child.getResidence()));
            actualResidenceLabel.setText(formatText(child.getActualResidence()));

            // Информация о представителе
            representativeNameLabel.setText(formatText(child.getRepresentativeName()));
            representativeResidenceLabel.setText(formatText(child.getRepresentativeResidence()));
            representativeActualResidenceLabel.setText(formatText(child.getRepresentativeActualResidence()));
            representativePhoneLabel.setText(formatText(child.getRepresentativePhone()));

            // Данные освидетельствования
            examinationNumberLabel.setText(child.getExaminationNumber() != null ?
                    child.getExaminationNumber().toString() : "Не указано");

            if (child.getExaminationDate() != null) {
                examinationDateLabel.setText(child.getExaminationDate().toLocalDate().format(dateFormatter));
            } else {
                examinationDateLabel.setText("Не указано");
            }

            firstTimeLabel.setText(formatBoolean(child.isFirstTime()));
            repeatLabel.setText(formatBoolean(child.isRepeat()));
            untilEighteenLabel.setText(formatBoolean(child.isUntilEighteen()));

            // Образовательные данные
            educationPlaceLabel.setText(formatText(child.getEducationPlace()));
            educationProgramLabel.setText(formatText(child.getEducationProgram()));
            educationConditionsLabel.setText(formatText(child.getEducationConditions()));

            // Данные об инвалидности
            healthLossDegreeLabel.setText(formatText(child.getHealthLossDegree()));

            if (child.getHealthLossTerm() != null) {
                healthLossTermLabel.setText(child.getHealthLossTerm().toLocalDate().format(dateFormatter));
            } else {
                healthLossTermLabel.setText("Не указано");
            }

            disabilityReasonLabel.setText(formatText(child.getDisabilityReason()));
            expertDecisionAdditionArea.setText(formatText(child.getExpertDecisionAddition()));

            // Системная информация
            if (child.getCreatedDate() != null) {
                createdDateLabel.setText(child.getCreatedDate().toLocalDate().format(dateFormatter));
            } else {
                createdDateLabel.setText("Не указано");
            }

            statusLabel.setText("Информация загружена: " + child.getFullName());
            statusLabel.setTextFill(Color.GREEN);

            // Выводим в консоль для отладки
            printDebugInfo();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка при отображении данных: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
        }
    }

    private String formatGender(String genderCode) {
        if (genderCode == null || genderCode.trim().isEmpty()) return "Не указано";
        return switch (genderCode.toUpperCase()) {
            case "М", "MALE", "МУЖСКОЙ" -> "Мужской";
            case "Ж", "FEMALE", "ЖЕНСКИЙ" -> "Женский";
            default -> genderCode;
        };
    }

    private String formatBoolean(Boolean value) {
        if (value == null) return "Не указано";
        return value ? "Да" : "Нет";
    }

    private String formatText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Не указано";
        }
        return text.trim();
    }

    private void printDebugInfo() {
        System.out.println("=== Отладочная информация о ребенке ===");
        System.out.println("ID: " + child.getId());
        System.out.println("ФИО: " + child.getFullName());
        System.out.println("Дата рождения: " + child.getBirthDate());
        System.out.println("Пол: " + child.getGender());
        System.out.println("Место жительства: " + child.getResidence());
        System.out.println("Представитель: " + child.getRepresentativeName());
        System.out.println("Телефон: " + child.getRepresentativePhone());
        System.out.println("Степень утраты: " + child.getHealthLossDegree());
        System.out.println("Причина инвалидности: " + child.getDisabilityReason());
        System.out.println("Место обучения: " + child.getEducationPlace());
        System.out.println("======================================");
    }

    @FXML
    private void handleEdit() {
        if (child != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-child.fxml"));
                Parent root = loader.load();

                ChildEditController controller = loader.getController();
                controller.setChildData(child);

                Stage stage = (Stage) idLabel.getScene().getWindow();
                stage.close();

                Stage editStage = new Stage();
                editStage.setScene(new Scene(root, 1000, 800));
                editStage.setTitle("Редактирование: " + child.getFullName());
                editStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Не удалось открыть окно редактирования", e.getMessage());
            }
        }
    }

    @FXML
    private void handlePrint() {
        // TODO: Реализовать печать информации
        showAlert("Информация", "Печать",
                "Печать информации о ребенке " + child.getFullName() + " будет доступна в следующей версии.");
    }

    @FXML
    private void handleExport() {
        // TODO: Реализовать экспорт в PDF/Word
        showAlert("Информация", "Экспорт",
                "Экспорт информации о ребенке " + child.getFullName() + " будет доступен в следующей версии.");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}