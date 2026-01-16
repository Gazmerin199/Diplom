package com.example.bd.diplommm;

import com.example.bd.diplommm.models.ChildModel;
import com.example.bd.diplommm.service.DatabaseService;
import com.example.bd.diplommm.service.ValidationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddChildController implements Initializable {

    // Основные поля
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField patronymicField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;

    // Labels для ошибок
    @FXML private Label lastNameError;
    @FXML private Label firstNameError;
    @FXML private Label birthDateError;
    @FXML private Label genderError;
    @FXML private Label representativeNameError;
    @FXML private Label representativePhoneError;
    @FXML private Label healthLossDegreeError;

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

    @FXML private Label statusLabel;
    private final Pattern phonePattern = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]{10,15}$");
    private final Pattern numberPattern = Pattern.compile("^[0-9]+$");
    private final Pattern namePattern = Pattern.compile("^[А-Яа-яЁё\\-\\s]+$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupValidators();
        setupDefaultValues();
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
                "IV степень"
        );
        healthLossDegreeComboBox.setValue("I степень");
    }

    private void setupDefaultValues() {
        birthDatePicker.setValue(LocalDate.now().minusYears(10)); // Ребенок примерно 10 лет
        examinationDatePicker.setValue(LocalDate.now());
        healthLossTermPicker.setValue(LocalDate.now().plusYears(1)); // Срок через год

        firstTimeCheckBox.setSelected(true);
        repeatCheckBox.setSelected(false);
        untilEighteenCheckBox.setSelected(true); // По умолчанию ребенок до 18 лет
    }

    private void setupValidators() {
        // Валидация обязательных полей
        setupRequiredFieldValidator(lastNameField, lastNameError, "Фамилия обязательна");
        setupRequiredFieldValidator(firstNameField, firstNameError, "Имя обязательно");
        setupRequiredFieldValidator(representativeNameField, representativeNameError, "ФИО представителя обязательно");
        setupRequiredFieldValidator(representativePhoneField, representativePhoneError, "Телефон представителя обязателен");

        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !namePattern.matcher(newValue).matches()) {
                lastNameError.setText("Только русские буквы и дефис");
                lastNameError.setVisible(true);
            } else if (!newValue.isEmpty()) {
                lastNameError.setVisible(false);
            }
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !namePattern.matcher(newValue).matches()) {
                firstNameError.setText("Только русские буквы и дефис");
                firstNameError.setVisible(true);
            } else if (!newValue.isEmpty()) {
                firstNameError.setVisible(false);
            }
        });

        representativePhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !phonePattern.matcher(newValue).matches()) {
                representativePhoneError.setText("Некорректный номер телефона");
                representativePhoneError.setVisible(true);
            } else if (!newValue.isEmpty()) {
                representativePhoneError.setVisible(false);
            }
        });

        birthDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isAfter(LocalDate.now())) {
                    birthDateError.setText("Дата не может быть в будущем");
                    birthDateError.setVisible(true);
                } else if (newValue.isBefore(LocalDate.of(1900, 1, 1))) {
                    birthDateError.setText("Дата не может быть раньше 1900 года");
                    birthDateError.setVisible(true);
                } else {
                    birthDateError.setVisible(false);
                }
            }
        });
        examinationNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !numberPattern.matcher(newValue).matches()) {
                examinationNumberField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            } else {
                examinationNumberField.setStyle("");
            }
        });
    }

    private void setupRequiredFieldValidator(TextField field, Label errorLabel, String errorMessage) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        });
    }

    private void clearAllErrorLabels() {
        lastNameError.setVisible(false);
        firstNameError.setVisible(false);
        birthDateError.setVisible(false);
        genderError.setVisible(false);
        representativeNameError.setVisible(false);
        representativePhoneError.setVisible(false);
        healthLossDegreeError.setVisible(false);
    }

    @FXML
    private void handleSave() {
        clearAllErrorLabels();

        ValidationService.ValidationResult validation = validateForm();

        if (!validation.isValid()) {
            showValidationErrors(validation);
            return;
        }

        try {
            ChildModel child = createChildFromFields();

            // Сохранение в базу данных
            boolean success = DatabaseService.getInstance().saveChildIPRA(child);

            if (success) {
                statusLabel.setText("Ребенок успешно добавлен!");
                statusLabel.setTextFill(Color.GREEN);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех");
                alert.setHeaderText("Добавление ребенка");
                alert.setContentText("Информация о ребенке успешно сохранена в базу данных.");
                alert.showAndWait();

                // Очистка формы после успешного сохранения
                handleClear();

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

    private ValidationService.ValidationResult validateForm() {
        // Создаем временный объект для валидации
        ChildModel tempChild = createChildFromFields();
        return ValidationService.validateChild(tempChild);
    }

    private ChildModel createChildFromFields() {
        ChildModel child = new ChildModel();

        // Основная информация
        child.setLastName(lastNameField.getText().trim());
        child.setFirstName(firstNameField.getText().trim());
        child.setPatronymic(patronymicField.getText().trim());

        if (birthDatePicker.getValue() != null) {
            child.setBirthDate(Date.valueOf(birthDatePicker.getValue()));
        }

        child.setGender(convertGenderToCode(genderComboBox.getValue()));

        // Место жительства
        child.setResidence(residenceArea.getText().trim());
        child.setActualResidence(actualResidenceArea.getText().trim());

        // Представитель
        child.setRepresentativeName(representativeNameField.getText().trim());
        child.setRepresentativeResidence(representativeResidenceArea.getText().trim());
        child.setRepresentativeActualResidence(representativeActualResidenceArea.getText().trim());
        child.setRepresentativePhone(representativePhoneField.getText().trim());

        // Освидетельствование
        if (!examinationNumberField.getText().isEmpty()) {
            try {
                child.setExaminationNumber(Integer.parseInt(examinationNumberField.getText().trim()));
            } catch (NumberFormatException e) {
                child.setExaminationNumber(null);
            }
        }

        if (examinationDatePicker.getValue() != null) {
            child.setExaminationDate(Date.valueOf(examinationDatePicker.getValue()));
        }

        child.setFirstTime(firstTimeCheckBox.isSelected());
        child.setRepeat(repeatCheckBox.isSelected());
        child.setUntilEighteen(untilEighteenCheckBox.isSelected());

        // Образование
        child.setEducationPlace(educationPlaceField.getText().trim());
        child.setEducationProgram(educationProgramField.getText().trim());
        child.setEducationConditions(educationConditionsArea.getText().trim());

        // Инвалидность
        child.setHealthLossDegree(healthLossDegreeComboBox.getValue());

        if (healthLossTermPicker.getValue() != null) {
            child.setHealthLossTerm(Date.valueOf(healthLossTermPicker.getValue()));
        }

        child.setDisabilityReason(disabilityReasonField.getText().trim());
        child.setExpertDecisionAddition(expertDecisionAdditionArea.getText().trim());

        return child;
    }

    private String convertGenderToCode(String gender) {
        if (gender == null) return "М";
        return switch (gender) {
            case "Мужской" -> "М";
            case "Женский" -> "Ж";
            default -> gender;
        };
    }

    private void showValidationErrors(ValidationService.ValidationResult validation) {
        StringBuilder errorMessage = new StringBuilder("Обнаружены ошибки валидации:\n\n");
        for (String error : validation.getErrors()) {
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

    @FXML
    private void handleClear() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение очистки");
        alert.setHeaderText("Очистка формы");
        alert.setContentText("Вы уверены, что хотите очистить все поля формы?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clearForm();
                statusLabel.setText("Форма очищена. Заполните обязательные поля (*)");
                statusLabel.setTextFill(Color.BLACK);
            }
        });
    }

    private void clearForm() {
        // Основная информация
        lastNameField.clear();
        firstNameField.clear();
        patronymicField.clear();
        birthDatePicker.setValue(LocalDate.now().minusYears(10));
        genderComboBox.setValue("Мужской");

        // Место жительства
        residenceArea.clear();
        actualResidenceArea.clear();

        // Представитель
        representativeNameField.clear();
        representativeResidenceArea.clear();
        representativeActualResidenceArea.clear();
        representativePhoneField.clear();

        // Освидетельствование
        examinationNumberField.clear();
        examinationDatePicker.setValue(LocalDate.now());
        firstTimeCheckBox.setSelected(true);
        repeatCheckBox.setSelected(false);
        untilEighteenCheckBox.setSelected(true);

        // Образование
        educationPlaceField.clear();
        educationProgramField.clear();
        educationConditionsArea.clear();

        // Инвалидность
        healthLossDegreeComboBox.setValue("I степень");
        healthLossTermPicker.setValue(LocalDate.now().plusYears(1));
        disabilityReasonField.clear();
        expertDecisionAdditionArea.clear();

        // Очистка ошибок
        clearAllErrorLabels();
    }

    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение отмены");
        alert.setHeaderText("Отмена добавления");
        alert.setContentText("Вы уверены, что хотите отменить добавление ребенка? Все введенные данные будут потеряны.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow();
            }
        });
    }

    @FXML
    private void handleValidate() {
        clearAllErrorLabels();

        ValidationService.ValidationResult validation = validateForm();

        if (validation.isValid()) {
            statusLabel.setText("Валидация пройдена успешно!");
            statusLabel.setTextFill(Color.GREEN);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Валидация");
            alert.setHeaderText("Успешная валидация");
            alert.setContentText("Все поля заполнены корректно. Можете сохранять данные.");
            alert.showAndWait();
        } else {
            showValidationErrors(validation);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) lastNameField.getScene().getWindow();
        stage.close();
    }
}