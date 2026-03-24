package com.taxaudit.domain.dto;

import com.taxaudit.domain.enums.Severity;

public record SeverityCountDto(Severity severity,Long count) {}