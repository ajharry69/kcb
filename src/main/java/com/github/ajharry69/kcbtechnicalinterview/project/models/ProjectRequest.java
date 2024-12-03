package com.github.ajharry69.kcbtechnicalinterview.project.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProjectRequest(
        @NotNull
        @NotBlank
        String name,
        String description
) {
}
