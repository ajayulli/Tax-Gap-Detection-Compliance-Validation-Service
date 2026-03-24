package com.taxaudit.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.Transaction;

import java.util.Optional;

public interface TaxRuleEvaluator {

    // This must exactly match the rule_name in the MySQL tax_rule table
    String getRuleName();

    // Returns an ExceptionRecord if the rule is violated, or Optional.empty() if it passes
    Optional<ExceptionRecord> evaluate(Transaction transaction, JsonNode config);
}