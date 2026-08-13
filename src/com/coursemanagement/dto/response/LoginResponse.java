package com.coursemanagement.dto.response;

import com.coursemanagement.model.enums.Role;

public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Role role;

    public LoginResponse(
            String accessToken,
            String tokenType,
            Role role
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Role getRole() {
        return role;
    }
}