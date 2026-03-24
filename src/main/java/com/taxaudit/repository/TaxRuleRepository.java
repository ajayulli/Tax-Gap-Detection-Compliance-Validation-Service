package com.taxaudit.repository;

import com.taxaudit.domain.entity.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {

    // Spring Data JPA automatically writes the SQL for this method name
    List<TaxRule> findByIsActiveTrue();
}