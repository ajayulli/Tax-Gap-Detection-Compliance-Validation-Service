package com.taxaudit.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TaxRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;
    private boolean isActive;

    // MySQL native JSON column definition
    @Column(columnDefinition = "json")
    private String configuration;
}