package com.encora.omar.bustamante.TodoApp.Backend;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Todo {
    public enum Priority {HIGH, MEDIUM, LOW}


    private int id;
    private String text;
    private LocalDateTime dueDate;
    private boolean done;
    private LocalDateTime doneDate;
    private Priority priority;
    private LocalDateTime creationDate; // Buscar una mejor variabel

    public Todo(int id, String text, LocalDateTime dueDate, boolean done, LocalDateTime doneDate, Priority priority, LocalDateTime creationDate) {
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
        this.done = done;
        this.doneDate = doneDate;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
