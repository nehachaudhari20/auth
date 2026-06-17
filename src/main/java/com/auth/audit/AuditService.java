package com.auth.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    

    public void log(
            String email,
            String action,
            String details) {

        AuditLog auditLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .userEmail(email)
                .action(action)
                .details(details)
                .createdAt(Instant.now())
                .build();

        auditLogRepository.save(auditLog);
    }
}