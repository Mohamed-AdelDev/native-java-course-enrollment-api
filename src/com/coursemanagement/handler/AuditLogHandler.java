package com.coursemanagement.handler;

import com.coursemanagement.model.AuditLog;
import com.coursemanagement.repository.AuditLogRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.security.AuthenticatedUser;
import com.coursemanagement.service.AuditService;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class AuditLogHandler implements HttpHandler {

    private static final AuditLogRepository auditLogRepository =
            RepositoryManager.auditLogRepository;

    private static final AuditService auditService =
            new AuditService(auditLogRepository);

    private static final AuthenticationService authenticationService =
            new AuthenticationService(
                    RepositoryManager.studentRepository,
                    RepositoryManager.tokenRepository
            );

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        try {

            AuthenticatedUser user =
                    authenticationService.authenticate(
                            HttpUtil.getHeader(
                                    exchange,
                                    "Authorization"
                            )
                    );

            if (user.getRole()
                    != com.coursemanagement.model.enums.Role.Admin) {

                throw new com.coursemanagement.security.ForbiddenException(
                        "Administrator access required"
                );
            }

            String method =
                    HttpUtil.getRequestMethod(exchange);

            String path =
                    HttpUtil.getRequestPath(exchange);

            if (method.equals("GET")
                    && path.equals("/api/audit-logs")) {

                getAuditLogs(exchange);
                return;
            }

            HttpUtil.sendResponse(exchange, 405);

        } catch (Exception e) {

            com.coursemanagement.exception.GlobalExceptionHandler
                    .handle(exchange, e);
        }
    }

    private void getAuditLogs(
            HttpExchange exchange) throws IOException {

        String entityType =
                HttpUtil.getQueryParameter(
                        exchange,
                        "entityType"
                );

        List<AuditLog> logs;

        if (entityType == null
                || entityType.isBlank()) {

            logs =
                    auditService.getAllLogs();

        } else {

            logs =
                    auditService.getLogsByEntityType(
                            entityType
                    );
        }

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                JsonUtil.toJsonAuditLogs(logs)
        );
    }
}