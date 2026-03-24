package com.taxaudit.controller;

import com.taxaudit.domain.dto.CustomerSummaryDto;
import com.taxaudit.domain.dto.SeverityCountDto;
import com.taxaudit.repository.ExceptionRecordRepository;
import com.taxaudit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final TransactionRepository transactionRepository;
    private final ExceptionRecordRepository exceptionRepository;

    @GetMapping("/customer-summary")
    public ResponseEntity<List<CustomerSummaryDto>> getCustomerTaxSummary() {
        // Calls the custom JPQL query defined in the repository
        return ResponseEntity.ok(transactionRepository.getCustomerTaxSummaries());
    }

    @GetMapping("/exception-summary")
    public ResponseEntity<Map<String, Object>> getExceptionSummary() {
        long totalExceptions = exceptionRepository.count();
        List<SeverityCountDto> severityCounts = exceptionRepository.countBySeverity();

        return ResponseEntity.ok(Map.of(
                "totalExceptions", totalExceptions,
                "countsBySeverity", severityCounts
        ));
    }
}