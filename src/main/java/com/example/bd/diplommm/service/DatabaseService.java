package com.example.bd.diplommm.service;

import com.example.bd.diplommm.models.ChildModel;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {
    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());
    private Connection connection;
    private static DatabaseService instance;

    // Конфигурация базы данных
    private static final String DB_PATH = "c:/database.accdb"; // или полный путь "C:/path/to/database.accdb"
    private static final String JDBC_URL = "jdbc:ucanaccess://" + DB_PATH;

    // Приватный конструктор (Singleton)
    private DatabaseService() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(JDBC_URL);
            LOGGER.info("Подключение к базе данных установлено");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Драйвер UCanAccess не найден", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка подключения к базе данных", e);
        }
    }

    // Получение экземпляра (Singleton)
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public void createTable() throws SQLException {
        String ipraTable = "CREATE TABLE child_ipra (" +
                "id COUNTER CONSTRAINT IPRA_PK PRIMARY KEY, " +
                "last_name TEXT(100) NOT NULL, " +
                "first_name TEXT(100) NOT NULL, " +
                "patronymic TEXT(100), " +
                "birth_date DATETIME, " +
                "gender TEXT(10), " +
                "residence TEXT(255), " +
                "actual_residence TEXT(255), " +
                "representative_name TEXT(255), " +
                "representative_residence TEXT(255), " +
                "representative_actual_residence TEXT(255), " +
                "representative_phone TEXT(20), " +
                "examination_number INTEGER, " +
                "examination_date DATETIME, " +
                "education_place TEXT(255), " +
                "education_program TEXT(255), " +
                "specialty TEXT(255), " +
                "health_loss_degree TEXT(50), " +
                "health_loss_term DATETIME, " +
                "disability_reason TEXT(255), " +
                "expert_decision_addition MEMO, " +
                "organ_disorders MEMO, " +
                "work_conditions MEMO, " +
                "harmful_factors MEMO, " +
                "education_conditions MEMO, " +
                "first_time YESNO, " +
                "repeat YESNO, " +
                "until_eighteen YESNO, " +
                "created_date DATETIME DEFAULT NOW())";

        try (Statement stmt = connection.createStatement()) {
            try {
                stmt.executeUpdate(ipraTable);
                System.out.println("Таблица 'child_ipra' создана");
            } catch (SQLException e) {
                System.out.println("Таблица 'child_ipra' уже существует");
            }
        }
    }
    public boolean saveChildIPRA(ChildModel childModel) {
        checkTableStructure();
        String sql = "INSERT INTO child_ipra (last_name, first_name, patronymic, birth_date, gender, " +
                "residence, actual_residence, representative_name, representative_residence, " +
                "representative_actual_residence, representative_phone, examination_number, " +
                "examination_date, education_place, education_program, " +
                "health_loss_degree, health_loss_term, disability_reason, " +
                "expert_decision_addition, education_conditions, first_time, repeat, until_eighteen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Вспомогательная функция для безопасной установки значений
            int index = 1;

            // 1. last_name (обязательное поле)
            setStringOrNull(stmt, index++, childModel.getLastName());

            // 2. first_name (обязательное поле)
            setStringOrNull(stmt, index++, childModel.getFirstName());

            // 3. patronymic
            setStringOrNull(stmt, index++, childModel.getPatronymic());

            // 4. birth_date
            setDateOrNull(stmt, index++, childModel.getBirthDate());

            // 5. gender
            setStringOrNull(stmt, index++, childModel.getGender());

            // 6. residence
            setStringOrNull(stmt, index++, childModel.getResidence());

            // 7. actual_residence
            setStringOrNull(stmt, index++, childModel.getActualResidence());

            // 8. representative_name
            setStringOrNull(stmt, index++, childModel.getRepresentativeName());

            // 9. representative_residence
            setStringOrNull(stmt, index++, childModel.getRepresentativeResidence());

            // 10. representative_actual_residence
            setStringOrNull(stmt, index++, childModel.getRepresentativeActualResidence());

            // 11. representative_phone
            setStringOrNull(stmt, index++, childModel.getRepresentativePhone());

            // 12. examination_number
            setIntegerOrNull(stmt, index++, childModel.getExaminationNumber());

            // 13. examination_date
            setDateOrNull(stmt, index++, childModel.getExaminationDate());

            // 14. education_place
            setStringOrNull(stmt, index++, childModel.getEducationPlace());

            // 15. education_program
            setStringOrNull(stmt, index++, childModel.getEducationProgram());

            // 16. health_loss_degree
            setStringOrNull(stmt, index++, childModel.getHealthLossDegree());

            // 17. health_loss_term
            setDateOrNull(stmt, index++, childModel.getHealthLossTerm());

            // 18. disability_reason
            setStringOrNull(stmt, index++, childModel.getDisabilityReason());

            // 19. expert_decision_addition
            setStringOrNull(stmt, index++, childModel.getExpertDecisionAddition());

            // 20. education_conditions
            setStringOrNull(stmt, index++, childModel.getEducationConditions());

            // 21. first_time
            setBooleanOrNull(stmt, index++, childModel.isFirstTime());

            // 22. repeat
            setBooleanOrNull(stmt, index++, childModel.isRepeat());

            // 23. until_eighteen
            setBooleanOrNull(stmt, index++, childModel.isUntilEighteen());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getErrorCode() + " - " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());

            // Детальная диагностика
            if (e.getMessage().contains("integrity constraint")) {
                System.err.println("Ошибка ограничений целостности данных");
            } else if (e.getMessage().contains("NULL")) {
                System.err.println("Попытка вставить NULL в NOT NULL поле");
            } else if (e.getMessage().contains("data type")) {
                System.err.println("Несоответствие типа данных");
            }

            // Выводим стек вызовов для отладки
            System.err.println("Детальная информация об ошибке:");
            e.printStackTrace();

            return false;
        }
    }
    private void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.trim().isEmpty()) {
            stmt.setString(index, value.trim());
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }

    private void setDateOrNull(PreparedStatement stmt, int index, Date value) throws SQLException {
        if (value != null) {
            stmt.setDate(index, value);
        } else {
            stmt.setNull(index, Types.DATE);
        }
    }

    private void setIntegerOrNull(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value != null) {
            stmt.setInt(index, value);
        } else {
            stmt.setNull(index, Types.INTEGER);
        }
    }

    private void setBooleanOrNull(PreparedStatement stmt, int index, Boolean value) throws SQLException {
        if (value != null) {
            stmt.setBoolean(index, value);
        } else {
            stmt.setNull(index, Types.BOOLEAN);
            // Или, если Access не поддерживает BOOLEAN, используйте BIT:
            // stmt.setNull(index, Types.BIT);
        }
    }
    public void checkTableStructure() {
        String sql = "SELECT TOP 1 * FROM child_ipra";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("=== Структура таблицы child_ipra ===");
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnType = metaData.getColumnTypeName(i);
                boolean isNullable = metaData.isNullable(i) == ResultSetMetaData.columnNullable;

                System.out.printf("%d: %s (%s) %s%n",
                        i, columnName, columnType,
                        isNullable ? "NULL" : "NOT NULL");
            }
            System.out.println("=====================================");

        } catch (SQLException e) {
            System.err.println("Ошибка при проверке структуры таблицы: " + e.getMessage());
        }
    }
    public List<ChildModel> getAllChildrenTable() throws SQLException {
        List<ChildModel> children = new ArrayList<>();
        String sql = "SELECT * FROM child_ipra ORDER BY created_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ChildModel child = mapResultSetToChild(rs);
                children.add(child);
            }
        }
        System.out.println("Было загружено " + children.size() + " детей");
        return children;
    }
    public void printTableStructure() throws SQLException {
        String sql = "SELECT * FROM child_ipra WHERE 1=0"; // Получаем только структуру
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("=== Структура таблицы child_ipra ===");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(i + ": " + metaData.getColumnName(i) +
                        " (" + metaData.getColumnTypeName(i) + ")");
            }
            System.out.println("=====================================");
        }
    }
    public int getChildrenCount() {
        String sql = "SELECT COUNT(*) AS total FROM child_ipra";
        int count = 0;

        try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка получения количества детей: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }
    public List<ChildModel> searchChildren(String searchTerm) {
        List<ChildModel> children = new ArrayList<>();
        String sql = "SELECT * FROM child_ipra WHERE " +
                "last_name LIKE ? OR " +
                "first_name LIKE ? OR " +
                "patronymic LIKE ? OR " +
                "representative_name LIKE ? " +
                "ORDER BY last_name, first_name";

        try (PreparedStatement stmt = (PreparedStatement) connection.createStatement()) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ChildModel child = mapResultSetToChild(rs);
                children.add(child);
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Ошибка поиска детей: " + e.getMessage());
        }

        return children;
    }
    public ChildModel getChildById(int id) throws SQLException {
        String sql = "SELECT * FROM child_ipra WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToChild(rs);
            }
        }
        return null;
    }
    private ChildModel mapResultSetToChild(ResultSet rs) throws SQLException {
        ChildModel child = new ChildModel();

        child.setId(rs.getInt("id"));
        child.setLastName(rs.getString("last_name"));
        child.setFirstName(rs.getString("first_name"));
        child.setPatronymic(rs.getString("patronymic"));
        child.setBirthDate(rs.getDate("birth_date"));
        child.setGender(rs.getString("gender"));
        child.setResidence(rs.getString("residence"));
        child.setActualResidence(rs.getString("actual_residence"));
        child.setRepresentativeName(rs.getString("representative_name"));
        child.setRepresentativeResidence(rs.getString("representative_residence"));
        child.setRepresentativeActualResidence(rs.getString("representative_actual_residence"));
        child.setRepresentativePhone(rs.getString("representative_phone"));
        child.setExaminationNumber(rs.getInt("examination_number"));
        child.setExaminationDate(rs.getDate("examination_date"));
        child.setEducationPlace(rs.getString("education_place"));
        child.setEducationProgram(rs.getString("education_program"));
        child.setHealthLossDegree(rs.getString("health_loss_degree"));
        child.setHealthLossTerm(rs.getDate("health_loss_term"));
        child.setDisabilityReason(rs.getString("disability_reason"));
        child.setExpertDecisionAddition(rs.getString("expert_decision_addition"));
        child.setEducationConditions(rs.getString("education_conditions"));
        child.setFirstTime(rs.getBoolean("first_time"));
        child.setRepeat(rs.getBoolean("repeat"));
        child.setUntilEighteen(rs.getBoolean("until_eighteen"));
        child.setCreatedDate(rs.getDate("created_date"));

        // Для отладки выводим в консоль
        System.out.println("Загружен ребенок: ID=" + child.getId() +
                ", ФИО=" + child.getFullName() +
                ", Дата рождения=" + child.getBirthDate() +
                ", Степень утраты=" + child.getHealthLossDegree());

        return child;
    }
    public boolean updateChild(ChildModel child) {
        String sql = "UPDATE child_ipra SET " +
                "last_name = ?, first_name = ?, patronymic = ?, birth_date = ?, " +
                "gender = ?, residence = ?, actual_residence = ?, " +
                "representative_name = ?, representative_residence = ?, " +
                "representative_actual_residence = ?, representative_phone = ?, " +
                "examination_number = ?, examination_date = ?, education_place = ?, " +
                "education_program = ?, health_loss_degree = ?, health_loss_term = ?, " +
                "disability_reason = ?, expert_decision_addition = ?, " +
                "education_conditions = ?, first_time = ?, repeat = ?, until_eighteen = ? " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, child.getLastName());
            stmt.setString(2, child.getFirstName());
            stmt.setString(3, child.getPatronymic());
            stmt.setDate(4, child.getBirthDate());
            stmt.setString(5, child.getGender());
            stmt.setString(6, child.getResidence());
            stmt.setString(7, child.getActualResidence());
            stmt.setString(8, child.getRepresentativeName());
            stmt.setString(9, child.getRepresentativeResidence());
            stmt.setString(10, child.getRepresentativeActualResidence());
            stmt.setString(11, child.getRepresentativePhone());
            stmt.setObject(12, child.getExaminationNumber());
            stmt.setDate(13, child.getExaminationDate());
            stmt.setString(14, child.getEducationPlace());
            stmt.setString(15, child.getEducationProgram());
            stmt.setString(16, child.getHealthLossDegree());
            stmt.setDate(17, child.getHealthLossTerm());
            stmt.setString(18, child.getDisabilityReason());
            stmt.setString(19, child.getExpertDecisionAddition());
            stmt.setString(20, child.getEducationConditions());
            stmt.setBoolean(21, child.isFirstTime());
            stmt.setBoolean(22, child.isRepeat());
            stmt.setBoolean(23, child.isUntilEighteen());
            stmt.setInt(24, child.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error updating child: " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean childExists(String lastName, String firstName, String patronymic, Date birthDate) {
        String sql = "SELECT COUNT(*) FROM child_ipra WHERE " +
                "last_name = ? AND first_name = ? AND patronymic = ? AND birth_date = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, patronymic);
            stmt.setDate(4, birthDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка проверки существования ребенка: " + e.getMessage());
        }

        return false;
    }
    public boolean deleteChild(int childId) {
        String sql = "DELETE FROM child_ipra WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, childId);
            int affectedRows = stmt.executeUpdate();
            System.out.println("Удалено записей: " + affectedRows + " (ID: " + childId + ")");
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при удалении ребенка с ID=" + childId);
            System.err.println("SQL Error: " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
