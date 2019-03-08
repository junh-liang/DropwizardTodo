package com.junhong.liang.todo.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Todo {

    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    @Valid
    private List<Task> tasks;

    public Todo() {
        // Default constructor, for Jackson deserialization
    }

    // Constructor for initializing a TO-DO item without ID
    public Todo(String name, String desc, List<Task> tasks) {
        this(null, name, desc, tasks);
    }

    public Todo(String id, String name, String desc, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Task> getTasks() {
        return tasks;
    }

}
