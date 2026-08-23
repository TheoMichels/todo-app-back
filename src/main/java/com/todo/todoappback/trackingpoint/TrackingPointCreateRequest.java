package com.todo.todoappback.trackingpoint;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TrackingPointCreateRequest(
        @NotBlank String title,
        String status,
        String nextStep,
        LocalDate nextDueDate
) {
}
