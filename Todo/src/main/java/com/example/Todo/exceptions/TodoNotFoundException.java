package com.example.Todo.exceptions;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(String message) {
        super(message);
    }
}
