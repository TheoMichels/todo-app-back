package com.todo.todoappback.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

    List<Todo> findBySectionIdAndDone(UUID sectionId, boolean done);

    List<Todo> findBySectionId(UUID sectionId);

    List<Todo> findByDone(boolean done);
}
