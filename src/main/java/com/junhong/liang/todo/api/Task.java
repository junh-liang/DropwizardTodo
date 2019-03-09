package com.junhong.liang.todo.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;
import java.util.UUID;


public class Task {

    private String id = UUID.randomUUID().toString();

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    public Task() {
        // Default constructor for Jackson deserialization
    }

    public Task(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.description = desc;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
