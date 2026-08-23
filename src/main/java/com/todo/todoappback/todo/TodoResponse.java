package com.todo.todoappback.todo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TodoResponse(
        UUID id,
        UUID sectionId,
        String title,
        boolean done,
        Priority priority,
        LocalDate dueDate,
        Instant createdAt
) {

    static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getSectionId(),
                todo.getTitle(),
                todo.isDone(),
                todo.getPriority(),
                todo.getDueDate(),
                todo.getCreatedAt()
        );
    }
}
