package com.coursemanagement.handler;

import com.coursemanagement.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HealthHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!HttpUtil.getRequestMethod(exchange).equals("GET")) {
            HttpUtil.sendResponse(exchange, 405);
            return;
        }

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                """
                {
                  "status": "UP",
                  "application": "Course Enrollment API"
                }
                """
        );
    }
}