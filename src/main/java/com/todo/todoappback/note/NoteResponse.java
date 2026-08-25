package com.todo.todoappback.note;

import java.time.Instant;
import java.util.UUID;

public record NoteResponse(UUID id, String title, String description, Instant createdAt) {

    static NoteResponse from(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getDescription(), note.getCreatedAt());
    }
}
