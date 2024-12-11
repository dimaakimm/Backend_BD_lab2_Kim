package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHttpServer {
    private static final Logger logger = Logger.getLogger(SimpleHttpServer.class.getName());

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/get-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Устанавливаем заголовки CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                try {
                    String jsonData = CSVworker.readAndConvertToJSON();
                    sendJsonResponse(exchange, 200, jsonData);
                }
                catch (Exception e) {
                    sendJsonResponse(exchange, 200, e.getMessage());
                }

            }
        });
        server.createContext("/add-row", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }

                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.createRow(requestBody);
                    sendJsonResponse(exchange, 200, requestBody);

                }
                catch (NumberFormatException e) {

                    exchange.sendResponseHeaders(403, e.getMessage().getBytes().length); // Устанавливаем длину сообщения
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(e.getMessage().getBytes()); // Отправляем сообщение об ошибке
                    }
                }catch (IllegalArgumentException e){
                    exchange.sendResponseHeaders(403, e.getMessage().getBytes().length); // Устанавливаем длину сообщения
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(e.getMessage().getBytes()); // Отправляем сообщение об ошибке
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }


            }
        });
        server.createContext("/delete-row", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }

                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.deleteRow(requestBody);
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }


            }
        });
        server.createContext("/show-rows", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }

                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    String jsonData = CSVworker.select(requestBody);
                    sendJsonResponse(exchange, 200, jsonData);
                }
                catch (NumberFormatException e) {
                    String errorMessage = "Error with finding rows!";
                    exchange.sendResponseHeaders(403, errorMessage.getBytes().length); // Устанавливаем длину сообщения
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorMessage.getBytes()); // Отправляем сообщение об ошибке
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/save-backup-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.saveTable();
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/clear-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.clearTable();
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/set-backup-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.getFromBackup();
                    sendJsonResponse(exchange, 200, requestBody);
                }

                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/create-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.createTable();
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/delete-table", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.deleteTable();
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (IOException e) {
                    String errorMessage = "Error with deleting table!";
                    exchange.sendResponseHeaders(403, errorMessage.getBytes().length); // Устанавливаем длину сообщения
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorMessage.getBytes()); // Отправляем сообщение об ошибке
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/exist", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }
                try {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    if (CSVworker.exist()){
                        Gson gson = new Gson();
                        Map<String, Integer> responseMap = new HashMap<>();
                        responseMap.put("exist", 1);
                        String jsonResponse = gson.toJson(responseMap);
                        sendJsonResponse(exchange, 200, jsonResponse);
                    }
                    else {
                        sendJsonResponse(exchange, 403, requestBody);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }
            }
        });
        server.createContext("/edit-row", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Установка заголовков CORS
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(200, -1); // Возвращаем успешный статус для OPTIONS запроса
                    return;
                }

                // Получение тела запроса JSON
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                try {
                    CSVworker.editRow(requestBody);
                    sendJsonResponse(exchange, 200, requestBody);
                }
                catch (Exception e) {
                    e.printStackTrace(); // Обработка других исключений
                }


            }
        });
        server.start();
        logger.info("Server started on port 8000");

        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            logger.log(Level.SEVERE, "Uncaught exception in thread: " + thread.getName(), throwable);
        });
    }


    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonData.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonData.getBytes());
        os.close();
    }

}
