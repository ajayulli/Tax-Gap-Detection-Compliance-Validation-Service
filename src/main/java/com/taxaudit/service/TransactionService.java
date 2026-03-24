package com.taxaudit.service;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.EventType;
import com.taxaudit.repository.ExceptionRecordRepository;
import com.taxaudit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TaxEngineService taxEngine;
    private final RuleEngineService ruleEngine;
    private final TransactionRepository transactionRepo;
    private final ExceptionRecordRepository exceptionRepo;
    private final AuditService auditService;

    @Transactional
    public void processBatch(List<Transaction> transactions) {
        for (Transaction tx : transactions) {

            // 1. Log Ingestion
            auditService.logEvent(tx.getTransactionId(), EventType.INGESTION, "{\"status\": \"STARTED\"}");

            // 2. Core Tax Math
            taxEngine.calculateTaxGap(tx);
            auditService.logEvent(tx.getTransactionId(), EventType.TAX_COMPUTATION,
                    "{\"taxGap\": " + tx.getTaxGap() + ", \"status\": \"" + tx.getComplianceStatus() + "\"}");

            // 3. Dynamic Rules
            List<ExceptionRecord> exceptions = ruleEngine.executeRules(tx);
            auditService.logEvent(tx.getTransactionId(), EventType.RULE_EXECUTION,
                    "{\"exceptionsFound\": " + exceptions.size() + "}");

            transactionRepo.save(tx);
            if (!exceptions.isEmpty()) {
                exceptionRepo.saveAll(exceptions);
            }
        }
    }
}