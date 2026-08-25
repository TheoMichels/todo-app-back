package com.todo.todoappback.trackingpoint;

import com.todo.todoappback.section.Section;
import com.todo.todoappback.section.SectionRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * One-time, idempotent fixup for tracking points created before sections existed
 * on this resource: assigns them to the oldest section instead of leaving them
 * orphaned. Runs on every startup but is a no-op once nothing has a null
 * sectionId, which is why this lives as code rather than a SQL migration —
 * there's no migration tool wired up for this project yet.
 */
@Component
public class TrackingPointSectionBackfill implements ApplicationRunner {

    private final TrackingPointRepository trackingPointRepository;
    private final SectionRepository sectionRepository;

    public TrackingPointSectionBackfill(
            TrackingPointRepository trackingPointRepository,
            SectionRepository sectionRepository
    ) {
        this.trackingPointRepository = trackingPointRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<TrackingPoint> orphaned = trackingPointRepository.findBySectionIdIsNull();
        if (orphaned.isEmpty()) {
            return;
        }

        sectionRepository.findAll().stream()
                .min(Comparator.comparing(Section::getCreatedAt))
                .ifPresent(defaultSection -> {
                    orphaned.forEach(point -> point.setSectionId(defaultSection.getId()));
                    trackingPointRepository.saveAll(orphaned);
                });
    }
}
