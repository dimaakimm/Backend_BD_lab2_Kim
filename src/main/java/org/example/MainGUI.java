package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();

    public MainGUI() {
        setTitle("Database Manager");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1)); // Увеличено для новых кнопок

        JButton createDbBtn = new JButton("Создать базу данных");
        JButton deleteDbBtn = new JButton("Удалить базу данных");
        JButton clearTableBtn = new JButton("Очистить таблицу");
        JButton addRecordBtn = new JButton("Добавить запись");
        JButton searchBtn = new JButton("Поиск по имени");
        JButton updateBtn = new JButton("Обновить запись");
        JButton deleteBtn = new JButton("Удалить по имени");
        JButton createTableBtn = new JButton("Создать таблицу");
        JButton dropTableBtn = new JButton("Удалить таблицу");

        createDbBtn.addActionListener(e -> dbManager.createDatabase());
        deleteDbBtn.addActionListener(e -> dbManager.deleteDatabase());
        clearTableBtn.addActionListener(e -> dbManager.clearTable());

        addRecordBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Введите имя:");
            if (name == null || name.isEmpty()) return;
            String ageStr = JOptionPane.showInputDialog("Введите возраст:");
            if (ageStr == null || ageStr.isEmpty()) return;
            try {
                int age = Integer.parseInt(ageStr);
                dbManager.addRecord(name, age);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: возраст должен быть числом!");
            }
        });

        searchBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Введите имя для поиска:");
            if (name != null && !name.isEmpty()) {
                dbManager.searchByName(name);
            }
        });

        updateBtn.addActionListener(e -> {
            String oldName = JOptionPane.showInputDialog("Введите текущее имя:");
            if (oldName == null || oldName.isEmpty()) return;
            String newName = JOptionPane.showInputDialog("Введите новое имя:");
            if (newName == null || newName.isEmpty()) return;
            String ageStr = JOptionPane.showInputDialog("Введите новый возраст:");
            if (ageStr == null || ageStr.isEmpty()) return;
            try {
                int age = Integer.parseInt(ageStr);
                dbManager.updateRecord(oldName, newName, age);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: возраст должен быть числом!");
            }
        });

        deleteBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Введите имя для удаления:");
            if (name != null && !name.isEmpty()) {
                dbManager.deleteByName(name);
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

        add(createDbBtn);
        add(deleteDbBtn);
        add(clearTableBtn);
        add(addRecordBtn);
        add(searchBtn);
        add(updateBtn);
        add(deleteBtn);
        add(createTableBtn);
        add(dropTableBtn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
