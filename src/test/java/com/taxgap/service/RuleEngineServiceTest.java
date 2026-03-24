package com.taxgap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxgap.domain.entity.ExceptionRecord;
import com.taxgap.domain.entity.TaxRule;
import com.taxgap.domain.entity.Transaction;
import com.taxgap.repository.TaxRuleRepository;
import com.taxgap.rules.HighValueRuleEvaluator;
import com.taxgap.rules.TaxRuleEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleEngineServiceTest {

    @Mock
    private TaxRuleRepository taxRuleRepository;

    private RuleEngineService ruleEngineService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TaxRuleEvaluator> evaluators = List.of(new HighValueRuleEvaluator());
        ruleEngineService = new RuleEngineService(taxRuleRepository, objectMapper, evaluators);
    }

    @Test
    void executeRules_HighValueTransaction_CreatesException() {
        TaxRule highValueRule = new TaxRule();
        highValueRule.setRuleName("HIGH_VALUE_TRANSACTION");
        highValueRule.setActive(true);
        highValueRule.setConfiguration("{\"threshold\": 10000}");

        when(taxRuleRepository.findByIsActiveTrue()).thenReturn(List.of(highValueRule));

        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-999");
        tx.setAmount(new BigDecimal("15000"));

        List<ExceptionRecord> exceptions = ruleEngineService.executeRules(tx);

        assertEquals(1, exceptions.size());
        assertEquals("HIGH_VALUE_TRANSACTION", exceptions.get(0).getRuleName());
    }
}