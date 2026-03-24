package com.taxaudit.controller;

import com.taxaudit.domain.dto.CustomerSummaryDto;
import com.taxaudit.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/customer-summary")
    public ResponseEntity<List<CustomerSummaryDto>> getCustomerTaxSummary() {
        // Calls the custom JPQL query defined in the repository
        return ResponseEntity.ok(reportService.getCustomerTaxSummaries());
    }

    @GetMapping("/exception-summary")
    public ResponseEntity<Map<String, Object>> getExceptionSummary() {
        return ResponseEntity.ok(reportService.getExceptionSummary());
    }
}