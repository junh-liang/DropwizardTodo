package com.junhong.liang.todo.api;

import java.util.List;

public interface TodoService {

    /**
     * Get TO-DO item according to id
     *
     * @param id ID of the TO-DO item
     * @return The TO-DO item, null if the it does not exists
     */
    Todo getTodoById(String id);

    /**
     * Get all TO-DO items
     *
     * @return A list of TO-DO items
     */
    List<Todo> getAllTodos();

    /**
     * Add a new TO-DO item
     *
     * @param todo The TO-DO item without ID
     * @return The newly added TO-DO item with newly assigned ID
     */
    Todo addTodo(Todo todo);

    /**
     * Update TO-DO item with given ID
     *
     * @param id ID of the TO-DO item to be updated
     * @param todo the new TO-DO item
     * @return the new TO-DO item
     */
    Todo updateTodo(String id, Todo todo);

    /**
     * Delete TO-DO item with given ID
     *
     * @param id ID of the TO-DO item to be deleted
     * @return true if item is deleted, false if it does not exists
     */
    boolean deleteTodo(String id);

    /**
     *  Clear all TO-DO item in storage
     */
    void clearAll();

}
