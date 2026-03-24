package com.taxaudit.domain.entity;

import com.taxaudit.domain.enums.EventType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    // Captures the old/new values or rule information in JSON format
    @Column(columnDefinition = "json")
    private String detailJson;

    private LocalDateTime timestamp = LocalDateTime.now();
}