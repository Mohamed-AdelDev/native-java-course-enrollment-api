package com.coursemanagement.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    public static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    public static void sendResponse(HttpExchange exchange,
                                    int statusCode) throws IOException {

        exchange.sendResponseHeaders(statusCode, -1);
        exchange.close();
    }

    public static String getRequestMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    public static String getRequestPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    public static String getRequestBody(HttpExchange exchange) throws IOException {

        return new String(exchange.getRequestBody().readAllBytes());
    }

    public static String getHeader(HttpExchange exchange, String headerName) {

        return exchange.getRequestHeaders().getFirst(headerName);
    }

    public static String getQuery(HttpExchange exchange) {

        return exchange.getRequestURI().getQuery();
    }

    public static Long getIdFromPath(HttpExchange exchange) {

        String path = getRequestPath(exchange);

        String[] parts = path.split("/");

        return Long.parseLong(parts[3]);
    }
    public static String getQueryParameter(HttpExchange exchange, String key) {

        String query = getQuery(exchange);

        if (query == null) {
            return null;
        }

        String[] parameters = query.split("&");

        for (String parameter : parameters) {

            String[] pair = parameter.split("=");

            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }

        return null;
    }
}