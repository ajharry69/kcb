package com.github.ajharry69.kcbtechnicalinterview.project.models;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProjectResponse(
        UUID id,
        String name,
        String description
) {
}
