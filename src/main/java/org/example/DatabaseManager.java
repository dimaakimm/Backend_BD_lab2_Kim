package org.example;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "first";
    private static final String USER = "root"; // Укажите свой логин
    private static final String PASSWORD = "password"; // Укажите свой пароль

    private Connection conn;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            System.out.println("✅ Подключение к базе данных успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
    }

    public void createDatabase() {
        executeProcedure("{CALL create_database()}");
    }

    public void deleteDatabase() {
        executeProcedure("{CALL delete_database()}");
    }

    public void clearTable() {
        executeProcedure("{CALL clear_table()}");
    }

    public void addRecord(String name, int age) {
        try (CallableStatement stmt = conn.prepareCall("{CALL add_record(?, ?)}")) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.execute();
            System.out.println("✅ Запись добавлена");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления: " + e.getMessage());
        }
    }

    public void searchByName(String name) {
        try (CallableStatement stmt = conn.prepareCall("{CALL search_by_name(?)}")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("👤 Найдено: " + rs.getString("name") + ", " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
    }

    public void updateRecord(String oldName, String newName, int newAge) {
        try (CallableStatement stmt = conn.prepareCall("{CALL update_record(?, ?, ?)}")) {
            stmt.setString(1, oldName);
            stmt.setString(2, newName);
            stmt.setInt(3, newAge);
            stmt.execute();
            System.out.println("✅ Запись обновлена");
        } catch (SQLException e) {
            System.out.println("Ошибка обновления: " + e.getMessage());
        }
    }

    public void deleteByName(String name) {
        executeProcedureWithParam("{CALL delete_by_name(?)}", name);
    }

    public void createTable(String tableName) {
        executeProcedureWithParam("{CALL create_table(?)}", tableName);
    }

    public void dropTable(String tableName) {
        executeProcedureWithParam("{CALL drop_table(?)}", tableName);
    }

    private void executeProcedure(String sql) {
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.execute();
            System.out.println("✅ Операция выполнена");
        } catch (SQLException e) {
            System.out.println("Ошибка выполнения: " + e.getMessage());
        }
    }

    private void executeProcedureWithParam(String sql, String param) {
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, param);
            stmt.execute();
            System.out.println("✅ Операция выполнена");
        } catch (SQLException e) {
            System.out.println("Ошибка выполнения: " + e.getMessage());
        }
    }
}
