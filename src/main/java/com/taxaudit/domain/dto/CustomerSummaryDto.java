package com.taxaudit.domain.dto;

import java.math.BigDecimal;

public record CustomerSummaryDto(
        String customerId,
        BigDecimal totalAmount,
        BigDecimal totalReportedTax,
        BigDecimal totalExpectedTax,
        BigDecimal totalTaxGap,
        Long totalTransactions,
        Long nonCompliantTransactions,
        Double complianceScore
) {
}