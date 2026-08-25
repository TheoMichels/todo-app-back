package com.todo.todoappback.todo;

import com.todo.todoappback.common.ResourceNotFoundException;
import com.todo.todoappback.section.SectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final SectionRepository sectionRepository;

    public TodoService(TodoRepository todoRepository, SectionRepository sectionRepository) {
        this.todoRepository = todoRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Todo> find(UUID sectionId, Boolean done) {
        if (sectionId != null && done != null) {
            return todoRepository.findBySectionIdAndDone(sectionId, done);
        }
        if (sectionId != null) {
            return todoRepository.findBySectionId(sectionId);
        }
        if (done != null) {
            return todoRepository.findByDone(done);
        }
        return todoRepository.findAll();
    }

    public Todo create(TodoCreateRequest request) {
        if (!sectionRepository.existsById(request.sectionId())) {
            throw new ResourceNotFoundException("Section introuvable");
        }
        Todo todo = new Todo(request.sectionId(), request.title().trim(), request.priority(), request.dueDate());
        return todoRepository.save(todo);
    }

    public Todo applyPatch(UUID todoId, Map<String, Object> patch) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche introuvable"));

        if (patch.containsKey("title")) {
            Object value = patch.get("title");
            if (!(value instanceof String title) || title.isBlank()) {
                throw new IllegalArgumentException("Le titre ne peut pas être vide");
            }
            todo.setTitle(title.trim());
        }

        if (patch.containsKey("priority")) {
            Object value = patch.get("priority");
            if (!(value instanceof String priorityValue)) {
                throw new IllegalArgumentException("La priorité ne peut pas être nulle");
            }
            todo.setPriority(parsePriority(priorityValue));
        }

        if (patch.containsKey("dueDate")) {
            Object value = patch.get("dueDate");
            todo.setDueDate(value == null ? null : LocalDate.parse((String) value));
        }

        if (patch.containsKey("done")) {
            Object value = patch.get("done");
            if (!(value instanceof Boolean done)) {
                throw new IllegalArgumentException("done doit être un booléen");
            }
            todo.setDone(done);
        }

        return todoRepository.save(todo);
    }

    public void delete(UUID todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new ResourceNotFoundException("Tâche introuvable");
        }
        todoRepository.deleteById(todoId);
    }

    public void deleteAll(List<UUID> todoIds) {
        todoRepository.deleteAllByIdInBatch(todoIds);
    }

    private Priority parsePriority(String value) {
        try {
            return Priority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Priorité invalide : " + value);
        }
    }
}
