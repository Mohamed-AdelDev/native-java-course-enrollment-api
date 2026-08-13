package com.coursemanagement.handler;

import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.implentation.InMemoryEnrollmentRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.security.AuthenticatedUser;
import com.coursemanagement.security.AuthenticationException;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.service.EnrollmentService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class EnrollmentHandler implements HttpHandler {

    private static final EnrollmentRepository enrollmentRepository =
            new InMemoryEnrollmentRepository();

    private static final EnrollmentService enrollmentService =
            new EnrollmentService(enrollmentRepository);

    private static final AuthenticationService authenticationService =
            new AuthenticationService(
                    RepositoryManager.studentRepository,
                    RepositoryManager.tokenRepository
            );

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method =
                HttpUtil.getRequestMethod(exchange);

        String path =
                HttpUtil.getRequestPath(exchange);

        try {

            if (!method.equals("GET")) {

                HttpUtil.sendResponse(exchange, 405);
                return;
            }

            AuthenticatedUser user =
                    authenticationService.authenticate(
                            HttpUtil.getHeader(
                                    exchange,
                                    "Authorization"
                            )
                    );

            if (path.equals("/api/enrollments")) {

                getMyEnrollments(exchange, user);

            } else if (path.matches(
                    "/api/enrollments/\\d+")) {

                getMyEnrollment(exchange, user);

            } else {

                HttpUtil.sendResponse(exchange, 404);
            }

        } catch (AuthenticationException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    401,
                    JsonUtil.error(e.getMessage())
            );

        } catch (IllegalArgumentException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    404,
                    JsonUtil.error(e.getMessage())
            );
        }
    }

    private void getMyEnrollments(
            HttpExchange exchange,
            AuthenticatedUser user
    ) throws IOException {

        List<EnrollmentResponse> responses =
                enrollmentService.findMyEnrollments(
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
            AuthenticatedUser user
    ) throws IOException {

        Long enrollmentId =
                HttpUtil.getIdFromPath(exchange);

        var response =
                enrollmentService.findMyEnrollment(
                        enrollmentId,
                        user.getUserId()
                );

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJson(response)
        );
    }
}