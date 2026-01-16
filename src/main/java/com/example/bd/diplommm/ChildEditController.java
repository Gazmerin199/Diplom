package com.example.bd.diplommm;

import com.example.bd.diplommm.models.ChildModel;
import com.example.bd.diplommm.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChildEditController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label idLabel;
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField patronymicField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;

    // Место жительства
    @FXML private TextArea residenceArea;
    @FXML private TextArea actualResidenceArea;

    // Представитель
    @FXML private TextField representativeNameField;
    @FXML private TextArea representativeResidenceArea;
    @FXML private TextArea representativeActualResidenceArea;
    @FXML private TextField representativePhoneField;

    // Освидетельствование
    @FXML private TextField examinationNumberField;
    @FXML private DatePicker examinationDatePicker;
    @FXML private CheckBox firstTimeCheckBox;
    @FXML private CheckBox repeatCheckBox;
    @FXML private CheckBox untilEighteenCheckBox;

    // Образование
    @FXML private TextField educationPlaceField;
    @FXML private TextField educationProgramField;
    @FXML private TextArea educationConditionsArea;

    // Инвалидность
    @FXML private ComboBox<String> healthLossDegreeComboBox;
    @FXML private DatePicker healthLossTermPicker;
    @FXML private TextField disabilityReasonField;
    @FXML private TextArea expertDecisionAdditionArea;

    // Статус
    @FXML private Label statusLabel;

    private ChildModel child;
    private ChildModel originalChild;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Pattern phonePattern = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]{10,15}$");
    private final Pattern numberPattern = Pattern.compile("^[0-9]+$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupValidators();
    }

    private void setupComboBoxes() {
        // Настройка ComboBox для пола
        genderComboBox.getItems().addAll("Мужской", "Женский");
        genderComboBox.setValue("Мужской");

        // Настройка ComboBox для степени утраты здоровья
        healthLossDegreeComboBox.getItems().addAll(
                "I степень",
                "II степень",
                "III степень",
                "IV степень",
                "Не указана"
        );
        healthLossDegreeComboBox.setValue("Не указана");
    }

    private void setupValidators() {
        // Валидация номера телефона
        representativePhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !phonePattern.matcher(newValue).matches()) {
                representativePhoneField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                representativePhoneField.setStyle("");
            }
        });

        // Валидация номера акта
        examinationNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !numberPattern.matcher(newValue).matches()) {
                examinationNumberField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                examinationNumberField.setStyle("");
            }
        });

        // Валидация обязательных полей
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRequiredField(lastNameField, newValue);
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRequiredField(firstNameField, newValue);
        });
    }

    private void validateRequiredField(Control field, String value) {
        if (value == null || value.trim().isEmpty()) {
            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            field.setStyle("");
        }
    }

    public void setChildData(ChildModel child) {
        this.child = child;
        this.originalChild = child.copy();
        displayChildData();
    }

    private void displayChildData() {
        if (child == null) {
            statusLabel.setText("Ошибка: данные ребенка не загружены");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        try {
            titleLabel.setText("Редактирование: " + child.getFullName());
            idLabel.setText(child.getId() != null ? child.getId().toString() : "Новый");

            // Основная информация
            lastNameField.setText(child.getLastName());
            firstNameField.setText(child.getFirstName());
            patronymicField.setText(child.getPatronymic());

            if (child.getBirthDate() != null) {
                birthDatePicker.setValue(child.getBirthDate().toLocalDate());
            }

            genderComboBox.setValue(convertGenderFromCode(child.getGender()));

            // Место жительства
            residenceArea.setText(child.getResidence());
            actualResidenceArea.setText(child.getActualResidence());

            // Представитель
            representativeNameField.setText(child.getRepresentativeName());
            representativeResidenceArea.setText(child.getRepresentativeResidence());
            representativeActualResidenceArea.setText(child.getRepresentativeActualResidence());
            representativePhoneField.setText(child.getRepresentativePhone());

            // Освидетельствование
            examinationNumberField.setText(child.getExaminationNumber() != null ?
                    child.getExaminationNumber().toString() : "");

            if (child.getExaminationDate() != null) {
                examinationDatePicker.setValue(child.getExaminationDate().toLocalDate());
            }

            firstTimeCheckBox.setSelected(child.isFirstTime());
            repeatCheckBox.setSelected(child.isRepeat());
            untilEighteenCheckBox.setSelected(child.isUntilEighteen());

            // Образование
            educationPlaceField.setText(child.getEducationPlace());
            educationProgramField.setText(child.getEducationProgram());
            educationConditionsArea.setText(child.getEducationConditions());

            // Инвалидность
            healthLossDegreeComboBox.setValue(
                    child.getHealthLossDegree() != null ? child.getHealthLossDegree() : "Не указана"
            );

            if (child.getHealthLossTerm() != null) {
                healthLossTermPicker.setValue(child.getHealthLossTerm().toLocalDate());
            }

            disabilityReasonField.setText(child.getDisabilityReason());
            expertDecisionAdditionArea.setText(child.getExpertDecisionAddition());

            statusLabel.setText("Данные загружены. Редактируйте поля.");
            statusLabel.setTextFill(Color.GREEN);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка при загрузке данных: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
        }
    }

    private String convertGenderFromCode(String genderCode) {
        if (genderCode == null) return "Мужской";
        return switch (genderCode.toUpperCase()) {
            case "М", "MALE" -> "Мужской";
            case "Ж", "FEMALE" -> "Женский";
            default -> genderCode;
        };
    }

    private String convertGenderToCode(String gender) {
        if (gender == null) return "М";
        return switch (gender) {
            case "Мужской" -> "М";
            case "Женский" -> "Ж";
            default -> gender;
        };
    }

    @FXML
    private void handleSave() {
        List<String> validationErrors = validateAllFields();

        if (!validationErrors.isEmpty()) {
            showValidationErrors(validationErrors);
            return;
        }

        try {
            updateChildFromFields();

            // Сохранение в базу данных
            boolean success = DatabaseService.getInstance().updateChild(child);

            if (success) {
                statusLabel.setText("Данные успешно сохранены!");
                statusLabel.setTextFill(Color.GREEN);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText("Сохранение данных");
                alert.setContentText("Информация о ребенке успешно сохранена в базе данных.");
                alert.showAndWait();

                closeWindow();
            } else {
                throw new Exception("Не удалось сохранить данные в базу");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка при сохранении: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить данные");
            alert.setContentText("Ошибка: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private List<String> validateAllFields() {
        List<String> errors = new ArrayList<>();

        // Обязательные поля
        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            errors.add("Фамилия обязательна для заполнения");
        }

        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errors.add("Имя обязательно для заполнения");
        }

        if (birthDatePicker.getValue() == null) {
            errors.add("Дата рождения обязательна для заполнения");
        } else if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
            errors.add("Дата рождения не может быть в будущем");
        }

        // Валидация телефона
        if (!representativePhoneField.getText().isEmpty() &&
                !phonePattern.matcher(representativePhoneField.getText()).matches()) {
            errors.add("Номер телефона должен содержать только цифры и символы: + - ( )");
        }

        // Валидация номера акта
        if (!examinationNumberField.getText().isEmpty() &&
                !numberPattern.matcher(examinationNumberField.getText()).matches()) {
            errors.add("Номер акта должен содержать только цифры");
        }

        // Валидация дат
        if (healthLossTermPicker.getValue() != null &&
                healthLossTermPicker.getValue().isBefore(LocalDate.of(1900, 1, 1))) {
            errors.add("Срок утраты здоровья не может быть раньше 1900 года");
        }

        if (examinationDatePicker.getValue() != null &&
                examinationDatePicker.getValue().isBefore(LocalDate.of(1900, 1, 1))) {
            errors.add("Дата освидетельствования не может быть раньше 1900 года");
        }

        return errors;
    }

    private void showValidationErrors(List<String> errors) {
        StringBuilder errorMessage = new StringBuilder("Обнаружены ошибки валидации:\n\n");
        for (String error : errors) {
            errorMessage.append("• ").append(error).append("\n");
        }

        statusLabel.setText("Ошибки валидации");
        statusLabel.setTextFill(Color.RED);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибки валидации");
        alert.setHeaderText("Пожалуйста, исправьте следующие ошибки:");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
    }

    private void updateChildFromFields() {
        // Основная информация
        child.setLastName(lastNameField.getText().trim());
        child.setFirstName(firstNameField.getText().trim());
        child.setPatronymic(patronymicField.getText().trim());

        if (birthDatePicker.getValue() != null) {
            child.setBirthDate(Date.valueOf(birthDatePicker.getValue()));
        }

        child.setGender(convertGenderToCode(genderComboBox.getValue()));

        // Место жительства
        child.setResidence(residenceArea.getText());
        child.setActualResidence(actualResidenceArea.getText());

        // Представитель
        child.setRepresentativeName(representativeNameField.getText().trim());
        child.setRepresentativeResidence(representativeResidenceArea.getText());
        child.setRepresentativeActualResidence(representativeActualResidenceArea.getText());
        child.setRepresentativePhone(representativePhoneField.getText().trim());

        // Освидетельствование
        if (!examinationNumberField.getText().isEmpty()) {
            child.setExaminationNumber(Integer.parseInt(examinationNumberField.getText().trim()));
        } else {
            child.setExaminationNumber(null);
        }

        if (examinationDatePicker.getValue() != null) {
            child.setExaminationDate(Date.valueOf(examinationDatePicker.getValue()));
        } else {
            child.setExaminationDate(null);
        }

        child.setFirstTime(firstTimeCheckBox.isSelected());
        child.setRepeat(repeatCheckBox.isSelected());
        child.setUntilEighteen(untilEighteenCheckBox.isSelected());

        // Образование
        child.setEducationPlace(educationPlaceField.getText().trim());
        child.setEducationProgram(educationProgramField.getText().trim());
        child.setEducationConditions(educationConditionsArea.getText());

        // Инвалидность
        String degree = healthLossDegreeComboBox.getValue();
        child.setHealthLossDegree(degree.equals("Не указана") ? null : degree);

        if (healthLossTermPicker.getValue() != null) {
            child.setHealthLossTerm(Date.valueOf(healthLossTermPicker.getValue()));
        } else {
            child.setHealthLossTerm(null);
        }

        child.setDisabilityReason(disabilityReasonField.getText().trim());
        child.setExpertDecisionAddition(expertDecisionAdditionArea.getText());
    }

    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение отмены");
        alert.setHeaderText("Отмена редактирования");
        alert.setContentText("Вы уверены, что хотите отменить редактирование? Все несохраненные изменения будут потеряны.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow();
            }
        });
    }

    @FXML
    private void handleReset() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение сброса");
        alert.setHeaderText("Сброс изменений");
        alert.setContentText("Вы уверены, что хотите сбросить все изменения к исходным значениям?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                displayChildData();
                statusLabel.setText("Изменения сброшены к исходным значениям");
                statusLabel.setTextFill(Color.BLUE);
            }
        });
    }

    @FXML
    private void handleValidate() {
        List<String> validationErrors = validateAllFields();

        if (validationErrors.isEmpty()) {
            statusLabel.setText("Валидация пройдена успешно!");
            statusLabel.setTextFill(Color.GREEN);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Валидация");
            alert.setHeaderText("Успешная валидация");
            alert.setContentText("Все поля заполнены корректно. Можете сохранять данные.");
            alert.showAndWait();
        } else {
            showValidationErrors(validationErrors);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }
}