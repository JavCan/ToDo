package com.itesm.application.usecase;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GetTodosByUserUseCase {
    private final TodoRepository todoRepository;

    @Inject
    public GetTodosByUserUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> execute(UUID userId) {
        return todoRepository.findTodosByUserWithJoinFetch(userId);
    }
}
