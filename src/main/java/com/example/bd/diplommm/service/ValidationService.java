package com.example.bd.diplommm.service;

import com.example.bd.diplommm.models.ChildModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]{10,15}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[А-Яа-яЁё\\-\\s]+$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[А-Яа-яЁё0-9\\s\\-\\.\\,]+$");

    public static ValidationResult validateChild(ChildModel child) {
        List<String> errors = new ArrayList<>();

        if (child.getLastName() == null || child.getLastName().trim().isEmpty()) {
            errors.add("Фамилия обязательна для заполнения");
        } else if (!NAME_PATTERN.matcher(child.getLastName()).matches()) {
            errors.add("Фамилия должна содержать только русские буквы, дефисы и пробелы");
        }
        if (child.getFirstName() == null || child.getFirstName().trim().isEmpty()) {
            errors.add("Имя обязательно для заполнения");
        } else if (!NAME_PATTERN.matcher(child.getFirstName()).matches()) {
            errors.add("Имя должно содержать только русские буквы, дефисы и пробелы");
        }
        if (child.getBirthDate() == null) {
            errors.add("Дата рождения обязательна для заполнения");
        } else {
            LocalDate birthDate = child.getBirthDate().toLocalDate();
            LocalDate now = LocalDate.now();
            if (birthDate.isAfter(now)) {
                errors.add("Дата рождения не может быть в будущем");
            }
            if (birthDate.isBefore(LocalDate.of(2007, 1, 1))) {
                errors.add("Дата рождения не может быть раньше 2007 года");
            }
        }
        if (child.getGender() == null || child.getGender().trim().isEmpty()) {
            errors.add("Пол обязателен для заполнения");
        }
        if (child.getHealthLossDegree() == null || child.getHealthLossDegree().trim().isEmpty()) {
            errors.add("Степень утраты здоровья обязательна для заполнения");
        }
        if (child.getRepresentativePhone() != null && !child.getRepresentativePhone().trim().isEmpty() && !PHONE_PATTERN.matcher(child.getRepresentativePhone()).matches()) {
            errors.add("Некорректный номер телефона представителя");
        }
        if (child.getResidence() != null &&
                !child.getResidence().trim().isEmpty() &&
                !ADDRESS_PATTERN.matcher(child.getResidence()).matches()) {
            errors.add("Адрес места жительства содержит недопустимые символы");
        }
        if (child.getExaminationDate() != null) {
            LocalDate examDate = child.getExaminationDate().toLocalDate();
            if (examDate.isAfter(LocalDate.now())) {
                errors.add("Дата освидетельствования не может быть в будущем");
            }
            if (examDate.isBefore(LocalDate.of(2007, 1, 1))) {
                errors.add("Дата освидетельствования не может быть раньше 2007 года");
            }
        }
        if (child.getHealthLossTerm() != null) {
            LocalDate healthTerm = child.getHealthLossTerm().toLocalDate();
            if (healthTerm.isBefore(LocalDate.of(2007, 1, 1))) {
                errors.add("Срок утраты здоровья не может быть раньше 2007 года");
            }
            if (child.getBirthDate() != null &&
                    healthTerm.isBefore(child.getBirthDate().toLocalDate())) {
                errors.add("Срок утраты здоровья не может быть раньше даты рождения");
            }
        }
        if (child.isFirstTime() && child.isRepeat()) {
            errors.add("Ребенок не может быть одновременно первичным и повторным");
        }
        return new ValidationResult(errors);
    }

    public static class ValidationResult {
        private final List<String> errors;
        private final boolean isValid;

        public ValidationResult(List<String> errors) {
            this.errors = errors;
            this.isValid = errors.isEmpty();
        }

        public boolean isValid() {
            return isValid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            if (isValid) {
                return "Валидация пройдена успешно";
            }

            StringBuilder sb = new StringBuilder("Найдены ошибки валидации:\n");
            for (String error : errors) {
                sb.append("• ").append(error).append("\n");
            }
            return sb.toString();
        }
    }
    public static ValidationResult validateChildForAdd(ChildModel child, DatabaseService dbService) {
        List<String> errors = validateChild(child).getErrors();

        // Проверка на дубликаты
        if (child.getLastName() != null && child.getFirstName() != null &&
                child.getBirthDate() != null) {

            boolean exists = dbService.childExists(
                    child.getLastName(),
                    child.getFirstName(),
                    child.getPatronymic(),
                    child.getBirthDate()
            );

            if (exists) {
                errors.add("Ребенок с такими ФИО и датой рождения уже существует в базе данных");
            }
        }

        return new ValidationResult(errors);
    }

}