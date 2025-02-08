package com.encora.omar.bustamante.TodoApp.Backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.encora.omar.bustamante.TodoApp.Backend.Todo.Priority.*;

@RestController
public class TodoController {
    private final List<Todo> todos = new ArrayList<>();

    public TodoController(){
        todos.add(new Todo(1, "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(2, "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(3, "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(4, "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(5, "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(6, "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(7, "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(8, "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(9, "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(10, "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(11, "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(12, "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
    }

    @GetMapping("/")
    public String hello(){
        return "Hola Mundo";
    }

    @GetMapping("/todos")
    public List<Todo> getTodos(){
        return todos;
    }

    @GetMapping("/todos/{id}")
    public Todo getTodoById(@PathVariable int id){
        Todo todo = todos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if(todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return todo;
    }

    @GetMapping("/todos/page/{num}")
    public List<Todo> getPage(@PathVariable int num){
        int end = num * 10;
        int start = end - 10;
        if(start >= todos.size()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else if (end > todos.size()) {
            end = todos.size();
        }
        return todos.subList(start, end);
    }

    @GetMapping("/todos/page/{num}/{sort}")
    public List<Todo> getSortedPage(@PathVariable int num,@PathVariable String sort){
        List<Todo> page = getPage(num);
        switch(sort){
            case "high":
                page = page.stream().sorted(Comparator.comparing(Todo::getPriority)).toList();
                break;
            case "low":
                page = page.stream().sorted(Comparator.comparing(Todo::getPriority).reversed()).toList();
                break;
            case "due":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate)).toList();
                break;
            case "dueHigh":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate).thenComparing(Todo::getPriority)).toList();
                break;
            case "dueLow":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate).thenComparing(Todo::getPriority, Comparator.reverseOrder())).toList();
                break;
            default:
                break;
        }

        return page;
    }
}
