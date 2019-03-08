package com.junhong.liang.todo.api;

import org.mongojack.Id;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TodoServiceInMemoryImpl implements TodoService {


    private Map<String, Todo> todoList;
    private static AtomicLong id;

    public TodoServiceInMemoryImpl() {
        todoList = new Hashtable<>();
        id = new AtomicLong();
    }

    @Override
    public Todo getTodoById(String id) {
        return todoList.get(id);
    }

    @Override
    public List<Todo> getAllTodos() {
        List<Todo> list = new ArrayList<>();
        for (String id : todoList.keySet()) {
            list.add(todoList.get(id));
        }
        return list;
    }

    @Override
    public Todo addTodo(Todo todo) {
        String newId = UUID.randomUUID().toString();
        Todo newTodo = new Todo(newId, todo.getName(), todo.getDescription(), todo.getTasks());
        todoList.put(newId, newTodo);
        return newTodo;
    }

    @Override
    public Todo updateTodo(String id, Todo todo) {
        Todo newTodo = new Todo(id, todo.getName(), todo.getDescription(), todo.getTasks());
        todoList.put(id, newTodo);
        return newTodo;
    }

    @Override
    public boolean deleteTodo(String id) {
        todoList.remove(id);
        return true;
    }

    @Override
    public void clearAll() {
        todoList.clear();
    }

}
