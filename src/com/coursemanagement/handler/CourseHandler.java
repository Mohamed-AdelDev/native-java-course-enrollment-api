package com.coursemanagement.handler;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.model.enums.CourseStatus;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.implentation.InMemoryCourseRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.security.AuthenticationException;
import com.coursemanagement.security.ForbiddenException;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.service.CourseService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class CourseHandler implements HttpHandler {

    private static final CourseRepository courseRepository =
            RepositoryManager.courseRepository;

    private static final CourseService courseService =
            new CourseService(courseRepository);

    private static final AuthenticationService authenticationService =
            new AuthenticationService(
                    RepositoryManager.studentRepository,
                    RepositoryManager.tokenRepository
            );

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getRequestMethod(exchange);
        String path = HttpUtil.getRequestPath(exchange);

        try {

            String authorization =
                    HttpUtil.getHeader(exchange, "Authorization");


            if (method.equals("GET")) {


                authenticationService.authenticate(authorization);

                if (path.equals("/api/courses")) {

                    getAllCourses(exchange);

                } else if (path.matches("/api/courses/\\d+")) {

                    getCourse(exchange);

                } else {

                    HttpUtil.sendJsonResponse(
                            exchange,
                            404,
                            JsonUtil.error("Endpoint not found")
                    );
                }

                return;
            }


            if (method.equals("POST")) {


                authenticationService.requireAdmin(authorization);

                if (path.equals("/api/courses")) {

                    createCourse(exchange);

                } else {

                    HttpUtil.sendJsonResponse(
                            exchange,
                            404,
                            JsonUtil.error("Endpoint not found")
                    );
                }

                return;
            }


            if (method.equals("PUT")) {


                authenticationService.requireAdmin(authorization);

                if (path.matches("/api/courses/\\d+")) {

                    replaceCourse(exchange);

                } else {

                    HttpUtil.sendJsonResponse(
                            exchange,
                            404,
                            JsonUtil.error("Endpoint not found")
                    );
                }

                return;
            }

            if (method.equals("PATCH")) {


                authenticationService.requireAdmin(authorization);

                if (path.matches("/api/courses/\\d+/status")) {

                    updateCourseStatus(exchange);

                } else {

                    HttpUtil.sendJsonResponse(
                            exchange,
                            404,
                            JsonUtil.error("Endpoint not found")
                    );
                }

                return;
            }



            if (method.equals("DELETE")) {


                authenticationService.requireAdmin(authorization);

                if (path.matches("/api/courses/\\d+")) {

                    deleteCourse(exchange);

                } else {

                    HttpUtil.sendJsonResponse(
                            exchange,
                            404,
                            JsonUtil.error("Endpoint not found")
                    );
                }

                return;
            }


            HttpUtil.sendResponse(exchange, 405);

        } catch (AuthenticationException e) {


            HttpUtil.sendJsonResponse(
                    exchange,
                    401,
                    JsonUtil.error(e.getMessage())
            );

        } catch (ForbiddenException e) {


            HttpUtil.sendJsonResponse(
                    exchange,
                    403,
                    JsonUtil.error(e.getMessage())
            );
        }
    }

    private void getAllCourses(
            HttpExchange exchange
    ) throws IOException {

        try {

            String statusValue =
                    HttpUtil.getQueryParameter(exchange, "status");

            String title =
                    HttpUtil.getQueryParameter(exchange, "title");

            String minPriceValue =
                    HttpUtil.getQueryParameter(exchange, "minPrice");

            String maxPriceValue =
                    HttpUtil.getQueryParameter(exchange, "maxPrice");

            String sort =
                    HttpUtil.getQueryParameter(exchange, "sort");


            CourseStatus status = null;

            if (statusValue != null) {

                try {

                    status = CourseStatus.valueOf(
                            statusValue.toUpperCase()
                    );

                } catch (IllegalArgumentException e) {

                    throw new IllegalArgumentException(
                            "Invalid status parameter"
                    );
                }
            }


            BigDecimal minPrice = null;

            if (minPriceValue != null) {

                try {

                    minPrice = new BigDecimal(minPriceValue);

                } catch (NumberFormatException e) {

                    throw new IllegalArgumentException(
                            "Invalid minPrice parameter"
                    );
                }
            }


            BigDecimal maxPrice = null;

            if (maxPriceValue != null) {

                try {

                    maxPrice = new BigDecimal(maxPriceValue);

                } catch (NumberFormatException e) {

                    throw new IllegalArgumentException(
                            "Invalid maxPrice parameter"
                    );
                }
            }
            List<CourseResponse> responses =
                    courseService.findCourses(
                            status,
                            title,
                            minPrice,
                            maxPrice,
                            sort
                    );

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    JsonUtil.toJsoncourses(responses)
            );

        } catch (IllegalArgumentException e) {


            HttpUtil.sendJsonResponse(
                    exchange,
                    400,
                    JsonUtil.error(e.getMessage())
            );
        }
    }


    private void getCourse(
            HttpExchange exchange
    ) throws IOException {

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
                    JsonUtil.error("Course not found")
            );
        }
    }


    private void createCourse(
            HttpExchange exchange
    ) throws IOException {

        try {

            String body =
                    HttpUtil.getRequestBody(exchange);


            if (!JsonUtil.isValidJson(body)) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error("Invalid JSON")
                );

                return;
            }

            CreateCourseRequest request =
                    buildCourseRequest(body);

            CourseResponse response =
                    courseService.createCourse(request);


            exchange.getResponseHeaders().add(
                    "Location",
                    "/api/courses/" + response.getId()
            );

            HttpUtil.sendJsonResponse(
                    exchange,
                    201,
                    JsonUtil.toJson(response)
            );

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


    private void replaceCourse(
            HttpExchange exchange
    ) throws IOException {

        try {

            String body =
                    HttpUtil.getRequestBody(exchange);


            if (!JsonUtil.isValidJson(body)) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error("Invalid JSON")
                );

                return;
            }

            Long id =
                    HttpUtil.getIdFromPath(exchange);

            CreateCourseRequest request =
                    buildCourseRequest(body);

            CourseResponse response =
                    courseService.replaceCourse(
                            id,
                            request
                    );

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    JsonUtil.toJson(response)
            );

        } catch (IllegalArgumentException e) {

            if ("Course not found".equals(e.getMessage())) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        404,
                        JsonUtil.error("Course not found")
                );

            } else {


                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error(e.getMessage())
                );
            }

        } catch (Exception e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    400,
                    JsonUtil.error("Invalid JSON")
            );
        }
    }


    private void updateCourseStatus(
            HttpExchange exchange
    ) throws IOException {

        try {

            String body =
                    HttpUtil.getRequestBody(exchange);

            if (!JsonUtil.isValidJson(body)) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error("Invalid JSON")
                );

                return;
            }

            Long id =
                    HttpUtil.getIdFromPath(exchange);

            UpdateCourseStatusRequest request =
                    new UpdateCourseStatusRequest(
                            JsonUtil.getEnum(
                                    body,
                                    "status",
                                    CourseStatus.class
                            )
                    );

            CourseResponse response =
                    courseService.updateCourseStatus(
                            id,
                            request
                    );

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    JsonUtil.toJson(response)
            );

        } catch (IllegalArgumentException e) {

            if ("Course not found".equals(e.getMessage())) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        404,
                        JsonUtil.error("Course not found")
                );

            } else {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error(e.getMessage())
                );
            }

        } catch (Exception e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    400,
                    JsonUtil.error("Invalid JSON")
            );
        }
    }


    private void deleteCourse(
            HttpExchange exchange
    ) throws IOException {

        try {

            Long id =
                    HttpUtil.getIdFromPath(exchange);

            courseService.deleteCourse(id);


            HttpUtil.sendResponse(
                    exchange,
                    204
            );

        } catch (IllegalArgumentException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    404,
                    JsonUtil.error("Course not found")
            );
        }
    }


    private CreateCourseRequest buildCourseRequest(
            String body
    ) {

        return new CreateCourseRequest(

                JsonUtil.getString(
                        body,
                        "title"
                ),

                JsonUtil.getString(
                        body,
                        "description"
                ),

                JsonUtil.getDecimal(
                        body,
                        "price"
                ),

                JsonUtil.getInt(
                        body,
                        "capacity"
                ),

                JsonUtil.getEnum(
                        body,
                        "status",
                        CourseStatus.class
                )
        );
    }
}