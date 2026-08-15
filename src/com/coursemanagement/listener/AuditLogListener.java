package com.coursemanagement.listener;

import com.coursemanagement.event.EnrollmentConfirmedEvent;
import com.coursemanagement.event.EventListener;
import com.coursemanagement.model.AuditLog;
import com.coursemanagement.service.AuditService;

import java.time.LocalDateTime;

public class AuditLogListener
        implements EventListener {

    private final AuditService auditService;

    public AuditLogListener(
            AuditService auditService) {

        this.auditService = auditService;
    }

    @Override
    public void onEnrollmentConfirmed(
            EnrollmentConfirmedEvent event) {

        AuditLog auditLog =
                new AuditLog(
                        "ENROLLMENT_CONFIRMED",
                        "ENROLLMENT",
                        event.getEnrollmentId(),
                        "Enrollment confirmed successfully for student "
                                + event.getStudentId(),
                        LocalDateTime.now()
                );

        auditService.createAuditLog(auditLog);
    }
}