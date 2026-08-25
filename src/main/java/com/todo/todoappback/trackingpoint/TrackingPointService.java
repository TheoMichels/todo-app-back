package com.todo.todoappback.trackingpoint;

import com.todo.todoappback.common.ResourceNotFoundException;
import com.todo.todoappback.section.SectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TrackingPointService {

    private final TrackingPointRepository trackingPointRepository;
    private final SectionRepository sectionRepository;

    public TrackingPointService(
            TrackingPointRepository trackingPointRepository,
            SectionRepository sectionRepository
    ) {
        this.trackingPointRepository = trackingPointRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<TrackingPoint> find(UUID sectionId) {
        if (sectionId != null) {
            return trackingPointRepository.findBySectionId(sectionId);
        }
        return trackingPointRepository.findAll();
    }

    public TrackingPoint create(TrackingPointCreateRequest request) {
        if (!sectionRepository.existsById(request.sectionId())) {
            throw new ResourceNotFoundException("Section introuvable");
        }
        TrackingPoint point = new TrackingPoint(
                request.sectionId(),
                request.title().trim(),
                request.status(),
                request.nextStep(),
                request.nextDueDate()
        );
        return trackingPointRepository.save(point);
    }

    public TrackingPoint applyPatch(UUID pointId, Map<String, Object> patch) {
        TrackingPoint point = trackingPointRepository.findById(pointId)
                .orElseThrow(() -> new ResourceNotFoundException("Point de suivi introuvable"));

        if (patch.containsKey("title")) {
            Object value = patch.get("title");
            if (!(value instanceof String title) || title.isBlank()) {
                throw new IllegalArgumentException("Le titre ne peut pas être vide");
            }
            point.setTitle(title.trim());
        }

        if (patch.containsKey("status")) {
            point.setStatus((String) patch.get("status"));
        }

        if (patch.containsKey("nextStep")) {
            point.setNextStep((String) patch.get("nextStep"));
        }

        if (patch.containsKey("nextDueDate")) {
            Object value = patch.get("nextDueDate");
            point.setNextDueDate(value == null ? null : LocalDate.parse((String) value));
        }

        return trackingPointRepository.save(point);
    }

    public void delete(UUID pointId) {
        if (!trackingPointRepository.existsById(pointId)) {
            throw new ResourceNotFoundException("Point de suivi introuvable");
        }
        trackingPointRepository.deleteById(pointId);
    }
}
