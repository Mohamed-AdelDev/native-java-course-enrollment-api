package com.coursemanagement.handler;

import com.coursemanagement.dto.response.LoginResponse;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.repository.TokenRepository;
import com.coursemanagement.repository.implentation.RepositoryManager;
import com.coursemanagement.security.AuthenticationException;
import com.coursemanagement.service.AuthenticationService;
import com.coursemanagement.util.HttpUtil;
import com.coursemanagement.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthHandler implements HttpHandler {

    private static final StudentRepository studentRepository =
            RepositoryManager.studentRepository;

    private static final TokenRepository tokenRepository =
            RepositoryManager.tokenRepository;

    private static final AuthenticationService authenticationService =
            new AuthenticationService(
                    studentRepository,
                    tokenRepository
            );

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method =
                HttpUtil.getRequestMethod(exchange);

        if (!method.equals("POST")) {

            HttpUtil.sendResponse(
                    exchange,
                    405
            );

            return;
        }

        login(exchange);
    }

    private void login(
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

            String email =
                    JsonUtil.getString(
                            body,
                            "email"
                    );

            String password =
                    JsonUtil.getString(
                            body,
                            "password"
                    );

            if (email == null || email.isBlank()) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Email is required"
                        )
                );

                return;
            }

            if (password == null || password.isBlank()) {

                HttpUtil.sendJsonResponse(
                        exchange,
                        400,
                        JsonUtil.error(
                                "Password is required"
                        )
                );

                return;
            }

            LoginResponse response =
                    authenticationService.login(
                            email,
                            password
                    );

            String json = """
                    {
                      "accessToken": "%s",
                      "tokenType": "%s",
                      "role": "%s"
                    }
                    """.formatted(
                    JsonUtil.escape(
                            response.getAccessToken()
                    ),
                    JsonUtil.escape(
                            response.getTokenType()
                    ),
                    response.getRole()
            );

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

        } catch (AuthenticationException e) {

            HttpUtil.sendJsonResponse(
                    exchange,
                    401,
                    JsonUtil.error(
                            "Invalid email or password"
                    )
            );
        }
    }
}