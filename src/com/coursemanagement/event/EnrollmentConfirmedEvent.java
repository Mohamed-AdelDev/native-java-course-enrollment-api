package com.coursemanagement.event;

import java.time.LocalDateTime;

public class EnrollmentConfirmedEvent {

    private final Long enrollmentId;
    private final Long studentId;
    private final Long courseId;
    private final LocalDateTime confirmedAt;

    public EnrollmentConfirmedEvent(
            Long enrollmentId,
            Long studentId,
            Long courseId) {

        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.confirmedAt = LocalDateTime.now();
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}