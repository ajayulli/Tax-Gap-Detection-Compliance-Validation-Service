package com.taxaudit.service;

import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.ComplianceStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxEngineService {

    public void calculateTaxGap(Transaction tx) {
        if (tx.getReportedTax() == null) {
            tx.setComplianceStatus(ComplianceStatus.NON_COMPLIANT);
            return;
        }

        BigDecimal expected = tx.getAmount().multiply(tx.getTaxRate());
        BigDecimal gap = expected.subtract(tx.getReportedTax());

        tx.setExpectedTax(expected);
        tx.setTaxGap(gap);

        if (gap.abs().compareTo(BigDecimal.ONE) <= 0) {
            tx.setComplianceStatus(ComplianceStatus.COMPLIANT);
        } else if (gap.compareTo(BigDecimal.ONE) > 0) {
            tx.setComplianceStatus(ComplianceStatus.UNDERPAID);
        } else {
            tx.setComplianceStatus(ComplianceStatus.OVERPAID);
        }
    }
}