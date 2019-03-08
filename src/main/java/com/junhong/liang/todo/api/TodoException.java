package com.junhong.liang.todo.api;

public class TodoException extends RuntimeException {

    public static final int RESOURCE_NOT_FOUND = 0;
    public static final int TODO_MALFORMED = 1;

    private final int code;

    public TodoException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
