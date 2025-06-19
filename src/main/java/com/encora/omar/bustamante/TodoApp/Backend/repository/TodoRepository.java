package com.encora.omar.bustamante.TodoApp.Backend.repository;

import com.encora.omar.bustamante.TodoApp.Backend.dto.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    List<Todo> findAll();
    Optional<Todo> findById(int id);
    void save(Todo todo);
    void update(Todo todo);
    void deleteById(int id);
    int getNextId();
}
