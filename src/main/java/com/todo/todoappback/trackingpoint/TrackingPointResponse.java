package com.todo.todoappback.trackingpoint;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TrackingPointResponse(
        UUID id,
        String title,
        String status,
        String nextStep,
        LocalDate nextDueDate,
        Instant createdAt
) {

    static TrackingPointResponse from(TrackingPoint point) {
        return new TrackingPointResponse(
                point.getId(),
                point.getTitle(),
                point.getStatus(),
                point.getNextStep(),
                point.getNextDueDate(),
                point.getCreatedAt()
        );
    }
}
