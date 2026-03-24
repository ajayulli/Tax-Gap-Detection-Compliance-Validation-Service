
package com.taxaudit.controller;

import com.taxaudit.domain.entity.Transaction;
import com.taxaudit.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTransactions(@RequestBody List<Transaction> transactions) {
        transactionService.processBatch(transactions);
        return ResponseEntity.ok("Successfully processed " + transactions.size() + " transactions.");
    }
}