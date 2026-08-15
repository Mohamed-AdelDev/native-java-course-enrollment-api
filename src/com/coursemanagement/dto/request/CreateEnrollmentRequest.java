package com.coursemanagement.dto.request;

public class CreateEnrollmentRequest {

    private Long studentId;
    private Long courseId;
    private String discountType;

    public CreateEnrollmentRequest(
            Long studentId,
            Long courseId,
            String discountType) {

        this.studentId = studentId;
        this.courseId = courseId;
        this.discountType = discountType;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}