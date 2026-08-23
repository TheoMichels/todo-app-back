package com.todo.todoappback.trackingpoint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackingPointRepository extends JpaRepository<TrackingPoint, UUID> {
}
