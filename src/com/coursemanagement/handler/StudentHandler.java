package com.coursemanagement.handler;

import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.repository.InMemoryStudentRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.service.StudentService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class StudentHandler implements HttpHandler {

    private final StudentRepository studentRepository = new InMemoryStudentRepository();

    private final StudentService studentService =
            new StudentService(studentRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getRequestMethod(exchange);
        String path = HttpUtil.getRequestPath(exchange);

        switch (method) {

            case "GET":
                if (path.matches("/api/students/\\d+")) {
                    getStudent(exchange);
                } else {
                    HttpUtil.sendResponse(exchange, 404);
                }
                break;

            case "POST":
                createStudent(exchange);
                break;

            default:
                HttpUtil.sendResponse(exchange, 405);
        }

    }
    private void createStudent(HttpExchange exchange) throws IOException {

        String body = HttpUtil.getRequestBody(exchange);

        RegisterStudentRequest request = new RegisterStudentRequest(
                JsonUtil.getString(body, "fullName"),
                JsonUtil.getString(body, "email"),
                JsonUtil.getString(body, "password")
        );
        StudentResponse response =
                studentService.registerStudent(request);

        String json = JsonUtil.toJson(response);

        HttpUtil.sendJsonResponse(exchange, 201, json);
    }

    private void getStudent(HttpExchange exchange) throws IOException {

        try {

            Long id = HttpUtil.getIdFromPath(exchange);

            StudentResponse response =
                    studentService.findStudentById(id);

            String json = JsonUtil.toJson(response);

            HttpUtil.sendJsonResponse(exchange, 200, json);

        } catch (IllegalArgumentException e) {

            HttpUtil.sendJsonResponse(exchange, 404, """
                {
                  "error":"Student not found"
                }
                """);
        }
    }
}