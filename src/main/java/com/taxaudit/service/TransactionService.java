package com.taxaudit.service;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.EventType;
import com.taxaudit.repository.ExceptionRecordRepository;
import com.taxaudit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

            // --- Requirement 1: Explicit Data Validation ---
            if (tx.getAmount() == null || tx.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                tx.setValidationStatus("FAILURE");
                tx.setFailureReason("Amount must be present and greater than 0");
                transactionRepo.save(tx);
                continue; // Skip math and rules for invalid transactions
            }
            if (tx.getDate() == null || tx.getCustomerId() == null || tx.getTaxRate() == null || tx.getReportedTax() == null) {
                tx.setValidationStatus("FAILURE");
                tx.setFailureReason("Missing required fields (date, customerId, taxRate, or reportedTax)");
                transactionRepo.save(tx);
                continue; // Skip math and rules for invalid transactions
            }

            // If it passes validation, mark it as a success and proceed
            tx.setValidationStatus("SUCCESS");

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