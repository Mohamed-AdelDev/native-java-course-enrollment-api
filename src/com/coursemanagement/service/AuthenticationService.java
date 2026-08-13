package com.coursemanagement.service;

import com.coursemanagement.dto.response.LoginResponse;
import com.coursemanagement.model.Student;
import com.coursemanagement.model.enums.Role;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.repository.TokenRepository;
import com.coursemanagement.security.AuthenticatedUser;
import com.coursemanagement.security.AuthenticationException;
import com.coursemanagement.security.ForbiddenException;

import java.util.UUID;

public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final TokenRepository tokenRepository;

    public AuthenticationService(
            StudentRepository studentRepository,
            TokenRepository tokenRepository) {

        this.studentRepository = studentRepository;
        this.tokenRepository = tokenRepository;
    }


    public LoginResponse login(
            String email,
            String password
    ) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AuthenticationException(
                                "Invalid email or password"
                        ));

        if (!student.getPassword().equals(password)) {

            throw new AuthenticationException(
                    "Invalid email or password"
            );
        }

        if (!student.isActive()) {

            throw new AuthenticationException(
                    "User is inactive"
            );
        }

        AuthenticatedUser user =
                new AuthenticatedUser(
                        student.getId(),
                        student.getEmail(),
                        student.getRole()
                );

        String token = UUID.randomUUID().toString();

        tokenRepository.save(token, user);

        return new LoginResponse(
                token,
                "Bearer",
                student.getRole()
        );
    }


    public AuthenticatedUser authenticate(
            String authorizationHeader
    ) {

        if (authorizationHeader == null
                || authorizationHeader.isBlank()) {

            throw new AuthenticationException(
                    "Authentication required"
            );
        }

        if (!authorizationHeader.startsWith("Bearer ")) {

            throw new AuthenticationException(
                    "Invalid authorization header"
            );
        }

        String token =
                authorizationHeader.substring(7);

        if (token.isBlank()) {

            throw new AuthenticationException(
                    "Invalid token"
            );
        }

        return tokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new AuthenticationException(
                                "Invalid token"
                        ));
    }


    public AuthenticatedUser requireAdmin(
            String authorizationHeader
    ) {

        AuthenticatedUser user =
                authenticate(authorizationHeader);

        if (user.getRole() != Role.Admin) {

            throw new ForbiddenException(
                    "Admin access required"
            );
        }

        return user;
    }
}