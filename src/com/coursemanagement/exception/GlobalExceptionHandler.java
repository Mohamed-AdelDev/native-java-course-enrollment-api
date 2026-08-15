package com.coursemanagement.exception;

import com.coursemanagement.security.AuthenticationException;
import com.coursemanagement.security.ForbiddenException;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.LocalDateTime;

public class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    public static void handle(
            HttpExchange exchange,
            Exception exception) throws IOException {

        int status = 500;
        String error = "Internal Server Error";
        String message = "An unexpected error occurred";
        Object details = null;

        if (exception instanceof InvalidJsonException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof ValidationException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof DuplicateEnrollmentException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof CourseNotAvailableException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof UnsupportedPaymentMethodException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof AuthenticationException) {

            status = 401;
            error = "Unauthorized";
            message = exception.getMessage();

        } else if (exception instanceof ForbiddenException) {

            status = 403;
            error = "Forbidden";
            message = exception.getMessage();

        } else if (exception instanceof ResourceNotFoundException) {

            status = 404;
            error = "Not Found";
            message = exception.getMessage();

        } else if (exception instanceof MethodNotAllowedException) {

            status = 405;
            error = "Method Not Allowed";
            message = exception.getMessage();

        } else if (exception instanceof IllegalArgumentException) {

            status = 400;
            error = "Bad Request";
            message = exception.getMessage();

        } else if (exception instanceof IOException) {

            status = 500;
            error = "Internal Server Error";
            message = "An internal server error occurred";
        }

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        status,
                        error,
                        message,
                        HttpUtil.getRequestPath(exchange),
                        details
                );

        HttpUtil.sendJsonResponse(
                exchange,
                status,
                JsonUtil.toJson(response)
        );
    }
}