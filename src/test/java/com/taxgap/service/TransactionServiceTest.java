package com.taxaudit.service;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.EventType;
import com.taxaudit.repository.ExceptionRecordRepository;
import com.taxaudit.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TaxEngineService taxEngineService;
    @Mock private RuleEngineService ruleEngineService;
    @Mock private TransactionRepository transactionRepo;
    @Mock private ExceptionRecordRepository exceptionRepo;
    @Mock private AuditService auditService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void processBatch_SuccessfulProcessing_SavesTransactionAndAuditLogs() {
        // 1. Setup Test Data
        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-001");
        List<Transaction> batch = List.of(tx);

        // Simulate no rule exceptions being found
        when(ruleEngineService.executeRules(tx)).thenReturn(List.of());

        // 2. Execute
        transactionService.processBatch(batch);

        // 3. Verify interactions
        verify(taxEngineService, times(1)).calculateTaxGap(tx);
        verify(ruleEngineService, times(1)).executeRules(tx);
        verify(transactionRepo, times(1)).save(tx);
        
        // Verify exceptionRepo was NOT called since list was empty
        verify(exceptionRepo, never()).saveAll(any());

        // Verify Audit Service was called for all 3 events
        verify(auditService).logEvent(eq("TXN-001"), eq(EventType.INGESTION), anyString());
        verify(auditService).logEvent(eq("TXN-001"), eq(EventType.TAX_COMPUTATION), anyString());
        verify(auditService).logEvent(eq("TXN-001"), eq(EventType.RULE_EXECUTION), anyString());
    }

    @Test
    void processBatch_WithExceptions_SavesExceptions() {
        // 1. Setup Test Data
        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-002");
        List<Transaction> batch = List.of(tx);

        ExceptionRecord exception = new ExceptionRecord();
        exception.setTransactionId("TXN-002");

        // Simulate rule engine finding an exception
        when(ruleEngineService.executeRules(tx)).thenReturn(List.of(exception));

        // 2. Execute
        transactionService.processBatch(batch);

        // 3. Verify the exception repository was called to save the failure
        verify(transactionRepo, times(1)).save(tx);
        verify(exceptionRepo, times(1)).saveAll(any());
    }
}
