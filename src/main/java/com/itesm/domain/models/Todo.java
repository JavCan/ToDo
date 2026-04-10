package com.itesm.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Todo {
    private UUID uuid;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;

    public Todo(){
    }
    public Todo(UUID uuid, String title, String description, boolean completed, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                '}';
    }
}
