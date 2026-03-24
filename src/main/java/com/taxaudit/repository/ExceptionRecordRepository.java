package com.taxaudit.repository;

import com.taxaudit.domain.dto.SeverityCountDto;
import com.taxaudit.domain.entity.ExceptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExceptionRecordRepository extends JpaRepository<ExceptionRecord, Long>, QueryByExampleExecutor<ExceptionRecord> {

    @Query("""
        SELECT new com.taxaudit.domain.dto.SeverityCountDto(e.severity, COUNT(e)) 
        FROM ExceptionRecord e 
        GROUP BY e.severity
    """)
    List<SeverityCountDto> countBySeverity();
}