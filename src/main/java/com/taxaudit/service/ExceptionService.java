package com.taxaudit.service;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.enums.Severity;
import com.taxaudit.repository.ExceptionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    private final ExceptionRecordRepository exceptionRepository;

    public List<ExceptionRecord> getAllExceptions() {
        return exceptionRepository.findAll();
    }

    public List<ExceptionRecord> filterExceptions(String customerId, Severity severity, String ruleName) {
        // Build a dynamic probe for Query By Example based on provided params
        ExceptionRecord probe = new ExceptionRecord();
        probe.setCustomerId(customerId);
        probe.setSeverity(severity);
        probe.setRuleName(ruleName);

        Example<ExceptionRecord> example = Example.of(probe);
        return exceptionRepository.findAll(example);
    }
}