package com.junhong.liang.todo.core;

import com.junhong.liang.todo.api.TodoException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TodoExceptionMapper implements ExceptionMapper<TodoException> {

    public Response toResponse(TodoException e) {

        int statusCode;

        switch (e.getCode()) {
            case TodoException.RESOURCE_NOT_FOUND:
                statusCode = 404;
                break;
            case TodoException.TODO_MALFORMED:
                statusCode = 400;
                break;
            default:
                statusCode = 500;
        }

        return Response.status(statusCode)
                .entity(new ResponseBody(statusCode, e.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();

    }

    class ResponseBody {

        public int code;
        public String message;

        public ResponseBody(){}

        public ResponseBody(int code, String message){
            this.code = code;
            this.message = message;
        }

    }
}
