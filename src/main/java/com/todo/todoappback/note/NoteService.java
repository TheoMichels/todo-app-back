package com.todo.todoappback.note;

import com.todo.todoappback.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note create(String title, String description) {
        return noteRepository.save(new Note(title.trim(), description));
    }

    public Note update(UUID noteId, String title, String description) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note introuvable"));
        note.setTitle(title.trim());
        note.setDescription(description);
        return noteRepository.save(note);
    }

    public void delete(UUID noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new ResourceNotFoundException("Note introuvable");
        }
        noteRepository.deleteById(noteId);
    }
}
