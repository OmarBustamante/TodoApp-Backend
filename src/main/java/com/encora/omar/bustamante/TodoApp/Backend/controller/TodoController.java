package com.encora.omar.bustamante.TodoApp.Backend.controller;

import com.encora.omar.bustamante.TodoApp.Backend.dto.Todo;
import com.encora.omar.bustamante.TodoApp.Backend.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String hello() {
        return "Hola Mundo";
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todoService.getAll();
    }

    @GetMapping("/todos/todo/{id}")
    public Todo getTodoById(@PathVariable int id) {
        return todoService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/todos/filter")
    public List<Todo> getFilterList(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Todo.Priority priority,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String sort
    ) {
        List<Todo> page = todoService.getAll();
        // Filtros
        if (text != null) {
            page = page.stream()
                    .filter(todo -> todo.getText() != null && todo.getText().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
        if (priority != null) {
            page = page.stream()
                    .filter(todo -> todo.getPriority() == priority)
                    .toList();
        }
        if (done != null) {
            page = page.stream()
                    .filter(todo -> todo.isDone() == done)
                    .toList();
        }

        // Sorting
        if (sort != null) {
            switch (sort) {
                case "high" -> page = page.stream().sorted(Comparator.comparing(Todo::getPriority)).toList();
                case "low" -> page = page.stream().sorted(Comparator.comparing(Todo::getPriority).reversed()).toList();
                case "due" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))).toList();
                case "far" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).toList();
                case "dueHigh" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Todo::getPriority)).toList();
                case "dueLow" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Todo::getPriority, Comparator.reverseOrder())).toList();
                case "farHigh" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed().thenComparing(Todo::getPriority)).toList();
                case "farLow" -> page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed().thenComparing(Todo::getPriority, Comparator.reverseOrder())).toList();
                default -> { /* sin orden */ }
            }
        }
        return page;
    }

    @GetMapping("/todos/page/{num}")
    public List<Todo> getPage(
            @PathVariable int num,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Todo.Priority priority,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String sort
    ) {
        List<Todo> filtered = getFilterList(text, priority, done, sort);
        int end = num * 10;
        int start = end - 10;
        if (start >= filtered.size()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else if (end > filtered.size()) {
            end = filtered.size();
        }
        return filtered.subList(start, end);
    }

    @PostMapping("/todos")
    public Todo create(@RequestBody Todo todo) {
        todo.setId(todoService.getNextId());
        todo.setCreationDate(LocalDateTime.now());
        todoService.save(todo);
        return todo;
    }

    @PostMapping("todos/{id}/done")
    public void setDone(@PathVariable int id) {
        Optional<Todo> opt = todoService.getById(id);
        if (opt.isPresent()) {
            Todo todo = opt.get();
            todo.setDone(true);
            todo.setDoneDate(LocalDateTime.now());
            todoService.update(todo);
        }
    }

    @PutMapping("/todos/{id}")
    public void update(
            @PathVariable int id,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Todo.Priority priority
    ) {
        Optional<Todo> opt = todoService.getById(id);
        if (opt.isPresent()) {
            Todo todo = opt.get();
            if (text != null) todo.setText(text);
            if (date != null) todo.setDueDate(date);
            if (priority != null) todo.setPriority(priority);
            todoService.update(todo);
        }
    }

    @PutMapping("/todos/{id}/undone")
    public void undone(@PathVariable int id) {
        Optional<Todo> opt = todoService.getById(id);
        if (opt.isPresent()) {
            Todo todo = opt.get();
            todo.setDone(false);
            todo.setDoneDate(null);
            todoService.update(todo);
        }
    }

    @DeleteMapping("todos/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<Todo> todo = todoService.getById(id);
        if (todo.isPresent()) {
            todoService.deleteById(id);
            return ResponseEntity.ok("Todo Deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
        }
    }
}