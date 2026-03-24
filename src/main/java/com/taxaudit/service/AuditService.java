package com.taxaudit.service;

import com.taxaudit.domain.entity.AuditLog;
import com.taxaudit.domain.enums.EventType;
import com.taxaudit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(String transactionId, EventType eventType, String detailJson) {
        AuditLog log = new AuditLog();
        log.setTransactionId(transactionId);
        log.setEventType(eventType);
        log.setDetailJson(detailJson);

        auditLogRepository.save(log);
    }
}