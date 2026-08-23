package com.todo.todoappback.section;

import java.time.Instant;
import java.util.UUID;

public record SectionResponse(UUID id, String name, Instant createdAt) {

    static SectionResponse from(Section section) {
        return new SectionResponse(section.getId(), section.getName(), section.getCreatedAt());
    }
}
