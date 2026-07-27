package com.coursemanagement.handler;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.model.enums.CourseStatus;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.InMemoryCourseRepository;
import com.coursemanagement.service.CourseService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class CourseHandler implements HttpHandler {

    private static final CourseRepository courseRepository =
            new InMemoryCourseRepository();

    private static final CourseService courseService =
            new CourseService(courseRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getRequestMethod(exchange);
        String path = HttpUtil.getRequestPath(exchange);

        switch (method) {

            case "GET":

                if (path.equals("/api/courses")) {
                    getAllCourses(exchange);

                } else if (path.matches("/api/courses/\\d+")) {
                    getCourse(exchange);

                } else {
                    HttpUtil.sendResponse(exchange, 404);
                }

                break;

            case "POST":
                createCourse(exchange);
                break;

            default:
                HttpUtil.sendResponse(exchange, 405);
        }
    }

    private void getAllCourses(HttpExchange exchange) throws IOException {

        List<CourseResponse> responses =
                courseService.findAllCourses();

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJsoncourses(responses)
        );
    }

    private void getCourse(HttpExchange exchange) throws IOException {

        try {

            Long id = HttpUtil.getIdFromPath(exchange);

            CourseResponse response =
                    courseService.findCourseById(id);

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

    private void createCourse(HttpExchange exchange) throws IOException {

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

            CreateCourseRequest request = new CreateCourseRequest(
                    JsonUtil.getString(body, "title"),
                    JsonUtil.getString(body, "description"),
                    JsonUtil.getDecimal(body, "price"),
                    JsonUtil.getInt(body, "capacity"),
                    JsonUtil.getEnum(body, "status", CourseStatus.class)
            );

            CourseResponse response =
                    courseService.createCourse(request);

            exchange.getResponseHeaders().add(
                    "Location",
                    "/api/courses/" + response.getId()
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
}