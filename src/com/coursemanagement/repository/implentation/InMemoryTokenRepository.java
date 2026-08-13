package com.coursemanagement.repository.implentation;

import com.coursemanagement.repository.TokenRepository;
import com.coursemanagement.security.AuthenticatedUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryTokenRepository implements TokenRepository {

    private final Map<String, AuthenticatedUser> tokens = new HashMap<>();

    @Override
    public void save(String token, AuthenticatedUser user) {
        tokens.put(token, user);
    }

    @Override
    public Optional<AuthenticatedUser> findByToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    @Override
    public void delete(String token) {
        tokens.remove(token);
    }
}