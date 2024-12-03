package com.github.ajharry69.kcbtechnicalinterview.task.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Builder
public record TaskRequest(
        @NotNull
        @NotBlank
        String name,
        TaskStatus status,
        LocalDate dueDate,
        UUID projectId
) {
}
