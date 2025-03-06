package org.example;

import javax.swing.*;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private Connection conn;
    private JTextArea outputArea;
    private String databaseName;

    public DatabaseManager(JTextArea outputArea, String databaseName) {
        this.outputArea = outputArea;
        this.databaseName = databaseName;
        if (databaseName != null && !databaseName.trim().isEmpty()) {
            connectToDatabase();
        } else {
            appendOutput("❌ Ошибка: База данных не выбрана.\n");
        }
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(URL + databaseName, USER, PASSWORD);
            appendOutput("✅ Подключение к базе данных " + databaseName + " успешно!\n");
        } catch (SQLException e) {
            appendOutput("❌ Ошибка подключения: " + e.getMessage() + "\n");
            conn = null;
        }
    }

    public static void createDatabase(String dbName) {
        if (dbName == null || dbName.trim().isEmpty()) {
            System.out.println("❌ Ошибка: Имя базы данных не указано.");
            return;
        }
        executeGeneralUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "`");
    }

    public static void deleteDatabase(String dbName) {
        if (dbName == null || dbName.trim().isEmpty()) {
            System.out.println("❌ Ошибка: Имя базы данных не указано.");
            return;
        }
        executeGeneralUpdate("DROP DATABASE IF EXISTS `" + dbName + "`");
    }

    public void listDatabases() {
        executeGeneralQuery("SHOW DATABASES");
    }

    public void listTables() {
        if (!isConnected()) return;
        executeQuery("SHOW TABLES");
    }

    public void createTable(String tableName) {
        if (!isConnected()) return;
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + tableName + "` (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description TEXT)");
    }

    public void dropTable(String tableName) {
        if (!isConnected()) return;
        executeUpdate("DROP TABLE IF EXISTS `" + tableName + "`");
    }

    public void clearTable(String tableName) {
        if (!isConnected()) return;
        executeUpdate("DELETE FROM `" + tableName + "`");
    }

    public void addRecord(String tableName, String name, String description) {
        if (!isConnected()) return;
        executeUpdate("INSERT INTO `" + tableName + "` (name, description) VALUES ('" + name + "', '" + description + "')");
    }

    public void searchByName(String tableName, String name) {
        if (!isConnected()) return;
        executeQuery("SELECT * FROM `" + tableName + "` WHERE name = '" + name + "'");
    }

    public void updateRecord(String tableName, String oldName, String newName, String description) {
        if (!isConnected()) return;
        executeUpdate("UPDATE `" + tableName + "` SET name = '" + newName + "', description = '" + description + "' WHERE name = '" + oldName + "'");
    }

    public void deleteByName(String tableName, String name) {
        if (!isConnected()) return;
        executeUpdate("DELETE FROM `" + tableName + "` WHERE name = '" + name + "'");
    }

    private boolean isConnected() {
        if (conn == null) {
            appendOutput("❌ Ошибка: Подключение к базе данных не установлено.\n");
            return false;
        }
        return true;
    }

    private void executeUpdate(String query) {
        if (!isConnected()) return;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            appendOutput("✅ Операция выполнена\n");
        } catch (SQLException e) {
            appendOutput("❌ Ошибка выполнения: " + e.getMessage() + "\n");
        }
    }

    private void executeQuery(String query) {
        if (!isConnected()) return;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appendOutput("🔹 " + rs.getString(1) + "\n");
            }
        } catch (SQLException e) {
            appendOutput("❌ Ошибка выполнения: " + e.getMessage() + "\n");
        }
    }

    private static void executeGeneralUpdate(String query) {
        try (Connection tempConn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = tempConn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("❌ Ошибка выполнения: " + e.getMessage());
        }
    }

    private void executeGeneralQuery(String query) {
        try (Connection tempConn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = tempConn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appendOutput("🔹 " + rs.getString(1) + "\n");
            }
        } catch (SQLException e) {
            appendOutput("❌ Ошибка выполнения: " + e.getMessage() + "\n");
        }
    }

    private void appendOutput(String message) {
        SwingUtilities.invokeLater(() -> outputArea.append(message));
    }
}