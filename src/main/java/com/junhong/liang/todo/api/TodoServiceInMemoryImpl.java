package com.junhong.liang.todo.api;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TodoServiceInMemoryImpl implements TodoService {


    private Map<String, Todo> todoList;

    public TodoServiceInMemoryImpl() {
        todoList = new Hashtable<>();
    }

    @Override
    public Todo getTodoById(String id) {
        Todo td = todoList.get(id);
        if (td == null) {
            throw new TodoException(TodoException.RESOURCE_NOT_FOUND, "Todo with ID " + id + " not found.");
        }
        return td;
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
        if (!Todo.isValid(todo,false)) {
            throw new TodoException(TodoException.TODO_MALFORMED, "Todo is malformed.");
        }
        String newId = UUID.randomUUID().toString();
        Todo newTodo = new Todo(newId, todo.getName(), todo.getDescription(), todo.getTasks());
        todoList.put(newId, newTodo);
        return newTodo;
    }

    @Override
    public Todo updateTodo(String id, Todo todo) {
        if (!Todo.isValid(todo,true)) {
            throw new TodoException(TodoException.TODO_MALFORMED, "Todo is malformed.");
        }
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
