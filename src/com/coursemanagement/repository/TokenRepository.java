package com.coursemanagement.repository;

import com.coursemanagement.security.AuthenticatedUser;

import java.util.Optional;

public interface TokenRepository {

    void save(String token, AuthenticatedUser user);

    Optional<AuthenticatedUser> findByToken(String token);

    void delete(String token);
}