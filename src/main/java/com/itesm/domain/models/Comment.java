package com.itesm.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
    private UUID uuid;
    private String content;
    private User author;
    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(UUID uuid, String content, User author, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
