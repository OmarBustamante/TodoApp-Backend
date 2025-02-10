package com.encora.omar.bustamante.TodoApp.Backend;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.encora.omar.bustamante.TodoApp.Backend.Todo.Priority.*;

@RestController
public class TodoController {
    private final List<Todo> todos = new ArrayList<>();// crea una nueva lista de todos para la bd
    private int idCounter = 0;
    private int addId(){
        idCounter++;
        return idCounter;
    }
    //Agregamos datos para prbar la bd
    public TodoController(){
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), true, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), true, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), true, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), true, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), true, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), true, null, Todo.Priority.LOW, LocalDateTime.now()));
    }

    @GetMapping("/")
    public String hello(){
        return "Hola Mundo";
    }

    @GetMapping("/todos")
    public List<Todo> getTodos(){
        return todos;
    }

    @GetMapping("/todos/todo/{id}")
    public Todo getTodoById(@PathVariable int id){
        Todo todo = todos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if(todo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return todo;
    }

    @GetMapping("/todos/filter")
    public List<Todo> getFilterList(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Todo.Priority priority,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String sort
    ){
        List<Todo> page = todos;
        page = page.stream().filter(todo -> text == null || todo.getText().toLowerCase().contains(text.toLowerCase()))
                .filter(todo -> priority == null || todo.getPriority() == priority)
                .filter(todo -> done == null || todo.isDone() == done)
                .toList();

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
            case null, default:
                break;
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
    ){
        int end = num * 10;
        int start = end - 10;

        List<Todo> page = getFilterList(text, priority, done, sort);

        if(start >= page.size()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else if (end > page.size()) {
            end = page.size();
        }

        return page.subList(start, end);
    }

    @PostMapping("/todos")
    public Todo create(@RequestBody @Valid Todo todo){
        todo.setId(addId());
        todos.add(todo);
        return todo;
    }

    @PostMapping("todos/{id}/done")
    public void setDone(@PathVariable int id){
        todos.replaceAll(todo -> {
            if(todo.getId() == id){
                todo.setDone(true);
            }
            return todo;
        });
    }

    @PutMapping("/todos/{id}")
    public void update(
            @PathVariable int id,
            @RequestBody(required = false) String text,
            @RequestBody(required = false) LocalDateTime date,
            @RequestBody(required = false) Todo.Priority priority
    ){
        todos.replaceAll(todo -> {
            if(todo.getId() == id){
                if (text != null) todo.setText(text);
                if (date != null) todo.setDueDate(date);
                if (priority != null) todo.setPriority(priority);
            }
            return todo;
        });
    }

    @PutMapping("/todos/{id}/undone")
    public void undone(@PathVariable int id){
        todos.replaceAll(todo -> {
            if(todo.getId() == id){
                todo.setDone(false);
            }
            return todo;
        });
    }
}
