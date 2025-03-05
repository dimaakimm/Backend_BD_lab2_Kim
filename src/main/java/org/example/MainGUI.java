package org.example;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private DatabaseManager dbManager;
    private final JTextArea outputArea;
    private String selectedDatabase;

    public MainGUI() {
        setTitle("Database Manager");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(10, 1));

        JButton selectDbBtn = new JButton("Выбрать базу данных");
        JButton createDbBtn = new JButton("Создать базу данных");
        JButton deleteDbBtn = new JButton("Удалить базу данных");
        JButton clearTableBtn = new JButton("Очистить таблицу");
        JButton addRecordBtn = new JButton("Добавить запись");
        JButton searchBtn = new JButton("Поиск по имени");
        JButton updateBtn = new JButton("Обновить запись");
        JButton deleteBtn = new JButton("Удалить по имени");
        JButton createTableBtn = new JButton("Создать таблицу");
        JButton dropTableBtn = new JButton("Удалить таблицу");

        selectDbBtn.addActionListener(e -> {
            String dbName = JOptionPane.showInputDialog("Введите имя базы данных для работы:");
            if (dbName != null && !dbName.isEmpty()) {
                selectedDatabase = dbName;
                dbManager = new DatabaseManager(outputArea, selectedDatabase);
                outputArea.append("Выбрана база данных: " + dbName + "\n");
            }
        });

        createDbBtn.addActionListener(e -> {
            String dbName = JOptionPane.showInputDialog("Введите имя новой базы данных:");
            if (dbName != null && !dbName.isEmpty()) {
                DatabaseManager.createDatabase(dbName);
                outputArea.append("Создана база данных: " + dbName + "\n");
            }
        });

        deleteDbBtn.addActionListener(e -> {
            String dbName = JOptionPane.showInputDialog("Введите имя базы данных для удаления:");
            if (dbName != null && !dbName.isEmpty()) {
                DatabaseManager.deleteDatabase(dbName);
                outputArea.append("Удалена база данных: " + dbName + "\n");
            }
        });

        clearTableBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы для очистки:");
            if (tableName != null && !tableName.isEmpty()) {
                dbManager.clearTable(tableName);
            }
        });

        addRecordBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы:");
            if (tableName == null || tableName.isEmpty()) return;
            String name = JOptionPane.showInputDialog("Введите имя:");
            if (name == null || name.isEmpty()) return;
            String description = JOptionPane.showInputDialog("Введите описание:");
            if (description == null || description.isEmpty()) return;
            dbManager.addRecord(tableName, name, description);
        });

        searchBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы:");
            if (tableName != null && !tableName.isEmpty()) {
                String name = JOptionPane.showInputDialog("Введите имя для поиска:");
                dbManager.searchByName(tableName, name);
            }
        });

        updateBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы:");
            if (tableName == null || tableName.isEmpty()) return;
            String oldName = JOptionPane.showInputDialog("Введите текущее имя:");
            if (oldName == null || oldName.isEmpty()) return;
            String newName = JOptionPane.showInputDialog("Введите новое имя:");
            if (newName == null || newName.isEmpty()) return;
            String description = JOptionPane.showInputDialog("Введите новое описание:");
            if (description == null || description.isEmpty()) return;
            dbManager.updateRecord(tableName, oldName, newName, description);
        });

        deleteBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы:");
            if (tableName != null && !tableName.isEmpty()) {
                String name = JOptionPane.showInputDialog("Введите имя для удаления:");
                dbManager.deleteByName(tableName, name);
            }
        });

        createTableBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы:");
            if (tableName != null && !tableName.isEmpty()) {
                dbManager.createTable(tableName);
            }
        });

        dropTableBtn.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog("Введите имя таблицы для удаления:");
            if (tableName != null && !tableName.isEmpty()) {
                dbManager.dropTable(tableName);
            }
        });

        buttonPanel.add(selectDbBtn);
        buttonPanel.add(createDbBtn);
        buttonPanel.add(deleteDbBtn);
        buttonPanel.add(clearTableBtn);
        buttonPanel.add(addRecordBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(createTableBtn);
        buttonPanel.add(dropTableBtn);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
