package com.todo.todoappback.trackingpoint;

import java.time.LocalDate;

/**
 * Documentation-only shape for springdoc: the controller binds the raw JSON body
 * as a Map so it can tell "field omitted" (leave alone) apart from "field
 * explicitly null" (clear it) — see {@link TrackingPointService#applyPatch}.
 */
public record TrackingPointUpdateRequest(
        String title,
        String status,
        String nextStep,
        LocalDate nextDueDate
) {
}
