package com.coursemanagement.exception;

public class CourseNotAvailableException extends RuntimeException {

    public CourseNotAvailableException(String message) {
        super(message);
    }
}