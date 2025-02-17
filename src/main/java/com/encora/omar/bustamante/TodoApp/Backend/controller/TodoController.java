package com.encora.omar.bustamante.TodoApp.Backend.controller;

import com.encora.omar.bustamante.TodoApp.Backend.dto.Todo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
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
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", null, false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar backend", LocalDateTime.of(2025, 2, 10, 10, 0), false, null, Todo.Priority.MEDIUM, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Frontend", LocalDateTime.of(2025, 2, 12, 15, 0), false, null, Todo.Priority.HIGH, LocalDateTime.now()));
        todos.add(new Todo(addId(), "Terminar Main", LocalDateTime.of(2025, 2, 15, 7, 30), false, null, Todo.Priority.LOW, LocalDateTime.now()));
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
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))).toList();
                break;
            case "far":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).toList();
                break;
            case "dueHigh":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Todo::getPriority)).toList();
                break;
            case "dueLow":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Todo::getPriority, Comparator.reverseOrder())).toList();
                break;
            case "farHigh":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed().thenComparing(Todo::getPriority)).toList();
                break;
            case "farLow":
                page = page.stream().sorted(Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed().thenComparing(Todo::getPriority, Comparator.reverseOrder())).toList();
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
        todo.setCreationDate(LocalDateTime.now());
        todos.add(todo);
        return todo;
    }

    @PostMapping("todos/{id}/done")
    public void setDone(@PathVariable int id){
        todos.replaceAll(todo -> {
            if(todo.getId() == id){
                todo.setDone(true);
                todo.setDoneDate(LocalDateTime.now());
            }
            return todo;
        });
    }

    @PutMapping("/todos/{id}")
    public void update(
            @PathVariable int id,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Todo.Priority priority
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
                todo.setDoneDate(null);
            }
            return todo;
        });
    }

    @DeleteMapping("todos/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<Todo> todo = todos.stream().filter(t -> t.getId() == id).findFirst();
        if(todo.isPresent()){
            todos.remove(todo.get());
            return ResponseEntity.ok("Todo Deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
        }
    }
}
