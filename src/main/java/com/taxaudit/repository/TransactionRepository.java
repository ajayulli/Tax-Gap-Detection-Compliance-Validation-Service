package com.taxaudit.repository;

import com.taxaudit.domain.dto.CustomerSummaryDto;
import com.taxaudit.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("""
        SELECT new com.taxaudit.domain.dto.CustomerSummaryDto(
            t.customerId, 
            SUM(t.amount), 
            SUM(t.reportedTax), 
            SUM(t.expectedTax), 
            SUM(t.taxGap), 
            COUNT(t), 
            SUM(CASE WHEN t.complianceStatus = 'NON_COMPLIANT' THEN 1L ELSE 0L END),
            (100.0 - (SUM(CASE WHEN t.complianceStatus = 'NON_COMPLIANT' THEN 1L ELSE 0L END) * 100.0 / COUNT(t)))
        ) 
        FROM Transaction t 
        GROUP BY t.customerId
    """)
    List<CustomerSummaryDto> getCustomerTaxSummaries();
}