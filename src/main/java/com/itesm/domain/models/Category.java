package com.itesm.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Category {
    private UUID uuid;
    private String name;
    private LocalDateTime createdAt;

    public Category() {
    }

    public Category(UUID uuid, String name, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.name = name;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
