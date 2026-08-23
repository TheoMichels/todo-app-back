package com.todo.todoappback.section;

import jakarta.validation.constraints.NotBlank;

public record SectionCreateRequest(@NotBlank String name) {
}
