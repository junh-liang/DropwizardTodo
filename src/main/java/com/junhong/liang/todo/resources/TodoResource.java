package com.junhong.liang.todo.resources;

import com.codahale.metrics.annotation.Timed;

import com.junhong.liang.todo.api.TodoService;
import com.junhong.liang.todo.api.Todo;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    private TodoService todoService;

    public TodoResource(TodoService todoService) {
        this.todoService = todoService;
    }

    @GET
    @Timed
    public List<Todo> getTodos() {
        return todoService.getAllTodos();
    }

    @POST
    @Timed
    public Todo addTodo(@Valid Todo todo) {
        return todoService.addTodo(todo);
    }

    @GET
    @Path("/{id}")
    @Timed
    public Todo getTodoById(@PathParam("id") String id) {
        return todoService.getTodoById(id);
    }

    @PUT
    @Path("/{id}")
    @Timed
    public Todo updateTodoById(@PathParam("id") String id, @Valid Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @DELETE
    @Path("{id}")
    @Timed
    public void deleteTodoById(@PathParam("id") String id) {
        todoService.deleteTodo(id);
        return;
    }

}
