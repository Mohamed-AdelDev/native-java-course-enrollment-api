package com.coursemanagement.dto.response;

public class LoginResponse {

    private Long studentId;
    private String fullName;
    private String message;

    public LoginResponse(Long studentId, String fullName, String message) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.message = message;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMessage() {
        return message;
    }
}