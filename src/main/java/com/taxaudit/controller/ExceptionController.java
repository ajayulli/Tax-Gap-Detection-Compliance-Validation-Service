package com.taxaudit.controller;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.enums.Severity;
import com.taxaudit.repository.ExceptionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exceptions")
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionRecordRepository exceptionRepository;

    @GetMapping
    public ResponseEntity<List<ExceptionRecord>> getAllExceptions() {
        return ResponseEntity.ok(exceptionRepository.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExceptionRecord>> filterExceptions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) String ruleName) {

        // Build a dynamic probe for Query By Example based on provided params
        ExceptionRecord probe = new ExceptionRecord();
        probe.setCustomerId(customerId);
        probe.setSeverity(severity);
        probe.setRuleName(ruleName);

        Example<ExceptionRecord> example = Example.of(probe);

        return ResponseEntity.ok(exceptionRepository.findAll(example));
    }
}