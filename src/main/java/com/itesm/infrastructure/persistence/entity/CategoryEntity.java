package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 80, unique = true)
    private String name;
    @Column(length = 20)
    private String color;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<TodoEntity> todos = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CategoryEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<TodoEntity> getTodos() {
        return todos;
    }

    public void setTodos(Set<TodoEntity> todos) {
        this.todos = todos;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}