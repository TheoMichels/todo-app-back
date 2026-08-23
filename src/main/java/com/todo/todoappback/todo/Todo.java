package com.todo.todoappback.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID sectionId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean done;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    private LocalDate dueDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Todo() {
    }

    public Todo(UUID sectionId, String title, Priority priority, LocalDate dueDate) {
        this.sectionId = sectionId;
        this.title = title;
        this.done = false;
        this.priority = priority != null ? priority : Priority.STANDARD;
        this.dueDate = dueDate;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
