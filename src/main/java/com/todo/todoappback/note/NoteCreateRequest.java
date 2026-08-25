package com.todo.todoappback.note;

import jakarta.validation.constraints.NotBlank;

public record NoteCreateRequest(@NotBlank String title, String description) {
}
