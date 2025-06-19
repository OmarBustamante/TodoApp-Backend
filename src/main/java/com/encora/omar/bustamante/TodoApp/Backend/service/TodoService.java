package com.encora.omar.bustamante.TodoApp.Backend.service;

import com.encora.omar.bustamante.TodoApp.Backend.dto.Todo;
import com.encora.omar.bustamante.TodoApp.Backend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        // Inicializa datos de ejemplo aquí si lo deseas
    }

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getById(int id) {
        return todoRepository.findById(id);
    }

    public void save(Todo todo) {
        todoRepository.save(todo);
    }

    public void update(Todo todo) {
        todoRepository.update(todo);
    }

    public void deleteById(int id) {
        todoRepository.deleteById(id);
    }

    public int getNextId() {
        return todoRepository.getNextId();
    }

    // Aquí se puede copiar la lógica de filtros, paginación, etc., si deseas mantener la lógica igual.
}