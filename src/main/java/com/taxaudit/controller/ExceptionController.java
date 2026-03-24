package com.taxaudit.controller;

import com.taxaudit.domain.entity.ExceptionRecord;
import com.taxaudit.domain.enums.Severity;
import com.taxaudit.service.ExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exceptions")
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionService exceptionService;

    @GetMapping
    public ResponseEntity<List<ExceptionRecord>> getAllExceptions() {
        return ResponseEntity.ok(exceptionService.getAllExceptions());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExceptionRecord>> filterExceptions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) String ruleName) {

        return ResponseEntity.ok(exceptionService.filterExceptions(customerId, severity, ruleName));
    }
}