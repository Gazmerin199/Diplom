package com.example.bd.diplommm;

import com.example.bd.diplommm.models.ChildModel;
import com.example.bd.diplommm.service.DatabaseService;
import com.example.bd.diplommm.service.ExcelExporter;
import com.example.bd.diplommm.service.PDFReportGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button childrenAdd;
    @FXML private Label childCountLabel;
    @FXML private TableView<ChildModel> childrenTable;
    @FXML private TableColumn<ChildModel, Integer> idColumn;
    @FXML private TableColumn<ChildModel, String> nameColumn;
    @FXML private TableColumn<ChildModel, String> disabilityColumn;
    @FXML private TableColumn<ChildModel, Void> actionsColumn;

    private final ObservableList<ChildModel> childrenData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MainController initialize called");
        try {
            DatabaseService.getInstance().printTableStructure();

            setupTable();
            loadChildrenData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка инициализации", e.getMessage());
        }
    }

    private void setupTable() {
        System.out.println("Setting up table...");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle("-fx-alignment: CENTER;");

        idColumn.setCellFactory(column -> new TableCell<ChildModel, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    System.out.println("Cell ID: " + item);
                }
            }
        });
        nameColumn.setCellValueFactory(cellData -> {
            ChildModel child = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(child.getFullName());
        });
        disabilityColumn.setCellValueFactory(cellData -> {
            ChildModel child = cellData.getValue();
            String disability = child.getHealthLossDegree();
            return new javafx.beans.property.SimpleStringProperty(disability != null ? disability : "Не указана");
        });
        actionsColumn.setCellFactory(new Callback<TableColumn<ChildModel, Void>, TableCell<ChildModel, Void>>() {
            @Override
            public TableCell<ChildModel, Void> call(final TableColumn<ChildModel, Void> param) {
                return new TableCell<ChildModel, Void>() {
                    private final Button viewButton = new Button("Просмотреть");
                    private final Button editButton = new Button("Изменить");
                    private final Button deleteButton = new Button("Удалить");
                    private final HBox buttonsContainer = new HBox(5, viewButton, editButton, deleteButton);
                    {
                        viewButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 10px;");
                        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 10px;");
                        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 10px;");
                        viewButton.setOnAction(event -> {
                            ChildModel child = getTableView().getItems().get(getIndex());
                            handleViewChildDetails(child);
                        });

                        editButton.setOnAction(event -> {
                            ChildModel child = getTableView().getItems().get(getIndex());
                            handleEditChild(child);
                        });

                        deleteButton.setOnAction(event -> {
                            ChildModel child = getTableView().getItems().get(getIndex());
                            handleDeleteChild(child);
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        });
    }
    private void loadChildrenData() throws SQLException {
        System.out.println("Loading children data...");
        childrenData.clear();
        var childrenList = DatabaseService.getInstance().getAllChildrenTable();
        childrenData.addAll(childrenList);
        for (ChildModel child : childrenData) {
            System.out.println("Loaded child - ID: " + child.getId() +
                    ", Name: " + child.getFullName() +
                    ", Disability: " + child.getHealthLossDegree());
        }
        childrenTable.setItems(childrenData);
        int count = DatabaseService.getInstance().getChildrenCount();
        childCountLabel.setText("В банке данных " + count + " детей");
        System.out.println("Loaded " + childrenData.size() + " records");
        System.out.println("Table columns: " + childrenTable.getColumns());
        System.out.println("ID column cell value factory: " + idColumn.getCellValueFactory());
    }

    private void handleViewChildDetails(ChildModel child) {
        try {
            System.out.println("Запрашиваем полные данные для ребенка ID: " + child.getId());
            ChildModel fullChild = DatabaseService.getInstance().getChildById(child.getId());
            if (fullChild == null) {
                showAlert("Ошибка", "Данные не найдены",
                        "Не удалось загрузить информацию о ребенке с ID: " + child.getId());
                return;
            }
            System.out.println("Загруженные данные:");
            System.out.println("  ФИО: " + fullChild.getFullName());
            System.out.println("  Дата рождения: " + fullChild.getBirthDate());
            System.out.println("  Степень утраты: " + fullChild.getHealthLossDegree());
            System.out.println("  Место жительства: " + fullChild.getResidence());
            System.out.println("  Представитель: " + fullChild.getRepresentativeName());
            System.out.println("  Телефон: " + fullChild.getRepresentativePhone());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-child.fxml"));
            Parent root = loader.load();

            ChildViewController controller = loader.getController();
            controller.setChildData(fullChild);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Просмотр информации о ребенке: " + fullChild.getFullName());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно просмотра", e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка базы данных", e.getMessage());
        }
    }

    private void handleDeleteChild(ChildModel child) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление записи о ребенке");
        alert.setContentText("Вы уверены, что хотите удалить запись о ребенке:\n" +
                child.getFullName() + "? (ID: " + child.getId() + ")");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    DatabaseService.getInstance().deleteChild(child.getId());
                    childrenData.remove(child);
                    childCountLabel.setText("В банке данных " + childrenData.size() + " детей");
                    showAlert("Успех", "Запись успешно удалена",
                            "Запись о ребенке " + child.getFullName() + " удалена.");

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Ошибка", "Не удалось удалить запись", e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleAddChild() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-child.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Добавление нового ребенка");
            stage.show();
            stage.setOnHidden(event -> {
                try {
                    loadChildrenData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно добавления", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser() {
        ChildModel selectedChild = childrenTable.getSelectionModel().getSelectedItem();
        if (selectedChild != null) {
            handleDeleteChild(selectedChild);
        } else {
            showAlert("Внимание", "Не выбрана запись",
                    "Пожалуйста, выберите ребенка из таблицы для удаления.");
        }
    }

    @FXML
    private void handleViewChildren() {
        try {
            System.out.println("Refreshing children list...");
            loadChildrenData();
            showAlert("Обновление", "Данные обновлены",
                    "Список детей успешно обновлен. Загружено " + childrenData.size() + " записей.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось обновить данные", e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        try {
            System.out.println("Manual refresh triggered...");
            loadChildrenData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось обновить данные", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleExportAllPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить общий отчет как PDF");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF файлы", "*.pdf"),
                    new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            fileChooser.setInitialFileName("Общий_отчет_" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf");

            File file = fileChooser.showSaveDialog(childrenTable.getScene().getWindow());

            if (file != null) {
                boolean success = PDFReportGenerator.generateChildrenListReport(
                        DatabaseService.getInstance().getAllChildrenTable(),
                        file.getAbsolutePath()
                );

                if (success) {
                    showAlert("Успех", "Общий PDF отчет создан",
                            "Отчет успешно сохранен в файл:\n" + file.getAbsolutePath());
                } else {
                    showAlert("Ошибка", "Не удалось создать отчет",
                            "Проверьте права доступа к файлу или наличие библиотек.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка создания общего PDF", e.getMessage());
        }
    }
    @FXML
    private void handleEditChild(ChildModel child) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-child.fxml"));
            Parent root = loader.load();

            ChildEditController controller = loader.getController();
            controller.setChildData(child);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 800));
            stage.setTitle("Редактирование: " + child.getFullName());
            stage.show();

            stage.setOnHidden(event -> {
                try {
                    loadChildrenData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно редактирования", e.getMessage());
        }
    }

    public void handleExportExcel(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Экспорт в Excel");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel файлы", "*.xlsx"),
                    new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            fileChooser.setInitialFileName("Дети_инвалиды_" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx");

            File file = fileChooser.showSaveDialog(childrenTable.getScene().getWindow());

            if (file != null) {
                boolean success = ExcelExporter.exportChildrenToExcel(
                        DatabaseService.getInstance().getAllChildrenTable(),
                        file.getAbsolutePath()
                );

                if (success) {
                    showAlert("Успех", "Экспорт в Excel завершен",
                            "Данные успешно экспортированы в файл:\n" + file.getAbsolutePath());
                } else {
                    showAlert("Ошибка", "Не удалось экспортировать данные",
                            "Проверьте права доступа к файлу или наличие библиотек.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Ошибка экспорта в Excel", e.getMessage());
        }
    }

    public void handleExportPDF(ActionEvent actionEvent) {
        ChildModel selectedChild = childrenTable.getSelectionModel().getSelectedItem();

        if (selectedChild != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Сохранить отчет как PDF");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PDF файлы", "*.pdf"),
                        new FileChooser.ExtensionFilter("Все файлы", "*.*")
                );
                fileChooser.setInitialFileName("Отчет_" + selectedChild.getLastName() + ".pdf");

                File file = fileChooser.showSaveDialog(childrenTable.getScene().getWindow());

                if (file != null) {
                    boolean success = PDFReportGenerator.generateChildReport(selectedChild, file.getAbsolutePath());

                    if (success) {
                        showAlert("Успех", "PDF отчет создан",
                                "Отчет успешно сохранен в файл:\n" + file.getAbsolutePath());
                    } else {
                        showAlert("Ошибка", "Не удалось создать отчет",
                                "Проверьте права доступа к файлу или наличие библиотек.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка создания PDF", e.getMessage());
            }
        } else {
            showAlert("Внимание", "Не выбран ребенок",
                    "Пожалуйста, выберите ребенка из таблицы для экспорта.");
        }
    }
}