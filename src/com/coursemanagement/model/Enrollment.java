package com.coursemanagement.model;

import com.coursemanagement.enums.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Enrollment {
    private Long id;

    private Long studentId;

    private Long courseId;

    private BigDecimal originalPrice;

    private BigDecimal discountAmount;

    private BigDecimal finalPrice;

    private EnrollmentStatus status;

    private LocalDateTime enrollmentDate;

    public Enrollment(Long studentId, Long courseId, BigDecimal originalPrice, BigDecimal discountAmount, BigDecimal finalPrice, EnrollmentStatus status, LocalDateTime enrollmentDate) {

        this.studentId = studentId;
        this.courseId = courseId;
        this.originalPrice = originalPrice;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", originalPrice=" + originalPrice +
                ", discountAmount=" + discountAmount +
                ", finalPrice=" + finalPrice +
                ", status=" + status +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
