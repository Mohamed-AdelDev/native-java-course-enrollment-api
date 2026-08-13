package com.coursemanagement.security;

import com.coursemanagement.model.enums.Role;

public class AuthenticatedUser {

    private final Long userId;
    private final String email;
    private final Role role;

    public AuthenticatedUser(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}