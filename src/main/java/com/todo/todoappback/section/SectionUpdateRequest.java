package com.todo.todoappback.section;

import jakarta.validation.constraints.NotBlank;

public record SectionUpdateRequest(@NotBlank String name) {
}
