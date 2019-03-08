package com.junhong.liang.todo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import org.mongojack.*;

import java.util.*;

public class TodoServiceMongoImpl implements TodoService {


    private JacksonDBCollection<MongoTodo, String> collection;

    public TodoServiceMongoImpl(DB mongoDb) {
        this.collection = JacksonDBCollection.wrap(mongoDb.getCollection("mongo"), MongoTodo.class, String.class);
    }

    @Override
    public Todo getTodoById(String id) {
        MongoTodo mongoTodo;
        try {
            mongoTodo = collection.findOneById(id);
        } catch (IllegalArgumentException e) {
            // This is necessary because if the user put in a invalid ObjectID, mongo will throw IllegalArgumentException
            throw new TodoException(TodoException.RESOURCE_NOT_FOUND, "Todo with ID " + id + " not found.");
        }

        if(mongoTodo == null) {
            throw new TodoException(TodoException.RESOURCE_NOT_FOUND, "Todo with ID " + id + " not found.");
        }
        return mongoTodo.getTodo();
    }

    @Override
    public List<Todo> getAllTodos() {
        List<Todo> list = new ArrayList<>();
        DBCursor<MongoTodo> cursor = collection.find();
        while(cursor.hasNext()) {
            list.add(cursor.next().getTodo());
        }
        return list;
    }

    @Override
    public Todo addTodo(Todo todo) {
        if (!Todo.isValid(todo,false)) {
            throw new TodoException(TodoException.TODO_MALFORMED, "Todo is malformed.");
        }
        MongoTodo newTodo = new MongoTodo(todo);
        return collection.insert(newTodo).getSavedObject().getTodo();
    }

    @Override
    public Todo updateTodo(String id, Todo todo) {
        if (!Todo.isValid(todo,true)) {
            throw new TodoException(TodoException.TODO_MALFORMED, "Todo is malformed.");
        }
        Todo newTodo = new Todo(id, todo.getName(), todo.getDescription(), todo.getTasks());
        collection.updateById(id, new MongoTodo(newTodo));
        return newTodo;
    }

    @Override
    public boolean deleteTodo(String id) {
        collection.remove(DBQuery.is("_id", id));
        return true;
    }

    @Override
    public void clearAll() {
        collection.remove(DBQuery.empty());
    }

    static class MongoTodo {

        @Id @ObjectId
        private String id;
        @JsonProperty
        private String name;
        @JsonProperty
        private String description;
        @JsonProperty
        private List<Task> tasks;

        MongoTodo() {
            // Default constructor for Jackson deserialization
        }

        MongoTodo(Todo todo) {
            this.id = todo.getId();
            this.name = todo.getName();
            this.description = todo.getDescription();
            this.tasks = todo.getTasks();
        }

        Todo getTodo() {
            return new Todo(this.id, this.name, this.description, this.tasks);
        }

    }


}
