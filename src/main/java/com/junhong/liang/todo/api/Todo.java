package com.junhong.liang.todo.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    public static boolean isValid(Todo td) {
        // Validate Task in tasks, assume tasks can't have same ID
        Set<String> set = new HashSet<>();
        for(Task t : td.getTasks()) {
            if(set.contains(t.getId())) return false;
            set.add(t.getId());
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(name, todo.name) &&
                Objects.equals(description, todo.description) &&
                Objects.equals(tasks, todo.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tasks);
    }
}
