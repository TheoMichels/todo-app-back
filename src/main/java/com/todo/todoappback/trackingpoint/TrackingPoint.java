package com.todo.todoappback.trackingpoint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tracking_points")
public class TrackingPoint {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String status;

    @Column(name = "next_step", length = 2000, nullable = false)
    private String nextStep;

    private LocalDate nextDueDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected TrackingPoint() {
    }

    public TrackingPoint(String title, String status, String nextStep, LocalDate nextDueDate) {
        this.title = title;
        this.status = status == null ? "" : status;
        this.nextStep = nextStep == null ? "" : nextStep;
        this.nextDueDate = nextDueDate;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep == null ? "" : nextStep;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
