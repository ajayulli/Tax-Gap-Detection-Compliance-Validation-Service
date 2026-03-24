package com.taxaudit.service;

import com.taxaudit.domain.dto.CustomerSummaryDto;
import com.taxaudit.domain.dto.SeverityCountDto;
import com.taxaudit.repository.ExceptionRecordRepository;
import com.taxaudit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.taxaudit.domain.dto.CustomerExceptionCountDto;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final ExceptionRecordRepository exceptionRepository;

    public List<CustomerSummaryDto> getCustomerTaxSummary() {
        return transactionRepository.getCustomerTaxSummaries();
    }

    public Map<String, Object> getExceptionSummary() {
        long totalExceptions = exceptionRepository.count();
        List<SeverityCountDto> severityCounts = exceptionRepository.countBySeverity();

        // --- Requirement 6B: Fetch customer-wise exception counts ---
        List<CustomerExceptionCountDto> customerCounts = exceptionRepository.countExceptionsByCustomer();

        return Map.of(
                "totalExceptions", totalExceptions,
                "countsBySeverity", severityCounts,
                "customerWiseExceptions", customerCounts
        );
    }
}