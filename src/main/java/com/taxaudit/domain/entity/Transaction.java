package com.taxaudit.domain.entity;

import com.taxaudit.domain.enums.ComplianceStatus;
import com.taxaudit.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    private String transactionId;

    private LocalDate date;
    private String customerId;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal reportedTax;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    // --- Requirement 1: Validation Storage ---
    private String validationStatus; // "SUCCESS" or "FAILURE"
    @Column(length = 500)
    private String failureReason;

    // Fields calculated by our TaxEngineService
    private BigDecimal expectedTax;
    private BigDecimal taxGap;

    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;
}
