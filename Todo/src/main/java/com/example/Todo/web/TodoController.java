package com.example.Todo.web;

import com.example.Todo.entities.Todo;
import com.example.Todo.services.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        Todo created = service.create(todo);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id,
                                       @RequestBody Todo todo) {
        return ResponseEntity.ok(service.update(id, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
