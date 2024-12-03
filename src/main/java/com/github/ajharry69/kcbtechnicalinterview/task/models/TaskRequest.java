package com.github.ajharry69.kcbtechnicalinterview.task.models;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        TaskStatus status,
        @NotNull
        @Future
        LocalDate dueDate
) {
}
