package com.taxaudit.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.Severity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class HighValueRuleEvaluator implements TaxRuleEvaluator {

    @Override
    public String getRuleName() {
        return "HIGH_VALUE_TRANSACTION";
    }

    @Override
    public Optional<ExceptionRecord> evaluate(Transaction tx, JsonNode config) {
        //  (e.g., {"threshold": 10000})
        if (config.has("threshold")) {
            BigDecimal threshold = new BigDecimal(config.get("threshold").asText());

            if (tx.getAmount().compareTo(threshold) > 0) {
                ExceptionRecord record = new ExceptionRecord();
                record.setTransactionId(tx.getTransactionId());
                record.setCustomerId(tx.getCustomerId());
                record.setRuleName(getRuleName());
                record.setSeverity(Severity.HIGH);
                record.setMessage("Transaction amount (" + tx.getAmount() + ") exceeds the high-value threshold of " + threshold);

                return Optional.of(record);
            }
        }
        return Optional.empty(); // Rule passed
    }
}