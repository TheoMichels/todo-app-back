package com.todo.todoappback.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record TodoCreateRequest(
        @NotNull UUID sectionId,
        @NotBlank String title,
        Priority priority,
        LocalDate dueDate
) {
}
