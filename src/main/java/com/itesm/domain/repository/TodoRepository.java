package com.itesm.domain.repository;

import com.itesm.domain.models.Todo;

import java.util.List;


public interface TodoRepository {
    Todo save(Todo todo);
    List<Todo> findAllTodos();
}
