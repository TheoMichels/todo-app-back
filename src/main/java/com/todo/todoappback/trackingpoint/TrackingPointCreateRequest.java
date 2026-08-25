package com.todo.todoappback.trackingpoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record TrackingPointCreateRequest(
        @NotNull UUID sectionId,
        @NotBlank String title,
        String status,
        String nextStep,
        LocalDate nextDueDate
) {
}
