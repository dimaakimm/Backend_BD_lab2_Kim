package org.example;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class CSVworker {
    private static String csvFilePath = "output.csv";
    private static String backupFilePath = "backup.csv";

    public static List<String> titles = List.of("id", "name", "number", "email");
    public static void createTable() {


        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {

            writer.println("id,name,number,email");
            System.out.println("CSV файл успешно создан.");
        } catch (IOException e) {
            System.err.println("Ошибка при создании CSV файла: " + e.getMessage());
        }
    }
    public static String readAndConvertToJSON() {
        List<Map<String, String>> data = new ArrayList<>();
        try {
            Reader in = new FileReader(csvFilePath); // Путь к вашему CSV файлу
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                for (String title : titles) {
                    row.put(title, record.get(title));
                }
                data.add(row);
            }
            Table tableProps = new Table(titles, data);
            Gson gson = new Gson();
            return gson.toJson(tableProps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }

    public static void createRow(String requestBody) {
        Gson gson = new Gson();
        AddRowProps addRowProps = gson.fromJson(requestBody, AddRowProps.class);

        int id;
        int number;
        try {
            id = Integer.parseInt(addRowProps.getId());
            number = Integer.parseInt(addRowProps.getNumber());

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка: ID и NUMBER должны принимать числовые значения.", e);
        }

        // Считываем существующие ID из CSV
        Set<Integer> existingIds = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > 0) {
                    try {
                        existingIds.add(Integer.parseInt(columns[0]));
                    } catch (NumberFormatException ignored) {
                        // Игнорируем строки с некорректным ID
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка при чтении CSV файла: " + e.getMessage(), e);
        }

        // Проверяем наличие ID
        if (existingIds.contains(id)) {
            throw new IllegalArgumentException("Ошибка: Строка с таким ID уже существует.");
        }

        // Добавляем новую строку
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath, true))) {
            String row = String.join(",", addRowProps.getId(), addRowProps.getName(), addRowProps.getNumber(), addRowProps.getEmail());
            writer.println(row);
            System.out.println("Новая строка успешно добавлена в CSV файл.");
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка при записи в CSV файл: " + e.getMessage(), e);
        }
    }
    public static void deleteRow(String requestBody) {
        Gson gson = new Gson();
        SelectDeleteRowProps deleteRowProps = gson.fromJson(requestBody, SelectDeleteRowProps.class);

        // Создаем временный список для хранения строк
        List<String> updatedLines = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String clearedLine = clearStringIf(line, deleteRowProps.getValue(), deleteRowProps.getTitleIndex());
                updatedLines.add(clearedLine);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении CSV файла: " + e.getMessage());
            return;
        }

        // Записываем обновленные строки обратно в исходный файл
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {
            for (String updatedLine : updatedLines) {
                writer.println(updatedLine);
            }

            System.out.println("Строка успешно удалена из CSV файла.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в CSV файл: " + e.getMessage());
        }
    }
    public static String select(String requestBody) {
        Gson gson = new Gson();
        SelectDeleteRowProps selectRowProps = gson.fromJson(requestBody, SelectDeleteRowProps.class);
        List<Map<String, String>> selectedData = new ArrayList<>();

        try {
            Reader in = new FileReader(csvFilePath);
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                boolean matchCondition = false;

                for (String title : titles) {
                    String cellValue = record.get(title);
                    if (title.equals(selectRowProps.getTitle()) && cellValue.equals(selectRowProps.getValue())) {
                        matchCondition = true;
                    }
                    row.put(title, cellValue);
                }

                if (matchCondition) {
                    selectedData.add(row);
                }
            }

            // JSON объект с заголовками и данными
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("titles", titles);
            jsonResponse.put("data", selectedData);

            return gson.toJson(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "[]";
    }
    public static void saveTable() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(backupFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("CSV файл успешно скопирован.");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании CSV файла: " + e.getMessage());
        }
    }
    public static void clearTable() {
        try (RandomAccessFile raf = new RandomAccessFile(csvFilePath, "rw")) {
            String header = raf.readLine(); // Читаем первую строку
            if (header == null || header.isEmpty()) {
                System.err.println("Ошибка: CSV файл пуст или не содержит заголовка.");
                return;
            }
            raf.setLength(0); // Очищаем файл
            raf.write((header + System.lineSeparator()).getBytes()); // Записываем обратно только заголовок
            System.out.println("Все строки в CSV файле успешно очищены, кроме первой.");
        } catch (IOException e) {
            System.err.println("Ошибка при очистке CSV файла: " + e.getMessage());
        }
    }
    public static void getFromBackup(){
        try (BufferedReader reader = new BufferedReader(new FileReader(backupFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("CSV файл успешно скопирован.");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании CSV файла: " + e.getMessage());
        }
    }
    public static void deleteTable() throws IOException {
        File file = new File(csvFilePath);
        if (file.exists()) { // Проверяем, существует ли файл
            if (file.delete()) {
                System.out.println("Файл успешно удален: " + csvFilePath);
            } else {
                throw new IOException("Не удалось удалить файл: " + csvFilePath);
            }
        } else {
            System.err.println("Файл не существует: " + csvFilePath);
        }
    }
    public static void editRow(String requestBody) throws IOException {
        Gson gson = new Gson();
        // Преобразуем строку запроса в объект
        EditRowProps props = gson.fromJson(requestBody, EditRowProps.class);
        // Получаем карту ненулевых полей
        Map<String, String> nonNullFields = props.getNonNullFields();
        int index = Integer.parseInt(props.getIndex()) + 2;  // Индекс строки для редактирования

        // Создаем временный список для хранения всех строк
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line); // Читаем все строки в список
            }

            // Проверяем, что индекс строки существует в файле
            int nonEmptyRowCount = 0;
            boolean rowFound = false;

            // Обрабатываем строки
            for (int i = 0; i < lines.size(); i++) {
                String currentLine = lines.get(i);

                if (!currentLine.trim().isEmpty()) { // Пропускаем пустые строки
                    nonEmptyRowCount++;
                    if (nonEmptyRowCount == index) {
                        // Строка найдена, обновляем её
                        String[] columns = currentLine.split(",");

                        // Обновляем только те поля, которые не null
                        if (nonNullFields.containsKey("id")) {
                            columns[0] = nonNullFields.get("id");  // Обновляем поле id
                        }
                        if (nonNullFields.containsKey("name")) {
                            columns[1] = nonNullFields.get("name");  // Обновляем поле name
                        }
                        if (nonNullFields.containsKey("number")) {
                            columns[2] = nonNullFields.get("number");  // Обновляем поле number
                        }
                        if (nonNullFields.containsKey("email")) {
                            columns[3] = nonNullFields.get("email");  // Обновляем поле email
                        }

                        // Перезаписываем строку в новый формат
                        lines.set(i, String.join(",", columns));
                        rowFound = true;
                        break;
                    }
                }
            }

            // Если строка не найдена
            if (!rowFound) {
                throw new IOException("Не удалось найти строку с индексом " + index);
            }

            // Перезаписываем все строки обратно в файл
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
                System.out.println("Строка успешно обновлена.");
            }

        } catch (IOException e) {
            throw new IOException("Ошибка при обработке файла: " + e.getMessage());
        }
    }
    public static boolean exist() {
        File file = new File(csvFilePath);
        return file.exists(); // Проверяем, существует ли файл
    }
    public static String clearStringIf(String input, String value, int index) {
        String[] elements = input.split(",");
        if (elements.length >= 3 && value.equals(elements[index].trim())) {
            return "";
        } else {
            return input;
        }
    }

}