package com.coursemanagement.handler;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.PaymentRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.security.AuthenticatedUser;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.service.EnrollmentFacade;
import com.coursemanagement.service.EnrollmentService;
import com.coursemanagement.service.PaymentService;
import com.coursemanagement.event.EventPublisher;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class EnrollmentHandler implements HttpHandler {

    private static final EnrollmentRepository enrollmentRepository =
            RepositoryManager.enrollmentRepository;

    private static final CourseRepository courseRepository =
            RepositoryManager.courseRepository;

    private static final PaymentRepository paymentRepository =
            RepositoryManager.paymentRepository;

    private static final EnrollmentService enrollmentService =
            new EnrollmentService(
                    enrollmentRepository,
                    courseRepository
            );

    private static final PaymentService paymentService =
            new PaymentService(
                    paymentRepository,
                    enrollmentRepository,
                    courseRepository
            );

    private static final EnrollmentFacade enrollmentFacade =
            new EnrollmentFacade(
                    enrollmentService,
                    paymentService,
                    enrollmentRepository,
                    EventPublisher.getInstance()
            );

    private static final AuthenticationService authenticationService =
            new AuthenticationService(
                    RepositoryManager.studentRepository,
                    RepositoryManager.tokenRepository
            );

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            AuthenticatedUser user =
                    authenticationService.authenticate(
                            HttpUtil.getHeader(
                                    exchange,
                                    "Authorization"
                            )
                    );

            String method =
                    HttpUtil.getRequestMethod(exchange);

            String path =
                    HttpUtil.getRequestPath(exchange);

            if (method.equals("POST")
                    && path.equals("/api/enrollments")) {

                createEnrollment(exchange, user);
                return;
            }

            if (method.equals("GET")
                    && path.equals("/api/enrollments")) {

                getMyEnrollments(exchange, user);
                return;
            }

            if (method.equals("GET")
                    && path.matches(
                    "/api/enrollments/\\d+")) {

                getMyEnrollment(exchange, user);
                return;
            }

            if (method.equals("GET")
                    && path.matches(
                    "/api/students/\\d+/enrollments")) {

                getStudentEnrollments(exchange, user);
                return;
            }

            if (method.equals("DELETE")
                    && path.matches(
                    "/api/enrollments/\\d+")) {

                deleteEnrollment(exchange, user);
                return;
            }

            HttpUtil.sendResponse(exchange, 405);

        } catch (Exception e) {

            com.coursemanagement.exception.GlobalExceptionHandler
                    .handle(exchange, e);
        }
    }

    private void createEnrollment(
            HttpExchange exchange,
            AuthenticatedUser user) throws IOException {

        String body =
                HttpUtil.getRequestBody(exchange);

        if (!JsonUtil.isValidJson(body)) {
            throw new com.coursemanagement.exception.InvalidJsonException(
                    "Invalid JSON"
            );
        }

        Integer studentIdValue =
                JsonUtil.getInt(body, "studentId");

        Integer courseIdValue =
                JsonUtil.getInt(body, "courseId");

        String discountType =
                JsonUtil.getString(body, "discountType");

        if (studentIdValue == null) {
            throw new com.coursemanagement.exception.ValidationException(
                    "studentId is required"
            );
        }

        if (courseIdValue == null) {
            throw new com.coursemanagement.exception.ValidationException(
                    "courseId is required"
            );
        }

        if (discountType == null
                || discountType.isBlank()) {

            throw new com.coursemanagement.exception.ValidationException(
                    "discountType is required"
            );
        }

        Long studentId =
                studentIdValue.longValue();

        Long courseId =
                courseIdValue.longValue();

        if (!user.getUserId().equals(studentId)) {

            throw new com.coursemanagement.security.ForbiddenException(
                    "You cannot create enrollment for another student"
            );
        }

        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(
                        studentId,
                        courseId,
                        discountType
                );

        EnrollmentResponse response =
                enrollmentFacade.createEnrollment(request);

        HttpUtil.sendJsonResponse(
                exchange,
                201,
                JsonUtil.toJson(response)
        );
    }

    private void getMyEnrollments(
            HttpExchange exchange,
            AuthenticatedUser user) throws IOException {

        List<EnrollmentResponse> responses =
                enrollmentFacade.getMyEnrollments(
                        user.getUserId()
                );

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJsonEnrollments(responses)
        );
    }

    private void getMyEnrollment(
            HttpExchange exchange,
            AuthenticatedUser user) throws IOException {

        Long enrollmentId =
                HttpUtil.getIdFromPath(exchange);

        EnrollmentResponse response =
                enrollmentFacade.getMyEnrollment(
                        enrollmentId,
                        user.getUserId()
                );

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJson(response)
        );
    }

    private void getStudentEnrollments(
            HttpExchange exchange,
            AuthenticatedUser user) throws IOException {

        String path =
                HttpUtil.getRequestPath(exchange);

        String[] parts =
                path.split("/");

        Long studentId =
                Long.parseLong(parts[3]);

        if (!user.getUserId().equals(studentId)) {

            throw new com.coursemanagement.security.ForbiddenException(
                    "You cannot access another student's enrollments"
            );
        }

        List<EnrollmentResponse> responses =
                enrollmentFacade.getMyEnrollments(studentId);

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJsonEnrollments(responses)
        );
    }

    private void deleteEnrollment(
            HttpExchange exchange,
            AuthenticatedUser user) throws IOException {

        Long enrollmentId =
                HttpUtil.getIdFromPath(exchange);

        enrollmentFacade.getMyEnrollment(
                enrollmentId,
                user.getUserId()
        );

        enrollmentFacade.deleteEnrollment(
                enrollmentId
        );

        HttpUtil.sendResponse(
                exchange,
                204
        );
    }
}