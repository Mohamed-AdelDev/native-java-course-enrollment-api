package com.coursemanagement.handler;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.event.EventPublisher;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.PaymentRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.service.EnrollmentFacade;
import com.coursemanagement.service.EnrollmentService;
import com.coursemanagement.service.PaymentService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PaymentHandler implements HttpHandler {

    private static final PaymentRepository paymentRepository =
            RepositoryManager.paymentRepository;

    private static final EnrollmentRepository enrollmentRepository =
            RepositoryManager.enrollmentRepository;

    private static final CourseRepository courseRepository =
            RepositoryManager.courseRepository;

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
    public void handle(HttpExchange exchange)
            throws IOException {

        try {

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
                    && path.matches(
                    "/api/enrollments/\\d+/payments")) {

                createPayment(exchange);
                return;
            }

            HttpUtil.sendResponse(exchange, 405);

        } catch (Exception e) {

            com.coursemanagement.exception.GlobalExceptionHandler
                    .handle(exchange, e);
        }
    }

    private void createPayment(
            HttpExchange exchange) throws IOException {

        Long enrollmentId =
                getEnrollmentId(exchange);

        String body =
                HttpUtil.getRequestBody(exchange);

        if (!JsonUtil.isValidJson(body)) {

            throw new com.coursemanagement.exception.InvalidJsonException(
                    "Invalid JSON"
            );
        }

        String paymentMethod =
                JsonUtil.getString(
                        body,
                        "paymentMethod"
                );

        String paymentReference =
                JsonUtil.getString(
                        body,
                        "paymentReference"
                );

        if (paymentMethod == null
                || paymentMethod.isBlank()) {

            throw new com.coursemanagement.exception.ValidationException(
                    "paymentMethod is required"
            );
        }

        if (paymentReference == null
                || paymentReference.isBlank()) {

            throw new com.coursemanagement.exception.ValidationException(
                    "paymentReference is required"
            );
        }

        CreatePaymentRequest request =
                new CreatePaymentRequest(
                        paymentMethod,
                        paymentReference
                );

        PaymentResponse response =
                enrollmentFacade.payEnrollment(
                        enrollmentId,
                        request
                );

        HttpUtil.sendJsonResponse(
                exchange,
                201,
                JsonUtil.toJson(response)
        );
    }

    private Long getEnrollmentId(
            HttpExchange exchange) {

        String path =
                HttpUtil.getRequestPath(exchange);

        String[] parts =
                path.split("/");

        return Long.parseLong(parts[3]);
    }
}