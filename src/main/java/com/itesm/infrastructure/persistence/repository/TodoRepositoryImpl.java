package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import com.itesm.infrastructure.mapper.TodoMapper;
import com.itesm.infrastructure.persistence.entity.TodoEntity;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TodoRepositoryImpl implements TodoRepository, PanacheRepositoryBase<TodoEntity, UUID> {

    @Override
    @Transactional
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        if (todo.getOwnerId() != null) {
            // Evita una consulta SELECT, crea un proxy
            entity.setOwner(getEntityManager().getReference(UserEntity.class, todo.getOwnerId()));
        }
        persist(entity);
        return TodoMapper.toDomain(entity);
    }

    @Override
    public List<Todo> findAllTodos() {
        List<TodoEntity> todoEntities = findAll().stream().toList();
        List<Todo> todos = new ArrayList<>();
        for (TodoEntity todoEntity : todoEntities) {
            todos.add(TodoMapper.toDomain(todoEntity));
        }
        return todos;
    }

    @Override
    public List<Todo> findTodosByUserWithJoinFetch(UUID userId) {
        List<TodoEntity> entities = find(
                "SELECT DISTINCT t FROM TodoEntity t " +
                        "LEFT JOIN FETCH t.owner " +
                        "LEFT JOIN FETCH t.categories " +
                        "LEFT JOIN FETCH t.comments " +
                        "WHERE t.owner.id = ?1",
                userId).list();

        List<Todo> todos = new ArrayList<>();
        for (TodoEntity entity : entities) {
            todos.add(TodoMapper.toDomain(entity));
        }
        return todos;
    }
}