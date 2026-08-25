package com.todo.todoappback.note;

import jakarta.validation.constraints.NotBlank;

public record NoteUpdateRequest(@NotBlank String title, String description) {
}
