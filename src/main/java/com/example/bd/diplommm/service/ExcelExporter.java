package com.example.bd.diplommm.service;

import com.example.bd.diplommm.models.ChildModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static boolean exportChildrenToExcel(List<ChildModel> children, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            Sheet sheet = workbook.createSheet("Список детей");

            // Заголовок
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("СПИСОК ДЕТЕЙ С ИНВАЛИДНОСТЬЮ");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Подзаголовок
            Row subtitleRow = sheet.createRow(1);
            Cell subtitleCell = subtitleRow.createCell(0);
            subtitleCell.setCellValue("Всего детей: " + children.size());
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

            // Пропуск строки
            sheet.createRow(2);

            // Заголовки таблицы
            Row headerRow = sheet.createRow(3);
            String[] headers = {"№", "ID", "Фамилия", "Имя", "Отчество", "Дата рождения", "Возраст", "Степень утраты"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // Данные
            int rowNum = 4;
            int counter = 1;
            for (ChildModel child : children) {
                Row row = sheet.createRow(rowNum++);

                // №
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(counter++);
                cell0.setCellStyle(dataStyle);

                // ID
                Cell cell1 = row.createCell(1);
                if (child.getId() != null) {
                    cell1.setCellValue(child.getId());
                }
                cell1.setCellStyle(dataStyle);

                // Фамилия
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(child.getLastName() != null ? child.getLastName() : "");
                cell2.setCellStyle(dataStyle);

                // Имя
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(child.getFirstName() != null ? child.getFirstName() : "");
                cell3.setCellStyle(dataStyle);

                // Отчество
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(child.getPatronymic() != null ? child.getPatronymic() : "");
                cell4.setCellStyle(dataStyle);

                // Дата рождения
                Cell cell5 = row.createCell(5);
                if (child.getBirthDate() != null) {
                    cell5.setCellValue(child.getBirthDate().toLocalDate().format(DATE_FORMATTER));
                }
                cell5.setCellStyle(dateStyle);

                // Возраст
                Cell cell6 = row.createCell(6);
                if (child.getAge() != null) {
                    cell6.setCellValue(child.getAge());
                }
                cell6.setCellStyle(dataStyle);

                // Степень утраты
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(child.getHealthLossDegree() != null ? child.getHealthLossDegree() : "");
                cell7.setCellStyle(dataStyle);
            }

            // Авторазмер колонок
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Лист 2: Подробная информация
            Sheet detailsSheet = workbook.createSheet("Подробная информация");
            createDetailedSheet(detailsSheet, children, workbook);

            // Лист 3: Статистика
            Sheet statsSheet = workbook.createSheet("Статистика");
            createStatisticsSheet(statsSheet, children, workbook);

            // Сохранение файла
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void createDetailedSheet(Sheet sheet, List<ChildModel> children, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // Заголовок
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("ПОДРОБНАЯ ИНФОРМАЦИЯ О ДЕТЯХ");
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        // Пропуск строки
        sheet.createRow(1);

        // Заголовки таблицы
        Row headerRow = sheet.createRow(2);
        String[] headers = {"ID", "ФИО", "Дата рожд.", "Пол", "Место жительства",
                "Представитель", "Телефон", "Степень утраты",
                "Причина", "Место обучения", "Программа"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        // Данные
        int rowNum = 3;
        for (ChildModel child : children) {
            Row row = sheet.createRow(rowNum++);

            // ID
            Cell cell0 = row.createCell(0);
            if (child.getId() != null) {
                cell0.setCellValue(child.getId());
            }
            cell0.setCellStyle(dataStyle);

            // ФИО
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(child.getFullName());
            cell1.setCellStyle(dataStyle);

            // Дата рождения
            Cell cell2 = row.createCell(2);
            if (child.getBirthDate() != null) {
                cell2.setCellValue(child.getBirthDate().toLocalDate().format(DATE_FORMATTER));
            }
            cell2.setCellStyle(dataStyle);

            // Пол
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(child.getGender());
            cell3.setCellStyle(dataStyle);

            // Место жительства
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(child.getResidence() != null ? child.getResidence() : "");
            cell4.setCellStyle(dataStyle);

            // Представитель
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(child.getRepresentativeName() != null ? child.getRepresentativeName() : "");
            cell5.setCellStyle(dataStyle);

            // Телефон
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(child.getRepresentativePhone() != null ? child.getRepresentativePhone() : "");
            cell6.setCellStyle(dataStyle);

            // Степень утраты
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(child.getHealthLossDegree() != null ? child.getHealthLossDegree() : "");
            cell7.setCellStyle(dataStyle);

            // Причина
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(child.getDisabilityReason() != null ? child.getDisabilityReason() : "");
            cell8.setCellStyle(dataStyle);

            // Место обучения
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(child.getEducationPlace() != null ? child.getEducationPlace() : "");
            cell9.setCellStyle(dataStyle);

            // Программа
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(child.getEducationProgram() != null ? child.getEducationProgram() : "");
            cell10.setCellStyle(dataStyle);
        }

        // Авторазмер колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void createStatisticsSheet(Sheet sheet, List<ChildModel> children, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle boldStyle = createBoldStyle(workbook);

        // Заголовок
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("СТАТИСТИЧЕСКИЕ ДАННЫЕ");
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // Пропуск строки
        sheet.createRow(1);

        // Общая статистика
        Row statsRow1 = sheet.createRow(2);
        Cell statsCell1 = statsRow1.createCell(0);
        statsCell1.setCellValue("ОБЩАЯ СТАТИСТИКА");
        statsCell1.setCellStyle(boldStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        // Расчет статистики
        long total = children.size();
        long withDegree = children.stream()
                .filter(c -> c.getHealthLossDegree() != null && !c.getHealthLossDegree().isEmpty())
                .count();
        long minors = children.stream()
                .filter(c -> c.getAge() != null && c.getAge() < 18)
                .count();
        long males = children.stream()
                .filter(c -> c.getGender() != null &&
                        (c.getGender().equals("М") || c.getGender().equals("Мужской")))
                .count();
        long females = children.stream()
                .filter(c -> c.getGender() != null &&
                        (c.getGender().equals("Ж") || c.getGender().equals("Женский")))
                .count();

        // Вывод статистики
        String[] statsLabels = {"Всего детей:", "Со степенью утраты:",
                "Несовершеннолетних:", "Мальчиков:", "Девочек:"};
        String[] statsValues = {String.valueOf(total), String.valueOf(withDegree),
                String.valueOf(minors), String.valueOf(males), String.valueOf(females)};

        for (int i = 0; i < statsLabels.length; i++) {
            Row row = sheet.createRow(3 + i);
            Cell labelCell = row.createCell(0);
            labelCell.setCellValue(statsLabels[i]);
            labelCell.setCellStyle(boldStyle);

            Cell valueCell = row.createCell(1);
            valueCell.setCellValue(statsValues[i]);
            valueCell.setCellStyle(dataStyle);
        }

        // Распределение по степени утраты
        Row degreeRow = sheet.createRow(9);
        Cell degreeCell = degreeRow.createCell(0);
        degreeCell.setCellValue("РАСПРЕДЕЛЕНИЕ ПО СТЕПЕНИ УТРАТЫ ЗДОРОВЬЯ");
        degreeCell.setCellStyle(boldStyle);
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 3));

        // Расчет распределения
        long degree1 = children.stream()
                .filter(c -> c.getHealthLossDegree() != null && c.getHealthLossDegree().contains("I"))
                .count();
        long degree2 = children.stream()
                .filter(c -> c.getHealthLossDegree() != null && c.getHealthLossDegree().contains("II"))
                .count();
        long degree3 = children.stream()
                .filter(c -> c.getHealthLossDegree() != null && c.getHealthLossDegree().contains("III"))
                .count();
        long degree4 = children.stream()
                .filter(c -> c.getHealthLossDegree() != null && c.getHealthLossDegree().contains("IV"))
                .count();
        long unknown = children.stream()
                .filter(c -> c.getHealthLossDegree() == null || c.getHealthLossDegree().isEmpty())
                .count();

        String[] degreeLabels = {"I степень:", "II степень:", "III степень:",
                "IV степень:", "Не указана:"};
        String[] degreeValues = {String.valueOf(degree1), String.valueOf(degree2),
                String.valueOf(degree3), String.valueOf(degree4),
                String.valueOf(unknown)};

        for (int i = 0; i < degreeLabels.length; i++) {
            Row row = sheet.createRow(10 + i);
            Cell labelCell = row.createCell(0);
            labelCell.setCellValue(degreeLabels[i]);
            labelCell.setCellStyle(boldStyle);

            Cell valueCell = row.createCell(1);
            valueCell.setCellValue(degreeValues[i]);
            valueCell.setCellStyle(dataStyle);
        }

        // Авторазмер колонок
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setDataFormat(workbook.createDataFormat().getFormat("dd.mm.yyyy"));
        return style;
    }

    private static CellStyle createBoldStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }
}