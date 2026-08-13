package com.coursemanagement.handler;

import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.repository.implentation.InMemoryStudentRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.service.StudentService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class StudentHandler implements HttpHandler {

    private static final StudentRepository studentRepository =
            RepositoryManager.studentRepository;

    private static final StudentService studentService =
            new StudentService(studentRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getRequestMethod(exchange);
        String path = HttpUtil.getRequestPath(exchange);

        switch (method) {

            case "GET":

                if (path.equals("/api/students")) {
                    getAllStudents(exchange);

                } else if (path.matches("/api/students/\\d+")) {
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

        try {


            String body = HttpUtil.getRequestBody(exchange);

            if (!JsonUtil.isValidJson(body)) {
                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error("Invalid JSON")
                );
                return;
            }

            RegisterStudentRequest request = new RegisterStudentRequest(
                    JsonUtil.getString(body, "fullName"),
                    JsonUtil.getString(body, "email"),
                    JsonUtil.getString(body, "password")
            );

            StudentResponse response =
                    studentService.registerStudent(request);

            exchange.getResponseHeaders().add(
                    "Location",
                    "/api/students/" + response.getId()
            );

            HttpUtil.sendJsonResponse(exchange, 201, JsonUtil.toJson(response));

        } catch (IllegalArgumentException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    400,
                    JsonUtil.error(e.getMessage())
            );

        } catch (Exception e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    400,
                    JsonUtil.error("Invalid JSON")
            );
        }
    }

    private void getStudent(HttpExchange exchange) throws IOException {

        try {

            Long id = HttpUtil.getIdFromPath(exchange);

            StudentResponse response =
                    studentService.findStudentById(id);

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    JsonUtil.toJson(response)
            );

        } catch (IllegalArgumentException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    404,
                    JsonUtil.error(e.getMessage())
            );
        }
    }

    private void getAllStudents(HttpExchange exchange) throws IOException {

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJsonstudents(
                        studentService.findAllStudents()
                )
        );
    }
}