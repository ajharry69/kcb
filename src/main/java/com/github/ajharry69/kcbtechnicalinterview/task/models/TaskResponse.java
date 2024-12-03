package com.github.ajharry69.kcbtechnicalinterview.task.models;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Builder
public record TaskResponse(
        UUID id,
        String name,
        TaskStatus status,
        LocalDate dueDate,
        UUID projectId
) {
}
