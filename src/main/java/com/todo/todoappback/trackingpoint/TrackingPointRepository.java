package com.todo.todoappback.trackingpoint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackingPointRepository extends JpaRepository<TrackingPoint, UUID> {

    List<TrackingPoint> findBySectionId(UUID sectionId);

    List<TrackingPoint> findBySectionIdIsNull();
}
