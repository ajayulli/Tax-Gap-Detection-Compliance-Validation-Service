package com.taxaudit.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.domain.enums.Severity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class GstSlabViolationEvaluator implements TaxRuleEvaluator {

    @Override
    public String getRuleName() {
        return "GST_SLAB_VIOLATION";
    }

    @Override
    public Optional<ExceptionRecord> evaluate(Transaction tx, JsonNode config) {
        // Expected config: {"slabThreshold": 50000, "requiredRate": 0.18}
        if (config.has("slabThreshold") && config.has("requiredRate")) {
            BigDecimal slabThreshold = new BigDecimal(config.get("slabThreshold").asText());
            BigDecimal requiredRate = new BigDecimal(config.get("requiredRate").asText());

            boolean exceedsSlab = tx.getAmount().compareTo(slabThreshold) > 0;
            boolean underTaxed = tx.getTaxRate().compareTo(requiredRate) < 0;

            if (exceedsSlab && underTaxed) {
                ExceptionRecord record = new ExceptionRecord();
                record.setTransactionId(tx.getTransactionId());
                record.setCustomerId(tx.getCustomerId());
                record.setRuleName(getRuleName());
                record.setSeverity(Severity.MEDIUM);
                record.setMessage("Amount exceeds GST slab, but tax rate (" + tx.getTaxRate() + ") is lower than required (" + requiredRate + ").");

                return Optional.of(record);
            }
        }
        return Optional.empty();
    }
}