package com.coursemanagement.repository;

import com.coursemanagement.model.AuditLog;

import java.util.HashMap;
import java.util.*;
import java.util.Optional;

public class InMemoryAuditLogRepository implements AuditLogRepository{
    private final Map<Long, AuditLog>auditLogs = new HashMap<>();
    private Long nextId = 1l;


    @Override
    public AuditLog save(AuditLog auditLog) {

        if (auditLog.getId() == null) {
            auditLog.setId(nextId++);
        }

        auditLogs.put(auditLog.getId(), auditLog);

        return auditLog;
    }

    @Override
    public Optional<AuditLog> findById(Long id) {
        return Optional.ofNullable(auditLogs.get(id));
    }

    @Override
    public List<AuditLog> findAll() {
        return new ArrayList<>(auditLogs.values());
    }

    @Override
    public List<AuditLog> findByEntityType(String entityType) {

        List<AuditLog> result = new ArrayList<>();

        for (AuditLog auditLog : auditLogs.values()) {

            if (auditLog.getEntityType().equals(entityType)) {
                result.add(auditLog);
            }

        }

        return result;
    }
}
