package com.taxgap.service;

import com.taxgap.domain.entity.Transaction;
import com.taxgap.domain.enums.EventType;
import com.taxgap.repository.ExceptionRecordRepository;
import com.taxgap.repository.TransactionRepository;
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

    @Mock private TaxCalculationService taxCalculationService;
    @Mock private RuleEngineService ruleEngineService;
    @Mock private TransactionRepository transactionRepo;
    @Mock private ExceptionRecordRepository exceptionRepo;
    @Mock private AuditService auditService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void processBatch_SuccessfulProcessing_SavesTransactionAndAuditLogs() {
        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-001");
        List<Transaction> batch = List.of(tx);

        when(ruleEngineService.executeRules(tx)).thenReturn(List.of());

        transactionService.processBatch(batch);

        verify(taxCalculationService, times(1)).calculateTaxGap(tx);
        verify(ruleEngineService, times(1)).executeRules(tx);
        verify(transactionRepo, times(1)).save(tx);
        verify(exceptionRepo, never()).saveAll(any());

        verify(auditService).logEvent(eq("TXN-001"), eq(EventType.INGESTION), anyString());
    }
}