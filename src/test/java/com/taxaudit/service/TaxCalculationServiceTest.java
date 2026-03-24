package com.taxaudit.service;

import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.ComplianceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculationServiceTest {

    private TaxCalculationService taxCalculationService;

    @BeforeEach
    void setUp() {
        taxCalculationService = new TaxCalculationService();
    }

    @Test
    void calculateTaxGap_MissingReportedTax_ReturnsNonCompliant() {
        Transaction tx = new Transaction();
        tx.setAmount(new BigDecimal("1000"));
        tx.setTaxRate(new BigDecimal("0.10"));
        tx.setReportedTax(null);

        taxCalculationService.calculateTaxGap(tx);

        assertEquals(ComplianceStatus.NON_COMPLIANT, tx.getComplianceStatus());
        assertNull(tx.getExpectedTax());
    }

    @Test
    void calculateTaxGap_ExactMatch_ReturnsCompliant() {
        Transaction tx = new Transaction();
        tx.setAmount(new BigDecimal("1000"));
        tx.setTaxRate(new BigDecimal("0.10")); // Expected: 100
        tx.setReportedTax(new BigDecimal("100"));

        taxCalculationService.calculateTaxGap(tx);

        assertAll(
                () -> assertEquals(ComplianceStatus.COMPLIANT, tx.getComplianceStatus()),
                () -> assertEquals(0, new BigDecimal("100").compareTo(tx.getExpectedTax())),
                () -> assertEquals(0, BigDecimal.ZERO.compareTo(tx.getTaxGap()))
        );
    }
}