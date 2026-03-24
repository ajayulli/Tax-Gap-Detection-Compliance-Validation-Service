package com.taxaudit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.entity.TaxRule;
import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.repository.TaxRuleRepository;
import com.taxaudit.rules.TaxRuleEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RuleEngineService {

    private final TaxRuleRepository ruleRepository;
    private final ObjectMapper objectMapper;
    private final Map<String, TaxRuleEvaluator> evaluators;

    // Spring automatically injects all implementations of TaxRuleEvaluator
    public RuleEngineService(TaxRuleRepository ruleRepository,
                             ObjectMapper objectMapper,
                             List<TaxRuleEvaluator> evaluatorList) {
        this.ruleRepository = ruleRepository;
        this.objectMapper = objectMapper;
        // Maps the rule name (e.g., "HIGH_VALUE_TRANSACTION") directly to its implementing class
        this.evaluators = evaluatorList.stream()
                .collect(Collectors.toMap(TaxRuleEvaluator::getRuleName, Function.identity()));
    }

    public List<ExceptionRecord> executeRules(Transaction tx) {
        List<ExceptionRecord> exceptions = new ArrayList<>();
        List<TaxRule> activeRules = ruleRepository.findByIsActiveTrue();

        for (TaxRule rule : activeRules) {
            TaxRuleEvaluator evaluator = evaluators.get(rule.getRuleName());
            if (evaluator != null) {
                try {
                    JsonNode config = objectMapper.readTree(rule.getConfiguration());
                    // Execute the rule and add the exception if it fails
                    evaluator.evaluate(tx, config).ifPresent(exceptions::add);
                } catch (Exception e) {
                    System.err.println("Failed to parse rule config for: " + rule.getRuleName());
                }
            }
        }
        return exceptions;
    }
}