package com.taxaudit.domain.entity;

import com.taxaudit.domain.enums.Severity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ExceptionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String customerId;
    private String ruleName;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(length = 500)
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();
}