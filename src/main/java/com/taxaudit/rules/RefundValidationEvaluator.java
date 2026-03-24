package com.taxaudit.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.Severity;
import com.taxaudit.domain.enums.TransactionType;
import com.taxaudit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefundValidationEvaluator implements TaxRuleEvaluator {

    private final TransactionRepository transactionRepository;

    @Override
    public String getRuleName() {
        return "REFUND_VALIDATION";
    }

    @Override
    public Optional<ExceptionRecord> evaluate(Transaction tx, JsonNode config) {
        if (tx.getTransactionType() == TransactionType.REFUND) {

            BigDecimal maxAutoRefund = config.has("maxRefundLimit")
                    ? new BigDecimal(config.get("maxRefundLimit").asText())
                    : BigDecimal.valueOf(5000); // Default fallback

            if (tx.getAmount().compareTo(maxAutoRefund) > 0) {
                ExceptionRecord record = new ExceptionRecord();
                record.setTransactionId(tx.getTransactionId());
                record.setCustomerId(tx.getCustomerId());
                record.setRuleName(getRuleName());
                record.setSeverity(Severity.HIGH);
                record.setMessage("Refund amount (" + tx.getAmount() + ") exceeds automated validation limit. Requires manual original sale verification.");

                return Optional.of(record);
            }
        }
        return Optional.empty();
    }
}