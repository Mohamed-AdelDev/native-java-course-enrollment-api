package com.coursemanagement.service;

import com.coursemanagement.model.AuditLog;
import com.coursemanagement.repository.AuditLogRepository;

import java.util.List;

public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog createAuditLog(
            AuditLog auditLog) {

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {

        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByEntityType(
            String entityType) {

        return auditLogRepository
                .findByEntityType(entityType);
    }
}