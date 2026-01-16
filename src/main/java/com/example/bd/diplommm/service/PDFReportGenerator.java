package com.example.bd.diplommm.service;

import com.example.bd.diplommm.models.ChildModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;

public class PDFReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static boolean generateChildReport(ChildModel child, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Создаем шрифт с поддержкой кириллицы
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf",
                    PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            document.setFont(font);

            // Заголовок
            Paragraph title = new Paragraph("ИНФОРМАЦИЯ О РЕБЕНКЕ С ИНВАЛИДНОСТЬЮ")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16)
                    .setBold();
            document.add(title);

            // Дата генерации
            Paragraph date = new Paragraph("Дата генерации отчета: " +
                    LocalDate.now().format(DATE_FORMATTER))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10)
                    .setItalic();
            document.add(date);

            document.add(new Paragraph("\n"));

            // Основная информация
            Paragraph section1 = new Paragraph("1. ОСНОВНАЯ ИНФОРМАЦИЯ")
                    .setBold()
                    .setFontSize(14);
            document.add(section1);

            Table table1 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table1.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table1, "ID:", child.getId() != null ? child.getId().toString() : "Не указано");
            addTableRow(table1, "ФИО:", child.getFullName());
            addTableRow(table1, "Дата рождения:",
                    child.getBirthDate() != null ?
                            child.getBirthDate().toLocalDate().format(DATE_FORMATTER) :
                            "Не указано");
            addTableRow(table1, "Возраст:",
                    child.getAge() != null ? child.getAge() + " лет" : "Не указано");
            addTableRow(table1, "Пол:", child.getGender());

            document.add(table1);
            document.add(new Paragraph("\n"));

            // Место жительства
            Paragraph section2 = new Paragraph("2. МЕСТО ЖИТЕЛЬСТВА")
                    .setBold()
                    .setFontSize(14);
            document.add(section2);

            Table table2 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table2.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table2, "По прописке:",
                    child.getResidence() != null ? child.getResidence() : "Не указано");
            addTableRow(table2, "Фактическое:",
                    child.getActualResidence() != null ? child.getActualResidence() : "Не указано");

            document.add(table2);
            document.add(new Paragraph("\n"));

            // Информация о представителе
            Paragraph section3 = new Paragraph("3. ЗАКОННЫЙ ПРЕДСТАВИТЕЛЬ")
                    .setBold()
                    .setFontSize(14);
            document.add(section3);

            Table table3 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table3.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table3, "ФИО представителя:",
                    child.getRepresentativeName() != null ? child.getRepresentativeName() : "Не указано");
            addTableRow(table3, "Телефон:",
                    child.getRepresentativePhone() != null ? child.getRepresentativePhone() : "Не указано");

            document.add(table3);
            document.add(new Paragraph("\n"));

            // Медико-социальная экспертиза
            Paragraph section4 = new Paragraph("4. МЕДИКО-СОЦИАЛЬНАЯ ЭКСПЕРТИЗА")
                    .setBold()
                    .setFontSize(14);
            document.add(section4);

            Table table4 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table4.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table4, "Степень утраты здоровья:",
                    child.getHealthLossDegree() != null ? child.getHealthLossDegree() : "Не указана");
            addTableRow(table4, "Срок утраты здоровья:",
                    child.getHealthLossTerm() != null ?
                            child.getHealthLossTerm().toLocalDate().format(DATE_FORMATTER) :
                            "Не указан");
            addTableRow(table4, "Причина инвалидности:",
                    child.getDisabilityReason() != null ? child.getDisabilityReason() : "Не указана");

            document.add(table4);
            document.add(new Paragraph("\n"));

            // Образование
            Paragraph section5 = new Paragraph("5. ОБРАЗОВАНИЕ")
                    .setBold()
                    .setFontSize(14);
            document.add(section5);

            Table table5 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table5.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table5, "Место обучения:",
                    child.getEducationPlace() != null ? child.getEducationPlace() : "Не указано");
            addTableRow(table5, "Образовательная программа:",
                    child.getEducationProgram() != null ? child.getEducationProgram() : "Не указана");
            addTableRow(table5, "Условия обучения:",
                    child.getEducationConditions() != null ? child.getEducationConditions() : "Не указаны");

            document.add(table5);
            document.add(new Paragraph("\n"));

            // Дополнительная информация
            Paragraph section6 = new Paragraph("6. ДОПОЛНИТЕЛЬНАЯ ИНФОРМАЦИЯ")
                    .setBold()
                    .setFontSize(14);
            document.add(section6);

            Table table6 = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            table6.setWidth(UnitValue.createPercentValue(100));

            addTableRow(table6, "Номер акта освидетельствования:",
                    child.getExaminationNumber() != null ? child.getExaminationNumber().toString() : "Не указан");
            addTableRow(table6, "Дата освидетельствования:",
                    child.getExaminationDate() != null ?
                            child.getExaminationDate().toLocalDate().format(DATE_FORMATTER) :
                            "Не указана");
            addTableRow(table6, "Первичное:", child.isFirstTime() ? "Да" : "Нет");
            addTableRow(table6, "Повторное:", child.isRepeat() ? "Да" : "Нет");
            addTableRow(table6, "До 18 лет:", child.isUntilEighteen() ? "Да" : "Нет");

            document.add(table6);
            document.add(new Paragraph("\n"));

            // Примечания
            if (child.getExpertDecisionAddition() != null && !child.getExpertDecisionAddition().isEmpty()) {
                Paragraph notes = new Paragraph("Примечания:\n" + child.getExpertDecisionAddition())
                        .setFontSize(10);
                document.add(notes);
            }

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean generateChildrenListReport(List<ChildModel> children, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf",
                    PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            document.setFont(font);

            // Заголовок
            Paragraph title = new Paragraph("СПИСОК ДЕТЕЙ С ИНВАЛИДНОСТЬЮ")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16)
                    .setBold();
            document.add(title);

            Paragraph subtitle = new Paragraph("Всего детей: " + children.size())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12);
            document.add(subtitle);

            Paragraph date = new Paragraph("Дата генерации: " +
                    LocalDate.now().format(DATE_FORMATTER))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10)
                    .setItalic();
            document.add(date);

            document.add(new Paragraph("\n"));

            // Таблица
            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 30, 20, 20, 20}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Заголовки таблицы
            table.addHeaderCell(new Cell().add(new Paragraph("№").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("ФИО").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Дата рождения").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Возраст").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Степень утраты").setBold()));

            // Данные
            int counter = 1;
            for (ChildModel child : children) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(counter++))));
                table.addCell(new Cell().add(new Paragraph(child.getFullName())));

                String birthDate = child.getBirthDate() != null ?
                        child.getBirthDate().toLocalDate().format(DATE_FORMATTER) : "Не указано";
                table.addCell(new Cell().add(new Paragraph(birthDate)));

                String age = child.getAge() != null ? child.getAge() + " лет" : "Не указано";
                table.addCell(new Cell().add(new Paragraph(age)));

                String degree = child.getHealthLossDegree() != null ?
                        child.getHealthLossDegree() : "Не указана";
                table.addCell(new Cell().add(new Paragraph(degree)));
            }

            document.add(table);

            // Статистика в конце
            document.add(new Paragraph("\n\n"));

            long withDegree = children.stream()
                    .filter(c -> c.getHealthLossDegree() != null && !c.getHealthLossDegree().isEmpty())
                    .count();

            long minors = children.stream()
                    .filter(c -> c.getAge() != null && c.getAge() < 18)
                    .count();

            Paragraph stats = new Paragraph("СТАТИСТИКА:\n" +
                    "• Всего детей: " + children.size() + "\n" +
                    "• С указанной степенью утраты: " + withDegree + "\n" +
                    "• Несовершеннолетних: " + minors)
                    .setFontSize(11);
            document.add(stats);

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void addTableRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold()));
        table.addCell(new Cell().add(new Paragraph(value != null ? value : "Не указано")));
    }
}