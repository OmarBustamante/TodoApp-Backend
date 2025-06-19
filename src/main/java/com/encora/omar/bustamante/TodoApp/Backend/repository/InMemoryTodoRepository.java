package com.encora.omar.bustamante.TodoApp.Backend.repository;

import com.encora.omar.bustamante.TodoApp.Backend.dto.Todo;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryTodoRepository implements TodoRepository {
    private final List<Todo> todos = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public InMemoryTodoRepository() {
        // Datos de ejemplo (puedes mover esta lógica después)
        todos.add(new Todo(getNextId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar backend", null, false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(getNextId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
    }

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos);
    }

    @Override
    public Optional<Todo> findById(int id) {
        return todos.stream().filter(t -> t.getId() == id).findFirst();
    }

    @Override
    public void save(Todo todo) {
        todos.add(todo);
    }

    @Override
    public void update(Todo todo) {
        todos.replaceAll(t -> t.getId() == todo.getId() ? todo : t);
    }

    @Override
    public void deleteById(int id) {
        todos.removeIf(t -> t.getId() == id);
    }

    @Override
    public int getNextId() {
        return idCounter.incrementAndGet();
    }
}